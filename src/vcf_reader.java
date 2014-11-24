import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ezvcard.*;
import ezvcard.io.*;
import ezvcard.types.*;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import sun.misc.BASE64Encoder;

public class vcf_reader {
    protected BufferedReader reader;
    private File f;
    private String[][] res;
    private ArrayList<String[]> vcf_content = new ArrayList();
    private ArrayList<String> cur_vcf_content = new ArrayList();
    private List<FormattedNameType> names;
    private List<StructuredNameType> addnames;
    private List<EmailType> emails;
    private List<TelephoneType> phones;
    private List<PhotoType> photos;
    private List<String> suffixes, prefixes;


    public vcf_reader() {

    }

    public String[][] getVCF(String[] FilePathes) {
        int vcfsize, i, j = 0;
        for (i = 0; i <= 8; i++) {
            cur_vcf_content.add("");
        }
        for (i = 0; i < FilePathes.length; i++) {       // Начинаем перебор всех файлов
            f = new File(FilePathes[i]);                    // Начинаем чтение текущего файла
            try {
                VCardReader vcr = new VCardReader(f);
                VCard vcard = null;
                while ((vcard = vcr.readNext()) != null) {
                    cur_vcf_content.clear();
                    for (j = 0; j <= 9; j++) {
                        cur_vcf_content.add("");
                    }
                    emails = vcard.getEmails();
                    if (!emails.isEmpty())
                        cur_vcf_content.set(8, emails.get(0).getValue());
                    names = vcard.getFormattedNames();
                    if (!names.isEmpty())
                        cur_vcf_content.set(0, names.get(0).getValue());
                    phones = vcard.getTelephoneNumbers();

                    if (!phones.isEmpty()) {
                        for (int phone_count = 0; phone_count < phones.size(); phone_count++) {
                            if (phones.get(phone_count).getSubTypes().get("TYPE").contains("PREF")) {
                                cur_vcf_content.set(6, phones.get(phone_count).getText());
                            } else {
                                cur_vcf_content.set(7, phones.get(phone_count).getText());
                            }
                        }
                    }
                    addnames = vcard.getStructuredNames();
                    if (!addnames.isEmpty()) {
                        cur_vcf_content.set(1, addnames.get(0).getFamily());
                        cur_vcf_content.set(2, addnames.get(0).getGiven());
                        cur_vcf_content.set(3, addnames.get(0).getAltId());
                        prefixes = addnames.get(0).getPrefixes();
                        if (!prefixes.isEmpty()) {
                            String result = "";
                            for (int prefcount = 0; prefcount < prefixes.size(); prefcount++) {
                                result += prefixes.get(prefcount) + " ";
                            }
                            cur_vcf_content.set(4, result);
                        }
                        suffixes = addnames.get(0).getSuffixes();
                        if (!suffixes.isEmpty()) {
                            String result = "";
                            for (int sufcount = 0; sufcount < suffixes.size(); sufcount++) {
                                result += suffixes.get(sufcount) + " ";
                            }
                            cur_vcf_content.set(5, result);
                        }
                    }
                    photos = vcard.getPhotos();
                    if (photos != null && photos.size() > 0) {
                        byte[] buf = photos.get(0).getData();
                        String encphoto = new sun.misc.BASE64Encoder().encode(buf);
                        cur_vcf_content.set(9, photos.get(0).getType() + ":" + encphoto);
                    }
                    vcf_content.add(cur_vcf_content.toArray(new String[cur_vcf_content.size()]));
                }
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
                WriteReadingError();

            }
        }
        res = new String[vcf_content.size()][8];
        for (j = 0; j < vcf_content.size(); j++) {
            res[j] = vcf_content.get(j);
        }
        return res;
    }

    protected void WriteReadingError() {
        System.out.println("Ошибка чтения файла: " + f.getAbsolutePath() + "!");
        JOptionPane.showMessageDialog(null, "Ошибка чтения файла: " + f.getAbsolutePath() + "!");
    }
}


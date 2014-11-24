import java.io.*;

import ezvcard.*;
import ezvcard.parameters.EmailTypeParameter;
import ezvcard.parameters.ImageTypeParameter;
import ezvcard.parameters.TelephoneTypeParameter;
import ezvcard.types.*;
import javax.swing.*;
import org.apache.commons.lang3.*;


import freemarker.template.utility.StringUtil;
import sun.misc.BASE64Decoder;

public class vcf_writer {
    private BufferedWriter writer;
    String[][] vcfdata;
    private File f;


    public vcf_writer() {
        f = new File("output.vcf");
    }

    public void setVCFContent(String[][] vcf) {
        vcfdata = vcf;
    }

    protected void WriteWritingError() {
        System.out.println("Ошибка записи файла: " + f.getAbsolutePath() + "!");
        JOptionPane.showMessageDialog(null, "Ошибка записи файла: " + f.getAbsolutePath() + "!");
    }

    public void write() {
        try {
            writer = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
            if (!f.canWrite()) {
                WriteWritingError();
            }

            for (int i = 0; i < vcfdata.length; i++) {
                VCard vcard = new VCard();

                StructuredNameType name = new StructuredNameType();
                EmailType email = new EmailType();
                PhotoType photo = new PhotoType();
                ImageTypeParameter type = ImageTypeParameter.PNG;

                if (vcfdata[i][0] != null && vcfdata[i][0] != "")
                    vcard.setFormattedName(vcfdata[i][0]);
                if (vcfdata[i][1] != null && vcfdata[i][1] != "")
                    name.setFamily(vcfdata[i][1]);
                if (vcfdata[i][2] != null && vcfdata[i][2] != "")
                    name.setGiven(vcfdata[i][2]);
                if (vcfdata[i][3] != null && vcfdata[i][3] != "")
                    name.addAdditional(vcfdata[i][3]);
                if (vcfdata[i][4] != null && vcfdata[i][4] != "")
                    name.addPrefix(vcfdata[i][4]);
                if (vcfdata[i][5] != null && vcfdata[i][5] != "")
                    name.addSuffix(vcfdata[i][5]);
                email.setValue(vcfdata[i][8]);
                vcard.setStructuredName(name);

                if  (!StringUtils.isEmpty(vcfdata[i][8])) {
                    vcard.addEmail(vcfdata[i][8], EmailTypeParameter.PREF);
                }

                if (vcfdata[i][6] != null && vcfdata[i][6] != "") {
                    vcard.addTelephoneNumber(vcfdata[i][6], TelephoneTypeParameter.CELL, TelephoneTypeParameter.PREF);
                }

                if (vcfdata[i][7] != null && vcfdata[i][7] != "") {
                    vcard.addTelephoneNumber(vcfdata[i][7], TelephoneTypeParameter.VOICE);
                }

                if (vcfdata[i][9] != null && vcfdata[i][9] != "") {
                    String[] image = vcfdata[i][9].split(":");
                    if (image[0].toLowerCase().equals("jpeg")) {
                        type = ImageTypeParameter.JPEG;
                    }
                    photo.setData(new BASE64Decoder().decodeBuffer(image[1]), type);
                    vcard.addPhoto(photo);
                }
                String text = Ezvcard.write(vcard).version(VCardVersion.V2_1).go();
                writer.write(text);
            }
            writer.flush();
        } catch (IOException e) {
            WriteWritingError();
        }
    }
}

public class vcf_dup_remover {
    private String[][] vcf;
    private String[] empty = {"", "", "", "", "", "", "", "", "",""};

    public vcf_dup_remover() {

    }

    public void setVCF(String[][] vcfdata) {
        vcf = vcfdata;
    }

    public String[][] mergeByEmail() {
        for (int i = 0; i < vcf.length; i++) {
            String currentemail = vcf[i][8];
            for (int j = i + 1; j < vcf.length; j++) {
                boolean hasPhoto =vcf[j][8]!=null && vcf[j][8]!="";
                if (vcf[j][8] != "" && vcf[j][8].equals(currentemail)) {
                    if (hasPhoto)
                    {
                        vcf[i][9]=vcf[j][9];                             // Если есть фотка то кидает ее к элону
                    }
                    vcf[j] = empty;
                }
            }
        }
        return wipe(vcf);
    }

    public String[][] mergeByPhone() {
        for (int i = 0; i < vcf.length; i++) {
            String currentphone = vcf[i][6];
            for (int j = i + 1; j < vcf.length; j++) {
                if (vcf[j][6] != "" && vcf[j][6].equals(currentphone)) {
                    vcf[j] = empty;
                }
            }
        }
        return wipe(vcf);
    }


    private String[][] wipe(String[][] vcfdata) {
        int size = 0, i, finalpos = 0;
        for (i = 0; i < vcfdata.length; i++) {
            if (vcfdata[i] != empty)
                size++;
        }
        String[][] finish = new String[size][9];
        for (i = 0; i < vcfdata.length; i++) {
            if (vcfdata[i] != empty) {
                finish[finalpos] = vcfdata[i];
                finalpos++;
            }
        }
        return finish;
    }
}


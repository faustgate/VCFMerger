import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
import javax.swing.table.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Created with IntelliJ IDEA.
 * User: werwolf
 * Date: 19.03.13
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
public class frm_main {

    private JPanel vcfmerger;
    private JButton btn_clean_by_email;
    private JTable table1;
    private JButton btn_clean_by_phone;
    private JButton btn_write;
    private JScrollPane pane1;
    private String[][] vcfcontent;
    private vcf_reader vcf = new vcf_reader();
    //Массив содержащий заголоки таблицы
    String[] columnNames = {"Full Name", "Surname", "First name", "Middle name",
                            "Prefixes", "Suffixes", "Phone", "Cell Phone", "E-Mail","Photo"};

    //Массив содержащий информацию для таблицы
    Object[][] data;

    DefaultTableModel myTM = new DefaultTableModel(data, columnNames);
    DefaultTableModel myTM2;

    public frm_main() {
        vcfcontent = vcf.getVCF(getAllVCF());
        myTM2 = new DefaultTableModel(vcfcontent, columnNames);
        myTM = myTM2;
        vcfmerger.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                {
                    generateTableContent();
                }
            }
        });
        btn_clean_by_email.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vcf_dup_remover remov = new vcf_dup_remover();
                remov.setVCF(vcfcontent);
                vcfcontent=remov.mergeByEmail();
                DefaultTableModel myTM3 = new DefaultTableModel(vcfcontent, columnNames);
                myTM = myTM3;
                generateTableContent();
            }
        });
        btn_clean_by_phone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vcf_dup_remover remov = new vcf_dup_remover();
                remov.setVCF(vcfcontent);
                vcfcontent=remov.mergeByPhone();
                DefaultTableModel myTM4 = new DefaultTableModel(vcfcontent, columnNames);
                myTM = myTM4;
                generateTableContent();
            }
        });
        btn_write.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vcf_writer wrt = new vcf_writer();
                wrt.setVCFContent(vcfcontent);
                wrt.write();
            }
        });
    }

    private void generateTableContent()
    {
        table1.setModel(myTM);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table1.getColumnModel().getColumn(0).setPreferredWidth(80);
        table1.getColumnModel().getColumn(1).setPreferredWidth(300);
        TableRowSorter<TableModel> sorter = new TableRowSorter(myTM);
        table1.setRowSorter(sorter);
        // table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private String[] getAllVCF() {
        FilenameFilter filt = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("vcf");
            }
        };
        String[] res = new File(".").list(filt);
        return res;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("VCFMerge");
        frame.setContentPane(new frm_main().vcfmerger);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public int getHeaderCount() {
        return columnNames.length;
    }
}

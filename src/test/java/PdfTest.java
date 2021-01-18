import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张亚强
 * @date 2021/1/18 8:46 下午
 */
public class PdfTest {
    public static void main(String[] args) throws Exception {
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        InputStream inReport = null;
        try {
            inReport = new FileInputStream(Test.class.getResource("/").getPath()+ "demo.jrxml");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("name", "张亚强");


        List<HashMap> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("Field1",  "Field1-" + i);
            item.put("Field2",  "Field2-" + i);
            list.add(item);
        }
        JRDataSource dataSource = new JRBeanCollectionDataSource(list);

        jasperReport = JasperCompileManager.compileReport(inReport);
        jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        //导出PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint,"./测试PDF.pdf");
        //预览
//        JasperViewer.setDefaultLookAndFeelDecorated(false);
//        JasperViewer.viewReport(jasperPrint, false);
    }
}

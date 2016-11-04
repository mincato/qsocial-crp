package com.qsocialnow.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CaseReportServiceTest {

    private static String PATH_FILE_JRXML = "src/test/resources/reports/cases.jrxml";

    private static String PATH_FILE_JASPER = "src/test/resources/reports/cases.jasper";

    private static String PATH_FILE_EXCEL = "src/test/resources/reports/cases.xls";

    @Before
    public void init() {
    }

    @Ignore
    @Test
    public void test() throws JRException, FileNotFoundException {
        JasperCompileManager.compileReportToFile(PATH_FILE_JRXML, PATH_FILE_JASPER);
        JasperReport compiledReport = JasperCompileManager.compileReport(PATH_FILE_JRXML);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(JRParameter.REPORT_LOCALE, new Locale("es"));
        params.put("DOMAINS", new HashMap<String, String>());
        params.put("TRIGGERS", new HashMap<String, String>());
        Map<String, String> caseCategories = new HashMap<String, String>();
        caseCategories.put("AVfFSpXRgeHP5kyJ43lW", "case category 1");
        caseCategories.put("AVfFSpXqgeHP5kyJ43lX", "case category 2");
        params.put("CASE_CATEGORIES", caseCategories);

        Map<String, String> subjectCategories = new HashMap<String, String>();
        subjectCategories.put("AVfFSvdHgeHP5kyJ43la", "subject category 1");
        subjectCategories.put("AVfFSvctgeHP5kyJ43lZ", "subject category 2");
        params.put("SUBJECT_CATEGORIES", subjectCategories);
        InputStream stream = new FileInputStream(new File("src/main/resources/mocks/cases.json"));
        JasperPrint print = JasperFillManager.fillReport(compiledReport, params, new JsonDataSource(stream));
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(PATH_FILE_EXCEL));
        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
        xlsReportConfiguration.setOnePagePerSheet(false);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
        xlsReportConfiguration.setDetectCellType(true);
        xlsReportConfiguration.setWhitePageBackground(false);
        exporter.setConfiguration(xlsReportConfiguration);
        exporter.exportReport();
    }
}

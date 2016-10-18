package com.qsocialnow.service;

import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.persistence.CaseRepository;

@Service
public class CaseReportService {

	private static final Logger log = LoggerFactory
			.getLogger(CaseReportService.class);

	@Autowired
	private CaseRepository repository;

	public byte[] getReport() {
		try {
			JasperPrint print = this.buildJasperPrint();
			byte[] data = exportPrintToExcel(print);
			return data;
		} catch (Exception e) {
			log.error("error", e);
		}
		return null;
	}

	private byte[] exportPrintToExcel(JasperPrint print) throws JRException {
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setExporterInput(new SimpleExporterInput(print));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
		SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
		xlsReportConfiguration.setOnePagePerSheet(false);
		xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
		xlsReportConfiguration.setDetectCellType(true);
		xlsReportConfiguration.setWhitePageBackground(false);
		exporter.setConfiguration(xlsReportConfiguration);
		exporter.exportReport();
		byte[] data = out.toByteArray();
		return data;
	}

	private JasperPrint buildJasperPrint() throws JRException {
		InputStream reportStream = this.getClass().getResourceAsStream(
				"/reports/cases.jasper");
		JasperPrint print = JasperFillManager.fillReport(reportStream, null);
		return print;
	}

}

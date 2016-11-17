package com.qsocialnow.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.CasesFilterRequestReport;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.util.UserConstants;
import com.qsocialnow.persistence.CaseCategoryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.DomainRepository;
import com.qsocialnow.persistence.ReportRepository;
import com.qsocialnow.persistence.ResolutionRepository;
import com.qsocialnow.persistence.SegmentRepository;
import com.qsocialnow.persistence.SubjectCategoryRepository;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.TriggerRepository;

@Service
public class CaseReportService {

    private static final Integer REPORT_SIZE = 1000;

    private static final Logger log = LoggerFactory.getLogger(CaseReportService.class);

    @Autowired
    private CaseRepository repository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private CaseCategoryRepository caseCategoryRepository;

    @Autowired
    private SubjectCategoryRepository subjectCategoryRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CaseResultsService caseResultsService;

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    private static final String RESOURCE_BUNDLE_FILE_NAME = "reports";

    private static final String JASPER_CASES_REPORT_PATH = "/reports/cases.jasper";

    private static final String JASPER_CASES_BY_RESOLUTION_REPORT_PATH = "/reports/cases_by_resolution.jasper";

    private static final String JASPER_CASES_BY_STATE_REPORT_PATH = "/reports/cases_by_state.jasper";
    
    private static final String  JASPER_CASES_BY_ADMINISTRATIVE_UNIT_REPORT_PATH = "/reports/cases_by_administrative_unit.jasper";

    public byte[] getReport(CasesFilterRequestReport filterRequestReport) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            Locale locale = new Locale(filterRequestReport.getLanguage());
            ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE_NAME, locale);
            params.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
            TimeZone timeZone = filterRequestReport.getTimeZone() != null ? TimeZone.getTimeZone(filterRequestReport
                    .getTimeZone()) : TimeZone.getDefault();
            formatter.setTimeZone(timeZone);
            params.put("DATE_FORMATTER", formatter);
            addReportMaps(params);

            PageRequest pageRequest = new PageRequest(0, REPORT_SIZE, "openDate");
            pageRequest.setSortOrder(false);

            CasesFilterRequest filterRequest = filterRequestReport.getFilterRequest();
            List<String> teamsToFilter = new ArrayList<String>();
            List<Team> teams = teamRepository.findTeams(filterRequest.getUserName());
            if (teams != null) {
                for (Team team : teams) {
                    List<User> users = team.getUsers();
                    for (User user : users) {
                        if (user.getUsername().equals(filterRequest.getUserName())) {
                            if (user.isCoordinator()) {
                                teamsToFilter.add(team.getId());
                            }
                        }
                    }
                }
            }
            filterRequest.setTeamsToFilter(teamsToFilter);
            JsonArray jsonObject = repository.findAllAsJsonObject(pageRequest, filterRequest);
            InputStream is = new ByteArrayInputStream(jsonObject.toString().getBytes());
            JasperPrint print = this.buildJasperPrint(is, params);
            byte[] data = exportPrintToExcel(print);
            return data;
        } catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

    private void addReportMaps(Map<String, Object> params) {
        Map<String, ReportRepository> reportRepositories = new HashMap<>();
        List<String> keys = Arrays.asList("DOMAINS", "TRIGGERS", "SEGMENTS", "CASE_CATEGORIES", "SUBJECT_CATEGORIES",
                "RESOLUTIONS");
        reportRepositories.put("DOMAINS", domainRepository);
        reportRepositories.put("TRIGGERS", triggerRepository);
        reportRepositories.put("SEGMENTS", segmentRepository);
        reportRepositories.put("CASE_CATEGORIES", caseCategoryRepository);
        reportRepositories.put("SUBJECT_CATEGORIES", subjectCategoryRepository);
        reportRepositories.put("RESOLUTIONS", resolutionRepository);
        keys.stream().parallel().forEach(key -> {
            params.put(key, reportRepositories.get(key).findAllReport());
        });
    }

    private byte[] exportPrintToExcel(JasperPrint print) throws JRException {
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
        xlsReportConfiguration.setOnePagePerSheet(false);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(false);
        xlsReportConfiguration.setDetectCellType(true);
        xlsReportConfiguration.setWhitePageBackground(false);
        exporter.setConfiguration(xlsReportConfiguration);
        exporter.exportReport();
        byte[] data = out.toByteArray();
        return data;
    }

    private JasperPrint buildJasperPrint(InputStream stream, Map<String, Object> params) throws JRException {
        InputStream reportStream = this.getClass().getResourceAsStream(JASPER_CASES_REPORT_PATH);
        JasperPrint print = JasperFillManager.fillReport(reportStream, params, new JsonDataSource(stream));
        return print;
    }

    public byte[] getAggregationsReport(CasesFilterRequestReport filterRequestReport) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            PageResponse<ResultsListView>  pageResponse = caseResultsService.getResults(filterRequestReport.getFilterRequest());
            List<ResultsListView> results = pageResponse.getItems();
            String fileName = getFile(filterRequestReport.getFilterRequest().getFieldToSumarize());
            if (UserConstants.REPORT_BY_RESOLUTION.equals(filterRequestReport.getFilterRequest().getFieldToSumarize())) {
            	configureParamsByResolution(filterRequestReport.getFilterRequest(), params, results);
            } else if (UserConstants.REPORT_BY_STATUS.equals(filterRequestReport.getFilterRequest().getFieldToSumarize())) {
            	configureParamsByState(filterRequestReport.getFilterRequest(), params, results);
            }
            Locale locale = filterRequestReport.getLanguage() != null ? new Locale(filterRequestReport.getLanguage())
                    : Locale.getDefault();
            ResourceBundle resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_FILE_NAME, locale);
            params.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);

            InputStream reportStream = this.getClass().getResourceAsStream(fileName);
            JasperPrint print = JasperFillManager.fillReport(reportStream, params, new JRBeanCollectionDataSource(
                    results));
            byte[] data = exportPrintToExcel(print);
            return data;
        } catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }

	private void configureParamsByResolution(
			CasesFilterRequest filterRequest,
			Map<String, Object> params, List<ResultsListView> results) {
		List<ResultsListView> resultsByUser = new ArrayList<ResultsListView>();
		
		for (ResultsListView result : results) {
			filterRequest.setIdResolution(result.getIdResolution());
			List<ResultsListView> items = repository.sumarizeResolutionByUser(filterRequest);
			for (ResultsListView item : items) {
				item.setIdResolution(result.getIdResolution());
				item.setResolution(result.getResolution());
				resultsByUser.add(item);
			}
		}
		params.put("BY_USER", resultsByUser);
	}
	
	private void configureParamsByState(
			CasesFilterRequest filterRequest,
			Map<String, Object> params, List<ResultsListView> results) {
		List<ResultsListView> resultsByUser = new ArrayList<ResultsListView>();
		
		for (ResultsListView result : results) {
			filterRequest.setStatus(result.getStatus());
			List<ResultsListView> items = repository.sumarizeResolutionByUser(filterRequest);
			for (ResultsListView item: items) {
				item.setStatus(result.getStatus());
				resultsByUser.add(item);
			}
		}
		params.put("BY_USER", resultsByUser);
	
		Map<String, ResultsListView> resultsByPendingResponse = new HashMap<String, ResultsListView>();
		for (ResultsListView result : results) {
			filterRequest.setStatus(result.getStatus());
		    filterRequest.setPendingResponse("true");
	        filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_PENDING);
	        List<ResultsListView> items = repository.sumarizeByPending(filterRequest);
			for (ResultsListView item: items) {
				resultsByPendingResponse.put(result.getStatus(), item);
			}
		}
		params.put("BY_PENDING_RESPONSE", resultsByPendingResponse);
	}
	
	private String getFile(String fieldToSumarize) {
		if (UserConstants.REPORT_BY_RESOLUTION.equals(fieldToSumarize)) {
			return JASPER_CASES_BY_RESOLUTION_REPORT_PATH;
		} else if (UserConstants.REPORT_BY_STATUS.equals(fieldToSumarize)) {
			return JASPER_CASES_BY_STATE_REPORT_PATH;
        }
		return JASPER_CASES_BY_ADMINISTRATIVE_UNIT_REPORT_PATH;
	}
}

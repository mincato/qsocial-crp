package com.qsocialnow.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.persistence.CaseCategoryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.DomainRepository;
import com.qsocialnow.persistence.ReportRepository;
import com.qsocialnow.persistence.ResolutionRepository;
import com.qsocialnow.persistence.SegmentRepository;
import com.qsocialnow.persistence.SubjectCategoryRepository;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.TriggerRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

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

    public byte[] getReport(String domainId, String triggerId, String segmentId, String subject, String title,
            String description, String pendingResponse, String status, String fromOpenDate, String toOpenDate,
            String userName) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, ReportRepository> reportRepositories = new HashMap<>();
            List<String> keys = Arrays.asList("DOMAINS", "TRIGGERS", "SEGMENTS", "CASE_CATEGORIES",
                    "SUBJECT_CATEGORIES", "RESOLUTIONS");
            reportRepositories.put("DOMAINS", domainRepository);
            reportRepositories.put("TRIGGERS", triggerRepository);
            reportRepositories.put("SEGMENTS", segmentRepository);
            reportRepositories.put("CASE_CATEGORIES", caseCategoryRepository);
            reportRepositories.put("SUBJECT_CATEGORIES", subjectCategoryRepository);
            reportRepositories.put("RESOLUTIONS", resolutionRepository);
            long time = System.currentTimeMillis();
            keys.stream().parallel().forEach(key -> {
                params.put(key, reportRepositories.get(key).findAllReport());
            });
            System.out.println("tiempo: " + (System.currentTimeMillis() - time));

            PageRequest pageRequest = new PageRequest(0, REPORT_SIZE, "openDate");
            pageRequest.setSortOrder(false);

            List<String> teamsToFilter = new ArrayList<String>();
            log.info("Retriving cases from :" + userName);
            time = System.currentTimeMillis();
            List<Team> teams = teamRepository.findTeams(userName);
            System.out.println("tiempo: " + (System.currentTimeMillis() - time));
            if (teams != null) {
                for (Team team : teams) {
                    List<User> users = team.getUsers();
                    for (User user : users) {
                        if (user.getUsername().equals(userName)) {
                            if (user.isCoordinator()) {
                                log.info("User:" + userName + " coordinator: " + user.isCoordinator()
                                        + " belongs to team :" + team.getId());
                                teamsToFilter.add(team.getId());
                            }
                        }
                    }
                }
            }
            time = System.currentTimeMillis();
            JsonArray jsonObject = repository.findAllAsJsonObject(pageRequest, domainId, triggerId, segmentId, subject,
                    title, description, pendingResponse, status, fromOpenDate, toOpenDate, teamsToFilter, userName);
            System.out.println("tiempo: " + (System.currentTimeMillis() - time));
            InputStream is = new ByteArrayInputStream(jsonObject.toString().getBytes());
            JasperPrint print = this.buildJasperPrint(is, params);
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

    private JasperPrint buildJasperPrint(InputStream stream, Map<String, Object> params) throws JRException {
        InputStream reportStream = this.getClass().getResourceAsStream("/reports/cases.jasper");
        JasperPrint print = JasperFillManager.fillReport(reportStream, params, new JsonDataSource(stream));
        return print;
    }

    public byte[] getCasesByResolutionReport(String domainId) {
        try {
            PageResponse<ResultsListView> page = caseResultsService.getResults(domainId);
            InputStream reportStream = this.getClass().getResourceAsStream("/reports/cases_by_resolution.jasper");
            JasperPrint print = JasperFillManager.fillReport(reportStream, null,
                    new JRBeanCollectionDataSource(page.getItems()));
            byte[] data = exportPrintToExcel(print);
            return data;
        } catch (Exception e) {
            log.error("error", e);
        }
        return null;
    }
}

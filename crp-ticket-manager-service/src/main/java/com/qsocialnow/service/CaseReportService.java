package com.qsocialnow.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
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
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.TriggerReport;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.persistence.CaseCategoryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.DomainRepository;
import com.qsocialnow.persistence.SubjectCategoryRepository;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.TriggerRepository;

@Service
public class CaseReportService {

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

    public byte[] getReport(String subject, String title, String description, String pendingResponse, String status,
            String fromOpenDate, String toOpenDate, String userName) {
        try {
            Map<String, String> domainsAsMap = domainRepository.findAllReport();
            Map<String, TriggerReport> triggersAsMap = triggerRepository.findAllReport();
            Map<String, String> caseCategoriesAsMap = caseCategoryRepository.findAllReport();
            Map<String, String> subjectCategoriesAsMap = subjectCategoryRepository.findAllReport();

            PageRequest pageRequest = new PageRequest(0, 1000, "openDate");
            pageRequest.setSortOrder(false);

            List<String> teamsToFilter = new ArrayList<String>();
            log.info("Retriving cases from :" + userName);

            List<Team> teams = teamRepository.findTeams(userName);
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
            JsonArray jsonObject = repository.findAllAsJsonObject(pageRequest, subject, title, description,
                    pendingResponse, status, fromOpenDate, toOpenDate, teamsToFilter, userName);
            InputStream is = new ByteArrayInputStream(jsonObject.toString().getBytes());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("DOMAINS", domainsAsMap);
            params.put("TRIGGERS", triggersAsMap);
            params.put("CASE_CATEGORIES", caseCategoriesAsMap);
            params.put("SUBJECT_CATEGORIES", subjectCategoriesAsMap);
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

package com.qsocialnow.viewmodel.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.CaseCategorySetService;
import com.qsocialnow.services.TriggerService;

public class EditCaseCaseCategoriesFactory {

    private TriggerService triggerService;

    private CaseCategorySetService caseCategorySetService;

    public void init(EditCaseView currentCase) {
        if (currentCase.getTriggerCategories() == null) {

            List<CaseCategorySet> allTriggerCategories = triggerService.findCategories(currentCase.getCaseObject()
                    .getDomainId(), currentCase.getCaseObject().getTriggerId());
            currentCase.setAllTriggerCategories(allTriggerCategories);

            List<CaseCategorySet> onlyActiveTriggerCategories = allTriggerCategories.stream()
                    .filter(set -> set.isActive()).collect(Collectors.toList());
            currentCase.setTriggerCategories(onlyActiveTriggerCategories);
        }
        initCaseCategoriesSet(currentCase);
        initCategories(currentCase);

    }

    private void initCaseCategoriesSet(EditCaseView currentCase) {
        List<CaseCategorySet> categoriesSet = new ArrayList<>();
        Set<String> caseCategoriesSet = currentCase.getCaseObject().getCaseCategoriesSet();

        if (CollectionUtils.isNotEmpty(caseCategoriesSet)) {
            List<String> disassociatedIds = new ArrayList<>();

            for (String setId : caseCategoriesSet) {
                List<CaseCategorySet> allTriggerSets = currentCase.getAllTriggerCategories();
                Optional<CaseCategorySet> set = allTriggerSets.stream().filter(s -> s.getId().equals(setId))
                        .findFirst();
                if (set.isPresent()) {
                    categoriesSet.add(set.get());
                } else {
                    disassociatedIds.add(setId);
                }
            }

            if (CollectionUtils.isEmpty(disassociatedIds)) {
                currentCase.setDisassociatedCategories(new ArrayList<>());
            } else {
                List<CaseCategorySet> disassociatedSets = caseCategorySetService.findByIds(disassociatedIds);
                categoriesSet.addAll(disassociatedSets.stream().map(s -> {
                    s.setActive(false);
                    return s;
                }).collect(Collectors.toList()));

                List<CaseCategory> disassociatedCategories = disassociatedSets.stream()
                        .map(CaseCategorySet::getCategories).flatMap(l -> l.stream()).collect(Collectors.toList());
                currentCase.setDisassociatedCategories(disassociatedCategories.stream().map(c -> {
                    c.setActive(false);
                    return c;
                }).collect(Collectors.toList()));
            }
        }

        categoriesSet = deactivateAllCategoriesFromDeactivatedSets(categoriesSet);

        currentCase.setCaseCategoriesSet(categoriesSet);
    }

    private List<CaseCategorySet> deactivateAllCategoriesFromDeactivatedSets(List<CaseCategorySet> sets) {
        return sets.stream().map(s -> {
            if (!s.isActive()) {
                s.getCategories().stream().forEach(c -> c.setActive(false));
            }
            return s;
        }).collect(Collectors.toList());
    }

    private void initCategories(EditCaseView currentCase) {
        List<CaseCategory> categories;
        Set<String> caseCategories = currentCase.getCaseObject().getCaseCategories();
        if (CollectionUtils.isNotEmpty(caseCategories)) {
            Stream<CaseCategory> caseCategoriesStream = currentCase.getAllTriggerCategories().stream()
                    .map(categorySet -> categorySet.getCategories()).flatMap(l -> l.stream());
            categories = caseCategoriesStream.filter(caseCategory -> caseCategories.contains(caseCategory.getId()))
                    .collect(Collectors.toList());
            categories.addAll(currentCase.getDisassociatedCategories());
        } else {
            categories = new ArrayList<>();
        }
        currentCase.setCaseCategories(categories);
    }

    public CaseCategorySetService getCaseCategorySetService() {
        return caseCategorySetService;
    }

    public void setCaseCategorySetService(CaseCategorySetService caseCategorySetService) {
        this.caseCategorySetService = caseCategorySetService;
    }

    public TriggerService getTriggerService() {
        return triggerService;
    }

    public void setTriggerService(TriggerService triggerService) {
        this.triggerService = triggerService;
    }
}

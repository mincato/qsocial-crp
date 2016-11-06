package com.qsocialnow.viewmodel.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.SubjectCategorySetService;
import com.qsocialnow.services.TriggerService;

public class EditCaseSubjectCategoriesFactory {

    private TriggerService triggerService;

    private SubjectCategorySetService categorySetService;

    public void init(EditCaseView currentCase) {
        if (currentCase.getTriggerSubjectCategories() == null) {

            List<SubjectCategorySet> allTriggerCategories = triggerService.findSubjectCategories(currentCase
                    .getCaseObject().getDomainId(), currentCase.getCaseObject().getTriggerId());
            currentCase.setAllTriggerSubjectCategories(allTriggerCategories);

            List<SubjectCategorySet> onlyActiveTriggerCategories = allTriggerCategories.stream()
                    .filter(set -> set.isActive()).collect(Collectors.toList());
            currentCase.setTriggerSubjectCategories(onlyActiveTriggerCategories);
        }
        initCategoriesSet(currentCase);
        initCategories(currentCase);

    }

    private void initCategoriesSet(EditCaseView currentCase) {
        List<SubjectCategorySet> categoriesSet = new ArrayList<>();
        Set<String> setIds = currentCase.getCaseObject().getSubject().getSubjectCategorySet();

        if (CollectionUtils.isNotEmpty(setIds)) {
            List<String> disassociatedIds = new ArrayList<>();

            for (String setId : setIds) {
                List<SubjectCategorySet> allTriggerSets = currentCase.getAllTriggerSubjectCategories();
                Optional<SubjectCategorySet> set = allTriggerSets.stream().filter(s -> s.getId().equals(setId))
                        .findFirst();
                if (set.isPresent()) {
                    categoriesSet.add(set.get());
                } else {
                    disassociatedIds.add(setId);
                }
            }

            if (CollectionUtils.isEmpty(disassociatedIds)) {
                currentCase.setDisassociatedSubjectCategories(new ArrayList<>());
            } else {
                List<SubjectCategorySet> disassociatedSets = categorySetService.findByIds(disassociatedIds);
                categoriesSet.addAll(disassociatedSets.stream().map(s -> {
                    s.setActive(false);
                    return s;
                }).collect(Collectors.toList()));

                List<SubjectCategory> disassociatedCategories = disassociatedSets.stream()
                        .map(SubjectCategorySet::getCategories).flatMap(l -> l.stream()).collect(Collectors.toList());
                currentCase.setDisassociatedSubjectCategories(disassociatedCategories.stream().map(c -> {
                    c.setActive(false);
                    return c;
                }).collect(Collectors.toList()));
            }
        }

        categoriesSet = deactivateAllCategoriesFromDeactivatedSets(categoriesSet);

        currentCase.setSubjectCategoriesSet(categoriesSet);
    }

    private List<SubjectCategorySet> deactivateAllCategoriesFromDeactivatedSets(List<SubjectCategorySet> sets) {
        return sets.stream().map(s -> {
            if (!s.isActive()) {
                s.getCategories().stream().forEach(c -> c.setActive(false));
            }
            return s;
        }).collect(Collectors.toList());
    }

    private void initCategories(EditCaseView currentCase) {
        List<SubjectCategory> categories;
        Set<String> setIds = currentCase.getCaseObject().getSubject().getSubjectCategory();
        if (CollectionUtils.isNotEmpty(setIds)) {
            Stream<SubjectCategory> categoriesStream = currentCase.getAllTriggerSubjectCategories().stream()
                    .map(categorySet -> categorySet.getCategories()).flatMap(l -> l.stream());
            categories = categoriesStream.filter(cat -> setIds.contains(cat.getId())).collect(Collectors.toList());
            categories.addAll(currentCase.getDisassociatedSubjectCategories());
        } else {
            categories = new ArrayList<>();
        }
        currentCase.setSubjectCategories(categories);
    }

    public SubjectCategorySetService getCategorySetService() {
        return categorySetService;
    }

    public void setCategorySetService(SubjectCategorySetService categorySetService) {
        this.categorySetService = categorySetService;
    }

    public TriggerService getTriggerService() {
        return triggerService;
    }

    public void setTriggerService(TriggerService triggerService) {
        this.triggerService = triggerService;
    }

}

package com.qsocialnow.viewmodel.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.qsocialnow.common.model.config.Type;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.TriggerService;

public abstract class EditCaseCategoriesFactory<SET extends Type, CAT extends Type> {

    protected TriggerService triggerService;

    protected abstract void setTriggerSetsInCurrentCase(EditCaseView currentCase, List<SET> sets);

    protected abstract List<SET> getTriggerSetsFromCurrentCase(EditCaseView currentCase);

    protected abstract void setAllTriggerSetsInCurrentCase(EditCaseView currentCase, List<SET> sets);

    protected abstract List<SET> getAllTriggerSetsFromCurrentCase(EditCaseView currentCase);

    protected abstract List<SET> getTriggerSetsFromTriggerService(String domainId, String triggerId);

    protected abstract Set<String> getSetIdsFromCurrentCase(EditCaseView currentCase);

    protected abstract Set<String> getCategoriesIdsFromCurrentCase(EditCaseView currentCase);

    protected abstract List<CAT> getCategoriesFromSet(SET set);

    protected abstract List<SET> getDissasociatedSetsFromService(List<String> ids);

    protected abstract List<CAT> getDissasociatedCategoriesFromCurrentCase(EditCaseView currentCase);

    protected abstract void setDissasociatedCategoriesInCurrentCase(EditCaseView currentCase, List<CAT> categories);

    protected abstract void setSetsInCurrentCase(EditCaseView currentCase, List<SET> sets);

    protected abstract void setCategoriesInCurrentCase(EditCaseView currentCase, List<CAT> categories);

    public void init(EditCaseView currentCase) {
        if (getTriggerSetsFromCurrentCase(currentCase) == null) {

            List<SET> allTriggerCategories = getTriggerSetsFromTriggerService(
                    currentCase.getCaseObject().getDomainId(), currentCase.getCaseObject().getTriggerId());
            setAllTriggerSetsInCurrentCase(currentCase, allTriggerCategories);

            List<SET> onlyActiveTriggerCategories = allTriggerCategories.stream().filter(set -> set.isActive())
                    .collect(Collectors.toList());
            setTriggerSetsInCurrentCase(currentCase, onlyActiveTriggerCategories);
        }
        initCategoriesSet(currentCase);
        initCategories(currentCase);

    }

    private void initCategoriesSet(EditCaseView currentCase) {
        List<SET> categoriesSet = new ArrayList<>();
        Set<String> setIds = getSetIdsFromCurrentCase(currentCase);

        if (CollectionUtils.isNotEmpty(setIds)) {
            List<String> disassociatedIds = new ArrayList<>();

            for (String setId : setIds) {
                List<SET> allTriggerSets = getAllTriggerSetsFromCurrentCase(currentCase);
                Optional<SET> set = allTriggerSets.stream().filter(s -> s.getId().equals(setId)).findFirst();
                if (set.isPresent()) {
                    categoriesSet.add(set.get());
                } else {
                    disassociatedIds.add(setId);
                }
            }

            if (CollectionUtils.isEmpty(disassociatedIds)) {
                setDissasociatedCategoriesInCurrentCase(currentCase, new ArrayList<>());
            } else {
                List<SET> disassociatedSets = getDissasociatedSetsFromService(disassociatedIds);
                categoriesSet.addAll(disassociatedSets.stream().map(s -> {
                    s.setActive(false);
                    return s;
                }).collect(Collectors.toList()));

                List<CAT> disassociatedCategories = disassociatedSets.stream().map(s -> getCategoriesFromSet(s))
                        .flatMap(l -> l.stream()).collect(Collectors.toList());

                disassociatedCategories = disassociatedCategories.stream().map(c -> {
                    c.setActive(false);
                    return c;
                }).collect(Collectors.toList());

                setDissasociatedCategoriesInCurrentCase(currentCase, disassociatedCategories);
            }
        }

        categoriesSet = deactivateAllCategoriesFromDeactivatedSets(categoriesSet);

        setSetsInCurrentCase(currentCase, categoriesSet);
    }

    private List<SET> deactivateAllCategoriesFromDeactivatedSets(List<SET> sets) {
        return sets.stream().map(s -> {
            if (!s.isActive()) {
                getCategoriesFromSet(s).stream().forEach(c -> c.setActive(false));
            }
            return s;
        }).collect(Collectors.toList());
    }

    private void initCategories(EditCaseView currentCase) {
        List<CAT> categories;
        Set<String> catIds = getCategoriesIdsFromCurrentCase(currentCase);
        if (CollectionUtils.isNotEmpty(catIds)) {
            Stream<CAT> categoriesStream = getAllTriggerSetsFromCurrentCase(currentCase).stream()
                    .map(set -> getCategoriesFromSet(set)).flatMap(l -> l.stream());
            categories = categoriesStream.filter(cat -> catIds.contains(cat.getId())).collect(Collectors.toList());
            categories.addAll(getDissasociatedCategoriesFromCurrentCase(currentCase));
        } else {
            categories = new ArrayList<>();
        }
        setCategoriesInCurrentCase(currentCase, categories);
    }

    public TriggerService getTriggerService() {
        return triggerService;
    }

    public void setTriggerService(TriggerService triggerService) {
        this.triggerService = triggerService;
    }

}

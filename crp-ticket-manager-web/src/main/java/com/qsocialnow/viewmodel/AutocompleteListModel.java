package com.qsocialnow.viewmodel;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.SimpleListModel;

import com.qsocialnow.services.AutocompleteService;

public class AutocompleteListModel<T> extends SimpleListModel<T> {

    private static final long serialVersionUID = -6972919176649074592L;

    private static final Logger log = LoggerFactory.getLogger(AutocompleteListModel.class);

    private final WeakHashMap<String, WeakReference<ListModel<T>>> CUSTOM_DATA = new WeakHashMap<String, WeakReference<ListModel<T>>>();

    private ListModel<T> model;

    private AutocompleteService<T> service;

    private Object parameters = null;

    private int maxRows = 15;

    public AutocompleteListModel(AutocompleteService<T> autocompleteService, Object parameters) {
        super(Collections.emptyList());
        this.service = autocompleteService;
        this.parameters = parameters;
    }

    @Override
    public ListModel<T> getSubModel(Object value, int nRows) {
        if (value == null || StringUtils.isEmpty(value.toString())) {
            return new SimpleListModel<T>((List<T>) Collections.emptyList());
        }

        String customKey = (parameters != null ? parameters.toString() + "-" : "") + value.toString();
        if (!CUSTOM_DATA.containsKey(customKey)) {
            List<T> items;
            try {
                items = service.findBy(value.toString(), maxRows, parameters);
            } catch (Exception e) {
                log.error("There was an error trying to fetch items for autocomplete");
                items = Collections.emptyList();
                SimpleListModel<T> lm = new SimpleListModel<T>(items);
                return lm;
            }
            SimpleListModel<T> lm = new SimpleListModel<T>(items);
            model = lm;
            CUSTOM_DATA.put(customKey, new WeakReference<ListModel<T>>(lm));
        }
        return (SimpleListModel<T>) CUSTOM_DATA.get(customKey).get();
    }

    @Override
    public T getElementAt(int j) {
        return this.model.getElementAt(j);
    }
}

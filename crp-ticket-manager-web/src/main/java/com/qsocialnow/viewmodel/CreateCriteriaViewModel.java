package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.FilterType;
import com.qsocialnow.model.ConnotationView;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Media;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateCriteriaViewModel implements Serializable {

    private static final long serialVersionUID = -4119198423406156946L;

    private DetectionCriteria currentCriteria;

    private Set<Media> mediaTypes = new HashSet<>();

    private List<ConnotationView> connotations;

    private FilterView filter;

    private boolean allMedia;

    private boolean saved;

    public DetectionCriteria getCurrentCriteria() {
        return currentCriteria;
    }

    public Set<Media> getMediaTypes() {
        return mediaTypes;
    }

    public boolean isAllMedia() {
        return allMedia;
    }

    public void setAllMedia(boolean allMedia) {
        this.allMedia = allMedia;
    }

    public FilterView getFilter() {
        return filter;
    }

    public List<ConnotationView> getConnotations() {
        return connotations;
    }

    @Command
    public void save() {
        addMediaFilter();
        addDateRangeFilter();
        addConnotationFilter();
        saved = true;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Component comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("detectionCriteria", currentCriteria);
            BindUtils.postGlobalCommand(null, null, "addCriteria", args);
        }
    }

    @Init
    public void init() {
        currentCriteria = new DetectionCriteria();
        filter = new FilterView();
        Media media = new Media();
        media.setName("1");
        mediaTypes.add(media);
        media = new Media();
        media.setName("2");
        mediaTypes.add(media);
        connotations = Arrays.asList(ConnotationView.values());
    }

    @Command
    @NotifyChange("mediaTypes")
    public void selectAllMedia(@BindingParam("checked") boolean isPicked) {
        if (isPicked) {
            for (Media media : mediaTypes) {
                media.setChecked(isPicked);
            }
        }
    }

    @Command
    @NotifyChange("allMedia")
    public void selectMedia() {
        allMedia = false;
    }

    private void addMediaFilter() {
        List<Media> mediasPicked = mediaTypes.stream().filter(Media::isChecked).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mediasPicked)) {
            Filter mediaFilter = new Filter();
            mediaFilter.setType(FilterType.MEDIA);
            String parameters = mediasPicked.stream().map(Media::getName).collect(Collectors.joining("|"));
            mediaFilter.setParameters(parameters);
            currentCriteria.addFilter(mediaFilter);
        }
    }

    private void addDateRangeFilter() {
        if (filter.getStartDateTime() != null || filter.getEndDateTime() != null) {
            Filter dateRangeFilter = new Filter();
            dateRangeFilter.setType(FilterType.PERIOD);
            System.out.println("creando dates");
            StringBuilder parameters = new StringBuilder();
            if (filter.getStartDateTime() != null) {
                parameters.append(formatDate(filter.getStartDateTime()));
            }
            if (filter.getEndDateTime() != null) {
                parameters.append("|");
                parameters.append(formatDate(filter.getEndDateTime()));
            }
            dateRangeFilter.setParameters(parameters.toString());
            currentCriteria.addFilter(dateRangeFilter);
        }

    }

    private void addConnotationFilter() {
        if (filter.getConnotation() != null && !ConnotationView.ALL.equals(filter.getConnotation())) {
            Filter connotationFilter = new Filter();
            connotationFilter.setType(FilterType.CONNOTATION);
            connotationFilter.setParameters(filter.getConnotation().getValue());
            currentCriteria.addFilter(connotationFilter);
        }

    }

    private String formatDate(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm");
        return sdf.format(dateTime);
    }

}

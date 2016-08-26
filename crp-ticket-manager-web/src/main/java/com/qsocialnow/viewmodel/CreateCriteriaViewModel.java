package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
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
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class CreateCriteriaViewModel implements Serializable {

    private static final long serialVersionUID = -4119198423406156946L;

    private DetectionCriteria currentCriteria;

    private List<Media> mediaTypes;

    private List<ConnotationView> connotations;

    private FilterView filter;

    private boolean saved;

    public DetectionCriteria getCurrentCriteria() {
        return currentCriteria;
    }

    public List<Media> getMediaTypes() {
        return mediaTypes;
    }

    public FilterView getFilter() {
        return filter;
    }

    public List<ConnotationView> getConnotations() {
        return connotations;
    }

    public boolean isSaved() {
        return saved;
    }

    @Command
    @NotifyChange("saved")
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
        mediaTypes = Arrays.asList(Media.values());
        mediaTypes.stream().forEach(media -> media.setChecked(false));
        connotations = Arrays.asList(ConnotationView.values());
    }

    @Command
    @NotifyChange("mediaTypes")
    public void selectAllMedia(@BindingParam("checked") boolean isPicked, @BindingParam("media") Media media) {
        if (Media.ALL.equals(media)) {
            for (Media mediaType : mediaTypes) {
                if (!Media.ALL.equals(mediaType)) {
                    mediaType.setChecked(isPicked);
                }
            }
        } else {
            Media.ALL.setChecked(isPicked);
        }
    }

    private void addMediaFilter() {
        List<Media> mediasPicked = mediaTypes.stream().filter(media -> !Media.ALL.equals(media) && media.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mediasPicked)) {
            Filter mediaFilter = new Filter();
            mediaFilter.setType(FilterType.MEDIA);
            String parameters = mediasPicked.stream().map(Media::getValue).collect(Collectors.joining("|"));
            mediaFilter.setParameters(parameters);
            currentCriteria.addFilter(mediaFilter);
        }
    }

    private void addDateRangeFilter() {
        if (filter.getStartDateTime() != null || filter.getEndDateTime() != null) {
            Filter dateRangeFilter = new Filter();
            dateRangeFilter.setType(FilterType.PERIOD);
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

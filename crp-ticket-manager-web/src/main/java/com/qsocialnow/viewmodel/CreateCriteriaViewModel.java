package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.model.ConnotationView;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Media;
import com.qsocialnow.model.MediaView;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class CreateCriteriaViewModel implements Serializable {

    private static final long serialVersionUID = -4119198423406156946L;

    private DetectionCriteria currentCriteria;

    private List<MediaView> mediaTypes;

    private List<ConnotationView> connotations;

    private FilterView filter;

    private boolean saved;

    public DetectionCriteria getCurrentCriteria() {
        return currentCriteria;
    }

    public List<MediaView> getMediaTypes() {
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
        initMedias();
        connotations = Arrays.asList(ConnotationView.values());
    }

    private void initMedias() {
        mediaTypes = new ArrayList<>();
        for (Media media : Media.values()) {
            MediaView mediaView = new MediaView();
            mediaView.setMedia(media);
            mediaView.setChecked(false);
            mediaTypes.add(mediaView);
        }
    }

    @Command
    @NotifyChange("mediaTypes")
    public void selectAllMedia(@BindingParam("checked") boolean isPicked, @BindingParam("media") MediaView mediaView) {
        if (Media.ALL.equals(mediaView.getMedia())) {
            for (MediaView mediaType : mediaTypes) {
                if (!Media.ALL.equals(mediaType.getMedia())) {
                    mediaType.setChecked(isPicked);
                }
            }
        } else if (!isPicked) {
            mediaTypes.stream().filter(media -> Media.ALL.equals(media.getMedia())).findFirst().get()
                    .setChecked(isPicked);
        }
    }

    private void addMediaFilter() {
        List<MediaView> mediasPicked = mediaTypes.stream()
                .filter(media -> !Media.ALL.equals(media.getMedia()) && media.isChecked()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mediasPicked)) {
            Filter mediaFilter = new Filter();
            mediaFilter.setType(FilterType.MEDIA);
            String parameters = mediasPicked.stream().map(media -> media.getMedia().getValue())
                    .collect(Collectors.joining("|"));
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
        SimpleDateFormat sdf = new SimpleDateFormat(FilterConstants.DATE_TIME_FORMAT);
        return sdf.format(dateTime);
    }

}

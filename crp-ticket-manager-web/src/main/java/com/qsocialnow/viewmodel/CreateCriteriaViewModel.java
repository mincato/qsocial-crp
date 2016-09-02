package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.qsocialnow.common.model.config.ConnotationFilter;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.MediaFilter;
import com.qsocialnow.common.model.config.PeriodFilter;
import com.qsocialnow.model.Connotation;
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

    private FilterView filterView;

    private boolean saved;

    public DetectionCriteria getCurrentCriteria() {
        return currentCriteria;
    }

    public List<MediaView> getMediaTypes() {
        return mediaTypes;
    }

    public FilterView getFilter() {
        return filterView;
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
        Filter filter = new Filter();
        addMediaFilter(filter);
        addDateRangeFilter(filter);
        addConnotationFilter(filter);
        currentCriteria.setFilter(filter);
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
        filterView = new FilterView();
        initMedias();
        initConnotations();
    }

    private void initConnotations() {
        connotations = new ArrayList<>();
        for (Connotation connotation : Connotation.values()) {
            ConnotationView connotationView = new ConnotationView();
            connotationView.setConnotation(connotation);
            connotationView.setChecked(false);
            connotations.add(connotationView);
        }
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

    @Command
    @NotifyChange("connotations")
    public void selectAllConnotations(@BindingParam("checked") boolean isPicked,
            @BindingParam("connotation") ConnotationView connotationView) {
        if (Connotation.ALL.equals(connotationView.getConnotation())) {
            for (ConnotationView connotation : connotations) {
                if (!Connotation.ALL.equals(connotation.getConnotation())) {
                    connotation.setChecked(isPicked);
                }
            }
        } else if (!isPicked) {
            connotations.stream().filter(connotation -> Connotation.ALL.equals(connotation.getConnotation()))
                    .findFirst().get().setChecked(isPicked);
        }
    }

    private void addMediaFilter(Filter filter) {
        boolean allSelected = mediaTypes.stream().anyMatch(
                media -> Media.ALL.equals(media.getMedia()) && media.isChecked());
        if (!allSelected) {
            List<MediaView> mediasPicked = mediaTypes.stream()
                    .filter(media -> !Media.ALL.equals(media.getMedia()) && media.isChecked())
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(mediasPicked)) {
                MediaFilter mediaFilter = new MediaFilter();
                Long[] options = mediasPicked.stream().map(media -> media.getMedia().getValue())
                        .toArray(size -> new Long[size]);
                mediaFilter.setOptions(options);
                filter.setMediaFilter(mediaFilter);
            }
        }
    }

    private void addDateRangeFilter(Filter filter) {
        if (filterView.getStartDateTime() != null || filterView.getEndDateTime() != null) {
            PeriodFilter periodFilter = new PeriodFilter();
            if (filterView.getStartDateTime() != null) {
                periodFilter.setStartDateTime(filterView.getStartDateTime().getTime());
            }
            if (filterView.getEndDateTime() != null) {
                periodFilter.setEndDateTime(filterView.getEndDateTime().getTime());
            }
            filter.setPeriodFilter(periodFilter);
        }

    }

    private void addConnotationFilter(Filter filter) {
        boolean allSelected = connotations.stream().anyMatch(
                connotation -> Connotation.ALL.equals(connotation.getConnotation()) && connotation.isChecked());
        if (!allSelected) {
            List<ConnotationView> connotationsPicked = connotations
                    .stream()
                    .filter(connotation -> !Connotation.ALL.equals(connotation.getConnotation())
                            && connotation.isChecked()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(connotationsPicked)) {
                ConnotationFilter connotationFilter = new ConnotationFilter();
                Short[] options = connotationsPicked.stream()
                        .map(connotation -> connotation.getConnotation().getValue()).toArray(size -> new Short[size]);
                connotationFilter.setOptions(options);
                filter.setConnotationFilter(connotationFilter);
            }
        }
    }
}

package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.NotifyCommands;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.bind.annotation.ToServerCommand;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommands({ @NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.selected"),
        @NotifyCommand(value = "zmapbox-modal$refresh", onChange = "_vm_.refreshMap"), })
@ToClientCommand({ "modal$closeEvent", "zmapbox-modal$refresh" })
@ToServerCommand({ "zmapbox-modal$chooseCoordinatesEvent", "modal$openEvent" })
public class ChooseCoordinatesManualCaseViewModel implements Serializable {

    private static final long serialVersionUID = -4008288120380459785L;

    private boolean selected = false;

    private Double latitude;
    private Double longitude;

    private boolean refreshMap = false;

    @Init
    public void init() {
    }

    public boolean isSelected() {
        return selected;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (selected) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("latitude", this.latitude);
            args.put("longitude", this.longitude);
            BindUtils.postGlobalCommand(null, null, "changeCoordinates", args);
        }
    }

    @Command
    @NotifyChange("refreshMap")
    public void open(@ContextParam(ContextType.VIEW) Div comp) {
        refreshMap = true;
    }

    @Command("zmapbox-modal$chooseCoordinatesEvent")
    @NotifyChange("selected")
    public void choose(@BindingParam("lng") Double lng, @BindingParam("lat") Double lat) {
        this.latitude = lat;
        this.longitude = lng;
        this.selected = true;
    }

    public boolean isRefreshMap() {
        return refreshMap;
    }

    public void setRefreshMap(boolean refreshMap) {
        this.refreshMap = refreshMap;
    }

}

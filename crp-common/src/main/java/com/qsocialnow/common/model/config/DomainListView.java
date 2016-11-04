package com.qsocialnow.common.model.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DomainListView implements Serializable {

    private static final long serialVersionUID = 934475061381107999L;

    private String id;

    private String name;

    private Map<Long, Thematic> thematicsById;

    private String thematicNames;

    private List<Long> thematicIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThematicNames() {
        return thematicNames;
    }

    public Collection<Thematic> getThematics() {
        return thematicsById.values();
    }

    public void setThematics(Collection<Thematic> thematics) {
        thematicsById = new HashMap<>();
        for (Thematic thematic : thematics) {
            thematicsById.put(thematic.getId(), thematic);
        }
        Collection<Thematic> names = thematicsById.values();
        thematicNames = names.stream().map(Thematic::getNombre).collect(Collectors.joining(", "));
    }

    public List<Long> getThematicIds() {
        return thematicIds;
    }

    public void setThematicIds(List<Long> thematicIds) {
        this.thematicIds = thematicIds;
    }

}
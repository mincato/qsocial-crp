package com.qsocialnow.viewmodel.team;

import java.util.List;

public class TeamListView<T> {

    private List<T> list;

    private List<T> filteredList;

    private boolean enabledAdd = true;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<T> filteredList) {
        this.filteredList = filteredList;
    }

    public boolean isEnabledAdd() {
        return enabledAdd;
    }

    public void setEnabledAdd(boolean enabledAdd) {
        this.enabledAdd = enabledAdd;
    }

}

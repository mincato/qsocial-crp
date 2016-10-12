package com.qsocialnow.model;

import com.google.common.base.Objects;

public class CategoryGroupBySerieIdInput {

    private Long thematicId;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThematicId() {
        return thematicId;
    }

    public void setThematicId(Long thematicId) {
        this.thematicId = thematicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CategoryGroupBySerieIdInput other = (CategoryGroupBySerieIdInput) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.thematicId, other.thematicId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.thematicId);
    }

}

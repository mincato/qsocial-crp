package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Docs {

    private Long count;

    private Long deleted;

    public Docs() {
    }

    /**
     *
     * @param count
     * @param deleted
     */
    public Docs(Long count, Long deleted) {
        this.count = count;
        this.deleted = deleted;
    }

    /**
     *
     * @return The count
     */
    public Long getCount() {
        return count;
    }

    /**
     *
     * @param count
     *            The count
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     *
     * @return The deleted
     */
    public Long getDeleted() {
        return deleted;
    }

    /**
     *
     * @param deleted
     *            The deleted
     */
    public void setDeleted(Long deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(count).append(deleted).toHashCode();
    }

    @Override
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }
        if ((other instanceof Docs) == false) {
            return false;
        }
        Docs rhs = ((Docs) other);
        return new EqualsBuilder().append(count, rhs.count).append(deleted, rhs.deleted).isEquals();
    }

}

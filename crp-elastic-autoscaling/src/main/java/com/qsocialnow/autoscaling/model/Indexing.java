package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Indexing {

    @SerializedName("index_total")
    @Expose
    private Integer indexTotal;
    @SerializedName("index_time_in_millis")
    @Expose
    private Integer indexTimeInMillis;
    @SerializedName("index_current")
    @Expose
    private Integer indexCurrent;
    @SerializedName("index_failed")
    @Expose
    private Integer indexFailed;
    @SerializedName("delete_total")
    @Expose
    private Integer deleteTotal;
    @SerializedName("delete_time_in_millis")
    @Expose
    private Integer deleteTimeInMillis;
    @SerializedName("delete_current")
    @Expose
    private Integer deleteCurrent;
    @SerializedName("noop_update_total")
    @Expose
    private Integer noopUpdateTotal;
    @SerializedName("is_throttled")
    @Expose
    private Boolean isThrottled;
    @SerializedName("throttle_time_in_millis")
    @Expose
    private Integer throttleTimeInMillis;

    public Indexing(Integer indexTotal, Integer indexTimeInMillis, Integer indexCurrent, Integer indexFailed,
            Integer deleteTotal, Integer deleteTimeInMillis, Integer deleteCurrent, Integer noopUpdateTotal,
            Boolean isThrottled, Integer throttleTimeInMillis) {
        this.indexTotal = indexTotal;
        this.indexTimeInMillis = indexTimeInMillis;
        this.indexCurrent = indexCurrent;
        this.indexFailed = indexFailed;
        this.deleteTotal = deleteTotal;
        this.deleteTimeInMillis = deleteTimeInMillis;
        this.deleteCurrent = deleteCurrent;
        this.noopUpdateTotal = noopUpdateTotal;
        this.isThrottled = isThrottled;
        this.throttleTimeInMillis = throttleTimeInMillis;
    }

    /**
     *
     * @return The indexTotal
     */
    public Integer getIndexTotal() {
        return indexTotal;
    }

    /**
     *
     * @param indexTotal
     *            The index_total
     */
    public void setIndexTotal(Integer indexTotal) {
        this.indexTotal = indexTotal;
    }

    /**
     *
     * @return The indexTimeInMillis
     */
    public Integer getIndexTimeInMillis() {
        return indexTimeInMillis;
    }

    /**
     *
     * @param indexTimeInMillis
     *            The index_time_in_millis
     */
    public void setIndexTimeInMillis(Integer indexTimeInMillis) {
        this.indexTimeInMillis = indexTimeInMillis;
    }

    /**
     *
     * @return The indexCurrent
     */
    public Integer getIndexCurrent() {
        return indexCurrent;
    }

    /**
     *
     * @param indexCurrent
     *            The index_current
     */
    public void setIndexCurrent(Integer indexCurrent) {
        this.indexCurrent = indexCurrent;
    }

    /**
     *
     * @return The indexFailed
     */
    public Integer getIndexFailed() {
        return indexFailed;
    }

    /**
     *
     * @param indexFailed
     *            The index_failed
     */
    public void setIndexFailed(Integer indexFailed) {
        this.indexFailed = indexFailed;
    }

    /**
     *
     * @return The deleteTotal
     */
    public Integer getDeleteTotal() {
        return deleteTotal;
    }

    /**
     *
     * @param deleteTotal
     *            The delete_total
     */
    public void setDeleteTotal(Integer deleteTotal) {
        this.deleteTotal = deleteTotal;
    }

    /**
     *
     * @return The deleteTimeInMillis
     */
    public Integer getDeleteTimeInMillis() {
        return deleteTimeInMillis;
    }

    /**
     *
     * @param deleteTimeInMillis
     *            The delete_time_in_millis
     */
    public void setDeleteTimeInMillis(Integer deleteTimeInMillis) {
        this.deleteTimeInMillis = deleteTimeInMillis;
    }

    /**
     *
     * @return The deleteCurrent
     */
    public Integer getDeleteCurrent() {
        return deleteCurrent;
    }

    /**
     *
     * @param deleteCurrent
     *            The delete_current
     */
    public void setDeleteCurrent(Integer deleteCurrent) {
        this.deleteCurrent = deleteCurrent;
    }

    /**
     *
     * @return The noopUpdateTotal
     */
    public Integer getNoopUpdateTotal() {
        return noopUpdateTotal;
    }

    /**
     *
     * @param noopUpdateTotal
     *            The noop_update_total
     */
    public void setNoopUpdateTotal(Integer noopUpdateTotal) {
        this.noopUpdateTotal = noopUpdateTotal;
    }

    /**
     *
     * @return The isThrottled
     */
    public Boolean getIsThrottled() {
        return isThrottled;
    }

    /**
     *
     * @param isThrottled
     *            The is_throttled
     */
    public void setIsThrottled(Boolean isThrottled) {
        this.isThrottled = isThrottled;
    }

    /**
     *
     * @return The throttleTimeInMillis
     */
    public Integer getThrottleTimeInMillis() {
        return throttleTimeInMillis;
    }

    /**
     *
     * @param throttleTimeInMillis
     *            The throttle_time_in_millis
     */
    public void setThrottleTimeInMillis(Integer throttleTimeInMillis) {
        this.throttleTimeInMillis = throttleTimeInMillis;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(indexTotal).append(indexTimeInMillis).append(indexCurrent)
                .append(indexFailed).append(deleteTotal).append(deleteTimeInMillis).append(deleteCurrent)
                .append(noopUpdateTotal).append(isThrottled).append(throttleTimeInMillis).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Indexing) == false) {
            return false;
        }
        Indexing rhs = ((Indexing) other);
        return new EqualsBuilder().append(indexTotal, rhs.indexTotal).append(indexTimeInMillis, rhs.indexTimeInMillis)
                .append(indexCurrent, rhs.indexCurrent).append(indexFailed, rhs.indexFailed)
                .append(deleteTotal, rhs.deleteTotal).append(deleteTimeInMillis, rhs.deleteTimeInMillis)
                .append(deleteCurrent, rhs.deleteCurrent).append(noopUpdateTotal, rhs.noopUpdateTotal)
                .append(isThrottled, rhs.isThrottled).append(throttleTimeInMillis, rhs.throttleTimeInMillis).isEquals();
    }

}

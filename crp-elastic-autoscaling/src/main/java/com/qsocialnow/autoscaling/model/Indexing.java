package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Indexing {

    @SerializedName("index_total")
    @Expose
    private Long indexTotal;
    @SerializedName("index_time_in_millis")
    @Expose
    private Long indexTimeInMillis;
    @SerializedName("index_current")
    @Expose
    private Long indexCurrent;
    @SerializedName("index_failed")
    @Expose
    private Long indexFailed;
    @SerializedName("delete_total")
    @Expose
    private Long deleteTotal;
    @SerializedName("delete_time_in_millis")
    @Expose
    private Long deleteTimeInMillis;
    @SerializedName("delete_current")
    @Expose
    private Long deleteCurrent;
    @SerializedName("noop_update_total")
    @Expose
    private Long noopUpdateTotal;
    @SerializedName("is_throttled")
    @Expose
    private Boolean isThrottled;
    @SerializedName("throttle_time_in_millis")
    @Expose
    private Long throttleTimeInMillis;

    public Indexing(Long indexTotal, Long indexTimeInMillis, Long indexCurrent, Long indexFailed,
            Long deleteTotal, Long deleteTimeInMillis, Long deleteCurrent, Long noopUpdateTotal,
            Boolean isThrottled, Long throttleTimeInMillis) {
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
    public Long getIndexTotal() {
        return indexTotal;
    }

    /**
     *
     * @param indexTotal
     *            The index_total
     */
    public void setIndexTotal(Long indexTotal) {
        this.indexTotal = indexTotal;
    }

    /**
     *
     * @return The indexTimeInMillis
     */
    public Long getIndexTimeInMillis() {
        return indexTimeInMillis;
    }

    /**
     *
     * @param indexTimeInMillis
     *            The index_time_in_millis
     */
    public void setIndexTimeInMillis(Long indexTimeInMillis) {
        this.indexTimeInMillis = indexTimeInMillis;
    }

    /**
     *
     * @return The indexCurrent
     */
    public Long getIndexCurrent() {
        return indexCurrent;
    }

    /**
     *
     * @param indexCurrent
     *            The index_current
     */
    public void setIndexCurrent(Long indexCurrent) {
        this.indexCurrent = indexCurrent;
    }

    /**
     *
     * @return The indexFailed
     */
    public Long getIndexFailed() {
        return indexFailed;
    }

    /**
     *
     * @param indexFailed
     *            The index_failed
     */
    public void setIndexFailed(Long indexFailed) {
        this.indexFailed = indexFailed;
    }

    /**
     *
     * @return The deleteTotal
     */
    public Long getDeleteTotal() {
        return deleteTotal;
    }

    /**
     *
     * @param deleteTotal
     *            The delete_total
     */
    public void setDeleteTotal(Long deleteTotal) {
        this.deleteTotal = deleteTotal;
    }

    /**
     *
     * @return The deleteTimeInMillis
     */
    public Long getDeleteTimeInMillis() {
        return deleteTimeInMillis;
    }

    /**
     *
     * @param deleteTimeInMillis
     *            The delete_time_in_millis
     */
    public void setDeleteTimeInMillis(Long deleteTimeInMillis) {
        this.deleteTimeInMillis = deleteTimeInMillis;
    }

    /**
     *
     * @return The deleteCurrent
     */
    public Long getDeleteCurrent() {
        return deleteCurrent;
    }

    /**
     *
     * @param deleteCurrent
     *            The delete_current
     */
    public void setDeleteCurrent(Long deleteCurrent) {
        this.deleteCurrent = deleteCurrent;
    }

    /**
     *
     * @return The noopUpdateTotal
     */
    public Long getNoopUpdateTotal() {
        return noopUpdateTotal;
    }

    /**
     *
     * @param noopUpdateTotal
     *            The noop_update_total
     */
    public void setNoopUpdateTotal(Long noopUpdateTotal) {
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
    public Long getThrottleTimeInMillis() {
        return throttleTimeInMillis;
    }

    /**
     *
     * @param throttleTimeInMillis
     *            The throttle_time_in_millis
     */
    public void setThrottleTimeInMillis(Long throttleTimeInMillis) {
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

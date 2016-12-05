package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Total {

    @SerializedName("total_in_bytes")
    @Expose
    private Integer totalInBytes;

    @SerializedName("free_in_bytes")
    @Expose
    private Integer freeInBytes;

    @SerializedName("available_in_bytes")
    @Expose
    private Integer availableInBytes;

    /**
     * No args constructor for use in serialization
     *
     */
    public Total() {
    }

    /**
     *
     * @param usedPercent
     * @param usedInBytes
     * @param totalInBytes
     * @param freeInBytes
     * @param freePercent
     */
    public Total(Integer totalInBytes, Integer freeInBytes, Integer availableInBytes) {
        super();
        this.totalInBytes = totalInBytes;
        this.freeInBytes = freeInBytes;
        this.availableInBytes = availableInBytes;
    }

    public Integer getTotalInBytes() {
        return totalInBytes;
    }

    public void setTotalInBytes(Integer totalInBytes) {
        this.totalInBytes = totalInBytes;
    }

    public Integer getFreeInBytes() {
        return freeInBytes;
    }

    public void setFreeInBytes(Integer freeInBytes) {
        this.freeInBytes = freeInBytes;
    }

    public Integer getAvailableInBytes() {
        return availableInBytes;
    }

    public void setAvailableInBytes(Integer availableInBytes) {
        this.availableInBytes = availableInBytes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalInBytes).append(freeInBytes).append(availableInBytes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Total) == false) {
            return false;
        }
        Total rhs = ((Total) other);
        return new EqualsBuilder().append(totalInBytes, rhs.totalInBytes).append(freeInBytes, rhs.freeInBytes)
                .append(availableInBytes, rhs.availableInBytes).isEquals();
    }
}

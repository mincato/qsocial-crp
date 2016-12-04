package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mem {

    @SerializedName("total_in_bytes")
    @Expose
    private Integer totalInBytes;
    @SerializedName("free_in_bytes")
    @Expose
    private Integer freeInBytes;
    @SerializedName("used_in_bytes")
    @Expose
    private Integer usedInBytes;
    @SerializedName("free_percent")
    @Expose
    private Integer freePercent;
    @SerializedName("used_percent")
    @Expose
    private Integer usedPercent;

    /**
     * No args constructor for use in serialization
     *
     */
    public Mem() {
    }

    /**
     *
     * @param usedPercent
     * @param usedInBytes
     * @param totalInBytes
     * @param freeInBytes
     * @param freePercent
     */
    public Mem(Integer totalInBytes, Integer freeInBytes, Integer usedInBytes, Integer freePercent, Integer usedPercent) {
        super();
        this.totalInBytes = totalInBytes;
        this.freeInBytes = freeInBytes;
        this.usedInBytes = usedInBytes;
        this.freePercent = freePercent;
        this.usedPercent = usedPercent;
    }

    /**
     *
     * @return The totalInBytes
     */
    public Integer getTotalInBytes() {
        return totalInBytes;
    }

    /**
     *
     * @param totalInBytes
     *            The total_in_bytes
     */
    public void setTotalInBytes(Integer totalInBytes) {
        this.totalInBytes = totalInBytes;
    }

    /**
     *
     * @return The freeInBytes
     */
    public Integer getFreeInBytes() {
        return freeInBytes;
    }

    /**
     *
     * @param freeInBytes
     *            The free_in_bytes
     */
    public void setFreeInBytes(Integer freeInBytes) {
        this.freeInBytes = freeInBytes;
    }

    /**
     *
     * @return The usedInBytes
     */
    public Integer getUsedInBytes() {
        return usedInBytes;
    }

    /**
     *
     * @param usedInBytes
     *            The used_in_bytes
     */
    public void setUsedInBytes(Integer usedInBytes) {
        this.usedInBytes = usedInBytes;
    }

    /**
     *
     * @return The freePercent
     */
    public Integer getFreePercent() {
        return freePercent;
    }

    /**
     *
     * @param freePercent
     *            The free_percent
     */
    public void setFreePercent(Integer freePercent) {
        this.freePercent = freePercent;
    }

    /**
     *
     * @return The usedPercent
     */
    public Integer getUsedPercent() {
        return usedPercent;
    }

    /**
     *
     * @param usedPercent
     *            The used_percent
     */
    public void setUsedPercent(Integer usedPercent) {
        this.usedPercent = usedPercent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalInBytes).append(freeInBytes).append(usedInBytes).append(freePercent)
                .append(usedPercent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Mem) == false) {
            return false;
        }
        Mem rhs = ((Mem) other);
        return new EqualsBuilder().append(totalInBytes, rhs.totalInBytes).append(freeInBytes, rhs.freeInBytes)
                .append(usedInBytes, rhs.usedInBytes).append(freePercent, rhs.freePercent)
                .append(usedPercent, rhs.usedPercent).isEquals();
    }

}

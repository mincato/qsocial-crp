package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Os {

    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("cpu_percent")
    @Expose
    private Integer cpuPercent;
    @SerializedName("load_average")
    @Expose
    private Double loadAverage;
    @SerializedName("mem")
    @Expose
    private Mem mem;

    /**
     * No args constructor for use in serialization
     *
     */
    public Os() {
    }

    /**
     *
     * @param timestamp
     * @param cpuPercent
     * @param mem
     * @param swap
     * @param loadAverage
     */
    public Os(Long timestamp, Integer cpuPercent, Double loadAverage, Mem mem) {
        super();
        this.timestamp = timestamp;
        this.cpuPercent = cpuPercent;
        this.loadAverage = loadAverage;
        this.mem = mem;
    }

    /**
     *
     * @return The timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     *            The timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return The cpuPercent
     */
    public Integer getCpuPercent() {
        return cpuPercent;
    }

    /**
     *
     * @param cpuPercent
     *            The cpu_percent
     */
    public void setCpuPercent(Integer cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    /**
     *
     * @return The loadAverage
     */
    public Double getLoadAverage() {
        return loadAverage;
    }

    /**
     *
     * @param loadAverage
     *            The load_average
     */
    public void setLoadAverage(Double loadAverage) {
        this.loadAverage = loadAverage;
    }

    /**
     *
     * @return The mem
     */
    public Mem getMem() {
        return mem;
    }

    /**
     *
     * @param mem
     *            The mem
     */
    public void setMem(Mem mem) {
        this.mem = mem;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(timestamp).append(cpuPercent).append(loadAverage).append(mem).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Os) == false) {
            return false;
        }
        Os rhs = ((Os) other);
        return new EqualsBuilder().append(timestamp, rhs.timestamp).append(cpuPercent, rhs.cpuPercent)
                .append(loadAverage, rhs.loadAverage).append(mem, rhs.mem).isEquals();
    }

}

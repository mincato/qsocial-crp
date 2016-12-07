package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Store {

    @SerializedName("size_in_bytes")
    @Expose
    private Long sizeInBytes;
    @SerializedName("throttle_time_in_millis")
    @Expose
    private Long throttleTimeInMillis;

    /**
     * No args constructor for use in serialization
     *
     */
    public Store() {
    }

    /**
     *
     * @param throttleTimeInMillis
     * @param sizeInBytes
     */
    public Store(Long sizeInBytes, Long throttleTimeInMillis) {
        this.sizeInBytes = sizeInBytes;
        this.throttleTimeInMillis = throttleTimeInMillis;
    }

    /**
     *
     * @return The sizeInBytes
     */
    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    /**
     *
     * @param sizeInBytes
     *            The size_in_bytes
     */
    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
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
        return new HashCodeBuilder().append(sizeInBytes).append(throttleTimeInMillis).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Store) == false) {
            return false;
        }
        Store rhs = ((Store) other);
        return new EqualsBuilder().append(sizeInBytes, rhs.sizeInBytes)
                .append(throttleTimeInMillis, rhs.throttleTimeInMillis).isEquals();
    }

}

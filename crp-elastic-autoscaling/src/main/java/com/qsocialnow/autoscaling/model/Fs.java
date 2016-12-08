package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fs {

    @SerializedName("total")
    @Expose
    private Total total;

    public Fs() {
    }

    public Fs(Total total) {
        super();
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(total).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Fs) == false) {
            return false;
        }
        Fs rhs = ((Fs) other);
        return new EqualsBuilder().append(total, rhs.total).isEquals();
    }

}

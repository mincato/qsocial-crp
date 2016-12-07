package com.qsocialnow.autoscaling.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Node {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("indices")
    @Expose
    private Indices indices;

    @SerializedName("os")
    @Expose
    private Os os;

    @SerializedName("fs")
    @Expose
    private Fs fs;

    /**
     * No args constructor for use in serialization
     *
     */
    public Node() {
    }

    /**
     *
     * @param process
     * @param timestamp
     * @param os
     * @param threadPool
     * @param name
     * @param fs
     * @param indices
     * @param jvm
     * @param script
     * @param breakers
     */
    public Node(Integer timestamp, String name, Indices indices, Os os, Fs fs) {
        super();
        this.name = name;
        this.indices = indices;
        this.os = os;
        this.fs = fs;
    }

    /**
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *            The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return The indices
     */
    public Indices getIndices() {
        return indices;
    }

    /**
     *
     * @param indices
     *            The indices
     */
    public void setIndices(Indices indices) {
        this.indices = indices;
    }

    /**
     *
     * @return The os
     */
    public Os getOs() {
        return os;
    }

    /**
     *
     * @param os
     *            The os
     */
    public void setOs(Os os) {
        this.os = os;
    }

    public Fs getFs() {
        return fs;
    }

    public void setFs(Fs fs) {
        this.fs = fs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(indices).append(os).append(fs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Node) == false) {
            return false;
        }
        Node rhs = ((Node) other);
        return new EqualsBuilder().append(name, rhs.name).append(indices, rhs.indices).append(os, rhs.os)
                .append(fs, rhs.fs).isEquals();
    }

}

package com.qsocialnow.common.model.config;

import java.util.Date;
import java.util.List;

public class Trigger {

    private String id;
    
    private String name;

    private Date init;

    private Date end;

    private String description;

    private List<Segment> segments;

    private List<CustomerGroup> customerGroups;

    private List<Resolution> resolutions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInit() {
        return init;
    }

    public void setInit(Date init) {
        this.init = init;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegment(List<Segment> segments) {
        this.segments = segments;
    }

    public List<CustomerGroup> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(List<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public List<Resolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<Resolution> resolutions) {
        this.resolutions = resolutions;
    }
    
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;
	}

}

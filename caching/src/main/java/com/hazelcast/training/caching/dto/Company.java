package com.hazelcast.training.caching.dto;

import java.io.Serializable;
import java.util.List;

public class Company implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String companyName;
    private List<Associate> associates;

    public Company() {
    }

    public Company(String companyName) {
        this.companyName = companyName;
    }

    public Company(Integer id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Associate> getAssociates() {
        return associates;
    }

    public void setAssociates(List<Associate> associates) {
        this.associates = associates;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("Company{");
        sb.append("id=").append(id);
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", associates=").append(associates);
        sb.append('}');
        return sb.toString();
    }
}

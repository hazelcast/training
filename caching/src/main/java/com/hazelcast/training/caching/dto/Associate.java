package com.hazelcast.training.caching.dto;

import java.io.Serializable;

public class Associate implements Serializable {

    private static final long serialVersionUID = 1L;
    //mandatory attributes:
    private Integer id;
    private String associateName;
    private Integer companyId;
    private Company company;

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("Associate{");
        sb.append("associateName='").append(associateName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Associate() {
    }

    public Associate(String associateName) {
        this.associateName = associateName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssociateName() {
        return associateName;
    }

    public void setAssociateName(String associateName) {
        this.associateName = associateName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

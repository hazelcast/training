package com.hazelcast.training.caching.dto;

import java.io.Serializable;
import java.util.List;

public class Company implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String companyName;
    private List<Associate> associates;
    private byte[] payload;

    private Integer associateNum;
    private boolean active;

    public Company() {
    }

    public Company(String companyName, boolean active, Integer associateNum) {
        this.companyName = companyName;
        this.active = active;
        this.associateNum = associateNum;
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

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("Company{");
        if (id != null) {
            sb.append("id=").append(id);
            sb.append(", ");
        }
        sb.append("companyName='").append(companyName).append('\'');
        if (associates != null) {
            sb.append(", associates=").append(associates);
        }
        sb.append('}');
        return sb.toString();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getAssociateNum() {
        return associateNum;
    }

    public void setAssociateNum(Integer associateNum) {
        this.associateNum = associateNum;
    }
}

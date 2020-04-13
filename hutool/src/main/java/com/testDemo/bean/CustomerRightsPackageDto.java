package com.testDemo.bean;

import java.util.List;

/**
 * @author : zouren
 * @date : 2020/3/12 17:11
 */
public class CustomerRightsPackageDto {
    private String apcPackageEntityId;
    private String name;
    private int packageType;
    private int use_value = 1;
    private int use_type = 0;
    private int use = 0;
    private List<CustomerRightsServerDto> serverDtoList;

    public String getApcPackageEntityId() {
        return apcPackageEntityId;
    }

    public void setApcPackageEntityId(String apcPackageEntityId) {
        this.apcPackageEntityId = apcPackageEntityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public int getUse_value() {
        return use_value;
    }

    public void setUse_value(int use_value) {
        this.use_value = use_value;
    }

    public int getUse_type() {
        return use_type;
    }

    public void setUse_type(int use_type) {
        this.use_type = use_type;
    }

    public int getUse() {
        return use;
    }

    public void setUse(int use) {
        this.use = use;
    }

    public List<CustomerRightsServerDto> getServerDtoList() {
        return serverDtoList;
    }

    public void setServerDtoList(List<CustomerRightsServerDto> serverDtoList) {
        this.serverDtoList = serverDtoList;
    }
}


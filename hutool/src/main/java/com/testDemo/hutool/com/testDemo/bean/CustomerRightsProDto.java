package com.testDemo.hutool.com.testDemo.bean;

import java.util.List;

/**
 * @author : zouren
 * @date : 2020/3/12 17:11
 */
public class CustomerRightsProDto {
    private String apcProductEntityId;
    private String name;
    private List<CustomerRightsPackageDto> packageDtoList;

    public String getApcProductEntityId() {
        return apcProductEntityId;
    }

    public void setApcProductEntityId(String apcProductEntityId) {
        this.apcProductEntityId = apcProductEntityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CustomerRightsPackageDto> getPackageDtoList() {
        return packageDtoList;
    }

    public void setPackageDtoList(List<CustomerRightsPackageDto> packageDtoList) {
        this.packageDtoList = packageDtoList;
    }
}


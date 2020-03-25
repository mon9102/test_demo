package com.testDemo.hutool.com.testDemo.bean;

/**
 * @author : zouren
 * @date : 2020/3/12 17:11
 */

public class CustomerRightsDto {
    private String apcProductEntityId;
    private String apcPackageEntityId;
    private int packageType;
    private int rightsState;
    private CustomerRightsProDto customerRightsProDto;
    public String getApcProductEntityId() {
        return apcProductEntityId;
    }

    public void setApcProductEntityId(String apcProductEntityId) {
        this.apcProductEntityId = apcProductEntityId;
    }

    public String getApcPackageEntityId() {
        return apcPackageEntityId;
    }

    public void setApcPackageEntityId(String apcPackageEntityId) {
        this.apcPackageEntityId = apcPackageEntityId;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public int getRightsState() {
        return rightsState;
    }

    public void setRightsState(int rightsState) {
        this.rightsState = rightsState;
    }

    public CustomerRightsProDto getCustomerRightsProDto() {
        return customerRightsProDto;
    }

    public void setCustomerRightsProDto(CustomerRightsProDto customerRightsProDto) {
        this.customerRightsProDto = customerRightsProDto;
    }
}


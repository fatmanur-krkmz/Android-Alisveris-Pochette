package com.fnmeinss.pochette;

public class Information {
    private String address;
    private String institutionName;
    private String info;

    public Information(){
    }
    public Information(String address, String institutionName, String info) {
        this.address = address;
        this.institutionName = institutionName;
        this.info = info;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

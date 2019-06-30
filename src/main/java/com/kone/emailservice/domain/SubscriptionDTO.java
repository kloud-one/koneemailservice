package com.kone.emailservice.domain;


public class SubscriptionDTO {

    private OrgUserEmailInfo orgUserEmailInfo;
    private byte[] token;


    public OrgUserEmailInfo getOrgUserEmailInfo() {
        return orgUserEmailInfo;
    }

    public void setOrgUserEmailInfo(OrgUserEmailInfo orgUserEmailInfo) {
        this.orgUserEmailInfo = orgUserEmailInfo;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

}

package com.kone.emailservice.domain;


public class UnsubscriptionDTO {

    private OrgUserEmailInfo orgUserEmailInfo;
    private byte[] token;
    private String reason;


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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

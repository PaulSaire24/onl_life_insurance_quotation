package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.rbvd.lib.r304.service.dao.impl.InsQuotInsertBoDAO;

public class QuotationStore {

    private String creationUser;
    private String userAudit;
    private InsQuotInsertBoDAO insQuotInsertBoDAO;

    public String getUserAudit() {
        return userAudit;
    }

    public void setUserAudit(String userAudit) {
        this.userAudit = userAudit;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    @Override
    public String toString() {
        return "QuotationStore{" +
                "creationUser='" + creationUser + '\'' +
                ", userAudit='" + userAudit + '\'' +
                '}';
    }
}

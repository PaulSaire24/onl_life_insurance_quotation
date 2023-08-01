package com.bbva.rbvd.lib.r304.transfer;

import java.util.List;

public class PayloadProperties {
    private String documentTypeId;
    private String documentTypeIdAsText;

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentTypeIdAsText() {
        return documentTypeIdAsText;
    }

    public void setDocumentTypeIdAsText(String documentTypeIdAsText) {
        this.documentTypeIdAsText = documentTypeIdAsText;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PayloadProperties{");
        sb.append("documentTypeId='").append(documentTypeId).append('\'');
        sb.append(", documentTypeIdAsText='").append(documentTypeIdAsText).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package com.bbva.rbvd.lib.r304.pattern.impl;


import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.pattern.Quotation;

public abstract class QuotationDecorator implements Quotation {
    private PreQuotation preQuotation;
    private PostQuotation postQuotation;

    public QuotationDecorator(PreQuotation preQuotation, PostQuotation postQuotation) {
        this.preQuotation = preQuotation;
        this.postQuotation = postQuotation;
    }

    public PreQuotation getPreQuotation() {
        return preQuotation;
    }

    public void setPreQuotation(PreQuotation preQuotation) {
        this.preQuotation = preQuotation;
    }

    public PostQuotation getPostQuotation() {
        return postQuotation;
    }

    public void setPostQuotation(PostQuotation postQuotation) {
        this.postQuotation = postQuotation;
    }
}

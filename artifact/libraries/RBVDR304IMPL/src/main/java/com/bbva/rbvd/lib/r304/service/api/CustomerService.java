package com.bbva.rbvd.lib.r304.service.api;

import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.rbvd.lib.r303.RBVDR303;

public class CustomerService {
    private static RBVDR303 rbvdR303;

    private CustomerService() {
    }

    public static CustomerBO listCustomerService (String input){
        return rbvdR303.executeListCustomerService(input);
    }
}

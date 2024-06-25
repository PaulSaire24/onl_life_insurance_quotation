package com.bbva.rbvd.lib.r303.impl.business;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pisd.dto.insurance.bo.ErrorRimacBO;
import com.bbva.rbvd.lib.r303.impl.util.Constans;
import com.bbva.rbvd.lib.r303.impl.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;

public class ExceptionBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionBusiness.class);

    public void handler(RestClientException exception) {
        if(exception instanceof HttpClientErrorException) {
            LOGGER.info("ExceptionBusiness - HttpClientErrorException");
            this.clientExceptionHandler((HttpClientErrorException) exception);
        } else {
            LOGGER.info("ExceptionBusiness - HttpServerErrorException");
            throw new BusinessException("RBVD00000158", false, "Ocurrio un problema en el servidor");
        }
    }

    private void clientExceptionHandler(HttpClientErrorException exception) {
        String responseBody = exception.getResponseBodyAsString();
        LOGGER.info("ExceptionBusiness - clientExceptionHandler() - Response body: {}", responseBody);
        ErrorRimacBO rimacError = this.getErrorObject(responseBody);
        LOGGER.info("ExceptionBusiness - clientExceptionHandler() - rimacError details: {}", rimacError.getError().getDetails());
        this.throwingBusinessException(rimacError);
    }

    private void throwingBusinessException(ErrorRimacBO rimacError) {
        BusinessException businessException = new BusinessException("RBVD00000119", false, "Error desde Rimac con la cotizacion enviada");
        setBusinessException(rimacError, businessException);
        throw businessException;
    }

    private static void setBusinessException(ErrorRimacBO rimacError, BusinessException businessException) {
        StringBuilder details = new StringBuilder();
        if (!CollectionUtils.isEmpty(rimacError.getError().getDetails())) {
            for (String detail : rimacError.getError().getDetails()) {
                if (details.length() > 0) {
                    details.append(" | ");
                }
                details.append(decodeMessage(detail));
            }
        }
        // E1:ERROR DE DATOS, E2:ERROR FUNCIONAL
        if(details.length() > 0){
            businessException.setAdviceCode(Constans.Error.BBVAE1 + Constans.Error.COD_008411);
            businessException.setMessage(rimacError.getError().getMessage().concat(" : ").concat(details.toString()));
        }else{
            businessException.setAdviceCode(Constans.Error.BBVAE2 + Constans.Error.COD_008411);
            businessException.setMessage(rimacError.getError().getMessage());
        }
    }

    public static String decodeMessage(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private ErrorRimacBO getErrorObject(String responseBody) {
        return JsonHelper.getInstance().deserialization(responseBody, ErrorRimacBO.class);
    }


}

package com.qsocialnow.rest.swagger;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;

@SwaggerDefinition
public class SwaggerConfiguration implements ReaderListener {

    @Override
    public void afterScan(Reader reader, Swagger swagger) {
        swagger.addSecurityDefinition("bearer", new ApiKeyAuthDefinition("Authorization", In.HEADER));
    }

    @Override
    public void beforeScan(Reader arg0, Swagger arg1) {
        // TODO Auto-generated method stub

    }

}

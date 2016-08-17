package com.qsocialnow.rest.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.exception.ValidationException;

@Service
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Autowired
    private RestResponseHandler responseHandler;

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        Map<String, List<String>> data = new HashMap<>();

        for (ConstraintViolation<?> violation : constraintViolations) {
            String field = violation.getPropertyPath().toString();
            List<String> messages = data.get(field);
            if (messages == null) {
                messages = new ArrayList<>();
            }
            messages.add(violation.getMessage());
            data.put(field, messages);
        }

        ValidationException validationException = new ValidationException(data.toString());
        validationException.setData(data);
        return responseHandler.buildErrorResponse(validationException);
    }

}

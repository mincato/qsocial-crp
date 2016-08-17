package com.qsocialnow.rest.response;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.exception.BackEndException;

@Service
public class BackEndErrorFactory {

    private Map<String, BackEndError> exceptionMapping;

    private static final String UNKNOWN_ERROR_CODE = "Unknown";

    private static final Class<?> ROOT_EXCEPTION = Exception.class;

    public BackEndError buildInternalError(Exception e) {

        BackEndError error = create(e);

        if (StringUtils.isBlank(error.getMessage())) {
            error.setMessage(e.getMessage());
        }

        if (e instanceof BackEndException) {
            BackEndException backEndException = (BackEndException) e;
            error.setData(backEndException.getData());
            formatMessage(error, backEndException);
        }

        return error;
    }

    private BackEndError create(Exception e) {

        Class<?> exceptionClass = e.getClass();

        BackEndError error = exceptionMapping.get(exceptionClass.getName());

        while (error == null && !ROOT_EXCEPTION.equals(exceptionClass)) {
            exceptionClass = exceptionClass.getSuperclass();
            error = exceptionMapping.get(exceptionClass.getName());
        }

        if (error == null) {
            error = exceptionMapping.get(UNKNOWN_ERROR_CODE);
        }
        return new BackEndError(error);
    }

    private void formatMessage(BackEndError error, BackEndException backEndException) {
        List<Object> params = backEndException.getParams();
        if (CollectionUtils.isNotEmpty(params)) {
            String pattern = error.getMessage();
            if (StringUtils.isNotBlank(pattern)) {
                String messageFormatted = MessageFormat.format(pattern, params.toArray());
                error.setMessage(messageFormatted);
            }
        }
    }

    public Map<String, BackEndError> getExceptionMapping() {
        return exceptionMapping;
    }

    public void setExceptionMapping(Map<String, BackEndError> exceptionMapping) {
        this.exceptionMapping = exceptionMapping;
    }

}

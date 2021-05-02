package com.solactive.codechallenge.tickpricemonitor.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Ramesh.Yaleru on 9/23/2019
 */
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

    private Date timestamp;
    private String message;//TODO: Safely remove
    private List<ErrorMessage> errors;
    private long statusCode;
    private ErrorMessage error;
    //for command response
    private String correlationId;
    private String consumerId;

    public ExceptionResponse(ExceptionBase exceptionBase, long statusCode) {
        super();
        this.timestamp = new Date();
        this.statusCode = statusCode;
    }


    public ExceptionResponse(ErrorMessage errorMessage, long statusCode) {
        super();
        this.timestamp = new Date();
        this.error = errorMessage;
        this.statusCode = statusCode;
    }

    public ExceptionResponse(String errorMessage, long statusCode) {
        super();
        this.timestamp = new Date();
        this.message = errorMessage;
        this.statusCode = statusCode;
    }

    public ExceptionResponse(List<ErrorMessage> errors, long statusCode) {
        this.timestamp = new Date();
        this.errors = errors;
        this.statusCode = statusCode;
    }

    public ExceptionResponse(List<ErrorMessage> errors, long statusCode, String correlationId, String consumerId) {
        this.timestamp = new Date();
        this.errors = errors;
        this.statusCode = statusCode;
        this.correlationId = correlationId;
        this.consumerId = consumerId;
    }

    public ExceptionResponse(String errorMessage, long statusCode, String correlationId, String consumerId) {
        super();
        this.timestamp = new Date();
        this.message = errorMessage;
        this.statusCode = statusCode;
        this.correlationId = correlationId;
        this.consumerId = consumerId;
    }

    public long getStatusCode() {
        return statusCode;
    }

    //public Date getTimestamp() {        return timestamp;    }

    /*public String getMessage() {
        return message;
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }*/

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionResponse that = (ExceptionResponse) o;
        return statusCode == that.statusCode &&
                Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, statusCode);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrors(List<ErrorMessage> errors) {
        this.errors = errors;
    }
}

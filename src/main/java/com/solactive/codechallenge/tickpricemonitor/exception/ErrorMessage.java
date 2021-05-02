package com.solactive.codechallenge.tickpricemonitor.exception;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    private String errorCode;
    private String messageEn;
    private String messageAr;

    public ErrorMessage(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.messageEn = errorMessage;
    }

    public ErrorMessage(String errorCode, String errorMessageEn, String errorMessageAr) {
        this.errorCode = errorCode;
        this.messageEn = errorMessageEn;
        this.messageAr = errorMessageAr;
    }

    public static ErrorMessage.ErrorMessageBuilder builder() {
        return new ErrorMessage.ErrorMessageBuilder();
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMessageEn() {
        return this.messageEn;
    }

    public String getMessageAr() {
        return this.messageAr;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessageEn(final String messageEn) {
        this.messageEn = messageEn;
    }

    public void setMessageAr(final String messageAr) {
        this.messageAr = messageAr;
    }

    public ErrorMessage() {
    }


    public static class ErrorMessageBuilder {
        private String errorCode;
        private String messageEn;
        private String messageAr;

        ErrorMessageBuilder() {
        }

        public ErrorMessage.ErrorMessageBuilder errorCode(final String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorMessage.ErrorMessageBuilder messageEn(final String messageEn) {
            this.messageEn = messageEn;
            return this;
        }

        public ErrorMessage.ErrorMessageBuilder messageAr(final String messageAr) {
            this.messageAr = messageAr;
            return this;
        }

        public ErrorMessage build() {
            return new ErrorMessage(this.errorCode, this.messageEn, this.messageAr);
        }

        public String toString() {
            return "ErrorMessage.ErrorMessageBuilder(errorCode=" + this.errorCode + ", messageEn=" + this.messageEn + ", messageAr=" + this.messageAr + ")";
        }
    }
}
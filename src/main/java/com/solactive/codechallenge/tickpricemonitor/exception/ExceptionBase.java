package com.solactive.codechallenge.tickpricemonitor.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 *
 */
public class ExceptionBase extends RuntimeException {

    private static final long serialVersionUID = 1596208156393065428L;
    private static final Logger LOGGER = LogManager.getLogger();
    private  ErrorCode errorCode =null;
    private boolean logged;
    private boolean fatal;
    private Serializable[] messageParameters = null;
    private String strErrorCode;


    //Added newly

    private String ruleName;
    private ErrorMessage error;
    private String ruleStatus;


    /**
     * @param errorCode Well defined error code for the error type. Used in logs and for translation
     *                  to user messages.
     * @param message   Technical message. Used for debugging purpose, not intended for end users.
     */
    public ExceptionBase(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


    public ExceptionBase(String ruleName, ErrorMessage errorMessage, String ruleStatus) {

        this.ruleName = ruleName;
        this.error = errorMessage;
        this.ruleStatus = ruleStatus;
    }


    public ExceptionBase(ErrorMessage errorMessage) {
        this.error = errorMessage;
    }




    /**
     * @param errorCode         Well defined error code for the error type. Used in logs and for
     *                          translation to user messages.
     * @param message           Technical message. Used for debugging purpose, not intended for end
     *                          users.
     * @param messageParameters Array of message parameters. Usually used as parameters for generating
     *                          i18n messages from errorCode.
     */
    public ExceptionBase(ErrorCode errorCode, String message, Serializable... messageParameters) {
        super(message);
        this.errorCode = errorCode;
        this.messageParameters = messageParameters;
    }

    /**
     * @param errorCode Well defined error code for the error type. Used in logs and for translation
     *                  to user messages.
     * @param message   Technical message. Used for debugging purpose, not intended for end users.
     * @param cause     Original cause of the exception, use with caution since clients must include
     *                  the class of the cause also (e.g. a vendor specific database exception should
     *                  not be exposed to clients).
     */
    public ExceptionBase(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ExceptionBase(Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * @param errorCode         Well defined error code for the error type. Used in logs and for
     *                          translation to user messages.
     * @param message           Technical message. Used for debugging purpose, not intended for end
     *                          users.
     * @param cause             Original cause of the exception, use with caution since clients must
     *                          include the class of the cause also (e.g. a vendor specific database
     *                          exception should not be exposed to clients).
     * @param messageParameters Array of message parameters. Usually used as parameters for generating
     *                          i18n messages from errorCode.
     */
    public ExceptionBase(ErrorCode errorCode, String message, Throwable cause,
                         Serializable... messageParameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.messageParameters = messageParameters;
    }

    /**
     * This flag indicates that the error is of fatal charcter and needs special attention, such as an
     * alert in surveilance montoring.
     */
    public boolean isFatal() {
        return fatal;
    }

    /**
     * @see #isFatal()
     */
    public void setFatal(boolean fatal) {
        this.fatal = fatal;
    }

    /**
     * This flag indicates that the exception has been logged. Used to avoid duplicate logging of the
     * same error.
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * @see #isLogged()
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * Well defined error code for the error type. Used in logs and for translation to user messages.
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * This method returns an array that contains objects that can be converted to String objects and
     * inserted into the error message.
     *
     * @return Array of parameters to message - null if not defined
     */
    public Serializable[] getMessageParameters() {
        return messageParameters;
    }

    public void setMessageParameters(Serializable[] messageParameters) {
        this.messageParameters = messageParameters;
    }

    /**
     * Returns the error message string of this throwable object. <p>
     * <p/>
     * Overrides the method in {@link Throwable} to allow for parameter insertion. If parameter
     * insertion fails though, the message is returned without parameters inserted into the message
     * String.
     *
     * @return the error message string of this exception
     */
    public String getMessage() {
        if (messageParameters != null) {
            try {
                return MessageFormat.format(super.getMessage(), (Object[]) getMessageParameters());
            } catch (Exception t) {
                LOGGER.catching(t);
                return super.getMessage();
            }
        } else {
            return super.getMessage();
        }
    }

    /**
     * Returns a string representation of this exception instance, on the form:
     * <code>ClassName[errorCode]:Message</code>
     *
     * @return the string representation
     */
    public String toString() {
        return getClass().getName() + "[" + getErrorCode() + "]:" + getMessage();
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getStrErrorCode() {
        return strErrorCode;
    }

    public void setStrErrorCode(String strErrorCode) {
        this.strErrorCode = strErrorCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

    public String getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(String ruleStatus) {
        this.ruleStatus = ruleStatus;
    }
}

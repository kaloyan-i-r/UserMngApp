/*
 * � 2014 Experian Limited.  All rights reserved.
 *
 * Copyright in the whole or any part of this document belongs to Experian
 * Limited and it must be kept strictly confidential by the recipient and
 * must not be used, sold, licensed, transferred, copied or reproduced in
 * whole or in any part in any manner or form or on any media to any person
 * without the prior written consent of Experian Limited.
 */

package org.demo.userapp.error;


/**
 * Error resource object to encapsulate error information for passing back to the client side
 * in a HTTP response.
 * @author pagei
 */
public class ErrorResource {
    /**
     * Status code.
     */
    private int status;
    /**
     * Error message.
     */
    private String message;
    /**
     * Error should be presented in an alert pop-up.
     */
    private boolean alert;
    /**
     * Error should be marked on a field.
     */
    private boolean fieldError;
    /**
     * Error should be redirected to another page.
     */
    private boolean errorRedirect;

    /**
     * Default c'tor.
     */
    public ErrorResource() {
    }

    /**
     * Get the status code.
     * @return the status code int value.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set the status code.
     * @param status the status code int value.
     */
    public void setStatus(final int status) {
        this.status = status;
    }

    /**
     * Get the error message.
     * @return the error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the error message.
     * @param message the error message.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Get whether the error should be presented in an alert.
     * @return boolean flag, true if the error should be an alert.
     */
    public boolean isAlert() {
        return alert;
    }

    /**
     * Set whether the error should be presented in an alert.
     * @param isAlert boolean flag, true if the error should be an alert.
     */
    public void setAlert(final boolean isAlert) {
        this.alert = isAlert;
    }

    /**
     * Get whether the error should be marked on a field.
     * @return boolean flag, true if the error should be marked on a field.
     */
    public boolean isFieldError() {
        return fieldError;
    }

    /**
     * Set whether the error should be marked on a field.
     * @param isFieldError boolean flag, true if the error should be marked on a field.
     */
    public void setFieldError(final boolean isFieldError) {
        this.fieldError = isFieldError;
    }

    /**
     * Get whether the error should cause a page redirect.
     * @return boolean flag, true if the error should cause a page redirect.
     */
    public boolean isErrorRedirect() {
        return errorRedirect;
    }

    /**
     * Set whether the error should cause a page redirect.
     * @param isErrorRedirect boolean flag, true if the error should cause a page redirect.
     */
    public void setErrorRedirect(final boolean isErrorRedirect) {
        this.errorRedirect = isErrorRedirect;
    }
}

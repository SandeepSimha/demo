package com.sancheru.demo;

/**
 * This interface provides a generic callback method signature that is used throughout the SDK
 * to return arguments on success or exception when they occur during asynchronous routines
 *
 * @param <T> Typed value for response object in onResponse callback
 */
public interface AsyncListener<T> {

    /**
     * Async Callback method, to be used when we make any http method call
     *
     * @param response
     */
    void onResponse(final T response);
}
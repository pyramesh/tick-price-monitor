package com.solactive.codechallenge.tickpricemonitor.exception;

import java.io.Serializable;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
public interface ErrorCode<T extends ErrorCode> extends Serializable {
    String code();

    Class<T> type();
}


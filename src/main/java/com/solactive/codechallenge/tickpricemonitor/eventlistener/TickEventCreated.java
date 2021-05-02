package com.solactive.codechallenge.tickpricemonitor.eventlistener;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Getter
@Builder
public class TickEventCreated {

    private String instrumentIdentifier;
    private String status;


}


package com.solactive.codechallenge.tickpricemonitor.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class InstrumentTick implements Serializable {

    private String instrument;
    private List<Tick> ticks;
    long receivedTimestamp;

    public InstrumentTick(String instrument) {
        this.instrument = instrument;
        this.ticks = new ArrayList<>();
    }
}

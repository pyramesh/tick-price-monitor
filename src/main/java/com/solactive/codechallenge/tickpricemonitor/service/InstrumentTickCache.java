package com.solactive.codechallenge.tickpricemonitor.service;

import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.model.InstrumentTick;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
public interface InstrumentTickCache {
    void storeInstrument(TickRequest tickRequest);
    StatisticResponse fetchAllStatisticsByInstrument(String instrumentIdentifier);
    StatisticResponse fetchAllStatistics();
    void clearCache();
}

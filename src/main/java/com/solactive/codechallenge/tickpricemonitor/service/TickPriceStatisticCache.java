package com.solactive.codechallenge.tickpricemonitor.service;

import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
public interface TickPriceStatisticCache {
    void calculateAndStoreStatistics(String instrumentIdentifier);
    StatisticResponse fetchAllStatistics();
    StatisticResponse fetchStatisticsFromCache(String instrument) ;
}

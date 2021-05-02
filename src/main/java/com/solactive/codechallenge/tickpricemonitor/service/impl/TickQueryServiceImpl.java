package com.solactive.codechallenge.tickpricemonitor.service.impl;

import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.service.TickPriceStatisticCache;
import com.solactive.codechallenge.tickpricemonitor.service.TickQueryService;
import org.springframework.stereotype.Service;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Service
public class TickQueryServiceImpl implements TickQueryService {

    private TickPriceStatisticCache tickPriceStatisticCache;


    TickQueryServiceImpl(TickPriceStatisticCache tickPriceStatisticCache){
        this.tickPriceStatisticCache = tickPriceStatisticCache;
    }

    @Override
    public StatisticResponse fetchStatistics() {
        return tickPriceStatisticCache.fetchAllStatistics();
    }

    @Override
    public StatisticResponse fetchStatisticsByInstrument(String instrumentIdentifier) {
        //fetch statistics from cache by instrument
        return tickPriceStatisticCache.fetchStatisticsFromCache(instrumentIdentifier);
        //return tickPriceStatisticCache.fetchStatistics(instrumentIdentifier);
    }
}

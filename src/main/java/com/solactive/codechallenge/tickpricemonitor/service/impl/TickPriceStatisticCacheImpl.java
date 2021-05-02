package com.solactive.codechallenge.tickpricemonitor.service.impl;

import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.service.InstrumentTickCache;
import com.solactive.codechallenge.tickpricemonitor.service.TickPriceStatisticCache;
import com.solactive.codechallenge.tickpricemonitor.validator.ValidTickTimeStampValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Component
@Slf4j
public class TickPriceStatisticCacheImpl implements TickPriceStatisticCache {

    private Map<String, StatisticResponse> statisticsMap = new ConcurrentHashMap<>();

    ValidTickTimeStampValidator validTickTimeStampValidator;
    InstrumentTickCache instrumentTickCache;

    TickPriceStatisticCacheImpl(InstrumentTickCache instrumentTickCache, ValidTickTimeStampValidator validTickTimeStampValidator){
        this.instrumentTickCache = instrumentTickCache;
        this.validTickTimeStampValidator = validTickTimeStampValidator;
    }



    @Override
    public StatisticResponse fetchAllStatistics() {
        return instrumentTickCache.fetchAllStatistics();
    }

    @Override
    public void calculateAndStoreStatistics(String instrumentIdentifier) {
        StatisticResponse statisticsByInstrumentResponse = instrumentTickCache.fetchAllStatisticsByInstrument(instrumentIdentifier);
        //store in statistic Map
        statisticsMap.put(instrumentIdentifier, statisticsByInstrumentResponse);
    }

    @Override
    public StatisticResponse fetchStatisticsFromCache(String instrument) {
        StatisticResponse response= statisticsMap.get(instrument);
        //filter out which are old than than configured sliding time
        if(response != null) {
            return Stream.of(response)
                    .filter(statisticResponse -> validTickTimeStampValidator.isValid(statisticResponse.getCalculatedTimeStamp()))
                    .findFirst().orElse(new StatisticResponse());
        }
        return new StatisticResponse();
    }


}

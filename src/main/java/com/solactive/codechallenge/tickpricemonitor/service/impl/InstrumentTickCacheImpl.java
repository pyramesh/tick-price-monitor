package com.solactive.codechallenge.tickpricemonitor.service.impl;

import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.model.InstrumentTick;
import com.solactive.codechallenge.tickpricemonitor.model.Tick;
import com.solactive.codechallenge.tickpricemonitor.service.InstrumentTickCache;
import com.solactive.codechallenge.tickpricemonitor.validator.ValidTickTimeStampValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Component
@Slf4j
public class InstrumentTickCacheImpl implements InstrumentTickCache {

    private Map<String, InstrumentTick> realTimeInstrumentMap = new ConcurrentHashMap<>();
    ValidTickTimeStampValidator validTickTimeStampValidator;

    InstrumentTickCacheImpl(ValidTickTimeStampValidator validTickTimeStampValidator){
        this.validTickTimeStampValidator= validTickTimeStampValidator;
    }

    @Override
    public void storeInstrument(TickRequest tickRequest) {
        String instrumentIdentifier = tickRequest.getInstrument();
        Tick tick = Tick.builder().price(tickRequest.getPrice())
                .timestamp(tickRequest.getTimeStamp())
                .build();
        realTimeInstrumentMap.putIfAbsent(instrumentIdentifier, new InstrumentTick(instrumentIdentifier));
        realTimeInstrumentMap.get(instrumentIdentifier).getTicks().add(tick);
        realTimeInstrumentMap.get(instrumentIdentifier).setReceivedTimestamp(System.currentTimeMillis());
    }

    @Override
    public StatisticResponse fetchAllStatisticsByInstrument(String instrumentIdentifier) {
        return calculateAndFetchStatistics(fetchTickByInstrument(instrumentIdentifier));
    }
    @Override
    public StatisticResponse fetchAllStatistics() {
        return calculateAndFetchStatistics(fetchAllTicks());
    }

    @Override
    public void clearCache() {
        realTimeInstrumentMap.clear();
    }

    private StatisticResponse calculateAndFetchStatistics(List<Tick> ticks){
        StatisticResponse response = new StatisticResponse();

        final List<Double> instrumentPrices = ticks.stream()
                .filter(Objects::nonNull)
                .filter(t -> validTickTimeStampValidator.isValid(t.getTimestamp()))
                .map(Tick::getPrice)
                .collect(Collectors.toList());
        final Long count = instrumentPrices.stream().count();
        if (count > 0) {
            response = StatisticResponse.builder()
                    .avg(instrumentPrices.parallelStream().mapToDouble(Double::doubleValue).average().getAsDouble())
                    .max(instrumentPrices.stream().max(Double::compareTo).get())
                    .min(instrumentPrices.stream().min(Double::compareTo).get())
                    .count(count)
                    .calculatedTimeStamp(Instant.now().toEpochMilli())
                    .build();
        }
        return response;
    }
    private List<Tick> fetchTickByInstrument(String instrumentIdentifier) {
        List<Tick> ticks = null;
        InstrumentTick instrumentTick= null;
        //if instrument is already exists and instrument modifiedTime is within time intervel
        if(realTimeInstrumentMap.containsKey(instrumentIdentifier) &&
                validTickTimeStampValidator.isValid(realTimeInstrumentMap.get(instrumentIdentifier).getReceivedTimestamp())){
            ticks = realTimeInstrumentMap.get(instrumentIdentifier).getTicks()
                    .parallelStream()
                    .filter(Objects::nonNull)
                    .filter(t -> validTickTimeStampValidator.isValid(t.getTimestamp()))
                    .collect(Collectors.toList());

        }//if doesn't exists
        else{
            ticks = new ArrayList<Tick>();
        }
        return ticks;
    }

    private List<Tick> fetchAllTicks() {
        //check all instrument updated date
        //and then filter
        List<Tick> ticks= realTimeInstrumentMap.values().stream()
                .filter(Objects::nonNull)
                .filter(instrumentTick ->  validTickTimeStampValidator.isValid(instrumentTick.getReceivedTimestamp()))
                .map(InstrumentTick::getTicks)
                .flatMap(List::stream)
                .filter(t -> validTickTimeStampValidator.isValid(t.getTimestamp()))
                .collect(Collectors.toList());

        log.info("ticks ={}", ticks);
        return ticks;
    }
}

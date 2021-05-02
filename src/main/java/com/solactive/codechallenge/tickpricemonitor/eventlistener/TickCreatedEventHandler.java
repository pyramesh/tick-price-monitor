package com.solactive.codechallenge.tickpricemonitor.eventlistener;

import com.solactive.codechallenge.tickpricemonitor.service.TickPriceStatisticCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Component
@Slf4j
public class TickCreatedEventHandler {

    @Autowired
    TickPriceStatisticCache tickPriceStatisticCache;

    @EventListener
    public void listenTickEvent(TickEventCreated tickEventCreated) {
        log.info("TickCreatedEventHandler :: listenTickEvent");
        String  instrumentIdentifier= tickEventCreated.getInstrumentIdentifier();
        tickPriceStatisticCache.calculateAndStoreStatistics(instrumentIdentifier);
    }
}

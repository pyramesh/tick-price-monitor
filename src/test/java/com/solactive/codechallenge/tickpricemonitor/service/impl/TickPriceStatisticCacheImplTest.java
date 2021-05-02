package com.solactive.codechallenge.tickpricemonitor.service.impl;

import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.service.TickPriceStatisticCache;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ramesh.Yaleru on 5/1/2021
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class TickPriceStatisticCacheImplTest {
    @Autowired
    InstrumentTickCacheImpl instrumentTickCache;

    @Autowired
    TickPriceStatisticCache tickPriceStatisticCache;

    @Value("${tick.sliding.time.window}")
    private long slidingTimeInterval;

    @Before
    public void clearInmemCache(){
        instrumentTickCache.clearCache();
    }

    @Test
    public void testFetchStatistics(){
        TickRequest tickRequest= creatTick();
        StatisticResponse response= tickPriceStatisticCache.fetchStatisticsFromCache(tickRequest.getInstrument());
        assertEquals(100.0,response.getMin(),0);
        assertEquals(100.0,response.getAvg(),0);
        assertEquals(100.0,response.getMax(),0);
        assertEquals(1,response.getCount(),0);
    }

    @Test
    public void testCalculateAndStoreStatistics(){
        instrumentTickCache.clearCache();
        TickRequest tickRequest= creatTick();
        tickPriceStatisticCache.calculateAndStoreStatistics(tickRequest.getInstrument());
        StatisticResponse response=tickPriceStatisticCache.fetchStatisticsFromCache(tickRequest.getInstrument());
        assertEquals(100.0,response.getMin(),0);
        assertEquals(100.0,response.getAvg(),0);
        assertEquals(100.0,response.getMax(),0);
        assertEquals(1,response.getCount(),0);
    }

    @Test
    public void testFetchAllStatistics(){
        instrumentTickCache.clearCache();
        TickRequest tickRequest= creatTick();
        TickRequest tickRequest1= creatTick1();
        tickPriceStatisticCache.calculateAndStoreStatistics(tickRequest.getInstrument());
        tickPriceStatisticCache.calculateAndStoreStatistics(tickRequest1.getInstrument());
        StatisticResponse response=tickPriceStatisticCache.fetchAllStatistics();
        assertEquals(100.0,response.getMin(),0);
        assertEquals(150.0,response.getAvg(),0);
        assertEquals(200.0,response.getMax(),0);
        assertEquals(2,response.getCount(),0);
    }
    private TickRequest creatTick() {
        TickRequest tickRequest = new TickRequest();
        tickRequest.setInstrument("IBM.N");
        tickRequest.setPrice(100.0);
        tickRequest.setTimeStamp(Instant.now().toEpochMilli());
        instrumentTickCache.storeInstrument(tickRequest);
        return tickRequest;
    }
    private TickRequest creatTick1() {
        TickRequest tickRequest = new TickRequest();
        tickRequest.setInstrument("Emirates");
        tickRequest.setPrice(200.0);
        tickRequest.setTimeStamp(Instant.now().toEpochMilli());
        instrumentTickCache.storeInstrument(tickRequest);
        return tickRequest;
    }
}

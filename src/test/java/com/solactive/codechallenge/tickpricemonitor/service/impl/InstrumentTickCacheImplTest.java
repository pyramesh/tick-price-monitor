package com.solactive.codechallenge.tickpricemonitor.service.impl;

import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ramesh.Yaleru on 5/1/2021
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class InstrumentTickCacheImplTest {

    @Autowired
    InstrumentTickCacheImpl instrumentTickCache;

    @Value("${tick.sliding.time.window}")
    private long slidingTimeInterval;

    @Before
    public void clearInmemCache(){
        instrumentTickCache.clearCache();
    }

    @Test
    public void testClearCache(){
        TickRequest tickRequest = new TickRequest();
        tickRequest.setInstrument("Emaratech");
        tickRequest.setPrice(1220.0);
        tickRequest.setTimeStamp(Instant.now().toEpochMilli());
        instrumentTickCache.storeInstrument(tickRequest);

        instrumentTickCache.clearCache();
        StatisticResponse response= instrumentTickCache.fetchAllStatisticsByInstrument(tickRequest.getInstrument());
        assertEquals(0.0,response.getMin(),0);
        assertEquals(0.0,response.getAvg(),0);
        assertEquals(0.0,response.getMax(),0);
        assertEquals(0,response.getCount(),0);
    }
    @Test
    public void testStoreInstrument() throws InterruptedException {
        instrumentTickCache.clearCache();
        //TimeUnit.MILLISECONDS.sleep(slidingTimeInterval);
        TickRequest tickRequest = new TickRequest();
        tickRequest.setInstrument("Emaratech");
        tickRequest.setPrice(200.0);
        tickRequest.setTimeStamp(Instant.now().toEpochMilli());
        instrumentTickCache.storeInstrument(tickRequest);

        StatisticResponse response= instrumentTickCache.fetchAllStatisticsByInstrument(tickRequest.getInstrument());
        assertNotNull(response);
        assertEquals(200,response.getMin(),0);
        assertEquals(200.0,response.getAvg(),0);
        assertEquals(200.0,response.getMax(),0);
        assertEquals(1,response.getCount(),0);
    }

    @Test
    public void testFetchAllStatistics(){
        TickRequest tickRequest = new TickRequest();
        tickRequest.setInstrument("Emaratech");
        tickRequest.setPrice(200.0);
        tickRequest.setTimeStamp(Instant.now().toEpochMilli());
        instrumentTickCache.storeInstrument(tickRequest);

        TickRequest tickRequest2 = new TickRequest();
        tickRequest2.setPrice(100.0);
        tickRequest2.setInstrument("Emirates");
        tickRequest2.setTimeStamp(Instant.now().toEpochMilli());
        instrumentTickCache.storeInstrument(tickRequest2);

        StatisticResponse response= instrumentTickCache.fetchAllStatistics();
        assertNotNull(response);
        assertEquals(100.0,response.getMin(),0);
        assertEquals(150.0,response.getAvg(),0);
        assertEquals(200.0,response.getMax(),0);
        assertEquals(2,response.getCount(),0);
    }
}

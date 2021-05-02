package com.solactive.codechallenge.tickpricemonitor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Ramesh.Yaleru on 5/1/2021
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class TickQueryServiceImplTest {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${tick.sliding.time.window}")
    private long slidingTimeInterval;

    @Autowired
    TickQueryServiceImpl tickQueryService;

    @Autowired
    InstrumentTickCacheImpl instrumentTickCache;

    @Test
    public void fetchStatisticsByInstrument() throws Exception {
        TickRequest tickRequest=mockTickRequest();
        creatTick(tickRequest);

        StatisticResponse response=tickQueryService.fetchStatisticsByInstrument(tickRequest.getInstrument());
        assertEquals(300.0,response.getMin(),0);
        assertEquals(300.0,response.getAvg(),0);
        assertEquals(300.0,response.getMax(),0);
        assertEquals(1,response.getCount(),0);
    }

    @Test
    public void testFetchStatistics() throws Exception {
        instrumentTickCache.clearCache();
        TickRequest tickRequest=mockTickRequest();
        creatTick(tickRequest);
        TickRequest tickRequest1=mockTickRequest1();
        creatTick(tickRequest1);

        TimeUnit.MILLISECONDS.sleep(1000);

        StatisticResponse response=tickQueryService.fetchStatistics();
        assertEquals(300.0,response.getMin(),0);
        assertEquals(450.0,response.getAvg(),0);
        assertEquals(600.0,response.getMax(),0);
        assertEquals(2,response.getCount(),0);

    }

   TickRequest mockTickRequest(){
        return TickRequest.builder()
                .instrument("IBM.N")
                .price(300.0)
                .timeStamp(Instant.now().toEpochMilli())
                .build();
    }
    TickRequest mockTickRequest1(){
        return TickRequest.builder()
                .instrument("Emirates NBD")
                .price(600.0)
                .timeStamp(Instant.now().toEpochMilli())
                .build();
    }
    private void creatTick(TickRequest tickRequest) throws Exception {
        MockHttpServletResponse response
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();
    }
}

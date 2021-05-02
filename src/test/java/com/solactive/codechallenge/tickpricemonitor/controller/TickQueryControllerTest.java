package com.solactive.codechallenge.tickpricemonitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.service.impl.InstrumentTickCacheImpl;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Ramesh.Yaleru on 5/1/2021
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TickQueryControllerTest {

    @Autowired
    InstrumentTickCacheImpl instrumentTickCache;

    @Before
    public void clearInmemCache(){
        instrumentTickCache.clearCache();
    }
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    //@Value("${tick.sliding.time.window}")
    private long slidingTimeInterval=61000;

    @Test
    @Order(1)
    public void fetchStatistics_when_No_Instrument_ticks_Found() throws Exception {
        instrumentTickCache.clearCache();
        // when
        MockHttpServletResponse response
                = mockMvc.perform(get("/api/v1/statistics")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMin(),0);
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getAvg(),0);
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMax(),0);
        assertEquals(0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getCount(),0);
    }

    @Test
    @Order(2)
    public void fetchStatisticsByInstrument_After_SpecificInstrument_posted_Success() throws Exception {
        instrumentTickCache.clearCache();
        //create tick
        TickRequest tickRequest = mockInstrumentTick();
        MockHttpServletResponse tickResponse
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        TimeUnit.MILLISECONDS.sleep(1000);
        // when
        MockHttpServletResponse response
                = mockMvc.perform(get("/api/v1/statistics/"+tickRequest.getInstrument())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(100.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMin(),0);
        assertEquals(100.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getAvg(),0);
        assertEquals(100.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMax(),0);
        assertEquals(1,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getCount(),0);
    }
    @Test
    @Order(3)
    public void fetchStatisticsByInstrument_After_multple_SpecificInstrument_posted_Success() throws Exception {
        instrumentTickCache.clearCache();
        //create tick1
        TickRequest tickRequest = mockInstrumentTick();
        MockHttpServletResponse tickResponse
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        //create tick2
        TickRequest tickRequest1 = mockInstrumentTick();
        tickRequest1.setPrice(150.00);
        MockHttpServletResponse tickResponse2
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest1))
        ).andReturn().getResponse();

        TimeUnit.MILLISECONDS.sleep(1000);
        // when
        MockHttpServletResponse response
                = mockMvc.perform(get("/api/v1/statistics/"+tickRequest.getInstrument())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(100.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMin(),0);
        assertEquals(125.00,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getAvg(),0);
        assertEquals(150.00,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMax(),0);
        assertEquals(2,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getCount(),0);
    }


    @Test
    @Order(4)
    public void fetchStatistics_slidingTime_exceeded_Success() throws Exception {
        //create tick
        TickRequest tickRequest = mockInstrumentTick();
        MockHttpServletResponse tickResponse
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        TimeUnit.MILLISECONDS.sleep(slidingTimeInterval);
        // when
        MockHttpServletResponse response
                = mockMvc.perform(get("/api/v1/statistics")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMin(),0);
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getAvg(),0);
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMax(),0);
        assertEquals(0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getCount(),0);
    }

    @Test
    @Order(5)
    public void fetchStatisticsByInstrument_slidingTime_exceeded_Success() throws Exception {
        //create tick
        TickRequest tickRequest = mockInstrumentTick();
        MockHttpServletResponse tickResponse
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        TimeUnit.MILLISECONDS.sleep(slidingTimeInterval);
        // when
        MockHttpServletResponse response
                = mockMvc.perform(get("/api/v1/statistics/"+tickRequest.getInstrument())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMin(),0);
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getAvg(),0);
        assertEquals(0.0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getMax(),0);
        assertEquals(0,objectMapper.readValue(response.getContentAsString(), StatisticResponse.class).getCount(),0);
    }



    private TickRequest mockInstrumentTick() {
        return TickRequest.builder()
                .instrument("IBM.N")
                .price(100.00)
                .timeStamp(Instant.now().toEpochMilli())
                .build();

    }
}

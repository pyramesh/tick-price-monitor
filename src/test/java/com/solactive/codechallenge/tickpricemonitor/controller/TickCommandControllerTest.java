package com.solactive.codechallenge.tickpricemonitor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Ramesh.Yaleru on 5/1/2021
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class TickCommandControllerTest {
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createTick_within_slidingTime_60Secs_Success() throws Exception {
        // when
        TickRequest tickRequest = mockInstrumentTick();

        MockHttpServletResponse response
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void createTick_within_old_slidingTime_60Secs_Error() throws Exception {
        // when
        TickRequest tickRequest = mockInstrumentTick();
        //old tickTimestamp
        tickRequest.setTimeStamp(1619839958206l);

        MockHttpServletResponse response
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void createTick_without_instrument_Error() throws Exception {
        // when
        TickRequest tickRequest = mockInstrumentTick();
        tickRequest.setInstrument(null);

        MockHttpServletResponse response
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createTick_without_price_Error() throws Exception {
        // when
        TickRequest tickRequest = mockInstrumentTick();
        tickRequest.setPrice(null);

        MockHttpServletResponse response
                = mockMvc.perform(post("/api/v1/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tickRequest))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    private TickRequest mockInstrumentTick() {
        return TickRequest.builder()
                .instrument("IBM.N")
                .price(100.50)
                .timeStamp(Instant.now().toEpochMilli())
                .build();

    }
}

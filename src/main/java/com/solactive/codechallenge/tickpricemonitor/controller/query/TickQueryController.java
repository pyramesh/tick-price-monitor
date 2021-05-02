package com.solactive.codechallenge.tickpricemonitor.controller.query;

import com.solactive.codechallenge.tickpricemonitor.config.ApiError;
import com.solactive.codechallenge.tickpricemonitor.config.ApiErrors;
import com.solactive.codechallenge.tickpricemonitor.dto.StatisticResponse;
import com.solactive.codechallenge.tickpricemonitor.service.TickQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@RestController
@RequestMapping("/v1/statistics")
@Slf4j
public class TickQueryController {

    @Autowired
    private TickQueryService tickQueryService;

    @GetMapping
    public ResponseEntity<StatisticResponse> fetchStatistics() {
        long starTime = System.currentTimeMillis();
        StatisticResponse statisticResponse=  tickQueryService.fetchStatistics();
        log.info("Execution time for fetchStatistics  ={} msec", System.currentTimeMillis() -starTime);
        return new ResponseEntity<>(statisticResponse, HttpStatus.OK);
    }

    @GetMapping("/{instrument_identifier}")
    @ApiErrors(value = {
            @ApiError(code = 200, reason = "Request is OK"),
            @ApiError(code = 404, reason = "No Resource found for the requested uri."),
            @ApiError(code = 417, reason = "Expectation Failed"),
            @ApiError(code = 204, reason = "No Content found"),
            @ApiError(code = 500, reason = "Internal Error Occurred while processing request.")})
    public ResponseEntity<StatisticResponse> fetchStatisticsByInstrument(@PathVariable("instrument_identifier") String instrumentIdentifier) {
        long starTime = System.currentTimeMillis();
        StatisticResponse statisticResponse=  tickQueryService.fetchStatisticsByInstrument(instrumentIdentifier);
        log.info("Execution time for fetchStatisticsByInstrument  ={} msec", System.currentTimeMillis() -starTime);
        return new ResponseEntity<>(statisticResponse, HttpStatus.OK);
    }

}

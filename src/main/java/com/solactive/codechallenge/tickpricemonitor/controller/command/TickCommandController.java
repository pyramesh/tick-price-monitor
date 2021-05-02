package com.solactive.codechallenge.tickpricemonitor.controller.command;

import com.solactive.codechallenge.tickpricemonitor.config.ApiError;
import com.solactive.codechallenge.tickpricemonitor.config.ApiErrors;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.dto.TickResponse;
import com.solactive.codechallenge.tickpricemonitor.service.TickCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

/**
 * @author Ramesh.Yaleru on 4/29/2021
 */
@RestController
@RequestMapping("/v1")
@Slf4j
public class TickCommandController {

    @Autowired
    private TickCommandService tickCommandService;

    @PostMapping("/ticks")

    @ApiOperation(value = "create tick", notes = "create tick")
    @ApiErrors(value = {
            @ApiError(code = 200, reason = "Request is OK"),
            @ApiError(code = 404, reason = "No Resource found for the requested uri."),
            @ApiError(code = 417, reason = "Expectation Failed"),
            @ApiError(code = 204, reason = "No Content found"),
            @ApiError(code = 500, reason = "Internal Error Occurred while processing request.")})
    public ResponseEntity<TickResponse> createTick(@RequestHeader HttpHeaders headers, @RequestBody @Valid TickRequest tickRequest) {
        TickResponse response = tickCommandService.saveTick(tickRequest);
        log.info("response = {}",response);
        return new ResponseEntity(response.getStatusCode());
    }
}

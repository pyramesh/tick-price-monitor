package com.solactive.codechallenge.tickpricemonitor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.dto.TickResponse;
import com.solactive.codechallenge.tickpricemonitor.kafka.KafkaService;
import com.solactive.codechallenge.tickpricemonitor.service.TickCommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Service
@Slf4j
@Transactional
public class TickCommandServiceImpl implements TickCommandService {

    @Value("${tick.sliding.time.window}")
    private long slidingTimeInterval;

    @Value("${TICK_COMMANDS_TOPIC}")
    private String commandTopic;

    private KafkaService kafkaService;

    TickCommandServiceImpl(KafkaService kafkaService){
        this.kafkaService = kafkaService;
    }

    /*@Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.tick.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.tick.routing.name}")
    private String routingName;*/

    @Override
    public TickResponse saveTick(TickRequest tickRequest) {
        //validate
        Pair<Boolean, TickResponse> responsePair= validateRequest(tickRequest);
        if(responsePair.getKey()){
            //put in the TOPIC
            sendMessageQueue(tickRequest);
            log.info("responsePair value ={}", responsePair);
            return responsePair.getValue();
        }
        return responsePair.getValue();
    }

    @Transactional
    public void sendMessageQueue(TickRequest tickRequest) {
        try {
            kafkaService.sendMessage(tickRequest.getInstrument(), commandTopic, new ObjectMapper().writeValueAsString(tickRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //rabbitTemplate.convertAndSend(exchangeName, routingName, tickRequest);
    }


    private Pair<Boolean, TickResponse> validateRequest(TickRequest request) {
        TickResponse resp;
        boolean result = false;
       if (request.getTimeStamp() < (System.currentTimeMillis() - slidingTimeInterval)) {
            resp = TickResponse.builder().statusCode(HttpStatus.NO_CONTENT).build();
        } else {
            resp = TickResponse.builder().statusCode(HttpStatus.CREATED).build();
            result = true;
        }
        return new ImmutablePair<>(result, resp);
    }

}

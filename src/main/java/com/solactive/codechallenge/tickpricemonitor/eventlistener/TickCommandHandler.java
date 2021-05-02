package com.solactive.codechallenge.tickpricemonitor.eventlistener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solactive.codechallenge.tickpricemonitor.dto.TickRequest;
import com.solactive.codechallenge.tickpricemonitor.model.InstrumentTick;
import com.solactive.codechallenge.tickpricemonitor.service.InstrumentTickCache;
import com.solactive.codechallenge.tickpricemonitor.service.TickPriceStatisticCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ramesh.Yaleru on 4/30/2021
 */
@Component
@Slf4j
public class TickCommandHandler {

    private Map<String, InstrumentTick> instrumentMap = new ConcurrentHashMap<>();

    @Autowired
    private InstrumentTickCache instrumentTickCache;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    ObjectMapper objectMapper = new ObjectMapper();
    //@RabbitListener(queues = "${rabbitmq.tick.queue.name}")
    @Transactional
    @KafkaListener(topics = "${TICK_COMMANDS_TOPIC}", groupId = "${TICK_COMMANDS_TOPIC_GROUP_ID}",
            concurrency = "${message.consumer.thread.count}", containerFactory = "kafkaListenerContainerFactory")
    public void handleRequest(String requestMessage) {
       TickRequest tickRequest = null;
        try{
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            tickRequest = objectMapper.readValue(requestMessage, TickRequest.class);
        }catch (Exception exp){
            exp.printStackTrace();
        }
        instrumentTickCache.storeInstrument(tickRequest);
        prepareAndPublishEvent(tickRequest.getInstrument());
    }

    private void prepareAndPublishEvent(String instrument) {
        TickEventCreated tickEventCreated =  TickEventCreated.builder()
                .instrumentIdentifier(instrument)
                .status("CREATED")
                .build();
        applicationEventPublisher.publishEvent(tickEventCreated);
    }


    
}

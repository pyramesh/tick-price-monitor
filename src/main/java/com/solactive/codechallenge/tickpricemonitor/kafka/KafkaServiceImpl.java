package com.solactive.codechallenge.tickpricemonitor.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author Ramesh.Yaleru on 1/6/2021
 */
@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Override
    @Transactional
    public ListenableFuture<SendResult<String, String>> sendMessage(String key,String topic,  String message) {
        LOGGER.info("KafkaService :: sendMessage  for topic ={} , key ={}", topic, key);
        if(LOGGER.isDebugEnabled())
            LOGGER.debug("KafkaService :: sendMessage message = {}", message);
        ListenableFuture<SendResult<String, String>> future = key != null ? kafkaTemplate.send(topic,key, message): kafkaTemplate.send(topic,message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(final SendResult<String, String> message) {
                LOGGER.info("sent message with getRecordMetadata = {} " , message.getRecordMetadata());
            }
            @Override
            public void onFailure(final Throwable throwable) {
                LOGGER.error("ImmigrationFile Microservice :: There is some issues while sending message to kafka server message={} , error={} " + message, throwable);
                throw new RuntimeException(throwable);
            }
        });
        return future;

    }

//    @Override
//    public ListenableFuture<SendResult<String, String>> sendMessage(ProducerRecord message) {
//        LOGGER.info("KafkaService :: sendMessage  for message");
//        if(LOGGER.isDebugEnabled())
//            LOGGER.debug("KafkaService :: sendMessage message = {}", message);
//        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);
//        future.addCallback(new ListenableFutureCallback<>() {
//            @Override
//            public void onSuccess(final SendResult<String, String> message) {
//                LOGGER.info("sent message with getRecordMetadata = {} ", message.getRecordMetadata());
//            }
//
//            @Override
//            public void onFailure(final Throwable throwable) {
//                LOGGER.error("unable to send message= {} and throwable ={}" , message, throwable);
//                throw new RuntimeException(throwable);
//            }
//        });
//        return future;
//
//    }
}

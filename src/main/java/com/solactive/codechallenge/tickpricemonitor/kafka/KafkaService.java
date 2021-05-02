package com.solactive.codechallenge.tickpricemonitor.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author Ramesh.Yaleru on 1/6/2021
 */
public interface KafkaService {
    ListenableFuture<SendResult<String, String>> sendMessage(String key,String topic,String message) ;
    //public ListenableFuture<SendResult<String, String>> sendMessage(ProducerRecord message);

}

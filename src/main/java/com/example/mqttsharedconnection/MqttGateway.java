package com.example.mqttsharedconnection;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface MqttGateway {

  void publish(Object message, @Header(MqttHeaders.TOPIC) String topic);
}

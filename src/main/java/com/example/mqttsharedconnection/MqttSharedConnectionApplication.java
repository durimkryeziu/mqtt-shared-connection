package com.example.mqttsharedconnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class MqttSharedConnectionApplication {

  private static final Logger log = LoggerFactory.getLogger(MqttSharedConnectionApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(MqttSharedConnectionApplication.class, args);
  }

  @Lazy
  @Bean
  CommandLineRunner publishMessage(@Lazy MqttGateway gateway) {
    return args -> {
      log.info("Publishing a message to MQTT Broker...");
      gateway.publish("{ \"framework\": \"Spring Integration\" }", "topic1");
    };
  }
}

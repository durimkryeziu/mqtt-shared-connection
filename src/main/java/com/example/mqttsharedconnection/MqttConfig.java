package com.example.mqttsharedconnection;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.mqtt.core.ConsumerStopAction;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

@Configuration(proxyBeanMethods = false)
class MqttConfig {

  private static final Logger log = LoggerFactory.getLogger(MqttConfig.class);

  private static final String PUB_CLIENT_ID = "sameClientId";
  private static final String SUB_CLIENT_ID = "sameClientId";

  private final Jackson2JsonObjectMapper jackson2JsonObjectMapper;

  MqttConfig(Jackson2JsonObjectMapper jackson2JsonObjectMapper) {
    this.jackson2JsonObjectMapper = jackson2JsonObjectMapper;
  }

  @Bean
  MqttPahoClientFactory clientFactory() {
    MqttConnectOptions connectionOptions = new MqttConnectOptions();
    connectionOptions.setServerURIs(new String[]{"tcp://localhost:1883"});
    connectionOptions.setUserName("admin");
    connectionOptions.setPassword("123456".toCharArray());
    connectionOptions.setCleanSession(true);
    connectionOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
    DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
    clientFactory.setConnectionOptions(connectionOptions);
    clientFactory.setConsumerStopAction(ConsumerStopAction.UNSUBSCRIBE_CLEAN);
    return clientFactory;
  }

  @Bean
  IntegrationFlow mqttOutFlow(MqttPahoClientFactory clientFactory) {
    MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(PUB_CLIENT_ID, clientFactory);
    messageHandler.setAsync(true);
    return IntegrationFlows.from(MqttGateway.class)
        .transform(Transformers.toJson(this.jackson2JsonObjectMapper))
        .log("outFlow")
        .handle(messageHandler)
        .get();
  }

  @Bean
  IntegrationFlow mqttInFlow(MqttPahoClientFactory clientFactory) {
    MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(SUB_CLIENT_ID, clientFactory, "notified");
    return IntegrationFlows.from(adapter)
        .log("inFlow")
        .handle(this::handleMessage)
        .get();
  }

  void handleMessage(Message<?> message) throws MessagingException {
    log.info("Received message: {}", message.getPayload());
  }

}

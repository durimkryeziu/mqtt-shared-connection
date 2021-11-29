/*
 * Copyright (C) 2021 - 2021 Philips Domestic Appliances Holding B.V.
 * All rights reserved.
 */

package com.example.mqttsharedconnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;

@Configuration(proxyBeanMethods = false)
class IntegrationConfig {

  private final ObjectMapper objectMapper;

  IntegrationConfig(ObjectProvider<ObjectMapper> objectMapper) {
    this.objectMapper = objectMapper.getObject();
  }

  @Bean
  Jackson2JsonObjectMapper jsonObjectMapper() {
    return new Jackson2JsonObjectMapper(this.objectMapper);
  }

}

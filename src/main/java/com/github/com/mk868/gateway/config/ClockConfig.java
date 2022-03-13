package com.github.com.mk868.gateway.config;

import java.time.Clock;
import java.time.InstantSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {

  @Bean
  public InstantSource instantSource() {
    return Clock.systemUTC();
  }

}

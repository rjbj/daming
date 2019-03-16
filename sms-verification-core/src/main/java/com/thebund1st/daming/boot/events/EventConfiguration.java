package com.thebund1st.daming.boot.events;

import com.thebund1st.daming.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class EventConfiguration {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @ConditionalOnMissingBean(EventPublisher.class)
    @Bean
    public EventPublisher eventPublisher() {
        return event -> applicationEventPublisher.publishEvent(event);
    }


}
package com.yas.webhook.integration.inbound;

import com.fasterxml.jackson.databind.JsonNode;
import com.yas.webhook.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventInbound {

  private WebhookService webhookService;

  @KafkaListener(topics = {
      "${webhook.integration.kafka.product.topic-name}"}, groupId = "${spring.kafka.consumer.group-id}")
  public void onProductEvent(JsonNode jsonNode) {
    webhookService.handleEvent(jsonNode);
  }
}

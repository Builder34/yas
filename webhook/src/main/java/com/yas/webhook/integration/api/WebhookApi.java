package com.yas.webhook.integration.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WebhookApi {

  private RestClient restClient;

  public void notify(String url, JsonNode jsonNode) {
    restClient.post()
        .uri("")
        .body(jsonNode);
  }
}

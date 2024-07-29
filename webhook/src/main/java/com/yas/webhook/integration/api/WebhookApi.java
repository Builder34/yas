package com.yas.webhook.integration.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class WebhookApi {

  private final RestClient restClient;

  public void notify(String url, JsonNode jsonNode) {
    restClient.post()
        .uri(url)
        .body(jsonNode);
  }
}

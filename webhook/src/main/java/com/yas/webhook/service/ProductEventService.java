package com.yas.webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.yas.webhook.config.constants.MessageCode;
import com.yas.webhook.config.exception.NotFoundException;
import com.yas.webhook.model.Event;
import com.yas.webhook.model.HookEvent;
import com.yas.webhook.model.enumeration.EventName;
import com.yas.webhook.model.enumeration.Operation;
import com.yas.webhook.repository.EventRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventService {

  private final EventRepository eventRepository;
  private final WebHookService webHookService;

  public void onUpdatedProductEvent(JsonNode updatedEvent) {
    String operation = updatedEvent.get("op").asText();
    if (!Objects.equals(operation, Operation.UPDATE.getName())) {
      return;
    }
    Event event = eventRepository.findByName(EventName.ON_PRODUCT_UPDATED)
        .orElseThrow(() -> new NotFoundException(MessageCode.EVENT_NOT_FOUND, EventName.ON_PRODUCT_UPDATED));
    List<HookEvent> hookEvents = event.getHookEvents();
    hookEvents.forEach(hookEvent -> {
      String url = hookEvent.getHook().getPayloadUrl();
      JsonNode payload = updatedEvent.get("after");

      webHookService.notifyToWebhook(url, payload);
    });
  }

}

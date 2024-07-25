package com.yas.webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.yas.webhook.repository.WebhookRepository;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WebhookService {

  private final WebhookRepository webhookRepository;

  public WebhookService(WebhookRepository webhookRepository) {
    this.webhookRepository = webhookRepository;
  }

  public WebhookListGetVm getPageableWebhooks(int pageNo, int pageSize) {
    return null;
  }

  public List<WebhookVm> findAllWebhooks() {
    return List.of();
  }

  public WebhookVm findById(Long id) {
    return null;
  }

  public WebhookVm create(WebhookPostVm webhookPostVm) {
    return null;
  }

  public void update(WebhookPostVm webhookPostVm, Long id) {

  }

  public void delete(Long id) {

  }

  public void handleEvent(JsonNode jsonNode) {
    
  }
}

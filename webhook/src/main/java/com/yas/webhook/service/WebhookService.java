package com.yas.webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.yas.webhook.config.constants.MessageCode;
import com.yas.webhook.config.exception.NotFoundException;
import com.yas.webhook.integration.api.WebhookApi;
import com.yas.webhook.model.Event;
import com.yas.webhook.model.Webhook;
import com.yas.webhook.model.WebhookEvent;
import com.yas.webhook.model.mapper.WebhookMapper;
import com.yas.webhook.model.viewmodel.webhook.HookEventVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import com.yas.webhook.repository.EventRepository;
import com.yas.webhook.repository.WebhookRepository;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WebhookService {

  private final WebhookRepository webhookRepository;
  private final EventRepository eventRepository;
  private final WebhookMapper webhookMapper;
  private final WebhookApi webHookApi;

  public WebhookListGetVm getPageableWebhooks(int pageNo, int pageSize) {
    PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
    Page<Webhook> webhooks = webhookRepository.findAll(pageRequest);
    return webhookMapper.toWebhookListGetVm(webhooks, pageNo, pageSize);
  }

  public List<WebhookVm> findAllWebhooks() {
    List<Webhook> webhooks = webhookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    return webhooks.stream().map(webhookMapper::toWebhookVm).toList();
  }

  public WebhookVm findById(Long id) {
    return webhookMapper.toWebhookVm(webhookRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageCode.WEBHOOK_NOT_FOUND, id)));
  }

  public WebhookVm create(WebhookPostVm webhookPostVm) {
    Webhook createdWebhook = this.initializeCreatedWebHook(webhookPostVm);
    Webhook webHook = webhookRepository.save(createdWebhook);
    return webhookMapper.toWebhookVm(webHook);
  }

  public void update(WebhookPostVm webhookPostVm, Long id) {
    Webhook webHook = webhookRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageCode.WEBHOOK_NOT_FOUND, id));
    Webhook updatedWebhook = this.initializeUpdatedWebHook(webHook, webhookPostVm);
    webhookRepository.save(updatedWebhook);
  }

  public void delete(Long id) {
    if (!webhookRepository.existsById(id)) {
      throw new NotFoundException(MessageCode.WEBHOOK_NOT_FOUND, id);
    }
    webhookRepository.deleteById(id);
  }

  @Async
  public void notifyToWebhook(String url, JsonNode payload) {
    webHookApi.notify(url, payload);
  }

  private Webhook initializeCreatedWebHook(WebhookPostVm webhookPostVm){
    Webhook createdWebhook = webhookMapper.toCreatedWebhook(webhookPostVm);
    List<WebhookEvent> hookEvents = initializeHookEvents(createdWebhook, webhookPostVm.getHookEventVms());
    createdWebhook.setHookEvents(hookEvents);
    return createdWebhook;
  }

  private Webhook initializeUpdatedWebHook(Webhook webHook, WebhookPostVm webhookPostVm) {
    Webhook updatedWebhook = webhookMapper.toUpdatedWebhook(webHook, webhookPostVm);
    List<WebhookEvent> hookEvents = initializeHookEvents(updatedWebhook, webhookPostVm.getHookEventVms());
    updatedWebhook.setHookEvents(hookEvents);
    return updatedWebhook;
  }

  private List<WebhookEvent> initializeHookEvents(Webhook webHook, List<HookEventVm> hookEventVms) {
    return hookEventVms.stream().map(hookEventVm -> {
      WebhookEvent hookEvent = new WebhookEvent();
      hookEvent.setWebhook(webHook);
      Event event = eventRepository.findById(hookEventVm.getEventId())
              .orElseThrow(() -> new NotFoundException(MessageCode.EVENT_NOT_FOUND, hookEventVm.getEventId()));
      hookEvent.setEvent(event);
      return hookEvent;
    }).toList();
  }
}

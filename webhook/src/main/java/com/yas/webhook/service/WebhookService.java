package com.yas.webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.yas.webhook.config.constants.MessageCode;
import com.yas.webhook.config.exception.NotFoundException;
import com.yas.webhook.integration.api.WebhookApi;
import com.yas.webhook.model.Webhook;
import com.yas.webhook.model.WebhookEvent;
import com.yas.webhook.model.mapper.WebhookMapper;
import com.yas.webhook.model.viewmodel.webhook.WebhookEventVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import com.yas.webhook.repository.EventRepository;
import com.yas.webhook.repository.WebhookEventRepository;
import com.yas.webhook.repository.WebhookRepository;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WebhookService {

  private final WebhookRepository webhookRepository;
  private final EventRepository eventRepository;
  private final WebhookEventRepository webhookEventRepository;
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
    Webhook createdWebhook = webhookMapper.toCreatedWebhook(webhookPostVm);
    createdWebhook = webhookRepository.save(createdWebhook);
    if (!CollectionUtils.isEmpty(webhookPostVm.getWebhookEventVms())) {
      List<WebhookEvent> webhookEvents = initializeWebhookEvents(createdWebhook.getId(), webhookPostVm.getWebhookEventVms());
      webhookEvents = webhookEventRepository.saveAll(webhookEvents);
      createdWebhook.setWebhookEvents(webhookEvents);
    }
    return webhookMapper.toWebhookVm(createdWebhook);
  }

  public void update(WebhookPostVm webhookPostVm, Long id) {
    Webhook existedWebHook = webhookRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageCode.WEBHOOK_NOT_FOUND, id));
    Webhook updatedWebhook = webhookMapper.toUpdatedWebhook(existedWebHook, webhookPostVm);
    webhookRepository.save(updatedWebhook);
    webhookEventRepository.deleteAll(existedWebHook.getWebhookEvents().stream().toList());
    if (!CollectionUtils.isEmpty(webhookPostVm.getWebhookEventVms())) {
      List<WebhookEvent> webhookEvents = initializeWebhookEvents(id, webhookPostVm.getWebhookEventVms());
      webhookEventRepository.saveAll(webhookEvents);
    }
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

  private List<WebhookEvent> initializeWebhookEvents(Long webhookId, List<WebhookEventVm> hookEventVms) {
    return hookEventVms.stream().map(hookEventVm -> {
      WebhookEvent webhookEvent = new WebhookEvent();
      webhookEvent.setWebhookId(webhookId);
      long eventId = hookEventVm.getEventId();
      eventRepository.findById(eventId)
              .orElseThrow(() -> new NotFoundException(MessageCode.EVENT_NOT_FOUND, eventId));
      webhookEvent.setEventId(eventId);
      return webhookEvent;
    }).toList();
  }
}

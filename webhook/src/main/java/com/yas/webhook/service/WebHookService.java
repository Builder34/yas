package com.yas.webhook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.yas.webhook.config.constants.MessageCode;
import com.yas.webhook.config.exception.NotFoundException;
import com.yas.webhook.model.Event;
import com.yas.webhook.model.HookEvent;
import com.yas.webhook.model.WebHook;
import com.yas.webhook.model.mapper.WebHookMapper;
import com.yas.webhook.model.viewmodel.webhook.HookEventVm;
import com.yas.webhook.repository.EventRepository;
import com.yas.webhook.repository.WebHookRepository;
import com.yas.webhook.model.viewmodel.webhook.WebHookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookVm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WebHookService {

  private final WebHookRepository webhookRepository;
  private final EventRepository eventRepository;
  private final WebHookMapper webhookMapper;

  public WebHookService(WebHookRepository webhookRepository, EventRepository eventRepository, WebHookMapper webhookMapper) {
    this.webhookRepository = webhookRepository;
      this.eventRepository = eventRepository;
      this.webhookMapper = webhookMapper;
  }

  public WebHookListGetVm getPageableWebhooks(int pageNo, int pageSize) {
    PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
    Page<WebHook> webhooks = webhookRepository.findAll(pageRequest);
    return webhookMapper.toWebhookListGetVm(webhooks, pageNo, pageSize);
  }

  public List<WebHookVm> findAllWebhooks() {
    List<WebHook> webhooks = webhookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    return webhooks.stream().map(webhookMapper::toWebhookVm).toList();
  }

  public WebHookVm findById(Long id) {
    return webhookMapper.toWebhookVm(webhookRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageCode.WEBHOOK_NOT_FOUND, id)));
  }

  public WebHookVm create(WebHookPostVm webhookPostVm) {
    WebHook createdWebhook = this.initializeCreatedWebHook(webhookPostVm);
    WebHook webHook = webhookRepository.save(createdWebhook);
    return webhookMapper.toWebhookVm(webHook);
  }

  public void update(WebHookPostVm webhookPostVm, Long id) {
    WebHook webHook = webhookRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageCode.WEBHOOK_NOT_FOUND, id));
    WebHook updatedWebhook = this.initializeUpdatedWebHook(webHook, webhookPostVm);
    webhookRepository.save(updatedWebhook);
  }

  public void delete(Long id) {
    if (!webhookRepository.existsById(id)) {
      throw new NotFoundException(MessageCode.WEBHOOK_NOT_FOUND, id);
    }
    webhookRepository.deleteById(id);
  }

  public void handleEvent(JsonNode jsonNode) {
    
  }

  private WebHook initializeCreatedWebHook(WebHookPostVm webhookPostVm){
    WebHook createdWebhook = webhookMapper.toCreatedWebhook(webhookPostVm);
    List<HookEvent> hookEvents = initializeHookEvents(createdWebhook, webhookPostVm.getHookEventVms());
    createdWebhook.setHookEvents(hookEvents);
    return createdWebhook;
  }

  private WebHook initializeUpdatedWebHook(WebHook webHook, WebHookPostVm webhookPostVm) {
    WebHook updatedWebhook = webhookMapper.toUpdatedWebhook(webHook, webhookPostVm);
    List<HookEvent> hookEvents = initializeHookEvents(updatedWebhook, webhookPostVm.getHookEventVms());
    updatedWebhook.setHookEvents(hookEvents);
    return updatedWebhook;
  }

  private List<HookEvent> initializeHookEvents(WebHook webHook, List<HookEventVm> hookEventVms) {
    return hookEventVms.stream().map(hookEventVm -> {
      HookEvent hookEvent = new HookEvent();
      hookEvent.setHook(webHook);
      Event event = eventRepository.findById(hookEventVm.getEventId())
              .orElseThrow(() -> new NotFoundException(MessageCode.EVENT_NOT_FOUND, hookEventVm.getEventId()));
      hookEvent.setEvent(event);
      return hookEvent;
    }).toList();
  }
}

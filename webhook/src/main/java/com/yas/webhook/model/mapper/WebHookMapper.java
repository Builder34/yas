package com.yas.webhook.model.mapper;

import com.yas.webhook.model.WebhookEvent;
import com.yas.webhook.model.WebHook;
import com.yas.webhook.model.viewmodel.webhook.HookEventVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebHookMapper {

    @Mapping(target = "hookEventVms", source = "hookEvents", qualifiedByName = "toHookEventVms")
    WebHookVm toWebhookVm(WebHook webhook);

    @Named("toHookEventVms")
    default List<HookEventVm> toHookEventVms(List<WebhookEvent> hookEvents){
        return hookEvents.stream().map(hookEvent
                -> HookEventVm.builder()
                .eventId(hookEvent.getEventId())
                .webHookEvent(hookEvent.getEvent().getName())
                .build()).toList();
    }

    default WebHookListGetVm toWebhookListGetVm(Page<WebHook> webHooks, int pageNo, int pageSize) {
        return WebHookListGetVm.builder()
                .webhooks(webHooks.stream().map(this::toWebhookVm).toList())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(webHooks.getTotalElements())
                .totalElements( webHooks.getTotalPages())
                .isLast( webHooks.isLast()).build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payloadUrl", source = "webhookPostVm.payloadUrl")
    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "secret", source = "webhookPostVm.secret")
    @Mapping(target = "isActive", source = "webhookPostVm.isActive")
    @Mapping(target = "hookEvents", ignore = true)
    WebHook toUpdatedWebhook(@MappingTarget WebHook webHook, WebHookPostVm webhookPostVm);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hookEvents", ignore = true)
    WebHook toCreatedWebhook(WebHookPostVm webhookPostVm);

}

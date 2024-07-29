package com.yas.webhook.model.mapper;

import com.yas.webhook.model.Webhook;
import com.yas.webhook.model.WebhookEvent;
import com.yas.webhook.model.viewmodel.webhook.HookEventVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebhookMapper {

    @Mapping(target = "hookEventVms", source = "hookEvents", qualifiedByName = "toHookEventVms")
    WebhookVm toWebhookVm(Webhook webhook);

    @Named("toHookEventVms")
    default List<HookEventVm> toHookEventVms(List<WebhookEvent> hookEvents){
        return hookEvents.stream().map(hookEvent
                -> HookEventVm.builder()
                .eventId(hookEvent.getEventId())
                .hookEvent(hookEvent.getEvent().getName())
                .build()).toList();
    }

    default WebhookListGetVm toWebhookListGetVm(Page<Webhook> webHooks, int pageNo, int pageSize) {
        return WebhookListGetVm.builder()
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
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Webhook toUpdatedWebhook(@MappingTarget Webhook webhook, WebhookPostVm webhookPostVm);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hookEvents", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Webhook toCreatedWebhook(WebhookPostVm webhookPostVm);

}

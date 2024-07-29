package com.yas.webhook.model.mapper;

import com.yas.webhook.model.Webhook;
import com.yas.webhook.model.WebhookEvent;
import com.yas.webhook.model.viewmodel.webhook.WebhookEventVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import org.hibernate.mapping.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WebhookMapper {

    @Mapping(target = "webhookEventVms", source = "webhookEvents", qualifiedByName = "toWebhookEventVms")
    @Mapping(target = "secret", ignore = true)
    WebhookVm toWebhookVm(Webhook webhook);

    @Named("toWebhookEventVms")
    default List<WebhookEventVm> toWebhookEventVms(List<WebhookEvent> webhookEvents){
        return CollectionUtils.isEmpty(webhookEvents) ? Collections.emptyList() :webhookEvents.stream().map(webhookEvent
                -> WebhookEventVm.builder()
                .eventId(webhookEvent.getEventId())
                .build()).toList();
    }

    default WebhookListGetVm toWebhookListGetVm(Page<Webhook> webhooks, int pageNo, int pageSize) {
        return WebhookListGetVm.builder()
                .webhooks(webhooks.stream().map(this::toWebhookVm).toList())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(webhooks.getTotalElements())
                .totalElements( webhooks.getTotalPages())
                .isLast( webhooks.isLast()).build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payloadUrl", source = "webhookPostVm.payloadUrl")
    @Mapping(target = "contentType", ignore = true)
    @Mapping(target = "secret", source = "webhookPostVm.secret")
    @Mapping(target = "isActive", source = "webhookPostVm.isActive")
    @Mapping(target = "webhookEvents", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Webhook toUpdatedWebhook(@MappingTarget Webhook webhook, WebhookPostVm webhookPostVm);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "webhookEvents", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    Webhook toCreatedWebhook(WebhookPostVm webhookPostVm);

}

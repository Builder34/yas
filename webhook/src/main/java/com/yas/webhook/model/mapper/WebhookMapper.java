package com.yas.webhook.model.mapper;

import com.yas.webhook.model.WebHook;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import org.mapstruct.Mapper;

@Mapper
public interface WebhookMapper {

    WebhookVm toWebhookVm(WebHook webhook);
}

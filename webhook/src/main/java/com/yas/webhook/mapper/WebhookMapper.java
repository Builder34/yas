package com.yas.webhook.mapper;

import com.yas.webhook.model.WebHook;
import com.yas.webhook.viewmodel.webhook.WebhookVm;
import org.mapstruct.Mapper;

@Mapper
public interface WebhookMapper {

    WebhookVm toWebhookVm(WebHook webhook);
}

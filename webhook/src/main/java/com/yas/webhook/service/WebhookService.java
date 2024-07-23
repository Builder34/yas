package com.yas.webhook.service;

import com.yas.webhook.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.viewmodel.webhook.WebhookVm;

import java.util.List;

public interface WebhookService {

    WebhookListGetVm getPageableWebhooks(int pageNo, int pageSize);

    List<WebhookVm> findAllWebhooks();

    WebhookVm findById(Long id);

    WebhookVm create(WebhookPostVm webhookPostVm);

    void update(WebhookPostVm webhookPostVm, Long id);

    void delete(Long id);
}

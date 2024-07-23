package com.yas.webhook.service;

import com.yas.webhook.repository.WebhookRepository;
import com.yas.webhook.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.viewmodel.webhook.WebhookVm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WebhookServiceImpl implements WebhookService{

    private final WebhookRepository webhookRepository;

    public WebhookServiceImpl(WebhookRepository webhookRepository) {
        this.webhookRepository = webhookRepository;
    }

    @Override
    public WebhookListGetVm getPageableWebhooks(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public List<WebhookVm> findAllWebhooks() {
        return List.of();
    }

    @Override
    public WebhookVm findById(Long id) {
        return null;
    }

    @Override
    public WebhookVm create(WebhookPostVm webhookPostVm) {
        return null;
    }

    @Override
    public void update(WebhookPostVm webhookPostVm, Long id) {

    }

    @Override
    public void delete(Long id) {

    }
}

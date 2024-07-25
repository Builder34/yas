package com.yas.webhook.repository;

import com.yas.webhook.model.WebHook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookRepository extends JpaRepository<WebHook, Long> {
}

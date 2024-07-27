package com.yas.webhook.repository;

import com.yas.webhook.model.Event;
import com.yas.webhook.model.WebHook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}

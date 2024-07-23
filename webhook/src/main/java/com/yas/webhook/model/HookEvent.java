package com.yas.webhook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hook_event")
@Getter
@Setter
@NoArgsConstructor
public class HookEvent {

  @Column(name = "hookId")
  private Long hookId;

  @Column(name = "event_id")
  private Long eventId;

  @ManyToOne
  @JoinColumn(name = "hook_id", updatable = false)
  private Hook hook;

  @ManyToOne
  @JoinColumn(name = "event_id", updatable = false)
  private Event event;
}

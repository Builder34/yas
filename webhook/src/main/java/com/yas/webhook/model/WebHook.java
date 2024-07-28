package com.yas.webhook.model;

import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "webhook")
@Getter
@Setter
@NoArgsConstructor
public class WebHook {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "payload_url")
  private String payloadUrl;
  @Column(name = "content_type")
  private String contentType;
  @Column(name = "secret")
  private String secret;
  @Column(name = "is_active")
  private Boolean isActive;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "hook")
  List<HookEvent> hookEvents;

}

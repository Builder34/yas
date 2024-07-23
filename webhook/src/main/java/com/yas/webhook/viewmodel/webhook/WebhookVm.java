package com.yas.webhook.viewmodel.webhook;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class WebhookVm {
    public Long id;
    String name;
    String secret;
}

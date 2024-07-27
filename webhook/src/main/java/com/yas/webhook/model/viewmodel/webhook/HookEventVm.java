package com.yas.webhook.model.viewmodel.webhook;

import com.yas.webhook.model.enumeration.WebHookEvent;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class HookEventVm {
    long eventId;
    WebHookEvent webHookEvent;
}

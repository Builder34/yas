package com.yas.webhook.model.viewmodel.webhook;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebHookListGetVm {
    List<WebHookVm> webhooks;
    int pageNo;
    int pageSize;
    long totalElements;
    long totalPages;
    boolean isLast;
}


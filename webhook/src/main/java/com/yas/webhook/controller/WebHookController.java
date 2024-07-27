package com.yas.webhook.controller;

import com.yas.webhook.config.constants.ApiConstant;
import com.yas.webhook.config.constants.PageableConstant;
import com.yas.webhook.model.viewmodel.error.ErrorVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebHookVm;
import com.yas.webhook.service.WebHookService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(ApiConstant.WEBHOOK_URL)
public class WebHookController {
  private final WebHookService webhookService;

  public WebHookController(WebHookService webhookService) {
    this.webhookService = webhookService;
  }

  @GetMapping("/paging")
  public ResponseEntity<WebHookListGetVm> getPageableWebhooks(
      @RequestParam(value = "pageNo", defaultValue = PageableConstant.DEFAULT_PAGE_NUMBER, required = false) final int pageNo,
      @RequestParam(value = "pageSize", defaultValue = PageableConstant.DEFAULT_PAGE_SIZE, required = false) final int pageSize) {
    return ResponseEntity.ok(webhookService.getPageableWebhooks(pageNo, pageSize));
  }

  @GetMapping
  public ResponseEntity<List<WebHookVm>> listWebhooks() {
    return ResponseEntity.ok(webhookService.findAllWebhooks());
  }

  @GetMapping("/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ApiConstant.CODE_200, description = ApiConstant.OK, content = @Content(schema = @Schema(implementation = WebHookVm.class))),
      @ApiResponse(responseCode = ApiConstant.CODE_404, description = ApiConstant.NOT_FOUND, content = @Content(schema = @Schema(implementation = ErrorVm.class)))})
  public ResponseEntity<WebHookVm> getWebhook(@PathVariable("id") final Long id) {
    return ResponseEntity.ok(webhookService.findById(id));
  }

  @PostMapping
  @ApiResponses(value = {
      @ApiResponse(responseCode = ApiConstant.CODE_201, description = ApiConstant.CREATED, content = @Content(schema = @Schema(implementation = WebHookVm.class))),
      @ApiResponse(responseCode = ApiConstant.CODE_400, description = ApiConstant.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ErrorVm.class)))})
  public ResponseEntity<WebHookVm> createWebhook(
      @Valid @RequestBody final WebHookPostVm webhookPostVm,
      final UriComponentsBuilder uriComponentsBuilder) {
    WebHookVm webhook = webhookService.create(webhookPostVm);
    return ResponseEntity.created(
            uriComponentsBuilder
                .replacePath("/webhooks/{id}")
                .buildAndExpand(webhook)
                .toUri())
        .body(webhook);
  }

  @PutMapping("/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ApiConstant.CODE_204, description = ApiConstant.NO_CONTENT, content = @Content()),
      @ApiResponse(responseCode = ApiConstant.CODE_404, description = ApiConstant.NOT_FOUND, content = @Content(schema = @Schema(implementation = ErrorVm.class))),
      @ApiResponse(responseCode = ApiConstant.CODE_400, description = ApiConstant.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ErrorVm.class)))})
  public ResponseEntity<Void> updateWebhook(@PathVariable final Long id,
      @Valid @RequestBody final WebHookPostVm webhookPostVm) {
    webhookService.update(webhookPostVm, id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ApiConstant.CODE_204, description = ApiConstant.NO_CONTENT, content = @Content()),
      @ApiResponse(responseCode = ApiConstant.CODE_404, description = ApiConstant.NOT_FOUND, content = @Content(schema = @Schema(implementation = ErrorVm.class))),
      @ApiResponse(responseCode = ApiConstant.CODE_400, description = ApiConstant.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ErrorVm.class)))})
  public ResponseEntity<Void> deleteWebhook(@PathVariable(name = "id") final Long id) {
    webhookService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

package com.example.equipment.controller;

import com.example.equipment.entity.History;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.HistoryForm;
import com.example.equipment.service.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HistoryController {
  private final HistoryService historyService;

  public HistoryController(HistoryService historyService) {
    this.historyService = historyService;
  }

  // 指定した設備IDの点検履歴を取得する
  @GetMapping("/equipments/{equipmentId}/histories")
  public List<History> getHistoryByEquipmentId(@PathVariable("equipmentId") int equipmentId) {
    return historyService.findHistoryByEquipmentId(equipmentId);
  }

  // 指定した設備IDの点検履歴をHistoryForm型でリクエストされた内容で登録する
  @PostMapping("/equipments/{equipmentId}/histories")
  public ResponseEntity<Map<String, String>> createHistory(
      @PathVariable("equipmentId") int equipmentId,
      @RequestBody @Validated HistoryForm form, UriComponentsBuilder uriBuilder) {
    History history = historyService.createHistory(equipmentId, form);
    URI url = uriBuilder
        .path("/equipments/" + equipmentId + "/histories/" + history.getCheckHistoryId())
        .build().toUri();
    return ResponseEntity.created(url).body(Map.of("message", "点検履歴が正常に登録されました"));
  }

  // 指定したIDの点検履歴をHistoryForm型でリクエストされた内容で更新する
  @PatchMapping("/histories/{checkHistoryId}")
  public ResponseEntity<Map<String, String>> updateHistory(
      @PathVariable("checkHistoryId") int checkHistoryId,
      @RequestBody @Validated HistoryForm form) {
    historyService.updateHistory(
        checkHistoryId, form.getImplementationDate(), form.getCheckType(), form.getResult());
    return ResponseEntity.ok(Map.of("message", "点検履歴が正常に更新されました"));
  }

  // 指定したIDの点検履歴を削除する
  @DeleteMapping("/histories/{checkHistoryId}")
  public ResponseEntity<Map<String, String>> deleteHistoryByCheckHistoryId(
      @PathVariable("checkHistoryId") int checkHistoryId) {
    historyService.deleteHistoryByCheckHistoryId(checkHistoryId);
    return ResponseEntity.ok(Map.of("message", "点検履歴が正常に削除されました"));
  }

  // 指定した設備IDに紐づく点検履歴を削除する
  @DeleteMapping("/equipments/{equipmentId}/histories")
  public ResponseEntity<Map<String, String>> deleteHistoryByEquipmentId(
      @PathVariable("equipmentId") int equipmentId) {
    historyService.deleteHistoryByEquipmentId(equipmentId);
    return ResponseEntity.ok(Map.of("message", "点検履歴が正常に削除されました"));
  }

  // リソースが存在しない時のエラーハンドリング
  @ExceptionHandler(value = ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNoResourceFound(
      ResourceNotFoundException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "timestamp", ZonedDateTime.now().toString(),
        "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
        "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
        "message", e.getMessage(),
        "path", request.getRequestURI());
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  // バリデーションチェックによるエラーハンドリング
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "timestamp", ZonedDateTime.now().toString(),
        "status", String.valueOf(HttpStatus.BAD_REQUEST.value()),
        "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "message", "implementationDate,checkType,"
            + "resultは必須項目です。checkTypeは10文字以内、resultは50文字以内で入力してください",
        "path", request.getRequestURI());
    return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
  }
}

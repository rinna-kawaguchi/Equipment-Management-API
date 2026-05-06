package com.example.equipment.controller;

import com.example.equipment.entity.CheckType;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.CheckTypeForm;
import com.example.equipment.service.CheckTypeService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CheckTypeController {

  private final CheckTypeService checkTypeService;

  public CheckTypeController(CheckTypeService checkTypeService) {
    this.checkTypeService = checkTypeService;
  }

  @GetMapping("/check-types")
  public List<CheckType> getCheckTypes() {
    return checkTypeService.findAllCheckTypes();
  }

  @PostMapping("/check-types")
  public ResponseEntity<Map<String, String>> createCheckType(
      @RequestBody @Validated CheckTypeForm form, UriComponentsBuilder uriBuilder) {
    CheckType checkType = checkTypeService.createCheckType(form);
    URI url = uriBuilder.path("/check-types/" + checkType.getCheckTypeId()).build().toUri();
    return ResponseEntity.created(url).body(Map.of("message", "点検種別が正常に登録されました"));
  }

  @DeleteMapping("/check-types/{checkTypeId}")
  public ResponseEntity<Map<String, String>> deleteCheckType(
      @PathVariable("checkTypeId") int checkTypeId) {
    checkTypeService.deleteCheckType(checkTypeId);
    return ResponseEntity.ok(Map.of("message", "点検種別が正常に削除されました"));
  }

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

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "timestamp", ZonedDateTime.now().toString(),
        "status", String.valueOf(HttpStatus.BAD_REQUEST.value()),
        "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "message", "nameは必須項目です。20文字以内で入力してください",
        "path", request.getRequestURI());
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}

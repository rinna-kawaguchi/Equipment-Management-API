package com.example.equipment.controller;

import com.example.equipment.entity.Plan;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.PlanForm;
import com.example.equipment.service.PlanService;
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
public class PlanController {
  private final PlanService planService;

  public PlanController(PlanService planService) {
    this.planService = planService;
  }

  // 指定した設備の点検計画を取得する
  @GetMapping("/equipments/{equipmentId}/plans")
  public List<Plan> getPlanByEquipmentId(@PathVariable("equipmentId") int equipmentId) {
    return planService.findPlanByEquipmentId(equipmentId);
  }

  // 指定した設備の点検計画を登録する
  @PostMapping("/equipments/{equipmentId}/plans")
  public ResponseEntity<Map<String, String>> createPlan(
      @PathVariable("equipmentId") int equipmentId,
      @RequestBody @Validated PlanForm form, UriComponentsBuilder uriBuilder) {
    Plan plan = planService.createPlan(equipmentId, form);
    URI url = uriBuilder
        .path("/equipments/" + equipmentId + "/plans/" + plan.getCheckPlanId()).build().toUri();
    return ResponseEntity.created(url).body(Map.of("message", "点検計画が正常に登録されました"));
  }

  @PatchMapping("/plans/{checkPlanId}")
  public ResponseEntity<Map<String, String>> updatePlan(
      @PathVariable("checkPlanId") int checkPlanId, @RequestBody @Validated PlanForm form) {
    planService.updatePlan(checkPlanId, form.getCheckType(), form.getPeriod(), form.getDeadline());
    return ResponseEntity.ok(Map.of("message", "点検計画が正常に更新されました"));
  }

  @DeleteMapping("/plans/{checkPlanId}")
  public ResponseEntity<Map<String, String>> deletePlan(
      @PathVariable("checkPlanId") int checkPlanId) {
    planService.deletePlan(checkPlanId);
    return ResponseEntity.ok(Map.of("message", "点検計画が正常に削除されました"));
  }

  @DeleteMapping("/equipments/{equipmentId}/plans")
  public ResponseEntity<Map<String, String>> deletePlanByEquipmentId(
      @PathVariable("equipmentId") int equipmentId) {
    planService.deletePlanByEquipmentId(equipmentId);
    return ResponseEntity.ok(Map.of("message", "点検計画が正常に削除されました"));
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
        "message", "checkType,periodは必須項目です。10文字以内で入力してください",
        "path", request.getRequestURI());
    return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
  }
}

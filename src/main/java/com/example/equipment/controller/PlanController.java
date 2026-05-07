package com.example.equipment.controller;

import com.example.equipment.entity.Plan;
import com.example.equipment.form.PlanForm;
import com.example.equipment.service.PlanService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class PlanController {

  private final PlanService planService;

  public PlanController(PlanService planService) {
    this.planService = planService;
  }

  @GetMapping("/equipments/{equipmentId}/plans")
  public List<Plan> getPlanByEquipmentId(@PathVariable("equipmentId") int equipmentId) {
    return planService.findPlanByEquipmentId(equipmentId);
  }

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
    planService.updatePlan(checkPlanId, form.getCheckTypeId(), form.getPeriodValue(),
        form.getPeriodUnit(), form.getDeadline());
    return ResponseEntity.ok(Map.of("message", "点検計画が正常に更新されました"));
  }

  @DeleteMapping("/plans/{checkPlanId}")
  public ResponseEntity<Map<String, String>> deletePlanByCheckPlanId(
      @PathVariable("checkPlanId") int checkPlanId) {
    planService.deletePlanByCheckPlanId(checkPlanId);
    return ResponseEntity.ok(Map.of("message", "点検計画が正常に削除されました"));
  }

  @DeleteMapping("/equipments/{equipmentId}/plans")
  public ResponseEntity<Map<String, String>> deletePlanByEquipmentId(
      @PathVariable("equipmentId") int equipmentId) {
    planService.deletePlanByEquipmentId(equipmentId);
    return ResponseEntity.ok(Map.of("message", "点検計画が正常に削除されました"));
  }
}

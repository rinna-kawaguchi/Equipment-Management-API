package com.example.equipment.controller;

import com.example.equipment.entity.Plan;
import com.example.equipment.form.PlanForm;
import com.example.equipment.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
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
  @GetMapping("/equipments/{equipmentId}/plan")
  public List<Plan> getPlanByEquipmentId(@PathVariable("equipmentId") int equipmentId) {
    return planService.findPlanByEquipmentId(equipmentId);
  }

  // 指定した設備の点検計画を登録する
  @PostMapping("/equipments/{equipmentId}/plan")
  public ResponseEntity<Map<String, String>> createPlan(
      @PathVariable("equipmentId") int equipmentId,
      @RequestBody @Validated PlanForm form, UriComponentsBuilder uriBuilder) {
    Plan plan = planService.createPlan(equipmentId, form);
    URI url = uriBuilder
        .path("/equipments/" + equipmentId + "/plan/" + plan.getCheckPlanId()).build().toUri();
    return ResponseEntity.created(url).body(Map.of("message", "点検計画が正常に登録されました"));
  }
}

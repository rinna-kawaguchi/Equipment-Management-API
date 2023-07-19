package com.example.equipment.controller;

import com.example.equipment.entity.Plan;
import com.example.equipment.service.PlanService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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
}

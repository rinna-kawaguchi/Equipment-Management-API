package com.example.equipment.service;

import com.example.equipment.entity.Plan;
import com.example.equipment.form.PlanForm;
import java.util.List;

public interface PlanService {

  List<Plan> findPlanByEquipmentId(int equipmentId);

  Plan createPlan(int equipmentId, PlanForm form);

  void updatePlan(int checkPlanId, String checkType, String period, String deadline);

  void deletePlanByCheckPlanId(int checkPlanId);

  void deletePlanByEquipmentId(int equipmentId);
}

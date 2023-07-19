package com.example.equipment.service;

import com.example.equipment.entity.Plan;
import java.util.List;

public interface PlanService {

  List<Plan> findPlanByEquipmentId(int equipmentId);
}

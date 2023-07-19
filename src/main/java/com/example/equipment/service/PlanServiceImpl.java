package com.example.equipment.service;

import com.example.equipment.entity.Plan;
import com.example.equipment.form.PlanForm;
import com.example.equipment.mapper.PlanMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

  private final PlanMapper planMapper;

  public PlanServiceImpl(PlanMapper planMapper) {
    this.planMapper = planMapper;
  }

  @Override
  public List<Plan> findPlanByEquipmentId(int equipmentId) {
    return planMapper.findPlanByEquipmentId(equipmentId);
  }

  @Override
  public Plan createPlan(int equipmentId, PlanForm form) {
    Plan plan = new Plan(equipmentId, form.getCheckType(), form.getPeriod(), form.getDeadline());
    planMapper.insertPlan(plan);
    return plan;
  }
}

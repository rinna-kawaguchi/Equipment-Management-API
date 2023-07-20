package com.example.equipment.service;

import com.example.equipment.entity.Plan;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.PlanForm;
import com.example.equipment.mapper.EquipmentMapper;
import com.example.equipment.mapper.PlanMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

  private final PlanMapper planMapper;
  private final EquipmentMapper equipmentMapper;

  public PlanServiceImpl(PlanMapper planMapper, EquipmentMapper equipmentMapper) {
    this.planMapper = planMapper;
    this.equipmentMapper = equipmentMapper;
  }

  // 指定した設備IDが存在しない場合は例外をスローする
  @Override
  public List<Plan> findPlanByEquipmentId(int equipmentId) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    return planMapper.findPlanByEquipmentId(equipmentId);
  }

  // 指定した設備IDが存在しない場合は例外をスローする
  @Override
  public Plan createPlan(int equipmentId, PlanForm form) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    Plan plan = new Plan(equipmentId, form.getCheckType(), form.getPeriod(), form.getDeadline());
    planMapper.insertPlan(plan);
    return plan;
  }

  @Override
  public void updatePlan(int checkPlanId, String checkType, String period, String deadline) {
    planMapper.findPlanByCheckPlanId(checkPlanId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    planMapper.updatePlan(checkPlanId, checkType, period, deadline);
  }
}

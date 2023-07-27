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

  // 指定した設備IDに紐づく点検計画を取得する。
  // 指定した設備IDの設備が存在しない場合であっても例外はスローせず取得できるようにする。
  @Override
  public List<Plan> findPlanByEquipmentId(int equipmentId) {
    return planMapper.findPlanByEquipmentId(equipmentId);
  }

  // 指定した設備IDの設備に紐づく点検計画を登録する。
  // 指定した設備IDの設備が存在しない場合は登録できないよう例外をスローする。
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

  @Override
  public void deletePlanByCheckPlanId(int checkPlanId) {
    planMapper.findPlanByCheckPlanId(checkPlanId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    planMapper.deletePlanByCheckPlanId(checkPlanId);
  }

  // 指定した設備IDに紐づく点検計画の削除。
  // 指定した設備IDをもつ設備が存在しない場合であっても例外はスローせず削除できるようにする。
  @Override
  public void deletePlanByEquipmentId(int equipmentId) {
    planMapper.deletePlanByEquipmentId(equipmentId);
  }
}

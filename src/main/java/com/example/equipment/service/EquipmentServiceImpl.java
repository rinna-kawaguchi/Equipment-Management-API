package com.example.equipment.service;

import com.example.equipment.controller.FindEquipmentResponse;
import com.example.equipment.entity.Equipment;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.EquipmentForm;
import com.example.equipment.mapper.EquipmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

  private final EquipmentMapper equipmentMapper;

  public EquipmentServiceImpl(EquipmentMapper equipmentMapper) {
    this.equipmentMapper = equipmentMapper;
  }

  // 設備の条件検索
  @Override
  public List<FindEquipmentResponse> findEquipment(
      String name, String number, String location, String deadline) {
    if (StringUtils.hasLength(deadline)) {
      return equipmentMapper.findEquipmentByDate(name, number, location, deadline);
    } else {
      return equipmentMapper.findEquipment(name, number, location);
    }
  }

  // 設備のID検索
  @Override
  public Equipment findEquipmentById(int equipmentId) {
    return equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
  }

  // 設備の登録処理
  @Override
  public Equipment createEquipment(EquipmentForm form) {
    Equipment equipment = new Equipment(form.getName(), form.getNumber(), form.getLocation());
    equipmentMapper.insertEquipment(equipment);
    return equipment;
  }

  // 設備の更新処理
  @Override
  public void updateEquipment(int equipmentId, String name, String number, String location) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    equipmentMapper.updateEquipment(equipmentId, name, number, location);
  }

  // 設備の削除処理
  @Override
  public void deleteEquipment(int equipmentId) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    equipmentMapper.deleteEquipment(equipmentId);
  }
}

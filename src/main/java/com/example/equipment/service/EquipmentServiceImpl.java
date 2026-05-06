package com.example.equipment.service;

import com.example.equipment.controller.FindEquipmentResponse;
import com.example.equipment.entity.Equipment;
import com.example.equipment.exception.DuplicateEquipmentException;
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

  @Override
  public List<FindEquipmentResponse> findEquipment(
      String name, String number, String location, String deadline) {
    if (StringUtils.hasLength(deadline)) {
      return equipmentMapper.findEquipmentByDate(name, number, location, deadline);
    } else {
      return equipmentMapper.findEquipment(name, number, location);
    }
  }

  @Override
  public Equipment findEquipmentById(int equipmentId) {
    return equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
  }

  @Override
  public Equipment createEquipment(EquipmentForm form) {
    if (equipmentMapper.existsDuplicateEquipment(
        form.getName(), form.getNumber(), form.getLocation())) {
      throw new DuplicateEquipmentException(
          "同じ設備名称・設備番号・設置場所の設備が既に登録されています");
    }
    Equipment equipment = new Equipment(
        form.getName(), form.getNumber(), form.getLocation(), form.isAutoCalculationFlag());
    equipmentMapper.insertEquipment(equipment);
    return equipment;
  }

  @Override
  public void updateEquipment(int equipmentId, String name, String number, String location,
      boolean autoCalculationFlag) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    if (equipmentMapper.existsDuplicateEquipmentWithOtherId(equipmentId, name, number, location)) {
      throw new DuplicateEquipmentException(
          "同じ設備名称・設備番号・設置場所の設備が既に登録されています");
    }
    equipmentMapper.updateEquipment(equipmentId, name, number, location, autoCalculationFlag);
  }

  @Override
  public void deleteEquipment(int equipmentId) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    equipmentMapper.deleteEquipment(equipmentId);
  }
}

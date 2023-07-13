package com.example.equipment.service;

import com.example.equipment.entity.Equipment;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.mapper.EquipmentMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

  private final EquipmentMapper equipmentMapper;

  public EquipmentServiceImpl(EquipmentMapper equipmentMapper) {
    this.equipmentMapper = equipmentMapper;
  }

  @Override
  public List<Equipment> findEquipment(String name, String number, String location) {
    return equipmentMapper.findEquipment(name, number, location);
  }

  @Override
  public Equipment findEquipmentById(int equipmentId) {
    return equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
  }
}

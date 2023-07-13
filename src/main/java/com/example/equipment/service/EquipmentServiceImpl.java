package com.example.equipment.service;

import com.example.equipment.entity.Equipment;
import com.example.equipment.mapper.EquipmentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

  private EquipmentMapper equipmentMapper;

  public EquipmentServiceImpl(EquipmentMapper equipmentMapper) {
    this.equipmentMapper = equipmentMapper;
  }

  @Override
  public List<Equipment> findAll() {
    return equipmentMapper.findAll();
  }
}

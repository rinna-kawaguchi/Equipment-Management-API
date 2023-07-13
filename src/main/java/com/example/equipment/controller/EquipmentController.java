package com.example.equipment.controller;

import com.example.equipment.entity.Equipment;
import com.example.equipment.service.EquipmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EquipmentController {
  private EquipmentService equipmentService;

  public EquipmentController(EquipmentService equipmentService) {
    this.equipmentService = equipmentService;
  }

  @GetMapping("/equipment")
  public List<Equipment> getEquipments() {
    return equipmentService.findAll();
  }
}

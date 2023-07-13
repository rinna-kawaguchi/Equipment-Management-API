package com.example.equipment.controller;

import com.example.equipment.entity.Equipment;
import com.example.equipment.service.EquipmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class EquipmentController {
  private final EquipmentService equipmentService;

  public EquipmentController(EquipmentService equipmentService) {
    this.equipmentService = equipmentService;
  }

  @GetMapping("/equipment")
  public List<Equipment> getEquipments(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "number", required = false) String number,
      @RequestParam(value = "location", required = false) String location
  ) {
    return equipmentService.findEquipment(name, number, location);
  }
}

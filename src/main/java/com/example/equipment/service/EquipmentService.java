package com.example.equipment.service;

import com.example.equipment.entity.Equipment;
import com.example.equipment.form.EquipmentCreateForm;
import java.util.List;

public interface EquipmentService {

  List<Equipment> findEquipment(String name, String number, String location);

  Equipment findEquipmentById(int equipmentId);

  Equipment createEquipment(EquipmentCreateForm form);
}

package com.example.equipment.service;

import com.example.equipment.entity.Equipment;
import java.util.List;

public interface EquipmentService {

  List<Equipment> findEquipment(String name, String number, String location);
}

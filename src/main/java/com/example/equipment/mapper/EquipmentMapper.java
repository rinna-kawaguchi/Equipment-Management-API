package com.example.equipment.mapper;

import com.example.equipment.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EquipmentMapper {

  @Select("SELECT * FROM equipments WHERE name LIKE '%${name}%' AND number LIKE '%${number}%' AND"
      + " location LIKE '%${location}%'")
  List<Equipment> findEquipment(String name, String number, String location);
}

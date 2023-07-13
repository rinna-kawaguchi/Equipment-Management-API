package com.example.equipment.mapper;

import com.example.equipment.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Optional;

@Mapper
public interface EquipmentMapper {

  @Select("SELECT * FROM equipments WHERE name LIKE '%${name}%' AND number LIKE '%${number}%' AND"
      + " location LIKE '%${location}%'")
  List<Equipment> findEquipment(String name, String number, String location);

  @Select("SELECT * FROM equipments WHERE equipment_id = #{equipmentId}")
  Optional<Equipment> findEquipmentById(int equipmentId);

}

package com.example.equipment.mapper;

import com.example.equipment.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EquipmentMapper {
  @Select("SELECT * FROM equipments")
  List<Equipment> findAll();
}

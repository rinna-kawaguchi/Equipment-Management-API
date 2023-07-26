package com.example.equipment.mapper;

import com.example.equipment.controller.FindEquipmentResponse;
import com.example.equipment.entity.Equipment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import java.util.Optional;

@Mapper
public interface EquipmentMapper {

  @Select("SELECT equipments.equipment_id, name, number, location, check_type, deadline "
      + "FROM equipments JOIN plans ON equipments.equipment_id = plans.equipment_id "
      + "WHERE name LIKE '%${name}%' AND number LIKE '%${number}%' "
      + "AND location LIKE '%${location}%'")
  List<FindEquipmentResponse> findEquipment(
      String name, String number, String location);

  @Select("SELECT equipments.equipment_id, name, number, location, check_type, deadline "
      + "FROM equipments JOIN plans ON equipments.equipment_id = plans.equipment_id "
      + "WHERE name LIKE '%${name}%' AND number LIKE '%${number}%' "
      + "AND location LIKE '%${location}%' AND deadline <= #{deadline}")
  List<FindEquipmentResponse> findEquipmentByDate(
      String name, String number, String location, String deadline);

  @Select("SELECT * FROM equipments WHERE equipment_id = #{equipmentId}")
  Optional<Equipment> findEquipmentById(int equipmentId);

  @Insert("INSERT INTO equipments (name, number, location)"
      + " VALUES (#{name}, #{number}, #{location})")
  @Options(useGeneratedKeys = true, keyProperty = "equipmentId")
  void insertEquipment(Equipment equipment);

  @Update("UPDATE equipments SET name = #{name}, number = #{number}, location = #{location}"
      + " WHERE equipment_id = #{equipmentId}")
  void updateEquipment(int equipmentId, String name, String number, String location);

  @Delete("DELETE FROM equipments WHERE equipment_id = #{equipmentId}")
  void deleteEquipment(int equipmentId);
}

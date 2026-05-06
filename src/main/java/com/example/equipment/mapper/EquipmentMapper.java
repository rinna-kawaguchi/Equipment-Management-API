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

  @Select("SELECT equipments.equipment_id, equipments.name, number, location,"
      + " auto_calculation_flag, check_plan_id, check_types.name AS check_type_name, deadline "
      + "FROM equipments LEFT JOIN plans ON equipments.equipment_id = plans.equipment_id "
      + "LEFT JOIN check_types ON plans.check_type_id = check_types.check_type_id "
      + "WHERE equipments.name LIKE '%${name}%' AND number LIKE '%${number}%' "
      + "AND location LIKE '%${location}%' "
      + "ORDER BY equipments.equipment_id, deadline ASC")
  List<FindEquipmentResponse> findEquipment(
      String name, String number, String location);

  @Select("SELECT equipments.equipment_id, equipments.name, number, location,"
      + " auto_calculation_flag, check_plan_id, check_types.name AS check_type_name, deadline "
      + "FROM equipments LEFT JOIN plans ON equipments.equipment_id = plans.equipment_id "
      + "LEFT JOIN check_types ON plans.check_type_id = check_types.check_type_id "
      + "WHERE equipments.name LIKE '%${name}%' AND number LIKE '%${number}%' "
      + "AND location LIKE '%${location}%' AND deadline <= #{deadline} "
      + "ORDER BY equipments.equipment_id, deadline ASC")
  List<FindEquipmentResponse> findEquipmentByDate(
      String name, String number, String location, String deadline);

  @Select("SELECT * FROM equipments WHERE equipment_id = #{equipmentId}")
  Optional<Equipment> findEquipmentById(int equipmentId);

  @Select("SELECT EXISTS(SELECT 1 FROM equipments"
      + " WHERE name = #{name} AND number = #{number} AND location = #{location})")
  boolean existsDuplicateEquipment(String name, String number, String location);

  @Select("SELECT EXISTS(SELECT 1 FROM equipments"
      + " WHERE name = #{name} AND number = #{number} AND location = #{location}"
      + " AND equipment_id != #{equipmentId})")
  boolean existsDuplicateEquipmentWithOtherId(
      int equipmentId, String name, String number, String location);

  @Insert("INSERT INTO equipments (name, number, location, auto_calculation_flag)"
      + " VALUES (#{name}, #{number}, #{location}, #{autoCalculationFlag})")
  @Options(useGeneratedKeys = true, keyProperty = "equipmentId")
  void insertEquipment(Equipment equipment);

  @Update("UPDATE equipments SET name = #{name}, number = #{number}, location = #{location},"
      + " auto_calculation_flag = #{autoCalculationFlag} WHERE equipment_id = #{equipmentId}")
  void updateEquipment(int equipmentId, String name, String number, String location,
      boolean autoCalculationFlag);

  @Delete("DELETE FROM equipments WHERE equipment_id = #{equipmentId}")
  void deleteEquipment(int equipmentId);
}

package com.example.equipment.mapper;

import com.example.equipment.controller.FindEquipmentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface MailMapper {

  @Select("SELECT equipments.equipment_id, equipments.name, number, location,"
      + " auto_calculation_flag, check_plan_id, check_types.name AS check_type_name, deadline "
      + "FROM equipments LEFT JOIN plans ON equipments.equipment_id = plans.equipment_id "
      + "LEFT JOIN check_types ON plans.check_type_id = check_types.check_type_id "
      + "WHERE deadline IS NOT NULL")
  List<FindEquipmentResponse> findEquipmentWithDeadline();
}

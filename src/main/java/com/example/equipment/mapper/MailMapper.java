package com.example.equipment.mapper;

import com.example.equipment.controller.FindEquipmentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface MailMapper {

  @Select("SELECT equipments.equipment_id, name, number, location, "
      + "check_plan_id, check_type, deadline "
      + "FROM equipments LEFT JOIN plans ON equipments.equipment_id = plans.equipment_id "
      + "WHERE deadline IS NOT NULL")
  List<FindEquipmentResponse> findEquipmentWithDeadline();
}

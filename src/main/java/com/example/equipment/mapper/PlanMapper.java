package com.example.equipment.mapper;

import com.example.equipment.entity.Plan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PlanMapper {

  @Select("SELECT * FROM plans WHERE equipment_id = #{equipmentId}")
  List<Plan> findPlanByEquipmentId(int equipmentId);

  @Insert("INSERT INTO plans (equipment_id, check_type, period, deadline)"
      + " VALUES (#{equipmentId}, #{checkType}, #{period}, #{deadline})")
  @Options(useGeneratedKeys = true, keyProperty = "checkPlanId")
  void insertPlan(Plan plan);

}

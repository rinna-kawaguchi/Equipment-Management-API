package com.example.equipment.mapper;

import com.example.equipment.entity.Plan;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PlanMapper {

  @Results({
      @Result(column = "is_manual_override", property = "manualOverride")
  })
  @Select("SELECT * FROM plans WHERE equipment_id = #{equipmentId}")
  List<Plan> findPlanByEquipmentId(int equipmentId);

  @Results({
      @Result(column = "is_manual_override", property = "manualOverride")
  })
  @Select("SELECT * FROM plans WHERE check_plan_id = #{checkPlanId}")
  Optional<Plan> findPlanByCheckPlanId(int checkPlanId);

  @Insert("INSERT INTO plans (equipment_id, check_type_id, period_value, period_unit,"
      + " deadline, is_manual_override)"
      + " VALUES (#{equipmentId}, #{checkTypeId}, #{periodValue}, #{periodUnit},"
      + " #{deadline}, #{manualOverride})")
  @Options(useGeneratedKeys = true, keyProperty = "checkPlanId")
  void insertPlan(Plan plan);

  @Update("UPDATE plans SET check_type_id = #{checkTypeId}, period_value = #{periodValue},"
      + " period_unit = #{periodUnit}, deadline = #{deadline},"
      + " is_manual_override = #{manualOverride} WHERE check_plan_id = #{checkPlanId}")
  void updatePlan(int checkPlanId, int checkTypeId, int periodValue, String periodUnit,
      String deadline, boolean manualOverride);

  @Delete("DELETE FROM plans WHERE check_plan_id = #{checkPlanId}")
  void deletePlanByCheckPlanId(int checkPlanId);

  @Delete("DELETE FROM plans WHERE equipment_id = #{equipmentId}")
  void deletePlanByEquipmentId(int equipmentId);
}

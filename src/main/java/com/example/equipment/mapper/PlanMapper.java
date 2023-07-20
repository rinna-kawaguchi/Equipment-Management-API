package com.example.equipment.mapper;

import com.example.equipment.entity.Plan;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PlanMapper {

  @Select("SELECT * FROM plans WHERE equipment_id = #{equipmentId}")
  List<Plan> findPlanByEquipmentId(int equipmentId);

  @Select("SELECT * FROM plans WHERE check_plan_id = #{checkPlanId}")
  Optional<Plan> findPlanByCheckPlanId(int checkPlanId);

  @Insert("INSERT INTO plans (equipment_id, check_type, period, deadline)"
      + " VALUES (#{equipmentId}, #{checkType}, #{period}, #{deadline})")
  @Options(useGeneratedKeys = true, keyProperty = "checkPlanId")
  void insertPlan(Plan plan);

  @Update("UPDATE plans SET check_type = #{checkType}, period = #{period}, deadline = #{deadline}"
      + " WHERE check_plan_id = #{checkPlanId}")
  void updatePlan(int checkPlanId, String checkType, String period, String deadline);

  @Delete("DELETE FROM plans WHERE check_plan_id = #{checkPlanId}")
  void deletePlan(int checkPlanId);
}

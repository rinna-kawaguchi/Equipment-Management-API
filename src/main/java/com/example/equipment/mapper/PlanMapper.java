package com.example.equipment.mapper;

import com.example.equipment.entity.Plan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PlanMapper {
  @Select("SELECT * FROM plans WHERE equipment_id = #{equipment_id}")
  List<Plan> findPlanByEquipmentId(int equipmentId);

}

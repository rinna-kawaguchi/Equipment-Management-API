package com.example.equipment.mapper;

import com.example.equipment.entity.EquipmentCheckTypeInclusion;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import java.util.Optional;

@Mapper
public interface EquipmentCheckTypeInclusionMapper {

  @Select("SELECT * FROM equipment_check_type_inclusions WHERE equipment_id = #{equipmentId}")
  List<EquipmentCheckTypeInclusion> findInclusionsByEquipmentId(int equipmentId);

  @Select("SELECT * FROM equipment_check_type_inclusions WHERE inclusion_id = #{inclusionId}")
  Optional<EquipmentCheckTypeInclusion> findInclusionByInclusionId(int inclusionId);

  @Insert("INSERT INTO equipment_check_type_inclusions"
      + " (equipment_id, upper_check_type_id, lower_check_type_id)"
      + " VALUES (#{equipmentId}, #{upperCheckTypeId}, #{lowerCheckTypeId})")
  @Options(useGeneratedKeys = true, keyProperty = "inclusionId")
  void insertInclusion(EquipmentCheckTypeInclusion inclusion);

  @Update("UPDATE equipment_check_type_inclusions"
      + " SET upper_check_type_id = #{upperCheckTypeId},"
      + " lower_check_type_id = #{lowerCheckTypeId}"
      + " WHERE inclusion_id = #{inclusionId}")
  void updateInclusion(int inclusionId, int upperCheckTypeId, int lowerCheckTypeId);

  @Delete("DELETE FROM equipment_check_type_inclusions WHERE inclusion_id = #{inclusionId}")
  void deleteInclusionByInclusionId(int inclusionId);

  @Delete("DELETE FROM equipment_check_type_inclusions WHERE equipment_id = #{equipmentId}")
  void deleteInclusionsByEquipmentId(int equipmentId);
}

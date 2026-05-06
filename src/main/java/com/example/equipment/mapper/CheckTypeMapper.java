package com.example.equipment.mapper;

import com.example.equipment.entity.CheckType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Optional;

@Mapper
public interface CheckTypeMapper {

  @Select("SELECT * FROM check_types ORDER BY check_type_id ASC")
  List<CheckType> findAllCheckTypes();

  @Select("SELECT * FROM check_types WHERE check_type_id = #{checkTypeId}")
  Optional<CheckType> findCheckTypeById(int checkTypeId);

  @Insert("INSERT INTO check_types (name) VALUES (#{name})")
  @Options(useGeneratedKeys = true, keyProperty = "checkTypeId")
  void insertCheckType(CheckType checkType);

  @Delete("DELETE FROM check_types WHERE check_type_id = #{checkTypeId}")
  void deleteCheckTypeById(int checkTypeId);
}

package com.example.equipment.mapper;

import com.example.equipment.entity.History;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import java.util.Optional;

@Mapper
public interface HistoryMapper {

  @Select("SELECT * FROM histories WHERE equipment_id = #{equipmentId}")
  List<History> findHistoryByEquipmentId(int equipmentId);

  @Select("SELECT * FROM histories WHERE check_history_id = #{checkHistoryId}")
  Optional<History> findHistoryByCheckHistoryId(int checkHistoryId);

  @Insert("INSERT INTO histories (equipment_id, implementation_date, check_type, result)"
      + " VALUES (#{equipmentId}, #{implementationDate}, #{checkType}, #{result})")
  @Options(useGeneratedKeys = true, keyProperty = "checkHistoryId")
  void insertHistory(History history);

  @Update("UPDATE histories SET implementation_date = #{implementationDate}, check_type = "
      + "#{checkType}, result = #{result} WHERE check_history_id = #{checkHistoryId}")
  void updateHistory(int checkHistoryId, String implementationDate, String checkType,
                     String result);

  @Delete("DELETE FROM histories WHERE check_history_id = #{checkHistoryId}")
  void deleteHistoryByCheckHistoryId(int checkPlanId);

  @Delete("DELETE FROM histories WHERE equipment_id = #{equipmentId}")
  void deleteHistoryByEquipmentId(int equipmentId);
}

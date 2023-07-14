package com.example.equipment.mapper;

import com.example.equipment.entity.Equipment;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentMapperTest {

  @Autowired
  EquipmentMapper equipmentMapper;

  // name,number,locationのいずれもクエリパラメータを入力しない場合、設備が全数検索されることのテスト
  @Test
  @DataSet(value = "datasets/equipments.yml")
  @Transactional
  void 設備の全数検索ができること() {
    assertThat(equipmentMapper.findEquipment(null, null, null)).hasSize(3).contains(
        new Equipment(1, "真空ポンプA", "A1-C001A", "Area1"),
        new Equipment(2, "吸込ポンプB", "A2-C002B", "Area2"),
        new Equipment(3, "吐出ポンプC", "A3-C003C", "Area3")
    );
  }

}

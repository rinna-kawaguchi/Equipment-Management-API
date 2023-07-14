package com.example.equipment.mapper;

import com.example.equipment.entity.Equipment;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
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

  // 設備検索でname,number,locationに文字列を指定しない場合、設備が全数返されることのテスト
  @Test
  @DataSet(value = "datasets/equipments.yml")
  @Transactional
  void 全ての設備を取得できること() {
    assertThat(equipmentMapper.findEquipment(null, null, null)).hasSize(3).contains(
        new Equipment(1, "真空ポンプA", "A1-C001A", "Area1"),
        new Equipment(2, "吸込ポンプB", "A2-C002B", "Area2"),
        new Equipment(3, "吐出ポンプC", "A3-C003C", "Area3")
    );
  }

  @Test
  @DataSet(value = "datasets/equipments.yml")
  @Transactional
  void name_number_locationに指定した内容と部分一致する設備が取得できること() {
    assertThat(equipmentMapper.findEquipment("ポンプ", "C00", "1")).hasSize(1).contains(
        new Equipment(1, "真空ポンプA", "A1-C001A", "Area1")
    );
  }

  @Test
  @DataSet(value = "datasets/empty.yml")
  @Transactional
  void 設備が無い時に空のListが返されること() {
    assertThat(equipmentMapper.findEquipment(null, null, null)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/equipments.yml")
  @Transactional
  void 指定したIDの設備が取得できること() {
    assertThat(equipmentMapper.findEquipmentById(1)).contains(
        new Equipment(1, "真空ポンプA", "A1-C001A", "Area1")
    );
  }

  @Test
  @DataSet(value = "datasets/equipments.yml")
  @Transactional
  void 指定したIDの設備が存在しない時に空のOptionalが返されること() {
    assertThat(equipmentMapper.findEquipmentById(4)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/equipments.yml")
  @ExpectedDataSet(value = "datasets/insert_equipment.yml", ignoreCols = "equipment_id")
  @Transactional
  void 設備登録ができ既存のIDより大きい数字のIDが採番されること() {
    Equipment equipment = new Equipment("真空ポンプB", "A1-C001B", "Area1");
    equipmentMapper.insertEquipment(equipment);
    assertThat(equipment.getEquipmentId()).isGreaterThan(3);
  }

  @Test
  @DataSet(value = "datasets/equipments.yml")
  @ExpectedDataSet(value = "datasets/update_equipment.yml")
  @Transactional
  void 指定したIDの設備が更新できること() {
    equipmentMapper.updateEquipment(1, "真空ポンプB", "A1-C001B", "Area1");
  }

  @Test
  @DataSet(value = "datasets/equipments.yml")
  @ExpectedDataSet(value = "datasets/delete_equipment.yml")
  @Transactional
  void 指定したIDの設備が削除できること() {
    equipmentMapper.deleteEquipment(1);
  }
}

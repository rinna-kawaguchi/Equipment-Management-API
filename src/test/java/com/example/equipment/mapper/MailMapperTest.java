package com.example.equipment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.equipment.controller.FindEquipmentResponse;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MailMapperTest {

  @Autowired
  MailMapper mailMapper;

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/mail/plans_mixed_deadline.yml")
  @Transactional
  void deadlineが存在する設備の点検計画のみ取得できること() {
    assertThat(mailMapper.findEquipmentWithDeadline()).hasSize(1).contains(
        new FindEquipmentResponse(1, "真空ポンプA", "A1-C001A", "Area1", true, 1, "簡易点検",
            "2023-09-30")
    );
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml, datasets/equipment/equipments.yml,"
      + " datasets/plan/empty.yml")
  @Transactional
  void deadlineが存在しない時に空のListが返されること() {
    assertThat(mailMapper.findEquipmentWithDeadline()).isEmpty();
  }
}
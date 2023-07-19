package com.example.equipment.mapper;

import com.example.equipment.entity.Plan;
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

class PlanMapperTest {

  @Autowired
  PlanMapper planMapper;

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 指定した設備IDの点検計画が取得できること() {
    assertThat(planMapper.findPlanByEquipmentId(1)).hasSize(2).contains(
        new Plan(1, 1, "簡易点検", "1年", "2023-09-30"),
        new Plan(2, 1, "本格点検", "5年", "2026-09-30")
    );
  }

  @Test
  @DataSet(value = "datasets/plan/empty.yml")
  @Transactional
  void 点検計画が無い時に空のListが返されること() {
    assertThat(planMapper.findPlanByEquipmentId(1)).isEmpty();
  }
}
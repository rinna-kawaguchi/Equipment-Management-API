package com.example.equipment.mapper;

import com.example.equipment.entity.Plan;
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

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @Transactional
  void 指定した点検計画IDが存在しない時に空のOptionalが返されること() {
    assertThat(planMapper.findPlanByCheckPlanId(5)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/insert_plan.yml", ignoreCols = "check_plan_id")
  @Transactional
  void 点検計画の登録ができ既存のIDより大きい数字の点検計画IDが採番されること() {
    Plan plan =new Plan(1, "取替", "10年", "2030-09-30");
    planMapper.insertPlan(plan);
    assertThat(plan.getCheckPlanId()).isGreaterThan(4);
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/update_plan.yml")
  @Transactional
  void 指定したIDの点検計画が更新できること() {
    planMapper.updatePlan(2, "取替", "10年", "2030-09-30");
  }

  @Test
  @DataSet(value = "datasets/plan/plans.yml")
  @ExpectedDataSet(value = "datasets/plan/delete_plan.yml")
  @Transactional
  void 指定したIDの点検計画が削除できること() {
    planMapper.deletePlan(4);
  }
}

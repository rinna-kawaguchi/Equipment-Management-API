package com.example.equipment.mapper;

import com.example.equipment.entity.EquipmentCheckTypeInclusion;
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
class EquipmentCheckTypeInclusionMapperTest {

  @Autowired
  EquipmentCheckTypeInclusionMapper inclusionMapper;

  @Test
  @DataSet(value = "datasets/inclusion/inclusions.yml")
  @Transactional
  void 指定した設備IDの包含関係が取得できること() {
    assertThat(inclusionMapper.findInclusionsByEquipmentId(1)).hasSize(2).contains(
        new EquipmentCheckTypeInclusion(1, 1, 1, 2),
        new EquipmentCheckTypeInclusion(2, 1, 2, 3)
    );
  }

  @Test
  @DataSet(value = "datasets/inclusion/empty.yml")
  @Transactional
  void 包含関係が無い時に空のListが返されること() {
    assertThat(inclusionMapper.findInclusionsByEquipmentId(1)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/inclusion/inclusions.yml")
  @Transactional
  void 指定した包含関係IDの包含関係が取得できること() {
    assertThat(inclusionMapper.findInclusionByInclusionId(1))
        .isEqualTo(java.util.Optional.of(new EquipmentCheckTypeInclusion(1, 1, 1, 2)));
  }

  @Test
  @DataSet(value = "datasets/inclusion/inclusions.yml")
  @Transactional
  void 指定した包含関係IDが存在しない時に空のOptionalが返されること() {
    assertThat(inclusionMapper.findInclusionByInclusionId(99)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/insert_inclusion.yml", ignoreCols = "inclusion_id")
  @Transactional
  void 包含関係の登録ができ既存のIDより大きい数字の包含関係IDが採番されること() {
    EquipmentCheckTypeInclusion inclusion = new EquipmentCheckTypeInclusion(1, 1, 3);
    inclusionMapper.insertInclusion(inclusion);
    assertThat(inclusion.getInclusionId()).isGreaterThan(2);
  }

  @Test
  @DataSet(value = "datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/update_inclusion.yml")
  @Transactional
  void 指定したIDの包含関係が更新できること() {
    inclusionMapper.updateInclusion(2, 1, 3);
  }

  @Test
  @DataSet(value = "datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/delete_inclusion.yml")
  @Transactional
  void 指定したIDの包含関係が削除できること() {
    inclusionMapper.deleteInclusionByInclusionId(2);
  }

  @Test
  @DataSet(value = "datasets/inclusion/inclusions.yml")
  @ExpectedDataSet(value = "datasets/inclusion/empty.yml")
  @Transactional
  void 指定した設備IDに紐づく包含関係が削除できること() {
    inclusionMapper.deleteInclusionsByEquipmentId(1);
  }
}
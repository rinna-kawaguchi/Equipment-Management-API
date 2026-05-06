package com.example.equipment.mapper;

import com.example.equipment.entity.CheckType;
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
class CheckTypeMapperTest {

  @Autowired
  CheckTypeMapper checkTypeMapper;

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 全ての点検種別が取得できること() {
    assertThat(checkTypeMapper.findAllCheckTypes()).hasSize(3).contains(
        new CheckType(1, "簡易点検"),
        new CheckType(2, "本格点検"),
        new CheckType(3, "取替")
    );
  }

  @Test
  @DataSet(value = "datasets/check_type/empty.yml")
  @Transactional
  void 点検種別が無い時に空のListが返されること() {
    assertThat(checkTypeMapper.findAllCheckTypes()).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 指定したIDの点検種別が取得できること() {
    assertThat(checkTypeMapper.findCheckTypeById(1)).isEqualTo(java.util.Optional.of(new CheckType(1, "簡易点検")));
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @Transactional
  void 指定した点検種別IDが存在しない時に空のOptionalが返されること() {
    assertThat(checkTypeMapper.findCheckTypeById(99)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @ExpectedDataSet(value = "datasets/check_type/insert_check_type.yml", ignoreCols = "check_type_id")
  @Transactional
  void 点検種別の登録ができ既存のIDより大きい数字の点検種別IDが採番されること() {
    CheckType checkType = new CheckType("電気点検");
    checkTypeMapper.insertCheckType(checkType);
    assertThat(checkType.getCheckTypeId()).isGreaterThan(3);
  }

  @Test
  @DataSet(value = "datasets/check_type/check_types.yml")
  @ExpectedDataSet(value = "datasets/check_type/delete_check_type.yml")
  @Transactional
  void 指定したIDの点検種別が削除できること() {
    checkTypeMapper.deleteCheckTypeById(3);
  }
}
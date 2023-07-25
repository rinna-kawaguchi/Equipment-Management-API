package com.example.equipment.mapper;

import com.example.equipment.entity.History;
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
class HistoryMapperTest {

  @Autowired
  HistoryMapper historyMapper;

  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @Transactional
  void 指定した設備IDの点検履歴が取得できること() {
    assertThat(historyMapper.findHistoryByEquipmentId(1)).hasSize(2).contains(
        new History(1, 1, "2022-09-30", "簡易点検", "良"),
        new History(2, 1, "2021-09-30", "本格点検", "良")
    );
  }

  @Test
  @DataSet(value = "datasets/history/empty.yml")
  @Transactional
  void 点検履歴が無い時に空のListが返されること() {
    assertThat(historyMapper.findHistoryByEquipmentId(1)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @Transactional
  void 指定した点検履歴IDが存在しない時に空のOptionalが返されること() {
    assertThat(historyMapper.findHistoryByCheckHistoryId(5)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @ExpectedDataSet(value = "datasets/history/insert_history.yml", ignoreCols = "check_history_id")
  @Transactional
  void 点検履歴の登録ができ既存のIDより大きい数字の点検履歴IDが採番されること() {
    History history =new History(1, "2015-09-30", "取替", "良");
    historyMapper.insertHistory(history);
    assertThat(history.getCheckHistoryId()).isGreaterThan(4);
  }

  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @ExpectedDataSet(value = "datasets/history/update_history.yml")
  @Transactional
  void 指定したIDの点検履歴が更新できること() {
    historyMapper.updateHistory(2, "2015-09-30", "取替", "良");
  }

  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @ExpectedDataSet(value = "datasets/history/delete_history.yml")
  @Transactional
  void 指定したIDの点検履歴が削除できること() {
    historyMapper.deleteHistoryByCheckHistoryId(4);
  }

  @Test
  @DataSet(value = "datasets/history/histories.yml")
  @ExpectedDataSet(value = "datasets/history/delete_by_equipment_id.yml")
  @Transactional
  void 指定した設備IDに紐づく点検履歴が削除できること() {
    historyMapper.deleteHistoryByEquipmentId(2);
  }
}

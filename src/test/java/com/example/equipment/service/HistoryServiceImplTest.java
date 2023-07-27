package com.example.equipment.service;

import com.example.equipment.entity.History;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.HistoryForm;
import com.example.equipment.mapper.EquipmentMapper;
import com.example.equipment.mapper.HistoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest {

  @InjectMocks
  HistoryServiceImpl historyServiceImpl;

  @Mock
  HistoryMapper historyMapper;

  @Mock
  EquipmentMapper equipmentMapper;

  @Test
  public void 点検履歴登録の際に存在しない設備IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(equipmentMapper).findEquipmentById(99);

    HistoryForm form = new HistoryForm("2022-08-31", "取替", "良");
    History history = new History(99, form.getImplementationDate(), form.getCheckType(), form.getResult());
    assertThatThrownBy(() -> historyServiceImpl.createHistory(99, form))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(equipmentMapper, times(1)).findEquipmentById(99);
    verify(historyMapper, never()).insertHistory(history);
  }

  @Test
  public void 点検履歴更新の際に存在しない点検履歴IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(historyMapper).findHistoryByCheckHistoryId(99);

    assertThatThrownBy(() -> historyServiceImpl.updateHistory(99, "2022-08-31", "取替", "良"))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(historyMapper, times(1)).findHistoryByCheckHistoryId(99);
    verify(historyMapper, never()).updateHistory(99, "2022-08-31", "取替", "良");
  }

  @Test
  public void 点検履歴削除の際に存在しない点検計画IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(historyMapper).findHistoryByCheckHistoryId(99);

    assertThatThrownBy(() -> historyServiceImpl.deleteHistoryByCheckHistoryId(99))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(historyMapper, times(1)).findHistoryByCheckHistoryId(99);
    verify(historyMapper, never()).deleteHistoryByCheckHistoryId(99);
  }
}

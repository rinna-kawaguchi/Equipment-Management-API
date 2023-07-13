package com.example.equipment.service;

import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.mapper.EquipmentMapper;
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
class EquipmentServiceTest {
  @InjectMocks
  EquipmentServiceImpl equipmentServiceImpl;

  @Mock
  EquipmentMapper equipmentMapper;

  @Test
  public void 設備のID検索で存在しないIDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(equipmentMapper).findEquipmentById(99);

    assertThatThrownBy(() -> equipmentServiceImpl.findEquipmentById(99))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
    });
    verify(equipmentMapper, times(1)).findEquipmentById(99);
  }

  @Test
  public void 設備更新で存在しないIDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(equipmentMapper).findEquipmentById(99);

    assertThatThrownBy(() -> equipmentServiceImpl.updateEquipment(99, "真空ポンプA", "A1-C001A", "Area1"))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(equipmentMapper, times(1)).findEquipmentById(99);
    verify(equipmentMapper, never()).updateEquipment(99, "真空ポンプA", "A1-C001A", "Area1");
  }

  @Test
  public void 設備削除で存在しないIDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(equipmentMapper).findEquipmentById(99);

    assertThatThrownBy(() -> equipmentServiceImpl.deleteEquipment(99))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(equipmentMapper, times(1)).findEquipmentById(99);
    verify(equipmentMapper, never()).deleteEquipment(99);
  }

  // 設備の条件検索、ID検索、登録処理、更新処理、削除処理のテストについては、Mapperテストおよび結合テストに確認する
}

package com.example.equipment.service;

import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.EquipmentCheckTypeInclusion;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.EquipmentCheckTypeInclusionForm;
import com.example.equipment.mapper.EquipmentCheckTypeInclusionMapper;
import com.example.equipment.mapper.EquipmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// Mapperを呼び出しているだけの部分については、単体テストを割愛しMapper単体テスト及び結合テストで確認する。
@ExtendWith(MockitoExtension.class)
class EquipmentCheckTypeInclusionServiceImplTest {

  @InjectMocks
  EquipmentCheckTypeInclusionServiceImpl inclusionServiceImpl;

  @Mock
  EquipmentCheckTypeInclusionMapper inclusionMapper;

  @Mock
  EquipmentMapper equipmentMapper;

  @Test
  public void formからgetした内容で包含関係を登録できること() {
    EquipmentCheckTypeInclusionForm form = new EquipmentCheckTypeInclusionForm(1, 2);
    doReturn(Optional.of(new Equipment("真空ポンプA", "A1-C001A", "Area1", false)))
        .when(equipmentMapper).findEquipmentById(1);
    doNothing().when(inclusionMapper).insertInclusion(any());

    EquipmentCheckTypeInclusion result = inclusionServiceImpl.createInclusion(1, form);

    assertThat(result.getEquipmentId()).isEqualTo(1);
    assertThat(result.getUpperCheckTypeId()).isEqualTo(1);
    assertThat(result.getLowerCheckTypeId()).isEqualTo(2);
    verify(equipmentMapper, times(1)).findEquipmentById(1);
    verify(inclusionMapper, times(1)).insertInclusion(any());
  }

  @Test
  public void 包含関係登録の際に存在しない設備IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(equipmentMapper).findEquipmentById(99);

    EquipmentCheckTypeInclusionForm form = new EquipmentCheckTypeInclusionForm(1, 2);
    assertThatThrownBy(() -> inclusionServiceImpl.createInclusion(99, form))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(equipmentMapper, times(1)).findEquipmentById(99);
    verify(inclusionMapper, never()).insertInclusion(any());
  }

  @Test
  public void 包含関係更新の際に存在しない包含関係IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(inclusionMapper).findInclusionByInclusionId(99);

    EquipmentCheckTypeInclusionForm form = new EquipmentCheckTypeInclusionForm(1, 2);
    assertThatThrownBy(() -> inclusionServiceImpl.updateInclusion(99, form))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(inclusionMapper, times(1)).findInclusionByInclusionId(99);
    verify(inclusionMapper, never()).updateInclusion(99, 1, 2);
  }

  @Test
  public void 包含関係削除の際に存在しない包含関係IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(inclusionMapper).findInclusionByInclusionId(99);

    assertThatThrownBy(() -> inclusionServiceImpl.deleteInclusionByInclusionId(99))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(inclusionMapper, times(1)).findInclusionByInclusionId(99);
    verify(inclusionMapper, never()).deleteInclusionByInclusionId(99);
  }
}
package com.example.equipment.service;

import com.example.equipment.entity.Plan;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.PlanForm;
import com.example.equipment.mapper.EquipmentMapper;
import com.example.equipment.mapper.PlanMapper;
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

// 例外スロー以外の処理はmapperを呼び出しているだけのため、単体テストは割愛し、mapper単体テスト及び結合テストで確認する。

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

  @InjectMocks
  PlanServiceImpl planserviceImpl;

  @Mock
  PlanMapper planMapper;

  @Mock
  EquipmentMapper equipmentMapper;

  @Test
  public void 点検計画登録の際に存在しない設備IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(equipmentMapper).findEquipmentById(99);

    PlanForm form = new PlanForm("簡易点検", "1年", "2023-09-30");
    Plan plan = new Plan(99, form.getCheckType(), form.getPeriod(), form.getDeadline());
    assertThatThrownBy(() -> planserviceImpl.createPlan(99, form))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(equipmentMapper, times(1)).findEquipmentById(99);
    verify(planMapper, never()).insertPlan(plan);
  }

  @Test
  public void 点検計画更新の際に存在しない点検計画IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(planMapper).findPlanByCheckPlanId(99);

    assertThatThrownBy(() -> planserviceImpl.updatePlan(99, "簡易点検", "1年", "2023-09-30"))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(planMapper, times(1)).findPlanByCheckPlanId(99);
    verify(planMapper, never()).updatePlan(99, "簡易点検", "1年", "2023-09-30");
  }

  @Test
  public void 点検計画削除の際に存在しない点検計画IDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(planMapper).findPlanByCheckPlanId(99);

    assertThatThrownBy(() -> planserviceImpl.deletePlanByCheckPlanId(99))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(planMapper, times(1)).findPlanByCheckPlanId(99);
    verify(planMapper, never()).deletePlanByCheckPlanId(99);
  }
}

package com.example.equipment.service;

import com.example.equipment.entity.CheckType;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.CheckTypeForm;
import com.example.equipment.mapper.CheckTypeMapper;
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
class CheckTypeServiceImplTest {

  @InjectMocks
  CheckTypeServiceImpl checkTypeServiceImpl;

  @Mock
  CheckTypeMapper checkTypeMapper;

  @Test
  public void formからgetした内容で点検種別を登録できること() {
    CheckTypeForm form = new CheckTypeForm("電気点検");
    doNothing().when(checkTypeMapper).insertCheckType(any());

    CheckType result = checkTypeServiceImpl.createCheckType(form);

    assertThat(result.getName()).isEqualTo("電気点検");
    verify(checkTypeMapper, times(1)).insertCheckType(any());
  }

  @Test
  public void 点検種別削除の際に存在しないIDを指定した時に例外がスローされること() {
    doReturn(Optional.empty()).when(checkTypeMapper).findCheckTypeById(99);

    assertThatThrownBy(() -> checkTypeServiceImpl.deleteCheckType(99))
        .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Not Found");
        });
    verify(checkTypeMapper, times(1)).findCheckTypeById(99);
    verify(checkTypeMapper, never()).deleteCheckTypeById(99);
  }
}
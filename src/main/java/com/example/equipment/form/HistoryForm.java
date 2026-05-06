package com.example.equipment.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HistoryForm {

  @NotBlank(message = "必須項目です")
  private String implementationDate;

  @Min(value = 1, message = "1以上の値を入力してください")
  private int checkTypeId;

  @Size(max = 50, message = "50文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String result;
}

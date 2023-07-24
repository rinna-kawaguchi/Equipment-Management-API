package com.example.equipment.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HistoryForm {

  @NotBlank(message = "必須項目です")
  private String implementationDate;

  @Size(max = 10, message = "10文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String checkType;

  @Size(max = 50, message = "10文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String result;
}

package com.example.equipment.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanForm {

  @Size(max = 10, message = "10文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String checkType;

  @Size(max = 10, message = "10文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String period;

  private String deadline;
}

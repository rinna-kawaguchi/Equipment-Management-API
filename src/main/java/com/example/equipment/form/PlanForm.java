package com.example.equipment.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PlanForm {

  @Size(max = 10, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String checkType;

  @Size(max = 10, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String period;

  private String deadline;
}

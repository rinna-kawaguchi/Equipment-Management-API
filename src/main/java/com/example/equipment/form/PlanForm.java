package com.example.equipment.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlanForm {

  @Min(value = 1, message = "1以上の値を入力してください")
  private int checkTypeId;

  @Min(value = 1, message = "1以上の値を入力してください")
  private int periodValue;

  @Pattern(regexp = "day|week|month|year", message = "day,week,month,yearのいずれかを入力してください")
  @NotBlank(message = "必須項目です")
  private String periodUnit;

  private String deadline;
}

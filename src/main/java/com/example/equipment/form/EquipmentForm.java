package com.example.equipment.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentForm {

  @Size(max = 20, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String name;

  @Size(max = 20, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String number;

  @Size(max = 20, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String location;
}

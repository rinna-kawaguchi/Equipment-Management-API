package com.example.equipment.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class EquipmentCreateForm {

  @Size(max = 20, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String name;

  @Size(max = 20, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  @NotBlank
  private String number;

  @Size(max = 20, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  @NotBlank
  private String location;
}

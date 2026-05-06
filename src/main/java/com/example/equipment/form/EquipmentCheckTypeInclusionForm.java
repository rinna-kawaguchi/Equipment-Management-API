package com.example.equipment.form;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentCheckTypeInclusionForm {

  @Min(value = 1, message = "1以上の値を入力してください")
  private int upperCheckTypeId;

  @Min(value = 1, message = "1以上の値を入力してください")
  private int lowerCheckTypeId;
}

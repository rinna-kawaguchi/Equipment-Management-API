package com.example.equipment.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CheckTypeForm {

  @Size(max = 20, message = "20文字以内で入力してください")
  @NotBlank(message = "必須項目です")
  private String name;

  @JsonCreator
  public CheckTypeForm(@JsonProperty("name") String name) {
    this.name = name;
  }
}

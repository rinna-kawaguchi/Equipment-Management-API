package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor

public class CheckType {

  private int checkTypeId;
  private String name;

  public CheckType(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckType checkType = (CheckType) o;
    return checkTypeId == checkType.checkTypeId
        && Objects.equals(name, checkType.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checkTypeId, name);
  }
}

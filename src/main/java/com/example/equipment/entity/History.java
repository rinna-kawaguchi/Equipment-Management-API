package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor

public class History {

  private int checkHistoryId;
  private int equipmentId;
  private int checkTypeId;
  private String implementationDate;
  private String result;

  public History(int equipmentId, int checkTypeId, String implementationDate, String result) {
    this.equipmentId = equipmentId;
    this.checkTypeId = checkTypeId;
    this.implementationDate = implementationDate;
    this.result = result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    History history = (History) o;
    return checkHistoryId == history.checkHistoryId
        && equipmentId == history.equipmentId
        && checkTypeId == history.checkTypeId
        && Objects.equals(implementationDate, history.implementationDate)
        && Objects.equals(result, history.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checkHistoryId, equipmentId, checkTypeId, implementationDate, result);
  }
}

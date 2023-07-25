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
  private String implementationDate;
  private String checkType;
  private String result;

  public History(int equipmentId, String implementationDate, String checkType, String result) {
    this.equipmentId = equipmentId;
    this.implementationDate = implementationDate;
    this.checkType = checkType;
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
        && Objects.equals(implementationDate, history.implementationDate)
        && Objects.equals(checkType, history.checkType)
        && Objects.equals(result, history.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checkHistoryId, equipmentId, implementationDate, checkType, result);
  }
}

package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
}

package com.example.equipment.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class FindEquipmentResponse {

  private int equipmentId;
  private String name;
  private String number;
  private String location;
  private Integer checkPlanId;
  private String checkType;
  private String deadline;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FindEquipmentResponse that = (FindEquipmentResponse) o;
    return equipmentId == that.equipmentId
        && Objects.equals(name, that.name)
        && Objects.equals(number, that.number)
        && Objects.equals(location, that.location)
        && Objects.equals(checkType, that.checkType)
        && Objects.equals(deadline, that.deadline);
  }

  @Override
  public int hashCode() {
    return Objects.hash(equipmentId, name, number, location, checkType, deadline);
  }
}

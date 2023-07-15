package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor

public class Equipment {

  private int equipmentId;
  private String name;
  private String number;
  private String location;

  public Equipment(String name, String number, String location) {
    this.name = name;
    this.number = number;
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Equipment equipment = (Equipment) o;
    return equipmentId == equipment.equipmentId
        && Objects.equals(name, equipment.name)
        && Objects.equals(number, equipment.number)
        && Objects.equals(location, equipment.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(equipmentId, name, number, location);
  }
}

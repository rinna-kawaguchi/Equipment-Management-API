package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor

public class EquipmentCheckTypeInclusion {

  private int inclusionId;
  private int equipmentId;
  private int upperCheckTypeId;
  private int lowerCheckTypeId;

  public EquipmentCheckTypeInclusion(
      int equipmentId, int upperCheckTypeId, int lowerCheckTypeId) {
    this.equipmentId = equipmentId;
    this.upperCheckTypeId = upperCheckTypeId;
    this.lowerCheckTypeId = lowerCheckTypeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EquipmentCheckTypeInclusion that = (EquipmentCheckTypeInclusion) o;
    return inclusionId == that.inclusionId
        && equipmentId == that.equipmentId
        && upperCheckTypeId == that.upperCheckTypeId
        && lowerCheckTypeId == that.lowerCheckTypeId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(inclusionId, equipmentId, upperCheckTypeId, lowerCheckTypeId);
  }
}

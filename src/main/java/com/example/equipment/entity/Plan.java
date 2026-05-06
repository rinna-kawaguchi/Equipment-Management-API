package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor

public class Plan {

  private int checkPlanId;
  private int equipmentId;
  private int checkTypeId;
  private int periodValue;
  private String periodUnit;
  private String deadline;
  private boolean manualOverride;

  public Plan(int equipmentId, int checkTypeId, int periodValue, String periodUnit,
      String deadline, boolean manualOverride) {
    this.equipmentId = equipmentId;
    this.checkTypeId = checkTypeId;
    this.periodValue = periodValue;
    this.periodUnit = periodUnit;
    this.deadline = deadline;
    this.manualOverride = manualOverride;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Plan plan = (Plan) o;
    return checkPlanId == plan.checkPlanId
        && equipmentId == plan.equipmentId
        && checkTypeId == plan.checkTypeId
        && periodValue == plan.periodValue
        && Objects.equals(periodUnit, plan.periodUnit)
        && Objects.equals(deadline, plan.deadline)
        && manualOverride == plan.manualOverride;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        checkPlanId, equipmentId, checkTypeId, periodValue, periodUnit, deadline, manualOverride);
  }
}

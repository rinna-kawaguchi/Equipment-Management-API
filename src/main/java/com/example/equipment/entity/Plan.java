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
  private String checkType;
  private String period;
  private String deadline;

  public Plan(int equipmentId, String checkType, String period, String deadline) {
    this.equipmentId = equipmentId;
    this.checkType = checkType;
    this.period = period;
    this.deadline = deadline;
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
        && Objects.equals(checkType, plan.checkType)
        && Objects.equals(period, plan.period)
        && Objects.equals(deadline, plan.deadline);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checkPlanId, equipmentId, checkType, period, deadline);
  }
}

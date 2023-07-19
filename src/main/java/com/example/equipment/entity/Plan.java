package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Plan {

  private int checkPlanId;
  private int equipmentId;
  private String checkType;
  private String period;
  private String deadline;
}

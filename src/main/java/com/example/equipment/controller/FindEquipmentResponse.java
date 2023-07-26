package com.example.equipment.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindEquipmentResponse {

  private int equipmentId;
  private String name;
  private String number;
  private String location;
  private String checkType;
  private String deadline;
}

package com.example.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
}

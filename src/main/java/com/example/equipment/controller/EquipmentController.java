package com.example.equipment.controller;

import com.example.equipment.entity.Equipment;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.service.EquipmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class EquipmentController {
  private final EquipmentService equipmentService;

  public EquipmentController(EquipmentService equipmentService) {
    this.equipmentService = equipmentService;
  }

  @GetMapping("/equipment")
  public List<Equipment> getEquipments(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "number", required = false) String number,
      @RequestParam(value = "location", required = false) String location
  ) {
    return equipmentService.findEquipment(name, number, location);
  }

  @GetMapping("/equipment/{equipmentId}")
  public Equipment getEquipmentById(@PathVariable("equipmentId") int equipmentId) {
    return equipmentService.findEquipmentById(equipmentId);
  }

  @ExceptionHandler(value = ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNoResourceFound(
      ResourceNotFoundException e, HttpServletRequest request) {
    Map<String, String> body = Map.of(
        "timestamp", ZonedDateTime.now().toString(),
        "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
        "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
        "message", e.getMessage(),
        "path", request.getRequestURI());
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }
}

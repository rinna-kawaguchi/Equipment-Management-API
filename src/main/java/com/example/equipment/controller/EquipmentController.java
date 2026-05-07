package com.example.equipment.controller;

import com.example.equipment.entity.Equipment;
import com.example.equipment.form.EquipmentForm;
import com.example.equipment.service.EquipmentService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class EquipmentController {

  private final EquipmentService equipmentService;

  public EquipmentController(EquipmentService equipmentService) {
    this.equipmentService = equipmentService;
  }

  @GetMapping("/equipments")
  public List<FindEquipmentResponse> getEquipments(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "number", required = false) String number,
      @RequestParam(value = "location", required = false) String location,
      @RequestParam(value = "deadline", required = false) String deadline
  ) {
    return equipmentService.findEquipment(name, number, location, deadline);
  }

  @GetMapping("/equipments/{equipmentId}")
  public Equipment getEquipmentById(@PathVariable("equipmentId") int equipmentId) {
    return equipmentService.findEquipmentById(equipmentId);
  }

  @PostMapping("/equipments")
  public ResponseEntity<Map<String, String>> createEquipment(
      @RequestBody @Validated EquipmentForm form, UriComponentsBuilder uriBuilder) {
    Equipment equipment = equipmentService.createEquipment(form);
    URI url = uriBuilder.path("/equipment/" + equipment.getEquipmentId()).build().toUri();
    String newId = String.valueOf(equipment.getEquipmentId());
    return ResponseEntity.created(url).body(
        Map.of("message", "設備が正常に登録されました", "newId", newId));
  }

  @PatchMapping("/equipments/{equipmentId}")
  public ResponseEntity<Map<String, String>> updateEquipment(
      @PathVariable("equipmentId") int equipmentId, @RequestBody @Validated EquipmentForm form) {
    equipmentService.updateEquipment(equipmentId, form.getName(), form.getNumber(),
        form.getLocation(), form.isAutoCalculationFlag());
    return ResponseEntity.ok(Map.of("message", "設備が正常に更新されました"));
  }

  @DeleteMapping("/equipments/{equipmentId}")
  public ResponseEntity<Map<String, String>> deleteEquipment(
      @PathVariable("equipmentId") int equipmentId) {
    equipmentService.deleteEquipment(equipmentId);
    return ResponseEntity.ok(Map.of("message", "設備が正常に削除されました"));
  }
}

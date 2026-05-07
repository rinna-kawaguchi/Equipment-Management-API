package com.example.equipment.controller;

import com.example.equipment.entity.EquipmentCheckTypeInclusion;
import com.example.equipment.form.EquipmentCheckTypeInclusionForm;
import com.example.equipment.service.EquipmentCheckTypeInclusionService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class EquipmentCheckTypeInclusionController {

  private final EquipmentCheckTypeInclusionService inclusionService;

  public EquipmentCheckTypeInclusionController(
      EquipmentCheckTypeInclusionService inclusionService) {
    this.inclusionService = inclusionService;
  }

  @GetMapping("/equipments/{equipmentId}/inclusions")
  public List<EquipmentCheckTypeInclusion> getInclusionsByEquipmentId(
      @PathVariable("equipmentId") int equipmentId) {
    return inclusionService.findInclusionsByEquipmentId(equipmentId);
  }

  @PostMapping("/equipments/{equipmentId}/inclusions")
  public ResponseEntity<Map<String, String>> createInclusion(
      @PathVariable("equipmentId") int equipmentId,
      @RequestBody @Validated EquipmentCheckTypeInclusionForm form,
      UriComponentsBuilder uriBuilder) {
    EquipmentCheckTypeInclusion inclusion = inclusionService.createInclusion(equipmentId, form);
    URI url = uriBuilder
        .path("/equipments/" + equipmentId + "/inclusions/" + inclusion.getInclusionId())
        .build().toUri();
    return ResponseEntity.created(url).body(Map.of("message", "包含関係が正常に登録されました"));
  }

  @PatchMapping("/equipments/{equipmentId}/inclusions/{inclusionId}")
  public ResponseEntity<Map<String, String>> updateInclusion(
      @PathVariable("equipmentId") int equipmentId,
      @PathVariable("inclusionId") int inclusionId,
      @RequestBody @Validated EquipmentCheckTypeInclusionForm form) {
    inclusionService.updateInclusion(inclusionId, form);
    return ResponseEntity.ok(Map.of("message", "包含関係が正常に更新されました"));
  }

  @DeleteMapping("/equipments/{equipmentId}/inclusions/{inclusionId}")
  public ResponseEntity<Map<String, String>> deleteInclusion(
      @PathVariable("equipmentId") int equipmentId,
      @PathVariable("inclusionId") int inclusionId) {
    inclusionService.deleteInclusionByInclusionId(inclusionId);
    return ResponseEntity.ok(Map.of("message", "包含関係が正常に削除されました"));
  }

  @DeleteMapping("/equipments/{equipmentId}/inclusions")
  public ResponseEntity<Map<String, String>> deleteInclusionsByEquipmentId(
      @PathVariable("equipmentId") int equipmentId) {
    inclusionService.deleteInclusionsByEquipmentId(equipmentId);
    return ResponseEntity.ok(Map.of("message", "包含関係が正常に削除されました"));
  }
}

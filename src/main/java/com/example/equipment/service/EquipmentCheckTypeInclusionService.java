package com.example.equipment.service;

import com.example.equipment.entity.EquipmentCheckTypeInclusion;
import com.example.equipment.form.EquipmentCheckTypeInclusionForm;
import java.util.List;

public interface EquipmentCheckTypeInclusionService {

  List<EquipmentCheckTypeInclusion> findInclusionsByEquipmentId(int equipmentId);

  EquipmentCheckTypeInclusion createInclusion(int equipmentId,
      EquipmentCheckTypeInclusionForm form);

  void updateInclusion(int inclusionId, EquipmentCheckTypeInclusionForm form);

  void deleteInclusionByInclusionId(int inclusionId);

  void deleteInclusionsByEquipmentId(int equipmentId);
}

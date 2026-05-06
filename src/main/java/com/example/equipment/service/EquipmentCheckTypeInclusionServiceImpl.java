package com.example.equipment.service;

import com.example.equipment.entity.EquipmentCheckTypeInclusion;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.EquipmentCheckTypeInclusionForm;
import com.example.equipment.mapper.EquipmentCheckTypeInclusionMapper;
import com.example.equipment.mapper.EquipmentMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EquipmentCheckTypeInclusionServiceImpl implements EquipmentCheckTypeInclusionService {

  private final EquipmentCheckTypeInclusionMapper inclusionMapper;
  private final EquipmentMapper equipmentMapper;

  public EquipmentCheckTypeInclusionServiceImpl(
      EquipmentCheckTypeInclusionMapper inclusionMapper, EquipmentMapper equipmentMapper) {
    this.inclusionMapper = inclusionMapper;
    this.equipmentMapper = equipmentMapper;
  }

  @Override
  public List<EquipmentCheckTypeInclusion> findInclusionsByEquipmentId(int equipmentId) {
    return inclusionMapper.findInclusionsByEquipmentId(equipmentId);
  }

  @Override
  public EquipmentCheckTypeInclusion createInclusion(int equipmentId,
      EquipmentCheckTypeInclusionForm form) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    EquipmentCheckTypeInclusion inclusion = new EquipmentCheckTypeInclusion(
        equipmentId, form.getUpperCheckTypeId(), form.getLowerCheckTypeId());
    inclusionMapper.insertInclusion(inclusion);
    return inclusion;
  }

  @Override
  public void updateInclusion(int inclusionId, EquipmentCheckTypeInclusionForm form) {
    inclusionMapper.findInclusionByInclusionId(inclusionId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    inclusionMapper.updateInclusion(inclusionId, form.getUpperCheckTypeId(),
        form.getLowerCheckTypeId());
  }

  @Override
  public void deleteInclusionByInclusionId(int inclusionId) {
    inclusionMapper.findInclusionByInclusionId(inclusionId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    inclusionMapper.deleteInclusionByInclusionId(inclusionId);
  }

  @Override
  public void deleteInclusionsByEquipmentId(int equipmentId) {
    inclusionMapper.deleteInclusionsByEquipmentId(equipmentId);
  }
}

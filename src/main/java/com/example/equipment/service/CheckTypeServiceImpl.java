package com.example.equipment.service;

import com.example.equipment.entity.CheckType;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.CheckTypeForm;
import com.example.equipment.mapper.CheckTypeMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CheckTypeServiceImpl implements CheckTypeService {

  private final CheckTypeMapper checkTypeMapper;

  public CheckTypeServiceImpl(CheckTypeMapper checkTypeMapper) {
    this.checkTypeMapper = checkTypeMapper;
  }

  @Override
  public List<CheckType> findAllCheckTypes() {
    return checkTypeMapper.findAllCheckTypes();
  }

  @Override
  public CheckType createCheckType(CheckTypeForm form) {
    CheckType checkType = new CheckType(form.getName());
    checkTypeMapper.insertCheckType(checkType);
    return checkType;
  }

  @Override
  public void deleteCheckType(int checkTypeId) {
    checkTypeMapper.findCheckTypeById(checkTypeId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    checkTypeMapper.deleteCheckTypeById(checkTypeId);
  }
}

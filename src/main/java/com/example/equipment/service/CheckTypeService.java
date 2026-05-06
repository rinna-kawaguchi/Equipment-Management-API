package com.example.equipment.service;

import com.example.equipment.entity.CheckType;
import com.example.equipment.form.CheckTypeForm;
import java.util.List;

public interface CheckTypeService {

  List<CheckType> findAllCheckTypes();

  CheckType createCheckType(CheckTypeForm form);

  void deleteCheckType(int checkTypeId);
}

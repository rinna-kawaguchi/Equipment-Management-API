package com.example.equipment.service;

import com.example.equipment.entity.History;
import com.example.equipment.form.HistoryForm;

import java.util.List;

public interface HistoryService {

  List<History> findHistoryByEquipmentId(int equipmentId);

  History createHistory(int equipmentId, HistoryForm form);

  void updateHistory(int checkHistoryId, String implementationDate, String checkType,
                     String result);

  void deleteHistoryByCheckHistoryId(int checkHistoryId);

  void deleteHistoryByEquipmentId(int equipmentId);
}

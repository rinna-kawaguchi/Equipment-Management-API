package com.example.equipment.service;

import com.example.equipment.entity.History;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.form.HistoryForm;
import com.example.equipment.mapper.EquipmentMapper;
import com.example.equipment.mapper.HistoryMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

  private final HistoryMapper historyMapper;
  private final EquipmentMapper equipmentMapper;

  public HistoryServiceImpl(HistoryMapper hisotryMapper, EquipmentMapper equipmentMapper) {
    this.historyMapper = hisotryMapper;
    this.equipmentMapper = equipmentMapper;
  }

  // 指定した設備IDが存在しない場合は例外をスローする
  @Override
   public List<History> findHistoryByEquipmentId(int equipmentId) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    return historyMapper.findHistoryByEquipmentId(equipmentId);
  }

  // 指定した設備IDが存在しない場合は例外をスローする
  @Override
  public History createHistory(int equipmentId, HistoryForm form) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    History history = new History(equipmentId, form.getImplementationDate(), form.getCheckType(),
        form.getResult());
    historyMapper.insertHistory(history);
    return history;
  }

  @Override
  public void updateHistory(
      int checkHistoryId, String implementationDate, String checkType, String result) {
    historyMapper.findHistoryByCheckHistoryId(checkHistoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    historyMapper.updateHistory(checkHistoryId, implementationDate, checkType, result);
  }

  @Override
  public void deleteHistoryByCheckHistoryId(int checkHistoryId) {
    historyMapper.findHistoryByCheckHistoryId(checkHistoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    historyMapper.deleteHistoryByCheckHistoryId(checkHistoryId);
  }

  @Override
  public void deleteHistoryByEquipmentId(int equipmentId) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    historyMapper.deleteHistoryByEquipmentId(equipmentId);
  }
}

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

  public HistoryServiceImpl(HistoryMapper historyMapper, EquipmentMapper equipmentMapper) {
    this.historyMapper = historyMapper;
    this.equipmentMapper = equipmentMapper;
  }

  // 指定した設備IDの点検履歴を取得するMapperを呼び出す。設備IDが存在しない場合は例外をスローする
  @Override
   public List<History> findHistoryByEquipmentId(int equipmentId) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    return historyMapper.findHistoryByEquipmentId(equipmentId);
  }

  // formからgetしたリクエスト内容で、指定した設備IDの点検履歴を登録するMapperを呼び出す。
  // 設備IDが存在しない場合は例外をスローする
  @Override
  public History createHistory(int equipmentId, HistoryForm form) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

    History history = new History(equipmentId, form.getImplementationDate(), form.getCheckType(),
        form.getResult());
    historyMapper.insertHistory(history);
    return history;
  }

  // formでリクエストされた内容で、指定したIDの点検履歴を更新するMapperを呼び出す。
  // 指定した点検履歴IDが存在しない場合は例外をスローする
  @Override
  public void updateHistory(
      int checkHistoryId, String implementationDate, String checkType, String result) {
    historyMapper.findHistoryByCheckHistoryId(checkHistoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    historyMapper.updateHistory(checkHistoryId, implementationDate, checkType, result);
  }

  // 指定したIDの点検履歴を削除するMapperを呼び出す。点検履歴IDが存在しない場合は例外をスローする
  @Override
  public void deleteHistoryByCheckHistoryId(int checkHistoryId) {
    historyMapper.findHistoryByCheckHistoryId(checkHistoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    historyMapper.deleteHistoryByCheckHistoryId(checkHistoryId);
  }

  // 指定した設備IDに紐づく点検履歴を削除するMapperを呼びだす。設備IDが存在しない場合は例外をスローする
  @Override
  public void deleteHistoryByEquipmentId(int equipmentId) {
    equipmentMapper.findEquipmentById(equipmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    historyMapper.deleteHistoryByEquipmentId(equipmentId);
  }
}

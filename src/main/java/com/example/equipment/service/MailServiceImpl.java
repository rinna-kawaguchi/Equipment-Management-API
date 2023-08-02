package com.example.equipment.service;

import com.example.equipment.controller.FindEquipmentResponse;
import com.example.equipment.mapper.MailMapper;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

  private final MailMapper mailMapper;
  private final MailSender mailSender;

  public MailServiceImpl(MailMapper mailMapper, MailSender mailSender) {
    this.mailMapper = mailMapper;
    this.mailSender = mailSender;
  }

  @Override
  public void mailSend() {
    List<FindEquipmentResponse> equipmentsWithDeadline = mailMapper.findEquipmentWithDeadline();

    // 現在から１ヶ月後の日付を取得
    LocalDate now = LocalDate.now();
    LocalDate oneMonthLater = now.plusMonths(1);

    // メールの宛先と件名を設定
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo("kwrn.study@gmail.com");
    msg.setSubject("[notification] 設備の点検期限が近づいています");

    for (FindEquipmentResponse findEquipmentResponse : equipmentsWithDeadline) {
      // 点検期限が現在から１ヶ月後の場合はメールを送付する
      if (LocalDate.parse(findEquipmentResponse.getDeadline()).isEqual(oneMonthLater)) {
        // メールの本文を設定
        msg.setText("次の設備の点検期限が１ヶ月後に迫っています。ご注意ください。 "
            + "\n設備名称：" + findEquipmentResponse.getName()
            + "\n設備番号：" + findEquipmentResponse.getNumber()
            + "\n設置場所：" + findEquipmentResponse.getLocation()
            + "\n点検種別：" + findEquipmentResponse.getCheckType()
            + "\n点検期限：" + findEquipmentResponse.getDeadline());
        mailSender.send(msg);
      }
    }
  }
}

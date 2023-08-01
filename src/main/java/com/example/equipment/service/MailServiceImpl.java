package com.example.equipment.service;

import com.example.equipment.controller.FindEquipmentResponse;
import com.example.equipment.mapper.MailMapper;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
  public void mailSend() throws ParseException {
    List<FindEquipmentResponse> equipmentsWithDeadline = mailMapper.findEquipmentWithDeadline();

    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date targetDate = sdFormat.parse("2023-10-01");

    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo("rin3810400h@gmail.com");
    msg.setSubject("[notification] 設備の点検期限が近づいています");

    for (FindEquipmentResponse findEquipmentResponse : equipmentsWithDeadline) {
      if (sdFormat.parse(findEquipmentResponse.getDeadline()).before(targetDate)) {
        msg.setText("次の設備の点検期限が近づいているのでご注意ください。 "
            + "\n設備名称：" + findEquipmentResponse.getName()
            + "\n設備番号：" + findEquipmentResponse.getNumber()
            + "\n設置場所：" + findEquipmentResponse.getLocation()
            + "\n点検期限：" + findEquipmentResponse.getDeadline());
        mailSender.send(msg);
      }
    }
  }
}

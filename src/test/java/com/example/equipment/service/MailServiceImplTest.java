package com.example.equipment.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.equipment.controller.FindEquipmentResponse;
import com.example.equipment.mapper.MailMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

  @InjectMocks
  MailServiceImpl mailServiceImpl;

  @Mock
  MailMapper mailMapper;

  @Mock
  MailSender mailSender;

  @Test
  void deadlineが1ヶ月後の場合にメールが送信されること() {
    String deadline = LocalDate.now().plusMonths(1).toString();
    FindEquipmentResponse equipment =
        new FindEquipmentResponse(1, "真空ポンプA", "A1-C001A", "Area1", true, 1, "簡易点検", deadline);
    doReturn(List.of(equipment)).when(mailMapper).findEquipmentWithDeadline();

    mailServiceImpl.mailSend();

    verify(mailSender).send(any(SimpleMailMessage.class));
  }

  @Test
  void deadlineが1ヶ月後でない場合にメールが送信されないこと() {
    String deadline = LocalDate.now().plusMonths(2).toString();
    FindEquipmentResponse equipment =
        new FindEquipmentResponse(1, "真空ポンプA", "A1-C001A", "Area1", true, 1, "簡易点検", deadline);
    doReturn(List.of(equipment)).when(mailMapper).findEquipmentWithDeadline();

    mailServiceImpl.mailSend();

    verify(mailSender, never()).send(any(SimpleMailMessage.class));
  }
}

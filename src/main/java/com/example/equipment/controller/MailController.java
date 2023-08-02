package com.example.equipment.controller;

import com.example.equipment.service.MailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@RestController
public class MailController {

  private final MailService mailService;

  public MailController(MailService mailService) {
    this.mailService = mailService;
  }

  // 1日おきに実行する
  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
  @GetMapping("/mail")
  public void write1() throws ParseException {
    mailService.mailSend();
  }
}

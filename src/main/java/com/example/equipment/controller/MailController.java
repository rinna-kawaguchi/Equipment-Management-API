package com.example.equipment.controller;

import com.example.equipment.service.MailService;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class MailController {

  private final MailSender mailSender;
  private final MailService mailService;

  public MailController(MailSender mailSender, MailService mailService) {
    this.mailSender = mailSender;
    this.mailService = mailService;
  }

  @GetMapping("/mail")
  public String write1() throws ParseException {

    mailService.mailSend();
    return "hello";
  }
}

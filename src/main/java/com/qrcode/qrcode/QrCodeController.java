package com.qrcode.qrcode;

import com.google.zxing.WriterException;
import com.qrcode.qrcode.Service.QrCodeService;
import com.qrcode.qrcode.model.Disciplina;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController
public class QrCodeController {
  @Autowired
  private QrCodeService qrCodeService;

  @PostMapping("/createNewQrCode")
  public String createNewQrCode(@ModelAttribute("request") Disciplina request)
      throws WriterException, IOException, NoSuchAlgorithmException {
    Random r = new Random();

    request.setCdDisciplina(String.valueOf(r.nextInt(80000)));
    request.setTurma("154898");
    request.setQtCredito("4");
    request.setCdProfessor("085439");

    return qrCodeService.writeQR(request);
  }
}

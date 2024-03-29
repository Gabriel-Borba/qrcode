package com.qrcode.qrcode.Service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qrcode.qrcode.model.Disciplina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Controller
public class QrCodeService {

  @Autowired
  private ResourceLoader resourceLoader;

  @PostMapping("/create")
  public String createNewAccount(@ModelAttribute("request") Disciplina request, Model model)
      throws WriterException, IOException {
    String qrCodePath = writeQR(request);
    model.addAttribute("code", qrCodePath);
    return "QRcode";
  }

  @GetMapping("/readQR")
  public String verifyQR(@RequestParam("qrImage") String qrImage, Model model) throws Exception {
    model.addAttribute("content", readQR(qrImage));
    model.addAttribute("code", qrImage);
    return "QRcode";

  }

  private String writeQR(Disciplina request) throws WriterException, IOException {
    String qcodePath = "src/main/resources/static/images/" + request.getCdDisciplina() + "-QRCode.png";
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(request.getCdDisciplina() + "\n" + request.getQtCredito() + "\n"
        + request.getQtCredito() + "\n" + request.getCdProfessor(), BarcodeFormat.QR_CODE, 350, 350);
    Path path = FileSystems.getDefault().getPath(qcodePath);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    return "/images/" + request.getCdDisciplina() + "-QRCode.png";
  }

  private String readQR(String qrImage) throws Exception {
    final Resource fileResource = resourceLoader.getResource("classpath:static/" + qrImage);
    File QRfile = fileResource.getFile();
    BufferedImage bufferedImg = ImageIO.read(QRfile);
    LuminanceSource source = new BufferedImageLuminanceSource(bufferedImg);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    Result result = new MultiFormatReader().decode(bitmap);
    System.out.println("Barcode Format: " + result.getBarcodeFormat());
    System.out.println("Content: " + result.getText());
    return result.getText();

  }
}

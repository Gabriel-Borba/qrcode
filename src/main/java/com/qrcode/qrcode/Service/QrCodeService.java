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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Controller
public class QrCodeService {

  @Autowired
  private ResourceLoader resourceLoader;

  @PostMapping("/create")
  public String createNewAccount(@ModelAttribute("request") Disciplina request, Model model)
      throws WriterException, IOException, NoSuchAlgorithmException {
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

  public String writeQR(Disciplina request) throws WriterException, IOException, NoSuchAlgorithmException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    BitMatrix bitMatrix = qrCodeWriter.encode(createHash(request.toString()) + request.getCdDisciplina() + "\n" + request.getQtCredito() + "\n"
        + request.getTurma() + "\n" + request.getCdProfessor(), BarcodeFormat.QR_CODE, 350, 350);
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);
    stream.flush();
    byte[] baseEncoded = Base64.getEncoder().encode(stream.toByteArray());
    return new String(baseEncoded);
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

  public String createHash(String code) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedhash = digest.digest(
        code.getBytes(StandardCharsets.UTF_8));
    return new String(encodedhash);

  }
}

package com.example.goldbet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class BarcodeGenerator {

    public static String generateBarcodeImage(String barcodeText, String filePath) throws WriterException, IOException {
        // Create a QRCodeWriter instance
        Code128Writer barcodeWriter = new Code128Writer();

        // Generate the barcode as a BitMatrix
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 200, 100);

        // Save the barcode image to a file
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return filePath;
    }
}
package com.data;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String pdfPath = "downloads/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
        String csvPath = "dados.csv";
        String zipPath = "intuitive";

        extractTableToCSV(pdfPath, csvPath);
        zipFile(csvPath, zipPath);
    }

    private static void extractTableToCSV(String pdfPath, String csvPath) {
        try (PDDocument document = PDDocument.load(new File(pdfPath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath))) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            boolean tableStarted = false;
            for (String line : lines) {

                if (!tableStarted && line.contains("OD") && line.contains("AMB")) {
                    tableStarted = true;

                    line = line.replace("OD", "Odontológico")
                            .replace("AMB", "Ambulatorial");
                    writer.write(line.trim().replaceAll("\\s+", ","));
                    writer.newLine();
                } else if (tableStarted) {

                    if (line.trim().isEmpty() || line.contains("Legenda")) {
                        break;
                    }
                    writer.write(line.trim().replaceAll("\\s+", ","));
                    writer.newLine();
                }
            }
            System.out.println("CSV criado com sucesso: " + csvPath);
        } catch (IOException e) {
            System.err.println("Erro ao extrair a tabela ou criar o CSV: " + e.getMessage());
        }
    }

    private static void zipFile(String sourceFile, String zipFileName) {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(new File(sourceFile))) {

            ZipEntry zipEntry = new ZipEntry(Paths.get(sourceFile).getFileName().toString());
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, bytesRead);
            }
            zos.closeEntry();
            System.out.println("Arquivo ZIP criado com sucesso: " + zipFileName);
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo ZIP: " + e.getMessage());
        }
    }
}

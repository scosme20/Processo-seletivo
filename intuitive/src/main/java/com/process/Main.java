package com.process;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        String url = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
        String download = "Downloads";
        String zip = "intuitive.zip";

        try {
            Document conect = Jsoup.connect(url).get();
            System.out.println("Conexão estabelecida com sucesso");


            Elements links = conect.select("a[href$=.pdf]:contains(Anexo I), a[href$=.pdf]:contains(Anexo II)");

            if (links.isEmpty()) {
                System.out.println("Nenhum link de anexo encontrado.");
                return;
            }


            Files.createDirectories(Paths.get(download));


            try (FileOutputStream fos = new FileOutputStream(zip);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                for (Element link : links) {
                    String arquivoUrl = link.absUrl("href");
                    String arquivoNome = Paths.get(new URL(arquivoUrl).getPath()).getFileName().toString();
                    Path arquivo = Paths.get(download, arquivoNome);


                    try (InputStream dw = new URL(arquivoUrl).openStream()) {
                        Files.copy(dw, arquivo, StandardCopyOption.REPLACE_EXISTING);
                    }
                    System.out.println("Arquivo baixado: " + arquivoNome);

                    try (FileInputStream fis = new FileInputStream(arquivo.toFile())) {
                        zos.putNextEntry(new ZipEntry(arquivoNome));
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                    System.out.println("Adicionado ao ZIP: " + arquivoNome);
                }
            }
            System.out.println("Arquivos compactados em: " + zip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

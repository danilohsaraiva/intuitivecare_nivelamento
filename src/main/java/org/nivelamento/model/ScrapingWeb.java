package org.nivelamento.model;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ScrapingWeb {

    private final String URL_SITE = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private final String DOWNLOAD_DIR = "downloads/";


    public List<File> downloadAnexosIeIIemPDF() {
        List<File> downloadFiles = new ArrayList<>();

        try {
            Files.createDirectories(Paths.get(DOWNLOAD_DIR));

            Document document = Jsoup.connect(URL_SITE).get();


            for (Element link : document.select("a.internal-link")) {
                // Corrigido para usar "href" ao invés de "a.internal-link"

                String href = link.absUrl("href");

                String text = link.text().toLowerCase();

                //Essa abordagem não tive sucesso pois com text acaba fazendo o download com extensão  .xlsx
                /*
                    if (text.contains("anexo i") || text.contains("anexo ii")) {
                        System.out.println("Baixando: " + href);
                        File file = downloadFile(href, DOWNLOAD_DIR);

                        if (file != null) {
                            System.out.println("Anexo encontrado");
                            downloadFiles.add(file);
                        }
                    } else {
                        System.out.println("Não encontrou...");
                    }
                */


                if ((href.toLowerCase().contains("anexo_i") && href.toLowerCase().contains(".pdf")) || (href.toLowerCase().contains("anexo_ii") && href.toLowerCase().contains(".pdf"))) {
                    System.out.println("Baixando: " + href);
                    File file = downloadFile(href, DOWNLOAD_DIR);

                    if (file != null) {
                        System.out.println("Anexo encontrado");
                        downloadFiles.add(file);
                    }
                } else {
                    System.out.println("Não encontrou...");
                }
            }

            return downloadFiles;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadFiles;
    }

    public void compactaAnexosEmArquivoZIP() {

    }

    private File downloadFile(String fileUrl, String saveDir) {

        try {
            URL url = new URL(fileUrl);

            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/"));
            File file = new File(saveDir + fileName);
            FileUtils.copyURLToFile(url, file);

            return file;

        } catch (IOException e) {
            System.err.println("Erro ao baixar arquivo: " + fileUrl);
            return null;
        }
    }

    public void zipArquivos(List<File> files, String zipFileName) throws IOException {

        String zipFilePath = DOWNLOAD_DIR + File.separator + zipFileName;

        try (ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new FileOutputStream(zipFilePath))) {
            for (File file : files) {
                ZipArchiveEntry entry = new ZipArchiveEntry(file, file.getName());
                zipOut.putArchiveEntry(entry);
                Files.copy(file.toPath(), zipOut);
                zipOut.closeArchiveEntry();
            }
            zipOut.finish();
        }
    }
}

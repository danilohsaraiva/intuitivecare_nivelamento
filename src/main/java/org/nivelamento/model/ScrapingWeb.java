package org.nivelamento.model;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ScrapingWeb {
    private final String ARQUIVOS_ZIP_DIR = "util/arquivoscompactados/";
    private final String URL_SITE = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private final String DOWNLOAD_DIR = "downloads/";

    private final String DADOS_PATH = "util";


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

    public void zipArquivos(String nomeArquivoZip) throws IOException {

        Path anexoI = Paths.get(DOWNLOAD_DIR, "Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf");
        Path anexoII = Paths.get(DOWNLOAD_DIR, "Anexo_II_DUT_2021_RN_465.2021_RN628.2025_RN629.2025.pdf");

        Path caminhoZip = Paths.get(ARQUIVOS_ZIP_DIR, nomeArquivoZip + ".zip");

        if (!Files.exists(anexoI) || !Files.exists(anexoII)) {
            System.out.println("Erro: Arquivo PDF não encontrado");
            return;
        }

        Files.createDirectories(caminhoZip.getParent());

        if (Files.exists(caminhoZip)) {
            Files.delete(caminhoZip);
        }

        try (FileOutputStream fos = new FileOutputStream(caminhoZip.toFile());
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            Path[] arquivos = { anexoI, anexoII };

            for (Path arquivo : arquivos) {
                try (FileInputStream fis = new FileInputStream(arquivo.toFile())) {

                    ZipEntry zipEntry = new ZipEntry(arquivo.getFileName().toString());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                }
            }

            System.out.println("Arquivos compactados com sucesso: " + caminhoZip);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao compactar arquivo");
        }
    }
}

package org.nivelamento.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TransformacaoDados {
    public final String PATH_ARQUIVO_CSV = "util/dados.csv";
    public final String DIRETORIO_CSV = "util";

    public final String PATH_ZIP = "util/arquivoscompactados/";

    public String extrairDadosTabelaRolAnexoI(String pathPDF) throws IOException {
        PDDocument documento = PDDocument.load(new File(pathPDF));
        PDFTextStripper pdfStripper = new PDFTextStripper();

        pdfStripper.setSortByPosition(true);

        String texto = pdfStripper.getText(documento);

        documento.close();
        return texto;
    }

    public void substituiColunaODeAMB() {

        try {
            Path pathCSV = Paths.get(DIRETORIO_CSV, "dados.csv");

            List<String> linhas = Files.readAllLines(pathCSV);

            List<String> novasLinhas = new ArrayList<>();

            for (String linha : linhas) {
                String novaLinha = linha.replace("OD", "Seg. Odontológico").replace("AMB", "Seg. Ambulatorial");

                novasLinhas.add(novaLinha);
            }

            Files.write(Path.of(PATH_ARQUIVO_CSV), novasLinhas);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void salvarDadosEmTabelaEstruturadaCsv(String texto) {
        List<String[]> linhasGerais = new ArrayList<>();
        String[] linhasSeparadasPorEspaco = texto.split("\n");


        for (String linha : linhasSeparadasPorEspaco) {
            linhasGerais.add(linha.split("\\s{2,}", -1));
        }

        if (!Files.exists(Paths.get(DIRETORIO_CSV))) {
            try {
                Files.createDirectories(Paths.get(DIRETORIO_CSV));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_ARQUIVO_CSV))) {
            for (String[] linha : linhasGerais) {
                writer.write(String.join(";", linha));
                writer.newLine();
            }
            System.out.println("Arquivo CSV salvo com sucesso em: " + PATH_ARQUIVO_CSV);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo CSV: " + e.getMessage());
        }
    }

    public void compactaCSV(String novoNomeArquivoCompactado) throws IOException {
        String novoNome = "Teste_" + novoNomeArquivoCompactado + ".zip";

        Path caminhoCSV = Paths.get(DIRETORIO_CSV, "dados.csv");
        Path caminhoZip = Paths.get(PATH_ZIP, novoNome);

        if (!Files.exists(caminhoCSV)) {
            System.out.println("Erro: Arquivo CSV não encontrado -> " + caminhoCSV);
            return;
        }

        Files.createDirectories(caminhoZip.getParent());

        if (Files.exists(caminhoZip)) {
            Files.delete(caminhoZip);
        }

        try (FileInputStream fis = new FileInputStream(caminhoCSV.toFile());
             FileOutputStream fos = new FileOutputStream(caminhoZip.toFile());
             ZipOutputStream zos = new ZipOutputStream(fos)) {


            ZipEntry zipEntry = new ZipEntry(caminhoCSV.getFileName().toString());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();

            System.out.println("Arquivo compactado com sucesso: " + caminhoZip);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao compactar arquivo");
        }

    }
}

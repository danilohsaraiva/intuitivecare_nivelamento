package org.nivelamento;

import org.nivelamento.model.ScrapingWeb;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        ScrapingWeb sw = new ScrapingWeb();

        List<File> listaDownloads = sw.downloadAnexosIeIIemPDF();

        sw.zipArquivos(listaDownloads, "Anexos.zip");
    }
}
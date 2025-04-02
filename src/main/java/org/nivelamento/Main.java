package org.nivelamento;

import org.nivelamento.model.ScrapingWeb;
import org.nivelamento.model.TransformacaoDados;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {


        ScrapingWeb sw = new ScrapingWeb();

        List<File> listaDownloads = sw.downloadAnexosIeIIemPDF();
        sw.zipArquivos("Anexos");

        TransformacaoDados td = new TransformacaoDados();
         td.salvarDadosEmTabelaEstruturadaCsv(td.extrairDadosTabelaRolAnexoI("downloads/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf"));
         td.substituiColunaODeAMB();
         td.compactaCSV("DaniloSaraiva");
   }
}
# Intuitive Care - Nivelamento

Nivelamento 2025 - Intuitive Care

📌 Linguagem utilizada: Java<br>

## Observações

### Geral

Para os testes 1 e 2, criei uma classe que contém métodos que executam as atividades propostas.

### 1. Teste web Scraping

- O método **downloadAnexosIeIIemPDF()** realiza o download dos Anexo | e Anexo || em formato PDF, ao chamar o método *
  *downloadFile()**, os arquivos são armazenados na pasta download;
- O método **downloadFile()** faz o download e é chamado pelo método acima;
- O método **zipArquivos()** Compacta os arquivos AnexoI e AnexoII no formato **zip**, armazenando no diretório *
  *util/arquivoscompactados**

### 2. Teste de Transformação de Dados

- O método **extrairDadosTabelaRolAnexoI()** retorna uma String contendo os dados presentes no arquivo.
- O método **salvarDadosEmTabelaEstruturadaCsv**...

# Intuitive Care - Nivelamento

Nivelamento 2025 - Intuitive Care

📌 Linguagem utilizada: Java<br>
📌 Banco de Dados: MySQL

## Observações

### Geral

Para os testes 1 e 2, criei uma classe que contém métodos que executam as atividades propostas.

### 1. Teste web Scraping

#### ✅ Download dos Anexos I e II em formato PDF
#### ✅ Compactação de todos os Anexos em um único arquivo (formatos **Zip**, RAR, etc.).

- O método **downloadAnexosIeIIemPDF()** realiza o download dos Anexo | e Anexo || em formato PDF, ao chamar o método **downloadFile()**, os arquivos são armazenados na pasta [**📁downloads/**](https://github.com/danilohsaraiva/intuitivecare_nivelamento/tree/main/downloads)
- O método **downloadFile()** faz o download e é chamado pelo método acima;
- O método **zipArquivos()** Compacta os arquivos AnexoI e AnexoII no formato **zip**, armazenando no diretório [**📁util/arquivoscompactados/**](https://github.com/danilohsaraiva/intuitivecare_nivelamento/tree/main/util/arquivoscompactados).

### 2. Teste de Transformação de Dados

#### ✅ Extraia os dados da tabela Rol de Procedimentos e Eventos em Saúde do PDF do Anexo I do teste 1
#### ✅ Salve os dados em uma tabela estruturada, em formato csv.
#### ✅ Compacte o csv em um arquivo denominado "Teste_{seu_nome}.zip".
#### ✅ Substitua as abreviações das colunas OD e AMB pelas descrições completas, conforme a legenda no rodapé.

- Os arquivos zipados são compactados e salvos em [**📁util/arquivoscompactados/**](https://github.com/danilohsaraiva/intuitivecare_nivelamento/tree/main/util/arquivoscompactados)

### 3. Teste de Banco de Dados (DB)
_Crie scripts .sql (compatíveis com MySQL 8. ou Postgres >10.0)_

#### ✅ Crie queries s para estruturar tabelas necessárias para o arquivo csv
#### ✅ Elabore queries para importar o conteúdo dos arquivos preparados, atentando para o encoding correto.
#### ❌ Desenvolva uma query analítica para responder:
- Quais as 10 operadoras com maiores despesas em "EVENTOS/ SINISTROS CONHECIDOS OU
AVISADOS DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR" no último trimestre?
- Quais as 10 operadoras com maiores despesas nessa categoria no último ano?

Obs: Tentei utilizar diversos processos afim de melhorar as queries em geral<br>

| Processo         | Objetivo                          |
|------------------|-----------------------------------|
| Criação de INDEX | Otimiza consultas                 |
| Criação de View  | Facilita consultas complexas      |
| Subquery         | Melhora a performance             |
| Procedure        | Reduz timeout e otimiza execuções |

No entanto não tive exito ao tentar elaborar as queries, tendo em vista o  **erro: Lost connection to MySQL serving during query** persistir ao tentar manipular o acervo de dados.

Todos os scripts estão na pasta [**📁util/desafio_3_itens_necessarios/...**]()
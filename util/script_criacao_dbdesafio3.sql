-- DROPANDO DATABASE CASO EXISTA
DROP DATABASE IF EXISTS dbdesafio_3;

-- CRIANDO DATA BASE
CREATE DATABASE dbdesafio_3;

USE dbdesafio_3;

-- CRIANDO TABELA relatorio_cadop
CREATE TABLE relatorio_cadop (
	 id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     registro_ans VARCHAR(6),
     cnpj VARCHAR(14),
     razao_social VARCHAR(140),
     nome_fantasia VARCHAR(140),
     modalidade VARCHAR(140),
     logradouro VARCHAR(140),
     numero VARCHAR(140),
     complemento VARCHAR(140),
     bairro VARCHAR(150),
     cidade VARCHAR(150),
     uf VARCHAR(2),
     cep VARCHAR(10),
     ddd VARCHAR(2),
     telefone VARCHAR(150),
     fax VARCHAR(11),
     endereco_eletronico VARCHAR(150),
     representante VARCHAR(150),
     cargo_representante VARCHAR(150),
     regiao_comercializacao VARCHAR(150),
     data_registro_ans DATE
);

-- CRIANDO TABELA demonstracao_bancaria
CREATE TABLE demonstracao_bancaria(
	id_demonstraco_bancaria BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    data_demonstracao_bancaria DATE,
    reg_ans VARCHAR(50),
    cd_conta_contabil VARCHAR(150),
    descricao VARCHAR(250), 
    vl_saldo_inicial double(10,2),
    vl_saldo_final double (10,2)
);

ALTER TABLE demonstracao_bancaria 
MODIFY COLUMN vl_saldo_inicial DOUBLE,
MODIFY COLUMN vl_saldo_final DOUBLE;


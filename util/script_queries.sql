USE dbdesafio_3;
-- Desenvolva uma query analítica para responder:
-- Quais as 10 operadoras com maiores despesas em "EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR" no último trimestre?
-- Despesas = prejuízo "Então VL_SALDO_INICIAL tem de ser 

-- tentei alterar as configurações para conexão não ser interrompida
SET SESSION wait_timeout = 1200;
SET SESSION net_read_timeout = 1200;
SET SESSION net_write_timeout = 1200;


-- Executar índices somente depois de importar os arquivos do script_importacao_arquivos_csv
-- Criei índices baseados nas consultas

CREATE INDEX idx_registro_ans ON relatorio_cadop (registro_ans);
CREATE INDEX idx_reg_ans ON demonstracao_bancaria (reg_ans);
CREATE INDEX idx_vsf_reg_ans ON demonstracao_bancaria (reg_ans, vl_saldo_final);
CREATE INDEX idx_data ON demonstracao_bancaria (data_demonstracao_bancaria);
CREATE INDEX idx_data_saldo ON demonstracao_bancaria(data_demonstracao_bancaria, vl_saldo_final);

SHOW INDEX FROM demonstracao_bancaria;


-- Teste (Nome das empresas)
SELECT razao_social as `Nome da Empresa` 
	FROM relatorio_cadop as rc INNER JOIN demonstracao_bancaria as dmb
		ON rc.registro_ans = dmb.reg_ans
			GROUP BY razao_social;

-- Traz valores ordenados do maior para o menor
SELECT SUM(dmb.vl_saldo_final) as `Total Despesas` FROM demonstracao_bancaria as dmb 
		GROUP BY dmb.reg_ans ORDER BY `Total Despesas` DESC LIMIT 10;
        
-- Tentei subqueries mas dá erro: Lost connection to MySQL serving during query
SELECT `Número Registro ANS`, `Soma Saldo Final`
FROM(
	SELECT reg_ans AS `Número Registro ANS`, 
		   SUM(vl_saldo_final) AS `Soma Saldo Final`,
			descricao
	FROM demonstracao_bancaria
	WHERE data_demonstracao_bancaria BETWEEN '2024-10-01' AND '2024-12-31'
	GROUP BY reg_ans
) as filtra_por_data;

SELECT reg_ans AS `Número Registro ANS`, 
		   SUM(vl_saldo_final) AS `Soma Saldo Final`,
			descricao
	FROM demonstracao_bancaria
	WHERE data_demonstracao_bancaria BETWEEN '2024-10-01' AND '2024-12-31'
	GROUP BY reg_ans ORDER BY `Soma Saldo Final`;

-- Busca os a soma dos valores das despesas finais e o código de registro ANS aplicando o filtro de descrição
SELECT reg_ans, 
       SUM(vl_saldo_final) AS `Valor Despesas`
FROM demonstracao_bancaria
WHERE descricao = 'EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS  DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR '
GROUP BY reg_ans;


-- Verificando dados entre x a y
SELECT dmb.reg_ans AS `Número Registro ANS`, SUM(dmb.vl_saldo_final) as `Soma Saldo Final`
FROM demonstracao_bancaria AS dmb
WHERE data_demonstracao_bancaria BETWEEN '2024-10-01' AND '2024-12-31' GROUP BY dmb.reg_ans ORDER BY `Soma Saldo Final` DESC LIMIT 10;

-- Testando tabela temporária
CREATE TEMPORARY TABLE temp_dmb_ultimo_trimestre AS
SELECT reg_ans, sum(vl_saldo_final) as `Soma Despesas`
    FROM demonstracao_bancaria
    WHERE data_demonstracao_bancaria BETWEEN '2024-10-01' AND '2024-12-31' group by reg_ans;

 
 DELIMITER $$

CREATE PROCEDURE DezMaioresSaldoFinal(IN dataInicio DATE, IN dataFim DATE)
BEGIN
    -- Ajustar timeout para evitar desconexão
    SET SESSION wait_timeout = 600;
    SET SESSION net_read_timeout = 600;
    SET SESSION net_write_timeout = 600;
    
    -- Criar ou limpar tabela temporária
    CREATE TEMPORARY TABLE IF NOT EXISTS temp_dmb_ultimo_trimestre (
        reg_ans INT,
        soma_saldo_final DECIMAL(18,2)
    );
    
    DELETE FROM temp_dmb_ultimo_trimestre;
    
    -- Inserir os dados na tabela temporária
    INSERT INTO temp_dmb_ultimo_trimestre (reg_ans, soma_saldo_final)
    SELECT reg_ans, SUM(vl_saldo_final) AS soma_saldo_final
    FROM demonstracao_bancaria
    WHERE data_demonstracao_bancaria BETWEEN dataInicio AND dataFim
    GROUP BY reg_ans;

    -- Selecionar os resultados ordenados
    SELECT reg_ans AS `Número Registro ANS`, soma_saldo_final AS `Soma Saldo Final`
    FROM temp_dmb_ultimo_trimestre
    ORDER BY soma_saldo_final DESC
    LIMIT 10;

END$$
-- Tentando abordagem utilizando Procedure
DELIMITER ;

DELIMITER $$

CREATE PROCEDURE GetTop10SaldoFinal()
BEGIN
    -- Definir um tempo maior para evitar timeout
    SET SESSION wait_timeout = 600;
    SET SESSION net_read_timeout = 600;
    SET SESSION net_write_timeout = 600;

    -- Criar uma tabela temporária para armazenar os dados processados
    CREATE TEMPORARY TABLE IF NOT EXISTS temp_dmb_ultimo_trimestre AS 
    SELECT reg_ans, SUM(vl_saldo_final) AS soma_saldo_final
    FROM demonstracao_bancaria
    WHERE data_demonstracao_bancaria BETWEEN '2024-10-01' AND '2024-12-31'
    GROUP BY reg_ans;

    -- Selecionar os resultados ordenados
    SELECT reg_ans AS `Número Registro ANS`, soma_saldo_final AS `Soma Saldo Final`
    FROM temp_dmb_ultimo_trimestre
    ORDER BY soma_saldo_final DESC
    LIMIT 10;
    
END$$

DELIMITER ;
-- Quais as 10 operadoras com maiores despesas nessa categoria no último ano?
SET SQL_SAFE_UPDATES = 0;
CALL DezMaioresSaldoFinal('2024-10-01', '2024-12-31');
SET SQL_SAFE_UPDATES = 1;
call GetTop10SaldoFinal();
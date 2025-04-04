SELECT * FROM gvrer.itinerario;
SELECT * FROM gvrer.destino;
SELECT * FROM gvrer.motivodaviagem;
SELECT * FROM gvrer.itinerario_destino;
SELECT * FROM gvrer.itinerario_destino;



SELECT 
	nomeitinerario, 
    NOMEMOTIVODAVIAGEM,
    sequencia,
    nomedestino,
    observacaodestino
	FROM gvrer.itinerario 
    inner join motivodaviagem 
		on (motivodaviagem_MOTIVODAVIAGEMID = MOTIVODAVIAGEMID) 
	inner join itinerario_destino
		on (itinerarioid = itinerario_itinerarioid)
	inner join destino
		on (destino_destinoid = destinoid)
    where itinerarioid = 5
;


select * from pessoa;
select * from fisica;
select * from endereco;

select 
	pessoaid,
    fisicaid,
    nome,
    sobrenome,
    concat(if(isnull(logradouro),"", logradouro), " ",if(isnull(numero),"", numero), ", ", nomebairro, ", ", nomecidade, "-", uf, " CEP: ",cep) as 'endereco', 
    complemento,
    referencia,
    nomeestado,
    nomepais    
    from pessoa as p
	inner join fisica as f on (p.pessoaid = f.pessoa_pessoaid)
    left join endereco as e on (p.pessoaid = e.pessoa_pessoaid)
    left join rua as r on (e.rua_ruaid = r.ruaid)
    left join bairro as b on (r.bairro_bairroid = b.bairroid)
    left join cidade as c on (b.cidade_cidadeid = c.cidadeid)
    left join estado as es on (c.estado_estadoid = es.estadoid)
    left join pais as pa on (es.estadoid = pa.paisid)
    -- where nome like '%dery%'
;

delimiter $$
	create procedure cadastrarPessoaFisica(
		nome varchar(200),
        sobrenome varchar(200),
        datanascimento timestamp
        )
		begin
			insert into pessoa 
				(pessoa.datanascimento) 
					value (datanascimento);
			insert into fisica 
				(fisica.nome, fisica.sobrenome, fisica.pessoa_pessoaid) 
					value (nome, sobrenome, last_insert_id());
        end $$
delimiter ;

-- Drop apaga a procedure --
-- drop procedure cadastrarPessoaFisica;

-- Call aciona a procedure --
-- call cadastrarPessoaFisica(); 




INSERT 
	INTO `gvrer`.`itinerario` (
		`nomeitinerario`, 
        `valor`, 
        `observacoes`, 
        `seguroviagem`, 
        `dataida`, 
        `horaida`, 
        `datavolta`, 
        `horavolta`) 
	VALUES (
		'KM12', 
        '0', 
        'PAUSA CAFÉ DA MANAÃ', 
        '', 
        '26/06/2025', 
        '08 H', 
        '26/05=2025', 
        '17 H')
;





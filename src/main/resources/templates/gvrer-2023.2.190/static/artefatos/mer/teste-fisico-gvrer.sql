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





INSERT INTO db_register.C_TIPODATOCONTACTO (IDTIPODATOCONTACTO,TIPOCONTACTO,DESCRIPCION) VALUES
	 (1,'EMAIL','Correo electronico'),
	 (2,'MOVIL','Telefono Movil'),
	 (3,'FIJO','Telefono Fijo');

USE `db_register`;
CREATE  OR REPLACE VIEW `V_ELEGIBLE_IDBADGE` AS
SELECT
    persona.IDPERSONA,
    afiliacion.IDAFILIACION,
    aficionado.IDAFICIONADO,
    aspirante.IDASPIRANTE,
    imagen.IDIMAGEN
FROM T_PERSONA persona
LEFT JOIN T_AFILIACION afiliacion on afiliacion.IDPERSONA = persona.IDPERSONA
LEFT JOIN T_AFICIONADO aficionado on aficionado.IDPERSONA = persona.IDPERSONA
LEFT JOIN T_ASPIRANTE aspirante on aspirante.IDPERSONA = persona.IDPERSONA
LEFT JOIN T_IMAGEN imagen on imagen.IDPERSONA = persona.IDPERSONA AND imagen.IDTIPOIMAGENDOCUMENTO = 1;
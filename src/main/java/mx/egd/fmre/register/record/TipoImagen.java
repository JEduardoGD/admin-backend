package mx.egd.fmre.register.record;

import java.util.Date;

public record TipoImagen(
		int idTipoImagen,
		String tipo,
		String descripcion,
		Date fechaInicio,
		Date fechaFin
		) {
	
}

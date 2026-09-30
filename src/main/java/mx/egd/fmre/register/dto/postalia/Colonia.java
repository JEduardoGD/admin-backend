package mx.egd.fmre.register.dto.postalia;

import java.io.Serializable;

import lombok.Data;

@Data
public class Colonia implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6085883941128624250L;
	private String nombre;
	private String tipo;
}

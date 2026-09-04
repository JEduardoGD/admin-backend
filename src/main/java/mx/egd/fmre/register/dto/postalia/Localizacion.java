package mx.egd.fmre.register.dto.postalia;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Localizacion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8504826313243838666L;
	@JsonProperty("codigo_postal")
	private String codigoPostal;
	private String estado;
	private String municipio;
	private String ciudad;
	private String zona;
	private List<Colonia> colonias;
}

package mx.egd.fmre.register.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "T_IMAGEN")
public class ImagenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDIMAGEN")
	private Integer idImagen;

    @ManyToOne 
    @JoinColumn(name = "IDPERSONA")
    private PersonaEntity persona;

    @Column(name = "UUID")
    private String uuid;

    @ManyToOne 
    @JoinColumn(name = "IDTIPOIMAGENDOCUMENTO")
    private TipoImagenDocumentoEntity tipoImagenDocumento;
    
}

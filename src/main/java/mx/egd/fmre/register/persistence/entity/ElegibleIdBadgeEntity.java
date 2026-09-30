package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Immutable
@Table(name = "V_ELEGIBLE_IDBADGE")
public class ElegibleIdBadgeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IDPERSONA")
    private Integer idPersona;

    @Column(name = "IDAFILIACION")
    private Integer idAfiliacion;

    @Column(name = "IDAFICIONADO")
    private Integer idAficionado;

    @Column(name = "IDASPIRANTE")
    private Integer idAspirante;

    @Column(name = "IDIMAGEN")
    private Integer idImagen;

}

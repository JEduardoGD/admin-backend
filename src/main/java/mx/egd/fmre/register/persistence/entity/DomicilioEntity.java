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
@Table(name = "T_DOMICILIO")
public class DomicilioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDOMICILIO")
    private Integer idDomicilio;

    @ManyToOne 
    @JoinColumn(name = "IDPERSONA")
    private PersonaEntity persona;

    @Column(name = "CP")
    private String cp;

    @Column(name = "DOMICILIO")
    private String domicilio;

    @Column(name = "ENTIDADFED")
    private String entidadFederativa;

    @Column(name = "MUNICIPIO")
    private String municipio;

    @Column(name = "LOCALIDAD")
    private String localidad;

    @Column(name = "PAIS")
    private String pais;
}

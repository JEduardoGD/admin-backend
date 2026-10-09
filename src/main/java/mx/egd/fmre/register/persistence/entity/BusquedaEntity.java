package mx.egd.fmre.register.persistence.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "V_BUSQUEDA")
public class BusquedaEntity implements Serializable {
    private static final long serialVersionUID = 2606721581079336433L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RN")
    private int idBusqueda;

    @Column(name = "IDPERSONA")
    private int idPersona;

    @Column(name = "IDAFILIACION")
    private int idAfiliacion;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "PRIMERAPELLIDO")
    private String primerApellido;

    @Column(name = "SEGUNDOAPELLIDO")
    private String segundoApellido;

    @Column(name = "INDICATIVO")
    private String indicativo;
}

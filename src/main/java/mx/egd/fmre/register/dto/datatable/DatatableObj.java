package mx.egd.fmre.register.dto.datatable;

import java.io.Serializable;
import java.util.Random;

import lombok.Data;

@Data
public class DatatableObj implements Serializable {/**
     * 
     */
    private static final long serialVersionUID = 2459362902364558239L;
    private long idPersona;
    private String name;
    private boolean readyForCredencial = new Random().nextBoolean(); 
}

package mx.egd.fmre.register.dto.datatable;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DataTableResponse implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1502106489864557963L;
    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<DatatableObj> data;
}
package mx.egd.fmre.register.service;

import mx.egd.fmre.register.dto.datatable.DataTableResponse;
import mx.egd.fmre.register.dto.datatable.QueryObj;

public interface DatatableService {

    DataTableResponse get(QueryObj queryObj);


}

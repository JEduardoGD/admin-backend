package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.datatable.DataTableResponse;
import mx.egd.fmre.register.dto.datatable.DatatableObj;
import mx.egd.fmre.register.dto.datatable.QueryObj;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.PersonaRepository;
import mx.egd.fmre.register.service.DatatableService;

@Service
@RequiredArgsConstructor
public class DatatableServiceImpl implements DatatableService {
    
    private final PersonaRepository personaRepository;
    
    @Override
    public DataTableResponse get(QueryObj queryObj) {
        List<PersonaEntity> personaList = personaRepository.findAll();
        return processList(queryObj, personaList);
    }
    
    private DataTableResponse processList(QueryObj queryObj, List<PersonaEntity> personaList) {
        int draw = 1;
        long recordsTotal = personaList.stream().count();
        long recordsFiltered = personaList.stream().count();
        List<DatatableObj> data = personaList.stream().map(p -> {
            StringBuffer nameSb = new StringBuffer();
            nameSb.append(p.getNombre()).append(" ").append(p.getPrimerApellido());
            if (p.getSegundoApellido() != null) {
                nameSb.append(" ").append(p.getSegundoApellido());
            }

            DatatableObj datatableObj = new DatatableObj();
            datatableObj.setIdPersona(p.getIdPersona());
            datatableObj.setName(nameSb.toString());
            return datatableObj;
        }).collect(Collectors.toList());

        DataTableResponse dataTableResponse = new DataTableResponse();
        dataTableResponse.setDraw(draw);
        dataTableResponse.setRecordsTotal(recordsTotal);
        dataTableResponse.setRecordsFiltered(recordsFiltered);
        dataTableResponse.setData(data);

        return dataTableResponse;
    }
}

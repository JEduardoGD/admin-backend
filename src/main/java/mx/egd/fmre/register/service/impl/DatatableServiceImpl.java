package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.datatable.DataTableResponse;
import mx.egd.fmre.register.dto.datatable.DatatableObj;
import mx.egd.fmre.register.dto.datatable.QueryObj;
import mx.egd.fmre.register.persistence.entity.ElegibleIdBadgeEntity;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.PersonaRepository;
import mx.egd.fmre.register.service.DatatableService;
import mx.egd.fmre.register.service.ElegibleIdBadgeService;

@Service
@RequiredArgsConstructor
public class DatatableServiceImpl implements DatatableService {

    private final PersonaRepository personaRepository;
    private final ElegibleIdBadgeService elegibleIdBadgeService;

    @Override
    public DataTableResponse get(QueryObj queryObj) {
        Pageable pageable = PageRequest.of(0, 10/* , Sort.by("registrationDate").descending() */);
        List<PersonaEntity> personaList = personaRepository.searchByTerm(queryObj.search().value(), pageable);
        return processList(queryObj, personaList);
    }

    private DataTableResponse processList(QueryObj queryObj, List<PersonaEntity> personaList) {
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
            datatableObj.setReadyForCredencial(validateIdBadge(p.getIdPersona()));
            return datatableObj;
        }).collect(Collectors.toList());

        DataTableResponse dataTableResponse = new DataTableResponse();
        dataTableResponse.setDraw(queryObj.draw());
        dataTableResponse.setRecordsTotal(recordsTotal);
        dataTableResponse.setRecordsFiltered(recordsFiltered);
        dataTableResponse.setData(data);

        return dataTableResponse;
    }

    private boolean validateIdBadge(int personaId) {
        List<ElegibleIdBadgeEntity> elegibleIdBadgeList = elegibleIdBadgeService.findByIdPersona(personaId);
        ElegibleIdBadgeEntity elegibleIdBadgeEntity;
        if (elegibleIdBadgeList != null && !elegibleIdBadgeList.isEmpty()) {
            elegibleIdBadgeEntity = elegibleIdBadgeList.get(0);
        } else {
            return false;
        }
        boolean haveAficionadoOrAspiranteReg = elegibleIdBadgeEntity.getIdAficionado() != null
                || elegibleIdBadgeEntity.getIdAspirante() != null;
        boolean haveAfiliacion = elegibleIdBadgeEntity.getIdAfiliacion() != null;
        boolean havePersonalImagen = elegibleIdBadgeEntity.getIdImagen() != null;
        return haveAficionadoOrAspiranteReg && haveAfiliacion && havePersonalImagen;
    }
}

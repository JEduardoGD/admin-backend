package mx.egd.fmre.register.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.datatable.DataTableResponse;
import mx.egd.fmre.register.dto.datatable.DatatableObj;
import mx.egd.fmre.register.dto.datatable.QueryObj;
import mx.egd.fmre.register.persistence.entity.BusquedaEntity;
import mx.egd.fmre.register.persistence.entity.ElegibleIdBadgeEntity;
import mx.egd.fmre.register.persistence.repository.BusquedaRepository;
import mx.egd.fmre.register.service.DatatableService;
import mx.egd.fmre.register.service.ElegibleIdBadgeService;

@Service
@RequiredArgsConstructor
public class DatatableServiceImpl implements DatatableService {

    private final ElegibleIdBadgeService elegibleIdBadgeService;
    private final BusquedaRepository busquedaRepository;

    @Override
    public DataTableResponse get(QueryObj queryObj) {
        Pageable pageable = PageRequest.of(0, 10 , Sort.by("idPersona").descending());
        List<BusquedaEntity> busquedaEntityList = busquedaRepository.searchByTerm(queryObj.search().value(), pageable);
        return processList(queryObj, busquedaEntityList);
    }

    private DataTableResponse processList(QueryObj queryObj, List<BusquedaEntity> busquedaEntityList) {
        long recordsTotal = busquedaEntityList.stream().count();
        long recordsFiltered = busquedaEntityList.stream().count();
        List<DatatableObj> data = busquedaEntityList.stream().map(r -> {
            StringBuffer nameSb = new StringBuffer();
            nameSb.append(r.getNombre()).append(" ").append(r.getPrimerApellido());
            if (r.getSegundoApellido() != null) {
                nameSb.append(" ").append(r.getSegundoApellido());
            }
            DatatableObj datatableObj = new DatatableObj();
            datatableObj.setIdPersona(r.getIdPersona());
            datatableObj.setName(nameSb.toString());
            datatableObj.setReadyForCredencial(validateIdBadge(r.getIdPersona()));
            datatableObj.setCallsign(r.getIndicativo());

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

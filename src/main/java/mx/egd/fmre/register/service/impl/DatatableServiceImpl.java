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
import mx.egd.fmre.register.dto.datatable.QueryObj.Column;
import mx.egd.fmre.register.dto.datatable.QueryObj.Search;
import mx.egd.fmre.register.persistence.entity.PersonaEntity;
import mx.egd.fmre.register.persistence.repository.PersonaRepository;
import mx.egd.fmre.register.service.DatatableService;

@Service
@RequiredArgsConstructor
public class DatatableServiceImpl implements DatatableService {
    
    private final PersonaRepository personaRepository;
    
    private static final String REGEXP = "REGEXP";
    private static final String LIKE = "LIKE";
    private static final String SPACE = " ";
    private static final String S_OR_S = " OR ";
    
    @Override
    public DataTableResponse get(QueryObj queryObj) {
        String searchConditions = createSearchConditions(queryObj);
        Pageable pageable = PageRequest.of(1, 10, Sort.by("registrationDate").descending());
        List<PersonaEntity> personaList = personaRepository.searchByRegexConditions(queryObj, pageable);
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
            return datatableObj;
        }).collect(Collectors.toList());

        DataTableResponse dataTableResponse = new DataTableResponse();
        dataTableResponse.setDraw(queryObj.draw());
        dataTableResponse.setRecordsTotal(recordsTotal);
        dataTableResponse.setRecordsFiltered(recordsFiltered);
        dataTableResponse.setData(data);

        return dataTableResponse;
    }
    
    private String createSearchConditions(QueryObj queryObj) {
        Search search = queryObj.search();
        List<Column> searchableColumns = queryObj.columns().stream()
                .filter(Column::searchable)
                .toList();
        return searchableColumns.stream().map(c -> {
            StringBuffer strSearchColumn = new StringBuffer();
            strSearchColumn
                    .append(c.data())
                    .append(SPACE)
                    .append(search.regex() ? REGEXP : LIKE)
                    .append(SPACE)
                    .append(String.format("'%%%s%%'", search.value()));
            return strSearchColumn.toString();
        }).collect(Collectors.joining(S_OR_S));
    }
}

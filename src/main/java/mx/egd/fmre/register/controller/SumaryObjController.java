package mx.egd.fmre.register.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.egd.fmre.register.dto.datatable.DataTableResponse;
import mx.egd.fmre.register.dto.datatable.QueryObj;
import mx.egd.fmre.register.service.DatatableService;

@RestController
@RequestMapping("sumary")
@RequiredArgsConstructor
public class SumaryObjController {
    
    private final DatatableService datatableService;

    @PostMapping
    public ResponseEntity<DataTableResponse> findAllActive(@RequestBody QueryObj queryObj){
        DataTableResponse dataTableResponse = datatableService.get(queryObj);
        return new ResponseEntity<>(dataTableResponse, HttpStatus.OK);
    }
}

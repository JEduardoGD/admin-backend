package mx.egd.fmre.register.dto.datatable;

import java.util.List;

public record QueryObj(
        int draw,
        int start,
        int length,
        Search search,
        List<Order> order,
        List<Column> columns
    ) {
        public record Search(
            String value,
            boolean regex
        ) {}

        public record Order(
            int column,
            String dir   // "asc" or "desc"
        ) {}

        public record Column(
            String data,
            String name,
            boolean searchable,
            boolean orderable,
            Search search
        ) {}
    }


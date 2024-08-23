package com.carritocompras.repository;

import com.carritocompras.dto.ReportTopProducDto;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public class ReportTopRepository {
    private final R2dbcEntityTemplate entityTemplate;
    public ReportTopRepository(R2dbcEntityTemplate entityTemplate) {
        this.entityTemplate = entityTemplate;
    }

    public Flux<ReportTopProducDto> findTopArticulos(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT " +
                "o.idordersale AS idordersale, " +
                "o.idcustomer AS idcustomer, " +
                "o.idshoppingcart AS idshoppingcart, " +
                "d.idproduct AS idproduct, " +
                "p.description AS description, " +
                "SUM(d.quantity) AS quantity_sale " +
                "FROM ordersale o " +
                "JOIN shoppingcart s on o.idshoppingcart = s.idshoppingcart " +
                "JOIN detailcart d on s.idshoppingcart = d.idshoppingcart " +
                "JOIN product p on d.idproduct = p.idproduct " +
                "WHERE o.orderdate " +
                "BETWEEN $1 AND $2 " +
                "GROUP BY o.idordersale," +
                "o.idcustomer," +
                "o.idshoppingcart," +
                "d.idproduct," +
                "p.description " +
                "LIMIT 5";

        return entityTemplate.getDatabaseClient()
                .sql(query)
                .bind("$1", startDate)
                .bind("$2", endDate)
                .map(row -> new ReportTopProducDto(
                        row.get("idordersale", Integer.class),
                        row.get("idcustomer", Integer.class),
                        row.get("idshoppingcart", Integer.class),
                        row.get("idproduct", Integer.class),
                        row.get("description", String.class),
                        row.get("quantity_sale", Integer.class)
                ))
                .all();
    }
}

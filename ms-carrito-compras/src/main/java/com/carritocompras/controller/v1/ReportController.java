package com.carritocompras.controller.v1;

import com.carritocompras.dto.ReportTopProducDto;
import com.carritocompras.model.OrderBuy;
import com.carritocompras.model.OrderSale;
import com.carritocompras.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reports")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/buys")
    public Flux<OrderBuy> getOrderBuys(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return reportService.getOrderBuys(startDate, endDate);
    }

    @GetMapping("/sales")
    public Flux<OrderSale> getOrderSales(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return reportService.getOrderSales(startDate, endDate);
    }

    @GetMapping("/topFive-articulos")
    public Flux<ReportTopProducDto> getTopArticulos(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return reportService.findTopArticulos(startDate, endDate);
    }
}

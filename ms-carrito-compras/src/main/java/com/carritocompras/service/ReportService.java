package com.carritocompras.service;

import com.carritocompras.dto.ReportTopProducDto;
import com.carritocompras.model.OrderBuy;
import com.carritocompras.model.OrderSale;
import com.carritocompras.repository.IOrderBuyRepository;
import com.carritocompras.repository.IOrderSaleRepository;
import com.carritocompras.repository.IProductRepository;
import com.carritocompras.repository.ReportTopRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReportService {
    private final IOrderBuyRepository iOrderBuyRepository;
    private final IOrderSaleRepository iOrderSaleRepository;
    private final ReportTopRepository repository;
    public Flux<OrderBuy> getOrderBuys(LocalDateTime startDate, LocalDateTime endDate) {
        return iOrderBuyRepository.findByOrderDateBetween(startDate, endDate);
    }

    public Flux<OrderSale> getOrderSales(LocalDateTime startDate, LocalDateTime endDate) {
        return iOrderSaleRepository.findByOrderDateBetween(startDate, endDate);
    }

    public Flux<ReportTopProducDto> findTopArticulos(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findTopArticulos(startDate, endDate);
    }

}

package ai_data_intelligence.controller;

import ai_data_intelligence.entity.Sale;
import ai_data_intelligence.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sale create(@RequestBody Sale sale) {
        return salesService.create(sale);
    }

    @GetMapping
    public List<Sale> findAll() {
        return salesService.findAll();
    }
}

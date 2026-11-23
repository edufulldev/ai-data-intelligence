package ai_data_intelligence.service;

import ai_data_intelligence.entity.Sale;
import ai_data_intelligence.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;

    public Sale create(Sale sale) {
        sale.setId(null);
        return salesRepository.save(sale);
    }

    public List<Sale> findAll() {
        return salesRepository.findAll();
    }
}

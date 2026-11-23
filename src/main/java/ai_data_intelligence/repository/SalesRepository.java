package ai_data_intelligence.repository;

import ai_data_intelligence.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SalesRepository extends JpaRepository<Sale, Long> {

    @Query("""
        SELECT SUM(s.unitPrice * s.quantity)
        FROM Sale s
        WHERE s.saleDate >= :startDate
          AND s.saleDate < :endExclusive
        """)
    BigDecimal calculateRevenueByPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endExclusive") LocalDateTime endExclusive
    );
}

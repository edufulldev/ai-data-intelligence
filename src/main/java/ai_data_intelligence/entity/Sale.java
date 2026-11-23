package ai_data_intelligence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private LocalDateTime saleDate;

    @PrePersist
    public void prePersist() {
        if(saleDate == null)
            saleDate = LocalDateTime.now();
    }
}

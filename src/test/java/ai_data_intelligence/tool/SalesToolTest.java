package ai_data_intelligence.tool;

import ai_data_intelligence.repository.SalesRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesToolTest {

    @Test
    void shouldRejectInvalidDate() {

        SalesRepository repository = mock(SalesRepository.class);
        SalesTool salesTool = new SalesTool(repository);

        String result = salesTool.getRevenueByPeriod(
                "2026-02-31",
                "2026-03-15"
        );
        assertTrue(result.startsWith("ERRO"));
        verifyNoInteractions(repository);
    }
    @Test
    void shouldRejectInvertedDateRange() {

        // Arrange
        SalesRepository repository = mock(SalesRepository.class);
        SalesTool salesTool = new SalesTool(repository);

        // Act
        String result = salesTool.getRevenueByPeriod(
                "2026-11-30",
                "2026-11-01"
        );

        // Assert
        assertEquals("ERRO: A data final não pode ser anterior à data inicial.", result);
        verifyNoInteractions(repository);
    }
    @Test
    void shouldReturnRevenueForValidPeriod() {

        // Arrange
        SalesRepository repository = mock(SalesRepository.class);
        SalesTool salesTool = new SalesTool(repository);

        LocalDateTime start = LocalDateTime.of(2026, 11, 1, 0, 0);
        LocalDateTime endExclusive = LocalDateTime.of(2026, 12, 1, 0, 0);

        when(repository.calculateRevenueByPeriod(start, endExclusive))
                .thenReturn(new BigDecimal("15350.00"));

        // Act
        String result = salesTool.getRevenueByPeriod(
                "2026-11-01",
                "2026-11-30"
        );

        // Assert
        assertEquals(
                "Faturamento entre 2026-11-01 e 2026-11-30: R$ 15350.00",
                result
        );

        verify(repository, times(1))
                .calculateRevenueByPeriod(start, endExclusive);
    }
    @Test
    void shouldReturnZeroWhenNoSalesExist() {

        // Arrange
        SalesRepository repository = mock(SalesRepository.class);
        SalesTool salesTool = new SalesTool(repository);

        LocalDateTime start = LocalDateTime.of(2025, 11, 1, 0, 0);
        LocalDateTime endExclusive = LocalDateTime.of(2025, 12, 1, 0, 0);

        when(repository.calculateRevenueByPeriod(start, endExclusive))
                .thenReturn(null);

        // Act
        String result = salesTool.getRevenueByPeriod(
                "2025-11-01",
                "2025-11-30"
        );

        // Assert
        assertEquals("Faturamento entre 2025-11-01 e 2025-11-30: R$ 0", result);

        verify(repository, times(1))
                .calculateRevenueByPeriod(start, endExclusive);
    }

}

package ai_data_intelligence.tool;

import ai_data_intelligence.entity.Sale;
import ai_data_intelligence.repository.SalesRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SalesTool {

    private final SalesRepository salesRepository;

    // 1. Faturamento total
    @Tool("Consulta o faturamento total das vendas cadastradas no banco de dados em reais")
    public BigDecimal getTotalSales() {

        System.out.println("SalesTool.getTotalSales() executado");

        return salesRepository.findAll()
                .stream()
                .map(sale -> sale.getUnitPrice()
                        .multiply(BigDecimal.valueOf(sale.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 2. Quantidade total vendida
    @Tool("Consulta a quantidade total de unidades vendidas de todos os produtos cadastrados")
    public Long getTotalQuantitySold() {

        System.out.println("SalesTool.getTotalQuantitySold() executado");

        return salesRepository.findAll()
                .stream()
                .mapToLong(Sale::getQuantity)
                .sum();
    }

    // 3. Faturamento agrupado por produto
    @Tool("Retorna o faturamento de todos os produtos agrupados. Use para listar os valores de cada produto. " +
            "Para identificar o produto com maior faturamento, utilize getTopRevenueProduct.")
    public Map<String, BigDecimal> getRevenueByProduct() {

        System.out.println("SalesTool.getRevenueByProduct() executado");

        Map<String, BigDecimal> revenueByProduct =
                salesRepository.findAll()
                        .stream()
                        .collect(Collectors.groupingBy(
                                Sale::getProductName,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        sale -> sale.getUnitPrice()
                                                .multiply(BigDecimal.valueOf(sale.getQuantity())),
                                        BigDecimal::add
                                )
                        ));

        System.out.println("Faturamento calculado pelo Java: " + revenueByProduct);

        return revenueByProduct;
    }

    // 4. Produto com maior faturamento
    @Tool("Identifica o produto com maior faturamento no banco de dados")
    public String getTopRevenueProduct() {

        System.out.println("SalesTool.getTopRevenueProduct() executado");

        Map<String, BigDecimal> revenueByProduct = getRevenueByProduct();

        return revenueByProduct.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> "Produto: " + entry.getKey()
                        + " | Faturamento: R$ " + entry.getValue())
                .orElse("Nenhuma venda cadastrada.");
    }

    @Tool("Consulta o faturamento de um produto específico pelo nome informado pelo usuário")
    public BigDecimal getRevenueByProductName(String productName) {

        System.out.println("SalesTool.getRevenueByProductName() executado: " + productName);

        return salesRepository.findAll()
                .stream()
                .filter(sale -> sale.getProductName().equalsIgnoreCase(productName))
                .map(sale -> sale.getUnitPrice().multiply(BigDecimal.valueOf(sale.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Tool("Consulta a quantidade total de unidades vendidas de um produto específico, identificado pelo nome")
    public Long getQuantitySoldByProductName(String productName) {

        System.out.println("SalesTool.getQuantitySoldByProductName() executado: " + productName);

        return salesRepository.findAll().stream()
                .filter(sale -> sale.getProductName().equalsIgnoreCase(productName))
                .mapToLong(Sale::getQuantity)
                .sum();
    }

    @Tool("Calcula o faturamento total das vendas realizadas entre uma data inicial e uma data final, incluindo ambas as datas.")
    public String getRevenueByPeriod(
            @P("Data inicial no formato yyyy-MM-dd") String startDate,
            @P("Data final no formato yyyy-MM-dd") String endDate) {

        System.out.println(
                "SalesTool.getRevenueByPeriod() executado: "
                        + startDate + " até " + endDate
        );

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (end.isBefore(start)) {
                return "ERRO: A data final não pode ser anterior à data inicial.";
            }

            BigDecimal revenue = salesRepository.calculateRevenueByPeriod(
                    start.atStartOfDay(),
                    end.plusDays(1).atStartOfDay()
            );

            if (revenue == null) {
                revenue = BigDecimal.ZERO;
            }

            return "Faturamento entre " + startDate
                    + " e " + endDate
                    + ": R$ " + revenue;

        } catch (DateTimeParseException e) {
            return "ERRO: Uma das datas informadas é inválida. "
                    + "Verifique se a data existe no calendário "
                    + "e se está no formato yyyy-MM-dd.";
        }
    }
}

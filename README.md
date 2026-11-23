AI Data Intelligence
Agente de IA generativa para análise de vendas em linguagem natural, desenvolvido com Java 21, Spring Boot, LangChain4j, Ollama e PostgreSQL.

O projeto demonstra como integrar um modelo de linguagem (LLM) a dados de uma aplicação corporativa por meio de Tool Calling. Em vez de inventar indicadores, 
o agente identifica a consulta solicitada, chama ferramentas Java que acessam o banco de dados e transforma os resultados em uma resposta em português.
Escopo: projeto de estudos e portfólio, executado localmente. Não é uma plataforma de BI em produção.

1. Objetivo
Construir uma API REST capaz de receber perguntas sobre vendas em linguagem natural, consultar dados persistidos no PostgreSQL e retornar respostas compreensíveis ao usuário.

Exemplos de perguntas:
- "Qual foi o faturamento total das vendas?"
- "Quantas unidades foram vendidas?"
- "Qual produto gerou o maior faturamento?"
- "Qual foi o faturamento do Notebook Dell?"
- "Qual foi o faturamento entre 1º e 30 de novembro de 2026?"

  2. Tecnologias

| Tecnologia | Utilização |
| --- | --- |
| Java 21 | Linguagem de programação |
| Spring Boot 4.1.2 | Aplicação e API REST |
| LangChain4j 1.23.0 | Integração do agente com o LLM e ferramentas |
| Ollama | Execução local do modelo de linguagem |
| Qwen 2.5 (`qwen2.5:3b`) | Interpretação das perguntas e elaboração das respostas |
| PostgreSQL 16 | Persistência dos dados de vendas |
| Spring Data JPA / Hibernate | Acesso ao banco e consultas agregadas |
| Docker Compose | Inicialização do banco de dados local |
| Maven | Build e gerenciamento de dependências |
| JUnit 5 e Mockito | Testes automatizados |
| Spring Boot Actuator | Endpoint de verificação de saúde da aplicação |

3. Arquitetura

   Responsabilidades
   | Componente | Papel |
| --- | --- |
| `AgentController` | Recebe perguntas via HTTP e devolve respostas do agente |
| `AgentService` | Orquestra a chamada à interface do agente |
| `SalesAgent` | Define o comportamento do agente com instruções de sistema e mensagem do usuário |
| `AiConfig` | Registra os beans do modelo Ollama e do agente LangChain4j |
| `SalesTool` | Disponibiliza operações Java para consultas de vendas por Tool Calling |
| `SalesRepository` | Executa consultas JPA e agregações no PostgreSQL |
| `Sale` | Representa uma venda persistida |
| `SalesController` / `SalesService` | Camada REST e de serviço das operações de vendas |

O fluxo principal é: pergunta → interpretação pelo LLM → seleção da ferramenta → consulta ao banco → resultado → resposta em linguagem natural.

4. Estrutura de pacotes
   src/
├── main/
│   ├── java/ai_data_intelligence/
│   │   ├── AiDataIntelligenceApplication.java
│   │   ├── agent/SalesAgent.java
│   │   ├── config/AiConfig.java
│   │   ├── controller/
│   │   │   ├── AgentController.java
│   │   │   └── SalesController.java
│   │   ├── entity/Sale.java
│   │   ├── repository/SalesRepository.java
│   │   ├── service/
│   │   │   ├── AgentService.java
│   │   │   └── SalesService.java
│   │   └── tool/SalesTool.java
│   └── resources/application.properties
└── test/java/ai_data_intelligence/
    ├── AiDataIntelligenceApplicationTests.java
    └── tool/SalesToolTest.java


O SalesAgent é uma interface integrada ao LangChain4j, com instruções de sistema em português e método de conversa que recebe a pergunta do usuário.

O AiConfig registra:
- Um bean ChatModel, usando OllamaChatModel com qwen2.5:3b.
- Um bean SalesAgent, criado com AiServices.builder(...) e vinculado ao SalesTool.

O SalesTool expõe métodos Java para o agente consultar indicadores, incluindo:
- Faturamento total.
- Quantidade total vendida.
- Faturamento por produto.
- Produto com maior faturamento.
- Quantidade vendida por produto.
- Faturamento entre duas datas.
Conceito central: o LLM interpreta a intenção e escolhe a ferramenta; os valores financeiros vêm das operações Java e do PostgreSQL. O modelo não deve criar números sem consulta.

Exemplo de ferramenta: faturamento por período
O método getRevenueByPeriod(startDate, endDate):
1. Recebe as datas em yyyy-MM-dd.
2. Converte os textos para LocalDate.
3. Rejeita um intervalo em que a data final é anterior à inicial.
4. Consulta o repositório com início inclusivo e fim exclusivo (endDate + 1 dia).
5. Converte um resultado null da soma para BigDecimal.ZERO.
6. Retorna o faturamento calculado ou uma mensagem de erro de validação.

Testes automatizados
Foram implementados testes com JUnit 5 e Mockito para verificar o comportamento da ferramenta de faturamento por período:
Os testes de SalesToolTest usam mocks; portanto, não comprovam, isoladamente, o funcionamento real do Ollama ou do PostgreSQL. A integração com o banco e o agente foi também exercitada manualmente via API.

O projeto permitiu praticar os principais conceitos de uma aplicação de IA generativa integrada a sistemas corporativos:
- Diferença entre um chatbot que apenas gera texto e um agente que executa ferramentas.
- Integração de LLM local sem depender de uma API de modelo hospedada externamente.
- Uso de Java e Spring Boot como camada confiável de regras e acesso a dados.
- Separação entre interpretação em linguagem natural e cálculo determinístico.
- Validação de entradas, tratamento de erros e testes automatizados.
- Organização do projeto para manutenção e publicação no GitHub.


   

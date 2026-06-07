# 📚 Estrutura de Arquitetura do Projeto Zenon Bank

## Visão Geral

O projeto foi reorganizado seguindo os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**, dividindo o código em camadas bem definidas.

## Estrutura de Pacotes

```
br.com.zenon
├── Main.java, DBMain.java, ReportMain.java, IngestorMain.java
│   └── Entry points da aplicação
│
├── domain/                         [Lógica de Negócio - Núcleo Imutável]
│   ├── model/
│   │   ├── Transaction.java        (Record com validações)
│   │   ├── TransactionType.java    (Enum de tipos de transação)
│   │   └── TransactionCustomer.java (Dados do cliente)
│   │
│   └── service/
│       └── FraudAnalyzer.java      (Análise de fraudes e estatísticas)
│
├── application/                    [Casos de Uso e Orquestração]
│   ├── ingestor/
│   │   ├── TransactionIngestor.java           (Leitura simples de CSV)
│   │   └── EfficientTransactionIngestor.java  (Leitura paralela e em batches)
│   │
│   └── report/
│       └── TransactionReport.java  (Geração de relatórios)
│
└── infrastructure/                 [Detalhes Técnicos - BD, I/O, Persistência]
    └── persistence/
        ├── repository/
        │   ├── TransactionRepository.java         (Interface)
        │   ├── TransactionListRepository.java     (Implementação em memória - List)
        │   ├── TransactionMapRepository.java      (Implementação em memória - Map)
        │   └── TransactionSQLRepository.java      (Implementação com Banco de Dados)
        │
        └── database/
            └── ConnectionFactory.java (Gerenciamento de conexões JDBC)
```

## Descrição das Camadas

### 1. **Domain** (br.com.zenon.domain)
Contém a lógica de negócio pura, independente de frameworks ou tecnologias.

- **model/**: Entidades do domínio (Transaction, TransactionType, TransactionCustomer)
  - Records imutáveis com validações de negócio
  - Não contêm dependências externas
  
- **service/**: Serviços de domínio que orquestram lógica de negócio
  - FraudAnalyzer: Análise de fraudes, cálculo de estatísticas, busca de clientes suspeitos

### 2. **Application** (br.com.zenon.application)
Implementa os casos de uso da aplicação, orquestrando entidades do domínio e utilizando infraestrutura.

- **ingestor/**: Responsável por ler e processar dados de origem
  - TransactionIngestor: Leitura simples e sequencial de CSV
  - EfficientTransactionIngestor: Leitura paralela com processamento em batches (Virtual Threads)
  
- **report/**: Geração de relatórios e estatísticas
  - TransactionReport: Processa dados e calcula métricas

### 3. **Infrastructure** (br.com.zenon.infrastructure)
Contém implementações técnicas: acesso ao BD, I/O, drivers, etc.

- **persistence/repository/**: Implementações do padrão Repository
  - Interface TransactionRepository: Define contrato de persistência
  - Implementações em memória (List e Map) para testes/cache
  - Implementação com SQL (JDBC) para persistência permanente
  
- **persistence/database/**: Gerenciamento de conexões
  - ConnectionFactory: Factory para criar conexões MySQL

## Vantagens da Nova Arquitetura

✅ **Separação de Responsabilidades**: Cada camada tem uma responsabilidade clara
✅ **Testabilidade**: Fácil criar testes unitários para o domain sem dependências externas
✅ **Manutenibilidade**: Código organizado e fácil de localizar funcionalidades
✅ **Escalabilidade**: Novos repositórios ou ingestores podem ser adicionados facilmente
✅ **Flexibilidade**: Trocar implementações (BD, formato de entrada) sem afetar o domain
✅ **Independência de Framework**: Domain não depende de Hibernate, Spring, etc.

## Fluxo de Dados

```
CSV (Arquivo) 
    ↓
TransactionIngestor / EfficientTransactionIngestor (application/ingestor)
    ↓
List<Transaction> (domain/model)
    ↓
FraudAnalyzer (domain/service)
    ↓
Resultados: fraudes, estatísticas, clientes suspeitos
    ↓
TransactionRepository (infrastructure/persistence/repository)
    ↓
BD MySQL ou Cache em Memória
```

## Como Usar

### Processar CSV e analisar fraudes (Main.java)
```java
TransactionIngestor ingestor = new TransactionIngestor();
List<Transaction> transactions = ingestor.read("arquivo.csv");

FraudAnalyzer analyzer = new FraudAnalyzer(transactions);
long fraudCount = analyzer.countFrauds();
```

### Persistir no Banco de Dados (DBMain.java)
```java
TransactionRepository repository = new TransactionSQLRepository();
repository.save(transaction);
Optional<Transaction> found = repository.findByOriginName("cliente");
```

### Processar grandes volumes em paralelo (IngestorMain.java)
```java
EfficientTransactionIngestor ingestor = new EfficientTransactionIngestor();
ingestor.readAsBatch("arquivo.csv", repository::saveAll);
```

## Próximos Passos

1. ✅ Reorganização completa em camadas
2. ⏳ Adicionar testes unitários para o domain
3. ⏳ Implementar padrão DTO (Data Transfer Objects) para a camada application
4. ⏳ Adicionar logging estruturado (SLF4J + Logback)
5. ⏳ Documentar API com JavaDoc completo

---
**Última atualização**: Junho 2026
**Versão**: 2.0 (Arquitetura refatorada)


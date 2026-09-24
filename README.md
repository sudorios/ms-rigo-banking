# 🏦 Proyecto Bancario NTT - Arquitectura de Microservicios Reactivos

Este proyecto es una solución bancaria basada en Java 17, Spring Boot 3.x y programación reactiva (WebFlux). Implementa patrones de microservicios robustos.

## 🏗️ Arquitectura del Sistema

```mermaid
graph TD
    Client[Cliente / Frontend / Postman] -->|HTTP REST| API_Gateway[API Gateway :8080]
    
    subgraph Spring Cloud Infrastructure
        Config[Config Server :8888]
        Eureka[Eureka Discovery :8761]
    end

    subgraph Business Microservices
        API_Gateway -->|Ruta| MS1(Customer Service :8081)
        API_Gateway -->|Ruta| MS2(Account Service :8082)
        API_Gateway -->|Ruta| MS3(Credit Service :8083)
        API_Gateway -->|Ruta| MS4(Transaction Service :8084)
        API_Gateway -->|Ruta| MS5(Yanki Wallet :8085)
    end
    
    MS1 & MS2 & MS3 & MS4 & MS5 -.->|Registra| Eureka
    MS1 & MS2 & MS3 & MS4 & MS5 -.->|Lee config| Config

    subgraph Data & Event Streaming
        MS1 & MS2 & MS3 & MS4 & MS5 --> MongoDB[(MongoDB :27017)]
        MS4 & MS5 --> Kafka{{Apache Kafka :9092}}
        MS4 & MS5 --> Redis[(Redis Cache :6379)]
    end
```

## 🔄 Diagramas de Secuencia

### 1. Apertura de Cuenta Pasiva con Validación
```mermaid
sequenceDiagram
    participant C as Cliente
    participant GW as API Gateway
    participant AS as Account Service
    participant CS as Customer Service
    participant CR as Credit Service
    participant DB as MongoDB

    C->>GW: POST /api/v1/bankaccounts
    GW->>AS: Enruta request
    AS->>CS: GET /api/v1/customers/{id}
    CS-->>AS: Retorna CustomerDto
    AS->>CR: GET /api/v1/credits/customer/{id}
    CR-->>AS: Retorna List<CreditDto>
    
    note over AS: Valida Deuda Vencida
    note over AS: Valida Tarjeta de Crédito (si VIP/PYME)
    note over AS: Valida Máximo 1 cuenta (si Personal)
    
    AS->>DB: save(BankAccount)
    DB-->>AS: Entidad guardada
    AS-->>GW: 201 CREATED
    GW-->>C: Response JSON
```

### 2. Transferencia a Terceros (Saga Básica)
```mermaid
sequenceDiagram
    participant C as Cliente
    participant GW as API Gateway
    participant TS as Transaction Service
    participant AS as Account Service
    participant DB as MongoDB

    C->>GW: POST /api/v1/transactions (Transferencia)
    GW->>TS: Enruta request
    TS->>AS: GET /api/v1/bankaccounts/{origen} (Valida Saldo)
    AS-->>TS: Retorna Cuenta Origen
    TS->>AS: GET /api/v1/bankaccounts/{destino} (Valida Existencia)
    AS-->>TS: Retorna Cuenta Destino
    
    note over TS: Resta saldo origen
    note over TS: Suma saldo destino
    
    TS->>AS: PUT /api/v1/bankaccounts/{origen} (Actualiza)
    TS->>AS: PUT /api/v1/bankaccounts/{destino} (Actualiza)
    TS->>DB: save(Transaction)
    DB-->>TS: Entidad guardada
    TS-->>GW: 201 CREATED
    GW-->>C: Response JSON
```

## 🚀 Cómo ejecutar el proyecto

1. **Levantar Infraestructura Base** (MongoDB, Redis, Kafka):
   ```bash
   docker-compose up -d
   ```

2. **Levantar Microservicios** (Orden recomendado):
   - `config-server` (mvn spring-boot:run)
   - `eureka-server` (mvn spring-boot:run)
   - `customer-service`, `account-service`, `credit-service`
   - `transaction-service`, `yanki-service`
   - `api-gateway`

3. **Acceder a Swagger (OpenAPI)**:
   Puedes documentarte interactuando con los endpoints a través del Gateway o directamente:
   - `http://localhost:8081/swagger-ui.html`

## 📦 Repositorios Git Independientes
Los microservicios ya tienen su repositorio local inicializado (`git init`). 
Agrega tus remotos (`git remote add origin URL`) y haz `git push`.

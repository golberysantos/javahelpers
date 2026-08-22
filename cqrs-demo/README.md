CQRS demo (Java + Spring Boot) - In-memory Event Store

Quickstart

Requirements: Java 17+, Maven

Run:

```powershell
cd D:\TOSHIBA320GB\wamp\www\gos\javahelpers\cqrs-demo
mvn spring-boot:run
```

Endpoints

- POST /orders  -> body: { "orderId": "o1", "customerId": "c1", "amount": 123.45 }
  Returns 201 Created
- GET /orders/{orderId} -> returns read model (JSON) or 404

This tiny demo shows:
- Command path: HTTP POST -> Command Handler -> InMemoryEventStore
- Projection: Event listener updates an in-memory read model
- Query path: HTTP GET -> Read Model

Notes

This is a pedagogical example — not for production. Use persistent event stores and durable brokers for real systems.

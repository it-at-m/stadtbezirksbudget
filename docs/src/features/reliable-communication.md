# Reliable communication

For this project, reliable communication between the individual components is required. The CIT form server, DBS (Zammad), and the Stadtbezirksbudget
application must
be interconnected. In particular, the reliability and fault tolerance of the communication between CIT and the application are crucial, as incoming
citizen applications must never be lost.

For this reason, we are using the Kafka event bus. It is operated centrally and with high availability, ensuring reliability without requiring the
Stadtbezirksbudget
application itself to be fault-tolerant.

```mermaid
flowchart LR
    classDef project stroke: #0f0
    cit[CIT form server]
    citEai[Stadtbezirksbudget\nCIT-EAI]
    dbs[DBS Zammad]
    dbsEai[Zammad-EAI]
    sbb[Stadtbezirksbudget\nBackend]
    kafka[(Kafka event bus)]
    cit --> citEai:::project
    citEai:::project --> kafka
    dbs --> dbsEai
    dbsEai --> kafka
    kafka --> sbb:::project
```

Only the CIT-EAI and the Stadtbezirksbudget application are maintained as part of this project. All other components are operated by other departments.

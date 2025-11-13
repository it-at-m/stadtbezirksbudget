# Dataflow diagram

The data flow diagram illustrates the various data flows within different status stages of the application.
It categorizes the flows based on whether they occur automatically or manually, indicating the responsible actor, the target system, the specific data involved, and any manual steps required in the process.

Notably, it does not display all non-automatic status transitions that must be manually set by the user (caseworker).
For a comprehensive view of all status transitions, one should refer to the [status values page](./status-values).

## Legend

The colors used in the diagram (blue and orange) correspond to those in the status diagram and are further clarified in the legend, which includes explanations of all symbols used.

![Legend-diagram showing all symbols and explaining them](./dataflow-diagram/diagram-legend.png)

## Necessary states for application-approval

### Offen

![Dataflow-diagram showing all data-flows and related information of state "Offen"](./dataflow-diagram/Offen-state.png)

### Vorbereitung

![Dataflow-diagram showing all data-flows and related information of state "Vorbereitung"](./dataflow-diagram/Vorbereitung-state.png)

### Angenommen

![Dataflow-diagram showing all data-flows and related information of state "Angenommen"](./dataflow-diagram/Angenommen-state.png)

### Bewilligt

![Dataflow-diagram showing all data-flows and related information of state "Bewilligt"](./dataflow-diagram/Bewilligt-state.png)

### Nachricht an Bürger

![Dataflow-diagram showing all data-flows and related information of state "Nachricht an Bürger"](./dataflow-diagram/NachrichtAnBuerger-state.png)

### Finanzierung

![Dataflow-diagram showing all data-flows and related information of state "Finanzierung"](./dataflow-diagram/Finanzierung-state.png)

### Abgeschlossen

![Dataflow-diagram showing all data-flows and related information of state "Abgeschlossen/Abgelehnt"](./dataflow-diagram/Abgelehnt-state.png)

## Further states

### Rückzahlung

![Dataflow-diagram showing all data-flows and related information of state "Rückzahlung"](./dataflow-diagram/Rueckzahlung-state.png)

### Warten

![Dataflow-diagram showing all data-flows and related information of state "Warten"](./dataflow-diagram/Warten-state.png)

### Zurückgewiesen

![Dataflow-diagram showing all data-flows and related information of state "Zurückgewiesen"](./dataflow-diagram/Zurueckgewiesen-state.png)

### Abgelehnt

![Dataflow-diagram showing all data-flows and related information of state "Abgeschlossen/Abgelehnt"](./dataflow-diagram/Abgelehnt-state.png)

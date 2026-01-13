package de.muenchen.stadtbezirksbudget.backend.common;

public interface AntragView {
    AntragstellerView getAntragsteller();

    ProjektView getProjekt();

    interface AntragstellerView {
        String getName();
    }

    interface ProjektView {
        String getTitel();
    }
}

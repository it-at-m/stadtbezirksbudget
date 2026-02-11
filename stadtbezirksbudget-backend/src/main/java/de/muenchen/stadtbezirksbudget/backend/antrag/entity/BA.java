package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

public enum BA {
    ALTSTADT_LEHEL(1, "Altstadt-Lehel"),
    LUDWIGSVORSTADT_ISARVORSTADT(2, "Ludwigsvorstadt-Isarvorstadt"),
    MAXVORSTADT(3, "Maxvorstadt"),
    SCHWABING_WEST(4, "Schwabing-West"),
    AU_HAIDHAUSEN(5, "Au-Haidhausen"),
    SENDLING(6, "Sendling"),
    SENDLING_WESTPARK(7, "Sendling-Westpark"),
    SCHWANTHALERHOEHE(8, "Schwanthalerhöhe"),
    NEUHAUSEN_NYMPHENBURG(9, "Neuhausen-Nymphenburg"),
    MOOSACH(10, "Moosach"),
    MILBERTSHOFEN_AM_HART(11, "Milbertshofen-Am Hart"),
    SCHWABING_FREIMANN(12, "Schwabing-Freimann"),
    BOGENHAUSEN(13, "Bogenhausen"),
    BERG_AM_LAIM(14, "Berg am Laim"),
    TRUDERING_RIEM(15, "Trudering-Riem"),
    RAMERSDORF_PERLACH(16, "Ramersdorf-Perlach"),
    OBERGIESING_FASANGARTEN(17, "Obergiesing-Fasangarten"),
    UNTERGIESING_HARLACHING(18, "Untergiesing-Harlaching"),
    THALKIRCHEN_OBERSENDLING_FORSTENRIED_FUERSTENRIED_SOLLN(19, "Thalkirchen-Obersendling-Forstenried-Fürstenried-Solln"),
    HADERN(20, "Hadern"),
    PASING_OBERMENZING(21, "Pasing-Obermenzing"),
    AUBING_LOCHHAUSEN_LANGWIED(22, "Aubing-Lochhausen-Langwied"),
    ALLACH_UNTERMENZING(23, "Allach-Untermenzing"),
    FELDMOCHING_HASENBERGL(24, "Feldmoching-Hasenbergl"),
    LAIM(25, "Laim");

    private final int nummer;
    private final String name;

    BA(final int nummer, final String name) {
        this.nummer = nummer;
        this.name = name;
    }

    public static String getNameByBerzirksausschussNr(final int bezirksausschussNr) {
        for (final BA ba : values()) {
            if (ba.nummer == bezirksausschussNr) {
                return ba.name;
            }
        }
        throw new IllegalArgumentException("Invalid Bezirksausschuss number: " + bezirksausschussNr);
    }
}

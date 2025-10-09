package de.muenchen.stadtbezirksbudget.antrag;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Finanzierungsmittel extends BaseEntity {
    private Kategorie kategorie;
    private double betrag;
    private String direktoriumNotiz;
}

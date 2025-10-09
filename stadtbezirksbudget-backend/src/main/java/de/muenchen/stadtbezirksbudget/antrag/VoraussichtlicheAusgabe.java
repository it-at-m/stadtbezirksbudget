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
public class VoraussichtlicheAusgabe extends BaseEntity {
    private String kategorie;
    private double betrag;
    private String direktoriumNotiz;
}

package de.muenchen.stadtbezirksbudget.antrag;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class Vertretungsberechtigter extends BaseEntity {
    private String nachname;
    private String vorname;
    private String telefonNr;
    private String email;
    private String mobilNr;

    @ManyToOne
    private Adresse adresse;
}

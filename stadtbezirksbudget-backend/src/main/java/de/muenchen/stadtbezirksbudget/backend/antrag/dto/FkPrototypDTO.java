package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FkPrototypDTO {
    private String nameAntragsteller;
    private String geldinstitut;
    private Integer bezirksausschussNr;
}

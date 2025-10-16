package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DummyEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private int age;
    private double weight;
}

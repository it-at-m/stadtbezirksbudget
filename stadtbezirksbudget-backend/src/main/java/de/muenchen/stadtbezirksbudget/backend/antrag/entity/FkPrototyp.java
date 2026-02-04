package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fk_prototyp")
@Getter
@Setter
public class FkPrototyp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameAntragsteller;

    private String geldinstitut;

    private Integer bezirksausschussNr;

    private LocalDateTime createdAt = LocalDateTime.now();
}
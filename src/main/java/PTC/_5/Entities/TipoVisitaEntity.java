package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBTIPOVISITA")
@Getter
@Setter
public class TipoVisitaEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqTipVis")
    @SequenceGenerator(name = "SeqTipVis", sequenceName = "SeqTipVis", allocationSize = 1)
    @Column(name = "IDTIPOVISITA")
    private Long idTipoVisita;

    @Column(name = "TIPOVISITA")
    private String tipoVisita;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @OneToMany(mappedBy = "tipoVisita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitaEntity> visitas = new ArrayList<>();
}

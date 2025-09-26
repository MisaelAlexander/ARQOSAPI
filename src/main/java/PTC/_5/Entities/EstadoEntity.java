package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TBEstado")
public class EstadoEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqEstado")
    @SequenceGenerator(name = "SeqEstado", sequenceName = "SeqEstado", allocationSize = 1)
    @Column(name = "IDEstado")
    private Long idEstado;

    @Column(name = "Estado", nullable = false)
    private String estado;

    @OneToMany(mappedBy = "estado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitaEntity> visitas = new ArrayList<>();
}

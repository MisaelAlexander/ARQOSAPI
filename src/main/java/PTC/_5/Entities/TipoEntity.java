package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TBTIPO")
public class TipoEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqTipo")
    @SequenceGenerator(name = "SeqTipo", sequenceName = "SeqTipo", allocationSize = 1)
    @Column(name = "IDTipo")
    private Long IDTipo;

    @Column(name = "Tipo")
    private String tipo;

    @OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL)
    private List<InmuebleEntity> inmuebles;
}

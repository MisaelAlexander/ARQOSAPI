package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "TBHISTORIAL")
public class HistorialEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQHIS")
    @SequenceGenerator(name = "SEQHIS", sequenceName = "SEQHIS", allocationSize = 1)
    @Column(name = "IDHistorial")
    private Long idHistorial;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDUsuario", referencedColumnName = "IDUsuario")
    private UsuarioEntity usuario;
}

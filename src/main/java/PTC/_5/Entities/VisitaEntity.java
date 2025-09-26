package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "TBVISITA")
public class VisitaEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQVIS")
    @SequenceGenerator(name = "SEQVIS", sequenceName = "SEQVIS", allocationSize = 1)
    @Column(name = "IDVISITA")
    private Long idvisita;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA")
    private Date fecha;

    @Temporal(TemporalType.TIME)
    @Column(name = "HORA")
    private LocalTime hora;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDESTADO", referencedColumnName = "IDESTADO")
    private EstadoEntity estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDINMUEBLE", referencedColumnName = "IDINMUEBLE")
    private InmuebleEntity inmueble;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDUSUARIO")
    private UsuarioEntity vendedor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDUSUARIO")
    private UsuarioEntity cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPOVISITA", referencedColumnName = "IDTIPOVISITA")
    private TipoVisitaEntity tipoVisita;
}

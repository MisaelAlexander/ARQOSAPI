package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "TBComentarios")
public class ComentariosEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQCOMIN")
    @SequenceGenerator(name = "SEQCOMIN", sequenceName = "SEQCOMIN", allocationSize = 1)
    @Column(name = "IDComentarios")
    private Long idComentario;

    @Column(name = "Comentario")
    private String comentario;

    @Column(name = "Puntuacion")
    private BigDecimal puntuacion;

    @Column(name = "Fecha")
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "IDUsuario", referencedColumnName = "IDUsuario")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "IDInmueble", referencedColumnName = "IDInmueble")
    private InmuebleEntity inmueble;
}

package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "TBCOMENTARIOSUSUARIOS")
public class ComentariosUsuarioEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQCOMUS")
    @SequenceGenerator(name = "SEQCOMUS", sequenceName = "SEQCOMUS", allocationSize = 1)
    @Column(name = "IDCOMENTARIOS")
    private Long idComentario;

    @Column(name = "COMENTARIO")
    private String comentario;

    @Column(name = "PUNTUACION")
    private BigDecimal puntuacion;

    @Column(name = "FECHA")
    private Date fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDCLIENTE")
    private UsuarioEntity cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDVENDEDOR")
    private UsuarioEntity vendedor;
}

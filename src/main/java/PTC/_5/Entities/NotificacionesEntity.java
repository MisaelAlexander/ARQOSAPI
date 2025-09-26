package PTC._5.Entities;

import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TBNOTIFICACION")
public class NotificacionesEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQNO")
    @SequenceGenerator(name = "SEQNO", sequenceName = "SEQNO", allocationSize = 1)
    @Column(name = "IDNOTIFICACION")
    private Long IDNotificacion;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDUSUARIO")
    private UsuarioEntity usuario;
}

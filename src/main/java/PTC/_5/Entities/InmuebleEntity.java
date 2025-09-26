package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TBINMUEBLE")
public class InmuebleEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQINMUEBLE")
    @SequenceGenerator(name = "SEQINMUEBLE", sequenceName = "SEQINMUEBLE", allocationSize = 1)
    @Column(name = "IDINMUEBLE")
    private Long IDInmueble;
    @Column(name = "TITULO")
    private String titulo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPO", referencedColumnName = "IDTIPO")
    private TipoEntity tipo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDUBICACION", referencedColumnName = "IDUBICACION")
    private UbicacionEntity ubicacion;
    @Column(name = "PRECIO")
    private Double Precio;
    @Column(name = "LONGITUD")
    private Double Longitud;
    @Column(name = "LATITUD")
    private Double Latitud;
    @Column(name = "DESCRIPCION")
    private String Descripcion;
    @Column(name = "ESTADO")
    private Boolean estado;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDUSUARIO", referencedColumnName = "IDUSUARIO")
    private UsuarioEntity usuario;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDBANIOS", referencedColumnName = "IDBANIOS")
    private BaniosEntity Banios;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDHABITACIONES", referencedColumnName = "IDHABITACIONES")
    private HabitacionesEntity Habitaciones;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitaEntity> visitas = new ArrayList<>();

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoritosEntity> favoritos = new ArrayList<>();


}

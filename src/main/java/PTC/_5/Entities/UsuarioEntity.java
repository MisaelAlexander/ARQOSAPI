package PTC._5.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TBUSUARIOS")
@Getter
@Setter
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUSER")
    @SequenceGenerator(name = "SEQUSER", sequenceName = "SEQUSER", allocationSize = 1)
    @Column(name = "IDUSUARIO")
    private Long IDUsuario;

    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "CONTRASENIA")
    private String contrasena;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDDescripcion", referencedColumnName = "IDDescripcion")
    private DescripcionEntity DESCRIPCION;

    @Column(name = "ESTADO")
    private Boolean estado;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHADECREACION")
    private Date fechaDeCreacion;

    @OneToMany(mappedBy = "usuario")
    private List<InmuebleEntity> inmuebles = new ArrayList<>();

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitaEntity> visitasComoVendedor = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisitaEntity> visitasComoCliente = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoritosEntity> favoritos = new ArrayList<>();

}


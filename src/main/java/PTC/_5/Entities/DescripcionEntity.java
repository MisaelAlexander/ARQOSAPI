package PTC._5.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TBDESCRIPCION")
public class DescripcionEntity
{
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDESCRIP")
    /*Me di cuenta que par que me funcionara la secuencia encesitba alocation
    * el cual empieza la sequencia con valor 1*/
    @SequenceGenerator(name = "SEQDESCRIP", sequenceName = "SEQDESCRIP", allocationSize = 1)
    @Column(name = "IDDESCRIPCION")
    private Long IDDescripcion;
    @Column(name = "NOMBRE")
    private String Nombre;
    @Column(name = "APELLIDO")
    private String Apellido;
    @Column(name = "DUI")
    private String dui;
    @ManyToOne
    @JoinColumn(name = "IDUBICACION",referencedColumnName = "IDUBICACION")
    private UbicacionEntity Ubicacion;
    @ManyToOne
    @JoinColumn(name = "IDROL", referencedColumnName = "IDROL")
    private RolEntity Rol;
    @Column(name = "DIRECCION")
    private String Direccion;
    @Column(name = "CORREO")
    private  String correo;
    @Column(name = "TELEFONO")
    private  String  telefono;
    @Column(name = "FOTOPERFIL")
    private String FotoPerfil;
    @Temporal(TemporalType.DATE)
    @Column (name = "FECHANACIMIENTO")
    private Date FechaNacimiento;
    @Column (name = "ESTADO")
    private Boolean Estado;

    //Relacion @One to One
    //ES COMO UN PUENTE PARA CONETAR ROL Y UBICAION
    @OneToOne(mappedBy = "descripcion")
    private UsuarioEntity usuario;
}

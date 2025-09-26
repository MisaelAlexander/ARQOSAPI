package PTC._5.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBUBICACION")
@Getter
@Setter
public class UbicacionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqUbicacion")
    @SequenceGenerator(name = "SeqUbicacion", sequenceName = "SeqUbicacion", allocationSize = 1)
    @Column(name = "IDUBICACION")
    private Long IDUbicacion;

    @Column(name = "UBICACION")
    private String ubicacion;

    //Relacion OnetoMany con el Descripcion
    @OneToMany(mappedBy = "Ubicacion", cascade = CascadeType.ALL)
    private List<DescripcionEntity> Ubicaciones = new ArrayList<>();
}

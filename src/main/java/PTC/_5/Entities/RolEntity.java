package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TBROL")
public class RolEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqRol")
    @SequenceGenerator(name = "SeqRol", sequenceName = "SeqRol", allocationSize = 1)
    @Column(name = "IDROL")
    private Long IDRol;

    @Column(name = "ROL")
    private  String Rol;

    //Relacion OnetoMany
    @OneToMany(mappedBy = "Rol", cascade = CascadeType.ALL)
    private List<DescripcionEntity> Roles = new ArrayList<>();
}

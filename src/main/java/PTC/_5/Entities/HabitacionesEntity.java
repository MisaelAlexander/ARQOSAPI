package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TBHABITACIONES")
public class HabitacionesEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqHabitaciones")
    @SequenceGenerator(name = "SeqHabitaciones", sequenceName = "SeqHabitaciones", allocationSize = 1)
    @Column(name = "IDHABITACIONES")
    private Long IDHabitaciones;

    @Column(name = "HABITACIONES")
    private String Habitaciones;

    @OneToMany(mappedBy = "Habitaciones", cascade = CascadeType.ALL)
    private List<InmuebleEntity> inmuebles;
}

package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TBBanios")
public class BaniosEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqBanios")
    @SequenceGenerator(name = "SeqBanios", sequenceName = "SeqBanios", allocationSize = 1)
    @Column(name = "IDBANIOS")
    private Long IDBanios;

    @Column(name = "BANIOS")
    private String Banios;

    @OneToMany(mappedBy = "Banios", cascade = CascadeType.ALL)
    private List<InmuebleEntity> inmuebles;
}

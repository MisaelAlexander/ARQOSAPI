package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "TBFOTOS")
public class FotoEntity
{
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQFOTO")
@SequenceGenerator(name = "SEQFOTO", sequenceName = "SEQFOTO", allocationSize = 1)
@Column(name = "IDFOTOS")
    private Long IDFoto;

@Column(name = "FOTOS")
    private String Fotos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDINMUEBLE", referencedColumnName = "IDINMUEBLE")
    private InmuebleEntity inmueble;
    /*idinmueble*/
}

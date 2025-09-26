package PTC._5.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TBFAVORITO")
public class FavoritosEntity
{
     @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQFAV")
        @SequenceGenerator(name = "SEQFAV", sequenceName = "SEQFAV", allocationSize = 1)
        @Column(name = "IDFAVORITO")
        private Long idfavorito;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "IDUSUARIO", referencedColumnName = "IDUSUARIO")
        private UsuarioEntity usuario;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "IDINMUEBLE", referencedColumnName = "IDINMUEBLE")
        private InmuebleEntity inmueble;


}

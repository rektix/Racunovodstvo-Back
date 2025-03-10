package raf.si.racunovodstvo.knjizenje.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Knjizenje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long knjizenjeId;
    @Column(nullable = false)
    @NotNull(message = "Broj naloga je obavezan")
    private String brojNaloga;
    @Column(nullable = false)
    @NotNull(message = "Datum je obavezna")
    private Date datumKnjizenja;
    @ManyToOne
    @JoinColumn(name = "dokument")
    private Dokument dokument;
    @OneToMany(mappedBy = "knjizenje", fetch =  FetchType.EAGER)
    private List<Konto> konto;
    @Column
    private String komentar;
}

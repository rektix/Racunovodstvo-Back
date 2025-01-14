package raf.si.racunovodstvo.knjizenje.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raf.si.racunovodstvo.knjizenje.model.*;
import raf.si.racunovodstvo.knjizenje.model.enums.TipDokumenta;
import raf.si.racunovodstvo.knjizenje.model.enums.TipFakture;
import raf.si.racunovodstvo.knjizenje.repositories.FakturaRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KontnaGrupaRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KontoRepository;


import java.util.*;

@Component
public class BootstrapData implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(BootstrapData.class);
    private final FakturaRepository fakturaRepository;
    private final KontnaGrupaRepository kontnaGrupaRepository;
    private final KontoRepository kontoRepository;
    private final KnjizenjeRepository knjizenjeRepository;


    @Autowired
    public BootstrapData(FakturaRepository fakturaRepository,
                         KontoRepository kontoRepository,
                         KontnaGrupaRepository kontnaGrupaRepository,
                         KnjizenjeRepository knjizenjeRepository
    ) {
        this.fakturaRepository = fakturaRepository;
        this.kontoRepository = kontoRepository;
        this.knjizenjeRepository = knjizenjeRepository;
        this.kontnaGrupaRepository = kontnaGrupaRepository;
    }

    private Faktura getDefaultFaktura() {

        Faktura f1 = new Faktura();
        f1.setBrojFakture("1");
        f1.setIznos(1000.00);
        f1.setTipFakture(TipFakture.ULAZNA_FAKTURA);
        f1.setDatumIzdavanja(new Date());
        f1.setDatumPlacanja(new Date());
        f1.setKurs(117.8);
        f1.setNaplata(1000.00);
        f1.setPorez(10.00);
        f1.setPorezProcenat(1.00);
        f1.setProdajnaVrednost(1000.00);
        f1.setValuta("EUR");
        f1.setBrojDokumenta("1234");
        f1.setRokZaPlacanje(new Date());
        f1.setTipDokumenta(TipDokumenta.FAKTURA);

        return f1;
    }

    private Preduzece getDefaultPreduzece() {
        Preduzece p1 = new Preduzece();
        p1.setNaziv("Test Preduzece");
        p1.setPib("111222333");
        p1.setAdresa("test adresa");
        p1.setGrad("Beograd");

        return p1;
    }

    private Konto createKonto(KontnaGrupa kg, Knjizenje knj, Double duguje, Double potrazuje) {
        Konto konto = new Konto();
        konto.setDuguje(duguje);
        konto.setPotrazuje(potrazuje);
        konto.setKnjizenje(knj);
        konto.setKontnaGrupa(kg);
        return konto;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading Data...");

        Faktura f1 = getDefaultFaktura();
        f1.setIznos(1000.00);
        f1.setTipFakture(TipFakture.IZLAZNA_FAKTURA);
        f1.setPreduzeceId(1L);

        Faktura f2 = getDefaultFaktura();
        f2.setIznos(2000.00);
        f2.setPreduzeceId(2L);

        Faktura f3 = getDefaultFaktura();
        f3.setIznos(3000.00);
        f3.setPreduzeceId(2L);
        f3.setTipFakture(TipFakture.IZLAZNA_FAKTURA);

        Faktura f4 = getDefaultFaktura();
        f4.setIznos(4000.00);
        f4.setPreduzeceId(2L);

        Faktura f5 = getDefaultFaktura();
        f5.setIznos(3000.00);
        f5.setPreduzeceId(2L);
        f5.setTipFakture(TipFakture.IZLAZNA_FAKTURA);

        this.fakturaRepository.save(f1);
        this.fakturaRepository.save(f2);
        this.fakturaRepository.save(f3);
        this.fakturaRepository.save(f4);
        this.fakturaRepository.save(f5);

        KontnaGrupa kg1 = new KontnaGrupa();
        kg1.setBrojKonta("0");
        kg1.setNazivKonta("Naziv kontne grupe 0");
        KontnaGrupa kg2 = new KontnaGrupa();
        kg2.setBrojKonta("1");
        kg2.setNazivKonta("Naziv kontne grupe 1");
        KontnaGrupa kg3 = new KontnaGrupa();
        kg3.setBrojKonta("01");
        kg3.setNazivKonta("Naziv kontne grupe 01");
        KontnaGrupa kg4 = new KontnaGrupa();
        kg4.setBrojKonta("010");
        kg4.setNazivKonta("Naziv kontne grupe 010");
        KontnaGrupa kg5 = new KontnaGrupa();
        kg5.setBrojKonta("0101");
        kg5.setNazivKonta("Naziv kontne grupe 0101");
        KontnaGrupa kg6 = new KontnaGrupa();
        kg6.setBrojKonta("01011");
        kg6.setNazivKonta("Naziv kontne grupe 01011");
        KontnaGrupa kg7 = new KontnaGrupa();
        kg7.setBrojKonta("12");
        kg7.setNazivKonta("Naziv kontne grupe 12");
        KontnaGrupa kg8 = new KontnaGrupa();
        kg8.setBrojKonta("10");
        kg8.setNazivKonta("Naziv kontne grupe 10");
        KontnaGrupa kg9 = new KontnaGrupa();
        kg9.setBrojKonta("2");
        kg9.setNazivKonta("Naziv kontne grupe 2");
        KontnaGrupa kg10 = new KontnaGrupa();
        kg10.setBrojKonta("02");
        kg10.setNazivKonta("Naziv kontne grupe 02");
        KontnaGrupa kg11 = new KontnaGrupa();
        kg11.setBrojKonta("21");
        kg11.setNazivKonta("Naziv kontne grupe 21");
        KontnaGrupa kg12 = new KontnaGrupa();
        kg12.setBrojKonta("120");
        kg12.setNazivKonta("Naziv kontne grupe 120");
        this.kontnaGrupaRepository.saveAll(Arrays.asList(kg1, kg2, kg3, kg4, kg5, kg6, kg7, kg8, kg9, kg10, kg11, kg12));

        Knjizenje knj1 = new Knjizenje();
        knj1.setDatumKnjizenja(new Date());
        Knjizenje knj2 = new Knjizenje();
        knj1.setBrojNaloga("N123S3");
        knj2.setDatumKnjizenja(new Date());
        knj2.setBrojNaloga("N123FF3");
        Knjizenje knj3 = new Knjizenje();
        knj3.setDatumKnjizenja(new Date());
        Knjizenje knj4 = new Knjizenje();
        knj4.setDatumKnjizenja(new Date());
        knj3.setBrojNaloga("N13S3");
        knj4.setBrojNaloga("N23FF3");
        this.knjizenjeRepository.save(knj1);
        this.knjizenjeRepository.save(knj2);
        this.knjizenjeRepository.save(knj3);
        this.knjizenjeRepository.save(knj4);

        Konto k1 = createKonto(kg1, knj1, 1299.0, 900.0);
        Konto k2 = createKonto(kg2, knj2, 1300.0, 848.0);
        Konto k3 = createKonto(kg3, knj3, 700.0, 940.0);
        Konto k4 = createKonto(kg4, knj4, 1000.0, 504.0);
        Konto k5 = createKonto(kg5, knj1, 1003.0, 203.0);
        Konto k6 = createKonto(kg6, knj2, 200.0, 504.0);
        Konto k7 = createKonto(kg7, knj4, 2311.0, 2003.0);
        Konto k8 = createKonto(kg8, knj3, 100.0, 504.0);
        Konto k9 = createKonto(kg9, knj2, 450.0, 304.0);
        Konto k10 = createKonto(kg10, knj3, 1030.0, 584.0);
        Konto k11 = createKonto(kg11, knj3, 1020.0, 704.0);
        Konto k12 = createKonto(kg12, knj3, 1700.0, 1504.0);
        Konto k13 = createKonto(kg1, knj4, 1090.0, 1004.0);
        Konto k14 = createKonto(kg1, knj4, 1200.0, 1504.0);
        Konto k15 = createKonto(kg11, knj4, 1430.0, 1594.0);
        Konto k16 = createKonto(kg8, knj4, 1000.0, 504.0);

        this.kontoRepository.saveAll(Arrays.asList(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10, k11, k12, k13, k14, k15, k16));

        Konto konto1 = new Konto();
        konto1.setDuguje(1000.0);
        konto1.setPotrazuje(500.0);
        konto1.setKnjizenje(knj1);
        konto1.setKontnaGrupa(kg1);
        konto1 = kontoRepository.save(konto1);

        Konto konto2 = new Konto();
        konto2.setDuguje(2000.0);
        konto2.setKnjizenje(knj1);
        konto2.setKontnaGrupa(kg1);
        konto2.setPotrazuje(1000.0);
        kontoRepository.save(konto2);

        Konto konto3 = new Konto();
        konto3.setDuguje(0.0);
        konto3.setKnjizenje(knj1);
        konto3.setKontnaGrupa(kg1);
        konto3.setPotrazuje(1000.0);
        kontoRepository.save(konto3);

        Knjizenje knjizenje = new Knjizenje();

        kontoRepository.save(konto1);
        knjizenje.setKnjizenjeId(1L);
        knjizenje.setKonto(List.of(konto1, konto2, konto3));
        knjizenje.setDatumKnjizenja(new Date());
        knjizenje.setBrojNaloga("N 1234");
        knjizenjeRepository.save(knjizenje);
        konto1.setKnjizenje(knjizenje);
        konto2.setKnjizenje(knjizenje);
        konto3.setKnjizenje(knjizenje);
        kontoRepository.save(konto1);

        log.info("Data loaded!");
    }
}

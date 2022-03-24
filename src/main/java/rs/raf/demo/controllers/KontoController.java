package rs.raf.demo.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.demo.model.Konto;
import rs.raf.demo.services.IService;
import rs.raf.demo.services.impl.KontoService;

@CrossOrigin
@RestController
@RequestMapping("/api/konto")
public class KontoController {

    private final IService<Konto, Long> kontoService;

    public KontoController(KontoService kontoService) {
        this.kontoService = kontoService;
    }
}
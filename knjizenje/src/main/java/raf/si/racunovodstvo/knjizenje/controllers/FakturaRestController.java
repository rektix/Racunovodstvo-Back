package raf.si.racunovodstvo.knjizenje.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import raf.si.racunovodstvo.knjizenje.model.Faktura;
import raf.si.racunovodstvo.knjizenje.model.FakturaWithPreduzece;
import raf.si.racunovodstvo.knjizenje.model.Preduzece;
import raf.si.racunovodstvo.knjizenje.services.FakturaService;
import raf.si.racunovodstvo.knjizenje.services.impl.IFakturaService;
import raf.si.racunovodstvo.knjizenje.utils.ApiUtil;
import raf.si.racunovodstvo.knjizenje.utils.SearchUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
@RequestMapping("/api/faktura")
public class FakturaRestController {

    private final IFakturaService fakturaService;

    private final SearchUtil<Faktura> searchUtil;

    private RestTemplate restTemplate;

    private String URL = "http://preduzece/api/preduzece/%d";

    public FakturaRestController(FakturaService fakturaService, RestTemplate restTemplate) {
        this.fakturaService = fakturaService;
        this.restTemplate = restTemplate;
        this.searchUtil = new SearchUtil<>();
    }

    private Preduzece getPreduzeceById(Long id, String token) throws IOException {
        if(id == null){
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity request = new HttpEntity(headers);

        // baca izuzetak ako nije ispravak token
        ResponseEntity<String> response = restTemplate.exchange(String.format(URL, id), HttpMethod.GET, request, String.class);
        String result = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(result, Preduzece.class);
    }

    private FakturaWithPreduzece addPreduzeceToFaktura(Faktura faktura, String token) throws IOException {
        FakturaWithPreduzece fakturaWithPreduzece = new FakturaWithPreduzece(faktura,getPreduzeceById(faktura.getPreduzeceId(), token));
        return fakturaWithPreduzece;
    }

    private List<FakturaWithPreduzece> addPreduzeceToFakturaList(List<Faktura> sveFakture, String token) throws IOException {
        List<FakturaWithPreduzece> sveFaktureWithPreduzece = new ArrayList<>();
        for(Faktura faktura:sveFakture){
            sveFaktureWithPreduzece.add(addPreduzeceToFaktura(faktura, token));
        }
        return sveFaktureWithPreduzece;
    }

    @GetMapping(value = "/sume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSume(@RequestParam String tipFakture){
        return ResponseEntity.ok(fakturaService.getSume(tipFakture));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFakture(
            @RequestParam(defaultValue = ApiUtil.DEFAULT_PAGE) @Min(ApiUtil.MIN_PAGE) Integer page,
            @RequestParam(defaultValue = ApiUtil.DEFAULT_SIZE) @Min(ApiUtil.MIN_SIZE) @Max(ApiUtil.MAX_SIZE) Integer size,
            @RequestParam(defaultValue = "dokumentId")  String[] sort,
            @RequestHeader(name="Authorization") String token
    ) throws IOException {
        Pageable pageSort = ApiUtil.resolveSortingAndPagination(page, size, sort);
        return ResponseEntity.ok(addPreduzeceToFakturaList(fakturaService.findAll(pageSort).toList(), token));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam(name = "search") String search, @RequestHeader(name="Authorization") String token) throws IOException {
        Specification<Faktura> spec = this.searchUtil.getSpec(search);
        List<Faktura> result = fakturaService.findAll(spec);
        return ResponseEntity.ok(addPreduzeceToFakturaList(result, token));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFaktura(@Valid @RequestBody Faktura faktura, @RequestHeader(name="Authorization") String token) throws IOException {
        if(getPreduzeceById(faktura.getPreduzeceId(), token)== null){
            throw new PersistenceException(String.format("Ne postoji preduzece sa id-jem %s",faktura.getPreduzeceId()));
        }
        return ResponseEntity.ok(fakturaService.save(faktura));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFaktura(@Valid @RequestBody Faktura faktura, @RequestHeader(name="Authorization") String token) throws IOException {
        Long preduzeceId = faktura.getPreduzeceId();
        if(preduzeceId != null && getPreduzeceById(faktura.getPreduzeceId(), token) == null){
            throw new PersistenceException(String.format("Ne postoji preduzece sa id-jem %s",faktura.getPreduzeceId()));
        }
        Optional<Faktura> optionalFaktura = fakturaService.findById(faktura.getDokumentId());
        if(optionalFaktura.isPresent()) {
            return ResponseEntity.ok(fakturaService.save(faktura));
        }
        throw new EntityNotFoundException();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFaktura(@PathVariable("id") Long id){
        Optional<Faktura> optionalFaktura = fakturaService.findById(id);
        if (optionalFaktura.isPresent()){
            fakturaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EntityNotFoundException();
    }
}

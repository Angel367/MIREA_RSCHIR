package com.example.egor.Controllers;

import com.example.egor.Entities.Products.Telephone;
import com.example.egor.Repositories.TelephoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/telephones")
public class TelephoneController {
    private final TelephoneRepository telephoneRepository;
    @Autowired
    public TelephoneController(TelephoneRepository telephoneRepository) {
        this.telephoneRepository = telephoneRepository;
    }
    @GetMapping
    public ResponseEntity<List<Telephone>> getAllTelephones() {
        List<Telephone> telephones = telephoneRepository.findAll();
        return new ResponseEntity<>(telephones, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Telephone> getTelephoneById(@PathVariable Long id) {
        Telephone telephone = telephoneRepository.findById(id).orElse(null);
        if (telephone != null) {
            return new ResponseEntity<>(telephone, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<Telephone> createTelephone(@RequestBody Telephone telephone) {
        Telephone savedTelephone = telephoneRepository.save(telephone);

        return new ResponseEntity<>(savedTelephone, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Telephone> updateTelephone(@PathVariable Long id, @RequestBody Telephone updatedTelephone) {
        if (telephoneRepository.existsById(id)) {
            updatedTelephone.setId(id);
            Telephone savedTelephone = telephoneRepository.save(updatedTelephone);
            return new ResponseEntity<>(savedTelephone, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTelephone(@PathVariable Long id) {
        if (telephoneRepository.existsById(id)) {
            telephoneRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

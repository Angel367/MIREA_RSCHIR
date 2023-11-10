package com.example.egor.Controllers;

import com.example.egor.Entities.Products.Telephone;
import com.example.egor.Entities.User;
import com.example.egor.Repositories.TelephoneRepository;
import com.example.egor.Controllers.AuthenticationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.List;

@RestController
@RequestMapping("/api/telephones")
public class TelephoneController {
    private final TelephoneRepository telephoneRepository;
    @Autowired
    private AuthenticationController authenticationController;

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
    public ResponseEntity<String> createTelephone(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Telephone telephone) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен", HttpStatus.FORBIDDEN);
        }
        if (Objects.equals(authenticatedUser.getRole(), "SELLER") || (Objects.equals(authenticatedUser.getRole(), "ADMIN"))) {
            telephone.setSeller(authenticatedUser);
            Telephone savedTelephone = telephoneRepository.save(telephone);
            return new ResponseEntity<>(savedTelephone.toString(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTelephone(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody Telephone updatedTelephone) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        Telephone existingTelephone;
        if (!telephoneRepository.existsById(id)) {
            return new ResponseEntity<>("Телефон не найден", HttpStatus.NOT_FOUND);
        } else {
            existingTelephone = telephoneRepository.getById(id);
        }
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен", HttpStatus.FORBIDDEN);
        }
        if (authenticatedUser.getId() != existingTelephone.getSeller().getId()) {
            return new ResponseEntity<>("Вы не являетесь продавцом этого телефона", HttpStatus.FORBIDDEN);
        }
        if (Objects.equals(authenticatedUser.getRole(), "SELLER") || (Objects.equals(authenticatedUser.getRole(), "ADMIN"))) {
            existingTelephone.setManufacturer(updatedTelephone.getManufacturer());
            existingTelephone.setProductType(updatedTelephone.getProductType());
            existingTelephone.setPrice(updatedTelephone.getPrice());
            existingTelephone.setBatteryCapacity(updatedTelephone.getBatteryCapacity());
            existingTelephone.setPhoneNumber(updatedTelephone.getPhoneNumber());
            existingTelephone.setName(updatedTelephone.getName());
            telephoneRepository.save(existingTelephone);
            return new ResponseEntity<>(existingTelephone.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTelephone(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен", HttpStatus.FORBIDDEN);
        }
        if (Objects.equals(authenticatedUser.getRole(), "SELLER") || Objects.equals(authenticatedUser.getRole(), "ADMIN")) {
            if (telephoneRepository.existsById(id)) {
                telephoneRepository.deleteById(id);
                return new ResponseEntity<>("Телефон удален", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Телефон не найден", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }
}

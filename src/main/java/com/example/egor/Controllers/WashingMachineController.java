package com.example.egor.Controllers;

import com.example.egor.Entities.Products.WashingMachine;
import com.example.egor.Entities.User;
import com.example.egor.Repositories.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/washingmachines")
public class WashingMachineController {

    private final WashingMachineRepository washingMachineRepository;
    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    public WashingMachineController(WashingMachineRepository washingMachineRepository) {
        this.washingMachineRepository = washingMachineRepository;
    }

    @GetMapping
    public ResponseEntity<List<WashingMachine>> getAllWashingMachines() {
        List<WashingMachine> washingMachines = washingMachineRepository.findAll();
        return new ResponseEntity<>(washingMachines, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WashingMachine> getWashingMachineById(@PathVariable Long id) {
        WashingMachine washingMachine = washingMachineRepository.findById(id).orElse(null);
        if (washingMachine != null) {
            return new ResponseEntity<>(washingMachine, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> createWashingMachine(@RequestHeader("Authorization") String authorizationHeader, @RequestBody WashingMachine washingMachine) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен", HttpStatus.FORBIDDEN);
        }
        if (Objects.equals(authenticatedUser.getRole(), "SELLER") || Objects.equals(authenticatedUser.getRole(), "ADMIN")) {
            washingMachine.setSeller(authenticatedUser);
            WashingMachine savedWashingMachine = washingMachineRepository.save(washingMachine);
            return new ResponseEntity<>(savedWashingMachine.toString(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateWashingMachine(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody WashingMachine updatedWashingMachine) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        WashingMachine existingWashingMachine;
        if (!washingMachineRepository.existsById(id)) {
            return new ResponseEntity<>("Стиральная машина не найдена", HttpStatus.NOT_FOUND);
        } else {
            existingWashingMachine = washingMachineRepository.getById(id);
        }
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен", HttpStatus.FORBIDDEN);
        }
        if (authenticatedUser.getId() != existingWashingMachine.getSeller().getId()) {
            return new ResponseEntity<>("Вы не являетесь продавцом этой стиральной машины", HttpStatus.FORBIDDEN);
        }
        if (Objects.equals(authenticatedUser.getRole(), "SELLER") || Objects.equals(authenticatedUser.getRole(), "ADMIN")) {
            existingWashingMachine.setManufacturer(updatedWashingMachine.getManufacturer());
            existingWashingMachine.setTankCapacity(updatedWashingMachine.getTankCapacity());
            existingWashingMachine.setPrice(updatedWashingMachine.getPrice());
            existingWashingMachine.setProductType(updatedWashingMachine.getProductType());
            existingWashingMachine.setName(updatedWashingMachine.getName());
            washingMachineRepository.save(existingWashingMachine);
            return new ResponseEntity<>(existingWashingMachine.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWashingMachine(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) {
        User authenticatedUser = authenticationController.getUserByToken(authorizationHeader);
        if (authenticatedUser == null) {
            return new ResponseEntity<>("Некорректный JWT токен", HttpStatus.FORBIDDEN);
        }
        if (Objects.equals(authenticatedUser.getRole(), "SELLER") || Objects.equals(authenticatedUser.getRole(), "ADMIN")) {
            if (washingMachineRepository.existsById(id)) {
                washingMachineRepository.deleteById(id);
                return new ResponseEntity<>("Стиральная машина удалена", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Стиральная машина не найдена", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Ваша роль не SELLER", HttpStatus.FORBIDDEN);
        }
    }
}

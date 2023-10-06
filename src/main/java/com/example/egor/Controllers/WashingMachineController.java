package com.example.egor.Controllers;

import com.example.egor.Entities.WashingMachine;
import com.example.egor.Repositories.WashingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/washingmachines")
public class WashingMachineController {

    private final WashingMachineRepository washingMachineRepository;

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
    public ResponseEntity<WashingMachine> createWashingMachine(@RequestBody WashingMachine washingMachine) {
        WashingMachine savedWashingMachine = washingMachineRepository.save(washingMachine);
        return new ResponseEntity<>(savedWashingMachine, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WashingMachine> updateWashingMachine(@PathVariable Long id, @RequestBody WashingMachine updatedWashingMachine) {
        if (washingMachineRepository.existsById(id)) {
            updatedWashingMachine.setId(id);
            WashingMachine savedWashingMachine = washingMachineRepository.save(updatedWashingMachine);
            return new ResponseEntity<>(savedWashingMachine, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWashingMachine(@PathVariable Long id) {
        if (washingMachineRepository.existsById(id)) {
            washingMachineRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

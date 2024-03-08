package org.example.virtual_wallet.controllers.Rest;

import org.example.virtual_wallet.exceptions.InvalidOperationException;
import org.example.virtual_wallet.models.dtos.DummyDto;
import org.example.virtual_wallet.services.contracts.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/dummy")
public class DummyRestController {
    private final DummyService dummyService;

    @Autowired
    public DummyRestController(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositMoney(@RequestBody DummyDto dto){
        try {
            if (dummyService.depositMoney(dto.getExpirationDate(), dto.getAmount())) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (InvalidOperationException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

    }
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestBody DummyDto dto){
        try {
            if (dummyService.withdrawMoney(dto.getExpirationDate(), dto.getAmount())) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (InvalidOperationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    private LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth localDate = YearMonth.parse(date,formatter);
        return localDate.atEndOfMonth();
    }
}

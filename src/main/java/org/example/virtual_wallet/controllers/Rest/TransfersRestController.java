package org.example.virtual_wallet.controllers.Rest;

import jakarta.validation.Valid;
import org.example.virtual_wallet.exceptions.AuthorizationException;
import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.helpers.AuthenticationHelper;
import org.example.virtual_wallet.helpers.mappers.TransactionsExternalMapper;
import org.example.virtual_wallet.models.TransactionsExternal;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.models.dtos.DummyDto;
import org.example.virtual_wallet.models.dtos.TransferDto;
import org.example.virtual_wallet.services.contracts.TransactionsExternalService;
import org.example.virtual_wallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/transfers")
public class TransfersRestController {
    private final TransactionsExternalService service;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TransactionsExternalMapper transactionsExternalMapper;
    private static final String DUMMY_END_POINT = "http://localhost:8080/api/dummy";
    private final static String INSUFFICIENT_AVAILABILITY = "Insufficient availability. Try with different amount.";


    @Autowired
    public TransfersRestController(TransactionsExternalService service,
                                   UserService userService,
                                   AuthenticationHelper authenticationHelper,
                                   TransactionsExternalMapper transactionsExternalMapper) {
        this.service = service;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.transactionsExternalMapper = transactionsExternalMapper;
    }

    @PostMapping({"/{action}"})
    public TransactionsExternal financialOperation(@RequestHeader HttpHeaders httpHeaders,
                                                   @PathVariable String action,
                                                   @Valid @RequestBody TransferDto transferDto) {
        User user = new User();

        try {
            user = authenticationHelper.tryGetUser(httpHeaders);
            TransactionsExternal transfer = new TransactionsExternal();
            switch (action) {
                case "deposit":
                    transfer = transactionsExternalMapper.depositDto(user, transferDto);
                    String depositUrl = DUMMY_END_POINT + "/deposit";
                    RestTemplate template = new RestTemplate();

                    HttpHeaders headers = new HttpHeaders();
                    HttpEntity<DummyDto> entity = new HttpEntity<>(transactionsExternalMapper
                            .transferToDummyDto(transfer), headers);
                    ResponseEntity<String> response = template.exchange(depositUrl, HttpMethod.POST, entity, String.class);
                    if (!response.getStatusCode().equals(HttpStatus.OK)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INSUFFICIENT_AVAILABILITY);
                    }

                    return service.createDeposit(transfer);
                case "withdraw":
                    transfer = transactionsExternalMapper.withdrawalDto(user, transferDto);
                    return service.createWithdrawal(transfer);
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (RestClientException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INSUFFICIENT_AVAILABILITY);
        }

        return null;
    }

    @GetMapping("/deposits")
    public List<TransactionsExternal> getDeposits(@RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getDeposits(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/withdrawals")
    public List<TransactionsExternal> getWithdrawals(@RequestHeader HttpHeaders httpHeaders) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return service.getWithdrawals(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}

package org.example.virtual_wallet;

import org.example.virtual_wallet.exceptions.EntityNotFoundException;
import org.example.virtual_wallet.models.Token;
import org.example.virtual_wallet.repositories.contracts.TokenRepository;
import org.example.virtual_wallet.services.TokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository mockRepository;

    @InjectMocks
    private TokenServiceImpl mockService;

    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAllActive();
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getByToken_Should_Return_Code_When_Match_Exists() {
        var token = Helpers.mockToken();
        //Arrange
        Mockito.when(mockRepository.getByToken(token.getCode()))
                .thenReturn(Helpers.mockToken());
        //Act
        var result = mockService.getByToken(token.getCode());
        //Assert
        Assertions.assertEquals("12345678", result.getCode());
    }

    @Test
    public void getById_Should_Throw_When_Match_Doesnt_Exist() {
        var token = Helpers.mockToken();
        //Arrange
        Mockito.when(mockRepository.getByToken(token.getCode()))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByToken(token.getCode()));
    }
    @Test
    public void getAllActive_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAllActive();
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }
    @Test
    public void getUserTokens_Should_Call_Repository() {
        //Arrange, Act
        mockService.getUserToken(Helpers.createMockUserRegular().getId());
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getUserToken(Helpers.createMockUserRegular().getId());
    }
    @Test
    public void create_Should_Call_Repository() {
        //Arrange
        var user = Helpers.createMockUserRegular();

        //Act
        mockService.create(user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Token.class));
    }

}

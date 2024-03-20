package org.example.virtual_wallet;

import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.exceptions.*;
import org.example.virtual_wallet.filters.UserFilterOptions;
import org.example.virtual_wallet.models.User;
import org.example.virtual_wallet.repositories.UserRepositoryImpl;
import org.example.virtual_wallet.repositories.contracts.UserRepository;
import org.example.virtual_wallet.services.CardServiceImpl;
import org.example.virtual_wallet.services.UserServiceImpl;
import org.example.virtual_wallet.services.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository mockRepository;

    @Mock
    private WalletServiceImpl mockWalletService;

    @Mock
    private CardServiceImpl mockCardService;


    @InjectMocks
    private UserServiceImpl mockService;


    @Test
    public void getAll_Should_Call_Repository() {
        //Arrange, Act
        mockService.getAll();
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_Return_User_When_Match_Exists() {
        //Arrange
        int id = Mockito.anyInt();
        Mockito.when(mockRepository.getById(id))
                .thenReturn(Helpers.createMockUserRegular());
        //Act
        var result = mockService.getById(id);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("mockUsername", result.getUsername());
        Assertions.assertEquals("mockmail@gmail.com", result.getEmail());
        Assertions.assertEquals("0888123456", result.getPhoneNumber());

    }
    @Test
    public void getById_Should_Throw_When_No_Match_Found() {
        //Arrange
        int id = Mockito.anyInt();
        Mockito.when(mockRepository.getById(id))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getById(id));
    }
    @Test
    public void getByUsername_Should_Return_User_When_Match_Exists() {
        //Arrange
        String username = Mockito.anyString();
        Mockito.when(mockRepository.getByUsername(username))
                .thenReturn(Helpers.createMockUserRegular());
        //Act
        var result = mockService.getByUsername(username);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("mockUsername", result.getUsername());
        Assertions.assertEquals("mockmail@gmail.com", result.getEmail());
        Assertions.assertEquals("0888123456", result.getPhoneNumber());

    }
    @Test
    public void getByUsername_Should_Throw_When_No_Match_Found() {
        //Arrange
        String username = Mockito.anyString();
        Mockito.when(mockRepository.getByUsername(username))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByUsername(username));
    }
    @Test
    public void getByEmail_Should_Return_User_When_Match_Exists() {
        //Arrange
        String email = Mockito.anyString();
        Mockito.when(mockRepository.getByEmail(email))
                .thenReturn(Helpers.createMockUserRegular());
        //Act
        var result = mockService.getByEmail(email);
        //Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("mockUsername", result.getUsername());
        Assertions.assertEquals("mockmail@gmail.com", result.getEmail());
        Assertions.assertEquals("0888123456", result.getPhoneNumber());

    }
    @Test
    public void getByEmail_Should_Throw_When_No_Match_Found() {
        //Arrange
        String email = Mockito.anyString();
        Mockito.when(mockRepository.getByEmail(email))
                .thenThrow(EntityNotFoundException.class);
        //Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> mockService.getByEmail(email));
    }
    @Test
    public void blockUser_Should_Update_User() {
        //Arrange
        var userToBlock = Helpers.createMockUserRegular();
        //Act
        mockService.blockUserByAdmin(userToBlock, Helpers.createMockUserAdmin());
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(userToBlock);
        Assertions.assertTrue(userToBlock.getRoleType().equals(RoleType.BANNED));
    }
    @Test
    public void blockUser_Should_Throw_When_Executor_Not_Admin() {
        //Arrange
        var userToBlock = Helpers.createMockUserRegular();

//        Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> mockService.blockUserByAdmin(userToBlock, Helpers.createMockUserRegular()));
    }
    @Test
    public void active_Should_Update_User() {
        //Arrange
        var userToBlock = Helpers.createMockUserRegular();
        //Act
        mockService.unblockUserByAdmin(userToBlock, Helpers.createMockUserAdmin());
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(userToBlock);
        Assertions.assertTrue(userToBlock.getRoleType().equals(RoleType.REGULAR));
    }
    @Test
    public void unBlockUser_Should_Throw_When_Executor_Not_Admin() {
        //Arrange
        var userToBlock = Helpers.createMockUserRegular();

//        Act, Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> mockService.unblockUserByAdmin(userToBlock, Helpers.createMockUserRegular()));
    }
    @Test
    public void makeAdmin_Should_Call_Repo_When_Valid() {
        //Arrange
        var executor = Helpers.createMockUserAdmin();
        var user = Helpers.createMockUserRegular();

        //Act
        mockService.promoteUserToAdmin(user, executor);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(user);
    }
    @Test
    public void filterUsers_Should_Call_Repository() {
        //Arrange
        var params = Mockito.any(UserFilterOptions.class);
        var executor = Helpers.createMockUserAdmin();

        //Act
        mockService.getAllFiltered(params,executor);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getAllFiltered(params);
    }
    @Test
    public void create_Should_Call_Repository_When_Valid_Parameters() {
        //Arrange
        var user = Helpers.createMockUserRegular();
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);
        //Act
        mockService.create(user);
        //Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(user);
    }
    @Test
    public void create_Should_Throw_When_Username_Not_Unique() {
        //Arrange
        var user = Helpers.createMockUserRegular();
        var user2 = Helpers.createMockUserRegular();
        user2.setId(2);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenReturn(user2);

        //Act, Assert
        Assertions.assertThrows(UsernameDuplicateException.class,
                () -> mockService.create(user));
    }

//    @Test
//    public void create_Should_Throw_When_Email_Not_Unique() {
//        // Arrange
//        var user = Helpers.createMockUserRegular();
//        Mockito.when(mockRepository.getByEmail(user.getEmail()))
//                .thenReturn(user);
//
//        // Act, Assert
//        Assertions.assertThrows(EmailDuplicateException.class,
//                () -> mockService.create(user));
//    }

    @Test
    public void create_Should_Throw_When_PhoneNumber_Not_Unique() {
        //Arrange
        var user = Helpers.createMockUserRegular();
        var user2 = Helpers.createMockUserRegular();
        user2.setId(2);
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByUsername(user.getUsername()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(mockRepository.getByPhoneNumber(user.getPhoneNumber()))
                .thenReturn(user2);
        //Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> mockService.create(user));
    }



}

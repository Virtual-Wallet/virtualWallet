package org.example.virtual_wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.example.virtual_wallet.enums.AccountStatus;
import org.example.virtual_wallet.enums.RoleType;
import org.example.virtual_wallet.exceptions.InvalidOperationException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "picture")
    @JsonIgnore
    private String picture;
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus accountStatus = AccountStatus.PENDING_EMAIL;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_cards",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private Set<Card> cards;


//    @ManyToOne
//    @JoinTable(
//            name = "roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Role role;
//    @OneToMany(mappedBy = "user")
//    private Set<Role>roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contacts_lists",
            joinColumns = @JoinColumn(name = "owner"),
            inverseJoinColumns = @JoinColumn(name = "member")
    )
    private Set<User> contactLists;

    public User() {
    }

    public Set<User> getContactLists() {
        return contactLists;
    }

    public void setContactLists(Set<User> contactLists) {
        this.contactLists = contactLists;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

//        public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }

    public void advanceAccountStatus(AccountStatus accountStatus) {
        if (accountStatus != AccountStatus.ACTIVE) {
            setAccountStatus(AccountStatus.values()[accountStatus.ordinal() + 1]);
        } else {
            throw new InvalidOperationException("Can't advance account status, already at " + getAccountStatus());
        }
    }

    public void revertAccountStatus(AccountStatus accountStatus) {
        if (accountStatus != AccountStatus.PENDING_EMAIL) {
            setAccountStatus(AccountStatus.values()[accountStatus.ordinal() - 1]);
        } else {
            throw new InvalidOperationException("Can't revert account status, already at " + getAccountStatus());
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return id == user.id && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}

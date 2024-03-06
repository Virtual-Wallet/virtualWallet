package org.example.virtual_wallet.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "contacts_lists")
public class ContactList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_list_id")
    private int id;


    @ManyToOne
    @JoinColumn(name = "member", referencedColumnName = "user_id")
    private User member;

    public ContactList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }
}

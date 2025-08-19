package com.SuperToni.SuperToni.client;
import com.SuperToni.SuperToni.model.Person;
import com.SuperToni.SuperToni.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Client")
public class Client extends Person{

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "phone")
    @NotEmpty
    private Integer phoneNumber;

    @Column(name = "email")
    @Email
    @NotEmpty
    private String email;

    @OneToOne(cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    
}

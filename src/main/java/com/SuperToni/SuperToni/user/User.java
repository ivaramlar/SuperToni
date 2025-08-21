package com.SuperToni.SuperToni.user;

import com.SuperToni.SuperToni.model.BaseEntity;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name =  "appusers")
public class User extends BaseEntity{
    @Column(unique = true)
    @NonNull
    @Size(min=3, max=20)
    private String userName;

    @NonNull
    private String password;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "authority")
    Authorities authority;



    
}

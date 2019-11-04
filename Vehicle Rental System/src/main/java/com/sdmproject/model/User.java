package com.sdmproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.sdmproject.orm.DatabaseTable;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable("user")
public class User {

    private int id;
    
    private String email;
    
    private String password;
    
    private String firstName;
    
    private String lastName;
    
    private int active;
    
    private Set<Role> roles;

}

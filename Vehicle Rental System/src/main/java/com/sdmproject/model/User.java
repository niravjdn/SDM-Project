package com.sdmproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.sdmproject.orm.DatabaseField;
import com.sdmproject.orm.DatabaseTable;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable("user")
public class User {

	@DatabaseField(id = true, ai = true, columnName = "user_id")
    private int id;
    
	@DatabaseField
    private String email;
    
	@DatabaseField
    private String password;
    
	@DatabaseField
    private String firstName;
    
	@DatabaseField
    private String lastName;
    
	@DatabaseField
    private int active;
    
    private Set<Role> roles;

}

package com.sdmproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.sdmproject.orm.DatabaseTable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable("role")
public class Role {
    private int id;
    private String role;
}

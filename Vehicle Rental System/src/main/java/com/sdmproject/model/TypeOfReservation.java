package com.sdmproject.model;

import java.util.Arrays;
import java.util.Optional;

public enum TypeOfReservation {
	RENTAL(0), RESERVATION(1);
	
	 private int value; 
	  
    // getter method 
    public int getValue() 
    { 
        return this.value; 
    } 
    
    private TypeOfReservation(int value) 
    { 
        this.value = value; 
    } 
    
    public static Optional<TypeOfReservation> valueOf(int value) {
        return Arrays.stream(values())
            .filter(item -> item.value == value)
            .findFirst();
    }
}

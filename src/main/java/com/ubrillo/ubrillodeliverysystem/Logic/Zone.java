package com.ubrillo.ubrillodeliverysystem.Logic;


public enum Zone {
    NORTHLONDON("northlondon"), SOUTHLONDON("southlondon"),
    WESTLONDON("westlondon"), EASTLONDON("eastlondon");
    private String zone;
    private  Zone(String st){zone = st;}
    public String toString(){return zone;}
}
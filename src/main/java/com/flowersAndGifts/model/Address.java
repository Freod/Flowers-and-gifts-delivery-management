package com.flowersAndGifts.model;

import java.util.Objects;

public class Address {
    private String country;
    private String city;
    private String homeNumber;
    private String postCode;

    public Address(String country, String city, String homeNumber, String postCode) {
        this.country = country;
        this.city = city;
        this.homeNumber = homeNumber;
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) && Objects.equals(city, address.city) && Objects.equals(homeNumber, address.homeNumber) && Objects.equals(postCode, address.postCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, homeNumber, postCode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", homeNumber='" + homeNumber + '\'' +
                ", postCode='" + postCode + '\'' +
                '}';
    }
}

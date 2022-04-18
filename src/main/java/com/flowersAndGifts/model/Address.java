package com.flowersAndGifts.model;

import java.util.Objects;

public class Address {
    private String country;
    private String address;
    private String city;
    private String postcode;

    public Address(String country, String address, String city, String postcode) {
        this.country = country;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return Objects.equals(country, address1.country) && Objects.equals(address, address1.address) && Objects.equals(city, address1.city) && Objects.equals(postcode, address1.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, address, city, postcode);
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}

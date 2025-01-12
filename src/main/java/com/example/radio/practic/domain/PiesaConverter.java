package com.example.radio.practic.domain;

public class PiesaConverter extends EntityConverter<Piesa>{
    @Override
    public String toString(Piesa piesa) {
        return piesa.toString();
    }

    @Override
    public Piesa fromString(String string) {
        String[] parts = string.split(",");
        int id = Integer.parseInt(parts[0]);
        String formatie = parts[1];
        String titlu = parts[2];
        String gen = parts[3];
        String durata = parts[4];

        return new Piesa(id, formatie, titlu, gen, durata);
    }
}

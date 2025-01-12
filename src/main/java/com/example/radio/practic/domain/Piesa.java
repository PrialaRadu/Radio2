package com.example.radio.practic.domain;

public class Piesa extends Entity{
    private String formatie;
    private String titlu;
    private String gen;
    private String durata;

    public Piesa(int id, String formatie, String titlu, String gen, String durata) {
        super(id);
        this.formatie = formatie;
        this.titlu = titlu;
        this.gen = gen;
        this.durata = durata;
    }

    public String getFormatie() {
        return formatie;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getGen() {
        return gen;
    }

    public String getDurata() {
        return durata;
    }

    @Override
    public String toString() {
        return "Piesa{" +
               "formatie=" + formatie +
               ", titlu=" + titlu +
               ", gen=" + gen +
               ", durata=" + durata +
                '}' + '\n';
    }
}

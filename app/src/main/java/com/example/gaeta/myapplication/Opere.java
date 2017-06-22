package com.example.gaeta.myapplication;

/**
 * PJDCC - Classe che modella l'oggetto opera.
 *
 * @authors Oranger Edoardo, Settembre Gaetano, Recchia Vito, Marchese Vito
 * @version 1.0
 */
public class Opere {

    private String id;
    private String Titolo;
    private String Autore;
    private String Corrente;
    private String Anno;
    private String Categoria;
    private String Dimensioni;
    private String Immagine;
    private String Descrizione;


    public void setID(String id) {
        this.id = id;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public void setAutore(String autore) {
        Autore = autore;
    }

    public void setCorrente(String corrente) {
        Corrente = corrente;
    }

    public void setAnno(String anno) {
        Anno = anno;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public void setDimensioni(String dimensioni) {
        Dimensioni = dimensioni;
    }

    public void setImmagine(String immagine) {
        Immagine = immagine;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public String getId() {
        return id;
    }

    public String getTitolo() {
        return Titolo;
    }

    public String getAutore() {
        return Autore;
    }

    public String getCorrente() {
        return Corrente;
    }

    public String getAnno() {
        return Anno;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getDimensioni() {
        return Dimensioni;
    }

    public String getImmagine() {
        return Immagine;
    }

    public String getDescrizione() {
        return Descrizione;
    }
}



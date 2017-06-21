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


    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public String getTitolo() {
        return Titolo;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public String getAutore() {
        return Autore;
    }

    public void setAutore(String autore) {
        Autore = autore;
    }

    public String getCorrente() {
        return Corrente;
    }

    public void setCorrente(String corrente) {
        Corrente = corrente;
    }

    public String getAnno() {
        return Anno;
    }

    public void setAnno(String anno) {
        Anno = anno;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getDimensioni() {
        return Dimensioni;
    }

    public void setDimensioni(String dimensioni) {
        Dimensioni = dimensioni;
    }

    public String getImmagine() {
        return Immagine;
    }

    public void setImmagine(String immagine) {
        Immagine = immagine;
    }
}

package com.example.navigationdrawer.ui.tienda;

public class RecompensaInfo {

    String nombre;
    String descripcion;
    int plannerCoins;
    int imagen;
    boolean adquirida;

    public final static String plannerCoinsAux = "PC :";

    // De cada recompensa almacenamos su nombre, su descripcion, su coste en plannerCoins, su imagen y si ha sido comprada o no
    public RecompensaInfo(String nombre, String descripcion, int plannerCoins, int imagen, boolean adquirida){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.plannerCoins = plannerCoins;
        this.imagen = imagen;
        this.adquirida = adquirida;
    }

    /*  Conjunto de Getters y Setters */

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPlannerCoins() {
        return plannerCoins;
    }

    public void setPlannerCoins(int plannerCoins) {
        this.plannerCoins = plannerCoins;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public boolean isAdquirida() {
        return adquirida;
    }

    public void setAdquirida(boolean adquirida) {
        this.adquirida = adquirida;
    }
}

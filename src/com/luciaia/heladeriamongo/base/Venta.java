package com.luciaia.heladeriamongo.base;

import org.bson.types.ObjectId;

public class Venta {

    private ObjectId id;
    private int cantidad;
    private double precioTotal;
    private ObjectId id_helado;
    private String nombreHelado;

    public Venta(int cantidad, double precioTotal, ObjectId id_helado, String nombreHelado) {
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.id_helado = id_helado;
        this.nombreHelado = nombreHelado;
    }

    public Venta() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public ObjectId getId_helado() {
        return id_helado;
    }

    public void setId_helado(ObjectId id_helado) {
        this.id_helado = id_helado;
    }

    public String getNombreHelado() {
        return nombreHelado;
    }

    public void setNombreHelado(String nombreHelado) {
        this.nombreHelado = nombreHelado;
    }

    @Override
    public String toString() {
        return nombreHelado + " x " + cantidad + "uds = " + precioTotal + "€";
    }
}

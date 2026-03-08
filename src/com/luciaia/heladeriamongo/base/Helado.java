package com.luciaia.heladeriamongo.base;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Date;

public class Helado {
    private ObjectId id;
    private String nombre;
    private double precio;
    private LocalDate fechaApertura;
    private LocalDate fechaCaducidad;
    private String sabor;
    private boolean azucar;
    private double litros;
    private ObjectId id_proveedor;
    private String nombreProveedor;

    public Helado(String nombre, double precio, LocalDate fechaApertura, LocalDate fechaCaducidad, String sabor, boolean azucar, double litros, ObjectId id_proveedor, String nombreProveedor) {
        this.nombre = nombre;
        this.precio = precio;
        this.fechaApertura = fechaApertura;
        this.fechaCaducidad = fechaCaducidad;
        this.sabor = sabor;
        this.azucar = azucar;
        this.litros = litros;
        this.id_proveedor = id_proveedor;
        this.nombreProveedor = nombreProveedor;
    }

    public Helado() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public boolean isAzucar() {
        return azucar;
    }

    public void setAzucar(boolean azucar) {
        this.azucar = azucar;
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }

    public ObjectId getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(ObjectId id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    @Override
    public String toString() {
        return nombre + " | " + precio + "€ | lote: " + fechaApertura + " | cad: " + fechaCaducidad + " | " + sabor + " | " + azucar + " | " + litros + "L | " + nombreProveedor;
    }
}

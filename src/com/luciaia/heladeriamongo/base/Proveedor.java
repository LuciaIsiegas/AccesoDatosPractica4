package com.luciaia.heladeriamongo.base;

import org.bson.types.ObjectId;

public class Proveedor {
    private ObjectId id;
    private String nombre;
    private String personaContacto;
    private String email;
    private String telefono;
    private String direccion;

    public Proveedor(String nombre, String personaContacto, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.personaContacto = personaContacto;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Proveedor() {
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

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return nombre + " | " + personaContacto + " | " + email + " | " + telefono + " | " + direccion;
    }
}

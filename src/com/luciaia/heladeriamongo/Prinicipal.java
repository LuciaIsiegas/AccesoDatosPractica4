package com.luciaia.heladeriamongo;

import com.luciaia.heladeriamongo.gui.Controlador;
import com.luciaia.heladeriamongo.gui.Modelo;
import com.luciaia.heladeriamongo.gui.Vista;

public class Prinicipal {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo,vista);
    }
}

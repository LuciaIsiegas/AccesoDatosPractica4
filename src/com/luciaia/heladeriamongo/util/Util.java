package com.luciaia.heladeriamongo.util;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {
    /**
     * Metodo que formatea la fecha
     * @param fecha Fecha a formatear
     * @return Fecha formateada
     */
    public static String formatearFecha(LocalDate fecha) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formateador.format(fecha);
    }

    /**
     * Metodo que parsea la fecha
     * @param fecha Fecha a parsear
     * @return fecha parseada
     */
    public static LocalDate parsearFecha(String fecha){
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(fecha, formateador);
    }

    public static void mensajeError(String msg, String titulo) {
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public static void mensajeInfo(String msg, String titulo) {
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static int mensajeConfirmación(String msg, String titulo) {
        return JOptionPane.showConfirmDialog(null, msg, titulo, JOptionPane.YES_NO_OPTION);
    }

    public static boolean esFloat(String numero) {
        try {
            float decimal = Float.parseFloat(numero);
            return decimal > 0;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    public static boolean esEntero(String numero) {
        try {
            float entero = Integer.parseInt(numero);
            return entero > 0;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    public static boolean validarTelefono(String telefono) {
        return telefono.isEmpty() || telefono.matches("\\d{9}");
    }

    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailClean = email.trim();
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return emailClean.matches(regex);
    }

    public static boolean consultaValida(String input) {
        return !input.contains(";") && !input.toLowerCase().contains("delete");
    }
}

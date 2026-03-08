package com.luciaia.heladeriamongo.gui;

import com.luciaia.heladeriamongo.base.Helado;
import com.luciaia.heladeriamongo.base.Proveedor;
import com.luciaia.heladeriamongo.base.Venta;
import com.luciaia.heladeriamongo.util.Util;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controlador implements ActionListener, KeyListener, ListSelectionListener {
    Vista vista;
    Modelo modelo;
    boolean editando;
    boolean conectado;
    boolean filtroProducto;

    /**
     * Constructor de la clase Controlador
     *
     * @param modelo Modelo del programa
     * @param vista  Vista del programa
     */
    public Controlador(Modelo modelo, Vista vista) {
        this.vista = vista;
        this.modelo = modelo;
        this.editando = false;
        this.filtroProducto = false;

        conectar();
        conectado = true;

        addActionListeners(this);
        refrescarTodo();
    }

    /**
     * Metodo que conecta con la base de datos
     */
    private void conectar() {
        if (modelo.getCliente() == null) {
            modelo.conectar();
        }
    }

    // COMPROBAR CAMPOS OBLIGATORIOS ---------------------------------------------------------------------------------------

    /***
     * Comrpueba si hay campos vacios en producto
     */
    private boolean hayCamposVaciosProducto() {
        try {
            if (!vista.txtNombreProducto.getText().isEmpty()
                    && !vista.txtPrecioProducto.getText().isEmpty()
                    && !vista.dateCaducidad.getText().isEmpty()
                    && !vista.comboProveedor.getSelectedItem().toString().isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException ne) {
            return true;
        }
    }

    /***
     * Comrpueba si hay campos vacios en helado
     */
    private boolean hayCamposVaciosHelado() {
        try {
            if (!vista.panelHelado.comboSabor.getSelectedItem().toString().isEmpty()
                    && !vista.panelHelado.litrosHeladoTxt.getText().isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException ne) {
            return true;
        }
    }

    /***
     * Comrpueba si hay campos vacios en proveedor
     */
    private boolean hayCamposVaciosProveedor() {
        return vista.txtNombreProveedor.getText().isEmpty()
                || vista.txtContactoProveedor.getText().isEmpty()
                || vista.txtEmailProveedor.getText().isEmpty();
    }

    /***
     * Comrpueba si hay campos vacios en venta
     */
    private boolean hayCamposVaciosVenta() {
        try {
            if (!vista.comboHelado.getSelectedItem().toString().isEmpty()
                    && !vista.txtCantidadVenta.getText().isEmpty()) {
                return false;
            }
            return true;
        } catch (NullPointerException ne) {
            return true;
        }
    }

    /***
     * Valida los campos en helado
     */
    private boolean validarCamposProducto() {
        // Consultas válidas
        if (!Util.consultaValida(vista.txtNombreProducto.getText())
                || !Util.consultaValida(vista.txtPrecioProducto.getText())
                || !Util.consultaValida(vista.txtNombreProducto.getText())
                || !Util.consultaValida(vista.panelHelado.litrosHeladoTxt.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosProducto() || hayCamposVaciosHelado()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.esFloat(vista.txtPrecioProducto.getText())) {
            Util.mensajeError("Precio en formato de número decimal o entero (positivo)", "Campos Incorrectos");
            return false;
        }

        if (!Util.esFloat(vista.panelHelado.litrosHeladoTxt.getText())) {
            Util.mensajeError("Litros en formato de número decimal o entero (positivo)", "Campos Incorrectos");
            return false;
        }
        return true;
    }

    /***
     * Valida los campos en proveedor
     */
    private boolean validarCamposProveedor() {
        // Consultas válidas
        if (!Util.consultaValida(vista.txtNombreProveedor.getText())
                || !Util.consultaValida(vista.txtContactoProveedor.getText())
                || !Util.consultaValida(vista.txtEmailProveedor.getText())
                || !Util.consultaValida(vista.txtTelefonoProveedor.getText())
                || !Util.consultaValida(vista.txtDireccionProveedor.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosProveedor()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarEmail(vista.txtEmailProveedor.getText())) {
            Util.mensajeError("El email del proveedor no tiene un formato correcto", "Campos Incorrectos");
            return false;
        }

        if (!Util.validarTelefono(vista.txtTelefonoProveedor.getText())) {
            Util.mensajeError("El teléfono del proveedor no tiene un formato correcto (9 dígitos)", "Campos Incorrectos");
            return false;
        }
        return true;
    }

    /***
     * Valida los campos en ventaProducto
     */
    private boolean validarCamposVenta() {
        // Consultas válidas
        if (!Util.consultaValida(vista.txtCantidadVenta.getText())) {
            Util.mensajeError("Por favor introduzca correctamente los datos", "Campos Erróneos");
            return false;
        }

        if (hayCamposVaciosVenta()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return false;
        }

        if (!Util.esEntero(vista.txtCantidadVenta.getText())) {
            Util.mensajeError("Cantidad en formato de número entero (positivo)", "Campos Incorrectos");
            return false;
        }
        return true;
    }
    // COMPROBAR CAMPOS OBLIGATORIOS FIN ---------------------------------------------------------------------------------------


    // LIMPIAR PANELES ---------------------------------------------------------------------------------------

    /***
     * Resetea el panel de Producto
     */
    private void resetearProducto() {
        vista.txtNombreProducto.setEditable(true);

        vista.listHelado.setEnabled(true);
        vista.btnGuardarProducto.setVisible(false);
        vista.btnCancelarProducto.setVisible(false);
        vista.btnEditaProducto.setEnabled(true);
        vista.btnNuevoProducto.setEnabled(true);
        vista.btnBorrarBBDDProducto.setEnabled(true);
        vista.btnLimpiarProducto.setEnabled(true);

        limpiarCamposProducto();
        limpiarCamposHelado();
        refrescarProducto();
        editando = false;
    }

    /***
     * Resetea el panel de proveedor
     */
    private void resetearProveedor() {
        vista.txtNombreProveedor.setEditable(true);
        vista.listProveedor.setEnabled(true);
        vista.lisHeladoProveedor.setEnabled(true);
        vista.btnGuardarProveedor.setVisible(false);
        vista.btnCancelarProveedor.setVisible(false);
        vista.btnEditarProveedor.setEnabled(true);
        vista.btnNuevoProveedor.setEnabled(true);
        vista.btnLimpiarProveedor.setEnabled(true);
        vista.btnBorrarBBDDProveedor.setEnabled(true);

        limpiarCamposProveedor();
        refrescarProveedor();
        editando = false;
    }

    /***
     * Resetea el panel de venta
     */
    private void resetearVenta() {
        vista.listVenta.setEnabled(true);
        vista.btnGuardarVenta.setVisible(false);
        vista.btnCancelarVenta.setVisible(false);
        vista.btnEditarVenta.setEnabled(true);
        vista.btnNuevoVenta.setEnabled(true);
        vista.btnLimpiarVenta.setEnabled(true);
        vista.btnBorrarBBDDVenta.setEnabled(true);

        limpiarCamposVenta();
        refrescarVenta();
        editando = false;
    }

    /***
     * Limpiar los campos de producto
     */
    private void limpiarCamposProducto() {
        vista.txtNombreProducto.setText(null);
        vista.txtPrecioProducto.setText(null);
        vista.comboProveedor.setSelectedIndex(-1);
        vista.dateApertura.setText(null);
        vista.dateCaducidad.setText(null);
    }

    /***
     * Limpiar los campos de helado
     */
    private void limpiarCamposHelado() {
        vista.panelHelado.comboSabor.setSelectedIndex(-1);
        vista.panelHelado.conAzucarRadioButton.setSelected(true);
        vista.panelHelado.litrosHeladoTxt.setText(null);
    }

    /***
     * Limpiar los campos de proveedor
     */
    private void limpiarCamposProveedor() {
        vista.txtNombreProveedor.setText(null);
        vista.txtContactoProveedor.setText(null);
        vista.txtEmailProveedor.setText(null);
        vista.txtTelefonoProveedor.setText(null);
        vista.txtDireccionProveedor.setText(null);
    }

    /***
     * Limpiar los campos de venta
     */
    private void limpiarCamposVenta() {
        vista.comboHelado.setSelectedIndex(-1);
        vista.txtCantidadVenta.setText(null);
    }
    // LIMPIAR PANELES FIN ---------------------------------------------------------------------------------------


    // REFRESCAR TABLAS ---------------------------------------------------------------------------------------

    /***
     * Actualizar los campos
     */
    private void refrescarTodo() {
        refrescarProveedor();
        refrescarProducto();
        refrescarVenta();
    }

    /***
     * Listar los proveedores
     */
    private void refrescarProveedor() {
        vista.dlmProveedor.clear();
        ArrayList<Proveedor> lista = modelo.getProveedores();
        for (Proveedor proveedor : lista) {
            vista.dlmProveedor.addElement(proveedor);
        }
        vista.comboProveedor.removeAllItems();
        ArrayList<Proveedor> prov = modelo.getProveedores();

        for (Proveedor proveedor : prov) {
            vista.comboProveedor.addItem(proveedor);
        }
        vista.comboProveedor.setSelectedIndex(-1);
    }

    /***
     * Listar los helados por proveedor
     */
    private void listarHeladosProveedor() {
        int filaEditando = vista.listProveedor.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún proveedor seleccionado", "Selecciona un proveedor");
            return;
        }
        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        vista.dlmHeladoProveedor.clear();
        ArrayList<Helado> lista = modelo.getHeladosProveedor(proveedor.getId());
        for (Helado helado : lista) {
            vista.dlmHeladoProveedor.addElement(helado);
        }
    }

    /***
     * Listar los helados
     */
    private void refrescarProducto() {
        filtroProducto = false;
        vista.dlmProducto.clear();
        ArrayList<Helado> lista = modelo.getHelados();
        for (Helado helado : lista) {
            vista.dlmProducto.addElement(helado);
        }
        vista.comboHelado.removeAllItems();
        ArrayList<Helado> hel = modelo.getHelados();

        for (Helado helado : hel) {
            vista.comboHelado.addItem(helado);
        }
        vista.comboHelado.setSelectedIndex(-1);
    }

    /***
     * Listar las ventas
     */
    private void refrescarVenta() {
        ArrayList<Venta> lista = modelo.getVentas();
        vista.dlmVenta.clear();
        for (Venta venta : lista) {
            vista.dlmVenta.addElement(venta);
        }
    }
    // REFRESCAR TABLAS FIN ---------------------------------------------------------------------------------------


    // NUEVO -----------------------------------------------------------------------------------------

    /***
     * Crea un helado
     */
    private void nuevoProducto() {
        if (!validarCamposProducto()) {
            return;
        }
        Helado helado = new Helado();
        helado.setNombre(vista.txtNombreProducto.getText());
        double precio = Double.parseDouble(vista.txtPrecioProducto.getText());
        precio = Math.round(precio * 100.0) / 100.0; // redondea a 2 decimales
        helado.setPrecio(precio);
        helado.setFechaApertura(vista.dateApertura.getDate());
        helado.setFechaCaducidad(vista.dateCaducidad.getDate());
        Proveedor proveedor = (Proveedor) vista.comboProveedor.getSelectedItem();
        helado.setId_proveedor(proveedor.getId());
        helado.setNombreProveedor(proveedor.getNombre());
        helado.setSabor(vista.panelHelado.comboSabor.getSelectedItem().toString());
        helado.setAzucar(vista.panelHelado.conAzucarRadioButton.isSelected());
        double litros = Double.parseDouble(vista.panelHelado.litrosHeladoTxt.getText());
        litros = Math.round(litros * 100.0) / 100.0; // redondea a 2 decimales
        helado.setLitros(litros);
        modelo.guardarObjeto(helado);

        resetearProducto();
        Util.mensajeInfo("Se ha añadido un nuevo helado", "Nuevo Helado");
    }

    /***
     * Crea un proveedor
     */
    private void nuevoProveedor() {
        if (!validarCamposProveedor()) {
            return;
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(vista.txtNombreProveedor.getText());
        proveedor.setPersonaContacto(vista.txtContactoProveedor.getText());
        proveedor.setEmail(vista.txtEmailProveedor.getText());
        proveedor.setTelefono(vista.txtTelefonoProveedor.getText());
        proveedor.setDireccion(vista.txtDireccionProveedor.getText());
        modelo.guardarObjeto(proveedor);

        resetearProveedor();
        Util.mensajeInfo("Se ha añadido un nuevo proveedor", "Nuevo Proveedor");
    }

    /***
     * Crea una venta
     */
    private void nuevoVenta() {
        if (hayCamposVaciosVenta()) {
            Util.mensajeError("Por favor rellene los campos obligatorios", "Campos Incorrectos");
            return;
        }
        Venta venta = new Venta();
        Helado helado = (Helado) vista.comboHelado.getSelectedItem();
        int cantidad = Integer.parseInt(vista.txtCantidadVenta.getText());
        venta.setId_helado(helado.getId());
        venta.setNombreHelado(helado.getNombre());
        venta.setCantidad(cantidad);
        double total = cantidad * helado.getPrecio();
        total = Math.round(total * 100.0) / 100.0; // redondea a 2 decimales
        venta.setPrecioTotal(total);
        modelo.guardarObjeto(venta);

        resetearVenta();
        Util.mensajeInfo("Se ha añadido una nueva venta", "Nueva Venta");
    }
    // NUEVO FIN -----------------------------------------------------------------------------------------


    // EDITAR -----------------------------------------------------------------------------------------

    /***
     * Modifica un helado
     */
    private void editarProducto() {
        int filaEditando = vista.listHelado.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún producto seleccionado", "Selecciona un producto");
            return;
        }
        Helado helado = (Helado) vista.listHelado.getSelectedValue();
        vista.txtNombreProducto.setText(helado.getNombre());
        vista.txtPrecioProducto.setText(String.valueOf(helado.getPrecio()));
        vista.dateApertura.setDate(LocalDate.parse(String.valueOf(helado.getFechaApertura())));
        vista.dateCaducidad.setDate(LocalDate.parse(String.valueOf(helado.getFechaCaducidad())));

        Proveedor proveedor = modelo.getProveedorPorId(helado.getId_proveedor());
        vista.comboProveedor.setEditable(true);
        vista.comboProveedor.setSelectedItem(proveedor);
        vista.comboProveedor.setEditable(false);
        vista.panelHelado.comboSabor.setSelectedItem(helado.getSabor());
        if (helado.isAzucar()) {
            vista.panelHelado.conAzucarRadioButton.setSelected(true);
        } else {
            vista.panelHelado.sinAzucarRadioButton.setSelected(true);
        }
        vista.panelHelado.litrosHeladoTxt.setText(String.valueOf(helado.getLitros()));

        vista.listHelado.setEnabled(false);
        vista.txtNombreProducto.setEditable(false);
        vista.btnGuardarProducto.setVisible(true);
        vista.btnCancelarProducto.setVisible(true);
        vista.btnEditaProducto.setEnabled(false);
        vista.btnNuevoProducto.setEnabled(false);
        vista.btnBorrarBBDDProducto.setEnabled(false);
        vista.btnLimpiarProducto.setEnabled(false);
        editando = true;
    }

    /***
     * Modifica un proveedor
     */
    private void editarProveedor() {
        int filaEditando = vista.listProveedor.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ningún proveedor seleccionado", "Selecciona un proveedor");
            return;
        }

        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        vista.txtNombreProveedor.setText(proveedor.getNombre());
        vista.txtContactoProveedor.setText(proveedor.getPersonaContacto());
        vista.txtEmailProveedor.setText(proveedor.getEmail());
        vista.txtTelefonoProveedor.setText(proveedor.getTelefono());
        vista.txtDireccionProveedor.setText(proveedor.getDireccion());

        vista.listProveedor.setEnabled(false);
        vista.txtNombreProveedor.setEditable(false);

        vista.btnGuardarProveedor.setVisible(true);
        vista.btnCancelarProveedor.setVisible(true);
        vista.btnEditarProveedor.setEnabled(false);
        vista.btnNuevoProveedor.setEnabled(false);
        vista.btnBorrarBBDDProveedor.setEnabled(false);
        vista.btnLimpiarProveedor.setEnabled(false);
        editando = true;
    }

    /***
     * Modifica una venta
     */
    private void editarVenta() {
        int filaEditando = vista.listVenta.getSelectedIndex();
        if (filaEditando < 0) {
            Util.mensajeError("No hay ninguna venta seleccionado", "Selecciona una venta");
            return;
        }

        Venta venta = (Venta) vista.listVenta.getSelectedValue();
        Helado helado = modelo.getHeladoPorId(venta.getId_helado());
        vista.comboHelado.setEditable(true);
        vista.comboHelado.setSelectedItem(helado);
        vista.comboHelado.setEditable(false);
        vista.txtCantidadVenta.setText(String.valueOf(venta.getCantidad()));

        vista.listVenta.setEnabled(false);

        vista.btnGuardarVenta.setVisible(true);
        vista.btnCancelarVenta.setVisible(true);
        vista.btnEditarVenta.setEnabled(false);
        vista.btnNuevoVenta.setEnabled(false);
        vista.btnBorrarBBDDVenta.setEnabled(false);
        vista.btnLimpiarVenta.setEnabled(false);
        editando = true;
    }
    // EDITAR FIN -----------------------------------------------------------------------------------------


    // GUARDAR -----------------------------------------------------------------------------------------

    /***
     * Guarda las modificaciones en un helado
     */
    private void guardarProducto() {
        if (!validarCamposProducto()) {
            return;
        }
        Helado helado = (Helado) vista.listHelado.getSelectedValue();
        helado.setNombre(vista.txtNombreProducto.getText());
        double precio = Double.parseDouble(vista.txtPrecioProducto.getText());
        precio = Math.round(precio * 100.0) / 100.0; // redondea a 2 decimales
        helado.setPrecio(precio);
        helado.setFechaApertura(vista.dateApertura.getDate());
        helado.setFechaCaducidad(vista.dateCaducidad.getDate());
        Proveedor proveedor = (Proveedor) vista.comboProveedor.getSelectedItem();
        helado.setId_proveedor(proveedor.getId());
        helado.setNombreProveedor(proveedor.getNombre());
        helado.setSabor(vista.panelHelado.comboSabor.getSelectedItem().toString());
        helado.setAzucar(vista.panelHelado.conAzucarRadioButton.isSelected());
        double litros = Double.parseDouble(vista.panelHelado.litrosHeladoTxt.getText());
        litros = Math.round(litros * 100.0) / 100.0; // redondea a 2 decimales
        helado.setLitros(litros);
        modelo.modificarHelado(helado);

        resetearProducto();
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Producto");
    }

    /***
     * Guarda las modificaciones en un proveedor
     */
    private void guardarProveedor() {
        if (!validarCamposProveedor()) {
            return;
        }
        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        proveedor.setNombre(vista.txtNombreProveedor.getText());
        proveedor.setPersonaContacto(vista.txtContactoProveedor.getText());
        proveedor.setEmail(vista.txtEmailProveedor.getText());
        proveedor.setTelefono(vista.txtTelefonoProveedor.getText());
        proveedor.setDireccion(vista.txtDireccionProveedor.getText());
        modelo.modificarProveedor(proveedor);

        resetearProveedor();
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Proveedor");
    }

    /***
     * Guarda las modificaciones en una ventaProducto
     */
    private void guardarVenta() {
        if (!validarCamposVenta()) {
            return;
        }
        Venta venta = (Venta) vista.listVenta.getSelectedValue();
        Helado helado = (Helado) vista.comboHelado.getSelectedItem();
        int cantidad = Integer.parseInt(vista.txtCantidadVenta.getText());
        venta.setId_helado(helado.getId());
        venta.setNombreHelado(helado.getNombre());
        venta.setCantidad(cantidad);
        double total = cantidad * helado.getPrecio();
        total = Math.round(total * 100.0) / 100.0; // redondea a 2 decimales
        venta.setPrecioTotal(total);
        modelo.modificarVenta(venta);

        resetearVenta();
        Util.mensajeInfo("Cambios guardados con éxito", "Editar Venta");
    }
    // GUARDAR FIN -----------------------------------------------------------------------------------------


    // ELIMINAR -----------------------------------------------------------------------------------------

    /***
     * Elimina un helado
     */
    private void eliminarHelado() {
        int fila = vista.listHelado.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ningún producto seleccionado", "Selecciona un producto");
            return;
        }

        Helado helado = (Helado) vista.listHelado.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar \"" + helado.getNombre() + "\"?", "Eliminar producto");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.borrarHelado(helado);
            refrescarProducto();
            if (editando) {
                resetearProducto();
            }
            Util.mensajeInfo("Se ha eliminado \"" + helado.getNombre() + "\"", "Producto Eliminado");
        }
    }

    /***
     * Elimina un proveedor
     */
    private void eliminarProveedor() {
        int fila = vista.listProveedor.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ningún proveedor seleccionado", "Selecciona un proveedor");
            return;
        }

        Proveedor proveedor = (Proveedor) vista.listProveedor.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar \"" + proveedor.getNombre() + "\"?", "Eliminar proveedor");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.borrarProveedor(proveedor);
            refrescarProveedor();
            if (editando) {
                resetearProveedor();
            }
            Util.mensajeInfo("Se ha eliminado \"" + proveedor.getNombre() + "\"", "Proveedor Eliminado");
        }
    }

    /***
     * Elimina una venta
     */
    private void eliminarVenta() {
        int fila = vista.listVenta.getSelectedIndex();
        if (fila < 0) {
            Util.mensajeError("No hay ninguna venta seleccionada", "Selecciona una venta");
            return;
        }

        Venta venta = (Venta) vista.listVenta.getSelectedValue();
        int resp = Util.mensajeConfirmación("¿Desea eliminar venta nº \"" + venta.getId() + "\"?", "Eliminar venta");
        if (resp == JOptionPane.OK_OPTION) {
            modelo.borrarVenta(venta);
            refrescarVenta();
            if (editando) {
                resetearVenta();
            }
            Util.mensajeInfo("Se ha eliminado venta nº \"" + venta.getId() + "\"", "Venta Eliminada");
        }
    }
    // ELIMINAR FIN -----------------------------------------------------------------------------------------


    // BORRAR BBDD -----------------------------------------------------------------------------------------

    /***
     * Elimina la BBDD de Helados
     */
    private void borrarBBDDHelado() {
        if (modelo.getHelados().isEmpty()) {
            Util.mensajeInfo("No existen productos a borrar", "Borrar productos");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todos los productos?", "Borrar Productos");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.eliminarBBDDHelado();
            refrescarProducto();
        }
    }

    /***
     * Elimina la BBDD de proveedores
     */
    private void borrarBBDDProveedor() {
        if (modelo.getProveedores().isEmpty()) {
            Util.mensajeInfo("No existen proveedores a borrar", "Borrar Proveedores");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todos los proveedores?", "Borrar Proveedores");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.eliminarBBDDProveedor();
            refrescarProveedor();
        }
    }

    /***
     * Elimina la BBDD de Ventas
     */
    private void borrarBBDDVenta() {
        if (modelo.getVentas().isEmpty()) {
            Util.mensajeInfo("No existen ventas a borrar", "Borrar Ventas");
            return;
        }

        int resp1 = Util.mensajeConfirmación("¿Desea borrar todas las ventas?", "Borrar Ventas");
        if (resp1 == JOptionPane.OK_OPTION) {
            modelo.eliminarBBDDVenta();
            refrescarVenta();
        }
    }
    // BORRAR BBDD FIN -----------------------------------------------------------------------------------------

    // BUSCAR -----------------------------------------------------------------------------------------
    private void buscarHelado() {
        if (!filtroProducto) {
            vista.dlmProducto.clear();
            ArrayList<Helado> lista = modelo.getHelados(vista.txtBuscarHelado.getText());
            for (Helado helado : lista) {
                vista.dlmProducto.addElement(helado);
            }
            vista.btnBuscarHelado.setText("Todos");
            filtroProducto = true;
        } else {
            vista.dlmProducto.clear();
            ArrayList<Helado> lista = modelo.getHelados();
            for (Helado helado : lista) {
                vista.dlmProducto.addElement(helado);
            }
            vista.btnBuscarHelado.setText("Buscar");
            vista.txtBuscarHelado.setText(null);
            filtroProducto = false;
        }
    }


    // LISTENERS --------------------------------------------------------------------------------------------

    /***
     * Añadir los listener
     * @param listener
     */
    private void addActionListeners(ActionListener listener) {
        // Limpiar
        vista.btnLimpiarProducto.addActionListener(listener);
        vista.btnLimpiarProducto.setActionCommand("limpiarProducto");
        vista.btnLimpiarProveedor.addActionListener(listener);
        vista.btnLimpiarProveedor.setActionCommand("limpiarProveedor");
        vista.btnLimpiarVenta.addActionListener(listener);
        vista.btnLimpiarVenta.setActionCommand("limpiarVenta");

        // Nuevo
        vista.btnNuevoProducto.addActionListener(listener);
        vista.btnNuevoProducto.setActionCommand("anadirProducto");
        vista.btnNuevoProveedor.addActionListener(listener);
        vista.btnNuevoProveedor.setActionCommand("anadirProveedor");
        vista.btnNuevoVenta.addActionListener(listener);
        vista.btnNuevoVenta.setActionCommand("anadirVenta");

        // Editar
        vista.btnEditaProducto.addActionListener(listener);
        vista.btnEditaProducto.setActionCommand("editarProducto");
        vista.btnEditarProveedor.addActionListener(listener);
        vista.btnEditarProveedor.setActionCommand("editarProveedor");
        vista.btnEditarVenta.addActionListener(listener);
        vista.btnEditarVenta.setActionCommand("editarVenta");

        // Eliminar
        vista.btnEliminarProducto.addActionListener(listener);
        vista.btnEliminarProducto.setActionCommand("eliminarProducto");
        vista.btnEliminarProveedor.addActionListener(listener);
        vista.btnEliminarProveedor.setActionCommand("eliminarProveedor");
        vista.btnEliminarVenta.addActionListener(listener);
        vista.btnEliminarVenta.setActionCommand("eliminarVenta");

        // Borrar BBDD
        vista.btnBorrarBBDDProducto.addActionListener(listener);
        vista.btnBorrarBBDDProducto.setActionCommand("borrarProducto");
        vista.btnBorrarBBDDProveedor.addActionListener(listener);
        vista.btnBorrarBBDDProveedor.setActionCommand("borrarProveedor");
        vista.btnBorrarBBDDVenta.addActionListener(listener);
        vista.btnBorrarBBDDVenta.setActionCommand("borrarVenta");

        // Guardar
        vista.btnGuardarProducto.addActionListener(listener);
        vista.btnGuardarProducto.setActionCommand("guardarProducto");
        vista.btnGuardarProveedor.addActionListener(listener);
        vista.btnGuardarProveedor.setActionCommand("guardarProveedor");
        vista.btnGuardarVenta.addActionListener(listener);
        vista.btnGuardarVenta.setActionCommand("guardarVenta");

        // Cancelar
        vista.btnCancelarProducto.addActionListener(listener);
        vista.btnCancelarProducto.setActionCommand("cancelarProducto");
        vista.btnCancelarProveedor.addActionListener(listener);
        vista.btnCancelarProveedor.setActionCommand("cancelarProveedor");
        vista.btnCancelarVenta.addActionListener(listener);
        vista.btnCancelarVenta.setActionCommand("cancelarVenta");

        // Mostrar
        vista.btnMostrarHeladoPorveedor.addActionListener(listener);
        vista.btnMostrarHeladoPorveedor.setActionCommand("mostrarHeladosProveedor");

        // Buscar
        vista.btnBuscarHelado.addActionListener(listener);
        vista.btnBuscarHelado.setActionCommand("buscarHelado");

        vista.itemSalir.addActionListener(listener);
        vista.itemDesconectar.addActionListener(listener);
    }
    // LISTENERS FIN --------------------------------------------------------------------------------------------


    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommmand = e.getActionCommand();
        CardLayout cl = (CardLayout) (vista.panelCard.getLayout());

        switch (actionCommmand) {
            case "Desconectar":
                if (conectado) {
                    modelo.desconectar();
                    vista.itemDesconectar.setText("Conectar");
                    vista.bloquearVistaExceptoMenu();
                    conectado = false;
                } else {
                    modelo.conectar();
                    vista.itemDesconectar.setText("Desconectar");
                    vista.desbloquearVista();
                    conectado = true;
                }
                break;
            case "Salir":
                System.exit(0);
                break;

            // PANEL CARD
            case "Helado":
                cl.show(vista.panelCard, "Helado");
                break;

            // LIMPIAR
            case "limpiarProducto":
                limpiarCamposProducto();
                limpiarCamposHelado();
                break;
            case "limpiarProveedor":
                limpiarCamposProveedor();
                break;
            case "limpiarVenta":
                limpiarCamposVenta();
                break;

            // NUEVO
            case "anadirProducto":
                nuevoProducto();
                break;
            case "anadirProveedor":
                nuevoProveedor();
                break;
            case "anadirVenta":
                nuevoVenta();
                break;

            // EDITAR
            case "editarProducto":
                editarProducto();
                break;
            case "editarProveedor":
                editarProveedor();
                break;
            case "editarVenta":
                editarVenta();
                break;

            // ELIMINAR
            case "eliminarProducto":
                eliminarHelado();
                break;
            case "eliminarProveedor":
                eliminarProveedor();
                break;
            case "eliminarVenta":
                eliminarVenta();
                break;

            // BORRAR BBDD
            case "borrarProducto":
                borrarBBDDHelado();
                break;
            case "borrarProveedor":
                borrarBBDDProveedor();
                break;
            case "borrarVenta":
                borrarBBDDVenta();
                break;

            // GUARDAR
            case "guardarProducto":
                guardarProducto();
                break;
            case "guardarProveedor":
                guardarProveedor();
                break;
            case "guardarVenta":
                guardarVenta();
                break;

            // CANCELAR
            case "cancelarProducto":
                resetearProducto();
                break;
            case "cancelarProveedor":
                resetearProveedor();
                break;
            case "cancelarVenta":
                resetearVenta();
                break;

            // MOSTRAR
            case "mostrarHeladosProveedor":
                listarHeladosProveedor();
                break;

            // BUSCAR
            case "buscarHelado":
                buscarHelado();
                break;

            case "refrescarVenta":
                resetearVenta();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}

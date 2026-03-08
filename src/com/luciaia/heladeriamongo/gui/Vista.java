package com.luciaia.heladeriamongo.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.luciaia.heladeriamongo.base.enums.SaborHelado;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {
    private final static String TITULO_FRAME = "Heladería MVC";
    JTabbedPane tabbedPane1;
    JPanel panel1;
    // productos
    JPanel JPanelProducto;
    JButton btnLimpiarProducto;
    JButton btnCancelarProducto;
    JButton btnGuardarProducto;
    JButton btnNuevoProducto;
    JButton btnEditaProducto;
    JButton btnEliminarProducto;
    JButton btnBorrarBBDDProducto;
    JPanel panelCard;
    PanelHelado panelHelado;
    JTextField txtNombreProducto;
    JTextField txtPrecioProducto;
    DatePicker dateCaducidad;
    DatePicker dateApertura;
    JComboBox comboProveedor;
    // Proveedores
    JPanel JPanelProveedor;
    JTextField txtNombreProveedor;
    JTextField txtContactoProveedor;
    JTextField txtEmailProveedor;
    JTextField txtTelefonoProveedor;
    JTextField txtDireccionProveedor;
    JButton btnLimpiarProveedor;
    JButton btnNuevoProveedor;
    JButton btnEditarProveedor;
    JButton btnEliminarProveedor;
    JButton btnBorrarBBDDProveedor;
    JButton btnCancelarProveedor;
    JButton btnGuardarProveedor;
    // Ventas
    JPanel JPanelVenta;
    JComboBox comboHelado;
    JTextField txtCantidadVenta;
    JButton btnNuevoVenta;
    JButton btnEditarVenta;
    JButton btnEliminarVenta;
    JButton btnBorrarBBDDVenta;
    JButton btnLimpiarVenta;
    JButton btnGuardarVenta;
    JButton btnCancelarVenta;
    // tablas
    JList listHelado;
    JList listProveedor;
    JList listVenta;
    JList lisHeladoProveedor;
    DefaultListModel dlmProducto;
    DefaultListModel dlmProveedor;
    DefaultListModel dlmVenta;
    DefaultListModel dlmHeladoProveedor;
    //menubar
    JMenuItem itemDesconectar;
    JMenuItem itemSalir;
    // Buscar
    JTextField txtBuscarHelado;
    JButton btnBuscarHelado;

    JButton btnMostrarHeladoPorveedor;


    public Vista() {
        super(TITULO_FRAME);
        initFrame();
    }

    public void initFrame() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Image icono = Toolkit.getDefaultToolkit().getImage(
                getClass().getClassLoader().getResource("Icono.png")
        );

        setIconImage(icono);
        //radioHelado.setSelected(true);
        crearPanelCard();
        botonesVisibles();

        CardLayout cl = (CardLayout) (panelCard.getLayout());
        cl.show(panelCard, "Helado");

        //llamo menu
        setMenu();
        //cargo enumerados
        setEnumComboBox();
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setListModels();
    }

    private void setMenu() {
        JMenuBar mbBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        itemDesconectar = new JMenuItem("Desconectar");
        itemDesconectar.setActionCommand("Desconectar");
        itemSalir = new JMenuItem("Salir");
        itemSalir.setActionCommand("Salir");

        mbBar.setBackground(new Color(255, 203, 133));
        itemDesconectar.setBackground(new Color(255, 203, 133));
        itemSalir.setBackground(new Color(255, 203, 133));

        menu.add(itemDesconectar);
        menu.add(itemSalir);
        mbBar.add(menu);
        mbBar.add(Box.createHorizontalGlue());
        this.setJMenuBar(mbBar);
    }

    private void setEnumComboBox() {
        for (SaborHelado constant : SaborHelado.values()) {
            panelHelado.comboSabor.addItem(constant.getValor());
        }
        panelHelado.comboSabor.setSelectedIndex(-1);
    }

    private void setListModels() {
        dlmProducto = new DefaultListModel();
        listHelado.setModel(dlmProducto);
        dlmProveedor = new DefaultListModel();
        listProveedor.setModel(dlmProveedor);
        dlmVenta = new DefaultListModel();
        listVenta.setModel(dlmVenta);
        dlmHeladoProveedor = new DefaultListModel();
        lisHeladoProveedor.setModel(dlmHeladoProveedor);
    }

    private void botonesVisibles() {
        btnCancelarProducto.setVisible(false);
        btnGuardarProducto.setVisible(false);
        btnCancelarProveedor.setVisible(false);
        btnGuardarProveedor.setVisible(false);
        btnCancelarVenta.setVisible(false);
        btnGuardarVenta.setVisible(false);
    }

    public void crearPanelCard() {
        panelHelado = new PanelHelado();
        panelHelado.conAzucarRadioButton.setSelected(true);
        panelCard.add(panelHelado.panel1, "Helado");
    }

    public void bloquearVista() {
        setEnabledRecursivo(panel1, false);
    }

    public void desbloquearVista() {
        setEnabledRecursivo(panel1, true);
    }

    private void setEnabledRecursivo(Container container, boolean enabled) {
        for (Component c : container.getComponents()) {
            c.setEnabled(enabled);
            if (c instanceof Container) {
                setEnabledRecursivo((Container) c, enabled);
            }
        }
    }

    public void bloquearVistaExceptoMenu() {
        bloquearVista();
        itemDesconectar.setEnabled(true);
        itemSalir.setEnabled(true);
    }
}

package com.luciaia.heladeriamongo.gui;

import com.luciaia.heladeriamongo.base.Helado;
import com.luciaia.heladeriamongo.base.Proveedor;
import com.luciaia.heladeriamongo.base.Venta;
import com.luciaia.heladeriamongo.util.Util;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class Modelo {
    private MongoClient cliente;
    private MongoCollection<Document> helados;
    private MongoCollection<Document> proveedores;
    private MongoCollection<Document> ventas;

    /**
     * Metodo conectar que conecta con la base de datos
     */
    public void conectar() {
        cliente = new MongoClient();
        String DATABASE = "Heladeria";
        MongoDatabase db = cliente.getDatabase(DATABASE);

        String COLECCION_HELADOS = "Helado";
        helados = db.getCollection(COLECCION_HELADOS);
        String COLECCION_PROVEEDORES = "Proveedor";
        proveedores = db.getCollection(COLECCION_PROVEEDORES);
        String COLECCION_VENTAS = "Venta";
        ventas = db.getCollection(COLECCION_VENTAS);
    }

    /**
     * Metodo que desconecta de la base de datos
     */
    public void desconectar() {
        cliente.close();
        cliente = null;
    }

    /**
     * Metodo que guarda en la base de datos cualquier objeto
     * @param obj Objeto de la clase Helado, Proveedor o Venta
     */
    public void guardarObjeto(Object obj) {
        if (obj instanceof Helado) {
            helados.insertOne(objectToDocument(obj));
        } else if (obj instanceof Proveedor) {
            proveedores.insertOne(objectToDocument(obj));
        } else if (obj instanceof Venta) {
            ventas.insertOne(objectToDocument(obj));
        }
    }

    /**
     * Metodo que devulve un arraylist con todos los helados
     * @return Devuelve un arraylist con todos los helados
     */
    public ArrayList<Helado> getHelados() {
        ArrayList<Helado> lista = new ArrayList<>();

        for (Document document : helados.find()) {
            lista.add(documentToHelado(document));
        }
        return lista;
    }

    /**
     * Metodo que devulve un arraylist con todos los proveedores
     * @return Devuelve un arraylist con todos los proveedores
     */
    public ArrayList<Proveedor> getProveedores() {
        ArrayList<Proveedor> lista = new ArrayList<>();

        for (Document document : proveedores.find()) {
            lista.add(documentToProveedor(document));
        }
        return lista;
    }

    /**
     * Metodo que devulve un arraylist con todos las ventas
     * @return Devuelve un arraylist con todos las ventas
     */
    public ArrayList<Venta> getVentas() {
        ArrayList<Venta> lista = new ArrayList<>();

        for (Document document : ventas.find()) {
            lista.add(documentToVenta(document));
        }
        return lista;
    }

    /**
     * Metodo que devuelve el MongoClient
     * @return MongoClient
     */
    public MongoClient getCliente() {
        return cliente;
    }

    /**
     * Metodo que borrar el helado
     * @param helado recambio a borrar
     */
    public void borrarHelado(Helado helado) {
        helados.deleteOne(objectToDocument(helado));
    }

    /**
     * Metodo que borrar Helados
     */
    public void eliminarBBDDHelado() {
        helados.drop();
    }

    /**
     * Metodo que borrar el proveedor
     * @param proveedor recambio a borrar
     */
    public void borrarProveedor(Proveedor proveedor) {
        proveedores.deleteOne(objectToDocument(proveedor));
    }

    /**
     * Metodo que borrar Proveedores
     */
    public void eliminarBBDDProveedor() {
        proveedores.drop();
    }

    /**
     * Metodo que borrar el venta
     * @param venta recambio a borrar
     */
    public void borrarVenta(Venta venta) {
        ventas.deleteOne(objectToDocument(venta));
    }

    /**
     * Metodo que borrar Proveedores
     */
    public void eliminarBBDDVenta() {
        ventas.drop();
    }

    /**
     * Metodo que modifica un helado
     * @param helado recambio a modificar
     */
    public void modificarHelado(Helado helado) {
        helados.replaceOne(new Document("_id",helado.getId()), objectToDocument(helado));
    }

    /**
     * Metodo que modifica un proveedor
     * @param proveedor recambio a modificar
     */
    public void modificarProveedor(Proveedor proveedor) {
        proveedores.replaceOne(new Document("_id",proveedor.getId()), objectToDocument(proveedor));
    }

    /**
     * Metodo que modifica un venta
     * @param venta recambio a modificar
     */
    public void modificarVenta(Venta venta) {
        ventas.replaceOne(new Document("_id",venta.getId()), objectToDocument(venta));
    }

    /**
     * Metodo que pasa un documento a un objeto de la clase Helado
     * @param dc Documento que transformar
     * @return Objeto de la clase Helado
     */
    public Helado documentToHelado(Document dc) {
        Helado helado = new Helado();

        helado.setId(dc.getObjectId("_id"));
        helado.setNombre(dc.getString("nombre"));
        helado.setPrecio(dc.getDouble("precio"));
        helado.setFechaApertura(Util.parsearFecha(dc.getString("fecha_apertura")));
        helado.setFechaCaducidad(Util.parsearFecha(dc.getString("fecha_caducidad")));
        helado.setSabor(dc.getString("sabor"));
        helado.setAzucar(dc.getBoolean("azucar"));
        helado.setLitros(dc.getDouble("litros"));
        helado.setId_proveedor(dc.getObjectId("id_proveedor"));
        helado.setNombreProveedor(dc.getString("nombre_proveedor"));
        return helado;
    }

    /**
     * Metodo que pasa un documento a un objeto de la clase Proveedor
     * @param dc Documento que transformar
     * @return Objeto de la clase Proveedor
     */
    public Proveedor documentToProveedor(Document dc) {
        Proveedor proveedor = new Proveedor();

        proveedor.setId(dc.getObjectId("_id"));
        proveedor.setNombre(dc.getString("nombre"));
        proveedor.setPersonaContacto(dc.getString("persona_contacto"));
        proveedor.setEmail(dc.getString("email"));
        proveedor.setTelefono(dc.getString("telefono"));
        proveedor.setDireccion(dc.getString("direccion"));
        return proveedor;
    }

    /**
     * Metodo que pasa un documento a un objeto de la clase Venta
     * @param dc Documento que transformar
     * @return Objeto de la clase Venta
     */
    public Venta documentToVenta(Document dc) {
        Venta venta = new Venta();

        venta.setId(dc.getObjectId("_id"));
        venta.setCantidad(dc.getInteger("cantidad"));
        venta.setPrecioTotal(dc.getDouble("precio_total"));
        venta.setId_helado(dc.getObjectId("id_helado"));
        venta.setNombreHelado(dc.getString("nombre_helado"));
        return venta;
    }

    /**
     * Metodo que pasa un objeto de las clase Helado, Proveedor o Venta a un Document
     * @param obj Objeto de la clase Helado, Proveedor o Venta
     * @return Devuelve un documento
     */
    public Document objectToDocument(Object obj) {
        Document dc = new Document();

        if (obj instanceof Helado) {
            Helado helado = (Helado) obj;

            dc.append("nombre", helado.getNombre());
            dc.append("precio", helado.getPrecio());
            dc.append("fecha_apertura", Util.formatearFecha(helado.getFechaApertura()));
            dc.append("fecha_caducidad", Util.formatearFecha(helado.getFechaCaducidad()));
            dc.append("sabor", helado.getSabor());
            dc.append("azucar", helado.isAzucar());
            dc.append("litros", helado.getLitros());
            dc.append("id_proveedor", helado.getId_proveedor());
            dc.append("nombre_proveedor", helado.getNombreProveedor());
        } else if (obj instanceof Proveedor) {
            Proveedor proveedor = (Proveedor) obj;

            dc.append("nombre", proveedor.getNombre());
            dc.append("persona_contacto", proveedor.getPersonaContacto());
            dc.append("email", proveedor.getEmail());
            dc.append("direccion", proveedor.getDireccion());
            dc.append("telefono", proveedor.getTelefono());
        } else if (obj instanceof Venta) {
            Venta venta = (Venta) obj;

            dc.append("cantidad", venta.getCantidad());
            dc.append("precio_total", venta.getPrecioTotal());
            dc.append("id_helado", venta.getId_helado());
            dc.append("nombre_helado", venta.getNombreHelado());
        } else {
            return null;
        }
        return dc;
    }

    /**
     * Metodo que rellena el ComboBox de helado
     * @param lista lista de helados
     * @param combo ComboBox al que las queremos añadir
     */
    public void setComboBoxHelado(ArrayList<Helado> lista, JComboBox combo){
        combo.addItem("");
        for (Helado helado: lista) {
            combo.addItem(helado);
        }
        combo.setSelectedIndex(-1);
    }

    /**
     * Metodo que rellena el ComboBox de proveedor
     * @param lista lista de proveedores
     * @param combo ComboBox al que las queremos añadir
     */
    public void setComboBoxProveedor(ArrayList<Proveedor> lista, JComboBox combo){
        combo.addItem("");
        for (Proveedor proveedor: lista) {
            combo.addItem(proveedor);
        }
        combo.setSelectedIndex(-1);
    }

    /**
     * Metodo que devuelve todos los helados de un proveedor
     * @param id_proveedor ObjectId de la proveedor
     * @return ArrayList de helados
     */
    public ArrayList<Helado> getHeladosProveedor(ObjectId id_proveedor) {
        ArrayList<Helado> lista = new ArrayList<>();
        Document query = new Document();

        query.append("id_proveedor",id_proveedor);

        for (Document document : helados.find(query)) {
            lista.add(documentToHelado(document));
        }
        return lista;
    }

    /**
     * Metodo que lista los helados por nombre
     * @param comparador cadena que queremos buscar
     * @return ArrayList de helados
     */
    public ArrayList<Helado> getHelados(String comparador) {
        ArrayList<Helado> lista = new ArrayList<>();
        Document query = new Document();
        List<Document> listaCriterios = new ArrayList<>();

        listaCriterios.add(new Document("nombre", new Document("$regex", "/*" + comparador + "/*")));
        listaCriterios.add(new Document("sabor", new Document("$regex", "/*" + comparador + "/*")));
        query.append("$or", listaCriterios);

        for (Document document : helados.find(query)) {
            lista.add(documentToHelado(document));
        }
        return lista;
    }

    /**
     * Metodo que busca un proveedor por su ID
     * @param id ObjectId del proveedor
     * @return Proveedor encontrado o null si no existe
     */
    public Proveedor getProveedorPorId(ObjectId id) {
        Document query = new Document("_id", id);
        Document doc = proveedores.find(query).first();

        if (doc != null) {
            return documentToProveedor(doc);
        } else {
            return null;
        }
    }

    /**
     * Método que busca un helado por su ID
     * @param id ObjectId del helado
     * @return Helado encontrado o null si no existe
     */
    public Helado getHeladoPorId(ObjectId id) {
        Document query = new Document("_id", id);
        Document doc = helados.find(query).first();

        if (doc != null) {
            return documentToHelado(doc);
        } else {
            return null;
        }
    }
}

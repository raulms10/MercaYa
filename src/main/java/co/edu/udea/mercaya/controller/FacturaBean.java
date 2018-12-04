/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.controller;

import co.edu.udea.mercaya.logica.DetalleFacadeLocal;
import co.edu.udea.mercaya.logica.ProductoFacadeLocal;
import co.edu.udea.mercaya.logica.VentaFacadeLocal;
import co.edu.udea.mercaya.model.Detalle;
import co.edu.udea.mercaya.model.DetallePK;
import co.edu.udea.mercaya.logica.VentaFacade;
import co.edu.udea.mercaya.model.Producto;
import co.edu.udea.mercaya.model.Venta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author raulio
 */
public class FacturaBean implements Serializable{

    @EJB
    private ProductoFacadeLocal productoFacade;

    @EJB
    private VentaFacadeLocal ventaFacade;

    @EJB
    private DetalleFacadeLocal detalleFacade;
    
    private Producto selectedProduct = new Producto();
    private List<Producto> listProduct = new ArrayList<>();
    private String cantidad;
    private Integer cantidadInt;
    private String descuento;
    private BigDecimal descuentoInt;
    private List<Producto> listProductToShop = new ArrayList<>();
    private List<Producto> listSelectedProducts = new ArrayList<>();
    private String tipoPago;
    
    private String factura;
    private String producto;
    private Integer idFactura = 0;
    
    private List<Venta> listVentas;
    private Venta ventaSelected;
    
    private List<Detalle> listDetalles = new ArrayList<Detalle>();;
    
    private List<Producto> listProductos = new ArrayList<Producto>();
    
    
    /**
     * Creates a new instance of FacturaBean
     */
    public FacturaBean() {
    
    }
    
    public String guardarVenta(){
        Venta v = new Venta(90);
        v.setFecha(new Date());
        v.setSubtotal(2000);
        v.setTotal(4000);
        v.setCajero("aaa");
        ventaFacade.create(v);
        return "goList";
    }
    
    public void buscarProductos(){
        listProductToShop = new ArrayList<>();
        cantidad = "";
        descuento = "";
        listProduct = productoFacade.findAll();
    }
    
    private boolean existeProducto(Integer codigo){
        for (Producto prod : listProductToShop) {
            if (Objects.equals(prod.getCodigo(), codigo)){
                prod.setCantidad(cantidadInt);
                prod.setDescuento(descuentoInt);
                return true;
            } 
        }
        return false;
    }
    
    public String agregarProducto(){
        FacesContext context = FacesContext.getCurrentInstance(); //Cargamos el contexto para mostrar los mensajes
        try {
            cantidadInt = Integer.valueOf(cantidad);
            descuentoInt = BigDecimal.valueOf(Double.valueOf(descuento));
        } catch (NumberFormatException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La cantidad y el descuento deben ser un entero y un decimal respectivamente"));
            return "";
        }
        
        if (cantidadInt <= 0 || Double.valueOf(descuento) < 0){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La cantidad y el descuento deben ser valores posivitos"));
            return "";
        } 
        
        System.out.println("Adding");
        Producto prod;
        if (selectedProduct != null && selectedProduct.getCodigo() != null){
            for (Producto p: listProduct){
                if (Objects.equals(p.getCodigo(), selectedProduct.getCodigo()) && !existeProducto(p.getCodigo())){
                    prod = p.newObject();
                    prod.setCantidad(cantidadInt);
                    prod.setDescuento(descuentoInt);
                    listProductToShop.add(prod);
                    System.out.println("Prod: " + prod.getNombre());
                }
            }
            System.out.println("Size: " + listProductToShop.size());
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Debe seleccionar el producto que desea añadir al carrito"));
        }
        return "";
    }
    
    public String comprarProductos(){
        FacesContext context = FacesContext.getCurrentInstance(); //Cargamos el contexto para mostrar los mensajes
        
        System.out.println("Shopping...");
        if (listProductToShop != null && !listProductToShop.isEmpty()){
            if (ventaFacade == null || detalleFacade == null){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No es posible guardar la información"));
            }
            Venta venta = new Venta();
            venta.setCajero("Almacenes Merca Ya");
            //Calcular iva
            idFactura = ventaFacade.lastIdInsert();
            if (idFactura==null){
                idFactura=0;
            }
            factura = idFactura.toString();
            venta.setFactura(idFactura+1);
            venta.setFecha(new Date());
            venta.setTipopago(tipoPago);
            venta.setSubtotal(0);
            venta.setTotal(0);
            venta.setTotalDesc(0);
            System.out.println("Iva.... Id: " + idFactura);
            ventaFacade.create(venta);
            Detalle detalleVenta;
            DetallePK detelleVentaPK;
            for (Producto p: listProductToShop){
                detelleVentaPK = new DetallePK(p.getCodigo(), venta.getFactura());
                detalleVenta = new Detalle(detelleVentaPK);
                detalleVenta.setDescuento(p.getDescuento());
                detalleVenta.setProducto1(p);
                venta.setSubtotal(venta.getSubtotal() + p.getCantidad()*p.getValorunitario());
                venta.setTotal(venta.getTotal() + p.getCantidad()*p.getValorunitario()*p.getIva().doubleValue()/100);
                venta.setTotalDesc(venta.getTotalDesc() + p.getCantidad()*p.getValorunitario()*(p.getIva().doubleValue()+100)*p.getDescuento().doubleValue()/10000);
                System.out.println("Prod: " + p.getNombre() + " Sub:" + venta.getSubtotal() + " Total: " + venta.getTotal() + " Desc: " + venta.getTotalDesc());
                detalleVenta.setVenta1(venta);
                detalleFacade.create(detalleVenta);
            }
            venta.setTotal(venta.getTotal() + venta.getSubtotal());
            venta.setTotalDesc(venta.getTotal() - venta.getTotalDesc());
            ventaFacade.edit(venta);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bien hecho!", "Compra realizada con éxito"));
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nada para comprar", "Debe añadir Productos al carrito"));
        }
        return "";
    }
    
    public String borrarProducto(){
        System.out.println("Borrando...");
        if (listSelectedProducts != null || !listSelectedProducts.isEmpty()) {
            for (Producto p : listSelectedProducts) {
                listProductToShop.remove(p);
                System.out.println("Borrado");
            }
        }
        return "";
    }
        /**
     * Busca la venta existente en la base de datos. Esta lista queda almacenada en la variabla 'venta'
     * @return Cadena que indica el estado de la consulta
     */
    public String buscarVenta() {
        FacesContext context = FacesContext.getCurrentInstance(); //Cargamos el contexto para mostrar los mensajes
        if (this.ventaFacade == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha podido buscar a las Personas"));
            return "Error en la búsqueda";
        }
        if (factura == null || factura == ""){
            return "";
        }
        try{
            idFactura = Integer.valueOf(factura);
        }catch(NumberFormatException e){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe digitar sólo valores numéricos"));
            return "";
        }
        //Hacemos la consulta a la base de datos
        System.out.println("HGHGAGdfghjkH "+factura);
        listVentas = this.ventaFacade.findAll(idFactura);
        ventaSelected = listVentas.get(0);
        listDetalles = detalleFacade.findAll(idFactura);
        for (Detalle listDetalle : listDetalles) {
            listProductos.add(listDetalle.getProducto1());;
        }
        System.out.println("HGHGAGH"+ventaSelected.getCajero());
        return "Exito al buscar";
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Producto getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Producto selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<Producto> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Producto> listProduct) {
        this.listProduct = listProduct;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public List<Producto> getListProductToShop() {
        return listProductToShop;
    }

    public void setListProductToShop(List<Producto> listProductToShop) {
        this.listProductToShop = listProductToShop;
    }

    public List<Producto> getListSelectedProducts() {
        return listSelectedProducts;
    }

    public void setListSelectedProducts(List<Producto> listSelectedProducts) {
        this.listSelectedProducts = listSelectedProducts;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }
    
    public ProductoFacadeLocal getProductoFacade() {
        return productoFacade;
    }

    public void setProductoFacade(ProductoFacadeLocal productoFacade) {
        this.productoFacade = productoFacade;
    }

    public VentaFacadeLocal getVentaFacade() {
        return ventaFacade;
    }

    public void setVentaFacade(VentaFacadeLocal ventaFacade) {
        this.ventaFacade = ventaFacade;
    }

    public DetalleFacadeLocal getDetalleFacade() {
        return detalleFacade;
    }

    public void setDetalleFacade(DetalleFacadeLocal detalleFacade) {
        this.detalleFacade = detalleFacade;
    }

    public Venta getVentaSelected() {
        return ventaSelected;
    }

    public void setVentaSelected(Venta ventaSelected) {
        this.ventaSelected = ventaSelected;
    }

    public List<Producto> getListProductos() {
        return listProductos;
    }

    public void setListProductos(List<Producto> listProductos) {
        this.listProductos = listProductos;
    }
    
    
}

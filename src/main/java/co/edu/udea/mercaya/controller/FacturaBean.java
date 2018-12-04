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
    
    
    /**
     * Creates a new instance of FacturaBean
     */
    public FacturaBean() {
    
    }
    
    public String guardarProducto(){
        Producto p = new Producto(11);
        p.setIva(BigDecimal.valueOf(0));
        p.setLetra("A");
        p.setNombre("Papaya");
        p.setValorunitario(800);
        productoFacade.create(p);
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
            Integer id = ventaFacade.lastIdInsert();
            venta.setFactura(id+1);
            venta.setFecha(new Date());
            venta.setTipopago(tipoPago);
            venta.setSubtotal(0);
            venta.setTotal(0);
            venta.setTotalDesc(0);
            System.out.println("Iva.... Id: " + id);
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
    
}

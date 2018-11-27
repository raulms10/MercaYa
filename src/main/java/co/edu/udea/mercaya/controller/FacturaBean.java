/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.controller;

import co.edu.udea.mercaya.logica.DetalleFacadeLocal;
import co.edu.udea.mercaya.logica.ProductoFacadeLocal;
import co.edu.udea.mercaya.logica.VentaFacadeLocal;
import co.edu.udea.mercaya.model.Producto;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.ejb.EJB;

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.controller;

import co.edu.udea.mercaya.logica.DetalleFacadeLocal;
import co.edu.udea.mercaya.logica.ProductoFacadeLocal;
import co.edu.udea.mercaya.logica.VentaFacadeLocal;
import co.edu.udea.mercaya.logica.VentaFacade;
import co.edu.udea.mercaya.model.Detalle;
import co.edu.udea.mercaya.model.Producto;
import co.edu.udea.mercaya.model.Venta;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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
    
    private String factura;
    private String producto;
    
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
        v.setFecha(Date.valueOf("2015-02-04"));
        v.setSubtotal(2000);
        v.setTotal(4000);
        v.setCajero("aaa");
        ventaFacade.create(v);
        return "goList";
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

        Integer id = Integer.valueOf(factura);
        //Hacemos la consulta a la base de datos
        System.out.println("HGHGAGdfghjkH"+factura);
        listVentas = this.ventaFacade.findAll(id);
        ventaSelected = listVentas.get(0);
        
        listDetalles = detalleFacade.findAll(id);

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

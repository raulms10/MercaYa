/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author raulio
 */
@Entity
//@Table(name = "DETALLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detalle.findAll", query = "SELECT d FROM Detalle d")
    , @NamedQuery(name = "Detalle.findByProducto", query = "SELECT d FROM Detalle d WHERE d.detallePK.producto = :producto")
    , @NamedQuery(name = "Detalle.findByVenta", query = "SELECT d FROM Detalle d WHERE d.detallePK.venta = :venta")
    , @NamedQuery(name = "Detalle.findByDescuento", query = "SELECT d FROM Detalle d WHERE d.descuento = :descuento")})
public class Detalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetallePK detallePK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DESCUENTO")
    private BigDecimal descuento;
    @JoinColumn(name = "PRODUCTO", referencedColumnName = "CODIGO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Producto producto1;
    @JoinColumn(name = "VENTA", referencedColumnName = "FACTURA", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Venta venta1;

    public Detalle() {
    }

    public Detalle(DetallePK detallePK) {
        this.detallePK = detallePK;
    }

    public Detalle(int producto, int venta) {
        this.detallePK = new DetallePK(producto, venta);
    }

    public DetallePK getDetallePK() {
        return detallePK;
    }

    public void setDetallePK(DetallePK detallePK) {
        this.detallePK = detallePK;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Producto getProducto1() {
        return producto1;
    }

    public void setProducto1(Producto producto1) {
        this.producto1 = producto1;
    }

    public Venta getVenta1() {
        return venta1;
    }

    public void setVenta1(Venta venta1) {
        this.venta1 = venta1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallePK != null ? detallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detalle)) {
            return false;
        }
        Detalle other = (Detalle) object;
        if ((this.detallePK == null && other.detallePK != null) || (this.detallePK != null && !this.detallePK.equals(other.detallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.udea.mercaya.model.Detalle[ detallePK=" + detallePK + " ]";
    }
    
}

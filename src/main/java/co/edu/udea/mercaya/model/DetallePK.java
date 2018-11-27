/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author raulio
 */
@Embeddable
public class DetallePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTO")
    private int producto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VENTA")
    private int venta;

    public DetallePK() {
    }

    public DetallePK(int producto, int venta) {
        this.producto = producto;
        this.venta = venta;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    public int getVenta() {
        return venta;
    }

    public void setVenta(int venta) {
        this.venta = venta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) producto;
        hash += (int) venta;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetallePK)) {
            return false;
        }
        DetallePK other = (DetallePK) object;
        if (this.producto != other.producto) {
            return false;
        }
        if (this.venta != other.venta) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.udea.mercaya.model.DetallePK[ producto=" + producto + ", venta=" + venta + " ]";
    }
    
}

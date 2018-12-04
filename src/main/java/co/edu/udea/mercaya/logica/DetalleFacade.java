/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.logica;

import co.edu.udea.mercaya.model.Detalle;
import co.edu.udea.mercaya.model.DetallePK;
import co.edu.udea.mercaya.model.Detalle_;
import co.edu.udea.mercaya.model.Producto;
import co.edu.udea.mercaya.model.Venta;
import co.edu.udea.mercaya.model.Venta_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 *
 * @author raulio
 */
@Stateless
public class DetalleFacade extends AbstractFacade<Detalle> implements DetalleFacadeLocal {

    @PersistenceContext(unitName = "MercaYaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleFacade() {
        super(Detalle.class);
    }
    
    public List<Detalle> findAll(Integer factura) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder(); 
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        //Definimos de donde vamos a sacar los datos (FROM ontactos)
        Root<Detalle> detalleTable = cq.from(Detalle.class);
        Join<Detalle, Producto> productoTable = detalleTable.join(Detalle_.producto1);
        Join<Detalle, Venta> ventaTable = detalleTable.join(Detalle_.venta1);
        System.out.println("gaghashgsahsahgashagshagha");
        cq.where(cb.equal(ventaTable.get(Venta_.factura), factura));
        cq.select(detalleTable).distinct(true);
                
        List<Detalle> listaV = getEntityManager().createQuery(cq).getResultList();

        return listaV;
    }
}

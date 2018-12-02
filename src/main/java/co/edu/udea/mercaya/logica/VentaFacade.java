/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.logica;

import co.edu.udea.mercaya.model.Venta;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

/**
 *
 * @author raulio
 */
@Stateless
public class VentaFacade extends AbstractFacade<Venta> implements VentaFacadeLocal {

    @PersistenceContext(unitName = "MercaYaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VentaFacade() {
        super(Venta.class);
    }
    
    
    public List<Venta> findAll(Integer factura) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder(); 
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        //Definimos de donde vamos a sacar los datos (FROM ontactos)
        Root rootTable = cq.from(Venta.class);
        System.out.println("gaghashgsahsahgashagshagha");
        cq.select(rootTable)
                //Aplicamos el filtro para sólo traer los activos (WHERE estado = '1')
                .where(cb.equal(rootTable.get("factura"), factura));
        List<Venta> listaV = getEntityManager().createQuery(cq).getResultList();

        return listaV;
    }
    
}

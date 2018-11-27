/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udea.mercaya.logica;

import co.edu.udea.mercaya.model.Detalle;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
}

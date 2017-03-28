package edu.eci.pdsw.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.pdsw.sampleprj.dao.ClienteDAO;
import edu.eci.pdsw.sampleprj.dao.ItemDAO;
import edu.eci.pdsw.sampleprj.dao.ItemRentadoDAO;
import edu.eci.pdsw.sampleprj.dao.PersistenceException;
import edu.eci.pdsw.sampleprj.dao.TipoItemDAO;

import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.ItemRentado;
import edu.eci.pdsw.samples.entities.TipoItem;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Oscar Alba
 * @author Laura Ramos
 */

@Singleton
public class ServiciosAlquilerItemsImpl implements ServiciosAlquiler {
    
    private static final int MULTA_DIARIA=5000;
    private final static long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000;
    
    public ServiciosAlquilerItemsImpl() {
        
    }
    @Inject
    private ItemDAO daoItem;
    @Inject
    private ClienteDAO daoCliente;
    @Inject
    private ItemRentadoDAO daoItemRentado;
    @Inject
    private TipoItemDAO daoTipoItem;
    
  
    
    @Override
    public int valorMultaRetrasoxDia() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cliente consultarCliente(int docu) throws ExcepcionServiciosAlquiler {
        try {
            return daoCliente.load(docu);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item "+docu,ex);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Item> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
        try {
            return daoCliente.loadRentados(idcliente);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item "+idcliente,ex);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
        try {
            return daoCliente.loadAll();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar los clientes "+ex);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            return daoItem.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item "+id,ex);
        }
    }

    @Override
    public List<Item> consultarItemsDisponibles() throws ExcepcionServiciosAlquiler{
       try {
            return daoItem.loadAll();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar los clientes "+ex);
        }
    }

    @Override
    public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
        long total=0;
        try {
            ItemRentado ir = daoItemRentado.load(iditem);
             if(ir== null){
                 throw new ExcepcionServiciosAlquiler("El item "+iditem+"no esta en alquiler");
             }else{
                 LocalDate fechaMinimaEntrega = ir.getFechafinrenta().toLocalDate();
                 LocalDate fechaEntrega = fechaDevolucion.toLocalDate();
                 long diasRetraso = ChronoUnit.DAYS.between(fechaMinimaEntrega, fechaEntrega);
                 total=diasRetraso * MULTA_DIARIA;
             }
            
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosAlquilerItemsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
        
   
    }

    @Override
    public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
        TipoItem TI =null;
        try {
            TI = daoTipoItem.load(id);
            
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosAlquilerItemsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TI;
    }

    @Override
    public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
        List<TipoItem> tipos= null;
        try {
            tipos = daoTipoItem.loadAll();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosAlquilerItemsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tipos;
    }

    @Override
    public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
        LocalDate ld=date.toLocalDate();
        LocalDate ld2=ld.plusDays(numdias);
        Date date2 = Date.valueOf(ld2);
        try {
            if(daoCliente.load(docu).getDocumento()== docu){
                daoCliente.AddItemRentado((int)docu,item.getId(),date,date2);   
            }
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosAlquilerItemsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void registrarCliente(Cliente p) throws ExcepcionServiciosAlquiler {
        try {
            daoCliente.save(p);
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosAlquilerItemsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void registrarDevolucion(int iditem) throws ExcepcionServiciosAlquiler {
        //NULL
    }

    @Override
    public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
        long total = 0;
        try {
            if(daoItemRentado.load(iditem).getItem().getId()!= iditem){
                total = daoItem.load(iditem).getTarifaxDia()* numdias;
            }else{
                throw new ExcepcionServiciosAlquiler("El item " + iditem + "no esta en alquiler");
            }
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosAlquilerItemsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    @Override
    public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
        //No estan en el Bean
    }

    @Override
    public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
        //No estan en el Bean
    }


    @Override
    public void vetarCliente(long docu, int estado) throws ExcepcionServiciosAlquiler {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

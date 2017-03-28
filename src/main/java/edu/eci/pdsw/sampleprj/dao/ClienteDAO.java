/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.sampleprj.dao;

import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.entities.ItemRentado;
import java.util.Date;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author hcadavid
 */
public interface ClienteDAO {
    
    public void save(Cliente c) throws PersistenceException;
    
    public Cliente load(long id) throws PersistenceException;
    
    public List<ItemRentado> loadRentados(long id) throws PersistenceException;
    
    public List<Cliente> loadAll() throws PersistenceException;
    
    public void AddItemRentado(int id,int idit,Date fechainicio,Date fechafin) throws PersistenceException;
    
}
/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */

package creamylatte.business.services;

import creamylatte.business.models.Voter;
import java.util.List;
import javax.inject.Inject;


/**
 *
 * @author Hadouken
 */
public class VoterService {
    @Inject
    DBService service;
    
    
    public List<Voter> all(){
        return service.getManager().createNamedQuery("Voter.findAll").getResultList();
    }
    
    
    
}
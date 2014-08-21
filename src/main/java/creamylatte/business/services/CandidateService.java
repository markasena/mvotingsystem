/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */


package creamylatte.business.services;

import creamylatte.business.models.Candidate;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.Query;


/**
 *
 * @author Hadouken
 * This class handles all transaction for the Candidate Entity
 */
public class CandidateService {
    
    @Inject
    private DBService service;

    
    public List<Candidate> all(){
        return service.getManager().createNamedQuery("Candidate.findAll").getResultList();
    }
    
    public List<Candidate> search(String search){        
        List<Candidate> searched = new ArrayList<>();
        List<Candidate> firstNameList = service.getManager().createNamedQuery("Candidate.findByFirstName")
                .setParameter("firstName","%" + search + "%").getResultList();
        List<Candidate> lastNameList = service.getManager().createNamedQuery("Candidate.findByLastName")
                .setParameter("lastName", "%" + search + "%").getResultList();
        firstNameList.stream().filter((candidate) -> (!searched.contains(candidate))).forEach((candidate) -> {
            searched.add(candidate);
        });
        lastNameList.stream().filter((candidate) -> (!searched.contains(candidate))).forEach((candidate) -> {
            searched.add(candidate);
        });        
        return searched;
    }
    
    public List<Candidate> searchByGradeLevel(String gradeLevel){
        Query query = service.getManager().createNamedQuery("Candidate.findByGradeLevel");
        query.setParameter("gradeLevel", gradeLevel);
        return query.getResultList();
    }
    
    
   
    public void save(Candidate candidate){
        service.getTransaction().begin();
        Candidate merged = this.service.getManager().merge(candidate);;
        service.getTransaction().commit();
    }
    
    public void remove(Candidate candidate){
        service.getTransaction().begin();
        service.getManager().remove(candidate);
        service.getTransaction().commit();
    }
    
    public Candidate find(int id){        
        Candidate c = service.getManager().find(Candidate.class, id);
        if(c==null){
            return null;
        }
        return c;
    }
    
    
}

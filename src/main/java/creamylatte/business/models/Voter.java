/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */

package creamylatte.business.models;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Hadouken
 */
@Entity
@Table(name = "voter")
@NamedQueries({
@NamedQuery(name = "Voter.findAll", query = "SELECT v FROM Voter v")})
public class Voter implements Serializable {
    private IntegerProperty id;
    private StringProperty firstName;    
    private StringProperty lastName;    
    private StringProperty gradeLevel;
    private ObjectProperty<UserAccount> account;
    private ListProperty<Candidate> candidates;
    public Voter(){
        this.id = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.gradeLevel = new SimpleStringProperty();  
        this.account = new SimpleObjectProperty<>();        
        this.candidates = new SimpleListProperty<>();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    @Column(name = "grade_level")
    public String getGradeLevel() {
        return this.gradeLevel.get();
    }
    
    public void setGradeLevel(String gradeLevel){
        this.gradeLevel.set(gradeLevel);
    }
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account")
    public UserAccount getAccount(){
        return this.account.get();
    }
    
    public void setAccount(UserAccount account){
        this.account.set(account);
    }
    
    @Override
    public String toString(){
        return this.getLastName() + " , " + getFirstName();
    }
    
    @ManyToMany
    public List<Candidate> getCandidates(){
        return this.candidates.get();
    }
    
    public void setCandidates(List<Candidate> candidates){
        this.candidates.set(FXCollections.observableArrayList(candidates));
    }

    
    
    
}
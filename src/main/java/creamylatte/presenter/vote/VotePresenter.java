/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */

package creamylatte.presenter.vote;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import creamylatte.business.models.UserAccount;


/**
 * FXML Controller class
 *
 * @author Hadouken
 */
public class VotePresenter implements Initializable {
    
    
    @FXML
    private Label labelabel;
    
    
    @FXML
    private Button showMeName;
    
    private ObjectProperty<UserAccount> user;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = new SimpleObjectProperty<>();
    }    

    /**
     * @return the currentUser
     */
    public ObjectProperty<UserAccount> getUser() {
        return user;
    }
    
    @FXML
    private void showMeNameAction(ActionEvent event) {          
        labelabel.setText(user.get().getUsername());
    }
    
}

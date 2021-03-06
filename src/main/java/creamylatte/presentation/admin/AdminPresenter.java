/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */

package creamylatte.presentation.admin;

import creamylatte.presentation.admin.managecandidate.ManageCandidateView;
import creamylatte.presentation.admin.managepartylist.ManagePartyListView;
import creamylatte.presentation.admin.managevoter.ManageVoterView;
import creamylatte.presentation.admin.reports.graphresult.GraphResultView;
import creamylatte.presentation.admin.reports.results.ResultsView;
import creamylatte.presentation.admin.reports.useraccounts.UserAccountsView;
import creamylatte.presentation.login.LoginView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Hadouken
 */
public class AdminPresenter implements Initializable {
    @FXML
    private Button votersButton,manageCandidateButton,partyListButton;
    @FXML
    private AnchorPane centerPane;
    @FXML
    private AnchorPane topPane;
    @FXML
    private Button logoutButton;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button electionResultsButton;
    @FXML
    private Button electionGraphResultButton;
    @FXML
    private Button userReportsButton;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    @FXML
    private void manageCandidateButtonAction(ActionEvent event) {
        changePane(new ManageCandidateView().getView());        
    }
    
    @FXML
    private void partyListAction(ActionEvent event) {
        changePane(new ManagePartyListView().getView());        
    }

    @FXML
    private void votersButtonAction(ActionEvent event) {
        changePane(new ManageVoterView().getView());        
    }

    
    private void changePane(Parent parent){
        centerPane.getChildren().clear();
        centerPane.getChildren().add(parent);
    }

    @FXML
    private void logoutAction(ActionEvent event) {
        AnchorPane anchorPane = (AnchorPane)mainPane.getParent();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(new LoginView().getView());
    }

    @FXML
    private void electionResultsButtonAction(ActionEvent event) {
        changePane(new ResultsView().getView());
    }

    @FXML
    private void userReportsButtonAction(ActionEvent event) {
        changePane(new UserAccountsView().getView());
    }
    
    @FXML
    private void electionGraphResultButtonAction(ActionEvent event){
        changePane(new GraphResultView().getView());
    }
    
}

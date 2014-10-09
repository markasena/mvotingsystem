/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */

package creamylatte.presentation.admin.managecandidate.candidateoverview;

import creamylatte.business.models.Candidate;
import creamylatte.business.models.Party;
import creamylatte.business.services.CandidateService;
import creamylatte.presentation.admin.managecandidate.candidateform.CandidateFormView;
import creamylatte.presentation.admin.managecandidate.candidateprofile.CandidateProfilePresenter;
import creamylatte.presentation.admin.managecandidate.candidateprofile.CandidateProfileView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javax.inject.Inject;

/**
 * FXML Controller class
 *
 * @author Hadouken
 */
public class CandidateOverviewPresenter implements Initializable {
    @FXML
    private ListView<Candidate> candidateListView;
    @FXML
    private ComboBox<Party> partyListComboBox;
    @FXML
    private AnchorPane contentPane,mainPane;
    
    @FXML
    private Button showAllButton,addNewCandidateButton,removeCandidateButton;
    
    ObjectProperty<Candidate> selectedCandidate;
    CandidateProfilePresenter candidateProfilePresenter;
    CandidateProfileView candidateProfileView;
    
    
    @Inject
    CandidateService service;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedCandidate = new SimpleObjectProperty<>();
        candidateProfileView = new CandidateProfileView();
        candidateProfilePresenter = (CandidateProfilePresenter)candidateProfileView.getPresenter();
        contentPane.getChildren().add(candidateProfileView.getView());
        
        
        candidateListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        candidateListView.setItems(FXCollections.observableArrayList(service.getAllCandidates()));
        candidateListView.setCellFactory(new Callback<ListView<Candidate>,ListCell<Candidate>>(){ 
            @Override
            public ListCell<Candidate> call(ListView<Candidate> p) {                 
                final ListCell<Candidate> cell = new ListCell<Candidate>(){ 
                    @Override
                    protected void updateItem(Candidate t, boolean bln) {
                        super.updateItem(t, bln);                         
                        if(t != null){
                            setText(t.getParty().getName() + " - " +  t.getPosition().getName() + " " + t.getVoterId().getLastName() + " , " + 
                                    t.getVoterId().getFirstName());
                        }else{
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        
        partyListComboBox.setItems(FXCollections.observableArrayList(service.getAllParty()));
        partyListComboBox.setCellFactory(new Callback<ListView<Party>,ListCell<Party>>(){ 
            @Override
            public ListCell<Party> call(ListView<Party> p) {                 
                final ListCell<Party> cell = new ListCell<Party>(){ 
                    @Override
                    protected void updateItem(Party t, boolean bln) {
                        super.updateItem(t, bln);                         
                        if(t != null){
                            setText(t.getName());
                        }else{
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        
        partyListComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Party>() {
            @Override
            public void changed(ObservableValue<? extends Party> observable, Party oldValue, Party newValue) {
                if(newValue == null){
                    candidateListView.setItems(FXCollections.observableArrayList(service.getAllCandidates()));
                }else{
                    candidateListView.setItems(FXCollections.observableArrayList(newValue.getCandidates()));
                }
            }
        });
       
        candidateListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Candidate>() {
            @Override
            public void changed(ObservableValue<? extends Candidate> observable, Candidate oldValue, Candidate newValue) {
                selectedCandidate.set(newValue);
                candidateProfilePresenter.setSelectedCandidate(selectedCandidate);
            }
        });
        
    }    
    
    @FXML
    private void partyListComboBoxShown(Event event){
        
    }
    
    private void setListView(Party party){
        
    }
    
    @FXML
    private void showAllButtonAction(ActionEvent event) {
        partyListComboBox.getSelectionModel().clearSelection();
        candidateListView.setItems(FXCollections.observableArrayList(service.getAllCandidates()));
    }
    
    @FXML
    private void addNewCandidateButtonAction(ActionEvent event) {
        changePane(new CandidateFormView().getView());
    }
    
    @FXML
    private void removeCandidateButtonAction(ActionEvent event) {
        
    }
    
    private void changePane(Parent parent){
        AnchorPane anchorPane = (AnchorPane)mainPane.getParent();
        anchorPane.getChildren().clear();
        anchorPane.getChildren().add(parent);
    }
    
}

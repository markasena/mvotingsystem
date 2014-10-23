/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */

package creamylatte.presentation.admin.managecandidate.candidateform;

import creamylatte.business.models.Candidate;
import creamylatte.business.models.ImageWrapper;
import creamylatte.business.models.Party;
import creamylatte.business.models.Position;
import creamylatte.business.models.Voter;
import creamylatte.business.services.CandidateService;
import creamylatte.business.services.VoterService;
import creamylatte.presentation.admin.managecandidate.candidateoverview.CandidateOverviewView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javax.inject.Inject;

/**
 * FXML Controller class
 *
 * @author Hadouken
 */
public class CandidateFormPresenter implements Initializable {
    @FXML
    private ComboBox<Position> positionComboBox;
    @FXML
    private ComboBox<Party> partylistComboBox;

    @FXML
    private Button selectPhotoButton;
    @FXML
    private Button saveButton;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label gradeLevelLabel;
    @FXML
    private ImageView candidateImageView;
    @FXML
    private ListView<Voter> studentListView;

    @FXML
    private TextField filterStudentField;
    
    private AnchorPane anchorPane;
    
    @Inject
    CandidateService service;
    @Inject
    VoterService voterService;
    
    ObservableList<Party> partyList;
    ObservableList<Position> positionList;
    
    ObservableList<Voter> votersData;
    FilteredList<Voter> filteredVotersData;

    ObservableList<Position> positionsData;
    FilteredList<Position> filteredPositionsData;
    
    Image img;    
    File file;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button cancelButton;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        votersData = FXCollections.observableArrayList();
        filteredVotersData = new FilteredList<>(votersData, p -> true);
        filterStudentField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        filteredVotersData.setPredicate(voter ->{
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (voter.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                } else if (voter.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                return false;
            });
        });
        studentListView.setItems(filteredVotersData);
        
        positionsData = FXCollections.observableArrayList();
        filteredPositionsData = new FilteredList<>(positionsData, p -> true);
        partylistComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Party> observable, Party oldValue, Party newValue) -> {
            positionComboBox.setItems(FXCollections.observableArrayList(getAvailablePositions(newValue)));
        });
        
        
        positionComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Position> observable, Position oldValue, Position newValue) -> {
               votersData.clear(); 
               if(newValue.getName().equalsIgnoreCase("President")){                   
                   votersData.addAll(getAvailableCandidates(service.searchByGradeLevel("Ten")));
               }else if(newValue.getName().equalsIgnoreCase("Vice President")){
                   votersData.addAll(getAvailableCandidates(service.searchByGradeLevel("Nine")));
                }else if(newValue.getName().equalsIgnoreCase("Grade 7 Representative")){
                   votersData.addAll(getAvailableCandidates(service.searchByGradeLevel("Seven")));
                }else if(newValue.getName().equalsIgnoreCase("Grade 8 Representative")){
                   votersData.addAll(getAvailableCandidates(service.searchByGradeLevel("Eight")));
                }else if(newValue.getName().equalsIgnoreCase("Grade 9 Representative")){
                   votersData.addAll(getAvailableCandidates(service.searchByGradeLevel("Nine")));
                }else if(newValue.getName().equalsIgnoreCase("Grade 10 Representative")){
                   votersData.addAll(getAvailableCandidates(service.searchByGradeLevel("Ten")));
                }else{
                    votersData.addAll(getAvailableCandidates(service.getAllVoter()));
                }
        });

        partylistComboBox.setCellFactory(new Callback<ListView<Party>,ListCell<Party>>(){ 
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

        positionComboBox.setCellFactory(new Callback<ListView<Position>,ListCell<Position>>(){ 
            @Override
            public ListCell<Position> call(ListView<Position> p) {                 
                final ListCell<Position> cell = new ListCell<Position>(){ 
                    @Override
                    protected void updateItem(Position t, boolean bln) {
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
        
        

       studentListView.setCellFactory(new Callback<ListView<Voter>,ListCell<Voter>>(){ 
            @Override
            public ListCell<Voter> call(ListView<Voter> p) {                 
                final ListCell<Voter> cell = new ListCell<Voter>(){ 
                    @Override
                    protected void updateItem(Voter t, boolean bln) {
                        super.updateItem(t, bln);                         
                        if(t != null){
                            setText(t.getFirstName() + ", " + t.getLastName() +  " : " + t.getGradeLevel());
                        }else{
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
        
        

        studentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Voter>() {
           @Override
           public void changed(ObservableValue<? extends Voter> observable, Voter oldValue, Voter newValue) {
               firstNameLabel.setText(newValue.getFirstName());
               lastNameLabel.setText(newValue.getLastName());
               gradeLevelLabel.setText(newValue.getGradeLevel());
           }
       });

       filterStudentField.disableProperty().bind(positionComboBox.getSelectionModel().selectedItemProperty().isNull());
       studentListView.disableProperty().bind(positionComboBox.getSelectionModel().selectedItemProperty().isNull());
       positionComboBox.disableProperty().bind(partylistComboBox.getSelectionModel().selectedItemProperty().isNull());
       selectPhotoButton.disableProperty().bind(studentListView.selectionModelProperty().isNull());
       saveButton.disableProperty().bind(candidateImageView.imageProperty().isNull());
       
       
    }  
    
    public List<Voter> getAvailableCandidates(List<Voter> voters){
        List<Candidate> candidates = service.getAllCandidates();
        candidates.stream().filter((candidate) -> 
           (voters.contains(candidate.getVoterId()))).forEach((candidate) -> {
            voters.remove(candidate.getVoterId());
        });
        return voters;
    }
    
    
    
    @FXML
    private void showPartyList(Event event) { 
       partyList = FXCollections.observableArrayList(service.getAllParty());
       partylistComboBox.setItems(partyList);
    }
    
    @FXML
    private void showPositions(Event event) { 
        
    }
    
    
    @FXML
    private void selectPhotoAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png)(*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        file = fileChooser.showOpenDialog(selectPhotoButton.getScene().getWindow());
        if(file != null){
            img = new Image("file:" +file.getAbsolutePath());
            candidateImageView.setImage(img);
            
        }
    }


    @FXML
    private void saveButtonAction(ActionEvent event) {
        Candidate candidate = new Candidate();
        candidate.setParty(partylistComboBox.getSelectionModel().getSelectedItem());
        candidate.setPosition(positionComboBox.getSelectionModel().getSelectedItem());
        candidate.setVoterId(studentListView.getSelectionModel().getSelectedItem());
        if(file != null){
            byte[] imageData = new byte[(int) file.length()];
            try {            
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    fileInputStream.read(imageData);
                }
            } catch (IOException e) {
                System.out.println("file error.");
        }
            if(imageData != null){
              ImageWrapper image = new ImageWrapper();            
              image.setImageName(candidate.getVoterId().getFirstName()+candidate.getVoterId().getLastName() + ".png");
              image.setData(imageData); 
              candidate.setImage(image);  
            }
        }
        service.save(candidate);
        
        changePane(new CandidateOverviewView().getView());
    }
    
    
    private List<Position> getAvailablePositions(Party party){
        service.refresh(party);
        List<Position> positions = service.getAllPositions();
        int sargeantCounter = 0;
        int bmanagerCounter = 0;        
        for(Position position: positions){
            if(position.getName().equalsIgnoreCase("Sgt. at Arms")){
                sargeantCounter++;
            }else if(position.getName().equalsIgnoreCase("Bus. Manager")){
                bmanagerCounter++;
            }
        }
        List<Candidate> candidates = party.getCandidates();
        if(candidates.isEmpty()){
            return positions;
        }else{
            for(Candidate candidate : candidates){
                if(candidate.getPosition().getName().equalsIgnoreCase("Sgt. at Arms") 
                        || candidate.getPosition().getName().equalsIgnoreCase("Bus. Manager") ){
                    if(sargeantCounter == 2 || bmanagerCounter == 2){
                        positions.remove(candidate.getPosition());
                    }                    
                }                    
               else
                positions.remove(candidate.getPosition());
            }
            return positions;
        }
    }
    
    
    
    private void changePane(Parent parent){
        AnchorPane mainPane1 = (AnchorPane)mainPane.getParent();
        mainPane1.getChildren().clear();
        mainPane1.getChildren().add(parent);
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        
        
        
    }
    
}

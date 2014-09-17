/*
 * Voting System
 * Project By:  * 
 * Almiradz Mling  * 
 * Eduard John Madriaga  * 
 * Rodz Aguilar Piang  * 
 * Mark Kendrick Asena * 
 */


package creamylatte.presenter.admin.voter;

import creamylatte.business.models.Candidate;
import creamylatte.business.models.UserAccount;
import creamylatte.business.models.Voter;
import creamylatte.business.services.CandidateService;
import creamylatte.presenter.admin.voterform.VoterFormPresenter;
import creamylatte.presenter.admin.voterform.VoterFormView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javax.inject.Inject;

/**
 * FXML Controller class
 *
 * @author Hadouken
 */
public class VoterPresenter implements Initializable {
    
    @FXML
    private Button searchButton,deleteButton,editButton;    
    @FXML
    private TextField searchField;    
    @FXML
    private AnchorPane currentPane;
    @FXML
    private StackPane stackPane;    
    private TableView<Voter> voterTable;  
    
    ProgressIndicator progressIndicator;    
    Region veil;    
    Task<ObservableList<Voter>> task;
    ObservableList<Voter> candidates;
    ObjectProperty<Voter> selectedCandidate;    
    VoterFormPresenter voterFormPresenter;
    VoterFormView voterFormView;
    StringProperty label;
    @Inject
    CandidateService service;
    
    
    @FXML
    private Button addVoter;
    
    
    @FXML
    private PieChart voterChart;
    @FXML
    private AnchorPane dynamicPane;
    @FXML
    private Label dyanmicLabel;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private ComboBox<String> gradeLevelCBox;
    @FXML
    private Button saveButton;
    
    private double grade7,grade8,grade9,grade10;
    
    File file;
    BufferedImage bufferedImage;

    PieChart.Data pdGrade7,pdGrade8,pdGrade9,pdGrade10;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pdGrade7 = new PieChart.Data("Grade 7", grade7);
        pdGrade8 = new PieChart.Data("Grade 8", grade8);
        pdGrade9 = new PieChart.Data("Grade 9", grade9);
        pdGrade10 = new PieChart.Data("Grade 10", grade10);

        this.label = new SimpleStringProperty();
        this.candidates = FXCollections.observableArrayList();
        
        
        this.selectedCandidate = new SimpleObjectProperty<>(new Voter());              
        prepareTable();        
        loadAllVoters();   
        prepareChart();
        rebindProperty();           
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(newValue.isEmpty()){
                loadAllVoters(); 
            }
        });        
        gradeLevelCBox.setItems(FXCollections.observableArrayList("Seven","Eight","Nine","Ten"));
        this.firstNameField.textProperty().addListener(textFieldListeners(this.firstNameField));        
        this.lastNameField.textProperty().addListener(textFieldListeners(this.lastNameField)); 
        
                 final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
        for (final PieChart.Data data : voterChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        caption.setText(String.valueOf(data.getPieValue()) + "%");
                     }
                });
        }
        
        voterTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Voter>() {
            @Override
            public void changed(ObservableValue<? extends Voter> observable, Voter oldValue, Voter newValue) {
                if(newValue == null){
                    disableForm();
                }
            }
        });
    }   

    private void prepareChart(){
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(pdGrade7,pdGrade8,pdGrade9,pdGrade10);         
         voterChart.setData(pieChartData);

         
    }
    
    private void rebindProperty(){
        deleteButton.disableProperty().bind(voterTable.getSelectionModel().selectedItemProperty().isNull()); 
        editButton.disableProperty().bind(voterTable.getSelectionModel().selectedItemProperty().isNull()); 
        searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
//        firstNameField.disableProperty().bind(voterTable.getSelectionModel().selectedItemProperty().isNull());
//        lastNameField.disableProperty().bind(voterTable.getSelectionModel().selectedItemProperty().isNull());
//        gradeLevelCBox.disableProperty().bind(voterTable.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void searchVoter(ActionEvent event) {        
        loadAllVoters();
        rebindProperty();
    }
    
    @FXML
    private void deleteVoter(ActionEvent event) {
        if(voterTable.getSelectionModel().getSelectedItem() != null){
            service.remove(voterTable.getSelectionModel().getSelectedItem());
            stackPane.getChildren().clear();
            loadAllVoters();
            voterTable.getSelectionModel().clearSelection();
        }
        
    }
    
    @FXML
    private void editVoter(ActionEvent event) {        
        if(voterTable.getSelectionModel().getSelectedItem() != null){
            firstNameField.setText(this.voterTable.getSelectionModel().getSelectedItem().getFirstName());
            lastNameField.setText(this.voterTable.getSelectionModel().getSelectedItem().getLastName());
            if(!this.voterTable.getSelectionModel().getSelectedItem().getGradeLevel().equals(""))
                gradeLevelCBox.getSelectionModel().select(this.voterTable.getSelectionModel().getSelectedItem().getGradeLevel());
            enableForm();

        }
    }
    
//    @FXML
//    private void selectPhoto(ActionEvent event){
//        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png)(*.jpg)", "*.jpg");
//        fileChooser.getExtensionFilters().add(extFilter);
//        file = fileChooser.showOpenDialog(iSelectButton.getScene().getWindow());
//
//    }
    
        @FXML
    private void saveVoter(ActionEvent event) {
        Voter c;
        if(this.voterTable.getSelectionModel().getSelectedItem() == null){
            c = new Voter();
        }else{
            c = voterTable.getSelectionModel().getSelectedItem();
        }        
        c.setFirstName(firstNameField.textProperty().get().toLowerCase());
        c.setLastName(lastNameField.textProperty().get().toLowerCase());
        c.setGradeLevel(gradeLevelCBox.getSelectionModel().getSelectedItem());
        UserAccount ua;
        if(c.getAccount() == null){
            ua = new UserAccount();
            ua.setUsername(c.getFirstName()+","+c.getLastName());
            ua.setPassword("" + (int)(Math.random() * 1001));
            System.out.println(ua.getPassword());
            c.setAccount(ua);
        }
        service.save(c);
        loadAllVoters();
//        prepareChart();
        disableForm();
        voterTable.getSelectionModel().clearSelection();
    }
    
    
    
    
    //Prepare the table and columns
    private void prepareTable(){
        voterTable = new TableView<>();
        this.voterTable.setEditable(false);        
        ObservableList columns = voterTable.getColumns();
        final TableColumn firstNameColumn = createTextColumn("firstName", "First Name");
        columns.add(firstNameColumn);
        final TableColumn lastNameColumn = createTextColumn("lastName", "Last Name");
        columns.add(lastNameColumn);
        final TableColumn gradeLevelColumn = createTextColumn("gradeLevel", "Grade Level");
        columns.add(gradeLevelColumn);
        final TableColumn<Candidate, UserAccount> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
        passwordColumn.setCellFactory(new Callback<TableColumn<Candidate, UserAccount>, TableCell<Candidate, UserAccount>>() {
                @Override
                public TableCell<Candidate, UserAccount> call(TableColumn<Candidate, UserAccount> param) {
                    return new TableCell<Candidate, UserAccount>() {
                        @Override
                        protected void updateItem(UserAccount item, boolean empty) {
                            super.updateItem(item, empty);
                          if (!empty) {
                            // Use a SimpleDateFormat or similar in the format method
                            setText(item.getPassword());
                          } else {
                            setText(null);
                          }
                        }
                    };
                }
            });
        columns.add(passwordColumn);
        voterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        voterTable.setItems(this.candidates);
        voterTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);        
    }
    
    //create text column
    private TableColumn createTextColumn(String name, String caption) {
        TableColumn column = new TableColumn(caption);
        column.setCellValueFactory(new PropertyValueFactory<>(name));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        return column;
    }
    
    //load all candidates from DB
    private void loadAllVoters() {
        stackPane.getChildren().clear();
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(150, 150);
        veil = new Region();
        veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
        task = new Task<ObservableList<Voter>>() {
            @Override
            protected ObservableList<Voter> call() throws Exception {                
                    List<Voter> votersList= service.all();
                if(!searchField.getText().isEmpty()){
                    votersList = service.search(searchField.textProperty().get());
                }                
                Thread.sleep(5);
                candidates = FXCollections.observableArrayList(votersList);                
                return candidates;
            }
        };
        progressIndicator.progressProperty().bind(task.progressProperty());
        veil.visibleProperty().bind(task.runningProperty());
        progressIndicator.visibleProperty().bind(task.runningProperty());
        voterTable.itemsProperty().bind(task.valueProperty()); 
        stackPane.getChildren().addAll(voterTable,veil,progressIndicator);
        new Thread(task).start();
        voterTable.itemsProperty().addListener(new ChangeListener<ObservableList<Voter>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<Voter>> observable, ObservableList<Voter> oldValue, ObservableList<Voter> newValue) {
                pdGrade7.pieValueProperty().set(service.searchByGradeLevel("Seven").size());
                pdGrade8.pieValueProperty().set(service.searchByGradeLevel("Eight").size());
                pdGrade9.pieValueProperty().set(service.searchByGradeLevel("Nine").size());
                pdGrade10.pieValueProperty().set(service.searchByGradeLevel("Ten").size());
            }
        });
        
    }

    @FXML
    private void addVoter(ActionEvent event) {
        voterTable.getSelectionModel().clearSelection();
        firstNameField.clear();
        lastNameField.clear();
        gradeLevelCBox.getSelectionModel().clearSelection();
        enableForm();
    }


    
    private void enableForm(){
        firstNameField.setVisible(true);
        lastNameField.setVisible(true);
        gradeLevelCBox.setVisible(true);
        saveButton.setVisible(true);
    }
    
    private void disableForm(){
        firstNameField.setVisible(false);
        lastNameField.setVisible(false);
        gradeLevelCBox.setVisible(false);
        saveButton.setVisible(false);
    }
    
    
    private String firstCharToUpperCase(String string){
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
    
     private ChangeListener<String> textFieldListeners(TextField tf){
        final Pattern wholeNumberPattern = Pattern.compile("[A-Za-zñÑ]*");
        return (final ObservableValue<? extends String> observableValue, final String oldValue, final String newValue) -> {
            if (!wholeNumberPattern.matcher(newValue).matches())
                tf.setText(oldValue);
        };
    }
}

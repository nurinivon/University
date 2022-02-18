import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
/*
 * Controller is the class that uses as controller to handle the GUI's events
 */
public class Controller {
	//The following nodes are attached to the GUI nodes
    @FXML
    private Button addContactButton;

    @FXML
    private TableView<PhoneBookRecord> phoneBookTable;

    @FXML
    private TableColumn<PhoneBookRecord, String> nameCol;

    @FXML
    private TableColumn<PhoneBookRecord, String> phoneCol;
    
    @FXML
    private TableColumn<PhoneBookRecord, String> actionsCol;

    @FXML
    private TextField searchInput;

    @FXML
    private Button exportButton;

    @FXML
    private Button importButton;
    //constant variable for the class
    private final String PRE_DELETE_MESSAGE = "Are you sure you want to delete this record?";
    private final String SUCCESS_DELETE_MESSAGE = "Record deleted successfully";
    private final String SUCCESS_INSERT_MESSAGE = "Record inserted successfully";
    private final String SUCCESS_EDIT_MESSAGE = "Record updated successfully";
    private final String MUST_SELECT_DIRECTORY_MESSAGE = "You must select directory first";
    private final String MUST_SELECT_FILE_MESSAGE = "You must select file first";
    private final String SUCCESS_IMPORT_MESSAGE = "Records Imported Successfully";
    private final String SUCCESS_EXPORT_MESSAGE = "Records Exported Successfully";
    private final double PREF_INPUT_WIDTH = 400.0;
    private final double PREF_LABEL_WIDTH = 75.0;
    private final int PREF_VBOX_SPACING = 20;
    
  //private attributes for the controller class
    private static PhoneBook myPb;
    private File currentSelectedDirectory;
    private File currentSelectedFile;
    
    /*
     * initialize method using the FXML annotation is called after the Scene is loaded, and allow to initialize what we want in the program.
     */
    @FXML
    public void initialize() {
    	//initialize the phone book
    	myPb = new PhoneBook();
    	//add event listener for search text field.
    	this.searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
    		updateTableView();
    	});
    	//call initializing the table view
    	initTable();
    }
    
    /*
     * initTable is called after the controller is initialized.
     * it adjust the Table View to be feet with PhoneBookRecord instances so we could just add PhoneBookRecord items to the table.
     * for each row in the table we will add a Delete and Edit buttons
     */
    public void initTable() {
    	//set the cell factory values to the PhoneBookRecord attributes
    	nameCol.setCellValueFactory(new PropertyValueFactory<PhoneBookRecord, String>("name"));
    	phoneCol.setCellValueFactory(new PropertyValueFactory<PhoneBookRecord, String>("phone"));
    	
    	//create the actions cell with buttons for delete and edit actions
    	Callback<TableColumn<PhoneBookRecord, String>, TableCell<PhoneBookRecord, String>> cellFactory = new Callback<TableColumn<PhoneBookRecord, String>, TableCell<PhoneBookRecord, String>>() {
		    @Override
		    public TableCell<PhoneBookRecord, String> call(final TableColumn<PhoneBookRecord, String> param) {
		        final TableCell<PhoneBookRecord, String> cell = new TableCell<PhoneBookRecord, String>() {
		        	//create the buttons
		            final Button deleteBtn = new Button("Delete");
		            final Button editBtn = new Button("Edit");
		
		            @Override
		            public void updateItem(String item, boolean empty) {
		            	//only if there is an item in the row the buttons will show
		                super.updateItem(item, empty);
		                if (empty) {
		                    setGraphic(null);
		                    setText(null);
		                } else {
		                	//set the action events for the buttons
		                	deleteBtn.setOnAction(event -> {
		                    	PhoneBookRecord rec = getTableView().getItems().get(getIndex());
		                		deleteRow(rec);
		                    });
		                	editBtn.setOnAction(event -> {
		                    	PhoneBookRecord rec = getTableView().getItems().get(getIndex());
		                		editRow(rec);
		                    });
		                	//the buttons will be shown in HBox one next to each other
		                	HBox buttonsBox = new HBox();
		                	buttonsBox.getChildren().addAll(deleteBtn,editBtn);
		                    setGraphic(buttonsBox);
		                    setText(null);
		                }
		            }
		        };
		        return cell;
		    }
		};
		
		actionsCol.setCellFactory(cellFactory);
		
    }
    
    /*
     * deleteRow method is called when clicking the delete button on each row.
     * it gets the PhoneBookRecord that connected to the row and deletes it from the phone book
     * than it refresh the table view 
     */
    public void deleteRow(PhoneBookRecord rec) {
    	int input = JOptionPane.showConfirmDialog(null, PRE_DELETE_MESSAGE, null, JOptionPane.OK_CANCEL_OPTION);
    	if(input == JOptionPane.OK_OPTION) {
    		myPb.delete(rec);
			JOptionPane.showMessageDialog(null, SUCCESS_DELETE_MESSAGE);
    		updateTableView();
    	}
    }
    
    /*
     * editRow method is called when clicking the edit button on each row.
     * it gets the PhoneBookRecord that connected to the row and opens a dialog that allow to edit its values.
     */
    public void editRow(PhoneBookRecord originalRec) {
    	//creating custom dialog
    	Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Edit Contact");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        //creating VBox to store the nodes one above each other
        VBox fieldsBox = new VBox();
        
        //each field will be stored with a label and text field one next to each other in HBox
        HBox nameBox = new HBox();
        Label nameLabel = new Label("Name: ");
        TextField name = new TextField();
        nameLabel.setPrefWidth(PREF_LABEL_WIDTH);
        name.setPromptText("Name");
        name.setText(originalRec.getName());
        name.setPrefWidth(PREF_INPUT_WIDTH);
        nameBox.getChildren().add(nameLabel);
        nameBox.getChildren().add(name);
        
        //each field will be stored with a label and text field one next to each other in HBox
        HBox phoneBox = new HBox();
        Label phoneLabel = new Label("Phone: ");
        phoneLabel.setPrefWidth(PREF_LABEL_WIDTH);
        TextField phone = new TextField();
        phone.setPromptText("Phone");
        phone.setText(originalRec.getPhone());
        phone.setPrefWidth(PREF_INPUT_WIDTH);
        phoneBox.getChildren().add(phoneLabel);
        phoneBox.getChildren().add(phone);
        
        //adding the name and phone fields to the VBox
        fieldsBox.getChildren().add(nameBox);
        fieldsBox.getChildren().add(phoneBox);

        fieldsBox.setSpacing(PREF_VBOX_SPACING);
        
        //adding the VBox to the dialog content
        dialog.getDialogPane().setContent(fieldsBox);
        
        /*
         * adjusting the OK button to perform the edit of the row
         */
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
        	try {
        		//try to create new PhoneBookRecord, if it succeed it will update the original row
				PhoneBookRecord newRec = new PhoneBookRecord(name.getText(), phone.getText());
				myPb.update(originalRec, newRec.getName(), newRec.getPhone());
	    		JOptionPane.showMessageDialog(null, SUCCESS_EDIT_MESSAGE);
	    		//update the table view with the updated row
	    		updateTableView();
			} catch (Exception e) {
				ae.consume(); //consume means the dialog will not be closed and allow the user to perform changes
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
        });
        dialog.show();
    }
    
    /*
     * addContactClicked method is called when the "Add Contact" button is clicked.
     * it opens a dialog and allow the user to create a new contact and save it
     */
    public void addContactClicked(ActionEvent e) {
    	//creating custom dialog
    	Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Add Contact");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        //creating VBox to store the nodes one above each other
        VBox fieldsBox = new VBox();
        
        //each field will be stored with a label and text field one next to each other in HBox
        HBox nameBox = new HBox();
        Label nameLabel = new Label("Name: ");
        nameLabel.setPrefWidth(PREF_LABEL_WIDTH);
        TextField name = new TextField();
        name.setPromptText("Name");
        name.setPrefWidth(PREF_INPUT_WIDTH);
        nameBox.getChildren().add(nameLabel);
        nameBox.getChildren().add(name);
        
        //each field will be stored with a label and text field one next to each other in HBox
        HBox phoneBox = new HBox();
        Label phoneLabel = new Label("Phone: ");
        phoneLabel.setPrefWidth(PREF_LABEL_WIDTH);
        TextField phone = new TextField();
        phone.setPromptText("Phone");
        phone.setPrefWidth(PREF_INPUT_WIDTH);
        phoneBox.getChildren().add(phoneLabel);
        phoneBox.getChildren().add(phone);
        
        //adding the name and phone fields to the VBox
        fieldsBox.getChildren().add(nameBox);
        fieldsBox.getChildren().add(phoneBox);
        
        fieldsBox.setSpacing(PREF_VBOX_SPACING);
        
        //adding the VBox to the dialog content
        dialog.getDialogPane().setContent(fieldsBox);
        
        /*
         * adjusting the OK button to perform the insertion of the new row
         */
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
    		try {
    			//try to create new PhoneBookRecord, if it succeed it will be inserted to the phone book
    			PhoneBookRecord newRec = new PhoneBookRecord(name.getText(), phone.getText());
    			myPb.add(newRec);
        		this.searchInput.setText(""); //clear the search field
        		JOptionPane.showMessageDialog(null, SUCCESS_INSERT_MESSAGE);
        		//update the table view with the new row
        		updateTableView();
    		} catch (Exception error) {
    			ae.consume(); //consume means the dialog will not be closed and allow the user to perform changes
    			JOptionPane.showMessageDialog(null, error.getMessage());
    		}
        });
        dialog.show();
    }
    
    /*
     * updateTableView method is updating the Table UI with the appropriate Contacts according to the search dield input.
     * First it clears the current items, than it gets an iterator with the records.
     * For each entry, it create a row in the table
     */
    public void updateTableView() {
    	phoneBookTable.getItems().clear();
		Iterator<PhoneBookRecord> it = myPb.get(this.searchInput.getText());
		while(it.hasNext()) {
			PhoneBookRecord entry = it.next();
			phoneBookTable.getItems().add(entry);
		}
    }
    
    /*
     * importButtonClicked method is called after the import button is clicked.
     * It opens a dialog that allows the user to choose a file with PBRSerializable Objects and it will insert them to the phone book
     */
    public void importButtonClicked(ActionEvent e) {
    	//creating custom dialog
    	Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Import Contacts");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        //creating VBox to store the nodes one above each other
        VBox fieldsBox = new VBox();
        
        //creating file chooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        currentSelectedFile = null; //init the currentSelectedFile to null
        
        //creating text field to show the path to the selected file
        HBox pathBox = new HBox();
        Label pathLabel = new Label("Path: ");
        TextField path = new TextField();
        path.setDisable(true);//the text field is disabled as it correlated to the selected file
        path.setPrefWidth(PREF_INPUT_WIDTH);
        pathBox.getChildren().add(pathLabel);
        pathBox.getChildren().add(path);
        
        //adjusting the choose file button to invoke the file chooser
        Button chooseFileyButton = new Button("Choose File");
        chooseFileyButton.addEventFilter(ActionEvent.ACTION, ae -> {
        	//the choose file dialog needs to be aimed on which scene to perform
            Node currentNode = (Node) ae.getSource();
            //save the selected file to the currentSelectedFile var
            currentSelectedFile = fileChooser.showOpenDialog((Stage) currentNode.getScene().getWindow());
            //show in the path field the current file path
            if(currentSelectedFile != null) {
            	path.setText(currentSelectedFile.getPath());
    		}else {
            	path.setText("");
    		}
        });
        //adding the field and the button the VBox
        fieldsBox.getChildren().add(chooseFileyButton);
        fieldsBox.getChildren().add(pathBox);

        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setSpacing(PREF_VBOX_SPACING);
        //adding the VBox to the dialog content
        dialog.getDialogPane().setContent(fieldsBox);
        //adjusting the on action event for the OK button to perform the import action
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
        	//check if there is file selected
    		if(currentSelectedFile != null) {
    			//try to import the the records and handle exceptions. the user will be informed with the import status
    			try {
					myPb.importContacts(currentSelectedFile);
        			JOptionPane.showMessageDialog(null, SUCCESS_IMPORT_MESSAGE);
        			//update the table view with the new imported records
        			updateTableView();
				} catch (FileNotFoundException  e1) {
					//e1.printStackTrace();
        			JOptionPane.showMessageDialog(null, e1.getMessage());
				}catch(IOException e2) {
					//e2.printStackTrace();
        			JOptionPane.showMessageDialog(null, e2.getMessage());
				}catch(ClassNotFoundException  e3) {
					//e3.printStackTrace();
        			JOptionPane.showMessageDialog(null, e3.getMessage());
				}catch (Exception  e4) {
					//e4.printStackTrace();
        			JOptionPane.showMessageDialog(null, e4.getMessage());
				}
    		}else {
    			ae.consume(); //consume preventing the dialog from closing
    			JOptionPane.showMessageDialog(null, MUST_SELECT_FILE_MESSAGE);
    		}
	    });
	    dialog.show();
    }
    
    /*
     * importButtonClicked method is called after the export button is clicked.
     * It opens a dialog that allows the user to choose a directory in which he wants the output file will be saved.
     * The output file name is "output.txt"
     */
    public void exportButtonClicked(ActionEvent e) {
    	//creating custom dialog
    	Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Export Contacts");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        //creating VBox to store the nodes one above each other
        VBox fieldsBox = new VBox();
        
        //create a directory choose instance
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        currentSelectedDirectory = null; //init current directory to null
        
        //creating text field to show the path to the selected directory
        HBox pathBox = new HBox();
        Label pathLabel = new Label("Path: ");
        TextField path = new TextField();
        path.setDisable(true);//the text field is disabled as it correlated to the selected file
        path.setPrefWidth(PREF_INPUT_WIDTH);
        pathBox.getChildren().add(pathLabel);
        pathBox.getChildren().add(path);
        
        //adjusting the choose directory button to invoke the directory chooser
        Button chooseDirectoryButton = new Button("Choose Directory");
        chooseDirectoryButton.addEventFilter(ActionEvent.ACTION, ae -> {
        	//the choose directory dialog needs to be aimed on which scene to perform
            Node currentNode = (Node) ae.getSource();
            //save the selected directory to the currentSelectedDirectory var
            currentSelectedDirectory = directoryChooser.showDialog((Stage) currentNode.getScene().getWindow());
            //show in the path field the current directory path
            if(currentSelectedDirectory != null) {
            	path.setText(currentSelectedDirectory.getPath());
    		}else {
            	path.setText("");
    		}
        });
        //adding the field and the button the VBox
        fieldsBox.getChildren().add(chooseDirectoryButton);
        fieldsBox.getChildren().add(pathBox);

        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setSpacing(PREF_VBOX_SPACING);
        //adding the VBox to the dialog content
        dialog.getDialogPane().setContent(fieldsBox);
        //adjusting the on action event for the OK button to perform the export action
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
        		//check if there is directory selected
        		if(currentSelectedDirectory != null) {
        			//try to export the the records and handle exceptions. the user will be informed with the export status
        			try {
						myPb.exportContacts(currentSelectedDirectory, this.searchInput.getText());
	        			JOptionPane.showMessageDialog(null, SUCCESS_EXPORT_MESSAGE + "\n" + currentSelectedDirectory + "\\output.txt");
					} catch (FileNotFoundException  e1) {
						//e1.printStackTrace();
	        			JOptionPane.showMessageDialog(null, e1.getMessage());
					}catch(IOException e2) {
						//e2.printStackTrace();
	        			JOptionPane.showMessageDialog(null, e2.getMessage());
					}catch(ClassNotFoundException  e3) {
						//e3.printStackTrace();
	        			JOptionPane.showMessageDialog(null, e3.getMessage());
					}
        		}else {
        			ae.consume();//consume preventing the dialog from closing
        			JOptionPane.showMessageDialog(null, MUST_SELECT_DIRECTORY_MESSAGE);
        		}
        });
        dialog.show();
    }
}

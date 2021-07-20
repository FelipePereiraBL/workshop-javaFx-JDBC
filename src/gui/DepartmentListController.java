package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentServices;
import gui.util.Utils;

public class DepartmentListController implements Initializable
{
	private DepartmentServices services;
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer > tableColumId;
	@FXML
	private TableColumn<Department, String > tableColumName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	private void onBtNewAction(ActionEvent event)
	{
		Stage parentStage=Utils.currentStage(event);
		createDialogForm("/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentServices services)
	{
		this.services=services;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{	
		initializeNodes();
		
		Stage stage=(Stage)Main.getMainScene().getWindow();
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	private void initializeNodes() 
	{
		tableColumId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("name"));
	}
	
	public void updateTableView()
	{
		if(services==null)
		{
			throw new IllegalStateException("Services was null");
		}
		
		List<Department>list=services.findAll();
		obsList=FXCollections.observableArrayList(list);
		
		tableViewDepartment.setItems(obsList);
	}
	
	private void createDialogForm(String absoluteName, Stage parentStage)
	{
		try 
		{
			FXMLLoader loader=new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane=loader.load();
			
			Stage dialogStage=new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} 
		catch (IOException e) 
		{
			Alerts.showAlerts("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
		
	}

}

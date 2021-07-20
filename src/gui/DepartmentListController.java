package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentServices;

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
	private void onBtNewAction()
	{
		System.out.println("onBtNewAction");
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

}

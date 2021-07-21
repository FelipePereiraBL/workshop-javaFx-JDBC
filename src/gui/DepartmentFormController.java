package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentServices;

public class DepartmentFormController implements Initializable
{
	private DepartmentServices service;
	
	private Department entity;
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity)
	{
		this.entity=entity;
	}
	
	public void setDepartmentService(DepartmentServices service)
	{
		this.service=service;
	}
	
	@FXML
	public  void onBtSaveAction(ActionEvent event)
	{
		if(entity==null)
		{
			throw new IllegalStateException("Services was null");
		}
		if(service==null)
		{
			throw new IllegalStateException("Services was null");
		}
		try 
		{
			entity=getFormData();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close();
		}
		catch (DbException e) 
		{
			Alerts.showAlerts("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private Department getFormData() 
	{
		Department obj=new Department();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
		return obj;
	}
	@FXML
	public  void onBtCancelAction(ActionEvent event)
	{
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rs)
	{		
		initializeNodes();
	}
	
	private void initializeNodes()
	{
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData()
	{
		if(entity==null)
		{
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

}

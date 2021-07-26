package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListeners;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerServices;

public class SellerFormController implements Initializable
{
	private SellerServices service;
	
	private List<DataChangeListeners>dataChangeListeners=new ArrayList<>();
	
	private Seller entity;
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	public void setSeller(Seller entity)
	{
		this.entity=entity;
	}
	
	public void setSellerService(SellerServices service)
	{
		this.service=service;
	}
	
	public void subscribleChangeListener(DataChangeListeners listener)
	{
		dataChangeListeners.add(listener);
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
			notifyChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e)
		{
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) 
		{
			Alerts.showAlerts("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notifyChangeListeners() 
	{
		for (DataChangeListeners listener : dataChangeListeners)
		{
			listener.onChanged();
			
		}
		
	}

	private Seller getFormData() 
	{
		Seller obj=new Seller();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		ValidationException exception=new ValidationException("Validate Exception");
		
		if(txtName.getText()==null||txtName.getText().trim().equals(""))
		{
			exception.addErrors("name", "Field can´t be empty");
		}
		
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size()>0)
		{
			throw exception;
		}
		
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
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
	}
	
	public void updateFormData()
	{
		if(entity==null)
		{
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
		if(entity.getBirthDate()!=null)
		{
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}		
	}
	
	private void setErrorMessages(Map<String,String>errors)
	{
		Set<String>fields=errors.keySet();
		
		if(fields.contains("name"))
		{
			labelErrorName.setText(errors.get("name"));
		}
		
	}

}

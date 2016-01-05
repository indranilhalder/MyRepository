/**
 *
 */
package com.hybris.oms.tata.logisticscontacts;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.logistics.LogisticsFacade;
import com.hybris.oms.domain.logistics.dto.Logistics;
import com.hybris.oms.tata.services.EmailValidator;
import com.hybris.oms.tata.services.PhoneNumberValidation;


/**
 * This class is to show the editorial area in logistic widget
 * 
 */
public class LogisticsContactsEditor extends DefaultWidgetController
{
	private static final Logger LOG = LoggerFactory.getLogger(LogisticsContactsEditor.class);

	private final PhoneNumberValidation phonenumValidation = new PhoneNumberValidation();

	private final EmailValidator emailValidation = new EmailValidator();

	@Autowired
	private LogisticsFacade logisticsRestClient;
	private Grid createGrid;
	private Grid editGrid;

	private Textbox logisticsId;
	private Textbox logisticsName;
	private Textbox address;
	private Textbox contactName;
	private Textbox phoneNo;
	private Label lPNum;
	private Textbox faxNo;
	private Label lFax;
	private Textbox email;
	private Label lEmail;
	private Checkbox isActive;

	private Textbox logisticsIdEdit;
	private Textbox logisticsNameEdit;
	private Textbox addressEdit;
	private Textbox contactNameEdit;
	private Textbox phoneNoEdit;
	private Label lPNumEdit;
	private Textbox faxNoEdit;
	private Label lFaxEdit;
	private Textbox emailEdit;
	private Label lemailEdit;
	private Checkbox isActiveEdit;


	/**
	 * method to activate create and deactivate the update in editor area while selecting add symbol
	 * 
	 * @param status
	 * @throws InterruptedException
	 */
	@SocketEvent(socketId = "isactivecreate_contact")
	public void windowActiveCreate(final Boolean status) throws InterruptedException
	{

		createGrid.setVisible(status.booleanValue());
		editGrid.setVisible(false);

	}

	@Override
	public void initialize(final Component comp)
	{

		super.initialize(comp);
		lEmail.setValue("");
		lemailEdit.setValue("");
		lPNum.setValue("");
		lPNumEdit.setValue("");
		lFax.setValue("");
		lFaxEdit.setValue("");
		doFormEmpty(logisticsId, logisticsName, address, contactName, phoneNo, faxNo, email, isActive);

	}

	/**
	 * method to activate edit and deactivate the create in editor area while selecting edit symbol
	 * 
	 * @param status
	 * @throws InterruptedException
	 */
	@SocketEvent(socketId = "isactiveedit_contact")
	public void windowActiveEdit(final Object selectedItem) throws InterruptedException
	{
		final Logistics logistics = (Logistics) selectedItem;
		createGrid.setVisible(false);
		editGrid.setVisible(true);
		lemailEdit.setValue("");
		lPNumEdit.setValue("");
		lFaxEdit.setValue("");
		doFormEmpty(logisticsIdEdit, logisticsNameEdit, addressEdit, contactNameEdit, phoneNoEdit, faxNoEdit, emailEdit,
				isActiveEdit);
		logisticsIdEdit.setValue(logistics.getLogisticsid());
		logisticsNameEdit.setValue(logistics.getLogisticname());
		addressEdit.setValue(logistics.getAddress());
		contactNameEdit.setValue(logistics.getCName());
		phoneNoEdit.setValue(logistics.getCPhone());
		if (logistics.getActive())
		{
			isActiveEdit.setChecked(true);
		}
		faxNoEdit.setValue(logistics.getCFax());
		emailEdit.setValue(logistics.getCEmail());

	}

	/**
	 * method to save the newly create Logistics contact in database
	 * 
	 * and generate one socket event for updating the list view
	 * 
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "addSave", eventName = Events.ON_CLICK)
	public void addSave() throws InterruptedException
	{
		//calling webservices to create a new Logistics Contacts
		try
		{
			final Logistics logistics = getLogisticsContactsFormData(logisticsId, logisticsName, address, contactName, phoneNo,
					faxNo, email, isActive);
			if (logistics != null)
			{
				logisticsRestClient.createLogistics(logistics);
				sendOutput("refreshlogisticscontactlist", Boolean.TRUE);
				doFormEmpty(logisticsId, logisticsName, address, contactName, phoneNo, faxNo, email, isActive);
				Messagebox.show("Logistics Contacts Added Successfully");
			}
		}
		catch (final Exception e)
		{
			sendOutput("refreshlogisticscontactlist", Boolean.FALSE);
			LOG.error("Unable to create New Logistics Contacts  Error : " + e.getMessage());
		}


	}

	/**
	 * method to update Logistics calendar in database and generate one socket event for updating the list view
	 * 
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "updateSave", eventName = Events.ON_CLICK)
	public void updateSave() throws InterruptedException
	{

		try
		{
			final Logistics logistics = getLogisticsContactsFormData(logisticsIdEdit, logisticsNameEdit, addressEdit,
					contactNameEdit, phoneNoEdit, faxNoEdit, emailEdit, isActiveEdit);
			if (logistics != null)
			{
				logisticsRestClient.updateLogistics(logistics);
				sendOutput("refreshlogisticscontactlist_edit", Boolean.TRUE);
				Messagebox.show("Logistics Contacts Updated Successfully");
				//method to empty the fields
			}
		}
		catch (final Exception e)
		{
			sendOutput("refreshlogisticscontactlist_edit", Boolean.FALSE);
			LOG.error("Unable to update the  Logistics Contacts, Error : " + e.getMessage());
		}


	}

	/**
	 * 
	 * This socket event disable the create and editor widgets, after delete action performed in the list widget
	 */

	@SocketEvent(socketId = "contact_disble_contact_editor")
	public void disableAll(final Boolean selectedItem) throws InterruptedException
	{

		createGrid.setVisible(false);
		editGrid.setVisible(false);
	}

	@ViewEvent(componentID = "email", eventName = Events.ON_BLUR)
	public void emailValidationAction()
	{
		if (!emailValidation.validate(email.getValue()))
		{
			lEmail.setValue("Enter valid email..");
			//	Clients.showNotification("Not a email", "error", email, "top_right", 15);
		}

		else
		{
			lEmail.setValue("");
		}

	}

	@ViewEvent(componentID = "emailEdit", eventName = Events.ON_BLUR)
	public void emaieditlValidation()
	{
		if (!emailValidation.validate(emailEdit.getValue()))
		{
			lemailEdit.setValue("Enter valid email..");
			//Clients.showNotification("Not a email", "error", email, "top_right", 15);

		}

		else
		{
			lemailEdit.setValue("");
		}

	}

	@ViewEvent(componentID = "phoneNo", eventName = Events.ON_BLUR)
	public void phoneNoValidation()
	{
		if (!phonenumValidation.validate(phoneNo.getValue()))
		{
			lPNum.setValue("Enter valid phone number..");
			//Clients.showNotification("Not a email", "error", email, "top_right", 15);

		}

		else
		{
			lPNum.setValue("");
		}

	}

	@ViewEvent(componentID = "phoneNoEdit", eventName = Events.ON_BLUR)
	public void phoneeditNoValidation()
	{
		if (!phonenumValidation.validate(phoneNoEdit.getValue()))
		{
			lPNumEdit.setValue("Enter valid phone number..");
			//Clients.showNotification("Not a email", "error", email, "top_right", 15);

		}

		else
		{
			lPNumEdit.setValue("");
		}

	}

	@ViewEvent(componentID = "faxNo", eventName = Events.ON_BLUR)
	public void lFaxNoValidation()
	{
		if (!phonenumValidation.validate(faxNo.getValue()))
		{
			lFax.setValue("Enter valid fax number..");
			//Clients.showNotification("Not a email", "error", email, "top_right", 15);

		}

		else
		{
			lFax.setValue("");
		}

	}

	@ViewEvent(componentID = "faxNoEdit", eventName = Events.ON_BLUR)
	public void faxNoeditNoValidation()
	{
		if (!phonenumValidation.validate(faxNoEdit.getValue()))
		{
			lFaxEdit.setValue("Enter valid fax number..");
			//Clients.showNotification("Not a email", "error", email, "top_right", 15);

		}

		else
		{
			lFaxEdit.setValue("");
		}

	}

	/*
	 * @ViewEvent(componentID = "phoneNo", eventName = Events.ON_CHANGING) public void phonenumValidation() {
	 * 
	 * if(phoneNo.getV
	 * 
	 * }
	 */
	public Logistics getLogisticsContactsFormData(final Textbox logisticsContactId, final Textbox logisticsName,
			final Textbox logisticsContactAddress, final Textbox logisticsContactName, final Textbox logisticsPhoneNo,
			final Textbox contactFaxNo, final Textbox logisticsContactEmail, final Checkbox isActive) throws InterruptedException

	{

		final Logistics logistics = new Logistics();

		if (logisticsContactId.getValue().trim().equals("") || logisticsName.getValue().trim().equals("")
				|| logisticsContactAddress.getValue().trim().equals("") || logisticsContactName.getValue().trim().equals("")
				|| logisticsPhoneNo.getValue().trim().equals("") || contactFaxNo.getValue().trim().equals("")
				|| logisticsContactEmail.getValue().trim().equals("") || logisticsContactEmail.getValue().trim().equals("")
				|| logisticsPhoneNo.getValue().trim().equals("") || contactFaxNo.getValue().trim().equals(""))
		{

			Messagebox.show("Please Fill All Fields");
			return null;
		}

		if (!emailValidation.validate(logisticsContactEmail.getValue())
				|| !phonenumValidation.validate(logisticsPhoneNo.getValue()) || !phonenumValidation.validate(contactFaxNo.getValue()))
		{

			Messagebox.show("Please first salve the form errors..");
			return null;
		}
		logistics.setLogisticsid(logisticsContactId.getValue());
		logistics.setLogisticname(logisticsName.getValue());
		logistics.setAddress(logisticsContactAddress.getValue());
		logistics.setCName(logisticsContactName.getValue());
		logistics.setCPhone(logisticsPhoneNo.getValue());
		logistics.setCFax(contactFaxNo.getValue());
		logistics.setCEmail(logisticsContactEmail.getValue());

		if (isActive.isChecked())
		{
			logistics.setActive(true);
		}
		else
		{
			logistics.setActive(false);
		}
		return logistics;

	}


	public void doFormEmpty(final Textbox logisticsContactId, final Textbox logisticsName, final Textbox logisticsContactAddress,
			final Textbox logisticsContactName, final Textbox logisticsPhoneNo, final Textbox contactFaxNo,
			final Textbox logisticsContactEmail, final Checkbox isActive)

	{
		LOG.info("***************************************initialized method");
		logisticsContactId.setValue("");
		logisticsName.setValue("");
		logisticsContactAddress.setValue("");
		logisticsContactName.setValue("");
		logisticsPhoneNo.setValue("");
		contactFaxNo.setValue("");
		logisticsContactEmail.setValue(" ");
		isActive.setChecked(false);

	}

}

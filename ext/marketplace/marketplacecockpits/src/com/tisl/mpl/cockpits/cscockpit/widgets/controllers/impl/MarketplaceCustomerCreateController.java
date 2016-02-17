package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.CommunicationPreferencesModel;
import com.tisl.mpl.marketplacecommerceservices.service.ExtDefaultCustomerService;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCustomerCreateController;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.session.SessionService;

public class MarketplaceCustomerCreateController extends
DefaultCustomerCreateController {

	private static final Logger LOG = Logger
			.getLogger(MarketplaceCustomerCreateController.class);
	
	private static final String EMPTY_SPACE = " ";

	@Autowired
	private KeyGenerator userUniqueIdGenerator;
	
	@Autowired
	private ExtDefaultCustomerService extDefaultCustomerService;
	
	@Autowired
	private SessionService sessionService;

	@Override
	protected CustomerModel createNewCustomer(
			ObjectValueContainer customerObjectValueContainer) {

		final CustomerModel customerModel = getModelService().create(
				CustomerModel.class);
		final Set<ObjectValueContainer.ObjectValueHolder> allValues = customerObjectValueContainer
				.getAllValues();
		for (final ObjectValueContainer.ObjectValueHolder objectValueHolder : allValues) {
			final PropertyDescriptor propertyDescriptor = objectValueHolder
					.getPropertyDescriptor();
			Object currentValue = objectValueHolder.getCurrentValue();

			if (propertyDescriptor instanceof ItemAttributePropertyDescriptor
					&& currentValue != null) {
				final ItemAttributePropertyDescriptor attributePropertyDescriptor = (ItemAttributePropertyDescriptor) propertyDescriptor;
				if (attributePropertyDescriptor.isSingleAttribute()) {

					try {
						currentValue = getModelService().toPersistenceLayer(
								TypeTools.container2Item(null, currentValue));
						if (propertyDescriptor.isLocalized()) {
							setModelLocalizedValue(customerModel,
									attributePropertyDescriptor
									.getAttributeQualifier(),
									objectValueHolder.getLanguageIso(),
									currentValue);
						} else {
							setModelSingleValue(customerModel,
									attributePropertyDescriptor
									.getAttributeQualifier(),
									currentValue);
						}
					} catch (final AttributeNotSupportedException ex) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Attribute: " + ex.getQualifier()
									+ " is not supported by servicelayer! ");
						}
					}
				}

			}
		}

		return customerModel;
	}

	@Override
	public TypedObject createNewCustomer(
			final ObjectValueContainer customerObjectValueContainer,
			final String customerTypeCode) throws DuplicateUidException,
			ValueHandlerException {
		if (customerObjectValueContainer != null && customerTypeCode != null) {
			final CustomerModel customerModel = createNewCustomer(customerObjectValueContainer);
			setCustomerName(customerModel);
			setCustomerCommunicationPreferences(customerModel);
			customerModel.setUid(userUniqueIdGenerator.generate().toString());
			if (customerModel != null) {
				try {
					//Added to register a customer with a temporary password which is equivalent to the customer uid
					extDefaultCustomerService.registerCockpit(customerModel, customerModel.getUid());
					return getCockpitTypeService().wrapItem(customerModel);
				} catch (final ModelSavingException e) {
					String message = e.getMessage();
					if (e.getCause() instanceof InterceptorException) {
						final String interceptorMessage = e.getCause()
								.getMessage();
						message = interceptorMessage != null
								&& interceptorMessage
								.contains(INTERCEPTOR_EXCEPTION_MESSAGE_PREFIX_END) ? StringUtils
										.substringAfter(interceptorMessage,
												INTERCEPTOR_EXCEPTION_MESSAGE_PREFIX_END)
												: interceptorMessage;
					}

					throw new ValueHandlerException(message, e);
				} catch (final IllegalArgumentException e) {
					throw new ValueHandlerException(e.getMessage(), e);
				}
			}
		}

		return null;
	}

	/**
	 * It sets customer communication preferences(email, sms and push)
	 * @param customerModel
	 */
	private void setCustomerCommunicationPreferences(CustomerModel customerModel) {
		// TODO Auto-generated method stub
		if(sessionService.getAttribute("commPrefList")!=null){
			List<CommunicationPreferencesModel> commPrefList=sessionService.getAttribute("commPrefList");
		customerModel.setCommPreferences(commPrefList);
		}
	}

	/**
	 * It appends customer's first name and last name
	 * @param customerModel
	 */
	private void setCustomerName(final CustomerModel customerModel) {

		StringBuilder name = new StringBuilder();

		if (((StringUtils.isEmpty(customerModel.getFirstName())) || (StringUtils.isBlank(customerModel.getFirstName().trim())))
				&& ((StringUtils.isEmpty(customerModel.getLastName())) || (StringUtils.isBlank(customerModel.getLastName().trim())))) {
			customerModel.setName(customerModel.getOriginalUid());

			

			} else {
				
				if (StringUtils.isNotEmpty(customerModel.getFirstName())) {
					name.append(customerModel.getFirstName());
					
					name.append(EMPTY_SPACE);
				}
				
				if (StringUtils.isNotEmpty(customerModel.getLastName())) {
					name.append(customerModel.getLastName());
					
				}

				customerModel.setName(name.toString());
			}

		}

	
	
	}



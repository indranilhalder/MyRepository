/**
 *
 */
package com.tisl.mpl.facades.account.register.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.SendInvoiceProcessModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.facades.product.data.SendInvoiceData;
import com.tisl.mpl.marketplacecommerceservices.service.ExtDefaultCustomerService;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.service.MplCustomerWebService;
import com.tisl.mpl.util.CatalogUtils;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */



public class RegisterCustomerFacadeImpl extends DefaultCustomerFacade implements RegisterCustomerFacade
{
	private static final Logger LOG = Logger.getLogger(RegisterCustomerFacadeImpl.class);
	@Autowired
	private KeyGenerator userUniqueIdGenerator;
	@Autowired
	@Qualifier(MplConstants.EXTENDED_USER_SERVICE)
	private ExtendedUserService extUserService;
	@Autowired
	private ExtDefaultCustomerService extDefaultCustomerService;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplCustomerWebService mplCustomerWebService;

	private OrderModelService orderModelService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	ConfigurationService configurationService;



	@Resource(name = "defaultEmailService")
	private EmailService emailService;

	@Autowired
	private CatalogUtils catalogUtils;


	/**
	 * @return the userUniqueIdGenerator
	 */
	public KeyGenerator getUserUniqueIdGenerator()
	{
		return userUniqueIdGenerator;
	}


	/**
	 * @param userUniqueIdGenerator
	 *           the userUniqueIdGenerator to set
	 */
	public void setUserUniqueIdGenerator(final KeyGenerator userUniqueIdGenerator)
	{
		this.userUniqueIdGenerator = userUniqueIdGenerator;
	}


	/**
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}


	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
	}


	/**
	 * @return the extDefaultCustomerService
	 */
	public ExtDefaultCustomerService getExtDefaultCustomerService()
	{
		return extDefaultCustomerService;
	}


	/**
	 * @param extDefaultCustomerService
	 *           the extDefaultCustomerService to set
	 */
	public void setExtDefaultCustomerService(final ExtDefaultCustomerService extDefaultCustomerService)
	{
		this.extDefaultCustomerService = extDefaultCustomerService;
	}


	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}


	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}


	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}


	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}


	/**
	 * @return the commonI18NService
	 */
	@Override
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}


	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	@Override
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}


	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the mplCustomerWebService
	 */
	public MplCustomerWebService getMplCustomerWebService()
	{
		return mplCustomerWebService;
	}


	/**
	 * @param mplCustomerWebService
	 *           the mplCustomerWebService to set
	 */
	public void setMplCustomerWebService(final MplCustomerWebService mplCustomerWebService)
	{
		this.mplCustomerWebService = mplCustomerWebService;
	}


	/**
	 * @description this is used to register a new customer
	 * @param registerData
	 * @throws DuplicateUidException
	 */
	@Override
	public void register(final ExtRegisterData registerData, final int platformNumber) throws DuplicateUidException //TPR-6272 parameter platformNumber added
	{
		try
		{
			validateParameterNotNullStandardMessage(MplConstants.REGISTER_DATA, registerData);
			Assert.hasText(registerData.getLogin(), MplConstants.ASSERT_LOGIN_MSG);

			if (extUserService.isEmailUniqueForSite(registerData.getLogin()))
			{
				final CustomerModel newCustomer = getModelService().create(CustomerModel.class);
				newCustomer.setName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
				newCustomer.setFirstName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
				newCustomer.setLastName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
				newCustomer.setType(CustomerType.REGISTERED);
				newCustomer.setIscheckedMyRewards(Boolean.valueOf(registerData.isCheckTataRewards()));
				setUidForRegister(registerData, newCustomer);
				newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
				newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());

				final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
				final String site = currentBaseSite.getUid();

				if (MarketplaceFacadesConstants.LuxuryPrefix.equals(site))
				{
					newCustomer.setFirstName(registerData.getFirstName());
					newCustomer.setLastName(registerData.getLastName());
					if (registerData.getGender() != null && !"".equals(registerData.getGender())
							&& MplConstants.MALE.equals(registerData.getGender()))
					{
						newCustomer.setGender(Gender.MALE);
					}
					if (registerData.getGender() != null && !"".equals(registerData.getGender())
							&& MplConstants.FEMALE.equals(registerData.getGender()))
					{
						newCustomer.setGender(Gender.FEMALE);
					}
					newCustomer.setIsLuxuryCustomer(Boolean.TRUE);
					newCustomer.setMobileNumber(registerData.getMobilenumber());
				}

				extDefaultCustomerService.registerUser(newCustomer, registerData.getPassword(), registerData.getAffiliateId(),
						platformNumber);
				/*
				 * mplCustomerWebService.customerModeltoWsData(newCustomer,
				 * MarketplacecommerceservicesConstants.NEW_CUSTOMER_CREATE_FLAG, false);
				 */
				extUserService.addToRegisteredGroup(newCustomer);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0001);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description this method is used to check uniqueness og email id
	 * @param data
	 * @return boolean
	 */
	@Override
	public boolean checkUniquenessOfEmail(final ExtRegisterData data)
	{
		boolean flag = false;
		flag = extUserService.isEmailUniqueForSite(data.getLogin());
		//		if (!flag)
		//		{
		//			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0001);
		//		}
		return flag;

	}

	/**
	 * @description this method is used to check uniqueness of MobileNumber For Wallet
	 * @param data
	 * @return boolean
	 */
	@Override
	public boolean checkUniquenessOfMobileForWallet(final String mobileNumber)
	{
		boolean flag = false;
		flag = extUserService.isMobileUniqueForWallet(mobileNumber);
		//		if (!flag)
		//		{
		//			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0001);
		//		}
		return flag;

	}

	/**
	 * @description this is called to set uid for registration process
	 * @param registerData
	 * @param customer
	 */
	@Override
	protected void setUidForRegister(final RegisterData registerData, final CustomerModel customer)
	{
		try
		{
			customer.setUid(userUniqueIdGenerator.generate().toString());
			customer.setOriginalUid(registerData.getLogin());
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this method is used to register with social media
	 * @param registerData
	 * @return ExtRegisterData
	 */
	@Override
	public ExtRegisterData registerSocial(final ExtRegisterData registerData, final boolean isMobile, final int platformNumber) //SDI-639
	{
		try
		{
			ExtRegisterData data = new ExtRegisterData();
			validateParameterNotNullStandardMessage(MplConstants.REGISTER_DATA, registerData);
			Assert.hasText(registerData.getLogin(), MplConstants.ASSERT_LOGIN_MSG);
			if (extUserService.isEmailUniqueForSite(registerData.getLogin()))
			{
				final CustomerModel newCustomer = getModelService().create(CustomerModel.class);
				//				newCustomer.setName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
				//				newCustomer.setFirstName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
				//				newCustomer.setLastName(MarketplacecommerceservicesConstants.SINGLE_SPACE);


				newCustomer.setName(registerData.getFirstName());
				newCustomer.setFirstName(registerData.getFirstName());
				newCustomer.setLastName(registerData.getLastName());

				//Logic for Luxury adding firstname, lastname and Gender starts
				final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
				final String site = currentBaseSite.getUid();

				if (MarketplaceFacadesConstants.LuxuryPrefix.equals(site))
				{
					newCustomer.setFirstName(registerData.getFirstName());
					newCustomer.setLastName(registerData.getLastName());
					newCustomer.setIsLuxuryCustomer(Boolean.TRUE);
					if (registerData.getSocialMediaType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FACEBOOK))
					{
						if (registerData.getGender() != null && !"".equals(registerData.getGender())
								&& MplConstants.FBMALE.equalsIgnoreCase(registerData.getGender()))
						{
							newCustomer.setGender(Gender.MALE);
						}
						if (registerData.getGender() != null && !"".equals(registerData.getGender())
								&& MplConstants.FBFEMALE.equalsIgnoreCase(registerData.getGender()))
						{
							newCustomer.setGender(Gender.FEMALE);
						}
					}

					if (registerData.getSocialMediaType().equalsIgnoreCase(MarketplacecommerceservicesConstants.GOOGLE))
					{
						if (registerData.getGender() != null && !"".equals(registerData.getGender())
								&& MplConstants.FBMALE.equalsIgnoreCase(registerData.getGender()))
						{
							newCustomer.setGender(Gender.MALE);
						}
						if (registerData.getGender() != null && !"".equals(registerData.getGender())
								&& MplConstants.FBFEMALE.equalsIgnoreCase(registerData.getGender()))
						{
							newCustomer.setGender(Gender.FEMALE);
						}

					}

				}
				//Logic for Luxury adding firstname, lastname and Gender ends

				if (StringUtils.isNotEmpty(registerData.getSocialMediaType()))
				{
					if (registerData.getSocialMediaType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FACEBOOK))
					{
						newCustomer.setType(CustomerType.FACEBOOK_LOGIN);
					}
					if (registerData.getSocialMediaType().equalsIgnoreCase(MarketplacecommerceservicesConstants.GOOGLE))
					{
						newCustomer.setType(CustomerType.GOOGLE_LOGIN);
					}
				}
				setUidForRegister(registerData, newCustomer);
				newCustomer.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
				newCustomer.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
				//TPR-1372
				newCustomer.setIscheckedMyRewards(Boolean.valueOf(registerData.isCheckTataRewards()));

				//Register customer social
				newCustomer.setCustomerRegisteredBySocialMedia(Boolean.TRUE);
				data = extDefaultCustomerService.registerUserForSocialSignup(newCustomer, platformNumber); //SDI-639
				extUserService.addToRegisteredGroup(newCustomer);

				/*
				 * if (!isMobile) {
				 */
				/*
				 * Closing checks at gigya end for new Implementation Start
				 *
				 * try { LOG.debug("Method  registerSocial,Gigys's UID " + newCustomer.getUid());
				 * LOG.debug("Method  registerSocial SITE UID " + registerData.getUid());
				 * LOG.debug("Method  registerSocial FIRST_NAME " + registerData.getFirstName());
				 * LOG.debug("Method  registerSocial LAST_NAME " + registerData.getLastName()); final String gigyaMethod =
				 * configurationService.getConfiguration().getString(
				 * MarketplacecclientservicesConstants.METHOD_NOTIFY_REGISTRATION); LOG.debug("GIGYA METHOD" + gigyaMethod);
				 * //gigya code change for removing duplicate UID Start final int errorcode =
				 * gigyaservice.checkGigyaUID(newCustomer.getUid()); if (errorcode == 403005) {
				 * gigyaservice.notifyGigya(newCustomer.getUid(), registerData.getUid(), registerData.getFirstName(),
				 * registerData.getLastName(), registerData.getLogin(), gigyaMethod); } else {
				 * LOG.debug("UID already existing in Gigya for this  New Customer :error code " + errorcode);
				 * LOG.debug("UID already existing in Gigya for this  New Customer :new customer uid " +
				 * newCustomer.getUid());
				 * LOG.debug("UID already existing in Gigya for this  New Customer :new customer email " +
				 * newCustomer.getOriginalUid());
				 * LOG.debug("UID already existing in Gigya for this  New Customer :existing uid in Gigya" +
				 * registerData.getUid());
				 *
				 * LOG.debug("UID already existing in Gigya for this  New Customer :existing uid in Gigya" +
				 * registerData.getLogin()); } //gigya code change for removing duplicate UID end
				 *
				 * } catch (final Exception e) { LOG.error("error notifing gigya of new registration", e); }
				 *
				 * //} Closing checks at gigya end for new Implementation Stop
				 */
				return data;
			}
			else
			{
				final CustomerModel customerModel = (CustomerModel) extUserService.getUserForUID(registerData.getLogin());
				LOG.debug("customer model type before update " + customerModel.getType());
				//tpr-668 update customer type
				if (StringUtils.isNotEmpty(registerData.getSocialMediaType()))
				{
					if (registerData.getSocialMediaType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FACEBOOK))
					{
						customerModel.setType(CustomerType.FACEBOOK_LOGIN);
						LOG.debug("customer model type " + customerModel.getType());
					}
					if (registerData.getSocialMediaType().equalsIgnoreCase(MarketplacecommerceservicesConstants.GOOGLE))
					{
						customerModel.setType(CustomerType.GOOGLE_LOGIN);
						LOG.debug("customer model type " + customerModel.getType());
					}

					//tpr-668 update customer type
				}

				getModelService().save(customerModel);
				LOG.debug("Method  registerSocial return user ,SITE UID " + customerModel.getUid());
				LOG.debug("Method  registerSocial else FIRST_NAME " + registerData.getFirstName());
				LOG.debug("Method  registerSocial else LAST_NAME " + registerData.getLastName());
				LOG.debug("Method  registerSocial else GENDER " + registerData.getGender());
				LOG.debug("customer model email is   " + customerModel.getOriginalUid());

				if (registerData.getFirstName() != null)
				{
					registerData.setFirstName(registerData.getFirstName());
				}

				if (registerData.getLastName() != null)
				{
					registerData.setLastName(registerData.getLastName());
				}

				final String password = "TATACLiQSocialLogin";
				registerData.setPassword(password);
				registerData.setSocialLogin(true);
				LOG.debug(MplConstants.USER_ALREADY_REGISTERED + " via site login");
				return registerData;
				/*
				 * 
				 * Closing checks at gigya end for new Implementation Start
				 * 
				 * // final String gigyaMethod = configurationService.getConfiguration().getString( ///closing gigya methods
				 * // MarketplacecclientservicesConstants.METHOD_NOTIFY_REGISTRATION); // LOG.debug("GIGYA METHOD" +
				 * gigyaMethod); // // // //changes start for gigya duplicate uid check start // //TISUAT-5868 // if
				 * (!(customerModel.getUid().toString()).equals(registerData.getUid().toString())) // { // // final int
				 * errorcode = gigyaservice.checkGigyaUID(customerModel.getUid().toString()); // if (errorcode == 403005) //
				 * { // LOG.debug("Customer model UID is different as of Gigya UID:Error code is " + errorcode); //
				 * gigyaservice.notifyGigya(customerModel.getUid(), registerData.getUid(), registerData.getFirstName(), //
				 * registerData.getLastName(), registerData.getLogin(), gigyaMethod); // // } // } // else // {/* gigya uid
				 * matches with hybris
				 */
				//					LOG.debug("Customer model UID is same as of Gigya UID:UID is   " + registerData.getUid());
				//				}
				//				//changes start for gigya duplicate uid  check end




			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * Change uid.
	 *
	 * @param newUid
	 *           the new uid
	 * @throws DuplicateUidException
	 *            the duplicate uid exception
	 * @throws PasswordMismatchException
	 *            the password mismatch exception
	 */
	public void changeUid(final String newUid) throws DuplicateUidException, PasswordMismatchException
	{
		try
		{
			extDefaultCustomerService.changeUid(newUid);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public void changeUid(final String newUid, final String currentPassword) throws DuplicateUidException,
			PasswordMismatchException
	{
		try
		{
			extDefaultCustomerService.changeUid(newUid, currentPassword);
		}
		catch (final de.hybris.platform.commerceservices.customer.PasswordMismatchException pse)
		{
			throw new EtailNonBusinessExceptions(pse, MarketplacecommerceservicesConstants.B0009);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/**
	 * @description this method is used to fetch password detail for uid
	 * @param uid
	 */
	@Override
	public void forgottenPassword(final String uid)
	{
		try
		{
			Assert.hasText(uid, MplConstants.FIELD_UID_CANNOT_BE_EMPTY);
			final CustomerModel customerModel = extUserService.getUserForUID(uid.toLowerCase(), CustomerModel.class);
			getCustomerAccountService().forgottenPassword(customerModel);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * @ This method is used to send invoice to the customer
	 */

	@Override
	public void sendInvoice(final SendInvoiceData sendInvoiceData, final CustomerModel customer, final OrderModel orderModel)
	{
		try
		{
			final SendInvoiceProcessModel sendInvoiceProcessModel = (SendInvoiceProcessModel) businessProcessService.createProcess(
					"sendInvoice-" + sendInvoiceData.getCustomerEmail() + "-" + System.currentTimeMillis(), "sendInvoiceEmailProcess");
			//commented for carProject implementation
			//final OrderModel orderModel = getOrderModelService().getOrder(sendInvoiceData.getOrdercode());
			if (null != customer)
			{
				sendInvoiceProcessModel.setCustomer(customer);
				//final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("mplContentCatalog", "Online");
				final CatalogVersionModel catalogVersion = catalogUtils.getSessionCatalogVersionForContent();
				catalogVersionService.setSessionCatalogVersions(Collections.singleton(catalogVersion));
			}
			else
			{
				sendInvoiceProcessModel.setCustomer(getCurrentSessionCustomer());
			}
			sendInvoiceProcessModel.setSite(orderModel.getSite());
			sendInvoiceProcessModel.setCustomerEmail(sendInvoiceData.getCustomerEmail());

			//Added
			if (!StringUtils.isEmpty(sendInvoiceData.getInvoiceUrl()) && !StringUtils.isEmpty(sendInvoiceData.getTransactionId()))
			{
				//INC144318174- duplicate media fix

				/*
				 * if (!StringUtils.isEmpty(createInvoiceEmailAttachment(sendInvoiceData.getInvoiceUrl(),
				 * sendInvoiceData.getTransactionId()))) {
				 */
				sendInvoiceProcessModel.setInvoiceUrl(createInvoiceEmailAttachment(sendInvoiceData.getInvoiceUrl(),
						sendInvoiceData.getTransactionId()));
				//}
			}
			//End
			sendInvoiceProcessModel.setOrderCode(sendInvoiceData.getOrdercode());
			sendInvoiceProcessModel.setTransactionId(sendInvoiceData.getTransactionId());
			//Added for TPR-6286
			sendInvoiceProcessModel.setAbstractOrderEntryList(orderModel.getEntries());
			sendInvoiceProcessModel.setChildOrderList(orderModel.getChildOrders());
			sendInvoiceProcessModel.setDeliveryAddress(orderModel.getDeliveryAddress());
			sendInvoiceProcessModel.setOrderPlacedDate(orderModel.getCreationtime());

			sendInvoiceProcessModel.setStore(orderModel.getStore());

			sendInvoiceProcessModel.setCurrency(orderModel.getCurrency());
			sendInvoiceProcessModel.setLanguage(orderModel.getLanguage());
			modelService.save(sendInvoiceProcessModel);
			businessProcessService.startProcess(sendInvoiceProcessModel);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @param invoiceUrl
	 * @param transactionId
	 */
	private String createInvoiceEmailAttachment(final String invoiceUrl, final String transactionId)
	{
		final File invoiceFile = new File(invoiceUrl);
		FileInputStream input = null;
		EmailAttachmentModel emailAttachment = null;
		if (invoiceFile.exists())
		{
			String invoiceFileName = null;
			final String preInvoiceFileName = invoiceFile.getName();
			if (!preInvoiceFileName.isEmpty())
			{
				final int index = preInvoiceFileName.lastIndexOf('.');
				if (index > 0)
				{
					invoiceFileName = preInvoiceFileName.substring(0, index) + "_" + transactionId + "_"
							+ new Timestamp(System.currentTimeMillis()) + "." + preInvoiceFileName.substring(index + 1);
				}
			}
			try
			{
				input = new FileInputStream(invoiceFile);
				emailAttachment = emailService.createEmailAttachment(new DataInputStream(input), invoiceFileName,
						"application/octet-stream");

				LOG.info("******Invoice Email Attachment Created Successfully!!!******" + emailAttachment.getCode());

				//sendInvoiceData.setInvoiceUrl(emailAttachment.getCode());
			}
			catch (final FileNotFoundException e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(
						new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0008, e), null);
			}
			catch (final Exception ex)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(ex));

			}

		}
		return emailAttachment != null ? emailAttachment.getCode() : null;

	}


	/**
	 * @return the orderModelService
	 */
	public OrderModelService getOrderModelService()
	{
		return orderModelService;
	}


	/**
	 * @param orderModelService
	 *           the orderModelService to set
	 */
	@Required
	public void setOrderModelService(final OrderModelService orderModelService)
	{
		this.orderModelService = orderModelService;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.account.register.RegisterCustomerFacade#registerWalletMobileNumber(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void registerWalletMobileNumber(final String firstName, final String lastName, final String mobileNumber)
	{
		extDefaultCustomerService.registerWalletMobileNumber(firstName, lastName, mobileNumber);
	}

}

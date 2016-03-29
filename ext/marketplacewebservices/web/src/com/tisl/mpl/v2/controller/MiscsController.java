/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.acceleratorcms.model.components.SearchBoxComponentModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.order.CardTypeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.LanguageListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.CountryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.TitleListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserSignUpWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.commercewebservicescommons.mapping.impl.FieldSetBuilderContext;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.wishlist2.Wishlist2Service;
import com.tisl.mpl.service.MplSlaveMasterService;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.constants.YcommercewebservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.enums.FeedbackCategory;
import com.tisl.mpl.core.model.MplEnhancedSearchBoxComponentModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.netbank.MplNetBankingFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.account.register.ForgetPasswordFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.product.data.StateData;

import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtendedUserServiceImpl;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCommerceCartServiceImpl;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.order.data.CardTypeDataList;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.populator.HttpRequestCustomerUpdatePopulator;
import com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade;
import com.tisl.mpl.service.HomescreenService;
import com.tisl.mpl.service.MplCustomCategoryService;
import com.tisl.mpl.service.MplRestrictionServiceImpl;
import com.tisl.mpl.service.MplSellerMasterService;
import com.tisl.mpl.service.MplValidateAgainstXSDService;
import com.tisl.mpl.service.MplVersionService;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storesession.data.CurrencyDataList;
import com.tisl.mpl.storesession.data.LanguageDataList;
import com.tisl.mpl.user.data.CountryDataList;
import com.tisl.mpl.user.data.TitleDataList;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.utility.SearchSuggestUtilityMethods;
import com.tisl.mpl.wsdto.AboutUsResultWsData;
import com.tisl.mpl.wsdto.AutoCompleteResultWsData;
import com.tisl.mpl.wsdto.BannerWsDTO;
import com.tisl.mpl.wsdto.CategorySNSWsData;
import com.tisl.mpl.wsdto.CorporateAddressWsDTO;
import com.tisl.mpl.wsdto.HelpAndServicestWsData;
import com.tisl.mpl.wsdto.HomescreenListData;
import com.tisl.mpl.wsdto.ListPinCodeServiceData;
import com.tisl.mpl.wsdto.MplAutoCompleteResultWsData;
import com.tisl.mpl.wsdto.PaymentInfoWsDTO;
import com.tisl.mpl.wsdto.PinWsDto;
import com.tisl.mpl.wsdto.RestrictionPins;
import com.tisl.mpl.wsdto.SearchDropdownWsDTO;
import com.tisl.mpl.wsdto.SellerMasterWsDTO;
import com.tisl.mpl.wsdto.StateListWsDto;
import com.tisl.mpl.wsdto.StateWsDto;
import com.tisl.mpl.wsdto.UserResultWsDto;
import com.tisl.mpl.wsdto.VersionListResponseData;
import com.tisl.mpl.wsdto.VersionListResponseWsDTO;
import com.tisl.mpl.wsdto.WebSerResponseWsDTO;
import com.tisl.mpl.wsdto.WthhldTAXWsDTO;

import com.tisl.mpl.wsdto.SellerSlaveDTO;
import com.tisl.mpl.wsdto.SlaveInfoDTO;

/**
 * @author TCS
 */
@Controller
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
public class MiscsController extends BaseController
{


	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;
	@Autowired
	private ConfigurationService configurationService;
	@Resource(name = "httpRequestCustomerUpdatePopulator")
	private HttpRequestCustomerUpdatePopulator httpRequestCustomerUpdatePopulator;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource
	private ModelService modelService;


	@Autowired
	private ForgetPasswordFacade forgetPasswordFacade;

	@Autowired
	private ExtendedUserServiceImpl userexService;
	@Autowired
	private WishlistFacade wishlistFacade;

	@Autowired
	private MplSellerMasterService mplSellerInformationService;

	@Autowired
	private UserService userService;

	@Autowired
	private MplCustomerProfileService mplCustomerProfileService;

	@Autowired
	private Wishlist2Service wishlistService;

	@Resource(name = "passwordStrengthValidator")
	private Validator passwordStrengthValidator;

	@Autowired
	private MplCartFacade mplCartFacade;
	@Autowired
	private ExtendedUserService extUserService;

	@Autowired
	private CustomerAccountService customerAccountService;
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Resource(name = "homescreenservice")
	private HomescreenService homescreenservice;
	@Resource(name = "fieldSetBuilder")
	private FieldSetBuilder fieldSetBuilder;
	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@Autowired
	private MplCommerceCartServiceImpl mplCommerceCartService;

	@Autowired
	private MplRestrictionServiceImpl restrictionserviceimpl;

	@Autowired
	private MplValidateAgainstXSDService mplValidateAgainstXSDService;

	@Autowired
	private MplSellerMasterService mplSellerMasterService;

	@Resource(name = "mplCustomCategoryService")
	private MplCustomCategoryService mplCustomCategoryService;
	@Autowired
	private UpdateFeedbackFacade updateFeedbackFacade;
	@Autowired
	private MplNetBankingFacade mplNetBankingFacade;

	@Autowired
	private MplSlaveMasterService mplSlaveMasterService;
	
	@Resource(name="pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the forgetPasswordFacade
	 */
	public ForgetPasswordFacade getForgetPasswordFacade()
	{
		return forgetPasswordFacade;
	}

	/**
	 * @param forgetPasswordFacade
	 *           the forgetPasswordFacade to set
	 */
	public void setForgetPasswordFacade(final ForgetPasswordFacade forgetPasswordFacade)
	{
		this.forgetPasswordFacade = forgetPasswordFacade;
	}

	/**
	 * @return the userexService
	 */
	public ExtendedUserServiceImpl getUserexService()
	{
		return userexService;
	}

	/**
	 * @param userexService
	 *           the userexService to set
	 */
	public void setUserexService(final ExtendedUserServiceImpl userexService)
	{
		this.userexService = userexService;
	}

	/**
	 * @return the wishlistFacade
	 */
	public WishlistFacade getWishlistFacade()
	{
		return wishlistFacade;
	}

	/**
	 * @param wishlistFacade
	 *           the wishlistFacade to set
	 */
	public void setWishlistFacade(final WishlistFacade wishlistFacade)
	{
		this.wishlistFacade = wishlistFacade;
	}

	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerMasterService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}

	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerMasterService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}

	/**
	 * @return the wishlistService
	 */
	public Wishlist2Service getWishlistService()
	{
		return wishlistService;
	}

	/**
	 * @param wishlistService
	 *           the wishlistService to set
	 */
	public void setWishlistService(final Wishlist2Service wishlistService)
	{
		this.wishlistService = wishlistService;
	}

	/**
	 * @return the mplCartFacade
	 */
	public MplCartFacade getMplCartFacade()
	{
		return mplCartFacade;
	}

	/**
	 * @param mplCartFacade
	 *           the mplCartFacade to set
	 */
	public void setMplCartFacade(final MplCartFacade mplCartFacade)
	{
		this.mplCartFacade = mplCartFacade;
	}

	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartServiceImpl getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartServiceImpl mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}

	/**
	 * @return the mplSellerMasterService
	 */
	public MplSellerMasterService getMplSellerMasterService()
	{
		return mplSellerMasterService;
	}

	/**
	 * @param mplSellerMasterService
	 *           the mplSellerMasterService to set
	 */
	public void setMplSellerMasterService(final MplSellerMasterService mplSellerMasterService)
	{
		this.mplSellerMasterService = mplSellerMasterService;
	}

	/**
	 * @return the addressReversePopulator
	 */
	public Populator<AddressData, AddressModel> getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	/**
	 * @param addressReversePopulator
	 *           the addressReversePopulator to set
	 */
	public void setAddressReversePopulator(final Populator<AddressData, AddressModel> addressReversePopulator)
	{
		this.addressReversePopulator = addressReversePopulator;
	}

	/**
	 * @return the mplCategoryService
	 */
	public MplCategoryService getMplCategoryService()
	{
		return mplCategoryService;
	}

	/**
	 * @param mplCategoryService
	 *           the mplCategoryService to set
	 */
	public void setMplCategoryService(final MplCategoryService mplCategoryService)
	{
		this.mplCategoryService = mplCategoryService;
	}

	/**
	 * @return the mplCustomCategoryService
	 */
	public MplCustomCategoryService getMplCustomCategoryService()
	{
		return mplCustomCategoryService;
	}

	/**
	 * @param mplCustomCategoryService
	 *           the mplCustomCategoryService to set
	 */
	public void setMplCustomCategoryService(final MplCustomCategoryService mplCustomCategoryService)
	{
		this.mplCustomCategoryService = mplCustomCategoryService;
	}

	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;

	public MplPaymentFacade getMplPaymentFacade()
	{
		return mplPaymentFacade;
	}

	/**
	 * @param mplPaymentFacade
	 *           the mplPaymentFacade to set
	 */
	public void setMplPaymentFacade(final MplPaymentFacade mplPaymentFacade)
	{
		this.mplPaymentFacade = mplPaymentFacade;
	}

	@Autowired
	private Populator<AddressData, AddressModel> addressReversePopulator;
	@Autowired
	private MplVersionService mplVersionService;

	private static final Logger LOG = Logger.getLogger(MiscsController.class);

	//Priority

	//Added for SNS
	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;

	@Resource(name = "mplCategoryServiceImpl")
	private MplCategoryService mplCategoryService;

	@Autowired
	private SearchSuggestUtilityMethods searchSuggestUtilityMethods;

	//End of Declaration for SNS

	@Resource(name = "pinCodeFacade")
	private PinCodeServiceAvilabilityFacade pinCodeFacade;

	@Autowired
	private PriceDataFactory priceDataFactory;
	
	@Resource(name = "pincodeService")
	private PincodeService pincodeService;

	/*
	 * @Autowired private MplCheckoutFacade mplCheckoutFacade;
	 */


	/**
	 * Lists all available languages (all languages used for a particular store). If the list of languages for a base
	 * store is empty, a list of all languages available in the system will be returned.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of languages
	 */
	@RequestMapping(value = "/{baseSiteId}/languages", method = RequestMethod.GET)
	@Cacheable(value = MarketplacewebservicesConstants.MISCSCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getLanguages',#fields)")
	@ResponseBody
	public LanguageListWsDTO getLanguages(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final LanguageDataList dataList = new LanguageDataList();
		dataList.setLanguages(storeSessionFacade.getAllLanguages());
		return dataMapper.map(dataList, LanguageListWsDTO.class, fields);
	}

	/**
	 * Lists all available currencies (all usable currencies for the current store).If the list of currencies for stores
	 * is empty, a list of all currencies available in the system is returned.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of currencies
	 */
	@RequestMapping(value = "/{baseSiteId}/currencies", method = RequestMethod.GET)
	@Cacheable(value = MarketplacewebservicesConstants.MISCSCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCurrencies',#fields)")
	@ResponseBody
	public CurrencyListWsDTO getCurrencies(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CurrencyDataList dataList = new CurrencyDataList();
		dataList.setCurrencies(storeSessionFacade.getAllCurrencies());
		return dataMapper.map(dataList, CurrencyListWsDTO.class, fields);
	}

	/**
	 * Lists all supported delivery countries for the current store. The list is sorted alphabetically.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List supported delivery countries.
	 */
	//For Junit
	@RequestMapping(value = "/{baseSiteId}/deliverycountries", method = RequestMethod.GET)
	@Cacheable(value = MarketplacewebservicesConstants.MISCSCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getDeliveryCountries',#fields)")
	@ResponseBody
	public CountryListWsDTO getDeliveryCountries(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CountryDataList dataList = new CountryDataList();

		final List<CountryData> countryData = mplNetBankingFacade.getCountries();

		dataList.setCountries(countryData);

		return dataMapper.map(dataList, CountryListWsDTO.class, fields);
	}

	/**
	 * Lists all localized titles.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of titles
	 */
	@RequestMapping(value = "/{baseSiteId}/titles", method = RequestMethod.GET)
	@Cacheable(value = MarketplacewebservicesConstants.MISCSCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getTitles',#fields)")
	@ResponseBody
	public TitleListWsDTO getTitles(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final TitleDataList dataList = new TitleDataList();
		dataList.setTitles(userFacade.getTitles());
		return dataMapper.map(dataList, TitleListWsDTO.class, fields);
	}

	/**
	 * Lists supported payment card types.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of card types
	 */
	@RequestMapping(value = "/{baseSiteId}/cardtypes", method = RequestMethod.GET)
	@Cacheable(value = MarketplacewebservicesConstants.MISCSCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCardTypes',#fields)")
	@ResponseBody
	public CardTypeListWsDTO getCardTypes(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CardTypeDataList dataList = new CardTypeDataList();
		dataList.setCardTypes(checkoutFacade.getSupportedCardTypes());
		return dataMapper.map(dataList, CardTypeListWsDTO.class, fields);
	}

	@RequestMapping(value = "/{baseSiteId}/miscs/ordercreatexmltesting", method = RequestMethod.POST)
	@ResponseBody
	public void ordercreateXMLTesting(final InputStream fileInputStream) throws IOException
	{

		try
		{
			OutputStream outpuStream = new FileOutputStream(new File("D:\\Server_orderfull.xml"));
			int read = 0;
			final byte[] bytes = new byte[1024];
			outpuStream = new FileOutputStream(new File("D:\\Server_orderfull.xml"));
			while ((read = fileInputStream.read(bytes)) != -1)
			{

				outpuStream.write(bytes, 0, read);

			}
			outpuStream.flush();
			outpuStream.close();
		}
		catch (final IOException e)
		{
			//e.printStackTrace();
			LOG.error("IOException : ", e);
		}
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{baseSiteId}/updateprofile", method = RequestMethod.POST)
	//	@ResponseStatus(HttpStatus.OK)
	public void updateUser(final HttpServletRequest request) throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateUser: userId=" + customer.getUid());
		}
		httpRequestCustomerUpdatePopulator.populate(request, customer);
		customerFacade.updateFullProfile(customer);
	}

	// method to test merchant master data will be removed after
	@RequestMapping(value = "/{baseSiteId}/miscs/merchantmaster", method = RequestMethod.POST, consumes = "application/xml")
	@ResponseBody
	public void postOnlyXML1(final InputStream fileInputStream)
	{

		try
		{
			final OutputStream outpuStream = new FileOutputStream(new File("D:\\Server_merchantmaster.xml"));
			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = fileInputStream.read(bytes)) != -1)
			{

				outpuStream.write(bytes, 0, read);

			}
			outpuStream.flush();
			outpuStream.close();
		}
		catch (final IOException e)
		{
			//e.printStackTrace();
			LOG.error("postOnlyXML1 : ", e);
		}


	}

	/**
	 * This is the rest call for SlaveMaster.
	 * @author TECHOUTS
	 * @param slaves
	 * @param request
	 * @return 
	 */
	@RequestMapping(value = "/{baseSiteId}/slaveMaster", method = RequestMethod.POST)
	@ResponseBody
	public WebSerResponseWsDTO saveSellerSlave(final InputStream slaves, final HttpServletRequest request)
	{
		final WebSerResponseWsDTO userResult = new WebSerResponseWsDTO(); //Object to store result
		BufferedReader br = null;
		final StringBuilder sb = new StringBuilder();
		String saveStatus;
		try
		{
			String line;
			br = new BufferedReader(new InputStreamReader(slaves));
			while ((line = br.readLine()) != null)
			{
				sb.append(line); //Storing data in String Builder object to in order to persist input stream.
			}

			final ServletContext servletContext = request.getSession().getServletContext();
			final String relativeWebPath = MarketplacecommerceservicesConstants.SLAVE_MASTER_XSD_PATH;
			final String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
			final InputStream is0 = new ByteArrayInputStream(sb.toString().getBytes());
			mplValidateAgainstXSDService.validateAgainstXSD(is0, absoluteDiskPath); //Validating XML input received XSD.

			
				final InputStream is1 = new ByteArrayInputStream(sb.toString().getBytes());
				final XStream xstream = new XStream();
				xstream.processAnnotations(SellerSlaveDTO.class); // inform XStream to parse annotations in SellerInformationWSDTO class
				xstream.processAnnotations(SlaveInfoDTO.class); // and in two other classes...
				final String dateFormat = MarketplacecommerceservicesConstants.XSD_DATE_FORMAT;
				final String timeFormat = "";
				final String[] acceptableFormats =
				{ timeFormat };
				xstream.registerConverter(new DateConverter(dateFormat, acceptableFormats, true));
				final SellerSlaveDTO sellerSlavedto = (SellerSlaveDTO) xstream.fromXML(is1); // parse
				saveStatus = mplSlaveMasterService.insertUpdate(sellerSlavedto);
				if (saveStatus.equals(MarketplacecommerceservicesConstants.ERROR_CODE_1))
				{
					userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					userResult.setError(MarketplacecommerceservicesConstants.ERROR_MSG_INVALID_TYPE_CODE);
					LOG.debug(MarketplacecommerceservicesConstants.ERROR_MSG_INVALID_TYPE_CODE);
					return userResult;
				}
				if (saveStatus.equals(MarketplacecommerceservicesConstants.ERROR_FLAG))
				{
					userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
					LOG.debug(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
					return userResult;
				}
		}
		catch (final EtailBusinessExceptions e)
		{
			userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
			return userResult;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
			return userResult;
		}
		catch (final Exception e)
		{
			userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
			return userResult;
		}
		LOG.debug(MarketplacecommerceservicesConstants.DATA_SAVED_MSG);
		userResult.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
		return userResult;
	}
	/**
	 * Seller Master service for storing seller master information received from Seller Portal.
	 *
	 * @param sellerInformation
	 * @param request
	 * @return WebSerResponseWsDTO
	 */
	@RequestMapping(value = "/{baseSiteId}/sellerInformation", method = RequestMethod.POST)
	@ResponseBody
	public WebSerResponseWsDTO saveSellerInformation(final InputStream sellerInformation, final HttpServletRequest request)
	{
		final WebSerResponseWsDTO userResult = new WebSerResponseWsDTO(); //Object to store result
		BufferedReader br = null;
		final StringBuilder sb = new StringBuilder();
		String saveStatus;
		final Map map;
		try
		{
			String line;
			br = new BufferedReader(new InputStreamReader(sellerInformation));
			while ((line = br.readLine()) != null)
			{
				sb.append(line); //Storing data in String Builder object to in order to persist input stream.
			}


			final ServletContext servletContext = request.getSession().getServletContext();
			final String relativeWebPath = MarketplacecommerceservicesConstants.SELLER_MASTER_XSD_PATH;
			final String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
			final InputStream is0 = new ByteArrayInputStream(sb.toString().getBytes());
			map = mplValidateAgainstXSDService.validateAgainstXSD(is0, absoluteDiskPath); //Validating XML input received XSD.

			if (map.get(MarketplacecommerceservicesConstants.STATUS).toString()
					.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				final InputStream is1 = new ByteArrayInputStream(sb.toString().getBytes());
				final XStream xstream = new XStream();
				xstream.processAnnotations(SellerMasterWsDTO.class); // inform XStream to parse annotations in SellerInformationWSDTO class
				xstream.processAnnotations(CorporateAddressWsDTO.class); // and in two other classes...
				xstream.processAnnotations(WthhldTAXWsDTO.class);
				xstream.processAnnotations(PaymentInfoWsDTO.class); // we use for mappings
				final String dateFormat = MarketplacecommerceservicesConstants.XSD_DATE_FORMAT;
				final String timeFormat = "";
				final String[] acceptableFormats =
				{ timeFormat };
				xstream.registerConverter(new DateConverter(dateFormat, acceptableFormats, true));
				final SellerMasterWsDTO sellerdto = (SellerMasterWsDTO) xstream.fromXML(is1); // parse

				//saveStatus = mplSellerMasterService.saveSellerInformation(sellerdto); //Saving seller master information.
				saveStatus = mplSellerMasterService.insertUpdate(sellerdto);
				if (saveStatus.equals(MarketplacecommerceservicesConstants.ERROR_CODE_1))
				{
					userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					userResult.setError(MarketplacecommerceservicesConstants.ERROR_MSG_INVALID_TYPE_CODE);
					LOG.debug(MarketplacecommerceservicesConstants.ERROR_MSG_INVALID_TYPE_CODE);
					return userResult;
				}
				if (saveStatus.equals(MarketplacecommerceservicesConstants.ERROR_FLAG))
				{
					userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
					LOG.debug(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
					return userResult;
				}
			}
			else
			{
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_SCHEMA_MSG);
				userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				if (map.containsKey(MarketplacecommerceservicesConstants.MSG))
				{
					userResult.setError(map.get(MarketplacecommerceservicesConstants.MSG).toString());
				}
				else
				{
					userResult.setError(MarketplacecommerceservicesConstants.INVALID_SCHEMA_MSG);
				}
				return userResult;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
			return userResult;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
			return userResult;
		}
		catch (final Exception e)
		{
			userResult.setError(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
			return userResult;
		}
		LOG.debug(MarketplacecommerceservicesConstants.DATA_SAVED_MSG);
		userResult.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
		return userResult;
	} //End of sellerInformation


	//restriction setup

	/*
	 * restriction set up interface to save the data comming from seller portal
	 *
	 * @param restrictionXML
	 *
	 * @return void
	 */
	@RequestMapping(value = "/{baseSiteId}/miscs/restrictionServer", method = RequestMethod.POST)
	//@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public void restrictionSetUp(final InputStream restrictionXML) throws RequestParameterException, JAXBException
	{ // Using JAXB use this XML to populate the RestrictionWsDTO

		final JAXBContext jaxbContext = JAXBContext.newInstance(RestrictionPins.class);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		final RestrictionPins resDto = (RestrictionPins) jaxbUnmarshaller.unmarshal(restrictionXML);
		//restrictionserviceimpl.saveRestriction(resDto);
		restrictionserviceimpl.beforeSave(resDto);
	}


	//reset password or change password arunashis

	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{baseSiteId}/{userId}/password", method = RequestMethod.PUT)
	//@ResponseStatus(value = HttpStatus.ACCEPTED)
	@ResponseBody
	public UserResultWsDto resetPassword(@PathVariable final String userId, @RequestParam(required = false) final String old,
			@RequestParam(value = "new") final String newPassword)
	{
		final UserResultWsDto result = new UserResultWsDto();
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final UserSignUpWsDTO customer = new UserSignUpWsDTO();
		customer.setPassword(newPassword);
		validate(customer, "password", passwordStrengthValidator);
		if (containsRole(auth, "ROLE_TRUSTED_CLIENT") || containsRole(auth, "ROLE_CUSTOMERMANAGERGROUP"))
		{
			extUserService.setPassword(userId, newPassword);
		}
		else
		{
			if (StringUtils.isEmpty(old))
			{
				throw new RequestParameterException("Request parameter 'old' is missing.", RequestParameterException.MISSING, "old");
			}
			MplCustomerProfileData mplCustData = new MplCustomerProfileData();
			mplCustData.setDisplayUid(userId);
			mplCustData = mplCustomerProfileService.getCustomerProfileDetail(userId);
			final UserModel user = userService.getUserForUID(mplCustData.getUid());
			try
			{
				getCustomerAccountService().changePassword(user, old, newPassword);
			}
			catch (final de.hybris.platform.commerceservices.customer.PasswordMismatchException e)
			{
				//throw new PasswordMismatchException(e);
				result.setError("Please Enter valid password");
				result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				return result;
			}
			//customerFacade.changePassword(old, newPassword);
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			return result;
		}
		result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		return result;
	}


	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	private boolean containsRole(final Authentication auth, final String role)
	{
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	@RequestMapping(value = "/{baseSiteId}/homescreen", method = RequestMethod.GET)
	@ResponseBody
	public BannerWsDTO gethomescreen(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final HomescreenListData homescreendata = homescreenservice.getHomeScreenContent();
		final FieldSetBuilderContext context = new FieldSetBuilderContext();
		final Set<String> fieldSet = fieldSetBuilder.createFieldSet(/* HomescreenListData.class */BannerWsDTO.class,
				DataMapper.FIELD_PREFIX, fields, context);
		BannerWsDTO bannerdto = new BannerWsDTO();
		if (null != homescreendata && null != fieldSet)
		{
			bannerdto = dataMapper.map(homescreendata, BannerWsDTO.class, fieldSet);
		}

		return bannerdto;
	}

	/**
	 * mobile caching - send modified time for cacheable components
	 *
	 * @param fields
	 * @return VersionListResponseWsDTO
	 */
	@RequestMapping(value = "/{baseSiteId}/getVersionResponse", method = RequestMethod.GET)
	@ResponseBody
	public VersionListResponseWsDTO getVersionResponse(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		VersionListResponseData version = new VersionListResponseData();
		VersionListResponseWsDTO versionDto = new VersionListResponseWsDTO();
		try
		{
			version = mplVersionService.getVersionResponseDetails();
			final FieldSetBuilderContext context = new FieldSetBuilderContext();
			final Set<String> fieldSet = fieldSetBuilder.createFieldSet(VersionListResponseWsDTO.class, DataMapper.FIELD_PREFIX,
					fields, context);
			if (null != version && null != fieldSet)
			{
				versionDto = dataMapper.map(version, VersionListResponseWsDTO.class, fieldSet);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				versionDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				versionDto.setErrorCode(e.getErrorCode());
			}
			versionDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				versionDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				versionDto.setErrorCode(e.getErrorCode());
			}
			versionDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return versionDto;
	}

	/**
	 * @Description : For Search and Suggest
	 * @param searchString
	 * @param category
	 * @param fields
	 * @return resultData
	 */
	@RequestMapping(value = "/{baseSiteId}/searchAndSuggest", method = RequestMethod.POST)
	@ResponseBody
	public MplAutoCompleteResultWsData getAutoCompleteSuggestions(@RequestParam final String searchString, final String category,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		MplAutoCompleteResultWsData resultData = new MplAutoCompleteResultWsData();
		final AutoCompleteResultWsData wsData = new AutoCompleteResultWsData();
		String dropDownText = YcommercewebservicesConstants.EMPTY;
		List<ProductData> suggestedProducts = new ArrayList<ProductData>();

		SearchBoxComponentModel component;
		try
		{
			component = (SearchBoxComponentModel) cmsComponentService
					.getSimpleCMSComponent(YcommercewebservicesConstants.SEARCHBOXCOMPONENT);

			if (component.isDisplaySuggestions())
			{
				//wsData.setSuggestions(subList(productSearchFacade.getAutocompleteSuggestions(searchString), component
				//.getMaxSuggestions().intValue()));
				final List<AutocompleteSuggestionData> suggestions = subList(
						productSearchFacade.getAutocompleteSuggestions(searchString), component.getMaxSuggestions().intValue());

				//if (CollectionUtils.isNotEmpty(suggestions) && suggestions.size() > 0)
				if (CollectionUtils.isNotEmpty(suggestions))
				{
					wsData.setSuggestions(suggestions);
				}
				else
				{
					String substr = "";
					substr = searchString.substring(0, searchString.length() - 1);

					wsData.setSuggestions(subList(productSearchFacade.getAutocompleteSuggestions(substr), component
							.getMaxSuggestions().intValue()));

				}
				final SearchStateData searchState = new SearchStateData();
				final SearchQueryData searchQueryData = new SearchQueryData();
				searchQueryData
						.setValue(wsData.getSuggestions().size() > 0 ? wsData.getSuggestions().get(0).getTerm() : searchString);
				searchState.setQuery(searchQueryData);
				searchState.setSns(true);
				final PageableData pageableData = null;
				ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;

				if (!(wsData.getSuggestions().isEmpty()))
				{
					boolean categoryMatch = false;
					boolean sellerMatch = false;
					if (MarketplaceCoreConstants.ALL_CATEGORY.equalsIgnoreCase(category))
					{
						searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
								.textSearch(searchState, pageableData);

						wsData.setCategories(subList(searchPageData.getSnsCategories(), component.getMaxSuggestions().intValue()));
						wsData.setBrands(subList(searchPageData.getAllBrand(), component.getMaxSuggestions().intValue()));
						wsData.setSellerDetails(searchPageData.getAllSeller());
						resultData = populateDTOData(wsData);
					}
					else
					{
						if (category.startsWith(YcommercewebservicesConstants.DROPDOWN_CATEGORY))
						{
							categoryMatch = true;
							dropDownText = category.replaceFirst(YcommercewebservicesConstants.DROPDOWN_CATEGORY,
									YcommercewebservicesConstants.EMPTY);
							searchPageData = searchFacade.categorySearch(dropDownText, searchState, pageableData);
							wsData.setCategories(subList(searchPageData.getSnsCategories(), component.getMaxSuggestions().intValue()));
							wsData.setBrands(subList(searchPageData.getAllBrand(), component.getMaxSuggestions().intValue()));
							resultData.setTopCategories(searchSuggestUtilityMethods.getCategoryDetails(wsData));
							resultData.setTopBrands(searchSuggestUtilityMethods.getBrandDetails(wsData));

							//resultData.setSuggestedTerm(wsData.getSearchTerm());
						}
						else if (!categoryMatch && !sellerMatch && category.startsWith(YcommercewebservicesConstants.DROPDOWN_BRAND))
						{
							dropDownText = category.replaceFirst(YcommercewebservicesConstants.DROPDOWN_BRAND,
									YcommercewebservicesConstants.EMPTY);
							/*
							 * searchPageData = searchFacade.dropDownSearch(searchState, dropDownText,
							 * MarketplaceCoreConstants.BRAND, pageableData);
							 */
							searchPageData = searchFacade.categorySearch(dropDownText, searchState, pageableData);
							wsData.setBrands(subList(searchPageData.getAllBrand(), component.getMaxSuggestions().intValue()));
							wsData.setCategories(subList(searchPageData.getSnsCategories(), component.getMaxSuggestions().intValue()));
							resultData.setTopCategories(searchSuggestUtilityMethods.getCategoryDetails(wsData));
							resultData.setTopBrands(searchSuggestUtilityMethods.getBrandDetails(wsData));
							//wsData.setBrands(subList(searchPageData.getAllBrand(), component.getMaxSuggestions().intValue()));
							//resultData.setSuggestedTerm(wsData.getSearchTerm());
						}
						else if (!sellerMatch && category.startsWith(YcommercewebservicesConstants.DROPDOWN_SELLER))
						{
							sellerMatch = true;
							dropDownText = category.replaceFirst(YcommercewebservicesConstants.DROPDOWN_SELLER,
									YcommercewebservicesConstants.EMPTY);
							searchPageData = searchFacade.dropDownSearch(searchState, dropDownText, "sellerId", pageableData);
							wsData.setSellerDetails(searchPageData.getAllSeller());
							wsData.setBrands(subList(searchPageData.getAllBrand(), component.getMaxSuggestions().intValue()));
							wsData.setCategories(subList(searchPageData.getSnsCategories(), component.getMaxSuggestions().intValue()));
							resultData.setTopSellers(searchSuggestUtilityMethods.getSellerDetails(wsData));
							resultData.setTopCategories(searchSuggestUtilityMethods.getCategoryDetails(wsData));
							resultData.setTopBrands(searchSuggestUtilityMethods.getBrandDetails(wsData));
							resultData.setSuggestedTerm(wsData.getSearchTerm());
						}

					}

					if (null != searchPageData && null != searchPageData.getResults())
					{
						suggestedProducts = searchPageData.getResults();
					}


					if (null != suggestedProducts)
					{
						cleanSearchResults(suggestedProducts);
						wsData.setProductNames(subList(suggestedProducts, component.getMaxSuggestions().intValue()));
						wsData.setProducts(subList(suggestedProducts, component.getMaxProducts().intValue()));
						/*
						 * wsData.setSearchTerm(wsData.getSuggestions().size() > 0 ? wsData.getSuggestions().get(0).getTerm()
						 * : searchString);
						 */
						resultData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						resultData.setPopularProducts(searchSuggestUtilityMethods.getTopProductDetails(wsData));
						//resultData.setSuggestedTerm(wsData.getSearchTerm());
						final List<String> suggestion = new ArrayList<String>();
						for (final AutocompleteSuggestionData suggestionObj : wsData.getSuggestions())
						{
							if (null != suggestionObj.getTerm())
							{
								suggestion.add(suggestionObj.getTerm());
							}
						}
						if (!suggestion.isEmpty())
						{
							resultData.setSuggestionText(suggestion);
						}
					}
				}
				else
				{
					resultData = setErrorStatus();
				}
			}
			else
			{
				resultData = setErrorStatus();
			}
		}
		catch (final CMSItemNotFoundException exception)
		{
			LOG.error(exception.getMessage());
		}
		catch (final EtailBusinessExceptions exception)
		{
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
		}
		return resultData;
	}

	/**
	 * @Description :Populate DTO Data
	 * @param wsData
	 * @return resultdata
	 */
	private MplAutoCompleteResultWsData populateDTOData(final AutoCompleteResultWsData wsData)
	{
		final List<String> suggestionDataList = new ArrayList<String>();
		final MplAutoCompleteResultWsData resultdata = new MplAutoCompleteResultWsData();
		final List<CategorySNSWsData> topbrandsList = new ArrayList<CategorySNSWsData>();
		CategorySNSWsData brandwsDto = null;
		for (final CategoryData brand : wsData.getBrands())
		{
			brandwsDto = new CategorySNSWsData();
			if (null != brand.getCode())
			{
				brandwsDto.setCategoryCode(brand.getCode());
			}
			if (null != brand.getName())
			{
				brandwsDto.setCategoryName(brand.getName());
			}
			//brandwsDto.setType(getCategoryType(brand.getCode()));
			topbrandsList.add(brandwsDto);
		}
		if (!topbrandsList.isEmpty())
		{
			resultdata.setTopBrands(topbrandsList);
		}
		resultdata.setTopCategories(searchSuggestUtilityMethods.getCategoryDetails(wsData));
		resultdata.setTopSellers(searchSuggestUtilityMethods.getSellerDetails(wsData));
		if (null != wsData.getSuggestions() && !wsData.getSuggestions().isEmpty())
		{
			for (final AutocompleteSuggestionData suggestion : wsData.getSuggestions())
			{
				if (null != suggestion.getTerm())
				{
					suggestionDataList.add(suggestion.getTerm());
				}
			}

			if (!suggestionDataList.isEmpty())
			{
				resultdata.setSuggestedTerm(wsData.getSearchTerm());
			}
		}

		return resultdata;
	}

	/**
	 * @Description :Populate Error Data
	 * @return resultdata
	 */
	private MplAutoCompleteResultWsData setErrorStatus()
	{
		final MplAutoCompleteResultWsData resultdata = new MplAutoCompleteResultWsData();
		resultdata.setError(Localization.getLocalizedString("webservice.search.sns.error"));
		resultdata.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		return resultdata;

	}

	protected <E> List<E> subList(final List<E> list, final int maxElements)
	{
		if (CollectionUtils.isEmpty(list))
		{
			return Collections.emptyList();
		}

		if (list.size() > maxElements)
		{
			return list.subList(0, maxElements);
		}

		return list;
	}

	/**
	 * @Description : For Search and Suggest
	 * @param resultData
	 * @return: void
	 */
	private void cleanSearchResults(final List<ProductData> resultData)
	{
		for (final ProductData productData : resultData)
		{
			if (productData.getImages() == null)
			{
				final List<ImageData> images = new ArrayList<ImageData>(Arrays.asList(new ImageData()));
				productData.setImages(images);
			}
			if (productData.getPrice() == null)
			{
				productData.setPrice(new PriceData());
			}
		}
	}

	/**
	 * @Description : For Pincode Serviceability, OMS service call
	 * @param pin
	 * @param productCode
	 * @return List<PinCodeResponseData> from OMS
	 */
	@RequestMapping(value = MarketplacewebservicesConstants.URL, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPORJSON)
	@ResponseBody
	public PinWsDto getPin(@RequestParam(value = "pin") final String pin,
			@RequestParam(value = "productCode") final List<String> productCode) throws CMSItemNotFoundException
	{

		final PinWsDto pinWsDto = new PinWsDto();
		final Map<String, ListPinCodeServiceData> listOfDataList = new HashMap<String, ListPinCodeServiceData>();
		try
		{
			for (final String productCodeStr : productCode)
			{
				final ListPinCodeServiceData dataList = new ListPinCodeServiceData();
				if (null != productCodeStr && StringUtils.isNotEmpty(productCodeStr))
				{
					/*
					 * productModel = productService.getProductForCode(productCodeStr); ProductData productData = null; if
					 * (null != productModel) { productData = productFacade.getProductForOptions(productModel,
					 * Arrays.asList(ProductOption.BASIC, ProductOption.PRICE)); } else { throw new
					 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9037); } PincodeServiceData data = null;
					 * MarketplaceDeliveryModeData deliveryModeData = null; List<PinCodeResponseData> response = null;
					 *
					 * if (null != productData && null != productData.getSeller()) { for (final SellerInformationData seller
					 * : productData.getSeller()) { final List<MarketplaceDeliveryModeData> deliveryModeList = new
					 * ArrayList<MarketplaceDeliveryModeData>(); data = new PincodeServiceData(); if
					 * (!(seller.getDeliveryModes().isEmpty())) { for (final MarketplaceDeliveryModeData deliveryMode :
					 * seller.getDeliveryModes()) { if (null != deliveryMode && null != deliveryMode.getCode() && null !=
					 * seller.getUssid()) { deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(),
					 * seller.getUssid()); } deliveryModeList.add(deliveryModeData); }
					 * data.setDeliveryModes(deliveryModeList); }
					 *
					 * if (StringUtils.isNotEmpty(seller.getFullfillment())) {
					 * data.setFullFillmentType(seller.getFullfillment()); } if
					 * (StringUtils.isNotEmpty(seller.getShippingMode())) { data.setTransportMode(seller.getShippingMode());
					 * } if (null != seller.getMopPrice() && null != seller.getMopPrice().getValue()) { data.setPrice(new
					 * Double(seller.getMopPrice().getValue().doubleValue())); } if
					 * (StringUtils.isNotEmpty(seller.getIsCod())) { data.setIsCOD(seller.getIsCod()); } if
					 * (StringUtils.isNotEmpty(seller.getSellerID())) { data.setSellerId(seller.getSellerID()); } if
					 * (StringUtils.isNotEmpty(seller.getUssid())) { data.setUssid(seller.getUssid()); }
					 * data.setIsDeliveryDateRequired(MarketplacewebservicesConstants.NA); requestData.add(data); } }
					 */
					List<PinCodeResponseData> response = null;
					final PincodeModel pinCodeModelObj = pincodeService.getLatAndLongForPincode(pin);
					if( null != pinCodeModelObj)
					{
   					String configurableRadius = Config.getParameter("marketplacestorefront.configure.radius") != null ? Config.getParameter("marketplacestorefront.configure.radius") : "0";
   					LOG.debug("configurableRadius is:" + Double.parseDouble(configurableRadius));
   					final LocationDTO dto = new LocationDTO();
   					dto.setLongitude(pinCodeModelObj.getLongitude().toString());
   					dto.setLatitude(pinCodeModelObj.getLatitude().toString());
   					final Location myLocation = new LocationDtoWrapper(dto);
   					LOG.debug("Selected Location for Latitude..:" + myLocation.getGPS().getDecimalLatitude());
   					LOG.debug("Selected Location for Longitude..:" + myLocation.getGPS().getDecimalLongitude());
   					response = pinCodeFacade.getResonseForPinCode(productCodeStr, pin,
   							pincodeServiceFacade.populatePinCodeServiceData(productCodeStr, myLocation.getGPS(), Double.parseDouble(configurableRadius)));
					}
					if (null != response)
					{
						dataList.setPincodeListResponse(response);
					}
					listOfDataList.put(productCodeStr, dataList);
				}
				pinWsDto.setListOfDataList(listOfDataList);
			}
		}
		catch (final UnknownIdentifierException e)
		{
			final EtailNonBusinessExceptions ex = new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9037);
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				pinWsDto.setError(ex.getErrorMessage());
			}
			if (null != ex.getErrorCode())
			{
				pinWsDto.setErrorCode(ex.getErrorCode());
			}
			pinWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				pinWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				pinWsDto.setErrorCode(e.getErrorCode());
			}
			pinWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				pinWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				pinWsDto.setErrorCode(e.getErrorCode());
			}
			pinWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			final EtailNonBusinessExceptions ex = new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				pinWsDto.setError(ex.getErrorMessage());
			}
			if (null != ex.getErrorCode())
			{
				pinWsDto.setErrorCode(ex.getErrorCode());
			}
			pinWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return pinWsDto;
	}

	// get state
	@RequestMapping(value = "/{baseSiteId}/state", method = RequestMethod.GET)
	@ResponseBody
	public StateListWsDto getState(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		final StateListWsDto dataList = new StateListWsDto();
		final List<StateWsDto> statewsdtoList = new ArrayList<StateWsDto>();
		StateWsDto statewsdto = new StateWsDto();
		try
		{
			final List<StateData> stateDataList = accountAddressFacade.getStates();
			if (stateDataList != null)
			{
				for (final StateData stateData : stateDataList)
				{
					statewsdto = new StateWsDto();
					if (stateData.getName() != null)
					{
						statewsdto.setName(stateData.getName());
					}
					if (stateData.getCode() != null)
					{
						statewsdto.setCode(stateData.getCountryKey() + stateData.getCode());
					}
					statewsdtoList.add(statewsdto);
				}
				dataList.setStates(statewsdtoList);
				dataList.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				dataList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final Exception e)
		{
			dataList.setError(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return dataMapper.map(dataList, StateListWsDto.class, fields);
	}

	/**
	 * @param deliveryMode
	 * @param ussid
	 * @return MarketplaceDeliveryModeData
	 */
	/*
	 * private MarketplaceDeliveryModeData fetchDeliveryModeDataForUSSID(final String deliveryMode, final String ussid) {
	 * final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData(); final
	 * MplZoneDeliveryModeValueModel MplZoneDeliveryModeValueModel = mplCheckoutFacade
	 * .populateDeliveryCostForUSSIDAndDeliveryMode(deliveryMode, MarketplaceFacadesConstants.INR, ussid);
	 *
	 * if (null != MplZoneDeliveryModeValueModel) { if (null != MplZoneDeliveryModeValueModel.getValue()) { final
	 * PriceData priceData = formPriceData(MplZoneDeliveryModeValueModel.getValue()); if (null != priceData) {
	 * deliveryModeData.setDeliveryCost(priceData); } } if (null != MplZoneDeliveryModeValueModel.getDeliveryMode() &&
	 * null != MplZoneDeliveryModeValueModel.getDeliveryMode().getCode()) {
	 * deliveryModeData.setCode(MplZoneDeliveryModeValueModel.getDeliveryMode().getCode()); } if (null !=
	 * MplZoneDeliveryModeValueModel.getDeliveryMode() && null !=
	 * MplZoneDeliveryModeValueModel.getDeliveryMode().getDescription()) {
	 * deliveryModeData.setDescription(MplZoneDeliveryModeValueModel.getDeliveryMode().getDescription()); } if (null !=
	 * MplZoneDeliveryModeValueModel.getDeliveryMode() && null !=
	 * MplZoneDeliveryModeValueModel.getDeliveryMode().getName()) {
	 * deliveryModeData.setName(MplZoneDeliveryModeValueModel.getDeliveryMode().getName()); } if (null != ussid) {
	 * deliveryModeData.setSellerArticleSKU(ussid); }
	 *
	 * } return deliveryModeData; }
	 */
	/**
	 * Converting datatype of price
	 *
	 * @param price
	 * @return pData
	 */

	public PriceData formPriceData(final Double price)
	{
		final PriceData priceData = new PriceData();
		priceData.setPriceType(PriceDataType.BUY);
		priceData.setValue(new BigDecimal(price.doubleValue()));
		priceData.setCurrencyIso(MarketplaceFacadesConstants.INR);
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(priceData.getCurrencyIso());
		currency.setSymbol(priceData.getCurrencyIso());
		final PriceData pData = priceDataFactory.create(PriceDataType.BUY, priceData.getValue(), currency);
		return pData;
	}

	/**
	 * Display About Us Page
	 *
	 * @param fields
	 * @return aboutUsBannerData
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/{baseSiteId}/aboutUs", method = RequestMethod.GET)
	@ResponseBody
	public AboutUsResultWsData getAboutUs(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CMSItemNotFoundException
	{
		final AboutUsResultWsData aboutUsBannerData = getMplCustomCategoryService().getAboutus();

		return aboutUsBannerData;
	}

	/**
	 * Display Hel and Services Page
	 *
	 * @param fields
	 * @return helpNservicesData
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/{baseSiteId}/helpAndservices", method = RequestMethod.GET)
	@ResponseBody
	public HelpAndServicestWsData getHelpNServices(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CMSItemNotFoundException
	{
		final HelpAndServicestWsData helpNservicesData = getMplCustomCategoryService().getHelpnServices();

		return helpNservicesData;

	}

	////////////////// Help And Services /////////////////////

	/**
	 * @description Return all drop-down for a search
	 *
	 */
	@RequestMapping(value = "/{baseSiteId}/searchDropdown", method = RequestMethod.GET)
	@ResponseBody
	public SearchDropdownWsDTO getSearchDropdown()
	{

		final SearchDropdownWsDTO dataList = new SearchDropdownWsDTO();
		final Map<String, String> categoryMap = new HashMap<String, String>();
		final Map<String, String> brandMap = new HashMap<String, String>();
		final Map<String, String> sellerMap = new HashMap<String, String>();

		MplEnhancedSearchBoxComponentModel component;
		try
		{
			//TODO It was added in respect of MplEnhancedSearchBoxComponentController.java
			component = cmsComponentService.getSimpleCMSComponent(MarketplacewebservicesConstants.SEARCH_COMPONENT);
			final Collection<CategoryModel> categorys = component.getSearchBoxCategories();
			final Collection<CategoryModel> brands = component.getSearchBoxBrands();
			final Collection<SellerMasterModel> sellers = component.getSearchBoxSellerMaster();


			for (final CategoryModel category : categorys)
			{
				categoryMap.put(category.getCode(), category.getName());
			}

			for (final CategoryModel brand : brands)
			{
				brandMap.put(brand.getCode(), brand.getName());
			}

			for (final SellerMasterModel seller : sellers)
			{
				sellerMap.put(seller.getId(), seller.getLegalName());
			}

			//TODO remove stub method
			//final List<CategoryData> categoryData=mplCategoryService.getJsonProductHierearchy(hierarchyList)
			//category.put("code1", "Westside");
			//category.put("code2", "Titan");
			dataList.setCategoryList(categoryMap);
			dataList.setBrandList(brandMap);
			dataList.setSellerList(sellerMap);

			dataList.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			dataList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			dataList.setError(MarketplacecommerceservicesConstants.SEARCH_NOT_FOUND);
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return dataList;
		}
		catch (final Exception e)
		{
			dataList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			dataList.setError(MarketplacecommerceservicesConstants.SEARCH_NOT_FOUND);
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return dataList;

		}

		return dataList;
	}

	/**
	 * @description to capture Feedback NO
	 * @return userResultWsDto
	 * @throws CMSItemNotFoundException
	 */
	/*
	 * @Secured( { "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 */
	@RequestMapping(value = "/{baseSiteId}/feedbackno", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public UserResultWsDto captureFeedbackNo(@RequestParam final String emailId, @RequestParam final String searchCategory,
			@RequestParam final String searchText, @RequestParam final String comment, @RequestParam final String category)
			throws CMSItemNotFoundException
	{
		String returnValue = null;
		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		try
		{
			returnValue = updateFeedbackFacade.updateFeedbackNo(comment, category, emailId, searchCategory, searchText);
			userResultWsDto.setStatus(returnValue);
		}
		catch (final EtailBusinessExceptions e)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			userResultWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
			return userResultWsDto;
		}

		catch (final EtailNonBusinessExceptions e)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			userResultWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
			return userResultWsDto;
		}
		return userResultWsDto;
	}


	/**
	 * @description to get Feedback
	 * @return Map containing feedback categories
	 */
	/*
	 * @Secured( { "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 */
	@RequestMapping(value = "/{baseSiteId}/getFeedbackCategory", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public UserResultWsDto getFeedbackCategory()
	{
		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		try
		{
			final List<FeedbackCategory> category = enumerationService.getEnumerationValues(FeedbackCategory.class);
			if (null != category && !category.isEmpty())
			{
				final Map<String, String> sFeedBack = new HashMap<String, String>();
				for (final FeedbackCategory s : category)
				{
					sFeedBack.put(s.getCode(), s.getCode());
				}
				userResultWsDto.setCategory(sFeedBack);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			userResultWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
			return userResultWsDto;
		}

		catch (final EtailNonBusinessExceptions e)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			userResultWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
			return userResultWsDto;
		}

		return userResultWsDto;
	}

}

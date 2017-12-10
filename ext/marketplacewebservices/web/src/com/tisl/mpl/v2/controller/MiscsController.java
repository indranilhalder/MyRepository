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

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
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
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
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
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;
import de.hybris.platform.commercewebservicescommons.mapping.impl.FieldSetBuilderContext;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.util.localization.Localization;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.tis.mpl.facade.data.TicketStatusUpdate;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.enums.FeedbackCategory;
import com.tisl.mpl.core.model.MplEnhancedSearchBoxComponentModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.CODSelfShipResponseData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.facade.netbank.MplNetBankingFacade;
import com.tisl.mpl.facade.pancard.MplPancardFacade;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.facades.webform.MplWebFormFacade;
import com.tisl.mpl.marketplacecommerceservices.event.LuxuryPdpQuestionEvent;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;
import com.tisl.mpl.order.data.CardTypeDataList;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.pojo.PanCardResDTO;
import com.tisl.mpl.populator.HttpRequestCustomerUpdatePopulator;
import com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade;
import com.tisl.mpl.service.HomescreenService;
import com.tisl.mpl.service.MplCustomCategoryService;
import com.tisl.mpl.service.MplRestrictionServiceImpl;
import com.tisl.mpl.service.MplSellerMasterService;
import com.tisl.mpl.service.MplSlaveMasterService;
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
import com.tisl.mpl.wsdto.CRMWsData;
import com.tisl.mpl.wsdto.CRMWsDataParent;
import com.tisl.mpl.wsdto.CategoryBrandDTO;
import com.tisl.mpl.wsdto.CategorySNSWsData;
import com.tisl.mpl.wsdto.CorporateAddressWsDTO;
import com.tisl.mpl.wsdto.FileUploadResponseData;
import com.tisl.mpl.wsdto.HelpAndServicestWsData;
import com.tisl.mpl.wsdto.HomescreenListData;
import com.tisl.mpl.wsdto.ListPinCodeServiceData;
import com.tisl.mpl.wsdto.MplAutoCompleteResultWsData;
import com.tisl.mpl.wsdto.NewsletterWsDTO;
import com.tisl.mpl.wsdto.OfferListWsData;
import com.tisl.mpl.wsdto.OneTouchCancelReturnCrmRequestDTO;
import com.tisl.mpl.wsdto.OneTouchCancelReturnCrmRequestList;
import com.tisl.mpl.wsdto.OneTouchCancelReturnDTO;
import com.tisl.mpl.wsdto.OrderInfoWsDTO;
import com.tisl.mpl.wsdto.PaymentInfoWsDTO;
import com.tisl.mpl.wsdto.PinWsDto;
import com.tisl.mpl.wsdto.ProductSearchPageWsDto;
import com.tisl.mpl.wsdto.ResponseMaster;
import com.tisl.mpl.wsdto.RestrictionPins;
import com.tisl.mpl.wsdto.ReversePincodeExchangeData;
import com.tisl.mpl.wsdto.SearchDropdownWsDTO;
import com.tisl.mpl.wsdto.SellerMasterWsDTO;
import com.tisl.mpl.wsdto.SellerSlaveDTO;
import com.tisl.mpl.wsdto.SlaveInfoDTO;
import com.tisl.mpl.wsdto.StateListWsDto;
import com.tisl.mpl.wsdto.StateWsDto;
import com.tisl.mpl.wsdto.UserResultWsDto;
import com.tisl.mpl.wsdto.VersionListResponseData;
import com.tisl.mpl.wsdto.VersionListResponseWsDTO;
import com.tisl.mpl.wsdto.WebSerResponseWsDTO;
import com.tisl.mpl.wsdto.WthhldTAXWsDTO;


/**
 * @author TCS
 */
@Controller
@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
public class MiscsController extends BaseController
{

	private static final String APPLICATION_JSON = "application/json"; //Sonar fix
	private static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT"; //Sonar fix
	private static final String ROLE_CLIENT = "ROLE_CLIENT"; //Sonar fix
	private static final String FAILURE = "Failure";
	public static final String RETURN_TYPE_COD = "01";
	private static final String SUCCESS = "Success";




	@Resource(name = "brandFacade")
	private BrandFacade brandFacade;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "storeSessionFacade")
	private StoreSessionFacade storeSessionFacade;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;
	@Resource(name = "httpRequestCustomerUpdatePopulator")
	private HttpRequestCustomerUpdatePopulator httpRequestCustomerUpdatePopulator;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Autowired
	private UserService userService;
	@Autowired
	private MplCustomerProfileService mplCustomerProfileService;
	@Resource(name = "passwordStrengthValidator")
	private Validator passwordStrengthValidator;
	@Autowired
	private ExtendedUserService extUserService;
	@Autowired
	private CustomerAccountService customerAccountService;
	@Autowired
	private MplAccountAddressFacade accountAddressFacade;
	@Resource(name = "homescreenservice")
	private HomescreenService homescreenservice;
	@Resource(name = "fieldSetBuilder")
	private FieldSetBuilder fieldSetBuilder;

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
	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;
	@Resource(name = "categoryService")
	private CategoryService categoryService;

	//Exchange Changes
	@Resource(name = "exchangeGuideFacade")
	private ExchangeGuideFacade exchangeGuideFacade;

	@Resource(name = "mplPancardFacadeImpl")
	private MplPancardFacade mplPancardFacade;
	@Autowired
	private MplVersionService mplVersionService;
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
	/*
	 * @Resource(name = "mplCategoryServiceImpl") private MplCategoryService mplCategoryService;
	 */
	@Autowired
	private SearchSuggestUtilityMethods searchSuggestUtilityMethods;
	@Autowired
	private EventService eventservice;
	//End of Declaration for SNS
	@Resource(name = "pinCodeFacade")
	private PinCodeServiceAvilabilityFacade pinCodeFacade;
	@Autowired
	private PriceDataFactory priceDataFactory;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private ModelService modelService;
	private static final String APPLICATION_TYPE = "application/json";
	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	@Autowired
	private SessionService sessionService;
	//TPR-4512
	@Resource(name = "mplOrderFacade")
	private MplOrderFacade mplOrderFacade;
	@Autowired
	private ConfigurationService configurationService;
	//Newly added for TPR-1345:One touch CRM
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Resource(name = "orderConverter")
	private Converter<OrderModel, OrderData> orderConverter;
	@Resource(name = "cancelReturnFacade")
	private CancelReturnFacade cancelReturnFacade;
	@Autowired
	private DateUtilHelper dateUtilHelper;

	@Autowired
	private MplCouponFacade mplCouponFacade;

	@Autowired
	private MplWebFormFacade mplWebFormFacade;
	private static final Logger LOG = Logger.getLogger(MiscsController.class);


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
	{ ROLE_CLIENT, "ROLE_CUSTOMERGROUP", ROLE_TRUSTED_CLIENT, "ROLE_CUSTOMERMANAGERGROUP" })
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
	 *
	 * @author TECHOUTS
	 * @param slaves
	 * @param request
	 *
	 * @return WebSerResponseWsDTO
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
			userResult.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
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
			ExceptionUtil.getCustomizedExceptionTrace(e);
			userResult.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			userResult.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}


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
			ExceptionUtil.getCustomizedExceptionTrace(e);
			userResult.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			userResult.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
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
	{ ROLE_CLIENT, "ROLE_CUSTOMERGROUP", ROLE_TRUSTED_CLIENT, "ROLE_CUSTOMERMANAGERGROUP" })
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
		if (containsRole(auth, ROLE_TRUSTED_CLIENT) || containsRole(auth, "ROLE_CUSTOMERMANAGERGROUP"))
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
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false) final boolean isFromLuxuryWeb)
	{
		MplAutoCompleteResultWsData resultData = new MplAutoCompleteResultWsData();
		final AutoCompleteResultWsData wsData = new AutoCompleteResultWsData();
		List<ProductData> suggestedProducts = new ArrayList<ProductData>();
		final PageableData pageableData = null;
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		final List<String> suggestion = new ArrayList<String>();
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		try
		{
			LOG.debug("searchAndSuggest---------" + searchString);
			final List<AutocompleteSuggestionData> finalSuggestions = new ArrayList<AutocompleteSuggestionData>();
			List<AutocompleteSuggestionData> suggestions = productSearchFacade.getAutocompleteSuggestions(searchString);
			if (CollectionUtils.isNotEmpty(suggestions))
			{
				finalSuggestions.add(suggestions.get(0));
				wsData.setSuggestions(finalSuggestions);
			}
			else
			{
				String substr = "";
				substr = searchString.substring(0, searchString.length() - 1);
				suggestions = productSearchFacade.getAutocompleteSuggestions(substr);
				if (CollectionUtils.isNotEmpty(suggestions))
				{
					finalSuggestions.add(suggestions.get(0));
				}
				wsData.setSuggestions(finalSuggestions);
			}

			//if (CollectionUtils.isNotEmpty(suggestions) && suggestions.size() > 0)
			//			{
			//				wsData.setSuggestions(suggestions);
			//			}
			//			else
			//			{
			//				String substr = "";
			//				substr = searchString.substring(0, searchString.length() - 1);
			//				wsData.setSuggestions(productSearchFacade.getAutocompleteSuggestions(substr));
			//			}
			LOG.debug("searchAndSuggest-------------Size" + suggestions.size());

			//resultData.setSuggestions(productSearchFacade.getAutocompleteSuggestions(term));

			/*********** Fixing for Defect TISPRO-58 and TISPRD-346 Start */
			String tempSuggestion = "";
			final List<AutocompleteSuggestionData> suggestionsList = wsData.getSuggestions();
			if (CollectionUtils.isNotEmpty(suggestionsList))
			{
				final String firstSuggestion = suggestionsList.get(0).getTerm();

				final StringTokenizer termWordCount = new StringTokenizer(searchString, " ");
				final int count = termWordCount.countTokens();

				final String[] suggestedTerm = firstSuggestion.split(" ");
				for (int i = 0; i < count; i++)
				{
					if (i > 0)
					{
						tempSuggestion = tempSuggestion + " " + suggestedTerm[i];
					}
					else
					{
						tempSuggestion = suggestedTerm[i];
					}
				}
			}
			else
			{
				tempSuggestion = searchString;
			}

			searchQueryData.setValue(tempSuggestion);
			/*********** Fixing for Defect TISPRO-58 and TISPRD-346 End */
			//searchQueryData.setValue(resultData.getSuggestions().size() > 0 ? resultData.getSuggestions().get(0).getTerm() : term);
			searchState.setQuery(searchQueryData);
			searchState.setSns(true);
			if (isFromLuxuryWeb)
			{
				searchState.setLuxurySiteFrom(MarketplacecommerceservicesConstants.CHANNEL_WEB);
			}
			//			else
			//			{
			//				searchState.setLuxurySiteFrom(MarketplacecommerceservicesConstants.CHANNEL_APP);
			//			}


			if (CollectionUtils.isNotEmpty(wsData.getSuggestions()))
			{
				if (category.startsWith(MarketplaceCoreConstants.ALL_CATEGORY))
				{
					searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
							.textSearch(searchState, pageableData);
					wsData.setCategories(searchPageData.getSnsCategories());
					wsData.setBrands(searchPageData.getAllBrand());
					wsData.setSellerDetails(searchPageData.getAllSeller());
					resultData = populateDTOData(wsData);
					//allSearchFlag = true;
				}
				else
				{
					if (category.startsWith(MarketplacewebservicesConstants.DROPDOWN_CATEGORY)
							|| category.startsWith(MarketplacewebservicesConstants.DROPDOWN_BRAND))
					{
						searchPageData = searchFacade.categorySearch(category, searchState, pageableData);
					}
					else
					{
						searchPageData = searchFacade.dropDownSearch(searchState, category,
								MarketplacewebservicesConstants.POS_SELLERID, pageableData);
					}
					wsData.setCategories(searchPageData.getSnsCategories());
					wsData.setBrands(searchPageData.getAllBrand());
					wsData.setSellerDetails(searchPageData.getAllSeller());

					resultData.setTopSellers(searchSuggestUtilityMethods.getSellerDetails(wsData));
					resultData.setTopCategories(searchSuggestUtilityMethods.getCategoryDetails(wsData));
					resultData.setTopBrands(searchSuggestUtilityMethods.getBrandDetails(wsData));


				}
				suggestedProducts = searchPageData.getResults();
				//this is done to remove some of the data issues where we
				//have null images or price
				if (suggestedProducts != null)
				{
					cleanSearchResults(suggestedProducts);
					//resultData.setProductNames(subList(suggestedProducts, component.getMaxSuggestions()));
					//wsData.setProductNames(suggestedProducts);
					wsData.setProducts(suggestedProducts);
					//wsData.setSearchTerm(wsData.getSuggestions().size() > 0 ? wsData.getSuggestions().get(0).getTerm() : searchString);

					resultData.setPopularProducts(searchSuggestUtilityMethods.getTopProductDetails(wsData));
					resultData.setSuggestedTerm(wsData.getSearchTerm());

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
				resultData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				resultData = setErrorStatus();
			}
		}
		catch (final EtailNonBusinessExceptions eb)
		{
			LOG.debug("Error occured in getAutocompleteSuggestions :" + eb.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(eb);
			resultData = setErrorStatus();
		}
		catch (final Exception eb)
		{
			LOG.debug("Error occured in getAutocompleteSuggestions :" + eb.getMessage());
			ExceptionUtil.getCustomizedExceptionTrace(eb);
			resultData = setErrorStatus();
		}

		if (StringUtils.isNotEmpty(sessionService.getAttribute("queryType")))
		{
			LOG.debug("REmoving from Session Attribute query type");
			sessionService.removeAttribute("queryType");
		}
		return resultData;
	}


	/**
	 * @Description : For PDP widgets, search on offer ,color size etc
	 * @param type
	 * @param typeValue
	 * @param page
	 * @param fields
	 * @return resultData
	 */
	@RequestMapping(value = "/{baseSiteId}/getPDPWidgets", method =
	{ RequestMethod.POST, RequestMethod.GET }, produces = MarketplacecommerceservicesConstants.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductSearchPageWsDto searchProductDto(@RequestParam(required = true) final String type,
			@RequestParam(required = true) final String typeValue, @RequestParam(required = true) final int page,
			@RequestParam(required = true) final int pageSize, @RequestParam(required = false) final String categoryCode,
			@RequestParam(required = false) final String sortCode,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		String searchText = "";
		ProductSearchPageWsDto productSearchPage = new ProductSearchPageWsDto();
		ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
		try
		{
			final PageableData pageableData = createPageableData(page, pageSize, sortCode, ShowMode.Page);
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();

			if (StringUtils.isNotEmpty(typeValue) && StringUtils.isNotEmpty(type))
			{
				if (type.equalsIgnoreCase(MarketplacecommerceservicesConstants.OFFER))
				{
					if (StringUtils.isNotEmpty(categoryCode))
					{
						searchText = MarketplacecommerceservicesConstants.RELEVANCE_CATEGORY + categoryCode
								+ MarketplacecommerceservicesConstants.OFFER_COLON + typeValue;
					}
					else
					{
						searchText = MarketplacecommerceservicesConstants.RELEVANCE_OFFER + typeValue;
					}
				}
				else if (type.equalsIgnoreCase(MarketplacecommerceservicesConstants.COLOUR))
				{
					if (StringUtils.isNotEmpty(categoryCode))
					{
						searchText = MarketplacecommerceservicesConstants.RELEVANCE_CATEGORY + categoryCode
								+ MarketplacecommerceservicesConstants.COLOUR_COLON + typeValue;
					}
					else
					{
						searchText = MarketplacecommerceservicesConstants.RELEVANCE_COLOR + typeValue;
					}
				}
				else
				{
					if (StringUtils.isNotEmpty(categoryCode))
					{
						searchText = MarketplacecommerceservicesConstants.RELEVANCE_CATEGORY + categoryCode
								+ MarketplacecommerceservicesConstants.SIZE_COLON + typeValue.toUpperCase();
					}
					else
					{
						searchText = MarketplacecommerceservicesConstants.RELEVANCE_SIZE + typeValue.toUpperCase();
					}
				}

				searchQueryData.setValue(searchText);
				searchState.setQuery(searchQueryData);
				searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) productSearchFacade
						.textSearch(searchState, pageableData);

				if (searchPageData != null)
				{
					productSearchPage = searchSuggestUtilityMethods.setPDPSearchPageData(searchPageData);
				}
				//setting category
				if (StringUtils.isNotEmpty(categoryCode))
				{
					productSearchPage.setCategoryCode(categoryCode);
				}

				final ProductSearchPageWsDto sortingvalues = dataMapper.map(searchPageData, ProductSearchPageWsDto.class, fields);
				if (null != sortingvalues)
				{
					if (null != sortingvalues.getPagination())
					{
						productSearchPage.setPagination(sortingvalues.getPagination());
					}
					if (null != sortingvalues.getSorts())
					{
						productSearchPage.setSorts(sortingvalues.getSorts());
					}
					if (null != sortingvalues.getCurrentQuery())
					{
						productSearchPage.setCurrentQuery(sortingvalues.getCurrentQuery());
					}
					if (null != searchPageData.getSpellingSuggestion()
							&& null != searchPageData.getSpellingSuggestion().getSuggestion())
					{
						productSearchPage.setSpellingSuggestion(searchPageData.getSpellingSuggestion().getSuggestion());
					}
				}
			}
			else
			{
				productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				productSearchPage.setError(MarketplacecommerceservicesConstants.INVALIDSEARCHKEY);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
			//e.printStackTrace();
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
			//e.printStackTrace();
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			productSearchPage.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + ":" + e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			productSearchPage.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			productSearchPage.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			productSearchPage.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return productSearchPage;
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
	@RequestMapping(value = MarketplacewebservicesConstants.CHECK_PINCODE, method = RequestMethod.POST, produces = MarketplacewebservicesConstants.APPORJSON)
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


					List<PinCodeResponseData> response = null;
					final PincodeModel pinCodeModelObj = pincodeServiceFacade.getLatAndLongForPincode(pin);
					if (null != pinCodeModelObj)
					{

						final LocationDTO dto = new LocationDTO();
						dto.setLongitude(pinCodeModelObj.getLongitude().toString());
						dto.setLatitude(pinCodeModelObj.getLatitude().toString());
						final Location myLocation = new LocationDtoWrapper(dto);
						LOG.debug("Selected Location for Latitude..:" + myLocation.getGPS().getDecimalLatitude());
						LOG.debug("Selected Location for Longitude..:" + myLocation.getGPS().getDecimalLongitude());
						response = pinCodeFacade.getResonseForPinCode(productCodeStr, pin,
								pincodeServiceFacade.populatePinCodeServiceData(productCodeStr, myLocation.getGPS()));
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
			ExceptionUtil.getCustomizedExceptionTrace(e);
			pinWsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
			pinWsDto.setErrorCode(MarketplacecommerceservicesConstants.B9004);
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

	// check brand or category TPR-816
	/**
	 *
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
	 * 
	 * @param code
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{baseSiteId}/checkBrandOrCategory", method = RequestMethod.GET)
	@ResponseBody
	public CategoryBrandDTO checkBrandOrCategory(@RequestParam final String code)
	{
		final CategoryBrandDTO result = new CategoryBrandDTO();
		CategoryModel selectedCategory = null;
		try
		{
			if (StringUtils.isNotEmpty(code))
			{
				selectedCategory = categoryService.getCategoryForCode(code);
				//EQA comments
				if (selectedCategory != null && StringUtils.isNotEmpty(selectedCategory.getCode()))
				{
					result.setCode(code);
					result.setName(selectedCategory.getName());
					if (selectedCategory.getCode().contains(MarketplacewebservicesConstants.DROPDOWN_BRAND))
					{
						result.setType(MarketplacecommerceservicesConstants.BRAND);
					}
					else
					{
						result.setType(MarketplacecommerceservicesConstants.CATEGORY);
					}
					result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
			}

		}
		catch (final UnknownIdentifierException e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			result.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9421));
			result.setErrorCode(MarketplacecommerceservicesConstants.B9421);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			result.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			result.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}


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
		final AboutUsResultWsData aboutUsBannerData = mplCustomCategoryService.getAboutus();

		return aboutUsBannerData;
	}


	/**
	 * Display OFFERS IN PAYMENT PAGE
	 *
	 * @param fields
	 * @return aboutUsBannerData
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = "/{baseSiteId}/paymentSpecificOffers", method = RequestMethod.GET)
	@ResponseBody
	public OfferListWsData getPaymentSpecificOffers(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CMSItemNotFoundException
	{
		OfferListWsData offersData = new OfferListWsData();
		try
		{
			offersData = mplCouponFacade.getAllOffersForMobile();
			if (offersData != null)
			{
				offersData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}


		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				offersData.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				offersData.setErrorCode(e.getErrorCode());
			}
			offersData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				offersData.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				offersData.setErrorCode(e.getErrorCode());
			}
			offersData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			offersData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			offersData.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			offersData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return offersData;
	}

	/**
	 * Display OFFERS TERMS AND CONDITIONS IN PAYMENT PAGE
	 *
	 * @param fields
	 * @return aboutUsBannerData
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = "/{baseSiteId}/paymentSpecificOffersTermsAndCondition", method = RequestMethod.GET)
	@ResponseBody
	public OfferListWsData getPaymentSpecificOffersTermsAndCondition(
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws CMSItemNotFoundException
	{
		OfferListWsData offersData = new OfferListWsData();
		try
		{
			offersData = mplCouponFacade.getAllOffersTermsAndConditionForMobile();
			if (offersData != null)
			{
				offersData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}


		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				offersData.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				offersData.setErrorCode(e.getErrorCode());
			}
			offersData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				offersData.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				offersData.setErrorCode(e.getErrorCode());
			}
			offersData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			offersData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			offersData.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			offersData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return offersData;
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
		final HelpAndServicestWsData helpNservicesData = mplCustomCategoryService.getHelpnServices();

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
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			dataList.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			dataList.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			dataList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return dataList;
	}

	/**
	 * @description to capture Feedback NO
	 * @return userResultWsDto
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/{baseSiteId}/feedbackno", method = RequestMethod.GET, produces = APPLICATION_JSON)
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
	@RequestMapping(value = "/{baseSiteId}/getFeedbackCategory", method = RequestMethod.GET, produces = APPLICATION_JSON)
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

	@RequestMapping(value = "/{baseSiteId}/askAQuestion", method = RequestMethod.GET, produces = APPLICATION_JSON)
	@ResponseBody
	public UserResultWsDto askquestion(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam final String emailId, @RequestParam final String question, @RequestParam final String productCode)
	{
		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		try
		{
			final LuxuryPdpQuestionEvent eventObj = new LuxuryPdpQuestionEvent();
			eventObj.setCustomerEmailId(emailId);
			eventObj.setEmailTo("customerservices@tataunistore.com");
			eventObj.setMessage(question);
			eventObj.setSite(baseSiteService.getCurrentBaseSite());
			eventObj.setProductCode(productCode);
			eventservice.publishEvent(eventObj);
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final Exception ex)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			LOG.error("Exception occured while submitting question::::" + ex.getMessage());
		}

		return userResultWsDto;
	}

	//LW-176 starts
	@RequestMapping(value = "/{baseSiteId}/{emailId}/newsletter", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public NewsletterWsDTO getNewsletter(@PathVariable String emailId)
	{
		final NewsletterWsDTO news = new NewsletterWsDTO();
		final MplNewsLetterSubscriptionModel newsLetter = modelService.create(MplNewsLetterSubscriptionModel.class);
		List<MplNewsLetterSubscriptionModel> newsLetterSubscriptionList = new ArrayList<MplNewsLetterSubscriptionModel>();

		emailId = emailId.toLowerCase();

		if (!validateEmailAddress(emailId))
		{
			news.setSuccess("mailFormatError");
		}

		else
		{

			final boolean result = brandFacade.checkEmailId(emailId);

			if (result)
			{
				newsLetter.setEmailId(emailId);
				newsLetter.setIsLuxury(Boolean.TRUE);
				modelService.save(newsLetter);
				news.setSuccess("success");
			}

			else
			{
				newsLetterSubscriptionList = brandFacade.checkEmailIdForluxury(emailId);

				if (null != newsLetterSubscriptionList && !newsLetterSubscriptionList.isEmpty())
				{
					for (final MplNewsLetterSubscriptionModel mplNewsLetterSubscriptionModel : newsLetterSubscriptionList)
					{
						if ((mplNewsLetterSubscriptionModel.getEmailId().equalsIgnoreCase(emailId))
								&& (!(mplNewsLetterSubscriptionModel.getIsLuxury().booleanValue()) || mplNewsLetterSubscriptionModel
										.getIsLuxury() == null))
						{
							mplNewsLetterSubscriptionModel.setIsLuxury(Boolean.TRUE);
							modelService.save(mplNewsLetterSubscriptionModel);
							news.setSuccess("success");
						}
					}

				}
				else
				{
					news.setSuccess("subscribed");
				}
			}
		}
		return news;
	}

	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}//LW-176 ends



	/**
	 * Is Pincode Exchange Serviceable
	 *
	 * @param pincode
	 * @param l3code
	 * @return reverseCheckdata
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = "/{baseSiteId}/reversePincodeCheck/{pincode}", method = RequestMethod.POST)
	@ResponseBody
	public ReversePincodeExchangeData reversePincodeCheck(@PathVariable final String pincode,
			@RequestParam(required = false) final String l3code) throws CMSItemNotFoundException
	{

		final ReversePincodeExchangeData reverseCheckdata = new ReversePincodeExchangeData();
		final boolean isServicable = exchangeGuideFacade.isBackwardServiceble(pincode);
		reverseCheckdata.setPincodeResponse(exchangeGuideFacade.isBackwardServiceble(pincode));
		if (isServicable && StringUtils.isNotEmpty(l3code))
		{
			reverseCheckdata.setPriceMatrix(exchangeGuideFacade.getExchangeGuide(l3code));
		}


		return reverseCheckdata;
	}

	/*
	 * to receive pancard status from SP for jewellery
	 * 
	 * @param restrictionXML
	 * 
	 * @return void
	 */
	@RequestMapping(value = "/{baseSiteId}/miscs/pancardStatus", method = RequestMethod.POST)
	@ResponseBody
	public void pancardStatusFromSP(final InputStream panStatusXML) throws RequestParameterException, JAXBException
	{
		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(PanCardResDTO.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final PanCardResDTO resDTO = (PanCardResDTO) jaxbUnmarshaller.unmarshal(panStatusXML);

			mplPancardFacade.setPancardRes(resDTO);
		}
		catch (final RequestParameterException e)
		{
			LOG.error("the exception is **** " + e);
		}
		catch (final JAXBException e)
		{
			LOG.error("the exception is **** " + e);
		}
		catch (final Exception e)
		{
			LOG.error("the exception is **** " + e);
		}
	}

	//TPR-4840 starts
	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{baseSiteId}/orderCustDetailsByOrderId", method = RequestMethod.POST, produces = APPLICATION_JSON)
	@ResponseBody
	public OrderInfoWsDTO fetchCustomerOrderInfoByOrderId(
			@RequestParam(value = "orderRefNo", required = true) final String orderRefNo) throws EtailNonBusinessExceptions
	{
		OrderInfoWsDTO orderInfoWsDTO = new OrderInfoWsDTO();
		OrderModel orderModel = null;
		try
		{
			if (StringUtils.isNotEmpty(orderRefNo))
			{
				//Fetching parent order model by order id
				orderModel = mplOrderFacade.getOrderByParentOrderNo(orderRefNo);
				orderInfoWsDTO = mplOrderFacade.storeOrderInfoByOrderNo(orderModel);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured", e);
			orderInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return orderInfoWsDTO;
	}

	//TPR-4840 ends

	//TPR-4841 starts
	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{baseSiteId}/orderCustDetailsByTransactionId", method = RequestMethod.POST, produces = APPLICATION_JSON)
	@ResponseBody
	public OrderInfoWsDTO fetchCustomerOrderInfoByTransactionId(
			@RequestParam(value = "transactionId", required = true) final String transactionId) throws EtailNonBusinessExceptions
	{

		OrderInfoWsDTO orderInformationWsDTO = new OrderInfoWsDTO();
		OrderModel orderModel = null;

		try
		{
			if (StringUtils.isNotEmpty(transactionId))
			{
				//Fetching sub order model by transaction id
				orderModel = mplOrderFacade.fetchOrderInfoByTransactionId(transactionId);
				orderInformationWsDTO = mplOrderFacade.storeOrderInfoByTransactionId(orderModel, transactionId);
			}

		}
		catch (final Exception e)
		{
			LOG.error("Exception occured", e);
			orderInformationWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return orderInformationWsDTO;

	}

	//TPR-4841 ends

	//TPR-5225 starts
	@Secured(
	{ ROLE_CLIENT, ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{baseSiteId}/orderCustDetailsByMobileNo", method = RequestMethod.POST, produces = APPLICATION_JSON)
	@ResponseBody
	public OrderInfoWsDTO fetchCustomerOrderInfoByMobileNo(@RequestParam(value = "mobileNo", required = true) final String mobileNo)
			throws EtailNonBusinessExceptions
	{

		final int countLimit = Integer.parseInt(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.TRANSACTION_NO_KEY));

		//SDI-1193
		final int transactionLimit = Integer.parseInt(getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.TRANSACTION_LIMIT_BY_DATE));

		LOG.debug("**Transaction count Limit**" + countLimit);
		LOG.debug("**Transaction Limit by date**" + transactionLimit);
		OrderInfoWsDTO orderInformationWsDTO = new OrderInfoWsDTO();
		List<OrderModel> orderModels = new ArrayList<OrderModel>();
		try
		{

			if (StringUtils.isNotEmpty(mobileNo))
			{
				//Fetching parent order models by mobile no
				orderModels = mplOrderFacade.getOrderWithMobileNo(mobileNo, countLimit, transactionLimit);
				orderInformationWsDTO = mplOrderFacade.storeOrderInfoByMobileNo(orderModels, countLimit);
			}

		}
		catch (final Exception e)
		{
			LOG.error("Exception occured", e);
			orderInformationWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return orderInformationWsDTO;
	}

	//TPR-5225 ends


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

	//TPR-5225 ends

	/**
	 * Method: One touch Cancel and return--TPR-1345
	 *
	 * @param crmRequestXML
	 *           the input XML request from CRM
	 * @return XML , the output XML response to CRM
	 */
	@RequestMapping(value = "/{baseSiteId}/oneTouch", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_XML_VALUE }, produces =
	{ MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public Object oneTouchCancelReturn(final InputStream crmRequestXML)
	{



		LOG.info("==========Inside oneTouchCancelReturn controller==========");
		//instances & variables
		OneTouchCancelReturnDTO output = null;
		final Set<OneTouchCancelReturnDTO> outputList = new HashSet<OneTouchCancelReturnDTO>();
		final ResponseMaster oneTouchReturnDTOList = new ResponseMaster();
		boolean serviceabilty = true;
		boolean resultFlag = false;
		final String ussid = null;
		OrderEntryData orderEntry = new OrderEntryData();
		OneTouchCancelReturnCrmRequestList crmReqObj = null;
		String consignmentStatus = null;
		String transactionId = null;
		CODSelfShipData codSelfShipData = null;
		//New addition
		final List<String> checkList = new ArrayList<String>();
		final List<String> masterCheckList = new ArrayList<String>();
		String delayValue = "0";
		long delay = 0;
		List<AbstractOrderEntryModel> orderEntriesModel = null;
		Map<String, String> dataMap = null;//Added for TPR-5954
		StringBuilder imgUrl = null;//Added for TPR-5954
		final String subQuery = null;//Added for TPR-5954
		try
		{
			//Converting XML to JAVA Object
			final JAXBContext jaxbContext = JAXBContext.newInstance(OneTouchCancelReturnCrmRequestList.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			crmReqObj = (OneTouchCancelReturnCrmRequestList) jaxbUnmarshaller.unmarshal(crmRequestXML);
			final String delayOnOff = configurationService.getConfiguration().getString("onetouch.time.delay.onOff");
			LOG.debug("===========Thread on off switch:==========" + delayOnOff);
			if ("Y".equalsIgnoreCase(delayOnOff))
			{
				delayValue = configurationService.getConfiguration().getString("onetouch.time.delay");

				if (StringUtils.isNotEmpty(delayValue) && null != delayValue)
				{
					delay = Long.parseLong(delayValue);
				}
			}
			//Iterating over each object
			outer: for (final OneTouchCancelReturnCrmRequestDTO oneTouchCrmObj : crmReqObj.getOneTouchCancelReturnRequestDTOlist())
			{
				codSelfShipData = null;
				consignmentStatus = null;
				output = new OneTouchCancelReturnDTO();
				//Mandatory fields validation
				if (StringUtils.isNotEmpty(oneTouchCrmObj.getOrderRefNum())
						&& StringUtils.isNotEmpty(oneTouchCrmObj.getSubOrderNum()) && StringUtils.isNotEmpty(oneTouchCrmObj.getUSSID())
						&& StringUtils.isNotEmpty(oneTouchCrmObj.getTicketType())
						&& StringUtils.isNotEmpty(oneTouchCrmObj.getTransactionId()))
				{
					transactionId = oneTouchCrmObj.getTransactionId();
					if (LOG.isDebugEnabled())
					{
						LOG.debug("===========transaction id:==========" + oneTouchCrmObj.getTransactionId());
						LOG.debug("===========sub order id:============" + oneTouchCrmObj.getSubOrderNum());
					}
					try
					{
						//TPR-5954 || Start
						dataMap = new HashMap<String, String>();
						dataMap.put("comments", oneTouchCrmObj.getComments());
						dataMap.put("subreasoncode", oneTouchCrmObj.getSubReturnReasonCode());
						imgUrl = new StringBuilder();
						/*
						 * if (null != oneTouchCrmObj.getUploadImage()) { for (final String up :
						 * oneTouchCrmObj.getUploadImage()) { imgUrl.append(up); imgUrl.append(","); } subQuery =
						 * imgUrl.substring(0, imgUrl.length() - 1); dataMap.put("imgurl", subQuery);
						 * imgUrl.setLength(0);//Emptying the image path string }
						 */
						//TPR-5954 || End
						final OrderModel subOrderModel = orderModelService.getOrder(oneTouchCrmObj.getSubOrderNum());//Sub order model
						final OrderData orderData = getOrderConverter().convert(subOrderModel); //model converted to data
						orderEntriesModel = cancelReturnFacade.associatedEntries(subOrderModel, oneTouchCrmObj.getTransactionId());//associated order entries
						if (!masterCheckList.contains(oneTouchCrmObj.getTransactionId()))
						{
							for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
							{
								masterCheckList.add(abstractOrderEntryModel.getTransactionID());
							}
						}
						else
						{
							continue outer;
						}
						boolean deliveryCheckFlag = false;
						if (oneTouchCrmObj.getTicketType().equalsIgnoreCase(MarketplacewebservicesConstants.RETURN_TICKET))
						{
							if (!checkList.contains(oneTouchCrmObj.getTransactionId()))
							{
								//Pincode serviceablity check for RSP tickets
								if (oneTouchCrmObj.getTicketSubType().equalsIgnoreCase(MarketplacewebservicesConstants.TICKET_TYPE_RSP))
								{
									serviceabilty = cancelReturnFacade.oneTouchPincodeCheck(orderData, oneTouchCrmObj.getPincode(),
											oneTouchCrmObj.getTransactionId());
									LOG.debug("========Pincode serviceablity check result is=========" + serviceabilty);
								}
								//Checking the promotion Buy A and B get C free
								final boolean isBuyAandBgetC = cancelReturnFacade.appliedPromotionCheckOnetouch(subOrderModel);
								if (isBuyAandBgetC)
								{
									LOG.debug("===Inside BuyAandBgetC check====");
									boolean deliverymodeValidator = true;
									checkloop: for (final AbstractOrderEntryModel orderEntry1 : orderEntriesModel)
									{
										LOG.debug("===Inside checkloop check====");
										checkList.add(orderEntry1.getTransactionID());
										deliveryCheckFlag = true;
										for (final ConsignmentEntryModel con : orderEntry1.getConsignmentEntries())
										{
											LOG.debug("===Inside ConsignmentEntryModel loop===="
													+ con.getConsignment().getStatus().getCode());
											if (orderEntry1.getMplDeliveryMode().getDeliveryMode().getCode()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_AND_COLLECT))
											{
												LOG.debug("===Inside click and collect check loop====");
												deliverymodeValidator = false;
											}

											if (deliverymodeValidator)
											{
												if (!(con.getConsignment().getStatus().getCode())
														.equalsIgnoreCase(MarketplacewebservicesConstants.DELIVERED_STATUS.toString()))
												{
													LOG.debug("===Inside delivered loop====");
													deliveryCheckFlag = false;
													break checkloop;
												}
											}
											else if (!deliverymodeValidator)
											{
												if (!(con.getConsignment().getStatus().getCode())
														.equalsIgnoreCase(MarketplacewebservicesConstants.ORDER_COLLECTED_STATUS.toString()))
												{
													LOG.debug("===Inside order collected loop====");
													deliveryCheckFlag = false;
													break checkloop;
												}
											}
										}
									}
									if (!deliveryCheckFlag)
									{
										LOG.debug("===Inside deliveryCheckFlag loop====");
										for (final AbstractOrderEntryModel orderEntry2 : orderEntriesModel)
										{
											output = new OneTouchCancelReturnDTO();
											for (final ConsignmentEntryModel con : orderEntry2.getConsignmentEntries())
											{
												if ((con.getConsignment().getStatus().getCode()).equalsIgnoreCase("RETURN_INITIATED"))
												{
													output = populateResponseDateForCRM(oneTouchCrmObj.getOrderRefNum(),
															orderEntry2.getTransactionID(), serviceabilty,
															MarketplacewebservicesConstants.VALID_FLAG_S, "RETINIT_CSCP_S", true);
													outputList.add(output);
												}
												else
												{
													output = populateResponseDateForCRM(oneTouchCrmObj.getOrderRefNum(),
															orderEntry2.getTransactionID(), serviceabilty,
															MarketplacewebservicesConstants.VALID_FLAG_F, "RETINIT_CSCP_F", true);
													outputList.add(output);
												}
											}
										}
										continue outer;
									}
								}
							}
						}
						LOG.debug("========Fetching order entry details for transaction id========" + oneTouchCrmObj.getTransactionId());
						for (final OrderEntryData entry : orderData.getEntries())
						{
							if (null != entry.getTransactionId())
							{
								if (entry.getTransactionId().equalsIgnoreCase(transactionId))
								{
									orderEntry = entry;
									if (orderEntry.isGiveAway() || orderEntry.isIsBOGOapplied())

									{
										continue outer;
									}
									break;
								}
							}
							else
							{
								LOG.debug("===Inside blank transaction id loop====");
								output = populateResponseDateForCRM(oneTouchCrmObj.getOrderRefNum(), null, serviceabilty,
										MarketplacewebservicesConstants.VALID_FLAG_F, "BLK_TXN", false);
								outputList.add(output);
								continue outer;
							}
						}
						LOG.debug("========Fetching consignment details for order entry=========" + oneTouchCrmObj.getTransactionId());
						//FETCHING ORDER CONSIGNMENT STATUS
						if (null != orderEntry.getConsignment() && null != orderEntry.getConsignment().getStatus())
						{
							consignmentStatus = orderEntry.getConsignment().getStatus().getCode();
						}
						else
						{
							output = populateResponseDateForCRM(oneTouchCrmObj.getOrderRefNum(), oneTouchCrmObj.getTransactionId(),
									serviceabilty, MarketplacewebservicesConstants.VALID_FLAG_F, "BLK_CNSGNMNT", false);
							outputList.add(output);
							continue;
						}
						//For cancel
						if (oneTouchCrmObj.getTicketType().equalsIgnoreCase(MarketplacewebservicesConstants.CANCEL_TICKET))
						{
							LOG.debug("========Initiating cancellation of consignment=========");
							//----------IF CONSIGNMENT IS ALREADY CANCELLED---------
							if (!getMplOrderFacade().checkCancelStatus(consignmentStatus,
									MarketplacewebservicesConstants.CANCEL_ORDER_STATUS)
									&& getMplOrderFacade().checkCancelStatus(consignmentStatus,
											MarketplacewebservicesConstants.CANCEL_ELIGIBLE_STATUS))
							{
								resultFlag = cancelReturnFacade.oneTouchCancel(subOrderModel, orderData, orderEntry,
										oneTouchCrmObj.getCancelReasonCode(), ussid, oneTouchCrmObj.getTicketType(),
										oneTouchCrmObj.getRefundType(), false, SalesApplication.CALLCENTER, orderEntriesModel);
								for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
								{
									output = new OneTouchCancelReturnDTO();
									//Successful cancellation
									if (resultFlag)
									{
										output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
										output.setTransactionId(abstractOrderEntryModel.getTransactionID());
										output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_S);
										outputList.add(output);
									}
									//Failed cancellation
									else
									{
										output.setTransactionId(abstractOrderEntryModel.getTransactionID());
										output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
										output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_F);
										output.setRemarks(MarketplacewebservicesConstants.ERROR_IN_OMS);
										outputList.add(output);
									}
								}
							}
							//---------IF CONSIGNMENT IS ALREADY CANCELLED-----------
							else
							{
								for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
								{
									output = populateResponseDateForCRM(oneTouchCrmObj.getOrderRefNum(),
											abstractOrderEntryModel.getTransactionID(), serviceabilty,
											MarketplacewebservicesConstants.VALID_FLAG_F, "CNCL_INIT", false);
									outputList.add(output);
								}
							}
						}
						//For Return
						else if (oneTouchCrmObj.getTicketType().equalsIgnoreCase(MarketplacewebservicesConstants.RETURN_TICKET))
						{
							boolean FICO = false;
							//-----------IF CONSIGNMENT IS ALREADY RETURNED--------
							if (!getMplOrderFacade().checkCancelStatus(consignmentStatus,
									MarketplacewebservicesConstants.RETURN_ORDER_STATUS)
									&& (consignmentStatus.equalsIgnoreCase(MarketplacewebservicesConstants.DELIVERED_STATUS.toString()) || consignmentStatus
											.equalsIgnoreCase(MarketplacewebservicesConstants.ORDER_COLLECTED_STATUS.toString())))
							{
								//******INITITATING RETURN********
								if (serviceabilty)
								{
									LOG.debug("========Initiating Return of consignment=========");
									resultFlag = cancelReturnFacade.oneTouchReturn(orderData, orderEntry,
											oneTouchCrmObj.getReturnReasonCode(), oneTouchCrmObj.getTicketType(),
											SalesApplication.CALLCENTER, oneTouchCrmObj.getPincode(), orderEntriesModel, subOrderModel,
											codSelfShipData, oneTouchCrmObj.getUSSID(), oneTouchCrmObj.getTransactionId(), dataMap);
									//Return is successfull
									for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
									{
										output = new OneTouchCancelReturnDTO();
										if (resultFlag)
										{
											output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
											output.setTransactionId(abstractOrderEntryModel.getTransactionID());
											output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_S);
											output.setServiceability(MarketplacewebservicesConstants.VALID_FLAG_S);

											//TPR-6389 :: NEFT details have to be passed on to FICO after one touch return
											LOG.debug("--------Populating bank details for return of COD orders--------");
											if (subOrderModel.getModeOfOrderPayment().equalsIgnoreCase(MarketplacewebservicesConstants.COD)
													&& null != oneTouchCrmObj.getAccNum())
											{
												LOG.debug("Step 1");
												codSelfShipData = null;
												codSelfShipData = populateCODDataForFICO(subOrderModel, oneTouchCrmObj, orderData,
														abstractOrderEntryModel);
												LOG.debug("Step 2");
												if (null != codSelfShipData)
												{
													LOG.debug("Sending bank details to FICO start.....");
													FICO = sendBankDetailsToFICO(codSelfShipData);
												}
												if (FICO)
												{
													output.setRemarks("Bank details successfully sent to FICO");
												}
												else
												{
													output.setRemarks("Failed to send Bank details to FICO");
													LOG.debug("Failed to send Bank details to FICO");
												}
											}
											//TPR-6389--END

											outputList.add(output);
										}
										//Return is failure
										else
										{
											output.setTransactionId(abstractOrderEntryModel.getTransactionID());
											output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
											output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_F);
											output.setRemarks(MarketplacewebservicesConstants.ERROR_IN_OMS);
											outputList.add(output);
										}
									}
								}
								//Pincode is not serviceable
								else
								{
									for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
									{
										output = new OneTouchCancelReturnDTO();
										output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
										output.setTransactionId(abstractOrderEntryModel.getTransactionID());
										output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_F);
										output.setServiceability(MarketplacewebservicesConstants.VALID_FLAG_F);
										output.setRemarks(MarketplacewebservicesConstants.PINCODE_NOT_SERVICEABLE);
										outputList.add(output);
									}
								}
							}
							//---------IF CONSIGNMENT IS ALREADY RETURNED--------
							else
							{

								for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
								{
									output = new OneTouchCancelReturnDTO();
									///new addition to handle CS cockpit issues
									if (consignmentStatus.equalsIgnoreCase("RETURN_INITIATED"))
									{
										output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
										output.setTransactionId(abstractOrderEntryModel.getTransactionID());
										output.setServiceability(serviceabilty ? "S" : "F");
										//output.setServiceability(MarketplacewebservicesConstants.VALID_FLAG_S);
										output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_S);
										output.setRemarks(serviceabilty ? MarketplacewebservicesConstants.RETURN_ALREADY_INITIATED_CSCP
												: MarketplacewebservicesConstants.PINCODE_NOT_SERVICEABLE);
										outputList.add(output);
									}
									else
									{
										output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
										output.setTransactionId(abstractOrderEntryModel.getTransactionID());
										output.setServiceability(serviceabilty ? "S" : "F");
										output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_F);
										output.setRemarks(MarketplacewebservicesConstants.RETURN_ALREADY_INITIATED);
										outputList.add(output);
									}

								}
							}
						}
					}
					catch (final Exception e)
					{
						LOG.error(e.getMessage());
						//Failure response
						output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
						output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_F);
						output.setTransactionId(oneTouchCrmObj.getTransactionId());
						output.setRemarks(e.getMessage());
						outputList.add(output);
					}
				}
				else
				{
					output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
					output.setTransactionId(oneTouchCrmObj.getTransactionId());
					output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_F);
					output.setRemarks(MarketplacewebservicesConstants.MISSING_MANDATORY_FIELDS);
					outputList.add(output);
				}
				if ("Y".equalsIgnoreCase(delayOnOff))
				{
					Thread.sleep(delay); // do nothing for 1000 miliseconds (1 second)
				}
			}
			LOG.debug("========Sending response in XML format=========");
			//Automatic conversion of JAVA object to XML
			oneTouchReturnDTOList.setOneTouchList(new ArrayList<OneTouchCancelReturnDTO>(outputList));
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			//output.setOrderRefNum(oneTouchCrmObj.getOrderRefNum());
			output.setValidFlag(MarketplacewebservicesConstants.VALID_FLAG_F);
			output.setRemarks(MarketplacewebservicesConstants.FORMAT_MISMATCH);
			outputList.add(output);
			dataMap = null;
		}
		LOG.info("==========Finished executing oneTouchCancelReturn controller==========");
		dataMap = null;
		return oneTouchReturnDTOList;
	}

	/**
	 * Method to populate bank details that will sent to FICO for COD return orders.
	 */
	private CODSelfShipData populateCODDataForFICO(final OrderModel subOrderModel,
			final OneTouchCancelReturnCrmRequestDTO oneTouchdto, final OrderData orderData, final AbstractOrderEntryModel orderEntry)
	{
		LOG.debug("Inside populateCODDataForFICO method.....");
		CODSelfShipData codSelfShipData = new CODSelfShipData();
		try
		{
			final String title = getConfigurationService().getConfiguration()
					.getString(oneTouchdto.getTitle().toString() + "_title");
			if (null != subOrderModel.getUser().getUid())
			{
				codSelfShipData.setCustomerNumber(subOrderModel.getUser().getUid());
			}
			codSelfShipData.setTitle(title.toUpperCase());
			codSelfShipData.setOrderRefNo(oneTouchdto.getOrderRefNum());
			codSelfShipData.setOrderNo(oneTouchdto.getSubOrderNum());
			codSelfShipData.setBankName(oneTouchdto.getBankName());
			codSelfShipData.setBankBranch(oneTouchdto.getBranch());
			codSelfShipData.setName(oneTouchdto.getAccHolderName());
			codSelfShipData.setBankKey(oneTouchdto.getIFSC());
			codSelfShipData.setBankAccount(oneTouchdto.getAccNum());
			codSelfShipData.setTransactionID(orderEntry.getTransactionID());
			codSelfShipData.setTransactionType(subOrderModel.getModeOfOrderPayment());
			codSelfShipData.setOrderTag(MarketplacewebservicesConstants.ORDERTAG_TYPE_POSTPAID);
			codSelfShipData.setPaymentMode("N");
			codSelfShipData.setAmount(orderEntry.getNetAmountAfterAllDisc().toString());
			codSelfShipData.setTransactionType(RETURN_TYPE_COD);
			if (null != orderData.getCreated())
			{
				final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				codSelfShipData.setOrderDate(dateUtilHelper.convertDateWithFormat(formatter.format(orderData.getCreated())));
				codSelfShipData.setTransactionDate(dateUtilHelper.convertDateWithFormat(formatter.format(orderData.getCreated())));
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
			codSelfShipData = null;
			return codSelfShipData;
		}
		LOG.debug("finished executing populateCODDataForFICO method.....");
		return codSelfShipData;
	}

	//TPR-6389 :: NEFT details have to be passed on to FICO after a one touch return
	private boolean sendBankDetailsToFICO(final CODSelfShipData codSelfShipData)
	{
		LOG.info("Starting sending bank details to FICO");
		try
		{
			if (null != codSelfShipData)
			{
				final CODSelfShipResponseData codSelfShipResponseData = cancelReturnFacade.codPaymentInfoToFICO(codSelfShipData);
				if (null == codSelfShipResponseData.getSuccess() || !codSelfShipResponseData.getSuccess().equalsIgnoreCase(SUCCESS))
				{
					cancelReturnFacade.saveCODReturnsBankDetails(codSelfShipData);
					LOG.debug("Failed to post COD return paymnet details to FICO Order No:" + codSelfShipData.getOrderRefNo());

				}
				cancelReturnFacade.insertUpdateCustomerBankDetails(codSelfShipData);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex);
			return false;
		}
		LOG.info("Finished sending bank details to FICO");
		return true;
		//TPR-6389 :: END
	}

	//populate response data for CRM response
	private OneTouchCancelReturnDTO populateResponseDateForCRM(final String orderNum, final String txnId,
			final boolean serviceabilty, final String validFlag, final String msgCategory, final boolean returnCancelFlag)
	{
		final OneTouchCancelReturnDTO output = new OneTouchCancelReturnDTO();
		output.setOrderRefNum(orderNum);
		if (null != txnId)
		{
			output.setTransactionId(txnId);
		}
		if (returnCancelFlag)
		{
			output.setServiceability(serviceabilty ? "S" : "F");
		}
		output.setValidFlag(validFlag);

		switch (msgCategory)
		{
			case "RETINIT_CSCP_S":
				output.setRemarks(serviceabilty ? MarketplacewebservicesConstants.RETURN_ALREADY_INITIATED_CSCP
						: MarketplacewebservicesConstants.PINCODE_NOT_SERVICEABLE);

			case "RETINIT_CSCP_F":
				output.setRemarks(serviceabilty ? "All the products in promotion are not in delivered status"
						: MarketplacewebservicesConstants.PINCODE_NOT_SERVICEABLE);

			case "BLK_TXN":
				output.setRemarks("Transaction ID is not found in commerce system");

			case "BLK_CNSGNMNT":
				output.setRemarks("No consignment found");

			case "CNCL_INIT":
				output.setRemarks("Order is not eligible for cancellation in commerce system");
			default:
		}
		return output;
	}

	/**
	 * @return the orderConverter
	 */
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter
	 *           the orderConverter to set
	 */
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	/**
	 * @return the mplOrderFacade
	 */
	public MplOrderFacade getMplOrderFacade()
	{
		return mplOrderFacade;
	}

	/**
	 * @param mplOrderFacade
	 *           the mplOrderFacade to set
	 */
	public void setMplOrderFacade(final MplOrderFacade mplOrderFacade)
	{
		this.mplOrderFacade = mplOrderFacade;
	}

	/**
	 * This method is for web form ticket status update (TPR-5989)
	 */
	@RequestMapping(value = "/{baseSiteId}/webformTicketStatus", method = RequestMethod.POST, consumes = APPLICATION_JSON)
	@ResponseBody
	public String webformTicketStatusUpdate(@RequestBody final TicketStatusUpdate ticketStatusUpdate)
			throws EtailNonBusinessExceptions
	{
		try
		{
			mplWebFormFacade.webFormticketStatusUpdate(ticketStatusUpdate);
			return SUCCESS;
		}
		catch (final Exception exc)
		{
			LOG.error(exc);
			return FAILURE;
		}

	}

	/**
	 * @param emailId
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 */


	@RequestMapping(value = "/getWebCRMNodes", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public CRMWsDataParent getcemwebform(@PathVariable final String emailId) throws RequestParameterException,
			WebserviceValidationException, MalformedURLException
	{
		final CRMWsDataParent crmDto = new CRMWsDataParent();


		try
		{

			final List<CRMWsData> crmdata = mplWebFormFacade.getAllWebCRMTreedata();
			crmDto.setNodes(crmdata);
			crmDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);



		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				crmDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				crmDto.setErrorCode(e.getErrorCode());
			}
			crmDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				crmDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				crmDto.setErrorCode(e.getErrorCode());
			}
			crmDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return crmDto;
	}




	@RequestMapping(value = "/crmFileUpload", method = RequestMethod.POST)
	@ResponseBody
	public FileUploadResponseData getcrmFileUploadRequest(@RequestParam("file") final MultipartFile multipartFile)
			throws WebserviceValidationException
	{
		final FileUploadResponseData crmFileUploaddata = new FileUploadResponseData();
		String finalUrlForDispatchProof = null;

		try
		{

			LOG.debug("***************:" + multipartFile.getOriginalFilename());
			String fileUploadLocation = null;

			//String finalUrlForDispatchProof = null;
			//TISRLUAT-50
			if (null != configurationService)
			{
				fileUploadLocation = configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.CRM_FILE_UPLOAD_PATH);

				if (null != fileUploadLocation && !fileUploadLocation.isEmpty())
				{
					try
					{
						final byte barr[] = multipartFile.getBytes();
						finalUrlForDispatchProof = getPoDUploadPath(fileUploadLocation, multipartFile.getOriginalFilename()); //PRDI-151
						final BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(finalUrlForDispatchProof));
						bout.write(barr);
						bout.flush();
						bout.close();
						LOG.info("Txn ID: " + " >> Uploaded Proof of dispatch: " + finalUrlForDispatchProof);
					}
					catch (final Exception e)
					{
						LOG.error("Failed to upload PoD. Txnid: " + " -- Path: " + finalUrlForDispatchProof + " -- Exception: " + e);
					}

					crmFileUploaddata.setFileURL(finalUrlForDispatchProof);
					crmFileUploaddata.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				else
				{
					LOG.error("Failed to upload Proof of dispatch. POD File Upload location is not configured: "
							+ MarketplacecommerceservicesConstants.FILE_UPLOAD_PATH);
				}
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				crmFileUploaddata.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				crmFileUploaddata.setErrorCode(e.getErrorCode());
			}
			crmFileUploaddata.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				crmFileUploaddata.setError(e.getMessage());
			}
			crmFileUploaddata.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}





		return crmFileUploaddata;
	}

	/**
	 * @param fileUploadLocation
	 * @param originalFilename
	 * @return
	 */
	private String getPoDUploadPath(final String fileUploadLocation, final String originalFilename)
	{
		String date = null;
		Path path = null;
		final StringBuffer buffer = new StringBuffer();
		final SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
		try
		{
			date = sdf.format(new Date());
			path = Paths.get(fileUploadLocation + File.separator + date);
			//if directory exists?
			//"fileUploadLocation", transactionId and fileName cannot be null or blank.
			if (!Files.exists(path))
			{
				try
				{
					Files.createDirectories(path);
				}
				catch (final IOException e)
				{
					//fail to create directory
					LOG.error("Exception, while creating the Directory: " + path + " -- " + e);
				}
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception, While calculating the upload path: " + ex);
		}
		return buffer.append(path).append(File.separator).toString();
	}


}

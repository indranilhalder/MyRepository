package com.tealium.dataconnector.hybris;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Pattern;
import java.net.URL;
import java.security.Timestamp;
import java.util.Date;
import java.net.URL;

import com.tealium.util.udohelpers.TealiumHelper;
import com.tealium.util.udohelpers.TealiumHelper.UDOOptions;
import com.tealium.util.udohelpers.TealiumHelper.PrebuiltUDOPageTypes;
import com.tealium.util.udohelpers.UDO;
import com.tealium.util.udohelpers.exceptions.UDODefinitionException;
import com.tealium.util.udohelpers.exceptions.UDOUpdateException;
import com.tealium.addon.constants.TealiumIQWebConstants;
import com.tealium.addon.jalo.TealiumIQManager;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;

import javax.servlet.http.HttpSession;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.apache.log4j.Logger;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import org.apache.commons.lang.StringUtils;


public final class HybrisDataConverter
{


	private static UDO setupUDO(TealiumHelper tealiumHelper, PrebuiltUDOPageTypes pageType) throws UDODefinitionException,
			UDOUpdateException
	{
		tealiumHelper.assumePageTypeUDO("global").mayHaveStringFields(
				EnumSet.of(UDOOptions.WRITE_IF_EMPTY_OR_NULL, UDOOptions.REQUIRED), "page_name", "site_currency", "site_region");

		HttpServletRequest request = getRequest();
		UDO udo = tealiumHelper.createDefaultUDO(pageType);
		udo.getPageType().includesFieldsFromPageType("global");

		CurrencyData currencyData = (CurrencyData) request.getAttribute("currentCurrency");
		LanguageData languageData = (LanguageData) request.getAttribute("currentLanguage");
		List<Breadcrumb> breadcrumbs = new ArrayList();
		breadcrumbs = (List<Breadcrumb>) request.getAttribute("breadcrumbs");

		if (breadcrumbs != null)
		{
			udo.setValue("site_section", breadcrumbs.get(0).getName());
		}
		final Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (final Cookie cookie : cookies)
			{



				if (cookie.getName().equals("mpl-user"))
				{

					udo.setValue("user_id", cookie.getValue());
					//model.addAttribute("mpl-user", cookie.getValue());



				}
				if (cookie.getName().equals("mpl-userType"))
				{

					udo.setValue("user_type", cookie.getValue());
					//model.addAttribute("mpl-userType", cookie.getValue());



				}
			}
		}
		//Added 
		final HttpSession session = request.getSession();
		final String categoryId = (String) request.getAttribute("categoryCode");
		if (categoryId != null)
		{
			udo.setValue("categoryId", categoryId);
		}
		final String domainName = request.getServerName();
		if (domainName != null)
		{
			udo.setValue("IA_company", domainName);
		}
		if (session != null)
		{
			final String sessionId = session.getId();
			if (sessionId.contains("."))
			{
				final String[] parts = sessionId.split("\\.");
				udo.setValue("session_id", parts[0]);
			}
			else
			{
				udo.setValue("session_id", sessionId);
			}
		}
		//gets the visitor ip
		if (getVisitorIpAddress(request) != null)
		{
			udo.setValue("visitor_ip", getVisitorIpAddress(request));
		}

		CustomerData customerData = (CustomerData) request.getAttribute("customerData");
		if (customerData != null)
		{
			if (customerData.getUid() != null)
			{
				udo.setValue("user_id", customerData.getUid());
			}
		}
		if (session.getAttribute("socialLogin") != null)
		{
			udo.setValue("user_type", (String) session.getAttribute("socialLogin"));
		}
		if (currencyData.getIsocode() != null)
		{
			String siteCurrency = currencyData.getIsocode();
			udo.setValue("site_currency", siteCurrency);
		}
		if (languageData.getIsocode() != null)
		{
			String siteLanguage = languageData.getIsocode();
			udo.setValue("site_region", siteLanguage);
		}

		String pageNameString;
		String pageTypeString = (String) request.getAttribute("pageType");
		/*
		 * if (pageTypeString != null) { pageNameString = (pageTypeString).toLowerCase(); } else { pageNameString =
		 * (((ContentPageModel) request.getAttribute("cmsPage")).getLabel()).toLowerCase(); }
		 */

		final AbstractPageModel page = ((AbstractPageModel) request.getAttribute("cmsPage"));
		if (page.getName() != null)
		{
			pageNameString = page.getName();
		}
		else
		{
			pageNameString = pageTypeString;
		}


		if (pageTypeString != null)
		{

			if (pageTypeString.equalsIgnoreCase("productsearch") || pageTypeString.equalsIgnoreCase("product")
					|| pageTypeString.equalsIgnoreCase("category"))
			{
				if (getBreadcrumbsForAPage(request, udo) != null)
				{
					pageNameString = pageNameString + ":" + getBreadcrumbsForAPage(request, udo);

				}
			}

		}

		if (request.getAttribute("checkoutPageName") != null)
		{
			pageNameString = pageNameString + ":" + request.getAttribute("checkoutPageName");
		}
		
		if(pageNameString!=null && pageNameString.equalsIgnoreCase("Multi Checkout Summary Page") && !pageNameString.contains(":")){
			pageNameString = pageNameString + ": Select Address";
		}
		
		
		udo.setValue("page_name", pageNameString);
		return udo;
	}

	private static String getBreadcrumbsForAPage(final HttpServletRequest request, UDO udo) throws UDODefinitionException,
			UDOUpdateException
	{
		List<Breadcrumb> breadcrumbs = new ArrayList();
		breadcrumbs = (List<Breadcrumb>) request.getAttribute("breadcrumbs");
		String breadcrumbName = "";
		int count = 1;
		if (breadcrumbs != null)
		{


			for (final Breadcrumb breadcrumb : breadcrumbs)
			{
				breadcrumbName += breadcrumb.getName();
				if (count < breadcrumbs.size())
				{
					breadcrumbName += ":";

				}
				count++;
			}
		}

		return breadcrumbName;
	}

	private static String getVisitorIpAddress(HttpServletRequest request)
	{
		final String[] HEADERS_TO_TRY =
		{ "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
		for (String header : HEADERS_TO_TRY)
		{
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip))
			{
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	private static HttpServletRequest getRequest()
	{
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	private static TealiumHelper setupTealiumHelper() throws UDODefinitionException, UDOUpdateException
	{
		String accountString = null;
		String profileString = null;
		String targetString = null;

		if (Config.getParameter("tealiumIQ.account") != null)
		{
			accountString = Config.getParameter("tealiumIQ.account");
		}
		if (Config.getParameter("tealiumIQ.profile") != null)
		{
			profileString = Config.getParameter("tealiumIQ.profile");
		}
		if (Config.getParameter("tealiumIQ.target") != null)
		{
			targetString = Config.getParameter("tealiumIQ.target");
		}
		return new TealiumHelper(accountString, profileString, targetString);
	}

	public static String getSyncTag()
	{
		String scriptString = null;
		try
		{
			if (Config.getParameter("tealiumIQ.utagSyncEnabled") != "0")
			{
				TealiumHelper helper = setupTealiumHelper();
				scriptString = helper.outputUtagSyncJsTag();
			}

		}
		catch (Exception e)
		{
			scriptString = getExceptionString(e);
		}


		return scriptString;
	}

	public static String getHomeScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.HOME);

			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "home");
			udo.setValue("site_section", "home");
			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getGenericPageScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";

		try
		{
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.HOME);
			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "generic");
			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getSearchPageScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.SEARCH);

			HttpServletRequest request = getRequest();
			ProductCategorySearchPageData searchData = (ProductCategorySearchPageData) request.getAttribute("searchPageData");

			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "search");
			udo.setValue("site_section", "Search Results");
			if (searchData != null)
			{
				String searchKeyword = searchData.getFreeTextSearch();
				if (searchKeyword != null && searchKeyword.length() > 0)
				{
					udo.setValue(TealiumHelper.SearchPageUDO.PredefinedUDOFields.SEARCH_KEYWORD, searchKeyword);
				}
				String searchResults = ((PaginationData) searchData.getPagination()).getTotalNumberOfResults() + "";
				if (searchResults != null && searchResults.length() > 0)
				{
					udo.setValue(TealiumHelper.SearchPageUDO.PredefinedUDOFields.SEARCH_RESULTS, searchResults);
				}

				final String searchCategory = (String) request.getParameter("searchCategory");
				if (searchCategory != null)
				{
					udo.setValue("searchCategory", searchCategory);
				}
			}

			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (Exception e)
		{

		}

		return scriptString;
	}

	public static String getCategoryScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CATEGORY);

			HttpServletRequest request = getRequest();
			String categoryNameString = (String) request.getAttribute("categoryName");

			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "product");
			if (categoryNameString != null && categoryNameString.length() > 0)
			{
				udo.setValue(TealiumHelper.CategoryPageUDO.PredefinedUDOFields.PAGE_CATEGORY_NAME, categoryNameString);
			}

			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getProductPageScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			final TealiumHelper tealiumHelper = setupTealiumHelper();
			final UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.PRODUCT);

			final HttpServletRequest request = getRequest();

			final ProductData productData = (ProductData) request.getAttribute("product");


			final List<String> productCategoryList = new ArrayList<String>();
			final List<String> productCategoryIdList = new ArrayList<String>();
			String productCategory = "";
			String productBrand = "";
			String productSku = "";
			String productPrice = "";
			String productName = "";
			String categoryId = "";
			String productUnitPrice = "";
			String productSubCategoryName = "";
			if (productData != null && productData.getCategories()!=null)
			{
				for (final CategoryData category : productData.getCategories())
				{
					productCategoryList.add(category.getName());
					productCategoryIdList.add(category.getCode());
				}
			}
			final Object[] productCategoryStrings = productCategoryList.toArray();
			//Object[] productCategoryIdStrings = productCategoryIdList.toArray();

			if (productCategoryStrings.length > 0)
			{
				productCategory = (String) productCategoryStrings[0];
				categoryId = productCategoryIdList.get(0);
			}

			if (productCategoryStrings.length >= 2)
			{
				productSubCategoryName = (String) productCategoryStrings[1];
			}

			if (productData != null)
			{
				if (productData.getCode() != null)
				{
					productSku = productData.getCode();
					final BuyBoxFacade buyboxfacade = (BuyBoxFacade) Registry.getApplicationContext().getBean("buyBoxFacade");
					if (buyboxfacade != null)
					{
						final BuyBoxData buyboxdata = buyboxfacade.buyboxPrice(productSku);
						if (buyboxdata != null)
						{
							final PriceData specialPrice = buyboxdata.getSpecialPrice();
							final PriceData mrp = buyboxdata.getMrp();
							final PriceData mop = buyboxdata.getPrice();

							if (mrp != null)
							{
								productUnitPrice = mrp.getValue().toPlainString();
							}
							if (specialPrice != null)
							{
								productPrice = specialPrice.getValue().toPlainString();
							}
							else
							{
								productPrice = mop.getValue().toPlainString();
							}
						}
					}

				}
				/*
				 * if (productData.getPrice() != null) { productPrice = productData.getPrice().getValue().toPlainString(); }
				 */
				if (productData.getName() != null)
				{
					productName = productData.getName();
				}


				if (productData.getBrand() != null)
				{
					productBrand = productData.getBrand().getBrandname();
				}

				if (productData.getRootCategory() != null)
				{
					udo.setValue("site_section_detail", productData.getRootCategory());
				}
			}

			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "product")
					.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_BRAND, Arrays.asList(productBrand))
					.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY, Arrays.asList(productCategory))
					.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_ID, Arrays.asList(productSku))
					.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE, Arrays.asList(productPrice))
					.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_NAME, Arrays.asList(productName))
					.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_SKU, Arrays.asList(productSku))
					.addArrayValues(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE,
							Arrays.asList(productUnitPrice));
			udo.setValue(TealiumHelper.ProductPageUDO.PredefinedUDOFields.PAGE_SUBCATEGORY_NAME, productSubCategoryName);
			udo.setValue("category_id", categoryId);
			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (final Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getCartScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			final TealiumHelper tealiumHelper = setupTealiumHelper();
			final UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CART);

			final HttpServletRequest request = getRequest();
			final CartData cartData = (CartData) request.getAttribute("cartData");


			final List<String> productBrandList = new ArrayList<String>();
			final List<String> productCategoryList = new ArrayList<String>();
			final List<String> productIdList = new ArrayList<String>();
			final List<String> productListPriceList = new ArrayList<String>();
			final List<String> productNameList = new ArrayList<String>();
			final List<String> productQuantityList = new ArrayList<String>();
			final List<String> productSkuList = new ArrayList<String>();
			final List<String> productUnitPriceList = new ArrayList<String>();
			final List<String> pageSubCategories = new ArrayList<String>();
			String adobeProductSku = "";
			String page_subCategory_name = "";
			if (cartData != null)
			{
				if (cartData.getTotalPrice() != null)
				{
					final String cartTotal = cartData.getTotalPrice().getValue().toPlainString();
					udo.setValue("cart_total", cartTotal);
				}
				for (final OrderEntryData entry : cartData.getEntries())
				{
					final String sku = entry.getProduct().getCode();
					final String name = entry.getProduct().getName();
					final String quantity = entry.getQuantity() + "";
					final String basePrice = entry.getBasePrice().getValue().toPlainString();//base price for a cart entry
					final String totalEntryPrice = entry.getTotalPrice().getValue().toPlainString();//total price for a cart entry 

					final List<String> categoryList = new ArrayList<String>();
					//START [05-Feb-2016] R2.1 - Adding only a Null Check to fix Card payment issue.
					//Check that if (entry.getProduct().getCategories() != null) then only execute the loop. Else just log an 
					//error message and continue.
					Logger tealiumLog = Logger.getLogger(TealiumIQManager.class.getName());
					if (entry.getProduct() != null && entry.getProduct().getCategories() != null){
						tealiumLog.info(" entry.getProduct().getCategories() is NOT NULL. - sku,name = " + sku + " : " + name);
						for (final CategoryData thisCategory : entry.getProduct().getCategories())
						{
							categoryList.add(thisCategory.getName());
						}
					} else {
						tealiumLog.warn(" ***>>> entry.getProduct().getCategories() is NULL. - sku,name = " + sku + " : " + name);
					}
					//End [05-Feb-2016] R2.1 - Adding Null Check to fix Card payment issue. 
					final Object[] categoryStrings = categoryList.toArray();
					String category = "";
					if (categoryStrings.length > 0)
					{
						category = (String) categoryStrings[0];
					}
					String brand = null;
					if (entry != null && entry.getProduct() != null && entry.getProduct().getBrand() != null)
					{
						brand = entry.getProduct().getBrand().getBrandname();
					}
					if (categoryStrings.length >= 2)
					{
						page_subCategory_name = (String) categoryStrings[1];
						pageSubCategories.add(page_subCategory_name);
					}


					productBrandList.add(brand);
					productCategoryList.add(category);
					productIdList.add(sku);
					productListPriceList.add(totalEntryPrice);
					productNameList.add(name);
					productQuantityList.add(quantity);
					productSkuList.add(sku);
					productUnitPriceList.add(basePrice);
				}
				if (productSkuList != null)
				{
					int count = 1;
					for (final String sku : productSkuList)
					{
						final String appendedSku = ";" + sku;
						adobeProductSku += appendedSku;
						if (count < productSkuList.size())
						{
							adobeProductSku += ",";

						}
						count++;
					}
				}
			}

			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "checkout")
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_BRAND, productBrandList)
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY, productCategoryList)
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_ID, productIdList)
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE, productListPriceList)
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_NAME, productNameList)
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_QUANTITY, productQuantityList)
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_SKU, productSkuList)
					.addArrayValues(TealiumHelper.CartPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE, productUnitPriceList)
					.addArrayValues("page_subcategory_name", pageSubCategories);
			udo.setValue("adobe_product", adobeProductSku);

			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (final Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getOrderConfirmationScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			final TealiumHelper tealiumHelper = setupTealiumHelper();
			final UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CONFIRMATION);

			final HttpServletRequest request = getRequest();

			final OrderData orderData = (OrderData) request.getAttribute("orderData");
			final CurrencyData currencyData = (CurrencyData) request.getAttribute("currentCurrency");
			udo.setValue("site_section", "Order Confirmation");
			if (currencyData != null)
			{
				if (currencyData.getIsocode() != null)
				{
					final String siteCurrency = currencyData.getIsocode();
					udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_CURRENCY, siteCurrency);
				}
			}
			if (orderData != null)
			{
				if (orderData.getCode() != null)
				{
					final String orderIDString = orderData.getCode();
					udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_ID, orderIDString);
				}
				if (orderData.getTotalPrice() != null)
				{
					final String orderTotal = orderData.getTotalPrice().getValue().toPlainString();
					udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_TOTAL, orderTotal);
				}

				if (orderData.getMplPaymentInfo() != null)
				{
					String paymentType = "";
					if (orderData.getMplPaymentInfo().getPaymentOption().equalsIgnoreCase("Credit Card")
							|| orderData.getMplPaymentInfo().getPaymentOption().equalsIgnoreCase("Debit Card"))
					{
						paymentType = orderData.getMplPaymentInfo().getPaymentOption();
						if (null != orderData.getMplPaymentInfo().getCardCardType())
							paymentType += '|' + orderData.getMplPaymentInfo().getCardCardType();


					}
					else if (orderData.getMplPaymentInfo().getPaymentOption().equalsIgnoreCase("Netbanking"))
					{
						paymentType = orderData.getMplPaymentInfo().getPaymentOption();
						if (null != orderData.getMplPaymentInfo().getBank())
							paymentType += '|' + orderData.getMplPaymentInfo().getBank();
					}
					else
					{
						paymentType = orderData.getMplPaymentInfo().getPaymentOption();
					}
					udo.setValue("order_payment_type", paymentType);
				}
				/*
				 * if (orderData.getDeliveryCost() != null) { final String deliveryCost =
				 * orderData.getDeliveryCost().getValue().toPlainString();
				 * udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_SHIPPING, deliveryCost); }
				 */
				if (orderData.getTotalTax() != null)
				{
					final String totalTax = orderData.getTotalTax().getValue().toPlainString();
					udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_TAX, totalTax);
				}
				if (orderData.getTotalDiscounts() != null)
				{
					final String totalDiscounts = orderData.getTotalDiscounts().getValue().toPlainString();
					udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_DISCOUNT, totalDiscounts);
				}
				if (orderData.getSubTotal() != null)
				{
					final String subTotal = orderData.getSubTotal().getValue().toPlainString();
					udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.ORDER_SUBTOTAL, subTotal);
				}

				if (orderData.getCreated() != null)
				{
					final String orderDate = orderData.getCreated().toString();
					udo.setValue("order_date", orderDate);
				}


			}

			/*
			 * final String email = (String) request.getAttribute("email"); if (email != null) {
			 * udo.setValue(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.CUSTOMER_EMAIL, email); }
			 */

			final List<String> productBrandList = new ArrayList<String>();
			final List<String> productCategoryList = new ArrayList<String>();
			final List<String> productIdList = new ArrayList<String>();
			final List<String> productListPriceList = new ArrayList<String>();
			final List<String> productNameList = new ArrayList<String>();
			final List<String> productQuantityList = new ArrayList<String>();
			final List<String> productSkuList = new ArrayList<String>();
			final List<String> productUnitPriceList = new ArrayList<String>();
			final List<String> productDiscountList = new ArrayList<String>();
			final List<String> deliveryModes = new ArrayList<String>();
			final List<String> pageSubCategories = new ArrayList<String>();
			final List<String> orderShippingCharges = new ArrayList<String>();


			if (orderData != null)
			{
				for (final OrderEntryData entry : orderData.getEntries())
				{
					final String sku = entry.getProduct().getCode();
					final String name = entry.getProduct().getName();
					final String quantity = entry.getQuantity() + "";
					final String basePrice = entry.getBasePrice().getValue().toPlainString();//base price for a cart entry
					final String totalEntryPrice = entry.getTotalPrice().getValue().toPlainString();//total price for a cart entry 


					final List<String> categoryList = new ArrayList<String>();
					for (final CategoryData thisCategory : entry.getProduct().getCategories())
					{
						categoryList.add(thisCategory.getName());
					}
					final Object[] categoryStrings = categoryList.toArray();
					String category = "";
					String brand = "";
					String order_shipping = "";

					String page_subcategory_name = "";
					String order_shipping_charge = "";

					if (categoryStrings.length > 0)
					{
						category = (String) categoryStrings[0];
					}
					if (categoryStrings.length >= 2)
					{
						page_subcategory_name = (String) categoryStrings[1];
						pageSubCategories.add(page_subcategory_name);
					}

					if (entry.getProduct() != null && entry.getProduct().getBrand() != null)
					{
						brand = entry.getProduct().getBrand().getBrandname();
					}

					if (entry.getMplDeliveryMode() != null)
					{
						order_shipping = entry.getMplDeliveryMode().getName();
					}

					if (entry.getCurrDelCharge() != null)
					{

						order_shipping_charge = entry.getCurrDelCharge().getFormattedValue();
					}

					productBrandList.add(brand);
					productCategoryList.add(category);
					productIdList.add(sku);
					productListPriceList.add(totalEntryPrice);
					productNameList.add(name);
					productQuantityList.add(quantity);
					productSkuList.add(sku);
					productUnitPriceList.add(basePrice);
					productDiscountList.add("");
					deliveryModes.add(order_shipping);
					orderShippingCharges.add(order_shipping_charge);

				}


			}

			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "checkout")
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_BRAND, productBrandList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_CATEGORY, productCategoryList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_ID, productIdList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_LIST_PRICE, productListPriceList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_NAME, productNameList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_QUANTITY, productQuantityList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_SKU, productSkuList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_UNIT_PRICE, productUnitPriceList)
					.addArrayValues(TealiumHelper.ConfirmationPageUDO.PredefinedUDOFields.PRODUCT_DISCOUNT, productDiscountList)
					.addArrayValues("order_shipping_modes", deliveryModes).addArrayValues("page_subcategory_name", pageSubCategories)
					.addArrayValues("order_shipping_charges", orderShippingCharges);


			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (final Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getCustomerDetailScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CUSTOMER);

			HttpServletRequest request = getRequest();
			CustomerData customerData = (CustomerData) request.getAttribute("customerData");
			if (customerData != null)
			{
				if (customerData.getEmail() != null)
				{
					String customerEmailString = customerData.getEmail();
					udo.setValue(TealiumHelper.CustomerPageUDO.PredefinedUDOFields.CUSTOMER_EMAIL, customerEmailString);
				}
				StringBuilder nameString = new StringBuilder("");
				if (customerData.getFirstName() != null)
				{
					nameString.append(customerData.getFirstName());
				}
				if (customerData.getLastName() != null)
				{
					if (nameString.toString().length() > 0)
					{
						nameString.append(" ");
					}
					nameString.append(customerData.getLastName());
				}
				if (customerData.getTitleCode() != null)
				{
					String genderString = "unknown";
					if (Pattern.matches("^mr$", customerData.getTitleCode()))
					{
						genderString = "male";
					}
					else if (Pattern.matches("s$", customerData.getTitleCode()))
					{
						genderString = "female";
					}
					udo.setValue("gender", genderString);
				}

				if (customerData.getDisplayUid() != null)
				{
					udo.setValue("user_id", customerData.getDisplayUid());
				}
			}
			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "customer");

			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	public static String getErrorScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			TealiumHelper tealiumHelper = setupTealiumHelper();
			UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.HOME);

			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "error");
			udo.setValue("site_section", "error");
			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (Exception e)
		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}


	public static String getWishlistScript() throws UDODefinitionException, UDOUpdateException
	{
		String scriptString = "";
		try
		{
			final TealiumHelper tealiumHelper = setupTealiumHelper();
			final UDO udo = setupUDO(tealiumHelper, PrebuiltUDOPageTypes.CUSTOMER);

			final HttpServletRequest request = getRequest();
			String productSubCategoryName = "";
			final List<ProductData> datas = (List<ProductData>) request.getAttribute("ProductDatas");
			final WishlistFacade wishlistFacade = (WishlistFacade) Registry.getApplicationContext().getBean("defaultWishlistFacade");
			final ProductFacade productFacade = (ProductFacade) Registry.getApplicationContext().getBean("productFacade");
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final List<String> productIdListInWl = new ArrayList<String>();
			final List<String> productNameListInWl = new ArrayList<String>();
			final List<String> productBrandListInWl = new ArrayList<String>();
			final List<String> productListPriceListInWl = new ArrayList<String>();
			final List<String> productUnitPriceListInWl = new ArrayList<String>();
			final List<String> productSkuListInWl = new ArrayList<String>();
			final List<String> productCategoryListInWl = new ArrayList<String>();
			final List<String> productSubCategoryListInWl = new ArrayList<String>();
			final List<String> productQuantityListInWl = new ArrayList<String>();

			if (null != allWishlists)
			{
				for (final Wishlist2Model wishlistEntry : allWishlists)
				{
					for (final Wishlist2EntryModel itemEntry : wishlistEntry.getEntries())
					{
						productIdListInWl.add(itemEntry.getProduct().getCode());
						productNameListInWl.add(itemEntry.getProduct().getName());
						productQuantityListInWl.add(String.valueOf(1));
						final ProductData productData = productFacade.getProductForOptions(itemEntry.getProduct(),
								Arrays.asList(ProductOption.BASIC, ProductOption.CATEGORIES, ProductOption.SELLER));
						if (productData != null)
						{
							final BuyBoxFacade buyboxfacade = (BuyBoxFacade) Registry.getApplicationContext().getBean("buyBoxFacade");
							if (productData.getBrand() != null)
							{
								productBrandListInWl.add(productData.getBrand().getBrandname());
							}
							final List<String> productCategoryList = new ArrayList<String>();
							for (final CategoryData category : productData.getCategories())
							{
								productCategoryList.add(category.getName());
							}
							final Object[] productCategoryStrings = productCategoryList.toArray();
							String productCategory = "";
							if (productCategoryStrings.length > 0)
							{
								productCategory = (String) productCategoryStrings[0];
								productCategoryListInWl.add(productCategory);
							}
							if (productCategoryStrings.length >= 2)
							{
								productSubCategoryName = (String) (productCategoryStrings[1]);
								productSubCategoryListInWl.add(productSubCategoryName);
							}
							if (buyboxfacade != null)
							{
								final BuyBoxData buyboxdata = buyboxfacade.buyboxPrice(productData.getCode());
								if (buyboxdata != null)
								{
									productSkuListInWl.add(buyboxdata.getSellerArticleSKU());
									final PriceData specialPrice = buyboxdata.getSpecialPrice();
									final PriceData mrp = buyboxdata.getMrp();
									final PriceData mop = buyboxdata.getPrice();
									if (mrp != null)
									{
										productUnitPriceListInWl.add(mrp.getValue().toPlainString());
									}
									if (specialPrice != null)
									{
										productListPriceListInWl.add(specialPrice.getValue().toPlainString());
									}
									else
									{
										productListPriceListInWl.add(mop.getValue().toPlainString());
									}
								}
							}
						}
					}
				}
			}
			udo.setValue(TealiumHelper.HomePageUDO.PredefinedUDOFields.PAGE_TYPE, "wishlist")
					.addArrayValues("product_id", productIdListInWl).addArrayValues("product_name", productNameListInWl)
					.addArrayValues("product_quantity", productQuantityListInWl).addArrayValues("product_brand", productBrandListInWl)
					.addArrayValues("product_category", productCategoryListInWl)
					.addArrayValues("page_subcategory_name", productSubCategoryListInWl)
					.addArrayValues("product_sku", productSkuListInWl).addArrayValues("product_list_price", productListPriceListInWl)
					.addArrayValues("product_unit_price", productUnitPriceListInWl);

			scriptString = tealiumHelper.outputFullHtml(udo);
		}
		catch (final Exception e)

		{
			scriptString = getExceptionString(e);
		}

		return scriptString;
	}

	private static String getExceptionString(Exception e)
	{
		Date date = new Date();
		String referenceIDString = date.hashCode() + "";
		Logger tealiumLog = Logger.getLogger(TealiumIQManager.class.getName());
		tealiumLog.error((new StringBuilder()).append("Tealium error ID: ").append(referenceIDString), e);

		StringBuilder scriptBuilder = new StringBuilder();
		scriptBuilder.append("<!--  Tealium ERROR \n");
		scriptBuilder.append("There may be an error in your installation, please check you logging.");
		scriptBuilder.append("\n");
		scriptBuilder.append("Log refernce ID: ");
		scriptBuilder.append(referenceIDString);
		scriptBuilder.append("\n\t\t  END Tealium ERROR -->");
		return scriptBuilder.toString();
	}
}
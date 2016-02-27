/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.category.data.BITTSec3detailsData;
import com.tisl.mpl.category.data.CategoryLandingDataHierarchy;
import com.tisl.mpl.category.data.LandingDetailsforCategoryData;
import com.tisl.mpl.category.data.PromoBannerSecData;
import com.tisl.mpl.category.data.WAHASTTSec2detailsData;
import com.tisl.mpl.category.populator.ProductCategoryPopulator;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.DepartmentCollectionComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplCategoryCarouselComponentModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.VideoComponentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.cms.components.CMSMediaParagraphComponentModel;
import com.tisl.mpl.service.MplCustomCategoryService;
import com.tisl.mpl.wsdto.AboutUsResultWsData;
import com.tisl.mpl.wsdto.DepartmentHierarchyData;
import com.tisl.mpl.wsdto.DepartmentListHierarchyData;
import com.tisl.mpl.wsdto.DepartmentSubHierarchyData;
import com.tisl.mpl.wsdto.DepartmentSuperSubHierarchyData;
import com.tisl.mpl.wsdto.HelpAndServicestWsData;
import com.tisl.mpl.wsdto.ProductForCategoryData;
import com.tisl.mpl.wsdto.ProductMobileWsDTOList;


/**
 * @author TCS
 *
 */
public class MplCustomCategoryServiceImpl implements MplCustomCategoryService
{
	@Resource(name = "productCategoryPopulator")
	private ProductCategoryPopulator productforCategoryPopulator;
	@Resource(name = "categoryService")
	private CategoryService categoryService;
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;
	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;
	@Autowired
	private ModelService modelService;
	@Resource(name = "categoryModelUrlResolver")
	private UrlResolver<CategoryModel> categoryModelUrlResolver;




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
	 * @return the cmsComponentService
	 */
	public CMSComponentService getCmsComponentService()
	{
		return cmsComponentService;
	}

	/**
	 * @param cmsComponentService
	 *           the cmsComponentService to set
	 */
	public void setCmsComponentService(final CMSComponentService cmsComponentService)
	{
		this.cmsComponentService = cmsComponentService;
	}

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.VARIANT_FULL);
	private static final Logger LOG = Logger.getLogger(MplCustomCategoryServiceImpl.class);

	@Override
	public ProductForCategoryData getCategoryforCategoryid(final String CategoryId)
	{
		CategoryModel selectedCategory = getModelService().create(CategoryModel.class);
		if (null != categoryService && null != CategoryId)
		{
			selectedCategory = categoryService.getCategoryForCode(CategoryId);
		}
		String secondrootname = null;

		String cloth = null;
		String women = null;
		String men = null;
		String electroinics = null;
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESCLOTH)
		{
			cloth = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESCLOTH, "");
		}

		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESMEN)
		{
			men = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESMEN, "");
		}
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESWOMEN)
		{
			women = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESWOMEN, "");
		}

		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SALESELECTRONICS)
		{
			electroinics = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESELECTRONICS,
					"");
		}

		final List<CategoryModel> super_category_list = categoryService.getPathForCategory(selectedCategory);
		for (final CategoryModel cattype : super_category_list)
		{
			if (!(cattype instanceof ClassificationClassModel))
			{
				if (null != cattype.getCode()
						&& (cattype.getCode().equalsIgnoreCase(cloth) || cattype.getCode().equalsIgnoreCase(women)
								|| cattype.getCode().equalsIgnoreCase(men) || cattype.getCode().equalsIgnoreCase(electroinics))
						&& null != cattype.getName())
				{
					secondrootname = cattype.getName();
				}
			}
		}
		final List<ProductData> products = getProducts(selectedCategory);
		final ProductMobileWsDTOList prodlist = new ProductMobileWsDTOList();
		prodlist.setProducts(products);
		final ProductForCategoryData categoryForCatID = new ProductForCategoryData();
		if (null != secondrootname)
		{
			categoryForCatID.setCategory_type(secondrootname);
		}
		if (null != selectedCategory.getName())
		{
			categoryForCatID.setTitle(selectedCategory.getName());
		}
		String defaultCatalog = null;
		if (null != configurationService && null != configurationService.getConfiguration())
		{
			defaultCatalog = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.DEFAULTCATALOGID, "");
		}
		String defaultCatalogVersion = null;
		if (null != configurationService && null != configurationService.getConfiguration())
		{
			defaultCatalogVersion = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID, "");
		}
		final String SLASH = "/";
		String banner_image_prefix = null;
		if (null != defaultCatalog && null != defaultCatalogVersion)
		{
			banner_image_prefix = defaultCatalog + SLASH + defaultCatalogVersion;
		}
		productforCategoryPopulator.populate(selectedCategory, categoryForCatID);
		final String banner_image_url = categoryForCatID.getBanner_image();
		if (null != banner_image_prefix && null != banner_image_url)
		{
			categoryForCatID.setBanner_image(banner_image_prefix + banner_image_url);
		}
		if (!products.isEmpty())
		{
			categoryForCatID.setProductList(prodlist);
		}
		return categoryForCatID;
	}

	private List<ProductData> getProducts(final CategoryModel category)
	{
		final List<ProductData> products = new ArrayList<>();

		final Collection<CategoryModel> subcategories = categoryService.getAllSubcategoriesForCategory(category);

		//if (subcategories != null && subcategories.size() != 0)
		if (CollectionUtils.isNotEmpty(subcategories))
		{
			for (final CategoryModel subcategory : subcategories)
			{
				if (subcategory.getProducts() != null)
				{
					for (final ProductModel productModel : subcategory.getProducts())
					{
						if (productModel instanceof PcmProductVariantModel)
						{
							try
							{
								products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
							}
							catch (final Exception ex)
							{
								LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
								throw new EtailNonBusinessExceptions(ex);
							}

						}
					}
				}
			}
		}
		else
		{
			if (category.getProducts() != null)
			{
				for (final ProductModel productModel : category.getProducts())
				{
					category.getProducts().size();
					if (productModel instanceof PcmProductVariantModel)
					{
						try
						{
							products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
						}
						catch (final Exception ex)
						{
							LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
							throw new EtailNonBusinessExceptions(ex);
						}

					}
				}
			}
		}
		return products;
	}

	/*
	 * To get all categories shop by department
	 *
	 * @see com.tisl.mpl.service.MplCustomCategoryService#getallCategories()
	 */
	@Override
	public DepartmentListHierarchyData getAllCategories() throws EtailNonBusinessExceptions
	{

		final DepartmentListHierarchyData shopByDeptData = new DepartmentListHierarchyData();
		final List<DepartmentHierarchyData> deptDataList = new ArrayList<DepartmentHierarchyData>();
		try
		{
			Date modifiedTime = null;
			final DepartmentCollectionComponentModel shopByDeptComponent = getShopByDept();
			if (null != shopByDeptComponent)
			{
				modifiedTime = getModifiedTime();
			}
			List<CategoryModel> deptList = new ArrayList<CategoryModel>();
			CategoryModel category = getModelService().create(CategoryModel.class);
			CategoryModel department = null;
			List<DepartmentSubHierarchyData> subDeptDataList = null;
			DepartmentHierarchyData deptData = null;
			List<CategoryModel> secondSubCatList = null;
			List<CategoryModel> thirdSubCatList = null;
			DepartmentSubHierarchyData subDeptData = null;
			List<DepartmentSuperSubHierarchyData> superSubDeptDataList = null;
			DepartmentSuperSubHierarchyData thirdLevelCat = null;
			if (null != shopByDeptComponent.getDepartmentCollection())
			{
				deptList = (List<CategoryModel>) shopByDeptComponent.getDepartmentCollection();
			}
			for (final CategoryModel dept : deptList)
			{
				department = getModelService().create(CategoryModel.class);
				if (null != modifiedTime && null != dept.getModifiedtime() && dept.getModifiedtime().compareTo(modifiedTime) > 0)
				{
					modifiedTime = dept.getModifiedtime();
				}
				subDeptDataList = new ArrayList<DepartmentSubHierarchyData>();
				deptData = new DepartmentHierarchyData();
				if (null != dept)
				{
					category = dept;
				}
				if (null != category.getCode() && !StringUtils.isEmpty(category.getCode()))
				{
					deptData.setSuper_category_id(category.getCode());
				}
				if (null != category.getName() && !StringUtils.isEmpty(category.getName()))
				{
					deptData.setSuper_category_name(category.getName());
				}
				secondSubCatList = new ArrayList<CategoryModel>();
				if (null != category.getCode())
				{
					department = categoryService.getCategoryForCode(category.getCode());
				}
				if (null != department && null != modifiedTime && null != department.getModifiedtime()
						&& department.getModifiedtime().compareTo(modifiedTime) > 0)
				{
					modifiedTime = department.getModifiedtime();
				}

				if (null != department)
				{
					secondSubCatList = department.getCategories();
				}
				for (final CategoryModel subCategory : secondSubCatList)
				{
					if (null != subCategory && null != modifiedTime && null != subCategory.getModifiedtime()
							&& subCategory.getModifiedtime().compareTo(modifiedTime) > 0)
					{
						modifiedTime = subCategory.getModifiedtime();
					}
					thirdSubCatList = new ArrayList<CategoryModel>();
					subDeptData = new DepartmentSubHierarchyData();
					superSubDeptDataList = new ArrayList<DepartmentSuperSubHierarchyData>();
					if (null != subCategory.getCode() && !StringUtils.isEmpty(subCategory.getCode()))
					{
						subDeptData.setSub_category_id(subCategory.getCode());
					}
					if (null != subCategory.getName() && !StringUtils.isEmpty(subCategory.getName()))
					{
						subDeptData.setSub_category_name(subCategory.getName());
					}
					if (null != subCategory.getCategories() && !subCategory.getCategories().isEmpty())
					{
						thirdSubCatList = subCategory.getCategories();
					}
					for (final CategoryModel thirdlevelSubCategory : thirdSubCatList)
					{
						if (null != thirdlevelSubCategory && null != modifiedTime && null != thirdlevelSubCategory.getModifiedtime()
								&& thirdlevelSubCategory.getModifiedtime().compareTo(modifiedTime) > 0)
						{
							modifiedTime = thirdlevelSubCategory.getModifiedtime();
						}
						thirdLevelCat = new DepartmentSuperSubHierarchyData();
						if (null != thirdlevelSubCategory.getCode() && !StringUtils.isEmpty(thirdlevelSubCategory.getCode()))
						{
							thirdLevelCat.setSuper_sub_category_id(thirdlevelSubCategory.getCode());
						}
						if (null != thirdlevelSubCategory.getName() && !StringUtils.isEmpty(thirdlevelSubCategory.getName()))
						{
							thirdLevelCat.setSuper_sub_category_name(thirdlevelSubCategory.getName());
						}

						superSubDeptDataList.add(thirdLevelCat);
					}
					subDeptData.setSupersubCategories(superSubDeptDataList);

					subDeptDataList.add(subDeptData);
				}
				if (!subDeptDataList.isEmpty())
				{
					deptData.setSubCategories(subDeptDataList);
				}

				deptDataList.add(deptData);
			}
			if (null != modifiedTime && !StringUtils.isEmpty(modifiedTime.toString()))
			{
				shopByDeptData.setModifiedTime(modifiedTime.toString());
			}
			if (!deptDataList.isEmpty())
			{
				shopByDeptData.setShopbydepartment(deptDataList);
			}
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		return shopByDeptData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplCustomCategoryService#getAboutusBanner()
	 */
	@Override
	public AboutUsResultWsData getAboutus() throws EtailNonBusinessExceptions
	{
		final AboutUsResultWsData aboutUsBanner = new AboutUsResultWsData();
		try
		{
			String bannerComUid = null;
			String mainCompUid = null;
			String workingWithUsCompUid = null;
			String tataTrustCompUid = null;
			String tataBusinessCompUid = null;
			String contactCompUid = null;

			bannerComUid = MarketplacewebservicesConstants.ABOUTUSBANNER;
			mainCompUid = MarketplacewebservicesConstants.ABOUTUSPAGE;
			workingWithUsCompUid = MarketplacewebservicesConstants.WORKINGWITHUS;
			tataTrustCompUid = MarketplacewebservicesConstants.TATATRUSTMEDIA;
			tataBusinessCompUid = MarketplacewebservicesConstants.TATABUSINESSETHICS;
			contactCompUid = MarketplacewebservicesConstants.ABOUTUSCONTACTADDRESS;

			//data from AboutUsBannerComponent
			final SimpleBannerComponentModel bannerCom = (SimpleBannerComponentModel) cmsComponentService
					.getSimpleCMSComponent(bannerComUid);
			LOG.debug("bannerCom: " + bannerCom);
			aboutUsBanner.setBannerImageUrl(bannerCom.getMedia().getUrl());

			//data from AboutUsComponent
			final CMSParagraphComponentModel mainComp = (CMSParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(mainCompUid);
			LOG.debug("mainComp: " + mainComp);
			aboutUsBanner.setContent(getHTMLParsedTextContent(mainComp.getContent()));

			//data from ContactAddress
			final CMSParagraphComponentModel contactComp = (CMSParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(contactCompUid);
			LOG.debug("contactComp: " + contactComp);
			aboutUsBanner.setContactUs(getHTMLParsedTextContent(contactComp.getContent()));

			//data from WorkingWithUsComponent
			final CMSMediaParagraphComponentModel workingWithUsComp = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(workingWithUsCompUid);
			LOG.debug("workingWithUsComp: " + workingWithUsComp);
			aboutUsBanner.setWorkingWithUsCont(getHTMLParsedTextContent(workingWithUsComp.getContent()));
			aboutUsBanner.setWorkingWithUsImg(workingWithUsComp.getMedia().getUrl());

			//data from TataTrustMediaParagraphComponent
			final CMSMediaParagraphComponentModel tataTrustComp = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(tataTrustCompUid);
			LOG.debug("tataTrustComp: " + tataTrustComp);
			aboutUsBanner.setTataTrustCont(getHTMLParsedTextContent(tataTrustComp.getContent()));
			aboutUsBanner.setTataTrustImg(tataTrustComp.getMedia().getUrl());

			//data from TataBusinessEthicsMediaParagraphComponent
			final CMSMediaParagraphComponentModel tataBusinessComp = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(tataBusinessCompUid);
			LOG.debug("tataBusinessComp: " + tataBusinessComp);
			aboutUsBanner.setTataBusinessCont(getHTMLParsedTextContent(tataBusinessComp.getContent()));
			aboutUsBanner.setTataBusinessImg(tataBusinessComp.getMedia().getUrl());
		}
		catch (final Exception e)
		{
			LOG.error("About Us Page Error: " + e);
			throw new EtailNonBusinessExceptions(e);
		}
		return aboutUsBanner;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplCustomCategoryService#getHelpnServices()
	 */
	@Override
	public HelpAndServicestWsData getHelpnServices() throws EtailNonBusinessExceptions
	{
		final HelpAndServicestWsData helpNservices = new HelpAndServicestWsData();
		try
		{
			String callUsUid = null;
			String chatWithUsUid = null;
			String authNExclusiveUid = null;
			String customerFirstUid = null;
			String bestCollectionUid = null;
			String deliveryonTimeUid = null;
			String faqPageCompUid = null;
			String faqOrderCompUid = null;
			String faqReturnCompUid = null;


			callUsUid = MarketplacewebservicesConstants.CALLUS;
			chatWithUsUid = MarketplacewebservicesConstants.CHATWITHUS;
			authNExclusiveUid = MarketplacewebservicesConstants.AUTHNEXCLUSIVE;
			customerFirstUid = MarketplacewebservicesConstants.CUSTOMERFIRST;
			bestCollectionUid = MarketplacewebservicesConstants.BESTCOLLECTION;
			deliveryonTimeUid = MarketplacewebservicesConstants.DELIVERYONTIME;
			faqPageCompUid = MarketplacewebservicesConstants.FAQPAGECOMP;
			faqOrderCompUid = MarketplacewebservicesConstants.FAQORDERCOMP;
			faqReturnCompUid = MarketplacewebservicesConstants.FAQRETURNCOMP;

			// data from CallUsMediaParagraphComponent
			final CMSMediaParagraphComponentModel callUs = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(callUsUid);
			helpNservices.setCallUsComp(getHTMLParsedTextContent(callUs.getContent()));
			helpNservices.setCallUsImg(callUs.getMedia().getUrl());

			// data from ChatWithUsMediaParagraphComponent
			final CMSMediaParagraphComponentModel chatWithUs = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(chatWithUsUid);
			helpNservices.setChatWithUsComp(getHTMLParsedTextContent(chatWithUs.getContent()));
			helpNservices.setChatWithUsImg(chatWithUs.getMedia().getUrl());

			// data from AuthenticExclusiveMediaParagraphComponent
			final CMSMediaParagraphComponentModel authNExclusive = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(authNExclusiveUid);
			helpNservices.setAuthNexclusiveComp(getHTMLParsedTextContent(authNExclusive.getContent()));
			helpNservices.setAuthNexclusiveImg(authNExclusive.getMedia().getUrl());

			// data from CustomerFirstMediaParagraphComponent
			final CMSMediaParagraphComponentModel customerFirst = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(customerFirstUid);
			helpNservices.setCustomerFirstComp(getHTMLParsedTextContent(customerFirst.getContent()));
			helpNservices.setCustomerFirstImg(customerFirst.getMedia().getUrl());

			// data from BestCollectionMediaParagraphComponent
			final CMSMediaParagraphComponentModel bestCollection = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(bestCollectionUid);
			helpNservices.setBestCollectionComp(getHTMLParsedTextContent(bestCollection.getContent()));
			helpNservices.setBestCollectionImg(bestCollection.getMedia().getUrl());

			// data from DeliveryOnTimeMediaParagraphComponent
			final CMSMediaParagraphComponentModel deliveryonTime = (CMSMediaParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(deliveryonTimeUid);
			helpNservices.setDeliveryComp(getHTMLParsedTextContent(deliveryonTime.getContent()));
			helpNservices.setDeliveryImg(deliveryonTime.getMedia().getUrl());

			// data from HelpServiceFAQPageComponent
			final CMSParagraphComponentModel faqPageComp = (CMSParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(faqPageCompUid);
			helpNservices.setFaqPageComponent(getHTMLParsedTextContent(faqPageComp.getContent()));

			// data from HelpServiceOrderPageComponent
			final CMSParagraphComponentModel faqOrderComp = (CMSParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(faqOrderCompUid);
			helpNservices.setFaqOrderComponent(getHTMLParsedTextContent(faqOrderComp.getContent()));

			//data from HelpServiceRCPageComponent
			final CMSParagraphComponentModel faqReturnComp = (CMSParagraphComponentModel) cmsComponentService
					.getSimpleCMSComponent(faqReturnCompUid);
			helpNservices.setFaqReturnComponent(getHTMLParsedTextContent(faqReturnComp.getContent()));
		}
		catch (final Exception e)
		{
			LOG.error("About Us Page Error: " + e);
			throw new EtailNonBusinessExceptions(e);
		}

		return helpNservices;
	}

	/**
	 * Fetched the shop by department component
	 *
	 * @return DepartmentCollectionComponentModel
	 * @throws CMSItemNotFoundException
	 */
	private DepartmentCollectionComponentModel getShopByDept() throws CMSItemNotFoundException
	{
		String componentUid = null;
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SHOPBYDEPTCOMPONENT)
		{
			componentUid = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SHOPBYDEPTCOMPONENT, "");
		}

		final DepartmentCollectionComponentModel shopByDeptComponent = (DepartmentCollectionComponentModel) cmsComponentService
				.getSimpleCMSComponent(componentUid);
		return shopByDeptComponent;
	}

	/**
	 * Modified time
	 *
	 * @return String
	 * @throws CMSItemNotFoundException
	 */

	private Date getModifiedTime() throws CMSItemNotFoundException
	{
		final DepartmentCollectionComponentModel shopByDeptComponent = getShopByDept();
		Date modifiedTime = null;
		if (null != shopByDeptComponent && null != shopByDeptComponent.getModifiedtime())
		{
			modifiedTime = shopByDeptComponent.getModifiedtime();
		}
		return modifiedTime;
	}


	//************************************Retrieving Catergory name through id and checking the page*****************************

	@Override
	public LandingDetailsforCategoryData getCategoryforCategoryNameUsingId(final String CategoryId)
			throws EtailNonBusinessExceptions
	{
		try
		{
			CategoryModel selectedCategory = new CategoryModel();


			final List<WAHASTTSec2detailsData> secWAHASTTDetails = new ArrayList<WAHASTTSec2detailsData>();
			final List<PromoBannerSecData> promoBannerDetails = new ArrayList<PromoBannerSecData>();
			final List<BITTSec3detailsData> secBITTDetails = new ArrayList<BITTSec3detailsData>();
			final List<BITTSec3detailsData> secBITTVideoDetails = new ArrayList<BITTSec3detailsData>();
			final CategoryLandingDataHierarchy objCLDHdata = new CategoryLandingDataHierarchy();

			if (null != categoryService && null != CategoryId)
			{
				selectedCategory = categoryService.getCategoryForCode(CategoryId);
			}
			String secondrootname = null;

			String cloth = null;
			String women = null;
			String men = null;
			String electroinics = null;


			if (null != configurationService && null != configurationService.getConfiguration()
					&& null != MarketplacecommerceservicesConstants.SALESCLOTH)
			{
				cloth = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESCLOTH, "");
			}

			if (null != configurationService && null != configurationService.getConfiguration()
					&& null != MarketplacecommerceservicesConstants.SALESMEN)
			{
				men = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESMEN, "");
			}
			if (null != configurationService && null != configurationService.getConfiguration()
					&& null != MarketplacecommerceservicesConstants.SALESWOMEN)
			{
				women = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.SALESWOMEN, "");
			}

			if (null != configurationService && null != configurationService.getConfiguration()
					&& null != MarketplacecommerceservicesConstants.SALESELECTRONICS)
			{
				electroinics = configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.SALESELECTRONICS, "");
			}

			final List<CategoryModel> super_category_list = categoryService.getPathForCategory(selectedCategory);
			for (final CategoryModel cattype : super_category_list)
			{
				if (!(cattype instanceof ClassificationClassModel))
				{
					if (null != cattype.getCode()
							&& (cattype.getCode().equalsIgnoreCase(cloth) || cattype.getCode().equalsIgnoreCase(women)
									|| cattype.getCode().equalsIgnoreCase(men) || cattype.getCode().equalsIgnoreCase(electroinics))
							&& null != cattype.getName())
					{
						secondrootname = cattype.getName();
					}
				}
			}

			if (null != categoryService && null != CategoryId)
			{

				final List<AbstractCMSComponentModel> cmsComponentsListforCategoryDetails = getCMSComponentsforCategory(selectedCategory);


				objCLDHdata.setCategoryName(secondrootname);
				objCLDHdata.setCategoryType(selectedCategory.getName());

				for (final AbstractCMSComponentModel cmsComponent : cmsComponentsListforCategoryDetails)
				{

					if ((cmsComponent instanceof MplCategoryCarouselComponentModel))

					{
						if (null != ((MplCategoryCarouselComponentModel) cmsComponent).getTitle())

						{
							final String secWAHASTTtitle = ((MplCategoryCarouselComponentModel) cmsComponent).getTitle();
							objCLDHdata.setSection2NameTitle(secWAHASTTtitle);
						}

						if (null != ((MplCategoryCarouselComponentModel) cmsComponent).getCategories())

						{
							final List<CategoryModel> carouselcategoryList = ((MplCategoryCarouselComponentModel) cmsComponent)
									.getCategories();


							for (final CategoryModel category : carouselcategoryList)
							{

								final WAHASTTSec2detailsData objWAHASTTdata = new WAHASTTSec2detailsData();

								final String description = category.getDescription();
								final String backgroundImageURL = category.getThumbnail().getURL();
								final String ID = category.getCode();
								final String categoryURL = categoryModelUrlResolver.resolve(category);
								//final String name = ((MplCategoryCarouselComponentModel) cmsComponent).getName();


								objWAHASTTdata.setDescription(description);
								objWAHASTTdata.setBackgroundImageURL(backgroundImageURL);
								objWAHASTTdata.setID(ID);
								objWAHASTTdata.setUrl(categoryURL);



								secWAHASTTDetails.add(objWAHASTTdata);
								objCLDHdata.setSection2NameDetails(secWAHASTTDetails);



							}

						}
					}

					if ((cmsComponent instanceof MplBigPromoBannerComponentModel))
					{

						if (null != ((MplBigPromoBannerComponentModel) cmsComponent).getBannerImage())

						{

							final PromoBannerSecData objPBSDdata = new PromoBannerSecData();

							final String majorPromoTextTest = ((MplBigPromoBannerComponentModel) cmsComponent).getMajorPromoText();
							final String minorPromoText = ((MplBigPromoBannerComponentModel) cmsComponent).getMinorPromo1Text();
							final String bannerImageURL = ((MplBigPromoBannerComponentModel) cmsComponent).getBannerImage().getURL();
							//final String ID = ((MplBigPromoBannerComponentModel) cmsComponent).getTypeCode();

							//Parsing HTML tags

							final String majorPromoText = getHTMLParsedTextContent(majorPromoTextTest);


							objPBSDdata.setMajorPromoText(majorPromoText);
							objPBSDdata.setMinorPromoText(minorPromoText);
							objPBSDdata.setBannerImageURL(bannerImageURL);
							promoBannerDetails.add(objPBSDdata);
							objCLDHdata.setPromoBannerDetails(promoBannerDetails);




						}
					}

					if ((cmsComponent instanceof CMSParagraphComponentModel))
					{
						if (null != ((CMSParagraphComponentModel) cmsComponent).getContent())

						{

							final String contentBITTTest = ((CMSParagraphComponentModel) cmsComponent).getContent();
							//final String ID = ((CMSParagraphComponentModel) cmsComponent).getTypeCode();


							//Parsing HTML tags

							final String contentBITT = getHTMLParsedTextContent(contentBITTTest);
							objCLDHdata.setSection3NameContent(contentBITT);






						}
					}

					if ((cmsComponent instanceof SimpleBannerComponentModel))
					{


						if (null != ((SimpleBannerComponentModel) cmsComponent).getMedia())

						{

							final BITTSec3detailsData objBITTdata = new BITTSec3detailsData();


							final String imageDescriptionTest = ((SimpleBannerComponentModel) cmsComponent).getDescription();
							final String thumbnailURL = ((SimpleBannerComponentModel) cmsComponent).getMedia().getURL();
							final String imageTitle = ((SimpleBannerComponentModel) cmsComponent).getTitle();
							final String name = ((SimpleBannerComponentModel) cmsComponent).getName();
							//final String imageID = ((SimpleBannerComponentModel) cmsComponent).getTypeCode();

							//Parsing HTML tags

							final String imageDescription = getHTMLParsedTextContent(imageDescriptionTest);



							objBITTdata.setImageDescription(imageDescription);
							objBITTdata.setImageTitle(imageTitle);
							objBITTdata.setImageURL(thumbnailURL);
							objBITTdata.setName(name);

							secBITTDetails.add(objBITTdata);
							objCLDHdata.setSection3NameDetails(secBITTDetails);


						}
					}

					if ((cmsComponent instanceof VideoComponentModel))
					{


						if (null != ((VideoComponentModel) cmsComponent).getVideoUrl())

						{


							final BITTSec3detailsData objBITTVideodata = new BITTSec3detailsData();

							final String videoDescriptionTest = ((VideoComponentModel) cmsComponent).getVideoDescription();
							final String videoURL = ((VideoComponentModel) cmsComponent).getVideoUrl();
							final String videoTitle = ((VideoComponentModel) cmsComponent).getVideoTitle();
							//	final String name = ((VideoComponentModel) cmsComponent).getName();
							//	final String videoID = ((VideoComponentModel) cmsComponent).getTypeCode();

							//Parsing HTML tags

							final String videoDescription = getHTMLParsedTextContent(videoDescriptionTest);

							objBITTVideodata.setVideoDescription(videoDescription);
							objBITTVideodata.setVideoTitle(videoTitle);
							objBITTVideodata.setVideoURL(videoURL);


							secBITTVideoDetails.add(objBITTVideodata);
							objCLDHdata.setSection3VideoDetails(secBITTVideoDetails);

						}
					}

				}

			}


			final LandingDetailsforCategoryData landingforCategoryData = new LandingDetailsforCategoryData();
			landingforCategoryData.setCategoryComponent(objCLDHdata);

			if (null != landingforCategoryData.getCategoryComponent())
			{
				return landingforCategoryData;
			}
			else
			{
				return null;
			}


		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			throw new EtailNonBusinessExceptions(e);
		}


	}

	//********************************************End of Coding**********************************************************

	//*********************************************Parsing HTML Tags from content****************************************
	@Override
	public String getHTMLParsedTextContent(final String content) throws EtailNonBusinessExceptions
	{
		try

		{
			if (content == null)
			{
				return content;
			}
			final Document document = Jsoup.parse(content);
			document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
			final String strippedContent = document.html().replaceAll("\\s+", " ");
			final String strippedContentFinal = Jsoup.clean(strippedContent, "", Whitelist.none(),
					new Document.OutputSettings().prettyPrint(false));

			return strippedContentFinal.replaceAll("&nbsp;", "");
		}

		catch (final Exception e)
		{
			// YTODO Auto-generated catch block
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			throw new EtailNonBusinessExceptions(e);
		}


	}

	//********************************************End of Coding**********************************************************


	//************************************Fetching cmsComponents*************************************************

	@Override
	public List<AbstractCMSComponentModel> getCMSComponentsforCategory(final CategoryModel selectedCategory)
	{

		ContentPageModel contentPageModel = null;
		List<AbstractCMSComponentModel> cmsComponentsList = new ArrayList<AbstractCMSComponentModel>();
		final List<AbstractCMSComponentModel> cmsComponentsListFinal = new ArrayList<AbstractCMSComponentModel>();


		try
		{
			final String categoryName = selectedCategory.getName();

			if (null != cmsPageService && null != cmsPageService.getPageForLabelOrId(categoryName))
			{

				try
				{

					contentPageModel = cmsPageService.getPageForLabelOrId(categoryName);
					final List<ContentSlotForPageModel> contentSlotsList = contentPageModel.getContentSlots();

					for (final ContentSlotForPageModel contentSlot : contentSlotsList)
					{
						cmsComponentsList = contentSlot.getContentSlot().getCmsComponents();

						for (final AbstractCMSComponentModel cmsComponent : cmsComponentsList)
						{

							cmsComponentsListFinal.add(cmsComponent);

						}
					}
				}
				catch (final CMSItemNotFoundException e)
				{
					LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
					throw new EtailNonBusinessExceptions(e);
				}
			}
		}

		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
			throw new EtailNonBusinessExceptions(e);
		}

		return cmsComponentsListFinal;
	}

}

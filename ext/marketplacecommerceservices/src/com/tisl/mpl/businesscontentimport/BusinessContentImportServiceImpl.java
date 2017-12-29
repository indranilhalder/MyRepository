/**
 *
 */
package com.tisl.mpl.businesscontentimport;

import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.VideoComponentModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplCmsPageService;


/**
 * @author TCS
 *
 */
public class BusinessContentImportServiceImpl implements BusinessContentImportService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BusinessContentImportServiceImpl.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplCmsPageService cmsPageService;
	//Sonar fix
	//StringBuilder sbError;
	StringBuilder sbError = new StringBuilder(1000);


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
	 * @return the businessContentImportDao
	 */
	public BulkContentCreationDao getBusinessContentImportDao()
	{
		return businessContentImportDao;
	}

	/**
	 * @param businessContentImportDao
	 *           the businessContentImportDao to set
	 */
	public void setBusinessContentImportDao(final BulkContentCreationDao businessContentImportDao)
	{
		this.businessContentImportDao = businessContentImportDao;
	}


	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}


	private BulkContentCreationDao businessContentImportDao;

	private ModelService modelService;

	@Autowired
	private CatalogVersionService catalogVersionService;


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


	//@Description: Variable Declaration for Promotions
	private static final int PRODUCTCODE = 0;
	private static final int TEMPLATE = 1;
	private static final int TEMPLATE_HEADING = 2;
	private static final String LUX = "lux";



	@Override
	public String processUpdateForContentImport(final CSVReader reader, final CSVWriter writer, final Map<Integer, String> map,
			final Integer errorPosition, final boolean headerRowIncluded)
	{
		return processUpdateForContentImport(reader, writer, map, errorPosition, headerRowIncluded, null);
	}

	/**
	 * @Description: For Creating Content in bulk,checks for the basic coloumns
	 * @param reader
	 * @param writer
	 * @param map
	 * @param errorPosition
	 * @param headerRowIncluded
	 */
	@Override
	public String processUpdateForContentImport(final CSVReader reader, final CSVWriter writer, final Map<Integer, String> map,
			final Integer errorPosition, final boolean headerRowIncluded, final String site)
	{
		LOG.debug("Generationg Contents..");
		LOG.debug("Error Checking Contents..");
		int lineNo = 0;

		//Sonar-fix
		//sbError = new StringBuilder();

		while (reader.readNextLine())
		{
			lineNo++;
			//To skip header section defined in CSV
			if (lineNo == 1)
			{
				continue;
			}

			final Map<Integer, String> line = reader.getLine();
			final StringBuilder invalidColumns = new StringBuilder();

			final String productCode = line.get(Integer.valueOf(PRODUCTCODE));
			addInvalidColumnName(invalidColumns, "PRODUCTCODE", productCode);

			final String template = line.get(Integer.valueOf(TEMPLATE));
			addInvalidColumnName(invalidColumns, "TEMPLATE", template);

			//checks if there is entry for config in properties file
			final String searchForTemplateInConfig = configurationService.getConfiguration()
					.getString("businessConetnt.Template." + template);
			//add error in column for invalid template names
			if (StringUtils.isNotEmpty(template))
			{
				addInvalidColumnName(invalidColumns, "INVALID_TEMPLATE_NAME", searchForTemplateInConfig);
			}

			//if invalid columns are empty get inside
			if (StringUtils.isEmpty(invalidColumns.toString()))
			{
				if (StringUtils.isNotEmpty(searchForTemplateInConfig))
				{
					int countAfter = 3;// changed to 3 after inclusion of heading template TPR-4060
					final String[] attributeList = searchForTemplateInConfig.split(",");
					final Map<String, String> contentMap = new LinkedHashMap<>();


					for (final String attribute : attributeList)
					{
						contentMap.put(attribute, line.get(Integer.valueOf(countAfter)));
						//addInvalidColumnName(invalidColumns, attribute, line.get(Integer.valueOf(countAfter)));
						countAfter++;
					}

					if (StringUtils.isEmpty(invalidColumns.toString()))
					{
						if (null != site && site.equals(LUX))
						{
							processData(line, writer, contentMap, site);
						}
						else
						{
							processData(line, writer, contentMap);
						}

						continue;
					}
					else
					{
						//writeErrorData(writer, invalidColumns.toString(), line, "MISSING_VALUES", lineNo);
						errorLogger(invalidColumns.toString(), "MISSING_VALUES", productCode);
					}
				}
				continue;
			}
			else
			{
				//writeErrorData(writer, invalidColumns.toString(), line, "MISSING_VALUES", lineNo);
				errorLogger(invalidColumns.toString(), "MISSING_VALUES", productCode);
			}
			//add else to write the error for the file
		} //added for error population HMC
		return sbError.toString();
	}

	/**
	 * @Description: For mapping products against content template in bulk,checks for the basic coloumns
	 * @param reader
	 * @param writer
	 * @param map
	 * @param errorPosition
	 * @param headerRowIncluded
	 */
	@Override
	public void processUpdateForProductMappingImport(final CSVReader reader, final CSVWriter writer,
			final Map<Integer, String> map, final Integer errorPosition, final boolean headerRowIncluded)
	{
		LOG.debug("Mapping to product starts");
		//int lineNo = 0;
		while (reader.readNextLine())
		{
			//lineNo++;
			final Map<Integer, String> line = reader.getLine();
			final StringBuilder invalidColumns = new StringBuilder();

			final String productCode = line.get(Integer.valueOf(PRODUCTCODE));
			addInvalidColumnName(invalidColumns, "PRODUCTCODE", productCode);

			final String templateCode = line.get(Integer.valueOf(TEMPLATE));
			addInvalidColumnName(invalidColumns, "TEMPLATEPRODUCTCODE", templateCode);

			final String action = line.get(Integer.valueOf(2));
			addInvalidColumnName(invalidColumns, "ACTION", action);


			//if invalid columns are empty get inside
			if (StringUtils.isEmpty(invalidColumns.toString()))
			{
				ContentPageModel cm = null;
				final ProductModel product = businessContentImportDao.fetchProductforCode(productCode, null);
				try
				{
					cm = (ContentPageModel) getCmsPageService().getPageForIdandCatalogVersion(templateCode, getCatalogVersion());
					final List<ProductModel> productList = cm.getAssociatedProducts();
					final List<ProductModel> updateProductList = new ArrayList();
					// Deletion of product mapping
					if (action.equalsIgnoreCase("delete"))
					{

						if (productList.contains(product))
						{
							updateProductList.addAll(productList);
							updateProductList.remove(product);
						}
					}
					//addition of product mapping
					if (action.equalsIgnoreCase("add"))
					{
						if (!productList.contains(product))
						{
							updateProductList.add(product);
							updateProductList.addAll(productList);
						}
					}
					if (CollectionUtils.isNotEmpty(updateProductList))
					{
						cm.setAssociatedProducts(updateProductList);
						modelService.save(cm);
					}
				}
				catch (final Exception e)
				{
					LOG.error("No page exist with Uid:" + templateCode + ". Error is " + e.getMessage());
				}
				continue;
			}
			else
			{
				//writeErrorData(writer, invalidColumns.toString(), line, "MISSING_VALUES", lineNo);
				errorLogger(invalidColumns.toString(), "MISSING_VALUES", productCode);
			}
		}
	}

	private void processData(final Map<Integer, String> line, final CSVWriter writer, final Map<String, String> contentMap)
	{
		processData(line, writer, contentMap, null);
	}

	/**
	 * @Description: To process data for Content
	 * @param line
	 * @param writer
	 * @param contentMap
	 */
	private void processData(final Map<Integer, String> line, final CSVWriter writer, final Map<String, String> contentMap,
			final String site)
	{
		LOG.debug("Processing Content Data");
		try
		{
			//Check If Already Present
			final ProductModel product = businessContentImportDao.fetchProductforCode(line.get(Integer.valueOf(PRODUCTCODE)), site);
			String title = product.getTitle();
			if (title.length() > 220)
			{
				title = title.substring(0, 220);
			}
			final String template = line.get(Integer.valueOf(TEMPLATE));
			final String uid = makeUid(product.getCode(), title, template);
			List<AbstractCMSComponentModel> componentlist;

			try
			{
				ContentPageModel cmodel = null;
				if (null != site && site.equals(LUX))
				{
					cmodel = (ContentPageModel) getCmsPageService().getPageForIdandCatalogVersion(uid, getLuxCatalogVersion());
				}
				else
				{
					cmodel = (ContentPageModel) getCmsPageService().getPageForIdandCatalogVersion(uid, getCatalogVersion());

				}

				LOG.debug(cmodel); // To prevent sonar defect
				componentlist = makeComponents(contentMap, line, writer, true, site);
			}
			catch (final CMSItemNotFoundException e)
			{
				LOG.error("No page Exist..Making new page with Uid:" + uid + "Error is " + e.getMessage());
				componentlist = makeComponents(contentMap, line, writer, false, site);
				//Make Content Page
				final ContentPageModel cm = makeContentPageforProduct(line, writer, site);

				//Making content slot and assigning components
				final List<ContentSlotModel> cSlotList = makeContentSlot(line, writer, componentlist, site);

				//Make Content Slot for Page and assigning content slots to ContentPageModel
				makeCotentSlotforPage(cm, cSlotList, line, writer, site);
				//}
			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException exception)
		{
			final List<Integer> errorColumnList = errorListData(true);
			LOG.error("Exception in processing processData" + exception.getMessage());
			//populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "PRODUCT_CODE_NOT_FOUND", line.get(Integer.valueOf(PRODUCTCODE)));

		}
	}

	/**
	 * @return the cmsPageService
	 */
	public MplCmsPageService getCmsPageService()
	{
		return cmsPageService;
	}

	/**
	 * @param cmsPageService
	 *           the cmsPageService to set
	 */
	public void setCmsPageService(final MplCmsPageService cmsPageService)
	{
		this.cmsPageService = cmsPageService;
	}

	/**
	 * @Description: Creates content Page
	 * @param line
	 * @param writer
	 * @return ContentPageModel cm
	 */
	ContentPageModel makeContentPageforProduct(final Map<Integer, String> line, final CSVWriter writer, final String site)
	{
		LOG.debug("Making Content Page...");
		final List<Integer> errorColumnList = errorListData(false);
		ContentPageModel cm = null;
		try
		{
			final String productCode = line.get(Integer.valueOf(PRODUCTCODE));


			final List<ProductModel> productList = new ArrayList<>();
			productList.add(businessContentImportDao.fetchProductforCode(productCode, site));
			final String template = line.get(Integer.valueOf(TEMPLATE));
			final String templateName = configurationService.getConfiguration().getString("businessConetnt.Template.namePrefix")
					+ template;
			String title = productList.get(0).getTitle();
			if (title.length() > 220)
			{
				title = title.substring(0, 220);
			}
			final String templateHeading = line.get(Integer.valueOf(TEMPLATE_HEADING));//TPR- 4060

			final String uid = makeUid(productCode, title, template);


			final String label = configurationService.getConfiguration()
					.getString("businessConetnt.Template." + template + ".label");


			final PageTemplateModel templateModel = businessContentImportDao.fetchPageTemplate(templateName, site);

			if (templateModel != null && CollectionUtils.isNotEmpty(productList))
			{
				cm = new ContentPageModel();
				if (StringUtils.isNotEmpty(templateHeading))
				{
					cm.setTitle(templateHeading, Locale.US);//TPR-4060
				}
				cm.setUid(uid);
				cm.setName(uid);
				cm.setMasterTemplate(templateModel);
				cm.setDefaultPage(Boolean.TRUE);
				cm.setApprovalStatus(CmsApprovalStatus.APPROVED);
				cm.setHomepage(false);
				cm.setLabel(label);
				cm.setAssociatedProducts(productList);
				if (null != site && site.equals(LUX))
				{
					cm.setCatalogVersion(getLuxCatalogVersion());
				}
				else
				{
					cm.setCatalogVersion(getCatalogVersion());
				}

				modelService.save(cm);
			}


		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making ContentPage in makeContentPageforProduct " + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "ERROR_MAKING_CONTENT_PAGE", line.get(Integer.valueOf(PRODUCTCODE)));

		}
		catch (final Exception e)
		{
			LOG.error("Problem while Making ContentPage in makeContentPageforProduct " + e.getMessage());
			//populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "ERROR_MAKING_CONTENT_PAGE", line.get(Integer.valueOf(PRODUCTCODE)));
		}

		return cm;

	}

	/**
	 * @Description This method creates the components as recieved from contentMap
	 * @param contentMap
	 * @param line
	 * @param writer
	 * @param isUpdatefeed
	 * @return List<AbstractCMSComponentModel>
	 */
	List<AbstractCMSComponentModel> makeComponents(final Map<String, String> contentMap, final Map<Integer, String> line,
			final CSVWriter writer, final boolean isUpdatefeed, final String site)
	{
		final List<AbstractCMSComponentModel> componentlist = new ArrayList<>();
		//Making Components
		for (final Map.Entry<String, String> entry : contentMap.entrySet())
		{
			if (entry.getKey().startsWith("Image"))
			{
				SimpleBannerComponentModel sm = null;
				if (StringUtils.isNotEmpty(entry.getValue()))
				{
					sm = makeBannerComponent(entry.getValue(), entry.getKey(), line, writer, isUpdatefeed, site);
				}
				componentlist.add(sm);
			}
			else if (entry.getKey().startsWith("Video"))
			{
				VideoComponentModel vm = null;
				if (StringUtils.isNotEmpty(entry.getValue()))
				{
					vm = makeVideoComponent(entry.getValue(), entry.getKey(), line, writer, isUpdatefeed, site);
				}
				componentlist.add(vm);

			}
			else if (entry.getKey().startsWith("Text"))
			{
				CMSParagraphComponentModel cmspara = null;
				if (StringUtils.isNotEmpty(entry.getValue()))
				{
					cmspara = makeTextComponent(entry.getValue(), entry.getKey(), line, writer, isUpdatefeed, site);
				}
				componentlist.add(cmspara);
			}
		}
		return componentlist;
	}

	/**
	 * @Description: Creates SimpleBannerComponent
	 * @param imageUrl
	 * @param attributeName
	 * @param line
	 * @param writer
	 * @return SimpleBannerComponentModel sm
	 */
	SimpleBannerComponentModel makeBannerComponent(final String imageUrl, final String attributeName,
			final Map<Integer, String> line, final CSVWriter writer, final boolean isUpdatefeed, final String site)
	{
		LOG.debug("Making SimpleBannerComponent ...");
		final List<Integer> errorColumnList = errorListData(false);
		final String uid = makeUid(line.get(Integer.valueOf(PRODUCTCODE)), line.get(Integer.valueOf(TEMPLATE)), attributeName);
		SimpleBannerComponentModel sm = null;
		try
		{
			if (!isUpdatefeed)
			{
				try
				{
					sm = businessContentImportDao.getSimpleBannerComponentforUid(uid, site);
					sm.setUrlLink(imageUrl);
					modelService.save(sm);
				}
				catch (final ModelSavingException | ModelNotFoundException exception)
				{
					sm = new SimpleBannerComponentModel();
					sm.setUid(uid);
					sm.setName(uid);
					sm.setUrlLink(imageUrl);
					if (null != site && site.equals(LUX))
					{
						sm.setCatalogVersion(getLuxCatalogVersion());
					}
					else
					{
						sm.setCatalogVersion(getCatalogVersion());
					}

					modelService.save(sm);
				}
			}
			else
			{
				sm = businessContentImportDao.getSimpleBannerComponentforUid(uid, site);
				sm.setUrlLink(imageUrl);
				modelService.save(sm);
			}
		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making SimpleBannerComponent" + exception.getMessage());
			//populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "ERROR_MAKING_BANNER", line.get(Integer.valueOf(PRODUCTCODE)));
		}
		return sm;

	}

	/**
	 * @Description Make a video banner component model
	 * @param videoUrl
	 * @param attributeName
	 * @param line
	 * @param writer
	 * @param isUpdatefeed
	 * @return VideoComponentModel
	 */
	VideoComponentModel makeVideoComponent(final String videoUrl, final String attributeName, final Map<Integer, String> line,
			final CSVWriter writer, final boolean isUpdatefeed, final String site)
	{
		LOG.debug("Making VideoComponent ...");
		final List<Integer> errorColumnList = errorListData(false);
		final String uid = makeUid(line.get(Integer.valueOf(PRODUCTCODE)), line.get(Integer.valueOf(TEMPLATE)), attributeName);
		VideoComponentModel vm = null;
		try
		{
			if (!isUpdatefeed)
			{
				try
				{
					vm = businessContentImportDao.getVideoComponentforUid(uid, site);
					vm.setVideoUrl(videoUrl);
					modelService.save(vm);
				}
				catch (final ModelSavingException | ModelNotFoundException exception)
				{
					vm = new VideoComponentModel();
					vm.setUid(uid);
					vm.setName(uid);
					vm.setVideoUrl(videoUrl);
					if (null != site && site.equals(LUX))
					{
						vm.setCatalogVersion(getLuxCatalogVersion());
					}
					else
					{
						vm.setCatalogVersion(getCatalogVersion());
					}
					modelService.save(vm);
				}
			}
			else
			{
				vm = businessContentImportDao.getVideoComponentforUid(uid, site);
				vm.setVideoUrl(videoUrl);
				modelService.save(vm);
			}


		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making VideoComponent" + exception.getMessage());
			//populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "ERROR_MAKING_VIDEO", line.get(Integer.valueOf(PRODUCTCODE)));
		}
		return vm;

	}

	/**
	 * @Description makeTextComponent
	 * @param content
	 * @param attributeName
	 * @param line
	 * @param writer
	 * @param isUpdatefeed
	 * @return CMSParagraphComponentModel
	 */
	CMSParagraphComponentModel makeTextComponent(final String content, final String attributeName, final Map<Integer, String> line,
			final CSVWriter writer, final boolean isUpdatefeed, final String site)
	{
		LOG.debug("Making CMSParagraphComponentModel ...");
		final List<Integer> errorColumnList = errorListData(false);
		final String uid = makeUid(line.get(Integer.valueOf(PRODUCTCODE)), line.get(Integer.valueOf(TEMPLATE)), attributeName);
		CMSParagraphComponentModel cmsPara = null;
		final Locale loc = new Locale("en");
		try
		{
			if (!isUpdatefeed)
			{
				try
				{
					cmsPara = businessContentImportDao.getCMSParagraphComponentforUid(uid, site);
					cmsPara.setContent(content, loc);
					modelService.save(cmsPara);
				}
				catch (final ModelSavingException | ModelNotFoundException exception)
				{
					cmsPara = new CMSParagraphComponentModel();
					cmsPara.setUid(uid);
					cmsPara.setName(uid);
					cmsPara.setContent(content, loc);
					if (null != site && site.equals(LUX))
					{
						cmsPara.setCatalogVersion(getLuxCatalogVersion());
					}
					else
					{
						cmsPara.setCatalogVersion(getCatalogVersion());
					}
					//Saving the Model
					modelService.save(cmsPara);
				}
			}
			else
			{
				cmsPara = businessContentImportDao.getCMSParagraphComponentforUid(uid, site);
				cmsPara.setContent(content, loc);
				modelService.save(cmsPara);
			}

		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making CMSParagraphComponentModel" + exception.getMessage());
			//populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "ERROR_MAKING_PARAGRAPH", line.get(Integer.valueOf(PRODUCTCODE)));
		}
		return cmsPara;

	}

	/**
	 * @Description makeContentSlot
	 * @param line
	 * @param writer
	 * @param componentlist
	 * @return List<ContentSlotModel>
	 */
	List<ContentSlotModel> makeContentSlot(final Map<Integer, String> line, final CSVWriter writer,
			final List<AbstractCMSComponentModel> componentlist, final String site)
	{
		LOG.debug("Making Content Slot ...");
		final List<Integer> errorColumnList = errorListData(false);
		final List<ContentSlotModel> cSlotList = new ArrayList<>();
		final List<ContentSlotModel> cSlotListReturn = new ArrayList<>();
		ContentSlotModel cSlot = null;
		try
		{
			final String productCode = line.get(Integer.valueOf(PRODUCTCODE));
			final String template = line.get(Integer.valueOf(TEMPLATE));
			int index = 0;
			for (final AbstractCMSComponentModel component : componentlist)
			{
				if (component != null)
				{
					final List<AbstractCMSComponentModel> tempcomponentlist = new ArrayList<>();
					cSlot = new ContentSlotModel();
					cSlot.setUid(productCode + "_" + template + "_" + component.getUid());
					cSlot.setName(productCode + "_" + template + "_" + component.getUid());
					tempcomponentlist.add(component);
					cSlot.setActive(Boolean.TRUE);
					cSlot.setCmsComponents(tempcomponentlist);
					if (null != site && site.equals(LUX))
					{
						cSlot.setCatalogVersion(getLuxCatalogVersion());
					}
					else
					{
						cSlot.setCatalogVersion(getCatalogVersion());
					}

					cSlotList.add(cSlot);
					cSlotListReturn.add(index, cSlot);
				}
				else
				{
					cSlotListReturn.add(index, null);
				}
				index++;

			}
			modelService.saveAll(cSlotList);
		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making Content Slot" + exception.getMessage());
			//populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "ERROR_MAKING_SLOT", line.get(Integer.valueOf(PRODUCTCODE)));
		}
		//return cSlotList;
		return cSlotListReturn;
	}

	/**
	 * @Description makeCotentSlotforPage
	 * @param cm
	 * @param cSlotList
	 * @param line
	 * @param writer
	 */
	void makeCotentSlotforPage(final ContentPageModel cm, final List<ContentSlotModel> cSlotList, final Map<Integer, String> line,
			final CSVWriter writer, final String site)
	{
		LOG.debug("Making Content Slot for Page ...");

		final String template = line.get(Integer.valueOf(TEMPLATE));

		final List<Integer> errorColumnList = errorListData(false);
		final List<ContentSlotForPageModel> cSlotPageList = new ArrayList<>();
		final String sectionList[] = configurationService.getConfiguration()
				.getString("businessContent.Template." + template + ".Sections").split(",");

		ContentSlotForPageModel cSlotPage = null;
		try
		{
			int sectionCounter = 0;
			for (final ContentSlotModel contentSlot : cSlotList)
			{
				if (contentSlot != null)
				{
					cSlotPage = new ContentSlotForPageModel();
					cSlotPage.setUid(sectionList[sectionCounter] + contentSlot.getUid());
					cSlotPage.setPosition(sectionList[sectionCounter]);
					cSlotPage.setPage(cm);
					cSlotPage.setContentSlot(contentSlot);
					if (null != site && site.equals(LUX))
					{
						cSlotPage.setCatalogVersion(getLuxCatalogVersion());
					}
					else
					{
						cSlotPage.setCatalogVersion(getCatalogVersion());
					}

					cSlotPageList.add(cSlotPage);
				}
				sectionCounter++;
			}

			modelService.saveAll(cSlotPageList);
		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making Content Slot for Page" + exception.getMessage());
			//populateErrorEntry(line, writer, errorColumnList);
			errorLogger(errorColumnList.toString(), "ERROR_MAKING_SLOT_FOR_PAGE", line.get(Integer.valueOf(PRODUCTCODE)));
		}

	}

	/**
	 * @Description Populating error entries for logging into error csv
	 * @param line
	 * @param writer
	 * @param errorColumnList
	 */
	private void populateErrorEntry(final Map<Integer, String> line, final CSVWriter writer, final List<Integer> errorColumnList)
	{
		try
		{
			writeErrorData(writer, line, errorColumnList, line, MarketplacecommerceservicesConstants.ERRORMESSAGE);
		}
		catch (final IOException e)
		{
			LOG.error("IO exception occured in populateErrorEntry " + e.getMessage());
		}
	}

	/**
	 * @Description: Validates Data Uploaded
	 * @param isIncorrectCode
	 * @return data
	 */
	private List<Integer> errorListData(final boolean isIncorrectCode)
	{
		final List<Integer> data = new ArrayList<>();

		if (isIncorrectCode)
		{
			data.add(Integer.valueOf(PRODUCTCODE));
		}
		return data;
	}

	/**
	 * Return the Marketplace Staged Content Catalog
	 *
	 * @return catalogVersionModel
	 */
	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CONTENT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CONTENT_CATALOG_VERSION);
		return catalogVersionModel;
	}


	/**
	 * Return the Lux Staged Content Catalog
	 *
	 * @return catalogVersionModel
	 */
	private CatalogVersionModel getLuxCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.LUX_IMPORT_CONTENT_CATALOG_ID,
				MarketplacecommerceservicesConstants.LUX_IMPORT_CONTENT_CATALOG_VERSION);
		return catalogVersionModel;
	}


	/**
	 * @Description : Prints Error Message to file
	 * @param writer
	 * @param errorHeaderLine
	 * @param errorColumnList
	 * @param line
	 * @param errorMessage
	 * @throws IOException
	 */
	public void writeErrorData(final CSVWriter writer, final Map<Integer, String> errorHeaderLine,
			final List<Integer> errorColumnList, final Map<Integer, String> line, final String errorMessage) throws IOException
	{
		writer.write(errorHeaderLine);
		for (int i = 0; i < errorColumnList.size(); i++)
		{
			line.put(errorColumnList.get(i), errorMessage);
		}
		writer.writeComment("columnName," + "errorMessage ," + "Line");
		writer.write(line);
	}

	/**
	 * @Description : Prints Error Message to file
	 * @param writer
	 * @param line
	 * @param errorMessage
	 * @throws IOException
	 */
	public void writeErrorData(final CSVWriter writer, final String errorColumn, final Map<Integer, String> line,
			final String errorMessage) throws IOException
	{
		line.put(Integer.valueOf(0), errorColumn);
		line.put(Integer.valueOf(1), errorMessage);
		writer.writeComment("columnName," + "errorMessage ," + "Line");
		writer.write(line);
	}

	/**
	 * @Description : Prints Error Message to file
	 * @param writer
	 * @param line
	 * @param errorMessage
	 * @throws IOException
	 */
	public void writeErrorData(final CSVWriter writer, final String errorColumn, final Map<Integer, String> line,
			final String errorMessage, final int lineNo) throws IOException
	{
		final Integer lineNoCsv = Integer.valueOf(lineNo);
		line.put(Integer.valueOf(0), errorColumn);
		line.put(Integer.valueOf(1), errorMessage);
		line.put(Integer.valueOf(2), lineNoCsv.toString());
		writer.writeComment("columnName," + "errorMessage ," + "Line No." + ",Line");
		writer.write(line);
	}

	/**
	 * @desc errorLogger added for getting the exact error in HMC
	 * @param errorColumn
	 * @param errorMessage
	 * @param productCode
	 */
	public void errorLogger(final String errorColumn, final String errorMessage, final String productCode)
	{
		//sonar fix
		sbError.append("COLUMN_NAME,").append("ERROR_MESSAGE ,").append("PRODUCT_ID");
		sbError.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);
		sbError.append(errorColumn).append(MarketplacecommerceservicesConstants.COMMA).append(errorMessage)
				.append(MarketplacecommerceservicesConstants.COMMA).append(productCode);
		sbError.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);
	}



	/**
	 * @Description : Checks for Invalid Column
	 * @param invalidColumns
	 * @param columnName
	 * @param columnValue
	 */
	public void addInvalidColumnName(final StringBuilder invalidColumns, final String columnName, final String columnValue)
	{
		if (StringUtils.isEmpty(columnValue))
		{
			if (StringUtils.isEmpty(invalidColumns.toString()))
			{
				invalidColumns.append(columnName);
			}
			else
			{
				invalidColumns.append('-');
				invalidColumns.append(columnName);
			}
		}
	}

	/**
	 * @Description Make the UID for template
	 * @param param1
	 * @param param2
	 * @param param3
	 * @return String
	 */
	public String makeUid(final String param1, final String param2, final String param3)
	{
		return param1 + "_" + param2 + "_" + param3;
	}
}

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

	/**
	 * @Description: For Creating Content in bulk,checks for the basic coloumns
	 * @param reader
	 * @param writer
	 * @param map
	 * @param errorPosition
	 * @param headerRowIncluded
	 */
	@Override
	public void processUpdateForContentImport(final CSVReader reader, final CSVWriter writer, final Map<Integer, String> map,
			final Integer errorPosition, final boolean headerRowIncluded)
	{
		LOG.debug("Generationg Contents..");
		LOG.debug("Error Checking Contents..");
		while (reader.readNextLine())
		{
			final Map<Integer, String> line = reader.getLine();
			final StringBuilder invalidColumns = new StringBuilder();

			final String productCode = line.get(Integer.valueOf(PRODUCTCODE));
			addInvalidColumnName(invalidColumns, "PRODUCTCODE", productCode);

			final String template = line.get(Integer.valueOf(TEMPLATE));
			addInvalidColumnName(invalidColumns, "TEMPLATE", template);

			//checks if there is entry for config in properties file
			final String searchForTemplateInConfig = configurationService.getConfiguration().getString(
					"businessConetnt.Template." + template);
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
					int countAfter = 2;
					final String[] attributeList = searchForTemplateInConfig.split(",");
					final Map<String, String> contentMap = new LinkedHashMap<>();


					for (final String attribute : attributeList)
					{
						contentMap.put(attribute, line.get(Integer.valueOf(countAfter)));
						addInvalidColumnName(invalidColumns, attribute, line.get(Integer.valueOf(countAfter)));
						countAfter++;
					}

					if (StringUtils.isEmpty(invalidColumns.toString()))
					{
						processData(line, writer, contentMap);
						continue;
					}
					else
					{
						try
						{
							writeErrorData(writer, invalidColumns.toString(), line, "MISSING_VALUES");
						}
						catch (final IOException e)
						{
							LOG.error("IOException Occured " + e.getMessage());
						}
					}
				}
				continue;
			}
			else
			{
				try
				{
					writeErrorData(writer, invalidColumns.toString(), line, "MISSING_VALUES");
				}
				catch (final IOException e)
				{
					LOG.error("IOException Occured " + e.getMessage());
				}
			}
			//add else to write the error for the file
		}
	}

	/**
	 * @Description: To process data for Content
	 * @param line
	 * @param writer
	 * @param contentMap
	 */
	private void processData(final Map<Integer, String> line, final CSVWriter writer, final Map<String, String> contentMap)
	{
		LOG.debug("Processing Content Data");
		final boolean isIncorrectCode = false;
		try
		{
			//Check If Already Present
			final ProductModel product = businessContentImportDao.fetchProductforCode(line.get(Integer.valueOf(PRODUCTCODE)));
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
				final ContentPageModel cmodel = (ContentPageModel) getCmsPageService().getPageForIdandCatalogVersion(uid,
						getCatalogVersion());
				LOG.debug(cmodel);
				componentlist = makeComponents(contentMap, line, writer, true);
			}
			catch (final CMSItemNotFoundException e)
			{
				LOG.error("No page Exist..Making new page with Uid:" + uid + "Error is " + e.getMessage());
				componentlist = makeComponents(contentMap, line, writer, false);
				//Make Content Page
				final ContentPageModel cm = makeContentPageforProduct(line, writer);

				//Making content slot and assigning components
				final List<ContentSlotModel> cSlotList = makeContentSlot(line, writer, componentlist);

				//Make Content Slot for Page and assigning content slots to ContentPageModel
				makeCotentSlotforPage(cm, cSlotList, line, writer);
				//}
			}
		}
		catch (final ModelSavingException | ModelNotFoundException | NumberFormatException exception)
		{
			final List<Integer> errorColumnList = errorListData(isIncorrectCode);
			LOG.error("Exception in processing processData" + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
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
	ContentPageModel makeContentPageforProduct(final Map<Integer, String> line, final CSVWriter writer)
	{
		LOG.debug("Making Content Page...");
		final List<Integer> errorColumnList = errorListData(false);
		ContentPageModel cm = null;
		try
		{
			final String productCode = line.get(Integer.valueOf(PRODUCTCODE));


			final List<ProductModel> productList = new ArrayList<>();
			productList.add(businessContentImportDao.fetchProductforCode(productCode));
			final String template = line.get(Integer.valueOf(TEMPLATE));
			final String templateName = configurationService.getConfiguration().getString("businessConetnt.Template.namePrefix")
					+ template;
			String title = productList.get(0).getTitle();
			if (title.length() > 220)
			{
				title = title.substring(0, 220);
			}
			final String uid = makeUid(productCode, title, template);


			final String label = configurationService.getConfiguration()
					.getString("businessConetnt.Template." + template + ".label");


			final PageTemplateModel templateModel = businessContentImportDao.fetchPageTemplate(templateName);

			if (templateModel != null && CollectionUtils.isNotEmpty(productList))
			{
				cm = new ContentPageModel();
				cm.setUid(uid);
				cm.setName(uid);
				cm.setMasterTemplate(templateModel);
				cm.setDefaultPage(Boolean.TRUE);
				cm.setApprovalStatus(CmsApprovalStatus.APPROVED);
				cm.setHomepage(false);
				cm.setLabel(label);
				cm.setAssociatedProducts(productList);
				cm.setCatalogVersion(getCatalogVersion());
				modelService.save(cm);

			}


		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making ContentPage in makeContentPageforProduct " + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
		}
		catch (final Exception e)
		{
			LOG.error("Problem while Making ContentPage in makeContentPageforProduct " + e.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
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
			final CSVWriter writer, final boolean isUpdatefeed)
	{
		final List<AbstractCMSComponentModel> componentlist = new ArrayList<>();
		//Making Components
		for (final Map.Entry<String, String> entry : contentMap.entrySet())
		{
			if (entry.getKey().startsWith("Image"))
			{
				final SimpleBannerComponentModel sm = makeBannerComponent(entry.getValue(), entry.getKey(), line, writer,
						isUpdatefeed);
				componentlist.add(sm);
			}
			else if (entry.getKey().startsWith("Video"))
			{
				final VideoComponentModel vm = makeVideoComponent(entry.getValue(), entry.getKey(), line, writer, isUpdatefeed);
				componentlist.add(vm);

			}
			else if (entry.getKey().startsWith("Text"))
			{
				final CMSParagraphComponentModel cmspara = makeTextComponent(entry.getValue(), entry.getKey(), line, writer,
						isUpdatefeed);
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
			final Map<Integer, String> line, final CSVWriter writer, final boolean isUpdatefeed)
	{
		LOG.debug("Making SimpleBannerComponent ...");
		final List<Integer> errorColumnList = errorListData(false);
		final String uid = makeUid(line.get(Integer.valueOf(PRODUCTCODE)), line.get(Integer.valueOf(TEMPLATE)), attributeName);
		SimpleBannerComponentModel sm = null;
		try
		{
			if (!isUpdatefeed)
			{
				sm = new SimpleBannerComponentModel();
				sm.setUid(uid);
				sm.setName(uid);
				sm.setUrlLink(imageUrl);
				sm.setCatalogVersion(getCatalogVersion());
				modelService.save(sm);
			}
			else
			{
				sm = businessContentImportDao.getSimpleBannerComponentforUid(uid);
				sm.setUrlLink(imageUrl);
				modelService.save(sm);
			}
		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making SimpleBannerComponent" + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
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
			final CSVWriter writer, final boolean isUpdatefeed)
	{
		LOG.debug("Making VideoComponent ...");
		final List<Integer> errorColumnList = errorListData(false);
		final String uid = makeUid(line.get(Integer.valueOf(PRODUCTCODE)), line.get(Integer.valueOf(TEMPLATE)), attributeName);
		VideoComponentModel vm = null;
		try
		{
			if (!isUpdatefeed)
			{
				vm = new VideoComponentModel();
				vm.setUid(uid);
				vm.setName(uid);
				vm.setVideoUrl(videoUrl);
				vm.setCatalogVersion(getCatalogVersion());
				modelService.save(vm);
			}
			else
			{
				vm = businessContentImportDao.getVideoComponentforUid(uid);
				vm.setVideoUrl(videoUrl);
				modelService.save(vm);
			}


		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making VideoComponent" + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
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
	CMSParagraphComponentModel makeTextComponent(final String content, final String attributeName,
			final Map<Integer, String> line, final CSVWriter writer, final boolean isUpdatefeed)
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
				cmsPara = new CMSParagraphComponentModel();
				cmsPara.setUid(uid);
				cmsPara.setName(uid);
				cmsPara.setContent(content, loc);
				cmsPara.setCatalogVersion(getCatalogVersion());

				//Saving the Model
				modelService.save(cmsPara);
			}
			else
			{
				cmsPara = businessContentImportDao.getCMSParagraphComponentforUid(uid);
				cmsPara.setContent(content, loc);
				modelService.save(cmsPara);
			}

		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making CMSParagraphComponentModel" + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
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
			final List<AbstractCMSComponentModel> componentlist)
	{
		LOG.debug("Making Content Slot ...");
		final List<Integer> errorColumnList = errorListData(false);
		final List<ContentSlotModel> cSlotList = new ArrayList<>();
		ContentSlotModel cSlot = null;
		try
		{
			final String productCode = line.get(Integer.valueOf(PRODUCTCODE));
			final String template = line.get(Integer.valueOf(TEMPLATE));
			for (final AbstractCMSComponentModel component : componentlist)
			{
				final List<AbstractCMSComponentModel> tempcomponentlist = new ArrayList<>();
				cSlot = new ContentSlotModel();
				cSlot.setUid(productCode + "_" + template + "_" + component.getUid());
				cSlot.setName(productCode + "_" + template + "_" + component.getUid());
				tempcomponentlist.add(component);
				cSlot.setActive(Boolean.TRUE);
				cSlot.setCmsComponents(tempcomponentlist);
				cSlot.setCatalogVersion(getCatalogVersion());
				cSlotList.add(cSlot);

			}
			modelService.saveAll(cSlotList);
		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making Content Slot" + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
		}
		return cSlotList;
	}

	/**
	 * @Description makeCotentSlotforPage
	 * @param cm
	 * @param cSlotList
	 * @param line
	 * @param writer
	 */
	void makeCotentSlotforPage(final ContentPageModel cm, final List<ContentSlotModel> cSlotList, final Map<Integer, String> line,
			final CSVWriter writer)
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

				cSlotPage = new ContentSlotForPageModel();
				cSlotPage.setUid(sectionList[sectionCounter] + contentSlot.getUid());
				cSlotPage.setPosition(sectionList[sectionCounter]);
				cSlotPage.setPage(cm);
				cSlotPage.setContentSlot(contentSlot);
				cSlotPage.setCatalogVersion(getCatalogVersion());
				cSlotPageList.add(cSlotPage);
				sectionCounter++;
			}

			modelService.saveAll(cSlotPageList);
		}
		catch (final ModelSavingException | ModelNotFoundException exception)
		{
			LOG.error("Problem while Making Content Slot for Page" + exception.getMessage());
			populateErrorEntry(line, writer, errorColumnList);
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

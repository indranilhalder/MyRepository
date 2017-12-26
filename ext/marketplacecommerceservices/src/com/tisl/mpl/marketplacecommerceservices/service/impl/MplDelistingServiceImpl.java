package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MarketplaceDelistModel;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pojo.DelistDTO;
import com.tisl.mpl.pojo.ProductDelistingDTO;
import com.tisl.mpl.pojo.RecordsetDTO;
import com.tisl.mpl.pojo.SellerDelistingDTO;


/**
 * @author TCS
 */
//TISPRD-207 Changes

public class MplDelistingServiceImpl implements MplDelistingService
{
	@Autowired
	private MplDelistingDao mplDelistingDao;

	@Autowired
	private ModelService modelService;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplDelistingServiceImpl.class.getName());


	private final static String SELLERID = "Seller ID:";
	private final static String USSID = "USSID :";


	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	/**
	 * @return the mplDelistingDao
	 */
	public MplDelistingDao getMplDelistingDao()
	{
		return mplDelistingDao;
	}



	/**
	 * @param mplDelistingDao
	 *           the mplDelistingDao to set
	 */
	public void setMplDelistingDao(final MplDelistingDao mplDelistingDao)
	{
		this.mplDelistingDao = mplDelistingDao;
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



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService#getAllUSSIDforSeller(java.util.List)
	 */
	@Override
	public List<SellerInformationModel> getAllUSSIDforSeller(final String sellerId)
	{
		List<SellerInformationModel> sellerinfoList = new ArrayList<>();

		if (StringUtils.isNotEmpty(sellerId))

		{
			sellerinfoList = mplDelistingDao.getAllUSSIDforSeller(sellerId);
		}
		else
		{
			LOG.error("Seller ID is Not Present");
		}

		return sellerinfoList;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService#delistSeller(java.util.List,
	 * java.lang.String, java.lang.String)
	 * 
	 * @Javadoc
	 * 
	 * @ Description : Delist Based on Seller Id
	 * 
	 * @param : sellerModelList(List<SellerInformationModel>)
	 * 
	 * @param : delisting(String)
	 * 
	 * @param : blockOMS(String)
	 */
	@Override
	public void delistSeller(final List<SellerInformationModel> sellerModelList, final String delisting, final String blockOMS)
	{

		try
		{
			final List<SellerInformationModel> sellerModelSavingList = new ArrayList<>();
			for (final SellerInformationModel sellerModel : sellerModelList)
			{

				final StringBuilder stB = new StringBuilder(100);
				stB.append(SELLERID).append(sellerModel.getSellerID()).append(USSID).append(sellerModel.getSellerArticleSKU())
						.append("trying to delist.");

				LOG.info(stB.toString());

				if ("Y".equalsIgnoreCase(delisting))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("no"));
				}
				else if ("N".equalsIgnoreCase(delisting))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("yes"));
				}
				sellerModel.setBlockOMS(blockOMS);
				//Delisting Changes TISPRD-5345
				sellerModel.setDelistDate(defferedDate());
				sellerModelSavingList.add(sellerModel);
				LOG.info(SELLERID + sellerModel.getSellerID() + USSID + sellerModel.getSellerArticleSKU() + "has been delisted");
			}

			modelService.saveAll(sellerModelSavingList);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}

	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService#delistUSSID(java.util.List,
	 * java.lang.String, java.lang.String)
	 * 
	 * @Javadoc
	 * 
	 * @ Description : Delist Based on USSID
	 * 
	 * @param : sellerModelList(List<SellerInformationModel>)
	 * 
	 * @param : delisting(String)
	 */

	@Override
	public boolean delistUSSID(final List<SellerInformationModel> sellerModelList, final String delisting)
	{
		boolean successfulDelisting = true;
		try
		{
			final List<SellerInformationModel> sellerModelSavingList = new ArrayList<>();


			for (final SellerInformationModel sellerModel : sellerModelList)
			{
				final StringBuilder stB = new StringBuilder(100);

				stB.append(SELLERID).append(sellerModel.getSellerID()).append(USSID).append(sellerModel.getSellerArticleSKU())
						.append("trying to delist.");

				//				LOG.info("Seller ID:" + sellerModel.getSellerID() + "USSID :" + sellerModel.getSellerArticleSKU()
				//						+ "trying to delist");
				if ("Y".equalsIgnoreCase(delisting))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("no"));
				}
				else if ("N".equalsIgnoreCase(delisting))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("yes"));
				}
				//Delisting Changes TISPRD-5345
				sellerModel.setDelistDate(defferedDate());

				sellerModelSavingList.add(sellerModel);

				stB.append(SELLERID).append(sellerModel.getSellerID()).append(USSID).append(sellerModel.getSellerArticleSKU())
						.append("has been delisted");

				//				LOG.info("Seller ID: " + sellerModel.getSellerID() + " USSID : " + sellerModel.getSellerArticleSKU()
				//						+ "has been delisted");
			}

			modelService.saveAll(sellerModelSavingList);

		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
			successfulDelisting = false;
		}

		return successfulDelisting;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService#getModelforUSSID(java.lang.String)
	 */
	@Override
	public List<SellerInformationModel> getModelforUSSID(final String ussid)
	{

		if (StringUtils.isNotEmpty(ussid))

		{
			return mplDelistingDao.getModelforUSSID(ussid);
		}
		LOG.error("Seller ID is Not Present");

		return null;

	}

	@Override
	public List<SellerInformationModel> getModelforUSSID(final String ussid, final CatalogVersionModel catalogVersion)
	{
		//		List<SellerInformationModel> sellerinfoList = new ArrayList<>();
		//
		//
		//		if (StringUtils.isNotEmpty(ussid))
		//
		//		{
		//			sellerinfoList = mplDelistingDao.getModelforUSSID(ussid, catalogVersion);
		//		}
		//		else
		//		{
		//			LOG.error("Seller ID is Not Present");
		//		}
		//		return sellerinfoList;
		return mplDelistingDao.getModelforUSSID(ussid, catalogVersion);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService#FindUnprocessedRecord()
	 */
	@Override
	public List<MarketplaceDelistModel> findUnprocessedRecord()
	{
		try
		{

			return mplDelistingDao.findUnprocessedRecord();

		}

		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}


	}

	//Delisting Fix TISPRD-5345
	/*
	 * @Javadoc This method would return a date deffered by minutes defined in the local.properties by variable
	 * etail.delist.date.deffered or else by default it would deffer by 15minutes if the variable is not defined
	 * 
	 * @return defferedTime
	 */
	private Date defferedDate()
	{
		final int defferedTime = configurationService.getConfiguration().getInt("etail.delist.date.deffered", 15);

		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, defferedTime);

		return cal.getTime();

	}

	//SDI-2814 --- De-listing and Re-listing through xml from PI Start
	@Override
	public void delistProduct(final ProductDelistingDTO delistDTO) throws JAXBException
	{
		//SellerInformationModel sellerInfo = null;
		final List<RecordsetDTO> recordSetList = delistDTO.getRecordset();
		if (CollectionUtils.isNotEmpty(recordSetList))
		{
			for (final RecordsetDTO eachRecord : recordSetList)
			{
				if (StringUtils.isNotEmpty(eachRecord.getDelisting()))
				{
					final List<String> NoSellerUssid = new ArrayList();
					final List<SellerInformationModel> sellerSavingList = new ArrayList();
					boolean isSuccess = false;
					final DelistDTO delist = eachRecord.getDelist();
					final List<String> ussidList = delist.getUssid();
					final List<SellerInformationModel> sellerList = mplDelistingDao.getSellerModel(ussidList, getCatalogVersion(),
							NoSellerUssid);

					if (CollectionUtils.isNotEmpty(sellerList))
					{
						try
						{
							for (final SellerInformationModel sellerInfo : sellerList)
							{
								if (StringUtils.equalsIgnoreCase(eachRecord.getDelisting(), MarketplacecommerceservicesConstants.Y))
								{
									sellerInfo.setSellerAssociationStatus(SellerAssociationStatusEnum.NO);
									sellerInfo.setDelistDate(defferedDate());
									sellerSavingList.add(sellerInfo);
								}
								else
								{
									sellerInfo.setSellerAssociationStatus(SellerAssociationStatusEnum.YES);
									sellerInfo.setDelistDate(defferedDate());
									sellerSavingList.add(sellerInfo);
								}
							}
							modelService.saveAll(sellerSavingList);
							isSuccess = true;
						}
						catch (final Exception e)
						{
							LOG.error("The exception is " + e);
						}
					}
					else
					{
						NoSellerUssid.addAll(ussidList);
						LOG.error("empty seller information for ussids");
					}
					createUpdateDelistModel(sellerSavingList, ussidList, eachRecord.getSellerID(), isSuccess,
							eachRecord.getDelisting(), NoSellerUssid);
				}
				else
				{
					LOG.error("status empty");
				}
			}
		}
		else
		{
			LOG.error("no record present");
		}
	}

	private void createUpdateDelistModel(final List<SellerInformationModel> sellerSavingList, final List<String> ussidList,
			final String sellerId, final boolean isSuccess, final String status, final List<String> NoSellerUssid)
	{
		final List<MarketplaceDelistModel> savingList = new ArrayList();
		final List<MarketplaceDelistModel> ExistingDelistDetailsList = mplDelistingDao.fetchDelistDetails(ussidList);
		if (CollectionUtils.isNotEmpty(ExistingDelistDetailsList))
		{
			for (final MarketplaceDelistModel delistM : ExistingDelistDetailsList)
			{
				if (NoSellerUssid.contains(delistM.getDelistUssid()))
				{
					delistM.setDelistingStatus("Y");
					savingList.add(delistM);
				}
				else
				{
					for (final SellerInformationModel seller : sellerSavingList)
					{
						if (seller.equals(delistM.getDelistUssid()))
						{
							delistM.setDelistingStatus(status);
							savingList.add(delistM);
						}
					}
				}
			}
			modelService.saveAll(savingList);
		}
		else
		{
			for (final String sku : NoSellerUssid)
			{
				final MarketplaceDelistModel delistNInfo = new MarketplaceDelistModel();
				delistNInfo.setSellerID(sellerId);
				delistNInfo.setDelistingStatus("Y");
				delistNInfo.setDelistUssid(sku);
				delistNInfo.setIsProcessed(Boolean.FALSE);
				savingList.add(delistNInfo);
			}
			for (final SellerInformationModel seller : sellerSavingList)
			{
				final MarketplaceDelistModel delistInfo = new MarketplaceDelistModel();
				delistInfo.setSellerID(sellerId);
				delistInfo.setDelistingStatus(status);
				delistInfo.setDelistUssid(seller.getSellerArticleSKU());
				if (isSuccess)
				{
					delistInfo.setIsProcessed(Boolean.TRUE);
				}
				else
				{
					delistInfo.setIsProcessed(Boolean.FALSE);
				}
				savingList.add(delistInfo);
			}
			modelService.saveAll(savingList);
		}
	}

	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}

	@Override
	public void sellerDelist(final SellerDelistingDTO sellerDelistDTO) throws JAXBException
	{
		final List<SellerInformationModel> Ussidlist = getAllUSSIDforSeller(sellerDelistDTO.getSellerID());
		if (CollectionUtils.isNotEmpty(Ussidlist))
		{
			if ("Y".equalsIgnoreCase(sellerDelistDTO.getDelisting()))
			{
				delistOrRelistAllUssid(Ussidlist, sellerDelistDTO.getBlockOMS(), sellerDelistDTO.getDelisting());
			}
			else if ("N".equalsIgnoreCase(sellerDelistDTO.getDelisting()))
			{
				relistSelectedUssid(Ussidlist, sellerDelistDTO.getBlockOMS(), sellerDelistDTO.getDelisting());
			}
		}
	}

	private void delistOrRelistAllUssid(final List<SellerInformationModel> Ussidlist, final String blockOMS, final String delisting)
	{
		final List<SellerInformationModel> sellerModelSavingList = new ArrayList<>();
		for (final SellerInformationModel sellerModel : Ussidlist)
		{
			if (StringUtils.equalsIgnoreCase("Y", delisting))
			{
				sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("no"));
			}
			else
			{
				sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("yes"));
			}
			sellerModel.setDelistDate(defferedDate());
			sellerModel.setBlockOMS(blockOMS);
			sellerModelSavingList.add(sellerModel);
		}
		modelService.saveAll(sellerModelSavingList);
	}

	private void relistSelectedUssid(final List<SellerInformationModel> Ussidlist, final String blockOMS, final String delisting)
	{
		String sellerV = "";
		for (final SellerInformationModel sellerModel : Ussidlist)
		{
			sellerV = sellerV + "'" + sellerModel.getSellerArticleSKU() + "'" + ",";
		}
		//LOG.debug(Integer.valueOf(sellerV.length()));
		final String sellerVarb = sellerV.substring(0, sellerV.length() - 1);

		final List<MarketplaceDelistModel> delistList = mplDelistingDao.fetchDataForUssid(sellerVarb);
		final List<SellerInformationModel> finalList = new ArrayList(Ussidlist);
		if (CollectionUtils.isNotEmpty(delistList))
		{
			for (final MarketplaceDelistModel delist : delistList)
			{
				for (final SellerInformationModel seller : Ussidlist)
				{
					if (delist.getDelistUssid().equalsIgnoreCase(seller.getSellerArticleSKU())
							&& "Y".equalsIgnoreCase(delist.getDelistingStatus()))
					{
						finalList.remove(seller);
					}
				}
			}
			delistOrRelistAllUssid(finalList, blockOMS, delisting);
		}
		else
		{
			delistOrRelistAllUssid(Ussidlist, blockOMS, delisting);
		}
	}
	//SDI-2814 --- De-listing and Re-listing through xml from PI END --------------

}

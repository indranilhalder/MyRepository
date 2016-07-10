package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MarketplaceDelistModel;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.model.SellerInformationModel;


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
				sellerModel.setDelistDate(new Date());
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
				sellerModel.setDelistDate(new Date());

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
		List<SellerInformationModel> sellerinfoList = new ArrayList<>();


		if (StringUtils.isNotEmpty(ussid))

		{
			sellerinfoList = mplDelistingDao.getModelforUSSID(ussid);
		}
		else
		{
			LOG.error("Seller ID is Not Present");
		}
		return sellerinfoList;

	}


	@Override
	public List<SellerInformationModel> getModelforUSSID(final String ussid, final CatalogVersionModel catalogVersion)
	{
		List<SellerInformationModel> sellerinfoList = new ArrayList<>();


		if (StringUtils.isNotEmpty(ussid))

		{
			sellerinfoList = mplDelistingDao.getModelforUSSID(ussid, catalogVersion);
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







}
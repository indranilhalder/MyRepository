package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDelistingDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 */


public class MplDelistingServiceImpl implements MplDelistingService
{
	@Autowired
	private MplDelistingDao mplDelistingDao;

	@Autowired
	private ModelService modelService;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplDelistingServiceImpl.class.getName());


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

		if (sellerId != null && !sellerId.isEmpty())

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
	 */
	@Override
	public void delistSeller(final List<SellerInformationModel> sellerModelList, final String delisting, final String blockOMS)
	{

		try
		{
			for (final SellerInformationModel sellerModel : sellerModelList)
			{
				LOG.info(
						"Seller ID:" + sellerModel.getSellerID() + "USSID :" + sellerModel.getSellerArticleSKU() + "trying to delist");

				if (delisting.equalsIgnoreCase("Y"))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("no"));
				}
				else if (delisting.equalsIgnoreCase("N"))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("yes"));
				}
				sellerModel.setBlockOMS(blockOMS);
				sellerModel.setDelistDate(new Date());
				modelService.save(sellerModel);
				LOG.info(
						"Seller ID:" + sellerModel.getSellerID() + "USSID :" + sellerModel.getSellerArticleSKU() + "has been delisted");
			}
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
	 */
	@Override
	public void delistUSSID(final List<SellerInformationModel> sellerModelList, final String delisting)
	{

		try
		{


			for (final SellerInformationModel sellerModel : sellerModelList)
			{
				LOG.info(
						"Seller ID:" + sellerModel.getSellerID() + "USSID :" + sellerModel.getSellerArticleSKU() + "trying to delist");
				if (delisting.equalsIgnoreCase("Y"))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("no"));
				}
				else if (delisting.equalsIgnoreCase("N"))
				{
					sellerModel.setSellerAssociationStatus(SellerAssociationStatusEnum.valueOf("yes"));
				}
				sellerModel.setDelistDate(new Date());
				modelService.save(sellerModel);
				LOG.info("Seller ID: " + sellerModel.getSellerID() + " USSID : " + sellerModel.getSellerArticleSKU()
						+ "has been delisted");
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}

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


		if (ussid != null && !ussid.isEmpty())

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


		if (ussid != null && !ussid.isEmpty())

		{
			sellerinfoList = mplDelistingDao.getModelforUSSID(ussid, catalogVersion);
		}
		else
		{
			LOG.error("Seller ID is Not Present");
		}
		return sellerinfoList;

	}







}
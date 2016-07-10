/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.MplSellerInformationDAOImpl;
import com.tisl.mpl.model.RestrictionsetupModel;
import com.tisl.mpl.wsdto.PinData;
import com.tisl.mpl.wsdto.RestrictionPins;


/**
 * @author TCS
 * @param RestrictionPins
 *           object
 * @return void
 */
public class MplRestrictionServiceImpl
{
	@Resource
	private ModelService modelService;
	@Autowired
	private MplSellerInformationDAOImpl mplSellerInformationDAO;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private RestrictionsetupModel resModel;

	/**
	 * @return the mplSellerInformationDAO
	 */
	public MplSellerInformationDAOImpl getMplSellerInformationDAO()
	{
		return mplSellerInformationDAO;
	}

	/**
	 * @param mplSellerInformationDAO
	 *           the mplSellerInformationDAO to set
	 */
	public void setMplSellerInformationDAO(final MplSellerInformationDAOImpl mplSellerInformationDAO)
	{
		this.mplSellerInformationDAO = mplSellerInformationDAO;
	}

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

	private static final Logger LOG = Logger.getLogger(MplRestrictionServiceImpl.class);


	public void saveRestriction(final PinData resdto)
	{
		try
		{
			if (null != resdto.getPinCode() & !resdto.getPinCode().isEmpty())
			{
				resModel.setPincode(resdto.getPinCode());
			}
			if (null != resdto.getShipmentMode() & !resdto.getShipmentMode().isEmpty())
			{
				resModel.setShipmentMode(resdto.getShipmentMode());
			}
			if (null != resdto.getDeliveryMode() & !resdto.getDeliveryMode().isEmpty())
			{
				resModel.setDeliveryMode(resdto.getDeliveryMode());
			}
			if (null != resdto.getSellerID())
			{
				resModel.setSellerId(resdto.getSellerID());
			}
			if (null != resdto.getPrimaryCatID())
			{
				resModel.setPrimaryCatID(resdto.getPrimaryCatID());
			}
			if (null != resdto.getListingID())
			{
				resModel.setListingID(resdto.getListingID());
			}
			if (null != resdto.getUSSID())
			{
				resModel.setUSSID(resdto.getUSSID());
			}
			if (null != resdto.getCodRestricted() & !resdto.getCodRestricted().isEmpty())
			{
				resModel.setCodRestricted(resdto.getCodRestricted());
			}
			if (null != resdto.getPrepaidRestricted() & !resdto.getPrepaidRestricted().isEmpty())
			{
				resModel.setPrepaidRestricted(resdto.getPrepaidRestricted());
			}
			// checking all mandatory attributes before inserting into database
			if ((null != resdto.getCodRestricted() && null != resdto.getDeliveryMode() && null != resdto.getPinCode()
					&& null != resdto.getPrepaidRestricted() && null != resdto.getShipmentMode())
					&& (!resdto.getCodRestricted().isEmpty() && !resdto.getDeliveryMode().isEmpty() && !resdto.getPinCode().isEmpty()
							&& !resdto.getPrepaidRestricted().isEmpty() && !resdto.getShipmentMode().isEmpty()))
			{
				modelService.save(resModel);
			}



		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.RESTRICTION_SETUP_ERROR_MSG + ":" + e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.RESTRICTION_SETUP + ":" + e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.RESTRICTION_SETUP_ERROR + ":" + e);
		}
	}

	/**
	 * @param pinCode
	 * @param listingID
	 */
	public void beforeSave(final RestrictionPins resDto)
	{
		final List<PinData> restrictionWsDTO = resDto.getPin();
		RestrictionsetupModel rModel;
		for (final PinData resdto : restrictionWsDTO)
		{

			final String pinCode = resdto.getPinCode();
			final String listingID = resdto.getListingID();
			final String primaryCatId = resdto.getPrimaryCatID();
			final String sellerID = resdto.getSellerID();
			final String ussid = resdto.getUSSID();
			final String shipmentMode = resdto.getShipmentMode();
			final String deliveryMode = resdto.getDeliveryMode();

			if ((pinCode != null && !pinCode.isEmpty()) && (shipmentMode != null && !shipmentMode.isEmpty())
					&& (deliveryMode != null && !deliveryMode.isEmpty()))
			{
				//Check for SellerID and USSID
				if ((sellerID != null && ussid != null) && (!pinCode.isEmpty() && !sellerID.isEmpty() && !ussid.isEmpty())
						&& (listingID == null || listingID.isEmpty()) && (primaryCatId == null || primaryCatId.isEmpty()))
				{
					rModel = mplSellerInformationDAO.beforesave(pinCode, listingID, primaryCatId, sellerID, ussid, shipmentMode,
							deliveryMode);

					if (null != rModel)
					{
						setResModel(rModel);
					}
					else
					{
						setResModel(new RestrictionsetupModel());
					}
					saveRestriction(resdto);

				}//Check for PrimaryCategoryID and SellerID
				else if ((primaryCatId != null && sellerID != null)
						&& (!pinCode.isEmpty() && !sellerID.isEmpty() && !primaryCatId.isEmpty())
						&& (listingID == null || listingID.isEmpty() && (ussid == null || ussid.isEmpty())))
				{
					rModel = mplSellerInformationDAO.beforesave(pinCode, listingID, primaryCatId, sellerID, ussid, shipmentMode,
							deliveryMode);

					if (null != rModel)
					{
						setResModel(rModel);
					}
					else
					{
						setResModel(new RestrictionsetupModel());
					}
					saveRestriction(resdto);


				}//Check for PrimaryCategoryID
				else if ((primaryCatId != null)
						&& (!pinCode.isEmpty() && !primaryCatId.isEmpty())
						&& (listingID == null || listingID.isEmpty() && (ussid == null || ussid.isEmpty())
								&& (sellerID == null || sellerID.isEmpty())))
				{
					rModel = mplSellerInformationDAO.beforesave(pinCode, listingID, primaryCatId, sellerID, ussid, shipmentMode,
							deliveryMode);

					if (null != rModel)
					{
						setResModel(rModel);
					}
					else
					{
						setResModel(new RestrictionsetupModel());
					}
					saveRestriction(resdto);
				}//Check for ListingID
				else if ((listingID != null)
						&& (!pinCode.isEmpty() && !listingID.isEmpty())
						&& ((ussid == null || ussid.isEmpty()) && (sellerID == null || sellerID.isEmpty()) && (primaryCatId == null || primaryCatId
								.isEmpty())))
				{
					rModel = mplSellerInformationDAO.beforesave(pinCode, listingID, primaryCatId, sellerID, ussid, shipmentMode,
							deliveryMode);

					if (null != rModel)
					{
						setResModel(rModel);
					}
					else
					{
						setResModel(new RestrictionsetupModel());
					}
					saveRestriction(resdto);
				}//Criteria mismatch
				else
				{
					LOG.error(">>>>>> Restriction data did not match criteria <<<<<<");
				}

			}//Mandatory fields missing
			else
			{
				LOG.error(">>>> Mandatory fields missing <<<<<");
			}

		}
	}

	/**
	 * @return the resModel
	 */
	public RestrictionsetupModel getResModel()
	{
		return resModel;
	}

	/**
	 * @param resModel
	 *           the resModel to set
	 */
	public void setResModel(final RestrictionsetupModel resModel)
	{
		this.resModel = resModel;
	}

}

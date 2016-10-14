/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetLevelHelper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.wsdto.PincodeDataWsDTO;


/**
 * @author Techouts
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}")
public class MplPincodeController extends BaseCommerceController
{
	private final static Logger LOG = Logger.getLogger(MplPincodeController.class);
	private static final String PINCODE_URL = "/getPincodeData";
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;

	@Autowired
	private DataMapper dataMapper;
	@Autowired
	private PincodeServiceFacade pincodeServiceFacade;


	/**
	 * this method is used to get the PincodeData For the given pincode
	 *
	 * @author Techouts
	 * @param pincode
	 * @param fields
	 * @return PincodeDataWsDTO
	 */

	@RequestMapping(value = PINCODE_URL, method = RequestMethod.GET)
	@ResponseBody
	public PincodeDataWsDTO getPincodeData(@RequestParam final String pincode,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("pincode Entered: " + pincode);
		}
		PincodeData pincodeData = new PincodeData();
		final PincodeDataWsDTO pincodeDataWsDTO = new PincodeDataWsDTO();
		try
		{
			pincodeData = pincodeServiceFacade.getAutoPopulatePincodeData(pincode);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Exception occurred while getting the  pincode details for the pincode " + pincode);
			pincodeDataWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			pincodeDataWsDTO.setErrorCode(MarketplacecommerceservicesConstants.B9352);
			pincodeDataWsDTO.setError(e.getErrorMessage());
			return pincodeDataWsDTO;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred while getting the  pincode details for the pincode " + pincode);
			pincodeDataWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			pincodeDataWsDTO.setErrorCode(MarketplacecommerceservicesConstants.B9352);
			pincodeDataWsDTO.setError(e.getMessage());
			return pincodeDataWsDTO;
		}
		if (pincodeData == null)
		{
			LOG.debug("PincodeData not found for the entered pincode :" + pincode);
			pincodeDataWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			return pincodeDataWsDTO;
		}
		return dataMapper.map(pincodeData, PincodeDataWsDTO.class, fields);
	}
}

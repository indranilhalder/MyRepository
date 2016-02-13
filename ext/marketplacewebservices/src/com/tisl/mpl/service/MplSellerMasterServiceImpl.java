/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SellerMasterCorporateAddressModel;
import com.tisl.mpl.core.model.SellerMasterPaymentInfoModel;
import com.tisl.mpl.core.model.SellerMasterWthhldTAXModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerMasterDao;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.MplSellerInformationDAOImpl;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.SellerTypeGlobalCodesModel;
import com.tisl.mpl.wsdto.CorporateAddressWsDTO;
import com.tisl.mpl.wsdto.PaymentInfoWsDTO;
import com.tisl.mpl.wsdto.SellerMasterResponseWsDTO;
import com.tisl.mpl.wsdto.SellerMasterWsDTO;
import com.tisl.mpl.wsdto.WthhldTAXWsDTO;


/**
 * @author TCS
 *
 */
public class MplSellerMasterServiceImpl implements MplSellerMasterService
{

	@Resource
	private ModelService modelService;

	@Autowired
	private MplSellerInformationDAOImpl mplSellerInformationDAO;

	@Autowired
	private MplSellerMasterDao mplSellerMasterDao;

	@Autowired
	private CatalogService catalogService;


	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}

	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}



	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = MarketplacecommerceservicesConstants.SELLER_TYPE_GLOBAL_CODES)
	private MplSellerTypeGlobalCodesService mplSellerTypeGlobalCodesService;
	private static final Logger LOG = Logger.getLogger(MplSellerMasterServiceImpl.class);
	private SellerMasterModel masterModel;
	final private DateFormat formatter = new SimpleDateFormat(MarketplacecommerceservicesConstants.YYYYMMDD);




	//private SellerMasterModel masterModelUpdate;


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

	/**
	 * @return the mplSellerTypeGlobalCodesService
	 */
	public MplSellerTypeGlobalCodesService getMplSellerTypeGlobalCodesService()
	{
		return mplSellerTypeGlobalCodesService;
	}

	/**
	 * @param mplSellerTypeGlobalCodesService
	 *           the mplSellerTypeGlobalCodesService to set
	 */
	public void setMplSellerTypeGlobalCodesService(final MplSellerTypeGlobalCodesService mplSellerTypeGlobalCodesService)
	{
		this.mplSellerTypeGlobalCodesService = mplSellerTypeGlobalCodesService;
	}

	/**
	 * @return the formatter
	 */
	public DateFormat getFormatter()
	{
		return formatter;
	}

	/**
	 * @return the masterModel
	 */
	public SellerMasterModel getMasterModel()
	{
		return modelService.create(SellerMasterModel.class);
	}

	/**
	 * @param masterModel
	 *           the masterModel to set
	 */
	public void setMasterModel(final SellerMasterModel masterModel)
	{
		this.masterModel = masterModel;
	}



	/**
	 * @param sellerMasterWsDTO
	 */
	public void setMasterModelForRegisteredAddress(final SellerMasterWsDTO sellerMasterWsDTO)
	{
		LOG.debug("**********inside Registered addresss----seller master****************");
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress1()))
		{
			masterModel.setRegisteredAddress1(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress1());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress2()))
		{
			masterModel.setRegisteredAddress2(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress2());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCountry()))
		{
			masterModel.setRegisteredAddCountry(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCountry());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddState()))
		{
			masterModel.setRegisteredAddState(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddState());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCity()))
		{
			masterModel.setRegisteredAddCity(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCity());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddPin()))
		{
			masterModel.setRegisteredAddPin(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddPin());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredEmail()))
		{
			masterModel.setRegisteredEmail(sellerMasterWsDTO.getRegisterAddress().getRegisteredEmail());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredPhone()))
		{
			masterModel.setRegisteredPhone(sellerMasterWsDTO.getRegisterAddress().getRegisteredPhone());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredMobile()))
		{
			masterModel.setRegisteredMobile(sellerMasterWsDTO.getRegisterAddress().getRegisteredMobile());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredFAX()))
		{
			masterModel.setRegisteredFAX(sellerMasterWsDTO.getRegisterAddress().getRegisteredFAX());
		}
	}

	/**
	 * @param corporateAddressList
	 */
	public void setMasterModelForCorporateAddress(final List<CorporateAddressWsDTO> corporateAddressList)
	{
		SellerMasterCorporateAddressModel corpModel = modelService.create(SellerMasterCorporateAddressModel.class);
		final List<SellerMasterCorporateAddressModel> corpModelList = new ArrayList<SellerMasterCorporateAddressModel>();
		LOG.debug("**********inside corporate address----seller master****************");
		for (final CorporateAddressWsDTO corporateAddress : corporateAddressList)
		{
			corpModel = new SellerMasterCorporateAddressModel();
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddress1()))
			{
				corpModel.setCorporateAddress1(corporateAddress.getCorporateAddress1());
				LOG.debug("corporate address :" + corporateAddress.getCorporateAddress1());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddress2()))
			{
				corpModel.setCorporateAddress2(corporateAddress.getCorporateAddress2());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddCountry()))
			{
				corpModel.setCorporateAddCountry(corporateAddress.getCorporateAddCountry());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddState()))
			{
				corpModel.setCorporateAddState(corporateAddress.getCorporateAddState());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddCity()))
			{
				corpModel.setCorporateAddCity(corporateAddress.getCorporateAddCity());
			}
			if (0 != corporateAddress.getCorporateAddPin())
			{
				corpModel.setCorporateAddPin(Integer.valueOf((corporateAddress.getCorporateAddPin())));
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateEmail()))
			{
				corpModel.setCorporateEmail(corporateAddress.getCorporateEmail());
			}
			if (null != corporateAddress.getCorporatePhone())
			{
				corpModel.setCorporatePhone(corporateAddress.getCorporatePhone());
			}
			if (null != corporateAddress.getCorporateMobile())
			{
				corpModel.setCorporateMobile(corporateAddress.getCorporateMobile());
			}
			corpModelList.add(corpModel);
		}
		masterModel.setCorporateAddress(corpModelList);
		LOG.debug("added corporate address");

	}

	/**
	 * @param wthhldTAXList
	 * @throws ParseException
	 */
	public void setMasterModelForWthhldTAX(final List<WthhldTAXWsDTO> wthhldTAXList) throws ParseException
	{
		SellerMasterWthhldTAXModel corpModel = modelService.create(SellerMasterWthhldTAXModel.class);
		final List<SellerMasterWthhldTAXModel> corpModelList = new ArrayList<SellerMasterWthhldTAXModel>();
		LOG.debug("**********inside WthhldTAXWsDTO----seller master****************");
		for (final WthhldTAXWsDTO wthhldTAX : wthhldTAXList)
		{
			corpModel = new SellerMasterWthhldTAXModel();
			if (StringUtils.isNotEmpty(wthhldTAX.getWthhldTAXCntry()))
			{
				corpModel.setWthhldTAXCntry(wthhldTAX.getWthhldTAXCntry());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getWthhldTAXTyp()))
			{
				corpModel.setWthhldTAXTyp(wthhldTAX.getWthhldTAXTyp());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getWthhldTAXCode()))
			{
				corpModel.setWthhldTAXCode(wthhldTAX.getWthhldTAXCode());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getLiable()))
			{
				corpModel.setLiable(wthhldTAX.getLiable());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getReceiptType()))
			{
				corpModel.setReceiptType(wthhldTAX.getReceiptType());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getExemptionNo()))
			{
				corpModel.setExemptionNo(wthhldTAX.getExemptionNo());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getExemptionRate()))
			{
				corpModel.setExemptionRate(wthhldTAX.getExemptionRate());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getExemptionReason()))
			{
				corpModel.setExemptionReason(wthhldTAX.getExemptionReason());
			}
			if (null != wthhldTAX.getExemptionFromDate())
			{
				corpModel.setExemptionFromDate(formatter.parse(formatter.format(wthhldTAX.getExemptionFromDate())));
			}
			if (null != wthhldTAX.getExemptionToDate())
			{
				corpModel.setExemptionToDate(formatter.parse(formatter.format(wthhldTAX.getExemptionToDate())));
			}
			corpModelList.add(corpModel);
		}
		LOG.debug("SellerMasterWthhldTAXModel size is:" + corpModelList.size());
		masterModel.setWthhldTAX(corpModelList);
	}

	/**
	 * @param paymentInfoList
	 */
	public void setMasterModelForPaymentInfo(final List<PaymentInfoWsDTO> paymentInfoList)
	{
		SellerMasterPaymentInfoModel corpModel = modelService.create(SellerMasterPaymentInfoModel.class);
		final List<SellerMasterPaymentInfoModel> corpModelList = new ArrayList<SellerMasterPaymentInfoModel>();
		LOG.debug("**********inside setMasterModelForPaymentInfo: seller master****************");
		for (final PaymentInfoWsDTO paymentInfo : paymentInfoList)
		{
			corpModel = new SellerMasterPaymentInfoModel();
			if (StringUtils.isNotEmpty(paymentInfo.getBankCountry()))
			{
				corpModel.setBankCountry(paymentInfo.getBankCountry());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankState()))
			{
				corpModel.setBankState(paymentInfo.getBankState());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankCity()))
			{
				corpModel.setBankCity(paymentInfo.getBankCity());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankAddress()))
			{
				corpModel.setBankAddress(paymentInfo.getBankAddress());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankName()))
			{
				corpModel.setBankName(paymentInfo.getBankName());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankType()))
			{
				corpModel.setBankType(paymentInfo.getBankType());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getIFSCCode()))
			{
				corpModel.setIFSCCode(paymentInfo.getIFSCCode());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBranchName()))
			{
				corpModel.setBranchName(paymentInfo.getBranchName());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getAccHolderName()))
			{
				corpModel.setAccHolderName(paymentInfo.getAccHolderName());
			}
			if (null != paymentInfo.getAccNo())
			{
				corpModel.setAccNumber(paymentInfo.getAccNo());
			}
			/*
			 * if (null != paymentInfo.getAccNo()) { corpModel.setAccNo(paymentInfo.getAccNo()); }
			 */
			if (StringUtils.isNotEmpty(paymentInfo.getRecoAcc()))
			{
				corpModel.setRecoAcc(paymentInfo.getRecoAcc());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getMinorityIndicator()))
			{
				corpModel.setMinorityIndicator(paymentInfo.getMinorityIndicator());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getCheckDuplInv()))
			{
				corpModel.setCheckDuplInv(paymentInfo.getCheckDuplInv());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getPaymentMethodID()))
			{
				corpModel.setPaymentMethodID(paymentInfo.getPaymentMethodID());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getPaymentBlock()))
			{
				corpModel.setPaymentBlock(paymentInfo.getPaymentBlock());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getAlternatePayee()))
			{
				corpModel.setAlternatePayee(paymentInfo.getAlternatePayee());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getHouseBank()))
			{
				corpModel.setHouseBank(paymentInfo.getHouseBank());
			}
			corpModelList.add(corpModel);

		}
		masterModel.setPaymentInfo(corpModelList);
		LOG.debug("added payment information ");
	}

	public void setMasterModelForRegisteredAddressUpdate(final SellerMasterWsDTO sellerMasterWsDTO,
			final SellerMasterModel masterModelUpdate)
	{
		LOG.debug("**********inside Registered addresss update----seller master****************");
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress1()))
		{
			masterModelUpdate.setRegisteredAddress1(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress1());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress2()))
		{
			masterModelUpdate.setRegisteredAddress2(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddress2());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCountry()))
		{
			masterModelUpdate.setRegisteredAddCountry(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCountry());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddState()))
		{
			masterModelUpdate.setRegisteredAddState(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddState());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCity()))
		{
			masterModelUpdate.setRegisteredAddCity(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddCity());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddPin()))
		{
			masterModelUpdate.setRegisteredAddPin(sellerMasterWsDTO.getRegisterAddress().getRegisteredAddPin());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredEmail()))
		{
			masterModelUpdate.setRegisteredEmail(sellerMasterWsDTO.getRegisterAddress().getRegisteredEmail());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredPhone()))
		{
			masterModelUpdate.setRegisteredPhone(sellerMasterWsDTO.getRegisterAddress().getRegisteredPhone());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredMobile()))
		{
			masterModelUpdate.setRegisteredMobile(sellerMasterWsDTO.getRegisterAddress().getRegisteredMobile());
		}
		if (StringUtils.isNotEmpty(sellerMasterWsDTO.getRegisterAddress().getRegisteredFAX()))
		{
			masterModelUpdate.setRegisteredFAX(sellerMasterWsDTO.getRegisterAddress().getRegisteredFAX());
		}
	}

	private void setMasterModelForCorporateAddressUpdate(final List<CorporateAddressWsDTO> corporateAddressList,
			final SellerMasterModel masterModelUpdate)
	{
		// SellerMasterCorporateAddressModel corpModel = modelService.create(SellerMasterCorporateAddressModel.class);
		final List<SellerMasterCorporateAddressModel> corpModelListUpdate = new ArrayList<SellerMasterCorporateAddressModel>(
				masterModelUpdate.getCorporateAddress());
		//		final List<SellerMasterCorporateAddressModel> corpModelListUpdate = (List<SellerMasterCorporateAddressModel>) masterModelUpdate
		//				.getCorporateAddress();
		LOG.debug("**********inside corporate addresss update----seller master****************");
		for (final CorporateAddressWsDTO corporateAddress : corporateAddressList)
		{
			final SellerMasterCorporateAddressModel corpModelUpdate = modelService.create(SellerMasterCorporateAddressModel.class);
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddress1()))
			{
				corpModelUpdate.setCorporateAddress1(corporateAddress.getCorporateAddress1());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddress2()))
			{
				corpModelUpdate.setCorporateAddress2(corporateAddress.getCorporateAddress2());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddCountry()))
			{
				corpModelUpdate.setCorporateAddCountry(corporateAddress.getCorporateAddCountry());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddState()))
			{
				corpModelUpdate.setCorporateAddState(corporateAddress.getCorporateAddState());
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateAddCity()))
			{
				corpModelUpdate.setCorporateAddCity(corporateAddress.getCorporateAddCity());
			}
			if (0 != corporateAddress.getCorporateAddPin())
			{
				corpModelUpdate.setCorporateAddPin(Integer.valueOf((corporateAddress.getCorporateAddPin())));
			}
			if (StringUtils.isNotEmpty(corporateAddress.getCorporateEmail()))
			{
				corpModelUpdate.setCorporateEmail(corporateAddress.getCorporateEmail());
			}
			if (null != corporateAddress.getCorporatePhone())
			{
				corpModelUpdate.setCorporatePhone(corporateAddress.getCorporatePhone());
			}
			if (null != corporateAddress.getCorporateMobile())
			{
				corpModelUpdate.setCorporateMobile(corporateAddress.getCorporateMobile());
			}
			corpModelListUpdate.add(corpModelUpdate);
		}



		masterModelUpdate.setCorporateAddress(corpModelListUpdate);
		LOG.debug("**********corporate address updated----seller master****************");

	}


	public void setMasterModelForWthhldTAXUpdate(final List<WthhldTAXWsDTO> wthhldTAXList,
			final SellerMasterModel masterModelUpdate) throws ParseException
	{

		final List<SellerMasterWthhldTAXModel> corpWthhldModelListUpdate = new ArrayList<SellerMasterWthhldTAXModel>(
				masterModelUpdate.getWthhldTAX());

		//		final List<SellerMasterWthhldTAXModel> corpWthhldModelListUpdate = (List<SellerMasterWthhldTAXModel>) masterModelUpdate
		//				.getWthhldTAX();
		for (final WthhldTAXWsDTO wthhldTAX : wthhldTAXList)
		{
			final SellerMasterWthhldTAXModel corpWthhldModelUpdate = modelService.create(SellerMasterWthhldTAXModel.class);
			if (StringUtils.isNotEmpty(wthhldTAX.getWthhldTAXCntry()))
			{
				corpWthhldModelUpdate.setWthhldTAXCntry(wthhldTAX.getWthhldTAXCntry());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getWthhldTAXTyp()))
			{
				corpWthhldModelUpdate.setWthhldTAXTyp(wthhldTAX.getWthhldTAXTyp());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getWthhldTAXCode()))
			{
				corpWthhldModelUpdate.setWthhldTAXCode(wthhldTAX.getWthhldTAXCode());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getLiable()))
			{
				corpWthhldModelUpdate.setLiable(wthhldTAX.getLiable());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getReceiptType()))
			{
				corpWthhldModelUpdate.setReceiptType(wthhldTAX.getReceiptType());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getExemptionNo()))
			{
				corpWthhldModelUpdate.setExemptionNo(wthhldTAX.getExemptionNo());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getExemptionRate()))
			{
				corpWthhldModelUpdate.setExemptionRate(wthhldTAX.getExemptionRate());
			}
			if (StringUtils.isNotEmpty(wthhldTAX.getExemptionReason()))
			{
				corpWthhldModelUpdate.setExemptionReason(wthhldTAX.getExemptionReason());
			}
			if (null != wthhldTAX.getExemptionFromDate())
			{
				corpWthhldModelUpdate.setExemptionFromDate(formatter.parse(formatter.format(wthhldTAX.getExemptionFromDate())));
			}
			if (null != wthhldTAX.getExemptionToDate())
			{
				corpWthhldModelUpdate.setExemptionToDate(formatter.parse(formatter.format(wthhldTAX.getExemptionToDate())));
			}
			corpWthhldModelListUpdate.add(corpWthhldModelUpdate);
		}
		masterModelUpdate.setWthhldTAX(corpWthhldModelListUpdate);
		LOG.debug("**********WthhldTAX updated----seller master****************");

	}

	public void setMasterModelForPaymentInfoUpdate(final List<PaymentInfoWsDTO> paymentInfoList,
			final SellerMasterModel masterModelUpdate)
	{
		final List<SellerMasterPaymentInfoModel> corpModelListUpdate = new ArrayList<SellerMasterPaymentInfoModel>(
				masterModelUpdate.getPaymentInfo());

		//		final List<SellerMasterPaymentInfoModel> corpModelListUpdate = (List<SellerMasterPaymentInfoModel>) masterModelUpdate
		//				.getPaymentInfo();
		for (final PaymentInfoWsDTO paymentInfo : paymentInfoList)
		{
			final SellerMasterPaymentInfoModel corpModelUpdate = modelService.create(SellerMasterPaymentInfoModel.class);

			if (StringUtils.isNotEmpty(paymentInfo.getBankCountry()))
			{
				corpModelUpdate.setBankCountry(paymentInfo.getBankCountry());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankState()))
			{
				corpModelUpdate.setBankState(paymentInfo.getBankState());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankCity()))
			{
				corpModelUpdate.setBankCity(paymentInfo.getBankCity());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankAddress()))
			{
				corpModelUpdate.setBankAddress(paymentInfo.getBankAddress());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankName()))
			{
				corpModelUpdate.setBankName(paymentInfo.getBankName());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBankType()))
			{
				corpModelUpdate.setBankType(paymentInfo.getBankType());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getIFSCCode()))
			{
				corpModelUpdate.setIFSCCode(paymentInfo.getIFSCCode());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getBranchName()))
			{
				corpModelUpdate.setBranchName(paymentInfo.getBranchName());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getAccHolderName()))
			{
				corpModelUpdate.setAccHolderName(paymentInfo.getAccHolderName());
			}
			if (null != paymentInfo.getAccNo())
			{
				corpModelUpdate.setAccNumber(paymentInfo.getAccNo());
			}
			/*
			 * if (null != paymentInfo.getAccNo()) { corpModelUpdate.setAccNo(paymentInfo.getAccNo()); }
			 */
			if (StringUtils.isNotEmpty(paymentInfo.getRecoAcc()))
			{
				corpModelUpdate.setRecoAcc(paymentInfo.getRecoAcc());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getMinorityIndicator()))
			{
				corpModelUpdate.setMinorityIndicator(paymentInfo.getMinorityIndicator());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getCheckDuplInv()))
			{
				corpModelUpdate.setCheckDuplInv(paymentInfo.getCheckDuplInv());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getPaymentMethodID()))
			{
				corpModelUpdate.setPaymentMethodID(paymentInfo.getPaymentMethodID());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getPaymentBlock()))
			{
				corpModelUpdate.setPaymentBlock(paymentInfo.getPaymentBlock());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getAlternatePayee()))
			{
				corpModelUpdate.setAlternatePayee(paymentInfo.getAlternatePayee());
			}
			if (StringUtils.isNotEmpty(paymentInfo.getHouseBank()))
			{
				corpModelUpdate.setHouseBank(paymentInfo.getHouseBank());
			}
			corpModelListUpdate.add(corpModelUpdate);
		}
		masterModelUpdate.setPaymentInfo(corpModelListUpdate);
		LOG.debug("**********payment info updated----seller master****************");
	}


	public SellerMasterResponseWsDTO saveSellerMaster(final SellerMasterWsDTO sellerMasterWsDTO)
	{
		final SellerMasterResponseWsDTO sellerMasterResWsDTO = new SellerMasterResponseWsDTO();
		String status = MarketplacecommerceservicesConstants.SUCCESSS_RESP;
		final StringBuilder stringBuilder = new StringBuilder();
		try
		{

			final DateFormat formatter = new SimpleDateFormat(MarketplacecommerceservicesConstants.YYYYMMDD);
			//final SellerInformationModel resModel = modelService.create(SellerInformationModel.class);
			masterModel = getMasterModel();
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getId()))
			{
				//	resModel.setSellerID(sellerMasterWsDTO.getId());
				masterModel.setId(sellerMasterWsDTO.getId());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getFirstname()))
			{
				if (sellerMasterWsDTO.getLastname() != null && sellerMasterWsDTO.getMidname() != null)
				{
					stringBuilder.append(sellerMasterWsDTO.getFirstname()).append(sellerMasterWsDTO.getMidname())
							.append(sellerMasterWsDTO.getLastname());
					//resModel.setSellerName(stringBuilder.toString());
				}

				//Blocked for Sonar Fix
				//				else
				//				{
				//					//	resModel.setSellerName(sellerMasterWsDTO.getFirstname());
				//				}

				masterModel.setFirstname(sellerMasterWsDTO.getFirstname());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getType()))
			{
				final String sellerType = sellerMasterWsDTO.getType();
				final SellerTypeGlobalCodesModel sellerTypeGlobalCodesModel = mplSellerTypeGlobalCodesService
						.getDescription(sellerType);
				if (null == sellerTypeGlobalCodesModel)
				{
					status = MarketplacecommerceservicesConstants.ERROR_CODE_1;
					sellerMasterResWsDTO.setStatus(status);
					return sellerMasterResWsDTO;
				}
				if (StringUtils.isNotEmpty(sellerTypeGlobalCodesModel.getDescription()))
				{
					final String description = sellerTypeGlobalCodesModel.getDescription();
					//resModel.setSellerType(description);
					masterModel.setType(description);
				}
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getMidname()))
			{
				masterModel.setMidname(sellerMasterWsDTO.getMidname());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLastname()))
			{
				masterModel.setLastname(sellerMasterWsDTO.getLastname());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLegalName()))
			{
				masterModel.setLegalName(sellerMasterWsDTO.getLegalName());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getCompanyCode()))
			{
				masterModel.setCompanyCode(sellerMasterWsDTO.getCompanyCode());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getAccountGroup()))
			{
				masterModel.setAccountGroup(sellerMasterWsDTO.getAccountGroup());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getSearch1()))
			{
				masterModel.setSearch1(sellerMasterWsDTO.getSearch1());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getSearch2()))
			{
				masterModel.setSearch2(sellerMasterWsDTO.getSearch2());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getTitle()))
			{
				masterModel.setTitle(sellerMasterWsDTO.getTitle());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLanguage()))
			{
				masterModel.setLanguage(sellerMasterWsDTO.getLanguage());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getCustomer()))
			{
				masterModel.setCustomer(sellerMasterWsDTO.getCustomer());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getTAN()))
			{
				masterModel.setTAN(sellerMasterWsDTO.getTAN());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLST()))
			{
				masterModel.setLST(sellerMasterWsDTO.getLST());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getCST()))
			{
				masterModel.setCST(sellerMasterWsDTO.getCST());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getServiceTAXNo()))
			{
				masterModel.setServiceTAXNo(sellerMasterWsDTO.getServiceTAXNo());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getPAN()))
			{
				masterModel.setPAN(sellerMasterWsDTO.getPAN());
			}
			if (null != sellerMasterWsDTO.getPANValidThru())
			{
				masterModel.setPANValidThru(formatter.parse(formatter.format(sellerMasterWsDTO.getPANValidThru())));
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getSettlementPeriod()))
			{
				masterModel.setSettlementPeriod(sellerMasterWsDTO.getSettlementPeriod());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getPayoutPeriod()))
			{
				masterModel.setPayoutPeriod(sellerMasterWsDTO.getPayoutPeriod());
			}
			if (null != sellerMasterWsDTO.getRegisterAddress())
			{
				setMasterModelForRegisteredAddress(sellerMasterWsDTO);
			}
			if (null != sellerMasterWsDTO.getCorporateAddress())
			{
				setMasterModelForCorporateAddress(sellerMasterWsDTO.getCorporateAddress());
			}
			if (null != sellerMasterWsDTO.getWthhldTAX())
			{
				setMasterModelForWthhldTAX(sellerMasterWsDTO.getWthhldTAX());
			}
			if (null != sellerMasterWsDTO.getPaymentInfo())
			{
				setMasterModelForPaymentInfo(sellerMasterWsDTO.getPaymentInfo());
			}
			modelService.saveAll(masterModel);
			//resModel.setSellerMaster(masterModel);
			//modelService.save(resModel);
			/*
			 * final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(configurationService
			 * .getConfiguration().getString("DEFAULT_IMPORT_CATALOG_ID"),
			 * configurationService.getConfiguration().getString("DEFAULT_IMPORT_CATALOG_VERSION")); if (null !=
			 * catalogVersionModel) {
			 * 
			 * resModel.setCatalogVersion(catalogVersionModel); }
			 */

			//modelService.saveAll(resModel);

		}
		catch (final EtailBusinessExceptions e)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
		}
		catch (final Exception ex)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + ex);
		}

		sellerMasterResWsDTO.setStatus(status);
		sellerMasterResWsDTO.setSellerMaster(masterModel);
		return sellerMasterResWsDTO;
	}

	private boolean updateSellerInfoWithSellerMaster(final List<SellerInformationModel> sellerInfoModelList,
			final SellerMasterResponseWsDTO sellerMasterRes)
	{
		boolean status = false;
		try
		{
			if (null != sellerMasterRes && null != sellerMasterRes.getSellerMaster()
					&& !CollectionUtils.isEmpty(sellerInfoModelList))
			{
				for (final SellerInformationModel sellerInfoModel : sellerInfoModelList)
				{
					sellerInfoModel.setSellerMaster(sellerMasterRes.getSellerMaster());
					modelService.save(sellerInfoModel);
				}
				status = true;
			}
		}
		catch (final Exception e)
		{
			status = false;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_INFO_UPDATE_ERROR_MSG + ":" + e.getMessage());
		}
		return status;
	}


	private String saveSellerInformation(final SellerMasterWsDTO sellerMasterWsDTO, final SellerMasterModel masterModel)
	{
		String status = MarketplacecommerceservicesConstants.SUCCESSS_RESP;
		final StringBuilder stringBuilder = new StringBuilder();
		try
		{

			//	final DateFormat formatter = new SimpleDateFormat(MarketplacecommerceservicesConstants.YYYYMMDD);
			final SellerInformationModel resModel = modelService.create(SellerInformationModel.class);
			//	masterModel = getMasterModel();
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getId()))
			{
				resModel.setSellerID(sellerMasterWsDTO.getId());
				//	masterModel.setId(sellerMasterWsDTO.getId());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getFirstname()))
			{
				if (sellerMasterWsDTO.getLastname() != null && sellerMasterWsDTO.getMidname() != null)
				{
					stringBuilder.append(sellerMasterWsDTO.getFirstname()).append(sellerMasterWsDTO.getMidname())
							.append(sellerMasterWsDTO.getLastname());
					resModel.setSellerName(stringBuilder.toString());
				}
				else
				{
					resModel.setSellerName(sellerMasterWsDTO.getFirstname());
				}

				//masterModel.setFirstname(sellerMasterWsDTO.getFirstname());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getType()))
			{
				final String sellerType = sellerMasterWsDTO.getType();
				final SellerTypeGlobalCodesModel sellerTypeGlobalCodesModel = mplSellerTypeGlobalCodesService
						.getDescription(sellerType);
				if (null == sellerTypeGlobalCodesModel)
				{
					status = MarketplacecommerceservicesConstants.ERROR_CODE_1;
					return status;
				}
				if (StringUtils.isNotEmpty(sellerTypeGlobalCodesModel.getDescription()))
				{
					final String description = sellerTypeGlobalCodesModel.getDescription();
					resModel.setSellerType(description);
					//	masterModel.setType(description);
				}
			}
			if (null != masterModel)
			{
				resModel.setSellerMaster(masterModel);
			}
			final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(configurationService.getConfiguration()
					.getString("DEFAULT_IMPORT_CATALOG_ID"),
					configurationService.getConfiguration().getString("DEFAULT_IMPORT_CATALOG_VERSION"));
			if (null != catalogVersionModel)
			{

				resModel.setCatalogVersion(catalogVersionModel);
			}
			modelService.save(resModel);




			//modelService.saveAll(resModel);

		}
		catch (final EtailBusinessExceptions e)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
		}
		catch (final Exception ex)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + ex);
		}
		return status;
	}


	//@Override
	private String saveSellerMasterUpdate(final SellerMasterWsDTO sellerMasterWsDTO, final SellerMasterModel masterModelUpdate)
	{
		String status = MarketplacecommerceservicesConstants.SUCCESSS_RESP;
		final StringBuilder stringBuilder = new StringBuilder();

		try
		{

			final DateFormat formatter = new SimpleDateFormat(MarketplacecommerceservicesConstants.YYYYMMDD);

			//	final SellerMasterModel masterModelUpdate = resModelUpdate.getSellerMaster();
			LOG.debug("Seller master model" + masterModelUpdate);

			//removing existing corporate address starts
			final List<SellerMasterCorporateAddressModel> removableAddressList = new ArrayList<SellerMasterCorporateAddressModel>();
			for (final SellerMasterCorporateAddressModel corporateAddress : masterModelUpdate.getCorporateAddress())
			{
				removableAddressList.add(corporateAddress);
			}
			if (!removableAddressList.isEmpty())
			{
				modelService.removeAll(removableAddressList);
				LOG.debug("corporate address removed");
			}
			modelService.refresh(masterModelUpdate);
			//removing existing corporate address ends


			// removing existing SellerMaster TAX starts

			final List<SellerMasterWthhldTAXModel> removableWthhldTAXList = new ArrayList<SellerMasterWthhldTAXModel>();
			for (final SellerMasterWthhldTAXModel corpWthhldTAX : masterModelUpdate.getWthhldTAX())
			{
				removableWthhldTAXList.add(corpWthhldTAX);
			}
			if (!removableWthhldTAXList.isEmpty())
			{
				modelService.removeAll(removableWthhldTAXList);
				LOG.debug("removableWthhldTAXList removed");
			}
			modelService.refresh(masterModelUpdate);

			// removing existing SellerMaster Payment end


			//removing existing SellerMaster Payment starts

			final List<SellerMasterPaymentInfoModel> removablePaymentInfoList = new ArrayList<SellerMasterPaymentInfoModel>();
			for (final SellerMasterPaymentInfoModel corpPaymentInfo : masterModelUpdate.getPaymentInfo())
			{
				removablePaymentInfoList.add(corpPaymentInfo);
			}
			if (!removablePaymentInfoList.isEmpty())
			{
				modelService.removeAll(removablePaymentInfoList);
				LOG.debug("removablePaymentInfoList removed");
			}
			modelService.refresh(masterModelUpdate);

			//removing existing SellerMaster Payment ends


			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getId()))
			{
				//resModelUpdate.setSellerID(sellerMasterWsDTO.getId());
				masterModelUpdate.setId(sellerMasterWsDTO.getId());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getFirstname()))
			{
				LOG.debug("first name" + sellerMasterWsDTO.getFirstname());
				stringBuilder.append(sellerMasterWsDTO.getFirstname()).append(sellerMasterWsDTO.getMidname())
						.append(sellerMasterWsDTO.getLastname());

				//resModelUpdate.setSellerName(stringBuilder.toString());
				masterModelUpdate.setFirstname(sellerMasterWsDTO.getFirstname());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getType()))
			{
				final String sellerType = sellerMasterWsDTO.getType();
				final SellerTypeGlobalCodesModel sellerTypeGlobalCodesModel = mplSellerTypeGlobalCodesService
						.getDescription(sellerType);
				if (null == sellerTypeGlobalCodesModel)
				{
					status = MarketplacecommerceservicesConstants.ERROR_CODE_1;
					return status;
				}
				if (StringUtils.isNotEmpty(sellerTypeGlobalCodesModel.getDescription()))
				{
					final String description = sellerTypeGlobalCodesModel.getDescription();
					//resModelUpdate.setSellerType(description);
					masterModelUpdate.setType(description);
				}
			}


			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getMidname()))
			{
				masterModelUpdate.setMidname(sellerMasterWsDTO.getMidname());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLastname()))
			{
				masterModelUpdate.setLastname(sellerMasterWsDTO.getLastname());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLegalName()))
			{
				masterModelUpdate.setLegalName(sellerMasterWsDTO.getLegalName());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getCompanyCode()))
			{
				masterModelUpdate.setCompanyCode(sellerMasterWsDTO.getCompanyCode());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getAccountGroup()))
			{
				masterModelUpdate.setAccountGroup(sellerMasterWsDTO.getAccountGroup());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getSearch1()))
			{
				masterModelUpdate.setSearch1(sellerMasterWsDTO.getSearch1());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getSearch2()))
			{
				masterModelUpdate.setSearch2(sellerMasterWsDTO.getSearch2());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getTitle()))
			{
				masterModelUpdate.setTitle(sellerMasterWsDTO.getTitle());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLanguage()))
			{
				masterModelUpdate.setLanguage(sellerMasterWsDTO.getLanguage());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getCustomer()))
			{
				masterModelUpdate.setCustomer(sellerMasterWsDTO.getCustomer());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getTAN()))
			{
				masterModelUpdate.setTAN(sellerMasterWsDTO.getTAN());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getLST()))
			{
				masterModelUpdate.setLST(sellerMasterWsDTO.getLST());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getCST()))
			{
				masterModelUpdate.setCST(sellerMasterWsDTO.getCST());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getServiceTAXNo()))
			{
				masterModelUpdate.setServiceTAXNo(sellerMasterWsDTO.getServiceTAXNo());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getPAN()))
			{
				masterModelUpdate.setPAN(sellerMasterWsDTO.getPAN());
			}
			if (null != sellerMasterWsDTO.getPANValidThru())
			{
				masterModelUpdate.setPANValidThru(formatter.parse(formatter.format(sellerMasterWsDTO.getPANValidThru())));
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getSettlementPeriod()))
			{
				masterModelUpdate.setSettlementPeriod(sellerMasterWsDTO.getSettlementPeriod());
			}
			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getPayoutPeriod()))
			{
				masterModelUpdate.setPayoutPeriod(sellerMasterWsDTO.getPayoutPeriod());
			}

			if (null != sellerMasterWsDTO.getRegisterAddress())
			{
				setMasterModelForRegisteredAddressUpdate(sellerMasterWsDTO, masterModelUpdate);
				LOG.debug("reg address update set");

			}
			if (null != sellerMasterWsDTO.getCorporateAddress())
			{
				setMasterModelForCorporateAddressUpdate(sellerMasterWsDTO.getCorporateAddress(), masterModelUpdate);
				LOG.debug("corporate address update set");

			}

			if (null != sellerMasterWsDTO.getWthhldTAX())
			{
				setMasterModelForWthhldTAXUpdate(sellerMasterWsDTO.getWthhldTAX(), masterModelUpdate);
				LOG.debug("MasterModelForWthhldTAXUpdate update set");
			}

			if (null != sellerMasterWsDTO.getPaymentInfo())
			{
				setMasterModelForPaymentInfoUpdate(sellerMasterWsDTO.getPaymentInfo(), masterModelUpdate);
				LOG.debug("etMasterModelForPaymentInfoUpdate update set");
			}

			modelService.saveAll(masterModelUpdate);

		}

		catch (final EtailBusinessExceptions e)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
		}
		catch (final Exception ex)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG, ex);
			//ex.printStackTrace();
		}

		return status;
	}



	@Override
	public String insertUpdate(final SellerMasterWsDTO sellerMasterWsDTO)
	{
		String status = "";
		try
		{
			List<SellerInformationModel> sellerInfoModelList = null;

			SellerMasterResponseWsDTO sellerMasterRes = null;
			SellerMasterModel sellerMasterModel = null;
			/*
			 * if (StringUtils.isNotEmpty(sellerMasterWsDTO.getId())) { sellerInfoWithoutSellerMaster =
			 * mplSellerInformationService.getSellerInformationWithSellerMaster(sellerMasterWsDTO .getId()); }
			 */

			if (StringUtils.isNotEmpty(sellerMasterWsDTO.getId()))
			{
				sellerInfoModelList = mplSellerInformationDAO.getSellerInformation(sellerMasterWsDTO.getId());

				if (StringUtils.isNotEmpty(sellerMasterWsDTO.getIsupdate()) && sellerMasterWsDTO.getIsupdate().equalsIgnoreCase("I"))
				{
					sellerMasterRes = saveSellerMaster(sellerMasterWsDTO);
					if (!CollectionUtils.isEmpty(sellerInfoModelList) && null != sellerMasterRes)
					{
						boolean isSellerInfoUpdate = false;
						//sellerMasterRes = saveSellerMaster(sellerMasterWsDTO);
						isSellerInfoUpdate = updateSellerInfoWithSellerMaster(sellerInfoModelList, sellerMasterRes);
						if (!isSellerInfoUpdate)
						{
							status = MarketplacecommerceservicesConstants.ERROR_FLAG;
						}
					}
					else
					{
						//sellerMasterRes = saveSellerMaster(sellerMasterWsDTO);
						if (null != sellerMasterRes && null != sellerMasterRes.getSellerMaster())
						{
							status = saveSellerInformation(sellerMasterWsDTO, sellerMasterRes.getSellerMaster());
							return status;
						}

					}
				}
				else if (StringUtils.isNotEmpty(sellerMasterWsDTO.getIsupdate())
						&& sellerMasterWsDTO.getIsupdate().equalsIgnoreCase("U"))
				{
					if (StringUtils.isNotEmpty(sellerMasterWsDTO.getId()))
					{
						sellerMasterModel = mplSellerMasterDao.getSellerMaster(sellerMasterWsDTO.getId());
					}
					if (null != sellerMasterModel)
					{
						status = saveSellerMasterUpdate(sellerMasterWsDTO, sellerMasterModel);
						return status;
					}
					else
					{
						sellerMasterRes = saveSellerMaster(sellerMasterWsDTO);
						boolean isSellerInfoUpdate = false;
						if (!CollectionUtils.isEmpty(sellerInfoModelList))
						{
							isSellerInfoUpdate = updateSellerInfoWithSellerMaster(sellerInfoModelList, sellerMasterRes);
							if (!isSellerInfoUpdate)
							{
								status = MarketplacecommerceservicesConstants.ERROR_FLAG;
							}
						}
						else
						{
							if (null != sellerMasterRes && null != sellerMasterRes.getSellerMaster())
							{
								status = saveSellerInformation(sellerMasterWsDTO, sellerMasterRes.getSellerMaster());
								return status;
							}
						}
					}
				}
				else
				{
					status = MarketplacecommerceservicesConstants.ERROR_FLAG;
				}
				if (null != sellerMasterRes && StringUtils.isNotEmpty(sellerMasterRes.getStatus()))
				{
					status = sellerMasterRes.getStatus();
				}
			}

			/*
			 * if (sellerMasterWsDTO.getIsupdate().equalsIgnoreCase("U")) {
			 * 
			 * // if (masterModel.getId().equals(sellerMasterWsDTO.getId())) // { //final SellerInformationModel
			 * resModelUpdate = mplSellerInformationDAO.getSellerInformation(sellerMasterWsDTO.getId()); if (resModelUpdate
			 * == null) {
			 * 
			 * 
			 * sellerMasterRes = saveSellerMaster(sellerMasterWsDTO); if (null != sellerMasterRes &&
			 * StringUtils.isNotEmpty(sellerMasterRes.getStatus())) { status = sellerMasterRes.getStatus(); } if (null !=
			 * sellerMasterRes && null != sellerMasterRes.getSellerMaster()) { status =
			 * saveSellerInformation(sellerMasterWsDTO, sellerMasterRes.getSellerMaster()); } } else { status =
			 * saveSellerInformationUpdate(sellerMasterWsDTO, resModelUpdate); } //}
			 * 
			 * }
			 * 
			 * else if (sellerMasterWsDTO.getIsupdate().equalsIgnoreCase("I")) { status =
			 * saveSellerInformation(sellerMasterWsDTO); }
			 */
		}
		catch (final Exception e)
		{
			status = MarketplacecommerceservicesConstants.ERROR_FLAG;
			LOG.error(MarketplacecommerceservicesConstants.SELLER_MASTER_ERROR_MSG + ":" + e);
		}

		return status;

	}
}
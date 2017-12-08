/**
 *
 */
package com.tisl.mpl.bin.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.tisl.mpl.bin.constants.MarketplaceBinDbConstants;
import com.tisl.mpl.bin.dao.BinDao;
import com.tisl.mpl.bin.pojo.BankDataPojo;
import com.tisl.mpl.bin.service.BinService;
import com.tisl.mpl.binDb.model.BinModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.TypeOfError;
import com.tisl.mpl.core.model.BinErrorModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
@Component
@Qualifier(MarketplaceBinDbConstants.BINSERVICE)
public class BinServiceImpl implements BinService
{
	@Resource
	private BinDao binDao;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BinServiceImpl.class.getName());

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ModelService modelService;


	/**
	 * @Description This method sends the bin to call DB to fetch details related to Bin
	 *
	 * @param bin
	 * @return BinModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public BinModel checkBin(final String bin, final String cardType, final String mplCustomerID, final boolean isErrorCreation)
			throws EtailNonBusinessExceptions
	{
		final BinModel binmodel = getBinDao().fetchBankFromBin(bin);
		//Added For TPR-1035
		if (null == binmodel && isErrorCreation) // SONAR FIX
		{
			final BinErrorModel binError = getModelService().create(BinErrorModel.class);
			binError.setBin(bin);
			binError.setPaymentMode(cardType);
			binError.setTypeOfError(TypeOfError.BINNOTFOUND);
			binError.setCustomerId(mplCustomerID);
			getModelService().save(binError);
		}
		else if (null != binmodel && StringUtils.isNotEmpty(binmodel.getBinNo())
				&& !binmodel.getCardType().equalsIgnoreCase(cardType) && isErrorCreation) // SONAR FIX
		{
			final BinErrorModel binError = getModelService().create(BinErrorModel.class);
			binError.setBin(bin);
			binError.setPaymentMode(cardType);
			binError.setCustomerId(mplCustomerID);
			binError.setTypeOfError(TypeOfError.CARDMISMATCH);
			getModelService().save(binError);
		}
		return binmodel;
	}


	/**
	 *
	 * @Description : Generate CSV with Bank Details present in Bin but not in Bank
	 */
	@Override
	public void generateFileData()
	{
		try
		{
			final List<String> bankNameList = getBinDao().getBankDetails();
			if (CollectionUtils.isNotEmpty(bankNameList))
			{
				final List<BankDataPojo> csvBankDataList = populateData(bankNameList);
				if (CollectionUtils.isNotEmpty(csvBankDataList))
				{
					generateCSVData(csvBankDataList);
				}
			}
		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			throw exception;
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			throw exception;
		}
	}

	/**
	 * @Description : Generate CSV with Bank Details present in Bin but not in Bank
	 *
	 * @param csvBankDataList
	 */
	private void generateCSVData(final List<BankDataPojo> csvBankDataList)
	{
		FileWriter fileWriter = null;
		final File rootFolder = new File(configurationService.getConfiguration().getString(
				MarketplaceBinDbConstants.BANK_FILE_LOCATION, MarketplaceBinDbConstants.BANK_FILE_LOCATION_DATA),
				MarketplaceBinDbConstants.BANK_FILE_NAME
						+ configurationService.getConfiguration().getString("payment.bank.file.extension", ".csv"));
		try
		{
			fileWriter = new FileWriter(rootFolder);

			for (final BankDataPojo data : csvBankDataList)
			{
				fileWriter.append(data.getBankName().toUpperCase());
				fileWriter.append(MarketplaceBinDbConstants.BANK_FILE_DELIMITTER);
				fileWriter.append(data.getBankCode().toUpperCase());
				fileWriter.append(MarketplaceBinDbConstants.BANK_FILE_DELIMITTER);
				fileWriter.append(data.getBaseStoreUId());
				fileWriter.append(MarketplaceBinDbConstants.BANK_FILE_NEW_LINE_SEPARATOR);
			}
		}
		catch (final IOException exception)
		{
			LOG.error("IO Exception", exception);
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}
		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException exception)
			{
				LOG.error("Error while flushing/closing fileWriter !!!" + exception.getMessage());
			}
		}
	}


	/**
	 * @description Populate Data in POJO Class
	 *
	 * @param bankNameList
	 * @return csvBankDataList
	 */
	private List<BankDataPojo> populateData(final List<String> bankNameList)
	{
		final List<BankDataPojo> csvBankDataList = new ArrayList<BankDataPojo>();
		for (final String bankData : bankNameList)
		{
			if (StringUtils.isNotEmpty(bankData))
			{
				final BankDataPojo pojo = validateBankData(bankData);
				if (null != pojo)
				{
					csvBankDataList.add(pojo);
				}
			}
		}
		return csvBankDataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.bin.service.BinService#fetchBankFromCustomerSavedCard(java.lang.String,
	 * de.hybris.platform.core.model.user.CustomerModel)
	 */
	//TPR-7486
	@Override
	public String fetchBankFromCustomerSavedCard(final String cardRefNum, final CustomerModel Customer)
	{
		return getBinDao().fetchBankFromCustomerSavedCard(cardRefNum, Customer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.bin.service.BinService#fetchBanknameFromBin(java.lang.String)
	 */
	//TPR-7486
	@Override
	public String fetchBanknameFromBin(final String bin) throws EtailNonBusinessExceptions
	{
		String bankname = MarketplacecommerceservicesConstants.EMPTY;
		final BinModel binmodel = getBinDao().fetchBankFromBin(bin);
		if (null != binmodel && StringUtils.isNotEmpty(binmodel.getBankName()))
		{
			bankname = binmodel.getBankName();
		}
		return bankname;
	}

	/**
	 * Validate Bank Data
	 *
	 * @param bankData
	 * @return BankDataPojo
	 */
	private BankDataPojo validateBankData(final String bankData)
	{
		final BankDataPojo pojo = new BankDataPojo();
		if (bankData.contains(MarketplaceBinDbConstants.COMMA))
		{
			final StringBuilder sb = new StringBuilder();
			sb.append(MarketplaceBinDbConstants.BANK_FILE_SLASH);
			sb.append(bankData);
			sb.append(MarketplaceBinDbConstants.BANK_FILE_SLASH);
			pojo.setBankCode(sb.toString());
			pojo.setBankName(sb.toString());
		}
		else
		{
			pojo.setBankCode(bankData);
			pojo.setBankName(bankData);
		}
		pojo.setBaseStoreUId(MarketplaceBinDbConstants.BASESTORE_UID);
		return pojo;
	}

	/**
	 * @return the binDao
	 */
	public BinDao getBinDao()
	{
		return binDao;
	}

	/**
	 * @param binDao
	 *           the binDao to set
	 */
	public void setBinDao(final BinDao binDao)
	{
		this.binDao = binDao;
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
}

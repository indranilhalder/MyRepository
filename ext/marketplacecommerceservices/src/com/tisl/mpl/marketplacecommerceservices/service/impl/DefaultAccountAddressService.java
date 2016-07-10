/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.daos.AccountAddressDao;
import com.tisl.mpl.marketplacecommerceservices.service.AccountAddressService;
import com.tisl.mpl.model.StateModel;


/**
 * @author TCS
 *
 */
public class DefaultAccountAddressService implements AccountAddressService
{
	@Autowired
	private AccountAddressDao accountAddressDao;

	/**
	 * @return the accountAddressDao
	 */
	public AccountAddressDao getAccountAddressDao()
	{
		return accountAddressDao;
	}

	/**
	 * @param accountAddressDao
	 *           the accountAddressDao to set
	 */
	public void setAccountAddressDao(final AccountAddressDao accountAddressDao)
	{
		this.accountAddressDao = accountAddressDao;
	}

	/**
	 *
	 */
	@Override
	public List<StateData> getStates()
	{
		try
		{
			final List<StateData> stateDataList = new ArrayList<StateData>();
			final List<StateModel> stateModelList = accountAddressDao.getStates();

			//if (null != stateModelList && stateModelList.size() > 0)
			if (CollectionUtils.isNotEmpty(stateModelList))
			{
				for (final StateModel newModel : stateModelList)
				{
					final StateData oStateData = new StateData();
					oStateData.setCountryKey(newModel.getCountrykey());
					oStateData.setCode(newModel.getRegion());
					oStateData.setName(newModel.getDescription());
					stateDataList.add(oStateData);
				}
			}
			return stateDataList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}

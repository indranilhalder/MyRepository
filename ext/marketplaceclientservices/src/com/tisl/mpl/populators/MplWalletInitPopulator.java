/**
 * 
 */
package com.tisl.mpl.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.QCInitResponseDetailModel;
import com.tisl.mpl.service.QCInitDataBean;


/**
 * @author TUL
 *
 */
public class MplWalletInitPopulator implements Populator<QCInitResponseDetailModel, QCInitDataBean>
{


	@Override
	public void populate(QCInitResponseDetailModel source, QCInitDataBean target) throws ConversionException
	{

		if(StringUtils.isNotEmpty(source.getCurrentBatchNumber().toString()))
		{

			target.setCurrentBatchNumber(source.getCurrentBatchNumber().toString());
			System.out.println("Setting Wallet Batch Number :::::::::::::::: "+source.getCurrentBatchNumber().toString());
		}

	}

}

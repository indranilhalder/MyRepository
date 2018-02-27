/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.QCInitResponseDetailModel;
import com.tisl.mpl.daos.impl.MplQCInitDataImpl;
import com.tisl.mpl.pojo.response.QCInitializationResponse;


/**
 * @author TUL
 *
 */
public class MplQCInitServiceImpl
{

	@Resource
	public MplQCInitDataImpl mplQCInitDataImpl;

	@Resource(name = "mplWalletServices")
	private MplWalletServices mplWalletServices;

	@Resource(name = "modelService")
	private ModelService modelService;
	
	@Resource(name = "qcInitDataBean")
	private QCInitDataBean qcInitDataBean;
	
	

	/**
	 * @return the qcInitDataBean
	 */
	public QCInitDataBean getQcInitDataBean()
	{
		return qcInitDataBean;
	}

	/**
	 * @param qcInitDataBean the qcInitDataBean to set
	 */
	public void setQcInitDataBean(QCInitDataBean qcInitDataBean)
	{
		this.qcInitDataBean = qcInitDataBean;
	}

	private Converter<QCInitResponseDetailModel, QCInitDataBean> mplWalletInitConvertor;
	

	/**
	 * @return the mplWalletInitConvertor
	 */
	public Converter<QCInitResponseDetailModel, QCInitDataBean> getMplWalletInitConvertor()
	{
		return mplWalletInitConvertor;
	}

	/**
	 * @param mplWalletInitConvertor the mplWalletInitConvertor to set
	 */
	public void setMplWalletInitConvertor(Converter<QCInitResponseDetailModel, QCInitDataBean> mplWalletInitConvertor)
	{
		this.mplWalletInitConvertor = mplWalletInitConvertor;
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

	/**
	 * @return the mplWalletServices
	 */
	public MplWalletServices getMplWalletServices()
	{
		return mplWalletServices;
	}

	/**
	 * @param mplWalletServices
	 *           the mplWalletServices to set
	 */
	public void setMplWalletServices(final MplWalletServices mplWalletServices)
	{
		this.mplWalletServices = mplWalletServices;
	}

	/**
	 * @return the mplQCInitDataImpl
	 */
	public MplQCInitDataImpl getMplQCInitDataImpl()
	{
		return mplQCInitDataImpl;
	}

	/**
	 * @param mplQCInitDataImpl
	 *           the mplQCInitDataImpl to set
	 */
	public void setMplQCInitDataImpl(final MplQCInitDataImpl mplQCInitDataImpl)
	{
		this.mplQCInitDataImpl = mplQCInitDataImpl;
	}

	/**
	 * Initializes Check For QC
	 */
	public void init() 
	{
		try
		{			
			QCInitResponseDetailModel qcInitModel = getMplQCInitDataImpl().getQCInitDetailDataImpl();

			if (null != qcInitModel)
			{
				//getMplWalletInitConvertor().convert(qcInitModel);
				
				getQcInitDataBean().setCurrentBatchNumber("" + qcInitModel.getCurrentBatchNumber());

			}
			else
			{
				final QCInitializationResponse initResponse = getMplWalletServices().walletInitilization();

				if (null != initResponse && Integer.parseInt("" + initResponse.getResponseCode()) == 0)
				{
					final QCInitResponseDetailModel qcInitModal = getModelService().create(QCInitResponseDetailModel.class);

					qcInitModal.setAcquirerId(initResponse.getApiWebProperties().getAcquirerId());

					qcInitModal.setCurrentBatchNumber(Long.valueOf("" + initResponse.getApiWebProperties().getCurrentBatchNumber()));

					getModelService().save(qcInitModal);

					final QCInitResponseDetailModel qcInitModelData = getMplQCInitDataImpl().getQCInitDetailDataImpl();

					if (null != qcInitModelData)
					{
						//getMplWalletInitConvertor().convert(qcInitModelData);
						getQcInitDataBean().setCurrentBatchNumber("" + qcInitModelData.getCurrentBatchNumber());

					}
				}
			}
		}
		catch (final Exception ex)
		{
			
			ex.printStackTrace();
		}

	}


}

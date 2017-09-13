/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.QCInitResponseDetailModel;
import com.tisl.mpl.daos.impl.MplQCInitDataImpl;
import com.tisl.mpl.exception.QCServiceCallException;
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
	public QCInitDataBean qcInitDataBean;

	/**
	 * @return the qcInitDataBean
	 */
	public QCInitDataBean getQcInitDataBean()
	{
		return qcInitDataBean;
	}

	/**
	 * @param qcInitDataBean
	 *           the qcInitDataBean to set
	 */
	public void setQcInitDataBean(final QCInitDataBean qcInitDataBean)
	{
		this.qcInitDataBean = qcInitDataBean;
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
	public void loadQCInit() throws QCServiceCallException
	{


		QCInitResponseDetailModel qcInitModel = new QCInitResponseDetailModel();

		try
		{
			//			if (null == getQcInitDataBean().getCurrentBatchNumber()
			//					&& StringUtils.isEmpty(getQcInitDataBean().getCurrentBatchNumber()))
			//			{

			System.out.println("******************************************  Start INIT *********************************");
			qcInitModel = getMplQCInitDataImpl().getQCInitDetailDataImpl();

			if (null != qcInitModel)
			{
				getQcInitDataBean().setCurrentBatchNumber("" + qcInitModel.getCurrentBatchNumber());

			}
			else
			{

				System.out.println("******* QC SERVICE CALL FOR INITILIZATION *****");
				final QCInitializationResponse initResponse = getMplWalletServices().walletInitilization();

				if (null != initResponse && Integer.parseInt("" + initResponse.getResponseCode()) == 0)
				{
					final QCInitResponseDetailModel qcInitModal = getModelService().create(QCInitResponseDetailModel.class);

					qcInitModal.setAcquirerId(initResponse.getApiWebProperties().getAcquirerId());

					qcInitModal.setCurrentBatchNumber(Long.valueOf("" + initResponse.getApiWebProperties().getCurrentBatchNumber()));

					getModelService().save(qcInitModal);

					qcInitModel = getMplQCInitDataImpl().getQCInitDetailDataImpl();


					if (null != qcInitModel)
					{
						getQcInitDataBean().setCurrentBatchNumber("" + qcInitModel.getCurrentBatchNumber());

					}
				}
			}
			//}
		}
		catch (final Exception ex)
		{

			ex.printStackTrace();
		}

	}


}

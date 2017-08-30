/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.services.pancard.impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.pancard.MplPancardDao;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService;
import com.tisl.mpl.pojo.OrderLineResData;
import com.tisl.mpl.pojo.PanCardResDTO;
import com.tisl.mpl.service.MplPancardUploadService;


/**
 * @author TCS
 *
 */
public class MplPancardServiceImpl implements MplPancardService
{
	private static final Logger LOG = Logger.getLogger(MplPancardServiceImpl.class);

	public static final String PANCARD_PATH = "user.pancard.folder.location";
	//public static final String PANCARD_IMAGE_EXTENSION = "user.pancard.folder.extension";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "mplPancardDao")
	private MplPancardDao mplPancardDao;


	@Resource(name = "mplPancardUploadserviceImpl")
	private MplPancardUploadService mplPancardUploadserviceImpl;

	@Resource(name = "notificationService")
	private NotificationService notificationService;


	/**
	 * @return the mplPancardDao
	 */
	public MplPancardDao getMplPancardDao()
	{
		return mplPancardDao;
	}

	/**
	 * @param mplPancardDao
	 *           the mplPancardDao to set
	 */
	public void setMplPancardDao(final MplPancardDao mplPancardDao)
	{
		this.mplPancardDao = mplPancardDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService#getPanCardOrderId(java.lang.String)
	 */

	//for checking model corresponding to the  existing orderid already exists or not
	@Override
	public List<PancardInformationModel> getPanCardOrderId(final String orderreferancenumber)
	{


		return mplPancardDao.getPanCardOrderId(orderreferancenumber);

	}

	//For sending pancard details to SP through PI and save data into database for new pancard entry
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService#setPanCardDetailsAndPIcall(java.lang
	 * .String, java.lang.String, java.lang.String, java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public void setPanCardDetailsAndPIcall(final String orderreferancenumber, final List<String> transactionidList,
			final String customername, final String pancardnumber, final MultipartFile file) throws IllegalStateException,
			IOException, JAXBException
	{
		// YTODO Auto-generated method stub

		String pancardImagePath = null;
		final LocalDate localDate = LocalDate.now();
		final String datepath = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDate);

		final String fileUploadDirectory = configurationService.getConfiguration().getString(PANCARD_PATH);
		final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		final String fileName = orderreferancenumber + "-"
				+ DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss").format(LocalDateTime.now()) + fileExtension;

		if (StringUtils.isNotEmpty(fileUploadDirectory))
		{
			pancardImagePath = fileUploadDirectory + "/" + datepath + "/" + fileName;
		}
		final File newFile = new File(pancardImagePath);
		if (!newFile.exists())
		{
			newFile.mkdirs();
		}

		file.transferTo(newFile);


		//generate xml in PI---->SP starts
		final String status = mplPancardUploadserviceImpl.generateXmlForPanCard(null, orderreferancenumber, transactionidList,
				pancardImagePath);
		//generate xml in PI---->SP ends

		//save data in model starts
		//final PancardInformationModel panCardModel = modelService.create(PancardInformationModel.class);
		PancardInformationModel panCardModel = null;
		final List<PancardInformationModel> panCardModelList = new ArrayList<PancardInformationModel>();

		for (final String transId : transactionidList)
		{
			panCardModel = modelService.create(PancardInformationModel.class);
			if (StringUtils.isNotEmpty(orderreferancenumber))
			{
				panCardModel.setOrderId(orderreferancenumber);
			}
			if (StringUtils.isNotEmpty(pancardImagePath))
			{
				panCardModel.setPath(pancardImagePath);
			}
			if (StringUtils.isNotEmpty(transId))
			{
				panCardModel.setTransactionId(transId);
			}
			if (StringUtils.isNotEmpty(customername))
			{
				panCardModel.setName(customername);
			}
			if (StringUtils.isNotEmpty(pancardnumber))
			{
				panCardModel.setPancardNumber(pancardnumber);
			}
			if (StringUtils.isNotEmpty(status) && !"faliure".equalsIgnoreCase(status))
			{
				LOG.debug("if " + MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION + " " + status);
				panCardModel.setStatus(MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION);
			}
			else
			{
				LOG.debug("if " + MarketplacecommerceservicesConstants.NA + " " + status);
				panCardModel.setStatus(MarketplacecommerceservicesConstants.NA);
			}
			panCardModelList.add(panCardModel);
		}
		modelService.saveAll(panCardModelList);
		//save data in model ends

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService#refreshPanCardDetailsAndPIcall(de.
	 * hybris.platform.core.model.PancardInformationModel, java.lang.String,
	 * org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public void refreshPanCardDetailsAndPIcall(final List<PancardInformationModel> pModelList,
			final List<PancardInformationModel> pModel, final String pancardnumber, final MultipartFile file)
			throws IllegalStateException, IOException, JAXBException
	{
		// YTODO Auto-generated method stub
		String previousFolderPath = "";
		String fileExtension = "";
		String fileName = "";
		String updatedImagePath = "";
		String orderId = "";
		OrderModel orderModel = null;

		final List<PancardInformationModel> pancardList = new ArrayList<PancardInformationModel>();

		final LocalDateTime localDate = LocalDateTime.now();
		final String datepath = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss").format(localDate);

		if (CollectionUtils.isNotEmpty(pModel))
		{
			orderId = pModel.get(0).getOrderId();

			final List<OrderModel> oModel = getOrderForCode(orderId);

			if (CollectionUtils.isNotEmpty(oModel))
			{
				orderModel = oModel.get(0);
			}
			previousFolderPath = pModel.get(0).getPath().substring(0, pModel.get(0).getPath().lastIndexOf("/"));
			fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			fileName = orderId + "-" + datepath + fileExtension;

			updatedImagePath = previousFolderPath + "/" + fileName;

			final File newFile = new File(updatedImagePath);
			if (!newFile.exists())
			{
				newFile.mkdirs();
			}
			file.transferTo(newFile);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("****pancard image path update**" + updatedImagePath);
			}
		}

		if (StringUtils.isNotEmpty(orderId) && StringUtils.isNotEmpty(updatedImagePath) && CollectionUtils.isNotEmpty(pModelList))
		{
			//generate xml in PI---->SP starts
			final String status = mplPancardUploadserviceImpl.generateXmlForPanCard(pModelList, orderId, null, updatedImagePath);
			//generate xml in PI---->SP ends

			if (CollectionUtils.isNotEmpty(pModel) && StringUtils.isNotEmpty(updatedImagePath))
			{
				for (final PancardInformationModel pm : pModel)
				{
					pm.setPath(updatedImagePath);
					pm.setPancardNumber(pancardnumber);
					if (StringUtils.isNotEmpty(status) && !"faliure".equalsIgnoreCase(status))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("if " + MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION + " " + status);
						}
						pm.setStatus(MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION);
					}
					else
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("if " + MarketplacecommerceservicesConstants.NA + " " + status);
						}
						pm.setStatus(MarketplacecommerceservicesConstants.NA);
					}
					pancardList.add(pm);
				}
				modelService.saveAll(pancardList);
				orderModel.setIsRejMailTriggred(Boolean.FALSE);
				modelService.save(orderModel);
			}
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService#getOrderForCode(java.lang.String)
	 */
	@Override
	public List<OrderModel> getOrderForCode(final String orderreferancenumber)
	{
		// YTODO Auto-generated method stub
		return mplPancardDao.getOrderForCode(orderreferancenumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService#setPancardRes(com.tisl.mpl.pojo.
	 * PanCardResDTO)
	 */
	@Override
	public void setPancardRes(final PanCardResDTO resDTO) throws JAXBException
	{
		// YTODO Auto-generated method stub
		final List<PancardInformationModel> pModel = getPanCardOrderId(resDTO.getOrderId());
		OrderModel orderModel = null;

		final List<OrderModel> oModel = getOrderForCode(resDTO.getOrderId());

		if (CollectionUtils.isNotEmpty(oModel))
		{
			orderModel = oModel.get(0);
		}

		if (null != pModel)
		{
			for (final PancardInformationModel pmNew : pModel)
			{
				for (final OrderLineResData olrs : resDTO.getOrderLine())
				{
					if (StringUtils.isNotEmpty(pmNew.getTransactionId()) && StringUtils.isNotEmpty(olrs.getTransactionId())
							&& pmNew.getTransactionId().equalsIgnoreCase(olrs.getTransactionId()))
					{
						if (StringUtils.isNotEmpty(olrs.getPancardStatus()))
						{
							pmNew.setStatus(olrs.getPancardStatus());
							modelService.save(pmNew);
						}
					}
				}
			}

			if (null != orderModel)
			{
				for (final PancardInformationModel pmNew : pModel)
				{
					if (StringUtils.isNotEmpty(pmNew.getStatus())
							&& MarketplacecommerceservicesConstants.REJECTED.equalsIgnoreCase(pmNew.getStatus()))
					{

						if (BooleanUtils.isTrue(orderModel.getIsRejMailTriggred()))
						{
							break;
						}
						else
						{
							try
							{
								notificationService.triggerEmailAndSmsOnPancardReject(orderModel);
								orderModel.setIsRejMailTriggred(Boolean.TRUE);
								modelService.save(orderModel);
							}
							catch (final Exception e1)
							{ // YTODO
							  // Auto-generated catch block
								LOG.error("Exception during sending mail >> " + e1);
							}
						}
					}
				}
			}

		}
	}

}

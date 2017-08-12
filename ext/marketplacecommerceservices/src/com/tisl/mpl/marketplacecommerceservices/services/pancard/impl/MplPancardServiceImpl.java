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
import com.tisl.mpl.xml.pojo.CRMpancardResponse;


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


	//for creating ticket and saving it in the db i.e. first time entry
	@Override
	public void setPancardFileAndOrderIdservice(final String orderreferancenumber, final String transactionid,
			final String customername, final String pancardnumber, final MultipartFile file)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String fileUploadDirectory = configurationService.getConfiguration().getString(PANCARD_PATH);
			//commenting as we will save the extension as it is
			//final String fileExtension = configurationService.getConfiguration().getString(PANCARD_IMAGE_EXTENSION);

			final String storageDirectory = fileUploadDirectory;
			final String newFilenameBase = orderreferancenumber;
			final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

			final String newFilename = newFilenameBase + fileExtension;

			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			final LocalDate localDate = LocalDate.now();

			final String datepath = dtf.format(localDate);
			final String newpath = storageDirectory + "/" + datepath + "/" + newFilename;
			final File newFile = new File(newpath);
			if (!newFile.exists())
			{
				newFile.mkdirs();
			}

			file.transferTo(newFile);

			LOG.debug("****pancard image path**" + newpath);

			//final String newpath = storageDirectory + "/" + newFilename;
			//System.out.println("*******************Pancard details save loaction::::" + newpath);

			//here we have to get the response and set the status and pan card number
			final String ticket = mplPancardUploadserviceImpl.createticketPancardModeltoDTO(orderreferancenumber, transactionid,
					newpath, pancardnumber);
			String response = null;

			if (StringUtils.isNotEmpty(ticket))
			{
				response = mplPancardUploadserviceImpl.createCrmTicket(ticket);//For the first time we have to set the order status as In progress
			}

			final PancardInformationModel consumed = modelService.create(PancardInformationModel.class);
			consumed.setOrderId(orderreferancenumber);
			consumed.setPath(newpath);
			consumed.setTransactionId(transactionid);
			consumed.setName(customername);
			consumed.setPancardNumber(pancardnumber);

			if (StringUtils.isNotEmpty(response))//For success submission we need to set the response as In progress for the first time(Hard coded )
			{
				consumed.setStatus("In progress");
			}

			modelService.save(consumed);
		}
		catch (final JAXBException ex)
		{
			ex.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService#refreshPancardDetailsService(de.hybris
	 * .platform.core.model.PancardInformationModel, org.springframework.web.multipart.MultipartFile)
	 */


	//for updating the pancard details
	@Override
	public void refreshPancardDetailsService(final PancardInformationModel oModel, final MultipartFile file,
			final String pancardnumber)
	{


		try
		{
			// YTODO Auto-generated method stub
			final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String updatedPath = null;
			String previousExtension = null;
			if (null != oModel && StringUtils.isNotEmpty(oModel.getPath()))
			{
				previousExtension = oModel.getPath().substring(oModel.getPath().lastIndexOf("."));
				updatedPath = oModel.getPath().replace(previousExtension, fileExtension);
			}
			final File newFile = new File(updatedPath);
			if (!newFile.exists())
			{
				newFile.mkdirs();
			}

			file.transferTo(newFile);

			LOG.debug("****pancard image path update**" + updatedPath);

			final String ticket = mplPancardUploadserviceImpl.ticketPancardModeltoDTO(oModel, updatedPath, pancardnumber);
			//final String ticket = mplPancardTicketCRMservice.createticketPancardModeltoDTO(oModel.getOrderId(),
			//	oModel.getTransactionId(), newpath, pancardnumber);

			String response = null;
			oModel.setPath(updatedPath);
			oModel.setPancardNumber(pancardnumber);
			if (StringUtils.isNotEmpty(ticket))
			{
				response = mplPancardUploadserviceImpl.createCrmTicket(ticket);
			}
			if (StringUtils.isNotEmpty(response) && StringUtils.isNotEmpty(oModel.getStatus()))
			{
				oModel.setStatus(oModel.getStatus());
			}
			modelService.save(oModel);

		}
		catch (final JAXBException ex)
		{
			ex.printStackTrace();
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService#getCrmStatusForPancardDetailsFacade
	 * (de.hybris.platform.core.model.PancardInformationModel)
	 */
	@Override
	public String getCrmStatusForPancardDetailsService(final PancardInformationModel oModel)
	{
		// YTODO Auto-generated method stub
		String crmStatus = null;
		final CRMpancardResponse crmpancardResponse = mplPancardUploadserviceImpl.getCrmStatusForPancardDetails(oModel);
		if (crmpancardResponse != null && StringUtils.isNotEmpty(crmpancardResponse.getStatus()))
		{
			crmStatus = crmpancardResponse.getStatus();
		}
		if (StringUtils.isNotEmpty(crmStatus))
		{
			oModel.setStatus(crmStatus);
			modelService.save(oModel);
		}
		return crmStatus;
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
		final PancardInformationModel panCardModel = modelService.create(PancardInformationModel.class);
		final List<PancardInformationModel> panCardModelList = new ArrayList<PancardInformationModel>();

		for (final String transId : transactionidList)
		{
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
			final PancardInformationModel pModel, final String pancardnumber, final MultipartFile file)
			throws IllegalStateException, IOException, JAXBException
	{
		// YTODO Auto-generated method stub
		final LocalDateTime localDate = LocalDateTime.now();
		final String datepath = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss").format(localDate);

		final String previousFolderPath = pModel.getPath().substring(0, pModel.getPath().lastIndexOf("/"));
		final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		final String fileName = pModel.getOrderId() + "-" + datepath + fileExtension;

		final String updatedImagePath = previousFolderPath + "/" + fileName;

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

		//generate xml in PI---->SP starts
		final String status = mplPancardUploadserviceImpl.generateXmlForPanCard(pModelList, pModel.getOrderId(), null,
				updatedImagePath);
		//generate xml in PI---->SP ends
		pModel.setPath(updatedImagePath);
		pModel.setPancardNumber(pancardnumber);

		if (null != status && StringUtils.isNotEmpty(status) && !"faliure".equalsIgnoreCase(status))
		{
			pModel.setStatus(MarketplacecommerceservicesConstants.PENDING_FOR_VERIFICATION);
		}
		else
		{
			pModel.setStatus(MarketplacecommerceservicesConstants.NA);
		}
		modelService.save(pModel);

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
		final List<PancardInformationModel> pModelR = new ArrayList();
		boolean isMailSent = false;
		String pancardStatus = null;

		final List<OrderModel> oModel = getOrderForCode(resDTO.getOrderId());

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
					pancardStatus = olrs.getPancardStatus();
				}
			}
			if (MarketplacecommerceservicesConstants.REJECTED.equalsIgnoreCase(pancardStatus))
			{
				for (final PancardInformationModel pm : pModel)
				{
					if (MarketplacecommerceservicesConstants.REJECTED.equalsIgnoreCase(pm.getStatus()))
					{
						if (pm.getIsMailTriggred().equals(Boolean.TRUE))
						{
							isMailSent = true;
							break;
						}
						else
						{
							//trigger mail for pancard reject
							try
							{
								if (null != oModel.get(0))
								{
									notificationService.triggerEmailAndSmsOnPancardReject(oModel.get(0));
									isMailSent = true;
								}
							}
							catch (final Exception e1)
							{ // YTODO
							  // Auto-generated catch block
								LOG.error("Exception during sending mail >> " + e1.getMessage());
							}

							break;
						}
					}
				}
				if (isMailSent)
				{
					for (final PancardInformationModel pm : pModel)
					{
						if (Boolean.FALSE.equals(pm.getIsMailTriggred()))
						{
							pm.setIsMailTriggred(Boolean.TRUE);
							pModelR.add(pm);
						}
					}
					modelService.saveAll(pModelR);
				}
			}
		}
	}

}

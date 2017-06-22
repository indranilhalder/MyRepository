/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.services.pancard.impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.marketplacecommerceservices.daos.pancard.MplPancardDao;
import com.tisl.mpl.marketplacecommerceservices.services.pancard.MplPancardService;
import com.tisl.mpl.service.MplPancardTicketCRMservice;
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


	@Resource(name = "mplPancardTicketCRMserviceImpl")
	private MplPancardTicketCRMservice mplPancardTicketCRMservice;


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
	public PancardInformationModel getPanCardOrderId(final String orderreferancenumber)
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
			final String ticket = mplPancardTicketCRMservice.createticketPancardModeltoDTO(orderreferancenumber, transactionid,
					newpath, pancardnumber);
			String response = null;

			if (StringUtils.isNotEmpty(ticket))
			{
				response = mplPancardTicketCRMservice.createCrmTicket(ticket);//For the first time we have to set the order status as In progress
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

			final String ticket = mplPancardTicketCRMservice.ticketPancardModeltoDTO(oModel, updatedPath, pancardnumber);
			//final String ticket = mplPancardTicketCRMservice.createticketPancardModeltoDTO(oModel.getOrderId(),
			//	oModel.getTransactionId(), newpath, pancardnumber);

			String response = null;
			oModel.setPath(updatedPath);
			oModel.setPancardNumber(pancardnumber);
			if (StringUtils.isNotEmpty(ticket))
			{
				response = mplPancardTicketCRMservice.createCrmTicket(ticket);
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
		final CRMpancardResponse crmpancardResponse = mplPancardTicketCRMservice.getCrmStatusForPancardDetails(oModel);
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




}

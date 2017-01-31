/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.services.pancard.impl;

import de.hybris.platform.core.model.PancardInformationModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

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
	public static final String PANCARD_PATH = "user.pancard.folder.location";

	public static final String PANCARD_IMAGE_EXTENSION = "user.pancard.folder.extension";

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
	public void setPancardFileAndOrderIdservice(final String orderreferancenumber, final String customername,
			final String pancardnumber, final MultipartFile file)
	{
		// YTODO Auto-generated method stub
		try
		{
			final String fileUploadDirectory = configurationService.getConfiguration().getString(PANCARD_PATH);
			final String fileExtension = configurationService.getConfiguration().getString(PANCARD_IMAGE_EXTENSION);

			final String storageDirectory = fileUploadDirectory;
			final String newFilenameBase = orderreferancenumber;

			/*
			 * final String fileExtension =
			 * file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			 */

			final String newFilename = newFilenameBase + fileExtension;

			final String newpath = storageDirectory + "/" + newFilename;
			final File newFile = new File(newpath);
			if (!newFile.exists())
			{
				newFile.mkdirs();
			}

			file.transferTo(newFile);

			//final String newpath = storageDirectory + "/" + newFilename;
			//System.out.println("*******************Pancard details save loaction::::" + newpath);


			//I have to create a data class here  and  I have to call the method of MplPancardTicketCRMserviceImpl by passing that data class

			/*
			 * first save the model then create the ticket
			 *
			 * final PancardInformationModel consumed = modelService.create(PancardInformationModel.class);
			 * consumed.setOrderId(orderreferancenumber); consumed.setPath(newpath); consumed.setTransactionId("");
			 * consumed.setTicket(""); consumed.setStatus(""); consumed.setName(customername);
			 * consumed.setPancardNumber(pancardnumber); modelService.save(consumed);
			 */

			//sending orderid and path for ticket creation
			/* final CRMpancardResponse crmpancardResponse = */



			//here we have to get the response and set the status and pan card number
			mplPancardTicketCRMservice.createticketPancardModeltoDTO(orderreferancenumber, newpath, customername, pancardnumber);


			//saving to database table PancardInformation

			final PancardInformationModel consumed = modelService.create(PancardInformationModel.class);
			consumed.setOrderId(orderreferancenumber);
			consumed.setPath(newpath);
			consumed.setTransactionId("");
			consumed.setTicket("");
			consumed.setStatus("");
			consumed.setName(customername);
			consumed.setPancardNumber(pancardnumber);
			modelService.save(consumed);

			/*
			 * after getting the status and ticket number from CRM
			 * 
			 * 
			 * consumed.setTicket(value); consumed.setStatus(value); modelService.save(consumed);
			 */

		}
		catch (final JAXBException ex)
		{
			ex.printStackTrace();
		}
		catch (final IOException e)
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
	public void refreshPancardDetailsService(final PancardInformationModel oModel, final MultipartFile file)
	{


		try
		{
			// YTODO Auto-generated method stub

			//for sending ticket while refreshing  orderid, newpath, customername, pancardnumber
			//at the time of updating we have to check whether we have to send the ticket again or not ....we have to add a checking here
			final CRMpancardResponse crmpancardResponse = mplPancardTicketCRMservice.ticketPancardModeltoDTO(oModel);

			oModel.setStatus(crmpancardResponse.getStatus());
			oModel.setTicket(crmpancardResponse.getTicketNo());

			//for updating  the model
			modelService.save(oModel);

			//for updating the image in the specific path
			final String newpath = oModel.getPath();
			final File newFile = new File(newpath);
			if (!newFile.exists())
			{
				newFile.mkdirs();
			}

			file.transferTo(newFile);
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




}

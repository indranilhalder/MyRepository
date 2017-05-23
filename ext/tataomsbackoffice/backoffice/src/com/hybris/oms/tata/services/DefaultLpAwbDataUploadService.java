/**
 *
 */
package com.hybris.oms.tata.services;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import com.csvreader.CsvReader;
import com.hybris.oms.api.orderlogistics.OrderLogisticsFacade;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEdit;
import com.hybris.oms.domain.lpawb.dto.OrderLineInfo;
import com.hybris.oms.tata.constants.TataomsbackofficeConstants;
import com.techouts.backoffice.exception.InvalidFileNameException;


/**
 * this class is Used For Lp Awb BuklUpload Purpose
 *
 * @author Techouts
 */
public class DefaultLpAwbDataUploadService implements LpAwbDataUploadService
{
	@Resource(name = "orderLogisticsRestClient")
	private OrderLogisticsFacade orderLogisticsUpdateFacade;

	private Media media;
	private static final String CSV = ".csv";
	private static final String DATE_FORMAT = "ddMMyyyyHHmm";
	private static final String[] PROPERTY_FILE_ERRORS =
	{ "Lp Awb Forward File Path", "Lp Awb Return File Path" };
	private static final Logger LOG = Logger.getLogger(DefaultLpAwbDataUploadService.class);
	@Resource(name = "filePathProviderService")
	private FilePathProviderService filePathProviderService;
	private final String header = "orderID,transactionId,lpname,awbnumber";

	@Override
	public void lpAwbBulkUploadCommon(final UploadEvent uploadEvent, final String transactionType, final String userId,
			final String roleId)
	{
		try
		{
			if (!filePathProviderService.propertyFilePathValidation(PROPERTY_FILE_ERRORS,
					TataomsbackofficeConstants.LPAWB_FORWARD_FILE_PATH, TataomsbackofficeConstants.LPAWB_RETURN_FILE_PATH))
			{
				return;
			}
			final String destinationFile;
			final String destFilePath;
			media = uploadEvent.getMedia();
			LOG.info("the format " + media.getFormat());
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
			final String TimeStamp = simpleDateFormat.format(new Date());
			if (media.getName().startsWith(TataomsbackofficeConstants.LPAWB_FORWARD_FiLE_PREFIX))
			{
				destinationFile = TataomsbackofficeConstants.LPAWB_FORWARD_FiLE_PREFIX.concat(TimeStamp).concat(CSV);
				destFilePath = TataomsbackofficeConstants.LPAWB_FORWARD_FILE_PATH;
			}
			else if (media.getName().startsWith(TataomsbackofficeConstants.LPAWB_RETURN_FiLE_PREFIX))
			{
				destinationFile = TataomsbackofficeConstants.LPAWB_RETURN_FiLE_PREFIX.concat(TimeStamp).concat(CSV);
				destFilePath = TataomsbackofficeConstants.LPAWB_RETURN_FILE_PATH;
			}
			else
			{
				throw new InvalidFileNameException("Invalid file prefix name");
			}
			if (media.isBinary())
			{
				if (media.getStreamData().read() == -1)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				final java.io.File destFile = new File(destFilePath.trim(), destinationFile);
				FileUtils.copyInputStreamToFile(media.getStreamData(), new File(destFilePath.trim(), destinationFile));
				sendBulkDatatoOms(destFile, transactionType, userId, roleId);

			} //end is Binary Check
			else
			{
				if (media.getStringData().length() == 0)
				{
					LOG.info("File is Empty ...");
					Messagebox.show("File is Empty ");
					return;
				}
				final java.io.File destFile = new File(destFilePath.trim(), destinationFile);
				FileUtils.writeStringToFile(destFile, media.getStringData());
				sendBulkDatatoOms(destFile, transactionType, userId, roleId);
			}
		}
		catch (final InvalidFileNameException e)
		{
			Messagebox.show(e.getMessage());
		}
		catch (final Exception message)
		{
			message.printStackTrace();
		}
	}

	/**
	 * @param filePathProviderService
	 *           the filePathProviderService to set
	 */
	public void setFilePathProviderService(final FilePathProviderService filePathProviderService)
	{
		this.filePathProviderService = filePathProviderService;
	}

	private void sendBulkDatatoOms(final File destFile, final String transactionType, final String userId, final String roleId)
	{
		final LPOverrideAWBEdit lpEdit = new LPOverrideAWBEdit();
		lpEdit.setTransactionType(transactionType);//set transaction Type
		lpEdit.setUserId(userId);
		lpEdit.setRoleId(roleId);
		if (destFile.getName().startsWith(TataomsbackofficeConstants.LPAWB_FORWARD_FiLE_PREFIX))
		{
			lpEdit.setIsReturn(false);
		}
		else
		{
			lpEdit.setIsReturn(true);
		}
		final ArrayList<OrderLineInfo> orderLineInfoList = new ArrayList<OrderLineInfo>();
		CsvReader records = null;
		FileReader fileReader = null;
		try
		{
			fileReader = new FileReader(destFile);
			records = new CsvReader(fileReader);
			if (records.readHeaders())
			{
				LOG.info("Record Object Headers " + records.getHeaders() + records.getHeaderCount() + records.getHeader(0));
				for (final String headerField : records.getHeaders())
				{
					if (!header.contains(headerField))
					{
						throw new InvalidFileNameException("Invalid File Headers Found");
					}
				}
				while (records.readRecord())
				{
					final OrderLineInfo info = new OrderLineInfo();
					info.setOrderId(records.get("orderID"));
					info.setTransactionId(records.get("transactionId"));
					info.setLogisticName(records.get("lpname"));
					info.setAwbNumber(records.get("awbnumber"));
					info.setLpOverride(true);
					info.setNextLP(false);
					orderLineInfoList.add(info);

				}
				lpEdit.setOrderLineInfo(orderLineInfoList);
				final boolean updateResult = orderLogisticsUpdateFacade.bulkUpload(lpEdit);
				if (updateResult)
				{
					Messagebox.show("Lp awb BulkUpload Sucess");
					return;
				}
				else
				{
					Messagebox.show("Lp awb BulkUpload Faild ,please find mail attachment");
				}
			}
			else
			{
				Messagebox.show("Bulk Upload Faild due to heders");
				return;
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			records.close();
		}
	}

	/**
	 * @param orderLogisticsUpdateFacade
	 *           the orderLogisticsUpdateFacade to set
	 */
	public void setOrderLogisticsUpdateFacade(final OrderLogisticsFacade orderLogisticsUpdateFacade)
	{
		this.orderLogisticsUpdateFacade = orderLogisticsUpdateFacade;
	}

}

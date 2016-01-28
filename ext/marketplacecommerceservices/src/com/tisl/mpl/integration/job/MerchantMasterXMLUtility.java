/**
 *
 */
package com.tisl.mpl.integration.job;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.MerchantMasterTableModel;
import com.tisl.mpl.pojo.MerchantMasterXMLData;
import com.tisl.mpl.pojo.MerchantTableXMLData;
import com.tisl.mpl.service.MerchantMasterWebServiceImpl;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 * @param: List of MerchantMasterTable Model
 * @return : xml string
 */
public class MerchantMasterXMLUtility
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SalesOrderXMLUtility.class.getName());
	@Autowired
	private MerchantMasterWebServiceImpl merchantMasterWebService;

	public String generateMasterData(final List<MerchantMasterTableModel> merchantData)
	{
		final MerchantMasterXMLData xmlData = new MerchantMasterXMLData();
		List<MerchantTableXMLData> bulkDataList = new ArrayList<MerchantTableXMLData>();
		String xmlString = MarketplacecommerceservicesConstants.EMPTYSPACE;

		try
		{
			if (null != merchantData && !merchantData.isEmpty())
			{
				bulkDataList = getMasterData(merchantData);
				if (null != bulkDataList && !bulkDataList.isEmpty())
				{
					xmlData.setMerchantMasterDataList(bulkDataList);
					final JAXBContext context = JAXBContext.newInstance(MerchantMasterXMLData.class);
					final Marshaller m = context.createMarshaller();
					m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					final StringWriter sw = new StringWriter();
					m.marshal(xmlData, sw);
					xmlString = sw.toString();
					LOG.debug(xmlString);
					merchantMasterWebService.merchantMaster(xmlString);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}

		return xmlString;
	}

	private List<MerchantTableXMLData> getMasterData(final List<MerchantMasterTableModel> merchantdata)
	{
		final List<MerchantTableXMLData> bulkMerchantDataList = new ArrayList<MerchantTableXMLData>();
		for (final MerchantMasterTableModel merobj : merchantdata)
		{
			final MerchantTableXMLData merchantMasterXMLData = new MerchantTableXMLData();

			if (null != merobj.getMerchantCode())
			{
				merchantMasterXMLData.setMerchantCode(merobj.getMerchantCode());
			}

			if (null != merobj.getMerchantDesc())
			{
				merchantMasterXMLData.setMerchantDesc(merobj.getMerchantDesc());
			}
			if ((null != merobj.getMerchantCode() && null != merobj.getMerchantDesc())
					&& (!merobj.getMerchantDesc().isEmpty() && !merobj.getMerchantCode().isEmpty()))
			{
				bulkMerchantDataList.add(merchantMasterXMLData);
			}

		}
		return bulkMerchantDataList;
	}

}

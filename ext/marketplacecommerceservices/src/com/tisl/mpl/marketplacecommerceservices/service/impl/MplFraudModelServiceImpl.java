/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.FraudStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fraud.model.FraudReportModel;
import de.hybris.platform.fraud.model.FraudSymptomScoringModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplFraudModelService;


/**
 * @author TCS
 *
 */
public class MplFraudModelServiceImpl implements MplFraudModelService
{

	@Autowired
	private ModelService modelService;

	/**
	 * This method updates the fraudModel against the order
	 *
	 * @param orderModel
	 * @param mplAudit
	 */
	@Override
	public void updateFraudModel(final OrderModel orderModel, final MplPaymentAuditModel mplAudit)
	{
		try
		{
			final FraudReportModel fraudModel = modelService.create(FraudReportModel.class);
			fraudModel.setOrder(orderModel);
			fraudModel.setCode(mplAudit.getAuditId());
			fraudModel.setTimestamp(new Date());
			final Collection<JuspayEBSResponseModel> riskList = mplAudit.getRisk();
			final List<JuspayEBSResponseModel> mplFraudList = new ArrayList<JuspayEBSResponseModel>();
			if (null != riskList)
			{
				mplFraudList.addAll(riskList);
			}
			if (null != mplFraudList.get(0))
			{
				if (StringUtils.isNotEmpty(mplFraudList.get(0).getEbs_bin_country()))
				{
					fraudModel.setEbsBinCountry(mplFraudList.get(0).getEbs_bin_country());
				}
				if (null != mplFraudList.get(0).getEbsRiskLevel())
				{
					fraudModel.setEbsRiskLevel(mplFraudList.get(0).getEbsRiskLevel().toString());
				}
				if (null != mplFraudList.get(0).getEbsRiskStatus())
				{
					setFraudStatus(fraudModel, mplFraudList.get(0).getEbsRiskStatus().toString());
				}

				final FraudSymptomScoringModel fraudSymptomScoringModel = new FraudSymptomScoringModel();
				final List<FraudSymptomScoringModel> fraudSymptomList = new ArrayList<FraudSymptomScoringModel>();
				if (null != mplFraudList.get(0).getEbsRiskLevel()
						&& StringUtils.isNotEmpty(mplFraudList.get(0).getEbsRiskPercentage()))
				{
					fraudSymptomScoringModel.setName(
							mplFraudList.get(0).getEbsRiskLevel().toString() + "_" + mplFraudList.get(0).getEbsRiskPercentage());

					fraudSymptomScoringModel.setScore(new Double(mplFraudList.get(0).getEbsRiskPercentage()).doubleValue());

				}
				else
				{
					fraudSymptomScoringModel.setName("TestName");
					fraudSymptomScoringModel.setScore(0);
				}
				fraudSymptomScoringModel.setFraudReport(fraudModel);
				modelService.save(fraudSymptomScoringModel);
				fraudSymptomList.add(fraudSymptomScoringModel);

				fraudModel.setFraudSymptomScorings(fraudSymptomList);
				modelService.save(fraudModel);

				modelService.save(orderModel);
			}

		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, "E0001");
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, "E0007");
		}

	}


	/**
	 * This method sets the fraud status in the fraud model
	 *
	 * @param fraudModel
	 * @param getEbsRiskStatus
	 */
	private void setFraudStatus(final FraudReportModel fraudModel, final String getEbsRiskStatus)
	{
		if (getEbsRiskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_APPROVED))
		{
			fraudModel.setStatus(FraudStatus.COMPLETED);
		}
		else if (getEbsRiskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_REVIEW))
		{
			fraudModel.setStatus(FraudStatus.PENDING);
		}
		else if (getEbsRiskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_REJECTED))
		{
			fraudModel.setStatus(FraudStatus.FAILED);
		}
		else if (getEbsRiskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_IGNORED))
		{
			fraudModel.setStatus(FraudStatus.ERROR);
		}
		else if (getEbsRiskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_FRAUD))
		{
			fraudModel.setStatus(FraudStatus.FRAUD);
		}
		else if (getEbsRiskStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_CHARGEBACK))
		{
			fraudModel.setStatus(FraudStatus.CHECK);
		}
	}
}

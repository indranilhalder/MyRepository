/**
 *
 */
package com.tisl.mpl.converter.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.tisl.mpl.core.model.JuspayCardResponseModel;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.JuspayPGResponseModel;
import com.tisl.mpl.core.model.JuspayRefundResponseModel;
import com.tisl.mpl.juspay.Refund;
import com.tisl.mpl.juspay.response.CardResponse;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.juspay.response.PaymentGatewayResponse;
import com.tisl.mpl.juspay.response.RiskResponse;


/**
 * @author TCS
 *
 */
public class JuspayOrderResponsePopulator implements Populator<JuspayOrderStatusModel, GetOrderStatusResponse>
{

	/**
	 * This method populates JuspayOrderStatusModel to GetOrderStatusResponse
	 */
	@Override
	public void populate(final JuspayOrderStatusModel source, final GetOrderStatusResponse target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setMerchantId(source.getMerchantId());
		target.setOrderId(source.getOrderId());
		target.setAmount(source.getAmount());
		target.setStatus(source.getStatus());
		target.setStatusId(source.getStatusId());
		target.setTxnId(source.getTxnId());
		target.setGatewayId(source.getGatewayId());
		target.setBankErrorCode(source.getBankErroCode());
		target.setBankErrorMessage(source.getBankErroMessage());

		final List<JuspayRefundResponseModel> refundModelList = source.getRefunds();
		if (CollectionUtils.isNotEmpty(refundModelList))
		{
			final List<Refund> refundList = new ArrayList<Refund>();
			for (final JuspayRefundResponseModel refMod : refundModelList)
			{
				final Refund refund = new Refund();
				refund.setAmount(refMod.getAmount());
				refund.setCreated(refMod.getCreated());
				refund.setId(refMod.getId());
				refund.setReference(refMod.getReference());
				refund.setStatus(refMod.getStatus());
				refund.setUniqueRequestId(refMod.getUniqueRequestId());

				refundList.add(refund);
			}

			target.setRefunds(refundList);
		}

		target.setRefunded(source.getRefunded());
		target.setAmountRefunded(source.getAmountRefunded());
		target.setUdf1(source.getUdf1());
		target.setUdf2(source.getUdf2());
		target.setUdf3(source.getUdf3());
		target.setUdf4(source.getUdf4());
		target.setUdf5(source.getUdf5());
		target.setUdf6(source.getUdf6());
		target.setUdf7(source.getUdf7());
		target.setUdf8(source.getUdf8());
		target.setUdf9(source.getUdf9());
		target.setUdf10(source.getUdf10());

		target.setPaymentMethodType(source.getPaymentMethodType());
		target.setPaymentMethod(source.getPaymentMethod());

		target.setBankEmi(source.getEmiBank());
		target.setBankTenure(source.getEmiTenure());

		final JuspayCardResponseModel cardResponseModel = source.getCardResponse();

		if (null != cardResponseModel)
		{
			final CardResponse cardResponse = new CardResponse();
			cardResponse.setCardBrand(cardResponseModel.getCardBrand());
			cardResponse.setCardIssuer(cardResponseModel.getCardIssuer());
			cardResponse.setCardType(cardResponseModel.getCardType());
			cardResponse.setNameOnCard(cardResponseModel.getNameOnCard());
			cardResponse.setExpiryMonth(cardResponseModel.getExpMonth());
			cardResponse.setExpiryYear(cardResponseModel.getExpYear());
			cardResponse.setCardISIN(cardResponseModel.getCardIsin());

			target.setCardResponse(cardResponse);
		}

		final JuspayPGResponseModel pgResponseModel = source.getPgResponse();

		if (null != pgResponseModel)
		{
			final PaymentGatewayResponse paymentGatewayResponse = new PaymentGatewayResponse();
			paymentGatewayResponse.setCreated(pgResponseModel.getCreated());
			paymentGatewayResponse.setExternalGatewayTxnId(pgResponseModel.getEpgTxnId());
			paymentGatewayResponse.setRootReferenceNumber(pgResponseModel.getRootReferenceNumber());
			paymentGatewayResponse.setAuthIdCode(pgResponseModel.getAuthIdCode());
			paymentGatewayResponse.setTxnId(pgResponseModel.getTxnId());
			paymentGatewayResponse.setResponseCode(pgResponseModel.getResponseCode());
			paymentGatewayResponse.setResponseMessage(pgResponseModel.getResponseMessage());

			target.setPaymentGatewayResponse(paymentGatewayResponse);
		}

		final JuspayEBSResponseModel riskResponseModel = source.getEbsResponse();

		if (null != riskResponseModel)
		{
			final RiskResponse riskResp = new RiskResponse();
			if (null != riskResponseModel.getEbsRiskLevel())
			{
				riskResp.setEbsRiskLevel(riskResponseModel.getEbsRiskLevel().toString());
			}
			if (StringUtils.isNotEmpty(riskResponseModel.getEbsRiskPercentage()))
			{
				if (riskResponseModel.getEbsRiskPercentage().indexOf('.') != -1)
				{
					riskResp.setEbsRiskPercentage(Long.valueOf(Integer.parseInt(riskResponseModel.getEbsRiskPercentage().substring(0,
							riskResponseModel.getEbsRiskPercentage().indexOf('.')))));
				}
				else
				{
					riskResp.setEbsRiskPercentage(Long.valueOf(Integer.parseInt(riskResponseModel.getEbsRiskPercentage())));
				}
			}
			riskResp.setEbsBinCountry(riskResponseModel.getEbs_bin_country());

			target.setRiskResponse(riskResp);
		}

	}
}

/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.facades.data.CustomerDataForOrderXML;
import com.tisl.mpl.facades.data.OrderDataTransactionForXML;
import com.tisl.mpl.facades.data.ProductDataForOrderXML;


/**
 * @author TCS
 *
 */
public class MplChildOrderPopulator implements Populator<OrderModel, com.tisl.mpl.facades.data.OrderDataTransactionForXML>
{

	private Converter<OrderModel, CustomerDataForOrderXML> mplCustomerDataConverter;
	private Converter<OrderModel, ProductDataForOrderXML> mplProductDataConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final OrderModel source, final OrderDataTransactionForXML target) throws ConversionException
	{
		final List<CustomerDataForOrderXML> customerAddress = new ArrayList<CustomerDataForOrderXML>();
		final List<ProductDataForOrderXML> productData = new ArrayList<ProductDataForOrderXML>();
		target.setTransactionId(source.getCode());
		if (source.getEntries() != null)
		{
			final AbstractOrderEntryModel entryModel = source.getEntries().get(0);
			if (entryModel != null)
			{
				target.setPrice(entryModel.getBasePrice().longValue());
				target.setUSSID(entryModel.getSelectedUSSID());
				if (entryModel.getProduct() != null)
				{
					target.setListingID(entryModel.getProduct().getCode());

					final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) entryModel.getProduct()
							.getRichAttribute();
					if (richAttributeModel != null)
					{
						if (richAttributeModel.get(0).getDeliveryFulfillModes() != null)
						{
							target.setFulfillmentType(richAttributeModel.get(0).getDeliveryFulfillModes().getCode());
						}

						if (richAttributeModel.get(0).getShippingModes() != null)
						{
							target.setTransportMode(richAttributeModel.get(0).getShippingModes().getCode());
						}


					}




					target.setIsCOD(true); //stubbed value
					target.setIsaGift(Boolean.FALSE); //stubbed value
					target.setProductSize("42"); //stubbed value
					target.setApportionedPrice(entryModel.getBasePrice().longValue() / 10); //TBD once Apportioned price in the model is available
					target.setPromotionCode("XYZ"); //stubbed value added
					target.setIsReturnable(true); //stubbed value added
					//target.setShippingCharge(source.getDeliveryCost());
					target.setShippingCharge(source.getDeliveryCost().longValue());
					target.setSellerID("website");
					target.setApportionedCODPrice(entryModel.getBasePrice().longValue() / 10); //Stubed value

					productData.add(getMplProductDataConverter().convert(source));
					target.setProductDetails(productData);
				}

			}

		}
		customerAddress.add(getMplCustomerDataConverter().convert(source));

		target.setCustomerAddress(customerAddress);


	}

	/**
	 * @return the mplCustomerDataConverter
	 */
	public Converter<OrderModel, CustomerDataForOrderXML> getMplCustomerDataConverter()
	{
		return mplCustomerDataConverter;
	}

	/**
	 * @param mplCustomerDataConverter
	 *           the mplCustomerDataConverter to set
	 */
	@Required
	public void setMplCustomerDataConverter(final Converter<OrderModel, CustomerDataForOrderXML> mplCustomerDataConverter)
	{
		this.mplCustomerDataConverter = mplCustomerDataConverter;
	}

	/**
	 * @return the mplProductDataConverter
	 */
	public Converter<OrderModel, ProductDataForOrderXML> getMplProductDataConverter()
	{
		return mplProductDataConverter;
	}

	/**
	 * @param mplProductDataConverter
	 *           the mplProductDataConverter to set
	 */
	public void setMplProductDataConverter(final Converter<OrderModel, ProductDataForOrderXML> mplProductDataConverter)
	{
		this.mplProductDataConverter = mplProductDataConverter;
	}
}

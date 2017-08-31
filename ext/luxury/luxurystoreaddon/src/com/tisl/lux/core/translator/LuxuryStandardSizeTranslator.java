/**
 *
 */
package com.tisl.lux.core.translator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tisl.lux.model.StandardSizeColorModel;
import com.tisl.mpl.core.jalo.PcmProductVariant;
import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author Madhavan
 *
 */
public class LuxuryStandardSizeTranslator extends AbstractSpecialValueTranslator
{

	private static final Logger LOG = Logger.getLogger(LuxuryStandardSizeTranslator.class);

	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator#init(de.hybris.platform.impex.jalo.header
	 * .SpecialColumnDescriptor)
	 */
	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean("flexibleSearchService");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator#performImport(java.lang.String,
	 * de.hybris.platform.jalo.Item)
	 */
	@Override
	public void performImport(final String cellValue, final Item processedItem) throws ImpExException
	{
		if (Config.getBoolean("luxury.stdsize.translator.enabled", false))
		{
			try
			{
				if (processedItem.getAttribute("productSource") instanceof PcmProductVariant)
				{

					final String brandCode = (String) processedItem.getAttribute("brandCode");

					final PcmProductVariant productSource = (PcmProductVariant) processedItem.getAttribute("productSource");
					final PcmProductVariantModel productModel = modelService.get(productSource.getPK());

					ServicesUtil.validateParameterNotNull(brandCode, "BrandCode cannot be null");
					ServicesUtil.validateParameterNotNull(productModel.getSize(), "BrandSize cannot be null");

					final List<String> categoryCodes = new ArrayList<String>();
					final Collection<CategoryModel> superCategories = productModel.getSupercategories();
					for (final CategoryModel categoryModel : superCategories)
					{
						categoryCodes.add("'" + categoryModel.getCode() + "'");
					}
					final String categories = String.join(",", categoryCodes);
					ServicesUtil.validateParameterNotNull(categories, "Category cannot be null");


					final Map params = new HashMap();
					final String queryString = "select {pk} from {StandardSizeColor as ssc}"
							+ " where {ssc:brand} = ?brand and {ssc:brandSize} = ?brandSize" + " and {ssc:category} in (" + categories
							+ ")";

					params.put("brand", brandCode);
					params.put("brandSize", productModel.getSize());

					LOG.info("brandCode - " + brandCode + "branSize - " + productModel.getSize());
					LOG.info("product - " + productSource.getPK() + "categories - " + categories);
					LOG.info("Query String - " + queryString);

					final SearchResult queryResult = flexibleSearchService.search(queryString, params);

					ServicesUtil.validateIfSingleResult(queryResult.getResult(), "No matching records found",
							"More than one record found");

					final StandardSizeColorModel sModel = (StandardSizeColorModel) queryResult.getResult().get(0);
					final String standardSize = sModel.getStandardSize();
					productModel.setBrandSize(productModel.getSize());
					productModel.setSize(standardSize);
					modelService.save(productModel);
				}
			}
			catch (final JaloInvalidParameterException e)
			{
				LOG.error(e);
			}
			catch (final JaloSecurityException e)
			{
				LOG.error(e);
			}
		}
	}
}

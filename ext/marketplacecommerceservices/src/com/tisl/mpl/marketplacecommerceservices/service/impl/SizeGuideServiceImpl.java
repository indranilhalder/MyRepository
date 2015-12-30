/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.SizeGuideDao;
import com.tisl.mpl.marketplacecommerceservices.service.SizeGuideService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class SizeGuideServiceImpl implements SizeGuideService
{
	@Resource
	private ProductService productService;

	@Resource
	private ClassificationService classificationService;

	@Resource(name = "sizeGuideDao")
	private SizeGuideDao sizeGuideDao;

	private static final Logger LOG = Logger.getLogger(SizeGuideServiceImpl.class);
	@Autowired
	private ConfigurationService configurationService;

	/**
	 * @description It will take the customer id from the customer model
	 * @param productCode
	 * @return List<SizeGuideModel>
	 *
	 */
	@Override
	public List<SizeGuideModel> getProductSizeGuideList(final String productCode) throws CMSItemNotFoundException
	{
		List<SizeGuideModel> sizeModels = null;
		try
		{
			final ProductModel productModel = productService.getProductForCode(productCode);
			final String sizeGuideCode = sizeChartCode(productModel);
			LOG.info("**********product code: " + productModel.getCode() + " sizeGuideCode:" + sizeGuideCode);

			sizeModels = sizeGuideDao.getsizeGuideByCode(sizeGuideCode);
			//sizeModels = new ArrayList(productModel.getSizeGuide());
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return sizeModels;
	}

	private String sizeChartCode(final ProductModel product)
	{
		String sizeGuideCode = null;
		//get size chart feature
		final FeatureList featureList = getClassificationService().getFeatures(product);
		for (final Feature feature : featureList)
		{
			final String featureName = feature.getName().replaceAll("\\s+", "");

			final String sizeChart = configurationService.getConfiguration().getString("product.sizechart.value")
					.replaceAll("\\s+", "");
			if (featureName.equalsIgnoreCase(sizeChart))
			{
				if (null != feature.getValue())
				{
					final FeatureValue sizeGuidefeatureVal = feature.getValue();
					sizeGuideCode = String.valueOf(sizeGuidefeatureVal.getValue());
					break;
				}
			}
		}

		return sizeGuideCode;
	}





	/**
	 * @return the classificationService
	 */
	public ClassificationService getClassificationService()
	{
		return classificationService;
	}





	/**
	 * @param classificationService
	 *           the classificationService to set
	 */
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

}

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import com.tisl.mpl.core.model.LuxProductCarouselComponentModel;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.product.PriceDataFactory;

import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Controller for CMS ProductReferencesComponent.
 */
@Controller("LuxProductCarouselComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.LuxProductCarouselComponent)
public class LuxProductCarouselComponentController extends AbstractCMSComponentController<LuxProductCarouselComponentModel> {
    protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

    @Resource(name = "accProductFacade")
    private ProductFacade productFacade;

    @Resource(name = "productSearchFacade")
    private ProductSearchFacade<ProductData> productSearchFacade;

    @Resource(name = "buyBoxFacade")
    private BuyBoxFacade buyBoxFacade;

    @Resource(name = "priceDataFactory")
    private PriceDataFactory priceDataFactory;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    public ProductFacade getProductFacade() {
        return productFacade;
    }

    public PriceDataFactory getPriceDataFactory() {
        return priceDataFactory;
    }

    protected CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }

    /**
     * @param buyBoxFacade the buyBoxFacade to set
     */
    public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade) {
        this.buyBoxFacade = buyBoxFacade;
    }


    @Override
    protected void fillModel(final HttpServletRequest request, final Model model, final LuxProductCarouselComponentModel component) {
        final List<ProductData> products = new ArrayList<>();
        products.addAll(collectLinkedProducts(component));
        products.addAll(collectSearchProducts(component));
        setPriceForProduct(products);
        model.addAttribute("title", component.getTitle());
        model.addAttribute("productData", products);
    }

    private void setPriceForProduct(List<ProductData> products) {
        for (ProductData product : products) {
            final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(product.getCode());
            if (buyboxdata != null) {
                final PriceData priceValue = buyboxdata.getPrice();
                final PriceData mrpPriceValue = buyboxdata.getMrpPriceValue();
                //final PriceData mop = buyboxdata.getPrice();
                product.setPrice(priceValue);
                product.setProductMRP(mrpPriceValue);

                //product.setSavingsOnProduct(specialPrice);


                if (null != mrpPriceValue && null != priceValue)
                {
                    final double savingsAmt = mrpPriceValue.getDoubleValue() - priceValue.getDoubleValue();
                    final double calculatedPerSavings = (savingsAmt / mrpPriceValue.getDoubleValue()) * 100;
                    //final double roundedOffValuebefore = Math.round(calculatedPerSavings * 100.0) / 100.0;
                    final double floorValue = Math.floor((calculatedPerSavings * 100.0) / 100.0);
                    final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf((int) floorValue),
                            getCommonI18NService().getCurrentCurrency());

                    product.setSavingsOnProduct(priceData);

                }
            }
        }
    }


    protected List<ProductData> collectLinkedProducts(final LuxProductCarouselComponentModel component) {
        final List<ProductData> products = new ArrayList<>();

        for (final ProductModel productModel : component.getProducts()) {
            products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
        }

        for (final CategoryModel categoryModel : component.getCategories()) {
            for (final ProductModel productModel : categoryModel.getProducts()) {
                products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
            }
        }

        return products;
    }

    protected List<ProductData> collectSearchProducts(final LuxProductCarouselComponentModel component) {
        final SearchQueryData searchQueryData = new SearchQueryData();
        searchQueryData.setValue(component.getSearchQuery());
        final String categoryCode = component.getCategoryCode();

        if (searchQueryData.getValue() != null && categoryCode != null) {
            final SearchStateData searchState = new SearchStateData();
            searchState.setQuery(searchQueryData);

            final PageableData pageableData = new PageableData();
            pageableData.setPageSize(100); // Limit to 100 matching results

            return productSearchFacade.categorySearch(categoryCode, searchState, pageableData).getResults();
        }

        return Collections.emptyList();
    }
}

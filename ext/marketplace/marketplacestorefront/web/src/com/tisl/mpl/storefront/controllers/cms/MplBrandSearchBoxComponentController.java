/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
@Controller("MplBrandSearchBoxComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplBrandSearchBoxComponent)
public class MplBrandSearchBoxComponentController extends MplEnhancedSearchBoxComponentController
{
	//This component is replicate the MplEnhancedSearchBoxComponent for microsite template


}

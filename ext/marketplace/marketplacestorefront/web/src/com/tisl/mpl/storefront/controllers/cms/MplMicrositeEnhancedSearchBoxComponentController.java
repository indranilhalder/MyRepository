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
@Controller("MplMicrositeEnhancedSearchBoxComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplMicrositeEnhancedSearchBoxComponent)
public class MplMicrositeEnhancedSearchBoxComponentController extends MplEnhancedSearchBoxComponentController
{
	//This component is replicate the MplEnhancedSearchBoxComponent for microsite template


}

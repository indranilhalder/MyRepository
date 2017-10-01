<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>
<%@ attribute name="showOnlySiteLogo" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/nav"%>
<!-- R2.3 for track order Start -->
<%@ taglib prefix="trackOrder" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/common/header"%> 


						


<!-- R2.3 for track order END -->

<%-- <cms:pageSlot position="TopHeaderSlot" var="component" element="div"
	class="container">
	<cms:component component="${component}" />
</cms:pageSlot> --%>
<c:if test="${param.source ne null and param.source eq 'App' }">
	<c:set var="showOnlySiteLogo" value="true"></c:set>
	<c:set var="hideSecureTransaction" value="true"></c:set>
	<c:set var="hideLogo" value="true"></c:set>
</c:if>
<!-- TPR-844 -->
			<div class="wishAddLoginPlp">
			<span><spring:theme code="product.wishListNonLoggedIn"></spring:theme></span>
			</div>
			<div class="wishAddSucessPlp">
			<span><spring:theme code="mpl.pdp.wishlistSuccess"></spring:theme></span>
			</div>
			<div class="wishAlreadyAddedPlp">
			<span><spring:theme code="mpl.pdp.wishlistAlreadyAdded"></spring:theme></span>
			</div>
			<!-- Changes for INC144313867 -->
			<div class="wishRemoveSucessPlp">
			<span><spring:theme code="mpl.pdp.wishlistRemoveSuccess"></spring:theme></span>
			</div>
	<!-- TPR-844 -->
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticHost"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('luxury.resource.host')" var="luxuryHost"/>
<header>	
	<!-- For Infinite Analytics Start -->
	<input type="hidden" id="ia_site_id" name="ia_site_id" value="${cmsSite.uid}"> 
	<input type="hidden" id="ia_site_page_id" name="ia_site_page_id" value="${cmsPage.uid}"> 
	<!-- changes for url structure change for pdp-->
	<input type="hidden" id="ia_category_code" name="ia_category_code" value="${fn:toUpperCase(categoryCode)}">
	<input type="hidden" id="ia_product_code" name="ia_product_code" value="${fn:toUpperCase(productCode)}">
	<!-- changes end -->
	<input type="hidden" id="ia_product_rootCategory_type" name="ia_product_rootCategory_type" value="${product.rootCategory}">
	<input type="hidden" id="mSellerID" name="mSellerID" value="${mSellerID}">
	<input type="hidden" id="rootEPForHttp" name="rootEPForHttp" value="${rootEPForHttp}">
	<input type="hidden" id="rootEPForHttps" name="rootEPForHttps" value="${rootEPForHttps}">
	<input type="hidden" id="ecompanyForIA" name="ecompanyForIA" value="${ecompanyForIA}">
	<input type="hidden" id="DamMediaHost" name="DamMediaHost" value="${DamMediaHost}">
	<input type="hidden" id="mplStaticResourceHost" name="mplStaticResourceHost" value="${mplStaticResourceHost}">
	<input type="hidden" id="previewVersion" name="previewVersion" value="${cmsPageRequestContextData.preview}">
	<input type="hidden" id="pageTemplateId" name="pageTemplateId"  value="${cmsPage.masterTemplate.uid}">
	<input type="hidden" id="userLoginType" name="userLoginType" value="${userLoginType}">		<!-- TPR-668 -->
	<!-- For Infinite Analytics End -->
	<input type="hidden" id="pageName" name="pageName" value="${cmsPage.name}">
	<!-- Static resource host -->
	<input type="hidden" id="staticHost" name="staticHost" value="//${staticHost}">
	<!-- End -->
	<div class="row header-row"></div>
	<c:choose>
		<c:when test="${empty showOnlySiteLogo }">
			<%--<div class="banner">
			 <div class="content" id="latestOffersContent"></div>
				<span class="toggle desktop helpmeshopbanner latestOffersBanner"><p>${headerConciergeTitle}</p></span> 
				
				<!-- <cms:pageSlot position="HeaderLinks" var="link"> -->
					<!--	<cms:component component="${link}" element="" />-->
				<!--	</cms:pageSlot> -->
			</div> --%>
		</c:when>
		<c:otherwise>
			<c:if test="${empty hideSecureTransaction}">
						<!-- <span class="secure secureTransaction secMobile"></span> -->
			</c:if>
		</c:otherwise>
	</c:choose>


	<div class="content">
	<!-- Luxury tab	 starts-->
	<c:if test="${!hideSecureTransaction}">
						<div id="flip-tabs" >				
							<ul id="flip-navigation" >  
					            <li class="selected"><a href="/" id="tab-1" >MARKETPLACE</a></li>
					            <li><a href="${luxuryHost}" id="tab-2" target="_blank">LUXURY</a></li>  
					        </ul> 
					    </div>
					    </c:if>
	 <!-- Luxury tab	 ends-->
		<div class="top">
			<c:if test="${empty showOnlySiteLogo }">
				<div class="toggle">
					<span></span> <span></span> <span></span>
				</div>
			</c:if>
			<div class="container">
				<c:if test="${empty showOnlySiteLogo }">
					<div class="left">
						<!-- Luxury tab	 starts-->
						<%-- <div id="flip-tabs" >				
							<ul id="flip-navigation" >  
					            <li class="selected"><a href="/" id="tab-1" >MARKETPLACE</a></li>
					            <li><a href="${luxuryHost}" id="tab-2" >LUXURY</a></li>  
					        </ul> 
					    </div> --%>
				        <!-- Luxury tab	 ends-->
						<ul>
							<%-- <li><a href="<c:url value="/helpservices"/>"><spring:theme
										code="header.help&Services" /></a></li> --%>
						</ul>
						
				   </div>      
					
				</c:if>

				<div class="marketplace compact">
					<c:if test="${empty hideLogo}">
						<cms:pageSlot position="TopHeaderSlot" var="logo" limit="1">
							<cms:component component="${logo}" />
						</cms:pageSlot>
					
					</c:if>
				</div>
				<c:set var="userLoggedIn" value="${true}"  />
				<div class="right">

					<ul>


						<!--Using this tag for 'My Bag' Link in header navigation pane and it will navigate to cart Page  -->

						<c:if test="${empty showOnlySiteLogo }">
							<c:if test="${empty hideHeaderLinks}">
								<cms:pageSlot position="MiniCart" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot>
								<!-- R2.3 for track order Start -->
								 <sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
						     	<c:set var="userLoggedIn" value="${false}"  />
									<li class="track_order_header"><a href="#" onclick="openTrackOrder()">
										<spring:theme code="trackorder.header.text" text="Track Order"/></a>
										
									</li>
								</sec:authorize> 
								<c:if test="${userLoggedIn eq 'true'}">
						     <c:if test="${empty showOnlySiteLogo }">
									<li class="track"><a href="<c:url value="/my-account/orders"/>"><spring:theme
												code="header.trackorder" /></a></li>
								</c:if> 
					         </c:if>
								<!-- R2.3 for track order END -->
								<li class="store-locator-header"><a href="${request.contextPath}/store-finder">Our Stores</a></li>
								<li class="download-app"><a href="${request.contextPath}/apps">Download App</a></li>
								
								
							</c:if>
						</c:if>
						<!--Using this tag for Track Order Link in header navigation pane and it will navigate to 'My Order page'  -->
					<!-- R2.3 for track order Start -->
					
				  <!-- R2.3 for track order END -->

					</ul>



				</div>
			</div>
			<div class="compact-toggle"></div>
		</div>

	<div class="overlay"></div>
		<!-- Using this tag for placing the site logo in header navigation pane -->

		<div class="bottom">
		<div class="bottom-header-wrapper">
			<c:choose>
				<c:when test="${empty showOnlySiteLogo }">
					<div class="marketplace" data-logo="marketplace">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}"/>
						</cms:pageSlot>
						<!-- <div class="mobile-bag bag">
						TISPRD-32-fix
							<a href="/store/mpl/en/cart">(<span class="responsive-bag-count"></span>)</a>
						</div> -->
					</div>
					<div class="marketplace linear-logo">
						<cms:pageSlot position="TopHeaderSlot" var="logo" limit="1">
							<cms:component component="${logo}"/>
						</cms:pageSlot>
						<div class="mobile-bag bag">
						<!-- TISPRD-32-fix -->
							<!-- <a href="/store/mpl/en/cart">(<span class="responsive-bag-count"></span>)</a> -->
							<a href="/cart"><span class="responsive-bag-count"></span></a>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="marketplace-checkout">
						<c:if test="${empty hideLogo}">
							<%-- <cms:pageSlot position="TopHeaderSlot" var="logo" limit="1">
								<cms:component component="${logo}" />
							</cms:pageSlot> --%>
							<div class="logo">
							<div class="desktop-logo" data-logo="marketplace">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}"/>
						</cms:pageSlot>
					</div>
					<div class="tab-logo">
						<cms:pageSlot position="TopHeaderSlot" var="logo" limit="1">
							<cms:component component="${logo}"/>
						</cms:pageSlot>
					</div>
					</div>
					
					<span>CHECKOUT</span>
					
					
					<button id="deliveryAddressSubmitUp" type="submit" class="button checkout-next" style="display:none;">Proceed to Payment</button>
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>



			<nav>
				<ul>
					<c:if test="${empty showOnlySiteLogo }">

<!-- changes for performance fixof TPR-561 -->
							<li class="ShopByDepartmentone">
					
						<div class="toggle shop_dept"><span><spring:theme code="navigation.department.shopBy"/></span>
							<span><spring:theme code="navigation.department.shopByDepartment"/></span></div> <!-- TPR-561 -->
								<span id="mobile-menu-toggle" class="mainli"></span>
									<ul class="shopByDepartment_ajax">
																		
									</ul>
									 </li>

						<cms:pageSlot position="NavigationBar" var="component">
							<c:if test ="${component.uid eq 'ShopByBrandComponent' }">
							<c:set var="componentName" value="${component.name}"/>
							<li class="ShopByBrand ${fn:replace(componentName,' ', '')}"><cms:component component="${component}" /></li>
							</c:if>
						</cms:pageSlot>
						<!-- changes for performance fixof TPR-561 -->

					</c:if>


				</ul>
			</nav> 
			
			<div class="search">
				<c:if test="${empty showOnlySiteLogo }">
					<!-- <button class="btn btn-default js-toggle-sm-navigation header-burgerMenu"
													type="button">
													<span class="glyphicon glyphicon-align-justify"></span>
												</button> -->

					<cms:pageSlot position="SearchBox" var="component">
						<cms:component component="${component}" />
					</cms:pageSlot>
				</c:if>
			</div>


			<!--   changes for Sticky Header in MyBag -->
			<div class="bag">
				<c:if test="${empty showOnlySiteLogo }">
					<%-- <a href="/store/mpl/en/cart" class="mini-cart-link myBag-sticky"
						data-mini-cart-url="/store/mpl/en/cart/rollover/MiniCart"
						data-mini-cart-refresh-url="/store/mpl/en/cart/miniCart/SUBTOTAL"
						data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
						style="position: static;"><spring:theme code="minicart.mybag" />&nbsp;(<span
						class="js-mini-cart-count-hover"></span>) </a> --%>
					<a href="/cart" class="mini-cart-link myBag-sticky"
						data-mini-cart-url="/cart/rollover/MiniCart"
						data-mini-cart-refresh-url="/cart/miniCart/SUBTOTAL"
						data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
						style="position: static;"><spring:theme code="minicart.mybag" />&nbsp;(<span
						class="js-mini-cart-count-hover"></span>) </a>
				</c:if>
			</div>
			</div>
		</div>
		<div class="compact-toggle mobile"></div>
	</div>
<c:if test="${param.blpLogo ne null}">
<div class="blp-serp-banner" style="background-color:#000;height:80px;">
<img class="image" alt="" src="${param.blpLogo}">
</div>
</c:if>
<!-- R2.3 for track order Start -->
<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
		<trackOrder:trackOrder />
	</sec:authorize> 
	<!-- R2.3 for track order END -->
	<a id="skiptonavigation"></a>
	<nav:topNavigation />
</header>

<c:if test="${empty showOnlySiteLogo }">
	<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"
		class="container">
		<cms:component component="${component}" />
	</cms:pageSlot>
</c:if>
<!-- Survey -->
<div class="feedback-form modal fade" id="feedBackFormModal">
	<div class="content" style="overflow: hidden;">
		<button class="close" data-dismiss="modal"></button>
		<div class="feedback-container">
		</div>
	</div>
	<div class="overlay" data-dismiss="modal"></div>
</div>
<style>
#feedBackFormModal.modal .content > .close:before {
	color: #fff !important;
}
#feedBackFormModal.modal .content > .close {
	right: 20px !important;
}

/*--------------Added for luxury site starts-----------*/
/* #flip-tabs{  
    width:300px;  
    margin:20px auto; position:relative;  
}  
#flip-navigation{  
    margin:0 0 10px; padding:0;   
    list-style:none;  
}  
#flip-navigation li{   
    display:inline;   
}  
#flip-navigation li a{  
    text-decoration:none; padding:10px;   
    margin-right:0px;  
    background:#f9f9f9;  
    color:#333; outline:none;  
    font-family:Arial; font-size:12px; text-transform:uppercase;  
}  
#flip-navigation li a:hover{  
    background:#999;   
    color:#f0f0f0;  
}  
#flip-navigation li.selected a{  
    background:#999;  
    color:#f0f0f0;  
}  
/* #flip-container{    
    width:300px;  
    font-family:Arial; font-size:13px;  
}  
#flip-container div{   
    background:#fff;  
}  */  */
/*--------------Added for luxury site ends-----------*/
</style>
<script>
var pathname = window.location.pathname;
if(pathname =='/checkout/multi/delivery-method/select'){
	$('#deliveryAddressSubmitUp').show();
	
	
}
</script>
<!--  Commented for TISPRD-1440  -->
<!-- <script>
/*$(document).ready(function(){
	var href = $(".marketplace,.linear-logo").find("a").attr("href");
	var p = href.split("?");
	$(".marketplace").find("a").attr("href",p[0]);
});*/

</script>  -->
<div class="wishAddSucessQV">
	<span><spring:theme code="mpl.pdp.wishlistSuccess"></spring:theme></span>
</div>
<div class="wishAddLoginQV">
	<span><spring:theme code="product.wishListNonLoggedIn"></spring:theme></span>
</div>
<div class="wishAlreadyAddedQV">
	<span><spring:theme code="mpl.pdp.wishlistAlreadyAdded"></spring:theme></span>
</div>

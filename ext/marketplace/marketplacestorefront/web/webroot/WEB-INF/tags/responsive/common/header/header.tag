<%@ attribute name="hideHeaderLinks" required="false"%>
<%@ attribute name="showOnlySiteLogo" required="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>

<!-- R2.3 for track order Start -->
<%@ taglib prefix="trackOrder" tagdir="/WEB-INF/tags/responsive/common/header"%> 
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

<header class="marketplace-header">	
	
	<%--<!-- geolocation start-->
	
	<input type="hidden" id="latlng" value="">
    <input type="hidden" id="location" value="">
    
    <!-- geolocation End--> --%>

	
	
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

					<ul class="headerUl">		<!-- PRDI-261 -->


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
						<button class="searchButtonGlobal"></button>
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
						<div class="transient-mini-bag"></div>
					<a href="/cart" class="mini-cart-link myBag-sticky"
						data-mini-cart-url="/cart/rollover/MiniCart"
						data-mini-cart-refresh-url="/cart/miniCart/SUBTOTAL"
						data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
						style="position: static;"><spring:theme code="minicart.mybag" />&nbsp;(<span
						class="js-mini-cart-count-hover"></span>) </a>
						
						<div class="mini-bag"></div> <!-- Added for UF-268 -->
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
<!-- Temporary fix for PRDI-230/263 below for sale-->
<c:if test="${not empty isLuxury and isLuxury == 'true'}">
<style>
@charset "UTF-8";.owl-carousel,.owl-carousel .owl-item{-webkit-tap-highlight-color:transparent;position:relative}.owl-carousel .animated{-webkit-animation-duration:1s;animation-duration:1s;-webkit-animation-fill-mode:both;animation-fill-mode:both}.owl-carousel .owl-animated-in{z-index:0}.owl-carousel .owl-animated-out{z-index:1}.owl-carousel .fadeOut{-webkit-animation-name:fadeOut;animation-name:fadeOut}@-webkit-keyframes fadeOut{0%{opacity:1}100%{opacity:0}}@keyframes fadeOut{0%{opacity:1}100%{opacity:0}}.owl-height{-webkit-transition:height .5s ease-in-out;-moz-transition:height .5s ease-in-out;-ms-transition:height .5s ease-in-out;-o-transition:height .5s ease-in-out;transition:height .5s ease-in-out}.owl-carousel{display:none;width:100%;z-index:1}.owl-carousel .owl-stage{position:relative;-ms-touch-action:pan-Y}.owl-carousel .owl-stage:after{content:".";display:block;clear:both;visibility:hidden;line-height:0;height:0}.owl-carousel .owl-stage-outer{position:relative;overflow:hidden;-webkit-transform:translate3d(0,0,0)}.owl-carousel .owl-controls .owl-dot,.owl-carousel .owl-controls .owl-nav .owl-next,.owl-carousel .owl-controls .owl-nav .owl-prev{cursor:pointer;cursor:hand;-webkit-user-select:none;-khtml-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.owl-carousel.owl-loaded{display:block}.owl-carousel.owl-loading{opacity:0;display:block}.owl-carousel.owl-hidden{opacity:0}.owl-carousel .owl-refresh .owl-item{display:none}.owl-carousel .owl-item{min-height:1px;float:left;-webkit-backface-visibility:hidden;-webkit-touch-callout:none;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.owl-carousel .owl-item img{display:block;width:100%;-webkit-transform-style:preserve-3d;transform-style:preserve-3d}.luxury-header .mobile-menu-box .mmega-menu>ul>li.active>.menu-click:after,.luxury-header .mobile-menu-box ul.sub-menu>li.active>.menu-click:after,.luxury-header .mobile-menu-box ul.sub-menu>li>a.active:after{transform:rotate(90deg)}.owl-carousel.owl-text-select-on .owl-item{-webkit-user-select:auto;-moz-user-select:auto;-ms-user-select:auto;user-select:auto}.owl-carousel .owl-grab{cursor:move;cursor:-webkit-grab;cursor:-o-grab;cursor:-ms-grab;cursor:grab}.owl-carousel.owl-rtl{direction:rtl}.owl-carousel.owl-rtl .owl-item{float:right}.no-js .owl-carousel{display:block}.owl-carousel .owl-item .owl-lazy{opacity:0;-webkit-transition:opacity .4s ease;-moz-transition:opacity .4s ease;-ms-transition:opacity .4s ease;-o-transition:opacity .4s ease;transition:opacity .4s ease}.owl-carousel .owl-video-wrapper{position:relative;height:100%;background:#000}.owl-carousel .owl-video-play-icon{position:absolute;height:80px;width:80px;left:50%;top:50%;margin-left:-40px;margin-top:-40px;cursor:pointer;z-index:1;-webkit-backface-visibility:hidden;-webkit-transition:scale .1s ease;-moz-transition:scale .1s ease;-ms-transition:scale .1s ease;-o-transition:scale .1s ease;transition:scale .1s ease}.owl-carousel .owl-video-play-icon:hover{-webkit-transition:scale(1.3,1.3);-moz-transition:scale(1.3,1.3);-ms-transition:scale(1.3,1.3);-o-transition:scale(1.3,1.3);transition:scale(1.3,1.3)}.owl-carousel .owl-video-playing .owl-video-play-icon,.owl-carousel .owl-video-playing .owl-video-tn{display:none}.owl-carousel .owl-video-tn{opacity:0;height:100%;background-position:center center;background-repeat:no-repeat;-webkit-background-size:contain;-moz-background-size:contain;-o-background-size:contain;background-size:contain;-webkit-transition:opacity .4s ease;-moz-transition:opacity .4s ease;-ms-transition:opacity .4s ease;-o-transition:opacity .4s ease;transition:opacity .4s ease}.owl-carousel .owl-video-frame{position:relative;z-index:1}.owl-theme .owl-dots .owl-dot{display:inline-block;zoom:1}.owl-theme .owl-dots .owl-dot span{width:10px;height:10px;margin:5px;background:#d6d6d6;display:block;-webkit-backface-visibility:visible;-webkit-transition:opacity .2s ease;-moz-transition:opacity .2s ease;-ms-transition:opacity .2s ease;-o-transition:opacity .2s ease;transition:opacity .2s ease;-webkit-border-radius:30px;-moz-border-radius:30px;border-radius:30px}.owl-theme .owl-dots .owl-dot.active span,.owl-theme .owl-dots .owl-dot:hover span{background:#333}@font-face{font-family:Roboto;font-style:normal;font-weight:300;src:local('Roboto Light'),local('Roboto-Light'),url(https://fonts.gstatic.com/s/roboto/v15/0eC6fl06luXEYWpBSJvXCBJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0460-052F,U+20B4,U+2DE0-2DFF,U+A640-A69F}@font-face{font-family:Roboto;font-style:normal;font-weight:300;src:local('Roboto Light'),local('Roboto-Light'),url(https://fonts.gstatic.com/s/roboto/v15/Fl4y0QdOxyyTHEGMXX8kcRJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0400-045F,U+0490-0491,U+04B0-04B1,U+2116}@font-face{font-family:Roboto;font-style:normal;font-weight:300;src:local('Roboto Light'),local('Roboto-Light'),url(https://fonts.gstatic.com/s/roboto/v15/-L14Jk06m6pUHB-5mXQQnRJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+1F00-1FFF}@font-face{font-family:Roboto;font-style:normal;font-weight:300;src:local('Roboto Light'),local('Roboto-Light'),url(https://fonts.gstatic.com/s/roboto/v15/I3S1wsgSg9YCurV6PUkTORJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0370-03FF}@font-face{font-family:Roboto;font-style:normal;font-weight:300;src:local('Roboto Light'),local('Roboto-Light'),url(https://fonts.gstatic.com/s/roboto/v15/NYDWBdD4gIq26G5XYbHsFBJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0102-0103,U+1EA0-1EF9,U+20AB}@font-face{font-family:Roboto;font-style:normal;font-weight:300;src:local('Roboto Light'),local('Roboto-Light'),url(https://fonts.gstatic.com/s/roboto/v15/Pru33qjShpZSmG3z6VYwnRJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0100-024F,U+1E00-1EFF,U+20A0-20AB,U+20AD-20CF,U+2C60-2C7F,U+A720-A7FF}@font-face{font-family:Roboto;font-style:normal;font-weight:300;src:local('Roboto Light'),local('Roboto-Light'),url(https://fonts.gstatic.com/s/roboto/v15/Hgo13k-tfSpn0qi1SFdUfVtXRa8TVwTICgirnJhmVJw.woff2) format('woff2');unicode-range:U+0000-00FF,U+0131,U+0152-0153,U+02C6,U+02DA,U+02DC,U+2000-206F,U+2074,U+20AC,U+2212,U+2215,U+E0FF,U+EFFD,U+F000}@font-face{font-family:Roboto;font-style:normal;font-weight:400;src:local('Roboto'),local('Roboto-Regular'),url(https://fonts.gstatic.com/s/roboto/v15/ek4gzZ-GeXAPcSbHtCeQI_esZW2xOQ-xsNqO47m55DA.woff2) format('woff2');unicode-range:U+0460-052F,U+20B4,U+2DE0-2DFF,U+A640-A69F}@font-face{font-family:Roboto;font-style:normal;font-weight:400;src:local('Roboto'),local('Roboto-Regular'),url(https://fonts.gstatic.com/s/roboto/v15/mErvLBYg_cXG3rLvUsKT_fesZW2xOQ-xsNqO47m55DA.woff2) format('woff2');unicode-range:U+0400-045F,U+0490-0491,U+04B0-04B1,U+2116}@font-face{font-family:Roboto;font-style:normal;font-weight:400;src:local('Roboto'),local('Roboto-Regular'),url(https://fonts.gstatic.com/s/roboto/v15/-2n2p-_Y08sg57CNWQfKNvesZW2xOQ-xsNqO47m55DA.woff2) format('woff2');unicode-range:U+1F00-1FFF}@font-face{font-family:Roboto;font-style:normal;font-weight:400;src:local('Roboto'),local('Roboto-Regular'),url(https://fonts.gstatic.com/s/roboto/v15/u0TOpm082MNkS5K0Q4rhqvesZW2xOQ-xsNqO47m55DA.woff2) format('woff2');unicode-range:U+0370-03FF}@font-face{font-family:Roboto;font-style:normal;font-weight:400;src:local('Roboto'),local('Roboto-Regular'),url(https://fonts.gstatic.com/s/roboto/v15/NdF9MtnOpLzo-noMoG0miPesZW2xOQ-xsNqO47m55DA.woff2) format('woff2');unicode-range:U+0102-0103,U+1EA0-1EF9,U+20AB}@font-face{font-family:Roboto;font-style:normal;font-weight:400;src:local('Roboto'),local('Roboto-Regular'),url(https://fonts.gstatic.com/s/roboto/v15/Fcx7Wwv8OzT71A3E1XOAjvesZW2xOQ-xsNqO47m55DA.woff2) format('woff2');unicode-range:U+0100-024F,U+1E00-1EFF,U+20A0-20AB,U+20AD-20CF,U+2C60-2C7F,U+A720-A7FF}@font-face{font-family:Roboto;font-style:normal;font-weight:400;src:local('Roboto'),local('Roboto-Regular'),url(https://fonts.gstatic.com/s/roboto/v15/CWB0XYA8bzo0kSThX0UTuA.woff2) format('woff2');unicode-range:U+0000-00FF,U+0131,U+0152-0153,U+02C6,U+02DA,U+02DC,U+2000-206F,U+2074,U+20AC,U+2212,U+2215,U+E0FF,U+EFFD,U+F000}@font-face{font-family:Roboto;font-style:normal;font-weight:500;src:local('Roboto Medium'),local('Roboto-Medium'),url(https://fonts.gstatic.com/s/roboto/v15/ZLqKeelYbATG60EpZBSDyxJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0460-052F,U+20B4,U+2DE0-2DFF,U+A640-A69F}@font-face{font-family:Roboto;font-style:normal;font-weight:500;src:local('Roboto Medium'),local('Roboto-Medium'),url(https://fonts.gstatic.com/s/roboto/v15/oHi30kwQWvpCWqAhzHcCSBJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0400-045F,U+0490-0491,U+04B0-04B1,U+2116}@font-face{font-family:Roboto;font-style:normal;font-weight:500;src:local('Roboto Medium'),local('Roboto-Medium'),url(https://fonts.gstatic.com/s/roboto/v15/rGvHdJnr2l75qb0YND9NyBJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+1F00-1FFF}@font-face{font-family:Roboto;font-style:normal;font-weight:500;src:local('Roboto Medium'),local('Roboto-Medium'),url(https://fonts.gstatic.com/s/roboto/v15/mx9Uck6uB63VIKFYnEMXrRJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0370-03FF}@font-face{font-family:Roboto;font-style:normal;font-weight:500;src:local('Roboto Medium'),local('Roboto-Medium'),url(https://fonts.gstatic.com/s/roboto/v15/mbmhprMH69Zi6eEPBYVFhRJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0102-0103,U+1EA0-1EF9,U+20AB}@font-face{font-family:Roboto;font-style:normal;font-weight:500;src:local('Roboto Medium'),local('Roboto-Medium'),url(https://fonts.gstatic.com/s/roboto/v15/oOeFwZNlrTefzLYmlVV1UBJtnKITppOI_IvcXXDNrsc.woff2) format('woff2');unicode-range:U+0100-024F,U+1E00-1EFF,U+20A0-20AB,U+20AD-20CF,U+2C60-2C7F,U+A720-A7FF}@font-face{font-family:Roboto;font-style:normal;font-weight:500;src:local('Roboto Medium'),local('Roboto-Medium'),url(https://fonts.gstatic.com/s/roboto/v15/RxZJdnzeo3R5zSexge8UUVtXRa8TVwTICgirnJhmVJw.woff2) format('woff2');unicode-range:U+0000-00FF,U+0131,U+0152-0153,U+02C6,U+02DA,U+02DC,U+2000-206F,U+2074,U+20AC,U+2212,U+2215,U+E0FF,U+EFFD,U+F000}@font-face{font-family:'Playfair Display';font-style:normal;font-weight:400;src:local('Playfair Display'),local('PlayfairDisplay-Regular'),url(https://fonts.gstatic.com/s/playfairdisplay/v10/2NBgzUtEeyB-Xtpr9bm1CUR-13DsDU150T1bKbJZejI.woff2) format('woff2');unicode-range:U+0400-045F,U+0490-0491,U+04B0-04B1,U+2116}@font-face{font-family:'Playfair Display';font-style:normal;font-weight:400;src:local('Playfair Display'),local('PlayfairDisplay-Regular'),url(https://fonts.gstatic.com/s/playfairdisplay/v10/2NBgzUtEeyB-Xtpr9bm1CfoVn-aGdXvQRwgLLg-TkDk.woff2) format('woff2');unicode-range:U+0100-024F,U+1E00-1EFF,U+20A0-20AB,U+20AD-20CF,U+2C60-2C7F,U+A720-A7FF}@font-face{font-family:'Playfair Display';font-style:normal;font-weight:400;src:local('Playfair Display'),local('PlayfairDisplay-Regular'),url(https://fonts.gstatic.com/s/playfairdisplay/v10/2NBgzUtEeyB-Xtpr9bm1Cdhy5e3cTyNKTHXrP9DO-Rc.woff2) format('woff2');unicode-range:U+0000-00FF,U+0131,U+0152-0153,U+02C6,U+02DA,U+02DC,U+2000-206F,U+2074,U+20AC,U+2212,U+2215,U+E0FF,U+EFFD,U+F000}@font-face{font-family:'Playfair Display';font-style:normal;font-weight:700;src:local('Playfair Display Bold'),local('PlayfairDisplay-Bold'),url(https://fonts.gstatic.com/s/playfairdisplay/v10/UC3ZEjagJi85gF9qFaBgIKHabUDGjprROP0Kzi4LtY8.woff2) format('woff2');unicode-range:U+0400-045F,U+0490-0491,U+04B0-04B1,U+2116}@font-face{font-family:'Playfair Display';font-style:normal;font-weight:700;src:local('Playfair Display Bold'),local('PlayfairDisplay-Bold'),url(https://fonts.gstatic.com/s/playfairdisplay/v10/UC3ZEjagJi85gF9qFaBgILCFnVHHm1VfBoEzRr6gqH0.woff2) format('woff2');unicode-range:U+0100-024F,U+1E00-1EFF,U+20A0-20AB,U+20AD-20CF,U+2C60-2C7F,U+A720-A7FF}@font-face{font-family:'Playfair Display';font-style:normal;font-weight:700;src:local('Playfair Display Bold'),local('PlayfairDisplay-Bold'),url(https://fonts.gstatic.com/s/playfairdisplay/v10/UC3ZEjagJi85gF9qFaBgIIsv7neNnoQYDmljOSnH1QE.woff2) format('woff2');unicode-range:U+0000-00FF,U+0131,U+0152-0153,U+02C6,U+02DA,U+02DC,U+2000-206F,U+2074,U+20AC,U+2212,U+2215,U+E0FF,U+EFFD,U+F000}.luxury-header{color:#333;font-family:Roboto;min-height:125px;line-height:1.42857143}.luxury-header .owl-carousel{padding:0}.luxury-header .owl-theme .owl-controls .owl-nav div:hover{background:0 0}.luxury-header .btn{border-radius:0}.luxury-header .btn-default{background-color:#000;color:#fff;padding:8px 30px;border:1px solid #000;text-transform:uppercase}.luxury-header .btn-default:active,.luxury-header .btn-default:focus,.luxury-header .btn-default:hover{background-color:#333;color:#fff;border-color:#333;box-shadow:none}.luxury-header a:hover{text-decoration:underline}.luxury-header .container:after,.luxury-header .container:before{display:table;content:" "}.luxury-header .container:after{clear:both}.luxury-header .container{padding:0 10px!important}.luxury-header .header-top{transition:all .4s;position:relative;z-index:99;background:#191919}.luxury-header .header-top .store-links{float:left;font-family:Roboto;font-weight:700;position:absolute;left:0;top:0;letter-spacing:1px}.luxury-header .header-top .store-links a{display:inline-block;padding:11px 20px 10px;text-transform:uppercase;font-size:14px;background:#000;width:170px;text-align:center;float:left}.luxury-header .header-top .store-links a:first-child{background:#fff;color:#a5173d}.luxury-header .header-top a{color:#fff}.luxury-header .header-top ul{list-style:none;margin:0;padding:0}body.menuOpen{overflow:hidden}.luxury-header li.last .top-drop-box{left:auto;right:0}.luxury-header li:hover .top-drop-box{opacity:1;visibility:visible;height:auto;top:100%}.luxury-header .top-drop-box{transition:top 0s;box-shadow:0 0 10px #000;z-index:-999;opacity:0;visibility:hidden;overflow:hidden;height:0;position:absolute;top:-1000px;left:0;width:250px;background:#fff;padding:20px}.luxury-header .top-drop-box h4{font-weight:400;padding:0;margin:0 0 15px}.luxury-header .header-top .top-user-links ul li .top-drop-box a{color:#333;text-decoration:none}.luxury-header .top-drop-box .foot{text-align:center;font-size:13px}.luxury-header .top-drop-box .or{text-align:center;padding:20px 0;font-size:16px}.luxury-header .form-control{border-radius:0}.luxury-header .mobile-menu-box{display:block;-webkit-transition:left 1s;transition:left .5s;position:fixed;height:100%;width:100%;overflow:visible;left:-110%;top:0;z-index:999}.luxury-header .mobile-menu-box.openm{left:0}.luxury-header .mobile-menu-box .mobile-menu-box-inner{background:#000;overflow:auto;height:100%;width:calc(100% - 18px)}.luxury-header .mobile-menu-box .mobile-menu-box-inner .mlogo-sec{padding:15px 20px;border-bottom:1px solid rgba(225,225,225,.2)}.luxury-header .mobile-menu-box .mobile-menu-box-inner .mlogo-sec img{height:30px}.luxury-header .menu>ul>li.active>a{text-decoration:none;font-weight:500;color:#fff}.luxury-header .mobile-menu-box .mmega-menu>ul>li{position:relative;border-bottom:1px solid #1b1b1b}.luxury-header .mobile-menu-box .mmega-menu>ul>li>a{position:relative;font-size:14px;display:inline-block;line-height:30px;padding:10px 20px;text-transform:uppercase;color:#cecece;text-decoration:none}.luxury-header .mobile-menu-box>ul>li>a:hover{font-weight:500;color:#fff}.luxury-header .mobile-menu-box ul{list-style:none;padding:0;margin:0}.luxury-header .mobile-menu-box .top-drop-box{width:100%!important;position:static;opacity:1;visibility:visible;height:auto;display:none}.luxury-header .mobile-menu-box .mega-menu-outer{position:static}.luxury-header .mobile-menu-box ul.sub-menu>li{position:relative;background:#eee;border-bottom:1px solid #dedede}.luxury-header .mobile-menu-box .top-user-links,.luxury-header .mobile-menu-box .top-user-links>ul>li{width:100%}.luxury-header .mobile-menu-box .top-user-links>ul>li{padding:5px 20px}.luxury-header .mobile-menu-box .top-user-links>ul>li>a{display:block}.luxury-header .mobile-menu-box .top-user-links>ul>li:hover .top-drop-box{display:none}.luxury-header .mobile-menu-box .top-user-links>ul>li.active{background:#4e4e4e}.luxury-header .mobile-menu-box .close-m{color:#fff;font-size:20px;position:absolute;right:24px;top:8px;width:40px;height:40px;background:#000;line-height:40px;text-align:center;opacity:.7;z-index:9}.luxury-header .mobile-menu-box .close-m .fa{line-height:40px}.luxury-header .top-user-links{float:right;font-size:12px}.luxury-header .top-user-links ul{list-style:none;padding:0;margin:0}.luxury-header .top-user-links>ul>li{display:inline-block;position:relative;padding:0 10px}.luxury-header .top-user-links>ul>li:hover .top-drop-box{display:block;opacity:1;visibility:visible}.luxury-header .top-user-links>ul>li>a{display:inline-block;padding:12px 10px 12px 26px;color:#cecece;position:relative;text-decoration:none}.luxury-header .top-user-links>ul>li>a:after{position:absolute;top:11px;left:0;content:"";width:20px;height:24px;background:url(https://luxuryuat.tataunistore.com/images/top-icons.png) no-repeat}.luxury-header .top-user-links>ul>li>a.ico2:after{background-position:-147px 0}.luxury-header .top-user-links>ul>li>a.ico3:after{background-position:-257px 0}.luxury-header .top-user-links>ul>li>a.ico4:after{background-position:-397px 0}.luxury-header .top-user-links>ul>li.last .top-drop-box{left:auto;right:0}.luxury-header .top-user-links>ul>li:hover{background-color:#4e4e4e;z-index:99}.luxury-header .menu-sec{background:#000;padding:15px 0;position:relative}.luxury-header .menu-sec .logo{float:left}.luxury-header .menu-sec .logo img{max-width:188px}.luxury-header .menu-sec .mmenu-trigger{padding:15px 10px;float:left;cursor:pointer;margin:0 4px;display:none}.luxury-header .menu-sec .mmenu-trigger span{height:2px;background:#dadada;display:block;width:15px;position:relative}.luxury-header .menu-sec .mmenu-trigger span:after,.luxury-header .menu-sec .mmenu-trigger span:before{content:'';position:absolute;width:100%;float:left;height:2px;background:#dadada;left:0}.luxury-header .menu-sec .mmenu-trigger span:after{top:-6px}.luxury-header .menu-sec .mmenu-trigger span:before{bottom:-6px}.luxury-header .mobile-search-cart{float:right;padding:0 10px 0 0;display:none}.luxury-header .mobile-search-cart a{background:url(https://luxuryuat.tataunistore.com/images/search-ico.png) 50% 50% no-repeat;background-size:16px auto;width:26px;height:26px;display:inline-block}.luxury-header .mobile-search-cart a.ico-cart{position:relative;background:url(https://luxuryuat.tataunistore.com/images/bag-icon.png) 50% 50% no-repeat;background-size:15px auto}.luxury-header .mobile-search-cart a.ico-cart span{min-width:15px;height:15px;line-height:15px;text-align:center;font-size:9px;letter-spacing:0;color:#fff;background:red;border-radius:50%;position:absolute;top:-5px;right:-5px;padding:0 4px}.luxury-header .menu{padding:0 0 0 40px;font-family:Roboto;display:inline-block}.luxury-header .menu *{transition:all 0s}.luxury-header .menu>ul{list-style:none;padding:0;margin:0}.luxury-header .menu>ul>li{display:inline-block;letter-spacing:1px}.luxury-header .menu>ul>li:hover{margin:-25px 0;padding:25px 0}.luxury-header .menu>ul>li>a{color:#cecece;display:inline-block;padding:10px 15px;text-transform:uppercase;font-size:15px;text-align:center}.luxury-header .menu>ul>li>a:hover{text-decoration:none;font-weight:500;color:#fff}.luxury-header .menu>ul>li:hover>a{text-decoration:none;font-weight:500}.luxury-header .searchForm{float:right}.luxury-header .searchbox{float:right;width:400px;position:relative;z-index:9}.luxury-header .searchbox .btn,.luxury-header .searchbox .form-control{background:0 0;border-color:#333;border-radius:0;height:40px;color:#fff}.luxury-header .searchbox .btn{background:url(https://luxuryuat.tataunistore.com/images/search-icon.jpg) 50% 50% no-repeat;width:40px;border-left:0}.luxury-header .searchbox .form-control{border-right:0;font-size:14px;letter-spacing:1px}.luxury-header .searchbox .ui-autocomplete-box{display:none;box-shadow:0 0 10px #000;max-height:500px;overflow:auto;font-weight:300;position:absolute;top:100%;left:0;width:100%;background:#fff;padding:10px;list-style:none;font-size:14px}.luxury-header .searchbox .ui-autocomplete-box .ui-menu-item{font-weight:500;padding:0 0 8px}.luxury-header .searchbox .ui-autocomplete-box .product-list{font-weight:300;padding:0 0 5px 10px}.luxury-header .searchbox .ui-autocomplete-box .product-list a{color:#777}.luxury-header .searchbox .ui-autocomplete-box a{color:#000}.luxury-header .searchbox .ui-autocomplete-box .product{width:50%;float:left;padding:10px 5px;border-top:1px solid #dfd1d5;margin:10px 0}.luxury-header .searchbox .ui-autocomplete-box .product .Best-Sellers{text-transform:uppercase;text-decoration:none}.luxury-header .searchbox .ui-autocomplete-box .product img{max-width:100%;margin:8px 0}.luxury-header .searchbox .ui-autocomplete-box .product:nth-last-child(1) .Best-Sellers{opacity:0}.luxury-header .mobile-menu-box ul.sub-menu>li>a{position:relative;display:inline-block}.luxury-header .main-menu-outer .top-user-links.myBag-link{display:none}.luxury-header .main-menu-outer .top-user-links.myBag-link>ul>li>a{color:#cecece}.luxury-header .top-user-links.myBag-link>ul>li:hover{color:#333;background:#4e4e4e}.luxury-header .top-user-links.myBag-link>ul>li>a.ico4:after{position:absolute;top:7px;left:0;content:"";width:20px;height:24px}.luxury-header .main-menu-outer.fixed{z-index:999;position:fixed;width:100%;top:0;left:0}.luxury-header .main-menu-outer.fixed .top-user-links{display:block}.luxury-header .mega-menu-outer{z-index:9;font-family:Roboto;background:#fff;border-bottom:1px solid #ccc;position:absolute;top:-1100%;left:0;width:100%;display:block}.luxury-header .mega-menu-outer:after{z-index:99;content:'';position:absolute;top:53px;left:0;width:100%;border-bottom:1px solid #ccc}.luxury-header .mega-menu-outer ul.sub-menu{height:53px;line-height:53px}ag .luxury-header .mega-menu-outer ul{list-style:none;padding:0;margin:0}.luxury-header .mega-menu-outer ul>li{display:inline-block;font-weight:300}.luxury-header .mega-menu-outer ul>li>a{position:relative;z-index:1;padding:0;min-width:156px;text-align:center;text-transform:uppercase;color:#333;text-decoration:none;display:block;font-size:14px}.luxury-header .mega-menu-outer ul>li li>a{text-align:left;min-width:10px}.luxury-header .mega-menu-outer ul.sub-menu>li.active>a,.luxury-header .mega-menu-outer ul.sub-menu>li>a:hover{border-bottom:2px solid #000;color:#000;font-weight:400;z-index:9999;line-height:52px;position:relative}.luxury-header .mega-menu-outer ul>li>a:hover{color:#000;font-weight:400}.luxury-header .mega-menu-outer ul>li .mega-menu-links{width:100%;position:absolute;box-shadow:0 5px 5px #cacaca;height:auto;left:0;padding:20px 0;display:block;background:#fff;overflow:hidden;opacity:0}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left{width:59%;float:left;padding:0 20px 0 0}.luxury-header .mega-menu-outer ul>li .mega-menu-links .container{max-width:1110px!important}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left>ul.list-style{width:33.33%;padding-right:20px;float:left}.luxury-header .mega-menu-outer ul>li .mega-menu-links h3{font-size:12px;text-transform:uppercase;margin:0 0 10px;font-weight:500;padding:0;line-height:normal}.luxury-header .mega-menu-outer ul>li .mega-menu-links h3 a{text-decoration:none;color:#333}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left>ul.list-style>li{display:block;text-transform:uppercase;text-decoration:none;line-height:normal}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left>ul.list-style>li>a{padding:6px 0;font-size:12px}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left>ul.list-style>li>a:hover{border:0}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-right{width:41%;float:left;padding:0}.luxury-header .mega-menu-outer ul>li:hover .mega-menu-links{display:block}.luxury-header .mega-menu-slider{position:relative}.luxury-header .mega-menu-slider .owl-controls{position:absolute;bottom:0;left:20px;width:100%;z-index:11;top:auto}.luxury-header .mega-menu-slider .owl-dots{display:none!important}.luxury-header .mega-menu-slider .owl-nav .owl-prev{position:absolute;left:0;bottom:0;font-size:0}.luxury-header .mega-menu-slider .owl-nav .owl-prev:after{content:"";display:inline-block;background:url(https://luxuryuat.tataunistore.com/images/arrow_left_grey.png) no-repeat;width:13px;height:24px;margin:5px 0}.luxury-header .mega-menu-slider .owl-nav .owl-next{position:absolute;right:40px;bottom:0;font-size:0}.luxury-header .mega-menu-slider .owl-nav .owl-next:after{content:"";display:inline-block;background:url(https://luxuryuat.tataunistore.com/images/arrow_right_grey.png) no-repeat;width:13px;height:24px;margin:5px 0}.luxury-header .mega-menu-slider .item{background:#f9f9f9}.luxury-header .mega-menu-slider .item img{max-height:260px;width:auto;margin:0 auto}.luxury-header .menu-slider-info{padding:20px;background:#f9f9f9;text-align:center}.luxury-header .menu-slider-info h3{color:#000;text-transform:uppercase;margin:0 0 15px;font-size:14px;font-family:Roboto;letter-spacing:1px}.luxury-header .menu-slider-info .btn{display:inline-block;min-width:120px;margin:0 auto;padding:8px 10px}.luxury-header .menu-slider-info .btn:hover{text-decoration:none}.luxury-header .mobile-menu-box .mmega-menu>ul>li>.menu-click{color:#9a9a9a;background:rgba(225,225,225,.2);position:absolute;width:50px;height:50px;display:block!important;right:0;top:0;text-align:center;line-height:50px}.luxury-header .mobile-menu-box .mmega-menu>ul>li>.menu-click:after{content:"\f105";transition:all .3s ease;display:block;font-family:fontAwesome;font-size:20px}.luxury-header .mobile-menu-box ul.sub-menu>li>.menu-click{background:#dedede;z-index:2;position:absolute;width:50px;height:40px;display:block!important;right:0;top:0;text-align:center;line-height:40px}.luxury-header .mobile-menu-box ul.sub-menu>li>.menu-click:after{content:"\f105";transition:all .3s ease;display:block;font-family:fontAwesome;font-size:20px}.luxury-header .my-bag .top-drop-box{width:550px!important}.luxury-header .mini-bag{position:relative;padding:0;margin:0}.luxury-header .mini-bag ul{list-style:none;padding:0;margin:0}.luxury-header .mini-bag>li.item{list-style:none;padding-left:120px!important;padding-right:100px!important;padding-bottom:20px;position:relative;height:160px;color:#333}.luxury-header .mini-bag>li.item ul{margin:0;padding:0}.luxury-header .mini-bag>li.item a{color:#333;padding:0!important}.luxury-header .mini-bag .item .product-img{position:absolute;left:0;width:110px}.luxury-header .mini-bag .item .product-img img{width:100%;max-height:155px;height:auto}.luxury-header .mini-bag .item .product{line-height:20px}.luxury-header .mini-bag .item .product h3{margin:0;line-height:18px}.luxury-header .mini-bag .item .product .product-brand-name a{font-size:12px;text-decoration:none}.luxury-header .mini-bag .item .product .product-name a{font-size:16px;font-weight:600;text-decoration:none;padding:0}.luxury-header .mini-bag .item .item-edit-details{position:absolute;bottom:15px;left:120px;line-height:20px}.luxury-header .mini-bag .item .item-edit-details li{display:block}.luxury-header .mini-bag .item .price{position:absolute;right:0;top:20px;width:70px;text-align:right;font-size:14px}.luxury-header .totalPayable{font-size:14px;font-weight:700;text-align:left;padding:20px 0 20px 120px!important;text-transform:capitalize}.luxury-header .totalPayable .total-payable-amount{float:right}.luxury-header .mini-bag .btn{width:100%;color:#fff!important}.luxury-header .wishlist-info>li{list-style:none}.luxury-header .wishlist-info>li a{font-weight:700;color:#333;text-decoration:none;padding:0!important}.luxury-header .wishlist-info>li a span{font-weight:400;color:#333;display:block}.luxury-header .wishlist-info>.all-wishlist{line-height:30px;display:block}.luxury-header .wishlist-info>.all-wishlist a{padding:0;text-align:left;text-decoration:underline;font-weight:700;font-size:14px;text-transform:uppercase}.luxury-header .account-details{margin:0;padding:0;font-size:14px;position:relative}.luxury-header .account-details .heading{font-weight:700;text-transform:uppercase;padding:5px 0}.luxury-header .account-details>li:last-child{position:absolute;right:0;top:0!important;padding:0!important}.luxury-header .account-details>li a{color:#333;font-weight:400;display:block;padding:5px 0!important}.luxury-header .mobile-menu-box *{transition:all 0}.luxury-header .top-notification li{padding:0 0 20px;border-bottom:1px solid #ccc;margin:0 0 20px}.luxury-header .top-notification li:last-child{padding:0;margin:0;border:0}.luxury-header .top-notification li .time{color:#ccc;float:right}.luxury-header .header-top .top-user-links ul li .top-drop-box a.remove,.luxury-header .mini-bag>li.item a.remove{color:#ff1c47;text-decoration:underline}.luxury-footer{padding:30px 0 0;background:#f5f5f5;font-size:13px;font-weight:400;overflow:hidden;font-family:Roboto}.footer-copyright,.footer-text{font-weight:300;font-size:11px}.luxury-footer a:hover{color:#000}.luxury-footer .footer-left{width:65%;float:left}.luxury-footer .footer-right{width:35%;float:right}.luxury-footer li{padding:7px 0;line-height:normal}.luxury-footer li:first-child{padding-top:10px}.luxury-footer li a{color:#333;text-decoration:none}.luxury-footer h3{margin:0 0 10px;font-size:13px;color:#333;text-transform:uppercase;font-family:Roboto;letter-spacing:1px}.luxury-footer h3.heading{padding:15px 0 0}.luxury-footer h3 img{max-width:152px}.luxury-footer .input-group{margin-top:20px}.luxury-footer .input-group>.form-control{border-radius:0;background:#fff;height:50px;line-height:38px;border:0;font-size:12px}.luxury-footer .input-group-btn .btn{line-height:38px;padding:6px 16px;border:0;text-transform:uppercase}.luxury-footer .social-sec{padding:20px 0 0}.luxury-footer .socil-links{padding:10px 0 0}.luxury-footer .socil-links a:first-child{padding-left:0}.luxury-footer .socil-links a{display:inline-block;padding:0 15px;font-size:15px;color:#000}.luxury-footer .socil-links a .fa{font-size:20px}.luxury-footer .apps-download{padding:0}.luxury-footer .apps-link a{display:inline-block;padding:0 25px 0 0;color:#000}.luxury-footer .apps-link a:hover{color:#333}.luxury-footer .apps-link a .fa{font-size:35px}.luxury-footer .newsletter-sec .input-group{max-width:470px;margin:20px auto 30px}.luxury-footer .newsletter-sec h3,.luxury-footer .newsletter-sec h3[class=sm-logo-footer]{text-align:left}.luxury-footer h3.toggle-links{position:relative}.luxury-footer ul{font-weight:300;padding:0;margin:0;list-style:none}.footer-text{padding:60px 0 20px;border-bottom:1px solid #ccc;letter-spacing:1px}.footer-copyright{padding:16px 0;letter-spacing:1px}.luxury-footer .btn-default{background-color:#000;color:#fff;padding:8px 30px;border:1px solid #000;text-transform:uppercase;border-radius:0}.luxury-footer .btn-default:active,.luxury-footer .btn-default:focus,.luxury-footer .btn-default:hover{background-color:#333;color:#fff;border-color:#333;box-shadow:none}.luxury-footer .footer-left-tabOnly{display:none}.luxury-footer .footer-left-tabOnly ul li{float:left;width:33.33%;padding:0 0 20px!important}.luxury-footer .footer-bottom-links ul{padding:0;margin:0;list-style:none;display:block!important}.luxury-footer .footer-bottom-links li{display:inline-block}.luxury-footer .footer-bottom-links li:first-child{font-weight:600}.luxury-footer .footer-bottom-links li:first-child:after{content:':'}.luxury-footer .footer-bottom-links li:last-child:after{content:''}.luxury-footer .footer-bottom-links li:after{content:'/';padding:0 2px}footer{position:relative!important}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left>ul.list-style:last-child{padding-bottom:0}footer .luxury-footer .footer-text{border-top:none;width:100%;margin:auto;overflow:hidden;letter-spacing:.83px;line-height:17px;text-align:justify;font-family:roboto;color:#333;padding:60px 0 20px;border-bottom:1px solid #ccc;font-size:11px}.luxury-footer .footer-bottom-links{float:left;width:100%;padding:10px 0 15px;margin-bottom:20px;border-bottom:1px solid #ccc}.luxury-footer .newsletter-sec .input-group .btn{box-shadow:none}.luxury-footer .footer-left .row{margin-left:-15px!important;margin-right:-15px!important}.luxury-footer .apps-download{display:none!important}.loaders-sec,.myBag_loader,.secLoaderBox{position:absolute;width:100%;height:100%;background:url(https://luxuryuat.tataunistore.com/images/spinner.gif) 50% 50% no-repeat rgba(255,255,255,.7);z-index:999;display:none;left:0;top:0}body .loaders-sec{position:fixed}body .control-label.sr-only{position:static;width:auto;height:auto;padding:0;margin:0 0 5px;overflow:hidden;clip:rect(0,0,0,0);border:0}body .body-Content{padding-bottom:30px!important}.luxury-header .menu ul li .mega-menu-outer.active{display:block;opacity:1;top:100%!important;z-index:9}.luxury-header .mega-menu-outer ul>li .mega-menu-links{z-index:-999}.luxury-header .mega-menu-outer ul>li .mega-menu-links.active{opacity:1;z-index:999999}.luxury-header .transient-mini-bag{position:fixed;top:20px;left:0;width:100%;z-index:999999}.luxury-header .mini-transient-bag{font-family:Roboto;width:300px;background-color:#4a4a4a;top:45px;right:-3px;opacity:.98;-webkit-box-shadow:-1px 4px 7px 1px rgba(0,0,0,.3);-moz-box-shadow:-1px 4px 7px 1px rgba(0,0,0,.3);box-shadow:-1px 4px 7px 1px rgba(0,0,0,.3);float:right;position:relative;line-height:1;letter-spacing:1px}.luxury-header .mini-transient-bag ul{list-style:none;padding:0;margin:0}.luxury-header .mini-transient-bag .cotainer{position:relative}.luxury-header .mini-transient-bag h3{margin:0;padding:0;line-height:15px}.luxury-header .mini-transient-bag p{padding:0;margin:0}.luxury-header .mini-transient-bag a:hover{color:#fff;text-decoration:none}.luxury-header .mini-transient-bag>ul>li.view-bag-li{border-top:none!important;width:calc(100% - -30px);width:-webkit-calc(100% - -30px);margin-left:-15px}.luxury-header .mini-transient-bag *{color:#fff}.luxury-header .mini-transient-bag>ul{padding:15px 15px 0}.luxury-header .mini-transient-bag:after{bottom:100%;left:90%;border:1px solid transparent;content:" ";height:0;width:0;position:absolute;pointer-events:none;border-bottom-color:#4a4a4a;border-width:9px}.luxury-header .mini-transient-bag .item{padding-bottom:5px;position:relative}.luxury-header .mini-transient-bag .item .product-img{position:relative;left:0;width:110px;display:inline-block;vertical-align:top;margin-bottom:10px;background:#fff;padding:10px}.luxury-header .mini-transient-bag .item .product-img img{max-width:100%;height:auto;margin:0 auto}.luxury-header .mini-transient-bag .item .product{line-height:20px;width:160px;display:inline-block;padding-left:10px;padding-right:10px;vertical-align:top}.luxury-header .mini-transient-bag .item .product .product-name a{font-size:14px;font-weight:600}.luxury-header .mini-transient-bag .item .item-edit-details{position:absolute;bottom:15px;left:120px;line-height:20px}.luxury-header .mini-transient-bag .item .item-edit-details li{display:block;margin-right:40px}.luxury-header .mini-transient-bag .item .item-edit-details li:last-of-type a{color:#ff1c47;text-decoration:underline}.luxury-header .mini-transient-bag .item .price{position:absolute;right:0;top:10px;width:100px;text-align:center}.luxury-header .mini-transient-bag>ul>li:not(.item){clear:both;border-top:1px solid #fff}.luxury-header .mini-transient-bag>ul>li:not(.item)::after{clear:both;content:"";display:table}.luxury-header .mini-transient-bag>ul>li:not(.item) .more-items{font-size:14px;font-weight:500;text-align:center;line-height:20px;margin-top:5px;margin-bottom:20px}.luxury-header .mini-transient-bag>ul>li:not(.item) a{text-align:center;letter-spacing:1px;font-weight:500;display:inline-block;color:#fff;line-height:50px;padding:0 25px;width:-webkit-calc(50% - 10px);width:calc(50% - 10px);float:left;text-transform:uppercase;font-size:14px;-webkit-transition:color .3s,background .3s;-moz-transition:color .3s,background .3s;transition:color .3s,background .3s}.luxury-header .mini-transient-bag>ul>li:not(.item) a.go-to-bag{display:block;color:#fff;border-top:1px solid #fff;text-align:center;padding:10px;text-transform:uppercase;clear:both;letter-spacing:2px;font-size:16px;font-weight:600!important;line-height:16px;width:100%}.luxury-header .mini-transient-bag>ul>li:not(.item) a.checkout{background-color:#a9143c;border:2px solid #a9143c}.luxury-header .mini-transient-bag>ul>li:not(.item) a.checkout:hover{color:#a9143c;background-color:#fff;-webkit-transition:color .15s,background .15s;-moz-transition:color .15s,background .15s;transition:color .15s,background .15s}.luxury-header .transient-offer{background-color:#ffb810;padding:2px 4px;line-height:normal;display:inline-block;color:#000!important;font-weight:500;word-break:break-all}.luxury-header span.mini-cart-close{position:absolute;top:-5px;right:13px;color:#fff;font-weight:500;font-size:24px;cursor:pointer;line-height:normal;-webkit-transform:rotate(45deg);-moz-transform:rotate(45deg);-ms-transform:rotate(45deg);-o-transform:rotate(45deg);transform:rotate(45deg)}.luxury-header .mini-transient-bag .addedText{display:inline-block;margin-bottom:10px}.fixed .searchbox{width:300px}.luxury-header .btn,.luxury-header .menu>ul>li,.luxury-header .searchbox .form-control,.luxury-header .top-user-links{letter-spacing:1px}.luxury-header input{font-family:Roboto}body.menuOpen .header{z-index:9999999999999!important}body.menuOpen .menu-sec:after{content:'';transition:background 1s,opacity 1s;left:0;top:0;position:fixed;opacity:.6;background:#fff;z-index:99;width:100%;height:100%}.luxury-header .mega-menu-outer ul>li .mega-menu-links{top:-10000px}.luxury-header .mega-menu-outer ul>li .mega-menu-links.active,.luxury-header .mega-menu-outer ul>li.active>.mega-menu-links{top:100%}@media (max-width:1200px){.luxury-header .searchbox{width:210px}}@media (max-width:991px){.luxury-header .header-top .top-user-links{font-size:11px}.luxury-header .header-top .top-user-links ul li{padding:0 5px}.luxury-header .header-top .store-links a{padding:9px 10px;font-size:11px;width:120px}.luxury-header .header-top .top-user-links ul li a{padding:9px 10px 9px 23px}.luxury-header .header-top .top-user-links ul li a:after{top:5px}.luxury-header .menu>ul>li>a{font-size:12px;min-width:60px}.luxury-header .menu-sec .logo img{height:30px}.luxury-header .searchbox .btn,.luxury-header .searchbox .form-control{height:35px}.luxury-header .menu-sec{padding:15px 0}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-right{display:none}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left{display:block;width:100%;padding:0}.luxury-header .mega-menu-outer ul>li>a{font-size:12px;min-width:100px;padding:0 20px}.luxury-header .mega-menu-outer .container{overflow:auto}.luxury-header .mega-menu-outer .container .sub-menu{width:950px}.luxury-header h2{font-size:18px}.luxury-header .mega-menu-outer ul>li .mega-menu-links{padding:30px 0 20px}.luxury-header .header{min-height:100px}.luxury-header .main-menu-outer .top-user-links.myBag-link>ul>li>a{padding:9px 10px 9px 26px}.luxury-header .main-menu-outer .top-user-links.myBag-link>ul>li>a:after{top:6px}.luxury-header .main-menu-outer .top-user-links.myBag-link{margin:0 -30px 0 0}.luxury-footer .newsletter-sec h3{text-align:center}.footer-text{padding:30px 0 10px;line-height:2}.luxury-footer .footer-right{margin:30px 0 0}.luxury-footer .input-group-btn .btn{padding:6px 33px}.luxury-footer,.luxury-footer h3{font-size:12px}.luxury-footer .footer-text{padding:30px 0 10px!important;line-height:2}}@media (min-width:768px){.luxury-footer .container,.luxury-header .container{width:750px!important}}@media (min-width:992px){.luxury-footer .container,.luxury-header .container{width:970px!important}}@media (min-width:768px) and (max-width:992px){.luxury-header .account-details>li:last-child a{padding-top:0!important}.visible-sm{display:block!important}}@media (max-width:768px){.luxury-header .container{padding:0 14px!important}}@media (min-width:1200px){.luxury-footer .container,.luxury-header .container{max-width:1360px!important;width:100%!important}}@media (max-width:767px){.luxury-header .header-top .top-user-links,.luxury-header .main-menu-outer.fixed .top-user-links.myBag-link,.luxury-header .mega-menu-outer:after,.luxury-header .menu{display:none}.luxury-header .container{padding:0 10px}.luxury-header{min-height:95px}.luxury-header .header-inner{position:fixed;top:0;width:100%;left:0;z-index:99}.luxury-header .main-menu-outer.fixed{position:static}.luxury-header .shopBy-sec ul.brand-shop li{width:50%}.luxury-header .shopBy-sec ul.brand-shop li:nth-child(2n+2){border-right:0}.luxury-header .shopBy-sec ul.brand-shop li:nth-last-child(-n+2){border-bottom:0}.luxury-header .header-top .container{padding:0!important}.luxury-header .header-top .store-links{position:static;width:100%}.luxury-header .header-top .store-links a{width:50%}.luxury-header .menu{position:absolute;width:100%;padding:0;background:#000;top:0;left:0}.luxury-header .menu>ul>li{width:33.33%;float:left}.luxury-header .menu>ul>li>a{min-width:100%;padding:16px 0}.luxury-header .mega-menu-outer ul>li{width:100%;position:relative}.luxury-header .mega-menu-outer ul>li .mega-menu-links{box-shadow:none;position:static}.luxury-header .searchbox{display:none;background:#000;padding:10px;position:absolute;top:60px;left:0;width:100%;z-index:99}.luxury-header .mega-menu-outer,.luxury-header .mega-menu-outer ul.sub-menu{height:auto;text-align:left;line-height:40px}.luxury-header .main-menu-outer .container{padding:0!important}.luxury-header .mega-menu-outer ul.sub-menu>li{display:block;border-bottom:1px solid #e5e5e5}.luxury-header .mega-menu-outer ul.sub-menu>li>a{padding:0 20px;text-align:left}.luxury-header .mega-menu-outer ul>li .mega-menu-links{padding:15px}.luxury-header .mega-menu-outer ul>li .mega-menu-links .col-left>ul.list-style{width:100%;padding:0 0 20px}.luxury-header .mega-menu-outer ul>li:hover .mega-menu-links{display:none}.luxury-header .menu-sec .mmenu-trigger,.luxury-header .mobile-search-cart{display:block}.luxury-header .mobile-menu-box .top-user-links>ul>li.my-bag,.luxury-header .slider .owl-carousel .item .captions p br{display:none}.luxury-header .slider .owl-carousel .item .captions{right:20px;max-width:175px}.luxury-header .slider .owl-carousel .item .captions h1{margin:0;padding:10px 0}.luxury-header .mini-bag .item .product-img{width:50px}.luxury-header .mini-bag>li.item{padding-left:64px!important}.luxury-header .mini-bag .item .item-edit-details{left:50px}.luxury-header .mega-menu-outer .container .sub-menu{width:auto}.luxury-header .mega-menu-outer ul.sub-menu>li.active>a,.luxury-header .mega-menu-outer ul.sub-menu>li>a:hover{border-bottom:0;line-height:inherit}.luxury-header .header-top{position:relative;top:0;width:100%;margin:0}.luxury-header .nav-up{margin-top:-33px}.luxury-footer .footer-left,.luxury-footer .footer-right{width:100%;padding:0;margin:0}.luxury-footer .footer-right{text-align:center;margin:0 0 30px}.luxury-footer .socil-links a:last-child{padding-right:0}.luxury-footer .apps-link a{padding:0 15px}.luxury-footer h3.toggle-links{font-size:12px;text-align:left;padding:5px 0}.luxury-footer h3.toggle-links img{max-width:104px}.luxury-footer h3.toggle-links:after{position:absolute;content:"\f105";transition:all .3s ease;display:inline-block;font-family:fontAwesome;right:5px;top:2px;font-size:20px}.luxury-footer h3.logo-footer:after{top:12px}.luxury-footer h3.toggle-links.active:after{transform:rotate(90deg)}.luxury-footer ul{display:none;margin:0 0 20px}.luxury-footer .input-group-btn .btn{padding:6px 20px}.luxury-footer li:first-child{padding-top:0}.luxury-header .mega-menu-outer ul>li .mega-menu-links{opacity:1;display:none}.luxury-header .mega-menu-outer{display:none;top:0!important;position:static!important}}    </style>
</c:if>
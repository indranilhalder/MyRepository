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
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<cms:pageSlot position="TopHeaderSlot" var="component" element="div"
	class="container">
	<cms:component component="${component}" />
</cms:pageSlot>
<c:if test="${param.source ne null and param.source eq 'App' }">
	<c:set var="showOnlySiteLogo" value="true"></c:set>
	<c:set var="hideSecureTransaction" value="true"></c:set>
	<c:set var="hideLogo" value="true"></c:set>
</c:if>

<header>	
	<!-- For Gigya Social Login -->
	<c:if test="${isGigyaEnabled=='Y'}">
	<SCRIPT type="text/javascript" lang="javascript" src="${gigyasocialloginurl}?apikey=${gigyaAPIKey}">
	
	</SCRIPT>
	
	<script>
function registerUser(eventObject)
{
	var encodedUID = encodeURIComponent(eventObject.UID);
	var encodedTimestamp=encodeURIComponent(eventObject.timestamp);
	var  encodedSignature=encodeURIComponent(eventObject.signature);
	console.log("SOCIAL LOGIN REFERER:-"+ window.location.href)
		 $.ajax({
				url : ACC.config.encodedContextPath + "/oauth2callback/socialLogin/",
				data : {
					'referer' : window.location.href,
					'emailId' : eventObject.user.email,
					'fName':  eventObject.user.firstName,
					'lName' : 	eventObject.user.lastName,
					'uid'		: encodedUID,
					'timestamp'	 :encodedTimestamp,
					'signature' :encodedSignature,
					'provider' :eventObject.user.loginProvider
					},
				type : "GET",
				cache : false,
				success : function(data) {
					//alert("success login page :- "+data);
					if(!data)							
						{
						
						}
						else
						{
							if(data.indexOf(ACC.config.encodedContextPath) > -1)
							{
								window.open(data,"_self");
							}
							else
							{
							var hostName=window.location.host;
							if(hostName.indexOf(':') >=0)
							{
								window.open(ACC.config.encodedContextPath +data,"_self");
							}	
							else
								{
							window.open("https://"+hostName+ACC.config.encodedContextPath +data,"_self");
								}
							}
							
						}	
				},
				error : function(resp) {
					console.log("Error Occured Login Page" + resp);					
				}
			});
	 
}

        // This method is activated when the page is loaded
        function onLoad() {
            // register for login event
            gigya.socialize.addEventHandlers({
                    context: { str: 'congrats on your' }
                    , onLogin: onLoginHandler                   
                    });
        }
        // onLogin Event handler
        function onLoginHandler(eventObj) {
            console.log(eventObj.context.str + ' ' + eventObj.eventName + ' to ' + eventObj.provider
                + '!\n' + eventObj.provider + ' user ID: ' +  eventObj.user.identities[eventObj.provider].providerUID);          
            
            registerUser(eventObj);      
            
        }        
        
        onLoad();
    </script>
</c:if>
	<!-- End  Gigya Social Login -->
	<!-- For Infinite Analytics Start -->
	<input type="hidden" id="ia_site_id" value="${cmsSite.uid}"> 
	<input type="hidden" id="ia_site_page_id" value="${cmsPage.uid}"> 
	<input type="hidden" id="ia_category_code" value="${categoryCode}">
	<input type="hidden" id="ia_product_code" value="${productCode}">
	<input type="hidden" id="ia_product_rootCategory_type" value="${product.rootCategory}">
	<input type="hidden" id="mSellerID" value="${mSellerID}">
	<input type="hidden" id="rootEPForHttp" value="${rootEPForHttp}">
	<input type="hidden" id="rootEPForHttps" value="${rootEPForHttps}">
	<input type="hidden" id="ecompanyForIA" value="${ecompanyForIA}">
	<input type="hidden" id="DamMediaHost" value="${DamMediaHost}">
	<input type="hidden" id="mplStaticResourceHost" value="${mplStaticResourceHost}">
	<!-- For Infinite Analytics End -->
	
	<div class="row header-row"></div>
	<c:choose>
		<c:when test="${empty showOnlySiteLogo }">
			<div class="banner">
				<span class="toggle desktop helpmeshopbanner"><p><spring:theme
						code="header.helpmeshop.howcanwehelpyou.title" /></p></span> <span
					class="toggle mobile helpmeshopbanner"> <p><spring:theme
						code="header.helpmeshop.howcanwehelpyou.mobile" /></p></span>
				<div class="content" id="helpmeshopcontent"></div>
				<%-- <cms:pageSlot position="HeaderLinks" var="link">
					<cms:component component="${link}" element="" />
				</cms:pageSlot> --%>
			</div>
		</c:when>
		<c:otherwise>
			<c:if test="${empty hideSecureTransaction}">
				<span class="secure secureTransaction"> <spring:theme
						code="text.secure.transaction" /></span>
			</c:if>
		</c:otherwise>
	</c:choose>


	<div class="content">

		<div class="top">
			<c:if test="${empty showOnlySiteLogo }">
				<div class="toggle">
					<span></span> <span></span> <span></span>
				</div>
			</c:if>
			<div class="container">
				<c:if test="${empty showOnlySiteLogo }">
					<div class="left">
						<ul>
							<%-- <li><a href="<c:url value="/helpservices"/>"><spring:theme
										code="header.help&Services" /></a></li> --%>

						</ul>
					</div>
				</c:if>

				<div class="marketplace compact">
					<c:if test="${empty hideLogo}">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}" />
						</cms:pageSlot>
					</c:if>
				</div>
				<div class="right">

					<ul>


						<!--Using this tag for 'My Bag' Link in header navigation pane and it will navigate to cart Page  -->

						<c:if test="${empty showOnlySiteLogo }">
							<c:if test="${empty hideHeaderLinks}">
								<cms:pageSlot position="MiniCart" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot>
								<li class="store-locator-header"><a href="#">Our Stores</a></li>
							</c:if>
						</c:if>
						<!--Using this tag for Track Order Link in header navigation pane and it will navigate to 'My Order page'  -->

						<%-- <c:if test="${empty showOnlySiteLogo }">
									<li class="track"><a href="<c:url value="/my-account/orders"/>"><spring:theme
												code="header.trackorder" /></a></li>
								</c:if> --%>

					</ul>



				</div>
			</div>
			<div class="compact-toggle"></div>
		</div>

	<div class="overlay"></div>
		<!-- Using this tag for placing the site logo in header navigation pane -->

		<div class="bottom">

			<c:choose>
				<c:when test="${empty showOnlySiteLogo }">
					<div class="marketplace">
						<cms:pageSlot position="SiteLogo" var="logo" limit="1">
							<cms:component component="${logo}" />
						</cms:pageSlot>
						<div class="mobile-bag bag">
						<!-- TISPRD-32-fix -->
							<a href="/store/mpl/en/cart">(<span class="responsive-bag-count"></span>)</a>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="marketplace-checkout">
						<c:if test="${empty hideLogo}">
							<cms:pageSlot position="SiteLogo" var="logo" limit="1">
								<cms:component component="${logo}" />
							</cms:pageSlot>
						</c:if>
					</div>
				</c:otherwise>
			</c:choose>



			<nav>
				<ul>
					<c:if test="${empty showOnlySiteLogo }">

						<cms:pageSlot position="NavigationBar" var="component">
							<li><cms:component component="${component}" /></li>
						</cms:pageSlot>

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
					<a href="/store/mpl/en/cart" class="mini-cart-link myBag-sticky"
						data-mini-cart-url="/store/mpl/en/cart/rollover/MiniCart"
						data-mini-cart-refresh-url="/store/mpl/en/cart/miniCart/SUBTOTAL"
						data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
						style="position: static;"><spring:theme code="minicart.mybag" />&nbsp;(<span
						class="js-mini-cart-count-hover"></span>) </a>
				</c:if>
			</div>
		</div>
		<div class="compact-toggle mobile"></div>
	</div>

	<a id="skiptonavigation"></a>
	<nav:topNavigation />
</header>

<c:if test="${empty showOnlySiteLogo }">
	<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"
		class="container">
		<cms:component component="${component}" />
	</cms:pageSlot>
</c:if>

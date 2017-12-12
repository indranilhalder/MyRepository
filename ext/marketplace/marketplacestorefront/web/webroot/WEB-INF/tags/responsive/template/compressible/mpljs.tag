<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- plugins --%>

<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/enquire.min.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/Imager.min.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.colorbox-min.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.form.min.js"></script>
	<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.cookie.min.js"></script>
	
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/matchMedia.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/matchMedia.addListener.js"></script>

<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.blockUI-2.66.js"></script>

<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.hoverIntent.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.pstrength.custom-1.2.0.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.syncheight.custom.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.tabs.custom.js"></script>

<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.zoom.custom.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/owl.carousel.custom.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/tree.jquery.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/smk-accordion.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.elevatezoom.js"></script>
	<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.visible.js"></script>
<%-- Custom ACC JS --%>

<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.address.js"></script> --%>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.autocomplete.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.micrositeautocomplete.js"></script>	
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.carousel.js"></script>

<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/cart')}">
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.cart.js"></script>
	<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.pickupinstore.js"></script>
</c:if>
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/checkout*') || fn:contains(requestScope['javax.servlet.forward.request_uri'],'/cart')}">
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.cartitem.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.checkout.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.checkoutsteps.js"></script>
</c:if>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.colorbox.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.common.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.forgottenpassword.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.global.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.imagegallery.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.langcurrencyselector.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.minicart.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.navigation.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.product.js"></script>

<%--<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.sellerDetails.js"></script>	--%>
	
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/p')}">
	<c:if test="${fn:contains(themeResourcePath,'theme-luxury')}">
    	<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.productDetail.js"></script>
	</c:if>
</c:if>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.quickview.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.ratingstars.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.refinements.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.tabs.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.track.js"></script>

<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.nowtrending.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/_autoload.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/plugins/jquery.picZoomer.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.brandcomponent.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.collapsible.panel.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.newWishlist.js"></script>

<%-- <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/c')}"> --%>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.comparenow.js"></script>
<%-- </c:if> --%>

<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.paginationsize.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.paginationsort.js"></script> 
	
<%-- for account address page --%>
<script>
	var resourcePath = "${commonResourcePath}";
</script>
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/contact')}">
<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.contactus.js"></script>
</c:if>

<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.click2call.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.click2chat.js"></script>
	
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.helpmeshop.js"></script>


<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/myInterest') or 
			  fn:contains(requestScope['javax.servlet.forward.request_uri'],'/myStyleProfile')}">

<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.myrecommendation.js"></script>
</c:if>	
<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.signinflyout.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/jquery.lazyload.min.js"></script>	
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl-primary/tisl.home.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.search.js"></script>

<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/reviews')}">
    <script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.reviewrating.js"></script>
</c:if> 
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.feedback.js"></script>
	
	<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/tealium.js"></script>
	

	<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.dtm.js"></script> --%>

<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" med="all" href="${themeResourcePath}/css/main-ie8.css"/>
<script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/html5shiv.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/respond.js"></script>
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/jquery.backstretch.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/background-size-shim.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/jquery.html5-placeholder-shim.js"></script>
<![endif]-->

<!-- Search feed back End -->


<%-- <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/update-profile') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/update-password') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/update-parsonal-detail') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/checkCurrentPassword') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'update-nickName') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/address-book') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/populateAddressDetail') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/addNewAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/editAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/set-default-address/*') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/remove-address/*') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/payment-details') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/remove-payment-method') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/orders') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/order/') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnRequest') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnSuccess') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/cancelSuccess') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/friendsInvite') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/inviteFriends') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/marketplace-preference') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/saveMplPreferences') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/my-account/') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/my-account')}">
	<script type="text/javascript" 
		src="${commonResourcePath}/js/mpl/acc.accountaddress.js"></script>
</c:if> --%>



<%-- AddOn JavaScript files --%>
<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
	<script type="text/javascript" src="${addOnJavaScript}"></script>
</c:forEach>

<!-- js entry for "Other sellers page" -->
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/viewSellers')}">
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.sellerDetails.js"></script>
</c:if> 
<!-- Js entry for store finder -->
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/store-finder')}">
 <script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.storefinder.js"></script>

</c:if>
<c:if test="${isIAEnabled}">
<script type="text/javascript"
	src="${commonResourcePath}/js/ia-plugins/ia_plugin_general.js" defer="defer"></script>
<script type="text/javascript" src="${commonResourcePath}/js/ia-plugins/tataia.js" defer="defer"></script>
</c:if>

<%-- <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/address-book') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/populateAddressDetail') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/addNewAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/editAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/set-default-address/*') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/remove-address/*') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/orders') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/order/*') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnRequest') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnSuccess') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/cancelSuccess')}">
	<script type="text/javascript"
		src="${commonResourcePath}/js/acc.accountpagination.js"></script>
</c:if> --%>

<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/feedback.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/smart-app-banner.js"></script>
	
<!--[if lt IE 9]>
<script type="text/javascript" src="${commonResourcePath}/js/html5shiv.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/respond.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.backstretch.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/background-size-shim.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.html5-placeholder-shim.js"></script>
<![endif]-->

<!-- Fix for defect TISPT-202 -->
<!-- TISPT-202  -->
<c:if test="fn:contains(requestScope['javax.servlet.forward.request_uri'],'/delivery-method/check')}">
	<script src="https://maps.googleapis.com/maps/api/js?v=3&amp;"></script>

<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/marketplacecheckoutaddon.js"></script>	
</c:if> 

<!--Track order PopUp and Detail page   -->
<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.trackorder.js"></script>

<!--Returns Page   -->
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.returns.js"></script>

<!--Single page  -->
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/checkout/single')}">
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.singlePageCheckout.js"></script>	
</c:if>
<!-- Web form TPR-5989 -->
<script type="text/javascript" src="${commonResourcePath}/js/mpl/simpleUpload.min.js"></script>	
<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.webform.js"></script>	



<c:if test="${fn:contains(themeResourcePath,'theme-luxury')}">
	<script type="text/javascript" src="${themeResourcePath}/js/lib/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/lib/jquery-ui-1.11.2.min.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/lib/jquery.selectBoxIt.min.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/lib/slick.min.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/lib/jquery.elevatezoom.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/lib/bootstrap.min.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/lib/jquery.validate.min.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/main.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/acc.accountaddress.js"></script>
	<script type="text/javascript" src="${themeResourcePath}/js/acc.productDetail.js"></script>
</c:if>

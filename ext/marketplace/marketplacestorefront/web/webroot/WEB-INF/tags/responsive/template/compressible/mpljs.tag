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
	src="${commonResourcePath}/js/mpl/matchMed.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/mpl/matchMed.addListener.js"></script>

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
	src="${commonResourcePath}/js/plugins/jquery.elevatezoom.js"></script>
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
    <script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.productDetail.js"></script>
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
	src="${commonResourcePath}/js/mpl/tisl.home.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.search.js"></script>


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


<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/update-profile') or
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
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/saveMplPreferences')}">
	<script type="text/javascript" 
		src="${commonResourcePath}/js/mpl/acc.accountaddress.js"></script>
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
		src="${commonResourcePath}/js/mpl/acc.accountpagination.js"></script>
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
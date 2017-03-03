<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<script>

var ussidArray=[];
var index = -1;
var seq = -1;
var mrp = '${product.productMRP}';
var currency='${product.productMRP.currencyIso}';
var mrpValue = '${product.productMRP.formattedValue}';
var sellersList = '${product.seller}';
var productCode = '${product.code}';
var buyboxskuId='';  
 /* $( document ).ready(function() { 		
	fetchPrice();		
 }); */
  
 
	
</script>

<!-- Displaying different tabs in PDP page -->
<!-- About Product, reviewsAndRatings and returnsAndRefunds Tab added for jewellery change  -->
<c:set var="validTabs" value="${VALID_TABS}" />
<div class="nav-wrapper ${product.rootCategory}">
	<ul class="nav pdp">
	    <c:if test="${fn:contains(validTabs, 'aboutproduct')}">
			<li id="tabs_aboutProduct" class="active">
				<spring:theme code="product.product.aboutProduct" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'stylenote')}">
			<li id="tabs_styleNotes" class="active">
				 <spring:theme code="product.product.styleNotes" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'reviewsAndRatings')}">
			<li id="tabs_styleNotes">
				 <spring:theme code="product.product.reviewsAndRatings" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
			<li id="tabs_styleNotes">
				 <spring:theme code="product.product.returnsAndRefunds" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'details')}">
			<li id="tabs_details" >
				 <spring:theme code="product.product.details" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'description')}">
			<li id="tabs_description" class="active">
				<spring:theme code="product.product.description" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'warranty')}">
			<li id="tabs_warranty">
				<spring:theme code="product.product.warranty" />
			</li>
		</c:if>
			<c:if test="${fn:contains(validTabs, 'knowmore')}">
			<li id="tabs_knowmore">
				<spring:theme code="product.product.knowmore" />
			</li>
		</c:if>
	</ul>
</div>
<ul class="tabs pdp">
	<c:if test="${fn:contains(validTabs, 'aboutproduct')}">
		<li id="about" class="tab-content active">
			<product:productAboutProductTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li id="style" class="tab-content active">
			<product:productStyleNotesTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'reviewsAndRatings')}">
		<li id="review" class="tab-content">
			<product:productReveiwsAndRatings product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
		<li id="return" class="tab-content">
			<product:productReturnsAndRefunds product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'details')}">
		<li id="details" class="tab-content">
			<product:productDetailsTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'description')}">
		<li id="description" class="tab-content active">
			<product:productDescriptionTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'warranty')}">
		<li id="warranty" class="tab-content">
			<product:productWarrantyTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'knowmore')}">
		<li id="knowmore" class="tab-content">
			<product:productTataPromiseTab product="${product}" />
		</li>
	</c:if>	
</ul>

 <div id="servicableUssid"></div>

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

<c:set var="validTabs" value="${VALID_TABS}" />
<section class="pdp-accordion accordion mt-40">
<div class="nav-wrapper">
<!-- <ul class="nav pdp"> -->

	<c:if test="${fn:contains(validTabs, 'details')}">
		<div class="accordion-title active">
			<h4>
				<spring:theme code="product.product.features" />
			</h4><i class="accordion-icon"></i>
			</div>
		</c:if>
	<c:if test="${fn:contains(validTabs, 'details')}">
		<div class="accordion-content">
			${product.articleDescription}
			<product:productDetailsTab product="${product}" />
		</div>
	</c:if>
	
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<div class="accordion-title">
			<h4>
				 <spring:theme code="product.product.styleNotes" />
			</h4><i class="accordion-icon"></i>
		</div>
	</c:if>
		<c:if test="${fn:contains(validTabs, 'stylenote')}">
			<div class="accordion-content">
				<product:productStyleNotesTab product="${product}" />
			</div>
		</c:if>
	
	
		<c:if test="${fn:contains(validTabs, 'warranty')}">
			<div class="accordion-title">	
			<h4>
				<spring:theme code="product.product.warranty" />
			</h4><i class="accordion-icon"></i>
				</div>
		</c:if>
	
		
		<c:if test="${fn:contains(validTabs, 'warranty')}">
			<div class="accordion-content">
				<product:productWarrantyTab product="${product}" />
			</div>
		</c:if>
	
		<c:if test="${fn:contains(validTabs, 'knowmore')}">
			<div class="accordion-title">	
			<h4>
				<spring:theme code="product.product.returns" />
			</h4><i class="accordion-icon"></i>
				</div>
		</c:if>

		
		<c:if test="${fn:contains(validTabs, 'knowmore')}">
		<div class="accordion-content">
			<product:productTataPromiseTab product="${product}" />
			</div>
		</c:if>
	
	
<!-- </ul> -->
</div>
<ul class="tabs pdp">
	<!-- INC144313814 fix start -->
	
	
 
	<!-- INC144313814 fix end -->



</ul>
</section>
 <div id="servicableUssid"></div>

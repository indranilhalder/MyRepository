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
<div class="nav-wrapper">
<ul class="nav pdp">
<!-- 	TISPRD-7604 fix start -->
<%-- 	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li id="tabs_styleNotes" class="active">
			 <spring:theme code="product.product.styleNotes" />
		</li>
	</c:if> --%>
<!-- 	TISPRD-7604 fix end -->
<!-- commented as part of PRDI-96 start -->
	<%-- <c:if test="${fn:contains(validTabs, 'details')}">
		<li id="tabs_details" class="active">
			 <spring:theme code="product.product.details" />
		</li>
	</c:if> --%>
	<!-- commented as part of PRDI-96 end -->
<!-- 	TISPRD-7604 fix start -->
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li id="tabs_styleNotes" class="active">			<!-- added class 'active' PRDI-96 -->
			 <spring:theme code="product.product.styleNotes" />
		</li>
	</c:if>
<!-- 	TISPRD-7604 fix end -->
<!-- moved as part of PRDI-96 start -->
<c:if test="${fn:contains(validTabs, 'details')}">
		<li id="tabs_details">
			 <spring:theme code="product.product.details" />
		</li>
	</c:if>
<!-- moved as part of PRDI-96 end -->

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
	<!-- INC144313814 fix start -->
	<!-- commented as part of PRDI-96 start -->
	<%-- <c:if test="${fn:contains(validTabs, 'details')}">
		<li class="active">
			<product:productDetailsTab product="${product}" />
		</li>
	</c:if> --%>
	<!-- commented as part of PRDI-96 end -->
	<!-- INC144313814 fix end -->
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li  class="active">				<!-- added class 'active' PRDI-96 -->
			<product:productStyleNotesTab product="${product}" />
		</li>
	</c:if>
	<!-- moved as part of PRDI-96 start -->
	<c:if test="${fn:contains(validTabs, 'details')}">
		<li>
			<product:productDetailsTab product="${product}" />
		</li>
	</c:if>
	<!-- moved as part of PRDI-96 end -->
	<c:if test="${fn:contains(validTabs, 'description')}">
		<li class="active">
			<product:productDescriptionTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'warranty')}">
		<li >
			<product:productWarrantyTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'knowmore')}">
		<li >
			<product:productTataPromiseTab product="${product}" />
		</li>
	</c:if>
</ul>

 <div id="servicableUssid"></div>

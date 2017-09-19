<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="luxProduct" tagdir="/WEB-INF/tags/responsive/product"%>

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
				<div id="details">
					<product:productDetailsTab product="${product}" />
				</div>
			</div>
		</c:if>

		<div class="accordion-title review-accordion">
			<h4>Rating & Reviews</h4> - <div id="ratingDiv"></div><i class="accordion-icon"></i>
		</div>
		<div class="accordion-content full-box review-accordion-content">
			<div id='commentsDiv'></div>
		</div>

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
				<product:luxProductTataPromiseTab product="${product}" />
			</div>
		</c:if>

		<div class="accordion-title emi-header" id="emiStickerId" style="display: none;">
			<h4>EMI Options</h4><i class="accordion-icon"></i>
		</div>
		<div class="accordion-content full-box">

			<div class="table">
				<input id="prodPrice" type="hidden" />
				<div class="emibox-left" id="bankNameForEMI"></div>
				<div class="emibox-right">
					<h5 class="emifselectbank">Please select a bank</h5>
					<table id="EMITermTable" class="other-sellers-table emi-table">
						<thead id="emiTableTHead" style="display:none">
						<th width="5%"><spring:theme
								code="checkout.multi.paymentMethod.addPaymentDetails.terms" /></th>
						<th width="25%"><spring:theme
								code="checkout.multi.paymentMethod.addPaymentDetails.interestRate" /></th>
						<th width="35%"><spring:theme
								code="checkout.multi.paymentMethod.addPaymentDetails.monthlyInstallment" /></th>
						<th width="35%"><spring:theme
								code="checkout.multi.paymentMethod.addPaymentDetails.interestPayable" /></th>
						</thead>

						<tbody id="emiTableTbody">
						<span id="emiSelectBank" style="display:none"><spring:theme code="emi.nobankselect"/></span>
						<span id="emiNoData" style="display:none"><spring:theme code="emi.nodata"/></span>
						</tbody>
					</table>
				</div>
			</div>
		</div>


		<!-- </ul> -->
	</div>
	<ul class="tabs pdp">
		<!-- INC144313814 fix start -->

		<!-- INC144313814 fix end -->
	</ul>
</section>
<!-- <label class="js-add-to-cart btn btn-default btn-lg btn-block visible-xs" for="addToCartButton">Add to Bag</label> -->
<div id="servicableUssid"></div>

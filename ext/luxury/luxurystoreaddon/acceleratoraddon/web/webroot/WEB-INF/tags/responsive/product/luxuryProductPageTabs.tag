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
	
	<div class="accordion-title">	
			<h4>Rating & Reviews</h4><i class="accordion-icon"></i>
	</div>					
	<div class="accordion-content full-box">	
		<div class="colum4 left">
			<table class="table design1">
			  <tr>
				   <td width="60%">	
				     <span class="rating-count">5</span>			   
					 <span class="rating">					   
					   <span class="full"></span><span class="full"></span><span class="full"></span><span class="full"></span><span class="full"></span>
					 </span>
					</td>
				   <td width="40%"></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">4</span>			   
					 <span class="rating">					   
					   <span class="full"></span><span class="full"></span><span class="full"></span><span class="full"></span><span class="half"></span>
					 </span>
					</td>
				   <td></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">3</span>			   
					 <span class="rating">					   
					   <span class="full"></span><span class="full"></span><span class="full"></span><span class="empty"></span><span class="empty"></span>
					 </span>
					</td>
				   <td></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">2</span>			   
					<span class="rating">					   
					   <span class="full"></span><span class="full"></span><span class="empty"></span><span class="empty"></span><span class="empty"></span>
					 </span>
					</td>
				   <td></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">1</span>			   
					 <span class="rating">					   
					   <span class="full"></span><span class="empty"></span><span class="empty"></span><span class="empty"></span><span class="empty"></span>
					 </span>
					</td>
				   <td></td>
			  </tr>
			</table>
		</div>
		<div class="table colum3 right box-size">
		    <h4>3.5 <span class="rating">					   
					   <span class="full"></span><span class="empty"></span><span class="empty"></span><span class="empty"></span><span class="empty"></span>
					 </span>
			</h4>
			<p>Great product. Qulity and switching was good and fit as expected,. This full sleeve
tees become one of my favourite too. Thank you CLIQ and style shell for this amazing Tees.</p>
            <span>by Bradley Oliver 22 May 2016</span>
		</div>
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
			<product:productTataPromiseTab product="${product}" />
			</div>
		</c:if>
		
		<div class="accordion-title emi-header">	
			 <h4>EMI Options</h4><i class="accordion-icon"></i>
			</div>					
			<div class="accordion-content full-box">
				
                <div class="table">
                   <div class="emibox-left" id="bankNameForEMI"></div>
                   <div class="emibox-right" id="emiTableTbody"></div>
                </div>
			</div>
	
	
<!-- </ul> -->
</div>
<ul class="tabs pdp">
	<!-- INC144313814 fix start -->
	
	
 
	<!-- INC144313814 fix end -->



</ul>
</section>
 <div id="servicableUssid"></div>

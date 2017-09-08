<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
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
<c:choose>
<c:when test="${product.rootCategory =='HomeFurnishing'}">
<div class="nav-wrapper">
<ul class="nav pdp productNav">
<!-- INC144315154 start -->
<%-- 	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li id="tabs_styleNotes" class="active">
			 <spring:theme code="product.product.styleNotes" />
		</li>

	</c:if> --%>
<!-- 	TISPRD-7604 fix end -->
<!-- commented as part of PRDI-96 start -->
	<%-- <c:if test="${fn:contains(validTabs, 'details')}">
		<li id="tabs_details" class="active">

	</c:if>
<!-- INC144315154 end -->
	<c:if test="${fn:contains(validTabs, 'details')}">
		<li id="tabs_details">

			 <spring:theme code="product.product.details" />
		</li>

	</c:if> --%>
	<!-- commented as part of PRDI-96 end -->
	 <c:if test="${fn:contains(validTabs, 'aboutproduct')}">
			<li id="tabs_aboutProduct" class="active">
				<spring:theme code="product.product.aboutProduct" />
			</li>
		</c:if>
		<!--CKD:TPR-6804:Start -->
	<c:if test="${fn:contains(validTabs, 'overview')}">
		<li id="tabs_productOverview" class="active"><spring:theme
				code="product.product.overview" /></li>
	</c:if>
	<!--CKD:TPR-6804:End -->
<!-- 	TISPRD-7604 fix start -->
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li id="tabs_styleNotes">			<!-- added class 'active' PRDI-96 -->
			 <spring:theme code="product.product.styleNotes" />
		</li>
	</c:if>
<!-- 	TISPRD-7604 fix end -->
	<c:if test="${fn:contains(validTabs, 'reviewsAndRatings')}">
			<li id="tabs_review">
				 <spring:theme code="product.product.reviewsAndRatings" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
			<li id="tabs_styleNotes_Refunds">
				 <spring:theme code="product.product.returnsAndRefunds" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'Returns')}">
			<li id="tabs_ret">
				 <spring:theme code="product.product.returns" />
			</li>
		</c:if>
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
		<!--CKD:TPR-250:Start -->
	<c:if test="${fn:contains(validTabs, 'brandInfo')}">
		<li id="tabs_brandInfo"><spring:theme
				code="product.product.brandInfo" /></li>
	</c:if>
	<!--CKD:TPR-250:End -->
</ul>
</div>
<ul class="tabs pdp productTabs">
	<!-- INC144313814 fix start -->
	<!-- commented as part of PRDI-96 start -->
	<%-- <c:if test="${fn:contains(validTabs, 'details')}">
		<li class="active">
			<product:productDetailsTab product="${product}" />
		</li>
	</c:if> --%>
	<!-- commented as part of PRDI-96 end -->
	<!-- INC144313814 fix end -->
	<c:if test="${fn:contains(validTabs, 'aboutproduct')}">
		<li id="about" class="tab-content active">
			<product:productAboutProductTab product="${product}" />
		</li>
	</c:if>
	<!--CKD:TPR-TPR-6804:Start -->
	<c:if test="${fn:contains(validTabs, 'overview')}">
		<li class="active">
			<product:productOverviewTab product="${product}" />
		</li>
	</c:if>
	<!--CKD:TPR-TPR-6804:End -->
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li>				<!-- added class 'active' PRDI-96 -->
			<product:productStyleNotesTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'reviewsAndRatings')}">
		<li id="review" class="tab-content">
			<product:productReveiwsAndRatings product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
		<li id="returnrefund" class="tab-content">
			<product:productReturnsAndRefunds product="${product}" />
		</li>
	</c:if>
	<%-- <c:if test="${fn:contains(validTabs, 'Returns')}">
		<li id="return" class="tab-content">
			<product:productReturns product="${product}" />
		</li>
	</c:if> --%>
	<!-- moved as part of PRDI-96 start -->
	<c:if test="${fn:contains(validTabs, 'details')}">
		<li>
			<product:productDetailsTab product="${product}" />
		</li>
	</c:if>
	<!-- moved as part of PRDI-96 end -->
	<!-- INC144313814 fix end -->
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
	<!--CKD:TPR-250:Start -->
	<c:if test="${fn:contains(validTabs, 'brandInfo')}">
		<li >
			<product:brandInfoTab product="${product}" />
		</li>
	</c:if>
	<!--CKD:TPR-250:End -->
</ul>
</c:when>
<c:otherwise>
<div class="nav-wrapper">
<ul class="nav pdp productNav">
<!-- INC144315154 start -->
<%-- 	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li id="tabs_styleNotes" class="active">
			 <spring:theme code="product.product.styleNotes" />
		</li>

	</c:if> --%>
<!-- 	TISPRD-7604 fix end -->
<!-- commented as part of PRDI-96 start -->
	<%-- <c:if test="${fn:contains(validTabs, 'details')}">
		<li id="tabs_details" class="active">

	</c:if>
<!-- INC144315154 end -->
	<c:if test="${fn:contains(validTabs, 'details')}">
		<li id="tabs_details">

			 <spring:theme code="product.product.details" />
		</li>

	</c:if> --%>
	<!-- commented as part of PRDI-96 end -->
	 <c:if test="${fn:contains(validTabs, 'aboutproduct')}">
			<li id="tabs_aboutProduct" class="active">
				<spring:theme code="product.product.aboutProduct" />
			</li>
		</c:if>
		<!--CKD:TPR-6804:Start -->
	<c:if test="${fn:contains(validTabs, 'overview')}">
		<li id="tabs_productOverview"><spring:theme
				code="product.product.overview" /></li>
	</c:if>
	<!--CKD:TPR-6804:End -->
<!-- 	TISPRD-7604 fix start -->
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li id="tabs_styleNotes" class="active">			<!-- added class 'active' PRDI-96 -->
			 <spring:theme code="product.product.styleNotes" />
		</li>
	</c:if>
<!-- 	TISPRD-7604 fix end -->
	<c:if test="${fn:contains(validTabs, 'reviewsAndRatings')}">
			<li id="tabs_review">
				 <spring:theme code="product.product.reviewsAndRatings" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
			<li id="tabs_styleNotes_Refunds">
				 <spring:theme code="product.product.returnsAndRefunds" />
			</li>
		</c:if>
		<c:if test="${fn:contains(validTabs, 'Returns')}">
			<li id="tabs_ret">
				 <spring:theme code="product.product.returns" />
			</li>
		</c:if>
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
		<!--CKD:TPR-250:Start -->
	<c:if test="${fn:contains(validTabs, 'brandInfo')}">
		<li id="tabs_brandInfo"><spring:theme
				code="product.product.brandInfo" /></li>
	</c:if>
	<!--CKD:TPR-250:End -->
</ul>
</div>
<ul class="tabs pdp productTabs">
	<!-- INC144313814 fix start -->
	<!-- commented as part of PRDI-96 start -->
	<%-- <c:if test="${fn:contains(validTabs, 'details')}">
		<li class="active">
			<product:productDetailsTab product="${product}" />
		</li>
	</c:if> --%>
	<!-- commented as part of PRDI-96 end -->
	<!-- INC144313814 fix end -->
	<c:if test="${fn:contains(validTabs, 'aboutproduct')}">
		<li id="about" class="tab-content active">
			<product:productAboutProductTab product="${product}" />
		</li>
	</c:if>
	<!--CKD:TPR-TPR-6804:Start -->
	<c:if test="${fn:contains(validTabs, 'overview')}">
		<li>
			<product:productOverviewTab product="${product}" />
		</li>
	</c:if>
	<!--CKD:TPR-TPR-6804:End -->
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<li class="active">				<!-- added class 'active' PRDI-96 -->
			<product:productStyleNotesTab product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'reviewsAndRatings')}">
		<li id="review" class="tab-content">
			<product:productReveiwsAndRatings product="${product}" />
		</li>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
		<li id="returnrefund" class="tab-content">
			<product:productReturnsAndRefunds product="${product}" />
		</li>
	</c:if>
	<%-- <c:if test="${fn:contains(validTabs, 'Returns')}">
		<li id="return" class="tab-content">
			<product:productReturns product="${product}" />
		</li>
	</c:if> --%>
	<!-- moved as part of PRDI-96 start -->
	<c:if test="${fn:contains(validTabs, 'details')}">
		<li>
			<product:productDetailsTab product="${product}" />
		</li>
	</c:if>
	<!-- moved as part of PRDI-96 end -->
	<!-- INC144313814 fix end -->
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
	<!--CKD:TPR-250:Start -->
	<c:if test="${fn:contains(validTabs, 'brandInfo')}">
		<li >
			<product:brandInfoTab product="${product}" />
		</li>
	</c:if>
	<!--CKD:TPR-250:End -->
</ul>
</c:otherwise>
</c:choose>

<%-- UF-377 starts --%>
<c:choose>
<c:when test="${product.rootCategory =='HomeFurnishing'}">
<div class="product-specification-accordion smk_accordion acc_with_icon">
 	<c:if test="${fn:contains(validTabs, 'overview')}">
		<div  id="overViewMobile" class="choose-address accordion_in acc_active">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.overview" /></h2>
			</div>
			<div class="acc_content" id="overviewAccordion" style="display:block">
				<product:productOverviewTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<div  id="styleNotesMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.styleNotes" /></h2>
			</div>
			<div class="acc_content" id="stylenoteAccordion">
				<product:productStyleNotesTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'details')}">
		<div id="detailsMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.details" /></h2>
			</div>
			<div class="acc_content" id="detailsAccordion">
				<product:productDetailsTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'description')}">
		<div id="descriptionMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.description" /></h2>
			</div>
			<div class="acc_content" id="descriptionAccordion">
				<product:productDescriptionTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'warranty')}">
		<div id="warrantyMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.warranty" /></h2>
			</div>
			<div class="acc_content" id="warrantyAccordion">
				<product:productWarrantyTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'knowmore')}">
		<div id="knowmoreMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.knowmore" /></h2>
			</div>
			<div class="acc_content" id="knowmoreAccordion">
				<product:productTataPromiseTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'brandInfo')}">
		<div id="brandInfoMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.brandInfo" /></h2>
			</div>
			<div class="acc_content" id="brandInfoAccordion">
				<product:brandInfoTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
		<div id="returnsAndRefundsMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.returnsAndRefunds" /></h2>
			</div>
			<div class="acc_content" id="returnRefundAccordion">
				<product:productReturnsAndRefunds product="${product}" />
			</div>
		</div>
	</c:if>
	<c:choose>
		<c:when test="${product.rootCategory=='Electronics'  || product.rootCategory=='Watches'}">
			<div class="choose-address accordion_in specMob">
				<div class="acc_head">
					<div class="acc_icon_expand"></div>
					<h2>Specification</h2>
				</div>
				<div class="acc_content">
					<c:choose>
						<c:when test="${product.rootCategory=='Watches'}">
						<%-- <c:if test="${not empty product.classifications}">
							 <div class="view-button">Check The Specs</div>
						</c:if> --%>
						<!-- <div class="hide-button" style="display:none;">Hide Specifications</div> -->
						 <div class="product-classifications wrapper">
							<c:if test="${not empty product.classifications}">
								<table class="stats-table">
									<tbody>
												<tr style="background-color: #f0f4f5;">
												<td colspan='2' style="font-weight: 700;"><div
														class="headline">Functions and Features</div></td>
											</tr>
												<c:if test="${not empty mapConfigurableAttributes }">
													<c:forEach var="classification"
														items="${mapConfigurableAttributes}">
														<tr style="border: 1px solid #f0f4f5;">
															<td style="border-right: 1px solid #f0f4f5;" class="title">
																<%-- ${outer.index} - ${inner.index} --%>
																${classification.key}
															</td>
															<td><c:choose>
																	<c:when test="${not empty classification.value }">
																		<c:forEach var="classValue" items="${classification.value }">
						   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}
						   						 		 </c:forEach>
																	</c:when>
																	<c:otherwise>
						   						 ${classification.key}
						   						</c:otherwise>
																</c:choose></td>
														</tr>
													</c:forEach>
												</c:if>
									</tbody>
								</table>
								 
							</c:if>
						</div>
						</c:when> 
						<c:otherwise>
							<c:if test="${not empty product.classifications}">
							<table class="electronics-accordian-tabs pdp specTabs">
					        		<c:forEach items="${product.classifications}" var="classification" varStatus="outer">
										<tr style="background-color: #f0f4f5;"><td colspan="2" style="font-weight: 700;"><h3>${classification.name}</h3></td></tr>
										<c:forEach items="${classification.features}" var="feature" varStatus="inner">										
										<c:forEach items="${feature.featureValues}" var="value"
														varStatus="status">
												<tr>
												<td style="border-right: 1px solid #f0f4f5;" class="title">${feature.name}</td>
												<td>
															${value.value}
															<c:choose>
															<c:when test="${feature.range}">
																	${not status.last ? '-' : feature.featureUnit.symbol}
																</c:when>
															<c:otherwise>
																	${feature.featureUnit.symbol}
																	${not status.last ? '<br/>' : ''}
																</c:otherwise>
														</c:choose>
													</td>
												</tr>	
												</c:forEach>
										</c:forEach>
									</c:forEach>
					         
					         </table>
							</c:if>
						</c:otherwise>
					</c:choose>	
				</div>
			</div>
		</c:when>
		<c:otherwise>
		</c:otherwise>
		</c:choose>
</div>
</c:when>
<c:otherwise>
<div class="product-specification-accordion smk_accordion acc_with_icon">
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<div  id="styleNotesMobile" class="choose-address accordion_in acc_active">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.styleNotes" /></h2>
			</div>
			<div class="acc_content" id="stylenoteAccordion" style="display:block">
				<product:productStyleNotesTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'details')}">
		<div id="detailsMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.details" /></h2>
			</div>
			<div class="acc_content" id="detailsAccordion">
				<product:productDetailsTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'description')}">
		<div id="descriptionMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.description" /></h2>
			</div>
			<div class="acc_content" id="descriptionAccordion">
				<product:productDescriptionTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'warranty')}">
		<div id="warrantyMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.warranty" /></h2>
			</div>
			<div class="acc_content" id="warrantyAccordion">
				<product:productWarrantyTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'knowmore')}">
		<div id="knowmoreMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.knowmore" /></h2>
			</div>
			<div class="acc_content" id="knowmoreAccordion">
				<product:productTataPromiseTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'brandInfo')}">
		<div id="brandInfoMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.brandInfo" /></h2>
			</div>
			<div class="acc_content" id="brandInfoAccordion">
				<product:brandInfoTab product="${product}" />
			</div>
		</div>
	</c:if>
	<c:if test="${fn:contains(validTabs, 'returnsAndRefunds')}">
		<div id="returnsAndRefundsMobile" class="choose-address accordion_in">
			<div class="acc_head">
				<div class="acc_icon_expand"></div>
				<h2><spring:theme code="product.product.returnsAndRefunds" /></h2>
			</div>
			<div class="acc_content" id="returnRefundAccordion">
				<product:productReturnsAndRefunds product="${product}" />
			</div>
		</div>
	</c:if>
	<c:choose>
		<c:when test="${product.rootCategory=='Electronics'  || product.rootCategory=='Watches'}">
			<div class="choose-address accordion_in specMob">
				<div class="acc_head">
					<div class="acc_icon_expand"></div>
					<h2>Specification</h2>
				</div>
				<div class="acc_content">
					<c:choose>
						<c:when test="${product.rootCategory=='Watches'}">
						<%-- <c:if test="${not empty product.classifications}">
							 <div class="view-button">Check The Specs</div>
						</c:if> --%>
						<!-- <div class="hide-button" style="display:none;">Hide Specifications</div> -->
						 <div class="product-classifications wrapper">
							<c:if test="${not empty product.classifications}">
								<table class="stats-table">
									<tbody>
												<tr style="background-color: #f0f4f5;">
												<td colspan='2' style="font-weight: 700;"><div
														class="headline">Functions and Features</div></td>
											</tr>
												<c:if test="${not empty mapConfigurableAttributes }">
													<c:forEach var="classification"
														items="${mapConfigurableAttributes}">
														<tr style="border: 1px solid #f0f4f5;">
															<td style="border-right: 1px solid #f0f4f5;" class="title">
																<%-- ${outer.index} - ${inner.index} --%>
																${classification.key}
															</td>
															<td><c:choose>
																	<c:when test="${not empty classification.value }">
																		<c:forEach var="classValue" items="${classification.value }">
						   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}
						   						 		 </c:forEach>
																	</c:when>
																	<c:otherwise>
						   						 ${classification.key}
						   						</c:otherwise>
																</c:choose></td>
														</tr>
													</c:forEach>
												</c:if>
									</tbody>
								</table>
								 
							</c:if>
						</div>
						</c:when> 
						<c:otherwise>
							<c:if test="${not empty product.classifications}">
							<table class="electronics-accordian-tabs pdp specTabs">
					        		<c:forEach items="${product.classifications}" var="classification" varStatus="outer">
										<tr style="background-color: #f0f4f5;"><td colspan="2" style="font-weight: 700;"><h3>${classification.name}</h3></td></tr>
										<c:forEach items="${classification.features}" var="feature" varStatus="inner">										
										<c:forEach items="${feature.featureValues}" var="value"
														varStatus="status">
												<tr>
												<td style="border-right: 1px solid #f0f4f5;" class="title">${feature.name}</td>
												<td>
															${value.value}
															<c:choose>
															<c:when test="${feature.range}">
																	${not status.last ? '-' : feature.featureUnit.symbol}
																</c:when>
															<c:otherwise>
																	${feature.featureUnit.symbol}
																	${not status.last ? '<br/>' : ''}
																</c:otherwise>
														</c:choose>
													</td>
												</tr>	
												</c:forEach>
										</c:forEach>
									</c:forEach>
					         
					         </table>
							</c:if>
						</c:otherwise>
					</c:choose>	
				</div>
			</div>
		</c:when>
		<c:otherwise>
		</c:otherwise>
		</c:choose>
</div>
</c:otherwise>
</c:choose>
<%-- UF-377 ends --%>
 <div id="servicableUssid"></div>


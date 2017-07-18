<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!--  CKD:TPR-250:Start-->
<c:set var="qStringPart" value ="?sellerId="/>
<c:set var="msiteSllerId" value =""/>
<c:if test="${ not empty param.sellerId}">
	<c:set  var="msiteSllerId"  value="${qStringPart}${param.sellerId}"/>
</c:if>
<!--  CKD:TPR-250:End-->

				<form:form
					action="${request.contextPath}/p/${product.code}/viewSellers" id="sellerForm"
					method="post">
					<product:sellerForm></product:sellerForm>
					
					<input type="hidden" maxlength="10" size="1" id="sellersSkuListId"
						name="sellersSkuListId" value="" />
					<input type="hidden" maxlength="10" size="1" id="sellerSkuId"
						name="sellerSkuId" value="" />
					<input type="hidden" maxlength="10" size="1" id="skuIdsWithNoStock"
						name="skuIdsWithNoStock" value="" />
					<input type="hidden" maxlength="10" size="1" id="skuIdForED"
						name="skuIdForED" value="" />
					<input type="hidden" maxlength="10" size="1" id="skuIdForHD"
						name="skuIdForHD" value="" />	
					<!--  Added for click-and-collect for view seller -->
					<input type="hidden" maxlength="10" size="1" id="skuIdForCNC"
						name="skuIdForCNC" value="" />
						<input type="hidden" maxlength="10" size="1" id="skuIdForCod"
						name="skuIdForCod" value="" />	

				
					<button type="submit" name="submit" id="submit" class="otherSellersFont">
					<%-- <button type="submit" name="submit" id="submit"
						class="otherSellersFont">
						<p id="otherSellerInfoId" class="other-sellers" style="display: none"><a>
							<span class="other-sellers-link">
							<span id="otherSellersId"></span>&nbsp;
							<span><spring:theme code="product.othersellers"></spring:theme></span></span>&nbsp;
							<spring:theme code="product.available"></spring:theme>
							&nbsp;<span id="minPriceId"></span>
						</a></p>
						<div id="otherSellerLinkId" style="display: none">
							<span id="otherSellersId"></span>&nbsp;<span
								class="other-sellers-info" style="color: #a9143c;"><spring:theme
									code="product.othersellersForNoStock"></spring:theme></span>&nbsp;
						</div>
					</button> --%>
					<!-- UF-32 -->
					<!--  CKD:TPR-250:Start-->
					<%-- <a href="${request.contextPath}/p/${product.code}/viewSellers" id="submit" class="otherSellersFont"> --%>
					<a href="${request.contextPath}/p/${product.code}/viewSellers${msiteSllerId}" id="submit" class="otherSellersFont">
					<!--  CKD:TPR-250:End-->
						<p id="otherSellerInfoId" class="other-sellers">&&nbsp;<span class="other-sellers-link"><span id="otherSellersId"></span>&nbsp;<span><spring:theme code="product.othersellers"></spring:theme></span></span><%-- &nbsp;
							<spring:theme code="product.available"></spring:theme>
							&nbsp;<span id="minPriceId"></span> --%>
						</p>   	<!-- TISSQAUATS-881 -->
						<%-- <div id="otherSellerLinkId" style="display: none">
							<span id="otherSellersId"></span>&nbsp;<span
								class="other-sellers-info" style="color: #a9143c;"><spring:theme
									code="product.othersellersForNoStock"></spring:theme></span>&nbsp;

						</div> --%>
						</a>
					</button>
					

					
				</form:form>
				<div id="othersSellerDivId" style="display: none">
					<table border="1">
						<thead>
							<tr>
								<th style="width: 200px" align="center"><spring:theme
										code="product.sellersname"></spring:theme></th>

								<th style="width: 100px" align="center"><spring:theme
										code="product.mop"></spring:theme></th>

								<th style="width: 100px" align="center"><spring:theme
										code="product.stock"></spring:theme></th>
							</tr>
						</thead>

						<tbody id="sellerListId">
						</tbody>

					</table>

					</div>

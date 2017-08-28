<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!-- details of the Product Details tab -->

<div class="tab-details">
	<ycommerce:testId code="productDetails_content_label">
		<ul>
  			<input type="hidden" value="${productCategoryType}" id="productCategoryType"/>
  			<c:choose>
  				<c:when test="${'Clothing' eq product.rootCategory || 'Footwear' eq product.rootCategory || 'Accessories' eq product.rootCategory}">
  					<c:forEach var="classification" items="${mapConfigurableAttributes}">
				   		<c:choose>
   							<c:when test="${not empty classification.value }">
   						 		<%-- <li> ${classification.key} -
   						 		<c:forEach var="classValue" items="${classification.value }">
   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}</li>
   						 		 </c:forEach> --%>
								<!--TISPRD-9399 START-->
   								<c:choose>
  									<c:when test="${'Accessories' eq product.rootCategory && (fn:containsIgnoreCase(classification.key, 'Feature1')||fn:containsIgnoreCase(classification.key, 'Feature2')||fn:containsIgnoreCase(classification.key, 'Feature3'))}">
  										<li>
   						 		<c:forEach var="classValue" items="${classification.value }">
   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}</li>
   						 		 </c:forEach>
  									</c:when>
  									<c:otherwise>
  										<li> ${classification.key} -
   						 		<c:forEach var="classValue" items="${classification.value }">
   						 			${classValue.key} &nbsp;&nbsp;${classValue.value}</li>
   						 		 </c:forEach>
  									</c:otherwise>
  								</c:choose>
								<!--TISPRD-9399 END -->
   							</c:when>
   						<c:otherwise>
   						<li> ${classification.key}</li>
   						</c:otherwise>	
   						</c:choose>
					</c:forEach>
  				</c:when>
  				<c:otherwise>
  					<c:forEach var="classification" items="${mapConfigurableAttribute}">
				   		<li>${classification.key} - ${classification.value}</li>
					</c:forEach>
  				</c:otherwise>
  			</c:choose>
  				
  			
  			<%-- <c:forEach var="classification" items="${mapConfigurableAttribute}">
				   <c:choose>
   					<c:when test="${not empty classification.value }">
   						 <li> ${classification.key} - ${classification.value.key} ${classification.value.value}</li>
   					</c:when>
   					<c:otherwise>
   						 <li> ${classification.key}</li>
   					</c:otherwise>	
   				</c:choose>
			</c:forEach> --%>
  				<li><spring:theme code="product.listing.id"></spring:theme>${product.code}</li>
  		</ul>
	</ycommerce:testId>
</div>
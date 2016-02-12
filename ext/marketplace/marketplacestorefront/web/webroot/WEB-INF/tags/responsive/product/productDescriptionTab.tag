<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Details for Product Description tab -->

<div class="tab-details">
	<ycommerce:testId code="productStyleNotes_content_label">
	   <c:if test="${not empty product.articleDescription}">
		<%-- <ul style="list-style-type:square">
  			<li>${product.articleDescription}</li>
  		</ul>
  		<li><spring:theme code="product.listing.id"></spring:theme>${product.code}</li> --%>
  		${product.articleDescription}
  		</c:if>
  		 <%-- <c:choose>
		 <c:when test="${not empty mapConfigurableAttribute}">
			<b>Key Features:</b>
		    </c:when>
		    <c:otherwise>
		    </c:otherwise>
		 </c:choose> --%>
  		<ul>
  		
  			<c:forEach var="classification" items="${mapConfigurableAttribute}">
				   <li>${classification.key} - ${classification.value}</li>
			</c:forEach>
  			<%-- <li><spring:theme code="product.listing.id"></spring:theme>${product.code}</li> --%>
  		</ul>
  		
	</ycommerce:testId>
</div>
<!-- PDP changes end -->


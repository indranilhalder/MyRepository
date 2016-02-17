<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<!-- details of the Product Details tab -->

<div class="tab-details">
	<ycommerce:testId code="productDetails_content_label">
		<ul>
  			<%-- <li>${product.brand.brandname}</li> --%>
  			<c:forEach var="classification" items="${mapConfigurableAttribute}">
				   <li>${classification.key} - ${classification.value}</li>
			</c:forEach>
  				<li><spring:theme code="product.listing.id"></spring:theme>${product.code}</li>
  		</ul>
	</ycommerce:testId>
</div>
<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Details of style notes tab -->
<div class="tab-details">
	<%-- <ycommerce:testId code="productDetails_content_label"> --%> 
	<ycommerce:testId code="productStyleNotes_content_label">
		<ul style="list-style:none">
  			<li>${product.articleDescription}</li>
  		</ul>
	</ycommerce:testId>
</div>

<!-- PDP changes end -->


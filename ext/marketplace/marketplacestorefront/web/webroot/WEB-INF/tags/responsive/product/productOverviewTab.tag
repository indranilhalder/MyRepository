<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Details of Overview tab -->

<ul style="list-style: none; padding-left: 0;">
	<div class="product-desc">
		<span class="key-label"> 
		<c:forEach var="overViewSection"
				items="${overviewTabSeq}">
				<%-- <c:forEach var="classification" items="${mapConfigurableAttributes}"> --%>
				<c:set var="list" value="${mapConfigurableAttributes[overViewSection]}" />
					<ul class="homefurnishing-overview">
					<c:if test="${not empty list}">
						<li class="homefurnishing-overview-title">
							${overViewSection}</li>
							</c:if>
						<c:forEach var="classValue" items="${mapConfigurableAttributes[overViewSection] }">
							<li class="homefurnishing-overview-desc">${classValue}</li>
						</c:forEach>
					</ul>
				<%-- </c:forEach> --%>
			</c:forEach> <%-- <li class="stylenote" itemprop="description">${product.articleDescription}</li> --%>
		</span>
	</div>
</ul>


<!-- </div> -->

<!-- PDP changes end  -->

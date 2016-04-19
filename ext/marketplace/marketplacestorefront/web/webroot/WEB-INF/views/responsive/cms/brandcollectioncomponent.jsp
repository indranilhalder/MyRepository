<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>	

<!-- <div id="navigation" style="margin-top: -2px;"></div> -->


	<c:if test ="${component.uid eq 'ShopByBrandComponent' }">
	<div class="toggle"><span><spring:theme code="navigation.department.shopBy" /></span>
	
	<span><span><spring:theme code="navigation.brand.shopByBrand" /></span></div>
	</c:if>
	
		
			<ul>

				<c:forEach items="${brandComponentCollection}" var="brand">
					<li><cms:component component="${brand}" evaluateRestriction="true" /></li>

				</c:forEach>
			</ul>
			

		<!-- </div> -->
		
	



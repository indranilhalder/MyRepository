<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- <footer id="footerByAjaxId"> -->
<footer>
<%-- <c:if test="${!fn:containsIgnoreCase(cmsPage.name, 'Cart Page')}"> --%>
<div class="callouts">
					  <div class="Padd">
						<ul>
						  <li> <p>TATA Promise</p> </li>
						  <li class="omni"> <p>Omni Channel</p> </li>
						  <li class="genuine"> <p>Genuine Brands</p> </li>
						  <li class="returns"> <p>30 Day Returns</p> </li>
						</ul>
					  </div>
					</div>
					<%-- </c:if> --%>

	<div id="footerByAjaxId"></div>
	<!-- <footer> -->
	<%-- 	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}"/>
	</cms:pageSlot> --%>
	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
</footer>

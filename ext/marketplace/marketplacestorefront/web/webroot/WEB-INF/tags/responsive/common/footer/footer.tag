<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<!-- <footer id="footerByAjaxId"> -->
<footer>
	<div id="footerByAjaxId"></div>
	<!-- <footer> -->
	<%-- 	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}"/>
	</cms:pageSlot> --%>
	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
</footer>

<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<footer>
<%-- <c:if test="${!fn:containsIgnoreCase(cmsPage.name, 'Cart Page')}"> --%>
<div class="callouts">
					  <div class="Padd">
						<ul>
						  <li> <p>Tata Trust</p> </li>
						  <li class="omni"> <p>CLiQ and PIQ</p> </li>
						  <li class="genuine"> <p>Authentic Brands</p> </li>
						  <li class="returns"> <p>Easy Returns</p> </li>
						</ul>
					  </div>
					</div>
					<%-- </c:if> --%>
	<!-- SEO TPR-5733 -->
	<c:if test="${fn:length(footerLinkList) gt 0}">
	<div id="footerLink">
		<c:set var="rowcount" value="-1"></c:set>
		<c:forEach items="${footerLinkList}" var="footerLinkRow">
			<div class="column">
				<ul>
					<c:forEach items="${footerLinkRow.value}" var="footerlinkColumnObj">
						<c:choose>
							<c:when test="${footerlinkColumnObj.key eq 0}">
								<li class="header"><a href="${footerlinkColumnObj.value.footerLinkURL}"><b>${footerlinkColumnObj.value.footerLinkName}</b></a></li>
							</c:when>
							<c:otherwise>
								<li class="node"><a href="${footerlinkColumnObj.value.footerLinkURL}">${footerlinkColumnObj.value.footerLinkName}</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
		</c:forEach>
	</div>
</c:if>
	<div id="footerByAjaxId"></div>
	<%-- 	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}"/>
	</cms:pageSlot> --%>
	<cms:pageSlot position="Footer" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
</footer>
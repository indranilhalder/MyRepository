
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<div class="brands">
<div class="wrapper">
	<c:if test="${not empty component.title }">
		<h2>${component.title}</h2>
	</c:if>

	<c:if test="${component.imageSize eq 'LARGE'}">
		<ul class="feature-brands">
			<c:forEach items="${component.brandList}" var="brandList">

				<li class="call-to-action-parent"><c:url var="subBrandUrl" value="${brandList.subBrandUrl}"></c:url>
				<c:choose>
				<c:when test="${not empty  subBrandUrl}">
					<a href="${subBrandUrl}"><img class="image  call-to-action-banner"
						src="${brandList.subBrandImage.URL}"  alt="${brandList.subBrandImage.altText}" />
                        <c:if test="${not empty component.text }">
						<span class="brand-subdesc call-to-action-name" >${component.text}</span>
						</c:if>
						<!-- logo added for TISPRD-1348  -->
						 <c:if test="${not empty brandList.subBrandLogo.URL }">
						 <img class="logo" src="${brandList.subBrandLogo.URL }" />
						 </c:if>
						  <c:if test="${not empty brandList.subBrandName}">
						  <span class="link-copy call-to-action-link"><b>SHOP ${brandList.subBrandName}</b></span> 
						  </c:if>
						 
						</a>
						</c:when>
						<c:otherwise>
						<img class="image call-to-action-banner"
						src="${brandList.subBrandImage.URL}"  alt="${brandList.subBrandImage.altText}" />
                        <c:if test="${not empty component.text }">
						<span class="brand-subdesc call-to-action-name">${component.text}</span>
						</c:if>
						<!-- logo added for TISPRD-1348  -->
						 <c:if test="${not empty brandList.subBrandLogo.URL }">
						 <img class="logo" src="${brandList.subBrandLogo.URL }" />
						 </c:if>
						  <c:if test="${not empty brandList.subBrandName}">
						  <span class="link-copy call-to-action-link"><b>SHOP ${brandList.subBrandName}</b></span> 
						  </c:if>
						</c:otherwise>
						</c:choose>
						</li>

			</c:forEach>
		</ul>
	</c:if>


	<c:if test="${component.imageSize eq 'THUMBNAIL'}">
		<ul class="more-brands">
			<c:forEach items="${component.brandList}" var="brandList">
				<li class="call-to-action-parent"><c:url var="subBrandUrl" value="${brandList.subBrandUrl}"></c:url>
					<a href="${subBrandUrl}"><img class="image call-to-action-banner" src="${brandList.subBrandImage.URL}" alt="${brandList.subBrandImage.altText}" />
					 <c:if test="${not empty component.text }">
					<span class="brand-subdesc call-to-action-name">${component.text}</span>
					</c:if>
					<c:if test="${not empty brandList.subBrandLogo.URL }">
						 <img class="logo" src="${brandList.subBrandLogo.URL }" />
						 </c:if>
					 <c:if test="${not empty brandList.subBrandName}">
						  <span class="link-copy call-to-action-link"><b>SHOP ${brandList.subBrandName}</b></span> 
						  </c:if>
					
					</a></li>
			</c:forEach>

		</ul>
	</c:if>
</div>
</div>



			
	
			
			

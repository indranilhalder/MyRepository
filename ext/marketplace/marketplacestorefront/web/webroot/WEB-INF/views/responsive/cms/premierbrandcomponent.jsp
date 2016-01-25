
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
		<h1>${component.title}</h1>
	</c:if>

	<c:if test="${component.imageSize eq 'LARGE'}">
		<ul class="feature-brands">
			<c:forEach items="${component.brandList}" var="brandList">

				<li><c:url var="subBrandUrl" value="${brandList.subBrandUrl}"></c:url>
					<a href="${subBrandUrl}"><img class="image"
						src="${brandList.subBrandImage.URL}" />

						<span class="brand-subdesc">${component.text}</span>
						  <span class="link-copy"><b>SHOP ${brandList.subBrandName}</b></span> 
						</a>
						</li>

			</c:forEach>
		</ul>
	</c:if>


	<c:if test="${component.imageSize eq 'THUMBNAIL'}">
		<ul class="more-brands">
			<c:forEach items="${component.brandList}" var="brandList">
				<li><c:url var="subBrandUrl" value="${brandList.subBrandUrl}"></c:url>
					<a href="${subBrandUrl}"><img class="logo"
						src="${brandList.subBrandImage.URL}" /></a></li>
			</c:forEach>

		</ul>
	</c:if>
</div>
</div>



			
	
			
			

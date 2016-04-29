<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<template:page pageTitle="${pageTitle}">
	<div class="container r2-sitemap" style="color:#4a4a4a;">
		<h1 class="r2-center">Site Map</h1>
		<c:forEach var="megamap" items="${megaMap}">
			<section>
				<h4 class="r2-borderBottom">
					<a href="#">${megamap.key.name} </a>
				</h4>
				<ul>
				<c:forEach var="l2MegaMap" items="${megamap.value}">
						<li>
							<ul>
								<h5 class="toggle"><c:url var="l2Url" value="/${l2MegaMap.key.name}/c-${l2MegaMap.key.code}"/><a href="${l2Url}">${l2MegaMap.key.name}</a></h5>
								<c:forEach var="l3MegaMap" items="${l2MegaMap.value}">
								<c:url
								value="/${l3MegaMap.name}/c-${l3MegaMap.code}"
								var="l3Url" />
								<li><a href="${l3Url}">${l3MegaMap.name}</a></li>
								</c:forEach>
							</ul>
						</li>
				</c:forEach>
				</ul>
			</section>
		</c:forEach>
	</div>
</template:page>



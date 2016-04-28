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
<style>
.r2-borderBottom {
    border-bottom: 2px solid #b9b9b9;
}
.r2-center{   
    text-align: center;
    float: left;
    width: 100%;
    margin:20px 0;
    color: #333333 !important;
}
.r2-sitemap {
    /* font-family: 'Avenir Medium'; */
}
.r2-sitemap section {
    width: 95%;
    margin: auto;
    border-color: #f3f3f3;
}
.r2-sitemap > section > ul > li{
    width: 20%;
    float: left;
}

.r2-sitemap > section > ul {
    height: auto;
    overflow: hidden;
    padding: 20px;
}
.r2-sitemap > section > ul > li > ul > h5 {
    text-transform: uppercase;
    white-space: nowrap;
    width: 90%;
    overflow: hidden;
    text-overflow: ellipsis;
        font-size: 12px;
        margin-top: 10px;
    margin-bottom: 10px;
        font-weight: 500;
}
.r2-sitemap > section > ul > li > ul li a {
	line-height: 18px;
	font-size: 12px;
}
.r2-sitemap section h4 {
    padding-bottom: 5px;
    /* font-family: 'Avenir Medium'; */
    font-style: 24px;
}
.r2-sitemap > section > ul > li > ul > li {
    padding: 4px 0px;
    font-size: 12px;
}
.r2-sitemap h1 {
    font-size: 36px;
    /* font-family: 'Avenir Medium'; */
}
.r2-sitemap a{
    color: #4A4A4A;
}
.r2-borderBottom {
    font-size: 18px;
    margin-bottom: 10px;
}
</style>
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
								<h5 class="toggle">${l2MegaMap.key.name}</h5>
								<c:forEach var="l3MegaMap" items="${l2MegaMap.value}">
								<li><a href="${l3MegaMap.code}">${l3MegaMap.name}</a></li>
								</c:forEach>
							</ul>
						</li>
				</c:forEach>
				</ul>
			</section>
		</c:forEach>
	</div>
</template:page>



<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<style>
.home-brands-you-love-carousel-brands {
    display: inline-block;
    width: 13.5%;
	padding: 0 10px;
    box-sizing: border-box;
}
.home-brands-you-love-carousel-brands img,.home-brands-you-love-side-image img,.home-brands-you-love-main-image img{
max-width:100%;
vertical-align: middle;
}
.home-brands-you-love-desc {
    text-align: center;
}
.home-brands-you-love-side-image {
    width: 25%;
    display: inline-block;
	padding:0 10px;
    box-sizing: border-box;
}
.home-brands-you-love-side-image.left {
vertical-align:bottom;
}
.home-brands-you-love-side-image.right {
	vertical-align:top;
    padding-top: 50px;
}
.home-brands-you-love-main-image {
    width: 48%;
    display: inline-block;
	padding: 0 10px;
    box-sizing: border-box;
	    vertical-align: bottom;
}
p.home-brands-you-love-main-image-heading {
    text-align: center;
    color: indianred;
	font-size: 14px;
}
p.home-brands-you-love-main-image-desc {
    text-align: center;
	font-size: 14px;
}
.home-brands-you-love-main-image-wrapper {
    position: relative;
}
.visit-store-wrapper{
position: absolute;
bottom:20px;
text-align: center;
width:100%;
}
.home-brands-you-love-main-image-wrapper .text{
    font-size: 24px;
    color: white;
    font-weight: 500;
}
.home-brands-you-love-main-image-wrapper .visit-store {
    background: #fff;
    border: none;
    height: 40px;
    width: 150px;
    text-transform: uppercase;
    font-size: 10px;
    font-weight: 600;
    letter-spacing: 1.5px;
}

.home-brands-you-love-wrapper h1{
text-align: center;
    font-size: 24px;
}

.home-brands-you-love-carousel-brands.active img{
border-bottom: 1px solid;
    padding: 5px;
}

/*best picks start*/
#bestPicks h1{
	padding: 40px 0;
    font-weight: 500;
    text-align: center;
    font-size: 24px;
}

#bestPicks .home-best-pick-carousel-img img{
	width: 100%;
}

#bestPicks .short-info span {
    font-size: 16px;
    font-family: "Avenir Next";
    font-weight: 400;
    text-align: center;
    line-height: 1;
    display: inline-block;
    padding-bottom: 3px;
    width: 100%;
}
#bestPicks .view-cliq-offers {
padding: 15px;
    border: 1px solid #a9143c;
    color: #a9143c;
    font-size: 12px;
    font-family: "Avenir Next";
    font-weight: 600;
	    margin: 15px auto;
    text-transform: uppercase;
    display: block;
    width: 200px;
    text-align: center;
}
/*best picks end*/
</style>

<template:page pageTitle="${pageTitle}">

	<div class="no-space homepage-banner">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</div>

	<div class="shop-promos">
		<ul class="promos">
			<cms:pageSlot position="Section2A" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
			<cms:pageSlot position="Section2B" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
		</ul>
	</div>
	<cms:pageSlot position="Section2C" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
	
	<div id="brandsYouLove" class="home-brands-you-love-wrapper"></div>
	<div id="bestPicks" class=""></div>
	
	<!-- For Infinite Analytics Start -->
	<div class="brands" id="ia_brands_favorites"></div>
	<div class="feature-categories" id="ia_categories_favorites"></div>
	<div class="feature-collections" id="ia_collections"></div>
	<div class="trending" id="ia_products_hot"></div>
	<!-- For Infinite Analytics End -->

	<cms:pageSlot position="Section5" var="feature" element="div"
		class="span-24 section5 cms_disp-img_slot promise">
		<cms:component component="${feature}" />
	</cms:pageSlot>

</template:page>


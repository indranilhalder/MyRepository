<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
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
	<div id="brandsYouLove" class="home-brands-you-love-wrapper feature-collections"></div>
	<div id="promobannerhomepage" class="buy-banner"></div>
	<div id="bestPicks" class="feature-collections"></div>
	<div id="productYouCare" class="feature-collections"></div>
	<!-- <div class="feature-categories" id="ia_categories_favorites"></div> -->
	<div class="feature-collections">
	<div id="stayQued" class="qued"></div>
	<div id="newAndExclusive" class=""></div>
	</div>
	<!-- For Infinite Analytics Start -->
	<!-- <div class="brands" id="ia_brands_favorites"></div> -->
	<!-- <div class="feature-categories" id="ia_categories_favorites"></div>
	<div class="feature-collections" id="ia_collections"></div> -->
	
	
	
	
	
	

















<div class="best_seller stw-list">
    <div class="yCmsComponent best_seller_section hide_clplist">
        <div class="content">Best Sellers</div>
    </div>
    <!-- <div class="Menu">
        <div class="mobile selectmenu">SWEATSHIRTS</div>
        <ul style="display: none;">
            <li class="">ALL</li>
            <li class="">MEN</li>
            <li class="">WOMEN</li>
            <li class="">ELECTRONICS</li>
            <li class="">FOOTWEAR</li>
        </ul>
    </div> -->
    <div class="yCmsComponent best_seller_section hide_clplist">
        <h1>ALL</h1>
        <div class="carousel-component">
        
<div class="trending" id="stw_widget"></div>

        </div>
    </div>
    <div class="yCmsComponent best_seller_section hide_clplist">
        <h1>MEN</h1>
        <div class="carousel-component">
        
<div class="trending" id="stw_widget"></div>

        </div>
    </div>
    <div class="yCmsComponent best_seller_section hide_clplist">
        <h1>WOMEN</h1>
        <div class="carousel-component">
        
<div class="trending" id="stw_widget"></div>

        </div>
    </div>
    <div class="yCmsComponent best_seller_section hide_clplist">
        <h1>ELECTRONICS</h1>
        <div class="carousel-component">
<div class="trending" id="stw_widget"></div>
        </div>
    </div>
    <div class="yCmsComponent best_seller_section hide_clplist">
        <h1>FOOTWEAR</h1>
        <div class="carousel-component">
<div class="trending" id="stw_widget"></div>
        </div>
    </div>
    <div class="yCmsComponent best_seller_section hide_clplist">
    </div>
</div>
















			

				

				

				

				











	
	
	
	
	
	
	
	
	
	<div class="trending" id="ia_products_hot"></div>
	<!-- For Infinite Analytics End -->
	<div id="showcase" class="showcase feature-collections"></div>
    <!-- Store Locator  -->
	 <cms:pageSlot position="Section7" var="feature" element="div">
		<cms:component component="${feature}" />
     </cms:pageSlot>
</template:page>

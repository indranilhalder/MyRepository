<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>

		<section class="lux-main-banner-slider text-center text-uppercase">
			<div class="banner-list">
				<div class="banner-img">
					<picture> <cms:pageSlot position="Brand-HeroBanner"
						var="HeroBanner" element="div"
						class="span-24 section5 cms_disp-img_slot">
						<cms:component component="${HeroBanner}" />
					</cms:pageSlot></picture>
				</div>
			</div>
		</section>

		<section class="shop-by-catagory text-center">
			<cms:pageSlot position="Brand-CategoryCurosel" var="CategoryCurosel" element="div"
				class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${CategoryCurosel}" />
			</cms:pageSlot>
			
				<cms:pageSlot position="Brand-ShowCase1" var="ShowCase1" element="div"
				class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${ShowCase1}" />
				</cms:pageSlot>
		
			<div class="lux-blp-carousel luxgender-carousel">
				<cms:pageSlot position="Brand-ProductCurosel1" var="feature" element="div"
				class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</section>
		
		
		
			 <cms:pageSlot position="Brand-ProductCurosel2" var="ProductCurosel2" element="div"
		class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${ProductCurosel2}" />
		</cms:pageSlot> 
		

		
		<div class="look-book">
			<div class="look-book-img"> 
				<div class="look-book-list clearfix">
					<div class="colmn">
						<ul class="list-unstyled clearfix">
							<cms:pageSlot position="Brand-MoreStoriesbanner1Col1" var="MoreStoriesbanner1Col1" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<li><cms:component component="${MoreStoriesbanner1Col1}" /></li>
							</cms:pageSlot>					
						</ul>
					</div>
					<div class="colmn">
						<ul class="list-unstyled clearfix">
							<cms:pageSlot position="Brand-MoreStoriesbanner1Col2" var="MoreStoriesbanner1Col2" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<li><cms:component component="${MoreStoriesbanner1Col2}" /></li> 
							</cms:pageSlot>					
						</ul>
					</div>
				</div>
			</div>
			
			<div class="look-book-img"> 
				<div class="look-book-list clearfix">
					<div class="colmn">
						<ul class="list-unstyled clearfix">
							<cms:pageSlot position="Brand-MoreStoriesbanner2Col1" var="MoreStoriesbanner2Col1" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<li><cms:component component="${MoreStoriesbanner2Col1}" /></li>
							</cms:pageSlot>					
						</ul>
					</div>
					<div class="colmn">
						<ul class="list-unstyled clearfix">
						<li>
							<cms:pageSlot position="Brand-MoreStoriesbanner2Col2" var="MoreStoriesbanner2Col2" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<li><cms:component component="${MoreStoriesbanner2Col2}"/> </li>
							</cms:pageSlot>					
						</ul>
					</div>
				</div>
			</div>
				<cms:pageSlot position="Brand-PopularCategory" var="feature" element="div"
		class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${feature}" />
		</cms:pageSlot>
		</div>
	
		
</template:page>
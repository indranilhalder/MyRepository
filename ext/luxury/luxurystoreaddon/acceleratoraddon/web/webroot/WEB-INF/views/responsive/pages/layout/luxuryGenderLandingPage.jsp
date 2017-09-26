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
				<picture> <cms:pageSlot position="Gender-HeroBanner"
										var="HeroBanner" element="div"
										class="span-24 section5 cms_disp-img_slot">
					<cms:component component="${HeroBanner}" />
				</cms:pageSlot></picture>


			</div>
		</div>
	</section>

<%-- 
<section class="seastion-sec">
	<ul class="clearfix list-unstyled">
		<li>
			<cms:pageSlot position="Gender-LeftContentSlot" var="feature" element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			
		</li>
		<li>
			<cms:pageSlot position="Gender-RightContentSlot" var="feature"
				element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			
		</li>
	</ul>
</section> --%>


	<section class="shop-by-catagory text-center">
		<cms:pageSlot position="Gender-CategoryCurosel" var="CategoryCurosel" element="div"
					  class="span-24 section5 cms_disp-img_slot">
			<cms:component component="${CategoryCurosel}" />
		</cms:pageSlot>
	</section>

	<section class="brand-slider-wrapper text-center">
		<cms:pageSlot position="Gender-WeeklyBanner1" var="WeeklyBanner1"
					  element="div" class="span-24 section5 cms_disp-img_slot">
			<cms:component component="${WeeklyBanner1}" />
		</cms:pageSlot>
	</section>
		<section class="brand-slider-wrapper text-center">
		<cms:pageSlot position="Gender-WeeklyBanner2" var="WeeklyBanner2"
					  element="div" class="span-24 section5 cms_disp-img_slot">
			<cms:component component="${WeeklyBanner2}" />
		</cms:pageSlot>
	</section>

	<cms:pageSlot position="Gender-ShortlookBook" var="feature" class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${feature}" />
	</cms:pageSlot>

	<cms:pageSlot position="Gender-ShowCase1" var="ShowCase1" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${ShowCase1}" />
	</cms:pageSlot>

	<cms:pageSlot position="Gender-ShowCase2" var="ShowCase2" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${ShowCase2}" />
	</cms:pageSlot>

	<section class="new-brand-slider-wrapper text-center">
		<cms:pageSlot position="Gender-WeeklyBanner2" var="WeeklyBanner2" element="div"
					  class="span-24 section5 cms_disp-img_slot">
			<cms:component component="${WeeklyBanner2}" />
		</cms:pageSlot>
	</section>



	<section class="luxgender-carousel shop-by-catagory text-center">
		<cms:pageSlot position="Gender-ProductCurosel1" var="ProductCurosel1" element="div"
					  class="span-24 section5 cms_disp-img_slot">
			<cms:component component="${ProductCurosel1}" />
		</cms:pageSlot>
	</section>
	
		<section class="luxgender-carousel shop-by-catagory text-center">
		<cms:pageSlot position="Gender-ProductCurosel2" var="ProductCurosel1" element="div"
					  class="span-24 section5 cms_disp-img_slot">
			<cms:component component="${ProductCurosel1}" />
		</cms:pageSlot>
	</section>


	
		<cms:pageSlot position="Gender-MoreStoriesbanner1Col1" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
		<cms:pageSlot position="Gender-MoreStoriesbanner1Col2" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
		<cms:pageSlot position="Gender-MoreStoriesbanner1Col3" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
		<cms:pageSlot position="Gender-MoreStoriesbanner1Col4" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
	<cms:pageSlot position="Gender-MoreStoriesbanner1Col5" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
	<cms:pageSlot position="Gender-MoreStoriesbanner1Col4" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
	<cms:pageSlot position="Gender-MoreStoriesbanner1Col5" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
		<cms:pageSlot position="Gender-MoreStoriesbanner1Col6" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
	<cms:pageSlot position="Gender-MoreStoriesbanner1Col7" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>
		<cms:pageSlot position="Gender-MoreStoriesbanner1Col8" var="component" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${component}" />
	</cms:pageSlot>

	<%--
        <div class="look-book">
            <div class="look-book-img">
                <div class="look-book-list clearfix">
                    <div class="colmn">
                        <ul class="list-unstyled clearfix">
                            </li>
                            </cms:pageSlot>
                        </ul>
                    </div>
                    <div class="colmn">
                        <ul class="list-unstyled clearfix">
                            <cms:pageSlot position="Gender-MoreStoriesbanner1Col2" var="MoreStoriesbanner1Col2" element="div"
                            class="span-24 section5 cms_disp-img_slot">
                            <li><cms:component component="${MoreStoriesbanner1Col2}" /></li>
                            </cms:pageSlot>
                        </ul>
                    </div>
                </div>
            </div>
        </div>--%>

	<cms:pageSlot position="Gender-PopularCategory" var="feature" element="div"
				  class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${feature}" />
	</cms:pageSlot>

</template:page>
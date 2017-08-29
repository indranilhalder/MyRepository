<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
   <div class="common_logo_slot">
   <div class='common_logo_slot_wrapper'>
   <c:set var="brandCode" value="${searchCode}" scope="session" /> 
  	<c:set var="categoryName" value="${dropDownText}" scope="session" />
   <cms:pageSlot position="CommonLogoParaSlot" var="feature">
		<cms:component component="${feature}" element="div" class="blp_title_banner" />
	</cms:pageSlot>
	</div>
	</div>
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	<div class="men_women_sec">
	<cms:pageSlot position="ForHimForHerSlot" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	</div>
	<div class="top_categories_blp">
	<cms:pageSlot position="TopCategoryHeadingSlot" var="feature">
		<cms:component component="${feature}" element="div" class="top_categories_section_blp"/>
	</cms:pageSlot>
	<div class="top_categories_wrapper_blp">
		<div class="top_categories_section_blp">
			<cms:pageSlot position="TopCategoryCol1Slot" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
		<div class="top_categories_section_blp sub_categories_blp">
			<cms:pageSlot position="TopCategoryCol2Slot" var="feature">
				<cms:component component="${feature}" element="div" class="top_categories_section_blp"/>
			</cms:pageSlot>
		</div>
		<div class="top_categories_section_blp">
			<cms:pageSlot position="TopCategoryCol3Slot" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
		<div class="top_categories_section_blp sub_categories_blp">
			<cms:pageSlot position="TopCategoryCol4Slot" var="feature">
				<cms:component component="${feature}" element="div" class="top_categories_section_blp"/>
			</cms:pageSlot>
		</div>
	</div>
	</div>
	<div class="featured_collection">
				 <cms:pageSlot position="Section2D" var="feature">
					<cms:component component="${feature}" element="div" class="featured_collection_section"/>
				</cms:pageSlot>
				</div>
	<div class="blp_top_brands">
	<cms:pageSlot position="TopBrandSlot" var="feature">
		<cms:component component="${feature}" element="div" class="blp_top_brands_section"/>
	</cms:pageSlot>
	</div>
	<%-- <div class="style_edit_blp">
		<cms:pageSlot position="Section2C" var="feature">
			<cms:component component="${feature}" element="div" class=""/>
		</cms:pageSlot>
	</div> --%>
	<div class="style_edit_blp">
		<div class="style_edit_left_blp">
			<cms:pageSlot position="StyleEditLeftColSlot" var="feature">
				<cms:component component="${feature}" element="div" class=""/>
			</cms:pageSlot>
		</div>
		<div class="style_edit_right_blp">
			<cms:pageSlot position="StyleEditRightColSlot" var="feature">
				<cms:component component="${feature}" element="div" class=""/>
			</cms:pageSlot>
		</div>
	</div>		
	<%-- <div class="shop_the_look">
		<cms:pageSlot position="Section2B" var="feature">
			<cms:component component="${feature}" element="div" class="shop_the_look_section"/>
		</cms:pageSlot>
	</div> --%>
	<div class="shop_the_look">
		<cms:pageSlot position="ShopForLookHeadingSlot" var="feature">
			<cms:component component="${feature}" element="div" class="shop_the_look_section"/>
		</cms:pageSlot>
			<cms:pageSlot position="ShopForLookRightColSlot" var="feature">
				<cms:component component="${feature}" element="div" class="shop_the_look_section shop_the_look_right"/>
			</cms:pageSlot>
		<div class="shop_the_look_left">
			<cms:pageSlot position="ShopForLookLeftColSlot" var="feature">
				<cms:component component="${feature}" element="div" class="shop_the_look_section"/>
			</cms:pageSlot>
		</div>
		<!-- <div class="shop_the_look_right"> -->
		
		<!-- </div> -->
	</div>
				
	<%-- <div class="top_deal_blp">
			<cms:pageSlot position="Section3" var="feature">
				<cms:component component="${feature}"  />
			</cms:pageSlot>
	</div> --%>
	
	<div class="best-offers_blp">
				 <cms:pageSlot position="BestOfferSection" var="feature">
					<cms:component component="${feature}" element="div" class="" />
				</cms:pageSlot>
				</div>
	
	<div class="top_deal_blp">
			<cms:pageSlot position="TopDealsSlot" var="feature">
				<cms:component component="${feature}"  class="top_deal_carousel_blp" />
			</cms:pageSlot>
			<div class="blog_container_blp">
				<div class='blog_feature_blp'>
					<cms:pageSlot position="TopDealsLeftColSlot" var="feature">
						<cms:component component="${feature}" class="top_deal_left_col_blp"  />
					</cms:pageSlot>
				</div>
				<div class='blog_feature_blp'>
					<cms:pageSlot position="TopDealsRightColSlot" var="feature">
						<cms:component component="${feature}"  class="top_deal_right_col_blp" />
					</cms:pageSlot>
				</div>
			</div>
	</div>
	
	
	
<div id="stw_widget_blp" class="lazy-reached-stw"></div>
	
	<div class="shop_for_blp">
		<cms:pageSlot position="ShopForHeading" var="feature">
				<cms:component component="${feature}" element="div" class="shop_for_component_blp"/>
		</cms:pageSlot>
		<div class="shop_for_wrapper">
		<div class="shop_for_left_wrapper_blp">
			<cms:pageSlot position="ShopForLeftCol" var="feature">
				<cms:component component="${feature}" element="div" class="shop_for_component_blp"/>
			</cms:pageSlot>
		</div>
		<div class="shop_for_links_blp">
			<cms:pageSlot position="ShopForRightCol" var="feature">
				<cms:component component="${feature}" element="div" class="shop_for_component_blp"/>
			</cms:pageSlot>
		</div>
		</div>
		<cms:pageSlot position="ShopForViewMore" var="feature">
				<cms:component component="${feature}" element="div" class="shop_for_component_blp"/>
		</cms:pageSlot>
	</div>

	
	<div id="productGrid" class="listing wrapper"> <!-- Added for TPR-198 -->
			<!-- <div class="left-block"> -->
			<cms:pageSlot position="Section4A" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			<!-- </div> -->
			<cms:pageSlot position="Section4B" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			</div>
	<product:productCompare/>		
	<!-- For Infinite Analytics Start -->
	<div class="trending" id="ia_products_new"></div>
	<!-- For Infinite Analytics End -->
</template:page>

<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- TPR-430 -->
<input type="hidden" id="product_category" value="${product_category}"/>

<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="CommonLogoParaSlot" var="feature">
		<cms:component component="${feature}" element="div" class="clp_title_banner" />
	</cms:pageSlot>
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	<%-- <cms:pageSlot position="ForHimForHerSlot" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot> --%>
	<%-- <div class="top_categories">
	<cms:pageSlot position="Section2A" var="feature">
		<cms:component component="${feature}" element="div" class="top_categories_section"/>
	</cms:pageSlot>
	</div> --%>
	<div class="top_categories">
	<cms:pageSlot position="TopCategoryHeadingSlot" var="feature">
		<cms:component component="${feature}" element="div" class="top_categories_section"/>
	</cms:pageSlot>
	<div class="top_categories_wrapper">
		<div class="top_categories_section">
			<cms:pageSlot position="TopCategoryCol1Slot" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
		<div class="top_categories_section sub_categories">
			<cms:pageSlot position="TopCategoryCol2Slot" var="feature">
				<cms:component component="${feature}" element="div" class="top_categories_section"/>
			</cms:pageSlot>
		</div>
		<div class="top_categories_section">
			<cms:pageSlot position="TopCategoryCol3Slot" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
		</div>
		<div class="top_categories_section sub_categories">
			<cms:pageSlot position="TopCategoryCol4Slot" var="feature">
				<cms:component component="${feature}" element="div" class="top_categories_section"/>
			</cms:pageSlot>
		</div>
	</div>
	</div>
	<div class="top_brands">
	<cms:pageSlot position="TopBrandSlot" var="feature">
		<cms:component component="${feature}" element="div" class="top_brands_section"/>
	</cms:pageSlot>
	</div>
	<%-- <div class="best_seller">
	<!-- <div class='Menu'><div class='mobile selectmenu'></div><ul></ul></div>
				<cms:pageSlot position="Section2B" var="feature">
					<cms:component component="${feature}" element="div" class="best_seller_section"/>
				</cms:pageSlot>
	</div>--%>	
	<div class="best_seller">
		<cms:pageSlot position="ShopForLookHeadingSlot" var="feature">
			<cms:component component="${feature}" element="div" class="best_seller_section best_seller_heading"/>
		</cms:pageSlot>
		<!-- <div class='Menu'><div class='mobile selectmenu'></div><ul></ul></div> -->
			
		<div class="best_seller_carousel">
			<div class='Menu'><div class='mobile selectmenu'></div><ul></ul></div>
			<cms:pageSlot position="ShopForLookLeftColSlot" var="feature">
				<cms:component component="${feature}" element="div" class="best_seller_section"/>
			</cms:pageSlot>
		</div>
		<cms:pageSlot position="ShopForLookRightColSlot" var="feature">
				<cms:component component="${feature}" element="div" class="best_seller_section best_seller_right best_seller_link"/>
			</cms:pageSlot>
		<!-- <div class="shop_the_look_right"> -->
		
		<!-- </div> -->
	</div>	
				<div class="winter_launch">
				 <cms:pageSlot position="Section2D" var="feature">
					<cms:component component="${feature}" element="div" class="winter_launch_section" />
				</cms:pageSlot>
				</div>
				<%-- <div class="style_edit style-edit-video">
				 <cms:pageSlot position="Section2C" var="feature">
					<cms:component component="${feature}" element="div" class="" />
				</cms:pageSlot>
				</div> --%>
				<div class="style_edit">
					<div class="style_edit_left">
						<cms:pageSlot position="StyleEditLeftColSlot" var="feature">
							<cms:component component="${feature}" element="div" class=""/>
						</cms:pageSlot>
					</div>
					<div class="style_edit_right">
						<cms:pageSlot position="StyleEditRightColSlot" var="feature">
							<cms:component component="${feature}" element="div" class=""/>
						</cms:pageSlot>
					</div>
				</div>	
				
				<div class="best-offers bestoffersCLP">
				 <cms:pageSlot position="BestOfferSection" var="feature">
					<cms:component component="${feature}" element="div" class="" />
				</cms:pageSlot>
				</div>
				<%-- <div class="top_deal">
					<cms:pageSlot position="Section3" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div> --%>
				<div class="top_deal">
						<cms:pageSlot position="TopDealsSlot" var="feature">
							<cms:component component="${feature}"  class="top_deal_carousel" />
						</cms:pageSlot>
						<div class="blog_container">
							<div class='blog_feature'>
								<cms:pageSlot position="TopDealsLeftColSlot" var="feature">
									<cms:component component="${feature}" class="top_deal_left_col"  />
								</cms:pageSlot>
							</div>
							<div class='blog_feature'>
								<cms:pageSlot position="TopDealsRightColSlot" var="feature">
									<cms:component component="${feature}"  class="top_deal_right_col" />
								</cms:pageSlot>
							</div>
						</div>
				</div>
			<%-- <div class="shop_for">
				<cms:pageSlot position="Section3A" var="feature">
					<cms:component component="${feature}" element="div" class="shop_for_component"/>
				</cms:pageSlot>
			</div> --%>
			<div class="shop_for">
				<cms:pageSlot position="ShopForHeading" var="feature">
						<cms:component component="${feature}" element="div" class="shop_for_component"/>
				</cms:pageSlot>
				<div class="shop_for_wrapper">
				<div class="shop_for_left_wrapper">
					<cms:pageSlot position="ShopForLeftCol" var="feature">
						<cms:component component="${feature}" element="div" class="shop_for_component"/>
					</cms:pageSlot>
				</div>
				<div class="shop_for_links">
					<cms:pageSlot position="ShopForRightCol" var="feature">
						<cms:component component="${feature}" element="div" class="shop_for_component"/>
					</cms:pageSlot>
				</div>
				</div>
				<cms:pageSlot position="ShopForViewMore" var="feature">
						<cms:component component="${feature}" element="div" class="shop_for_component"/>
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
	<div class="samsung-chat-div" id="samsung-chat-icon-id">
		<cms:pageSlot position="SamsungChat" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</div>	
	<product:productCompare/>
	<!-- For Infinite Analytics Start -->
	<div class="trending" id="ia_products_new"></div>
	<!-- For Infinite Analytics End -->
</template:page>

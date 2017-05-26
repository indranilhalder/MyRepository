<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<div>
	
	

<section class="lux-main-banner-slider text-center text-uppercase">
	<div class="banner-list">
		<div class="banner-img">
			<picture><cms:pageSlot
							position="CategoryHeroBanner" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot></picture>
		</div>
	</div>	
</section>	

 <section class="shop-by-catagory text-center">

	<cms:pageSlot position="CategoryCurosel" var="feature" element="div" class="span-24 section5 cms_disp-img_slot">
		<cms:component component="${feature}" />
	</cms:pageSlot>  
</section>
	
	  
		
		<cms:pageSlot
			position="Categorybanner2" var="feature" element="div"
			class="span-24 section5 cms_disp-img_slot">
			<cms:component component="${feature}" />
		</cms:pageSlot> 
		
		<div class="container">
			 <cms:pageSlot position="ProductCurosel" var="feature" element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>

		<%-- <table
			style="margin: 0; padding: 0; table-layout: fixed; "
			width="100%" height="100%" cellspacing="0">
			<tbody>
				<tr style="height: 30px;">
					<td style="width: 1791px; height: 30px;" colspan="4">
					
					<cms:pageSlot
							position="CategoryCurosel" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot>${feature.name}</td>
				</tr>
				<tr style="height: 30px;">
					<td style="width: 1791px; height: 30px;" colspan="4">
					
					<cms:pageSlot
							position="Categorybanner1" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						</td>
				</tr>
				<tr style="height: 30px;">
					<td style="width: 1791px; height: 30px;" colspan="4">
					<cms:pageSlot
							position="Categorybanner2" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot></td>
				</tr>
				<tr style="height: 30px;">
					<td style="width: 1791px; height: 30px;" colspan="4">
					<cms:pageSlot
							position="Categorybanner3" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot></td>
				</tr>
				<tr style="height: 31px;">
					<td style="width: 1791px; height: 30px;" colspan="4">
					<cms:pageSlot
							position="Categorybanner4" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</td>
				</tr>
				<tr style="height: 30px;">
					<td style="width: 1791px; height: 30px;" colspan="4">
					<cms:pageSlot
							position="ProductCurosel" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						</td>
				</tr>
				<tr style="height: 31px;">
					<td style="width: 1791px; height: 31px;" colspan="4">
					<cms:pageSlot
							position="WhyShopOnLuxury" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						</td>
				</tr>
				<tr style="height: 31px;">
					<td style="width: 1791px; height: 31px;" colspan="4"><cms:pageSlot
							position="EmailFooterHomePage" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						</td>
				</tr>
				<tr style="height: 31px;">
					<td style="width: 1791px; height: 31px;" colspan="4">
					<cms:pageSlot
							position="Footer" var="feature" element="div"
							class="span-24 section5 cms_disp-img_slot">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						</td>
				</tr>
			</tbody>
		</table> --%>
			</div>






</template:page>
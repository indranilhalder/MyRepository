<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables /> --%>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/mpl/acc.newWishlist.js"></script> --%>
<script >
function checkWishListName() {
	var wishlistName = $('#newWishlistName').val();
	var re = /^[ _a-zA-Z0-9_ ]*[ _a-zA-Z0-9_ ]+[ _a-zA-Z_ ]*$/i;
	var isValid = re.test(wishlistName);

	if (!isValid) {
		wishlistName = wishlistName.substring(0, wishlistName.length - 1);
		$('#newWishlistName').val(wishlistName);
		$("#errorCreate").html("<font color='#ff1c47'><b>*</b>Special charecters are not allowed</font>");
		$("#errorCreate").show().fadeOut(3000);
	}
}


$(document).ready(function() {    
    $('#myWishlistHeader').click(function(evt) {    	
        evt.preventDefault();        
        window.location.href = $(this).attr('href');

    });
    
    $('#createNewList').on('show.bs.modal', function () {
	    $(".product-info .product-image-container .zoom").css("z-index","1");
	    $(".zoomContainer").css("z-index","1");

	}); 
    $('#createNewList').on('hidden.bs.modal', function () {
	    $(this).find("input,textarea,select").val('').end();
	    $(".product-info .product-image-container .zoom").css("z-index","10000");
	    $(".zoomContainer").css("z-index","9999");

	}); 
    
    $('#newWishlistName').keyup(function() {
		checkWishListName();
	});
    
});
</script>


					   <li class="wishlist"> <a href="<c:url value="/my-account/wishList"/>" data-count="${wishlistproductcount}" id="myWishlistHeader"><spring:theme code="header.link.myWishList"/></a>
					      <div class="wishlist-info"> 
		                 </div>
			       </li>
 <div class="modal fade" id="createNewList">
			
			
			<div class="modal-content content" style="width:35%">
				<!-- Dynamically Insert Content Here -->
						    			
												
				<button type="button" class="close pull-right" data-dismiss="modal"
						aria-hidden="true" onclick="clearErrorMessage()"></button>
						
					<div class="create-new-wishlist-popup-container" id="myModalLabel">	
				<h1 class="modal-title">
					<spring:theme code="wishlist.create.otherWishlist" />
				</h1>
				
				
					 <ul>
					<label for="list-name" class="required"><spring:theme
							code="wishlist.list.name" /></label>
					<li>
					<!-- <input type="hidden" id="editWishListOld"
										name="wishlistOldName" value="" /> -->
					<input type="text" id="newWishlistName"
										name="newWishlistData" value="" maxlength="40" /> </li>
					<div id="errorCreate" style="display:none"></div> 	
					</ul>
					
					
					<button class="create_wishlist" id="CreateNewWishlist"><spring:theme code="wishlist.createWishlist" /></button>
									    
					<a class="close" href="" data-dismiss="modal"><spring:theme code="text.button.cancel" /></a>

				
				
			</div>
		</div>
		<div class="overlay" data-dismiss="modal"></div>
		</div>

		

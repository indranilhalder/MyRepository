<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />
<script>
	//jquery added to prevent stored Cross Site Scripting /TISSIT-1704/added for creating wishlist and renaming wishlist

	$(document).ready(function() {

		$('#newWishlistName').keyup(function() {
			validateEnteredName("newWishlistName","errorCreate");
		});
		$('#newWishlistName').blur(function() {
			validateEnteredName("newWishlistName","errorCreate");
		});
	});
	
	</script>

 
<template:page pageTitle="${pageTitle}">

	<body class="wishlist" onload="readyFunction();">

		<!-- START OF WRAPPER DIV SECTION -->
		<div class="wrapper wrapper-clearfix">

			<!-- START OF LEFT SECTION -->
			<div class="wishlist-nav">
				<!-- START OF SEARCH LIST ITEM STATIC COMPONENT -->
				<h3><spring:theme code="header.link.myWishList" /></h3>
				<%-- <form action="">
					<button type="submit"></button>
					<input type="text" placeholder="Find an item in your list">
				</form> --%>
				<!-- END OF SEARCH LIST ITEM STATIC COMPONENT -->
				<%-- ${fn:length(allWishLists)} --%>
				<c:if test="${ not empty allWishLists}">

					<!-- START OF WISHLIST LISTING COMPONENT -->
					<ul class="list">
						<c:forEach items="${allWishLists}" var="wishlist">
						<%--  <li><form:form
									action="${request.contextPath}/my-account/viewParticularWishlist"
									commandName="particularWishlistData" method="GET"
									class="view-wishlist-form">
									<form:hidden path="particularWishlistName"
										value="${wishlist.name}" />
									<button class="view-wishlist" type="submit"
										id="ViewParticularWishlist_${product.code}">${wishlist.name}</button>
								</form:form> <a href="wishlist.php">${wishlist.name}</a>
								<p>${wishlist.getEntries().size()}&nbsp;<spring:theme code="text.items" /></p></li> --%>
							<c:if test="${particularWishlistName eq wishlist.name}">
							<li ><a class="selectedWishlist" href="${request.contextPath}/my-account/viewParticularWishlist?particularWishlist=${wishlist.name}"><span class="title">${wishlist.name}</span>
						    	</a>
						    	<c:if test="${fn:length(WishlistProductDataList)>1}">
									<p>${fn:length(WishlistProductDataList)}<spring:theme code="text.items" />
										</p>
								</c:if>
								<c:if test="${fn:length(WishlistProductDataList)<=1}">
										<p>${fn:length(WishlistProductDataList)}<spring:theme code="text.item" />
										</p>
								</c:if>
						    	
						    	</li>
							</c:if>	
							<c:if test="${particularWishlistName != wishlist.name}">
							<li>
							
							<a href="${request.contextPath}/my-account/viewParticularWishlist?particularWishlist=${wishlist.name}"><span class="title">${wishlist.name}</span>
						    	</a>						    	
						    	<c:if test="${fn:length(wishlist.entries)>1}">
								<p>${fn:length(wishlist.entries)}<spring:theme code="text.items" /></p>
								</c:if>
								<c:if test="${fn:length(wishlist.entries)<=1}">
									<p>${fn:length(wishlist.entries)}<spring:theme code="text.item" /></p>
								</c:if>
						    </li>  
						    </c:if> 
						</c:forEach>

					</ul>
					<!-- END OF WISHLIST LISTING COMPONENT -->

				</c:if>

				<!-- IF WISHLIST IS EMPTY -->

				<!-- START OF STATIC COMPONENT -->
				<ul class="options">

					<!-- MANAGE LIST STATIC COMPONENT -->
					<li><a class="js-manage-myList" id="" data-toggle="modal"
						data-target="#manageMyList"
						data-mylist="<spring:theme code="text.help"  />"><spring:theme code="wishlist.manage" /></a></li>

					<!-- CREATE NEW LIST STATIC COMPONENT -->
					<li><a class="js-new-wishlist  create-newlist-link" href="#nogo"
						data-toggle="modal" data-target="#createNewList"
						data-head="<spring:theme code="text.help" />"><spring:theme	code="wishlist.create" /></a></li>
				</ul>
				<!-- END OF STATIC COMPONENT -->
			</div>
			<!-- END OF LEFT SECTION -->




			<!-- START OF RIGHT SECTION -->
			<div class="wishlist-content">

				<input id="showWishlist" type="hidden" value="${showWishlist}"></input>
				<input id="renderingMethod" type="hidden" value="${renderingMethod}"></input>
				<c:if test="${myAccountFlag eq 'N'}">
					<div id="newWishlist">
						<input type="button" class="btn btn-primary btn-block"
							value="Create New Wishlist and Add Product ${product.name} "
							onclick="createNewWishlist()">
					</div>
				</c:if>

				<c:if test="${myAccountFlag eq 'N'}">
					<div id="newWishListDetails">
						<form:form
							action="${request.contextPath}/my-account/createNewWishlist"
							commandName="newWishlistData" method="GET">

							<formElement:formInputBox idKey="wishlist.name"
								labelKey="wishlist.name" path="newWishlistName"
								inputCSS="form-control" mandatory="true" />

							<form:hidden path="productCode" value="${product.code}" />
							<br>
							<button class="btn btn-primary btn-block" type="submit"
								id="CreateNewWishlistAndAddProd_${product.code}"><spring:theme code="wishlist.createandadd" /></button>
						</form:form>
					</div>
				</c:if>

				<c:if test="${not empty allWishLists}">
				<div class="top">
					<div id="wishlistDetailItemNo">
						<h2>${particularWishlistName}</h2>
						<c:if test="${fn:length(WishlistProductDataList)>1}">
									<p>${fn:length(WishlistProductDataList)}<spring:theme code="text.items" />
										</p>
								</c:if>
								<c:if test="${fn:length(WishlistProductDataList)<=1}">
										<p>${fn:length(WishlistProductDataList)}<spring:theme code="text.item" />
										</p>
								</c:if>
						<input type="hidden" id="wishlistCount" value="${fn:length(WishlistProductDataList)}"/>
					</div>
					<div class="wishlistPagination">
                    <c:set var="productsInPage" value="${pageSizeInWl}"></c:set>
					<c:if test="${fn:length(WishlistProductDataList) > productsInPage }">
						<ul class="pagination">
							<li class="prev"  style="display: none"><span id="wishPrev" onClick="prev()"  >Prev</span></li>
							
							<li class="next"  style="display: none"><span id="wishNext" onClick="next()" >Next</span></li>
							
						</ul>
					</c:if>	
		
					</div>
					
					<!-- END OF PAGINATION STATIC COMPONENT -->
				</div>
				</c:if>
				<c:if test="${isDelisted eq 'true'}">
								<div style="color:#ff1c47;"><spring:theme code="wishlist.delistedProducts" /></div>
								</c:if>
				<div id="cartItems" class="products">
					<input type="hidden" id="count" value="${fn:length(WishlistProductDataList)}"/>
					<c:if test="${not empty allWishLists}">
                    <c:if test="${fn:length(WishlistProductDataList)<1}">
                    <p><spring:theme code="wishlist.empty.product" /></p>
                    </c:if>
                    </c:if>
					<c:if test="${fn:length(WishlistProductDataList)>0}">
					
					<!-- START: INSERTED for MSD -->
					<c:if test="${isMSDEnabled}">
					<input type="hidden" value="${isMSDEnabled}" name="isMSDEnabled" />
						<c:forEach items="${WishlistProductDataList}" var="wishListMSD">
							<c:if test="${wishListMSD.productData.rootCategory eq 'Clothing'||wishListMSD.productData.rootCategory eq 'Footwear'}">
							        <c:set var="includeMSDJS" scope="request" value="true"/>
							        <input type="hidden" value="true" name="isApparelExist"/>
							</c:if>
						</c:forEach>	
						<c:if test="${includeMSDJS eq 'true'}">
							<script type="text/javascript"	src="${msdjsURL}"></script>
						</c:if> 
						
													
									<!-- to get category id of the product -->	
									<c:forEach items="${WishlistProductDataList}" var="wpproductMSD">								
   									<c:forEach items="${wpproductMSD.productData.categories}" var="categoryForMSD">
								   <c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
								   	<input type="hidden" value="${categoryForMSD.code}" name="salesHierarchyCategoryMSD" />   
									</c:if>
								   </c:forEach>	
								   <!-- to get product  id -->							   
									<input type="hidden" value="${wpproductMSD.productData.code}" name="productCodeForMSD" />
									
									<c:if test="${not empty seller.spPrice}">
									<input type="hidden" value='<format:price priceData="${seller.spPrice}" displayFreeForZero="true" />' name="sppriceForMSD" />
									</c:if> 
									<c:if test="${empty seller.spPrice}">
									<input type="hidden" value='<format:price priceData="${seller.mopPrice}"
															displayFreeForZero="true" />' name="moppriceForMSD" />									
									</c:if>
									
									<input type="hidden" value="${wpproductMSD.productData.rootCategory}" name="rootCategoryMSD" /> 									
									</c:forEach>
									
					</c:if>					
					<!-- END:MSD -->
						<ul class="cart">
							<c:forEach items="${WishlistProductDataList}" var="wpproduct">
								<c:set value="${wpproduct.productData}" var="product" />
								<c:set value="${wpproduct.sellerInfoData}" var="seller" />
								<c:set value="${product.ussID}" var="entry_ussid" />
								<c:url value="${product.url}" var="productUrl" />
								<li>
									<div class="product-info">

										<a href="${productUrl}"> <product:productPrimaryImage
												product="${product}" format="thumbnail" />
										</a>

										<div>
											<ul>
												<!-- COMPANY OR BRAND NAME STATIC COMPONENT -->
												<li class="company">${fn:escapeXml(seller.sellername)}</li>

												<!-- PRODUCT NAME COMPONENT -->
												<li class="name"><ycommerce:testId
														code="cart_product_name">
														<a href="${productUrl}">${product.name}</a>
													</ycommerce:testId></li>

												<!-- PRODUCT PRICE COMPONENT -->
												<%-- <li class="price"><c:if
														test="${not empty seller.spPrice}">
														<format:price priceData="${seller.spPrice}"
															displayFreeForZero="true" />
													</c:if> <c:if test="${empty seller.spPrice}">
													<c:choose>
													<c:when test="${not empty seller.mopPrice}">
														<format:price priceData="${seller.mopPrice}"
															displayFreeForZero="true" /></c:when>
															<c:otherwise>
															<format:price priceData="${seller.mrpPrice}"
															displayFreeForZero="true" />
															</c:otherwise></c:choose>
													</c:if></li> --%>
												<li class="price"><format:price priceData="${wpproduct.price}"
															displayFreeForZero="true" /></li>
												<li><c:forEach items="${product.baseOptions}"
														var="option">
														<c:if
															test="${not empty option.selected and option.selected.url eq product.url}">
															<c:forEach
																items="${option.selected.variantOptionQualifiers}"
																var="selectedOption">
																<div>
																	<strong>${selectedOption.name}:</strong> <span>${selectedOption.value}</span>
																</div>
																<c:set var="entryStock"
																	value="${option.selected.stock.stockLevelStatus.code}" />
																<div class="clear"></div>
															</c:forEach>
														</c:if>
													</c:forEach></li>
											</ul>

											<!-- START OF STATIC COMPONENT -->
											<ul>
												<!-- APPAREL SIZE COMPONENT -->
												<c:choose>
												<c:when test="${not empty wpproduct.wishlistProductSize}">
												<li class="size"><spring:theme code="wishlist.size" />&nbsp
												${fn:escapeXml(wpproduct.wishlistProductSize)}</li>
												</c:when>
												</c:choose>
												
												<li class="size"><spring:theme code="wishlist.qty" /><spring:theme code="wishlist.qty.value" /></li>
												<!-- Date COMPONENT -->
												<li class="date-added"><spring:theme code="wishlist.added" />&nbsp 
													${fn:escapeXml(wpproduct.wishlistAddedDate)}</li>
												<c:if test="${not empty wpproduct.isOutOfStock && wpproduct.isOutOfStock eq 'Y'}">
												<li><span><spring:theme code="wishlist.product.outofstock" /></span></li>
												</c:if>
											</ul>

											<!-- ADD COMMENT STATIC COMPONENT -->
											<ul class="comments">
												<li><a href=""></a></li>
											</ul>
											<!-- END OF STATIC COMPONENT -->
										</div>
									</div> <!-- START OF STATIC COMPONENT FOR ADD COMMENT -->
									<form action="" class="wishlist-comment-form">
										<span class="chars-remaining"><span class="chars">250</span>
											Characters Left</span> <label for="comment">Comment</label>
										<textarea name="comment" id="" cols="30" rows="10"
											class="comment"></textarea>

										<div class="select-view">
											<select>
												<option>Low</option>
												<option>Low</option>
												<option>Low</option>
											</select> <span class="label">Priority</span>
											<div class="select-list">
												<span class="selected"> High </span>
												<ul>
													<li>Low</li>
													<li>Medium</li>
													<li>High</li>
												</ul>
											</div>
										</div>

										<button type="submit" class="orange">Save</button>
										<a href="" class="cancel close">Cancel</a>

									</form> <!-- END OF STATIC COMPONENT FOR ADD COMMENT-->

									<div class="actions">
										<c:url value="/cart/add" var="addToCartUrl" />
										<ycommerce:testId
											code="searchPage_addToCart_button_${product.code}">
											<form:form id="addToCartForm_${product.code}_${seller.ussid}"
												action="#" method="get"
												class="add_to_cart_wl_form add_to_bag_wl">
												<div id="addToCartForm_${product.code}_${seller.ussid}Title" class="addToCartFormTitleSuccessWl"></div>
												<input type="hidden" maxlength="3" size="1" id="qty"
													name="qty" value="1">
												<input type="hidden" maxlength="3" size="1" id="stock"
													name="stock" value="${seller.availableStock}">
												<input type="hidden" name="ussid" value="${seller.ussid}" />
												<input type="hidden" name="productCodePost"
													value="${product.code}" />
													<!-- For Infinite Analytics Start-->
													<input type="hidden" name="productArrayForIA" value="${product.code}"/>
													<!-- For Infinite Analytics End-->
												<input type="hidden" name="wishlistNamePost"
													value="${particularWishlistName}" />
												<c:choose>
													<c:when test="${not empty wpproduct.isOutOfStock && wpproduct.isOutOfStock eq 'Y'}">
														<span id="outOfStockId">
															<button id="addToCartButton" disabled="disabled"
																class="blue button js-add-to-cart_wl">
																<spring:theme code="basket.add.to.basket" />
															</button>
														</span>
													</c:when>

													<c:otherwise>
													<c:if test="${(not empty wpproduct.wishlistProductSize && wpproduct.productCategory eq 'Clothing')||(not empty wpproduct.wishlistProductSize && wpproduct.productCategory eq 'Footwear')}">
														<span>
															<button id="addToCartButton" type="${buttonType}"
																class="blue button js-add-to-cart_wl">
																<spring:theme code="basket.add.to.basket" />
															</button>
														</span>
													</c:if>
                                                    
													<c:if test="${empty wpproduct.wishlistProductSize && wpproduct.productCategory eq 'Electronics'}">
														<span>
															<button id="addToCartButton" type="${buttonType}"
																class="blue button js-add-to-cart_wl">
																<spring:theme code="basket.add.to.basket" />
															</button>
														</span>
														</c:if>
														<c:if test="${(empty wpproduct.wishlistProductSize && wpproduct.productCategory eq 'Clothing')||(empty wpproduct.wishlistProductSize &&wpproduct.productCategory eq 'Footwear')}">
														<span id="addToCartButtonId" style="display: none">
															<button type="button" id="addToCartButton" 
																class="blue button sizeNotSpecified_wl" data-toggle="modal"
															data-target="#redirectsToPDP">
																<spring:theme code="basket.add.to.basket" />
															</button>
														</span>
														</c:if>
														
													</c:otherwise>
												</c:choose>
                                            <input type="hidden" class="redirectsToPdp_Wl" value="${product.url}" />
                                            
                                            <!-- START: INSERTED for MSD -->												
												<c:if test="${isMSDEnabled}">
													<input type="hidden" value="${isMSDEnabled}" id="isMSDEnabled_wl_AddToBag" />
													<c:forEach items="${WishlistProductDataList}" var="wishListMSD">
														<c:if test="${wishListMSD.productData.rootCategory eq 'Clothing'||wishListMSD.productData.rootCategory eq 'Footwear'}">
														        <c:set var="includeMSDJS" scope="request" value="true"/>
														        <input type="hidden" value="true" id="isApparelExist_wl_AddToBag"/>
														</c:if>
													</c:forEach>	
													<c:if test="${includeMSDJS eq 'true'}">
														<script type="text/javascript"	src="${msdjsURL}"></script>
													</c:if>															
													<!-- to get category id of the product -->																				
							   						<c:forEach items="${wpproduct.productData.categories}" var="categoryForMSD">
													<c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
													<input type="hidden" value="${categoryForMSD.code}" id="salesHierarchyCategoryMSD_wl_AddToBag" />   
													</c:if>
													</c:forEach>	
													<!-- to get product  id -->							   
													<input type="hidden" value="${wpproduct.productData.code}" id="productCodeForMSD_wl_AddToBag" />										
													<c:if test="${not empty seller.spPrice}">
													<input type="hidden" value='<format:price priceData="${seller.spPrice}"	displayFreeForZero="true" />' id="sppriceForMSD_wl_AddToBag" />
													</c:if> 
													<c:if test="${empty seller.spPrice}">
													<input type="hidden" value='<format:price priceData="${seller.mopPrice}" displayFreeForZero="true" />' id="moppriceForMSD_wl_AddToBag" />									
													</c:if>										
													<input type="hidden" value="${wpproduct.productData.rootCategory}" id="rootCategoryMSD_wl_AddToBag" /> 									
																							
												</c:if>	
												
											<!-- END:MSD -->
                                            
											</form:form>
										</ycommerce:testId>

										
									 <c:if test="${myAccountFlag eq 'Y'}">
											 <span class="remove">
													<button class="remove-btn remove_product_from_wl"
														id="Remove_${product.code}" data-toggle="modal" data-target="#removeProductConfirmation"><spring:theme code="wishlist.remove" />
													</button>
												</span>
												<input type="hidden" id="particularWishlistName_wl" name="wishlistName" value="${particularWishlistName}" />
												<input type="hidden" id="productCode_wl" name="productCodeWl" value="${product.code}" />
					                            <input type="hidden" id="ussid_wl" name="ussidWl" value="${seller.ussid}" />
												<!-- START: INSERTED for MSD -->
												
												<c:if test="${isMSDEnabled}">
													<input type="hidden" value="${isMSDEnabled}" id="isMSDEnabled_wl" />
													<c:forEach items="${WishlistProductDataList}" var="wishListMSD">
														<c:if test="${wishListMSD.productData.rootCategory eq 'Clothing'||wishListMSD.productData.rootCategory eq 'Footwear'}">
														        <c:set var="includeMSDJS" scope="request" value="true"/>
														        <input type="hidden" value="true" id="isApparelExist_wl"/>
														</c:if>
													</c:forEach>	
													<c:if test="${includeMSDJS eq 'true'}">
														<script type="text/javascript"	src="${msdjsURL}"></script>
													</c:if>															
													<!-- to get category id of the product -->																				
							   						<c:forEach items="${wpproduct.productData.categories}" var="categoryForMSD">
													<c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
													<input type="hidden" value="${categoryForMSD.code}" id="salesHierarchyCategoryMSD_wl" />   
													</c:if>
													</c:forEach>	
													<!-- to get product  id -->							   
													<input type="hidden" value="${wpproduct.productData.code}" id="productCodeForMSD_wl" />										
													<c:if test="${not empty seller.spPrice}">
													<input type="hidden" value='<format:price priceData="${seller.spPrice}"	displayFreeForZero="true" />' id="sppriceForMSD_wl" />
													</c:if> 
													<c:if test="${empty seller.spPrice}">
													<input type="hidden" value='<format:price priceData="${seller.mopPrice}" displayFreeForZero="true" />' id="moppriceForMSD_wl" />									
													</c:if>										
													<input type="hidden" value="${wpproduct.productData.rootCategory}" id="rootCategoryMSD_wl" /> 									
																							
												</c:if>		
												
											<!-- END:MSD -->
										</c:if> 
                                     <span id="addtobagwl" style="display:none"><spring:theme code="product.addtocart.success"/></span>
									 <span id="addtobagerrorwl" style="display:none"><spring:theme code="product.error"/></span>
 									 <span id="bagtofullwl" style="display:none"><spring:theme code="product.addtocart.aboutfull"/></span>
									 <span id="bagfullwl" style="display:none"><spring:theme code="product.bag"/></span>
									</div>
								</li>
							</c:forEach>
							 <input type="hidden" id="redirectsToPdp_Wl" value="" />
						</ul>
					</c:if>
					<c:if test="${empty allWishLists}">
						<p><spring:theme code="wishlist.emptyList" /></p>
					</c:if>
					<input type="hidden" id="removeFrmWl_ussid" value="" />
					<input type="hidden" id="removeFrmWl_productcode" value="" />
					<input type="hidden" id="removeFrmWl_name" value="" />
					<!-- START: INSERTED for MSD -->		
					<input type="hidden" id="removeFrmWl_isMSDEnabled" value="" />
					<input type="hidden" id="removeFrmWl_isApparelExist" value="" />
					<input type="hidden" id="removeFrmWl_salesHierarchyCategoryMSD" value="" />
					<input type="hidden" id="removeFrmWl_productCodeForMSD" value="" />
					<input type="hidden" id="removeFrmWl_sppriceForMSD" value="" />
					<input type="hidden" id="removeFrmWl_moppriceForMSD" value="" />
					<input type="hidden" id="removeFrmWl_rootCategoryMSD" value="" />					
					<input type="hidden" id="AddToBagFrmWl_isMSDEnabled" value="" />
					<input type="hidden" id="AddToBagFrmWl_isApparelExist" value="" />
					<input type="hidden" id="AddToBagFrmWl_salesHierarchyCategoryMSD" value="" />
					<input type="hidden" id="AddToBagFrmWl_productCodeForMSD" value="" />
					<input type="hidden" id="AddToBagFrmWl_sppriceForMSD" value="" />
					<input type="hidden" id="AddToBagFrmWl_moppriceForMSD" value="" />
					<input type="hidden" id="AddToBagFrmWl_rootCategoryMSD" value="" />
					
					<!-- END:MSD -->	
				</div>
			</div>
			<!-- END OF RIGHT SECTION -->
		</div>
		<!-- END OF WRAPPER DIV SECTION -->

	<!--popup for manage my wishlist start-->
		<c:if test="${ not empty allWishLists}">

			<div class="modal fade" id="manageMyList">
				<div class="content" style="width:55%;max-width:450px;">
					<!-- Dynamically Insert Content Here -->
					<div class="manage-wishlist-container">

						<h1>
							<spring:theme code="wishlist.manage" />
						</h1>



						<form action="" class="manage-wishlist" style="padding: 0;float:left;">
							
								<div class="other-sellers">
									<table class="manage-wishlist other-sellers-table">
										<thead>
											<tr>
												<th class="title"><spring:theme code="wishlist.text" /></th>
												<th class="default"></th> 
												<th class="delete"><spring:theme code="wishlist.delete" /></th>
											</tr>
										</thead>
										<c:forEach items="${allWishLists}" var="wishlist">

											<tbody>
												<!-- List Name -->
												<tr>
													<td class="title"><span class="wishlist-name">${wishlist.name}</span>
														<div
															class="rename-wishlist-container help-popup-content-holder js-rename-wishlist-content">
															<input type="hidden" id="editWishListOld"
																class="rename-input" name="wishlistOldName" value="" />
															<input type="text" class="rename-input" id="editWishList"
																name="newWishlistName" value="" maxlength="40" />

															<button class="rename_link">
																<spring:theme code="wishlist.rename" />
															</button>
															<div class="clear"></div>
															<div id="errRename" class="rename-input"
																style="display: none;"></div>
														</div> <a class="rename js-rename-wishlist" href="#"><spring:theme
																code="wishlist.rename" /></a></td>

													<!-- Default Selector -->
													<td class="default"></td>
													<td class="delete"><input type="hidden"
														id="wishlistName" name="wishlistName"
														value="${wishlist.name}" /> <a href="#" class="delete delete_wishlist"
														type="submit" data-toggle="modal"
															data-target="#deleteConfirmation"><spring:theme
																code="wishlist.delete" /></a></td>
												</tr>
											</tbody>

										</c:forEach>
									</table>
									<input type="hidden" id="delete_wishlistName" value="" />
								</div>

								<!-- <a href="" class="close">Cancel</a> -->
								<%-- <button class="close" type="submit" id="" data-dismiss="modal"><spring:theme code="wishlist.create.cancel" /></button> --%>
				
						</form>
					
					<div class="actions">
					<button type="submit" data-dismiss="modal" onClick="saveChangesWl()">
						<spring:theme code="wishlist.save" />
					</button>
					
					<a class="close" href="" data-dismiss="modal"><spring:theme code="text.button.cancel" /></a>
					
					</div>
					</div>
					<button class="close" data-dismiss="modal"></button>
				</div>
				<div class="overlay" data-dismiss="modal"></div>
			</div>
		</c:if>
<!--popup for delete wishlist start-->

 		<div class="modal fade" id="deleteConfirmation">
 		<div class="overlay" data-dismiss="modal"></div>
			<div class="modal-dialog" style="top:-80%;background-color:#fff;">
			<div class="modal-content">
				<!-- Dynamically Insert Content Here -->
				<div class="modal-header">
				<h4 class="modal-title">
					<b><spring:theme code="text.deleteMessage.wishlist" /></b>
				</h4>
				<div class="wishlist-deletion-confirmation-block">
				<label class="wishlist-deletion-confirmation"><spring:theme
							code="wishlist.deleteConfirmation.message" /><label class="particular-wishlist-name"></label><spring:theme
							code="wishlist.delete.message" /></label>
				</div>
				<button class="deleteWlConfirmation" type="submit"><spring:theme code="text.wishlist.yes" /></button>
					<a class="close deleteWlConfirmationNo" href="" data-dismiss="modal"><spring:theme code="text.wishlist.no" /></a>
				</div>
				<!-- <button class="close" data-dismiss="modal"></button> -->
			</div>
		  </div>
		  
			
		</div> 
		
		<!--popup for redirect to PDP page start-->

 		<div class="modal fade" id="redirectsToPDP">
 		<div class="overlay" data-dismiss="modal"></div>
			
			<div class="modal-content content" style="width:35%">
			<button type="button" class="close pull-right" aria-hidden="true" data-dismiss="modal"></button>
				<!-- Dynamically Insert Content Here -->
				<div class="modal-header">
				<h4 class="modal-title">
					<b><spring:theme code="text.wishlist.pdp" /></b>
				</h4>
				<div class="wishlist-redirects-to-pdp-block">
				<label class="wishlist-redirects-to-pdp"><spring:theme
							code="wishlist.redirectsToPdp.message" /></label>
				</div>
				<button class="redirectsToPdpPage" type="submit"><spring:theme code="text.wishlist.ok" /></button>
				</div>
				<!-- <button class="close" data-dismiss="modal"></button> -->
			</div>
		</div> 
	
	<!--popup for remove product from wishlist start-->	
		<div class="modal fade" id="removeProductConfirmation">
 		<div class="overlay" data-dismiss="modal"></div>
			<div class="modal-dialog" style="top:-80%; background-color: #fff;">
			<div class="modal-content">
				<!-- Dynamically Insert Content Here -->
				<div class="modal-header">
				<h4 class="modal-title">
					<b><spring:theme code="text.removeProductMessage.wishlist" /></b>
				</h4>
				<div class="product-deletion-confirmation-block">
				<label class="product-deletion-confirmation"><spring:theme
							code="wishlist.removeProductConfirmation.message" /></label>
				</div>
				<button class="removeProductConfirmation" type="submit"><spring:theme code="text.wishlist.yes" /></button>
					<a class="close removeProductConfirmationNo" href="" data-dismiss="modal"><spring:theme code="text.wishlist.no" /></a>
				</div>
				<!-- <button class="close " data-dismiss="modal"></button> -->
			</div>
		  </div>
		  
			
		</div> 

		<!--popup for create new wishlist start-->
		<div class="modal fade" id="createNewList">
			<div class="overlay" data-dismiss="modal"></div>
			
			<div class="modal-content content" style="width:35%">
			
			<%-- <button class="close" data-dismiss="modal"><spring:theme code="wishlist.create.cancel" /></button> --%>
				<button type="button" class="close pull-right" onclick="clearErrorMessage()" aria-hidden="true" data-dismiss="modal"></button>
				
				<div class="click2chat-container" id="myModalLabel">
				
				<h1 class="modal-title">
					<spring:theme code="wishlist.create.otherWishlist" />
				</h1>
				<!-- Dynamically Insert Content Here -->
				<%-- <form:form
					action="${request.contextPath}/my-account/createNewWishlistWP"
					commandName="newWishlistData" class="create-wishlist" method="GET">
				
				<label for="list-name" class="required"><spring:theme
							code="wishlist.list.name" /></label>
							
				<form:input id="wishlist.name" class="inputText"
						idKey="wishlist.name" labelKey="wishlist.name"
						path="newWishlistName" maxlength="140" placeholder="New Wishlist"/> --%>
						
						<ul>
					<label for="list-name" class="required"><spring:theme
							code="wishlist.list.name" /></label>
					<li>
					<input type="text" id="newWishlistName"
										name="newWishlistData" value="" maxlength="40" /> </li>
					<div id="errorCreate" style="display:none"></div> 	
					</ul>
						
						<span></span>

					<%-- <formElement:formInputBox idKey="wishlist.name" labelKey="wishlist.name"
									path="newWishlistName" inputCSS="form-control" mandatory="true" /><br>	 --%>

					
					
				
				<div class="actions">			

<button class="" type="submit" id="CreateNewWishlist"><spring:theme code="wishlist.createWishlist" /></button>
					<a class="close" href="" data-dismiss="modal"><spring:theme code="text.button.cancel" /></a>

				</div>	
				</div>
				<!-- <button class="close" data-dismiss="modal"></button> -->



			</div>
		
		</div>
		<!--popup for create new wishlist ends-->
		
		<!-- create a new wishlist popup starts -->
		<c:if test="${empty allWishLists}">
			<div class="modal small fade" id="manageMyList">
				<div class="content emptyWishlist">
				<h2>Manage My Lists</h2>
					<p><spring:theme code="wishlist.emptyList" /></p>
					<button class="close" data-dismiss="modal"></button>
				</div>
				<div class="overlay" data-dismiss="modal"></div>
			</div>
		</c:if>
		<!-- create a new wishlist popup starts -->
	</body>

<!-- For Infinite Analytics Start -->
<c:if test="${fn:length(WishlistProductDataList) > 0}">	
	<div class="trending"  id="ia_products"></div>
</c:if>
<!-- For Infinite Analytics End -->


</template:page>
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script>
<script type="text/javascript">
	function readyFunction() {
		$("#wishlistDetailItemNo").css("display", "none");
		$("#newWishListDetails").css("display", "none");
		$("#allWishlists").css("display", "none");
		$("#newWishListDetailsWP").css("display", "none");
		$("#newWishlist").css("display", "none");
		$('#newWishlistName').val("");
		$('#newWishlistDescription').val("");
		$("#cartItems").css("display", "none");
		if ($("#renderingMethod").val() == 'wishList') {
			$("#allWishlists").css("display", "block");
			$("#newWishListDetailsWP").css("display", "block");
			$("#newWishlist").css("display", "block");
			$("#newWishListDetails").css("display", "none");
			$("#cartItems").css("display", "none");
		}
		if ($("#renderingMethod").val() == 'createNewWishlist') {
			$("#wishlistDetailItemNo").css("display", "block");
			$("#allWishlists").css("display", "block");
			$("#newWishListDetailsWP").css("display", "none");
			$("#newWishlist").css("display", "block");
			$('#newWishlistName').val("");
			$('#newWishlistDescription').val("");
			$("#newWishListDetails").css("display", "none");
			$("#cartItems").css("display", "block");
		}
		if ($("#renderingMethod").val() == 'addInExistingWishlist') {
			$("#allWishlists").css("display", "block");
			$("#newWishListDetailsWP").css("display", "none");
			$("#newWishlist").css("display", "block");
			$('#newWishlistName').val("");
			$('#newWishlistDescription').val("");
			$("#newWishListDetails").css("display", "none");
			$("#cartItems").css("display", "block");
		}
		if ($("#renderingMethod").val() == 'viewParticularWishlist') {
			$("#wishlistDetailItemNo").css("display", "block");
			$("#allWishlists").css("display", "block");
			$("#newWishListDetailsWP").css("display", "none");
			$("#newWishlist").css("display", "none");
			$('#newWishlistName').val("");
			$('#newWishlistDescription').val("");
			$("#newWishListDetails").css("display", "none");
			$("#cartItems").css("display", "block");
		}
		 if ($("#renderingMethod").val() == 'editParticularWishlistName') {
			$("#allWishlists").css("display", "block");
			$("#newWishListDetailsWP").css("display", "none");
			$("#newWishlist").css("display", "none");
			$('#newWishlistName').val("");
			$('#newWishlistDescription').val("");
			$("#newWishListDetails").css("display", "none");
			$("#cartItems").css("display", "block");
		} 
		 if ($("#renderingMethod").val() == 'removeItemFromWL') {
			$("#allWishlists").css("display", "block");
			$("#newWishListDetailsWP").css("display", "none");
			$("#newWishlist").css("display", "none");
			$('#newWishlistName').val("");
			$('#newWishlistDescription').val("");
			$("#newWishListDetails").css("display", "none");
			$("#cartItems").css("display", "block");
			}
		 
		 	}
	
	function clearErrorMessage(){
		
		$("#errorCreate").html(null);
	}

	
	function createNewWishlist() {
		$("#newWishListDetails").css("display", "block");
		$("#allWishlists").css("display", "none");
	}
	function createNewWishlistWP() {
		$("#newWishListDetailsWP").css("display", "block");
		$("#allWishlists").css("display", "block");
	}
	function checkField(id) {
		alert(id);
		return false;
	}
	
	var i=0;
	var page=0;
	var pagelimit=${pageSizeInWl};
	var total=$('#count').val();
	var noofpage=Math.ceil(total/pagelimit);
	
	if(noofpage > 1) {
    	$("#wishPrev").css("display", "inline-block");
	    $("#wishNext").css("display", "inline-block");
	    for(var i = 1; i<= noofpage; i++) {
			$(".wishlistPagination .pagination").append('<li class="wishlistPageNo">'+i+'</li>');
		}
	    $("#wishlistDetailItemNo p").html('1-'+pagelimit+'&nbsp;of&nbsp'+$("#wishlistDetailItemNo #wishlistCount").val()+'&nbsp;items');
    } 
	
		
		$("span.glyphicon.glyphicon-shopping-cart").css("line-height", "34px");
		$("button.btn.btn-link").css("padding", "9px 12px");
	    $(".js-rename-wishlist").click(function(){
			$("#editWishList,#editWishListOld").val($(this).parent().find('span').text());
			
			console.log($(this).parent().find('span').text());
		}); 
	    
		
	    $('#cartItems .cart>li').each(function(i)  {
	        $(this).attr('id', 'p_' + i);
	    i=i + 1;
	    });
	    disp(0,pagelimit);
	    $(".wishlistPagination .pagination li.wishlistPageNo").eq(0).addClass("currentPage");
	    $(document).on("click",".wishlistPageNo",function(){
	    	$(".wishlistPagination .pagination li.wishlistPageNo").removeClass("currentPage");
	    	$(this).addClass("currentPage");
	    	var pageNo = parseInt($(".wishlistPagination .pagination li.wishlistPageNo.currentPage").text());
	    	wishGoTo(pageNo);
	    	
	    });
	    wishGoTo(1);
	    function next() {
	    	var pageNo = parseInt($(".wishlistPagination .pagination li.wishlistPageNo.currentPage").text());
	    	if(pageNo !== noofpage) {
		    	wishGoTo(pageNo+1);
		    	$(".wishlistPagination .pagination li.wishlistPageNo").removeClass("currentPage");
		    	$(".wishlistPagination .pagination li.wishlistPageNo").eq(pageNo).addClass("currentPage");
	    	} else {
	    		return false;
	    	}
	    }
	    function prev() {
	    	var pageNo = parseInt($(".wishlistPagination .pagination li.wishlistPageNo.currentPage").text());
		    if(pageNo !== 1) {
		    	wishGoTo(pageNo-1);
		    	$(".wishlistPagination .pagination li.wishlistPageNo").removeClass("currentPage");
		    	$(".wishlistPagination .pagination li.wishlistPageNo").eq(pageNo-2).addClass("currentPage");
	    	} else {
	    		return false;
	    	}
		    
	    }
		function wishGoTo(pageNo) {
			$(".wishlist-content #cartItems ul.cart>li").css("display", "none");
	    	for(var i = pagelimit*(pageNo-1) ; i < pagelimit*pageNo; i++) {
	    		$(".wishlist-content #cartItems ul.cart>li").eq(i).css("display","block");
	    	}
	    	if(noofpage > 1) {
	    		$("#wishlistDetailItemNo p").html((pagelimit*(pageNo-1)+1)+'-'+pagelimit*pageNo+'&nbsp;of&nbsp'+$("#wishlistDetailItemNo #wishlistCount").val()+'&nbsp;items');
	    	}
	    	if(pageNo == noofpage && noofpage > 1) {
	    		$("#wishlistDetailItemNo p").html((pagelimit*(pageNo-1)+1)+'-'+$("#wishlistDetailItemNo #wishlistCount").val()+'&nbsp;of&nbsp'+$("#wishlistDetailItemNo #wishlistCount").val()+'&nbsp;items');
	    	}
	    	if(pageNo !== 1) {
	    		$(".wishlistPagination .pagination .prev").css("display", "inline-block");
	    	} else {
	    		$(".wishlistPagination .pagination .prev").css("display", "none");
	    	}
	    	if(pageNo !== noofpage) {
	    		$(".wishlistPagination .pagination .next").css("display", "inline-block");
	    	} else {
	    		$(".wishlistPagination .pagination .next").css("display", "none");
	    	}
		}
	
</script>


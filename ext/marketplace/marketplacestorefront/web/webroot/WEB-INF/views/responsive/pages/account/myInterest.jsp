<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />

	<template:page pageTitle="${pageTitle}">
	
			<!----- content Starts --------->

			<form class="questionnaire-form">
		<div class="myInterestGender">
			<fieldset class="gender active">
				<div class="questionnaire-title">
					<h1>Let's get personal</h1>
					<h2>Tell us about yourself and we will create personailized
						recommendations based on what you love.</h2>
				</div>
				<div class="question">
					<h3>What's your gender?</h3>
					<c:forEach items="${genderList}" var="gender" varStatus="status">
						<input type="radio" name="question-0"
							id="question-0-${status.index}" value="${gender}">
						<label for="question-0-${status.index}"> <img
							src="${commonResourcePath}/images/${gender}.jpeg"> <span>${gender}</span>
						</label>
					</c:forEach>
				</div>
			</fieldset>
		</div>


		<!-- Products -->

		<div class="myInterestCategory">
			<fieldset class="products">
				<div class="questionnaire-title">
					<h1>What types of products are you interested in?</h1>
					<h2>Choose at least one.</h2>
				</div>
				<div class="question categoryPopulate">
				<div class="products-questionnaire">
					<!-- category here products for ajax loading -->
				</div>
				</div>
				<span class="error product"></span>
				<div class="buttons">
					<c:if test="${param.automate ne true}">
					<button class="prev" type="button" id="catPrev">Previous</button>
					</c:if>
					<button class="nextButton" id="catNext" data-min="1" type="button" >Next</button>
				</div>
			</fieldset>
			<c:if test="${prevSelectedCats ne null}">
				<c:forEach items="${prevSelectedCats}" var="prevCat">
					<input name="prevSelectedCats" type="hidden" value="${prevCat.code}"/>
				</c:forEach>
			</c:if>
		</div>


		<!-- Products -->
				
				<!-- Brands -->
				<div class="brandsCategory">
				<fieldset class="brands">
					<div class="questionnaire-title">
						<h1>What brands are you interested in?</h1>
						<h2>Choose at least one.</h2>
					</div>
					<div id="brandContainer" class="question">
						<!-- brands loading from ajax  -->
					</div>
					<span class="error brand"></span>
					<div class="buttons">
						<button class="prev" id="brandPrev" type="button">Previous</button>
						<button class="nextButton" id="brandNext" data-min="1" type="button">Next</button>
					</div>
				</fieldset>
				<c:if test="${prevSelectedBrands ne null}">
				<c:forEach items="${prevSelectedBrands}" var="prevBrand">
					<input name="prevSelectedBrands" type="hidden" value="${prevBrand.code}"/>
				</c:forEach>
			</c:if>
				</div>
				<!-- Brands -->
				
				
				<!-- objects -->
				<div class="brandsSubCategory">
				<fieldset class="objects">
					<div class="questionnaire-title">
						<h1 id="objHeading"></h1>
						<h2>Choose at least three.</h2>
					</div>
					<div class="question" id="objects">
						<!-- objects for ajax loading -->
					</div>
					<span class="error object"></span>
					<div class="buttons">
						<button class="prev" id="objPrev" type="button">Previous</button>
						<button class="nextButton"  data-min="3" type="button" id="final">Create Style Profile</button>
					</div>
				</fieldset>
				</div>
				<!-- apparel objects -->
				
				<div class="brandsSubCategoryApparel">
				<fieldset class="objects-apparel">
					<div class="questionnaire-title">
						<h1 id="objHeadingApparel"></h1>
						<h2>Choose at least three.</h2>
					</div>
					<div class="question" id="apparelObjects">
						<!-- objects for ajax loading -->
					</div>
					<span class="error object"></span>
					<div class="buttons">
						<button class="prev" id="objPrevApparel" type="button">Previous</button>
						<button class="nextButton"  data-min="3" type="button" id="apparelFinal">Next</button>
					</div>
				</fieldset>
				</div>
					<!-- electronics object -->
				
				<div class="brandsSubCategoryeElectronics">	
				<fieldset class="objects-electronics">
					<div class="questionnaire-title">
						<h1 id="objHeadingElectronics"></h1>
						<h2>Choose at least three.</h2>
					</div>
					<div class="question" id="electronicObjects">
						<!-- objects for ajax loading -->
					</div>
					<span class="error final"></span>
					<div class="buttons">
						<button class="prev" id="objPrevElectronics" type="button">Previous</button>
						<button class="nextButton"  data-min="3" type="button" id="electronicFinal">Create Style Profile</button>
					</div>
				</fieldset>
				</div>
			</form>

			<!----- Content ENDS --------->

	</template:page>
	<style>
	.error{
		color: #ff1c47;
	}
	.nextButton {
    background-color: #a9143c;
    color: #fff;
}
.nextButton:hover {
    background-color: #fff;
    color: #a9143c;
}
</style>

<script>
	$(document).ready(function(){
		
		<c:if test="${param.automate eq true}">
		<c:if test="${param.catids ne null}">
		var catids = '${param.catids}';
		automateMyrecomendationBrandModification(catids);
		</c:if>
		</c:if>
		
		<c:if test="${param.automate eq true}">
		var gender = '${param.gender}';
		if(gender== "MALE"){
			myRecomendationCategoryModification(gender);
		}else if(gender == "FEMALE"){
			myRecomendationCategoryModification(gender);
		}
		</c:if>
	});
</script>

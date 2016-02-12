<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- <link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"> -->

<!-- Optional theme -->
<!-- <link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css"> -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

<script type="text/javascript" src="${commonResourcePath}/js/mpl/acc.helpmeshop.js"></script>


<c:url value="/search/helpmeshop" var="searchForHelpMeShopUrl" />


  

  <div class="close" id="closeConceirge">close</div>
  <h1><spring:theme code="header.helpmeshop.howcanwehelpyou" /></h1>
	<div class="help-me-shop-form"><form name="search_form_for_help_me_shop" method="post" action="${searchForHelpMeShopUrl}">
	<div>
				<span><spring:theme code="header.helpmeshop.iama" /></span>
						<select id="age" name="age">
							<option value="-Select-"><spring:theme code="text.age"/></option>
							<c:forEach var="i" begin="${startAgeLimit}" end="${endAgeLimit}"
								step="1">
								<c:set value="${ycommerce:determineAgeBand(i)}" var="ageBand"/>
								<option value="${ageBand}">${i}</option>
							</c:forEach>
						</select>
				
					
				<span><spring:theme code="header.helpmeshop.yearold" /></span>
						<select id="genderOrTitle" name="genderOrTitle">
							<option value="-Select-"><spring:theme code="text.gender"/></option>
							<%-- <c:forEach items="${genderOrTitleList}" var="genderOrTitle">
								<option value="${genderOrTitle.code}">${genderOrTitle.code}</option>
							</c:forEach> --%>
							<option value="MSH11">Man</option>
							<option value="MSH10">Woman</option>
							<option value="MPH1112100">Boy</option>
							<option value="MPH1112101" >Girl</option>
							<option value="MPH1112102">Infant</option>
						</select>
					
					</div>
					<div>
				<span><spring:theme code="header.helpmeshop.lookingfora" /></span>
						<%-- <select id="typeOfProduct" name="typeOfProduct"
							>
							<option value="-Select-"><spring:theme code="text.product"/></option>
							<c:forEach items="${typeOfProductList}" var="typeOfProduct">
								<option value="${typeOfProduct.name}">${typeOfProduct.name}</option>
							</c:forEach>
						</select> --%>
						<input id="typeOfProduct" type="text" name="typeOfProduct">
						
						<!-- <input id="typeOfProduct" type="text" value="Causal Shirt" name="typeOfProduct"> -->
					</div>
					<div>
				<span><spring:theme code="header.helpmeshop.becauseiam" /></span>
					<select id="reasonOrEvent" name="reasonOrEvent"
						>
						<option value="-Select-"><spring:theme code="text.occasion"/></option>
						<c:forEach items="${reasonOrEventList}" var="reasonOrEvent">
							<option value="${reasonOrEvent.filterName}">${reasonOrEvent.displayName}</option>
						</c:forEach>
					</select>
					</div>
					<div>
				<button type="submit" class="blue" onclick=" return navigateUrl() ;">
						<spring:theme code="header.helpmeshop.helpmeshop" />
					</button>
					</div>
					<!-- <div class="errorMessage"><div id="errConceirge"></div></div> -->
					<input type="hidden" name="CSRFToken" value="${CSRFToken}">

	</form>
	</div>
	<div class="fill-in-concierge">Fill all Values</div>
	<div class="select-value-concierge">Please select a value from the suggested values</div>

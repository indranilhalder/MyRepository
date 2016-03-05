<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="errorNoResults" required="true" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>

<c:url value="/store-finder" var="storeFinderFormAction" />
<input id="initialZoom" name="initialZoom" type="hidden" value="${initialZoom}"/>
					 <input name="markerZoom" id="markerZoom" type="hidden" value="${markerZoom}"/>
					 <input id="defLatitude" name="defLatitude" type="hidden" value="${latitude}"/>
					 <input id="defLongitude" name="defLongitude" type="hidden" value="${longitude}"/>
					 <input id="storefinderNoresult" name="storefinderNoresult" type="hidden" value="<spring:theme code="storelocator.error.no.results.title" text="No store results were found for your search criteria."/>"/>
<div class="row">
	<div class="col-lg-8">
		<div class="headline"><spring:theme code="storeFinder.find.a.store" /></div>
		 
		<div class="store-finder-search">
			<div class="row">
				<div class="col-sm-6">
					<form:form action="${storeFinderFormAction}" method="get" commandName="storeFinderForm">
						<ycommerce:testId code="storeFinder_search_box">
							<div class="input-group">
								<formElement:formInputBox idKey="storelocator-query" labelKey="storelocator.query" path="q" labelCSS="sr-only" inputCSS="form-control js-store-finder-search-input" mandatory="true"  placeholder="pickup.search.message" />
								<span class="input-group-btn">
									<button class="btn btn-primary" type="submit" data-search-empty="<spring:theme code="storelocator.error.no.results.subtitle" text="Check that you entered a valid postcode or place name."/>">
										<!-- <span class="glyphicon glyphicon-search"></span> -->
										<span>Find A Store</span>
									</button>
								</span>
							</div>
						</ycommerce:testId>
					</form:form>
				</div>
				<div class="col-sm-6">
					<ycommerce:testId code="storeFinder_nearMe_button">
						<%-- <button id="findStoresNearMe" class="btn-link" type="button">
							<spring:theme code="storeFinder.findStoresNearMe"/>
						</button> --%>
						 <a id="findStoresNearMe" disabled><spring:theme code="storeFinder.findStoresNearMe"/></a>
					</ycommerce:testId>
				
				</div>
				<div class="col-sm-6">
				     <span><spring:theme code="Store Near"/></span>
					 <label id="storeSearchTextValue"></label>
				</div>
			</div>
		</div>
	</div>
</div>
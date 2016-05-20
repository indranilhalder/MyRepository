<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="errorNoResults" required="true" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<style>
     .findStoreNearMeDisable{
       pointer-events:none;
     }
	.store-finder-search {
		margin-bottom: 10px;
	}
	
	.storesnear {
		float: right;
		text-align: right;
		margin-top: 32px;
		margin-bottom: 32px;
	}
	
	.storesnear span {
		font-weight: 500;
	}
	
	.js-store-finder {
		margin: 0px 32px;
	}
	
	.findStoresNearMe {
		float: right;
		position: absolute;
		margin-top: 12px;
		z-index: 9;
		margin-left: -8px;
		color:#A9143C;
	}
	
	.container_store_finder {
		margin: 0px 16px;
	}
	
	.body-Content {
		margin-top: -20px !important;
	}
	
	.input-group-btn>.store-btn{
	 background:#A9143C;
	 z-index:3;
	}
	.col-sm-6 .storeSearchTextValue{
	  color:#A9143C;
	}
	
	@media (max-width: 1170px) {
	 
	 .input-group-btn>.store-btn{
	   margin-top: 17px;
	 }
	 .container_store_finder {
		margin: 0px 0px;
	}
	.findStoresNearMe{
	   margin-left: -10px; 
	   white-space: pre;
	   margin-top:8px;
	}
	.js-store-finder{
	 margin :0px 12px;
	}
	.storesnear {
		margin-top: 0px;
		margin-bottom: 10px;
	}
	}
		
	}
	</style>
<c:url value="/store-finder" var="storeFinderFormAction" />
<input id="initialZoom" name="initialZoom" type="hidden" value="${initialZoom}"/>
					 <input name="markerZoom" id="markerZoom" type="hidden" value="${markerZoom}"/>
					 <input id="defLatitude" name="defLatitude" type="hidden" value="${latitude}"/>
					 <input id="defLongitude" name="defLongitude" type="hidden" value="${longitude}"/>
					 <input id="storefinderNoresult" name="storefinderNoresult" type="hidden" value="<spring:theme code="storelocator.error.no.results.title" text="No store results were found for your search criteria."/>"/>

<%-- <div class="headline findaStoreHeadLin"><spring:theme code="storeFinder.find.a.store" /></div> --%>
<div class="container_store_finder">
	<div class="row">
		<div class="col-md-6 col-sm-6 col-lg-6 store-finder-search">
			<div class="row">
				<div class="col-md-7 col-sm-7 col-xs-7"></div>
				<div class="col-md-5 col-sm-5 col-xs-5">
					<ycommerce:testId code="storeFinder_nearMe_button">
						<%-- <button id="findStoresNearMe" class="btn-link" type="button">
							<spring:theme code="storeFinder.findStoresNearMe"/>
						</button> --%>
						 <a id="findStoresNearMe" class="findStoresNearMe" disabled><spring:theme code="storeLocator.findStoresNearMe"/></a>
					</ycommerce:testId>
				</div>
			</div>
			<form:form action="${storeFinderFormAction}" method="get" commandName="storeFinderForm">
				<ycommerce:testId code="storeFinder_search_box">
					<div class="input-group">
						<formElement:formInputBox idKey="storelocator-query"  labelKey="storelocator.query" path="q" labelCSS="sr-only" inputCSS="form-control js-store-finder-search-input" mandatory="true"  placeholder="pickup.search.message" autocomplete="off" />
						<span class="input-group-btn">
							<button class="store-btn btn-primary" type="submit" data-search-empty="<spring:theme code="storelocator.error.no.results.subtitle" text="Check that you entered a valid postcode or place name."/>">
								<!-- <span class="glyphicon glyphicon-search"></span> -->
								<span>Find A Store</span>
							</button>
						</span>
					</div>
				</ycommerce:testId>
			</form:form>
		</div>
		<div class="col-md-6 col-sm-6 col-lg-6 storesnear" id="storesnear">
			<span class=""><spring:theme code="storeLocator.stores.Near"/></span>
			 <label class="storeSearchTextValue" id="storeSearchTextValue"></label>
		</div>
	</div>
</div>

<!-- <div class="row">
	<div class="col-lg-8 col-md-8">
		
		<div class="">
			<div class="row">
				<div class="col-sm-6 col-md-6 col-xs-6">
					
				</div>
			</div>
			<div class="row">
			 <div class="col-sm-6 col-md-6 col-xs-6">
					
				
				</div>
				<div class="col-sm-6">
				     
				</div>
			</div>
			
		</div>
	</div>
	<div class="col-lg-4 col-md-4"></div>
</div> -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="searchPageData" required="true"
	type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData"%>
<%@ attribute name="locationQuery" required="false"
	type="java.lang.String"%>
<%@ attribute name="geoPoint" required="false"
	type="de.hybris.platform.commerceservices.store.data.GeoPoint"%>
<%@ attribute name="numberPagesShown" required="true"
	type="java.lang.Integer"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/responsive/store"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action"%>

<c:url value="/store-finder" var="storeFinderFormAction" />

<div class="js-store-finder" data-url="${storeFinderFormAction}"
	style="display: block !important;">
	<ycommerce:testId code="storeFinder">
        
       <!--  <div style="padding: 1px;"></div> -->
		<div class="store-finder-map js-store-finder-map"></div>
		  <div id="legend" class="legend">
                           
          </div> 
          <div id="legend1" class="legend">
                           
          </div> 
		 

		<%-- <div class="store-finder-details-openings">
							<dl class="dl-horizontal js-store-openings"></dl>
							<div class="store-finder-details-openings-title"><spring:theme code="storeDetails.table.features" /></div>
							<ul class="js-store-features"></ul>
						</div> --%>

	</ycommerce:testId>

</div>
<!-- <div id="home-googleMap" class="home-googleMap"> -->

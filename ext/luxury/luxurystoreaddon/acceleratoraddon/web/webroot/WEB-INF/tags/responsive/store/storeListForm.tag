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
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/store"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/nav"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/action"%>
<style>
.store-finder-map{
  height:500px;
}
.container_store_finder_map {
		margin: 0px 0px;
	}


.store-finder-legends{
z-index: 1;
/* padding-left:20px */
position :absolute;
float:right;
right:10px;
top:15%;
padding-bottom:5px;
}
.googleMapLegends{
  /* width:initial; */
 /*  margin:-10px; */
}

@media (max-width: 768px) {
  .store-finder-legends{
  position:relative;
  width:100%;
  text-align:right;
  }
}
}

@media (max-width: 1170px) {
  .store-finder-legends{
     right:10px;
    }
}

</style>
<c:url value="/store-finder" var="storeFinderFormAction" />
<div class="container_store_finder_map">
<div class="row">
   <div class="js-store-finder" data-url="${storeFinderFormAction}"
	style="display: block !important;">
	<ycommerce:testId code="storeFinder">
		<div class="store-finder-map js-store-finder-map"></div>
	</ycommerce:testId>
   </div>
  </div>
<div class="row"> 
    <!-- <div id="store-finder-legends" class="store-finder-legends"> -->
       <div class="col-md-9 col-sm-6 col-lg-8">
       </div>
      <div class="col-md-3 col-sm-6 col-lg-4 store-finder-legends">
       <div>
                <div><img alt="" src="${commonResourcePath}/images/Bestseller_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/CottonWorld_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/Croma_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/Inc5_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/Jack_Jones_Legend.png" class="googleMapLegends"></div>
                
                <div><img alt="" src="${commonResourcePath}/images/Killer_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/Lenovo_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/Metro_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/Mochi_Legend.png" class="googleMapLegends"></div>
                
                <div><img alt="" src="${commonResourcePath}/images/Only_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/TheMobileStore_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/Tresmode_Legend.png" class="googleMapLegends"></div>
                <div><img alt="" src="${commonResourcePath}/images/VeroModa_Legend.png" class="googleMapLegends"></div>
                
                <div><img alt="" src="${commonResourcePath}/images/Westside_Legend.png" class="googleMapLegends"></div>
          </div> 
       </div>
    <!-- </div> -->
  </div>
  
</div>
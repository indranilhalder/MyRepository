<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3&amp;"></script>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
 
<script>

$(document).ready(function(){
	 //Default Value fro latitue and longitude.
	 var lat='${latitude}';
	 var lot='${longitude}';
	 
	 if (navigator.geolocation) {
	    navigator.geolocation.getCurrentPosition(function(position) {
	         lat=position.coords.latitude;
	    	 lot=position.coords.longitude;
	    	 getDataFromServer(lat,lot);
	    },function() {
	    	getDataFromServer(lat,lot);
	    },{ 
			enableHighAccuracy: true,
			timeout : 30000
	});
	  }else{
		  getDataFromServer(lat,lot);
	  }
});

function getDataFromServer(lat,lot){
	$.ajax({
        url :  ACC.config.encodedContextPath +"/view/HomeStoreLocatorComponentController"+"/"+lat+"/"+lot+"/",
        type: "GET",
        dataType : "json",
  	    cache: false,
  	    contentType : "application/json; charset=utf-8",
        success : function(data) {
        var response = JSON.stringify(data);
        console.log("Ajax data sent successfully, <br/> The Data is "+response);
        locatorJson = JSON.parse(data).results;
        initialize(locatorJson,lat,lot);
        },
        error : function(xhr, data, error) {
      	  console.log("Error in processing Ajax. Error Message : " +error)
			}
       }); 
}

function initialize(locatorJson,lat,lot)
{   
	//Control Zoom labels.
	var initialZoom=Number('${initialZoom}');
    var markerZoom= Number('${markerZoom}');
     
    //Map Properties.
	var myCenter=new google.maps.LatLng(lat,lot);
	var marker;
    var mapProp = {
  	center:myCenter,
  	zoom:initialZoom,
  	zoomControl:true,
  	scrollwheel: false,
  	zoomControlOptions:{
  		position:google.maps.ControlPosition.RIGHT_TOP
  	},
  	disableDefaultUI:false,
  	mapTypeId:google.maps.MapTypeId.ROADMAP };

    var map=new google.maps.Map(document.getElementById("home-googleMap"),mapProp);
   
    // Create a DIV to hold the control and call HomeControl()
    staticLegends(map);
    //Info window.
    var infowindow = new google.maps.InfoWindow({
	  content:""
	  });
    
    var markers = new Array();
	  
  for (var i = 0; i < locatorJson.length; i++) { 
	 var icon="";
	 var marker="";
	 var mplStoreImage=locatorJson[i].mplStoreImage;
	 console.log(mplStoreImage)
	 var normalMarkerIcon="";
	 var onClickMarkerIcon="";
	 var onHoverIcon="";
	 if(!(locatorJson[i].regularImgUrl)){
		 marker=new google.maps.Marker({
	 		 position: new google.maps.LatLng(locatorJson[i].geoPoint.latitude,locatorJson[i].geoPoint.longitude)
		   });
	 }else{
		//Create marker.
	     marker=new google.maps.Marker({
 		 position: new google.maps.LatLng(locatorJson[i].geoPoint.latitude,locatorJson[i].geoPoint.longitude),
 		 icon:locatorJson[i].regularImgUrl
	   });
	 }
	 
	 markers.push(marker);
	 
	       //Create info box
			google.maps.event.addListener(infowindow,'closeclick',function(){
				 //removeGamma(map);
				});
	 
	//Create event listner for click on marker event.
    google.maps.event.addListener(marker, 'click', (function(marker, i) {
							        return function() {
							          infowindow.setContent("Store Name: "+locatorJson[i].displayName);
							          infowindow.open(map, marker);
							          map.setZoom(markerZoom);
							          map.setCenter(marker.getPosition());
							          if(!(locatorJson[i].onClickImgUrl)){
							        	  console.debug("No On image.");
							          }else{
							        	  console.info("locatorJson[i].onClickImgUrl");
							        	 // marker.setIcon(locatorJson[i].onHoverImgUrl);  
							          }
							        }
							      })(marker, i));
marker.setMap(map);	  
autoCenter(markers,map);
}
  
//Add a Home control that returns the user to London
  function HomeControl(controlDiv, map) {
    controlDiv.style.padding = '25px';
    var controlUI = document.createElement('div');
    controlUI.style.backgroundColor = '#ffffff';
    controlUI.style.textAlign = 'center';
   // opacity: 0.6;
  //  filter: 'alpha(opacity=60)';
   // controlUI.title = 'Set map to London';
    controlDiv.appendChild(controlUI);
    var controlText = document.getElementById('overLayStoreFinderText');
    controlText.style.display = 'block';
    controlUI.appendChild(controlText);
    
  }  
  
}

function autoCenter(markers,map ) {
    //  Create a new viewpoint bound
    var bounds = new google.maps.LatLngBounds();
    //  Go through each...
    for (var i = 0; i < markers.length; i++) {  
				bounds.extend(markers[i].position);
    }
    //  Fit these bounds to the map
    map.fitBounds(bounds);
  //To control max zoom label
	google.maps.event.addListenerOnce(map, 'bounds_changed', function(event){
		  if(this.getZoom()>18){
			  this.setZoom(18); 
		  }
		});
  }
 
function applyGamma(map) {

    var mapStyles = [{
        "stylers": [{
            "gamma": 7
        }]
    }];

    map.setOptions({
        styles: mapStyles
    });
}



function removeGamma(map) {

    var mapStyles = [{
        "stylers": [{
            "gamma": 0
        }]
    }];

    map.setOptions({
        styles: mapStyles
    });
}
function staticLegends(map){
	 // Create a DIV to hold the control and call HomeLegendsControl()
    var homeLegendsControlDiv = document.createElement('div');
    var homeLegendsControl = new HomeLegendsControl(homeLegendsControlDiv, map);
    homeLegendsControlDiv.index = 2;
	map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(homeLegendsControlDiv);
}

//Add a Home control that returns the user to London
function HomeLegendsControl(controlDiv, map) {
	
	var iconURLPrefix = ACC.config.commonResourcePath+"/images/";
    var icons = [
      iconURLPrefix + 'Bestseller_Legend.png',
      iconURLPrefix + 'CottonWorld_Legend.png',
      iconURLPrefix + 'Croma_Legend.png',
      iconURLPrefix + 'Dell_Legend.png',
      iconURLPrefix + 'Inc5_Legend.png',
      iconURLPrefix + 'Killer_Legend.png',
      iconURLPrefix + 'Lenovo_Legend.png',
      iconURLPrefix + 'Metro_Legend.png',
      iconURLPrefix + 'Tresmode_Legend.png',
      iconURLPrefix + 'Westside_Legend.png',
    ]
    var iconsLength = icons.length;
    
    var legendStyle = document.getElementById("legend");
    
    console.info("Style"+legendStyle);
    
    var legend = document.getElementById('legend');
     
      for (var i = 0; i < icons.length; i++) { 
    	  var div = document.createElement('div');
	      var img1=document.createElement('img');
	      img1.src=icons[i];
	      img1.className='googleMapLegends';
	      div.appendChild(img1);
         legend.appendChild(div);
    } 
      console.info('info'+legend);
      controlDiv.style.padding = '10px';
      var controlUI = document.createElement('div');
      controlUI.style.backgroundColor = 'transparent';
      controlUI.style.textAlign = 'center';
      controlUI.style.right='60px';
      controlDiv.appendChild(controlUI);
      controlUI.appendChild(legend);
  
}

</script>
<style> 
  .container_store_finder_home {
		margin: 0px 0px;
		    vertical-align: baseline;
	} 
	
.container_store_finder_home_map {
		margin: 0px 22px;
	}	
	
.overLayStoreFinderText {
       top: 64px;
       width:400px;
       display:block;
       padding-bottom:15px;
       
}

.overLayStoreFinderText h1 {
    margin: 10px 0 15px;
    font-size: 28px;
    font-weight: 500;
    margin-left: 15px;
    margin-right: 15px;
    text-align: center;
    line-height:1.3;
}
.overLayStoreFinderText span {
    margin-top: 15px;
    padding: 0 40px;
    text-align: center;
    }
@media (max-width: 1170px) {
  
  #legend {
     display:none;
  }
  .overLayStoreFinderText {
    position: relative;
    width:auto;
    top:0px;
    /* background: #F7F8FA; */
    margin-top:-20px;
    left:0px;
  }
  
  .overLayStoreFinderText span {
     word-wrap: break-word; 
     padding:0 0px;
        }
  
  .home-googleMap {
	width: 104%;
  }
    .overLayStoreFinderText h1{
    margin-left: 0px;
    margin-right: 0px;
    word-wrap: break-word;
  } 
  
}
 
/* .col-centered{
    float: none;
    margin: 0 auto;
} */
#legend {
background: transparent;
}

.overLayStoreFinderText a {
    font-size: 12px;
    margin: 15px 0;
  }
  
</style>
<div class="container_store_finder_home">
	<div class="row">
	 <div class="col-md-12 col-sm-12 col-lg-6">
	       <div id="overLayStoreFinderText123" class="overLayStoreFinderText">
		   <h1><spring:theme code="storelocator.home.beinspired.text1" text="Be inspired online, or at one of our partner stores."/></h1>
			<span><spring:theme code="storelocator.home.beinspired.text2" text="Our seamless online and in-store experiences allow
				you to shop, make returns, and earn rewards on all your purchases
				across brands online or in-store."/>
			 </span>
		  <a href="${request.contextPath}/aboutus" class="r2-arrow"><spring:theme code="storelocator.home.aboutus.link.text" text="Learn more about our services"/></a> 
		  <a href="${request.contextPath}/store-finder" class="r2-arrow">
				<spring:theme code="storelocator.home.storefinder.link.text" text="Find a store"/></a>
         </div>
		</div>
	
	  
          <!-- <div id="home-googleMap" class="home-googleMap"> -->
      
  </div>
  <div class="row"> 
   <div id="home-googleMap" class="home-googleMap"> </div>
  </div>		
  <div id="legend"> </div> 
</div>
 
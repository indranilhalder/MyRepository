<%@ page trimDirectiveWhitespaces="true"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3&amp;key=${googleApiKey}"></script>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="jquery.tools.min.js"></script>
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
	    });
	  }else{
		  getDataFromServer(lat,lot);
	  }
});

function getDataFromServer(lat,lot){
	$.ajax({
        url :  ACC.config.encodedContextPath +"/view/HomeStoreLocatorComponentController"+"/"+lat+"/"+lot,
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
  	disableDefaultUI:true,
  	mapTypeId:google.maps.MapTypeId.ROADMAP };

    var map=new google.maps.Map(document.getElementById("home-googleMap"),mapProp);
   
    // Create a DIV to hold the control and call HomeControl()
    var homeControlDiv = document.createElement('div');
    var homeControl = new HomeControl(homeControlDiv, map);
    homeControlDiv.index = 1;
    map.controls[google.maps.ControlPosition.LEFT_CENTER].push(homeControlDiv);
    
    staticLegends(map);
    //Info window.
    var infowindow = new google.maps.InfoWindow({
	  content:""
	  });
    
    var markers = new Array();
	  
  for (var i = 0; i < locatorJson.length; i++) { 
	 var icon="";
	 var marker="";
	 //Create marker.
	 if(!(locatorJson[i].mapIcon)){
		  marker=new google.maps.Marker({
		 		position: new google.maps.LatLng(locatorJson[i].geoPoint.latitude,locatorJson[i].geoPoint.longitude)
			   });
	 }else {
		 icon=locatorJson[i].mapIcon.url;
	     marker=new google.maps.Marker({
 		 position: new google.maps.LatLng(locatorJson[i].geoPoint.latitude,locatorJson[i].geoPoint.longitude),
 		 icon:icon
	   });
	 
	 }
	 markers.push(marker);
	 
	 //Create info box
			google.maps.event.addListener(infowindow,'closeclick',function(){
				 removeGamma(map);
				});
	 
	//Create event listner for click on marker event.
    google.maps.event.addListener(marker, 'click', (function(marker, i) {
							        return function() {
							          infowindow.setContent("Store Name: "+locatorJson[i].name);
							          infowindow.open(map, marker);
							          map.setZoom(markerZoom);
							          map.setCenter(marker.getPosition());
							          applyGamma(map);
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
	
	 // Setup the different icons and shadows
    var iconURLPrefix = 'http://maps.google.com/mapfiles/ms/icons/';
    var icons = [
      iconURLPrefix + 'red-dot.png',
      iconURLPrefix + 'green-dot.png',
      iconURLPrefix + 'blue-dot.png',
      iconURLPrefix + 'orange-dot.png',
      iconURLPrefix + 'purple-dot.png',
    ]
    var iconsLength = icons.length;
    
    var legendStyle = document.getElementById("legend");
    
     console.info("Style"+legendStyle);
    
    var legend = document.getElementById('legend');
     
      for (var i = 0; i < icons.length; i++) { 
    	 var div = document.createElement('div');
         div.innerHTML = "Sample Data" +'<img src="' + icons[i] + '"> ';
         div.style=legendStyle;
         legend.appendChild(div);
    } 
      console.info(legend);
      controlDiv.style.padding = '25px';
      var controlUI = document.createElement('div');
      controlUI.style.backgroundColor = '#ffffff';
      controlUI.style.textAlign = 'center';
      controlUI.style.right='60px';
     // opacity: 0.6;
     // filter: 'alpha(opacity=60)';
     // controlUI.title = 'Set map to London';
      controlDiv.appendChild(controlUI);
      //var controlText = document.getElementById('overLayStoreFinderText');
      controlUI.appendChild(legend);
  
}

</script>


<div id="home-googleMap" class="home-googleMap">
</div>
<div id="legend">
                         
          </div> 

<div id="overLayStoreFinderText" class="overLayStoreFinderText">
	<h1>Be inspired online, or at one of our partner stores.</h1> 
	<span>To Our seamless online and in-store experiences  
	    allow you to shop, make returns, and earn  
	    rewards on all your purchases across brands   online or in-store.
	</span>
	   
	   <a href="${request.contextPath}/aboutus" class="r2-arrow">Learn more about our services</a>
		<a href="${request.contextPath}/store-finder" class="r2-arrow"> Find a Store</a>
	 
</div>


<%-- <!-- another link. uses the same overlay -->
<a href="${request.contextPath}/store-finder"  style="text-decoration:none">
   Find  a Store
</a> --%>
 
ACC.storefinder = {

	_autoload: [
		["init", $(".js-store-finder").length != 0],
		["bindStoreChange", $(".js-store-finder").length != 0],
		["bindSearch", $(".js-store-finder").length != 0],
		"bindPagination"
	],
	
	storeData:"",
	storeId:"",
	coords:{},
	storeSearchData:{},

	createListItemHtml: function (data,id){

		var item="";
		item+='<li class="store-finder-navigation-list-entry">';
		item+='<input type="radio" name="storeNamePost" value="'+data.displayName+'" id="store-filder-entry-'+id+'" class="js-store-finder-input" data-id="'+id+'">';
		item+='<label for="store-filder-entry-'+id+'" class="js-select-store-label">';
		item+='<span class="store-finder-navigation-list-entry-info">';
		item+='<span class="store-finder-navigation-list-entry-name">'+data.displayName+'</span>';
		item+='<span class="store-finder-navigation-list-entry-address">'+data.line1+' '+data.line2+'</span>';
		item+='<span class="store-finder-navigation-list-entry-city">'+data.town+'</span>';
		item+='</span>';
		item+='<span class="store-finder-navigation-list-entry-distance">';
		item+='<span>'+data.formattedDistance+'</span>';
		item+='</span>';
		item+='</label>';
		item+='</li>';
		return item;
	},

	refreshNavigation: function (){
		var listitems = "";
		data = ACC.storefinder.storeData
		
		if(data){
			for(i = 0;i < data["data"].length;i++){
				listitems += ACC.storefinder.createListItemHtml(data["data"][i],i)
			}
	 
			ACC.storefinder.bindStoreTestChange();
		}
		$("storeFinder").addClass("display: block");
	},


	bindPagination:function ()
	{
        console.debug("bindPagination"+document.getElementById("store-finder-map"));
	},

	bindStoreChange:function()
	{  
		$(document).ready(function(e){
	        ACC.global.addGoogleMapsApi("ACC.storefinder.loadinitGoogleMap");
			 
		});
		
		$(document).on("change","#storelocator-query",function(e){
			 
			console.debug($("#storelocator-query").val())
			var inputtext=$("#storelocator-query").val();
			if(inputtext){ 
			$('#storeSearchTextValue').text(inputtext);
			}
		})

	},



	initGoogleMap:function(){

		if($(".js-store-finder-map").length > 0){
			ACC.global.addGoogleMapsApi("ACC.storefinder.loadGoogleMap");
		}
	},
 
	loadGoogleMap: function(){

		storeInformation = ACC.storefinder.storeId;
		 var markerZoom= Number($("#markerZoom").val());
		 var initialZoom=  Number($("#initialZoom").val());
		var mapIcons={"TATA Store":"https://maps.google.com/mapfiles/marker" + 'A' + ".png"};
		
		if($(".js-store-finder-map").length > 0)
		{			
			$(".js-store-finder-map").attr("id","store-finder-map")
			var centerPoint = new google.maps.LatLng(storeData[0].latitude,storeData[0].longitude);
			
			var mapOptions = {
				zoom: initialZoom,
				zoomControl: false,
				panControl: false,
				streetViewControl: false,
				zoomControl:true,
			  	zoomControlOptions:{
			  		position:google.maps.ControlPosition.RIGHT_TOP
			  	},
			  	disableDefaultUI:false,
				mapTypeId: google.maps.MapTypeId.ROADMAP,
				center: centerPoint
			}
			
			var map = new google.maps.Map(document.getElementById("store-finder-map"), mapOptions);
			 //To create bounds.
			 var bounds = new google.maps.LatLngBounds();
			
			for (var i = 0; i < storeData.length; i++) { 
			 var localStoreInfo=storeData[i];
				
			var comIcon="";
			if(!(localStoreInfo["iconUrl"])){
				comIcon="https://maps.google.com/mapfiles/marker" + 'A' + ".png"
			 }else {
				 comIcon=localStoreInfo["iconUrl"];
				 mapIcons[localStoreInfo["displayName"]] = comIcon;
			   }
				 
			var  marker = new google.maps.Marker({
				position: new google.maps.LatLng(localStoreInfo["latitude"], localStoreInfo["longitude"]),
				map: map,
				title: localStoreInfo["name"],
				icon: comIcon,
				//opacity:0.6
			});
			
			//Add marker to bounds
			bounds.extend(marker.position);
			var infowindow = new google.maps.InfoWindow({
				content: "",
				disableAutoPan: false,
				maxWidth:100
			});
			google.maps.event.addListener(infowindow,'closeclick',function(){
				ACC.storefinder.removeGamma(map);
				});
			google.maps.event.addListener(marker, 'click', (function(marker, i) {
		        return function() {
		          var infoMsg=storeData[i];
		          var infoString="<div style=\"width:200px; height:100px\">"+infoMsg["displayName"]+" "+infoMsg["name"]+"</br> Distance Appx."+infoMsg["formattedDistance"]+" </br>"+infoMsg["line1"]
					+" "+infoMsg["line2"];
		          var openingMsg="";
		          if(infoMsg["openings"]){
		        	  var opening=infoMsg["openings"];
		        	  for (var key in opening) {
		    	          var value = opening[key];
		    	          openingMsg +="</br>"+ key+" : "+value;
		    	        } 
		          }
		          infowindow.setContent(infoString+openingMsg+"</div>");
		          infowindow.open(map, marker);
		          map.setZoom(markerZoom);
		          map.setCenter(marker.getPosition());
		          ACC.storefinder.applyGamma(map);
		        }
		      })(marker, i));
			marker.setMap(map);	 
			}
			//Auto set bounds
		    map.fitBounds(bounds);
			/* Create a DIV to hold the control and call StoreFinderLegendsControl() */
		    var homeLegendsControlDiv = document.createElement('div');
		    var homeLegendsControl = new ACC.storefinder.StoreFinderLegendsControl(homeLegendsControlDiv, map);
		    homeLegendsControlDiv.index = 1;
			map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(homeLegendsControlDiv);
		}
		
	},


	bindSearch:function(){

		$(document).on("submit",'#storeFinderForm', function(e){
			e.preventDefault()
			var q = $(".js-store-finder-search-input").val();

			if(q.length>0){
				 var geocoder = new google.maps.Geocoder();
				var lat='0';
				var lng='0';
				geocoder.geocode({ 'address': q }, function(results, status) {
				    if (status == google.maps.GeocoderStatus.OK) {
				    	var searchLocation = results[0].geometry.location;
				    	console.log("Check for logs.")
				    	lat=searchLocation.lat();
				    	lng=searchLocation.lng();
				    	console.log(lat);
				    	ACC.storefinder.getInitStoreData(null,lat,lng);
				    }else{
				    	ACC.storefinder.getInitStoreData(null,lat,lng);
				    } 
				        }); 
				// ACC.storefinder.getInitStoreData(q);
			}else{
				if($(".js-storefinder-alert").length<1){
					var emptySearchMessage = $(".btn-primary").data("searchEmpty")
					//$(".js-store-finder").hide();
					$("#storeFinder").before('<div class="js-storefinder-alert alert alert-danger alert-dismissable" ><button class="close" type="button" data-dismiss="alert" aria-hidden="true">×</button>' + emptySearchMessage + '</div>');
				}
			}
		})

		//$(".js-store-finder").hide();
		$(document).on("click",'#findStoresNearMe', function(e){
			//e.preventDefault()
			$('#findStoresNearMe').addClass("disabled");
			ACC.storefinder.getInitStoreData(null,ACC.storefinder.coords.latitude,ACC.storefinder.coords.longitude);
			$('#findStoresNearMe').removeAttr("disabled");
		})


	},


	getStoreData: function(page){
		ACC.storefinder.storeSearchData.page = page;
		ACC.storefinder.storeSearchData.show="All"
		url= $(".js-store-finder").data("url");
		$.ajax({
			url: url,
			data: ACC.storefinder.storeSearchData,
			type: "get",
			success: function (response){
				console.info("ajax..got sucess full data.");
				console.info(response);
				if(response){
				ACC.storefinder.storeData = $.parseJSON(response);
				ACC.storefinder.refreshNavigation();
				}else{
					//Show error message.
					ACC.storefinder.showError();
				}
			}
		});
	},

	getInitStoreData: function(q,latitude,longitude){
		$(".alert").remove();
		data ={
			"q":"" ,
			"page":0
		}
		if(q != null){
			data.q = q;
		}

		if(latitude != null){
			data.latitude = latitude;
		}

		if(longitude != null){
			data.longitude = longitude;
		}

		ACC.storefinder.storeSearchData = data;
		ACC.storefinder.getStoreData(data.page);
		//$(".js-store-finder").show();
		//$(".store-finder-search").hide();
		/*$(".js-store-finder-pager-prev").attr("disabled","disabled")
		$(".js-store-finder-pager-next").removeAttr("disabled")*/
	},

	init:function(){
		//$("#findStoresNearMe").attr("disabled","disabled");
		var initialZoom=Number($("#initialZoom"));
		$('#findStoresNearMe').addClass("disabled");
		if(navigator.geolocation){
			navigator.geolocation.getCurrentPosition(
				function (position){
					ACC.storefinder.coords = position.coords;
					$('#findStoresNearMe').removeAttr("disabled");
				},
				function (error)
				{
					console.log("An error occurred... The error code and message are: " + error.code + "/" + error.message);
				}
			);
		}
	},
	loadinitGoogleMap:function(){
		 var initialZoom= Number($("#initialZoom").val());
		 var defLat=$("#defLatitude").val(); 
		 var defLot=$("#defLongitude").val(); 
		 var initialZoom=Number($("#initialZoom").val());
		 $(".js-store-finder-map").attr("id","store-finder-map");

		var centerPoint = new google.maps.LatLng(defLat,defLot);
		var mapOptions = {
			zoom: initialZoom,
			zoomControl: false,
			panControl: false,
			streetViewControl: false,
			zoomControl:true,
		  	zoomControlOptions:{
		  		position:google.maps.ControlPosition.RIGHT_TOP
		  	},
		  	disableDefaultUI:false,
			mapTypeId: google.maps.MapTypeId.ROADMAP,
			center: centerPoint
		}
	  var map = new google.maps.Map(document.getElementById("store-finder-map"), mapOptions);
		/* Create a DIV to hold the control and call StoreFinderLegendsControl() */
	    var homeLegendsControlDiv = document.createElement('div');
	    var homeLegendsControl = new ACC.storefinder.StoreFinderLegendsControl(homeLegendsControlDiv, map);
	    homeLegendsControlDiv.index = 1;
		map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(homeLegendsControlDiv);
	}
	,
	bindStoreTestChange:function()
	{  
			storeData=ACC.storefinder.storeData["data"];
			var storeId=$(this).data("id");
			console.info(storeData);
			ACC.storefinder.storeId = storeData[storeId];
			ACC.storefinder.initGoogleMap();

	},
	showError:function()
	{   
		var errorMsg=$("#storefinderNoresult").val();
		globalErrorPopup(errorMsg);
	} 
	,
applyGamma:function(map) {

	    var mapStyles = [{
	        "stylers": [{
	          "gamma": 10,
	           // "visibility":'off',
	            // "color":'0000ffff'
	        }]
	    }];

	    map.setOptions({
	        styles: mapStyles
	    });
	}
	,
removeGamma:function(map) {
	    var mapStyles = [{
	        "stylers": [{
	            "gamma": 0
	        }]
	    }];

	    map.setOptions({
	        styles: mapStyles
	    });
	},
	StoreFinderLegendsControl:function (controlDiv,map){
		//controlDiv.style.background='white';
		controlDiv.style.padding='10px';
		 // Setup the different icons and shadows
	    var iconURLPrefix = 'http://maps.google.com/mapfiles/ms/icons/';
	    
	    var icons = [
	      iconURLPrefix + 'red-dot.png',
	      iconURLPrefix + 'green-dot.png',
	      iconURLPrefix + 'blue-dot.png',
	      iconURLPrefix + 'orange-dot.png',
	      iconURLPrefix + 'purple-dot.png',
	      iconURLPrefix + 'pink-dot.png',      
	      iconURLPrefix + 'yellow-dot.png'
	    ]
	    var iconsLength = icons.length;
	    var controlUI = document.createElement('div');
	      controlUI.style.backgroundColor = '#ffffff';
	      controlUI.style.textAlign = 'center';
	      controlUI.style.right='60px';
	      controlUI.style.padding='10px';
	      controlDiv.appendChild(controlUI);
	      
	    for (var i = 0; i < icons.length; i++) { 
	    	 var div = document.createElement('div');
	         div.innerHTML = "Sample Data" +'<img src="' + icons[i] + '"> ';
	         controlUI.appendChild(div);
	    }
	    console.info(controlDiv);  
	  // map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(legend);
	    
	}
};
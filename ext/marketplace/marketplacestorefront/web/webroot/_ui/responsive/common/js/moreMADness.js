//Added by TCS to retrieve the product,cat id amd price
function callMSD(productID,catIdMSD,msdRESTURL)
{
	console.log("Product ID for MSD:- "+productID)
	console.log("Category ID for MSD:- "+catIdMSD);	
	loadMAD(productID,catIdMSD,msdRESTURL);
}

//Given by MAD
var loadMAD = function(productID, categoryID,msdRESTURL) {

   	
    try {
   
        track(['pageView', productID, categoryID]);
    } catch(err) {
        console.log('Error tracking the Page View: '+err.message);
    }
    try {   
        $("a.tw").click(function() {
            track(['socialShare', 'twitter', productID, categoryID]);
        });
        $("a.fb").click(function() {
            track(['socialShare', 'facebook', productID, categoryID]);
        });        
        $("a.gp").click(function() {
            track(['socialShare', 'google', productID, categoryID]);
        });        
        } catch(err) {
        console.log('Error Adding trackers for social events: '+err.message);
    }

    // This loads our uuid, it would have been created if not present by the first track call at page view above
    try {
        uuid = readCookie('MADid');
    } catch(err) {
        console.log('Error reading the MAD cookie: '+err.message);
    }

    var params = new Object();

    /* The following three parameters must be sent with this POST call */

    params.productID = productID;
    params.numResults = "16";
    params.details = "true";
    params.uuid = uuid;
    /* End of parameters */
    $.ajax({
                
    			//url: 'https://tatadev.madstreetden.com/moreWeb',
    			url:    msdRESTURL,
                type: 'POST',
                dataType: 'json',
                crossDomain: true,
                data: params,

                success: function (data, textStatus, xhr) {
                	
                	/*var jsonDummyData = '{"status": "success","data":[{"specialPrice": "250", "productCode": "10010330101140123650", "landingPage": "../p/MP000000000000302?msdclick=true", "categoryName": "Work Wear", "listingId": "MP000000000000302", "exclusive": false, "brand": "Biba", "P_AVAILABLE": "170", "mrp": "", "productName": "Sleeveless Solid Mens Nehru Linen Jacket - Biba", "colorSwatches": [["Turquoise", "#40e0d0"], ["Green", "#61B329"], ["Blue", "#18A0E3"],["Red", "#40e0d0"], ["Orange", "#61B329"], ["Black", "#18A0E3"],["Turquoise", "#40e0d0"], ["Green", "#61B329"], ["Blue", "#18A0E3"]], "categoryCode": "MPH1111100103", "weightage": "-299", "productImage": "http://pcme2e.tataunistore.com/images/1348Wx2000H/MP000000000000302_1348Wx2000H_201510071133.jpeg", "price": "Rs.500", "new": false, "variantSize": ["xl", "38g"], "P_SELLERARTICLESKU": "10011300000000000000"}, {"specialPrice": "700", "productCode": "987654324", "landingPage": "../p/MP000000000000302?msdclick=true", "categoryName": "Work Wear", "listingId": "987654324", "exclusive": false, "brand": "Biba", "P_AVAILABLE": "170", "mrp": "Rs.2000", "productName": "Sleeveless Solid Mens Nehru Linen Jacket - Biba", "colorSwatches": [["Turquoise", "#40e0d0"], ["Green", "#61B329"], ["Blue", "#18A0E3"]], "categoryCode": "MPH1111100103", "weightage": "-299", "productImage": "http://pcme2e.tataunistore.com/images/1348Wx2000H/MP000000000000302_1348Wx2000H_201510051738.jpeg", "price": "Rs.500", "new": true, "variantSize": ["xl", "38g", "37", "36"], "P_SELLERARTICLESKU": "10011300000000000000"}, {"specialPrice": "", "productCode": "987654324", "landingPage": "../p/987654324?msdclick=true", "categoryName": "Work Wear", "listingId": "987654324", "exclusive": true, "brand": "Biba", "P_AVAILABLE": "200", "mrp": "Rs.3000", "productName": "Sleeveless Solid Mens Nehru Linen Jacket - Biba", "colorSwatches": [], "categoryCode": "MPH1111100103", "weightage": "-640", "productImage": "http://pcme2e.tataunistore.com/images/252Wx374H/MP000000000000301_252Wx374H_201510061913.jpeg", "price": "Rs.1000", "new": false, "variantSize": [], "P_SELLERARTICLESKU": "123653098765485130011719"}]}';
                    var obj = JSON.parse(jsonDummyData);
                    
                    if (obj['status'] == "success") {
                        products = obj['data'];*/
                	
                   var jsonData = {}
                    jsonData = data;                	
                    if (jsonData['status'] == "success") {
                        products = jsonData['data'];
                        console.log(products[0]);
                        // The Div for the carousel is being constructed below from the JSON response from the MSD server.
                        dS = '';                        
                        dS = dS +    '<div class="trending wrapper">';
                        dS = dS +        '<h1><span style="color: black !important;">Visually Similar Items</span></h1>';                        
                        dS = dS +        '<div class="spacer">';
                        dS = dS +            '<div class="slider product ready">';
                        dS = dS +                '<div class="frame">';
                        dS = dS +                    '<ul class="overflow visuallySimilarItems">';
                        for (x in products){                                                   	
                        	dS = dS +                    '<li onmouseover="showBothMSD(this)" onmouseout="hideBothMSD(this)" class="look slide item productParentList" style="width: 100%;">';
                            dS = dS +                        '<a href="' + products[x]['landingPage'] + '" class="product-tile" ';                        

                            dS = dS +                            'onClick="track([\'carouselClick\', \'';
                            dS = dS +                               productID+'\', \''+categoryID+'\', \'';
                            dS = dS +                               products[x]['listingId']+'\', \''+products[x]['categoryCode'] + '\', '+x+']);">';                            
                            dS = dS +                            '<div class="image" style="line-height: 347px; height: 347px; width: 221px;">';                           
                            dS = dS +                                '<img class="product-image"';
                            dS = dS +                                   'src="' + products[x]['productImage'] + '" alt="">';
                            
                            // if variants are present show only Quick view
                            if ((products[x].hasOwnProperty('colorSwatches') == true && products[x]['colorSwatches'].length > 0)  || (products[x].hasOwnProperty('variantSize') == true && products[x]['variantSize'].length > 0)) {                            	
                            	dS = dS + 	'<div onClick="popupwindowMSD(event,\''+products[x]['listingId'] + '\')" class="MSDQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer;bottom: 0px;; z-index: -1;font-size: 12px;left:0; visibility: hidden; color: #00cbe9;font-family: \'icomoon\';display: block; width:222px; margin: 10px 0px; text-align: center;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;line-height: 30px;">Quick View</div>';                           	
                            	
                            }                            
                            // Quickview and Add to Bag ends
                            
                            dS = dS +                            '</div>';                            
                            dS = dS +                            '<div class="short-info">';
                 
                            if (products[x].hasOwnProperty('colorSwatches') == true) {
                            	
                                dS = dS + '<ul class="color-swatch" style="top: -3px; !important">';
                                swatches = products[x]['colorSwatches'];
                                var moreColors = products[x]['colorSwatches'].length - 6;                               
                                jQuery.each(products[x]['colorSwatches'], function (icount, itemColor) {	
                        			if(icount < 6){
                        				dS = dS +                            '<li><span style=';
                                        dS = dS +                                   '"background-color: ' + swatches[icount][1];                                       
                                        dS = dS +                                   '"title="' + swatches[icount][0] + '">';
                                        dS = dS +                            '</span></li>';
                        			}
                        	  });
                                
                                if(products[x]['colorSwatches'].length > 6)
                            	{
                            	dS = dS +  '<li style="font-size:12px;letter-spacing: 1px;border: none;padding: 0;">+'+moreColors+'more</li>';                            	
                            	}  
                                
                                dS = dS +  '</ul>';
                            }
                            dS = dS +                                '<p class="company">' + products[x]['brand'] + '</p>';                            
                            dS = dS +                                '<h3 class="product-name">' + products[x]['productName'] +'</h3>';                            
                            dS = dS +                                '<div class="price">';                           
                            
                            if (products[x].hasOwnProperty('mrp') == true && products[x]['mrp'].length > 0) 
                            {
                            	
                            	dS = dS +                                '<p class="normal">' + products[x]['mrp'] +'</p>';
                                 
                                 if (products[x].hasOwnProperty('specialPrice') == true && products[x]['specialPrice'].length > 0) {
                                      dS = dS +                                '<p class="sale">' + products[x]['specialPrice'] +'</p>';
                                  }
                                 else
                                 {
                                 dS = dS +                                '<p class="sale">' + products[x]['price'] +'</p>';
                                 }
                                 
                             }
                            else
                            	{
                            	dS = dS +                                    '<p class="normal">' + products[x]['price'] +'</p>';
                            	if (products[x].hasOwnProperty('specialPrice') == true && products[x]['specialPrice'].length > 0) {
                                     dS = dS +                                '<p class="sale">' + products[x]['specialPrice'] +'</p>';
                                 }
                            	}
                            
                            dS = dS +                                '</div>';
                            dS = dS +                            '</div>';
                            dS = dS +                        '</a>';
                            
                            // To display sizes                            
                            if (products[x].hasOwnProperty('variantSize') == true) {
                                sizes = products[x]['variantSize'];
                                if(sizes.length > 0)
                                {
                                	var allMSDsizes = ["XXS", "XS", "S", "M", "L", "XL", "XXL"];
                                	if(sizes != '' || sizes != null || sizes != "undefined"){
                              		  var sortedSizes = []; /*This will stay empty if it's a numerical size list*/
                              		  for(var i=0; i<allMSDsizes.length; i++) {
                              		  if(sizes.indexOf(allMSDsizes[i]) > -1) {
                              		  sortedSizes.push(allMSDsizes[i]); /*Include smallest sizes first*/
                              		  }
                              		  }
                              		  if(sortedSizes.length > 0) { /*Use this if it's a string-based size array*/
                              		  sizes = sortedSizes;
                              		  } else {
                              		  sizes.sort() /*Not a string-based size array, sort normally*/
                              		  }
                              		  dS = dS + '<span class="sizesAvailableMSD" style="padding:5px;">Size : ['+sizes+'] </span>';
                              		 }
                                }                                
                            } 
                            // Display size ends
                            
                            dS = dS +                    '</li>';
                        }
                        dS = dS +                '</ul>';
                        dS = dS +            '</div>';
                        dS = dS +                '<ul class="arrows">';

                        // TELIUM: The carouselSwipe events are tracked below

                        dS = dS +                    '<li class="next" onClick="track([\'carouselSwipe\', \''+ productID +'\', \'' + categoryID+'\']);"></li>';
                        dS = dS +                    '<li class="prev" onClick="track([\'carouselSwipe\', \''+ productID +'\', \'' + categoryID+'\']);"></li>';
                        dS = dS +                '</ul>';
                        dS = dS +            '</div>';
                        dS = dS +        '</div>';
                        dS = dS +    '</div>';                       
                        // This puts in our recommendations into the view-similar-items div                        
                        $("div.view-similar-items").html(dS).resize();                       
                        console.log("MAD visually similar items loaded");
                        
                        
                        $(".visuallySimilarItems").owlCarousel({
                            items : 5,
                            scrollPerPage: true,
                            itemsDesktop : [1199,4],
                            itemsDesktopSmall : [980,3],
                            itemsTablet: [768,2],
                            itemsMobile : [479,1],
                            navigation: true,
                            navigationText : [],
                            pagination:false,
                            rewindNav : false
                		});
                        
                        $(".visuallySimilarItems .owl-prev").click(function(){
                        	$(".view-similar-items .prev").click();
                        });
                        $(".visuallySimilarItems .owl-next").click(function(){
                        	$(".view-similar-items .next").click();
                        });
                        
                        
                    } else {
                        console.log("MAD visually similar items not available");
                    }
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr);
                    console.log(errorThrown);
                }
          });
}


//Make quickview and Add to cart visible and on top
function showBothMSD(productElementMSD) {
	var qvMSD = productElementMSD.getElementsByClassName("MSDQuickView")[0];	 
	qvMSD.style.zIndex = 11;
	qvMSD.style.visibility = "visible";	
	}


//Make quickview and Add to cart invisible and behind other divs
function hideBothMSD(productElementMSD) {	
	var qvMSD = productElementMSD.getElementsByClassName("MSDQuickView")[0];	
	qvMSD.style.zIndex = -1;
	qvMSD.style.visibility = "hidden";	
}

//Create pop-up Quickview window
function popupwindowMSD(e,productId) {	
	e.preventDefault();
	console.log("MSD Quick View:- " + productId);
	ACC.colorbox.open("QV",{
		href:ACC.config.encodedContextPath+"/p/"+productId+"/quickView"
});
}

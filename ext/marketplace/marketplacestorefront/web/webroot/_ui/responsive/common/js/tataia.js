/*Facebook Variables to be Provided by TCS*/

var fbID = '';
var fbAccessToken = '';

/*Which widget we are working with*/
var widgetMode = '';

/*Initialize company specific variables*/
//ecompany = 'dev.tul.com';
rootEP = $('#rootEPForHttp').val();
if(location.protocol === "https:") {
  rootEP = $('#rootEPForHttps').val();
}
recEndPoint = rootEP + '/SocialGenomix/recommendations/products/jsonp';

//******************************************************************************* Populating Dynamic Parameter Values For IA
var allsizes = ["XXS", "XS", "S", "M", "L", "XL", "XXL"];
var hotDropdownselected = 'All Department';
var sortDropdownselected = "";
var currentPageURL = window.location.href;  
ecompany		= $('#ecompanyForIA').val(); 
var DamMediaHost		= $('#DamMediaHost').val();
var mplStaticResourceHost		= $('#mplStaticResourceHost').val();
user_id	     	= getCookie("mpl-user");
user_type		= getCookie("mpl-userType");
spid		   	= $('#ia_product_code').val();
domain			= $('#ia_product_rootCategory_type').val();
search_string	= $('#ia_search_text').val();
searchCategory_id		= $('#selectedSearchCategoryId').val(); // For Normal search
searchCategory_idFromMicrosite		= $('#selectedSearchCategoryIdMicrosite').val(); // For Microsite search
var daysDif = '';
var is_new_product = false;

if (searchCategory_id){
	if(searchCategory_id.indexOf("MSH") > -1){
	category_id = searchCategory_id;
	}
	else if(searchCategory_id.indexOf("MBH") > -1){
		brand_id = searchCategory_id;
		}else if(searchCategory_id !== 'all'){
			seller_id = searchCategory_id;	
		}else{
			category_id = '';
		}
}
if (searchCategory_idFromMicrosite){ // only for microsite search
	if(searchCategory_idFromMicrosite.indexOf("MBH") > -1){
		brand_id = searchCategory_idFromMicrosite;
		}
		else{
				category_id = '';
			}
}
// Array[productCode] for wishlist and cart pages
$.each($('input[name=productArrayForIA]'),function(prodArrayIndex,val){  
	if(prodArrayIndex < 5){
	site_product_array.push(val.value);
	}
});

//For homepage,search,searchEmpty,wishlist,cartPage,orderConfirmation
site_page_type = $('#ia_site_page_id').val();

if(site_page_type == 'productDetails'){
	site_page_type = 'productpage';  
}


if(site_page_type == 'myStyleProfile'){
	site_page_type = 'viewAllTrending';  // My Recommendation Page
	$.each($('input[name=categoryArrayForIA]'),function(catArrayIndex,val){
		if(catArrayIndex < 5){
		category_array.push(val.value);
		}
	});
	$.each($('input[name=brandArrayForIA]'),function(brandArrayIndex,val){  
		if(brandArrayIndex < 5){
		brand_array.push(val.value);
		}
	});

}
if(currentPageURL.indexOf("/c/MSH") > -1 || currentPageURL.indexOf("/c/SSH") > -1)
{
  site_page_type = 'category_landing_page';
  category_id = currentPageURL.split('/').pop();
  if(category_id.indexOf('?') > 0) {
    category_id = category_id.substr(0, category_id.indexOf('?'));
  }
}
if(currentPageURL.indexOf("/s/") > -1){
  site_page_type = 'seller';
  seller_id = currentPageURL.split('/').pop();  
  if(seller_id.indexOf('?') > 0) {
    seller_id = seller_id.substr(0, seller_id.indexOf('?'));
  }
}
if(currentPageURL.indexOf("/c/MBH") > -1){
  site_page_type = 'brand';
  brand_id = currentPageURL.split('/').pop();  
    if(brand_id.indexOf('?') > 0) {
    brand_id = brand_id.substr(0, brand_id.indexOf('?'));
  }
}
if(currentPageURL.indexOf("/m/") > -1){
	site_page_type = 'seller';
	seller_id = $('#mSellerID').val();
}
var footerPageType  	= $('#ia_footer_page_id').val();
if(footerPageType === 'footerLinkPage'){
	//site_page_type = 'footerPage';
	site_page_type = 'marketplace';
}
if(currentPageURL.indexOf("/search/helpmeshop") > -1){
	category_id = $('#helpMeShopSearchCatId').val();
}
if(site_page_type === 'orderConfirmationPage'){
	site_page_type = 'orderConfirmation';
}
//******************************************************************************* 


/*Get prior information from IA-set Cookies and start timers*/
init_iaplugin();


/*Only proceed on page types with recommendations*/
site_page_array = [ "homepage", "search", "searchEmpty", "cartPage",
                    "orderConfirmation", "wishlist", "seller", "brand",
                    "productpage", "product", "category_landing_page", "viewSellers",
                    "viewAllTrending","marketplace"];

if(jQuery.inArray(site_page_type, site_page_array) > -1) {
  callTataRec();
}
 
/*
Check if user has logged into the site
*/
function checkUser() {
	  user_type = getCookie("mpl-userType");
	  if(user_type.indexOf("facebook") === 0 || user_type.indexOf("FACEBOOK_LOGIN") === 0) {
	    uid = fbID;

	    /*New login, use current credentials*/
	    if(getCookie("IAUSERTYPE") !== 'REGISTERED' || getCookie("IAUSERTYPE") !== 'site_user') {
	        /*New elevated id we're not familiar with, get a new session id before continuing */          
	        if(getCookie("IAUSERID").length > 0 && getCookie("IAUSERID").indexOf(uid) !== 0) {
	          jQuery.ajax({
	            type: "GET",
	            url: rootEP + '/SocialGenomix/recommendations/init/jsonp',
	            jsonp: 'callback',
	            dataType: 'jsonp',
	            data: { 'referring_url': document.referrer,
	                    'ecompany': ecompany },
	            contentType: 'application/javascript',
	            success: function(response) {
	              document.cookie = 'IASESSIONID='+response.session_id+'; path=/';
	              ssid = response.session_id;
	              callEventApi('login', null);
	            }
	          });
	        } else {
	          callEventApi('login', null);
	        }
	        callFBApi(uid, FB.getAccessToken(), ssid);
	      }
	  } else {
	    if(getCookie("IAUSERTYPE").indexOf("facebook") === 0 || getCookie("IAUSERTYPE").indexOf("FACEBOOK_LOGIN") === 0) { 
	      /*Previously logged into Facebook, not anymore.  Send former credentials in Logout Event*/
	      callEventApi('logout', {"user_id":getCookie("IAUSERID"), "user_type":getCookie("IAUSERTYPE")});
	    }
	    if(user_type.indexOf("anonymous") === 0 || user_type.indexOf("session") === 0) {
	      /*Set if we're a session user*/
	      if(getCookie("IAUSERTYPE").indexOf("REGISTERED") === 0 || getCookie("IAUSERTYPE").indexOf("site_user") === 0) { 
	        /*Previously logged in as site user, not anymore.  Send former credentials in Logout Event*/
	        callEventApi('logout', {"user_id":getCookie("IAUSERID"), "user_type":getCookie("IAUSERTYPE")});
	      }
	      user_type = "session";
	      uid = ssid;
	    } else if (user_type.indexOf("REGISTERED") === 0 || user_type.indexOf("site_user") === 0) {
	      /*Set if we're a site user*/
	      uid = getCookie("mpl-user");
	      user_type = "site_user";

	      /*New login, use current credentials*/
	      if(getCookie("IAUSERTYPE") !== 'site_user') {
	        /*New elevated id we're not familiar with, get a new session id before continuing */          
	        if(getCookie("IAUSERID").length > 0 && getCookie("IAUSERID").indexOf(uid) !== 0) {
	          jQuery.ajax({
	            type: "GET",
	            url: rootEP + '/SocialGenomix/recommendations/init/jsonp',
	            jsonp: 'callback',
	            dataType: 'jsonp',
	            data: { 'referring_url': document.referrer,
	                    'ecompany': ecompany },
	            contentType: 'application/javascript',
	            success: function(response) {
	              document.cookie = 'IASESSIONID='+response.session_id+'; path=/';
	              ssid = response.session_id;
	              callEventApi('login', null);
	            }
	          });
	        } else {
	          callEventApi('login', null);
	        }
	        document.cookie = 'IAEMAILID='+uid+'; path=/';
	      }
	    }
	  }
	  document.cookie='IAUSERID='+uid+'; path=/';
	  document.cookie='IAUSERTYPE='+user_type+'; path=/';
	}

/*
Scan through the webpage and call the APIs for each element we detect
*/
function callForEachElement(params) {
  /*Check against all product widget elements*/
  for (var i=0; i<productWidgetElement.length; i++) {
    if(document.getElementById(productWidgetElement[i]) !== null) {
      var endpoint = '/SocialGenomix/recommendations/products/';
      if(productWidget[i] === "normal") { /*Normal is formatted sligtly differently*/
        endpoint += 'jsonp';
      } else {
        endpoint += productWidget[i] + '/jsonp';
      }


      if (productWidget[i].indexOf("hot") === 0 && 
          site_page_type === "viewAllTrending") {
        params.count = '100';
      } else {
        params.count = '15';
      }
      if(productWidget[i].indexOf("search") === 0 && site_page_type === 'viewAllTrending'){
    	  params.count = '100';
      }
      if(productWidget[i].indexOf("search") === 0 && site_page_type === 'search'){
    	  params.count = '15';
      }
      params.htmlElement = productWidgetElement[i];
      callRecApi(params, rootEP + endpoint);
    }
  }

  /*Check against all brand widget elements*/
  for (var i=0; i<brandWidgetElement.length; i++) {
    if(document.getElementById(brandWidgetElement[i]) !== null) {
      var endpoint = '/SocialGenomix/recommendations/';
      endpoint += brandWidget[i] + '/jsonp';

      params.count = '9';
      params.htmlElement = brandWidgetElement[i];
      callRecApi(params, rootEP + endpoint);
    }
  }

  /*Check against all category widget elements*/
  for (var i=0; i<categoryWidgetElement.length; i++) {
    if(document.getElementById(categoryWidgetElement[i]) !== null) {
      var endpoint = '/SocialGenomix/recommendations/';
      endpoint += categoryWidget[i] + '/jsonp';

      params.count = '8';
      if(categoryWidgetElement[i].indexOf('ia_categories_recent') > -1) {
          params.count = '2';
        }
      params.htmlElement = categoryWidgetElement[i];
      callRecApi(params, rootEP + endpoint);
    }
  }
  
  /*Check against all collection widget elements*/
  for (var i=0; i<collectionWidgetElement.length; i++) {
    if(document.getElementById(collectionWidgetElement[i]) !== null) {
      var endpoint = '/SocialGenomix/recommendations/';
      endpoint += collectionWidget[i] + '/jsonp';

      params.count = '3';
      params.htmlElement = collectionWidgetElement[i];
      callRecApi(params, rootEP + endpoint);
    }
  }
}

/*Filter based on categories, price ranges, and sale status*/
function getFilteredRecommendations(widgetElement, respData, priceRanges, sale, requestId) {
  /*Return an array where each index corresponds to the html of a page*/
  var filteredRespData = [];
  var page = -1;
  var afterFilter = 0;

  jQuery.each(respData, function() {
    /*Narrow by whether product is on sale, exclude product if the price is not less than original price*/
    //if((parseInt(this.price) >== parseInt(this.original_price)) && sale.length > 0)  {
    if((parseInt(this.discounted_price) === 0) && sale.length > 0)  {
      return;
    }
    /*Check if within sale range if price range specified*/
    var saleSatisfied = false;
    if(parseInt(this.discounted_price) > 0){
    	saleSatisfied = true;
    }else{//no sale filter
    	saleSatisfied = false; 
    }
    /*Check if within price range if price range specified*/
    var priceSatisfied = false;
    if(priceRanges.length === 0) { //no price filter
      priceSatisfied = true;  
    }
    for (var i=0; i<priceRanges.length; i++) {    	
      var prices = priceRanges[i].split(' - ');
      if(priceRanges[i].indexOf('And') > -1){
    	var  pricess = priceRanges[i].replace('And','-');
    	prices = pricess.split(' - ');
  	}
      var min = parseInt(prices[0].substr(1));
      var max = parseInt(prices[1].substr(1));
      if(isNaN(max)){
    	  max = '100000000';
      }
      if(min <= parseInt(this.price) && parseInt(this.price) <= max) {
        priceSatisfied = true;
        break; //condition met, stop searching
      }
    }
    /*If all our conditions are met, make HTML for this and add it to the page*/
    if(priceRanges.length > 0 && sale.length > 0){

    	   if (priceSatisfied === true && saleSatisfied === true){
    	      if(afterFilter % 20 === 0) {
    	        page++;
    	        filteredRespData[page] = "";
    	      }
    	      filteredRespData[page] += makeProductHtml(widgetElement, this, requestId);
    	      afterFilter++;
    	  }
    	} else if(priceRanges.length > 0 ) {
    	if(priceSatisfied === true ){
    	      if(afterFilter % 20 === 0) {
    	        page++;
    	        filteredRespData[page] = "";
    	      }
    	      filteredRespData[page] += makeProductHtml(widgetElement, this, requestId);
    	      afterFilter++;
    	  }
    	} else if(sale.length > 0 ) {
    	if (saleSatisfied === true ){
    	      if(afterFilter % 20 === 0) {
    	        page++;
    	        filteredRespData[page] = "";
    	      }
    	      filteredRespData[page] += makeProductHtml(widgetElement, this, requestId);
    	      afterFilter++;
    	  }
    	}else{
    		if(afterFilter % 20 === 0) {
    	        page++;
    	        filteredRespData[page] = "";
    	      }
    	      filteredRespData[page] += makeProductHtml(widgetElement, this, requestId);
    	      afterFilter++;
    	}
  });
  for (var i=0; i<5; i++) {
	  if(i <= page){
		  $('#iapage'+(i+1)).css("visibility", "visible");
		  $('#iapage_next').css("visibility", "visible");
		  $('#iapage'+(i+1)).removeClass('active');
          $('#iapage1').addClass('active');
	  }else{
		  $('#iapage'+(i+1)).css("visibility", "hidden");
		  $('#iapage_next').css("visibility", "hidden");
	  }
  }
  return filteredRespData;
}

function reorderRecommendations(widgetElement, respData, requestId) {
  var reorderedData = "";
  jQuery.each(respData, function() {
    reorderedData += makeProductHtml(widgetElement, this, requestId);
  });
  return reorderedData;
}
function reorderRecommendationGrid(widgetElement, respData, requestId) {
  var reorderedData = [];
  var i = 0;
  var page = -1;
  jQuery.each(respData, function() {
    if(i % 20 === 0) {
      page++;
      reorderedData[page] = "";
    }
    reorderedData[page] += makeProductHtml(widgetElement, this, requestId);
    i++;
  });
  return reorderedData;
}

/*TCS-provided add to cart code*/
function submitAddToCart(site_productid,site_ussid){
    var site_product_id = site_productid;
    var site_uss_id = site_ussid;
   
    var addToCartFormData='qty=1&pinCodeChecked=false&productCodePost='+site_product_id+'&wishlistNamePost=N&ussid='+site_uss_id+'&CSRFToken='+ ACC.config.CSRFToken +'';
  $.ajax({
    url : ACC.config.encodedContextPath+'/cart/add',
    data : addToCartFormData,
    type : 'post',
    cache : false,
    success : function(data) {
		if(data.indexOf("cnt:") >= 0){
		$("#status"+site_product_id).html("");
		//$("#status"+site_product_id).html("<font color='#00CBE9'>Bagged and ready!</font>");
		//$("#status"+site_product_id).show().fadeOut(5000);
		//ACC.product.displayAddToCart(data,formId,false);
		ACC.product.showTransientCart(site_uss_id); 
		$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").text(data.substring(4));
		
		//TISEE-882
		if(window.location.href.toLowerCase().indexOf('cart')>=0)
		{
			location.reload();
		}
		
		}
		else if(data=="reachedMaxLimit") {
			$("#status"+site_product_id).html("");
			$("#status"+site_product_id).html("<br/><font color='#ff1c47'>You are about to fill your bag completely!Please decrease the item quantity!</font>");
			$("#status"+site_product_id).show().fadeOut(5000);
		}
		
		else if(data=="crossedMaxLimit"){
			$("#status"+site_product_id).html("");
			$("#status"+site_product_id).html("<font color='#ff1c47'>Bag is full!</font>");
			$("#status"+site_product_id).show().fadeOut(5000);
		}
		else if(data=="outofinventory"){
			 $("#status"+site_product_id).html("<font color='#ff1c47'>Sorry, we don't seem to have the quantity you need. You might want to lower the quantity.</font>");
			 $("#status"+site_product_id).show().fadeOut(6000);
	   	     return false;
		}
		else if(data=="willexceedeinventory"){
			 $("#status"+site_product_id).html("<font color='#ff1c47'>Please decrease the quantity</font>");
			 $("#status"+site_product_id).show().fadeOut(6000);
	   		 return false;
		}
		else{
			$("#status"+site_product_id).html("");
			$("#status"+site_product_id).html("<br/><font color='#ff1c47'>Oops!Something went wrong!</font>");
			$("#status"+site_product_id).show().fadeOut(5000);
		}
	
	},
  	complete: function(){
        //$('#ajax-loader').hide();
    },
	error : function(resp) {
	//	alert("Add to Bag unsuccessful");
	}

  });
  if(spid.indexOf(site_product_id) === -1) {
	    params = {'count' : '0'};
	    params = buildParams(params);
	    callRecApi(params, rootEP + '/SocialGenomix/recommendations/products/jsonp');
	    //console.log(params);
	  }
	  callEventApi('add_to_cart', { "pname" : ['site_product_id','quantity'],
	                                "pvalue" : [spid, '1'] });
}

/*Make quickview visible and on top*/
function showQuickview(productElement) {

  var qv = productElement.getElementsByClassName("IAQuickView")[0];
  qv.style.zIndex = 11;
  qv.style.visibility = "visible";
}
/*Make quickview and Add to cart visible and on top*/
function showBoth(productElement) {

  var qv = productElement.getElementsByClassName("IAQuickView")[0];
  var a2c = productElement.getElementsByClassName("iaAddToCartButton")[0];
  qv.style.zIndex = 11;
  qv.style.visibility = "visible";
  a2c.style.zIndex = 11;
  a2c.style.visibility = "visible";
}
/*Make quickview and Add to cart invisible and behind other divs*/
function hideBoth(productElement) {
  var qv = productElement.getElementsByClassName("IAQuickView")[0];
  var a2c = productElement.getElementsByClassName("iaAddToCartButton")[0];
  qv.style.zIndex = -1;
  qv.style.visibility = "hidden";
  a2c.style.zIndex = -1;
  a2c.style.visibility = "hidden";
}
/*Make quickview invisible and behind other divs*/
function hideQuickView(productElement) {
  var qv = productElement.getElementsByClassName("IAQuickView")[0];
  qv.style.zIndex = -1;
  qv.style.visibility = "hidden";
}
/*Create pop-up Quickview window*/
function popupwindow(productId) {
	ACC.colorbox.open("QV",{
		href: ACC.config.encodedContextPath+"/p/"+productId+"/quickView",
		onComplete: function(){
			$(".imageList ul li img").css("height", "102px");
		}
});
	  params = {'count' : '0', 'site_product_id': productId};
	  params = buildParams(params);
	  callRecApi(params, rootEP + '/SocialGenomix/recommendations/products/jsonp');
}
function compareDateWithToday(SaleDate) {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();

	if(dd<10) {
	    dd='0'+dd
	} 
	if(mm<10) {
	    mm='0'+mm
	} 

	var today = new Date(mm+'/'+dd+'/'+yyyy+' 00:00:00');
	 // adjust diff for for daylight savings
		//var SaleDate = new Date("10/21/2015 00:00:00");
	 var hoursToAdjust = Math.abs(today.getTimezoneOffset() /60) - Math.abs(SaleDate.getTimezoneOffset() /60);
	 // apply the tz offset
	 SaleDate.addHours(hoursToAdjust); 
	    // The number of milliseconds in one day
	    var ONE_DAY = 1000 * 60 * 60 * 24
	    // Convert both dates to milliseconds
	    var today_ms = today.getTime()
	    var SaleDate_ms = SaleDate.getTime()
	    // Calculate the difference in milliseconds
	    var difference_ms = Math.abs(today_ms - SaleDate_ms)
	    // Convert back to days and return
	    return Math.round(difference_ms/ONE_DAY)
	}
	// you'll want this addHours function too 

	Date.prototype.addHours= function(h){
	    this.setHours(this.getHours()+h);
	    return this;
	}
/*Creates HTML of individual products*/
function makeProductHtml(widgetElement, obj, rid) { 
  /*This is a bad image*/
  if (typeof obj.image_url === "undefined") {
	  return;
  }
  if(obj.start_date != undefined){
	  daysDif = compareDateWithToday(new Date(obj.start_date))
	  if((daysDif < 8) && (daysDif >= 0)){
	  is_new_product = true;
	  }
  }
  
  if(obj.colors!= undefined){
		jQuery.each(obj.colors, function (icount, itemColor) {	
			if(itemColor == 'Pewter'){
				obj.colors[icount] = '#8E9294';
			}else if(itemColor == 'Peach'){
				obj.colors[icount] = '#FFDAB9';
			}else if(itemColor == 'Multi'){
				obj.colors[icount] = '/store/_ui/responsive/common/images/multi.jpg';
			}else if(itemColor == 'Metallic'){
				obj.colors[icount] = '#37FDFC';
			}
	  });
}

	  var IAurl = obj.url + '/store/mpl/en/p/'+obj.site_product_id+'/?iaclick=true&req=' + rid; /*iaclick=true for tracking our clicks vs. other services, pass request id to track clicks*/
	  if(spid.length > 0) { /*pass if product page or if this is applicable for whatever other reason*/
	    IAurl += '&rspid=' + spid;
	  }
	  var html = '';
	 
	  
	 if((obj.colors != null && obj.colors.length < 2) && (obj.sizes != null && obj.sizes.length < 2)){ 
		 html += '<li onmouseover="showBoth(this)" onmouseout="hideBoth(this)" class="look slide ' + widgetElement + '_list_elements productParentList" style="display: inline-block; width: 221px; margin-left: 10px; margin-right: 10px; height: 500px; margin-bottom: 20px;position: relative;">';
		 html += '<div onclick=popupwindow("'+obj.site_product_id+'") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 31%; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; margin: 10px 0; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height:70px;width: 108px;font-size:12px;"><span>Quick View</span></div><div onclick=submitAddToCart("'+obj.site_product_id+'","'+obj.site_uss_id+'") class="iaAddToCartButton" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 26%; z-index: -1; visibility: hidden; color: #00cbe9;display: block; margin: 35px 108px; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width: 109px;font-size:12px;"><span>Add To Bag</span></div>';
		
	 }else{
		 html += '<li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide ' + widgetElement + '_list_elements productParentList" style="display: inline-block; width: 221px; margin-left: 10px; margin-right: 10px; height: 500px; margin-bottom: 20px;position: relative;">';
		 html += '<div onclick=popupwindow("'+obj.site_product_id+'") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 31%; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; margin: 10px 0; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width: 218px;font-size:12px;"><span>Quick View</span></div>';
		 
	 }
	  html += '<a href="'+IAurl+'" class="product-tile" style="height: 423px; position: relative;">';
	  if(obj.image_url.indexOf("/") > -1){
		  html += '<div class="image" style="position: relative; left: 0; line-height: 347px; height: 347px;"><img class="product-image" style="font-size: 16px;text-overflow: ellipsis;" src="'+obj.image_url+'" alt="'+obj.name+'"/>';
		 if(is_new_product == true){
		  html += '<img class="new brush-strokes-sprite sprite-New" style="z-index: 0; display: block;margin-left: 14px;margin-top: 5px;" src="/store/_ui/responsive/common/images/transparent.png"/>';
		 }
		 if(obj.online_exclusive == true){
			  html += '<div class="online-exclusive" style="bottom: 17px !important;"><img class="brush-strokes-sprite sprite-Vector_Smart_Object" src="/store/_ui/responsive/common/images/transparent.png"><span>online exclusive</span></div>';  
		  }
		  html += '</div>';
		  
	  }else{
		  html += '<div class="image" style="position: relative; left: 0; line-height: 347px; height: 347px;"><img class="product-image" style="font-size: 16px;text-overflow: ellipsis;" src="/store/_ui/desktop/theme-blue/images/missing-product-300x300.jpg" alt="'+obj.name+'"/>';
		  if(is_new_product == true){
			  html += '<img class="new brush-strokes-sprite sprite-New" style="z-index: 0; display: block;margin-left: 14px;margin-top: 5px;" src="/store/_ui/responsive/common/images/transparent.png"/>';
			 }
		  if(obj.online_exclusive == true){
			  html += '<div class="online-exclusive" style="bottom: 18px !important;"><img class="brush-strokes-sprite sprite-Vector_Smart_Object" src="/store/_ui/responsive/common/images/transparent.png"><span>online exclusive</span></div>';  
		  }
		  html += '</div>';
		 
	  }
	  //html += '<div class="image" style="position: absolute; left: 0; line-height: 347px; height: 347px; width: 221px; background:center no-repeat url('+obj.image_url+'); background-size:contain;"></div>';
	  html += '<div class="short-info ia_short-info" style="position: relative; bottom: 0; left: 0; height: 66px; width: 221px;">';
	  html += '<ul class="color-swatch" style="top: -3px; !important;margin-right: 9px;">';
	  if(obj.colors.length < 3){
			jQuery.each(obj.colors, function (icount, itemColor) {	
				if(icount == 1){
				html += '<li style="padding: 0;margin-bottom: 0px;"><span style="background-color: '+itemColor+';" title='+itemColor+'></span></li>';
				}
		  });
	}else if(obj.colors.length > 2){
			jQuery.each(obj.colors, function (icounts, itemColors) {	
				if(icounts < 6){
				html += '<li style="padding: 0;margin-bottom: 0px;"><span style="background-color: '+itemColors+';" title='+itemColors+'></span></li>';
				}
		  });
	} 
		

			var moreColors = obj.colors.length - 6 ;
			if(obj.colors.length > 6){
			html += '<li style="font-size:12px;letter-spacing: 1px;border: none;padding: 0;margin-bottom: 0px;">+'+moreColors+'more</li>';
			}


	  html += '</ul>';
	  html += '<p class="company" >'+obj.brand+'</p>';
	  html += '<span class="product-name" style="text-overflow: ellipsis;word-break: break-word;">'+obj.name+'</span>';
	  html += '<div class="price">';
	  
		 
	  
	  // MOP == price,MRP== original price and deiscount price --Below rule is based on MRP > price > discount_price, MRP should be striked out always
	  if(parseInt(obj.discounted_price) > 0){
	  if(obj.discounted_price != null || obj.discounted_price != '' || obj.discounted_price != "undefined" ){
		  
		  if(obj.original_price != null && parseInt(obj.original_price) > parseInt(obj.discounted_price) ){
			  html += '<p class="old mrprice">₹'+parseInt(obj.original_price)+'</p>';
		  }
		  else if(obj.price != null && parseInt(obj.price) > parseInt(obj.discounted_price) && parseInt(obj.price) > 0){
			  html += '<p class="old moprice">₹'+parseInt(obj.price)+'</p>';
		  }
		  html += '<p class="sale discprice">₹'+parseInt(obj.discounted_price)+'</p>';
	  }
	  }
	  else if(obj.price != null || obj.price != '' || obj.price != "undefined"){
		  if(parseInt(obj.price) > 0){
		  if(obj.original_price != null && parseInt(obj.original_price) > parseInt(obj.price) ){
			  html += '<p class="old mrprice">₹'+parseInt(obj.original_price)+'</p>';
		  }
		  html += '<p class="sale moprice">₹'+parseInt(obj.price)+'</p>';
		  }
	  }
	  if(!obj.sizes){
		  html += '</div></div></a>';
		  html += '<p style="font-size: 12px;margin-top: 33px;color: rgb(255, 28, 71);" id="status'+obj.site_product_id+'"></p>';
		  html += '</li>';
		  return html;
	  }
	  else if(obj.sizes != '' || obj.sizes != null || obj.sizes != "undefined"){
		  if(obj.sizes.length > 0){
		  var sortedSizes = []; /*This will stay empty if it's a numerical size list*/	  
		  $.each(obj.sizes, function(index, item) {//Converting obj.sizes array values to UPPERCASE
			  obj.sizes[index] = item.toUpperCase();
			});		  
		  for(var i=0; i<allsizes.length; i++) {
		  if(obj.sizes.indexOf(allsizes[i]) > -1) {
		  sortedSizes.push(allsizes[i]); /*Include smallest sizes first*/
		  }
		  }
		  if(sortedSizes.length > 0) { /*Use this if it's a string-based size array*/
		  obj.sizes = sortedSizes;
		  } else {
		  obj.sizes.sort() /*Not a string-based size array, sort normally*/
		  }
		   	html += '<span style="padding-bottom: 0;" class="sizesAvailable">Size : ['+obj.sizes+'] </span>';
		  }
		  } 
	  html += '</div></div></a>';
	  html += '<p style="font-size: 12px;margin-top: 33px;color: rgb(255, 28, 71);" id="status'+obj.site_product_id+'"></p>';
	  html += '</li>';
	  return html;
	}

/*Call Recommendation API, retry if no session id*/
function callTataRec() {
  if(ssid) {
    /*See if we have any login credentials*/
    checkUser();
    /*Initialize params object we'll be passing around*/
    var params = {};
    if (site_page_type === "product" || site_page_type === "productpage") {
      document.cookie='prev_start_time=' + start_time.getTime() + '; path=/';      
      /*Check previous pages, add extra parameters if applicable*/
      refCheck();
      if (referring_request_id) {
        jQuery.extend(params, {'referring_request_id': referring_request_id});
        if (referring_site_product_id) {
          jQuery.extend(params, {'referring_site_product_id': referring_site_product_id});
        }
      }

      callForEachElement(buildParams(params));
    } else if (site_page_type === "marketplace") {
      /*We will be doing something else here soon*/
      callForEachElement(buildParams(params));      
    } else if (site_page_type === "search" || site_page_type === "searchEmpty" || site_page_type === "category" || site_page_type === "brand") {
      /*These pages contain grid view
      params.count = 100;*/
      callForEachElement(buildParams(params));
    } else {
      callForEachElement(buildParams(params));
    }
  } else {
    /*We haven't initialized, get a session id from IA*/
    jQuery.ajax({
      type: "GET",
      url: rootEP + '/SocialGenomix/recommendations/init/jsonp',
      jsonp: 'callback',
      dataType: 'jsonp',
      data: { 'referring_url': document.referrer },
      contentType: 'application/javascript',
      success: function(response) {
        document.cookie = "IASESSIONID="+response.session_id+'; path=/';
        ssid = response.session_id;
        callTataRec();
      }
    });
  }
}


function iaSortNameAsc(a, b){
	  var aName = a.name.toLowerCase();
	  var bName = b.name.toLowerCase(); 
	  return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
	}
	function iaSortNameDesc(a, b){
	  var aName = a.name.toLowerCase();
	  var bName = b.name.toLowerCase(); 
	  return ((aName > bName) ? -1 : ((aName > bName) ? 1 : 0));
	}
	function iaSortPriceAsc(a, b){
	  return ((a.price < b.price) ? -1 : ((a.price > b.price) ? 1 : 0));
	}
	function iaSortPriceDesc(a, b){
	  return ((a.price > b.price) ? -1 : ((a.price < b.price) ? 1 : 0));
	}

/*Given a response and widget kind, create the HTML we use to update page*/
function updatePage(response, widgetMode) {
  var widgetElement = "";
  if(typeof response.data === "undefined") {
    /*Either analytics is down or we passed a bad parameter*/
    return;
  }

  /*Product Widgets*/
  if(jQuery.inArray(widgetMode, productWidget) > -1) {
    /*So we can replace the same widget if we're narrowing down*/
    if(site_page_type !== 'category_landing_page' && widgetMode === "hot_in_category") {
      widgetMode = "hot";
    } else if(site_page_type !== 'category_landing_page' && widgetMode === "hot_in_category"){
    	widgetMode = "hot";
    }
    /*If it doesn't exist, we can stop*/
    widgetElement = productWidgetElement[jQuery.inArray(widgetMode, productWidget)];
    if (!document.getElementById(widgetElement)) {
      return;
    }
    /*It exists, lets name the payload because we may be modifying it*/
    var respData = response.data.recommendations;

    /*Use Carousel by default*/
    var slider = true;
    /*But for cases where we have an array of recommendations the size of 100, we will be using grid view*/
 

    if(!respData){
    	//console.log('No response data found for *'+widgetMode+'* wigdet');
    	return;
    }
    
    if(site_page_type === "viewAllTrending") {
            slider = false;
     }	
    	if(widgetMode === "recent"){
    		slider = true;
    	}
    

    /*Make everything blank to begin with*/
    document.getElementById(widgetElement).innerHTML = "";
    html = '';
    recIndex = 0;

    /*Staticly-built list of filters*/

    var categoryFilters = [] ;
    /* Category code for Dropdown Filter in hot and search widgets */
    var categoryCodeForFilters = [] ;
    $.each($('input#for_ia_hot_dropdown_name'),function(i,val){  
    	categoryFilters.push(val.value);
	});
    $.each($('input#for_ia_hot_dropdown_code'),function(i,val){  
    	categoryCodeForFilters.push(val.value);
	});
    
    /*SortBY dropdown*/
    var sortHtml = '<div class="select-view ia_select-view-sort">';
    	sortHtml += '<div class="select-list ia_select-list-sort"><span class="selected sortSelected">Sort by: '+sortDropdownselected+'</span><ul id="ia_category_select" style="width: auto;">';
    	sortHtml += '<li class="sort_li" id="name-asc">Name: A to Z</li>';
    	sortHtml += '<li class="sort_li" id="name-desc">Name: Z to A</li>';
    	sortHtml += '<li class="sort_li" id="price-asc">Price: Low to High</li>';
    	sortHtml += '<li class="sort_li" id="price-desc">Price: High to Low</li>';
    	sortHtml += '</ul></div></div>';
 
    var catHtml = '<div class="select-view ">'; 
    catHtml += '<div class="select-list"><span class="selected hotSelected">'+hotDropdownselected+'</span><ul id="ia_category_select" style="width: auto;">';
    for (var i=0; i<categoryFilters.length; i++) {
    	if(i==0){
    		 catHtml += '<li class="category_li" id="allCat">All Department</li>';
    	}
      catHtml += '<li class="category_li" id="'+categoryCodeForFilters[i]+'">'+categoryFilters[i]+'</li>';
    } 
    catHtml += '</ul></div></div>';
    
    if(slider) {
    	if(site_page_type === 'search' && widgetElement === 'ia_products_search'){
    		html += '<h1><span style="color: black !important;">Best Sellers</span>';
    	}else if(site_page_type === 'viewSellers' && widgetElement === 'ia_products'){
    		html += '<h1><span style="color: black !important;">You May Also Need</span>';
    	}else{
    		html += '<h1><span style="color: black !important;">'+productWidgetTitle[jQuery.inArray(widgetMode, productWidget)]+'</span>';
    	}
      
      /*For hot we need a scrolldown bar to select filters*/
      if(site_page_type === "homepage" || site_page_type ==="viewAllTrending" && widgetMode != "recent") {
        html += catHtml;
      }
      html += '</h1>';
      html += '<div class="spacer" style="padding: 0 25px;"><div class="slider product ready"><div class="frame"><ul id="' + widgetElement + '_list" class="overflow owl-carousel" style="width: 0.953empx; left: 0px;">';
    } else {
      if(site_page_type === "homepage" || site_page_type ==="viewAllTrending") {
        html += '<div class="listing wrapper"><div class="left-block"><ul class="listing-leftmenu"><h3><span>Filter By</span></h3><div>';
        html += '<li class="price"><h4 class="active">price</h4>';
        html += '<ul class="checkbox-menu price">';
        html += '<li><input type="checkbox" id="0-500"> <label for="0-500">₹0 - ₹500</label></li><li><input type="checkbox" id="500-1000"> <label for="500-1000">₹500 - ₹1000</label></li><li><input type="checkbox" id="1000-2000"> <label for="1000-2000">₹1000 - ₹2000</label></li><li><input type="checkbox" id="2000-3000"> <label for="2000-3000">₹2000 - ₹3000</label></li><li><input type="checkbox" id="3000-4000"> <label for="3000-4000">₹3000 - ₹4000</label></li><li><input type="checkbox" id="4000-5000"> <label for="4000-5000">₹4000 - ₹5000</label></li>';
        html += '<li><input type="checkbox" id="5000-10000"> <label for="5000-10000">₹5000 - ₹10000</label></li><li><input type="checkbox" id="10000-25000"> <label for="10000-25000">₹10000 - ₹25000</label></li><li><input type="checkbox" id="25000-50000"> <label for="25000-50000">₹25000 - ₹50000</label></li><li><input type="checkbox" id="And>50000"> <label for="And>50000">₹50000 And Above</label></li>';
        html += '</ul></li><li class="on sale"><h4 class="active">Sale</h4><ul class="checkbox-menu on-sale">';
        html += '<li><input type="checkbox" id="sale-filter"><label for="sale-filter">On Sale</label></li></ul></li></div></ul></div>';
        html += '<div class="right-block">';
        
        html += '<div><h1 class="ia_trending">'+productWidgetTitle[jQuery.inArray(widgetMode, productWidget)]+'</h1>';
        html += '</div>';
        /*SortBY dropdown only for search widget */
        if(widgetMode === "search"){
      	 html += sortHtml;
        }
        
        html += catHtml;
        
      }
      html += '<ul id="'+widgetElement+'_list" class="product-list" style="width: 964px; float: left;margin-top: 15px; ">';
      
    }

    var pageData = []; //array of strings each containing 20 products as HTML
    var page = -1; //index of page, immediately becomes 0 upon first product
    var activePage = 'iapage1'; //page we're currently on if in grid view
     
    if(slider) {
      /*Make recommendation html for a carousel*/
      jQuery.each(respData, function(i, v) {
        html += makeProductHtml(widgetElement, this, response.request_id);
        recIndex++;
      });
      if(widgetMode === "hot" && site_page_type == "homepage"){
          html += '</ul></div>';
          html += '</div></div><a href="http://'+window.location.host+'/store/mpl/en/viewAllTrending" class="button hotShowHide" style="display: inline-block;font-size: 12px;height: 40px;line-height: 40px;">View All Trending Products</a>';
          }
          else{
        	  html += '</ul></div>';
              html += '</div></div>'; 
          }

    } else {

      /*Make recommendation html for gridview by pages*/
      jQuery.each(respData, function(i, v) {
        if(recIndex % 20 === 0) {
          page++;
          pageData[page] = "";
        }
        pageData[page] += makeProductHtml(widgetElement, this, response.request_id);
        recIndex++;
      });
      html += pageData[0]; //start off with first page
      html += '</ul>';

      html += '<ul id="' + widgetElement + 'page_numbers" class="pagination" style="position: absolute; right: 0;line-height: 0px;cursor: pointer;margin-top: 15px;margin-right: 20px;">';
      html += '<li id="iapage1" class="number first active iapage" style="padding: 8px;"><a>1</a></li>';
      for(var i=1; i<pageData.length;i++) {
        html += '<li id="iapage'+(i+1)+'" class="number iapage" style="padding: 8px;"><a>'+(i+1)+'</a></li>';
      }
      html += '<li id="iapage_next" class="next" style="padding: 6px;"><a>Next <span class="lookbook-only"></span></a></li>';
      html += '</ul>';
      html += '</div>';
    }

    /*Insert the HTML*/
    if(recIndex > 0) {
      document.getElementById(widgetElement).innerHTML = html; 
    }

    /*if trending, reupdate based on user selection on dropdown.*/
    if(widgetMode === "hot" || widgetMode === "search") { 
      jQuery( ".category_li" ).on('click', function() { //
      var category_name = jQuery(this).text();
     $('.hotSelected').text(category_name);
    	  // selected category id from hot dropdown
    	  category_id = jQuery(this).attr('id');
        //console.log("category id slected in hot widget dropdown : "+category_id);
        var params = { 'count': '10' };
        if(!slider) {
          params.count = '100';
        }
        
        params.category_id = category_id;
        hotDropdownselected = category_name;
        
        if(category_id === "All Department") {
        	category_id = "";
        	hotDropdownselected = 'All Department';
        } 
        
        //params.category = category_id;
        var endpoint = '/SocialGenomix/recommendations/products/hot_in_category/jsonp';
        //$( ".owl-item" ).css( "display", "none" );
        callRecApi(buildParams(params), rootEP + endpoint);
      });
    }

    /*SortBY dropdown*/
    if(widgetMode === "search"){
    jQuery( ".sort_li" ).on('click', function() { //
        var sort_type = jQuery(this).text();
       $('.sortSelected').text(sort_type);
      	  // selected category id from hot dropdown
      	  sort_key = jQuery(this).attr('id');
          // params.category_id = category_id;
           sortDropdownselected = sort_type;
    
      	  if(sort_key === "name-asc"){
      		respData.sort(iaSortNameAsc);
      	  }else if(sort_key === "name-desc"){
      		respData.sort(iaSortNameDesc);
      	  }else if(sort_type === "price-asc"){
      		respData.sort(iaSortPriceAsc);
      	  }else{
      		respData.sort(iaSortPriceDesc);
      	  }
          if(slider) {
            document.getElementById(widgetElement + '_list').innerHTML = reorderRecommendation(widgetElement, respData, response.request_id);
          } else {
            pageData = reorderRecommendationGrid(widgetElement, respData, response.request_id);
            document.getElementById(widgetElement + '_list').innerHTML = pageData[0];
            /*Reset page numbers*/
            var pages = document.getElementById("ia_products_hotpage_numbers").childNodes;
            for(var i=pageData.length;i<pages.length-1;i++) { //ignore next
              pages[i].visibility='visible';
            }
            for(var i=pageData.length;i<pages.length-1;i++) { //ignore next
              pages[i].visibility='hidden';
            }
            activePage = 'iapage1';
          }
        });
    }

    /*Setup UI*/
    if(slider) { 
    	if(site_page_type == 'search' && widgetElement == 'ia_products_search'){
    		/*Animate Carousel*/
    	      $("#" + widgetElement + "_list").owlCarousel({
    	    	  
    	    	  items : 4,
    	          scrollPerPage: true,
    	          itemsDesktop : [1199,3],
    	          itemsDesktopSmall : [980,2],
    	          itemsTablet: [768,2],
    	          itemsMobile : [479,1],
    	          navigation: true,
    	          navigationText : [],
    	          pagination:false,
    	          rewindNav : false
    	        });
    	} 
      /*Animate Carousel*/
      $("#" + widgetElement + "_list").owlCarousel({
    	  
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

    } else { 

      /*Paginate GridView*/
      /*Go to page clicked*/
      jQuery(".iapage").on('click', function() {
    	  if(pageData[parseInt(jQuery(this).text()) - 1] != undefined){
        document.getElementById(widgetElement + '_list').innerHTML = pageData[parseInt(jQuery(this).text()) - 1];
    	  }else{
    		  document.getElementById(widgetElement + '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
    	  }
        jQuery('#'+activePage).removeClass('active');
        jQuery(this).addClass('active');
        activePage = this.id;
      });
      
      /*Go to next page if it exists*/
      jQuery( "#iapage_next").on('click', function() {
        var pageIndex = parseInt(activePage.replace( /^\D+/g, '')) + 1;
        if(pageIndex > 5){
        	pageIndex = pageIndex % 5;
        }
        if(document.getElementById('iapage' + pageIndex) !== null) {
          var el = document.getElementById('iapage' + pageIndex);
          if(pageData[parseInt(jQuery(el).text()) - 1] != undefined){
          document.getElementById(widgetElement + '_list').innerHTML = pageData[parseInt(jQuery(el).text()) - 1];
          }else{
        	  document.getElementById(widgetElement + '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
          }
          jQuery('#'+activePage).removeClass('active');
          jQuery(document.getElementById(el.id)).addClass('active');
          activePage = el.id;
        }   
      }); 

      /*Filter by price range, and whether it's on sale*/
      var priceRanges = [];
      var saleOptions = [];

      /*Select Price Filter*/
      jQuery(".price li label").on('click', function() {
        /*Follow the checkmark which isn't visible as an html element*/
        if(priceRanges.indexOf(this.textContent) > -1) {
          priceRanges.splice(priceRanges.indexOf(this.textContent), 1);
        } else {
          priceRanges.push(this.textContent);
        }
        pageData = getFilteredRecommendations(widgetElement, respData, priceRanges, saleOptions, response.request_id);          
        if(pageData[0] != undefined || pageData[0] != null){
        document.getElementById(widgetElement + '_list').innerHTML = pageData[0];
        }else{
        	document.getElementById(widgetElement + '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
        }
        /*Reset page numbers*/
        var pages = document.getElementById("ia_products_hotpage_numbers").childNodes;
        for(var i=pageData.length;i<pages.length-1;i++) { //ignore next
          pages[i].visibility='visible';
        }
        for(var i=pageData.length;i<pages.length-1;i++) { //ignore next
          pages[i].visibility='hidden';
        }
        activePage = 'iapage1';
      });

      /*Select sale filter*/
      jQuery(".on-sale li label").on('click', function() {
        /*Follow the checkmark which isn't visible as an html element*/
        if(saleOptions.indexOf(this.textContent) > -1) {
          saleOptions.splice(saleOptions.indexOf(this.textContent), 1);
        } else {
          saleOptions.push(this.textContent);
        }
        pageData = getFilteredRecommendations(widgetElement, respData, priceRanges, saleOptions, response.request_id);          
         if(pageData[0] != undefined){       
        document.getElementById(widgetElement + '_list').innerHTML = pageData[0];
         }else{
         	document.getElementById(widgetElement + '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
         }
        /*Reset page numbers*/
        var pages = document.getElementById("ia_products_hotpage_numbers").childNodes;
        for(var i=pageData.length;i<pages.length-1;i++) { //ignore next
          pages[i].visibility='visible';
        }
        for(var i=pageData.length;i<pages.length-1;i++) { //ignore next
          pages[i].visibility='hidden';
        }
        activePage = 'iapage1';
      });
    }
  } else if (jQuery.inArray(widgetMode, brandWidget) > -1) {

    /*Brand Widgets*/

    widgetElement = brandWidgetElement[jQuery.inArray(widgetMode, brandWidget)];
    if (!document.getElementById(widgetElement)) {
      return;
    }
    document.getElementById(widgetElement).innerHTML = "";    
    var html = "";
    var respData = response.data.brands;

    html += '<div class="wrapper"><h1 class="">'+brandWidgetTitle[jQuery.inArray(widgetMode, brandWidget)]+'</h1><ul class="feature-brands ia_feature_brands">';
    numRec = 0;
    jQuery.each(respData, function () {
    	queryUrl = this.url + '?req=' + response.request_id;
      if(numRec < 3) {
        html += '<li><a href="'+queryUrl+'" style=""><img class="image" src="'+this.image_url+'" width="432" height="439">';
        //logo and tagline, to be made obsolete with larger image
        html += '<img class="logo" src="'+this.logo_image_url+'" width="140" height="43">';
        html += '<span>'+this.description+'</span>';
        html += '</a></li>';
      } else if (numRec === 3) {
        html += '</ul><ul class="more-brands">';
      } 
      if (numRec >= 3) {
        html += '<li><a href="'+queryUrl+'"><img class="logo  hover-logo" src="'+this.logo_image_url+'" width="160" height="43">';
        html += '<img class="background" src="'+this.overlay_image_url+'" width="206" height="206" style="left: 0";></a></li>';
      }
      numRec++;
      if(numRec === 9) {
        return false;
      }
    });

    html += '</ul></div></div>';
    document.getElementById(widgetElement).innerHTML = html;
  } else if (jQuery.inArray(widgetMode, categoryWidget) > -1) {
    
	  /*Category Widgets*/

	    widgetElement = categoryWidgetElement[jQuery.inArray(widgetMode, categoryWidget)];
	    if (!document.getElementById(widgetElement)) {
	      return;
	    }
	    document.getElementById(widgetElement).innerHTML = "";    
	    var html = "";
	    var respData="";
	    if(widgetMode === "categories/recent") {
	     respData = response.data.recommendations;
	    }else{
	     respData = response.data.categories;
	    }
	    if (respData === null) {
	      return;
	    }
	    if(widgetMode !== "categories/recent") {
	      html += '<div class="wrapper">';
	    }
	    if(widgetMode === "categories/recent") { 
	    html += '<h1 class="">'+categoryWidgetTitle[jQuery.inArray(widgetMode, categoryWidget)]+'</h1>';
	    }else{
	    	html += '<h1 class="">'+categoryWidgetTitle[jQuery.inArray(widgetMode, categoryWidget)]+'</h1>';
	    	html += '<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference owl-carousel owl-theme" id="mplCategoryCarousel" style="opacity: 1; display: block;">';
	    }
	    var numRec = 0;
	    jQuery.each(respData, function () {
	    	queryUrl = this.url + '?req=' + response.request_id;
	      if(widgetMode === "categories/recent") { //FlyOut Menu
	        html += '<div style="display:inline-block; padding: 0; width: 100px; margin: 0px 10px;"><a href="'+queryUrl+'"><img class="image" src="'+this.image_url+'" width="113"/>';
	        html += '<p style="text-align: center;margin-top: 5px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">'+this.name+'</p></a></div>';
	      }else { //Normal Widget
	    	  html += '<div class="item slide">';
	    	  html += '<a href="'+queryUrl+'"><img src="'+this.image_url+'" class="image" style="height: 324px;"></a><span>'+this.name+'</span>';
	    	  html += '<a class="shop_link" href="'+queryUrl+'"><b>SHOP NOW</b></a></div>';
	      }
	      numRec++;
	      /*if(numRec === respData.length) {
	        return false;
	      }*/
	    });
	    if(widgetMode !== "categories/recent") {
	    	html += '</div>';
	    }
	    
	    html += '</div>';
	    
	    if(widgetMode !== "categories/recent") {
	      html += '</div>';
	    }    
	    if(numRec > 0) {
	      document.getElementById(widgetElement).innerHTML = html;
	    }
var carousel = $("#mplCategoryCarousel");
	    
	    carousel.owlCarousel({
	    	items : 4,
			navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [1199,3],
            itemsDesktopSmall : [980,2],
            itemsTablet: [768,2],
            itemsMobile : [479,1], 
			rewindNav: false,
			lazyLoad:true,
			navigation : true,	        
	        rewindNav : false,
	        scrollPerPage : true
	    });

	  } else if (jQuery.inArray(widgetMode, collectionWidget) > -1) {

		  /*Collection Widgets*/

    widgetElement = collectionWidgetElement[jQuery.inArray(widgetMode, collectionWidget)];
    if (!document.getElementById(widgetElement)) {
      return;
    }
    document.getElementById(widgetElement).innerHTML = "";    
    document.getElementById(widgetElement).className = "feature-collections";
    var html = "";
    var respData = response.data.bundles;
    if (respData === null) {
      return;
    }
    html += '<div class="wrapper background"><h1 class="">'+collectionWidgetTitle[jQuery.inArray(widgetMode, collectionWidget)]+'</h1><ul class="collections">';
    var numRec = 0;
    jQuery.each(respData, function () {
    	queryUrl = this.url + '?req=' + response.request_id;
      if(numRec < 1) { //first one show both images
        html += '<li class="chef sub "><img class="background brush-strokes-sprite sprite-water_color_copy_4" src="//'+mplStaticResourceHost+'/store/_ui/responsive/common/images/transparent.png">';
        html += '<img class="background brush-strokes-sprite sprite-water_color_copy_3" src="//'+mplStaticResourceHost+'/store/_ui/responsive/common/images/transparent.png">';
        html += '<h3>'+this.name+'</h3>';
        html += '<img class="image" src="//'+DamMediaHost+''+this.top_left_image_url+'" style="margin-right: 20px;width: calc(40% - 140px);">';
        html += '<img class="image" src="//'+DamMediaHost+''+this.top_right_image_url+'" style="width: calc(60% - 140px);width: -webkit-calc(60% - 140px);">';
        html += '<span>'+this.description+'';
        html += '<a href="'+queryUrl+'">Shop The Collection</a></span>';
        html += '</li>';
      } else if (numRec < 2){  //show only one image, different format
        html += '<li class=" sub "><img class="image" src="//'+DamMediaHost+''+this.bottom_left_image_url+'"><div class="copy"><h3>'+this.name+'</h3>';
        html += '<a href="'+queryUrl+'">The Hottest New Styles</a></div></li>';
      } else if (numRec < 3){
        html += '<li class="spotlight sub "><img class="image" src="//'+DamMediaHost+''+this.bottom_right_image_url+'"><div class="copy"><h3>'+this.name+'</h3>';
        html += '<a href="'+queryUrl+'">The Hottest New Styles</a></div></li>';
      } else {
        return;
      }
      numRec++;
    });
    html += '</ul></div></div>';
    if (numRec > 0) {
      document.getElementById(widgetElement).innerHTML = html;
    }
  } else {

    /*We should never get to this part of the code*/

    //console.log("widget mode we don't account for: " + widgetMode);
  }
}
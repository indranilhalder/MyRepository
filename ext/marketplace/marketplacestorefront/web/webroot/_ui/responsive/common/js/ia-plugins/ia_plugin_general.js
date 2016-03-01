/*
David Phipps
June 2014
This code contains methods 
*/

var rootEP = ''; //server/port combination of 
var recEndPoint = ''; //make api calls to here
var ecompany = ''; //ecompany
var spid = ''; //site product id
var ssid = ''; //session id of client
var uid = ''; //user id, change this variable first when this changes
var start_time; //when we begin

/*Optional Parameters*/
var referring_request_id = '';
var referring_site_product_id = '';
var site_page_type = '';
var domain = '';
var search_string = '';
var category_id = '';
var brand_id = '';
var seller_id = '';
var order_id = '';
var site_product_array = [];

/*Arrays to be populated by client to create parameters*/
var brand_array = [];
var category_array = [];

/*widget modes that are just products*/
productWidget = [ "complements", "similar", "bought_together", "normal", "hot", "best_selling", 
                  "new", "recent", "search", "hot_in_category", "hot_in_brand"];
productWidgetTitle = ["Complete The Look", "More Stuff Like This", "Things That Go With This", "You Know You Want This", "Hot Now", "Best Sellers", 
                      "Just In(credible)!", "Last Seen", "My Recommendations", "Hot Now", "Hot Now"];
productWidgetElement = ["ia_products_complements", "ia_products_similar", "ia_products_bought_together", "ia_products", "ia_products_hot", "ia_products_best_selling", "ia_products_new", 
                        "ia_products_recent", "ia_products_search", "ia_products_hot_in_category", "ia_products_hot_in_brand"];

/*widget modes that are text based due to being brand/category/collection based*/
brandWidget = [ "brands/favorites", "brands/hot_searches"];
brandWidgetTitle =  [ "Brands You Love", "Hot Brands"];
brandWidgetElement =  [ "ia_brands_favorites", "ia_brands_hot_searches"];

categoryWidget =  [ "categories/favorites", "categories/hot_searches", "categories/recent"];
categoryWidgetTitle = [ "Handpicked For You", "Hot Categories", "Recently Viewed Categories"];
categoryWidgetElement = [ "ia_categories_favorites", "ia_categories_hot_searches", "ia_categories_recent"];

collectionWidget = ["collections"];
collectionWidgetTitle = ["#StayQued"];
collectionWidgetElement = ["ia_collections"];

/*When we get to the webpage, use to determine time on page
Deprecated*/
function init_iaplugin() {
  /*Set up start time for time-related information*/
  start_time = new Date();

  /*Get prior user information from previous pages*/
  ssid = getCookie('IASESSIONID');
  if(getCookie('IAUSERTYPE') === 'facebook') {
    user_type = 'facebook';
    uid = getCookie('IAUSERID');   
  } else if(getCookie('IAUSERTYPE') === 'site_user'){
    user_type = 'site_user';
    uid = getCookie('IAUSERID');
  } else {
    user_type = 'session';
    uid = ssid;
    document.cookie='IAUSERTYPE=session; path=/';
  } 
}

/*Check referring values*/
function refCheck() {
  if (document.URL.indexOf('req=') > -1) {
    referring_request_id = document.URL.substr(document.URL.lastIndexOf("req=")+4);
    if(referring_request_id.indexOf("&") > -1) {
      referring_request_id = referring_request_id.substr(0, referring_request_id.indexOf("&"));
    } 
  }
  if (document.URL.indexOf('rspid=') > -1) {
    referring_site_product_id = document.URL.substr(document.URL.lastIndexOf("rspid=")+6);
    if(referring_site_product_id.indexOf("&") > -1) {                        
      referring_site_product_id = referring_site_product_id.substr(0,referring_site_product_id.indexOf("&"));
    }
    var data =  { 'pname':['referring_site_product_id','site_product_id'],
                  'pvalue':[referring_site_product_id, spid] };
    callEventApi('clickstream', data);
  }
}

/*Get parameter value by parsing the URL*/
function getURLField(param) {
  var index = document.URL.indexOf(param);
  var paramVal = "";
  if(index > 0) {
    paramVal = document.URL.substr(index+param.length);
    index = paramVal.indexOf('&');
    if(index > 0) {
      paramVal = paramVal.substr(0,index);   
    }
    index = paramVal.indexOf('#');
    if(index > 0) {
      paramVal = paramVal.substr(0,index);   
    }
  }
  return decodeURIComponent(paramVal);
}

/*Get email params*/
function emailCheck() {
  if(document.URL.indexOf("ia_action") > -1) {
    var emailPayload = {
      "referring_request_id":getURLField("req="),
      "utm_source":getURLField("utm_source="),
      "email_id":getURLField("email_id=")
    };
    if(getURLField("user_id=") && getURLField("user_type=")) {
      user_id = getURLField("user_id=");
      user_type = getURLField("user_type=");
      document.cookie = "IAUSERTYPE="+user_id+'; path=/';
      document.cookie = "IAUSERID="+user_type+'; path=/';
    }
    if(getURLField("session_id=")) {
      document.cookie = "IASESSIONID="+getURLField("session_id=")+'; path=/';
      if(user_type === "session") {
        user_id = getURLField("session_id=");
      }
    }
    callEventApi('email_click', emailPayload); 
    
    return {
     "ia_action":getURLField("ia_action="),
     "utm_medium":getURLField("utm_medium="),
     "utm_campaign":getURLField("utm_campaign="),
     "utm_source":getURLField("utm_source=")  
    };
  }
  return null;
}

function sendAddToCart(productIds, quantities) {
  productIds = '[' + productIds.toString() + ']';
  quantities = '[' + quantities.toString() + ']';
  var params =  { 
                      "pname" : ['site_product_ids', 'quantities'],
                      "pvalue" : [productIds, quantities]
                };
  callEventApi('add_to_cart', params)
}

function sendCartView(productIds, quantities) {
  productIds = '[' + productIds.toString() + ']';
  quantities = '[' + quantities.toString() + ']';
  var params =  { 
                      "pname" : ['site_product_ids', 'quantities'],
                      "pvalue" : [productIds, quantities]
                };
  callEventApi('cart_view', params)
}

/*getter for values stored in cookies*/
function getCookie(cname) {
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for(var i=0;i<ca.length;i++) {
    var c = ca[i].trim();
    c2 = c.split('=');
    if (c2[0] === cname){//.indexOf(cname) === 0){ 
  return c2[1];
}
}
return "";
}

/*Get style info so we can make it look nice*/
function styleOf(id, styleProp) {
  id = document.getElementById(id);
  var result;
  if (window.getComputedStyle) {
    result = document.defaultView.getComputedStyle(id,null).getPropertyValue(styleProp);
  } else if (id.currentStyle) {
    result = id.currentStyle[styleProp];
  } else {
    result = '';
  }
  return result;
}

//Handles building generalized parameter object for api calls
function buildParams(moreParams) {
	currentParams = { 'session_id': ssid,
    'client_user_agent': navigator.userAgent,
    'user_id': uid,
    'user_type': user_type,
    'ecompany': ecompany,
    'site_page_type': site_page_type
  };

  if(search_string !== "") {
    currentParams.search_string = search_string;
  }
  if(category_id !== "") {
    currentParams.category_id = category_id;
  }
  if(brand_id !== "") {
    currentParams.brand_id = brand_id;
  }
  if(seller_id !== "") {
    currentParams.seller_id = seller_id;
  }
  if(order_id !== "") {
    currentParams.order_id = order_id;
  }
  if(spid !== "") {
    currentParams.site_product_id = spid;
  }
  if(domain !== "") {
    currentParams.domain = domain;
  }
  
  if(site_product_array.length > 0) { //if multiple site product ids
    var spids = {};
    currentParams.site_product_id = '';
    for(var i=0; i<site_product_array.length; i++) {
      currentParams.site_product_id += site_product_array[i] + ','; 
    }
    currentParams.site_product_id = currentParams.site_product_id.substring(0, currentParams.site_product_id.length - 1);
  }
  if(category_array.length > 0) { //if multiple site product ids
	    var spids = {};
	    currentParams.category_id = '';
	    for(var i=0; i<category_array.length; i++) {
	      currentParams.category_id += category_array[i] + '	'; 
	    }
	    currentParams.category_id = currentParams.category_id.substring(0, currentParams.category_id.length - 1);
	  }
  if(brand_array.length > 0) { //if multiple site product ids
	    var spids = {};
	    currentParams.brand_id = '';
	    for(var i=0; i<brand_array.length; i++) {
	      currentParams.brand_id += brand_array[i] + '	'; 
	    }
	    currentParams.brand_id = currentParams.brand_id.substring(0, currentParams.brand_id.length - 1);
	  }
  jQuery.extend(currentParams, moreParams);
  return currentParams;
}

/*Call init API to get session id*/
function callInitApi() {
  var requestURL = rootEP + '/SocialGenomix/recommendations/init/jsonp';
  var data = {	'user_type': user_type,
    'user_id': uid,
    'referring_url': document.referrer
  };
  jQuery.ajax({
    type: "GET",
    url: requestURL,
    jsonp: 'callback',
    dataType: 'jsonp',
    data: data,
    contentType: 'application/javascript',
    success: function(response) {
      document.cookie = "IASESSIONID="+response.session_id+'; path=/';
      ssid=response.session_id;
    }
  });
}

/*Call Event API which takes miscellaneous events*/
function callEventApi(event_type, event_params) {
  var requestURL = rootEP + '/SocialGenomix/recommendations/events/jsonp';
  var timestamp = new Date();

  timestamp = timestamp.getTime();

  var time_on_page;
  if(event_type === 'clickstream') {
    time_on_page = timestamp - getCookie('prev_start_time');
  } else {
    time_on_page = timestamp - start_time.getTime();
  }  

  var params = {'ecompany'	: ecompany,
                'user_type'	: user_type,
                'user_id'	: uid,
                'session_id' : getCookie('IASESSIONID'),
                'timestamp'	: timestamp,
                'time_on_page'	: time_on_page,
                'event_type'	: event_type
              };
  jQuery.extend(params, event_params);
  
  jQuery.ajax({
    type: "GET",
    url: requestURL,
    data: params,
    jsonp: 'callback',
    dataType: 'jsonp',
    contentType: 'application/javascript',
    success: function(response, textStatus, jqXHR) {

    }, error: function(jqXHR, textStatus, errorThrown) {
      console.log(errorThrown);
      console.log(textStatus);
    }
  });
}

/*Call API which loads data of FB user*/
function callFBApi(facebook_id, access_token, session_id) {
  var requestURL = rootEP + '/SocialGenomix/data/fb/user/jsonp';
  var params = { 'ecompany'	: ecompany, 'facebook_id'	: facebook_id, 'access_token'	: access_token }; 

  if(session_id) {
    params.session_id = ssid;
  }

  jQuery.ajax({ 
    type: "GET",
    url: requestURL,
    jsonp: 'callback',
    dataType: 'jsonp',
    contentType: 'application/javascript',
    data: params,
    success: function(response, textStatus, xhr) {
      /*Yay it worked*/
    }, error: function(jqXHR, textStatus, errorThrown) {
      console.log(errorThrown);
      console.log(textStatus);
    } 
  });
}

/*Calls API which returns product recommendations*/
function callRecApi(params, requestURL) {  
  jQuery.extend(params, emailCheck());
  
  return jQuery.ajax({ 
    type: "GET",
    url: requestURL,
    /*jsonpCallback: "updatePage",*/
    dataType: 'jsonp',
    contentType: 'text/javascript; charset=utf-8',
    data: params,
    success: function(response, textStatus, jXHR) {
      requestURL = requestURL.substr(requestURL.indexOf("recommendations/")+"recommendations/".length)
      requestURL = requestURL.substr(0, requestURL.indexOf("/jsonp"));
      if(requestURL.indexOf("products/") > -1) {
        requestURL = requestURL.substr(requestURL.indexOf("products/")+"products/".length)
      }
      if(requestURL === "products") {
        requestURL = "normal";
      }
      updatePage(response, requestURL);
    }, error: function(jqXHR, textStatus, errorThrown) {
      eT = errorThrown;
		  tS = textStatus; 
    }
  });
}
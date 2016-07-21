 $(document).ready(function() {
	 //master tag
	 if($(window).width() < 650) {
	 	$('head').append('<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />');
	 }
	 //TISPRT-304 starts
	 $.each($(".facet-name js-facet-name").find("h3"), function() {
			if ($(this).text() == "Departments") {
				$(this).remove();
			}
		});
	 //TISPRT-304 ends
	//Simple banner component 
	 	var removeHref = $("div[data-logo=marketplace]").find("a").attr("href");
	 	var href = removeHref.split("?");
	 	$("div[data-logo=marketplace]").find("a").attr("href",href[0]);
	
	//Search box
	 	var selectedItemText = $("#enhancedSearchCategory").find('option:selected').text();
		 $("#searchBoxSpan").html(selectedItemText);
		 
		 $('.select-view .select-list').hover(function(){
			 $(this).find('ul').slideDown();
		 });
		$(document).on('click',".select-list .dropdown li",function(e) {
			 $("#enhancedSearchCategory").val(this.id);
			 $("#searchCodeForDropdown").val(this.id);
			//sessionStorage.setItem("selectedItemValue",$("#searchCategory").val());
			//sessionStorage.setItem("selectedItemText",$(this).html());
			$(".select-list .dropdown li").removeClass("selected");
			$(this).addClass("selected");
			$("#searchBoxSpan").html($(this).text());
			$(this).parents('.select-list').find('ul').slideUp();
			$("#js-site-search-input").focus(); 
		}); 
		
		
		$("#search_form").submit(function(event) {
			if($("#js-site-search-input").val().trim()=="") {
				var actionText = ACC.config.contextPath;
				var dropdownValue = $("#enhancedSearchCategory").val();
				//var dropdownName = $("#searchCategory").find('option:selected').text();

				if (!String.prototype.startsWith) {
					  String.prototype.startsWith = function(searchString, position) {
					    position = position || 0;
					    return this.indexOf(searchString, position) === position;
					  };
					}
				//TISPRO=660
				if (dropdownValue.startsWith("MSH") || dropdownValue.startsWith("MBH")) {
					actionText = (actionText +'/c-' + dropdownValue);
				} else if (!dropdownValue.startsWith("all")) {
					actionText = (actionText + '/s/' + dropdownValue);
				}
				$("#search_form :input").prop("disabled", true);
				$('#search_form').attr('action',actionText);
			} 
		});
		
		//End
		
		//Rotating images component
        	$(".icid li").each(function() {
        		if($(this).has("href")){
        			var icid = $(this).attr("data-bannerid");
        			var link = $(this).find("a").attr("href");
        			if(icid!=undefined && link!=undefined){
        				link = link + "?icid="+icid;
        				$(this).find("a").attr("href",link);
        			}
        		}
        	});
		//end
		
		//Tealium js.tag
		/*$(document).on("click", ".header-myAccountSignOut", function() {
			window.localStorage.removeItem("eventFired");
		});*/

		 $(document).on("click","form .pagination_a_link",function(e){
			event.preventDefault();
			var hrefurl = $(this).attr('href');
			$("#paginationForm").attr("action", hrefurl);
			$(this).closest('form').submit();
		 });  

		//TISPRO-183 -- Firing Tealium event only after successful user login
	/*	if(loginStatus){
			if (localStorage.getItem("eventFired")==null || window.localStorage.getItem("eventFired")!="true") {
				localStorage.setItem("eventFired","true");
				console.log("Login Success!!!");
				if(typeof utag == "undefined"){
					console.log("Utag is undefined")
				}
				else{
					console.log("Firing Tealium Event");
					utag.link({ "event_type" : "Login", "link_name" : "Login" });
				}
				
				//fireTealiumEvent();
				
				
				
			}  
		}*/
		//End
		
		//Smart Banner
		new SmartBanner({
			daysHidden: 0, // days to hide banner after close button is clicked (defaults to 15)
			daysReminder: 0, // days to hide banner after "VIEW" button is clicked (defaults to 90)
			appStoreLanguage: 'us', // language code for the App Store (defaults to user's browser language)
			title: 'TataCLiQ',
			author: 'Tata Unistore Ltd',
			speedIn: 300, // Show animation speed of the banner
		    speedOut: 400, // Close animation speed of the banner
			button: 'OPEN',
			force: null,
			store: {
	              ios: 'On the App Store',
	              android: 'In Google Play'
	          },
	          price: {
	              ios: 'FREE',
	              android: 'FREE'
	          }
	});

		//End
		//Global Error Popup remove
		$(document).on('hide.bs.modal', function () {
		    $("#globalErrorPopupMsg").remove();
		}); 
		//End
	
		
		//Department Hierarchy script from Product Refinemant.tag
		 $(".facet-name.js-facet-name h4").each(function(){
			if($(this).hasClass("false")){
		    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").hide(100);
		    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").hide(100);
			}
		}); 
	   //End
		 
 });
 
//Script from facetNavAppliedFilters.tag
 var serpSizeList=[];
	var serpSizeCount=-1;
	var sizeCount=0;
	var size= $('#sizeCountForAppliedFilter').val();
	var searchQuery=$('#searchQueryForAppliedFilter').val();
	if(typeof(searchQuery)!= "undefined"){
	var arr = searchQuery.split(':');
	for(var i = 0; i < arr.length; i++)
	{
		if (arr[i]=='size')
		{
			sizeCount++;
			//countFreq = 1;
		}
		else if (sizeCount >= 1)
		{
			serpSizeList[++serpSizeCount]=arr[i];
			if (sizeCount == size)
			{
				break;
			}
		}
	}
	}
	//End
 
	//Global Error Popup
	function globalErrorPopup(msg) {
		$("body").append('<div class="modal fade" id="globalErrorPopupMsg"><div class="content" style="padding: 10px;"><span style="display: block; margin: 7px 16px;line-height: 18px;">'+msg+'</span><button class="close" data-dismiss="modal"></button></div><div class="overlay" data-dismiss="modal"></div></div>');
		$("#globalErrorPopupMsg").modal('show');
	} 
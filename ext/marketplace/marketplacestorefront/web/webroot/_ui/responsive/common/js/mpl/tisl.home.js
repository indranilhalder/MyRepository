$("div.departmenthover").on("mouseover touchend", function() {
	
	var id = this.id;
	var code = id.substring(4);

	if(!$.cookie("dept-list") && window.localStorage) {
		for (var key in localStorage){
			if (key.indexOf("deptmenuhtml")>=0) {
				window.localStorage.removeItem(key);
				//console.log("Deleting.." + key);
			}
		}
	}
	
	if(window.localStorage && (html = window.localStorage.getItem("deptmenuhtml-"+code)) && html !="" ) {
		//console.log("Local");
		$("ul."+id).html(decodeURI(html));
	} else {
		$.ajax({
			url: ACC.config.encodedContextPath + "/departmentCollection",
			type: 'GET',
	 	    data: "&department="+code,
			success: function (html)
			{
				//console.log("Server");
				$("ul."+id).html(html);
				if(window.localStorage) {
					$.cookie("dept-list", "true", { expires: 1, path:"/store" });
					window.localStorage.setItem("deptmenuhtml-"+code, encodeURI(html));
				}
			}
		});
	}
});

$(".A-ZBrands").on("mouseover touchend", function(e) {
	if ($("li#atozbrandsdiplay").length) {
		//console.log("Dipslaying A-Z Brands..");
		
		if(!$.cookie("dept-list") && window.localStorage) {
			for (var key in localStorage){
				if (key.indexOf("atozbrandmenuhtml")>=0) {
					window.localStorage.removeItem(key);
					//console.log("Deleting.." + key);
				}
			}
		}
		
		if(window.localStorage && (html = window.localStorage.getItem("atozbrandmenuhtml")) && html !="" ) {
			//console.log("Local");
			if ($("div#appendedAtoZBrands") == null || $("div#appendedAtoZBrands").length == 0) {
				$("li#atozbrandsdiplay").append(decodeURI(html));
			}
		} else {
			//console.log("Server");
			$.ajax({
				url: ACC.config.encodedContextPath + "/atozbrands",
				type: 'GET',
				success: function (html)
				{
					console.log(html)
					if ($("div#appendedAtoZBrands") == null || $("div#appendedAtoZBrands").length == 0) {
						$("li#atozbrandsdiplay").append(html);
					}
					if(window.localStorage) {
						$.cookie("dept-list", "true", { expires: 1, path:"/store" });
						window.localStorage.setItem("atozbrandmenuhtml", encodeURI(html));
					}
				}
			});
		}
	}
});



$("span.helpmeshopbanner").on("click touchend", function() {
	
		$.ajax({
			url: ACC.config.encodedContextPath + "/helpmeshop",
			type: 'GET',
			success: function (html)
			{
				$("div#helpmeshopcontent").html(html);
			}
		});
});


$("a#tracklink").on("mouseover touchend", function(e) {
    e.stopPropagation();
	
	$.ajax({
		url: ACC.config.encodedContextPath + "/headerTrackOrder",
		type: 'GET',
		success: function (html)
		{
			$("ul.trackorder-dropdown").html(html);
		}
	});
});


$("a#myWishlistHeader").on("mouseover touchend", function(e) {
    e.stopPropagation();
	
	$.ajax({
		url: ACC.config.encodedContextPath + "/headerWishlist",
		type: 'GET',
 	    data: "&productCount="+$(this).attr("data-count"),
		success: function (html)
		{
			$("div.wishlist-info").html(html);
		}
	});
});
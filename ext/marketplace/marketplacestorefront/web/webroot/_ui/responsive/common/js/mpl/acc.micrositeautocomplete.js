ACC.micrositeautocomplete = {

	_autoload: [
		"bindMicrositeSearchAutocomplete"
	],

	bindMicrositeSearchAutocomplete: function ()
	{
		
		// extend the default autocomplete widget, to solve issue on multiple instances of the searchbox component
		$.widget( "custom.yautocomplete", $.ui.autocomplete, {
			_create:function(){
				
				// get instance specific options form the html data attr
				var option = this.element.data("options");
				// set the options to the widget
				this._setOptions({
					minLength: option.minCharactersBeforeRequest,
					displayProductImages: option.displayProductImages,
					delay: option.waitTimeBeforeRequest,
					autocompleteUrl: option.autocompleteUrl,
					source: this.source
					
				});
				// call the _super()
				$.ui.autocomplete.prototype._create.call(this);
				
			},
			options:{
				cache:{}, // init cache per instance
				focus: function (){return false;}, // prevent textfield value replacement on item focus
				select: function (event, ui){
					window.location.href = ui.item.url;
				}
			},_renderItem : function (ul, item){
				
				/*if (item.type == "autoSuggestion"){
					var renderHtml = "<a href='?q=" + item.value +"&best_search_keyword="+ item.searchterm + "' ><div class='name'>" + item.value + "</div></a>";
					return $("<li>")
							.data("item.autocomplete", item)
							.append(renderHtml)
							.appendTo(ul);
				}*/
				
				if (item.type == "autoSuggestion"){
					
					var renderHtml = "<a href='" + ACC.config.encodedContextPath + "/search/?q=" + item.value +"&best_search_keyword="+ item.searchterm + "' ><div class='name'>" + item.value + "</div></a>";
					return $("<li>")
							.data("item.autocomplete", item)
							.append(renderHtml)
							.appendTo(ul);
					
				}
				if (item.type == "productResult"){ 
					var renderHtml = "<a href='" + ACC.config.encodedContextPath + item.url + "' >";

					if (item.image != null){
						renderHtml += "<img src='" + item.image + "'  />";
					}

					renderHtml += 	"<div class='name'>" + item.value +"</div>";
					renderHtml += 	"<div class='price'>" + item.price +"</div>";
					renderHtml += 	"</a>";

					return $("<li class='product'>").data("item.autocomplete", item).append(renderHtml).appendTo(ul);
				}
				if (item.type == "brands")
				{		
					var renderHtml = "<a href='" + ACC.config.contextPath + item.url + "' class='clearfix'>";
					if(!item.valueset && item.index !== undefined && item.index==0){
						
						renderHtml+="<span class=''>" +"<strong>" + item.term + "</strong>" +"<br>" ;
						return $("<li>").data("item.autocomplete", item).append(renderHtml).appendTo(ul);

					} else {
						renderHtml += "<span class=''>" + "in " + item.value + "</span>" + "</a>";
					}
					//renderHtml += "<span class=''>" +"<strong>" + item.term + "</strong>" + " in " + item.value + "</span>" + "</a>";
					return $("<li class='product-list'>").data("item.autocomplete", item).append(renderHtml).appendTo(ul);
				}
				if (item.type == "category")
				{		
					
					var renderHtml = "<a href='" + ACC.config.contextPath + item.url + "' class='clearfix'>";
					
					if(!item.valueset && item.index !== undefined && item.index==0){
						
						renderHtml+="<span class=''>" +"<strong>" + item.term + "</strong>" +"<br>" ;
						return $("<li>").data("item.autocomplete", item).append(renderHtml).appendTo(ul);

					} else {
						renderHtml += "<span class=''>" + "in " + item.value + "</span>" + "</a>";
					}
					//renderHtml += "<span class=''>" +"<strong>" + item.term + "</strong>" + " in " + item.value + "</span>" + "</a>";
					return $("<li class='product-list' >").data("item.autocomplete", item).append(renderHtml).appendTo(ul);
				}
				if (item.type == "productName")
				{	
					
					var renderHtml = "<a href='" + ACC.config.contextPath + item.url + "' class=' clearfix'>";				
					renderHtml += "<span class='title'>" + item.value  +
							"</span></span>" +
							"</a>";
					return $("<li class='product-list'>").data("item.autocomplete", item).append(renderHtml).appendTo(ul);
				}
				if (item.type == "productResult")
				{				
					var renderHtml = "<a href='" + ACC.config.contextPath + item.url + "' class='product-list clearfix'>";
					if (option.displayProductImages &&  item.image != null)
					{
						renderHtml += "<span class='thumb'><img src='" + item.image + "' /></span><span class='desc clearfix'>";
					}
					renderHtml += "<span class='title'>" + item.manufacturer +" "+ item.value /*+ " in " + item.category*/ +
							"</span><span class='price'>" + item.price + "</span></span>" +
							"</a>";
					return $("<li class='product-list'>").data("item.autocomplete", item).append(renderHtml).appendTo(ul);
				}
			},
			source: function (request, response)
			{
				var self=this;
				var term = request.term.toLowerCase();
				var selectedCat = $("#micrositeSearchCategory option:selected").val();
				var termCombi = term+":"+selectedCat;
				//if (term in self.options.cache)
				if (termCombi in self.options.cache)
				{
					return response(self.options.cache[termCombi]);
				}

				$.getJSON(self.options.autocompleteUrl, {term: request.term, category: selectedCat}, function (data)
				{
					var autoSearchData = [];
//					if(data.suggestions != null){
//						$.each(data.suggestions, function (i, obj)
//						{
//							autoSearchData.push({
//								value: obj.term,
//								url: ACC.config.encodedContextPath + "/search?text=" + obj.term,
//								type: "autoSuggestion"
//							});
//						});
//					}
					if(data.brands != null){
						
						if(data.brands[0]!= undefined){
							
							
						
						autoSearchData.push(
								{value: data.brands[0].name,
									code: data.brands[0].code,
									desc: data.brands[0].description,	
									//url:  "/mpl/en/search/?text=" + data.searchTerm + "&searchCategory=" + data.brands[0],
									url:  "/mpl/en/search/?q=" + data.searchTerm + "%3Arelevance%3Abrand%3A" +  data.brands[0].code+"&search_category="+selectedCat+"&best_search_keyword="+term,
									term: data.searchTerm,
									type: "brands",
									index: 0,
									valueset: false
									});
						
						}
						$.each(data.brands, function (i, obj)
						{
							autoSearchData.push(
									{value: obj.name,
										code: obj.code,
										desc: obj.description,	
										//url:  "/mpl/en/search/?text=" + data.searchTerm + "&searchCategory=" + obj,
										url:  "/mpl/en/search/?q=" + data.searchTerm + "%3Arelevance%3Abrand%3A" +  obj.code+"&search_category="+selectedCat+"&best_search_keyword="+term,
										term: data.searchTerm,
										type: "brands",
										index: i,
										valueset: true
										});
							
						});
					}
					if(data.categories != null){
						
						if(data.categories[0]!= undefined){
							
							autoSearchData.push(
									{value: data.categories[0].name,
										code: data.categories[0].code,
										desc: data.categories[0].description,	
										//url:  "/mpl/en/search/?text=" + data.searchTerm + "&searchCategory=" + data.brands[0],
										url:  "/mpl/en/search/?q=" + data.searchTerm + "%3Arelevance%3Abrand%3A" +  data.categories[0].code+"&search_category="+selectedCat+"&best_search_keyword="+term,
										term: data.searchTerm,
										type: "category",
										index: 0,
										valueset: false
										});
						}
						
						$.each(data.categories, function (i, obj)
						{
							autoSearchData.push(
									{value: obj.name,
										code: obj.code,
										desc: obj.description,	
										//url: ACC.config.contextPath + obj.url + "/?q=" + data.searchTerm + "&text=" + data.searchTerm +"&searchCategory="+selectedCat,
										url:  "/mpl/en/search/?q=" + data.searchTerm + "%3Arelevance%3Acategory%3A" + obj.code+"&search_category="+selectedCat+"&best_search_keyword="+term,
										term: data.searchTerm,
										type: "category",
										index: i,
										valueset: true
										});
						});
					}
					/*if(data.seller != null){					
						$.each(data.categories, function (i, obj)
						{
							autoSearchData.push(
									{value: obj.name,
										code: obj.code,
										desc: obj.description,	
										url: ACC.config.contextPath + obj.url + "/?q=" + data.searchTerm + "&text=" + data.searchTerm +"&searchCategory="+selectedCat,
										term: data.searchTerm,
										type: "category"
										});
						});
					}*/
				/*	if(data.productNames != null){
						$.each(data.productNames, function (i, obj)
						{
							autoSearchData.push(
									{value: obj.name,
										code: obj.code,
										desc: obj.description,
										manufacturer: obj.manufacturer,
										url: ACC.config.contextPath + obj.url+ "/?searchCategory="+selectedCat,										
										type: "productName",										
										image: obj.images[0].url});
						});
					}*/
					
					if(data.suggestions != null){
					$.each(data.suggestions, function (i, obj)
			       	{
						if(i!=0){
						autoSearchData.push({
							value: obj.term,
							searchterm:term,
							url: ACC.config.encodedContextPath + "/search?text=" + obj.term +"&best_search_keyword="+term,
							type: "autoSuggestion"
						});
						}
					});
			     	}
					if(data.products != null){
						$.each(data.products, function (i, obj)
						{
							autoSearchData.push({
								value: obj.name,
								code: obj.code,
								desc: obj.description,
								manufacturer: obj.manufacturer,
								url:  obj.url  + "/?searchCategory="+selectedCat+"&best_product_id="+obj.code+"&search_category="+selectedCat+"&best_search_keyword="+term,
								price: obj.price.formattedValue,
								type: "productResult",
								image: (obj.images!=null && self.options.displayProductImages) ? obj.images[0].url : null // prevent errors if obj.images = null
							});
						});
					}
					self.options.cache[term] = autoSearchData;
					return response(autoSearchData);
				});
			}
		});
		
	
		$micrositesearch = $(".js-site-micrositesearch-input");
		if($micrositesearch.length>0){
			$micrositesearch.yautocomplete()
		}
	},

	bindSearchDropDown: function (department)
	{
		$('#micrositeSearchCategory').val('category-'+department);
	},
	
	bindSearchText: function (searchText)
	{
		$('#js-site-micrositesearch-input').val(searchText);
	},
	// For Microsite ShopByBrand Component
	bindMicrositeSearchDropDown: function (category)
	{
		//alert('this js called for microsite'+category);
		$('#micrositeSearchCategory').val(category);
	}
};
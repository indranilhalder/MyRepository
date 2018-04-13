$(document).ready(function() {
	//dropdown menu js starts 
	$('.category-l1 a').click(function(e) {
		e.preventDefault();
		if( $(this).next('ul').length > 0 ){
			
			$(this).parent().siblings().find('ul').hide();
			$(this).parent().siblings().find('a').removeClass('active-ul');
			
			$(this).next('ul').toggle();
			$(this).toggleClass('active-ul');
			
		}
	});

	if($('.category-l1 a').next('ul').length > 0){
		$('.category-l1 a').next('ul').prev('a').addClass('has-carrot');
	}

	if( $('.category-l1 > li').length > 8 ){
		 $('.category-l1 > li').slice(8).hide();
	}
	else{
		$('.view-more-categories').hide();
	}

	var hidden_categories = $('.category-l1 > li').length - 8;
	$('#hidden_categories_count').text(hidden_categories);

	$(document).on('click','.view-more-categories',function(e) {
		$('.category-l1 > li').slice(8).show();
		$(this).removeClass('view-more-categories').addClass('view-less-categories');
		$('#hidden_categories_text').text('less');
	});

	$(document).on('click','.view-less-categories',function(e) {
		$('.category-l1 > li').slice(8).hide();
		$(this).removeClass('view-less-categories').addClass('view-more-categories');
		$('#hidden_categories_text').text('more');
	});

	//dropdown menu js ends

	//toggle following brands
	$('#following-brands1,#following-brands2,#following-brands3,#following-brands4').click(function() {
		$(this).toggleClass('show-brands');
		$('.following-brands-slider').slick('refresh');
	});

	$('.mytablist').click(function() {
		$('.brands-slider').slick('refresh');
	});

	//search within brand list
	$('.search-for-brand').keyup(function(e) {
		var data = $(this).data('id');
		var input, filter, ul, li, a, i;
		input = document.getElementById("search-for-brand"+data);
		filter = input.value.toUpperCase();
		ul = document.getElementById("brandslist-leftsection"+data);
		li = ul.getElementsByTagName("li");
		for (i = 0; i < li.length; i++) {
			//a = li[i].getElementsByTagName("a")[0];
			if (li[i].innerHTML.toUpperCase().indexOf(filter) > -1) {
				li[i].style.display = "block";
				//li[i].parentElement.previousElementSibling.style.display = "block";
				//li[i].parentElement.style.display = "block";
			} else {
				li[i].style.display = "none";
				//li[i].parentElement.previousElementSibling.style.display = "none";
				//li[i].parentElement.style.display = "none";
			}
		}
	});		

	$('.brandinitials-section li a').click(function(e) {
		e.preventDefault();
		var bi_data_id = $(this).parents('.brandinitials-section').attr('id');
		var last_char = bi_data_id.substr(bi_data_id.length - 1);
		
		var brandinitial = $(this).attr('href');
		if( $(brandinitial).length > 0){
			//var brandinitial_offset = document.getElementById(brandinitial.substr(1)).offsetTop;
			var position = $('#brandslist-leftsection'+last_char+' '+brandinitial).position();
			var brandinitial_offset = position.top;
			$('#brandslist-leftsection'+last_char).scrollTop(brandinitial_offset);
			//document.getElementById('brandslist-leftsection'+last_char).scrollTop = brandinitial_offset;
		}
		$('#brandinitials-section'+last_char+' li a').removeClass('active-brandinitial');
		$(this).addClass('active-brandinitial');
	});
	

	$('.following-brands-slider').slick({
	  centerMode: true,
	  centerPadding: '20px',
	  slidesToShow: 3,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '12px',
			slidesToShow: 3,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	$('.hero-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 1,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '20px',
			slidesToShow: 1,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	$('.offer-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 1,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 1,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	$('.banner-product-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 2,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 2,
			slidesToScroll: 2,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});


	$('.video-product-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 2,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 2,
			slidesToScroll: 2,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	$('.theme-offers-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 2,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 2,
			slidesToScroll: 2,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	
	$('.theme-product-widget-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 2,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 2,
			slidesToScroll: 2,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});


	$('.auto-brand-product-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 2,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 2,
			slidesToScroll: 2,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	
	$('.subbrand-banner-blp-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 2,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 2,
			slidesToScroll: 2,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	$('.product-recommendation-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 2,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '15px',
			slidesToShow: 2,
			slidesToScroll: 2,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	//content widget
	$('.content-widget-slider').slick({
	  centerMode: true,
	  centerPadding: '60px',
	  slidesToShow: 1,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '35px',
			slidesToShow: 1,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	$('.brands-slider').slick({
	  centerMode: true,
	  arrows: false,
	  centerPadding: '60px',
	  slidesToShow: 1,
	  responsive: [
		{
		  breakpoint: 480,
		  settings: {
			arrows: false,
			centerMode: true,
			centerPadding: '20px',
			slidesToShow: 1,
			useCSS : true,
			cssEase : 'ease'
		  }
		}
	  ]
	});

	//countdown js starts
	// Set the date we're counting down to
	var end_time = $('#end_time').val();
	if(end_time){
		var countDownDate = new Date(end_time).getTime();
		//var countDownDate = new Date("2018-04-03 13:00:00").getTime();
		// Update the count down every 1 second
		var x = setInterval(function() {
			// Get todays date and time
			var now = new Date().getTime();
			// Find the distance between now an the count down date
			var distance = countDownDate - now;
			// Time calculations for days, hours, minutes and seconds
			var days = Math.floor(distance / (1000 * 60 * 60 * 24));
			var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
			var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
			var seconds = Math.floor((distance % (1000 * 60)) / 1000);
			// Output the result in an element with id="countdown"
			document.getElementById("countdown").innerHTML =  hours + ":" + minutes + ":" + seconds;
			// If the count down is over, write some text 
			if (distance < 0) {
				clearInterval(x);
				document.getElementById("countdown").innerHTML = "";
				document.getElementById("timer-icon").style.display = "none";
			}
		}, 1000);
	}
	//countdown js ends

	//play video js starts
	/*function playPause(myVideo) { 
		if (myVideo.paused) {
			myVideo.play(); 
		}
		else {
			myVideo.pause(); 
		}
	} */
	$('#play-video').click(function() {
		//var myVideo = document.getElementById("video-el"); 
		//playPause(myVideo);
		$("#video-el")[0].src += "?autoplay=1";
		$('#vpc-container').hide();
	});
	//play video js ends

});
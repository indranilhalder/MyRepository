
;(function($){
	$.fn.picZoomer = function(options){
		var opts = $.extend({}, $.fn.picZoomer.defaults, options), 

			$this = this,
//			$picBD = $('<div class="picZoomer-pic-wp"></div>').css({'width':opts.picWidth+'px', 'height':opts.picHeight+'px'}).appendTo($this),   demo
			$picBD = $('<div class="picZoomer-pic-wp"><div id="codId" style="display: none;" class="cod"><span>COD</span></div><div class="zoom" style="z-index:10000;"><a onClick="hit()"></a></div></div>').appendTo($this),
			$pic = $this.children('img').addClass('picZoomer-pic').appendTo($picBD),
			//$cursor = $('<div class="picZoomer-cursor"></div>').appendTo($picBD),
			//cursorSizeHalf = {w:$cursor.width()/2 ,h:$cursor.height()/2},
			$zoomWP = $('<div class="picZoomer-zoom-wp"><img src="" alt="" class="picZoomer-zoom-pic"></div>').appendTo($this),
			$zoomPic = $zoomWP.find('.picZoomer-zoom-pic'),
			picBDOffset = {x:$picBD.offset().left,y:$picBD.offset().top};
			
		
		opts.zoomWidth = opts.zoomWidth||opts.picWidth;
		opts.zoomHeight = opts.zoomHeight||opts.picHeight;
		var zoomWPSizeHalf = {w:opts.zoomWidth/2 ,h:opts.zoomHeight/2};


		$zoomWP.css({'width':opts.zoomWidth+'px', 'height':opts.zoomHeight+'px'});
		$zoomWP.css({'top': 0+'px', 'left': opts.picWidth+97+'px' , 'background-color':'#fff','border':'2px solid #000'});
		
		$zoomPic.css({'width':opts.picWidth*opts.scale+'px', 'height':opts.picHeight*opts.scale+'px'});

		var windowWidth=$(window).width();
		$picBD.on('mouseenter',function(event){
		var windowWidth=$(window).width();
		
		/*if(windowWidth > 974)
		{
			//$cursor.show();
			//$zoomWP.show();
		}*/
			//$zoomPic.attr('src',$("#zoomId img").attr('src')); 
		}).on('mouseleave',function(event){
			//$cursor.hide();
			$zoomWP.hide();
		}).on('mousemove', function(event){
			var x = event.pageX-picBDOffset.x,
				y = event.pageY-picBDOffset.y;
			if($("header .content .bottom").hasClass("active")) {
				y = event.pageY-picBDOffset.y+$(".bottom").height();
			}
			//$cursor.css({'left':x-cursorSizeHalf.w+'px', 'top':y-cursorSizeHalf.h+'px'});
			$zoomPic.css({'left':-(x*opts.scale-zoomWPSizeHalf.w)+(-50)+'px', 'top':-(y*opts.scale-zoomWPSizeHalf.h)+(-250)+'px'});

		});
		return $this;

	};
	$.fn.picZoomer.defaults = {
		picWidth: 470,
		picHeight: 633,
		scale: 2,
		zoomerPosition: {top: '0', left: '350px'}
		/*,
		zoomWidth: 320,
		zoomHeight: 320*/
	};
})(jQuery);
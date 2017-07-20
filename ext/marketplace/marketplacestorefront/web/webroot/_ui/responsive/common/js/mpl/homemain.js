/*! jquery.cookie v1.4.1 | MIT */
!function(a){"function"==typeof define&&define.amd?define(["jquery"],a):"object"==typeof exports?a(require("jquery")):a(jQuery)}(function(a){function b(a){return h.raw?a:encodeURIComponent(a)}function c(a){return h.raw?a:decodeURIComponent(a)}function d(a){return b(h.json?JSON.stringify(a):String(a))}function e(a){0===a.indexOf('"')&&(a=a.slice(1,-1).replace(/\\"/g,'"').replace(/\\\\/g,"\\"));try{return a=decodeURIComponent(a.replace(g," ")),h.json?JSON.parse(a):a}catch(b){}}function f(b,c){var d=h.raw?b:e(b);return a.isFunction(c)?c(d):d}var g=/\+/g,h=a.cookie=function(e,g,i){if(void 0!==g&&!a.isFunction(g)){if(i=a.extend({},h.defaults,i),"number"==typeof i.expires){var j=i.expires,k=i.expires=new Date;k.setTime(+k+864e5*j)}return document.cookie=[b(e),"=",d(g),i.expires?"; expires="+i.expires.toUTCString():"",i.path?"; path="+i.path:"",i.domain?"; domain="+i.domain:"",i.secure?"; secure":""].join("")}for(var l=e?void 0:{},m=document.cookie?document.cookie.split("; "):[],n=0,o=m.length;o>n;n++){var p=m[n].split("="),q=c(p.shift()),r=p.join("=");if(e&&e===q){l=f(r,g);break}e||void 0===(r=f(r))||(l[q]=r)}return l};h.defaults={},a.removeCookie=function(b,c){return void 0===a.cookie(b)?!1:(a.cookie(b,"",a.extend({},c,{expires:-1})),!a.cookie(b))}});
!function(a,b,c,d){function e(b,c){this.settings=null,this.options=a.extend({},e.Defaults,c),this.$element=a(b),this.drag=a.extend({},m),this.state=a.extend({},n),this.e=a.extend({},o),this._plugins={},this._supress={},this._current=null,this._speed=null,this._coordinates=[],this._breakpoint=null,this._width=null,this._items=[],this._clones=[],this._mergers=[],this._invalidated={},this._pipe=[],a.each(e.Plugins,a.proxy(function(a,b){this._plugins[a[0].toLowerCase()+a.slice(1)]=new b(this)},this)),a.each(e.Pipe,a.proxy(function(b,c){this._pipe.push({filter:c.filter,run:a.proxy(c.run,this)})},this)),this.setup(),this.initialize()}function f(a){if(a.touches!==d)return{x:a.touches[0].pageX,y:a.touches[0].pageY};if(a.touches===d){if(a.pageX!==d)return{x:a.pageX,y:a.pageY};if(a.pageX===d)return{x:a.clientX,y:a.clientY}}}function g(a){var b,d,e=c.createElement("div"),f=a;for(b in f)if(d=f[b],"undefined"!=typeof e.style[d])return e=null,[d,b];return[!1]}function h(){return g(["transition","WebkitTransition","MozTransition","OTransition"])[1]}function i(){return g(["transform","WebkitTransform","MozTransform","OTransform","msTransform"])[0]}function j(){return g(["perspective","webkitPerspective","MozPerspective","OPerspective","MsPerspective"])[0]}function k(){return"ontouchstart"in b||!!navigator.msMaxTouchPoints}function l(){return b.navigator.msPointerEnabled}var m,n,o;m={start:0,startX:0,startY:0,current:0,currentX:0,currentY:0,offsetX:0,offsetY:0,distance:null,startTime:0,endTime:0,updatedX:0,targetEl:null},n={isTouch:!1,isScrolling:!1,isSwiping:!1,direction:!1,inMotion:!1},o={_onDragStart:null,_onDragMove:null,_onDragEnd:null,_transitionEnd:null,_resizer:null,_responsiveCall:null,_goToLoop:null,_checkVisibile:null},e.Defaults={items:3,loop:!1,center:!1,mouseDrag:!0,touchDrag:!0,pullDrag:!0,freeDrag:!1,margin:0,stagePadding:0,merge:!1,mergeFit:!0,autoWidth:!1,startPosition:0,rtl:!1,smartSpeed:250,fluidSpeed:!1,dragEndSpeed:!1,responsive:{},responsiveRefreshRate:200,responsiveBaseElement:b,responsiveClass:!1,fallbackEasing:"swing",info:!1,nestedItemSelector:!1,itemElement:"div",stageElement:"div",themeClass:"owl-theme",baseClass:"owl-carousel",itemClass:"owl-item",centerClass:"center",activeClass:"active"},e.Width={Default:"default",Inner:"inner",Outer:"outer"},e.Plugins={},e.Pipe=[{filter:["width","items","settings"],run:function(a){a.current=this._items&&this._items[this.relative(this._current)]}},{filter:["items","settings"],run:function(){var a=this._clones,b=this.$stage.children(".cloned");(b.length!==a.length||!this.settings.loop&&a.length>0)&&(this.$stage.children(".cloned").remove(),this._clones=[])}},{filter:["items","settings"],run:function(){var a,b,c=this._clones,d=this._items,e=this.settings.loop?c.length-Math.max(2*this.settings.items,4):0;for(a=0,b=Math.abs(e/2);b>a;a++)e>0?(this.$stage.children().eq(d.length+c.length-1).remove(),c.pop(),this.$stage.children().eq(0).remove(),c.pop()):(c.push(c.length/2),this.$stage.append(d[c[c.length-1]].clone().addClass("cloned")),c.push(d.length-1-(c.length-1)/2),this.$stage.prepend(d[c[c.length-1]].clone().addClass("cloned")))}},{filter:["width","items","settings"],run:function(){var a,b,c,d=this.settings.rtl?1:-1,e=(this.width()/this.settings.items).toFixed(3),f=0;for(this._coordinates=[],b=0,c=this._clones.length+this._items.length;c>b;b++)a=this._mergers[this.relative(b)],a=this.settings.mergeFit&&Math.min(a,this.settings.items)||a,f+=(this.settings.autoWidth?this._items[this.relative(b)].width()+this.settings.margin:e*a)*d,this._coordinates.push(f)}},{filter:["width","items","settings"],run:function(){var b,c,d=(this.width()/this.settings.items).toFixed(3),e={width:Math.abs(this._coordinates[this._coordinates.length-1])+2*this.settings.stagePadding,"padding-left":this.settings.stagePadding||"","padding-right":this.settings.stagePadding||""};if(this.$stage.css(e),e={width:this.settings.autoWidth?"auto":d-this.settings.margin},e[this.settings.rtl?"margin-left":"margin-right"]=this.settings.margin,!this.settings.autoWidth&&a.grep(this._mergers,function(a){return a>1}).length>0)for(b=0,c=this._coordinates.length;c>b;b++)e.width=Math.abs(this._coordinates[b])-Math.abs(this._coordinates[b-1]||0)-this.settings.margin,this.$stage.children().eq(b).css(e);else this.$stage.children().css(e)}},{filter:["width","items","settings"],run:function(a){a.current&&this.reset(this.$stage.children().index(a.current))}},{filter:["position"],run:function(){this.animate(this.coordinates(this._current))}},{filter:["width","position","items","settings"],run:function(){var a,b,c,d,e=this.settings.rtl?1:-1,f=2*this.settings.stagePadding,g=this.coordinates(this.current())+f,h=g+this.width()*e,i=[];for(c=0,d=this._coordinates.length;d>c;c++)a=this._coordinates[c-1]||0,b=Math.abs(this._coordinates[c])+f*e,(this.op(a,"<=",g)&&this.op(a,">",h)||this.op(b,"<",g)&&this.op(b,">",h))&&i.push(c);this.$stage.children("."+this.settings.activeClass).removeClass(this.settings.activeClass),this.$stage.children(":eq("+i.join("), :eq(")+")").addClass(this.settings.activeClass),this.settings.center&&(this.$stage.children("."+this.settings.centerClass).removeClass(this.settings.centerClass),this.$stage.children().eq(this.current()).addClass(this.settings.centerClass))}}],e.prototype.initialize=function(){if(this.trigger("initialize"),this.$element.addClass(this.settings.baseClass).addClass(this.settings.themeClass).toggleClass("owl-rtl",this.settings.rtl),this.browserSupport(),this.settings.autoWidth&&this.state.imagesLoaded!==!0){var b,c,e;if(b=this.$element.find("img"),c=this.settings.nestedItemSelector?"."+this.settings.nestedItemSelector:d,e=this.$element.children(c).width(),b.length&&0>=e)return this.preloadAutoWidthImages(b),!1}this.$element.addClass("owl-loading"),this.$stage=a("<"+this.settings.stageElement+' class="owl-stage"/>').wrap('<div class="owl-stage-outer">'),this.$element.append(this.$stage.parent()),this.replace(this.$element.children().not(this.$stage.parent())),this._width=this.$element.width(),this.refresh(),this.$element.removeClass("owl-loading").addClass("owl-loaded"),this.eventsCall(),this.internalEvents(),this.addTriggerableEvents(),this.trigger("initialized")},e.prototype.setup=function(){var b=this.viewport(),c=this.options.responsive,d=-1,e=null;c?(a.each(c,function(a){b>=a&&a>d&&(d=Number(a))}),e=a.extend({},this.options,c[d]),delete e.responsive,e.responsiveClass&&this.$element.attr("class",function(a,b){return b.replace(/\b owl-responsive-\S+/g,"")}).addClass("owl-responsive-"+d)):e=a.extend({},this.options),(null===this.settings||this._breakpoint!==d)&&(this.trigger("change",{property:{name:"settings",value:e}}),this._breakpoint=d,this.settings=e,this.invalidate("settings"),this.trigger("changed",{property:{name:"settings",value:this.settings}}))},e.prototype.optionsLogic=function(){this.$element.toggleClass("owl-center",this.settings.center),this.settings.loop&&this._items.length<this.settings.items&&(this.settings.loop=!1),this.settings.autoWidth&&(this.settings.stagePadding=!1,this.settings.merge=!1)},e.prototype.prepare=function(b){var c=this.trigger("prepare",{content:b});return c.data||(c.data=a("<"+this.settings.itemElement+"/>").addClass(this.settings.itemClass).append(b)),this.trigger("prepared",{content:c.data}),c.data},e.prototype.update=function(){for(var b=0,c=this._pipe.length,d=a.proxy(function(a){return this[a]},this._invalidated),e={};c>b;)(this._invalidated.all||a.grep(this._pipe[b].filter,d).length>0)&&this._pipe[b].run(e),b++;this._invalidated={}},e.prototype.width=function(a){switch(a=a||e.Width.Default){case e.Width.Inner:case e.Width.Outer:return this._width;default:return this._width-2*this.settings.stagePadding+this.settings.margin}},e.prototype.refresh=function(){if(0===this._items.length)return!1;(new Date).getTime();this.trigger("refresh"),this.setup(),this.optionsLogic(),this.$stage.addClass("owl-refresh"),this.update(),this.$stage.removeClass("owl-refresh"),this.state.orientation=b.orientation,this.watchVisibility(),this.trigger("refreshed")},e.prototype.eventsCall=function(){this.e._onDragStart=a.proxy(function(a){this.onDragStart(a)},this),this.e._onDragMove=a.proxy(function(a){this.onDragMove(a)},this),this.e._onDragEnd=a.proxy(function(a){this.onDragEnd(a)},this),this.e._onResize=a.proxy(function(a){this.onResize(a)},this),this.e._transitionEnd=a.proxy(function(a){this.transitionEnd(a)},this),this.e._preventClick=a.proxy(function(a){this.preventClick(a)},this)},e.prototype.onThrottledResize=function(){b.clearTimeout(this.resizeTimer),this.resizeTimer=b.setTimeout(this.e._onResize,this.settings.responsiveRefreshRate)},e.prototype.onResize=function(){return this._items.length?this._width===this.$element.width()?!1:this.trigger("resize").isDefaultPrevented()?!1:(this._width=this.$element.width(),this.invalidate("width"),this.refresh(),void this.trigger("resized")):!1},e.prototype.eventsRouter=function(a){var b=a.type;"mousedown"===b||"touchstart"===b?this.onDragStart(a):"mousemove"===b||"touchmove"===b?this.onDragMove(a):"mouseup"===b||"touchend"===b?this.onDragEnd(a):"touchcancel"===b&&this.onDragEnd(a)},e.prototype.internalEvents=function(){var c=(k(),l());this.settings.mouseDrag?(this.$stage.on("mousedown",a.proxy(function(a){this.eventsRouter(a)},this)),this.$stage.on("dragstart",function(){return!1}),this.$stage.get(0).onselectstart=function(){return!1}):this.$element.addClass("owl-text-select-on"),this.settings.touchDrag&&!c&&this.$stage.on("touchstart touchcancel",a.proxy(function(a){this.eventsRouter(a)},this)),this.transitionEndVendor&&this.on(this.$stage.get(0),this.transitionEndVendor,this.e._transitionEnd,!1),this.settings.responsive!==!1&&this.on(b,"resize",a.proxy(this.onThrottledResize,this))},e.prototype.onDragStart=function(d){var e,g,h,i;if(e=d.originalEvent||d||b.event,3===e.which||this.state.isTouch)return!1;if("mousedown"===e.type&&this.$stage.addClass("owl-grab"),this.trigger("drag"),this.drag.startTime=(new Date).getTime(),this.speed(0),this.state.isTouch=!0,this.state.isScrolling=!1,this.state.isSwiping=!1,this.drag.distance=0,g=f(e).x,h=f(e).y,this.drag.offsetX=this.$stage.position().left,this.drag.offsetY=this.$stage.position().top,this.settings.rtl&&(this.drag.offsetX=this.$stage.position().left+this.$stage.width()-this.width()+this.settings.margin),this.state.inMotion&&this.support3d)i=this.getTransformProperty(),this.drag.offsetX=i,this.animate(i),this.state.inMotion=!0;else if(this.state.inMotion&&!this.support3d)return this.state.inMotion=!1,!1;this.drag.startX=g-this.drag.offsetX,this.drag.startY=h-this.drag.offsetY,this.drag.start=g-this.drag.startX,this.drag.targetEl=e.target||e.srcElement,this.drag.updatedX=this.drag.start,("IMG"===this.drag.targetEl.tagName||"A"===this.drag.targetEl.tagName)&&(this.drag.targetEl.draggable=!1),a(c).on("mousemove.owl.dragEvents mouseup.owl.dragEvents touchmove.owl.dragEvents touchend.owl.dragEvents",a.proxy(function(a){this.eventsRouter(a)},this))},e.prototype.onDragMove=function(a){var c,e,g,h,i,j;this.state.isTouch&&(this.state.isScrolling||(c=a.originalEvent||a||b.event,e=f(c).x,g=f(c).y,this.drag.currentX=e-this.drag.startX,this.drag.currentY=g-this.drag.startY,this.drag.distance=this.drag.currentX-this.drag.offsetX,this.drag.distance<0?this.state.direction=this.settings.rtl?"right":"left":this.drag.distance>0&&(this.state.direction=this.settings.rtl?"left":"right"),this.settings.loop?this.op(this.drag.currentX,">",this.coordinates(this.minimum()))&&"right"===this.state.direction?this.drag.currentX-=(this.settings.center&&this.coordinates(0))-this.coordinates(this._items.length):this.op(this.drag.currentX,"<",this.coordinates(this.maximum()))&&"left"===this.state.direction&&(this.drag.currentX+=(this.settings.center&&this.coordinates(0))-this.coordinates(this._items.length)):(h=this.coordinates(this.settings.rtl?this.maximum():this.minimum()),i=this.coordinates(this.settings.rtl?this.minimum():this.maximum()),j=this.settings.pullDrag?this.drag.distance/5:0,this.drag.currentX=Math.max(Math.min(this.drag.currentX,h+j),i+j)),(this.drag.distance>8||this.drag.distance<-8)&&(c.preventDefault!==d?c.preventDefault():c.returnValue=!1,this.state.isSwiping=!0),this.drag.updatedX=this.drag.currentX,(this.drag.currentY>16||this.drag.currentY<-16)&&this.state.isSwiping===!1&&(this.state.isScrolling=!0,this.drag.updatedX=this.drag.start),this.animate(this.drag.updatedX)))},e.prototype.onDragEnd=function(b){var d,e,f;if(this.state.isTouch){if("mouseup"===b.type&&this.$stage.removeClass("owl-grab"),this.trigger("dragged"),this.drag.targetEl.removeAttribute("draggable"),this.state.isTouch=!1,this.state.isScrolling=!1,this.state.isSwiping=!1,0===this.drag.distance&&this.state.inMotion!==!0)return this.state.inMotion=!1,!1;this.drag.endTime=(new Date).getTime(),d=this.drag.endTime-this.drag.startTime,e=Math.abs(this.drag.distance),(e>3||d>300)&&this.removeClick(this.drag.targetEl),f=this.closest(this.drag.updatedX),this.speed(this.settings.dragEndSpeed||this.settings.smartSpeed),this.current(f),this.invalidate("position"),this.update(),this.settings.pullDrag||this.drag.updatedX!==this.coordinates(f)||this.transitionEnd(),this.drag.distance=0,a(c).off(".owl.dragEvents")}},e.prototype.removeClick=function(c){this.drag.targetEl=c,a(c).on("click.preventClick",this.e._preventClick),b.setTimeout(function(){a(c).off("click.preventClick")},300)},e.prototype.preventClick=function(b){b.preventDefault?b.preventDefault():b.returnValue=!1,b.stopPropagation&&b.stopPropagation(),a(b.target).off("click.preventClick")},e.prototype.getTransformProperty=function(){var a,c;return a=b.getComputedStyle(this.$stage.get(0),null).getPropertyValue(this.vendorName+"transform"),a=a.replace(/matrix(3d)?\(|\)/g,"").split(","),c=16===a.length,c!==!0?a[4]:a[12]},e.prototype.closest=function(b){var c=-1,d=30,e=this.width(),f=this.coordinates();return this.settings.freeDrag||a.each(f,a.proxy(function(a,g){return b>g-d&&g+d>b?c=a:this.op(b,"<",g)&&this.op(b,">",f[a+1]||g-e)&&(c="left"===this.state.direction?a+1:a),-1===c},this)),this.settings.loop||(this.op(b,">",f[this.minimum()])?c=b=this.minimum():this.op(b,"<",f[this.maximum()])&&(c=b=this.maximum())),c},e.prototype.animate=function(b){this.trigger("translate"),this.state.inMotion=this.speed()>0,this.support3d?this.$stage.css({transform:"translate3d("+b+"px,0px, 0px)",transition:this.speed()/1e3+"s"}):this.state.isTouch?this.$stage.css({left:b+"px"}):this.$stage.animate({left:b},this.speed()/1e3,this.settings.fallbackEasing,a.proxy(function(){this.state.inMotion&&this.transitionEnd()},this))},e.prototype.current=function(a){if(a===d)return this._current;if(0===this._items.length)return d;if(a=this.normalize(a),this._current!==a){var b=this.trigger("change",{property:{name:"position",value:a}});b.data!==d&&(a=this.normalize(b.data)),this._current=a,this.invalidate("position"),this.trigger("changed",{property:{name:"position",value:this._current}})}return this._current},e.prototype.invalidate=function(a){this._invalidated[a]=!0},e.prototype.reset=function(a){a=this.normalize(a),a!==d&&(this._speed=0,this._current=a,this.suppress(["translate","translated"]),this.animate(this.coordinates(a)),this.release(["translate","translated"]))},e.prototype.normalize=function(b,c){var e=c?this._items.length:this._items.length+this._clones.length;return!a.isNumeric(b)||1>e?d:b=this._clones.length?(b%e+e)%e:Math.max(this.minimum(c),Math.min(this.maximum(c),b))},e.prototype.relative=function(a){return a=this.normalize(a),a-=this._clones.length/2,this.normalize(a,!0)},e.prototype.maximum=function(a){var b,c,d,e=0,f=this.settings;if(a)return this._items.length-1;if(!f.loop&&f.center)b=this._items.length-1;else if(f.loop||f.center)if(f.loop||f.center)b=this._items.length+f.items;else{if(!f.autoWidth&&!f.merge)throw"Can not detect maximum absolute position.";for(revert=f.rtl?1:-1,c=this.$stage.width()-this.$element.width();(d=this.coordinates(e))&&!(d*revert>=c);)b=++e}else b=this._items.length-f.items;return b},e.prototype.minimum=function(a){return a?0:this._clones.length/2},e.prototype.items=function(a){return a===d?this._items.slice():(a=this.normalize(a,!0),this._items[a])},e.prototype.mergers=function(a){return a===d?this._mergers.slice():(a=this.normalize(a,!0),this._mergers[a])},e.prototype.clones=function(b){var c=this._clones.length/2,e=c+this._items.length,f=function(a){return a%2===0?e+a/2:c-(a+1)/2};return b===d?a.map(this._clones,function(a,b){return f(b)}):a.map(this._clones,function(a,c){return a===b?f(c):null})},e.prototype.speed=function(a){return a!==d&&(this._speed=a),this._speed},e.prototype.coordinates=function(b){var c=null;return b===d?a.map(this._coordinates,a.proxy(function(a,b){return this.coordinates(b)},this)):(this.settings.center?(c=this._coordinates[b],c+=(this.width()-c+(this._coordinates[b-1]||0))/2*(this.settings.rtl?-1:1)):c=this._coordinates[b-1]||0,c)},e.prototype.duration=function(a,b,c){return Math.min(Math.max(Math.abs(b-a),1),6)*Math.abs(c||this.settings.smartSpeed)},e.prototype.to=function(c,d){if(this.settings.loop){var e=c-this.relative(this.current()),f=this.current(),g=this.current(),h=this.current()+e,i=0>g-h?!0:!1,j=this._clones.length+this._items.length;h<this.settings.items&&i===!1?(f=g+this._items.length,this.reset(f)):h>=j-this.settings.items&&i===!0&&(f=g-this._items.length,this.reset(f)),b.clearTimeout(this.e._goToLoop),this.e._goToLoop=b.setTimeout(a.proxy(function(){this.speed(this.duration(this.current(),f+e,d)),this.current(f+e),this.update()},this),30)}else this.speed(this.duration(this.current(),c,d)),this.current(c),this.update()},e.prototype.next=function(a){a=a||!1,this.to(this.relative(this.current())+1,a)},e.prototype.prev=function(a){a=a||!1,this.to(this.relative(this.current())-1,a)},e.prototype.transitionEnd=function(a){return a!==d&&(a.stopPropagation(),(a.target||a.srcElement||a.originalTarget)!==this.$stage.get(0))?!1:(this.state.inMotion=!1,void this.trigger("translated"))},e.prototype.viewport=function(){var d;if(this.options.responsiveBaseElement!==b)d=a(this.options.responsiveBaseElement).width();else if(b.innerWidth)d=b.innerWidth;else{if(!c.documentElement||!c.documentElement.clientWidth)throw"Can not detect viewport width.";d=c.documentElement.clientWidth}return d},e.prototype.replace=function(b){this.$stage.empty(),this._items=[],b&&(b=b instanceof jQuery?b:a(b)),this.settings.nestedItemSelector&&(b=b.find("."+this.settings.nestedItemSelector)),b.filter(function(){return 1===this.nodeType}).each(a.proxy(function(a,b){b=this.prepare(b),this.$stage.append(b),this._items.push(b),this._mergers.push(1*b.find("[data-merge]").andSelf("[data-merge]").attr("data-merge")||1)},this)),this.reset(a.isNumeric(this.settings.startPosition)?this.settings.startPosition:0),this.invalidate("items")},e.prototype.add=function(a,b){b=b===d?this._items.length:this.normalize(b,!0),this.trigger("add",{content:a,position:b}),0===this._items.length||b===this._items.length?(this.$stage.append(a),this._items.push(a),this._mergers.push(1*a.find("[data-merge]").andSelf("[data-merge]").attr("data-merge")||1)):(this._items[b].before(a),this._items.splice(b,0,a),this._mergers.splice(b,0,1*a.find("[data-merge]").andSelf("[data-merge]").attr("data-merge")||1)),this.invalidate("items"),this.trigger("added",{content:a,position:b})},e.prototype.remove=function(a){a=this.normalize(a,!0),a!==d&&(this.trigger("remove",{content:this._items[a],position:a}),this._items[a].remove(),this._items.splice(a,1),this._mergers.splice(a,1),this.invalidate("items"),this.trigger("removed",{content:null,position:a}))},e.prototype.addTriggerableEvents=function(){var b=a.proxy(function(b,c){return a.proxy(function(a){a.relatedTarget!==this&&(this.suppress([c]),b.apply(this,[].slice.call(arguments,1)),this.release([c]))},this)},this);a.each({next:this.next,prev:this.prev,to:this.to,destroy:this.destroy,refresh:this.refresh,replace:this.replace,add:this.add,remove:this.remove},a.proxy(function(a,c){this.$element.on(a+".owl.carousel",b(c,a+".owl.carousel"))},this))},e.prototype.watchVisibility=function(){function c(a){return a.offsetWidth>0&&a.offsetHeight>0}function d(){c(this.$element.get(0))&&(this.$element.removeClass("owl-hidden"),this.refresh(),b.clearInterval(this.e._checkVisibile))}c(this.$element.get(0))||(this.$element.addClass("owl-hidden"),b.clearInterval(this.e._checkVisibile),this.e._checkVisibile=b.setInterval(a.proxy(d,this),500))},e.prototype.preloadAutoWidthImages=function(b){var c,d,e,f;c=0,d=this,b.each(function(g,h){e=a(h),f=new Image,f.onload=function(){c++,e.attr("src",f.src),e.css("opacity",1),c>=b.length&&(d.state.imagesLoaded=!0,d.initialize())},f.src=e.attr("src")||e.attr("data-src")||e.attr("data-src-retina")})},e.prototype.destroy=function(){this.$element.hasClass(this.settings.themeClass)&&this.$element.removeClass(this.settings.themeClass),this.settings.responsive!==!1&&a(b).off("resize.owl.carousel"),this.transitionEndVendor&&this.off(this.$stage.get(0),this.transitionEndVendor,this.e._transitionEnd);for(var d in this._plugins)this._plugins[d].destroy();(this.settings.mouseDrag||this.settings.touchDrag)&&(this.$stage.off("mousedown touchstart touchcancel"),a(c).off(".owl.dragEvents"),this.$stage.get(0).onselectstart=function(){},this.$stage.off("dragstart",function(){return!1})),this.$element.off(".owl"),this.$stage.children(".cloned").remove(),this.e=null,this.$element.removeData("owlCarousel"),this.$stage.children().contents().unwrap(),this.$stage.children().unwrap(),this.$stage.unwrap()},e.prototype.op=function(a,b,c){var d=this.settings.rtl;switch(b){case"<":return d?a>c:c>a;case">":return d?c>a:a>c;case">=":return d?c>=a:a>=c;case"<=":return d?a>=c:c>=a}},e.prototype.on=function(a,b,c,d){a.addEventListener?a.addEventListener(b,c,d):a.attachEvent&&a.attachEvent("on"+b,c)},e.prototype.off=function(a,b,c,d){a.removeEventListener?a.removeEventListener(b,c,d):a.detachEvent&&a.detachEvent("on"+b,c)},e.prototype.trigger=function(b,c,d){var e={item:{count:this._items.length,index:this.current()}},f=a.camelCase(a.grep(["on",b,d],function(a){return a}).join("-").toLowerCase()),g=a.Event([b,"owl",d||"carousel"].join(".").toLowerCase(),a.extend({relatedTarget:this},e,c));return this._supress[b]||(a.each(this._plugins,function(a,b){b.onTrigger&&b.onTrigger(g)}),this.$element.trigger(g),this.settings&&"function"==typeof this.settings[f]&&this.settings[f].apply(this,g)),g},e.prototype.suppress=function(b){a.each(b,a.proxy(function(a,b){this._supress[b]=!0},this))},e.prototype.release=function(b){a.each(b,a.proxy(function(a,b){delete this._supress[b]},this))},e.prototype.browserSupport=function(){if(this.support3d=j(),this.support3d){this.transformVendor=i();var a=["transitionend","webkitTransitionEnd","transitionend","oTransitionEnd"];this.transitionEndVendor=a[h()],this.vendorName=this.transformVendor.replace(/Transform/i,""),this.vendorName=""!==this.vendorName?"-"+this.vendorName.toLowerCase()+"-":""}this.state.orientation=b.orientation},a.fn.owlCarousel=function(b){return this.each(function(){a(this).data("owlCarousel")||a(this).data("owlCarousel",new e(this,b))})},a.fn.owlCarousel.Constructor=e}(window.Zepto||window.jQuery,window,document),function(a,b){var c=function(b){this._core=b,this._loaded=[],this._handlers={"initialized.owl.carousel change.owl.carousel":a.proxy(function(b){if(b.namespace&&this._core.settings&&this._core.settings.lazyLoad&&(b.property&&"position"==b.property.name||"initialized"==b.type))for(var c=this._core.settings,d=c.center&&Math.ceil(c.items/2)||c.items,e=c.center&&-1*d||0,f=(b.property&&b.property.value||this._core.current())+e,g=this._core.clones().length,h=a.proxy(function(a,b){this.load(b)},this);e++<d;)this.load(g/2+this._core.relative(f)),g&&a.each(this._core.clones(this._core.relative(f++)),h)},this)},this._core.options=a.extend({},c.Defaults,this._core.options),this._core.$element.on(this._handlers)};c.Defaults={lazyLoad:!1},c.prototype.load=function(c){var d=this._core.$stage.children().eq(c),e=d&&d.find(".owl-lazy");!e||a.inArray(d.get(0),this._loaded)>-1||(e.each(a.proxy(function(c,d){var e,f=a(d),g=b.devicePixelRatio>1&&f.attr("data-src-retina")||f.attr("data-src");this._core.trigger("load",{element:f,url:g},"lazy"),f.is("img")?f.one("load.owl.lazy",a.proxy(function(){f.css("opacity",1),this._core.trigger("loaded",{element:f,url:g},"lazy")},this)).attr("src",g):(e=new Image,e.onload=a.proxy(function(){f.css({"background-image":"url("+g+")",opacity:"1"}),this._core.trigger("loaded",{element:f,url:g},"lazy")},this),e.src=g)},this)),this._loaded.push(d.get(0)))},c.prototype.destroy=function(){var a,b;for(a in this.handlers)this._core.$element.off(a,this.handlers[a]);for(b in Object.getOwnPropertyNames(this))"function"!=typeof this[b]&&(this[b]=null)},a.fn.owlCarousel.Constructor.Plugins.Lazy=c}(window.Zepto||window.jQuery,window,document),function(a){var b=function(c){this._core=c,this._handlers={"initialized.owl.carousel":a.proxy(function(){this._core.settings.autoHeight&&this.update()},this),"changed.owl.carousel":a.proxy(function(a){this._core.settings.autoHeight&&"position"==a.property.name&&this.update()},this),"loaded.owl.lazy":a.proxy(function(a){this._core.settings.autoHeight&&a.element.closest("."+this._core.settings.itemClass)===this._core.$stage.children().eq(this._core.current())&&this.update()},this)},this._core.options=a.extend({},b.Defaults,this._core.options),this._core.$element.on(this._handlers)};b.Defaults={autoHeight:!1,autoHeightClass:"owl-height"},b.prototype.update=function(){this._core.$stage.parent().height(this._core.$stage.children().eq(this._core.current()).height()).addClass(this._core.settings.autoHeightClass)},b.prototype.destroy=function(){var a,b;for(a in this._handlers)this._core.$element.off(a,this._handlers[a]);for(b in Object.getOwnPropertyNames(this))"function"!=typeof this[b]&&(this[b]=null)},a.fn.owlCarousel.Constructor.Plugins.AutoHeight=b}(window.Zepto||window.jQuery,window,document),function(a,b,c){var d=function(b){this._core=b,this._videos={},this._playing=null,this._fullscreen=!1,this._handlers={"resize.owl.carousel":a.proxy(function(a){this._core.settings.video&&!this.isInFullScreen()&&a.preventDefault()},this),"refresh.owl.carousel changed.owl.carousel":a.proxy(function(){this._playing&&this.stop()},this),"prepared.owl.carousel":a.proxy(function(b){var c=a(b.content).find(".owl-video");c.length&&(c.css("display","none"),this.fetch(c,a(b.content)))},this)},this._core.options=a.extend({},d.Defaults,this._core.options),this._core.$element.on(this._handlers),this._core.$element.on("click.owl.video",".owl-video-play-icon",a.proxy(function(a){this.play(a)},this))};d.Defaults={video:!1,videoHeight:!1,videoWidth:!1},d.prototype.fetch=function(a,b){var c=a.attr("data-vimeo-id")?"vimeo":"youtube",d=a.attr("data-vimeo-id")||a.attr("data-youtube-id"),e=a.attr("data-width")||this._core.settings.videoWidth,f=a.attr("data-height")||this._core.settings.videoHeight,g=a.attr("href");if(!g)throw new Error("Missing video URL.");if(d=g.match(/(http:|https:|)\/\/(player.|www.)?(vimeo\.com|youtu(be\.com|\.be|be\.googleapis\.com))\/(video\/|embed\/|watch\?v=|v\/)?([A-Za-z0-9._%-]*)(\&\S+)?/),d[3].indexOf("youtu")>-1)c="youtube";else{if(!(d[3].indexOf("vimeo")>-1))throw new Error("Video URL not supported.");c="vimeo"}d=d[6],this._videos[g]={type:c,id:d,width:e,height:f},b.attr("data-video",g),this.thumbnail(a,this._videos[g])},d.prototype.thumbnail=function(b,c){var d,e,f,g=c.width&&c.height?'style="width:'+c.width+"px;height:"+c.height+'px;"':"",h=b.find("img"),i="src",j="",k=this._core.settings,l=function(a){e='<div class="owl-video-play-icon"></div>',d=k.lazyLoad?'<div class="owl-video-tn '+j+'" '+i+'="'+a+'"></div>':'<div class="owl-video-tn" style="opacity:1;background-image:url('+a+')"></div>',b.after(d),b.after(e)};return b.wrap('<div class="owl-video-wrapper"'+g+"></div>"),this._core.settings.lazyLoad&&(i="data-src",j="owl-lazy"),h.length?(l(h.attr(i)),h.remove(),!1):void("youtube"===c.type?(f="http://img.youtube.com/vi/"+c.id+"/hqdefault.jpg",l(f)):"vimeo"===c.type&&a.ajax({type:"GET",url:"http://vimeo.com/api/v2/video/"+c.id+".json",jsonp:"callback",dataType:"jsonp",success:function(a){f=a[0].thumbnail_large,l(f)}}))},d.prototype.stop=function(){this._core.trigger("stop",null,"video"),this._playing.find(".owl-video-frame").remove(),this._playing.removeClass("owl-video-playing"),this._playing=null},d.prototype.play=function(b){this._core.trigger("play",null,"video"),this._playing&&this.stop();var c,d,e=a(b.target||b.srcElement),f=e.closest("."+this._core.settings.itemClass),g=this._videos[f.attr("data-video")],h=g.width||"100%",i=g.height||this._core.$stage.height();"youtube"===g.type?c='<iframe width="'+h+'" height="'+i+'" src="http://www.youtube.com/embed/'+g.id+"?autoplay=1&v="+g.id+'" frameborder="0" allowfullscreen></iframe>':"vimeo"===g.type&&(c='<iframe src="http://player.vimeo.com/video/'+g.id+'?autoplay=1" width="'+h+'" height="'+i+'" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>'),f.addClass("owl-video-playing"),this._playing=f,d=a('<div style="height:'+i+"px; width:"+h+'px" class="owl-video-frame">'+c+"</div>"),e.after(d)},d.prototype.isInFullScreen=function(){var d=c.fullscreenElement||c.mozFullScreenElement||c.webkitFullscreenElement;return d&&a(d).parent().hasClass("owl-video-frame")&&(this._core.speed(0),this._fullscreen=!0),d&&this._fullscreen&&this._playing?!1:this._fullscreen?(this._fullscreen=!1,!1):this._playing&&this._core.state.orientation!==b.orientation?(this._core.state.orientation=b.orientation,!1):!0},d.prototype.destroy=function(){var a,b;this._core.$element.off("click.owl.video");for(a in this._handlers)this._core.$element.off(a,this._handlers[a]);for(b in Object.getOwnPropertyNames(this))"function"!=typeof this[b]&&(this[b]=null)},a.fn.owlCarousel.Constructor.Plugins.Video=d}(window.Zepto||window.jQuery,window,document),function(a,b,c,d){var e=function(b){this.core=b,this.core.options=a.extend({},e.Defaults,this.core.options),this.swapping=!0,this.previous=d,this.next=d,this.handlers={"change.owl.carousel":a.proxy(function(a){"position"==a.property.name&&(this.previous=this.core.current(),this.next=a.property.value)},this),"drag.owl.carousel dragged.owl.carousel translated.owl.carousel":a.proxy(function(a){this.swapping="translated"==a.type},this),"translate.owl.carousel":a.proxy(function(){this.swapping&&(this.core.options.animateOut||this.core.options.animateIn)&&this.swap()},this)},this.core.$element.on(this.handlers)};e.Defaults={animateOut:!1,animateIn:!1},e.prototype.swap=function(){if(1===this.core.settings.items&&this.core.support3d){this.core.speed(0);var b,c=a.proxy(this.clear,this),d=this.core.$stage.children().eq(this.previous),e=this.core.$stage.children().eq(this.next),f=this.core.settings.animateIn,g=this.core.settings.animateOut;this.core.current()!==this.previous&&(g&&(b=this.core.coordinates(this.previous)-this.core.coordinates(this.next),d.css({left:b+"px"}).addClass("animated owl-animated-out").addClass(g).one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",c)),f&&e.addClass("animated owl-animated-in").addClass(f).one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",c))}},e.prototype.clear=function(b){a(b.target).css({left:""}).removeClass("animated owl-animated-out owl-animated-in").removeClass(this.core.settings.animateIn).removeClass(this.core.settings.animateOut),this.core.transitionEnd()},e.prototype.destroy=function(){var a,b;for(a in this.handlers)this.core.$element.off(a,this.handlers[a]);for(b in Object.getOwnPropertyNames(this))"function"!=typeof this[b]&&(this[b]=null)},a.fn.owlCarousel.Constructor.Plugins.Animate=e}(window.Zepto||window.jQuery,window,document),function(a,b,c){var d=function(b){this.core=b,this.core.options=a.extend({},d.Defaults,this.core.options),this.handlers={"translated.owl.carousel refreshed.owl.carousel":a.proxy(function(){this.autoplay()
},this),"play.owl.autoplay":a.proxy(function(a,b,c){this.play(b,c)},this),"stop.owl.autoplay":a.proxy(function(){this.stop()},this),"mouseover.owl.autoplay":a.proxy(function(){this.core.settings.autoplayHoverPause&&this.pause()},this),"mouseleave.owl.autoplay":a.proxy(function(){this.core.settings.autoplayHoverPause&&this.autoplay()},this)},this.core.$element.on(this.handlers)};d.Defaults={autoplay:!1,autoplayTimeout:5e3,autoplayHoverPause:!1,autoplaySpeed:!1},d.prototype.autoplay=function(){this.core.settings.autoplay&&!this.core.state.videoPlay?(b.clearInterval(this.interval),this.interval=b.setInterval(a.proxy(function(){this.play()},this),this.core.settings.autoplayTimeout)):b.clearInterval(this.interval)},d.prototype.play=function(){return c.hidden===!0||this.core.state.isTouch||this.core.state.isScrolling||this.core.state.isSwiping||this.core.state.inMotion?void 0:this.core.settings.autoplay===!1?void b.clearInterval(this.interval):void this.core.next(this.core.settings.autoplaySpeed)},d.prototype.stop=function(){b.clearInterval(this.interval)},d.prototype.pause=function(){b.clearInterval(this.interval)},d.prototype.destroy=function(){var a,c;b.clearInterval(this.interval);for(a in this.handlers)this.core.$element.off(a,this.handlers[a]);for(c in Object.getOwnPropertyNames(this))"function"!=typeof this[c]&&(this[c]=null)},a.fn.owlCarousel.Constructor.Plugins.autoplay=d}(window.Zepto||window.jQuery,window,document),function(a){"use strict";var b=function(c){this._core=c,this._initialized=!1,this._pages=[],this._controls={},this._templates=[],this.$element=this._core.$element,this._overrides={next:this._core.next,prev:this._core.prev,to:this._core.to},this._handlers={"prepared.owl.carousel":a.proxy(function(b){this._core.settings.dotsData&&this._templates.push(a(b.content).find("[data-dot]").andSelf("[data-dot]").attr("data-dot"))},this),"add.owl.carousel":a.proxy(function(b){this._core.settings.dotsData&&this._templates.splice(b.position,0,a(b.content).find("[data-dot]").andSelf("[data-dot]").attr("data-dot"))},this),"remove.owl.carousel prepared.owl.carousel":a.proxy(function(a){this._core.settings.dotsData&&this._templates.splice(a.position,1)},this),"change.owl.carousel":a.proxy(function(a){if("position"==a.property.name&&!this._core.state.revert&&!this._core.settings.loop&&this._core.settings.navRewind){var b=this._core.current(),c=this._core.maximum(),d=this._core.minimum();a.data=a.property.value>c?b>=c?d:c:a.property.value<d?c:a.property.value}},this),"changed.owl.carousel":a.proxy(function(a){"position"==a.property.name&&this.draw()},this),"refreshed.owl.carousel":a.proxy(function(){this._initialized||(this.initialize(),this._initialized=!0),this._core.trigger("refresh",null,"navigation"),this.update(),this.draw(),this._core.trigger("refreshed",null,"navigation")},this)},this._core.options=a.extend({},b.Defaults,this._core.options),this.$element.on(this._handlers)};b.Defaults={nav:!1,navRewind:!0,navText:["prev","next"],navSpeed:!1,navElement:"div",navContainer:!1,navContainerClass:"owl-nav",navClass:["owl-prev","owl-next"],slideBy:1,dotClass:"owl-dot",dotsClass:"owl-dots",dots:!0,dotsEach:!1,dotData:!1,dotsSpeed:!1,dotsContainer:!1,controlsClass:"owl-controls"},b.prototype.initialize=function(){var b,c,d=this._core.settings;d.dotsData||(this._templates=[a("<div>").addClass(d.dotClass).append(a("<span>")).prop("outerHTML")]),d.navContainer&&d.dotsContainer||(this._controls.$container=a("<div>").addClass(d.controlsClass).appendTo(this.$element)),this._controls.$indicators=d.dotsContainer?a(d.dotsContainer):a("<div>").hide().addClass(d.dotsClass).appendTo(this._controls.$container),this._controls.$indicators.on("click","div",a.proxy(function(b){var c=a(b.target).parent().is(this._controls.$indicators)?a(b.target).index():a(b.target).parent().index();b.preventDefault(),this.to(c,d.dotsSpeed)},this)),b=d.navContainer?a(d.navContainer):a("<div>").addClass(d.navContainerClass).prependTo(this._controls.$container),this._controls.$next=a("<"+d.navElement+">"),this._controls.$previous=this._controls.$next.clone(),this._controls.$previous.addClass(d.navClass[0]).html(d.navText[0]).hide().prependTo(b).on("click",a.proxy(function(){this.prev(d.navSpeed)},this)),this._controls.$next.addClass(d.navClass[1]).html(d.navText[1]).hide().appendTo(b).on("click",a.proxy(function(){this.next(d.navSpeed)},this));for(c in this._overrides)this._core[c]=a.proxy(this[c],this)},b.prototype.destroy=function(){var a,b,c,d;for(a in this._handlers)this.$element.off(a,this._handlers[a]);for(b in this._controls)this._controls[b].remove();for(d in this.overides)this._core[d]=this._overrides[d];for(c in Object.getOwnPropertyNames(this))"function"!=typeof this[c]&&(this[c]=null)},b.prototype.update=function(){var a,b,c,d=this._core.settings,e=this._core.clones().length/2,f=e+this._core.items().length,g=d.center||d.autoWidth||d.dotData?1:d.dotsEach||d.items;if("page"!==d.slideBy&&(d.slideBy=Math.min(d.slideBy,d.items)),d.dots||"page"==d.slideBy)for(this._pages=[],a=e,b=0,c=0;f>a;a++)(b>=g||0===b)&&(this._pages.push({start:a-e,end:a-e+g-1}),b=0,++c),b+=this._core.mergers(this._core.relative(a))},b.prototype.draw=function(){var b,c,d="",e=this._core.settings,f=(this._core.$stage.children(),this._core.relative(this._core.current()));if(!e.nav||e.loop||e.navRewind||(this._controls.$previous.toggleClass("disabled",0>=f),this._controls.$next.toggleClass("disabled",f>=this._core.maximum())),this._controls.$previous.toggle(e.nav),this._controls.$next.toggle(e.nav),e.dots){if(b=this._pages.length-this._controls.$indicators.children().length,e.dotData&&0!==b){for(c=0;c<this._controls.$indicators.children().length;c++)d+=this._templates[this._core.relative(c)];this._controls.$indicators.html(d)}else b>0?(d=new Array(b+1).join(this._templates[0]),this._controls.$indicators.append(d)):0>b&&this._controls.$indicators.children().slice(b).remove();this._controls.$indicators.find(".active").removeClass("active"),this._controls.$indicators.children().eq(a.inArray(this.current(),this._pages)).addClass("active")}this._controls.$indicators.toggle(e.dots)},b.prototype.onTrigger=function(b){var c=this._core.settings;b.page={index:a.inArray(this.current(),this._pages),count:this._pages.length,size:c&&(c.center||c.autoWidth||c.dotData?1:c.dotsEach||c.items)}},b.prototype.current=function(){var b=this._core.relative(this._core.current());return a.grep(this._pages,function(a){return a.start<=b&&a.end>=b}).pop()},b.prototype.getPosition=function(b){var c,d,e=this._core.settings;return"page"==e.slideBy?(c=a.inArray(this.current(),this._pages),d=this._pages.length,b?++c:--c,c=this._pages[(c%d+d)%d].start):(c=this._core.relative(this._core.current()),d=this._core.items().length,b?c+=e.slideBy:c-=e.slideBy),c},b.prototype.next=function(b){a.proxy(this._overrides.to,this._core)(this.getPosition(!0),b)},b.prototype.prev=function(b){a.proxy(this._overrides.to,this._core)(this.getPosition(!1),b)},b.prototype.to=function(b,c,d){var e;d?a.proxy(this._overrides.to,this._core)(b,c):(e=this._pages.length,a.proxy(this._overrides.to,this._core)(this._pages[(b%e+e)%e].start,c))},a.fn.owlCarousel.Constructor.Plugins.Navigation=b}(window.Zepto||window.jQuery,window,document),function(a,b){"use strict";var c=function(d){this._core=d,this._hashes={},this.$element=this._core.$element,this._handlers={"initialized.owl.carousel":a.proxy(function(){"URLHash"==this._core.settings.startPosition&&a(b).trigger("hashchange.owl.navigation")},this),"prepared.owl.carousel":a.proxy(function(b){var c=a(b.content).find("[data-hash]").andSelf("[data-hash]").attr("data-hash");this._hashes[c]=b.content},this)},this._core.options=a.extend({},c.Defaults,this._core.options),this.$element.on(this._handlers),a(b).on("hashchange.owl.navigation",a.proxy(function(){var a=b.location.hash.substring(1),c=this._core.$stage.children(),d=this._hashes[a]&&c.index(this._hashes[a])||0;return a?void this._core.to(d,!1,!0):!1},this))};c.Defaults={URLhashListener:!1},c.prototype.destroy=function(){var c,d;a(b).off("hashchange.owl.navigation");for(c in this._handlers)this._core.$element.off(c,this._handlers[c]);for(d in Object.getOwnPropertyNames(this))"function"!=typeof this[d]&&(this[d]=null)},a.fn.owlCarousel.Constructor.Plugins.Hash=c}(window.Zepto||window.jQuery,window,document);
ACC.carousel = {

	_autoload: [

	     "shopTheStyleCarousel",
	     "shopTheStyleHomeCarousel",

	     "StyleEditCarousel",

	     "myFun",

	     "shopTheLookCarousel",

	     "ClpTopDealsCarousel",

	     "ClpBestOffersCarousel",
	     "BlpBestOffersCarousel",

	     "blpTopDealsCarousel",		//add for INC_11189

	     "ClpBestSellerCarousel",

	     "shopByLookCarousel",
	     "offersCarousel",
	     /*"blpTopDealsCarousel",*/		//commented for INC_11189
	     "categoryCarousel",
	     "myStyleCarousel",
	     "heroProductCarousel",
	     "springflingCarousel",
	     "myReviewCarousel",
	     "advancedCategoryCarousel",
	     "pdpProductCarousel",
	       
		["bindCarousel", $(".js-owl-carousel").length >0]
	],

	carouselConfig:{
		"default":{
			navigation:true,
			rewindNav: false,
			navigationText : ["<span class='glyphicon glyphicon-chevron-left'></span>", "<span class='glyphicon glyphicon-chevron-right'></span>"],
			pagination:false,
			itemsCustom : [[0, 1], [640, 2], [1024, 5], [1400, 7]]
		},
		"rotating-image":{
			navigation:false,
			pagination:true,
			singleItem : true,
			rewindNav: false
		},
		"lazy-reference":{
			navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,7], 
			itemsDesktopSmall : [1400,5], 
			itemsTablet: [768,3], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true		
		}
	},

	bindCarousel: function(){
		
		$(".js-owl-carousel").each(function(){
			var $c = $(this)
			$.each(ACC.carousel.carouselConfig,function(key,config){
				if($c.hasClass("js-owl-"+key)){
					var $e = $(".js-owl-"+key);
					$e.owlCarousel(config);
				}
			});
		});

	},
	
	categoryCarousel: function(){
		
		$("#mplCategoryCarousel").owlCarousel({
		
					items:4,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 50,
            			},
            			// breakpoint from 768 up
            			768 : {
            				items:3,
            			},
            			// breakpoint from 768 up
            			1280 : {
            				items:4,
            			}			
            		}	
		
			/*navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true*/
		});
	},
	
	myFun: function(){
		$(".electronic-rotatingImage").owlCarousel({
			items:1,
    		//loop: true,
			loop: $("#rotatingImage img").length == 1 ? false : true,
    		nav:true,
    		dots:false,
    		navText:[]
		});
		/*TPR-268*/
		/*$("#rotatingImageMobile").owlCarousel({
			items:1,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
		});*/
		/*if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
			//alert(timeout);
			$("#rotatingImageTimeout").owlCarousel({
				navigation:false,
				rewindNav: true,
				autoPlay: timeout,
				navigationText : [],
				pagination:true,
				singleItem:true,
				autoHeight : true
			});
		}*/
	 
		//Desktop view
		/*TISSQAEE-403 Start*/
		if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
			var loop = $(".homepage-banner #rotatingImageTimeout img").length > 1 ? true :false;
			var dots = $(".homepage-banner #rotatingImageTimeout img").length > 1 ? true :false; 
			//$(".home-rotatingImage").owlCarousel({
				$("#rotatingImageTimeout").owlCarousel({
				items:1,
				nav:false,
				dots:dots,
				loop: loop,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });
			//UF-291 starts here
			if ($(window).width() >= 720) {	
				$("#rotatingImageTimeout img").each(function() {
				    if ($(this).attr("data-src")) {
						$(this).attr("src",$(this).attr("data-src"));
						$(this).removeAttr("data-src");
					}	
				});
			}	
			//UF-291 ends here
		}
			//Mobile View
			if(typeof homePageBannerTimeout!== "undefined"){
				var timeout = parseInt(homePageBannerTimeout) * 1000 ;
				var loop = $(".homepage-banner #rotatingImageTimeoutMobile img").length > 1 ? true :false;
				var dots = $(".homepage-banner #rotatingImageTimeoutMobile img").length > 1 ? true :false;
				
				$("#rotatingImageTimeoutMobile").owlCarousel({
					
					items:1,
					nav:false,
					dots:dots,
					loop: loop,
			        autoplay: true,
			        autoHeight : true,
			        autoplayTimeout: timeout
			    });
				//UF-291 starts here
				if ($(window).width() < 720) {	
					$("#rotatingImageTimeoutMobile img").each(function() {
					    if ($(this).attr("data-src")) {
							$(this).attr("src",$(this).attr("data-src"));
							$(this).removeAttr("data-src");
						}	
					});
				}
				//UF-291 ends here
			/*TISSQAEE-403 End*/
			/*TPR-268*/
			/*$("#rotatingImageTimeoutMobile").owlCarousel({
				items:1,
				dots:true,
				loop: false,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });*/
		/*	$("#rotatingImageTimeout").append('<div class="hbpagination"></div>');
			var bannerLength = $('#rotatingImageTimeout .owl-item').length;
			for (var i = 0 ; i<bannerLength; i++ ) {
				$("#rotatingImageTimeout .hbpagination").append('<span class="hb-page"></span>');
			}
			$("#rotatingImageTimeout .hb-page").first().addClass('active');*/
			
			/*$(document).on("click",".hb-page",function(){
				var req_pos = $(this).index(),cur_pos = $('.hb-page.active').index() ;
				
				if(req_pos < cur_pos) {
						ACC.carousel.homePageBannerCarousel(bannerLength - (cur_pos - req_pos));
				} else if (req_pos > cur_pos) {
						ACC.carousel.homePageBannerCarousel(req_pos - cur_pos);
				}
				$('.hb-page').removeClass('active');
				$(this).addClass('active');
			});*/
			
		/*	setInterval(function(){
				var ind = $('.hb-page.active').index();
				if(ind == bannerLength-1) {
					$('.hb-page').removeClass('active');
					$('.hb-page').eq(0).addClass('active');
				} else {
					$('.hb-page').removeClass('active');
					$('.hb-page').eq(ind+1).addClass('active');
				}
				ACC.carousel.homePageBannerCarousel(1);
			},timeout);*/
		}
	},
	
	
	StyleEditCarousel: function(){
		if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
		}
		else{
			var timeout = 0 ;
		}
			$(".style_edit .home-rotatingImage").owlCarousel({
				items:1,
				nav:false,
				dots:($(".style_edit .home-rotatingImage img").length == 1)?false:true,
				loop: ($(".style_edit .home-rotatingImage img").length == 1)?false:true,
		        autoplay: true,
		        autoHeight : true,
		        autoplayTimeout: timeout
		    });
			$(".style_edit .electronic-rotatingImage").owlCarousel({
				items:1,
				nav:false,
				dots:($(".style_edit .electronic-rotatingImage img").length == 1)?false:true,
				loop: ($(".style_edit .electronic-rotatingImage img").length == 1)?false:true,
				autoplay: true,
				autoHeight : true,
				autoplayTimeout: timeout
			});
		       // TISPRDT-1159
		       $(".style_edit_blp .home-rotatingImage").owlCarousel({
				items:1,
				nav:false,
				dots:($(".style_edit_blp .home-rotatingImage img").length == 1)?false:true,
				loop: ($(".style_edit_blp .home-rotatingImage img").length == 1)?false:true,
				autoplay: true,
				autoHeight : true,
				autoplayTimeout: timeout
			});
		
	},
	
	/*homePageBannerCarousel: function(count){
		
			var iw = $('#rotatingImageTimeout .owl-item').outerWidth(), ih = $("#rotatingImageTimeout .owl-item").first().next().find('.hero').height();
			$("#rotatingImageTimeout .owl-wrapper").css('transition','all 0.5s linear');
			$("#rotatingImageTimeout .owl-wrapper").css('transform','translate3d(-'+iw+'px, 0px, 0px)');
			$("#rotatingImageTimeout .owl-wrapper-outer").css('height',ih);
			setTimeout(function(){
			$("#rotatingImageTimeout .owl-wrapper").append($("#rotatingImageTimeout .owl-item").first());
			$("#rotatingImageTimeout .owl-wrapper").css('transition','all 0s linear');
			$("#rotatingImageTimeout .owl-wrapper").css('transform','translate3d(0px, 0px, 0px)');
			count--;
			if(count > 0) {
				ACC.carousel.homePageBannerCarousel(count);
			}
		
		},500);
	},*/
	
	shopByLookCarousel: function(){
		$(".shopByLookCarousel").owlCarousel({
			items:2,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false*/
		});
	},

	/*----------TPR-179(Shop The Style Start)------------*/
	shopTheStyleCarousel: function(){
		$("body.page-shopTheStyle .shopByLookCarousel").owlCarousel({
			items:3,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false*/
			responsive : {
						// breakpoint from 0 up
            			0 : {
            				items:2,
            			},
            			// breakpoint from 785 up
            			785 : {
            				items:3,
            			}		
            		}
			
		});
	},
	shopTheStyleHomeCarousel: function(){
		if(typeof homePageBannerTimeout!== "undefined"){
			var timeout = parseInt(homePageBannerTimeout) * 1000 ;
		}
		else{
			var timeout = 5000 ;
		}
	//var timeout = parseInt(homePageBannerTimeout) * 1000 ;
	var owl1 = $("body.page-shopTheStyle .home-rotatingImage#rotatingImageTimeout"); 
	owl1.owlCarousel({
			items:1,
			nav:false,
			dots:true,
			loop: true,
	        autoplay: true,
	        autoHeight : true,
	        autoplayTimeout: timeout
	    });
	var owl2 = $("body.page-shopTheStyle .home-rotatingImage#rotatingImageTimeoutMobile"); 
	owl2.owlCarousel({
		items:1,
		nav:false,
		dots:true,
		loop: true,
        autoplay: true,
        autoHeight : true,
        autoplayTimeout: timeout
    });
	    $('.owl-dot1 a').click(function(e){
	    				
			e.preventDefault();	
			$('.owl-controls li').removeClass('active');		
			owl1.trigger('to.owl.carousel',  0);
			owl2.trigger('to.owl.carousel',  0);
			//console.log("1");
			$(this).parent().parent().addClass('active');
		}); 
		 $('.owl-dot2 a').click(function(e){		
			e.preventDefault();	
			$('.owl-controls li').removeClass('active');		
			owl1.trigger('to.owl.carousel',  1);
			owl2.trigger('to.owl.carousel',  1);
			//console.log("2");	
			$(this).parent().parent().addClass('active');
		}); 
		 $('.owl-dot3 a').click(function(e){		
			e.preventDefault();
			$('.owl-controls li').removeClass('active');				
			owl1.trigger('to.owl.carousel',  2);
			owl2.trigger('to.owl.carousel',  2);
			//console.log("3");	
			$(this).parent().parent().addClass('active');
		}); 
		 $('.owl-dot4 a').click(function(e){	
			e.preventDefault();	
			$('.owl-controls li').removeClass('active');		
			owl1.trigger('to.owl.carousel',  3);
			owl2.trigger('to.owl.carousel',  3);
			//console.log("4");
			$(this).parent().parent().addClass('active');
		});
		 
		/*var owl2 = $("body.page-shopTheStyle .home-rotatingImage#rotatingImageTimeoutMobile");*/ 
    },
	 /*----------End TPR-179(Shop The Style)------------*/  

	ClpBestSellerCarousel: function(){
		$(".best_seller_section .shopByLookCarousel").owlCarousel({
			items:5,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
    		responsive: {
                0: {
                    items: 1,
    				stagePadding: 50
                },
                480: {
                    items: 2,
    				stagePadding: 75
                },
                700: {
                    items: 3
                },
                1000: {
                    items: 5
                }
            }
		});
	},
	/*sprint8(TPR-1672 CLP)*/
	offersCarousel: function(){
		$(".offersCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:1,
            				stagePadding: 50,
            			},
            			// breakpoint from 768 up
            			768 : {
            				items:3,
            			},
            			// breakpoint from 768 up
            			1280 : {
            				items:5,
            			}			
            		}
		/*sprint8(TPR-1672 CLP)*/
		
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	
	shopTheLookCarousel: function(){
		$(".shop_the_look .shopByLookCarousel").owlCarousel({
			/*items:2,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[]
		    responsive : {
			// breakpoint from 0 up
			0 : {
				items:1,
				stagePadding: 50,
			},
			// breakpoint from 480 up
			480 : {
				items:2,
				stagePadding: 75,
			},
			// breakpoint from 768 up
			700 : {
				items:3,
			},
			// breakpoint from 768 up
			1000 : {
				items:5,
			}			
		}*/
			items:3,
			nav: true,
	        navText: [],
	        loop: true,
	        responsive: {
	            0: {
	                items: 1,
					stagePadding: 75
	            },
	            480: {
	                items: 2,
					stagePadding: 75
	            },
	            768: {
	                items: 2
	            },
	            1280: {
	                items: 3
	            }
	        }
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:2,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : false*/
		});
	},

	/* start change for INC_11189*/
	/*blpTopDealsCarousel: function(){
		$(".top_deal_blp #mplAdvancedCategoryCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length == 1)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 2)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 3)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 768 up
            			1000 : {
            				items:5,
            				loop: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 5)?false:true,
                    		nav: ($(".top_deal_blp #mplAdvancedCategoryCarousel .image").length <= 5)?false:true,
            			}			
            		}	
			navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],
			});
	},*/
	blpTopDealsCarousel: function(){
		//changes for tpr-599(BLP and CLP changes)
		var slideBy= $('#slideBy').val()?$('#slideBy').val():1;
		var autoplayTimeout= $('#autoplayTimeout').val()?$('#autoplayTimeout').val():5000;
		var autoPlay= $('#autoPlay').val()?$.parseJSON($('#autoPlay').val()):true;
		$(".top_deal_blp .offersCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".top_deal_blp .offersCarousel .image").length == 1)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".top_deal_blp .offersCarousel .image").length <= 2)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".top_deal_blp .offersCarousel .image").length <= 3)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 768 up
            			1000 : {
            				items:5,
            				loop: ($(".top_deal_blp .offersCarousel .image").length <= 5)?false:true,
                    		nav: ($(".top_deal_blp .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}	
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	/* end change for INC_11189*/	

	ClpTopDealsCarousel: function(){
		
		//changes for tpr-599(BLP and CLP changes)
		var slideBy= $('.top_deal #slideByOffer').val()?$('.top_deal #slideByOffer').val():1;
		var autoPlay= $('.top_deal #autoPlayOffer').val()?$.parseJSON($('.top_deal #autoPlayOffer').val()):true;
		var autoplayTimeout= $('.top_deal #autoplayTimeoutOffer').val()?$('.top_deal #autoplayTimeoutOffer').val():5000;
		
		$(".top_deal .offersCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".top_deal .offersCarousel .image").length == 1)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".top_deal .offersCarousel .image").length <= 2)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".top_deal .offersCarousel .image").length <= 3)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 1000 up
            			1000 : {
            				items:5,
            				loop: ($(".top_deal .offersCarousel .image").length <= 5)?false:true,
            				nav: ($(".top_deal .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	/*sprint8 TPR-1672*/
	ClpBestOffersCarousel: function(){
		
		//changes for tpr-599(BLP and CLP changes)
		var slideBy= $('#slideByOffer').val()?$('#slideByOffer').val():1;
		var autoPlay= $('#autoPlayOffer').val()?$.parseJSON($('#autoPlayOffer').val()):true;
		var autoplayTimeout= $('#autoplayTimeoutOffer').val()?$('#autoplayTimeoutOffer').val():5000;
		
		$(".best-offers .offersCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".best-offers .offersCarousel .image").length == 1)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".best-offers .offersCarousel .image").length <= 2)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".best-offers .offersCarousel .image").length <= 3)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 1000 up
            			1000 : {
            				items:5,
            				loop: ($(".best-offers .offersCarousel .image").length <= 5)?false:true,
            				nav: ($(".best-offers .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	
BlpBestOffersCarousel: function(){
		
		//changes for tpr-599(BLP and CLP changes)
		var slideBy= $('#slideByOffer').val()?$('#slideByOffer').val():1;
		var autoPlay= $('#autoPlayOffer').val()?$.parseJSON($('#autoPlayOffer').val()):true;
		var autoplayTimeout= $('#autoplayTimeoutOffer').val()?$('#autoplayTimeoutOffer').val():5000;
		
		$(".best-offers_blp .offersCarousel").owlCarousel({
					items:5,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		autoplay: autoPlay,
                    autoHeight : false,
                    autoplayTimeout: autoplayTimeout,
                    slideBy: slideBy,
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            				loop: ($(".best-offers_blp .offersCarousel .image").length == 1)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length == 1)?false:true,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 75,
            				loop: ($(".best-offers_blp .offersCarousel .image").length <= 2)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length <= 2)?false:true,
            			},
            			// breakpoint from 700 up
            			700 : {
            				items:3,
            				loop: ($(".best-offers_blp .offersCarousel .image").length <= 3)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length <= 3)?false:true,
            			},
            			// breakpoint from 1000 up
            			1000 : {
            				items:5,
            				loop: ($(".best-offers_blp .offersCarousel .image").length <= 5)?false:true,
            				nav: ($(".best-offers_blp .offersCarousel .image").length <= 5)?false:true,
            			}			
            		}
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:4,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: false, 
			itemsMobile : true
		itemsDesktop : [5000,4], 
		itemsDesktopSmall : [1400,4], 
		itemsTablet: [650,2], 
		itemsMobile : [480,2],*/
			});
	},
	
	/*sprint8 TPR-1672*/

	myStyleCarousel: function(){
		$(".mystyle-carousel").owlCarousel({
			items:5,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
    		responsive : {
    			// breakpoint from 0 up
    			0 : {
    				items:3,
    			},			
    			// breakpoint from 807 up
    			807 : {
    				items:5,
    			}			
    		}	
			
			/*navigation:true,
			rewindNav: false,
			navigationText :[],
			pagination:false,
			items:5,
			itemsDesktop : false, 
			itemsDesktopSmall : false, 
			itemsTablet: [807,3],
			itemsMobile : false*/
		});
		
	},
	
	heroProductCarousel: function() {
		$(".product-listing.product-grid.hero_carousel").owlCarousel({
					items:4,
            		loop: true,
            		nav:true,
            		dots:false,
            		navText:[],
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 50,
            			},
            			// breakpoint from 768 up
            			768 : {
            				items:3,
            			},
            			// breakpoint from 768 up
            			1280 : {
            				items:4,
            			}			
            		}	
			/*navigation:true,
			rewindNav: false,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			lazyLoad:true*/
		});
	},
	
	springflingCarousel: function() {
		$("div.shop-sale.wrapper #defaultNowTrending").owlCarousel({
			items:6,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
    		responsive : {
    			// breakpoint from 0 up
    			0 : {
    				items:1,
    				stagePadding: 50,
    			},
    			// breakpoint from 480 up
    			480 : {
    				items:2,
    				stagePadding: 50,
    			},
    			// breakpoint from 768 up
    			768 : {
    				items:3,
    			},
    			// breakpoint from 768 up
    			1280 : {
    				items:6,
    			}			
    		}	
			/*navigation:true,
			rewindNav: false,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,6], 
			itemsDesktopSmall : [1400,6], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2]*/
		});
	},
	
	advancedCategoryCarousel: function(){
		//setTimeout(function() {
			console.log("inside timeout");
		$("#mplAdvancedCategoryCarousel").owlCarousel({
					items:4,
            		loop: true,
            		nav:false,
            		dots:false,
            		navText:[],
            		responsive : {
            			// breakpoint from 0 up
            			0 : {
            				items:1,
            				stagePadding: 50,
            			},
            			// breakpoint from 480 up
            			480 : {
            				items:2,
            				stagePadding: 50,
            			},
            			// breakpoint from 768 up
            			768 : {
            				items:3,
            			},
            			// breakpoint from 768 up
            			1280 : {
            				items:4,
            			}			
            		}	
		/*navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,4], 
			itemsDesktopSmall : [1400,4], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			lazyLoad:true*/
		});
		//},7000);
		console.log("outside timeout");
	},
	
	myReviewCarousel: function(){
		$("#my-review-carousel").on('initialize.owl.carousel initialized.owl.carousel', function() {
			$("#my-review-carousel").show();
                });
		$("#my-review-carousel").owlCarousel({
			items:5,
    		loop: true,
    		nav:true,
    		dots:false,
    		navText:[],
    		responsive : {
    			// breakpoint from 0 up
    			0 : {
    				items:1,
    				stagePadding: 50,
    			},
    			// breakpoint from 480 up
    			480 : {
    				items:2,
    				stagePadding: 50,
    			},
    			// breakpoint from 768 up
    			768 : {
    				items:3,
    			},
    			// breakpoint from 768 up
    			1280 : {
    				items:5,
    			}			
    		}	
			/*navigation:true,
			navigationText : [],
			pagination:false,
			itemsDesktop : [5000,5], 
			itemsDesktopSmall : [1400,5], 
			itemsTablet: [650,2], 
			itemsMobile : [480,2], 
			rewindNav: false,
			afterInit: function() {$("#my-review-carousel").show();}*/
		});
	},
	
	pdpProductCarousel: function(){
		$("#pdpProductCarousel").owlCarousel({
			items:1,

    		loop: $("#pdpProductCarousel img").length == 1 ? false : true,
    		navText:[],
    		responsive : {
    			// breakpoint from 0 up
    			0 : {
    				nav:false,
    	    		dots:true,
    			},
    			// breakpoint from 768 up
    			768 : {
    				nav:true,
    	    		dots:false,
    			}			
    		}	
		});
		$(".product-image-container.device .owl-stage-outer").prepend($(".product-image-container.device .wishlist-icon"))
	},
	
	/*shopBannerCarousel: function(){
	  $("#shopstyleCarousel").owlCarousel({
	 
	      autoPlay: 3000, //Set AutoPlay to 3 seconds
	 
	      items : 1,
	      loop: false
	 
	  });
	}*/
	
	/*New Homepage change*/
	/*timeoutCarousel: function(){
		var timeout = parseInt(homePageBannerTimeout) * 1000 ;
		//alert(timeout);
		$("#rotatingImageTimeout").owlCarousel({
			navigation:false,
			rewindNav: true,
			autoPlay: timeout,
			navigationText : [],
			pagination:true,
			singleItem:true
		});
	}*/
	
	

};
//################################################################
//#### Autoload
//################################################################
// 
// ACC.sample={
// 	_autoload: [
// 		"samplefunction",
// 		["somefunction", "some expression to test"]
// 		["somefunction","some expression to test","elsefunction"]
// 	],

// 	samplefunction:function(){
// 		//... do some suff here, executed every time ...
// 	},

// 	somefunction:function(){
// 		//... do some suff here. if expression match ...
// 	},

// 	elsefunction:function(){
// 		//... do some suff here. if expression NOT match ...
// 	}

// }

//  // sample expression: $(".js-storefinder-map").length != 0


function _autoload(){
	$.each(ACC,function(section,obj){
		if($.isArray(obj._autoload)){
			$.each(obj._autoload,function(key,value){
				if($.isArray(value)){
					if(value[1]){
						ACC[section][value[0]]();
					}else{
						if(value[2]){
							ACC[section][value[2]]()
						}
					}
				}else{
					ACC[section][value]();
				}
			})
		}
	})
}

$(function(){
	_autoload();
});
var headerLoggedinStatus = false;
var csrfDataChanged = false;
$(function() {
    $.ajax({
       url: ACC.config.encodedContextPath + "/fetchToken",
       type: 'GET',
       async:false,
       cache:false,
       success: function(data) {
           $("input[name='CSRFToken']").each(function() {
               this.value = data.token;
           });
           ACC.config.CSRFToken = data.token;
           ACC.config.SessionId = data.sessionId;
           ACC.config.VisitorIp = data.vistiorIp;
           
           var crsfSession = window.sessionStorage.getItem("csrf-token");
           if(window.sessionStorage && (null == crsfSession || crsfSession != data.token)){
          	 csrfDataChanged = true;
          	 window.sessionStorage.setItem("csrf-token",data.token);
           }
           //TISPRD-3357
           callSetHeader();
       }
   });
});
function callSetHeader() {
	//TISPRO-522 IE Issue Fix
	
	var header = window.sessionStorage.getItem("header");
	var userCookChanged = userCookieChanged();
	if(forceSetHeader() || null == header || userCookChanged == true || csrfDataChanged){
		$.ajax({
	        url: ACC.config.encodedContextPath + "/setheader?timestamp="+Date.now(),
	        type: 'GET',
	        cache:false,
	        success: function(data) {
	        	window.sessionStorage.setItem("header" , JSON.stringify(data));
	        	setHeader(data);
	        }
	    });
	}else{
		 header = JSON.parse(header);
		 setHeader(header);
	 	}
	}
 
$(document).on("mouseover touchend", "div.departmenthover", function() {
    var id = this.id;
    //var code = id.substring(4);

    if (!$.cookie("dept-list") && window.localStorage) {
        for (var key in localStorage) {
            if (key.indexOf("deptmenuhtml") >= 0) {
                window.localStorage.removeItem(key);
                // console.log("Deleting.." + key);

            }
        }
    }

//	    if (window.localStorage && (html = window.localStorage.getItem("deptmenuhtml-" + code)) && html != "") {
//	        // console.log("Local");
//	        $("ul." + id).html(decodeURI(html));
//	        $('header .content .container > .right ul li #mobile-menu-toggle + ul li ul.words li.long div').removeClass('toggle');
//			$('header .content .container > .right ul li #mobile-menu-toggle + ul li ul li.short div').removeClass('toggle');
//	        //LazyLoad();
//	    } else {
//	        $.ajax({
//	            url: ACC.config.encodedContextPath +
//	                "/departmentCollection",
//	            type: 'GET',
//	            data: "department=" + code,
//	            success: function(html) {
//	                // console.log("Server");
//	                $("ul." + id).html(html);
//	                if (window.localStorage) {
//	                	//TISPRD-8265-- Local Caching reduced to 10hrs from 1day
//	                	var date = new Date();
//	                	var minutes = 600;
//	                	date.setTime(date.getTime() + (minutes * 60 * 1000)); 
//	                	
//	                    $.cookie("dept-list", "true", {
//	                        expires: date,
//	                        path: "/"
//
//	                    });
//	                    window.localStorage.setItem(
//	                        "deptmenuhtml-" + code,
//	                        encodeURI(html));
//	                }
//	            },
//	            complete: function(){
//		        	$('header .content .container > .right ul li #mobile-menu-toggle + ul li ul.words li.long div').removeClass('toggle');
//		    		$('header .content .container > .right ul li #mobile-menu-toggle + ul li ul li.short div').removeClass('toggle');      	
//		        } 
//	        });
//	    } 
});


//$(".A-ZBrands").on("mouseover touchend", function(e) {
	$(document).on("mouseover touchend", ".A-ZBrands", function(e) {
	var componentUid = $("#componentUid").val();
    if ($("li#atozbrandsdiplay").length) {
        // console.log("Dipslaying A-Z Brands..");

        if (!$.cookie("dept-list") && window.localStorage) {
            for (var key in localStorage) {
                if (key.indexOf("atozbrandmenuhtml") >= 0) {
                    window.localStorage.removeItem(key);
                    // console.log("Deleting.." + key);

                }
            }
        }
        if (window.localStorage && (html = window.localStorage.getItem("atozbrandmenuhtml")) && html != "") {
            // console.log("Local");
            if ($("div#appendedAtoZBrands") == null || $(
                "div#appendedAtoZBrands").length == 0) {
                $("li#atozbrandsdiplay").append(decodeURI(html));
            }
        } else {
            // console.log("Server");

            $.ajax({
                url: ACC.config.encodedContextPath +
                    "/atozbrands",
                type: 'GET',
                data : {
					 "componentUid" : componentUid
					},
                success: function(html) {
                    //console.log(html)
                    if ($("div#appendedAtoZBrands") == null ||
                        $("div#appendedAtoZBrands").length ==
                        0) {
                        $("li#atozbrandsdiplay").append(
                            html);
                    }
                    if (window.localStorage) {
                        $.cookie("dept-list", "true", {
                            expires: 1,
                            path: "/"

                        });
                        window.localStorage.setItem(
                            "atozbrandmenuhtml",
                            encodeURI(html));
                    }
                }
            });
        }
    }
});

//$("span.helpmeshopbanner").on("click touchend", function() {
//
//	$.ajax({
//		url : ACC.config.encodedContextPath + "/helpmeshop",
//		type : 'GET',
//		success : function(html) {
//			$("div#latestOffersContent").html(html);
//		}
//	});
//});


$("span.latestOffersBanner").on("click touchend", function() {
	/*TPR-644 START*/
	utag.link(
		{
			link_obj: this, 
			link_text: 'concierge_view_details' , 
			event_type : 'concierge_view_details' 
		});
	/*TPR-644 END*/
	$.ajax({
		url : ACC.config.encodedContextPath + "/listOffers",
		type : 'GET',
		success : function(html) {
			$("div#latestOffersContent").html(html);
		},
		
		complete : function() {
			$(".topConcierge").owlCarousel({
				items:5,
	    		loop: true,
	    		nav:true,
	    		dots:false,
	    		navText:[],
	    		slideBy:'page',
	    		responsive : {
	    			// breakpoint from 0 up
        			0 : {
        				items:1,
        				stagePadding: 50,
        			},
        			// breakpoint from 480 up
        			480 : {
        				items:2,
        				stagePadding: 50,
        			},
        			// breakpoint from 768 up
        			768 : {
        				items:3,
        			},
        			// breakpoint from 768 up
        			1280 : {
        				items:5,
        			}			
	    		}	
				/*navigation:true,
				navigationText : [],
				pagination:false,
				itemsDesktop : [5000,5], 
				itemsDesktopSmall : [1400,5], 
				itemsTablet: [650,1], 
				itemsMobile : [480,1], 
				rewindNav: false,
				scrollPerPage:true*/
			});
			
			var tcitemLength = $(".topConcierge .owl-item").length,tcitemWidth = $(".topConcierge .owl-item").outerWidth() ;
			if (tcitemLength < 5) {
				$(".topConcierge .owl-wrapper").css({
				"width":tcitemLength*tcitemWidth,
				"margin" : "auto"
				});
			}
			
			
		}, 
	});
});


$(document).on('click',"div#closeConceirge",function(e) {
	$(this).parents('.banner').removeClass('active');
});

var trackLinkHover;
$("a#tracklink").on("mouseover touchend", function(e) {
    e.stopPropagation();
    trackLinkHover = setTimeout(function(){
    	$.ajax({
            url: ACC.config.encodedContextPath +
                "/headerTrackOrder",
            type: 'GET',
            cache:false,
            success: function(html) {
                $("ul.trackorder-dropdown").html(html);
            }
        });
    },300);
});
$("li.track.trackOrder").on("mouseleave", function() {
	clearTimeout(trackLinkHover);
});


var wishlistHover;
$("a#myWishlistHeader").on("mouseover touchend", function(e) {
    e.stopPropagation();
    wishlistHover = setTimeout(function(){
    	$.ajax({
            url: ACC.config.encodedContextPath + "/headerWishlist",
            type: 'GET',
            //data: "&productCount=" + $(this).attr("data-count"),
            data: "&productCount=" + $('li.wishlist').find('a').attr("data-count"),
            success: function(html) {
                $("div.wishlist-info").html(html);
                /*TPR-844*/
                var wlCode = [];
				$(".wlCode").each(function(){
					wlCode.push($(this).text().trim());
				});
				$(".plpWlcode").each(function(){
					var productURL = $(this).text(), n = productURL.lastIndexOf("-"), productCode=productURL.substring(n+1, productURL.length);
					//wlPlpCode.push(productCode.toUpperCase());
					
					for(var i = 0; i < wlCode.length; i++) {
						if(productCode.toUpperCase() == wlCode[i]) {
							console.log("Controle Inside");
							$(this).siblings(".plp-wishlist").addClass("added");
						}
					}
				});
				/*TPR-844*/
            }
        });
    },300);
    
});
$("li.wishlist").on("mouseleave", function() {
	clearTimeout(wishlistHover);
});
//TISPRO-522-IE Issue Fix
var loginHover;
$("li.ajaxloginhi").on("mouseover touchend", function(e) {
    e.stopPropagation();
    loginHover = setTimeout(function(){
    	if ($("ul.ajaxflyout").html().trim().length <= 0) {
            $.ajax({
                url: ACC.config.encodedContextPath +
                    "/headerloginhi?timestamp="+Date.now(),
                type: 'GET',
                success: function(html) {
                    $("ul.ajaxflyout").html(html);
                }
            });
        }
    },300);
});

$("li.logIn-hi").on("mouseleave", function(e) {
	clearTimeout(loginHover);
});
//

/*$(window).on("resize load", function() {
    activePos = ($(window).width() > 767) ? 3 : 1;
    console.log("activePosload"+activePos);
});*/

function getBrandsYouLoveAjaxCall() {
        var env = $("#previewVersion").val();
        var count= 0;
        var autoplayTimeout=5000;
        var slideBy=1;
        var autoPlay=true;
        
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath + "/getBrandsYouLove",
            data: dataString,
            success: function(response) {
            	//changes for TPR-1121
            	autoplayTimeout = response.autoplayTimeout?response.autoplayTimeout:autoplayTimeout;
            	slideBy = response.slideBy?response.slideBy:slideBy; 
                autoPlay= response.autoPlay != null ?response.autoPlay:autoPlay;

                //TPR-559 Show/Hide Components and Sub-components
	            if (response.hasOwnProperty("title") && response.hasOwnProperty("subComponents") && response.subComponents.length) {
                //console.log(response.subComponents);
                defaultComponentId = "";
                renderHtml = "<h2>" + response.title + "</h2>" +
                    "<div class='home-brands-you-love-carousel'>";
                $.each(response.subComponents, function(k, v) {
                    //console.log(v.brandLogoUrl);
                	
                    if (!v.showByDefault) {
                        renderHtml +=
                            "<div class='home-brands-you-love-carousel-brands item' data-count ="+ count +" id='" +
                            v.compId + "'><img src='" + v.brandLogoUrl +
                            "' alt='"+v.brandLogoAltText+"'></img></div>";

                    } else {
                        renderHtml +=
                            "<div class='home-brands-you-love-carousel-brands item' data-count ="+ count +" id='" +
                            v.compId + "'><img src='" + v.brandLogoUrl +
                            "' alt='"+v.brandLogoAltText+"'></img></div>";

                        defaultComponentId = v.compId;
                    }
                    count++;
                });
                renderHtml += "</div>";
                $('#brandsYouLove').html(renderHtml);
                getBrandsYouLoveContentAjaxCall(defaultComponentId);
	            }
            },
            error: function() {
                // globalErrorPopup('Failure!!!');
                console.log("Error while getting brands you love");
            },
            complete: function() {

            	//TPR-559 Show/Hide Components and Sub-components
            	if ($(".home-brands-you-love-carousel").length) {
	                $(".home-brands-you-love-carousel").owlCarousel({
	                	items:7,
	            		loop: true,
	            		nav:true,
	            		center:true,
	            		dots:false,
	            		navText:[],
	            		autoplay: autoPlay,
			            autoHeight : false,
	            		autoplayTimeout: autoplayTimeout,
	  	               slideBy: slideBy,
	            		responsive : {
	            			// breakpoint from 0 up
	            			0 : {
	            				items:1,
	            				stagePadding: 75,
	            			},
	            			// breakpoint from 480 up
	            			480 : {
	            				items:3,
	            				stagePadding: 50,
	            			},
	            			// breakpoint from 768 up
	            			768 : {
	            				items:3,
	            				stagePadding: 90,
	            			},
	            			// breakpoint from 768 up
	            			1080 : {
	            				items:7,
	            			}			
	            		}		
	                });
						var bulId = $(".home-brands-you-love-carousel .owl-item.active.center").find(".home-brands-you-love-carousel-brands").attr("id");
	                	getBrandsYouLoveContentAjaxCall(bulId);
	                /*	$(document).on('click', '.home-brands-you-love-carousel .owl-item.active', function () {
	                		
	                		$(".home-brands-you-love-carousel").trigger('to.owl.carousel', [$(this).find(".home-brands-you-love-carousel-brands").attr("data-count"), 500, true]);
	
	                	});*/
	                $(".home-brands-you-love-carousel").on('changed.owl.carousel', function(event) {
	                	setTimeout(function(){
	                	//	console.log($(".home-brands-you-love-carousel .owl-item.active.center").index());
	                		var bulId = $(".home-brands-you-love-carousel .owl-item.active.center").find(".home-brands-you-love-carousel-brands").attr("id");
	                		getBrandsYouLoveContentAjaxCall(bulId);
	                	},80);
	                	
	                });
	               /* $(".home-brands-you-love-carousel").on('changed.owl.carousel', function(event) {
	                    setTimeout(function(){
	                    	console.log("activePos"+activePos);
	                    	$(".home-brands-you-love-carousel .owl-item.active").eq(activePos).find(".home-brands-you-love-carousel-brands").click();
	                    },80)
	                })
	                
	                var index = $(
	                    ".home-brands-you-love-carousel-brands.active"
	                ).parents('.owl-item').index()
	                console.log("index"+index);
	                if (index > activePos) {
	                    for (var i = 0; i < index - activePos; i++) {
	                        $(
	                            ".home-brands-you-love-carousel .owl-wrapper"
	                        ).append($(
	                            ".home-brands-you-love-carousel .owl-item"
	                        ).first());
	                    }
	                } else if (index < activePos) {
	                    for (var i = 0; i < activePos - index; i++) {
	                        $(
	                            ".home-brands-you-love-carousel .owl-wrapper"
	                        ).prepend($(
	                            ".home-brands-you-love-carousel .owl-item"
	                        ).last());
	                    }
	                }*/
            	} 

            }
        });
 
}
    // Get Brands You Love Content AJAX

function getBrandsYouLoveContentAjaxCall(id) {
	
        if (window.localStorage && (html = window.localStorage.getItem(
            "brandContent-" + id)) && html != "") {
            // console.log("Local");
            $(".home-brands-you-love-carousel").css("margin-bottom", "33px");  /*UF-249*/
            //$('#brandsYouLove').append(defaultHtml);
            $('.home-brands-you-love-desc').remove();
            $('#brandsYouLove').append(decodeURI(html));
            //LazyLoad();
        } else {
            $.ajax({
                type: "GET",
                dataType: "json",
                beforeSend: function() {
                	var staticHost=$('#staticHost').val();
                    /*$(".home-brands-you-love-carousel").css(
                        "margin-bottom", "120px");
                    $("#brandsYouLove").append(
                        "<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 200px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='"+staticHost+"/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>"
                    );*/
                },
                url: ACC.config.encodedContextPath +
                    "/getBrandsYouLoveContent",
                data: {
                    "id": id
                },
                success: function(response) {
                    $('.home-brands-you-love-desc').remove();
                    defaultHtml =
                        "<div class='home-brands-you-love-desc'>";
                    if (typeof response.text !== "undefined") {
                        defaultHtml += response.text;
                    } /* UF-249*/
                    if (typeof response.firstProductImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='home-brands-you-love-side-image left'><a href='" +
                            ACC.config.encodedContextPath +
                            response.firstProductUrl +
                            "'><img src='" + response.firstProductImageUrl +
                            "'></img>";
                        if (typeof response.firstProductTitle !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='product-name'>" +
                                response.firstProductTitle + "</p>";
                        }
                        if (typeof response.firstProductPrice !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='price normal'><span class='priceFormat'>" +
                                response.firstProductPrice +
                                "</span></p>";
                        }
                        defaultHtml += "</a></div>"
                    }
                    /*defaultHtml +=
                        "<div class='home-brands-you-love-main-image'>";
                    if (typeof response.bannerImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='home-brands-you-love-main-image-wrapper'>";
                        if (typeof response.bannerText !==
                            "undefined") {
                            defaultHtml +=
                                "<div class='visit-store-wrapper'>" +
                                response.bannerText + "</div>";
                        }
                        defaultHtml += "<img src='" + response.bannerImageUrl +
                            "'></img></div></div>";
                    }*/
                    /******* changes for INC_11934 ***/
                    defaultHtml +=
                        "<div class='home-brands-you-love-main-image'>";
                    if (typeof response.bannerImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='home-brands-you-love-main-image-wrapper'>";
                        /*if (typeof response.bannerText !==
                            "undefined") {
                            defaultHtml +=
                                "<div class='visit-store-wrapper'>" +
                                response.bannerText + "</div>";
                        }*/  /*UF-249*/
                        if (typeof response.bannerUrl !== "undefined") {
                        	 defaultHtml += "<a href='"+response.bannerUrl+"'><img src='" + response.bannerImageUrl +
                             "'></img></a></div>";
                        } else {
                        	 defaultHtml += "<img src='" + response.bannerImageUrl +
                             "'></img></div>";
                        }
                        /* UF-249 start*/
                        if (typeof response.text !== "undefined") {
                            defaultHtml += response.text;
                        }
                        if (typeof response.bannerText !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='visit-store-wrapper'>" +
                            response.bannerText + "</div>";
                    }
                        /* UF-249 end*/
                    }
                    defaultHtml += '</div>';
                    
                    if (typeof response.secondproductImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            "<div class='home-brands-you-love-side-image right'><a href='" +
                            ACC.config.encodedContextPath +
                            response.secondProductUrl +
                            "'><img src='" + response.secondproductImageUrl +
                            "'></img>";
                        if (typeof response.secondProductTitle !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='product-name'>" +
                                response.secondProductTitle +
                                "</p>";
                        }
                        if (typeof response.secondProductPrice !==
                            "undefined") {
                            defaultHtml +=
                                "<p class='normal price'><span class='priceFormat'>" +
                                response.secondProductPrice +
                                "</span></p>";
                        }
                        defaultHtml += "</a></div>"
                    }
                    defaultHtml += "</div>";
                    $(".home-brands-you-love-carousel").css(
                        "margin-bottom", "33px");  /* UF-249 */
                    $('#brandsYouLove').append(defaultHtml);
                    window.localStorage.setItem("brandContent-" +
                        id, encodeURI(defaultHtml));
                },
                complete: function() {
                   /*$('#brandsYouLove .loaderDiv').remove();*/
                },
                error: function() {
                    /*$('#brandsYouLove .loaderDiv').remove();*/
                    /*$(".home-brands-you-love-carousel").css(
                        "margin-bottom", "33px");*/  /* UF-249 */
                    console.log(
                        "Error while getting brands you love content"
                    );
                }
            });
        }
    }
    // ENd AJAX CALL


//TPR-1672
function getBestOffersAjaxCall() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    var autoplayTimeout = 5000;
    var slideBy = 1;
    var autoPlay=true;
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getBestOffers",
        data: dataString,
        success: function(response) {
        	
        	//changes for TPR-1121
        	autoplayTimeout = response.autoplayTimeout?response.autoplayTimeout:autoplayTimeout;
        	slideBy = response.slideBy?response.slideBy:slideBy; 
            autoPlay= response.autoPlay != null ?response.autoPlay:autoPlay;
          //TPR-559 Show/Hide Components and Sub-components
        	if (response.hasOwnProperty("title") && response.hasOwnProperty("subItems")) {
	            renderHtml = "<h2>" + response.title + "</h2>" +
	                "<div class='home-best-offers-carousel'>";
	            $.each(response.subItems, function(k, v) {
	                if (v.url) {
	                    renderHtml += "<a href='" +
	                        appendIcid(v.url, v.icid) +
	                        "' class='item'>";
	                }
	                if (v.imageUrl) {
	                    renderHtml +=
	                        "<div class='home-best-Offers-carousel-img'> <img class='' src='" +
	                        v.imageUrl + "'></img></div>";
	                }
	                if (v.text) {
	                    renderHtml +=
	                        "<div class='short-info-bestOffers'>" + v.text +
	                        "</div>";
	                }
	                renderHtml += "</a>";
	            });
	            
	            renderHtml +=
	                // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
	             	"</div> <a href='";
	            if(typeof response.buttonLink!=="undefined"){
	            	 renderHtml +=response.buttonLink+"'";
	            }
	//            else{
	//            	renderHtml +=ACC.config.encodedContextPath+"/offersPage'";
	//            }
	            
	            renderHtml +="class='view-best-offers'>";
	            if(typeof response.buttonText!=="undefined"){
	            	 renderHtml +=response.buttonText;
	            }
	//            else{
	//            	 renderHtml +=" View Best Offers ";
	//            }
	            renderHtml +="</a>";
	               // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
	            	//"</div> <a href='"+ACC.config.encodedContextPath+"/offersPage' class='view-cliq-offers'> View Cliq Offers </a>";
	            $("#bestOffers").html(renderHtml);
	            // console.log()
        	}    
        },
        error: function() {
            console.log("Error while getting best picks");
        },
        complete: function() {
        	//TPR-559 Show/Hide Components and Sub-components
        	if ($(".home-best-offers-carousel").length) {
	        	$(".home-best-offers-carousel").owlCarousel({
	            	items:5,
	        		loop: true,
	        		nav:true,
	        		dots:false,
	        		navText:[],
	        		lazyLoad: false,
	        		autoplay:autoPlay,
	        		autoHeight : false,
	        		autoplayTimeout: autoplayTimeout,
		            slideBy: slideBy,
	        		responsive : {
	        			// breakpoint from 0 up
	        			0 : {
	        				items:1,
	        				stagePadding: 50,
	        			},
	        			// breakpoint from 480 up
	        			480 : {
	        				items:2,
	        				stagePadding: 50,
	        			},
	        			// breakpoint from 768 up
	        			768 : {
	        				items:3,
	        			},
	        			// breakpoint from 768 up
	        			1280 : {
	        				items:5,
	        			}			
	        		}		
	                /*navigation: true,
	                navigationText: [],
	                pagination: false,
	                itemsDesktop: [5000, 5],
	                itemsDesktopSmall: [1400, 5],
	                itemsTablet: [650, 1],
	                itemsMobile: [480, 1],
	                rewindNav: false,
	                lazyLoad: true,
	                scrollPerPage: true*/
	            });
        	}	
        }
    });
   
}


var bulCount = $(".home-brands-you-love-carousel-brands.active").index() - 1;
$(document).on("click", ".home-brands-you-love-carousel-brands",
		function() {
//			$(".home-brands-you-love-carousel-brands").removeClass('active');
//			$(this).addClass('active');
//			$('.home-brands-you-love-desc').remove();
	
//			getBrandsYouLoveContentAjaxCall($(this).attr("id"));
		});
var bulIndex = 0;

/*$(document).on("click", ".home-brands-you-love-carousel-brands", function() {
   
	$(".home-brands-you-love-carousel-brands").parent().removeClass('center');
    $(this).parent().addClass('active center');
    $('.home-brands-you-love-desc').remove();
  
    	//bulIndex = $(this).parents('.owl-item').index();

    getBrandsYouLoveContentAjaxCall($(this).attr("id"));
});*/
$(document).on("click", ".home-brands-you-love-carousel .owl-item", function() {
		/*var activePos = $(".home-brands-you-love-carousel .owl-item.center").index(),carLoop,count=0;
		bulIndex = $(this).index();
	 	if (bulIndex > activePos) {
	    	count = bulIndex - activePos;
	    	carLoop = setInterval(function(){
	    		if(count != 0) {
	    			$('.home-brands-you-love-carousel .owl-controls .owl-next').click();
	    			count--;
	    		} else {
	    			clearInterval(carLoop);
	    		}
	    	},80)
	    } else if (bulIndex < activePos) {
	    	count = activePos - bulIndex;
	    	carLoop = setInterval(function(){
	    		if(count != 0) {
	    			$('.home-brands-you-love-carousel .owl-controls .owl-prev').click();
	    			count--;
	    		} else {
	    			clearInterval(carLoop);
	    		}
	    	},80)
	    }*/
		var brandCarousel = $(".home-brands-you-love-carousel").data('owlCarousel');
		//console.log(carousel.relative($(this).index()));
		var relIndex = brandCarousel.relative($(this).index());
        $('.home-brands-you-love-carousel').trigger("to.owl.carousel", [relIndex, 500, true]);
});
/*$(document).on("click", ".bulprev", function() {
    $('.home-brands-you-love-desc').remove();
		$(".home-brands-you-love-carousel .owl-wrapper").prepend($(".home-brands-you-love-carousel .owl-item").last());
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").removeClass('active');
		$(".home-brands-you-love-carousel .home-brands-you-love-carousel-brands").eq(activePos).addClass('active');
		getBrandsYouLoveContentAjaxCall($(".home-brands-you-love-carousel-brands.active").attr('id'));
    var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
    $('.home-brands-you-love-desc').remove();
    $("#brandsYouLove .owl-wrapper").css('transition', 'all 0.3s ease');
    $("#brandsYouLove .owl-wrapper").css('transform', 'translate3d(' +
        iw + 'px, 0px, 0px)');
    setTimeout(function() {
        $(".home-brands-you-love-carousel .owl-wrapper").prepend(
            $(".home-brands-you-love-carousel .owl-item").last()
        );
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).removeClass('active');
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).eq(activePos).addClass('active');
        getBrandsYouLoveContentAjaxCall($(
            ".home-brands-you-love-carousel-brands.active"
        ).attr('id'));
        $("#brandsYouLove .owl-wrapper").css('transition',
            'all 0s ease');
        $("#brandsYouLove .owl-wrapper").css('transform',
            'translate3d(0px, 0px, 0px)');
    }, 300);
});
$(document).on("click", ".bulnext", function() {
    var iw = $('.home-brands-you-love-carousel .owl-item').outerWidth();
    $('.home-brands-you-love-desc').remove();
    $("#brandsYouLove .owl-wrapper").css('transition', 'all 0.3s ease');
    $("#brandsYouLove .owl-wrapper").css('transform', 'translate3d(-' +
        iw + 'px, 0px, 0px)');
    setTimeout(function() {
        $(".home-brands-you-love-carousel .owl-wrapper").append(
            $(".home-brands-you-love-carousel .owl-item").first()
        );
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).removeClass('active');
        $(
            ".home-brands-you-love-carousel .home-brands-you-love-carousel-brands"
        ).eq(activePos).addClass('active');
        getBrandsYouLoveContentAjaxCall($(
            ".home-brands-you-love-carousel-brands.active"
        ).attr('id'));
        $("#brandsYouLove .owl-wrapper").css('transition',
            'all 0s ease');
        $("#brandsYouLove .owl-wrapper").css('transform',
            'translate3d(0px, 0px, 0px)');
    }, 300);
});*/
if ($('#ia_site_page_id').val() == 'homepage') {
    /*setInterval(function() {

$('.bulnext').click();

}, 20000);
*/
}
// AJAX CALL BEST PICKS START


function getBestPicksAjaxCall() {
        var env = $("#previewVersion").val();
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        var autoplayTimeout = 5000;
        var slideBy = 1;
        var autoPlay=true;
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath + "/getBestPicks",
            data: dataString,
            success: function(response) {

            	//changes for TPR-1121
            	autoplayTimeout = response.autoplayTimeout?response.autoplayTimeout:autoplayTimeout;
            	slideBy = response.slideBy?response.slideBy:slideBy; 
                autoPlay= response.autoPlay != null ?response.autoPlay:autoPlay;
                
            	//TPR-559 Show/Hide Components and Sub-components
            	if (response.hasOwnProperty("title") && response.hasOwnProperty("subItems")) {
	                renderHtml = "<h2>" + response.title + "</h2>" +
	                    "<div class='home-best-pick-carousel'>";
	                $.each(response.subItems, function(k, v) {
	                    if (v.url) {
	                        renderHtml += "<a href='" +
	                            appendIcid(v.url, v.icid) +
	                            "' class='item'>";
	                    }
	                    if (v.imageUrl) {
	                        renderHtml +=
	                            "<div class='home-best-pick-carousel-img'> <img class='' src='" +
	                            v.imageUrl + "'></img></div>";
	                    }
	                    if (v.text) {
	                        renderHtml +=
	                            "<div class='short-info'>" + v.text +
	                            "</div>";
	                    }
	                    renderHtml += "</a>";
	                });
	                
	                renderHtml +=
	                    // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
	                 	"</div> <a href='";
	                if(typeof response.buttonLink!=="undefined"){
	                	 renderHtml +=response.buttonLink+"'";
	                }
	                else{
	                	renderHtml +=ACC.config.encodedContextPath+"/offersPage'";
	                }
	                
	                renderHtml +="class='view-cliq-offers'>";
	                if(typeof response.buttonText!=="undefined"){
	                	 renderHtml +=response.buttonText;
	                }
	                else{
	                	 renderHtml +=" View Cliq Offers ";
	                }
	                renderHtml +="</a>";
	                   // "</div> <a href='/store/view-all-offers' class='view-cliq-offers'> View Cliq Offers </a>";
	                	//"</div> <a href='"+ACC.config.encodedContextPath+"/offersPage' class='view-cliq-offers'> View Cliq Offers </a>";
	                $("#bestPicks").html(renderHtml);
	                // console.log()
            	}

            },
            error: function() {
                console.log("Error while getting best picks");
            },
            complete: function() {

            	//TPR-559 Show/Hide Components and Sub-components
            	if ($(".home-best-pick-carousel").length) {
	                $(".home-best-pick-carousel").owlCarousel({
	                	items:5,
	            		loop: true,
	            		nav:true,
	            		dots:false,
	            		navText:[],
	            		lazyLoad: false,
	            		autoplay:autoPlay,
			            autoHeight : false,
	            		autoplayTimeout: autoplayTimeout,
	  	               slideBy: slideBy,
	            		responsive : {
	            			// breakpoint from 0 up
	            			0 : {
	            				items:1,
	            				stagePadding: 50,
	            			},
	            			// breakpoint from 480 up
	            			480 : {
	            				items:2,
	            				stagePadding: 50,
	            			},
	            			// breakpoint from 768 up
	            			768 : {
	            				items:3,
	            			},
	            			// breakpoint from 768 up
	            			1080 : {
	            				items:5,
	            			}			
	            		}		
	                    /*navigation: true,
	                    navigationText: [],
	                    pagination: false,
	                    itemsDesktop: [5000, 5],
	                    itemsDesktopSmall: [1400, 5],
	                    itemsTablet: [650, 1],
	                    itemsMobile: [480, 1],
	                    rewindNav: false,
	                    lazyLoad: true,
	                    scrollPerPage: true*/
	                });
            	}    

            }
        });
       
    }

    // AJAX CALL BEST PICKS END
    //AJAX CALL PRODUCTS YOU CARE START

function getProductsYouCareAjaxCall() {
        var env = $("#previewVersion").val();
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        
        var slideBy=1;
        var autoplayTimeout=5000;
        var autoPlay=true;
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath + "/getProductsYouCare",
            data: dataString,
            success: function(response) {

            	//changes for TPR-1121
            	autoplayTimeout = response.autoplayTimeout?response.autoplayTimeout:autoplayTimeout;
            	slideBy = response.slideBy?response.slideBy:slideBy; 
                autoPlay= response.autoPlay != null ?response.autoPlay:autoPlay;
            	
            	//TPR-559 Show/Hide Components and Sub-components
                if (response.hasOwnProperty("title") && response.hasOwnProperty("categories") && response.title && response.categories.length) {
	                renderHtml = "<h2>" + response.title + "</h2>";
	                renderHtml +=
	                    "<div class='home-product-you-care-carousel'>";
	                $.each(response.categories, function(k, v) {
	                    var URL = ACC.config.encodedContextPath +
	                    /*"/Categories/" + v.categoryName*/ v.categoryPath +   //TISPRD_2315
	                    "/c-" + v.categoryCode.toLowerCase();
	                    	
		                //for url
		                if (!v.imageURL) {    
		                renderHtml += "<a href='" + appendIcid(
		                            URL, v.icid) +
		                        "' class='item'>";
		                }
		                else {
		                	renderHtml += "<a href='" + v.imageURL +
		                        "' class='item'>";
		                }
	                    //for image
	                    renderHtml +=
	                        "<div class='home-product-you-care-carousel-img'> <img class='' src='" +
	                        v.mediaURL + "'></img></div>";
	                 /*TPR-562 -start   */
	                   if(v.imageName) {
	                   renderHtml +=
	                     "<div class='short-info'><h3 class='product-name'><span>" +
	                        v.imageName +
	                       "</span></h3></div>";
	                    renderHtml += "</a>";
	                    
	                   }
	                   else {
	                	   renderHtml +=
	  	                     "<div class='short-info'><h3 class='product-name'><span>" +
	  	                        v.categoryName +
	  	                       "</span></h3></div>";
	  	                    renderHtml += "</a>";
	                   }
	                   /*TPR-562 -ends   */
	                });
	                renderHtml += "</div>";
	                $("#productYouCare").html(renderHtml);
                }    

            },
            error: function() {
                console.log(
                    'Error while getting getProductsYouCare');
            },

            complete : function() {
            	//TPR-559 Show/Hide Components and Sub-components
            	if ($(".home-product-you-care-carousel").length) {
						$(".home-product-you-care-carousel").owlCarousel({
							items : 4,
							loop : true,
							nav : true,
							dots : false,
							navText : [],
							lazyLoad : false,
							autoplay: autoPlay,
				            autoHeight : false,
							autoplayTimeout: autoplayTimeout,
				            slideBy: slideBy,
							responsive : {
								// breakpoint from 0 up
								0 : {
									items : 1,
									stagePadding : 50,
								},
								480 : {
									items:2,
									stagePadding: 50,
								},
								// breakpoint from 768 up
								768 : {
									items : 3,
								},
								// breakpoint from 768 up
								1080 : {
									items : 4,
								}

							}
						/*
						 * navigation: true, navigationText: [], pagination: false,
						 * itemsDesktop: [5000, 4], itemsDesktopSmall: [1400, 4],
						 * itemsTablet: [650, 2], itemsMobile: [480, 2], rewindNav:
						 * false, lazyLoad: true, scrollPerPage: true
						 */
						});
            		}	
				}
        });
    }
    // AJAX CALL PRODUCTS YOU CARE END

function getNewAndExclusiveAjaxCall() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
     var slideBy=1;
     var autoplayTimeout=5000;
     var autoPlay=true;
    
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getNewAndExclusive",
        data: dataString,
        success: function(response) {

        	//changes for TPR-1121
        	autoplayTimeout = response.autoplayTimeout?response.autoplayTimeout:autoplayTimeout;
        	slideBy = response.slideBy?response.slideBy:slideBy; 
            autoPlay= response.autoPlay != null ?response.autoPlay:autoPlay;
        	
        	//TPR-559 Show/Hide Components and Sub-components
            if (response.hasOwnProperty("title") && response.hasOwnProperty("newAndExclusiveProducts") && response.newAndExclusiveProducts.length) {
	        	var staticHost=$('#staticHost').val();
	            var defaultHtml = "";
	            renderHtml = "<h2>" + response.title + "</h2>" +
	                "<div class='js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference' id='new_exclusive'>";
	            $.each(response.newAndExclusiveProducts, function(
	                key, value) {
	            	if(value.isNew == 'Y')
	            	{
	            	renderNewHtml = "<div style='z-index: 1;' class='new'><img class='brush-strokes-sprite sprite-New' src='"+staticHost+"/_ui/responsive/common/images/transparent.png'><span>New</span></div>";
	            	} else {
	            		renderNewHtml = '';
	            	}
	                renderHtml +=
	                    "<div class='item slide'><div class='newExclusiveElement'><a href='" +
	                    ACC.config.encodedContextPath +
	                    value.productUrl + "'>"+renderNewHtml+"<img class='' src='" +
	                    value.productImageUrl +
	                    "'></img><p class='New_Exclusive_title'>" +
	                    value.productTitle +
	                    "</p><p class='New_Exclusive_price'><span class='priceFormat'>" +
	                    value.productPrice +
	                    "</span></p></a></div></div>";
	            });
	            renderHtml += "</div><a href='" + ACC.config.encodedContextPath +
	                "/search/viewOnlineProducts' class='new_exclusive_viewAll'>View All</a>";
	            $('#newAndExclusive').html(renderHtml);
            }   

        },
        error: function() {
            console.log("Error while getting new and exclusive");
        },
        complete: function() {

        	//TPR-559 Show/Hide Components and Sub-components
        	if ($("#new_exclusive").length) {
	            $("#new_exclusive").owlCarousel({
	            	items:3,
	        		loop: true,
	        		nav:true,
	        		dots:false,
	        		navText:[],
	        		lazyLoad: false,
	        		autoplay: autoPlay,
		            autoHeight : false,
		            autoplayTimeout: autoplayTimeout,
		            slideBy: slideBy,
	        		responsive : {
	        			// breakpoint from 0 up
	        			0 : {
	        				items:1,
	        				stagePadding: 50,
	        			},		
	        			480 : {
	        				items:2,
	        				stagePadding: 50,
	        			},	
	        			// breakpoint from 768 up
	        			768 : {
	        				items:3,
	        			}		
	        		}		
	                /*navigation: true,
	                rewindNav: false,
	                navigationText: [],
	                pagination: false,
	                items: 2,
	                itemsDesktop: false,
	                itemsDesktopSmall: false,
	                itemsTablet: false,
	                itemsMobile: false,
	                scrollPerPage: true,
	                lazyLoad: true*/
	            });
	            setTimeout(function() {
	                /*if($(window).width() > 773) {
						$('#newAndExclusive').css('min-height',$('#newAndExclusive').parent().height()+'px');
					}*/
	                //alert($('#newAndExclusive').height() +"|||"+$('#stayQued').height())
	            	if($('#stayQued').children().length == 0){
	            		$('#stayQued').css('min-height', 'auto');
	            	}
	            	else{
	                        $('#stayQued').css('min-height',
	                            $('#newAndExclusive').outerHeight() +
	                            'px');
	            	}
	            }, 2500);
	            $("#new_exclusive").on('changed.owl.carousel', function(event) {
	        		setTimeout(function(){
	        			var arrHtOwl=[],diffHtOwl=0;
	        		var len = $("#newAndExclusive .owl-item.active").length;
	        		for(var j=0;j<len;j++){
	        			arrHtOwl.push($("#newAndExclusive .owl-item.active").eq(j).find(".New_Exclusive_title").height());
	        		}
	        		if($(window).width() > 790) {
	        		arrHtOwl.splice(-1,1);
	        		}
	        		var max2 = Math.max.apply(Math,arrHtOwl);
	        		for(var k=0;k < arrHtOwl.length;k++){
	        			diffHtOwl = max2 - arrHtOwl[k];
	        			$("#newAndExclusive .owl-item.active").eq(k).find(".New_Exclusive_title").css("padding-bottom",+diffHtOwl);
	        		}
	        		},80);
	            });
        	}     

        }
    });
}

/* Promotional Banner Section starts */
function getPromoBannerHomepage() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath +
            "/getPromoBannerHomepage",
        data: dataString,
        success: function(response) {
        	//TPR-559 Show/Hide Components and Sub-components
        	if (response.hasOwnProperty("allBannerJsonObject") && response.allBannerJsonObject.length) {
	        	var arr= new Array();
	        	$.each( response, function(key, obj){
	                    arr.push(key,obj);
	            });
	        	var finalArr = arr[1];
	        	var count = finalArr.length;
	            if(window.sessionStorage && (seqPromo = window.sessionStorage.getItem("PromoBannerHomepage"))) {
	                seqPromo = parseInt(seqPromo);
	            	if (seqPromo == '' || seqPromo >= count || seqPromo < 1) {
	            		seqPromo = 1;
	            	} else {
	            		seqPromo=seqPromo+1;
	            	}
	        		showPromoBanner(finalArr[seqPromo-1]);
	        		window.sessionStorage.setItem("PromoBannerHomepage", seqPromo);
	        	} else {
	        		showPromoBanner(finalArr[0]);
	        		if(window.sessionStorage) {
	        			window.sessionStorage.setItem("PromoBannerHomepage", 1);
	        		}
	        	}
        	}   
        },
        error: function() {
            console.log('Failure in Promo!!!');
        }
    });
}

function showPromoBanner(response){
	 //console.log(response.bannerImage);
   var defaultHtml = "";
   var bannerUrlLink = response.bannerUrlLink;
   var bannerImage = response.bannerImage;
   var bannerAltText = response.bannerAltText;
   var promoText1 = response.promoText1;
   var promoText2 = response.promoText2;
   var promoText3 = response.promoText3;
   var promoText4 = response.promoText4;
   //renderHtml = '<img src="' + response.bannerImage +'"/>';
   renderHtml = promoText1;
   $('#promobannerhomepage').html(renderHtml);
}

/* Promotional Banner Section Ends */
/* StayQued Section starts */
function getStayQuedHomepage() {
    var env = $("#previewVersion").val();
    if (env == "true") {
        var dataString = 'version=Staged';
    } else {
        var dataString = 'version=Online';
    }
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ACC.config.encodedContextPath + "/getStayQuedHomepage",
        data: dataString,
        success: function(response) {
        	//TPR-559 Show/Hide Components and Sub-components
        	if (response.hasOwnProperty("allBannerJsonObject") && response.allBannerJsonObject.length) {
	        	var arr= new Array();
	        	$.each( response, function(key, obj){
	                    arr.push(key,obj);
	            });
	        	var finalArr = arr[1];
	        	var count = finalArr.length;
	            if(window.sessionStorage && (seqStay = window.sessionStorage.getItem("StayQuedHomepage"))) {
	                seqStay = parseInt(seqStay);
	            	if (seqStay == '' || seqStay >= count || seqStay < 1) {
	            		seqStay = 1;
	            	} else {
	            		seqStay=seqStay+1;
	            	}
	        		showStayQued(finalArr[seqStay-1]);
	        		window.sessionStorage.setItem("StayQuedHomepage", seqStay);
	        	} else {
	        		showStayQued(finalArr[0]);
	        		if(window.sessionStorage) {
	        			window.sessionStorage.setItem("StayQuedHomepage", 1);
	        		}
	        	}
        	}     
       },
        error: function() {
            console.log('Failure in StayQued!!!');
        }
    });
}

function showStayQued(response){
	//alert(response);
	var defaultHtml = "";
    var linkText = "";
    var bannerUrlLink = response.bannerUrlLink;
    var bannerImage = response.bannerImage;
    var bannerAltText = response.bannerAltText;
    var promoText1 = response.promoText1;
    var promoText2 = response.promoText2;
    var promoText3 = response.promoText3;
    var promoText4 = response.promoText4;
    if ($(promoText2).is('p')) {
        linkText = $(promoText2).text();
    } else {
        linkText = promoText2;
    }
    renderHtml =
    	'<h2><span class="h1-qued">Stay Qued</span></h2><div class="qued-padding"><div class="qued-content">' +      /* UF-249 */
        promoText1 + '<a href="' + bannerUrlLink +
        '" class="button maroon">' + linkText +
        '</a></div><div class="qued-image"><img class="lazy" src="' +
        bannerImage + '" class="img-responsive"></div></div>';
    $('#stayQued').html(renderHtml);
}


/* StayQued Section Ends */

// AJAX call for Showcase
function getShowCaseAjaxCall() {
        var env = $("#previewVersion").val();
        if (env == "true") {
            var dataString = 'version=Staged';
        } else {
            var dataString = 'version=Online';
        }
        $.ajax({
            type: "GET",
            dataType: "json",
            url: ACC.config.encodedContextPath +
                "/getCollectionShowcase",
            data: dataString,
            success: function(response) {
                //console.log(response.subComponents);
            	//TPR-559 Show/Hide Components and Sub-components
	            if (response.hasOwnProperty("title") && response.hasOwnProperty("subComponents") && response.subComponents.length) { 
	                defaultComponentId = "";
	                renderHtml = "<h2>" + response.title + "</h2>" +
	                    "<div class='MenuWrap'><div class='mobile selectmenu'></div> <div class='showcase-heading showcase-switch'>";
	                $.each(response.subComponents, function(k, v) {
	                    if (!v.showByDefault) {
	                        renderHtml +=
	                            "<div class='showcaseItem'><a id='" +
	                            v.compId + "'>" + v.headerText +
	                            "</a></div>";
	                    } else {
	                        renderHtml +=
	                            "<div class='showcaseItem'><a id='" +
	                            v.compId +
	                            "' class='showcase-border'>" +
	                            v.headerText + "</a></div>";
	                        defaultComponentId = v.compId;
	                    }
	                });
	                renderHtml += "</div></div>";
	                $('#showcase').html(renderHtml);
	                getShowcaseContentAjaxCall(defaultComponentId);
	                $('.selectmenu').text($(".showcaseItem .showcase-border").text());
	            }  
	            if($(".showcaseItem").length == 1){
                	$(".showcaseItem").addClass("one_showcase");
                }
                if($(".showcaseItem").length == 2){
                	$(".showcaseItem").addClass("two_showcase");
                }
            },
            error: function() {
                // globalErrorPopup('Failure!!!');
                console.log("Error while getting showcase");
            }
        });
    }
    // Get Showcase Content AJAX

function getShowcaseContentAjaxCall(id) {
	if (window.localStorage && (html = window.localStorage.getItem(
            "showcaseContent-" + id)) && html != "") {
            // console.log("Local");
            $('.about-one showcase-section').remove();
            $('#showcase').append(decodeURI(html));
            //LazyLoad();
        } else {
            $.ajax({
                type: "GET",
                dataType: "json",
                beforeSend: function() {
                	var staticHost=$('#staticHost').val();
                    /*$(".showcase-switch").css("margin-bottom",
                        "80px");
                    $("#showcase").append(
                        "<div class='loaderDiv' style='background: transparent;z-index: 100000;position: absolute; top: 150px;left: 50%;margin-left: -50px;display:inline-block;width:100px;height:100px;'><img src='"+staticHost+"/_ui/desktop/theme-blue/images/loading.gif' style='width:100%;'/></div>"
                    );*/
                },
                url: ACC.config.encodedContextPath +
                    "/getShowcaseContent",
                data: {
                    "id": id
                },
                success: function(response) {
                	
                    $('.about-one.showcase-section').remove();
                    defaultHtml =
                        "<div class='about-one showcase-section'>";
                    if (typeof response.bannerImageUrl !==
                        "undefined") {
                        defaultHtml += "<div class='desc-section'>";
                        if (typeof response.bannerUrl !==
                            "undefined") {
                            defaultHtml += "<a href='" + appendIcid(response.bannerUrl, response.icid) + "'>";
                        }
                        defaultHtml += "<img class='lazy' src='" + response.bannerImageUrl +
                            "'></img>";
                        if (typeof response.bannerUrl !==
                            "undefined") {
                            defaultHtml += "</a>";
                        }
                        defaultHtml += "</div>";
                    }
                    if (typeof response.text !== "undefined") {
                        defaultHtml += "<div class='desc-section'>" +
                            response.text + "</div>"
                    }
                    if (typeof response.firstProductImageUrl !==
                        "undefined") {
                        defaultHtml +=
                            " <div class='desc-section'><a href='" +
                            ACC.config.encodedContextPath +
                            response.firstProductUrl +
                            "'><img class='lazy' src='" + response.firstProductImageUrl +
                            "'></img>";
                        defaultHtml +=
                            "<div class='showcase-center'>";
                        if (typeof response.firstProductTitle !==
                            "undefined") {
                            defaultHtml +=
                                "<h2 class='product-name'>" +
                                response.firstProductTitle +
                                "</h2>";

                        }
                        if (typeof response.firstProductPrice !==
                            "undefined") {
                            defaultHtml +=
                                "<div class='price'><p class='normal'><span class='priceFormat'>" +
                                response.firstProductPrice +
                                "</span></p></div>";
                        }
                        defaultHtml += "</a></div>";
                    }
                    defaultHtml += "</div>";
                    /*$('#showcase .loaderDiv').remove();
                    $(".showcase-switch").css("margin-bottom",
                        "0px");*/
                    $('#showcase').append(defaultHtml);
                    window.localStorage.setItem("showcaseContent-" +
                        id, encodeURI(defaultHtml));
                },
                error: function() {
                    console.log(
                        "Error while getting showcase content");
                    /*$('#showcase .loaderDiv').remove();
                    $(".showcase-switch").css("margin-bottom",
                        "0px");*/
                }
            });
        }
    }
    // ENd AJAX CALL

$(document).on("click", ".showcaseItem", function() {
    var id = $(this).find('a').attr("id");
    $(".showcaseItem").find("a").removeClass("showcase-border");
    $(this).find('a').addClass('showcase-border');
    $('.about-one.showcase-section').remove();
    getShowcaseContentAjaxCall(id);
});
$(window).on('resize', function() {
    if ($(window).width() > 790) {
        $('#stayQued').css('min-height', 'auto');
    }
});

function appendIcid(url, icid) {
    if (url != null) {
        if (url.indexOf("?") != -1) {
            return url + "&icid=" + icid;
        } else {
            return url + "?icid=" + icid;
        }
    }
}
$(document).ready(function(){
	//TISPT-290
	if($('#pageTemplateId').val() =='LandingPage2Template'){
	lazyLoadDivs();

	setTimeout(function(){$(".timeout-slider").removeAttr("style")},1500);
}
//Fix for defect TISPT-202
getFooterOnLoad();

$(document).on("click", ".showcaseItem", function() {
	$('.selectmenu').text($(this).children().text());
	/*TPR-650 Start*/
	//TISQAEE-59
	var name=$(this).parents('#showcase').children('h2').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var value = $(this).find('a').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	utag.link({
		link_obj: this,
		link_text: name+'_'+value,
		event_type : name+'_click'
	});
	/*TPR-650 End*/
});

$(window).on("load resize", function() {
    if ($(window).width() <= 767) {
        $(".showcase-heading").hide();
        $(document).off("click",".selectmenu").on("click",".selectmenu",function() {
            $(".showcase-heading").slideToggle();
        });
        $(document).off("click",".showcase-heading").on("click",".showcase-heading",function() {
            $(this).slideUp();
        });
    } else {
        $(".showcase-heading").show();
        $(document).off("click",".showcase-heading").on("click",".showcase-heading",function() {
        });
    }
});

});
//call lazy load after ajaz for page stops
/*$(document).ajaxStop(function(){
	LazyLoad();
});

function LazyLoad(){
	$("img.lazy").lazyload({
    	effect : "fadeIn"
    });
}*/

$(document).ready(function() {
var resize_stop;
$(window).on('resize', function() {
	  clearTimeout(resize_stop);
	  resize_stop = setTimeout(function() {
		  $('.home-brands-you-love-carousel-brands.active').click();
		  
		  var tcitemLength = $(".topConcierge .owl-item").length,tcitemWidth = $(".topConcierge .owl-item").outerWidth() ;
			if (tcitemLength < 5) {
				$(".topConcierge .owl-wrapper").css({
				"width":tcitemLength*tcitemWidth,
				"margin" : "auto"
				});
			}
			
	  }, 250);
});

	
	if (!$.cookie("enhanced-search-list") && window.localStorage) {
        for (var key in localStorage) {
            if (key.indexOf("enhancedSearchData") >= 0) {
                window.localStorage.removeItem(key);

            }
        }
    }
    if (window.localStorage && (data = window.localStorage.getItem("enhancedSearchData")) && data != "") {
        populateEnhancedSearch(JSON.parse(data));
    } 
    else {
	$.ajax({
		url : ACC.config.encodedContextPath + "/view/MplEnhancedSearchBoxComponentController/searchdropdown",
		type : 'GET',
		//dataType: "json",
		success : function(enhancedSearchData){
			 populateEnhancedSearch(enhancedSearchData);
			
			if (window.localStorage) {
                $.cookie("enhanced-search-list", "true", {
                    expires: 1,
                    path: "/"

                });
                window.localStorage.setItem(
                    "enhancedSearchData",
                    JSON.stringify(enhancedSearchData));
            }
			
		},
		error : function(error){
		}
	});
    }
    
   $(".lazy-brands").on("mouseover touchend", function(e) {
	   var lazyImgs = $(this).find("ul.images").find("img.lazy");
	   $(lazyImgs).each(function(){
		   var original = $(this).attr("data-src");
		   $(this).attr("src",original);
		   $(this).removeAttr("data-src");
	   });
   });
   
   //showMobileShowCase();
});



function populateEnhancedSearch(enhancedSearchData)
{
	var searchCode=$("#searchCodeForDropdown").val();
	var notPresentCategory=true;
	var notPresentBrand=true;
	var notPresentSeller=true;
	if(enhancedSearchData.categoryData.length > 0){
		$(".select-view #enhancedSearchCategory").append('<optgroup label="Departments"></optgroup>');
		for (var i=0; i<enhancedSearchData.categoryData.length; i++){
			var code=enhancedSearchData.categoryData[i].code;
			var name=enhancedSearchData.categoryData[i].name;
			var className='';
			if(searchCode==code)
			{
				className="selected";
				notPresentCategory=false;
			}
			$(".enhanced-search ul[label=Departments]").append('<li id="'+code+'" class="'+className+'">'+name+'</li>');
			$(".select-view #enhancedSearchCategory optgroup[label=Departments]").append('<option value="'+code+'" '+ className+' >'+name+'</option>');
		}
		var selectedText = $(".select-list .dropdown li.selected").text();
		$("#searchBoxSpan").html(selectedText);
	}
	
	if(enhancedSearchData.brandData.length > 0){
		$(".select-view #enhancedSearchCategory").append('<optgroup label="Brands"></optgroup>');
		for (var i=0; i<enhancedSearchData.brandData.length; i++){
			var code=enhancedSearchData.brandData[i].code;
			var name=enhancedSearchData.brandData[i].name;
			var className='';
			if(searchCode==code)
			{
				className="selected";
				notPresentBrand=false;
			}
			$(".enhanced-search ul[label=Brands]").append('<li id="'+code+'" class="'+className+'">'+name+'</li>');
			$(".select-view #enhancedSearchCategory optgroup[label=Brands]").append('<option value="'+code+'" '+ className+' >'+name+'</option>');
			
		}
		var selectedText = $(".select-list .dropdown li.selected").text();
		$("#searchBoxSpan").html(selectedText);
	}
	
	if(enhancedSearchData.sellerData.length > 0){
		$(".select-view #enhancedSearchCategory").append('<optgroup label="Sellers"></optgroup>');
		for (var i=0; i<enhancedSearchData.sellerData.length; i++){
			var code=enhancedSearchData.sellerData[i].id;
			var name=enhancedSearchData.sellerData[i].name;
			var className='';
			if(searchCode==code)
			{
				className="selected";
				notPresentSeller=false;
			}
			$(".enhanced-search ul[label=Sellers]").append('<li id="'+code+'" class="'+className+'">'+name+'</li>');
			$(".select-view #enhancedSearchCategory optgroup[label=Sellers]").append('<option value="'+code+'" '+ className+' >'+name+'</option>');
		}
		var selectedText = $(".select-list .dropdown li.selected").text();
		$("#searchBoxSpan").html(selectedText);
	}
	
	if(notPresentCategory==true && notPresentBrand==true && notPresentSeller==true)
	{
		$(".select-list .dropdown li#all").addClass("selected");
		$("#searchBoxSpan").html($(".select-list .dropdown li#all").text());
	}
}
	
	//Added
	
//	$("div.toggle.brandClass").on("mouseover touchend", function() {
		$(document).on("mouseover touchend", "div.brandClass", function() {
		var componentUid = $(this).find('a').attr('id');
		 if (!$.cookie("dept-list") && window.localStorage) {
		        for (var key in localStorage) {
		            if (key.indexOf("brandhtml") >= 0) {
		                window.localStorage.removeItem(key);
		                // console.log("Deleting.." + key);

		            }
		        }
		    }
		 if (window.localStorage && (html = window.localStorage.getItem("brandhtml-" + componentUid)) && html != "") {
		        // console.log("Local");
		        //$("ul#"+componentUid).html(decodeURI(html));
			    $("ul[id='"+componentUid+"']").html(decodeURI(html));
		    }else{
		    	
		    	 $.ajax({
			            url: ACC.config.encodedContextPath +
			            "/shopbybrand",
			            type: 'GET',
			            data:{"compId":componentUid},
			            success: function(html) {
			                //$("ul#"+componentUid).html(html);
			            	$("ul[id='"+componentUid+"']").html(html); 
			                if (window.localStorage) {
			                	var date = new Date();
			                	var minutes = 600;
			                	date.setTime(date.getTime() + (minutes * 60 * 1000)); 
			                	
			                    $.cookie("dept-list", "true", {
			                        expires: date,
			                        path: "/"

			                    });
			                    window.localStorage.setItem(
			                        "brandhtml-" + componentUid,
			                        encodeURI(html));

			                }
			                
			            }
			        });
		    	
		    } 	    
	});
	//End
	
	// Fix for defect TISPT-202
	
	function openNeedHelpSec()
	{
		if(!$('.gwc-chat-embedded-window').hasClass('minimized')){
			$(this).removeClass("minimize");
			$("#h").toggle();
		}
	}
	function getFooterOnLoad()
	{
		var slotUid = "FooterSlot";
		var pageName = $('#pageName').val();
		if (!$.cookie("dept-list") && window.localStorage) {
	        for (var key in localStorage) {
	            if (key.indexOf("footerhtml") >= 0) {
	                window.localStorage.removeItem(key);                
	            }
	        }
	    }
		
		if (window.localStorage && (html = window.localStorage.getItem("footerhtml")) && html != "" && pageName != "Cart Page") {
			$("#footerByAjaxId").html(decodeURI(html));
	    } else {
	    	if(pageName != "Cart Page")
	    	{
	        $.ajax({
	            url: ACC.config.encodedContextPath +
	                "/getFooterContent",
	            type: 'GET',
	            data : {
					 "id" : slotUid
					},
	            success: function(footerhtml) {
	            	$("#footerByAjaxId").html(footerhtml);
	            	
	                if (window.localStorage) {
	                    $.cookie("dept-list", "true", {
	                        expires: 1,
	                        path: "/"

	                    });
	                    window.localStorage.setItem(
	                        "footerhtml",
	                        encodeURI(footerhtml));
	                }
	         
					if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
						var footer_height=$('footer').height() + 20 + 'px';
						$(".body-Content").css('padding-bottom',footer_height);
					}
					else{
						$(".body-Content").css('padding-bottom','0px');
					}
	            }
	        });
	      }  
	    }	
	}
	
	//TISPT-290
	function lazyLoadDivs(){
		//Changes
		
		$(window).on('scroll load',function() {
			lazyLoadfunction();
		});
		//End
		var ctrlKey = false;
		$(document).keydown(function(e) {
	        if (e.keyCode == 17) ctrlKey = true;
	    }).keyup(function(e) {
	        if (e.keyCode == 17) ctrlKey = false;
	    });

	    $(document).keydown(function(e) {
	        if (ctrlKey && (e.which == 109 || e.which == 107 || e.which == 189 || e.which == 187)) {
	        	lazyLoadfunction();
	        }
	    });
		
		
	}
	
	function lazyLoadfunction() {
		
		if ($(window).scrollTop() + $(window).height() >= $('#brandsYouLove').offset().top) {
	        if(!$('#brandsYouLove').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#brandsYouLove').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#brandsYouLove').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	 if (window.localStorage) {
	     		        for (var key in localStorage) {
	     		            if (key.indexOf("brandContent") >= 0) {
	     		                window.localStorage.removeItem(key);
	     		                //console.log("Deleting.." + key);
	     		            }
	     		        }
	     		    }
	     		    getBrandsYouLoveAjaxCall();
	        }
	        }
	}
		
		//TPR-1672
		if ($(window).scrollTop() + $(window).height() >= $('#bestOffers').offset().top) {
	        if(!$('#bestOffers').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#bestOffers').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#bestOffers').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getBestOffersAjaxCall();
	        }
	        }
	}
		
		if ($(window).scrollTop() + $(window).height() >= $('#promobannerhomepage').offset().top) {
	        if(!$('#promobannerhomepage').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#promobannerhomepage').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#promobannerhomepage').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getPromoBannerHomepage();
	        }
	        }
	}
		if ($(window).scrollTop() + $(window).height() >= $('#bestPicks').offset().top) {
	        if(!$('#bestPicks').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#bestPicks').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#bestPicks').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getBestPicksAjaxCall();
	        }
	        }
	}
		
		if ($(window).scrollTop() + $(window).height() >= $('#productYouCare').offset().top) {
	        if(!$('#productYouCare').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#productYouCare').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#productYouCare').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getProductsYouCareAjaxCall();
	        }
	        }
	}
		if ($(window).scrollTop() + $(window).height() >= $('#newAndExclusive').offset().top) {
	        if(!$('#newAndExclusive').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#newAndExclusive').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#newAndExclusive').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getNewAndExclusiveAjaxCall();
	        }
	        }
	}
		
		if ($(window).scrollTop() + $(window).height() >= $('#stayQued').offset().top) {
	        if(!$('#stayQued').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#stayQued').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#stayQued').children().length == 0 && $('#pageTemplateId').val() ==
	            'LandingPage2Template') {
	            	getStayQuedHomepage();
	        }
	        }
	}
		
		//alert("$('#showcase').offset().top ----------" + $('#showcase').offset().top);
		if ($(window).scrollTop() + $(window).height() >= $('#showcase').offset().top && $(window).width() > 767) {
	        if(!$('#showcase').attr('loaded')) {
	            //not in ajax.success due to multiple sroll events
	            $('#showcase').attr('loaded', true);

	            //ajax goes here
	            //by theory, this code still may be called several times
	            if ($('#showcase').children().length == 0 && $('#pageTemplateId').val() ==
			    'LandingPage2Template') {
			    if (window.localStorage) {
			        for (var key in localStorage) {
			            if (key.indexOf("showcaseContent") >= 0) {
			                window.localStorage.removeItem(key);
			                //console.log("Deleting.." + key);
			            }
			        }
			    }
			    getShowCaseAjaxCall();
			}
	        }
	}
	
		//alert("$('#showcaseMobile').offset().top ----------" + $('#showcaseMobile').offset().top); 
		if ($(window).scrollTop() + $(window).height() >= $('#showcaseMobile').offset().top && $(window).width() <= 767) {
			 if(!$('#showcaseMobile').attr('loaded')) {
				 //not in ajax.success due to multiple sroll events
		         $('#showcaseMobile').attr('loaded', true);
				 
		         if ($('#showcaseMobile').children().length == 0 && $('#pageTemplateId').val() == 'LandingPage2Template') {
					   showMobileShowCase();
				   }
			 	} 
		 }
}
$(document).ready(function()
{
	//start//
	$('header .content nav > ul > li > div.toggle').on('mouseover touchend',function()
	{
		$(this).parent().addClass('hovered');
		
		//department/////////////////////
		if($('header .content nav > ul > li:first-child').hasClass('hovered')) 
		{
			var id = $('header .content nav > ul > li.hovered > ul > li:first-child .departmenthover').attr('id');
		   // var code = id.substring(4);

		    if (!$.cookie("dept-list") && window.localStorage) {
		        for (var key in localStorage) {
		            if (key.indexOf("deptmenuhtml") >= 0) {
		                window.localStorage.removeItem(key);
		                // console.log("Deleting.." + key);

		            }
		        }
		    }
//	    if (window.localStorage && (html = window.localStorage.getItem("deptmenuhtml-" + code)) && html != "") 
//		{
//	        // console.log("Local");
//	        $("ul." + id).html(decodeURI(html));
//	        //LazyLoad();
//	        
//	    } else {
//	        $.ajax({
//	            url: ACC.config.encodedContextPath +
//	                "/departmentCollection",
//	            type: 'GET',
//	            data: "department=" + code,
//	            success: function(html) {
//	                // console.log("Server");
//	                $("ul." + id).html(html);
//	                if (window.localStorage) {
//	                	//TISPRD-8265-- Local Caching reduced to 10hrs from 1day
//	                	var date = new Date();
//	                	var minutes = 600;
//	                	date.setTime(date.getTime() + (minutes * 60 * 1000)); 
//	                	
//	                    $.cookie("dept-list", "true", {
//	                        expires: date,
//	                        path: "/"
//
//	                    });
//	                    window.localStorage.setItem(
//	                        "deptmenuhtml-" + code,
//	                        encodeURI(html));
//
//	                }
//	            }
//	        });
//
//
//	    }
		    
		} else {
			if($('header .content nav > ul > li.hovered > ul > li:first-child > div').hasClass('brandClass')) {
			////newbrand
				var componentUid = $('header .content nav > ul > li.hovered > ul > li:first-child > div > a ').attr('id');
				 if (!$.cookie("dept-list") && window.localStorage) {
				        for (var key in localStorage) {
				            if (key.indexOf("brandhtml") >= 0) {
				                window.localStorage.removeItem(key);
				                // console.log("Deleting.." + key);

				            }
				        }
				    }
				 if (window.localStorage && (html = window.localStorage.getItem("brandhtml-" + componentUid)) && html != "") {
				        // console.log("Local");
				        //$("ul#"+componentUid).html(decodeURI(html));
					    $("ul[id='"+componentUid+"']").html(decodeURI(html));
				    }else{
				    	
				    	 $.ajax({
					            url: ACC.config.encodedContextPath +
					            "/shopbybrand",
					            type: 'GET',
					            data:{"compId":componentUid},
					            success: function(html) {
					                //$("ul#"+componentUid).html(html);
					            	$("ul[id='"+componentUid+"']").html(html); 
					                if (window.localStorage) {
					                	var date = new Date();
					                	var minutes = 600;
					                	date.setTime(date.getTime() + (minutes * 60 * 1000)); 
					                	
					                    $.cookie("dept-list", "true", {
					                        expires: date,
					                        path: "/"

					                    });
					                    window.localStorage.setItem(
					                        "brandhtml-" + componentUid,
					                        encodeURI(html));

					                }
					                
					            }
					        });
				    	
				    }		       
				 ////////new brand
			} else if($('header .content nav > ul > li.hovered > ul > li:first-child > div').hasClass('A-ZBrands')) {
				
				var componentUid = $("#componentUid").val();
			    if ($("li#atozbrandsdiplay").length) {
			        // console.log("Dipslaying A-Z Brands..");

			        if (!$.cookie("dept-list") && window.localStorage) {
			            for (var key in localStorage) {
			                if (key.indexOf("atozbrandmenuhtml") >= 0) {
			                    window.localStorage.removeItem(key);
			                    // console.log("Deleting.." + key);
			                }
			            }
			        }
			        
					if (window.localStorage && (html = window.localStorage.getItem("atozbrandmenuhtml")) && html != "") {
			            // console.log("Local");
			            if ($("div#appendedAtoZBrands") == null || $(
			                "div#appendedAtoZBrands").length == 0) {
			                $("li#atozbrandsdiplay").append(decodeURI(html));
			            }
			        } else {
			            // console.log("Server");
			           
					   $.ajax({
			                url: ACC.config.encodedContextPath +
			                    "/atozbrands",
			                type: 'GET',
			                data : {
								 "componentUid" : componentUid
								},
			                success: function(html) {
			                    //console.log(html)
			                    if ($("div#appendedAtoZBrands") == null ||
			                        $("div#appendedAtoZBrands").length ==
			                        0) {
			                        $("li#atozbrandsdiplay").append(
			                            html);
			                    }
			                    if (window.localStorage) {
			                        $.cookie("dept-list", "true", {
			                            expires: 1,
			                            path: "/"

			                        });
			                        window.localStorage.setItem(
			                            "atozbrandmenuhtml",
			                            encodeURI(html));
			                    }
			                }
			            });
			        }
			    }

			}
		}
	});
	
	$(document).on('mouseover touchend','header .content nav > ul > li#shopMicrositeSeller > div.toggle',function(){
		$(this).parent().addClass('hovered');
	});
	$(document).on('mouseleave','header .content nav > ul > li.hovered > ul > li:first-child,header .content nav > ul > li > div.toggle',function(){
		$('header .content nav > ul > li').removeClass('hovered');
	});
	
});

	function forceUpdateHeader(){
		$.ajax({
	        url: ACC.config.encodedContextPath + "/setheader?timestamp="+Date.now(),
	        type: 'GET',
	        cache:false,
	        success: function(data) {
	        	window.sessionStorage.setItem("header" , JSON.stringify(data));
	        	setHeader(data);
	        }
	    });
	}
	
	function setHeader(data){
		   headerLoggedinStatus = data.loggedInStatus;
           //TISPT-197
           if(data.cartcount!='NaN')
       	{
           	$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").html(data.cartcount);
       	}
           else
           {
           	$("span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count").html('0');
           }
           
           if($("#pageType").val() == 'cart'){
         		
      			$('#mybagcnt').html(data.cartcount);
             		
             	}
           
           if (!headerLoggedinStatus) {

        	   $("a.headeruserdetails").html("Sign In / Sign Up"); /*UF-249 text change*/
             //Akamai caching
               $("a.headeruserdetails").attr('href','/login');
               $('#signIn').attr('class','sign-in-info signin-dropdown-body ajaxflyout');
               
               $("a.tracklinkcls").attr('href','/login');
               $("a.tracklinkcls").html('<span class="bell-icon"></span>&nbsp;Notifications');
           } else {
               var firstName = data.userFirstName;
               if (firstName == null || firstName.trim() ==
                   '') {
                   $("a.headeruserdetails").html("Hi!");
               } else {
                   $("a.headeruserdetails").html("Hi, " +
                       firstName + "!");
               }
               //Akamai caching
               $('#signIn').attr('class','dropdown-menu dropdown-hi loggedIn-flyout ajaxflyout');
               /*$("a.headeruserdetails").attr('href','/my-account');*/
               $("a.headeruserdetails").attr('href',''); /*client feedback to remove href*/
               $("a.tracklinkcls").attr('href','#');
               if(data.notificationCount != null){            	   
	               	 $("a.tracklinkcls").html('<span class="bell-icon"></span>&nbsp;Notifications&nbsp;(<span >'+data.notificationCount+'</span>)');
	               } else {
	               	 $("a.tracklinkcls").html('<span class="bell-icon"></span>&nbsp;Notifications');
	               } 
           }
	}
	
	function userCookieChanged(){
		var mplUserSession = window.sessionStorage.getItem("mpl-user");
        if(window.sessionStorage && (null == mplUserSession || mplUserSession != $.cookie("mpl-user"))){
       	 window.sessionStorage.setItem("mpl-user",$.cookie("mpl-user"));
       	 return true;
        }else{
        	return false;
        }
	}
	
	function forceSetHeader(){
	 var pageType = $("#pageType").val();
	 //Fix for INC144315287 & INC144315355
	 if(pageType == 'cart' || pageType == 'orderconfirmation' || pageType == 'update-profile' || pageType == 'homepage'){
		 return true;
	 }
	}
	
	
	
	$(document).ajaxComplete(function(){
	var arrHt=[],diffHt=0,arrHtOwl=[],diffHtOwl=0;
	$(".home-brands-you-love-wrapper .home-brands-you-love-desc p.product-name").each(function(){
		arrHt.push($(this).height());
	});
	//noprotect
	var max1 = Math.max.apply(Math,arrHt);
	for(var i=0;i < arrHt.length;i++){
	    diffHt = max1 - arrHt[i];
	    $("#brandsYouLove .home-brands-you-love-desc p.price").eq(i).css("margin-top",+diffHt);
	  }
	var len = $("#newAndExclusive .owl-item.active").length;
	for(var j=0;j<len;j++){
		arrHtOwl.push($("#newAndExclusive .owl-item.active").eq(j).find(".New_Exclusive_title").height());
	}
	if($(window).width() > 790) {
	arrHtOwl.splice(-1,1);
	}
	var max2 = Math.max.apply(Math,arrHtOwl);
	for(var k=0;k < arrHtOwl.length;k++){
		diffHtOwl = max2 - arrHtOwl[k];
		$("#newAndExclusive .owl-item.active").eq(k).find(".New_Exclusive_title").css("padding-bottom",+diffHtOwl);
	}
	
	
	});
	
	$(window).on("resize", function() {
		var arrHt=[],diffHt=0,arrHtOwl=[],diffHtOwl=0;
		$(".home-brands-you-love-wrapper .home-brands-you-love-desc p.product-name").each(function(){
			arrHt.push($(this).height());
		});
		//noprotect
		var max1 = Math.max.apply(Math,arrHt);
		for(var i=0;i < arrHt.length;i++){
		    diffHt = max1 - arrHt[i];
		    $("#brandsYouLove .home-brands-you-love-desc p.price").eq(i).css("margin-top",+diffHt);
		  }
		
		var len = $("#newAndExclusive .owl-item.active").length;
		for(var j=0;j<len;j++){
			arrHtOwl.push($("#newAndExclusive .owl-item.active").eq(j).find(".New_Exclusive_title").height());
		}
		if($(window).width() > 790) {
		arrHtOwl.splice(-1,1);
		}
		var max2 = Math.max.apply(Math,arrHtOwl);
		for(var k=0;k < arrHtOwl.length;k++){
			diffHtOwl = max2 - arrHtOwl[k];
			$("#newAndExclusive .owl-item.active").eq(k).find(".New_Exclusive_title").css("padding-bottom",+diffHtOwl);
		}
	});
	
	$(document).on("click","div.departmenthover + span#mobile-menu-toggle",function(){
		if($(this).siblings("ul.words").children().length == 0){
			$(this).siblings("div.departmenthover").mouseover();
		}
		
		
	});

	//TISPRD-4587
	/*$(document).ajaxComplete(function(){
		$("div#appendedAtoZBrands").eq(1).children("div#A-E").show();
		$(".lazy-brands>div.toggle.A-ZBrands,div#appendedAtoZBrands>div#A-E").mouseenter(function(){
			$("div#appendedAtoZBrands").eq(1).children("div#A-E").show();
		});
		$("div.toggle").mouseenter(function(){
			if($("div#appendedAtoZBrands").parents("li.lazy-brands").index() === 0){
			$("div#appendedAtoZBrands").eq(1).children("div#A-E").show();
			}
		});
		$("div#appendedAtoZBrands").eq(1).siblings("div#groups").children("div#group").mouseenter(function(){
			var index = $(this).index();
			$("div#appendedAtoZBrands").eq(1).children("div").removeClass("showAZBrandsImportant");
			$("div#appendedAtoZBrands").eq(1).children("div").hide();
			$("div#appendedAtoZBrands").eq(1).children("div").eq(index).addClass("showAZBrandsImportant");
			$("div#appendedAtoZBrands").eq(1).siblings("div#groups").children("div#group").children("a.brandGroupLink").css({"border-bottom-style": "none","font-weight": "400"});
			$(this).children("a.brandGroupLink").css({"border-bottom-width": "3px","border-bottom-style": "solid","font-weight":"bold"}); 
		});
	}); */

	$(document).on("click","div.brandClass + span#mobile-menu-toggle",function(){
		if($(this).siblings("ul.images").children().length == 0){
			$(this).siblings("div.brandClass").mouseover();
		}
		
		
	});
	$(document).on("click","div.A-ZBrands + span#mobile-menu-toggle",function(){
		if($(this).siblings("ul.a-z div.view_brands").siblings().length == 0){
			$(this).siblings("div.A-ZBrands").mouseover();
		}
		
		
	});
	
	//Added for luxury site starts
	$('document').ready(function(){
	    $('#flip-navigation li a').each(function(){  
	        $(this).click(function(){  
	            $('#flip-navigation li').each(function(){  
	                $(this).removeClass('selected');  
	            });  
	            $(this).parent().addClass('selected');           
	            // Here we get the href value of the selected tab
	            var selected_tab = $(this).find("a").attr("href");             
	            var starting = selected_tab.indexOf("#");
	            var sub = selected_tab.substring(starting);            
	            $(sub).fadeIn();
	            return false;  
	        });  
	    });  
	}); 
	//Added for luxury site ends

	
   //  Change for TISPRD-4587 
	$(document).on("mouseenter",".A-ZBrands",function(){
		
		if($("#A-E").css("display") == "block"){
			
			$("#A-E").css("display","block");
		}
	});
		
		
		$("div.azWrapper").eq(1).children("div#A-E").show();
		$(document).on("mouseenter","div.toggle",function(){
			$("div.azWrapper").eq(1).children("div").hide();
			if($("div.azWrapper").parents("li.lazy-brands").index() === 0){
			$("div.azWrapper").eq(1).children("div#A-E").show();
			}
		});
		$(document).on("mouseenter",".lazy-brands>div.toggle.A-ZBrands,div.azWrapper>div#A-E",function(){
			$("div.azWrapper").eq(1).children("div#A-E").show();
		});
		
		
		$(document).on("mouseenter",".a-z:last div#groups div#group",function(){
			var index = $(this).index();
			$("div.azWrapper").eq(1).children("div").removeClass("showAZBrandsImportant");
			$("div.azWrapper").eq(1).children("div").hide();
			$("div.azWrapper").eq(1).children("div").eq(index).addClass("showAZBrandsImportant");
			$("div.azWrapper").eq(1).siblings("div#groups").children("div#group").children("a.brandGroupLink").removeClass("present").css({"border-bottom-style": "none","font-weight": "400"});
			$(this).children("a.brandGroupLink").addClass("present").css({"border-bottom-width": "3px","border-bottom-style": "solid","font-weight":"bold"}); 
		});
		$(document).on("mouseleave",".a-z:last",function(){
			$("div.azWrapper").eq(1).children("div").removeClass("showAZBrandsImportant");
		})
		$(document).on("mouseenter mousemove","ul.a-z:last",function(){
			if($('.a-z:last div#groups div#group:first a').css("border-bottom-style") == "solid") {
				$(".azWrapper").eq(1).children("#A-E").addClass("importantDisplay")
			} else {
				$(".azWrapper").eq(1).children("#A-E").removeClass("importantDisplay");
			}
		});

	
		 //changes for performance fixof TPR-561 
		$(document).ready(function() {
			//$(".shop_dept").on("mouseover touchend", function(e) {
				//e.stopPropagation();
				if (!$('.shopByDepartment_ajax').children().length){  
					$.ajax({
						url: ACC.config.encodedContextPath +  "/shopbydepartment",
						type: 'GET',
						//cache:false,
						//changes for CAR-224
						cache:true,
						success: function(html) {
							$(".shopByDepartment_ajax").html(html);
						}
					});
				}	
			});
		/*Start TISSQAEE-325*/
		$(document).ajaxComplete(function(){
			paddingAdjust();
		});
		$(window).on("load",function(){
			paddingAdjust();
		});
		
		function paddingAdjust(){
			var ht = $(".mainContent-wrapper footer").height();
			$(".body-Content").css("padding-bottom",ht);
		}
		/*End TISSQAEE-325*/

		
		function showMobileShowCase(){
			var env = $("#previewVersion").val();
				if (env == "true") {
					var dataString = 'version=Staged&mob=true';
				} else {
					var dataString = 'version=Online&mob=true';
				}
				if (window.localStorage && (html = window.localStorage.getItem("showcaseContentMobile")) && html != "") {
					$('#showcaseMobile').html(decodeURI(html));
					$(".showcase-carousel").owlCarousel({
						items:1,
			    		loop: $(".showcase-carousel img").length == 1 ? false : true,
			    		navText:[],
			    		nav:true,
			    		dots:false
					});
				}else{
				$.ajax({
			            type: "GET",
			            dataType: "json",
			            url: ACC.config.encodedContextPath +
			                "/getCollectionShowcase",
			            data: dataString,
			            success: function(response) {
			            	try{
			            			var showCaseMobile = '<h2>'+response.title+'</h2>';
			            			showCaseMobile+= '<div class="owl-carousel showcase-carousel">';
								$.each(response.subComponents, function(k, v) {	
									  showCaseMobile+= getShowcaseMobileContentAjaxCall(v.compId);
									 
								});
								showCaseMobile+= '</div>';
								$('#showcaseMobile').html(showCaseMobile);
								window.localStorage.setItem("showcaseContentMobile",encodeURI(showCaseMobile));
			            	}
			            	catch(e)
			            	{
			            		console.log(e);
			            	}
			             },
						complete:function(){
						$(".showcase-carousel").owlCarousel({
						items:1,
			    		loop: $(".showcase-carousel img").length == 1 ? false : true,
			    		navText:[],
			    		nav:true,
			    		dots:false
					});
						},
			            error: function() {
			                console.log("Error while getting showcase");
			            }
			        });
				}
			}


			function getShowcaseMobileContentAjaxCall(id){
			var showCaseMobile = '';
			            $.ajax({
			                type: "GET",
			                dataType: "json",
							async:false,
			                beforeSend: function() {
			                	var staticHost=$('#staticHost').val();
			                    /*$(".showcase-switch").css("margin-bottom",
			                        "80px");
			                    $("#showcase").append(
			                        "<div class='loaderDiv' style='z-index: 100000;position: absolute; top: 150px;left: 50%;margin-left: -50px;'><img src='"+staticHost+"/_ui/responsive/common/images/red_loader.gif'/></div>"
			                    );*/
			                },
			                url: ACC.config.encodedContextPath +
			                    "/getShowcaseContent",
			                data: {
			                    "id": id
			                },
			                success: function(response) {
			                 
			                showCaseMobile+= '<div class="item showcase-section">';
							showCaseMobile+= '<div class="desc-section">'+response.text;
							showCaseMobile+= '<img class="lazy" src="'+response.bannerImageUrl+'">'
							showCaseMobile+= '</div>'
							showCaseMobile+= '</div>';               
			                },
			                error: function() {
			                    console.log(
			                        "Error while getting showcase content");
			                    //$('#showcase .loaderDiv').remove();
			                    //$(".showcase-switch").css("margin-bottom",
			                       // "0px");
			                }
			            });
			return showCaseMobile;
			}		
var productIdArray = [];
$(document).ready(
		

		function() {
			//TPR-4731 changes start
			if($('body').hasClass('pageType-ContentPage') && $('#ia_category_code').val() != '' && $('#ia_category_code').val().toLowerCase().indexOf('mbh') != -1){
 				var pageName = $('#pageName').val();
 				var brandName='';
 				if(typeof pageName != undefined && pageName != ''){
 					pageName=pageName.split('BrandStore-');
 					brandName = pageName[1].trim().toLowerCase();
 					if(typeof(Storage) !== "undefined") {
 						localStorage.setItem("brandName", brandName);
 					}
 				}
 			}
 			else if($('#pageType').val() != "category"){
 				if(typeof(Storage) !== "undefined") {
 					if(localStorage.getItem("brandName") != null){
 						localStorage.removeItem("brandName");
 					}
 				}
 			}
			//TPR-4731 changes end
			tealiumCallOnPageLoad();//Moving tealium on-load call to function so that it can be re-used.
});

/*TPR-429 Start*/

function differentiateSeller(){
	var sellerList = $('#pdpSellerIDs').val();
	var buyboxSeller = $("#sellerSelId").val();
	sellerList = sellerList.substr(1,(sellerList.length)-2);
	sellerList = sellerList.split(',');
	
	var otherSellers='';
	for (var i=0;i<sellerList.length;i++){
		if(sellerList[i].trim() != buyboxSeller){
			if(otherSellers == ''){
				otherSellers = sellerList[i].trim();
			}
			else{
				otherSellers += '_'+sellerList[i].trim();
			}
		}
	}
	
	$('#pdpBuyboxWinnerSellerID').val(buyboxSeller);
	$('#pdpOtherSellerIDs').val(otherSellers);
}

/*TPR-429 End*/

/*TPR-663 START*/
$('#deliveryMethodSubmit').click(function(){
	utag.link({
		"link_text": "delivery_choose_address_bottom_id2", "event_type" : "delivery_choose_address"
	});
});

$('#deliveryMethodSubmitUp').click(function(){
	utag.link({
		"link_text": "delivery_choose_address_top_id1", "event_type" : "delivery_choose_address"
	});
});

$('#deliveryAddressSubmitUp').click(function(){
	utag.link({
		"link_text": "proceed_pay_top_id1", "event_type" : "proceed_pay"
	});
});

$('#deliveryAddressSubmit').click(function(){
	utag.link({
		"link_text": "proceed_pay_bottom_id2", "event_type" : "proceed_pay"
	});
});



/*$('#newAddressButtonUp').click(function(){
	utag.link({
		"link_text": "proceed_pay_top_id1", "event_type" : "proceed_pay"
	});
	utag.link({
			
		"link_text": "proceed_pay_bottom_id2", "event_type" : "proceed_pay"
	});
	
});*/

/*TPR-663 END*/

/*TPR-645 Start*/
$(document).on('click','.jqtree-title.jqtree_common',function(){
	var filter_value= $(this).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['-]/g,"");
	var filter_type=$(this).parents('form').siblings('div.facet-name.js-facet-name.facet_desktop').find('h3').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/'/g,"");
	//var msg = name+"_"+val;
	utag.link({
		link_obj: this,
		link_text: 'search_filter_applied' ,
		event_type : 'search_filter_applied',
		"filter_type" : filter_type,
		"filter_value" : filter_value
	});
	restrictionFlag='true';
})
/*TPR-645 End*/

/*TPR-650 Start*/
$(document).on('mousedown','.owl-prev,.owl-next',function(e){
	var direction='';
	var title='';
	var msg='';
	
	if($(e.currentTarget).hasClass('owl-next')){
		direction="Next";
	}
	else{
		direction="Previous";
	}
	direction = direction.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	
	var pageType=$('#pageType').val();
	//Changes for Analytics Data layer schema changes | PDP
	if(pageType=='product' || pageType=='/sellersdetailpage'){
		title="similar_products";
		msg = title;
	}
	else{
		if($(this).parents('.owl-carousel').parents('.trending').length > 0){
			title=$(this).parents('.owl-carousel').parents('.trending').find('h2>span').text().trim();
		}
		else{
			title=$(this).parents('.owl-carousel').parent('div').find('h2').text().trim();
		}
		title = title.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		msg = (title+"_"+direction);
	}
	
	var msg = (title+"_"+direction);
	if(title){
		utag.link({
			link_obj: this,
			link_text: msg,
			event_type : title+"_navigation_click"
		});
	}
})

/*TPR-650 End*/

/*TPR-691 Starts(Few Functionality)*/
			/*live chat*/
			$(document).on("click","#chatMe",function(){
				utag.link(
						{link_obj: this,link_text: 'support_chat_click_on_chat', event_type : 'support_chat_click'}
						);
			});
			/*call*/
			$(document).on("click", "#callMe", function(e) {
				utag.link(
						{link_obj: this,link_text: 'support_call_click_on_call', event_type : 'support_call_click'}
						);
			});
			/*connect chat*/
			$(document).on("click", "#submitC2C", function(e) {
				//alert("hi");
				  var selectedOption = $('select[name="reason"] option:selected').val();
				  //alert("You have selected the country - " + selectedOption);
				  
				  if(selectedOption=="Order enquiry/ Place or cancel order")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Order_enquiry_Place_or_cancel_order_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Return  Product")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Return_Product_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Refund Enquiry")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Refund_Enquiry_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Product Information")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Product_Information_connect', event_type : 'support_chat_click'}
							  );
					  }
				  else
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Other_Assistance_connect', event_type : 'support_chat_click'}
							  );
					  }
				});
			
			/*cancel call*/
			$(document).on("mousedown", ".bcancel", function(e) {
				var id = $(this).attr('id');
			    //alert(id);
				var selectedOption = $('select[name="reason"] option:selected').val();
				//alert("You have selected the country - " + selectedOption);
				//alert("hi");
				if(id=="call"){
					if(selectedOption=="Order enquiry/ Place or cancel order")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_cancel', event_type : 'support_call_click'}
						  	);
					}
					else if(selectedOption=="Return  Product")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Return_Product_cancel', event_type : 'support_call_click'}
						  	);
					}
					else if(selectedOption=="Refund Enquiry")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Refund_Enquiry_cancel', event_type : 'support_call_click'}
						  	);
					}
					else if(selectedOption=="Product Information")
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Product_Information_cancel', event_type : 'support_call_click'}
							);
					}
					else
					{
						utag.link(
								{link_obj: this,link_text: 'support_call_Other_Assistance_cancel', event_type : 'support_call_click'}
						  	);
					}
				  } 
				if(id=="chat"){
					if(selectedOption=="Order enquiry/ Place or cancel order")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Order_enquiry_Place_or_cancel_order_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Return  Product")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Return_Product_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Refund Enquiry")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Refund_Enquiry_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else if(selectedOption=="Product Information")
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Product_Information_cancel', event_type : 'support_chat_click'}
							  );
					  }
				  else
					  {
					  utag.link(
							  {link_obj: this,link_text: 'support_chat_Other_Assistance_cancel', event_type : 'support_chat_click'}
							  );
					  }
					
					}
				});
			
			/*call generate OTP*/
			$(document).on("click","#generateOTPBtn",function(){
				//alert("hi");
				  var selectedOption = $('select[name="reason"] option:selected').val();
				  //alert("You have selected the country - " + selectedOption);
				  
				  if(selectedOption=="Order enquiry/ Place or cancel order")
				  {
					  utag.link(
						  {link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Return  Product")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Return_Product_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Refund Enquiry")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Refund_Enquiry_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Product Information")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Product_Information_generate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Other_Assistance_generate_otp', event_type : 'support_call_click'}
						  );
				  }
				  
			});
			/*call validate OTP*/
			$(document).on("click","#validateOTPBtn",function(){
				//alert("hi");
				  var selectedOption = $('select[name="reason"] option:selected').val();
				  //alert("You have selected the country - " + selectedOption);
				  
				  if(selectedOption=="Order enquiry/ Place or cancel order")
				  {
					  utag.link(
						  {link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Return  Product")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Return_Product_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Refund Enquiry")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Refund_Enquiry_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else if(selectedOption=="Product Information")
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Product_Information_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			     else
				  {
			    	 utag.link(
						  {link_obj: this,link_text: 'support_call_Other_Assistance_validate_otp', event_type : 'support_call_click'}
						  );
				  }
			});
			
			/*TPR-691 Ends(Few Functionality)*/
			
			/*TPR-675 starts 2nd part*/
			$(document).on("click", ".gig-comments-share-provider-checkbox.gig-comments-checkbox", function(e) {
				var selectedOption = $(this).attr('data-provider'); 
				//alert(selectedOption)
				 if(selectedOption=="twitter")
				  {
					 utag.link({link_text: 'review_social_share_twitter' , event_type : 'review_social_share' ,product_id : productIdArray });
				  }
			     else if(selectedOption=="facebook")
				  {
			    	 utag.link({link_text: 'review_social_share_facebook' , event_type : 'review_social_share' ,product_id : productIdArray });
				  }
				});
			/*TPR-675 ends 2nd part*/ 
			
			

			
			
			/**TPR-654*---ShopByDepartment	*/
			$(document).on('click', 'nav ul li.ShopByDepartmentone div a', function(e){
			//$("nav ul li.ShopByDepartmentone div a").click(function(e) {
				var that = $(this);
				var target = $(e.target);
				var hAr = "";
				var x= $.trim($(".toggle.shop_dept").text().replace(/[\t\n\']+/g,' '));
				x = x.replace(" ","").toLowerCase();
				x = x.replace(" ","_");
				//alert(x);
				
				var y = $.trim($(this).text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
				y = y.replace(/[\s\-]/g,"");
				//alert(y);
				var navigationClick= "top_navigation_click";
				
				if($(target).parent().hasClass("toggle departmenthover L1"))
				          {
					        hAr+= x+">>>>"+ ">>>>"+y;
					utag.link({"link_text":x+"_"+y,"event_type" : navigationClick});
					//alert(hAr);
				          }
				
				if($(target).parent().hasClass("toggle L2"))
				{
					var itsParentL1 = $.trim(that.parents().siblings(".departmenthover.L1").text().replace(/[\t\n\']+/g,' ')).toLowerCase();
					itsParentL1 = itsParentL1.replace(/[\s\-]/g,"");
					hAr+= x+">>>"+">>"+itsParentL1 +" >> "+ y;
					utag.link({"link_text":x+"_"+itsParentL1+"_"+y, "event_type" : navigationClick});
				}
				
				if(that.parent().hasClass("toggle L3")){
					var itsParentL1 =$.trim(that.parents().siblings(".departmenthover.L1").text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
					itsParentL1 = itsParentL1.replace(/[\s\-]/g,"");
					var itsParentL2 = $.trim(that.parent().parent().prevAll("li.short.words:first").text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
					itsParentL2 = itsParentL2.replace(/[\s\-]/g,"");
					console.log("dress")
					
					hAr+= x+">>>>" +">>>>>"+itsParentL1 +" >> "+ ">>"+itsParentL2 +$(this).text();
					utag.link({"link_text":x+"_"+itsParentL1+"_"+itsParentL2+"_"+y,"event_type" : navigationClick});
				}
				//console.log(hAr);
				//alert(hAr);
			});
			
			/*TPR-654*/	
						
			
			$(document).on('click', 'nav ul li.ShopByBrand ul li.short.images a', function(e){
				 var navigationClick= "top_navigation_click";
				 var lastItem = $.trim($(this).text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
				 lastItem = lastItem.replace(/[\s]/g,"");
				 //alert(lastItem);
				
				 var parentItem = '';
				 if ($(this).parents().hasClass('lazy-brands')) {
					 var parentObj = $(this).closest('.lazy-brands');
					 if (parentObj.find('.toggle.brandClass').length) {
						 parentItem = $.trim(parentObj.find('.toggle.brandClass').text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase(); 
					 }
					 else if(parentObj.find('.toggle.A-ZBrands').length){
						 parentItem = $.trim(parentObj.find('.toggle.A-ZBrands').text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase(); 
					 }
					 parentItem = parentItem.replace(/[\s]/g,"");
					 //alert(parentItem);
				 }
				 
				 var grandParentItem = $.trim($('div.toggle.shop_brand').text().replace(/[\t\n\']+/g,' ')).toLowerCase();
				 grandParentItem = grandParentItem.replace(" ","").toLowerCase();
				 grandParentItem = grandParentItem.replace(" ","_");
				 //alert(grandParentItem);
				 
				 utag.link({"link_text":grandParentItem+"_"+parentItem+"_"+lastItem,"event_type" : navigationClick});
			});

			 $('nav ul li.ShopByBrand div a').on('click', function(){
				 var navigationClick= "top_navigation_click";
				 var lastItem = $.trim($(this).text().replace(/[\t\n\'\-]+/g,' ')).toLowerCase();
				 lastItem = lastItem.replace(/[\s]/g,"");
				 //alert(lastItem);
		
				 var parentItem = $.trim($('div.toggle.shop_brand').text().replace(/[\t\n\']+/g,' ')).toLowerCase();
				 parentItem = parentItem.replace(" ","").toLowerCase();
				 parentItem = parentItem.replace(" ","_");
				 //alert(parentItem);
				 
				 utag.link({"link_text":parentItem+"_"+lastItem,"event_type" : navigationClick});
			 }); 

			 
/* Data Layer Schema Changes Starts*/

/*Thumbnail tracking*/
//$(document).on("click",".product-image-container .imageListCarousel .thumb",function(){
$(document).on("click",".product-info > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
	var thumbnail_value = $(this).parent().attr('class');
	var thumbnail_type = $(this).find('img').attr('data-type');
	utag.link({
		"link_text":"pdp_"+thumbnail_value+"_clicked",
		"event_type":"pdp_"+thumbnail_type+"_clicked",
		"thumbnail_value":thumbnail_value,
		product_id : productIdArray
	});
})

/*Product Specification*/
$(document).on("click",".nav-wrapper .nav.pdp",function(){
	utag.link({"link_text":"product_specification", "event_type":"view_prod_specification",product_id : productIdArray});
})

/*Out Of Stock During adding to bag*/
function errorAddToBag(errorMessage){
	utag.link({"error_type":errorMessage});
}

/*On Size selection | PDP*/
$(document).on('click',".variant-select > li", function(){
	var product_size = $(this).find('a').html();
	utag.link({
		"link_text":"pdp_size_"+product_size,
		"event_type":"pdp_size_selected",
		"product_size":product_size,
		product_id : productIdArray
	});
})


/*On Colour selection | PDP*/
$(document).on('click',".color-swatch > li", function(){
	var product_color = $(this).find('img').attr('title').toLowerCase();
	utag.link({
		"link_text":"pdp_color_"+product_color,
		"event_type":"pdp_color_selected",
		"product_color":product_color,
		product_id : productIdArray
	});
})

/*TPR-4803| hot now | homepage*/
$(document).on('click','#ia_products_hot .hotShowHide',function(){
	if(typeof utag !="undefined"){
		 utag.link({ link_text : 'shop_the_hotlist_clicked' , event_type : 'shop_the_hotlist_clicked' });
	 }	
})


/*TPR-4803|Hot now home page,pdp recommendations generic method|*/
$(document).on("click",".trending .owl-stage .owl-item",function(){
	var productID='';
	var url = $(this).find('a').attr('href');
	if(typeof(url) != "undefined"){
		url = url.split('/p/');
		var productIDlist = url[1].split('/');
		productID=productIDlist[0]
	}
	var productArray=[];
	productArray.push(productID);
	
	
	if($('#pageType').val() == "product" || $('#pageType').val() == "/sellersdetailpage"){
	utag.link({
			link_text: "similar_products_clicked",
			event_type : "similar_products_clicked",
			similar_product_id : productArray
			
		});
      }
	
	if($('#pageType').val() == "homepage"){
		utag.link({
			link_text: "hotnow_product_clicked",
			event_type : "hotnow_product_clicked",
			product_id : productArray
			
		});
	}
})

/*PDP Add to bag & Buy Now*/
function utagAddProductToBag(triggerPoint,productCodeMSD){
	    var productCodeArray=[];
	    var productcode='';
		var pageName='';
		var pageType = $('#pageName').val();
		if( pageType == "Product Details"){
			pageName="pdp";
			productCodeArray.push(productCodeMSD);
		}
		if(  pageType == "View Seller Page"){
			pageName="pdp";
			 productCode= $('#productCode').val();
			 productCodeArray.push(productCode);
		}

	utag.link({

		link_text: triggerPoint ,
		event_type : triggerPoint+"_"+ pageName,
		product_sku : productCodeArray,		// Product code passed as an array for Web Analytics - INC_11511  fix
		product_id :  productCodeArray
	});
}


/*TPR-4740  Continue Shopping | Cart  */
$(document).on("click",".continue-shopping.desk-view-shopping",function() {
	utag.link({
		link_text: "continue_shopping_clicked",
		event_type : "continue_shopping_clicked"
	});
})

//TPR-4739 | Expresscheckout | cart
$(document).on("click","#expressCheckoutButtonId",function(){
	utag.link({
		link_text: "cart_express_checkout_button_start",
		event_type : "cart_express_checkout_button_start"
	});
})

 /*TPR-4745  | Add New Address | Checkout */
$(document).on('click','.pincode-button',function(){
				 
utag.link({ link_text : 'add_new_address_clicked' ,event_type : 'add_new_address_clicked'});
})

/*TPR-4687 | Broken Image*/
$(window).load(function() {
	var brokenImageCount=0;	
	$('.mainContent-wrapper img').each(function(){
		var url = $(this).attr('src');
		if(url){
			if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
			      // image is broken
			    	brokenImageCount++;
			    }
		}

	});
	if(brokenImageCount > 0){
		var msg = brokenImageCount+" broken_image_found";
		utag.link({ 
			error_type : msg
		});
	}
});

/*TPR-4736|checkout|cart start*/
function pincodeServicabilityFailure(selectedPincode){
	if(typeof utag !="undefined")
		{
			//TPR-4736 | DataLAyerSchema changes | cart
			utag.link({

				"link_text": "cart_pincode_check_failure", 
				"event_type" : "cart_pincode_check_failure",
				"cart_pin_non_servicable" : selectedPincode
			});
		}
	
}

function pincodeServicabilitySuccess(selectedPincode){
	if(typeof utag !="undefined")
	{
		//TPR-4736 | DataLAyerSchema changes | cart
		utag.link({

			"link_text": "cart_pincode_check_success", 
			"event_type" : "cart_pincode_check_success",
			"cart_pin_servicable" : selectedPincode
		});
	}
}
/*TPR-4736|checkout|cart ends*/

/*Homepage changes starts*/	

/*TPR-4797 | Brand Studio changes Start*/
$(document).on("click", ".home-brands-you-love-carousel-brands", function() {
	var text = $(this).find('img').attr('alt');
	if(text != ""){
		text = $(this).find('img').attr('alt').toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		//TISQAEE-59
		var header = $('#brandsYouLove').find('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		utag.link({
			link_text: text,
			event_type : header+"_brand_clicked",
		});
	}
})


$(document).on('click','.home-brands-you-love-side-image',function(){
	var productName=$(this).find('.product-name').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var header = $('#brandsYouLove').find('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : productName, 
			event_type : header+"_clicked", 
		});
	} 
})

$(document).on('click','#brandsYouLove .visit-store',function(){
	var brandName ='';
	var header = $('#brandsYouLove').find('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	$('#brandsYouLove').find('.home-brands-you-love-carousel-brands.item').each(function(){
		if($(this).parent().hasClass('center')){
			brandName = $(this).find('img').attr('alt').toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		}
	})
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : brandName, 
			event_type : header+"_visit_store"
		});
	}
})
/*TPR-4797 | Brand Studio changes End*/

/*TPR-4798 | Top Deals changes start*/
$(document).on('click','.home-best-pick-carousel .owl-item a',function(){
	var productName = '';
	var header = $(this).parents('#bestPicks').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	$(this).find('.short-info span').each(function(){
		if(productName == ''){
			productName = $(this).text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		}
		else{
			productName = productName +"_"+ $(this).text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		}
	})
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : productName, 
			event_type : header+"_image_clicked"
		});
	}
})
/*TPR-4798 | Top Deals changes end*/

/*TPR-4807 | Homepage banner ads*/
$(document).on('click','.home-rotatingImage .owl-item img',function(){
	var bannerName=$(this).attr('alt').trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : 'banner_ad_'+bannerName, 
			event_type : 'banner_ad_clicked', 
		});
	}
})


 /*TPR-4805 | Inspire me changes start*/
 $(document).on('click','#showcase .button.maroon.btn-red',function(){
 	var header = $(this).parents('#showcase').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : header+"_read_story_clicked", 
 			event_type : header+"_read_story_clicked"
 		});
 	}
 })
 
 $(document).on('click','#showcase .button.trending-button.btn-trans',function(){
 	var header = $(this).parents('#showcase').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : header+"_read_more_clicked", 
 			event_type : header+"_read_more_clicked"
 		});
 	}
 })
 /*TPR-4805 | Inspire me changes end*/
 
 /*TPR-4800 | What to buy now changes start*/
 $(document).on('click','.home-product-you-care-carousel .owl-item a',function(){
 	var categoryName = $(this).find('.product-name > span').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	var header = $(this).parents('#productYouCare').children('h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : header+"_"+categoryName+"_clicked", 
 			event_type : header+"_"+categoryName+"_clicked"
 		});
 	}
 })
 /*TPR-4800 | What to buy now changes end*/
 
 
 /*TPR-4796 | Homepage banner carousel start*/
 $(document).on('click', '.home-rotatingImage .owl-dot', function(){
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : 'home_carousel_clicked', 
 			event_type : 'home_carousel_clicked'
 		});
 	}
 })
 /*TPR-4796 | Homepage banner carousel end*/
 
 /*	TPR-4802|stay qued|homepage*/
 $(document).on('click','.qued-content a',function(){
 
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'read_the_story_clicked' , event_type : 'read_the_story_clicked'});
 	 }
 })
 
 /*	TPR-4801 | new in| homepage start*/
 $(document).on('click','.new_exclusive_viewAll', function(){
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'new_in_view_all_clicked' , event_type : 'new_in_view_all_clicked'});
 	 }	
 })
 
 $(document).on('click','#new_exclusive .newExclusiveElement a', function(){
 	var productName = $(this).find('.New_Exclusive_title').text();
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : productName , event_type : 'new_in_clicked' });
 	 }	
 	
 })
 /*	TPR-4801 | new in| homepage ends*/
 
 /*TPR-5062 Start*/
 $(document).on("click",".download-app",function(){
 	if(typeof utag !="undefined"){
 	utag.link({
 		"link_text": "download_app", "event_type" : "download_app_clicked"});
 	}
 });
 
 $(document).on("click",".store-locator-header",function(){
 	if(typeof utag !="undefined"){
 	utag.link({
 		"link_text": "our_stores", "event_type" : "our_stores_clicked"});
 	}
 });
 
 $(document).on("click",".tracklinkcls",function(){
 	if(typeof utag !="undefined"){
 	utag.link({
 		"link_text": "user_notifications", "event_type" : "user_notifications_clicked"});
 }
 });
 
 $(document).on("click",".logged_in.dropdown.ajaxloginhi",function(){
 if(typeof utag !="undefined"){
 utag.link({
 	"link_text": "my_account", "event_type" : "my_account_clicked"});
 }
 });
 
 $(document).on("click",".wishlist",function(){
 if(typeof utag !="undefined"){
 utag.link({
 	"link_text": "my_wishlists", "event_type" : "my_wishlists_clicked"});
 }
 });
 
 $(document).on("click",".bag",function(){
 if(typeof utag !="undefined"){
 utag.link({
 	"link_text": "my_bag", "event_type" : "my_bag_clicked"});
 }
 });
 /*TPR-5062 End*/
 
 /*TPR-4799 | offers page changes | Start*/
 /*Top Brands|Offer page*/
 $(document).on('click','.clp_top_brands .top_brands_section a',function(){
 	var sectionName = $(this).parents('.top_brands').find('span > span').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }	
 })
 /*Top offers|Offer page*/
 $(document).on('click','.top_categories',function(){
 	var sectionName = $(this).find('span > span').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }	
 })
 /*Half price Store|Offer page*/
 $(document).on('click','.clp_winter_launch_wrapper',function(){
 	var sectionName = $(this).parents('.winter_launch').find('.winter_launch_section .content').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }
 })
 /*under 999 store|Offer page*/
 $(document).on('click','.shop_for',function(){
 	var sectionName = $(this).find('.yCmsComponent.shop_for_component .content p').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		 utag.link({ link_text : 'offers_'+sectionName+'_clicked' , event_type : 'offers_'+sectionName+'_clicked' });
 	 }
 })
 
 
 /*Top Deals changes*/
 $(document).on('click','.top_deal',function(){
 	var header = $(this).find('.feature-collections > h2').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : "offers_"+header+"_clicked", 
 			event_type : "offers_"+header+"_clicked"
 		});
 	}
})

//Best Seller changes start
$(document).on('mousedown','.best_seller .owl-prev,.owl-next',function(e){
	var direction='';
	if($(e.currentTarget).hasClass('owl-next')){
		direction="Next";
	}
	else{
		direction="Previous";
	}
	direction = direction.toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	var title=$(this).parents('.best_seller').find('.content > p').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : title+'_'+direction, 
			event_type : title+'_navigation_click'
		});
	}
})
 
 $(document).on("click", ".best_seller .Menu li", function() {
 	var text = $(this).text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	var title=$(this).parents('.best_seller').find('.content > p').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : text, 
 			event_type : title+'_menu_clicked'
 		});
 	}
 })
 
 $(document).on('click','.best_seller .js-owl-carousel .owl-item a',function(){
 	var productName=$(this).find('img').attr('title').toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	var title=$(this).parents('.best_seller').find('.content > p').text().trim().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	if(typeof utag !="undefined"){
 		utag.link({ 
 			link_text : productName, 
 			event_type : title+'_product_clicked', 
 		});
 	} 
 })
 
 //Best Seller changes end
 /*TPR-4799 | offers page changes | End*/ 
  /*Homepage changes Ends*/
  

/*Order History page changes Start*/
/*TPR-4751 & 4753 | Order history and track order changes*/
$(document).on('click','.pagination_ul .header .links a',function(){
	var message='';
	if($(this).attr('href').indexOf('viewOrder') >0){
		message='view_order_details';
	}
	else if($(this).attr('href').indexOf('trackOrder') >0){
		message='track_order_details';
	}
	if(typeof utag !="undefined"){
		utag.link({ 
			link_text : message, 
			event_type : message 
		});
	}
})

/*TPR-4752 |cancel order*/
$(document).on('click','.pagination_ul .item .actions a',function(){
if(typeof utag !="undefined"){
	 utag.link({ link_text : 'cancel_order' , event_type : 'cancel_order_clicked' });
}
})

/*TPR-4754|invite friends start*/
$(document).on('click','#lnInvite',function(){
if(typeof utag !="undefined"){
	 utag.link({ link_text : 'invite_your_friends_start' , event_type : 'invite_your_friends_start' });
}
})


/*TPR-4754|invite friends end*/
/*Order History page changes End*/
/*TPR-4731 | Brand page changes | Start*/
 $(document).on('click','.call-to-action-link',function(){
 	if($('body').hasClass('pageType-ContentPage') && $('#ia_category_code').val() != '' && $('#ia_category_code').val().toLowerCase().indexOf('mbh') != -1){
 		var pageName = $('#pageName').val();
 		var brandName='';
 		if(typeof pageName != undefined && pageName != ''){
 			pageName=pageName.split('BrandStore-');
 			brandName = pageName[1].trim();
 		}
 		var linkName = $(this).text().trim();
 		var linkText = $(this).parents('.call-to-action-parent').find('.call-to-action-name').text().trim();
 		var msg = '';
 		if(brandName != undefined && brandName != ''){
 			msg = brandName +"_";
 		}
 		if(linkText != undefined && linkText != ''){
 			msg = msg+"_"+linkText + "_";
 		}
 		if(linkName != undefined && linkName != ''){
 			msg = msg + linkName + "_clicked";
 		}
 		msg = msg.toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 		if(msg != undefined && msg != '' && typeof utag != undefined){
 			utag.link({
 				link_text: msg,
 				event_type : msg
 			});
 		}
 	}
 })
 
 
 $(document).on('click','.call-to-action-banner',function(){
 	if($('body').hasClass('pageType-ContentPage') && $('#ia_category_code').val() != '' && $('#ia_category_code').val().toLowerCase().indexOf('mbh') != -1){
 		var pageName = $('#pageName').val();
 		var brandName='';
 		if(typeof pageName != undefined && pageName != ''){
 			pageName=pageName.split('BrandStore-');
 			brandName = pageName[1].trim();
 		}
 		var bannerName = $(this).attr('alt').trim();
 		
 		var msg = '';
 		if(brandName != undefined && brandName != ''){
 			msg = brandName + "_";
 		}
 		if(bannerName != undefined && bannerName != ''){
 			msg = msg + bannerName + "_clicked";
 		}
 		msg = msg.toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 		if(msg != undefined && msg != '' && typeof utag != undefined){
 			utag.link({
 				link_text: msg,
 				event_type : "brand_banner_clicked"
 			});
 		}
 	}
 })
 
 
 function brandSubCategoryPageCheck(){
 	var brandName='';
 	var subCategoryName = '';
 	if(typeof(Storage) !== "undefined") {
 		if(localStorage.getItem("brandName") != null){
 			brandName = localStorage.getItem("brandName");
 			localStorage.removeItem("brandName");
 		}
 	}
 	if($(".facet-list.filter-opt").find('.Brand') && $(".facet-list.filter-opt").find('.Brand').next().val()){
 		if($(".facet-list.filter-opt").find('.Brand').next().val().toLowerCase() == brandName){
 			if(typeof brandName != undefined && brandName!= ''){
 				if($('#product_category').val() != undefined && $('#product_category').val() != ''){
 					subCategoryName = brandName +"_"+$('#product_category').val().trim();
 				}
 				if($('#page_subcategory_name').val() != undefined && $('#page_subcategory_name').val() != ''){
 					subCategoryName = subCategoryName +"_"+$('#page_subcategory_name').val().trim();
 				}
 				if($('#page_subcategory_name_l3').val() != undefined && $('#page_subcategory_name_l3').val() != ''){
 					subCategoryName = subCategoryName +"_"+$('#page_subcategory_name_l3').val().trim();
 				}
 			}
 		}
 	}
 	return subCategoryName;
 }
 /*TPR-4731 | Brand page changes | End*/ 

/* Data Layer Schema Changes Ends*/

//Moved tealium on-load call to function so that it can be re-used.
function tealiumCallOnPageLoad()
{

    var UTAG_SCRIPT_PROD = "<script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/prod/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script>";
	
	var UTAG_SCRIPT_DEV = "<script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/dev/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script>";
	var session_id = ACC.config.SessionId;
	
	
	var visitor_ip = ACC.config.VisitorIp;
	
	var user_type = $.cookie("mpl-userType");
	var user_id = $.cookie("mpl-user");
	var site_region = 'en';
	var site_currency ='INR';
	var domain_name = document.domain;
	
	var pageType = $('#pageType').val();
	var pageName=$('#pageName').val();
	// TPR-668
	var user_login_type = $('#userLoginType').val().trim();
	
	//TPR-672 START
	var promo_title='';
	var promo_id='';
	
	if($("#product_applied_promotion_title").val() && $("#product_applied_promotion_code").val() !=undefined)
	{
		
	promo_title=$("#product_applied_promotion_title").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
	promo_id=$("#product_applied_promotion_code").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
	}
	//TPR-672 END
	
	//TPR-4726
	var offerCount = '';
	if( $('.on-sale').length > 0 ){
		offerCount =	$('.on-sale').length ;
	}
	
	var newCount = '';
	if( $('.new').length > 0 ){
		newCount =		$('.new').length ;
	}

	var onlineExclusive='';
	if($('.online-exclusive').length > 0){
		onlineExclusive = $('.online-exclusive').length ;
	}
	//Web Thumbnail Images
	var thumbnailImageCount=0;
	var pdp_video_product_id;
	$(".product-info > .product-image-container > .productImageGallery .imageListCarousel").find("li").each(function(){
		thumbnailImageCount++;
		if($(this).find('img').attr('data-type') == 'video'){
			pdp_video_product_id=$('#product_id').val();
		}
	})
	
	// Added for tealium
	if (pageType == "homepage") {
		
	
		// Added for tealium
		/*$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataHome",
					type : 'GET',
					cache : false,
					success : function(data) {
						// console.log(data);
						$('#tealiumHome').html(data);
					}
				});*/
		// Added for TISPT-324
		
		
		
		var pageTypeHome = 'home';
		var site_section = 'home';
		var homePageTealium = '';
		//TPR-430
		var product_category = null;
		var page_subcategory_name = null;
		var page_subcategory_name_L3 = null;
		
		homePageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_login_type":"'+user_login_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeHome+'","page_name":"'+pageName+'","product_category":"'+product_category+'","page_subcategory_name":"'+page_subcategory_name+'","page_subcategory_name_L3":"'+page_subcategory_name_L3+'", "session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
		var script="";
		if(domain_name =="www.tatacliq.com"){
			
			script=UTAG_SCRIPT_PROD;
		}
		else{
			
			script=UTAG_SCRIPT_DEV;
		}
		homePageTealium+=script;
		$('#tealiumHome').html(homePageTealium);
	}
	// Tealium end

	if (pageType == "product"
			|| pageType == "/sellersdetailpage") {
		// Added for tealium
		$.ajax({
			url : ACC.config.encodedContextPath
					+ "/getTealiumDataProduct",
			type : 'GET',
			cache : false,
			success : function(data) {
				
				var tealiumData = "";
				tealiumData += ',"user_login_type":"'	//TPR-668
					+ user_login_type + '",';
				tealiumData += '"product_mrp":["'
						+ $("#product_unit_price").val() + '"],';
				tealiumData += '"site_section":"'
						+ $("#site_section").val() + '",';
				tealiumData += '"product_mop":["'
						+ $("#product_list_price").val() + '"],';
				tealiumData += '"product_discount":["'
					+ $("#product_discount").val() + '"],';
				tealiumData += '"product_discount_percentage":"'
					+ $("#product_discount_percentage").val() + '%",';
				tealiumData += '"product_name":["'
						+ $("#product_name").val() + '"],';
				tealiumData += '"product_sku":["'
						+ $("#product_sku").val() + '"],';
				if ($("#page_category_name").val() != 'undefined' && $("#page_category_name").val() != ''){
					tealiumData += '"page_category_name":"'
						+ $("#page_category_name").val() + '",';
				}
				if ($("#page_section_name").val() != 'undefined' && $("#page_section_name").val() != ''){
					tealiumData += '"page_section_name":"'
						+ $("#page_section_name").val() + '",';
				}
				tealiumData += '"page_name":"' + $("#page_name").val()
						+ '",';
				if(typeof(promo_title) != 'undefined' && promo_title !=''){ 
					tealiumData += '"offer_title":"'       //added for analytics schema
						+ promo_title + '",';
				}
				tealiumData += '"product_id":["'
						+ $("#product_id").val() + '"],';
				
				//TPR-430 Start
				if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
				tealiumData += '"product_category":'
					+ getListValue("product_category") + ',';            /*value passed as array instead of single string  INC_11511*/
					/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
				}
				if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
				tealiumData += '"page_subcategory_name":"'
						+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
				}
				if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
				tealiumData += '"page_subcategory_name_L3":"'
					+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",';
				}
				//TPR-430 End
				//For KIDSWEAR L4 needs to be populated 
				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
					tealiumData += '"page_subcategory_name_L4":"'
						+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '",';
					}
				tealiumData += '"product_brand":["'
						+ $("#product_brand").val() + '"],';
				tealiumData += '"site_section_detail":"'
						+ $("#site_section_detail").val() + '",';
				//TPR-672 START
				if(typeof(promo_title) != 'undefined' && promo_title !=''){ 
				tealiumData += '"promo_title":["'
					+promo_title+ '"],';
				}
				if(typeof(promo_id) != 'undefined' && promo_id !=''){ 
				tealiumData += '"promo_id":["'
					+promo_id+ '"],';
				}
				//TPR-672 END
				
				//Data Layer Schema changes
				tealiumData += '"product_stock_count":["'
					+ $("#product_stock_count").val() + '"],';
				tealiumData += '"out_of_stock":["'
					+ $("#out_of_stock").val() + '"],';
				tealiumData += '"product_image_count":"'
					+ thumbnailImageCount + '",';
				if (typeof(pdp_video_product_id) != 'undefined' || pdp_video_product_id != null){
					tealiumData += '"pdp_video_product_id":["'
						+ pdp_video_product_id + '"],';
				}
				
				//TPR-429 START
				tealiumData += '"seller_id":"'				//variable name changed | Data Layer Schema Changes 
					+ $("#pdpBuyboxWinnerSellerID").val() + '",';
				tealiumData += '"seller_name":"'
					+ $("#sellerNameId").html() + '",';
				if($("#pdpOtherSellerIDs").val() != 'undefined' && $("#pdpOtherSellerIDs").val() !=''){ 
					tealiumData += '"other_seller_ids":"'
					+ $("#pdpOtherSellerIDs").val() + '",';
				}
				//TPR-429 END
				
				//TPR-4688
				var sizeVariantList=$('#variant').find('li');
				if(sizeVariantList.length > 0){
					tealiumData += '"size_variant_count":"'
						+ sizeVariantList.length + '",';
				}
				
				//TPR-4692 | Breadcrumb 
				var breadcrum=[];
				$('.breadcrumbs.wrapper').find('li:not(.active)').each(function(){
					breadcrum.push($(this).find('a').text().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""));
				})
				if(typeof(breadcrum) != 'undefined' || breadcrum != null){
					for(var i=0;i<breadcrum.length;i++){
						if(i!=0){
							var fieldName = "page_subcategory_L"+i;
							tealiumData += '"'+fieldName+'":"'
								+ breadcrum[i] + '",';
						}
					}
					tealiumData += '"product_display_hierarchy":"'
						+ breadcrum + '"}';
				}
				data = data.replace("}<TealiumScript>", tealiumData);
				// console.log(data);
				
				$('#tealiumHome').html(data);
				var productCodeArray = [];
 				productCodeArray.push($("#product_id").val());
 				productIdArray = productCodeArray;
			}
		});
	}

	// Generic Page Script
	
	if (pageType != 'homepage' && pageType != 'product'
			&& pageType != '/sellersdetailpage' && pageType != 'productsearch'
			&& pageType != 'category' && pageType != 'cart'
			&& pageType != 'multistepcheckoutsummary'
			&& pageType != 'profile' 
			&& pageType != 'orderconfirmation'
			&& pageType != 'notfound'
			&& pageType != 'businesserrorfound'
			&& pageType != 'nonbusinesserrorfound'
			&& pageType != 'productsearch') {
		
		// Added for tealium
		/*$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataGeneric",
					type : 'GET',
					data:'pageName='+pageName,
					cache : false,
					success : function(data) {
						// console.log(data);
						$('#tealiumHome').html(data);
					}
				});*/
	
		var pageTypeGeneric = 'generic';
		var site_section = pageName;
        var genericPageTealium = '';
        //TPR-430
    	if($("#product_category").val() !=undefined){
        var product_category = $("#product_category").val().replace(/_+/g, '_');
        }
    	if($("#product_category").val() == undefined){
            var product_category = null;
            }
    	var page_subcategory_name = null;
    	var page_subcategory_name_l3 = null;
		
        genericPageTealium+='<script type="text/javascript"> var utag_data ={"site_region":"'+site_region+'","user_type":"'+user_type+'","user_login_type":"'+user_login_type+'","user_id":"'+user_id+'","page_type":"'+pageTypeGeneric+'","page_name":"'+pageName+'","product_category":"'+product_category+'","page_subcategory_name":"'+page_subcategory_name+'","page_subcategory_name_L3":"'+page_subcategory_name_l3+'","session_id":"'+session_id+'","visitor_ip":"'+visitor_ip+'","site_currency":"'+site_currency+'","site_section":"'+site_section+'","IA_company":"'+domain_name+'"}</script>';
		var script="";
		if(domain_name =="www.tatacliq.com"){
			
			script=UTAG_SCRIPT_PROD;
		}
		else{
			
			script=UTAG_SCRIPT_DEV;
		}
		genericPageTealium+=script;
		$('#tealiumHome').html(genericPageTealium);
	}
	// Tealium end
	
	// Added for tealium
	if (pageType == "category") {
		// Added for tealium
		$
				.ajax({
					url : ACC.config.encodedContextPath
							+ "/getTealiumDataCategory",
					type : 'GET',
					cache : false,
					success : function(data) {
						// console.log(data);
						var tealiumData = "";
						tealiumData += ',"user_login_type":"'	//TPR-668
							+ user_login_type + '",';
						tealiumData += '"page_category_name":"'
								+ $("#page_category_name").val() + '",';
						tealiumData += '"site_section":"'
								+ $("#site_section").val() + '",';
						tealiumData += '"page_name":"'
							+ $("#page_name").val() + '",';
						tealiumData += '"categoryId":"'
							+ $("#categoryId").val() + '",';
						if($("#out_of_stock_count").val() != undefined  && $("#out_of_stock_count").val() != null && $("#out_of_stock_count").val() != ''){
						tealiumData += '"out_of_stock_totalcount":"'		// TPR-4707
							+ $("#out_of_stock_count").val() + '",';
						}
						if(offerCount != undefined && offerCount != null && offerCount != ''){ 
						tealiumData += '"offer_product_count":"'		// TPR-4714
							+ offerCount + '",';
						}
						if(newCount != undefined && newCount != null && newCount != ''){ 
							tealiumData += '"new_product_count":"'		// TPR-4714
								+ newCount + '",';
							}
						if(onlineExclusive != undefined && onlineExclusive != null && onlineExclusive != '' ){ 
							tealiumData += '"onlineExclusive_product_count":"'		// TPR-4726
								+ onlineExclusive + '",';
							}
						var brand_sub_category = brandSubCategoryPageCheck();
 						if(brand_sub_category != undefined && brand_sub_category != null && brand_sub_category != ''){ 
 							tealiumData += '"brand_sub_category":"'
 								+ brand_sub_category + '",';
 							}
						/*TPR-430 Start*/
						if($("#product_category").val() != undefined && $("#product_category").val() != null && $("#product_category").val() != ''){ 
						tealiumData += '"product_category":'
							+ getListValue("product_category") + ',';                /*value passed as array instead of single string  INC_11511*/
							/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
						}
						if($("#page_subcategory_name").val() != undefined && $("#page_subcategory_name").val() != null && $("#page_subcategory_name").val() != ''){ 
						tealiumData += '"page_subcategory_name":"'
							+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
						}
						if($("#page_subcategory_name_l3").val() != undefined && $("#page_subcategory_name_l3").val() != null){ 
						tealiumData += '"page_subcategory_name_L3":"'
							+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '"}';
						}
						/*TPR-430 End*/
						data = data.replace("}<TealiumScript>", tealiumData);
						$('#tealiumHome').html(data);
					}
				});
	}
	// Tealium end
	
	//Search
	if(pageType == "productsearch"){
		$
		.ajax({
			url : ACC.config.encodedContextPath
					+ "/getTealiumDataSearch",
			type : 'GET',
			cache : false,
			success : function(data) {
				// console.log(data);
				var tealiumData = "";
				//TPR-430
				var product_category = null;
				var page_subcategory_name = null;
				var page_subcategory_name_L3 = null;
				tealiumData += ',"user_login_type":"'	//TPR-668
					+ user_login_type + '",';
				tealiumData += '"search_keyword":"'
						+ $("#search_keyword").val() + '",';
				tealiumData += '"searchCategory":"'
						+ $("#searchCategory").val() + '",';
				tealiumData += '"page_name":"'
					+ $("#page_name").val() + '",';
				tealiumData += '"search_results":"'
					+ $("#search_results").val() + '",';
				tealiumData += '"search_type":"'		// TPR-666
					+ $("#search_type").val() + '",';
				if($("#out_of_stock_count").val() != undefined  && $("#out_of_stock_count").val() != null && $("#out_of_stock_count").val() != ''){
				tealiumData += '"out_of_stock_count":"'		// TPR-4722
					+ $("#out_of_stock_count").val() + '",';
				}
				if(offerCount != undefined && offerCount != null && offerCount != ''){ 
				tealiumData += '"offer_product_count":"'		// TPR-4726
					+ offerCount + '",';
				}
				if(newCount != undefined && newCount != null && newCount != ''){ 
					tealiumData += '"new_product_count":"'		// TPR-4726
						+ newCount + '",';
					}
				if(onlineExclusive != undefined && onlineExclusive != null && onlineExclusive != '' ){ 
					tealiumData += '"onlineExclusive_product_count":"'		// TPR-4726
						+ onlineExclusive + '",';
					}
				//TPR-430 Start
				if(page_subcategory_name != undefined && page_subcategory_name != null && page_subcategory_name != ''){ 
				tealiumData += '"product_category":"'
					+ product_category + '",';
				}
				if(page_subcategory_name != undefined && page_subcategory_name != null && page_subcategory_name != ''){ 
				tealiumData += '"page_subcategory_name":"'		
					+ page_subcategory_name +'",';
				}
				tealiumData += '"page_subcategory_name_L3":"'		
					+ page_subcategory_name_L3 +'"}';
				
				data = data.replace("}<TealiumScript>", tealiumData);
				$('#tealiumHome').html(data);
			}
		});
		
	}
	
	if(pageType =="cart"){
		$
		.ajax({
			url : ACC.config.encodedContextPath
					+ "/getTealiumDataCart",
			type : 'GET',
			cache : false,
			success : function(data) {
				var qtyUpdated;
				if(window.sessionStorage.getItem("qtyUpdate")!=null){
					qtyUpdated=window.sessionStorage.getItem("qtyUpdate");
				}
				else{
					qtyUpdated=false;
				}
				var tealiumData = "";
				tealiumData += ',"user_login_type":"'	//TPR-668
					+ user_login_type + '",';
				tealiumData += '"cart_total":"'
						+ $("#cart_total").val() + '",';
				tealiumData += '"product_unit_price":'
						+ $("#product_unit_price").val() + ',';
				tealiumData += '"product_list_price":'
					+ $("#product_list_price").val() + ',';
				tealiumData += '"product_name":'
					+ $("#product_name").val() + ',';
				tealiumData += '"product_quantity":'
					+ $("#product_quantity").val() + ',';
				tealiumData += '"adobe_product":"'
					+ $("#adobe_product").val() + '",';
				tealiumData += '"product_sku":'
					+ $("#product_sku").val() + ',';
				tealiumData += '"product_id":'
					+ $("#product_id").val() + ',';
				tealiumData += '"product_brand":'
					+ $("#product_brand").val() + ',';				
				tealiumData += '"product_quantity_update":'
					+ qtyUpdated + ',';			
				tealiumData += '"checkout_seller_ids":"'		//for TPR-429
					+ $("#checkoutSellerIDs").val() + '",';
				//L1 L2 L3 For cart  TPR-4831
				if($("#page_subcategory_L1").val() !=undefined || $("#page_subcategory_L1").val() !=null){ 
					tealiumData += '"page_subcategory_L1":'
						+ getListValue("page_subcategory_L1") + ',';           
					}
				if($("#page_subcategory_L2").val() !=undefined || $("#page_subcategory_L2").val() !=null){ 
					tealiumData += '"page_subcategory_L2":'
						+ getListValue("page_subcategory_L2") + ',';            
					}
				if($("#page_subcategory_l3").val() !=undefined || $("#page_subcategory_l3").val() !=null){ 
					tealiumData += '"page_subcategory_l3":'
						+ getListValue("page_subcategory_l3") + ',';            
					}
				//added for kidswear:L4 needs to be populated
				if($("#page_subcategory_l4").val() !=undefined || $("#page_subcategory_l4").val() !=null){ 
					tealiumData += '"page_subcategory_l4":'
						+ getListValue("page_subcategory_l4") + ',';            
					}
				//L1 L2 L3 cart ends
				//TPR-430 Start
				if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
					tealiumData += '"product_category":'
					+ getListValue("product_category") + ',';	/*value passed as array instead of single string  INC_11511*/
					/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
				}
				if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
					tealiumData += '"page_subcategory_name":"'
					+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
				}
				if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
					tealiumData += '"page_subcategory_name_L3":"'
				+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",'; //TISPRDT-1462
				}
				//added for kidswear:L4 needs to be populated
				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
					tealiumData += '"page_subcategory_name_L4":"'
				+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '"}';
				}
			//TPR-430 End
				data = data.replace("}<TealiumScript>", tealiumData);
				$('#tealiumHome').html(data);
			}
		});
	}
	
	if(pageType =="multistepcheckoutsummary"){
		var checkoutPageName=$('#checkoutPageName').val();
		$
		.ajax({
			url : ACC.config.encodedContextPath
					+ "/getTealiumDataCheckout",
			type : 'GET',
			data:'checkoutPageName='+checkoutPageName,
			cache : false,
			success : function(data) {
				var tealiumData = "";
				tealiumData += ',"user_login_type":"'	//TPR-668
					+ user_login_type + '",';
				tealiumData += '"cart_total":"'
						+ $("#cart_total").val() + '",';
				tealiumData += '"product_unit_price":'
						+ $("#product_unit_price").val() + ',';
				tealiumData += '"product_list_price":'
					+ $("#product_list_price").val() + ',';
				tealiumData += '"product_name":'
					+ $("#product_name").val() + ',';
				tealiumData += '"product_quantity":'
					+ $("#product_quantity").val() + ',';
				tealiumData += '"adobe_product":"'
					+ $("#adobe_product").val() + '",';
				tealiumData += '"product_sku":'
					+ $("#product_sku").val() + ',';
				tealiumData += '"product_id":'
					+ $("#product_id").val() + ',';
				tealiumData += '"product_brand":'
					+ $("#product_brand").val() + ',';
				
				//kidswear
				//L1 L2 L3 For cart
				if($("#page_subcategory_L1").val() !=undefined || $("#page_subcategory_L1").val() !=null){ 
					tealiumData += '"page_subcategory_L1":'
						+ getListValue("page_subcategory_L1") + ',';           
					}
				if($("#page_subcategory_L2").val() !=undefined || $("#page_subcategory_L2").val() !=null){ 
					tealiumData += '"page_subcategory_L2":'
						+ getListValue("page_subcategory_L2") + ',';            
					}
				if($("#page_subcategory_l3").val() !=undefined || $("#page_subcategory_l3").val() !=null){ 
					tealiumData += '"page_subcategory_l3":'
						+ getListValue("page_subcategory_l3") + ',';            
					}
				//added for kidswear:L4 needs to be populated
				if($("#page_subcategory_l4").val() !=undefined || $("#page_subcategory_l4").val() !=null){ 
					tealiumData += '"page_subcategory_l4":'
						+ getListValue("page_subcategory_l4") + ',';            
					}
				//L1 L2 L3 cart ends
				//kidswear
				
				//TPR-430 Start
				if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
					tealiumData += '"product_category":'
					+ getListValue("product_category") + ',';                /*value passed as array instead of single string  INC_11511*/
				/*	+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
				}
				if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
					tealiumData += '"page_subcategory_name":"'
					+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
				}
				if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
					tealiumData += '"page_subcategory_name_L3":"'
				+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",';
				}
				//added for kidswear:L4 needs to be populated
				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
					tealiumData += '"page_subcategory_name_L4":"'
				+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '",'; //TISPRDT-1462
				}
			//TPR-430 End
				tealiumData += '"checkout_seller_ids":"'		//for TPR-429
					+ $("#checkoutSellerIDs").val() + '"}';
				data = data.replace("}<TealiumScript>", tealiumData);
				$("#tealiumHome").html(data);
			
				
			}
		});
		
		
		
	}

	
	//tpr-668  --for order page
	
	if(pageType =="orderconfirmation"){
		$
		.ajax({
			url : ACC.config.encodedContextPath
					+ "/getTealiumDataOrder",
			type : 'GET',
			cache : false,
			success : function(data) {
				var tealiumData = "";
				tealiumData += ',"user_login_type":"'	//TPR-668
					+ user_login_type + '",';
			    tealiumData += '"order_id":"'
						+ $('#orderIDString').val() + '",';
			     tealiumData += '"order_subtotal":"'
					+ $('#orderSubTotal').val() + '",';
			     tealiumData += '"order_date":"'
				+ $("#orderDate").val() + '",';
			     tealiumData += '"product_quantity":'
				+ $("#product_quantity").val() + ',';
				 tealiumData += '"order_payment_type":"'
					+ $("#order_payment_type").val() + '",';
		        tealiumData += '"product_sku":'
				+ $("#product_sku").val() + ',';
			    tealiumData += '"product_id":'
					+ $("#product_id").val() + ',';
				tealiumData += '"product_brand":'
					+ $("#product_brand").val() + ',';
				 tealiumData += '"order_shipping_charges":'
						+ $('#order_shipping_charges').val() + ',';
				 tealiumData += '"order_tax":"'
						+ $('#order_tax').val() + '",';
				 tealiumData += '"transaction_id":"'
						+ $('#transaction_id').val() + '",';
				 tealiumData += '"order_total":"'
						+ $('#order_total').val() + '",';
				 tealiumData += '"order_discount":"'
						+ $('#order_discount').val() + '",';
				 tealiumData += '"order_currency":"'
						+ $('#order_currency').val() + '",';
				 tealiumData += '"product_unit_price":'
						+ $('#product_unit_price').val() + ',';
				 tealiumData += '"product_list_price":'
						+ $('#product_list_price').val() + ',';
				 tealiumData += '"product_name":'
						+ $('#product_name').val() + ',';
				 tealiumData += '"order_shipping_modes":'
						+ $('#order_shipping_modes').val() + ',';
				 
				 //kidswear
				//L1 L2 L3 For cart
					if($("#page_subcategory_L1").val() !=undefined || $("#page_subcategory_L1").val() !=null){ 
						tealiumData += '"page_subcategory_L1":'
							+ getListValue("page_subcategory_L1") + ',';           
						}
					if($("#page_subcategory_L2").val() !=undefined || $("#page_subcategory_L2").val() !=null){ 
						tealiumData += '"page_subcategory_L2":'
							+ getListValue("page_subcategory_L2") + ',';            
						}
					if($("#page_subcategory_l3").val() !=undefined || $("#page_subcategory_l3").val() !=null){ 
						tealiumData += '"page_subcategory_l3":'
							+ getListValue("page_subcategory_l3") + ',';            
						}
					//added for kidswear:L4 needs to be populated
					if($("#page_subcategory_l4").val() !=undefined || $("#page_subcategory_l4").val() !=null){ 
						tealiumData += '"page_subcategory_l4":'
							+ getListValue("page_subcategory_l4") + ',';            
						}
					//L1 L2 L3 cart ends
					//kidswear

				//TPR-430 Start
				 if($("#product_category").val() !=undefined || $("#product_category").val() !=null){ 
					 tealiumData += '"product_category":'
					+ getListValue("product_category") + ',';                             /*value passed as array instead of single string  INC_11511*/
					/*+ $("#product_category").val().replace(/_+/g, '_') + '",';*/
				}
				if($("#page_subcategory_name").val() !=undefined || $("#page_subcategory_name").val() !=null){ 
					tealiumData += '"page_subcategory_name":"'
					+ $("#page_subcategory_name").val().replace(/_+/g, '_') + '",';
				}
				if($("#page_subcategory_name_l3").val() !=undefined || $("#page_subcategory_name_l3").val() !=null){ 
					tealiumData += '"page_subcategory_name_L3":"'
				+ $("#page_subcategory_name_l3").val().replace(/_+/g, '_') + '",';
				}
				//added for kidswear:L4 needs to be populated
				if($("#page_subcategory_name_l4").val() !=undefined || $("#page_subcategory_name_l4").val() !=null){ 
					tealiumData += '"page_subcategory_name_L4":"'
				+ $("#page_subcategory_name_l4").val().replace(/_+/g, '_') + '",'; //TISPRDT-1462
				}
			//TPR-430 End
				tealiumData += '"checkout_seller_ids":"'		//for TPR-429
					+ $("#checkoutSellerIDs").val() + '"}';
				data = data.replace("}<TealiumScript>", tealiumData);
				$("#tealiumHome").html(data);
			
			}
		});
	}
	
	
	//for order page
	
	/*TPR-648 start*/
	$('.shop-promos .promos a').click(function(){
		var brandText=$(this).text().replace(/ /g,'').toLowerCase()+ "_viewdetails";
		var brandClick="abcde_click";
		utag.link({"link_obj": this, "link_text": brandText, "event_type" : brandClick
				});
			
	});
	/*TPR-648 end*/
	
	
	/*TPR-657 starts*/ /* TPR-4730 SERP*/
	$('.feedbackYes.blue').click(function(){				
		var msg="search_feedback_yes";
		utag.link({"link_text": msg, "event_type" : msg
				});
			
	});
	
	$('.feedbackNo.orange').click(function(){				
		var msg="search_feedback_no";
		utag.link({"link_text": msg, "event_type" : msg
				});
			
	});	
	
	/*$('.feedBack-block #feedBackFormNo .feed-back #submit_button').click(function(){				
		var msg="search_feedback_no_submit";
		utag.link({"link_obj": this, "link_text": msg, "event_type" : msg
				});
			
	});*/
	/*TPR-657 ends*/
	
	/*TPR- 659 starts*/
	$(document).on("click",".view-cliq-offers",function(){
//		alert("viewcliq......")
		utag.link(
    			{link_obj: this, link_text: 'home_top_deal_view_offers' , event_type : 'home_top_deal_view_offers'}
    			);
		});
	/*TPR- 659  ends*/
}
	//TPR-5251  on size select utag data issue fix starts
    function populateSizeSelectUtagObject(){
	var productCodeArray = [];
	productCodeArray.push($("#product_id").val());
	productIdArray = productCodeArray;
	
	if(typeof utag_data !="undefined"){
		var utilityArray = [];
		utilityArray.push($("#product_sku").val());
		utag_data.product_sku = utilityArray;
		utilityArray = [];
		utilityArray.push($("#product_unit_price").val());
		utag_data.product_mrp = utilityArray;
		utilityArray = [];
		utilityArray.push($("#product_list_price").val());
		utag_data.product_mop = utilityArray;
		utilityArray = [];
		utilityArray.push($("#product_discount").val());
		utag_data.product_discount = utilityArray;
		utag_data.product_discount_percentage = $("#product_discount_percentage").val() + '%';
		utag_data.product_id = productIdArray;
		if($("#product_applied_promotion_code").val() != '' && $("#product_applied_promotion_code").val() !=undefined)
		{
			promo_id=$("#product_applied_promotion_code").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			utilityArray = [];
			utilityArray.push(promo_id);
			utag_data.promo_id = utilityArray;
		}
		else{
			if(utag_data.promo_id != undefined){
				delete utag_data.promo_id;
			}
		}
		if($("#product_applied_promotion_title").val() != '' && $("#product_applied_promotion_title").val() !=undefined)
		{
			promo_title=$("#product_applied_promotion_title").val().replace(/([~!@#$%^&*()-+=`{}\[\]\|\\:;'<>,.\/? ])+/g, '_');
			utilityArray = [];
			utilityArray.push(promo_title);
			utag_data.offer_title = utilityArray;
			utag_data.promo_title = utilityArray;
		}
		else{
			if(utag_data.offer_title != undefined){
				delete utag_data.offer_title;
			}
			if(utag_data.promo_title != undefined){
				delete utag_data.promo_title;
			}
		}
		
		utilityArray = [];
		utilityArray.push($("#product_stock_count").val());
		utag_data.product_stock_count = utilityArray;
		utag_data.out_of_stock = $("#out_of_stock").val();
		utag_data.seller_id = $("#pdpBuyboxWinnerSellerID").val();
		utag_data.seller_name = $("#sellerNameId").html();
		if($("#pdpOtherSellerIDs").val() != '' && $("#pdpOtherSellerIDs").val() !=undefined)
		{
			otherSellerIds=$("#pdpOtherSellerIDs").val();
			utag_data.other_seller_ids = $("#pdpOtherSellerIDs").val();
		}
		else{
			if(utag_data.other_seller_ids != undefined){
				delete utag_data.other_seller_ids;
			}
		}
	}
}
//TPR-5251  on size select utag data issue fix ends

//Comma separated strings changed to array of strings
function getListValue(divId){
	var categoryList = $("#"+divId).val().replace(/_+/g, '_');
	var categoryArray = categoryList.split(",");
	var finalCategoryArray=[];
	for (i = 0; i < categoryArray.length; i++) { 
		finalCategoryArray.push("\""+categoryArray[i]+"\"");
	}
	return "["+finalCategoryArray+"]";
}
/* TPR-4729 | SERP |Need help starts*/
 
 $(document).on('click',"#up",function(){
	 utag.link({link_text: 'need_help_clicked', event_type : 'need_help_clicked'});
 });
 /* TPR-4729 | SERP  ends*/ 

 /* TPR-4724 |Add to Bag |serp*/ 
 $(document).on('click','.serp_add_to_cart_form .js-add-to-cart',function(event){
	 var productId="productCodePost";
	 var product = $("#"+$(this).closest('form').attr("id")+" :input[name='" + productId +"']").val(); 
	 var productarray=[];
	     productarray.push(product);
	 if(typeof utag !="undefined"){
		utag.link({ link_text : "add_to_bag_serp" , event_type : "add_to_bag_serp" , product_sku : productarray });
	} 
 });

/*TPR-4721, TPR-4706 | Sort By in SERP|PLP*/
$(document).on('click','.list_title .UlContainer .sort',function(){
	if(typeof utag !="undefined"){
		var value = $(this).text().trim().toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
		utag.link({ 
			link_text : "sort_by_"+value , 
			event_type : "sort_by_selected" , 
			"sort_by_value" : value 
		});
	 }
})


/*TPR-4719, TPR-4704 | Search Filter in SERP|PLP*/
var restrictionFlag='false';

function setupSessionValues(){
	if($('.bottom-pagination .facet-list.filter-opt').children().length > 0){
		var filterTypeList=[];
		var filterTypeFinalList=[];
		var filterValueList=[];
		var sessionPageUrl=window.location.href;
		
		$('.bottom-pagination .facet-list.filter-opt').children().each(function(){
			filterTypeList.push($(this).children().eq(0).attr('class').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""));
			filterValueList.push($(this).children().eq(1).attr('value').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,""))
		})
		
		$.each(filterTypeList, function(i, el){
			if($.inArray(el, filterTypeFinalList) === -1){
				filterTypeFinalList.push(el)
			};
		});
		if(filterValueList.length > 0 && filterTypeList.length > 0){
			if(typeof(utag) !="undefined"){
				utag.link({ 
					link_text : "final_filter_list" , 
					event_type : "final_filter_list" , 
					"filter_types_final":filterTypeFinalList.toString(),
					"filter_values_final":filterValueList
				});
			}
		}
	}
}

//TPR-4725 | quickview
$(document).on('click','.quick-view-prod-details-link',function(){
	 if(typeof utag !="undefined"){
		 utag.link({ link_text : "quick_view_full_details_clicked" , event_type : "quick_view_full_details_clicked" });
	 }
})
//TPR-4727 | add to compare 2nd part
$(document).on('click','.compareRemoveLink',function(){
	if(typeof utag !="undefined"){
		 utag.link({ link_text : "add_to_compare_removed" , event_type : "add_to_compare_removed" });
	 }	
})
//TPR-4727 | add to compare 3rd part
$(document).on('click','#compareBtn',function(){
	if(typeof utag !="undefined"){
		 utag.link({ link_text : "compare_now_clicked" , event_type : "compare_now_clicked" });
	 }	
})
//TPR-4720 | Display First 5 Products  |serp 
function populateFirstFiveProductsSerp(){
	var count = 5; 
	var productArray= [];
    var searchResult = $("ul.product-list li.product-item").length;
	if(searchResult < count ){
		count = searchResult;
    }
   for(var i=0;i< count;i++)
   {
	 var selector = 'ul.product-list li.product-item:eq('+i+') span.serpProduct #productCode';
	product = $(selector).val();
	productArray.push(product);
   }
   if(typeof utag !="undefined"){
		 utag.link({ serp_first_5_products : productArray,product_id : productArray  });
	 }
   
   if(productArray.length == 0){
	   if(typeof utag !="undefined"){
			 utag.link({ error_type: 'NoProductsFound' });
		 }  
   }
}

$( window ).load(function() {
	if($('#pageType').val() == "productsearch"){
		populateFirstFiveProductsSerp();	
	}
	
	if($('#pageType').val() == "category" || $('#pageType').val() == "electronics" ){
		populateFirstFiveProductsPlp();
	}
	
	if($('#pageType').val() == "productsearch" ){
		  var isVisible = $('.search-empty.no-results.wrapper:visible').is(':visible');
		   if(isVisible && typeof utag !="undefined" ){
			     utag.link({"error_type":'nullSearch'});
		}
	}
});

//TPR-4705 | Display first 5  products |plp 
function populateFirstFiveProductsPlp(){
	var count = 5; 
	var productArray= [];
    var searchResult = $("ul.product-listing.product-grid li.product-item").length;
	if(searchResult < count ){
		count = searchResult;
    }
   for(var i=0;i< count;i++)
   {
      var  selector = 'ul.product-listing.product-grid li.product-item:eq('+i+') span.serpProduct #productCode';
      if(typeof selector !="undefined"){
  	    product = $(selector).val();
      }
      productArray.push(product);
      
  }
   if(typeof utag !="undefined"){
		 utag.link({ plp_first_5_products : productArray,product_id : productArray });
	 }	
   
   if(productArray.length == 0){
	   if(typeof utag !="undefined"){
			 utag.link({ error_type: 'NoProductsFound' });
		 }  
   }
}

//TPR-4725 |quick view  size|serp
$(document).on('click',"#quickViewVariant > li", function(){
	var product_size = $(this).find('a').html();
	utag.link({
		"link_text":"quick_view_size_"+product_size,
		"event_type":"quick_view_size_selected",
		"product_size":product_size
	});
})


/*Thumbnail tracking*/
//$(document).on("click",".product-image-container .imageListCarousel .thumb",function(){
$(document).on("click",".quick-view-popup > .product-image-container > .productImageGallery .imageListCarousel .thumb",function(){
	var thumbnail_value = $(this).parent().attr('class');
	var thumbnail_type = $(this).find('img').attr('data-type');
	utag.link({
		"link_text":"quickview_"+thumbnail_value+"_clicked",
		"event_type":"quickview_"+thumbnail_type+"_clicked",
		"thumbnail_value":thumbnail_value
	});
})

/*TPR-4725,4712 | colour selection on quickview*/
$(document).on('click',".color-swatch > li", function(){
	var page='';
	if($('#pageType').val() == "product"){
		page="pdp";
	}
	else{
		page="quickview";
	}
 	var product_color = $(this).find('img').attr('title').toLowerCase().replace(/ +$/, "").replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
 	utag.link({
 		"link_text":page+"_color_"+product_color,
 		"event_type":page+"_color_selected",
 		"product_color":product_color
 	});
 })
 

  /*TPR-4728 | add to compare page |1st part*/
 $('ul.jump li a').on('click',function(){
	 var categorySelected = $(this).text();
	 if(typeof utag !="undefined"){
		 utag.link({ link_text : categorySelected+"_clicked" , event_type :'jump_to_section_clicked' });
	 }	
 })
 /*TPR-4728 | add to compare page |2nd part*/
 $(document).on('click','.shop-now',function(){
	 var url = $(this).parents().find(".product-tile.cboxElement").attr('href').split("/")[2];
		var productIDlist = url.split("p-");
		var productID = productIDlist[1];
		var productArray=[];
		productArray.push(productID);
	 if(typeof utag !="undefined"){
		 utag.link({ link_text : 'shop_now_clicked' , event_type :'shop_now_clicked',product_id : productArray ,shop_now_product_id : productArray });
	 }
 })

 
 /*Out Of Stock During adding to bag*/
function errorAddToBag(errorMessage){
	utag.link({"error_type":errorMessage});
}

/*TPR-4687 | Broken Image*/
$(window).load(function() {
	tealiumBrokenImage();
});


function tealiumBrokenImage(){
	var brokenImageCount=0;	
	
	$('.mainContent-wrapper img').each(function(){
		var url = $(this).attr('src');
		if(url){
			if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
			      // image is broken
			    	brokenImageCount++;
			    }
		}

	});
	if(brokenImageCount > 0){
		var msg = brokenImageCount+" broken_image_found";
		utag.link({ 
			error_type : msg
		});
	}
}
/*TPR-4728 | add to compare page  3rd part */
$(".product-tile.cboxElement").click(function(){
	 var url = $(this).attr('href').split("/")[2];
	var productID = url.split("p-");
	var productArray=[];
	productArray.push(productID[1]);
	if(typeof utag !="undefined"){
		 utag.link({ link_text : 'shop_now_pop_up' , shop_now_product_id : productArray});
	 }
	}) 

/*PDP, quickview image hover*/
$(document).on("mouseover",".zoomContainer",function(e) {
	if($('#pageType').val() != "/compare"){
		var page='';
		if($('#pageType').val() == "product"){
			page = "pdp";
		}
		else {
			page = "quickview";
		}
		if(typeof utag !="undefined"){
			utag.link({
				link_text: page+"_image_hover",
				event_type : page+"_image_hover"
			});
		}
	}
});	


$(window).unload(function(event) {
	var pageType = $('#pageType').val();
	if(pageType == 'category' || pageType == 'productsearch'){
		if(restrictionFlag != 'true'){
			setupSessionValues();
		}
	}
});
/*----------Start of  validate email in feedback-----------*/
	mobileVal=false,deskVal=false,styleMobContainer=null,longStyleContainer=null,styleDeskContainer=null;
	function validateEmail()
    { 	 var x = document.getElementById("emailField");
		 if (/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(x.value))  
		  {  
			$("#invalidEmail").hide();
			return true;
		  }  
    }
	
	function validateEmailOnSubmit()
    { 	 var x = document.getElementById("emailField");
   
    if(x.value.trim().length<=0){
		$("#invalidEmail").show();
	 	return false;
	}else if (!(/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(x.value)))  
		  {  
		 	$("#invalidEmail").show();
		 	return false;
		 } else {
			 $("#shareFeedback").hide();
			 return true;
		 }
		 
    }


	
	function validateFeedback()
    { 	 var x = document.getElementById("commentNO");
		 if (x.value.trim().length>0)  
		  {  
			$("#invalidFeedback").hide();
		    return true;
		  }  
    }

	
	function validateFeedbackOnSubmit()
    { 	 var x = document.getElementById("commentNO");
		 if (x.value.trim().length<=0)  
		 { 	
		 	$("#invalidFeedback").show();
		 	$("#shareFeedback").show();
		 	return false;
		 } else {
			 return true;
		 }
    }
	
	/*---------- END of validate email in feedback-----------*/

/*------Start of Authentic Exclusive function ---*/
	
	function authentic() {
		var $li = $(".page-authenticAndExclusive ul.feature-brands li");
		for(var i = 0; i < $li.length; i+=3) {
			var a,b,c,d;
			a=parseInt($li.eq(i).find(".image").css("height"))+parseInt($li.eq(i).find(".logo").css("height"))+parseInt($li.eq(i).find(".logo").css("margin-bottom"))+parseInt($li.eq(i).find(".logo").css("margin-top"))+parseInt($li.eq(i).find("span").css("height"));
			b=parseInt($li.eq(i+1).find(".image").css("height"))+parseInt($li.eq(i+1).find(".logo").css("height"))+parseInt($li.eq(i+1).find(".logo").css("margin-bottom"))+parseInt($li.eq(i+1).find(".logo").css("margin-top"))+parseInt($li.eq(i+1).find("span").css("height"));
			c=parseInt($li.eq(i+2).find(".image").css("height"))+parseInt($li.eq(i+2).find(".logo").css("height"))+parseInt($li.eq(i+2).find(".logo").css("margin-bottom"))+parseInt($li.eq(i+2).find(".logo").css("margin-top"))+parseInt($li.eq(i+2).find("span").css("height"));
			
			if ( a > b && a > c ) {
					d = a; 
				}
		      else if ( b > a && b > c ) {
		    	  d = b;
		      }
		      else {
		    	  d = c;
		      }
			$li.eq(i).css("min-height",d);
			$li.eq(i+1).css("min-height",d);
			$li.eq(i+2).css("min-height",d);
		}

	}
	
/*------END of Authentic Exclusive function ---*/
	
/*--- Start of A-Z column function---*/
	
	function atoz_listing(){
		$('.brands-page').find("ul .desktop ul.list_custom li").removeClass("columns-4-mobile");
	var list_count;
	$('.brands-page').find("ul .desktop ul.list_custom li").each(function(){
		list_count=$(this).find("a").length;
		if(list_count <= 6)
		{				
			$(this).addClass("columns-1");
		}
		else if(list_count > 6 && list_count <= 12)
			{
				$(this).addClass("columns-2");
			}
		else if(list_count > 12 && list_count <= 18)
		{
			$(this).addClass("columns-3");
		}
		else
		{
			$(this).addClass("columns-4");
		}
	});
	
}		
	
/*--- END of A-Z column function---*/

//TISPRO-508	
	
	function closing() {
		$("#zoomModal").modal('hide');
		$("#zoomModal").removeClass("active");
	}
	function closingVideo(){
		$("#videoModal").modal('hide');
		$("#videoModal").removeClass("active");
		var x = $("#player").attr('src');
		var z = $("#player").attr('src', x+"&autoplay=0");
	}	
	
	
$(document).ready(function(){
	/*alert("fhgfhfgh");
	$(".shopstyle-indicator li").on("click", function(){


	    $("#rotatingImageTimeout").trigger("to.owl.carousel", [toIndex, $(this).index , true]);


	});*/

	//TISEEII-640 issue fix -- Start
	$(".facet.js-facet .js-facet-name").each(function(){
		var x = $(this).html().length; 
		//if(x == "6")
			if($.trim($(this).html())==''){
			//alert("no data");
				$(this).parent().hide();
			}
	});
	//TISEEII-640 issue fix -- End
	
	$(document).keydown(function(e){
		if(e.which == 27) {
			$('.modal').modal('hide');
		}
	}); 

	/*------------Start of SNS auto complete----------*/
			
			var style = null ;
			// For INC144315410
			var findGetParameter = function findGetParameter(parameterName) {
 			    var result = null,
 		        tmp = [];
 			    location.search
 			    .substr(1)
 		        .split("&")
 		        .forEach(function (item) {
 		        tmp = item.split("=");
 		        if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
 		    });
 		    return result;
			}

			var isLux = findGetParameter('isLux');
			console.log("isLux"+ isLux);
			var isLuxury = $("#isLuxury").val();
			console.log("isLuxury"+ isLuxury);
			if (isLuxury != "true" || isLuxury == "undefined"){
				console.log("isLuxury"+ isLuxury);
				isLuxury = false ;
			}
			if ( isLux == "false"){
				console.log("isLux"+ isLux);
				isLux = false ;
			}
			
			var marketplaceHeader = (isLux || isLuxury) ? false : true ;
			console.log("marketplaceHeader"+ marketplaceHeader);
			if (isLux == "true"){
				$(".marketplace-header").css("visibility","hidden");
			}
			else{
				$(".marketplace-header").css("visibility","visible");
			}
			if(marketplaceHeader){
			if(!($('body').hasClass("page-multiStepCheckoutSummaryPage") || $('body').hasClass("page-checkout-login")))
				{
					 if($(window).scrollTop() == 0){
						 window.setTimeout(function(){
							 style = "display:block;width: "+$("#js-site-search-input").css("width")+"; left: "+$("#js-site-search-input").offset().left+"px";
							 
							 $("#ui-id-1").attr("style",style);
						 },100);
						 
					 }	
					
					 style = "display:block;width: "+$('#js-site-search-input').css('width')+"; left: "+$('#js-site-search-input').offset().left+"px";
				}
			}
			  $("ul#ui-id-1").attr("style",style);
			  
			  $("#js-site-search-input").keypress(function(){
	
				  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css("border","1px solid #dfd1d5");
				});
	
				if($('body').hasClass("template-pages-layout-micrositePage1") && marketplaceHeader){
				    $(window).scroll(function () {
					  if($(".ui-autocomplete").is(":visible")){
					  window.setTimeout(function(){
						  $("#js-site-micrositesearch-input").parents('form#search_form_microsite').next('.ui-autocomplete.ui-front.links.ui-menu').css({
							  left : $('#js-site-micrositesearch-input').offset().left,
							  width: $('#js-site-micrositesearch-input').outerWidth()
						  });
						  },300);
					  }
					  if($(window).scrollTop() > 0){
					    	
					    	$("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').hide();
					    }
  					});    
				  $('#js-site-micrositesearch-input').keydown(function () {
					  $("#js-site-micrositesearch-input").parents('form#search_form_microsite').next('.ui-autocomplete.ui-front.links.ui-menu').css({
						  left : $('#js-site-micrositesearch-input').offset().left,
						  width: $('#js-site-micrositesearch-input').outerWidth()
					  });
			  });
		  }
			  
	/* -----------------END  of SNS auto complete---------*/
		
	/* -----------------Start of Out of stock styling---------*/
		 $('a.stockLevelStatus').parents('div.image').find('a img').addClass('out-of-stock-product');
		 
	/* -----------------END  of Out of stock styling---------*/
		 
		 
	/*----start of Sticky Bag --------*/
		 
			 /*var x = $(".js-mini-cart-count").text();*/		/*commented as part of UF-249*/
			 /*$(".js-mini-cart-count-hover").text(parseInt($(".js-mini-cart-count").text()));*/		/*commented as part of UF-249*/
			   
				  
			   $(window).scroll(function () {
				   if(marketplaceHeader){
					  /*--------changes for Sticky Header in MyBag--------------*/
				   if(!($('body').hasClass("page-multiStepCheckoutSummaryPage") || $('body').hasClass("page-checkout-login"))){
						 if( $(window).scrollTop() > $('.bottom').offset().top && !($('.bottom').hasClass('active'))){
				      $('.bottom').addClass('active');
				      $('.productGrid-header-wrapper').addClass('active-header');	
			    		//$(".productGrid-header-wrapper").addClass("active_adjust");
			      /*TPR-4471 change*/
				      $('header > .content .top').addClass('header_fix');
				    } else if ($(window).scrollTop() == 0){
				      $('.bottom').removeClass('active');
				      $('.productGrid-header-wrapper').removeClass('active-header');
				      //$(".productGrid-header-wrapper").removeClass("active_adjust");/*TPR-4471 change*/
				   $('header > .content .top').removeClass('header_fix');
				    }
					  }
				   }
				  }); 
	 /*----END of Sticky Bag --------*/
	
	/*----------start of changes to get search-text and category and assign to hidden fields-----------*/
		
		
			var dropText = document.getElementById("searchCategory");
			if(dropText!= null && dropText != "undefined" && dropText.selectedIndex >= 0)
			{
				var searchCategoryText = dropText.options[dropText.selectedIndex].text;
				var $wholeText = $("div.results").text();
				var positon = $wholeText.indexOf("f");
				var quotedText= $wholeText.slice(positon+4);
				var res1 = quotedText.substr(quotedText.indexOf("\"")+1);
				var res2 =res1.slice(0,res1.indexOf("\"")); 
				var searchTextFeedback = $('#text').val();
				$("#searchCategoryhidden").val(searchCategoryText);
				$("#searchText").val(searchTextFeedback);			  
			}
	/*----------start of changes to get search-text and category and assign to hidden fields-----------*/
		
		
	/*-------Start of saved cards card details in My account -------*/
		
		
			$(".saved-cards .number paymentItem .view-details").click(function(){
	
				if ($(this).parent().hasClass('active'))
				{
					$(".saved-cards .number.paymentItem").removeClass("active");
				}
				else
				{
					$(".saved-cards .number.paymentItem").addClass("active");
				}
			});
			
	/*-------END of saved cards card details in My account -------*/
	
	/*----- Start of selecting drop-down text in search box header---------*/	
			$(".select-list ul li").on("click", function(){
				  $(".selected-dropdownText").text($(this).text());
				});
			
	/*----- END  of selecting drop-down text in search box header---------*/
			
	/*-----------Start of SERP codes-----------------*/ 
			$(window).on("load resize", function() {
				if($(".toggle-filterSerp").css("display") == "block"){
				/*$(".facet-name.js-facet-name h4").first().addClass("active-mob");
				$(".facet-name.js-facet-name h4").parent().siblings().hide();
				$(".facet-name.js-facet-name h4.active-mob").parent().siblings().show();*/
				}
				else{
				$(".facet-name.js-facet-name h3").removeClass("active-mob");
					$(".facet_desktop .facet-name.js-facet-name h3").each(function(){
					if($(this).hasClass("active")){
						$(this).parent().siblings().show();
						$(this).parent().siblings().find("#searchPageDeptHierTree").show();
						$(this).parent().siblings().find("#categoryPageDeptHierTree").show();
					}
					});
				}
				
				$(".store-locator-header,.download-app").parent("ul").css("display","block");		/*PRDI-261*/
				
				});
			
			var sessionFacetName;
			$(document).on("click",".facet-name.js-facet-name h3",function(){
				if(typeof(Storage) !== "undefined") {
						if($(this).parents().hasClass("facet_mobile")){
							$(".facet-name.js-facet-name h3").removeClass("active-mob");
							$(this).parents(".facet_mobile").siblings().find(".facet-values.js-facet-values.js-facet-form").hide();
							if($(this).parent().siblings('#searchPageDeptHierTreeForm').length == 0){
								$('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").hide();
							}
							else{
								$(".facet-values.js-facet-values.js-facet-form").hide();
							}
							if($(this).parent().siblings('#categoryPageDeptHierTreeForm').length == 0){
								$('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").hide();
							}
							else{
								$(".facet-values.js-facet-values.js-facet-form").hide();
							}
							$(this).addClass("active-mob");
							$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').show();
							$(this).siblings('.brandSelectAllMain').show();
							$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").show();
							$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").show();
						}
						else{
					    if($(this).hasClass('active')) {
					    	$(this).removeClass('active');
					    	$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').hide(100);
					     	$(this).siblings('.brandSelectAllMain').hide(100);
					    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").hide(100);
					    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").hide(100);
					    } else {
					    	$(this).addClass('active');
					    	$(this).parent().siblings('.facet-values.js-facet-values.js-facet-form').show(100);
					    	$(this).siblings('.brandSelectAllMain').show(100);
					    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").show(100);
					    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").show(100);
					    }
					    
						}
					    	sessionFacetName = $(this).text();
							if(sessionStorage.getItem(sessionFacetName) == null){
								sessionStorage.setItem(sessionFacetName, false);
							} else{
								if($(this).hasClass('active')) {
									sessionStorage.setItem(sessionFacetName, true);
								} else {
									sessionStorage.setItem(sessionFacetName, false);
								}
							}
					    
							
				}
				else {
			        document.getElementById("result").innerHTML = "Sorry, your browser does not support web storage...";
			    }
			});
			
			
			$(".js-facet").each(function(){
				if(sessionStorage.getItem($(this).find('.js-facet-name > h3').text()) == "false" && null != sessionStorage.getItem($(this).find('.js-facet-name > h3').text())) {
					$(this).find('.js-facet-name h3').removeClass('active');
					$(this).find('.facet-values.js-facet-values.js-facet-form').hide(100);
					$(this).find('.brandSelectAllMain').hide(100);
				}
			});
			if(sessionStorage.getItem($('ul.product-facet > .js-facet-name > h3').text()) == "false" && null != sessionStorage.getItem($('ul.product-facet > .js-facet-name > h3').text())) {
				$('ul.product-facet > .js-facet-name > h3').removeClass('active');
				$('#searchPageDeptHierTreeForm #searchPageDeptHierTree').hide(100);
		    	$("#categoryPageDeptHierTreeForm #categoryPageDeptHierTree").hide(100);
			}
			
			
			//Moved out from document-ready
			/*$(".toggle-filterSerp").click(function(){
				$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
				$(this).toggleClass("active");
				});*/
			$(".product-facet .facet-list li input.applied-color").each(function(){
				var appliedColor = $(this).attr("value");
				$(".product-facet li.filter-colour a").each(function(){
					var availableColors = $(this).attr("title");
					if( appliedColor ==  availableColors ){
						$(this).parent().addClass("active");
					}
				});
			
			});	
			var search_count_text= $(".searchString i").text().trim();
			$(".searchString i").text(search_count_text);
	$(".facet-list.filter-opt li").each(function(){
		if($(this).find("input.colour").length > 0){
			var selected_colour = $(this).find("input.applied-color").val();
			$(".left-block .product-facet .facet.Colour .facet-list li.filter-colour").each(function(){
			var colour_name = $(this).find("input[name=facetValue]").val().split("_", 1);
			if(colour_name == selected_colour){
				$(this).addClass("selected-colour");
			}
			if(selected_colour == "Multicolor"){
				if(colour_name == "Multi"){
					$(this).addClass("selected-multi-colour");
				}
			}
			});
		}
		if($(this).find("input.size").length > 0){
			var selected_size = $(this).find("input.applied-color").val();
			$(".left-block .product-facet .facet.Size .facet-list li.filter-size").each(function(){
				var size_val = $(this).find(".js-facet-sizebutton").val();
				if(size_val == selected_size){
					$(this).addClass("selected-size");
				}
			});
		}
	});
	/*-----------END of SERP Codes-----------------*/ 
	
		
	/*--------------Common JS FUNCTIONALITY CODES----------*/
		
			$(".lookbook-text").each(function(){
				if($(this).children().length == 0) {
					$(this).parent().css("margin-bottom","0px");
					}
			});
			$(".electronics-brand .feature-brands").each(function(){
				if($(this).children().length == 0) {
					$(this).parents(".brands").hide();
					}
			});
			$(".apparel-brand .feature-brands").each(function(){
				if($(this).children().length == 0) {
					$(this).parents(".brands").hide();
				}
			});
			$(".feature-collections .collections .chef").each(function(){
				if($(this).children().length == 0) {
					$(this).hide();
				}
			});
			
			$(document).on("click",".toggle",function(e){
				var p = $(e.currentTarget).parent();
			    if(p.hasClass('active')) {
			      p.removeClass('active');
			    } else {
			      p.addClass('active');
			    }
			   /* var style=null;
				if($(window).width() < 773) {
					$("span#mobile-menu-toggle").unbind('click');
					$(document).on("click", "span#mobile-menu-toggle", function(){
						$("a#tracklink").mouseover();
						$(this).parent('li').siblings().find('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
						$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
						$(this).next().slideToggle();
						$(this).toggleClass("menu-dropdown-arrow");
					});
					--- Mobile view shop by brand and department ---

					$("li.short.words").siblings("li.long.words").hide();
					  $("li.short.words").unbind('click');
					  $("li.short.words").click(function(){
						$(this).toggleClass('active');
					    $(this).nextAll().each(function(){

					      if($(this).hasClass("short")) {
					        return false;
					      }

					      $(this).toggle();
					    });

					  });

					--- Mobile view shop by brand and department --- 
				}
				else {
					$("#mobile-menu-toggle").next().attr("style",style);
					$("li.short.words,li.long.words").next().attr("style",style); 
				} */
			});
			$(document).on("click","footer h3.toggle",function(e){
				
					if ($(window).width() > 790) {
						$(e.currentTarget).parent().removeClass("active");
					} 
			 }); 
			$(window).on("load resize", function() {
				if ($(window).width() > 790) {
					$("footer h3.toggle").parent().removeClass("active");
				}
			});
			$(".close").on("click",function(e){
				$(e.currentTarget).closest('.banner').removeClass('active');
			});
			
			$(".toggle a").on("click",function(e){
				e.stopPropagation();
				var p = $(e.currentTarget).parent().parent();
				if(!p.hasClass('active')) {
				      p.addClass('active');
				    } else {
				      window.location.href=$(e.currentTarget).attr('href');
				   }
			});
	
	/*------- END of Common functionality codes ------*/  
			
	/*----Start of SignIn & SignUp tab Switching -----*/
			
			 $("#signIn_link").on("click",function(e) {
			      $(this).addClass('active');
			      $("#SignUp_link,#sign_up_content").removeClass("active");
			      $("#sign_in_content").addClass("active");
			    });
			 $("#SignUp_link").on("click",function(e) {
			      $(this).addClass('active');
			      $("#signIn_link, #sign_in_content").removeClass("active");
			      $("#sign_up_content").addClass("active");
			    });
	/*----Start of SignIn & SignUp tab Switching -----*/
			 
			 /*----Start of  PDP tabs -----*/
			 $(".SpecWrap .Padd .tabs-block .nav > li:first-child, .SpecWrap .Padd .tabs-block .tabs > li:first-child").addClass("active");
			 $(".tabs-block .nav.pdp li").on("click",function(e) {
				 //$("ul.nav.pdp li").removeClass('active');
				 $(this).siblings().removeClass('active');
				 $(this).addClass('active');
				 var count = $(this).index() + 1;
				 if ($(this).parent().hasClass('productNav')){
					 $(this).parents().find("ul.tabs.pdp.productTabs>li").removeClass('active');
					 $(this).parents().find("ul.tabs.pdp.productTabs>li:nth-child("+count+")").addClass("active");
				 }
				 else {
					 $(this).parents().find("ul.tabs.pdp.specTabs>li").removeClass('active');
					 $(this).parents().find("ul.tabs.pdp.specTabs>li:nth-child("+count+")").addClass("active");
				 }
				 //$("ul.tabs.pdp>li").removeClass('active'); 
				// $("ul.tabs.pdp>li").eq(count).addClass("active");
			    }); 
	/*----END of  PDP tabs -----*/
	
	/*----Start of  SHop by brand A_E hover functionality  -----*/
			 $(".range").hide();
			 $(".range.current").show();
			 var id= $(".range.current").attr('id');
			
			 $('[data-tab='+id+']').css({
					"border-bottom":"3px solid",
					"font-weight":"bold"
				});
			 $('nav>ul>li>ul>li>.toggle').hover(function(){
				 $(".range").removeClass('current');
				 $('#A-E').addClass('current');
				  $(".range").hide();
			 $(".range.current").show();
			 var id= $(".range.current").attr('id');
			 $('.brandGroupLink').css({
				 "border-bottom":"none",
					"font-weight":"400"
				});
			 $('[data-tab='+id+']').css({
				 "border-bottom":"3px solid",
					"font-weight":"bold"
				});
			 })
	/*----END of  SHop by brand A_E hover functionality  -----*/
			 
	/*---Start of Checkout Payment tab switching  ----*/
			 var paymentModes =  $("#viewPaymentCredit, #viewPaymentDebit, #viewPaymentNetbanking, #viewPaymentCOD, #viewPaymentEMI,#viewPaymentCreditMobile, #viewPaymentDebitMobile, #viewPaymentNetbankingMobile, #viewPaymentCODMobile, #viewPaymentEMIMobile,#viewPaymentMRupee, #viewPaymentMRupeeMobile");
			 $(window).on('load resize',function(){	
			 paymentModes.on("click",function(e) {
				 $('.cart.wrapper .left-block .payments.tab-view ul.tabs').show(200);
				/*if($(window).width()<651){
				 $('.cart.wrapper .left-block .payments.tab-view ul.tabs').show(200);
				 $(this).parents('ul.nav').addClass('hide-menu');
				 $(this).parents('.left-block').find('h1.payment-options').addClass('hide-menu');
				 }*/
				 if(paymentModes.parent().hasClass("active")){
					 paymentModes.parent().removeClass("active");
				 }
				 $(this).parent().addClass("active"); 
				 $('ul.accepted-cards li').removeClass('active-card');
			 });
			
			/* $('.cart.wrapper .left-block .payments.tab-view .tabs li.change-payment').click(function(){
				 $(this).parent().hide(200);
				 $(this).parent().siblings('ul.nav').removeClass('hide-menu');
				 $(this).parents('.left-block').find('h1.payment-options').removeClass('hide-menu');
			 });*/
			 });
			 if($("#savedCard").css("display") === "block") {
				 $(".newCardPayment").css("display","none");
			 }
	/*---END of Checkout Payment tab switching  ----*/
			 
	 /*---Start of Micro site brand header toggle functionality ---*/
			 
			 if($('body').hasClass('template-pages-layout-micrositePage1')){
	
				 //$(this).find('header').first().addClass('compact');
				 $(this).find('header').first().find('.compact-toggle').addClass('open');
				 $(this).find('header').first().find('.compact-toggle').click(function(){
					 $(this).parents('header').toggleClass('compact');
					 $(this).parents('header').find('.compact-toggle').toggleClass('open');
				 }); 
				 
			
				 
				/* $(this).find('header').find('.compact-toggle').toggleClass('open');
				 $(this).find('header').first().find('.compact-toggle').click(function(){
					 $(this).parents('header').toggleClass('compact');
					 $(this).parents('header').find('.compact-toggle').toggleClass('open');
				 });*/
			 }
			/* $(window).scroll(function () {
			 if($("header .content .bottom").hasClass("active")){
					$("header > .content .top .compact-toggle.open").css("display","none");
				}
				else{
					$("header > .content .top .compact-toggle.open").css("display","inline-block");
				}
			 });*/
	/*---END of Micro site brand header toggle functionality ---*/
			 
	/*-------Start of Marketplace preferences--------*/
			  $(document).ready(function() {
				  if($("#radioInterest1").is(":checked")) {
					  $("#radioInterest1").click();
					}
			  })
			 
			 
			 
			 
			 $("#radioInterest1").click(function(){
					$("body .account.preferences .right-account form fieldset span input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset span label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form fieldset .mplPref-category input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset .mplPref-category label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form fieldset .freq input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset .freq label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});	
					$("body .account.preferences .right-account form fieldset input").prop("disabled","true");
					$("body .account.preferences .right-account form fieldset label").css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form .button-set a,body .account.preferences .right-account form .button-set a:hover").css({
						"color":"#000",
						"opacity":"0.5",
						"cursor":"not-allowed"
					});
					$("body .account.preferences .right-account form .button-set a").prop("disabled","true");
				})
				$("#radioInterest0").click(function(){
					var style=null;
					$("body .account.preferences .right-account form fieldset .mplPref-category input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset .mplPref-category label").attr("style",style);
					$("body .account.preferences .right-account form fieldset .freq input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset .freq label").attr("style",style);
					$("body .account.preferences .right-account form fieldset input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset label").attr("style",style);
					$("body .account.preferences .right-account form fieldset span input").removeAttr('disabled');
					$("body .account.preferences .right-account form fieldset span label").attr("style",style);
					$("body .account.preferences .right-account form .button-set a").removeAttr('disabled');
					$("body .account.preferences .right-account form .button-set a,body .account.preferences .right-account form .button-set a:hover").attr("style",style);
					
				})
				 $("#unsubcribe-link").click(function(e){
				        if($("#unsubcribe-link").attr("disabled")=="disabled")
				        {
				            e.preventDefault(); 
				        }        
				    });  
	/*-------End of marketplace preferences--------*/
				
	/*---Start of Questionnaire---*/
				$("body form.questionnaire-form").parents().find(".breadcrumbs.wrapper").css("display","none");
				
				$("body form.questionnaire-form").parents().find("header").addClass("compact");
				
				$("body div.marketplace-mystyle").find(".breadcrumbs.wrapper").css("display","none");
				
				$("body div.marketplace-mystyle").find("header").addClass("compact");
				
				
					 $("body form.questionnaire-form").parents().find('header').find('.compact-toggle').click(function(){
						 $(this).parents('header').toggleClass('compact');
						 $(this).parents('header').find('.compact-toggle').toggleClass('open');
						/* $(".compact-toggle.open").css({
							 "opacity":"1",
						 });*/
					 });
					 $("body div.marketplace-mystyle").find('header').find('.compact-toggle').click(function(){
						 $(this).parents('header').toggleClass('compact');
						 $(this).parents('header').find('.compact-toggle').toggleClass('open');
						 /*$(".compact-toggle.open").css({
							 "opacity":"1",
						 });*/
					 });
	
				$("#question-0-0,#question-0-1").click(function()
				{
					$(this).parents(".gender").removeClass("active").siblings(".products").addClass("active");
				});
				
				$('.questionnaire-form fieldset .buttons .next').click(function(){
					$(this).parents('fieldset').removeClass("active").next().addClass("active");
				});
				$('.questionnaire-form fieldset .buttons .prev').click(function(){
					$(this).parents('fieldset').removeClass("active").prev().addClass("active");
				});
	
		/*---END of Questionnaire---*/
		
	
		
	/*---Start of codes for A-Z Brands */
	
		    if($(window).resize().width()<=790){
		        var divId='#subcategory-'+$('.brandCategory').eq(0).attr('id');
		        $(divId).show();
		    }
	
		    $('.cmsBrands').each(function(){
		    	if($(this).find('.list_custom li').length == 0){
			    	   $(this).find('.nav-wrapper').hide();
			       }
		    });
		    $('.subBrands').each(function(){
		    	if($(this).find('.list_custom li').length == 0){
		    		$(this).find('.nav-wrapper').hide();
		    	}
		    });
		    $('.cmsMultiBrands').each(function(){
		    	if($(this).find('.list_custom li').length == 0){
		    		$(this).find('.nav-wrapper').hide();
		    	}
		    });
		$('select[name="test"]').change(function(){
			if($(this).find('.brandCategory').is(':selected')){
				var x=document.getElementById("mySelect").selectedIndex;
				var y=document.getElementById("mySelect").options;
				var i=y[x].index;
				$('.cmsBrands').hide();
		    	$(".subBrands").hide();
		    	$(".cmsMultiBrands").hide();
		    	/*$('.cmsBrands').eq(i).show();*/
		    	var divId='#subcategory-'+$(this).find('.brandCategory').eq(i).attr('id');
		        $(divId).show();
		       if($(divId).find('.list_custom li').length == 0){
		    	   $(divId).find('.nav-wrapper').hide();
		       }
		       
			}
		
		if($(this).find('.cmsManagedBrands').is(':selected')){
			var x=document.getElementById("mySelect").selectedIndex;
			var y=document.getElementById("mySelect").options;
			var i=y[x].index;
			var num=$('.brands-page .brands-left #mySelect .brandCategory').length;
			i=i-num;
			$('.cmsBrands').hide();
	    	$(".subBrands").hide();
	    	$(".cmsMultiBrands").hide();
	    	/*$('.cmsBrands').eq(i).show();*/
	    	var divId='#cmsManaged-'+$(this).find('.cmsManagedBrands').eq(i).attr('id');
	    	var divId1='#cmsMultiManaged-'+$(this).find('.cmsManagedBrands').eq(i).attr('id');
	    	$(divId).show();
	    	 $(divId1).show();
	    	 if($(divId).find('.list_custom li').length == 0){
		    	   $(divId).find('.nav-wrapper').hide();
		       }
	    	 if($(divId1).find('.list_custom li').length == 0){
		    	   $(divId1).find('.nav-wrapper').hide();
		       }
		}
		
				var leftListId=$(this).children(":selected").attr("id");
				$(".brands-page .brands-left ul > li").removeClass('active');
		    	$(".brands-page .brands-left ul > li>a").filter(function(){
	  				 return this.id === leftListId
						}).parent('li').addClass('active');
	});
		
	
			
			$('.cmsBrands').hide();
			$(".cmsMultiBrands").hide();
		  
		    $(".brandCategory").click(function(){
		    	$('.cmsBrands').hide();
		    	$(".subBrands").hide();
		    	$(".cmsMultiBrands").hide();
		    	var divId='#subcategory-'+$(this).attr('id');
		    	var selectOptionId=$(this).attr('id');
		    	$('#mySelect option').filter(function(){
	  				 return this.id === selectOptionId
						}).prop('selected', true);
		        $(divId).show();
		    });
		    $(".cmsManagedBrands").click(function(){
		    	$(".cmsBrands").hide();
		    	$(".subBrands").hide();
		    	$(".cmsMultiBrands").hide();
		    	var divId='#cmsManaged-'+$(this).attr('id');
		    	var divId1='#cmsMultiManaged-'+$(this).attr('id');
		    	var selectOptionId=$(this).attr('id');
		    	$('#mySelect option').filter(function(){
	  				 return this.id === selectOptionId
						}).prop('selected', true);
		    	 $(divId1).show();
		        $(divId).show();
		       
		    });
	
	
			$(".saved-cards .number .view-details").click(function(e){
				if ($(e.currentTarget).parent().hasClass('active'))
					{
					$(this).parent().removeClass("active");
					}
				else
					{
					$(this).parent().addClass("active");
					}
			});
			
		atoz_listing();
	    $('.brands-page .list_custom li span.letter').each(function(){
	    	if((($(this).text().toUpperCase().charCodeAt(0)>47) &&($(this).text().toUpperCase().charCodeAt(0)<58)) || (($(this).text().toUpperCase().charCodeAt(0)>64) &&($(this).text().toUpperCase().charCodeAt(0)<72))){
	    		
	    		$(this).addClass('a-g-set');
	    	}
	    	else if((($(this).text().toUpperCase().charCodeAt(0)>71) &&($(this).text().toUpperCase().charCodeAt(0)<79))){
	    		
	    		$(this).addClass('h-n-set');
	    	}
		else if((($(this).text().toUpperCase().charCodeAt(0)>78) &&($(this).text().toUpperCase().charCodeAt(0)<86))){
	    		
	    		$(this).addClass('o-u-set');
	    	}
		else{
			
			$(this).addClass('v-z-set');
		}
	    });
	
	$(".brands-page .list_custom li").hide();
		$(".brands-page .list_custom li span.letter.a-g-set").parent().show();
				
		 // TPR-1287 Start
	    $(".brandCategory").click(function(){
	    	
	    	var elementId =$(this).attr('id') ;
	    	var brandCatName = $(this).text().toLowerCase().trim() + "-brands";
	    		    	
	    	//window.history.pushState('obj', 'newtitle', '/brands/brandlist?cat='+elementId);
	    	window.history.pushState('obj', 'newtitle', '/brands/' + brandCatName +'/b-'+elementId.toLowerCase());
	    	
	    	
	    });
	    
	    
	    $(".cmsManagedBrands").click(function(){
	    	
	    	var elementId =$(this).attr('id') ;
	    	
	    	var brandCatName = $(this).text().toLowerCase().trim().replace(/\s/g, "-") + "-brands";
	    	
	    	//window.history.pushState('obj', 'newtitle', '/brands/brandlist?cat='+elementId);
	    	window.history.pushState('obj', 'newtitle', '/brands/' + brandCatName +'/b-'+elementId);
	    	
	    });
	  // TPR-1287 end
	
	    
	    $(".brands-page .desktop ul.nav > li").click(function() {
	    	$(".brands-page .desktop ul.nav > li").removeClass("active");
	    	$(this).addClass("active");
	    	var listId=$(this).attr('id');
	    	var letterid=".brands-page .desktop ul.nav > li#"+listId;
	    	var clickedBlock=".brands-page .list_custom li span.letter."+listId;
	    	$('.brands-page .list_custom li').hide();
	    	$(clickedBlock).parent().show();
	    	$(letterid).addClass('active');
			  $("body,html").animate({ scrollTop: $(this).parents('.desktop').offset().top }, "slow");

		});
	    $(".brands-page .brands-left ul > li").click(function() {
	    	$(".brands-page .brands-left ul > li").removeClass("active");
	    	$(this).addClass("active");
	    });
	    
	    
	/*---END of codes for A-Z Brands */
	
	/*-----Start of  for static benefits section --- */
	
	function benifits_height() {
		$('.benefits ul li').each(function(count) {
			var liHeight = ($('.sign-in .tabs li.active').height()+20)/3;
			var liPadding = $('.benefits ul li').eq(count).find("div:last-child").height();
		//	console.log("active"+liHeight);
			$('.benefits ul li').eq(count).css({
													"height" : liHeight+1,
													"padding" : (liHeight-liPadding)/2+"px 20px"
			
												});
			
		});
	}
	
		
		$('.sign-in .nav li, #sign_in_content .button_fwd_wrapper button, #sign_up_content .form-actions button').click(function(){
			benifits_height();
		});
		$(window).on("load resize",function(){
			if($(window).width() >= 790 ) {
				benifits_height();
			}
		});
		
	/*--END of  for static benefits section----- */
	
/* ---Start of Mobile view left nav --*/
		
		if($(window).width() < 790) {
			$(document).on("click","header .content .top .toggle",function(){
				if($(this).parent().hasClass("active")){
					$(this).parent().siblings(".overlay").addClass("overlay-sideNav");
					$("body").css("overflow","hidden");
				}
				else {
					$(this).parent().siblings(".overlay").removeClass("overlay-sideNav");
					$("body").css("overflow","auto");
				}
				
			});
		}
		
	/*---END of Mobile view left nav --*/	
		
		
		
		
		
	/*----Start of Wishlist Codes---------*/
		
		$('.js-rename-wishlist').click(function(){
			$('.rename-wishlist-container').hide();
			$(this).prev('.rename-wishlist-container').toggle();
			$('.js-rename-wishlist').show();
			$('.wishlist-name').show();
			$(this).toggle();
			$(this).siblings('.wishlist-name').toggle();
			
		});
		$('.modal').click( function (event) {
		
		    if(!($(event.target).hasClass('js-rename-wishlist')) && !($(event.target).hasClass('rename_link')) && !($(event.target).hasClass('rename-input'))){
		    	$('.rename-wishlist-container').hide();
		    	$('.js-rename-wishlist').show();
				$('.wishlist-name').show();
		    }
	
		});
		
		/*if($('body').find('a.wishlist#wishlist').length > 0){
		$('a.wishlist#wishlist').popover({ 
	    html : true,
	    content: function() {
	      return $(this).parents().find('.add-to-wishlist-container').html();
	    }
	  });
	  }
	  if($('body').find('input.wishlist#add_to_wishlist').length > 0){
		$('input.wishlist#add_to_wishlist').popover({ 
			html : true,
			placement: function (){
				if(($(window).width() < 768)) {
				return 'top';
				}
				else
					return 'bottom';
			},
			content: function() {
				return $(this).parents().find('.add-to-wishlist-container').html();
			}
		});
	  }*/
	  if($('body').find('a.cart_move_wishlist').length > 0){
		$('a.cart_move_wishlist').popover({ 
			html : true,
			content: function() {
				return $('.add-to-wishlist-container').html();
			}
		});
		}
		if($('body').find('ul.wish-share a.mailproduct').length > 0){
		$('ul.wish-share a.mailproduct').popover({ 
				html : true,
				content: function() {
					var loggedIn = $("#loggedIn").val();
					if(loggedIn=='true') {
						return $(this).parents().find('#emailSend').html();
					} else {
						return '<div style="padding: 10px;">'+$(this).parents().find('#emailLoggedInId').html()+'</div>';
						
					}
				}
			});
			}
		$('ul.wish-share div.share').mouseleave(function(){
			 $(this).find('[data-toggle="popover"]').popover('hide');
		});
	  $('body').on('click', function (e) {
	    $('[data-toggle="popover"]').each(function () {
	        
	        if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
	            $(this).popover('hide');
	        }
	    });
	});
	  
	  $('.create-newlist-link').click(function(){
		  $('header .content .top').toggleClass('no-z-index');
	  });
	  $('#createNewList').find("div.overlay").click(function(){
		  $('header .content .top').removeClass('no-z-index');
	  });
	  $('#createNewList').find("a.close").click(function(){
		  $('header .content .top').removeClass('no-z-index');
	  });
	  $('#createNewList').find("button.close").click(function(){
		  $('header .content .top').removeClass('no-z-index');
	  });
	  
	/*--------END of wishlist Codes------*/
	  
	 /*--- Start of function for Internet Explorer video z-index overlapping---*/
	
	  var ua = window.navigator.userAgent;
	  var msie = ua.indexOf("MSIE ");
	
	  if (msie > 0)
	  {      // If Internet Explorer, return version number
		  // alert(parseInt(ua.substring(msie + 5, ua.indexOf(".", msie))));
	
		  $('iframe').each(function(){
			  var url = $(this).attr("src");
			  $(this).attr("src",url+"?wmode=transparent");
		  });
	  }
	  if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)){
		  $("#js-site-search-input").attr("readonly","readonly");
		  $("#mailtext").attr("readonly","readonly");
			$('#js-site-search-input').on( 'click', function() {
			    $(this).removeAttr('readonly').focus().select();
			});
			
			$('#js-site-search-input').on( 'blur', function() {
			    $( this ).prop( 'readonly', 'readonly' );
			});
			$('#mailtext').on( 'click', function() {
				$(this).removeAttr('readonly').focus().select();
			});
			
			$('#mailtext').on( 'blur', function() {
				$( this ).prop( 'readonly', 'readonly' );
			});
			
		  
	  }
	 /*---END  of function for Internet Explorer video z-index overlapping ends----*/
		
		$(".checkout-shipping #addressForm input[type='checkbox']").change(function () {
		    if ($(this).prop( "checked" )===true) {
		        // checked
		       $(this).parent().css("color","#a9143c");
		       $(this).parent().addClass('checkbox-checked');
		       
		    }
		    else {
		    	$(this).parent().css("color","#666");
		    	$(this).parent().removeClass('checkbox-checked');
		    	}
		});
	
		/*---------Start of compareNow----------*/

		var comparsionProductsLength=$(".products-compareTable").find("td").length;
	
		if(comparsionProductsLength%3==0)
		{
			$(".products-compareTable tr td ,.comparison-table.stats tbody tr td ").css({
				"width":"33.3%",
			})
		}
		else if(comparsionProductsLength%2==0 && comparsionProductsLength%4==0)
		{
			$(".products-compareTable tr td ,.comparison-table.stats tbody tr td ").css({
				"width":"25%",
			})
		}
		else if(comparsionProductsLength%5==0)
		{
			$(".products-compareTable tr td ,.comparison-table.stats tbody tr td ").css({
				"width":"20%",
			});
		}
		
	/*---------END of compareNow ----------*/
		        
	
	
	/*-------- Script from Functionality team ( JS.tag script) -------*/
	
	
				var contextPath = ACC.config.contextPath;
				$("input[name=yes]").click(function() {
					$(".search-feedback").hide();
					$(".feed-back-categories").hide();
					$(".feed-back").hide();
					$(".feed-back-form").fadeIn();
				});
	
				$("input[name=no]").click(
						function() {
							$(".search-feedback").hide();
							$(".feed-back-form").hide();
							$(".feed-back-categories").show();
							//ajax call for categories
	
							$.ajax({
								url : contextPath
										+ "/feedback/searchcategotylist",
								type : "GET",
								returnType : "JSON",
								success : function(data) {
									listSelect = "";
								//	console.log(data);
									$.each(data, function(k, v) {
										if(v != null){
											listSelect += '<option value="'+v+'">'
													+ v + '</option>';
											}
									});
								//	console.log(listSelect);
									$("#feedCategory").html(listSelect);
									$(".feed-back").show();
								},
								fail : function(data) {
									alert("Failed to load categories");
								}
							});
						});
	
				$("#feedCategory").change(function() {
					$(".feed-back").show();
				});
	
				//ajax call for saving feedback on Yes
	
				$("input[name=submitFeedBackYes]").click(function() {
					var params = $("#feedBackFormYes").serialize();
					$.ajax({
						url : contextPath + "/feedback/feedbackyes",
						type : "GET",
						returnType : "text",
						data : params,
						success : function(data) {
							if (data == "SUCCESS") {
								alert("Thanks For your FeedBack");
								$(".feed-back-categories").hide();
								$(".feed-back").hide();
								$(".feedback-thankyou").fadeIn();
							}
	
						},
						fail : function(data) {
							alert("Failed to load categories");
						}
					});
				});
	
				$("input[name=submitFeedBackNo]").click(function() {
					var params = $("#feedBackFormNo").serialize();
					if(!validateEmailOnSubmit() && !validateFeedbackOnSubmit()) {
						return;
					}
					
					if(!validateEmailOnSubmit() || !validateFeedbackOnSubmit()) {
						return;
					}
					
					$.ajax({
						url : contextPath + "/feedback/feedbackno",
						type : "GET",
						returnType : "text",
						data : params,
						success : function(data) {
							if (data == "SUCCESS") {
								$(".search-feedback").hide();
								$(".feed-back-categories").hide();
								$(".feed-back").hide();
								$(".feed-back-form").fadeIn();
								/*TPR-4730*/
								if(typeof utag !="undefined"){
									utag.link({ link_text: 'search_feedback_no_submit', event_type : 'search_feedback_no_submit' });
									}	
							}
						},
						fail : function(data) {
							alert("Failed to load categories");
							/*TPR-4730*/
							if(typeof utag !="undefined"){
								utag.link({ error_type : "search_feedback_error" });
								}
							
						}
					});
				});
		
	
				$(".share-trigger").click(
						function() {
							if ($(this).parent().siblings(".share-wrapper").is(
									":visible")) {
	
								$(this).parent().siblings(".share-wrapper")
										.hide();
							} else {
								$(".share-wrapper").hide();
								$(this).parent().siblings(".share-wrapper")
										.show();
							}
						});
				$(".share-wrapper-buybox").hide();
				$(".share-trigger-buybox").click(function() {
					$(".share-wrapper-buybox").toggle();
				});
	
	/*---	END from Functionality team ( JS.tag script) ----*/
				
	/*----- Start of SERP pagination ----*/
				
	/*	$(".pagination").parents(".bottom-pagination").find("li.prev a").append("<span class='lookbook-only'> Page</span>");
		$(".pagination").parents(".bottom-pagination").find("li.next a").append("<span class='lookbook-only'> Page</span>");*/
		
	/*----- END of SERP pagination ----*/		
		
	
	
	
	/*--------Start of PDP pincode placeholder removing on focus ----*/
	
		
		$("#pin").focus(function(){
			$(this).attr("placeholder","");
			$(this).siblings(".placeholder").hide(); /* Fix for IE */
		});
		
		$("#pin").blur(function(){
			if($("#pin").val()=="")
			$(this).attr("placeholder","Pincode");
		});
		
	/*--------END of PDP pincode placeholder removing on focus ----*/	
		
	
	/*---- Start of added for hero products demarcation ---*/
	
	if($(".product-listing.product-grid.hero_carousel .owl-stage").children().length>0){
		$(".product-listing.product-grid.hero_carousel").css("border-bottom","1px solid #e5e5e5");
		$(".product-listing.product-grid.hero_carousel").before("<h3 class='heroTitle'>Shop Our Top Picks</h3>");
		
	}
	
	/*---- END of added for hero products demarcation ---*/
	
	/*---Start of Authentic and exclusive function call---*/
	
		 authentic();
	
		 $(window).on("load resize",function(){
				if($(".js-mini-cart-count").text() != undefined && $(".js-mini-cart-count").text()!=null)
					{
					if($(".js-mini-cart-count-hover").text().length > 1){
						$(".responsive-bag-count").text($($(".js-mini-cart-count-hover")[0]).text());		/*UF-249*/
					}else{
						$(".responsive-bag-count").text($(".js-mini-cart-count-hover").text());		/*UF-249*/
					}
					}
				var $li = $(".page-authenticAndExclusive ul.feature-brands li");
				if($(window).width() <790) {
					$li.css("min-height","0");
				} else {
					 authentic();
				}
			});
	
	/*---END of Authentic and exclusive function call---*/

	/*--- Start of  Mobile view Left menu Sign In toggle---- */
	$(window).on("load resize",function(e){
			var style=null;
			if($(window).width() < 773) {
				  $(document).off("click","span#mobile-menu-toggle").on("click","span#mobile-menu-toggle",function() {
					$("a#tracklink").mouseover();
					$(this).parent('li').siblings().children('#mobile-menu-toggle').removeClass("menu-dropdown-arrow");
					$(this).parent('li').siblings().find('#mobile-menu-toggle + ul').slideUp();
					$(this).next().slideToggle();
					$(this).toggleClass("menu-dropdown-arrow");
					
					
					/* $(this).parent('li').siblings("li.short.words").nextAll().each(function(){
						 alert('1')
					      if($(this).hasClass("short")) {
					        return false;
					      }
					     $(this).siblings("li.long.words").hide(200);
					    });
					    $(this).parent('li.short.words').nextAll().each(function(){
					    	alert('2')
						      if($(this).hasClass("short")) {
						        return false;
						      }
						      $(this).toggle(200);
					    });*/
					
				});
				  $(document).on("click","ul.words span#mobile-menu-toggle",function() {
					var id = $(this).parents('ul.words').siblings("div.departmenthover").attr("id"), ind = $(this).parent('li.short.words').index("."+id+" .short.words")
						$(".long.words").hide();
						div_container=$(this).parent(".short.words");
						if($(this).hasClass('menu-dropdown-arrow')){
							/*for(var i = $("."+id+" .short.words").eq(ind).index(); i < $("."+id+" .short.words").eq(ind+1).index()-1; i++) {
								$("."+id+" .long.words").eq(i-ind-1).show();
							}
							if(ind == ($("."+id+" .short.words").length/2)-1) {
								$("."+id+" .short.words").eq(ind).nextAll().show();
							}*/
							for(var i=1;i<$(".long.words").length;i++){
								if(div_container.next().hasClass("long")){
									div_container.next().show();
									div_container=div_container.next();
								}
								else
									break;
								}		
						} else {
							$(".long.words").hide();
						}
						longStyleContainer=$(this).parent(".short.words").next(".long.words").attr("style");
						$(this).parents("li.level1").nextAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
						$(this).parents("li.level1").prevAll("li.level1").find(".short").children("#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
				  });
				/*--- Mobile view shop by brand and department ---*/

				// $("li.short.words").siblings("li.long.words").hide();
			/*	$(document).off("click","li.short.words ").on("click","li.short.words",function() {
				    $(this).siblings("li.short.words").nextAll().each(function(){

				      if($(this).hasClass("short")) {
				        return false;
				      }

				      $(this).siblings("li.long.words").hide(200);
				    });
				    $(this).nextAll().each(function(){

					      if($(this).hasClass("short")) {
					        return false;
					      }

					      $(this).toggle(200);
					    });

				  });
*/
				/*--- Mobile view shop by brand and department ---*/ 
			}
			else {
				$("#mobile-menu-toggle").next().attr("style",style);
				$("li.short.words,li.long.words").next().attr("style",style); 
			}
			
			div_container=$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow").parent(".short.words");
			for(var i=1;i<$(".long.words").length;i++){
				if(div_container.next().hasClass("long")){
					div_container.next().attr("style",longStyleContainer);
					div_container=div_container.next();
				}
				else
					break;
				}	
			
		});
	$(".customer-care-tabs>.tabs .customer-care-tab>ul>li .tabs li").click(function(){
							$(this).nextAll().removeClass( "active" );
							$(this).prevAll().removeClass( "active" );
	 });	 
		
		/*--- END of  Mobile view Left menu Sign In toggle---- */
	/*--- Start of  Mobile view sort by arrow in SERP and PLP---- */
	
	/*$(".progtrckr .progress.processing").each(function(){
		var len = $(this).children("span.dot").length;
		if(len == 2) {
			$(this).children("span.dot").first().css("marginLeft","16.5%");
		} else if(len == 1) {
			$(this).children("span.dot").first().css("marginLeft","33%");		TPR-6013 Order History 
		}

	}); */
	$(".progtrckr .progress.processing").each(function(){
		var len = $(this).children("span.dot").length;
		if(len == 3) {
			$(this).children("span.dot").css("marginLeft","12%");
		}
		if(len == 2) {
			$(this).children("span.dot").css("marginLeft","16.5%");
		} 
		
		if(len == 4) {
			$(this).children("span.dot").css("marginLeft","8%");
		} 

	});
	$(".progtrckr").each(function(){
		//$(this).find(".progress.processing .dot:not(.inactive)").last().find('img').show();
		if($(this).find(".progress.processing .dot:not(.inactive)").last().next(".message").css("display") == "block"){
			$(this).find(".progress.processing .dot:not(.inactive)").last().find('img').show(); //TISPRDT-1570
		}
	});	/*TPR-6013 Order History */
	
	/*$(window).on("load resize",function(){
		if($(window).width()<651)
			{
		$('.feature-collections ul.collections li.chef.sub .simple-banner-component').each(function(){
		var h3_height= $(this).find('h3').height();
		if(h3_height>0){
			h3_height=h3_height + 30;
			$(this).siblings().css('padding-top',h3_height);
		}
		
		});
			}
	});*/
	if ($("#searchPageDeptHierTree").children().length==0){
		
		$("#searchPageDeptHierTree").css("padding","0px");
		$("#searchPageDeptHierTree").parent("form").siblings('li.facet').first().css("border-top","0px");
	}
	if ($(".customer-service").find(".side-nav").length == 0){
		$(".customer-service .left-nav-footer-mobile").css("display","none");
	}
	else{
		$(".customer-service .left-nav-footer-mobile").css("display","block");
	}
	$(".customer-service .side-nav ul li").each(function(){
		var title = $(this).find("a").attr("title");
		var link = $(this).find("a").attr("href");
		if ($(this).hasClass("active")){
			$(".customer-service .left-nav-footer-mobile").append("<option value="+link+" data-href="+link+" selected>"+title+"</option>");
		}
		else{
		$(".customer-service .left-nav-footer-mobile").append("<option value="+link+" data-href="+link+">"+title+"</option>");
		}
	});
	if($("#sameAsShipping").is(":checked")){
		$("#sameAsShipping").prev('h2').hide();
	}
	else{
		$("#sameAsShipping").prev('h2').show();
	}
	$("#sameAsShipping").click(function(){
		if($("#sameAsShipping").is(":checked")){
			$("#billingAddress fieldset .error-message").html("");
			$(this).prev('h2').hide();
		}
		else{
			$(this).prev('h2').show();
		}
		});

	$(window).on("load resize", function() {
		var mainImageHeight = $(".main-image").find("img.picZoomer-pic").height();
		var thumbnailImageHeight = (mainImageHeight / 5);
			$(".imageList ul li img").css("height", thumbnailImageHeight);
		});
	
	$('.marketplace-checkout').find('a').click(function(e){
		e.preventDefault();
	});
	$(".marketplace-checkout").parents().find('header .content .top .marketplace.compact a').hide();
	  $(window).scroll(function () {
		  if($(".ui-autocomplete").is(":visible") && marketplaceHeader){
			  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css({
				  /*start change of INC144313747*/
				  /*left : $('#js-site-search-input').offset().left,*/
				  /*end change of INC144313747*/
				  width: $('#js-site-search-input').outerWidth()
			  });
		  }
		  
	  });
	  $('#js-site-search-input').keydown(function () {
		  $("#js-site-search-input").parents('form#search_form').next('.ui-autocomplete.ui-front.links.ui-menu').css({
			      /*start change of INC144313747*/  
			  	  /*left : $('#js-site-search-input').offset().left,*/
			  	  /*end change of INC144313747*/
				  width: $('#js-site-search-input').outerWidth()
			  });
	  });
	  
	  $("input[name='j_username']").parents("form#extRegisterForm").attr("autocomplete","off");
		$("input[name='email']").parents("form#loginForm").attr("autocomplete","off");
		$("input[name='j_username'],input[name='email']").attr("autocomplete","off");
		$("input[type='password']").attr("autocomplete","new-password");
		$(window).on("load resize", function() {
			var $li = $("body .account .right-account.rewards>div.your-activity>ul.coupon-container .coupon-box");
			var top_margin=$li.css("margin-top");
			if (top_margin == '0px') {
			for(var i = 0; i < $li.length; i+=3) {
				var first_elem,second_elem,third_elem;
				first_elem=parseInt($li.eq(i).find("h2").css("height")) + parseInt($li.eq(i).find("p.coupon_count").css("height")) + parseInt($li.eq(i).find("div.left").css("height"));
				second_elem=parseInt($li.eq(i+1).find("h2").css("height")) + parseInt($li.eq(i+1).find("p.coupon_count").css("height")) + parseInt($li.eq(i+1).find("div.left").css("height"));;
				third_elem=parseInt($li.eq(i+2).find("h2").css("height")) + parseInt($li.eq(i+2).find("p.coupon_count").css("height")) + parseInt($li.eq(i+2).find("div.left").css("height"));;
				var li_max_height=Math.max(first_elem,second_elem,third_elem) + 30;
				$li.eq(i).css("height",li_max_height);
				$li.eq(i+1).css("height",li_max_height);
				$li.eq(i+2).css("height",li_max_height);
			}
			var remaining_li=$li.length % 3;
			if(remaining_li==2){
				first_elem=parseInt($li.eq($li.length - 1).find("h2").css("height")) + parseInt($li.eq($li.length - 1).find("p.coupon_count").css("height")) + parseInt($li.eq($li.length - 1).find("div.left").css("height"));;
				second_elem=parseInt($li.eq($li.length - 2).find("h2").css("height")) + parseInt($li.eq($li.length - 2).find("p.coupon_count").css("height")) + parseInt($li.eq($li.length - 2).find("div.left").css("height"));;
				var li_max_height=Math.max(first_elem,second_elem) + 30;
				$li.eq($li.length - 1).css("height",li_max_height);
				$li.eq($li.length - 2).css("height",li_max_height);
			}
			}
			else{
				for(var i = 0; i < $li.length; i+=2) {
					var first_elem,second_elem;
					first_elem=parseInt($li.eq(i).find("h2").css("height")) + parseInt($li.eq(i).find("p.coupon_count").css("height")) + parseInt($li.eq(i).find("div.left").css("height"));
					second_elem=parseInt($li.eq(i+1).find("h2").css("height")) + parseInt($li.eq(i+1).find("p.coupon_count").css("height")) + parseInt($li.eq(i+1).find("div.left").css("height"));
					var li_max_height=Math.max(first_elem,second_elem) + 30;
					$li.eq(i).css("height",li_max_height);
					$li.eq(i+1).css("height",li_max_height);
				}
				var remaining_li=$li.length % 2;
				if(remaining_li==1){
					$li.eq($li.length - 1).css("height",'auto');
				}
			}
		});
		
		if ('ontouchstart' in window) {
			$('body').addClass("touchDevice");
	 		$("header .content nav > ul > li > ul > li > .toggle a").attr("href","#nogo");
			}
		
		if($('.lookbook_wrapper .bottom-pagination').children().length==0){
			$('.lookbook_wrapper .bottom-pagination').css('padding','0');
			}
			if($('body .lookbook_wrapper .lookbook-pagination').children().length==0){
				$('body .lookbook_wrapper .lookbook-pagination').css('padding','0');
			}
			if($('.lookbook_wrapper .listing.wrapper .product-listing.product-grid').children().length==0){
			$('.lookbook_wrapper .listing.wrapper .product-listing.product-grid').parents().find('.listing.wrapper').css('height','0px');
			}

		if($('.promo-block .promo-img').children().length == 0){
			$('.promo-block .pdp-promoDesc').css({'float':'none','margin':'0px auto'});
		}
					
			$(document).on("click",".mini-cart-close",function(){
				$(this).parents('.mini-transient-bag').remove();
			});
			
			/*$(document).on("click","#addToCartButton, .serp-addtobag.js-add-to-cart",function(){
				if($(window).width() > 773) {
					$("#cboxClose").click();
				 $('html,body').animate({
				            scrollTop: 0
				        }, 500);
				 
				} 
				
				});*/
		/*	$('.tata-rewards').popover({
			    html: 'true',
			    placement: 'top',
			    trigger: 'hover',
			    content: $(".reward-popover").html()
			});*/
			
			$('.tata-rewards').popover({
				html: 'true',
			    placement: 'top',
			    trigger: 'manual',
			    content: $(".reward-popover").html()
		    }).on("mouseenter", function () {
		        var _this = this;
		        $(this).popover("show");
		        $(this).siblings(".popover").on("mouseleave", function () {
		            $(_this).popover('hide');
		        });
		    }).on("mouseleave", function () {
		        var _this = this;
		        setTimeout(function () {
		            if (!$(".popover:hover").length) {
		                $(_this).popover("hide")
		            }
		        }, 100);
		    });
		$(document).on("click",'.select-size',function() {
			$(this).toggleClass('active');
		});
		var selectOpen = false;
		$(document).on("mouseleave",'.select-size',function() {
			if($('.select-size').hasClass("active")) {
				selectOpen = true;
			}
		});
		$(document).on("click",function() {
			if(selectOpen) {
				$('.select-size').removeClass('active');
				selectOpen = false;
			}
		});
		/*start change for INC_11789*/
		/*if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
			var footer_height=$('footer').height() + 20 + 'px';
			$(".body-Content").css('padding-bottom',footer_height);
		}*/
		if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
			var footer_height=$('footer').height() + 20 + 'px';
			var ua_check = window.navigator.userAgent;
			var msie_check = ua_check.indexOf("MSIE ");
			$(".body-Content").css('padding-bottom',footer_height);
			$("body.page-homepage .body-Content").css('padding-bottom',$('footer').height() + 'px');
			if(msie_check > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)){
				setTimeout(function () {
					var footer_height=$('footer').height() + 20 + 'px';
					$(".body-Content").css('padding-bottom',footer_height);
					$("body.page-homepage .body-Content").css('padding-bottom',$('footer').height() + 'px');
				},500);
				}
		} 
		/*end change for INC_11789*/
		else{
			$(".body-Content").css('padding-bottom','0px');
		}
		
		$(window).on("resize", function() {
			if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
				var footer_height=$('footer').height() + 20 + 'px';
				$(".body-Content").css('padding-bottom',footer_height);
				$("body.page-homepage .body-Content").css('padding-bottom',$('footer').height() + 'px');
			}
			else{
				$(".body-Content").css('padding-bottom','0px');
			}
		});		
		
		if (navigator.userAgent.indexOf('Safari') > 0 && navigator.userAgent.indexOf('Chrome') < 0) {
			$("body").addClass('safariBrowser');
			/*if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
				$('header .content nav > ul > li:first-child + li').css('margin-left','25px');
			}*/
		}
		var resize_sbb;
		$(window).resize(function(){
			clearTimeout(resize_sbb);
			resize_sbb = setTimeout(function(){
				if (navigator.userAgent.indexOf('Safari') > 0 && navigator.userAgent.indexOf('Chrome') < 0) {
					if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
						$('header .content nav > ul > li:first-child + li').css('margin-left','25px');
					} else {
						$('header .content nav > ul > li:first-child + li').css('margin-left','0px');
					}
				}
			},100);
		});
		
		$('header .content nav > ul > li').on('touchend',function(){
			if($('header div.bottom .marketplace.linear-logo').css('display') == 'none' && $('body').hasClass('touchDevice')){
				$('header .content nav > ul > li').children('ul').css("height","0px");
				if($(this).children('ul').height() > 0) {
					$(this).children('ul').css("height","0px");
				} else {
					$(this).children('ul').css("height","450px");
				}
			}
				
		});

		$('header .content nav > ul > li ul').on('touchend',function(e){
			e.stopPropagation();
		});

		
		$(document).on('touchend',function(e){
			e.stopPropagation();
			if(!$(e.target).parents().is('nav') && $('body').hasClass('touchDevice') && $('header div.bottom .marketplace.linear-logo').css('display') == 'none') {
				$('header .content nav > ul > li').children('ul').css("height","0px");
			} 
				
			if(!$(e.target).parents().hasClass('select-list') && $('body').hasClass('touchDevice')) {
				$('.select-view .select-list').removeClass('touch_click');
				$('.select-view .select-list ul').css({'max-height':'0','border-color':'transparent'});
			}
		});

		$(document).on('touchend','.select-view .select-list',function(){
			if($('body').hasClass('touchDevice')){
				$('.select-view .select-list').removeClass('touch_click');
				if($(this).children('ul').height() > 2) {
					$(this).removeClass('touch_click');
					$(this).find('ul').css({'max-height':'0','border-color':'transparent'});
				} else {
					$(this).addClass('touch_click');
					$(this).find('ul').css({'max-height':'500px','border-color':'#dfd1d5'});	
				}
			}

		});

		$(document).on('touchend','.select-view .select-list ul',function(e){
			$('.select-view .select-list').removeClass('touch_click');
			$('.select-view .select-list ul').css({'max-height':'0','border-color':'transparent'});
			e.stopPropagation();
		}); 
		//TISPRO-480
		$('.sign-in-dropdown').mouseleave(function(){$('.sign-in-dropdown input').blur();});
		
		$(document).on('touchend','.select-view .select-list ul li',function(e){
			$(this).click();
		});
		$(window).on("load resize", function() {
			var filter_height = 0;
			if ($(".searchSpellingSuggestionPrompt").is(":visible")) {
				filter_height=$(".searchSpellingSuggestionPrompt").outerHeight() + 96; /* PRDI-69 */
			 /*else {
				filter_height=$(".facet-list.filter-opt").height() + 32;
			}*/
			$(".listing.wrapper .left-block").css("margin-top",filter_height+"px");
			}
		});
		
		$('.checkout.wrapper .product-block.addresses li.item .addressEntry').click(function(){
			$(this).find('input:radio[name=selectedAddressCode]').prop('checked', true);
			});
		
		//Mobile menu
		navhtml = $("nav").html();	
		
		$('header .content .container > .right').prepend(navhtml);
		$('header .content .container > .right ul:first-child > li div').removeClass('toggle');
		$('header .content .container > .right ul li #mobile-menu-toggle + ul li ul.words li.long div').removeClass('toggle');
		$('header .content .container > .right ul li #mobile-menu-toggle + ul li ul li').removeClass('toggle');
		$("ul li #mobile-menu-toggle+ul li ul.words li.long").unwrap("ul"); //TISPRDT-1296, TISPRDT-1300
		$("ul li #mobile-menu-toggle+ul li ul.words li.long").unwrap(".l2_wrapper"); //TISPRDT-1296, TISPRDT-1300
		setTimeout(function () {
  		  navhtmlMicrosite = $(".brand-header nav ul li").html();
  		 $('header .content .container > .right > ul:first-child').prepend('<li id="shopMicrositeSeller"></li>');
  		  $('header .content .container > .right > ul:first-child > li#shopMicrositeSeller').html(navhtmlMicrosite);
  		$('header .content .container > .right ul:first-child > li#shopMicrositeSeller div').removeClass('toggle');
		$('header .content .container > .right ul li#shopMicrositeSeller #mobile-menu-toggle + ul li ul.words li.long div').removeClass('toggle');
		$('header .content .container > .right ul li#shopMicrositeSeller #mobile-menu-toggle + ul li ul li').removeClass('toggle');
	        }, 50);
		
		$(document).off("click","header .content .container > .right > ul:first-child > li#shopMicrositeSeller li.level1 > div > span#mobile-menu-toggle").on("click", "header .content .container > .right > ul:first-child > li#shopMicrositeSeller li.level1 > div > span#mobile-menu-toggle",function(){
			$(this).parents(".level1").siblings().find("span#mobile-menu-toggle").removeClass("menu-dropdown-arrow");
			$(this).parents(".level1").siblings().find(".words").hide();
			$(this).parents(".level1").find(".words").toggle();
			
			
			
		});
		
		//Mobile level 1 active
		$(document).on('click','header > .content .top ul:first-child > li > span.mainli',function() {
			if($(this).prev().hasClass('bgred')){
				$(' header > .content .top ul:first-child li > div').removeClass("bgred");
				}else{
				$(' header > .content .top ul:first-child li > div').removeClass("bgred");
				$(this).prev().addClass("bgred");			
			}		
		});
	/*	$(document).on('click','header > .content .top ul:first-child li#shopMicrositeSeller > span#mobile-menu-toggle',function() {
			setTimeout(function () {
			if($(this).prev().hasClass('bgred')){
				console.log("inside if microsite");
				$(' header > .content .top ul:first-child li#shopMicrositeSeller > div').removeClass("bgred");
			}else{
				$(' header > .content .top ul:first-child li#shopMicrositeSeller > div').removeClass("bgred");	
				console.log("inside else microsite");
				$(this).prev().addClass("bgred");			
			}
			 }, 190);
		});*/

		//loadGigya();
		var sort_top=parseInt($(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top"));
		$(window).on("load resize", function() {
			if($(window).width() <= 773){
				/*$('.listing.wrapper .left-block').css('margin-top','20px');*/ /* PRDI-69 */
				var search_text_height = $(".listing.wrapper .search-result h2").height();
				var search_spelling_height = $(".searchSpellingSuggestionPrompt").height();
				
				if((search_text_height > 14) && (search_spelling_height <= 0)){
					var sort_top1 = sort_top + (search_text_height - 14);
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top1+"px");
				}
				else if((search_text_height <= 14) && (search_spelling_height > 0)){
					var sort_top_2=$(".searchSpellingSuggestionPrompt").height() + sort_top + 20;
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top_2+"px");
				}
				else if((search_text_height > 14) && (search_spelling_height > 0)){
					var sort_top3 = (search_text_height - 14) + $(".searchSpellingSuggestionPrompt").height() + sort_top + 20;
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top3+"px");
				}
				else{
					$(".listing.wrapper .right-block .listing-menu>div .wrapped-form.sort.mobile").css("top",sort_top+"px");
				}
				if($(".searchSpellingSuggestionPrompt").height()>0){
					var left_block_top_margin= $(".searchSpellingSuggestionPrompt").height() + 96; /* PRDI-69 */
					$('.listing.wrapper .left-block').css('margin-top',left_block_top_margin+'px');
				}
			}
		});
		// commented as part of UI-UX Fixes cart page UF-61
		//setTimeout(function () {
/*		$("body.page-cartPage .cart.wrapper .product-block li.item>ul.desktop>li.delivery").addClass("collapsed");
				$(".mobile-delivery").click(function(){
					$(this).parents("li.delivery").toggleClass("collapsed");
				});
*/		//}, 100);
		$(window).on("load resize", function() {
			$("body.page-cartPage .cart.wrapper .product-block li.item").each(function(){
				if($(this).find("ul.desktop>li.price").css("position")=="absolute"){
					var price_height=$(this).find("ul.desktop>li.price").height() + 20;
					$(this).find(".cart-product-info").css("padding-bottom",price_height+"px");
					var price_top = $(this).find(".cart-product-info").height() + 8;
					$(this).find("ul.desktop>li.price").css("top",price_top+"px");
					var qty_top = price_top + $(this).find("ul.desktop>li.price").height() + 11;
					$(this).find("ul.desktop>li.qty").css("top",qty_top+"px");
				}
				else{
					$(this).find(".cart-product-info").css("padding-bottom","0px");
					$(this).find("ul.desktop>li.price").css("top","auto");
					$(this).find("ul.desktop>li.qty").css("top","auto");
				}
			});
			if($("body.page-cartPage .cart.wrapper .checkout-types li.express-checkout").children().length == 0){
				$("body.page-cartPage .cart.wrapper .checkout-types li#checkout-id").addClass("onlyCheckout");
				$("body.page-cartPage .cart.wrapper .checkout-types").addClass("onlyCheckoutButton");
				$("body.page-cartPage .continue-shopping.desk-view-shopping").addClass("onlyCheckoutLink");
			}
			});
		
		$(".product-tile .image .item.quickview").each(function(){
			if($(this).find(".addtocart-component").length == 1){
				$(this).addClass("quick-bag-both");
			}
			});
		if($(".facet-list.filter-opt").children().length > 0){
		var filter_html = $(".listing.wrapper .right-block .facet-values.js-facet-values").html();
		$(".listing.wrapper .left-block").before(filter_html);
		$(".listing.wrapper .right-block .facet-values.js-facet-values").html("").hide();
		}
		else{
			$(".facet-list.filter-opt").hide();
		}
		
		$(document).ajaxComplete(function(){
			if($(".facet-list.filter-opt").children().length > 0){
			var filter_html = $(".listing.wrapper .right-block .facet-values.js-facet-values").html();
			$(".listing.wrapper .left-block").before(filter_html);
			$(".listing.wrapper .right-block .facet-values.js-facet-values").html("").hide();
			}
			else{
				$(".facet-list.filter-opt").hide();
			}
			$(".facet-list.filter-opt li").each(function(){
				if($(this).find("input.colour").length > 0){
					var selected_colour = $(this).find("input.applied-color").val();
					$(".left-block .product-facet .facet.Colour .facet-list li.filter-colour").each(function(){
					var colour_name = $(this).find("input[name=facetValue]").val().split("_", 1);
					if(colour_name == selected_colour){
						$(this).addClass("selected-colour");
					}
					if(selected_colour == "Multicolor"){
						if(colour_name == "Multi"){
							$(this).addClass("selected-multi-colour");
						}
					}
					});
				}
				if($(this).find("input.size").length > 0){
			var selected_size = $(this).find("input.applied-color").val();
			$(".left-block .product-facet .facet.Size .facet-list li.filter-size").each(function(){
				var size_val = $(this).find(".js-facet-sizebutton").val();
				if(size_val == selected_size){
					$(this).addClass("selected-size");
				}
			});
				}
			});
			if($(".listing.wrapper").length > 0){
				if($(".searchSpellingSuggestionPrompt").length>0){
					$(".toggle-filterSerp").css("margin-top",$(".searchSpellingSuggestionPrompt").height() + 40 + "px");
					$(".listing.wrapper .right-block").css("padding-top",$(".searchSpellingSuggestionPrompt").height() + 50 + "px");
				}
				if(($(".toggle-filterSerp").length>0) && ($(".facet-list.filter-opt").length>0)){
					var sort_top= $(".toggle-filterSerp").offset().top - $(".listing.wrapper").offset().top;
					if($(".facet-list.filter-opt").offset().top == 0){
						var pagination_top= sort_top - 4;
					}
					else{
						var pagination_top= sort_top - 16;
					}
					
					/*TISPRDT-1179*/	/*TISPRDT-1222*/
					if(!$("body").hasClass("page-productGrid")){
					if($(".listing.wrapper .right-block .listing-menu > div.list_title_sort").css("display") == "block"){
					$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top",sort_top+"px");
					}
					else{
						$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top","auto");
					}
					}
					$("body.page-search .listing.wrapper .right-block .listing-menu").css("display","block");
					/*TISPRDT-1179*/	/*TISPRDT-1222*/
					
					$(".listing.wrapper .right-block .listing-menu > div .pagination.mobile.tablet-pagination").css("top",pagination_top+"px");
				}
			}
			
			
if($(".facet.js-facet.Colour .js-facet-values.js-facet-form .more-lessFacetLinks").length == 0){	
var colorMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
				+'<div class="more checkbox-menu" style="display: block;">'
				+'<a href="#" class="more">+&nbsp;<span class="colourNumber">0</span>&nbsp;more&nbsp;<span class="colourText">colours</span></a>'
				+'</div><div class="less checkbox-menu" style="display: none;">'
				+'<a href="#" class="less"> - less colours</a>'
				+'</div></div>';

$(".facet.js-facet.Colour .js-facet-values.js-facet-form").append(colorMoreLess);
}
if($(".facet.js-facet.Size .js-facet-values.js-facet-form .more-lessFacetLinks").length == 0){	
var sizeMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
				+'<div class="more checkbox-menu" style="display: block;">'
				+'<a href="#" class="more">+&nbsp;<span class="sizeNumber">0</span>&nbsp;more&nbsp;<span class="sizeText">sizes</span></a>'
				+'</div><div class="less checkbox-menu" style="display: none;">'
				+'<a href="#" class="less"> - less sizes</a>'
				+'</div></div>'; 

$(".facet.js-facet.Size .js-facet-values.js-facet-form").append(sizeMoreLess);
}
if($(".facet.js-facet.Colour").length > 0){
	colorSwatch();
}
if($(".facet.js-facet.Size").length > 0){
	sizeSwatch();
}
$(".product-tile .image .item.quickview").each(function(){
	if($(this).find(".addtocart-component").length == 1){
		$(this).addClass("quick-bag-both");
	}
	});		
		});
		$(window).on("load resize", function() {
		if($(".listing.wrapper").length > 0){
			if($(".searchSpellingSuggestionPrompt").length>0){
				$(".toggle-filterSerp").css("margin-top",$(".searchSpellingSuggestionPrompt").height() + 40 + "px");
				$(".listing.wrapper .right-block").css("padding-top",$(".searchSpellingSuggestionPrompt").height() + 50 + "px");
			}
			if(($(".toggle-filterSerp").length>0) && ($(".facet-list.filter-opt").length>0)){
				var sort_top= $(".toggle-filterSerp").offset().top - $(".listing.wrapper").offset().top;
				if($(".facet-list.filter-opt").offset().top == 0){
					var pagination_top= sort_top - 4;
				}
				else{
					var pagination_top= sort_top - 16;
				}
				
				/*TISPRDT-1179*/		/*TISPRDT-1222*/
				if(!$("body").hasClass("page-productGrid")){
				if($(".listing.wrapper .right-block .listing-menu > div.list_title_sort").css("display") == "block"){
				$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top",sort_top+"px");
				}
				else{
					$(".listing.wrapper .right-block .sort_by_wrapper.listing-menu").css("top","auto");
				}
				}
				/*TISPRDT-1179*/		/*TISPRDT-1222*/
				
				$(".listing.wrapper .right-block .listing-menu > div .pagination.mobile.tablet-pagination").css("top",pagination_top+"px");
			}
		}
		});
		
		/*TPR-250*/
		$(".tabs-block .tabs.pdp.productTabs > li").each(function(){
			if($(this).find("li").length == 0 || ($(this).find("li").length != 0 && ($(this).find("li").eq(0).text().trim() === "")))
			$(this).html("We have not updated the description for this Brand yet. We will get this sorted in a while");
			});
		/*TPR-250*/
		
});

        var screenXs="480px";
        var screenSm="640px";
        var screenMd="1024px";
        var screenLg="1400px";
          
        var screenXsMin="480px";
        var screenSmMin="640px";
        var screenMdMin="1024px";
        var screenLgMin="1400px";

        var screenXsMax="639px";
        var screenSmMax="1023px";
        var screenMdMax="1399px";

        
/*colour and size swatch 3 lines TISPRM-123*/

var colorSwatchFlag,sizeSwatchFlag;

$(document).ready(function() {
	
	var colorMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
						+'<div class="more checkbox-menu" style="display: block;">'
						+'<a href="#" class="more">+&nbsp;<span class="colourNumber">0</span>&nbsp;more&nbsp;<span class="colourText">colours</span></a>'
						+'</div><div class="less checkbox-menu" style="display: none;">'
						+'<a href="#" class="less"> - less colours</a>'
						+'</div></div>';
	
	var sizeMoreLess = '<div class="more-lessFacetLinks" style="display:none;">'
						+'<div class="more checkbox-menu" style="display: block;">'
						+'<a href="#" class="more">+&nbsp;<span class="sizeNumber">0</span>&nbsp;more&nbsp;<span class="sizeText">sizes</span></a>'
						+'</div><div class="less checkbox-menu" style="display: none;">'
						+'<a href="#" class="less"> - less sizes</a>'
						+'</div></div>'; 
	
	$(".facet.js-facet.Colour .js-facet-values.js-facet-form").append(colorMoreLess);
	$(".facet.js-facet.Size .js-facet-values.js-facet-form").append(sizeMoreLess);
	colorSwatch();
	sizeSwatch();
	
	
	/*CLP section js starts*/
	
	//Top Categories section
/*	$(".top_categories .top_categories_section:not(:first-child)").wrapAll("<div class='top_categories_wrapper'></div>");
*/	
	/*if ($(".top_categories .top_categories_section").first().children().hasClass('content')) {
			$(".top_categories .top_categories_section:not(:first-child)").wrapAll("<div class='top_categories_wrapper'></div>");

	}
	else {
			$(".top_categories .top_categories_section").wrapAll("<div class='top_categories_wrapper'></div>");
$(".top_categories_wrapper").css("padding-top", "40px");
	} 
	
	
	$(".top_categories").find(".top_categories_wrapper>.top_categories_section:nth-child(3n + 1)").each(function(){
		$(this).nextAll().slice(0, 2).wrapAll("<div class='top_categories_section sub_categories'>");
		});*/
	//TPR-559 Hide/Unhide Component UI fixes starts
	if ($("div.top_categories").find('.content').length == 0) {
		$("div.top_categories").css("padding-top","50px");
	}	
	if ($("div.top_categories").find('.simple-banner-component').length == 0 && $("div.top_categories").find('.content').length == 0) {
		$("div.top_categories").hide();
	}
	//TPR-559 Hide/Unhide Component UI fixes ends 
	//Style Edit section
		/*$(".style_edit > div").slice(0,2).wrapAll("<div class='style_edit_left'>");*/
	if($(".style_edit .style_edit_left").children().length == 0	){
		$(".style_edit .style_edit_left").css("width", "auto");
	}
	//Top Brands section
		//TPR-559 Hide/Unhide Component UI fixes starts
		//$(".top_brands > div").slice(1).wrapAll("<div class='clp_top_brands'>");
		if ($("div.top_brands div").first().children().hasClass('content')) {
			$("div.top_brands div").first().addClass('top_brands_heading');
			$(".top_brands > div").slice(1).wrapAll("<div class='clp_top_brands'>");
		}
		else {
			$(".top_brands > div").wrapAll("<div class='clp_top_brands'>");
		}
			$(".clp_top_brands .top_brands_section").each(function(){
		if($(this).find(".simple-banner-component").length == 0){
			$(this).addClass("view_more_link");
		}
		});
		//TPR-559 Hide/Unhide Component UI fixes ends 
		//Best Seller section
		if (!$(".best_seller .best_seller_section").first().children().hasClass('content')) {
			$(".best_seller").css("padding-top","50px");
		}
/*		$(".best_seller .best_seller_section:first-child").after("<div class='Menu'><div class='mobile selectmenu'></div><ul></ul></div>"); 
*/		
	$(".best_seller .best_seller_carousel .best_seller_section").each(function(){
		var li_text= $(this).children("h1").text();
		if(li_text != ""){
		$(".best_seller .best_seller_carousel .Menu ul").append("<li>"+li_text+"</li>");
		}
		});
		var best_seller_count = $(".best_seller .Menu ul li").length;
		var active_best_seller_heading = parseInt((best_seller_count/2)) + 1;
		$(".best_seller .Menu ul li:nth-child("+active_best_seller_heading+")").addClass("active");
		//$(".best_seller .best_seller_section:nth-of-type(4)").addClass("show_clplist");
		var active_text_onload = $(".best_seller .Menu ul li.active").text();
		$(".best_seller .best_seller_carousel .best_seller_section").each(function(){
		var li_text_onload= $(this).children("h1").text();
		if(li_text_onload == active_text_onload){
		$(this).removeClass("hide_clplist").addClass("show_clplist");
		}
		});
		$(".best_seller .Menu .mobile.selectmenu").text($(".best_seller .Menu ul li.active").text());
		$(".best_seller .Menu ul li").off("click").on("click", function(){
			$(".best_seller .Menu ul li").removeClass("active");
			$(this).addClass("active");
			var active_text = $(this).text();
		$(".best_seller .best_seller_carousel .best_seller_section").each(function(){
		var li_text= $(this).children("h1").text();
		if(li_text == active_text){
		$(".best_seller .best_seller_section").removeClass("show_clplist").addClass("hide_clplist");
		$(this).removeClass("hide_clplist").addClass("show_clplist");
		}
		});
		$(".best_seller .Menu .mobile.selectmenu").text(active_text);
		$('.best_seller .Menu ul').slideUp();
		});
		$(".best_seller .Menu .selectmenu").off("click").on("click", function() {
            $(this).next().slideToggle();
        });
		//Winter Launch section
		/*$(".winter_launch  > .winter_launch_section").slice(-4).wrapAll("<div class='clp_winter_launch_wrapper'>");*/
		if ($(".winter_launch  > .winter_launch_section").first().children().hasClass('content')) {
			$(".winter_launch  > .winter_launch_section").first().addClass('winter_launch_heading');
			$(".winter_launch  > .winter_launch_section.winter_launch_heading").nextAll().wrapAll("<div class='clp_winter_launch_wrapper'>");
		}
		else {
			$(".winter_launch  > .winter_launch_section").wrapAll("<div class='clp_winter_launch_wrapper'>");
		}
		//Top deal blog section
		/* start change for INC_11268*/
		/*$(".top_deal  > a").nextAll().wrapAll("<div class='blog_container'>");*/
		/*if($(".top_deal > .carousel-component").length > 0){
			$(".top_deal  > .carousel-component + a").nextAll().wrapAll("<div class='blog_container'>");
		}
		else{
				$(".top_deal").children().wrapAll("<div class='blog_container'>");
		}*/
		/*end change for INC_11268*/
		/*$(".top_deal  > a").nextAll().wrapAll("<div class='blog_container'>");
		var clp_blog_count = $(".top_deal  > .blog_container").children().length;
		$(".top_deal  > .blog_container").children().slice(0,clp_blog_count/2).wrapAll("<div class='blog_feature'>");
		$(".top_deal  > .blog_container").children().slice(1,clp_blog_count - 1).wrapAll("<div class='blog_feature'>");
		$(".top_deal  > .blog_container > .blog_feature").each(function(){
			$(this).children().last().prevAll().wrapAll("<div class='blog_content'>");
		});*/
		$(".top_deal  > .blog_container > .blog_feature").each(function(){
			if ($(this).children().last().hasClass('simple-banner-component')) {
				$(this).children().last().prevAll().wrapAll("<div class='blog_content'>");
			}
			else {
				$(this).addClass("no-blog-image");
				$(this).children().wrapAll("<div class='blog_content'>");
			}
		});
		//Shop For section
		/*$(".shop_for .shop_for_component:not(:first-child)").slice(0,3).wrapAll("<div class='shop_for_left_wrapper'></div>");
		$(".shop_for > .shop_for_left_wrapper").nextAll().slice(0,4).wrapAll("<div class='shop_for_links'>");*/
		
		//For blank cms container remove bottom border
		
		if(($(".style_edit .style_edit_left").children().length == 0	) && ($(".style_edit .style_edit_right").children().length == 0	)){			
			$(".style_edit").hide();
			$(".top_deal").css("margin-top", "50px");
		}
		if($("body.template-pages-layout-apparelCategoryLandingPageV1 .body-Content").find(".top_brands").children().length==0){
			$(".top_brands").hide();
		}
		if($("body.template-pages-layout-apparelCategoryLandingPageV1 .body-Content").find(".winter_launch").children().length==0){
			$(".winter_launch").hide();
		}
		if($("body.template-pages-layout-apparelCategoryLandingPageV1 .body-Content").find(".top_deal .blog_container").children().length==0){
			$(".top_deal .blog_container").css("border-bottom","none");
		}
		if (!$('.shop_for .content').length && !$('.shop_for a').length && !$('.shop_for .simple-banner-component').length) {
			$(".shop_for").hide();
		} 
		if (!$('.top_deal .content').length && !$('.top_deal a').length && !$('.top_deal .simple-banner-component').length && !$('.top_deal .feature-categories').length) {
			$('.top_deal').hide();
		}
		if (!$('.best_seller .content').length && !$('.best_seller a').length && !$('.best_seller .carousel-component').length) {
			$('.best_seller').hide();
		}
		if (!$('.top_deal .blog_container .blog_feature .simple-banner-component').length && !$('.top_deal .blog_container .blog_feature .content').length && !$('.top_deal .blog_container .blog_feature a').length) {
			$('.top_deal .blog_container').hide();
		}
		
		//For CLP blank cms container remove extra space

		
		
		
		/*CLP section js ends*/
		
		/* UF-68 UF-69 */
		topMarginAdjust();
		/* UF-68 UF-69 */
});
$(window).resize(function() {
	
	/* UF-68 UF-69 */
	topMarginAdjust();
	/* UF-68 UF-69 */

	clearTimeout(colorSwatchFlag);
	clearTimeout(sizeSwatchFlag);
	colorSwatchFlag = setTimeout(function() {
		if(!$(".facet.js-facet.Colour .more-lessFacetLinks .less").is(':visible')) {
			colorSwatch();
		}
	}, 200)
	sizeSwatchFlag = setTimeout(function() {
		if(!$(".facet.js-facet.Size .more-lessFacetLinks .less").is(':visible')) {
			sizeSwatch();
		}
	}, 200)
	
	/* UF-68 UF-69 */
	
	/*if($(window).width() >= 1008){
		//$("body.page-cartPage .cartBottomCheck").removeClass("cartBottomCheckShow");
	var cartBottomCheckTopMargin = $("body.page-cartPage .cart-bottom-block").height() - $("body.page-cartPage .cartBottomCheck button#pinCodeButtonIdsBtm").outerHeight();
	$("body.page-cartPage .cartBottomCheck").addClass("cartBottomCheckShow").css("margin-top",cartBottomCheckTopMargin);
	}*/
	
	/* UF-68 UF-69 */
});

$(document).on("click",".facet.js-facet.Colour .more-lessFacetLinks .more",function(e) {
	e.preventDefault();
	$("li.filter-colour.deactivate").removeClass("deactivate");
	$('.facet.js-facet.Colour .more-lessFacetLinks .more').hide();
	$('.facet.js-facet.Colour .more-lessFacetLinks .less').show();
});
$(document).on("click",".facet.js-facet.Colour .more-lessFacetLinks .less",function(e) {
	e.preventDefault();
	colorSwatch();
	$('.facet.js-facet.Colour .more-lessFacetLinks .more').show();
	$('.facet.js-facet.Colour .more-lessFacetLinks .less').hide();
});
$(document).on("click",".facet.js-facet.Size .more-lessFacetLinks .more",function(e) {
	e.preventDefault();
	$("li.filter-size.deactivate").removeClass("deactivate");
	$('.facet.js-facet.Size .more-lessFacetLinks .more').hide();
	$('.facet.js-facet.Size .more-lessFacetLinks .less').show();
});
$(document).on("click",".facet.js-facet.Size .more-lessFacetLinks .less",function(e) {
	e.preventDefault();
	sizeSwatch();
	$('.facet.js-facet.Size .more-lessFacetLinks .more').show();
	$('.facet.js-facet.Size .more-lessFacetLinks .less').hide();
});
$(document).on('click','.facet.js-facet.Colour .js-facet-name h3',function(){
	setTimeout(function(){
		if(!$(this).hasClass('active')) {
			colorSwatch();
		}
	},80)
});
$(document).on('click','.facet.js-facet.Size .js-facet-name h3',function(){
	setTimeout(function(){
		if(!$(this).hasClass('active')) {
			sizeSwatch();
		}
	},80)
});

//changes for TISPRO-796
//$(document).on('click','.left-block .toggle-filterSerp',function(){
function toggleFilter(){
	colorSwatch();
	sizeSwatch();
	//Mobile view filter ajax
	//$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
	//$(".toggle-filterSerp").toggleClass("active");
	/*mobile filter*/
	$(".mob-filter-wrapper").fadeIn();
	$(this).toggleClass("active");
	
	//TPR-845
	// Fixing error of facet starts
	if ($(".facet_mobile .facet.js-facet.Colour").find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').length) {
		var spanCountMoreViewColor = $(".facet_mobile .facet.js-facet.Colour").find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-colour").length;
		//TISQAUATS-12 starts
		spanCountMoreViewColor = spanCountMoreViewColor + $(".facet_mobile .facet.js-facet.Colour").find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("li.selected-multi-colour").length;
		//TISQAUATS-12 ends
		if(spanCountMoreViewColor)
		{
			//$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCountMoreViewColor);
			//TISQAUATS-12 starts
			if ($(".facet_mobile .filter-colour.selected-colour").length) {
				$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCountMoreViewColor);
			} else {
				$(".facet_mobile .filter-colour.selected-multi-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCountMoreViewColor);
			}
			//TISQAUATS-12 ends
		}
	}
	else {
	// Fixing error of facet ends
		var spanCount_colour=$(".facet_mobile .filter-colour.selected-colour").length;
		//TISQAUATS-12 starts
		spanCount_colour = spanCount_colour + $(".facet_mobile .filter-colour.selected-multi-colour").length;
		//TISQAUATS-12 ends
		if(spanCount_colour>0)
		{
				//TISSQAUAT-723 starts
				//$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCount_colour);
				if ($(".facet_mobile .filter-colour.selected-colour").length) {
					$(".facet_mobile .filter-colour.selected-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCount_colour);
				}
				else {
					$(".facet_mobile .filter-colour.selected-multi-colour").parents(".facet.js-facet").find(".category-icons span").text(spanCount_colour);
				}
				//TISSQAUAT-723 ends
		}
	// Fixing error of facet starts	
	}
	// Fixing error of facet ends	

	var spanCount_size=$(".facet_mobile .filter-size.selected-size").length;
	if(spanCount_size>0)
	{
		$(".facet_mobile .filter-size.selected-size").parents(".facet.js-facet").find(".category-icons span").text(spanCount_size);
	}

	$(".facet_mobile .facet.js-facet").each(function(){
		//console.log('hi');
		var spanCountMoreView = $(this).find('ul.facet-list.js-facet-list.facet-list-hidden.js-facet-list-hidden').find("input[type=checkbox]:checked").length;
		if(spanCountMoreView){
			$(this).find(".category-icons span").text(spanCountMoreView);
		}else{
			var spanCount=$(this).find(".facet-list li").find("input[type=checkbox]:checked").length;
			if(spanCount>0)
				{
					$(this).find(".category-icons span").text(spanCount);
				}
		}
		});
	$(".category-icons").each(function(){
		if($(this).find("span").text() == ""){
			$(this).addClass("blank");
		}
		else{
			$(this).removeClass("blank");
		}
	});
	$(".facet-name.js-facet-name h3").removeClass("active-mob");
	$(".facet-name.js-facet-name h3").first().addClass("active-mob");
	$(".facet-name.js-facet-name h3").parent().siblings().hide();
	$(".facet-name.js-facet-name h3.active-mob").parent().siblings().show();
	$(".facet-name.js-facet-name h3.active-mob").parent().siblings().find("#searchPageDeptHierTree").show();
	$(".facet-name.js-facet-name h3.active-mob").parent().siblings().find("#categoryPageDeptHierTree").show();
	
	/*TPR-3658 start*/
	var j = 0;
	$(".listing.wrapper .mob-filter-wrapper > .listing-leftmenu > div.facet_mobile").not(".facet-name").each(function(){
		if($(this).children().length == 0){
			return true;
		}
		if($(this).find(".facet.js-facet.collectionIds").length > 0){
			return true;
		}
		if(j % 2 == 0){
			$(this).addClass("light-bg");
		}
		j++;
		});
	/*TPR-3658 end*/
}
		//	});
	$(".category-icons").each(function(){
	if($(this).find("span").text() == ""){
	$(this).addClass("blank");
	}
	else{
	$(this).removeClass("blank");
	}
	});

$(document).on('click','.left-block .filter-close',function(){
	/*mobile filter*/
	$(".mob-filter-wrapper").fadeOut();
	});
function colorSwatch() {
	var row = 0, start = 0, count_mobile = 0, end_mobile = 0,count_desktop = 0, end_desktop = 0, back = true;
	$(".facet_mobile li.filter-colour, .facet_desktop li.filter-colour").removeClass("deactivate");

	end_mobile = $(".facet_mobile li.filter-colour").length;
	end_desktop = $(".facet_desktop li.filter-colour").length;

	$(".facet_mobile li.filter-colour").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$(".facet_mobile li.filter-colour").slice(start, end_mobile).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	$(".facet_desktop li.filter-colour").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$(".facet_desktop li.filter-colour").slice(start, end_desktop).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	
	count_mobile = $(".facet_mobile li.filter-colour.deactivate").length;
	count_desktop = $(".facet_desktop li.filter-colour.deactivate").length;
	$(".facet_mobile .colourNumber").text(count_mobile);
	$(".facet_desktop .colourNumber").text(count_desktop);
	if(count_mobile == 0) {
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks').hide();
	} else if (count_mobile == 1) {
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colour");
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colours");
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	}
	if(count_desktop == 0) {
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks').hide();
	} else if (count_desktop == 1) {
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colour");
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more .colourText').text("colours");
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Colour .more-lessFacetLinks .less').hide();
	}
	
}


function sizeSwatch() {
	//alert();
	var row = 0, start = 0, count_mobile = 0, end_mobile = 0,count_desktop = 0, end_desktop = 0, back = true;
	$(".facet_mobile li.filter-size, .facet_desktop li.filter-size").removeClass("deactivate");

	end_mobile = $(".facet_mobile li.filter-size").length;
	end_desktop = $(".facet_desktop li.filter-size").length;

	$(".facet_mobile li.filter-size").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$(".facet_mobile li.filter-size").slice(start, end_mobile).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	$(".facet_desktop li.filter-size").each(
			function() {
				if ($(this).next().length != 0) {
					if (($(this).offset().top < $(this).next().offset().top)) {
						row++;
						if (row > 2 && back) {
							start = $(this).next().index();
							$(".facet_desktop li.filter-size").slice(start, end_desktop).addClass("deactivate");
							back = false;
						}
					}
				}
			});
	count_mobile = $(".facet_mobile li.filter-size.deactivate").length;
	count_desktop = $(".facet_desktop li.filter-size.deactivate").length;
	$(".facet_mobile .facet.js-facet.Size .sizeNumber").text(count_mobile);
	$(".facet_desktop .facet.js-facet.Size .sizeNumber").text(count_desktop);
	if(count_mobile == 0) {
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks').hide();
	} else if (count_mobile == 1) {
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("size");
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("sizes");
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_mobile .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	}
	if(count_desktop == 0) {
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks').hide();
	} else if (count_desktop == 1) {
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("size");
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	} else {
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more .sizeText').text("sizes");
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .more').show();
		$('.facet_desktop .facet.js-facet.Size .more-lessFacetLinks .less').hide();
	}

}
/*colour and size swatch 3 lines TISPRM-123*/

/*added for gigya   TISPT-203 */
function callGigya(){
	//Start
	
	$.ajax({
	        type: "GET",
	        url:gigyasocialloginurl+'?apikey='+gigyaApiKey,
	        success: function() {
	        	 $.ajax({
	 		        type: "GET",
	 		        url: commonResource+'/js/minified/acc.gigya.min.js?v='+buildNumber,
	 		        success: function() {
	 		        	loadGigya();
	 		        },
	 		        dataType: "script",
	 		        cache: true
	 		    });
	        },
	        dataType: "script",
	        cache: true
	    });
	//End 
}

function callGigyaWhenNotMinified(){
	//Start
	$.ajax({
	        type: "GET",
	        url:gigyasocialloginurl+'?apikey='+gigyaApiKey,
	        success: function() {
	        	 $.ajax({
	 		        type: "GET",
	 		        url: commonResource+'/js/minified/acc.gigya.js?v='+buildNumber,
	 		        success: function() {
	 		        	loadGigya();
	 		        },
	 		        dataType: "script",
	 		        cache: true
	 		    });
	        },
	        dataType: "script",
	        cache: true
	    });
	//End 
}

/* Changes for TISPT-203 ends  */

/*TPR-198 : Page reload on SortBy and ViewBy*/
//Sort by filter
//TPR- 565 custom sku addded functionality
function sortByFilterResult(top){
	
	if($("input[name=customSku]").length > 0){
		$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
		var pageNo = 1;
		if ($("#paginationFormBottom .pagination.mobile li.active span").length) {
			pageNo = $("#paginationFormBottom .pagination.mobile li.active span").text();
			if (typeof(pageNo) === "undefined"){
				pageNo = 1;
			}
		}  
		var requiredUrl = '/CustomSkuCollection/' + $("input[name=customSkuCollectionId]").val() + '/page-' + pageNo;
		var dataString = $('#sortForm' + top).serialize() + "&pageSize=" + $("select[name=pageSize]").val();
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			success : function(response) {
				console.log(response);
				// putting AJAX respons to view
				$('#facetSearchAjaxData .right-block, #facetSearchAjaxData .bottom-pagination, #facetSearchAjaxData .facet-list.filter-opt').remove();
				$('#facetSearchAjaxData .left-block').after(response);
			},
			error : function(xhr, status, error) {				
				console.log("Error >>>>>> " + error);
			},
			complete: function() {
				// AJAX changes for custom price filter
				$("#no-click").remove();
				$(".spinner").remove();
				
			}
		});
		
	}else{
		$("#hidden-option-width").html($(".sort-refine-bar select").find('option:selected').text());
		 var option_width=$("#hidden-option-width").width() + 22;
		$(".sort-refine-bar select.black-arrow-left").css("background-position-x",option_width);
		
		$('#sortForm'+top).submit();
	}
	
}

//View by filter
//TPR- 565 custom sku addded functionality
function viewByFilterResult(top){

	if($("input[name=customSku]").length > 0){
		$("body").append("<div id='no-click' style='opacity:0.60; background:black; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top: 50%; height: 30px;">');
		var pageNo = 1;
		if ($("#paginationFormBottom .pagination.mobile li.active span").length) {
			pageNo = $("#paginationFormBottom .pagination.mobile li.active span").text();
			if (typeof(pageNo) === "undefined"){
				pageNo = 1;
			}
		}		   
		var requiredUrl = '/CustomSkuCollection/' + $("input[name=customSkuCollectionId]").val() + '/page-' + pageNo;
		var dataString = $('#pageSize_form' + top).serialize();
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			data : dataString,
			success : function(response) {
				console.log(response);
				// putting AJAX respons to view
				$('#facetSearchAjaxData .right-block, #facetSearchAjaxData .bottom-pagination, #facetSearchAjaxData .facet-list.filter-opt').remove();
				$('#facetSearchAjaxData .left-block').after(response);
			},
			error : function(xhr, status, error) {				
				console.log("Error >>>>>> " + error);
			},
			complete: function() {
				// AJAX changes for custom price filter
				$("#no-click").remove();
				$(".spinner").remove();
			}
		});
		
	}else{
		$('#pageSize_form'+top).submit();
	}	
}
/*Filter scroll changes start*/
var fix_width= $(".listing.wrapper .left-block").width() + 38;	/*UF-290*/
$(window).on("scroll",function(){
	if($(window).width() > 790 && $('.listing.wrapper .right-block').height() > $('.listing.wrapper .left-block').height() && $('.listing.wrapper .left-block').length > 0) {
		
		if ($('.listing.wrapper .right-block').offset().top >= $('.listing.wrapper .left-block').offset().top - parseInt($('.listing.wrapper .left-block').css("margin-top"))){
			$('.listing.wrapper .left-block').removeClass("fix bottom");
		} else  {
			$('.listing.wrapper .left-block').addClass("fix")
		}
		
		if($(window).scrollTop() >  $('.listing.wrapper .left-block').height() - $(window).height() + $('.listing.wrapper .left-block').offset().top ){
			$('.listing.wrapper .left-block').addClass("fix").removeClass("bottom");
		}
		
		if($(window).scrollTop() >  $('.listing.wrapper .right-block').height() - $(window).height() + $('.listing.wrapper .right-block').offset().top ){
			$('.listing.wrapper .left-block').removeClass("fix").addClass("bottom");
		}
		$(".listing.wrapper .left-block.fix").css("width",fix_width+"px");	/*UF-290*/
	} else {
		$('.listing.wrapper .left-block').removeClass("fix bottom");
	}
});
/*Filter scroll changes end*/

$(document).ready(function() {
	$('.wish-share.mobile .share span').click(function(){
		if($(this).hasClass('active')) {
			$(this).removeClass('active')
		} else {
			$(this).addClass('active')
		}
	});
	$(document).on('click','.zoomLens',function(){hit();})
	$(document).on('click','.product-image-container.device img',function(){
		if($(this).attr("data-type")=='image'){		/*add if for INC144314454*/
			hit({
				windowWidth : $(window).width()
			});
		/*start change for INC144314454*/
		}else{
			var url = $(this).attr("data-videosrc");
			/*$("#player").show();
			$("#player").attr("src",url);*/
			$("#videoModal1 #player").attr("src",url).show();
			$("#videoModal1").modal();
			$("#videoModal1").addClass("active");
			//$(".productImagePrimary .picZoomer-pic-wp img").hide();
			/*$(".zoomContainer").remove();
			$('.picZoomer-pic').removeData('zoom-image');*/
			if ($(window).width() < 1025) {
				$(".product-info .product-image-container.device").show();
			}
		}
		/*end change for INC144314454*/
	});
	var pdpStyle;
		 $(window).on('load resize',function(){	
			clearTimeout(pdpStyle);
			 pdpStyle = setTimeout(function(){
				if(($(window).width() > 767) && ($(window).width() < 1025)) {
					var img_height = $(".product-info .product-image-container.device").height();
					var detail_height = $(".product-info .product-detail").height();
					if (img_height < detail_height ){
					var diff = detail_height - img_height;
					$(".product-info>div.tabs-block").css("margin-top","-"+diff+"px");
					}
					
				}
			},250)
		 });
});

$(document).ajaxComplete(function(){
	if(!$("body").hasClass("pageLabel-homepage") && !$("body").hasClass("template-pages-layout-micrositePage1")){
		$("body").find(".content-block-slider.electronic-brand-slider").removeClass("timeout-slider");
	}
});

/*TPR-179(Shop The Style start)*/
$(document).ready(function(){
	
	$(".timeout-slider").find(".owl-item.active").find(".item.slide").click(function(){
		var link = $(this).find("a").attr("href");
		console.log(link);
	});
	
	$(".item.slide").find("a").click(function(){
		  var link= $(this).attr("href"); //$(".item.slide").find("a").attr("href"); 
		  var sublink=link.substr(0, link.indexOf('?')); 
		  var id = "#"+sublink.split("#")[1];
		  console.log(id);
		  $('html, body').animate({
		        scrollTop: $(id).offset().top -100
		    }, 1000);
	});
});



/*END TPR-179(Shop The Style start)*/

/*checkout login error start*/
$(document).ready(function() {
	 $(window).on('load resize',function(){	
		 header_ht = $("header").outerHeight();
$("body:not(.page-checkout-login) .top.checkout-top .content").css("margin-top",header_ht);
if ($("body.page-checkout-login .global-alerts").length > 0){
	$(".top.checkout-top .content").css("margin-top","0px");
	$(".global-alerts").css("margin-top",header_ht+"px");
}
$(document).click(".global-alerts .close", function(){
	$(".global-alerts").css("margin-top","0px");
	$(".top.checkout-top .content").css("margin-top",header_ht+"px");
});
	 });
});
/*checkout login error end */


/*$(document).ready(function(){
	$(".feature-collections h2").each(function(){
		var txth2 = $(this).text();
		$(this).replaceWith("<h3>"+txth2+"</h3>");
	});
	$(".feature-collections h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2>"+txth1+"</h2>");
	});
	$(".menu li h3").each(function(){
		var txth = $(this).text();
		$(this).replaceWith("<h2>"+txth+"</h2>");
	});
	$(".electronics-brand .brands h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2>"+txth1+"</h2>");
	});
	$(".feature-categories h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2>"+txth1+"</h2>");
	});
	$(".trending h1").each(function(){
		var txth1 = $(this).text();
		$(this).replaceWith("<h2><span style='color: black !important;'>"+txth1+"</span></h2>");
	});
	
});*/


var ia_prod;
$(window).resize(function(){
	clearTimeout(ia_prod);
	ia_prod = setTimeout(function(){
		var a = $(".pdp .trending#ia_products .image").height()/2 + 20;
		$(".pdp .trending#ia_products .owl-controls").css("top",a)
	},100);
});
$(window).scroll(function(){
	if($(".pdp .trending#ia_products").children().length > 0 && $(window).scrollTop()  > $(".pdp .trending#ia_products").offset().top - $(window).height()) {
		var a = $(".pdp .trending#ia_products .image").height()/2 + 20;
		$(".pdp .trending#ia_products .owl-controls").css("top",a);
	}
});

/*checkout address modified starts*/

/*$(document).on("click",".acc_head",function(){
	$(this).siblings(".acc_content").slideToggle();
});
$(document).on("click",".add-address",function(){
	$(this).siblings(".formaddress").slideToggle();
	$(this).slideToggle();
});
$(document).on("click",".cancelBtn",function(){
	$(this).parents(".formaddress").siblings(".add-address").slideToggle();
	$(this).parents(".formaddress").slideToggle();
});*/
if ($(".address-accordion").length) {
    $(".address-accordion").smk_Accordion({
        closeAble: true,
        closeOther: false,
        slideSpeed: 750,
    })
}
$(".formaddress").hide();
$("#address-form").click(function() {
	/*added by sneha R2.3*/
	$(".editnewAddresPage").empty();
	/*end of sneha R2.3*/
    $(".add-address").hide();
    $(".formaddress").slideToggle();

});
  $(".cancelBtn").click(function() {
	  //alert('here');
	  	
        $(".editnewAddresPage, .formaddress").slideUp();
        $(".add-address").slideDown();
    });
	  $(document).on("click",".cancelBtnEdit",function(){	
		 // console.log("sadsadfsf");
		  var cancel_id = $(this).attr('id');
		  //console.log(cancel_id);
		  var address_id = cancel_id.split('-');
		 // console.log(address_id);
		$('#'+address_id[1]).slideUp();  
	  //$(this).parents().find(".formaddress").slideUp();
  });
$(".checkTab .address-list").last().addClass("last");
if($(".choose-address .acc_content").children(".address-list").length == 0){
	$(".add-address").css({
	  float: "none"
});
	$(".checkTab .formaddress").css({
		margin : "0px auto",
		float: "none",
		width: "80%",
		overflow: "hidden"
	});
//$(".choose-address .acc_head").css("text-align","center");
}
if ($("#couponMessage").children().length == 0){
	$("#couponMessage").css("padding","0px");
}
/*	$(document).on("click",".edit",function(e){	
		 e.stopPropagation();
	alert("hi");
	$(this).prev(".address").css("display","none");
});*/

/*checkout address modified ends*/


/* TPR-1217 starts Click And Collect Starts */
$(document).ready(function(){
	$(".checkout-shipping-items.left-block.left-block-width").parents(".checkout-content.cart.checkout.wrapper").addClass("shipCartWrapper");
	$(".shipCartWrapper").parents(".mainContent-wrapper").find("footer").addClass("shipCartFooter");
});
/* TPR-1217 starts Click And Collect Ends */

$('.checkout.wrapper .formaddress select[name="state"]').on("change",function(){$(this).css("color","#000");});

/* TPR-1601 checkout progress bar start */
$(document).ready(function(){
	if($(".progress-barcheck").hasClass("choosePage")){
		$(".step-1").addClass("active");
		$(".progress-barg span.step").addClass("step1");
		$(this).children().find(".step-2").addClass("in-active");
		$(this).children().find(".step-3").addClass("in-active");
		
	}
	else if ($(".progress-barcheck").hasClass("selectPage")){
		$(".step-2").addClass("active");
		$(".step-1").addClass("step-done");
		$(".progress-barg span.step").addClass("step2");
		$(this).children().find(".step-3").addClass("in-active");
	}
	else if  ($(".progress-barcheck").hasClass("paymentPage")){
		$(".step-3").addClass("active");
		$(".step-1,.step-2").addClass("step-done");
		$(".progress-barg span.step").addClass("step3");
	}
	/*start changes for INC_11120*/
	 /*setTimeout(function () {
		 if ($('body').find(".smartbanner.smartbanner-android").length > 0){
				$("body.page-multiStepCheckoutSummaryPage header, body.page-checkout-login header").css("margin-top","82px");
			}
			else{
				$("body.page-multiStepCheckoutSummaryPage header, body.page-checkout-login header").css("margin-top","0px");
			}
     }, 100);*/
	/*end changes for INC_11120*/
	 $(document).on("click",".smartbanner-close",function(){
		$("body.page-multiStepCheckoutSummaryPage header, body.page-checkout-login header").css("margin-top","0px");
	 });
	 $(".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller").show(); //TPR-4471
});
/* TPR-1601 checkout progress bar end  */

//----BLP------//
//--top category section---//
/*$(".top_categories_blp").first().find("ul.categories.count-3>li:nth-child(3n + 1)").each(function(){
		$(this).nextAll().slice(0, 2).wrapAll("<li class='sub_li_blp'><ul class='sub_ul_blp'>");
		});*/


//$(".top_categories_blp .top_categories_section_blp:not(:first-child)").wrapAll("<div class='top_categories_wrapper_blp'></div>");
/*$(".top_categories_blp").find(".top_categories_wrapper_blp>.top_categories_section_blp:nth-child(3n + 1)").each(function(){
	$(this).nextAll().slice(0, 2).wrapAll("<div class='top_categories_section_blp sub_categories_blp'>");
	});*/
	//TPR-559 Hide/Unhide Component UI fixes starts 
	if ($("div.top_categories_blp").find('.content').length == 0) {
		$("div.top_categories_blp").css("padding-top","50px");
	}	
	if ($("div.top_categories_blp").find('.simple-banner-component').length == 0 && $("div.top_categories_blp").find('.content').length == 0) {
		$("div.top_categories_blp").hide();
	}	
//TPR-559 Hide/Unhide Component UI fixes ends 
//--top category section end---//
//--top brands section----//
//TPR-559 Hide/Unhide Component UI fixes starts
		//$(".blp_top_brands > div").slice(1).wrapAll("<div class='top_brands_blp'>");
		if ($("div.blp_top_brands div").first().children().hasClass('content')) {
			$("div.blp_top_brands div").first().addClass('top_brands_heading');
			$(".blp_top_brands > div").slice(1).wrapAll("<div class='top_brands_blp'>");
		}
		else {
			$(".blp_top_brands > div").wrapAll("<div class='top_brands_blp'>");
		}
			
		//TPR-559 Hide/Unhide Component UI fixes ends 
//--top brands section end----//
//---style edit section start---//
/*$(".style_edit_blp > div").slice(0,3).wrapAll("<div class='style_edit_left_blp'>");*/
		if($(".style_edit_blp .style_edit_left_blp").children().length == 0	){
			$(".style_edit_blp .style_edit_left_blp").css("width", "auto");
		}
//---style edit section end-----//
//---feature collection section start---//
/*$(".featured_collection  > .featured_collection_section").slice(-4).wrapAll("<div class='blp_featured_collection_wrapper'>");*/
if ($(".featured_collection  > .featured_collection_section").first().children().hasClass('content')) {
	$(".featured_collection  > .featured_collection_section").first().addClass('feature_collection_heading');
	$(".featured_collection  > .featured_collection_section.feature_collection_heading").nextAll().wrapAll("<div class='blp_featured_collection_wrapper'>");
}
else {
	$(".featured_collection  > .featured_collection_section").wrapAll("<div class='blp_featured_collection_wrapper'>");
}
//----feature collection section end-----//
//--shop for section----//
/*$(".shop_for_blp > a").slice(0,4).wrapAll("<div class='shop_for_links_blp'>");*/

/*$(".shop_for_blp .shop_for_component_blp:not(:first-child)").slice(0,3).wrapAll("<div class='shop_for_left_wrapper_blp'></div>");
$(".shop_for_blp > .shop_for_left_wrapper_blp").nextAll().slice(0,4).wrapAll("<div class='shop_for_links_blp'>");
*///--shop for section end----//
//---shop the look section start----//
/*$(".shop_the_look > div").slice(2,4).wrapAll("<div class='shop_the_look_left'>");
$(".shop_the_look").find(".yCmsComponent.shop_the_look_section > a").addClass('shop_view_more');
$(".shop_the_look").find(".yCmsComponent.shop_the_look_section > a").parent().addClass('shop_view_more_wrapper');
*/
$(".shop_the_look .shop_the_look_left .shop_the_look_section").each(function(){
	if($(this).find(".carousel-component").length == 0){
		$(this).addClass("shop_view_more_link");
	}
});
//----shop the look section end-----//
//----blog section start------//
/*start change for INC_11268*/
/*$(".top_deal_blp  > a").nextAll().wrapAll("<div class='blog_container_blp'>");*/
/*if($(".top_deal_blp > .carousel-component").length > 0){
	$(".top_deal_blp  > .carousel-component + a").nextAll().wrapAll("<div class='blog_container_blp'>");
}
else{
		$(".top_deal_blp").children().wrapAll("<div class='blog_container_blp'>");
}*/
/*end change for INC_11268*/
/*$(".top_deal_blp  > a").nextAll().wrapAll("<div class='blog_container_blp'>");
var blp_blog_count = $(".top_deal_blp  > .blog_container_blp").children().length;
$(".top_deal_blp  > .blog_container_blp").children().slice(0,blp_blog_count/2).wrapAll("<div class='blog_feature_blp'>");
$(".top_deal_blp  > .blog_container_blp").children().slice(1,blp_blog_count - 1).wrapAll("<div class='blog_feature_blp'>");*/
$(".top_deal_blp  > .blog_container_blp > .blog_feature_blp").each(function(){
	if ($(this).children().last().hasClass('simple-banner-component')) {
			$(this).children().last().prevAll().wrapAll("<div class='blog_content_blp'>");
		}
		else {
			$(this).addClass("no-blog-image");
			$(this).children().wrapAll("<div class='blog_content_blp'>");
		}
});
//-----blog section end------//
//-----Logo slot start------//
/*$(".common_logo_slot > div").slice(0,2).wrapAll("<div class='common_logo_slot_wrapper'>");*/
if($(".common_logo_slot .common_logo_slot_wrapper").children().length ==0){
	$(".common_logo_slot .common_logo_slot_wrapper").css("background","#fff");
}
//-----Logo slot end------//
if($(".shop_the_look .shop_the_look_section").length==0){
	$(".shop_the_look").css("border-bottom","none");
}
if($(".body-Content").find(".top_deal_blp").children().length==0){
	$(".top_deal_blp").css("border-bottom","none");
}


if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".blp_top_brands").children().length==0){
	$(".blp_top_brands").hide();
}
if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".featured_collection").children().length==0){
	$(".featured_collection").hide();
}
if(($(".style_edit_blp .style_edit_left_blp").children().length == 0	) && ($(".style_edit_blp .style_edit_right_blp").children().length == 0	)){
	$(".style_edit_blp").hide();
	$(".top_deal_blp").css("margin-top", "50px");
}
if (!$('.shop_for_blp .content').length && !$('.shop_for_blp a').length && !$('.shop_for_blp .simple-banner-component').length) {
	$(".shop_for_blp").hide();
} 
/*if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".shop_the_look").children().length==0){	
	$(".shop_the_look").css({
		'padding' : '0px !important',
		'margin' : '0px'		
	});
}*/

if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".shop_the_look .shop_the_look_section").length==0){	
	/*$(".shop_the_look").prop("style","padding:0px !important;margin:0px;border-bottom:none");*/
	$(".shop_the_look").hide();
}


if (!$('.top_deal_blp .content').length && !$('.top_deal_blp a').length && !$('.top_deal_blp .simple-banner-component').length && !$('.top_deal_blp .feature-categories').length) {
	$('.top_deal_blp').hide();
}

if (!$('.top_deal_blp .blog_container_blp .blog_feature_blp .simple-banner-component').length && !$('.top_deal_blp .blog_container_blp .blog_feature_blp .content').length && !$('.top_deal_blp .blog_container_blp .blog_feature_blp a').length) {
	$('.top_deal_blp .blog_container_blp').hide();
}	
if($("body.template-pages-layout-newBrandLandingPageTemplate .body-Content").find(".shop_the_look").children().length==0){	
	/* start change for INC_11268*/
	/*$(".shop_the_look").prop("style","padding:0px !important;margin:0px");*/
	$(".shop_the_look").prop("style","padding:0px !important;margin:0px;border-bottom:none");
	/*end change for INC_11268*/
}

//-----BLP------//

/*
$(document).on("click",".toggle-filterSerp",function(){
$(".product-facet.js-product-facet.listing-leftmenu").slideToggle();
$(this).toggleClass("active");
});*/

$(window).on("load resize",function(){
	htVal=$(".trending .product-image").height();
	$(".trending .owl-controls").css("top",(htVal/2)+"px");
	$(".trending .owl-controls").css("display","block");
	
	/*sprint8*/
	var arr=[".best-offers_blp .view-best-offers",".best-offers .view-best-offers","#bestOffers.best-offers.feature-collections a.view-best-offers"];
	for(var i=0;i<arr.length;i++){
	if($(arr[i]).text().trim() != ""){
	$(arr[i]).addClass("bestBtnTxtPresence");
	}
	}
	/*sprint8*/
	
});

/*TPR-4471 starts*/

//$(window).on("load resize",function(e){
	//if($(window).width() > 773) {
$(document).off("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div.toggle").on("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div.toggle",function() {
	if($(window).width() <= 773){
	$(this).parent().toggleClass('clicked-menu');
	$(".productGrid-menu").toggleClass('prodGridMargin');
	if(!($(".productGrid-menu nav > ul > li.clicked-menu > ul").hasClass('clicked-ul'))){
		$(".productGrid-menu nav > ul > li.clicked-menu > ul").addClass("clicked-ul");
	}
	if(!($(".productGrid-menu nav > ul > li.clicked-menu > ul > li:first-child").hasClass('clicked-first-level'))){
		$(".productGrid-menu nav > ul > li.clicked-menu > ul > li:first-child").addClass("clicked-first-level");
	}
	}
	});
$(document).off("mouseover touchend",".productGrid-menu nav > ul > li.clicked-menu > ul > li").on("mouseover touchend",".productGrid-menu nav > ul > li.clicked-menu > ul > li",function() {
		$(".productGrid-menu nav > ul > li.clicked-menu > ul").removeClass('clicked-ul');
		$(".productGrid-menu nav > ul > li.clicked-menu > ul > li").removeClass('clicked-first-level');
		});

	//}
//});

/*UF-29*/
$(".gig-rating-readReviewsLink_pdp").on("click",function() {
	  $("body,html").animate({ scrollTop: $('#ReviewSecion').offset().top - 50 }, "slow");
});

/* UF-73*/
$('.cartItemBlankPincode a').click(function() {

	$('html,body').animate({ scrollTop: $(this.hash).offset().top - 85}, 300);
	$('#changePinDiv').addClass('blankPincode');
	$(this.hash).focus();
	return false;

	e.preventDefault();

	}); 

$(document).mouseup(function (e)
		{
		    var container = $(".cartItemBlankPincode a");

		    if (!container.is(e.target)
		        && container.has(e.target).length === 0)
		    {
		    	$('#changePinDiv').removeClass('blankPincode');
		    }
		});

/*UF-85*/
$("body.page-cartPage .cart.wrapper .checkout-types li#checkout-id").on("mouseover",function(){
	if($(this).find("a#checkout-enabled.checkout-disabled").length > 0){
		$(this).css("cursor","not-allowed");
	}
	else{
		$(this).css("cursor","default");
	}
});

/*TPR-4471 starts*/
$(window).on("load resize",function(e){
	var style=null;
	if($(window).width() < 773) {
		
		$(document).off("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div").on("click",".productGrid-menu nav > ul > li#shopMicrositeSeller > div",function() {
			if($(window).width() <= 773) {
			$(".productGrid-menu nav .mainli.menu-dropdown-arrow").parent("li.level1").css("background-color","rgb(169, 20, 60)");
			$(this).parent().toggleClass('clicked-menu-mobile');
			if($(this).parent().hasClass('clicked-menu-mobile'))
				mobileVal=true;
			else
				mobileVal=false;
			$(".productGrid-menu nav #mobile-menu-toggle + ul#topul").toggle();
			if($(".productGrid-menu nav ul li#shopMicrositeSeller").hasClass("clicked-menu-mobile"))
				$(".productGrid-menu nav").append("<div class='overlay overlay-sideNav'></div>");
			else
				$(".productGrid-menu nav .overlay.overlay-sideNav").remove();
			}
			});
		
		$(document).off("click",".productGrid-menu nav span#mobile-menu-toggle");
		
	}
	else {
		//$(".productGrid-menu nav #mobile-menu-toggle").next().attr("style",style);
		//$("li.short.words,li.long.words").next().attr("style",style); 
	}
	//$("li.level1 .words").css("display","none");
	
	
	/*$("li.level1 ul.words").hide();
	$(document).on("click","li.level1 .toggle",function(){
		$("li.level1 ul.words").hide();
		$(this).parent("li.level1").children("ul.words").toggleClass("L1Show");
	});*/
	
});

	/*$(document).off("click","li.level1 #mobile-menu-toggle").on("click","li.level1 #mobile-menu-toggle",function(){
		$(".clicked-menu-mobile#shopMicrositeSeller #topul li.level1 ul.words").prop("style","border:3px solid red");
		if($(this).hasClass("menu-dropdown-arrow") && $(this).parent().hasClass("level1")){
			alert("level1");
			$(this).parent(".level1").children("ul.words").css("display","block");
		}
		else if ($(this).hasClass("menu-dropdown-arrow") && $(this).parent().hasClass("short")){
			alert("short");
			$(this).parent().nextAll(".long").css("display","block");
			alert("short after");
		}
		else
			$(this).parent(".level1").children("ul.words").removeClass("L1WordsDisplay");
	});*/
	
/*$(document).ready(function(){
	if($(window).width() < 791){
		$("#shopMicrositeSeller").addClass("clicked-menu active clicked-menu-mobile");
	}
});*/
	/*$(document).off("click","span.mainli").on("click","span.mainli",function(){
			alert();
			//$("#shopMicrositeSeller").addClass("clicked-menu active clicked-menu-mobile");
			//$("#shopMicrositeSeller li.level1 ul.words").css("display","none");
		//$("li.level1 ul.words").css("border","3px solid red");
});*/
	
	/*$(window).on("load resize",function(){
		if($(window).width < 791)
		$("#shopMicrositeSeller").css("line-height",($('.productGrid-header').height()+"px"));
	});*/

/*$(document).on("click","#shopMicrositeSeller > span#mobile-menu-toggle",function(){
	var overlayContainer='<div class="overlay overlay-sideNav" style="display: block;"></div>';
	if($(window).width() < 791 && !$(".productGrid-header-wrapper .productGrid-header .productGrid-menu > nav > .overlay").hasClass("overlay-sideNav"))
		$(".productGrid-header-wrapper .productGrid-header .productGrid-menu > nav").append(overlayContainer);
});*/
/*$(window).on("load",function(){
	//$(".productGrid-menu #shopMicrositeSeller > div").css("display","block");
	$(".productGrid-menu #shopMicrositeSeller > div").html(" ");
	var span_container="<span></span><span></span><span></span>";
	$(".productGrid-menu nav > ul > li > div.toggle").append(span_container);
});*/
$(document).ajaxComplete(function(){
	//$(".productGrid-menu #shopMicrositeSeller > div").css("display","block");
	$(".productGrid-menu #shopMicrositeSeller > div").html(" ");
	var span_container="<span></span><span></span><span></span>";
	$(".productGrid-menu nav > ul > li > div.toggle").append(span_container);
});
$(document).on("click","#shopMicrositeSeller > div",function(){
			var width= ($(".productGrid-header").width() - 50 ) + "px";
		$("#topul").css("width", width);
	});

$(document).on("click",".clicked-menu.active.clicked-menu-mobile > div.toggle",function(){
	//$(".productGrid-menu #shopMicrositeSeller > div").css("margin-left","initial");
	//$(".productGrid-header-wrapper").css("height","75px");
});
$(document).on("click",".clicked-menu.active.clicked-menu-mobile .level1 > span#mobile-menu-toggle",function(){
	$(".productGrid-menu nav #mobile-menu-toggle + ul li.level1").css("background-color","#000");
	if(!$(this).hasClass("menu-dropdown-arrow"))
	$(this).parent(".level1").css("background-color","#A9143C");
	styleMobContainer = $(this).parent(".level1").attr("style");
});
/*$(".clicked-menu.active.clicked-menu-mobile .level1 > span#mobile-menu-toggle").click(function(){
	if(!$(this).hasClass("menu-dropdown-arrow"))
	$(this).parent(".level1").children("div.toggle").css("background-color","#A9143C");
});*/
/*$(document).on("click",".productGrid-header-wrapper .productGrid-header > div button",function(){
	alert();
	$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").animate({marginLeft: 0},{duration:500});
});*/
$(".productGrid-header-wrapper .productGrid-header > div button#micrositesearchButton").click(function(){
	$(".productGrid-header-wrapper").addClass("active_adjust");
	
	$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").animate({marginLeft: 2},{duration:200});
});
$(document).click(function (e)
		{
		    var container = $(".productGrid-header-wrapper .productGrid-header > div #search_form_microsite");
		    //var containerGlobal = $("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form']");
		    //var containerGlobalIcon = $(".searchButtonGlobal");
		    /*if ((!container.is(e.target) // if the target of the click isn't the container...
		        && container.has(e.target).length === 0) && container.find("input[type='text']").val().length === 0) // ... nor a descendant of the container*/		    
		    //
		    /* Change for TISSQAUAT-687 :: IE throws error*/
		    if ((!container.is(e.target) // if the target of the click isn't the container...
			        && container.has(e.target).length === 0) && container.find("input[type='text']").length === 0)
		    {
		    	$(".productGrid-header-wrapper").removeClass("active_adjust");
		    	$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").animate({marginLeft: 1000},{duration:200});
		    }
		   /* if ((((!containerGlobal.is(e.target) // if the target of the click isn't the container...
			        && containerGlobal.has(e.target).length === 0) && containerGlobal.find("input#js-site-search-input").val().length === 0) && 
			        (!containerGlobalIcon.is(e.target) // if the target of the click isn't the container...
			        && containerGlobalIcon.has(e.target).length === 0)) && $(window).width() < 791) // ... nor a descendant of the container
			    {
		    	containerGlobal.css("display","none");
			    }*/
		});
//$(document).ready(function(){
//	if(typeof($(".productGrid-header-wrapper .productGrid-header > div input[type='text']").val() != undefined)){
//		if($(".productGrid-header-wrapper .productGrid-header > div input[type='text']").val().length != 0){
//
//		$(".productGrid-header-wrapper .productGrid-header > div input[type='text']").css("margin-left","0");
//		alert();
//		if(($("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] input#js-site-search-input").val() != "") && $(window).width() < 791)
//			$("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form']").addClass("searchButtonGlobalTextBar");
//		else
//			$("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form']").removeClass("searchButtonGlobalTextBar");
//		}
//	}
//		
//});

$(document).on("click",".searchButtonGlobal",function(){
	if(($("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] input#js-site-search-input").val() == "" && !$("form#search_form[name='search_form']").hasClass("searchButtonGlobalTextBar")) || 
			$("header .content #flip-tabs ~ .bottom .bottom-header-wrapper .search form#search_form[name='search_form'] input#js-site-search-input").val() != "")
		$("form#search_form[name='search_form']").addClass("searchButtonGlobalTextBar");
	else
		$("form#search_form[name='search_form']").removeClass("searchButtonGlobalTextBar");
});
$(window).on("load resize",function(){
	//$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu clicked-menu-mobile");
	$(".productGrid-header-wrapper .productGrid-header>div.productGrid-menu").removeClass("prodGridMargin");
	//$(".productGrid-header-wrapper .overlay.overlay-sideNav").remove();
	if($(window).width() > 773){
		$(".productGrid-menu nav > ul > li > ul > li > ul.words").css("display","");
		$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu-mobile");
		$(".productGrid-menu  nav > ul > li > ul > li.level1").attr("style",null);
		if(deskVal === false){
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").addClass("heightZero").removeClass("heightEase");
			$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu active");
		}
		else{
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").removeClass("heightZero").addClass("heightEase");	
			$(".productGrid-menu #shopMicrositeSeller").addClass("clicked-menu active");
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").css('display','');
		}
	}
	if($(window).width() <= 773 && mobileVal === true){
		$(".productGrid-menu #shopMicrositeSeller").addClass("clicked-menu clicked-menu-mobile active");
		$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow").parent("li.level1").attr("style","background-color:rgb(169, 20, 60)");
		$(".productGrid-menu nav #mobile-menu-toggle + ul#topul").show();
		$(".productGrid-menu nav #mobile-menu-toggle.mainli.menu-dropdown-arrow + ul.words").css("display","block");
	}
	if($(window).width() <= 773 && mobileVal === false){
		$(".productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu active");
	}
	$(document).on("mouseenter",".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller",function(){
		$(".productGrid-menu nav>ul>li.clicked-menu>ul").removeClass("heightZero");
		if($(window).width() > 773){
			$(".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller").addClass("clicked-menu active");
			deskVal=true;
			$(".productGrid-menu nav > ul > li > ul > li").removeClass("hovered");
			$(".productGrid-menu nav > ul > li > ul > li:first-child").addClass("hovered");
			$(".productGrid-menu nav>ul>li.clicked-menu>ul").css("display","");
		}
	});
	$(document).on("mouseleave",".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller",function(){
		if($(window).width() > 773){
			$(".productGrid-header-wrapper .productGrid-header .productGrid-menu #shopMicrositeSeller").removeClass("clicked-menu active");
			deskVal=false;
			$(".productGrid-menu nav > ul > li > ul > li").removeClass("hovered");
		}
	});
	$(document).on("mouseenter",".productGrid-menu nav > ul > li > ul > li",function(){
		if($(window).width() > 773){
			$(".productGrid-menu nav > ul > li > ul > li").removeClass("hovered");
			$(this).addClass("hovered");
		}
	});
	var width= ($(".productGrid-header").width() - 50 ) + "px";
	$("#topul").css("width", width);
});

$(document).on("click",".overlay.overlay-sideNav",function(){
	$(".productGrid-menu nav > ul > li#shopMicrositeSeller > div").trigger("click");
});

$(document).ready(function(){
	$("head").append("<style id='headStyleId'></style>");
});
$(window).on("load resize",function(){
	var rightAdjust = "";
	if($(".productGrid-header-wrapper.active-header").hasClass("active_adjust"))
	rightAdjust = $(".productGrid-header-wrapper.active-header.active_adjust .productGrid-header > div input[type='text']").offset().left+2;
	if($(window).width() <= 773){
		$('head #headStyleId').html(('.productGrid-header-wrapper.active-header .productGrid-header > div.product-grid-search #micrositesearchButton:before{right:'+rightAdjust+'}'));
	}
		else
		$('head #headStyleId').html("");
	
	/*TPR-5061*/
	if($(window).width() <= 1007){
		var lenLi = $(".tabs-block .nav.pdp.productNav>li").length;
		widthPerLi = 100/lenLi;
		$(".tabs-block .nav.pdp.productNav>li").css("width",widthPerLi+"%");
	}
	else
		$(".tabs-block .nav.pdp.productNav>li").css("width","");
	/*TPR-5061*/
	
	/*$(".showcaseItem > a").removeClass("showcase-border");		TISSTRT-1525	
	$(".showcaseItem").eq(1).find("a").addClass("showcase-border");		TISSTRT-1525*/
	
});


//$(document).ready(function(){
//	var countBag = $(".productGrid-header-wrapper .productGrid-header div.bag .mini-cart-link.myBag-sticky > span:nth-of-type(2)").text();
//	$(".productGrid-header-wrapper .productGrid-header div.bag .mini-cart-link.myBag-sticky > span:nth-of-type(2)").text("("+countBag+")");
//});

/*TPR-4471 ends*/
if ($(".facet-list.filter-opt").children().length){
	$("body.page-productGrid .product-listing.product-grid.lazy-grid, body.page-productGrid .product-listing.product-grid.lazy-grid-facet, body.page-productGrid .product-listing.product-grid.lazy-grid-normal").css("padding-top","15px");  //INC144315068
	$("body.page-productGrid .facet-list.filter-opt").css("padding-top","65px");
	var filter_height = $(".facet-list.filter-opt").height() - 8;   /* PRDI-69 */
	$("body.page-productGrid .listing.wrapper .left-block").css("margin-top",filter_height + "px");
	/* UF-253 start */
	if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
	var sort_height ="-" + $(".facet-list.filter-opt").outerHeight() + "px";
	$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);
	}
	else{
		var sort_height =$(".facet-list.filter-opt").outerHeight() - 12 + "px";
		$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);	
	}
}
$(window).on("load resize", function() {
	if ($(".facet-list.filter-opt").children().length){
		$("body.page-productGrid .product-listing.product-grid.lazy-grid, body.page-productGrid .product-listing.product-grid.lazy-grid-facet, body.page-productGrid .product-listing.product-grid.lazy-grid-normal").css("padding-top","15px");  //INC144315068
		$("body.page-productGrid .facet-list.filter-opt").css("padding-top","65px");
		if($('header div.bottom .marketplace.linear-logo').css('display') == 'none'){
			var sort_height ="-" + $(".facet-list.filter-opt").outerHeight() + "px";
			$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);
			}
			else{
				var sort_height =$(".facet-list.filter-opt").height() + 12 + "px";
				$("body.page-productGrid .listing.wrapper .right-block .listing-menu").css("margin-top",sort_height);	
			}
	}	
	/* UF-257 start */
	/*if($('.smartbanner-show .smartbanner').css('display') == 'none'){
		$(".smartbanner-show").css("margin-top","0px");
	}*/
	/* UF-257 end */
});
/* UF-253 end */


$(document).ready(function(){ 
    $(window).scroll(function(){ 
        if ($(this).scrollTop() > 100) { 
            $('#scroll_to_top').fadeIn(); 
        } else { 
            $('#scroll_to_top').fadeOut(); 
        } 
    }); 
    //$('#scroll_to_top').click(function(e){ 
    $(document).on("click","#scroll_to_top",function(){
        $("html, body").animate({ scrollTop: 0 }, 600); 
        return false; 
    }); 
    
    /*UF-68 UF-69*/
    $(".page-cartPage .cart-total-block ul.checkOutBtnBtm > li.checkout-button").css("visibility","visible");
    //$("#pinCodeButtonIdsBtm").addClass("CheckAvailability");
    /*UF-68 UF-69*/
    
    /*TISSQAUATS-881*/
   /* if($(".product-info .product-detail .other-sellers#otherSellerInfoId .other-sellers-link #otherSellersId").text().length != 0)
    	$(".product-info .product-detail .other-sellers#otherSellerInfoId").css("display","block");*/
    /*TISSQAUATS-881*/
});

/* UF-68 UF-69 */
/*function topMarginAdjust(){
var arr_error = ["#unserviceablepincode_tooltip_btm","#error-Id_tooltip_btm","#emptyId_tooltip_btm"];
for(var i=0;i<arr_error.length;i++){
	if($(arr_error[i]).css("display") != "none")
		heightTopAdjust = $(arr_error[i]).outerHeight() + parseInt($(arr_error[i]).css("top"));
}
if($(window).width()>1007){
	var cartBottomCheckTopMargin = $("body.page-cartPage .cart-bottom-block .cart-total-block").height() - $("body.page-cartPage .cartBottomCheck button[name='pinCodeButtonId']").outerHeight() - heightTopAdjust;
	$("body.page-cartPage .cartBottomCheck").addClass("cartBottomCheckShow").css("margin-top",cartBottomCheckTopMargin);
}
else{
	$("body.page-cartPage .cartBottomCheck").addClass("cartBottomCheckShow").css("margin-top","");
	var changePinDivHeight = $("body.page-cartPage .cartBottomCheck.cartBottomCheckShow #changePinDiv").outerHeight();
	var cartBottomCheckTopMarginTab = $("body.page-cartPage .cart-bottom-block .cart-total-block .totals").outerHeight() + $("body.page-cartPage .cart-bottom-block .cart-total-block .checkout-types.onlyCheckoutButton.checkOutBtnBtm").outerHeight() + parseInt($("body.page-cartPage .cart-bottom-block .cart-total-block .checkout-types.onlyCheckoutButton.checkOutBtnBtm").css("margin-top"));
	$("body.page-cartPage .cartBottomCheck.cartBottomCheckShow").css("top",cartBottomCheckTopMarginTab);
}
}*/
function topMarginAdjust(){
	var heightTotals = $("body.page-cartPage .cart-bottom-block .cart-total-block .totals").outerHeight();
	if($(window).width() > 1007)
		$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm").css("margin-top",heightTotals+30);
	else
		$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm").css("margin-top","");
}

$(document).on("click","button[name='pinCodeButtonId']",function(){
	$("input[name='defaultPinCodeIds']").css("color","#000");
}); 

$(document).ajaxComplete(function(){
	//$("body.page-cartPage .cartBottomCheck button#pinCodeButtonIdsBtm").addClass("CheckAvailability");
	$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm li.checkout-button a#checkout-down-enabled.checkout-disabled").css("pointer-events","");
	$("body.page-cartPage .cart-total-block ul.checkOutBtnBtm li.checkout-button a#checkout-down-enabled.checkout-disabled").removeAttr("onclick");
	$("a#checkout-enabled.checkout-disabled").removeAttr("onclick");
	
	/*TISSQAUATS-881*/
	 /*if($(".product-info .product-detail .other-sellers#otherSellerInfoId .other-sellers-link #otherSellersId").text().length != 0)
	    	$(".product-info .product-detail .other-sellers#otherSellerInfoId").css("display","block");*/
	 /*TISSQAUATS-881*/
});

/* UF-68 UF-69 */


if($("#sameAsShippingEmi").is(":checked")){
	$("#sameAsShippingEmi").prev('h2').hide();
}
else{
	$("#sameAsShippingEmi").prev('h2').show();
}
$("#sameAsShippingEmi").click(function(){
	if($("#sameAsShippingEmi").is(":checked")){
		//$("#billingAddress fieldset .error-message").html("");
		$(this).prev('h2').hide();
	}
	else{
		$(this).prev('h2').show();
	}
	});


/*TISSQAEE-335*/
$(window).on("load resize",function(){
	topLeftLocator();
	
	/* TPR-6013 responsive class addition starts*/
	$("body .account .right-account .info,body .account .right-account .password,body .account .right-account .signOut,body .account .right-account .order-history").removeClass("responsiveProfile");
	if($(window).width() <= 1007)
		$("body .account .right-account .info,body .account .right-account .password").addClass("responsiveProfile");
	if($(window).width() <= 773)
		$("body .account .right-account .signOut,body .account .right-account .order-history").addClass("responsiveProfile");
	/* TPR-6013 responsive class addition starts*/
});
$(document).ajaxComplete(function(){
	topLeftLocator();
});
window.onload = function (){
	$(".store-finder-legends").css("display","block");
}

function topLeftLocator(){
var topLegend = $(".store-finder-search").outerHeight() + parseInt($(".store-finder-search").css("margin-bottom")) + $(".gmnoprint.gm-bundled-control .gmnoprint").height() + parseInt($(".gmnoprint.gm-bundled-control").css("margin-top"))  + 10;
$(".store-finder-legends").css("top",topLegend);
var leftLegend = $(".store-finder-map.js-store-finder-map").outerWidth() + parseInt($(".store-finder-map.js-store-finder-map").parent(".js-store-finder").css("margin-left")) - $(".store-finder-legends").width() - parseInt($(".gmnoprint.gm-bundled-control").css("margin-right")) - 15;
$(".store-finder-legends").css("left",leftLegend);
/*$(".store-finder-legends").css("display","block");*/
if($(window).width() < 751){
	$(".store-finder-legends").css("top","");
	$(".store-finder-legends").parents(".body-Content").css("padding-bottom","");
}
if($(window).width() < 313)
	$(".store-finder-legends").css("left","");
}
/*TISSQAEE-335*/
/*TPR-1283-code added for change heading height for brand filtered PLP--Starts*/
$(window).on("load resize",function(){
	var htH1 = $("body.page-productGrid .list_title h1").height();
	var lineHtH1 = parseInt($("body.page-productGrid .list_title h1").css("line-height"));
	var htH1Multiple = htH1/lineHtH1;
	var paddingOrigin = parseInt($("body.page-productGrid .facet-list.filter-opt").css("padding-top"));
	paddingOrigin = paddingOrigin + (lineHtH1 * (htH1Multiple-1));
	$("body.page-productGrid .facet-list.filter-opt").css("padding-top",paddingOrigin);
});
/*TPR-1283-code added for change heading height for brand filtered PLP--Ends*/
$(document).on("mouseover","header .content nav > ul > li > ul > li",function(){
	$(this).parent().parent().find(".toggle").addClass("show_arrow");
});
$(document).on("mouseout","header .content nav > ul > li > ul > li",function(){
	$(this).parent().parent().find(".toggle").removeClass("show_arrow");
});
/*added for UF-353*/
$(document).ready(function(){
	$("#footerByAjaxId ul li a[title='Store Locator']").parent().remove();
});
/*UF-353 end*/
/*Issue in payment page by selecting payment mode in kidswear ST testing*/
$(document).on("click",".cart.wrapper.checkout-payment .left-block .payments.tab-view .nav li",function(){
	$(".cart.wrapper.checkout-payment .left-block .payments.tab-view .nav li").removeClass("active");
	$(this).addClass("active");
	});
/*Issue in payment page by selecting payment mode in kidswear ST testing*/

/* TPR-6013 starts*/
$(document).on("click","body .account .right-account .password .blue.changePass",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","block");
	$(this).css("display","none");
	$("body .account .right-account .password .blue.changePassResponsive").css("display","none");
	$("body .account .right-account .password .blue.crossPass").css("display","block");
});
$(document).on("click","body .account .right-account .password .blue.changePassResponsive",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","block");
	$(this).css("display","none");
	$("body .account .right-account .password .blue.changePass").css("display","none");
	$("body .account .right-account .password .blue.crossPass").css("display","block");
});
$(document).on("click","body .account .right-account .password .blue.crossPass",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","");
	$(this).css("display","");
	if ($(window).width() > 790) {
		$("body .account .right-account .password .blue.changePass").css("display","block");
		$("body .account .right-account .password .blue.changePassResponsive").css("display","none");
	} else {
		$("body .account .right-account .password .blue.changePass").css("display","none");
		$("body .account .right-account .password .blue.changePassResponsive").css("display","block");
	}   
	
});
/*$(document).on("click","body .account .right-account .password .blue.crossPass",function(){
	$("body .account .right-account .password #frmUpdatePassword").css("display","");
	$(this).css("display","");
	$("body .account .right-account .password .blue.changePass").css("display","none");
	$("body .account .right-account .password .blue.changePassResponsive").css("display","block");
});*/

$(".deliveryTrack.status.suman").each(function(){
	$(this).find(".progtrckr.tabs").find("li").each(function(){
	if($(this).find(".commonBlock").length === 2){
	var index = $(this).index();
	$(this).parents(".progtrckr.tabs").siblings(".nav").find("li").eq(index).addClass("greenProgress");

	}
	});
	});
/* TPR-6013 ends*/

/* UF-377 starts */
$(document).ready(function(){
	if ($(".product-specification-accordion").length) {
	    $(".product-specification-accordion").smk_Accordion({
	        closeAble: true,
	        closeOther: true,
	        slideSpeed: 750,
	    });
	}
});
/* UF-377 ends */
(function(q){"object"===typeof exports&&"undefined"!==typeof module?module.exports=q():"function"===typeof define&&define.amd?define([],q):("undefined"!==typeof window?window:"undefined"!==typeof global?global:"undefined"!==typeof self?self:this).SmartBanner=q()})(function(){return function c(f,h,d){function b(e,g){if(!h[e]){if(!f[e]){var l="function"==typeof require&&require;if(!g&&l)return l(e,!0);if(a)return a(e,!0);l=Error("Cannot find module '"+e+"'");throw l.code="MODULE_NOT_FOUND",l;}l=h[e]=
{exports:{}};f[e][0].call(l.exports,function(a){var d=f[e][1][a];return b(d?d:a)},l,l.exports,c,f,h,d)}return h[e].exports}for(var a="function"==typeof require&&require,g=0;g<d.length;g++)b(d[g]);return b}({1:[function(c,f,h){var d=c("xtend/mutable"),b=c("component-query"),a=c("get-doc"),g=a&&a.documentElement,e=c("cookie-cutter"),u=c("ua-parser-js"),l=navigator.language.slice(-2)||navigator.userLanguage.slice(-2)||"us",m={ios:{appMeta:"apple-itunes-app",iconRels:["apple-touch-icon-precomposed","apple-touch-icon"],
getStoreLink:function(){return"https://itunes.apple.com/"+this.options.appStoreLanguage+"/app/id"+this.appId}},android:{appMeta:"google-play-app",iconRels:["android-touch-icon","apple-touch-icon-precomposed","apple-touch-icon"],getStoreLink:function(){return"intent://www.tatacliq.com/post/#Intent;scheme=http;package=com.tul.tatacliq;end;"}},windows:{appMeta:"msApplication-ID",iconRels:["windows-touch-icon","apple-touch-icon-precomposed","apple-touch-icon"],getStoreLink:function(){return"http://www.windowsphone.com/s?appid="+
this.appId}}};c=function(b){var a=u(navigator.userAgent);this.options=d({},{daysHidden:15,daysReminder:90,appStoreLanguage:l,button:"OPEN",store:{ios:"On the App Store",android:"In Google Play",windows:"In the Windows Store"},price:{ios:"FREE",android:"FREE",windows:"FREE"},force:!1},b||{});this.options.force?this.type=this.options.force:"Windows Phone"===a.os.name||"Windows Mobile"===a.os.name?this.type="windows":"iOS"===a.os.name&&6>parseInt(a.os.version)?this.type="ios":"Android"===a.os.name&&
(this.type="android");!this.type||navigator.standalone||e.get("smartbanner-closed")||e.get("smartbanner-installed")||(d(this,m[this.type]),this.parseAppId()&&(this.create(),this.show()))};c.prototype={constructor:c,create:function(){for(var d=this.getStoreLink(),e=this.options.price[this.type]+" - "+this.options.store[this.type],r,g=0;g<this.iconRels.length;g++){var t=b('link[rel="'+this.iconRels[g]+'"]');if(t){r=t.getAttribute("href");break}}var c=a.createElement("div");c.className="smartbanner smartbanner-"+
this.type;c.innerHTML='<div class="smartbanner-container"><a href="javascript:void(0);" class="smartbanner-close">&times;</a><span class="smartbanner-icon" style="background-image: url('+r+')"></span><div class="smartbanner-info"><div class="smartbanner-title">'+this.options.title+"</div><div>"+this.options.author+"</div><span>"+e+'</span></div><a href="'+d+'" class="smartbanner-button"><span class="smartbanner-button-text">'+this.options.button+"</span></a></div>";a.body?a.body.appendChild(c):a&&
a.addEventListener("DOMContentLoaded",function(){a.body.appendChild(c)});b(".smartbanner-button",c).addEventListener("click",this.install.bind(this),!1);b(".smartbanner-close",c).addEventListener("click",this.close.bind(this),!1)},hide:function(){g.classList.remove("smartbanner-show")},show:function(){g.classList.add("smartbanner-show")},close:function(){this.hide();e.set("smartbanner-closed","true",{path:"/",expires:new Date(+new Date+864E5*this.options.daysHidden)})},install:function(){this.hide();
e.set("smartbanner-installed","true",{path:"/",expires:new Date(+new Date+864E5*this.options.daysReminder)})},parseAppId:function(){var a=b('meta[name="'+this.appMeta+'"]');if(a)return this.appId="windows"===this.type?a.getAttribute("content"):/app-id=([^\s,]+)/.exec(a.getAttribute("content"))[1]}};f.exports=c},{"component-query":2,"cookie-cutter":3,"get-doc":4,"ua-parser-js":6,"xtend/mutable":7}],2:[function(c,f,h){function d(b,a){return a.querySelector(b)}h=f.exports=function(b,a){a=a||document;
return d(b,a)};h.all=function(b,a){a=a||document;return a.querySelectorAll(b)};h.engine=function(b){if(!b.one)throw Error(".one callback required");if(!b.all)throw Error(".all callback required");d=b.one;h.all=b.all;return h}},{}],3:[function(c,f,h){h=f.exports=function(d){d||(d={});"string"===typeof d&&(d={cookie:d});void 0===d.cookie&&(d.cookie="");return{get:function(b){for(var a=d.cookie.split(/;\s*/),g=0;g<a.length;g++){var e=a[g].split("=");if(unescape(e[0])===b)return unescape(e[1])}},set:function(b,
a,g){g||(g={});b=escape(b)+"="+escape(a);g.expires&&(b+="; expires="+g.expires);g.path&&(b+="; path="+escape(g.path));return d.cookie=b}}};"undefined"!==typeof document&&(c=h(document),h.get=c.get,h.set=c.set)},{}],4:[function(c,f,h){c=c("has-dom");f.exports=c()?document:null},{"has-dom":5}],5:[function(c,f,h){f.exports=function(){return"undefined"!==typeof window&&"undefined"!==typeof document&&"function"===typeof document.createElement}},{}],6:[function(c,f,h){(function(d,b){var a={extend:function(a,
b){for(var d in b)-1!=="browser cpu device engine os".indexOf(d)&&0===b[d].length%2&&(a[d]=b[d].concat(a[d]));return a},has:function(a,b){return"string"===typeof a?-1!==b.toLowerCase().indexOf(a.toLowerCase()):!1},lowerize:function(a){return a.toLowerCase()},major:function(a){return"string"===typeof a?a.split(".")[0]:b}},g=function(){for(var a,d=0,c,g,e,k,h,f,l=arguments;d<l.length&&!h;){var m=l[d],n=l[d+1];if("undefined"===typeof a)for(e in a={},n)n.hasOwnProperty(e)&&(k=n[e],"object"===typeof k?
a[k[0]]=b:a[k]=b);for(c=g=0;c<m.length&&!h;)if(h=m[c++].exec(this.getUA()))for(e=0;e<n.length;e++)f=h[++g],k=n[e],"object"===typeof k&&0<k.length?2==k.length?a[k[0]]="function"==typeof k[1]?k[1].call(this,f):k[1]:3==k.length?a[k[0]]="function"!==typeof k[1]||k[1].exec&&k[1].test?f?f.replace(k[1],k[2]):b:f?k[1].call(this,f,k[2]):b:4==k.length&&(a[k[0]]=f?k[3].call(this,f.replace(k[1],k[2])):b):a[k]=f?f:b;d+=2}return a},e=function(d,e){for(var c in e)if("object"===typeof e[c]&&0<e[c].length)for(var g=
0;g<e[c].length;g++){if(a.has(e[c][g],d))return"?"===c?b:c}else if(a.has(e[c],d))return"?"===c?b:c;return d},c={ME:"4.90","NT 3.11":"NT3.51","NT 4.0":"NT4.0",2E3:"NT 5.0",XP:["NT 5.1","NT 5.2"],Vista:"NT 6.0",7:"NT 6.1",8:"NT 6.2","8.1":"NT 6.3",10:["NT 6.4","NT 10.0"],RT:"ARM"},l={browser:[[/(opera\smini)\/([\w\.-]+)/i,/(opera\s[mobiletab]+).+version\/([\w\.-]+)/i,/(opera).+version\/([\w\.]+)/i,/(opera)[\/\s]+([\w\.]+)/i],["name","version"],[/\s(opr)\/([\w\.]+)/i],[["name","Opera"],"version"],[/(kindle)\/([\w\.]+)/i,
/(lunascape|maxthon|netfront|jasmine|blazer)[\/\s]?([\w\.]+)*/i,/(avant\s|iemobile|slim|baidu)(?:browser)?[\/\s]?([\w\.]*)/i,/(?:ms|\()(ie)\s([\w\.]+)/i,/(rekonq)\/([\w\.]+)*/i,/(chromium|flock|rockmelt|midori|epiphany|silk|skyfire|ovibrowser|bolt|iron|vivaldi|iridium|phantomjs)\/([\w\.-]+)/i],["name","version"],[/(trident).+rv[:\s]([\w\.]+).+like\sgecko/i],[["name","IE"],"version"],[/(edge)\/((\d+)?[\w\.]+)/i],["name","version"],[/(yabrowser)\/([\w\.]+)/i],[["name","Yandex"],"version"],[/(comodo_dragon)\/([\w\.]+)/i],
[["name",/_/g," "],"version"],[/(chrome|omniweb|arora|[tizenoka]{5}\s?browser)\/v?([\w\.]+)/i,/(qqbrowser)[\/\s]?([\w\.]+)/i],["name","version"],[/(uc\s?browser)[\/\s]?([\w\.]+)/i,/ucweb.+(ucbrowser)[\/\s]?([\w\.]+)/i,/JUC.+(ucweb)[\/\s]?([\w\.]+)/i],[["name","UCBrowser"],"version"],[/(dolfin)\/([\w\.]+)/i],[["name","Dolphin"],"version"],[/((?:android.+)crmo|crios)\/([\w\.]+)/i],[["name","Chrome"],"version"],[/XiaoMi\/MiuiBrowser\/([\w\.]+)/i],["version",["name","MIUI Browser"]],[/android.+version\/([\w\.]+)\s+(?:mobile\s?safari|safari)/i],
["version",["name","Android Browser"]],[/FBAV\/([\w\.]+);/i],["version",["name","Facebook"]],[/fxios\/([\w\.-]+)/i],["version",["name","Firefox"]],[/version\/([\w\.]+).+?mobile\/\w+\s(safari)/i],["version",["name","Mobile Safari"]],[/version\/([\w\.]+).+?(mobile\s?safari|safari)/i],["version","name"],[/webkit.+?(mobile\s?safari|safari)(\/[\w\.]+)/i],["name",["version",e,{"1.0":"/8","1.2":"/1","1.3":"/3","2.0":"/412","2.0.2":"/416","2.0.3":"/417","2.0.4":"/419","?":"/"}]],[/(konqueror)\/([\w\.]+)/i,
/(webkit|khtml)\/([\w\.]+)/i],["name","version"],[/(navigator|netscape)\/([\w\.-]+)/i],[["name","Netscape"],"version"],[/(swiftfox)/i,/(icedragon|iceweasel|camino|chimera|fennec|maemo\sbrowser|minimo|conkeror)[\/\s]?([\w\.\+]+)/i,/(firefox|seamonkey|k-meleon|icecat|iceape|firebird|phoenix)\/([\w\.-]+)/i,/(mozilla)\/([\w\.]+).+rv\:.+gecko\/\d+/i,/(polaris|lynx|dillo|icab|doris|amaya|w3m|netsurf|sleipnir)[\/\s]?([\w\.]+)/i,/(links)\s\(([\w\.]+)/i,/(gobrowser)\/?([\w\.]+)*/i,/(ice\s?browser)\/v?([\w\._]+)/i,
/(mosaic)[\/\s]([\w\.]+)/i],["name","version"]],cpu:[[/(?:(amd|x(?:(?:86|64)[_-])?|wow|win)64)[;\)]/i],[["architecture","amd64"]],[/(ia32(?=;))/i],[["architecture",a.lowerize]],[/((?:i[346]|x)86)[;\)]/i],[["architecture","ia32"]],[/windows\s(ce|mobile);\sppc;/i],[["architecture","arm"]],[/((?:ppc|powerpc)(?:64)?)(?:\smac|;|\))/i],[["architecture",/ower/,"",a.lowerize]],[/(sun4\w)[;\)]/i],[["architecture","sparc"]],[/((?:avr32|ia64(?=;))|68k(?=\))|arm(?:64|(?=v\d+;))|(?=atmel\s)avr|(?:irix|mips|sparc)(?:64)?(?=;)|pa-risc)/i],
[["architecture",a.lowerize]]],device:[[/\((ipad|playbook);[\w\s\);-]+(rim|apple)/i],["model","vendor",["type","tablet"]],[/applecoremedia\/[\w\.]+ \((ipad)/],["model",["vendor","Apple"],["type","tablet"]],[/(apple\s{0,1}tv)/i],[["model","Apple TV"],["vendor","Apple"]],[/(archos)\s(gamepad2?)/i,/(hp).+(touchpad)/i,/(kindle)\/([\w\.]+)/i,/\s(nook)[\w\s]+build\/(\w+)/i,/(dell)\s(strea[kpr\s\d]*[\dko])/i],["vendor","model",["type","tablet"]],[/(kf[A-z]+)\sbuild\/[\w\.]+.*silk\//i],["model",["vendor",
"Amazon"],["type","tablet"]],[/(sd|kf)[0349hijorstuw]+\sbuild\/[\w\.]+.*silk\//i],[["model",e,{"Fire Phone":["SD","KF"]}],["vendor","Amazon"],["type","mobile"]],[/\((ip[honed|\s\w*]+);.+(apple)/i],["model","vendor",["type","mobile"]],[/\((ip[honed|\s\w*]+);/i],["model",["vendor","Apple"],["type","mobile"]],[/(blackberry)[\s-]?(\w+)/i,/(blackberry|benq|palm(?=\-)|sonyericsson|acer|asus|dell|huawei|meizu|motorola|polytron)[\s_-]?([\w-]+)*/i,/(hp)\s([\w\s]+\w)/i,/(asus)-?(\w+)/i],["vendor","model",["type",
"mobile"]],[/\(bb10;\s(\w+)/i],["model",["vendor","BlackBerry"],["type","mobile"]],[/android.+(transfo[prime\s]{4,10}\s\w+|eeepc|slider\s\w+|nexus 7)/i],["model",["vendor","Asus"],["type","tablet"]],[/(sony)\s(tablet\s[ps])\sbuild\//i,/(sony)?(?:sgp.+)\sbuild\//i],[["vendor","Sony"],["model","Xperia Tablet"],["type","tablet"]],[/(?:sony)?(?:(?:(?:c|d)\d{4})|(?:so[-l].+))\sbuild\//i],[["vendor","Sony"],["model","Xperia Phone"],["type","mobile"]],[/\s(ouya)\s/i,/(nintendo)\s([wids3u]+)/i],["vendor",
"model",["type","console"]],[/android.+;\s(shield)\sbuild/i],["model",["vendor","Nvidia"],["type","console"]],[/(playstation\s[34portablevi]+)/i],["model",["vendor","Sony"],["type","console"]],[/(sprint\s(\w+))/i],[["vendor",e,{HTC:"APA",Sprint:"Sprint"}],["model",e,{"Evo Shift 4G":"7373KT"}],["type","mobile"]],[/(lenovo)\s?(S(?:5000|6000)+(?:[-][\w+]))/i],["vendor","model",["type","tablet"]],[/(htc)[;_\s-]+([\w\s]+(?=\))|\w+)*/i,/(zte)-(\w+)*/i,/(alcatel|geeksphone|huawei|lenovo|nexian|panasonic|(?=;\s)sony)[_\s-]?([\w-]+)*/i],
["vendor",["model",/_/g," "],["type","mobile"]],[/(nexus\s9)/i],["model",["vendor","HTC"],["type","tablet"]],[/[\s\(;](xbox(?:\sone)?)[\s\);]/i],["model",["vendor","Microsoft"],["type","console"]],[/(kin\.[onetw]{3})/i],[["model",/\./g," "],["vendor","Microsoft"],["type","mobile"]],[/\s(milestone|droid(?:[2-4x]|\s(?:bionic|x2|pro|razr))?(:?\s4g)?)[\w\s]+build\//i,/mot[\s-]?(\w+)*/i,/(XT\d{3,4}) build\//i,/(nexus\s[6])/i],["model",["vendor","Motorola"],["type","mobile"]],[/android.+\s(mz60\d|xoom[\s2]{0,2})\sbuild\//i],
["model",["vendor","Motorola"],["type","tablet"]],[/android.+((sch-i[89]0\d|shw-m380s|gt-p\d{4}|gt-n8000|sgh-t8[56]9|nexus 10))/i,/((SM-T\w+))/i],[["vendor","Samsung"],"model",["type","tablet"]],[/((s[cgp]h-\w+|gt-\w+|galaxy\snexus|sm-n900))/i,/(sam[sung]*)[\s-]*(\w+-?[\w-]*)*/i,/sec-((sgh\w+))/i],[["vendor","Samsung"],"model",["type","mobile"]],[/(samsung);smarttv/i],["vendor","model",["type","smarttv"]],[/\(dtv[\);].+(aquos)/i],["model",["vendor","Sharp"],["type","smarttv"]],[/sie-(\w+)*/i],["model",
["vendor","Siemens"],["type","mobile"]],[/(maemo|nokia).*(n900|lumia\s\d+)/i,/(nokia)[\s_-]?([\w-]+)*/i],[["vendor","Nokia"],"model",["type","mobile"]],[/android\s3\.[\s\w;-]{10}(a\d{3})/i],["model",["vendor","Acer"],["type","tablet"]],[/android\s3\.[\s\w;-]{10}(lg?)-([06cv9]{3,4})/i],[["vendor","LG"],"model",["type","tablet"]],[/(lg) netcast\.tv/i],["vendor","model",["type","smarttv"]],[/(nexus\s[45])/i,/lg[e;\s\/-]+(\w+)*/i],["model",["vendor","LG"],["type","mobile"]],[/android.+(ideatab[a-z0-9\-\s]+)/i],
["model",["vendor","Lenovo"],["type","tablet"]],[/linux;.+((jolla));/i],["vendor","model",["type","mobile"]],[/((pebble))app\/[\d\.]+\s/i],["vendor","model",["type","wearable"]],[/android.+;\s(glass)\s\d/i],["model",["vendor","Google"],["type","wearable"]],[/android.+(\w+)\s+build\/hm\1/i,/android.+(hm[\s\-_]*note?[\s_]*(?:\d\w)?)\s+build/i,/android.+(mi[\s\-_]*(?:one|one[\s_]plus)?[\s_]*(?:\d\w)?)\s+build/i],[["model",/_/g," "],["vendor","Xiaomi"],["type","mobile"]],[/\s(tablet)[;\/\s]/i,/\s(mobile)[;\/\s]/i],
[["type",a.lowerize],"vendor","model"]],engine:[[/windows.+\sedge\/([\w\.]+)/i],["version",["name","EdgeHTML"]],[/(presto)\/([\w\.]+)/i,/(webkit|trident|netfront|netsurf|amaya|lynx|w3m)\/([\w\.]+)/i,/(khtml|tasman|links)[\/\s]\(?([\w\.]+)/i,/(icab)[\/\s]([23]\.[\d\.]+)/i],["name","version"],[/rv\:([\w\.]+).*(gecko)/i],["version","name"]],os:[[/microsoft\s(windows)\s(vista|xp)/i],["name","version"],[/(windows)\snt\s6\.2;\s(arm)/i,/(windows\sphone(?:\sos)*|windows\smobile|windows)[\s\/]?([ntce\d\.\s]+\w)/i],
["name",["version",e,c]],[/(win(?=3|9|n)|win\s9x\s)([nt\d\.]+)/i],[["name","Windows"],["version",e,c]],[/\((bb)(10);/i],[["name","BlackBerry"],"version"],[/(blackberry)\w*\/?([\w\.]+)*/i,/(tizen)[\/\s]([\w\.]+)/i,/(android|webos|palm\sos|qnx|bada|rim\stablet\sos|meego|contiki)[\/\s-]?([\w\.]+)*/i,/linux;.+(sailfish);/i],["name","version"],[/(symbian\s?os|symbos|s60(?=;))[\/\s-]?([\w\.]+)*/i],[["name","Symbian"],"version"],[/\((series40);/i],["name"],[/mozilla.+\(mobile;.+gecko.+firefox/i],[["name",
"Firefox OS"],"version"],[/(nintendo|playstation)\s([wids34portablevu]+)/i,/(mint)[\/\s\(]?(\w+)*/i,/(mageia|vectorlinux)[;\s]/i,/(joli|[kxln]?ubuntu|debian|[open]*suse|gentoo|(?=\s)arch|slackware|fedora|mandriva|centos|pclinuxos|redhat|zenwalk|linpus)[\/\s-]?([\w\.-]+)*/i,/(hurd|linux)\s?([\w\.]+)*/i,/(gnu)\s?([\w\.]+)*/i],["name","version"],[/(cros)\s[\w]+\s([\w\.]+\w)/i],[["name","Chromium OS"],"version"],[/(sunos)\s?([\w\.]+\d)*/i],[["name","Solaris"],"version"],[/\s([frentopc-]{0,4}bsd|dragonfly)\s?([\w\.]+)*/i],
["name","version"],[/(ip[honead]+)(?:.*os\s([\w]+)*\slike\smac|;\sopera)/i],[["name","iOS"],["version",/_/g,"."]],[/(mac\sos\sx)\s?([\w\s\.]+\w)*/i,/(macintosh|mac(?=_powerpc)\s)/i],[["name","Mac OS"],["version",/_/g,"."]],[/((?:open)?solaris)[\/\s-]?([\w\.]+)*/i,/(haiku)\s(\w+)/i,/(aix)\s((\d)(?=\.|\)|\s)[\w\.]*)*/i,/(plan\s9|minix|beos|os\/2|amigaos|morphos|risc\sos|openvms)/i,/(unix)\s?([\w\.]+)*/i],["name","version"]]},m=function(b,c){if(!(this instanceof m))return(new m(b,c)).getResult();var e=
b||(d&&d.navigator&&d.navigator.userAgent?d.navigator.userAgent:""),f=c?a.extend(l,c):l;this.getBrowser=function(){var b=g.apply(this,f.browser);b.major=a.major(b.version);return b};this.getCPU=function(){return g.apply(this,f.cpu)};this.getDevice=function(){return g.apply(this,f.device)};this.getEngine=function(){return g.apply(this,f.engine)};this.getOS=function(){return g.apply(this,f.os)};this.getResult=function(){return{ua:this.getUA(),browser:this.getBrowser(),engine:this.getEngine(),os:this.getOS(),
device:this.getDevice(),cpu:this.getCPU()}};this.getUA=function(){return e};this.setUA=function(a){e=a;return this};this.setUA(e);return this};m.VERSION="0.7.10";m.BROWSER={NAME:"name",MAJOR:"major",VERSION:"version"};m.CPU={ARCHITECTURE:"architecture"};m.DEVICE={MODEL:"model",VENDOR:"vendor",TYPE:"type",CONSOLE:"console",MOBILE:"mobile",SMARTTV:"smarttv",TABLET:"tablet",WEARABLE:"wearable",EMBEDDED:"embedded"};m.ENGINE={NAME:"name",VERSION:"version"};m.OS={NAME:"name",VERSION:"version"};"undefined"!==
typeof h?("undefined"!==typeof f&&f.exports&&(h=f.exports=m),h.UAParser=m):d.UAParser=m;var n=d.jQuery||d.Zepto;if("undefined"!==typeof n){var p=new m;n.ua=p.getResult();n.ua.get=function(){return p.getUA()};n.ua.set=function(a){p.setUA(a);a=p.getResult();for(var b in a)n.ua[b]=a[b]}}})("object"===typeof window?window:this)},{}],7:[function(c,f,h){f.exports=function(b){for(var a=1;a<arguments.length;a++){var c=arguments[a],e;for(e in c)d.call(c,e)&&(b[e]=c[e])}return b};var d=Object.prototype.hasOwnProperty},
{}]},{},[1])(1)});
/*Facebook Variables to be Provided by TCS*/
var fbID = '';
var fbAccessToken = '';

/* Which widget we are working with */
var widgetMode = '';

/* Initialize company specific variables */
// ecompany = 'dev.tul.com';
rootEP = $('#rootEPForHttp').val();
if (location.protocol === "https:") {
	rootEP = $('#rootEPForHttps').val();
}
recEndPoint = rootEP + '/SocialGenomix/recommendations/products';


// *******************************************************************************
// Populating Dynamic Parameter Values For IA
var allsizes = [ "XXS", "XS", "S", "M", "L", "XL", "XXL" ];
var hotDropdownselected = 'All Departments';
var sortDropdownselected = "";
var currentPageURL = window.location.href;
ecompany = $('#ecompanyForIA').val();
var DamMediaHost = $('#DamMediaHost').val();
var mplStaticResourceHost = $('#mplStaticResourceHost').val();
user_id = getCookie("mpl-user");
user_type = getCookie("mpl-userType");
spid = $('#ia_product_code').val();
domain = $('#ia_product_rootCategory_type').val();
search_string = $('#ia_search_text').val();
searchCategory_id = $('#selectedSearchCategoryId').val(); // For Normal search
searchCategory_idFromMicrosite = $('#selectedSearchCategoryIdMicrosite').val(); // For
																				// Microsite
																				// search
var daysDif = '';
var is_new_product = false;

if (searchCategory_id) {
	if (searchCategory_id.indexOf("MSH") > -1) {
		category_id = searchCategory_id;
	} else if (searchCategory_id.indexOf("MBH") > -1) {
		brand_id = searchCategory_id;
	} else if (searchCategory_id !== 'all') {
		seller_id = searchCategory_id;
	} else {
		category_id = '';
	}
}
if (searchCategory_idFromMicrosite) { // only for microsite search
	if (searchCategory_idFromMicrosite.indexOf("MBH") > -1) {
		brand_id = searchCategory_idFromMicrosite;
	} else {
		category_id = '';
	}
}
// Array[productCode] for wishlist and cart pages
$.each($('input[name=productArrayForIA]'), function(prodArrayIndex, val) {
	if (prodArrayIndex < 5) {
		site_product_array.push(val.value);
	}
});

// For homepage,search,searchEmpty,wishlist,cartPage,orderConfirmation
site_page_type = $('#ia_site_page_id').val();

if (site_page_type == 'productDetails') {
	site_page_type = 'productpage';
}

if (site_page_type == 'myStyleProfile') {
	site_page_type = 'viewAllTrending'; // My Recommendation Page
	$.each($('input[name=categoryArrayForIA]'), function(catArrayIndex, val) {
		if (catArrayIndex < 5) {
			category_array.push(val.value);
		}
	});
	$.each($('input[name=brandArrayForIA]'), function(brandArrayIndex, val) {
		if (brandArrayIndex < 5) {
			brand_array.push(val.value);
		}
	});

}
// changes start for url structure changes
if (currentPageURL.indexOf("/c-msh") > -1
		|| currentPageURL.indexOf("/c-ssh") > -1) {
	site_page_type = 'category_landing_page';
	category_id = $('#ia_category_code').val();

}
if (currentPageURL.indexOf("/s/") > -1) {
	site_page_type = 'seller';
	seller_id = currentPageURL.split('/').pop();
	if (seller_id.indexOf('?') > 0) {
		seller_id = seller_id.substr(0, seller_id.indexOf('?'));
	}
}
if (currentPageURL.indexOf("/c-mbh") > -1) {
	site_page_type = 'brand';
	brand_id = $('#ia_category_code').val();

}
// changes end
if (currentPageURL.indexOf("/m/") > -1) {
	site_page_type = 'seller';
	seller_id = $('#mSellerID').val();
}
var footerPageType = $('#ia_footer_page_id').val();
if (footerPageType === 'footerLinkPage') {
	// site_page_type = 'footerPage';
	site_page_type = 'marketplace';
}
if (currentPageURL.indexOf("/search/helpmeshop") > -1) {
	category_id = $('#helpMeShopSearchCatId').val();
}
if (site_page_type === 'orderConfirmationPage') {
	site_page_type = 'orderConfirmation';
}
// *******************************************************************************
/* Only proceed on page types with recommendations */
site_page_array = [ "homepage", "search", "searchEmpty", "cartPage",
		"orderConfirmation", "wishlist", "seller", "brand", "productpage",
		"product", "category_landing_page", "viewSellers", "viewAllTrending",
		"marketplace" ];

/* Get prior information from IA-set Cookies and start timers */
setTimeout(function() {
	init_iaplugin();

	if (jQuery.inArray(site_page_type, site_page_array) > -1) {
		callTataRec();
	}
}, 2000);


/*
1121425 alternate for jquery unique function 
*/    

function distinctVal(arr){
    var newArray = [];
    for(var i=0, j=arr.length; i<j; i++){
        if(newArray.indexOf(arr[i]) == -1)
              newArray.push(arr[i]);  
    }
    return newArray;
}
/*
 * Check if user has logged into the site
 */
function checkUser() {
	user_type = getCookie("mpl-userType");
	if (user_type.indexOf("facebook") === 0
			|| user_type.indexOf("FACEBOOK_LOGIN") === 0) {
		// uid = fbID;
		// TISPRD-2183 FIX
		window.fbAsyncInit = function() {
			FB.getLoginStatus(function(response) {
				if (response.status === "connected") {
					uid = FB.getUserID();
				}
			});
		};
		// TISPRD-2183 FIX end

		/* New login, use current credentials */
		if (getCookie("IAUSERTYPE") !== 'REGISTERED'
				|| getCookie("IAUSERTYPE") !== 'site_user') {
			/*
			 * New elevated id we're not familiar with, get a new session id
			 * before continuing
			 */
			if (getCookie("IAUSERID").length > 0
					&& getCookie("IAUSERID").indexOf(uid) !== 0) {
				jQuery.ajax({
					type : "GET",
					url : rootEP + '/SocialGenomix/recommendations/init/jsonp',
					jsonp : 'callback',
					dataType : 'jsonp',
					data : {
						'referring_url' : document.referrer,
						'ecompany' : ecompany
					},
					contentType : 'application/javascript',
					success : function(response) {
						document.cookie = 'IASESSIONID=' + response.session_id
								+ '; path=/';
						ssid = response.session_id;
						callEventApi('login', null);
					}
				});
			} else {
				callEventApi('login', null);
			}
			// callFBApi(uid, FB.getAccessToken(), ssid);
			// TISPRD-2183 FIX
			window.fbAsyncInit = function() {
				FB.getLoginStatus(function(response) {
					if (response.status === "connected") {
						uid = FB.getUserID();
						callFBApi(uid, FB.getAccessToken(), ssid);
					}
				});
			};
			// TISPRD-2183 FIX end
		}
	} else {
		if (getCookie("IAUSERTYPE").indexOf("facebook") === 0
				|| getCookie("IAUSERTYPE").indexOf("FACEBOOK_LOGIN") === 0) {
			/*
			 * Previously logged into Facebook, not anymore. Send former
			 * credentials in Logout Event
			 */
			callEventApi('logout', {
				"user_id" : getCookie("IAUSERID"),
				"user_type" : getCookie("IAUSERTYPE")
			});
		}
		if (user_type.indexOf("anonymous") === 0
				|| user_type.indexOf("session") === 0) {
			/* Set if we're a session user */
			if (getCookie("IAUSERTYPE").indexOf("REGISTERED") === 0
					|| getCookie("IAUSERTYPE").indexOf("site_user") === 0) {
				/*
				 * Previously logged in as site user, not anymore. Send former
				 * credentials in Logout Event
				 */
				callEventApi('logout', {
					"user_id" : getCookie("IAUSERID"),
					"user_type" : getCookie("IAUSERTYPE")
				});
			}
			user_type = "session";
			uid = ssid;
		} else if (user_type.indexOf("REGISTERED") === 0
				|| user_type.indexOf("site_user") === 0) {
			/* Set if we're a site user */
			uid = getCookie("mpl-user");
			user_type = "site_user";

			/* New login, use current credentials */
			if (getCookie("IAUSERTYPE") !== 'site_user') {
				/*
				 * New elevated id we're not familiar with, get a new session id
				 * before continuing
				 */
				if (getCookie("IAUSERID").length > 0
						&& getCookie("IAUSERID").indexOf(uid) !== 0) {
					jQuery.ajax({
						type : "GET",
						url : rootEP
								+ '/SocialGenomix/recommendations/init/jsonp',
						jsonp : 'callback',
						dataType : 'jsonp',
						data : {
							'referring_url' : document.referrer,
							'ecompany' : ecompany
						},
						contentType : 'application/javascript',
						success : function(response) {
							document.cookie = 'IASESSIONID='
									+ response.session_id + '; path=/';
							ssid = response.session_id;
							callEventApi('login', null);
						}
					});
				} else {
					callEventApi('login', null);
				}
				document.cookie = 'IAEMAILID=' + uid + '; path=/';
			}
		}
	}
	document.cookie = 'IAUSERID=' + uid + '; path=/';
	document.cookie = 'IAUSERTYPE=' + user_type + '; path=/';
}

/*
 * Scan through the webpage and call the APIs for each element we detect
 */
function callForEachElement(params) {
	/* Check against all product widget elements */
	for (var i = 0; i < productWidgetElement.length; i++) {
		if (document.getElementById(productWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/products';
			if (productWidget[i] !== "normal") {
				endpoint += '/' + productWidget[i];

			}
			if (productWidget[i].indexOf("hot") === 0
					&& site_page_type === "viewAllTrending") {
				params.count = '100';
			} else {
				params.count = '15';
			}
			if (productWidget[i].indexOf("search") === 0
					&& site_page_type === 'viewAllTrending') {
				params.count = '100';
			}
			if (productWidget[i].indexOf("search") === 0
					&& site_page_type === 'search') {
				params.count = '15';
			}
			params.htmlElement = productWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}

	/* Check against all brand widget elements */
	for (var i = 0; i < brandWidgetElement.length; i++) {
		if (document.getElementById(brandWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/';
			endpoint += brandWidget[i];

			params.count = '9';
			params.htmlElement = brandWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}

	/* Check against all category widget elements */
	for (var i = 0; i < categoryWidgetElement.length; i++) {
		if (document.getElementById(categoryWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/';
			endpoint += categoryWidget[i];

			params.count = '8';
			if (categoryWidgetElement[i].indexOf('ia_categories_recent') > -1) {
				params.count = '2';
			}
			params.htmlElement = categoryWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}

	/* Check against all collection widget elements */
	for (var i = 0; i < collectionWidgetElement.length; i++) {
		if (document.getElementById(collectionWidgetElement[i]) !== null) {
			var endpoint = '/SocialGenomix/recommendations/';
			endpoint += collectionWidget[i];

			params.count = '3';
			params.htmlElement = collectionWidgetElement[i];
			callRecApi(params, rootEP + endpoint);
		}
	}
}

/* Filter based on categories, price ranges, and sale status */
function getFilteredRecommendations(widgetElement, respData, priceRanges, sale,
		requestId) {
	/* Return an array where each index corresponds to the html of a page */
	var filteredRespData = [];
	var page = -1;
	var afterFilter = 0;

	jQuery.each(respData, function() {
		/*
		 * Narrow by whether product is on sale, exclude product if the price is
		 * not less than original price
		 */
		// if((parseInt(this.price) >== parseInt(this.original_price)) &&
		// sale.length > 0) {
		if ((parseInt(this.discounted_price) === 0) && sale.length > 0) {
			return;
		}
		/* Check if within sale range if price range specified */
		var saleSatisfied = false;
		if (parseInt(this.discounted_price) > 0) {
			saleSatisfied = true;
		} else {// no sale filter
			saleSatisfied = false;
		}
		/* Check if within price range if price range specified */
		var priceSatisfied = false;
		if (priceRanges.length === 0) { // no price filter
			priceSatisfied = true;
		}
		for (var i = 0; i < priceRanges.length; i++) {
			var prices = priceRanges[i].split(' - ');
			if (priceRanges[i].indexOf('And') > -1) {
				var pricess = priceRanges[i].replace('And', '-');
				prices = pricess.split(' - ');
			}
			var min = parseInt(prices[0].substr(1));
			var max = parseInt(prices[1].substr(1));
			if (isNaN(max)) {
				max = '100000000';
			}
			if (min <= parseInt(this.price) && parseInt(this.price) <= max) {
				priceSatisfied = true;
				break; // condition met, stop searching
			}
		}
		/*
		 * If all our conditions are met, make HTML for this and add it to the
		 * page
		 */
		if (priceRanges.length > 0 && sale.length > 0) {

			if (priceSatisfied === true && saleSatisfied === true) {
				if (afterFilter % 20 === 0) {
					page++;
					filteredRespData[page] = "";
				}
				filteredRespData[page] += makeProductHtml(widgetElement, this,
						requestId);
				afterFilter++;
			}
		} else if (priceRanges.length > 0) {
			if (priceSatisfied === true) {
				if (afterFilter % 20 === 0) {
					page++;
					filteredRespData[page] = "";
				}
				filteredRespData[page] += makeProductHtml(widgetElement, this,
						requestId);
				afterFilter++;
			}
		} else if (sale.length > 0) {
			if (saleSatisfied === true) {
				if (afterFilter % 20 === 0) {
					page++;
					filteredRespData[page] = "";
				}
				filteredRespData[page] += makeProductHtml(widgetElement, this,
						requestId);
				afterFilter++;
			}
		} else {
			if (afterFilter % 20 === 0) {
				page++;
				filteredRespData[page] = "";
			}
			filteredRespData[page] += makeProductHtml(widgetElement, this,
					requestId);
			afterFilter++;
		}
	});
	for (var i = 0; i < 5; i++) {
		if (i <= page) {
			$('#iapage' + (i + 1)).css("visibility", "visible");
			$('#iapage_next').css("visibility", "visible");
			$('#iapage' + (i + 1)).removeClass('active');
			$('#iapage1').addClass('active');
		} else {
			$('#iapage' + (i + 1)).css("visibility", "hidden");
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
		if (i % 20 === 0) {
			page++;
			reorderedData[page] = "";
		}
		reorderedData[page] += makeProductHtml(widgetElement, this, requestId);
		i++;
	});
	return reorderedData;
}

/* TCS-provided add to cart code */
// Add to Bag changes incorporated given by IA start
function submitAddToCart(site_productid, site_ussid, iaref) {
	var site_product_id = site_productid;
	var site_uss_id = site_ussid;

	var addToCartFormData = 'qty=1&pinCodeChecked=false&productCodePost='
			+ site_product_id + '&wishlistNamePost=N&ussid=' + site_uss_id
			+ '&CSRFToken=' + ACC.config.CSRFToken + '';
	$
			.ajax({
				url : ACC.config.encodedContextPath + '/cart/add',
				data : addToCartFormData,
				type : 'post',
				cache : false,
				success : function(data) {
					if (data.indexOf("cnt:") >= 0) {
						$("#status" + site_product_id).html("");
						// $("#status"+site_product_id).html("<font
						// color='#00CBE9'>Bagged and ready!</font>");
						// $("#status"+site_product_id).show().fadeOut(5000);
						// ACC.product.displayAddToCart(data,formId,false);
						ACC.product.showTransientCart(site_uss_id);
						$(
								"span.js-mini-cart-count,span.js-mini-cart-count-hover,span.responsive-bag-count")
								.text(data.substring(4));

						// TISEE-882
						if (window.location.href.toLowerCase().indexOf('cart') >= 0) {
							location.reload();
						}

					} else if (data == "reachedMaxLimit") {
						$("#status" + site_product_id).html("");
						$("#status" + site_product_id)
								.html(
										"<br/><font color='#ff1c47'>You are about to fill your bag completely!Please decrease the item quantity!</font>");
						$("#status" + site_product_id).show().fadeOut(5000);
					}

					else if (data == "crossedMaxLimit") {
						$("#status" + site_product_id).html("");
						$("#status" + site_product_id).html(
								"<font color='#ff1c47'>Bag is full!</font>");
						$("#status" + site_product_id).show().fadeOut(5000);
					} else if (data == "outofinventory") {
						$("#status" + site_product_id)
								.html(
										"<font color='#ff1c47'>Sorry, we don't seem to have the quantity you need. You might want to lower the quantity.</font>");
						$("#status" + site_product_id).show().fadeOut(6000);
						return false;
					} else if (data == "willexceedeinventory") {
						$("#status" + site_product_id)
								.html(
										"<font color='#ff1c47'>Please decrease the quantity</font>");
						$("#status" + site_product_id).show().fadeOut(6000);
						return false;
					} else {
						$("#status" + site_product_id).html("");
						$("#status" + site_product_id)
								.html(
										"<br/><font color='#ff1c47'>Oops!Something went wrong!</font>");
						$("#status" + site_product_id).show().fadeOut(5000);
					}

				},
				complete : function() {
					// $('#ajax-loader').hide();
					/* PT Issues fix */
					forceUpdateHeader();
				},
				error : function(resp) {
					// alert("Add to Bag unsuccessful");
				}

			});
	if (spid.indexOf(site_product_id) === -1) {
		// Add to Bag changes incorporated given by IA
		params = {
			'count' : '0',
			'referring_site_product_id' : site_product_id
		};
		if (iaref) {
			params.referring_request_id = iaref;
		}
		params = buildParams(params);
		callRecApi(params, rootEP + '/SocialGenomix/recommendations/products');
		callEventApi('add_to_cart', {
			"pname" : [ 'site_product_id', 'quantity' ],
			"pvalue" : [ spid, '1' ]
		});
	} else {
		callEventApi('add_to_cart', {
			"pname" : [ 'site_product_id', 'quantity' ],
			"pvalue" : [ spid, '1' ]
		});
	}
}
// Add to Bag changes incorporated given by IA end

/* Make quickview visible and on top */
function showQuickview(productElement) {

	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	qv.style.zIndex = 11;
	qv.style.visibility = "visible";
	// Added as part of TPR-859 (size on hover)
	var size_bottom = $(productElement).find(".short-info").height() + 31;
	$(productElement).find(".sizesAvailable").css("bottom", size_bottom + "px");
	if ($(productElement).find(".sizesAvailable").length > 0) {
		$(productElement).find(".IAQuickView").addClass("size_on_hover");
	}
}
/* Make quickview and Add to cart visible and on top */
function showBoth(productElement) {

	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	var a2c = productElement.getElementsByClassName("iaAddToCartButton")[0];
	qv.style.zIndex = 11;
	qv.style.visibility = "visible";
	a2c.style.zIndex = 11;
	a2c.style.visibility = "visible";
}
/* Make quickview and Add to cart invisible and behind other divs */
function hideBoth(productElement) {
	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	var a2c = productElement.getElementsByClassName("iaAddToCartButton")[0];
	qv.style.zIndex = -1;
	qv.style.visibility = "hidden";
	a2c.style.zIndex = -1;
	a2c.style.visibility = "hidden";
}
/* Make quickview invisible and behind other divs */
function hideQuickView(productElement) {
	var qv = productElement.getElementsByClassName("IAQuickView")[0];
	qv.style.zIndex = -1;
	qv.style.visibility = "hidden";
}
/* Create pop-up Quickview window */
function popupwindow(productId) {
	ACC.colorbox.open("QV",
			{
				href : ACC.config.encodedContextPath + "/p/" + productId
						+ "/quickView",
				onComplete : function() {
					$(".imageList ul li img").css("height", "102px");
					ia_quickviewGallery();
				}
			});
	params = {
		'count' : '0',
		'site_product_id' : productId
	};
	params = buildParams(params);
	callRecApi(params, rootEP + '/SocialGenomix/recommendations/products');
}

function ia_quickviewGallery() {
	$(document)
			.ready(
					function() {
						var mainImageHeight = $(".main-image").find(
								"img.picZoomer-pic").height();
						var thumbnailImageHeight = (mainImageHeight / 5);
						$(".imageList ul li img").css("height",
								thumbnailImageHeight);
						$("#previousImage").css("opacity", "0.5");
						$("#nextImage").css("opacity", "1");
						var listHeight = $(".imageList li").height();
						if ($("#previousImage").length) {
							$(".imageList").css("height",
									(listHeight * imagePageLimit) + "px");
							$(".productImageGallery").css("height",
									(listHeight * imagePageLimit + 100) + "px");
						}
						$(".imageListCarousel").show();

						if ('ontouchstart' in window) {
							$(
									".quick-view-popup #variantForm .select-size span.selected")
									.next("ul").hide();
							$(
									".quick-view-popup #variantForm .select-size span.selected")
									.click(
											function() {
												$(this).next("ul").toggleClass(
														"select_height_toggle");
											});
						}

					});

}

function compareDateWithToday(SaleDate) {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1; // January is 0!
	var yyyy = today.getFullYear();

	if (dd < 10) {
		dd = '0' + dd
	}
	if (mm < 10) {
		mm = '0' + mm
	}

	var today = new Date(mm + '/' + dd + '/' + yyyy + ' 00:00:00');
	// adjust diff for for daylight savings
	// var SaleDate = new Date("10/21/2015 00:00:00");
	var hoursToAdjust = Math.abs(today.getTimezoneOffset() / 60)
			- Math.abs(SaleDate.getTimezoneOffset() / 60);
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
	return Math.round(difference_ms / ONE_DAY)
}
// you'll want this addHours function too

Date.prototype.addHours = function(h) {
	this.setHours(this.getHours() + h);
	return this;
}
/* Creates HTML of individual products */
function makeProductHtml(widgetElement, obj, rid) {
	// Start tpr-3736
	var iaUssid=obj.site_uss_id;
	var mop="";
	var mrp=""
	var spPrice="";
	var isProductPresent=false;
	
	if(iaResponseData != null && iaResponseData != undefined)
	{
	   for(var i in iaResponseData) {
		if(iaUssid==i){
	    if (iaResponseData.hasOwnProperty(i)) {
	    	var priceData = iaResponseData[i];
	    	for(var j = 0; j < priceData.length; j++)
	    	{
	    	   mrp=priceData[0];
	    	   mop=priceData[1];
	    	   spPrice=priceData[2];
	    	}
	    }
	    isProductPresent=true;
	    break;
	    
	}
	 }
}
	//End tpr-3736
	
	/* This is a bad image */
	if(isProductPresent){
	if (typeof obj.image_url === "undefined") {
		return;
	}
	if (obj.start_date != undefined) {
		daysDif = compareDateWithToday(new Date(obj.start_date))
		if ((daysDif < 8) && (daysDif >= 0)) {
			is_new_product = true;
		}
	}

	if (obj.colors != undefined) {
		if (typeof obj.colors === "string") { // arrays always
			obj.colors = [ obj.colors ];
		}

		jQuery
				.each(
						obj.colors,
						function(icount, itemColor) {
							if (itemColor == 'Pewter') {
								obj.colors[icount] = '#8E9294';
							} else if (itemColor == 'Peach') {
								obj.colors[icount] = '#FFDAB9';
							} else if (itemColor == 'Multi') {
								obj.colors[icount] = '/store/_ui/responsive/common/images/multi.jpg';
							} else if (itemColor == 'Metallic') {
								obj.colors[icount] = '#37FDFC';
							}
						});
	}
	/* IA Changes Start for store/mpl/en */

	var IAurl = obj.url + '/p/' + obj.site_product_id + '/?iaclick=true&req='
			+ rid; /*
					 * iaclick=true for tracking our clicks vs. other services,
					 * pass request id to track clicks
					 */
	/* IA Changes End for store/mpl/en */
	if (spid.length > 0) { /*
							 * pass if product page or if this is applicable for
							 * whatever other reason
							 */
		IAurl += '&rspid=' + spid;
	}
	var html = '';

	/* TISPRO-303 Changes-checking with 'type' */
	if ((obj.colors != null && obj.colors.length < 2)
			&& (obj.sizes != null && obj.sizes.length < 2)
			&& (obj.type == 'Electronics')) {
		html += '<li onmouseover="showBoth(this)" onmouseout="hideBoth(this)" class="look slide product-tile '
				+ widgetElement
				+ '_list_elements productParentList" style="display: inline-block; position: relative;">';
		// html += '<div onclick=popupwindow("'+obj.site_product_id+'")
		// class="IAQuickView" style="position: absolute; text-transform:
		// uppercase;cursor: pointer; bottom: 31%;left: 0px; z-index: -1;
		// visibility: hidden; color: #00cbe9;display: inline-block; width: 50%;
		// text-align: center;background: #f8f9fb;background-color: rgba(248,
		// 249, 251,0.77);-webkit-font-smoothing:
		// antialiased;height:70px;font-size:12px;"><span>Quick
		// View</span></div><div
		// onclick=submitAddToCart("'+obj.site_product_id+'","'+obj.site_uss_id+'")
		// class="iaAddToCartButton" style="position: absolute; text-transform:
		// uppercase;cursor: pointer; bottom: 31%; z-index: -1; visibility:
		// hidden; color: #00cbe9;display: inline-block;right:0; text-align:
		// center;background: #f8f9fb;background-color: rgba(248, 249,
		// 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width:
		// 50%;font-size:12px;"><span>Add To Bag</span></div>';

	} else {
		html += '<li onmouseover="showQuickview(this)" onmouseout="hideQuickView(this)" class="look slide product-tile '
				+ widgetElement
				+ '_list_elements productParentList" style="display: inline-block;position: relative;">';
		// html += '<div onclick=popupwindow("'+obj.site_product_id+'")
		// class="IAQuickView" style="position: absolute; text-transform:
		// uppercase;cursor: pointer; bottom: 31%; z-index: -1; visibility:
		// hidden; color: #00cbe9;display: block; width: 100%; text-align:
		// center;background: #f8f9fb;background-color: rgba(248, 249,
		// 251,0.77);-webkit-font-smoothing: antialiased;height:
		// 70px;font-size:12px;"><span>Quick View</span></div>';

	}
	html += '<a href="' + IAurl
			+ '" class="product-tile" style="position: relative;">';
	if (obj.image_url.indexOf("/") > -1) {
		html += '<div class="image" style="position: relative; left: 0;"><img class="product-image" style="font-size: 16px;text-overflow: ellipsis;" src="'
				+ obj.image_url + '" alt="' + obj.name + '"/>';
		if (is_new_product == true) {
			html += '<div style="z-index: 1;" class="new"><span>New</span></div>';
		}
		if (obj.online_exclusive == true) {
			html += '<div class="online-exclusive"><span>online exclusive</span></div>';
		}
		/* TISPRD-2119 Changes for Quick View position */
		if ((obj.colors != null && obj.colors.length < 2)
				&& (obj.sizes != null && obj.sizes.length < 2)
				&& (obj.type == 'Electronics')) {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0;left: 0px; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block; width: 50%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height:70px;font-size:12px;"><span>Quick View</span></div><div onclick=submitAddToCart("'
					+ obj.site_product_id
					+ '","'
					+ obj.site_uss_id
					+ '") class="iaAddToCartButton ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block;right:0; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width: 50%;font-size:12px;"><span>Add To Bag</span></div>';

		} else {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div>';

		}
		/* TISPRD-2119 Changes for Quick View position end */
		html += '</div>';

	} else {
		/* TISPRD-3135 changes for default image -removal of /store */
		html += '<div class="image" style="position: relative; left: 0;"><img class="product-image" style="font-size: 16px;text-overflow: ellipsis;" src="/_ui/desktop/theme-blue/images/missing-product-300x300.jpg" alt="'
				+ obj.name + '"/>';
		if (is_new_product == true) {
			html += '<div style="z-index: 1;" class="new"><span>New</span></div>';
		}
		if (obj.online_exclusive == true) {
			html += '<div class="online-exclusive"><span>online exclusive</span></div>';
		}
		/* TISPRD-2119 Changes for Quick View position */
		if ((obj.colors != null && obj.colors.length < 2)
				&& (obj.sizes != null && obj.sizes.length < 2)
				&& (obj.type == 'Electronics')) {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0;left: 0px; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block; width: 50%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height:70px;font-size:12px;"><span>Quick View</span></div><div onclick=submitAddToCart("'
					+ obj.site_product_id
					+ '","'
					+ obj.site_uss_id
					+ '") class="iaAddToCartButton ia_both" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: inline-block;right:0; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;width: 50%;font-size:12px;"><span>Add To Bag</span></div>';

		} else {
			html += '<div onclick=popupwindow("'
					+ obj.site_product_id
					+ '") class="IAQuickView" style="position: absolute; text-transform: uppercase;cursor: pointer; bottom: 0; z-index: -1; visibility: hidden; color: #00cbe9;display: block; width: 100%; text-align: center;background: #f8f9fb;background-color: rgba(248, 249, 251,0.77);-webkit-font-smoothing: antialiased;height: 70px;font-size:12px;"><span>Quick View</span></div>';

		}
		/* TISPRD-2119 Changes for Quick View position end */
		html += '</div>';

	}
	// html += '<div class="image" style="position: absolute; left: 0;
	// line-height: 347px; height: 347px; width: 221px; background:center
	// no-repeat url('+obj.image_url+'); background-size:contain;"></div>';
	html += '<div class="short-info ia_short-info" style="position: relative; padding:0;">';
	html += '<ul class="color-swatch" style="top: -3px; ">';
	if (obj.colors.length < 3) {
		jQuery
				.each(
						obj.colors,
						function(icount, itemColor) {
							if (icount == 1) {
								html += '<li style="padding: 0;margin-bottom: 0px;"><span style="background-color: '
										+ itemColor
										+ ';" title='
										+ itemColor
										+ '></span></li>';
							}
						});
	} else if (obj.colors.length > 2) {
		jQuery
				.each(
						obj.colors,
						function(icounts, itemColors) {
							if (icounts < 6) {
								html += '<li style="padding: 0;margin-bottom: 0px;"><span style="background-color: '
										+ itemColors
										+ ';" title='
										+ itemColors
										+ '></span></li>';
							}
						});
	}

	var moreColors = obj.colors.length - 6;
	if (obj.colors.length > 6) {
		html += '<li style="font-size:12px;letter-spacing: 1px;border: none;padding: 0;margin-bottom: 0px;">+'
				+ moreColors + 'more</li>';
	}

	html += '</ul>';
	html += '<p class="company" >' + obj.brand + '</p>';
	html += '<span class="product-name" style="text-overflow: ellipsis;word-break: break-word;">'
			+ obj.name + '</span>';
	html += '<div class="price">';
	console.log("**********MRP::" + mrp); 
	console.log("**********MOP::" + mop); 

	// MOP == price,MRP== original price and deiscount price --Below rule is
	// based on MRP > price > discount_price, MRP should be striked out always
	//Start tpr-3736
	if (parseInt(spPrice) > 0) {
		
		console.log("%%%%%%%%%%%%%%%%%%%%MRP::" + mrp); 
		console.log("%%%%%%%%%%%%%%%%%%%%%MOP::" + mop); 
		
		if (spPrice != null || spPrice != ''
				|| spPrice != "undefined") {

			if (mrp != null
					&& parseInt(mrp) > parseInt(spPrice)) {
				html += '<p class="old mrprice">₹'
						+ parseInt(mrp) + '</p>';
			} else if (mop != null
					&& parseInt(mop) > parseInt(spPrice)
					&& parseInt(mop) > 0) {
				html += '<p class="old moprice">₹' + parseInt(mop)
						+ '</p>';
			}
			// TISPRO-317 changes
			html += '<p class="sale discprice" id="spPrice">₹'
					+ parseInt(spPrice) + '</p>';
		}
	} 
	else if (Math.round(mrp) == (mop)) {
		console.log("MRP::" + mrp); 
		console.log("MOP::" + mop); 
		if(isNaN(mop)) {
			console.log("--------Inside isNan----");
			html += '<p class="normal moprice">₹' + mop + '</p>';
		}
		else {
		html += '<p class="normal moprice">₹' + parseInt(mop) + '</p>';
	}
		
	}

	else if (mop != null || mop != '' || mop != "undefined") {
		
		console.log("#################MRP::" + mrp); 
		console.log("####################MOP::" + mop); 
		if (parseInt(mop) > 0) {
			if (mrp != null
					&& parseInt(mrp) > parseInt(mop)) {
				html += '<p class="old mrprice">₹'
						+ parseInt(mrp) + '</p>';
			}
			html += '<p class="sale moprice">₹' + parseInt(mop) + '</p>';
		}
	}
	if (!obj.sizes) {
		
		html += '</div></div></a>';
		html += '<p style="font-size: 12px;margin-top: 33px;color: rgb(255, 28, 71);" id="status'
				+ obj.site_product_id + '"></p>';
		html += '</li>';
		
		/* Calculating execution time */
		var end = new Date(); 
		console.log("END TIME "+ end.getHours() + ":" + end.getMinutes() + ":" + end.getSeconds()); 
		
		
		return html;
		//End tpr-3736
	} else if (obj.sizes != '' || obj.sizes != null || obj.sizes != "undefined") {
		
		if (obj.sizes.length > 0) {
			var sortedSizes = []; /*
									 * This will stay empty if it's a numerical
									 * size list
									 */
			$.each(obj.sizes, function(index, item) {// Converting obj.sizes
														// array values to
														// UPPERCASE
				obj.sizes[index] = item.toUpperCase();
			});
			for (var i = 0; i < allsizes.length; i++) {
				if (obj.sizes.indexOf(allsizes[i]) > -1) {
					sortedSizes.push(allsizes[i]); /*
													 * Include smallest sizes
													 * first
													 */
				}
			}
			if (sortedSizes.length > 0) { /*
											 * Use this if it's a string-based
											 * size array
											 */
				obj.sizes = sortedSizes;
			} else {
				obj.sizes.sort() /*
									 * Not a string-based size array, sort
									 * normally
									 */
			}
			html += '</div><span class="sizesAvailable">Size : <span class="size-col">['
					+ obj.sizes + '] </span></span>';
		}
	}
	html += '</div></a>';
	html += '<p style="font-size: 12px;margin-top: 33px;color: rgb(255, 28, 71);" id="status'
			+ obj.site_product_id + '"></p>';
	html += '</li>';
	
	return html;
	}
	
	}

/* Call Recommendation API, retry if no session id */
function callTataRec() {
	if (ssid) {
		/* See if we have any login credentials */
		checkUser();
		/* Initialize params object we'll be passing around */
		var params = {};
		if (site_page_type === "product" || site_page_type === "productpage") {
			jQuery.ajax({

				type : "GET",

				url : rootEP
						+ '/SocialGenomix/recommendations/products/increment',

				jsonp : 'callback',

				dataType : 'jsonp',

				data : {
					'site_product_id' : spid,
					'ecompany' : ecompany,
					'session_id' : ssid
				},

				contentType : 'application/javascript',

				success : function(response) {
				}

			});
			document.cookie = 'prev_start_time=' + start_time.getTime()
					+ '; path=/';
			/* Check previous pages, add extra parameters if applicable */
			refCheck();
			if (referring_request_id) {
				jQuery.extend(params, {
					'referring_request_id' : referring_request_id
				});
				if (referring_site_product_id) {
					jQuery.extend(params, {
						'referring_site_product_id' : referring_site_product_id
					});
				}
			}
			if (document.getElementById('outOfStockId')) {
				if (document.getElementById('outOfStockId').style.display
						.indexOf("none") === -1) {
					params.out_of_stock = "true";
				} else {
					params.out_of_stock = "false";
				}
			}
			callForEachElement(buildParams(params));
		} else if (site_page_type === "marketplace") {
			/* We will be doing something else here soon */
			callForEachElement(buildParams(params));
		} else if (site_page_type === "search"
				|| site_page_type === "searchEmpty"
				|| site_page_type === "category" || site_page_type === "brand") {
			/*
			 * These pages contain grid view params.count = 100;
			 */
			callForEachElement(buildParams(params));
		} else {
			callForEachElement(buildParams(params));
		}
	} else {
		/* We haven't initialized, get a session id from IA */
		jQuery.ajax({
			type : "GET",
			url : rootEP + '/SocialGenomix/recommendations/init/jsonp',
			jsonp : 'callback',
			dataType : 'jsonp',
			data : {
				'referring_url' : document.referrer
			},
			contentType : 'application/javascript',
			success : function(response) {
				document.cookie = "IASESSIONID=" + response.session_id
						+ '; path=/';
				ssid = response.session_id;
				callTataRec();
			}
		});
	}
}

function iaSortNameAsc(a, b) {
	var aName = a.name.toLowerCase();
	var bName = b.name.toLowerCase();
	return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
}
function iaSortNameDesc(a, b) {
	var aName = a.name.toLowerCase();
	var bName = b.name.toLowerCase();
	return ((aName > bName) ? -1 : ((aName > bName) ? 1 : 0));
}
function iaSortPriceAsc(a, b) {
	return ((a.price < b.price) ? -1 : ((a.price > b.price) ? 1 : 0));
}
function iaSortPriceDesc(a, b) {
	return ((a.price > b.price) ? -1 : ((a.price < b.price) ? 1 : 0));
}

/* Given a response and widget kind, create the HTML we use to update page */
var iaResponseData=null;
function updatePage(response, widgetMode) {
	
//	response={"request_id":"9f5b15d6-eda5-47d3-84de-a2cba9fb77ec","status":200,"status_txt":"OK","data":{"location":null,"recommendations":[{"sizes":"","ia:weightage":-0.00010499,"site_uss_id":"100090CKJShirtGreyCMH393Z5W1BR07L","colors":["Gold"],"original_price":11499,"discounted_price":10499,"subbrand":"","site_product_id":"MP000000000107552","ia:seller_list":[[-0.00010499,{"availability_status":1,"available_quantity":1103,"price":11499,"msrp":11499,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499}]],"type":"Electronics","online_exclusive":false,"url":"","category":"MSH1210100","price":11499,"is_new_product":false,"image_url":"xxx","average_user_rating":2.05341359009462,"ia:seller":{"availability_status":1,"available_quantity":1103,"price":11499,"msrp":11499,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499},"name":"Meizu M3 Note 4G Dual Sim 32 GB (Gold)","brand":"Meizu","gender":"electronics"}],"filter_key":null,"filter_value":null},"server":"analytics-general-15"};
	
	//response={"request_id":"9f5b15d6-eda5-47d3-84de-a2cba9fb77ec","status":200,"status_txt":"OK","data":{"location":null,"recommendations":[{"sizes":"","ia:weightage":-0.00010499,"site_uss_id":"S00001000000000000000001","colors":["Gold"],"original_price":2499.00,"discounted_price":0.00,"subbrand":"","site_product_id":"987654321","ia:seller_list":[[-0.00010499,{"availability_status":1,"available_quantity":1103,"price":2499.00,"msrp":2499.00,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499}]],"type":"Electronics","online_exclusive":false,"url":"","category":"MSH1210100","price":2499.00,"is_new_product":false,"image_url":"xxx","average_user_rating":2.05341359009462,"ia:seller":{"availability_status":1,"available_quantity":1103,"price":2499.00,"msrp":2499.00,"weightage":-0.00010499,"seller_id":"100136","sku":"100136MEIZUM3NOTE01","discount":10499},"name":"Meizu M3 Note 4G Dual Sim 32 GB (Gold)","brand":"Meizu","gender":"electronics"},{"sizes":"","ia:weightage":-0.0001599,"site_uss_id":"123654098765485130011713","colors":["black"],"original_price":20528,"discounted_price":15990,"subbrand":"","site_product_id":"987654322","ia:seller_list":[[-0.0001599,{"availability_status":1,"available_quantity":16,"price":17850,"msrp":20528,"weightage":-0.0001599,"seller_id":"123846","sku":"123846NXG2KSL024","discount":15990}]],"type":"Electronics","online_exclusive":false,"url":"","category":"MSH1223100","price":17850,"is_new_product":false,"image_url":"https://img.tatacliq.com/images/252Wx374H/MP000000000757620_252Wx374H_20161128063952.jpeg","average_user_rating":2.148081518779059,"ia:seller":{"availability_status":1,"available_quantity":16,"price":17850,"msrp":20528,"weightage":-0.0001599,"seller_id":"123846","sku":"100090CKJShirtGreyCMH393Z5W1BR07XL","discount":15990},"name":"Acer Aspire ES1521 15.6\" Laptop (AMD, 1TB HDD) Black","brand":"Acer","gender":"electronics"}],"filter_key":null,"filter_value":null},"server":"analytics-general-15"};
	
	
	var widgetElement = "";
	if (typeof response.data === "undefined") {
		/* Either analytics is down or we passed a bad parameter */
		return;
	}
	if (response.data === null) {
		return;
	}
	/* Product Widgets */
	if (jQuery.inArray(widgetMode, productWidget) > -1) {
		/* If it doesn't exist, we can stop */
		widgetElement = productWidgetElement[jQuery.inArray(widgetMode,
				productWidget)];
		if (!document.getElementById(widgetElement)) {
			return;
		}
		/* It exists, lets name the payload because we may be modifying it */
		var respData = response.data.recommendations;

		/* Use Carousel by default */
		var slider = true;
		/*
		 * But for cases where we have an array of recommendations the size of
		 * 100, we will be using grid view
		 */
		//Srart tpr-3736
		var arrayList = response['data']['recommendations'];
		//console.log("***The response is*** " + JSON.stringify(arrayList));
		var usidList = [];
		var arrIndx = -1;
		for ( var i in arrayList) {
			var ussid = arrayList[i]['site_uss_id'];
			usidList[++arrIndx] = ussid;
		}
		var ussisLists=usidList.join(",");
		var requiredUrl = ACC.config.encodedContextPath + "/p-getIAResponse";
		var dataString = 'iaUssIds=' + ussisLists;
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : requiredUrl,
			async : false,
			data : dataString,
			cache : false,
			dataType : "json",
			success : function(data) {
				
				iaResponseData=data['iaResponse'];
			}
		});
		// End tpr-3736
		if (!respData) {
			// console.log('No response data found for *'+widgetMode+'*
			// wigdet');
			return;
		}

		if (site_page_type === "viewAllTrending") {
			slider = false;
		}
		if (widgetMode === "recent") {
			slider = true;
		}

		/* Make everything blank to begin with */
		document.getElementById(widgetElement).innerHTML = "";
		html = '';
		recIndex = 0;

		/* Staticly-built list of filters */

		var categoryFilters = [];
		/* Category code for Dropdown Filter in hot and search widgets */
		var categoryCodeForFilters = [];
		$.each($('input#for_ia_hot_dropdown_name'), function(i, val) {
			categoryFilters.push(val.value.split("||")[0]);
		});
		$.each($('input#for_ia_hot_dropdown_code'), function(i, val) {
			categoryCodeForFilters.push(val.value);
		});
		/* Removing duplicate categories */
		categoryFilters = distinctVal(categoryFilters);
		// categoryCodeForFilters = jQuery.unique(categoryCodeForFilters);
		categoryCodeForFilters = distinctVal(categoryCodeForFilters);
		/* SortBY dropdown */
		var sortHtml = '<div class="select-view ia_select-view-sort">';
		sortHtml += '<div class="select-list ia_select-list-sort"><span class="selected sortSelected">Sort by: '
				+ sortDropdownselected
				+ '</span><ul id="ia_category_select" style="width: auto;">';
		sortHtml += '<li class="sort_li" id="name-asc">Name: A to Z</li>';
		sortHtml += '<li class="sort_li" id="name-desc">Name: Z to A</li>';
		sortHtml += '<li class="sort_li" id="price-asc">Price: Low to High</li>';
		sortHtml += '<li class="sort_li" id="price-desc">Price: High to Low</li>';
		sortHtml += '</ul></div></div>';

		var catHtml = '<div class="select-view ">';
		// for release 2 changes in home-page headers-All Departments
		catHtml += '<div class="select-list"><span class="selected hotSelected">'+hotDropdownselected+'</span><ul id="ia_category_select" style="width: auto;">';
		for (var i = 0; i < categoryFilters.length; i++) {
			if (i == 0) {
				catHtml += '<li class="category_li" id="">All Departments</li>';
			}
			catHtml += '<li class="category_li" id="'
					+ categoryCodeForFilters[i] + '">' + categoryFilters[i]
					+ '</li>';
		}
		catHtml += '</ul></div></div>';

		if (slider) {
			if (site_page_type === 'search'
					&& widgetElement === 'ia_products_search') {
				html += '<h2><span style="color: black !important;">Best Sellers</span>';
			} else if (site_page_type === 'viewSellers'
					&& widgetElement === 'ia_products') {
				html += '<h2><span style="color: black !important;">You May Also Need</span>';
			} else {
				// for release 2 changes in pdp-page
				if (site_page_type === 'productpage'
						&& widgetElement === 'ia_products_complements') {
					html += '<h2><span style="color: black !important;">Things That Go With This</span>';
				} else {

					html += '<h2><span style="color: black !important;">'
							+ productWidgetTitle[jQuery.inArray(widgetMode,
									productWidget)] + '</span>';
				}
			}

			/* For hot we need a scrolldown bar to select filters */
			if (site_page_type === "homepage"
					|| site_page_type === "viewAllTrending"
					&& widgetMode != "recent") {
				html += catHtml;
			}
			html += '</h2>';
			html += '<div class="spacer" style="padding: 0 25px;"><div class="slider product ready"><div class="frame"><ul id="'
					+ widgetElement
					+ '_list" class="overflow owl-carousel" style="width: 0.953empx; left: 0px;">';
		} else {
			if (site_page_type === "homepage"
					|| site_page_type === "viewAllTrending") {
				html += '<div class="listing wrapper"><div class="left-block"><ul class="listing-leftmenu"><h3><span>Filter By</span></h3><div>';
				html += '<li class="price"><h4 class="active">price</h4>';
				html += '<ul class="checkbox-menu price">';
				html += '<li><input type="checkbox" id="0-500"> <label for="0-500">₹0 - ₹500</label></li><li><input type="checkbox" id="500-1000"> <label for="500-1000">₹500 - ₹1000</label></li><li><input type="checkbox" id="1000-2000"> <label for="1000-2000">₹1000 - ₹2000</label></li><li><input type="checkbox" id="2000-3000"> <label for="2000-3000">₹2000 - ₹3000</label></li><li><input type="checkbox" id="3000-4000"> <label for="3000-4000">₹3000 - ₹4000</label></li><li><input type="checkbox" id="4000-5000"> <label for="4000-5000">₹4000 - ₹5000</label></li>';
				html += '<li><input type="checkbox" id="5000-10000"> <label for="5000-10000">₹5000 - ₹10000</label></li><li><input type="checkbox" id="10000-25000"> <label for="10000-25000">₹10000 - ₹25000</label></li><li><input type="checkbox" id="25000-50000"> <label for="25000-50000">₹25000 - ₹50000</label></li><li><input type="checkbox" id="And>50000"> <label for="And>50000">₹50000 And Above</label></li>';
				html += '</ul></li><li class="on sale"><h4 class="active">Sale</h4><ul class="checkbox-menu on-sale">';
				html += '<li><input type="checkbox" id="sale-filter"><label for="sale-filter">On Sale</label></li></ul></li></div></ul></div>';
				html += '<div class="right-block">';

				html += '<div><h1 class="ia_trending">'
						+ productWidgetTitle[jQuery.inArray(widgetMode,
								productWidget)] + '</h1>';
				html += '</div>';
				/* SortBY dropdown only for search widget */
				if (widgetMode === "search") {
					html += sortHtml;
				}

				html += catHtml;

			}
			html += '<ul id="'
					+ widgetElement
					+ '_list" class="product-list" style="width: 100%; float: left;margin-top: 55px; ">';

		}

		var pageData = []; // array of strings each containing 20 products as
							// HTML
		var page = -1; // index of page, immediately becomes 0 upon first
						// product
		var activePage = 'iapage1'; // page we're currently on if in grid view

		if (slider) {
			/* Make recommendation html for a carousel */
			
			jQuery.each(respData, function(i, v) {
				html += makeProductHtml(widgetElement, this,
						response.request_id);
				recIndex++;
			});
			if (widgetMode === "hot" && site_page_type == "homepage") {
				html += '</ul></div>';
				/* IA Changes Start for store/mpl/en */
				html += '</div></div><a href="http://'
						+ window.location.host
						+ '/viewAllTrending" class="button hotShowHide" style="display: inline-block;font-size: 12px;height: 42px;line-height: 42px;">Shop the Hot List</a>'; /* UF-249*/
			}
			/* IA Changes End for store/mpl/en */
			else {
				html += '</ul></div>';
				html += '</div></div>';
			}

		} else {

			/* Make recommendation html for gridview by pages */
			jQuery.each(respData, function(i, v) {
				if (recIndex % 20 === 0) {
					page++;
					pageData[page] = "";
				}
				pageData[page] += makeProductHtml(widgetElement, this,
						response.request_id);
				recIndex++;
			});
			html += pageData[0]; // start off with first page
			html += '</ul>';

			html += '<ul id="'
					+ widgetElement
					+ 'page_numbers" class="pagination" style="position: absolute; right: 0;line-height: 0px;cursor: pointer;margin-top: 15px;padding:0px;">';
			html += '<li id="iapage1" class="number first active iapage" style="padding: 5px;"><a>1</a></li>';
			for (var i = 1; i < pageData.length; i++) {
				html += '<li id="iapage' + (i + 1)
						+ '" class="number iapage" style="padding: 5px;"><a>'
						+ (i + 1) + '</a></li>';
			}
			html += '<li id="iapage_next" class="next" style="padding: 5px;"><a>Next <span class="lookbook-only"></span></a></li>';
			html += '</ul>';
			html += '</div>';
		}

		/* Insert the HTML */
		if (recIndex > 0) {
			document.getElementById(widgetElement).innerHTML = html;
		}

		/* if trending, reupdate based on user selection on dropdown. */
		if (widgetMode === "hot" || widgetMode === "search") {
			jQuery(".category_li").on('click', function() { //
				var category_name = jQuery(this).text();
				$('.hotSelected').text(category_name);
				// selected category id from hot dropdown
				category_id = jQuery(this).attr('id');
				// console.log("category id slected in hot widget dropdown :
				// "+category_id);
				var params = {
					'count' : '10'
				};
				if (!slider) {
					params.count = '100';
				}

				params.category_id = category_id;
				hotDropdownselected = category_name;

				if (category_id === "All Department") {
					category_id = "";
					hotDropdownselected = 'All Department';
				}

				// params.category = category_id;
				var endpoint = '/SocialGenomix/recommendations/products/hot';
				// $( ".owl-item" ).css( "display", "none" );
				callRecApi(buildParams(params), rootEP + endpoint);
			});
		}

		/* SortBY dropdown */
		if (widgetMode === "search") {
			jQuery(".sort_li")
					.on(
							'click',
							function() { //
								var sort_type = jQuery(this).text();
								$('.sortSelected').text(sort_type);
								// selected category id from hot dropdown
								sort_key = jQuery(this).attr('id');
								// params.category_id = category_id;
								sortDropdownselected = sort_type;

								if (sort_key === "name-asc") {
									respData.sort(iaSortNameAsc);
								} else if (sort_key === "name-desc") {
									respData.sort(iaSortNameDesc);
								} else if (sort_type === "price-asc") {
									respData.sort(iaSortPriceAsc);
								} else {
									respData.sort(iaSortPriceDesc);
								}
								if (slider) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = reorderRecommendation(
											widgetElement, respData,
											response.request_id);
								} else {
									pageData = reorderRecommendationGrid(
											widgetElement, respData,
											response.request_id);
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[0];
									/* Reset page numbers */
									var pages = document
											.getElementById("ia_products_hotpage_numbers").childNodes;
									for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																								// next
										pages[i].visibility = 'visible';
									}
									for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																								// next
										pages[i].visibility = 'hidden';
									}
									activePage = 'iapage1';
								}
							});
		}

		/* Setup UI */
		if (slider) {
			if (site_page_type == 'search'
					&& widgetElement == 'ia_products_search') {
				/* Animate Carousel */
				$("#" + widgetElement + "_list").owlCarousel({

					items : 4,
					loop : true,
					nav : true,
					dots : false,
					navText : [],
					responsive : {
						// breakpoint from 0 up
						0 : {
							items : 1,
							stagePadding : 50,
						},
						// breakpoint from 480 up
						480 : {
							items : 2,
							stagePadding : 50,
						},
						// breakpoint from 768 up
						768 : {
							items : 3,
						},
						// breakpoint from 768 up
						1280 : {
							items : 5,
						}
					}

				/*
				 * items : 4,
				 * 
				 * scrollPerPage: true,
				 * 
				 * itemsDesktop : [1199,3],
				 * 
				 * itemsDesktopSmall : [980,2],
				 * 
				 * itemsTablet: [768,2],
				 * 
				 * itemsMobile : [479,1],
				 * 
				 * navigation: true,
				 * 
				 * navigationText : [],
				 * 
				 * pagination:false,
				 * 
				 * rewindNav : false
				 */

				});

			}
			/* Animate Carousel */
			$("#" + widgetElement + "_list").owlCarousel({

				items : 5,
				loop : true,
				nav : true,
				dots : false,
				navText : [],
				responsive : {
					// breakpoint from 0 up
					0 : {
						items : 1,
						stagePadding : 50,
					},
					// breakpoint from 480 up
					480 : {
						items : 2,
						stagePadding : 50,
					},
					// breakpoint from 768 up
					768 : {
						items : 3,
					},
					// breakpoint from 768 up
					1280 : {
						items : 5,
					}
				}

			/*
			 * items : 5,
			 * 
			 * scrollPerPage: true,
			 * 
			 * itemsDesktop : [1199,4],
			 * 
			 * itemsDesktopSmall : [980,3],
			 * 
			 * itemsTablet: [768,2],
			 * 
			 * itemsMobile : [479,1],
			 * 
			 * navigation: true,
			 * 
			 * navigationText : [],
			 * 
			 * pagination:false,
			 * 
			 * rewindNav : false
			 */
			});

		} else {

			/* Paginate GridView */
			/* Go to page clicked */
			jQuery(".iapage")
					.on(
							'click',
							function() {
								if (pageData[parseInt(jQuery(this).text()) - 1] != undefined) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[parseInt(jQuery(
											this).text()) - 1];
								} else {
									document.getElementById(widgetElement
											+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
								}
								jQuery('#' + activePage).removeClass('active');
								jQuery(this).addClass('active');
								activePage = this.id;
							});

			/* Go to next page if it exists */
			jQuery("#iapage_next")
					.on(
							'click',
							function() {
								var pageIndex = parseInt(activePage.replace(
										/^\D+/g, '')) + 1;
								if (pageIndex > 5) {
									pageIndex = pageIndex % 5;
								}
								if (document.getElementById('iapage'
										+ pageIndex) !== null) {
									var el = document.getElementById('iapage'
											+ pageIndex);
									if (pageData[parseInt(jQuery(el).text()) - 1] != undefined) {
										document.getElementById(widgetElement
												+ '_list').innerHTML = pageData[parseInt(jQuery(
												el).text()) - 1];
									} else {
										document.getElementById(widgetElement
												+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
									}
									jQuery('#' + activePage).removeClass(
											'active');
									jQuery(document.getElementById(el.id))
											.addClass('active');
									activePage = el.id;
								}
							});

			/* Filter by price range, and whether it's on sale */
			var priceRanges = [];
			var saleOptions = [];

			/* Select Price Filter */
			jQuery(".price li label")
					.on(
							'click',
							function() {
								/*
								 * Follow the checkmark which isn't visible as
								 * an html element
								 */
								if (priceRanges.indexOf(this.textContent) > -1) {
									priceRanges.splice(priceRanges
											.indexOf(this.textContent), 1);
								} else {
									priceRanges.push(this.textContent);
								}
								pageData = getFilteredRecommendations(
										widgetElement, respData, priceRanges,
										saleOptions, response.request_id);
								if (pageData[0] != undefined
										|| pageData[0] != null) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[0];
								} else {
									document.getElementById(widgetElement
											+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
								}
								/* Reset page numbers */
								var pages = document
										.getElementById("ia_products_hotpage_numbers").childNodes;
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'visible';
								}
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'hidden';
								}
								activePage = 'iapage1';
							});

			/* Select sale filter */
			jQuery(".on-sale li label")
					.on(
							'click',
							function() {
								/*
								 * Follow the checkmark which isn't visible as
								 * an html element
								 */
								if (saleOptions.indexOf(this.textContent) > -1) {
									saleOptions.splice(saleOptions
											.indexOf(this.textContent), 1);
								} else {
									saleOptions.push(this.textContent);
								}
								pageData = getFilteredRecommendations(
										widgetElement, respData, priceRanges,
										saleOptions, response.request_id);
								if (pageData[0] != undefined) {
									document.getElementById(widgetElement
											+ '_list').innerHTML = pageData[0];
								} else {
									document.getElementById(widgetElement
											+ '_list').innerHTML = "<h1 style='text-align:center;color: #22bfe6;font-size: 24px;'>No Results Found.</h1>";
								}
								/* Reset page numbers */
								var pages = document
										.getElementById("ia_products_hotpage_numbers").childNodes;
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'visible';
								}
								for (var i = pageData.length; i < pages.length - 1; i++) { // ignore
																							// next
									pages[i].visibility = 'hidden';
								}
								activePage = 'iapage1';
							});
		}
	} else if (jQuery.inArray(widgetMode, brandWidget) > -1) {

		/* Brand Widgets */

		widgetElement = brandWidgetElement[jQuery.inArray(widgetMode,
				brandWidget)];
		if (!document.getElementById(widgetElement)) {
			return;
		}
		document.getElementById(widgetElement).innerHTML = "";
		var html = "";
		var respData = response.data.brands;
		/* response check addition for 'Hot Brands' start */
		if (response.data.brands.length > 0) {
			html += '<div class="wrapper"><h1 class="">'
					+ brandWidgetTitle[jQuery.inArray(widgetMode, brandWidget)]
					+ '</h1><ul class="feature-brands ia_feature_brands">';
		} else {
			$("#ia_brands_hot_searches").css("background-color", "#FFFFFF");
		}
		/* response check addition for 'Hot Brands' end */
		numRec = 0;

		jQuery
				.each(
						respData,
						function() {
							queryUrl = this.url + '?req=' + response.request_id;
							if (numRec < 3) {
								html += '<li><a href="' + queryUrl
										+ '" style=""><img class="image" src="'
										+ this.image_url
										+ '" width="432" height="439">';
								// logo and tagline, to be made obsolete with
								// larger image
								html += '<img class="logo" src="'
										+ this.logo_image_url
										+ '" width="140" height="43">';
								html += '<span>' + this.description + '</span>';
								html += '</a></li>';
							} else if (numRec === 3) {
								html += '</ul><ul class="more-brands">';
							}
							if (numRec >= 3) {
								html += '<li><a href="'
										+ queryUrl
										+ '"><img class="logo  hover-logo" src="'
										+ this.logo_image_url
										+ '" width="160" height="43">';
								html += '<img class="background" src="'
										+ this.overlay_image_url
										+ '" width="206" height="206" style="left: 0";></a></li>';
							}
							numRec++;
							if (numRec === 9) {
								return false;
							}
						});

		html += '</ul></div></div>';
		document.getElementById(widgetElement).innerHTML = html;
	} else if (jQuery.inArray(widgetMode, categoryWidget) > -1) {

		/* Category Widgets */

		widgetElement = categoryWidgetElement[jQuery.inArray(widgetMode,
				categoryWidget)];
		if (!document.getElementById(widgetElement)) {
			return;
		}
		document.getElementById(widgetElement).innerHTML = "";
		var html = "";
		var respData = "";
		if (widgetMode === "categories/recent") {
			respData = response.data.recommendations;
		} else {
			respData = response.data.categories;
		}
		if (respData === null) {
			return;
		}
		if (widgetMode !== "categories/recent") {
			html += '<div class="wrapper">';
		}
		if (widgetMode === "categories/recent") {
			html += '<h1 class="">'
					+ categoryWidgetTitle[jQuery.inArray(widgetMode,
							categoryWidget)] + '</h1>';
		} else {
			html += '<h1 class="">'
					+ categoryWidgetTitle[jQuery.inArray(widgetMode,
							categoryWidget)] + '</h1>';
			html += '<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference owl-carousel owl-theme" id="mplCategoryCarousel" style="opacity: 1; display: block;">';
		}
		var numRec = 0;
		jQuery
				.each(
						respData,
						function() {
							queryUrl = this.url + '?req=' + response.request_id;
							if (widgetMode === "categories/recent") { // FlyOut
																		// Menu
								html += '<div style="display:inline-block; padding: 0; width: 100px; margin: 0px 10px;"><a href="'
										+ queryUrl
										+ '"><img class="image" src="'
										+ this.image_url + '" width="113"/>';
								html += '<p style="text-align: center;margin-top: 5px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">'
										+ this.name + '</p></a></div>';
							} else { // Normal Widget
								html += '<div class="item slide">';
								html += '<a href="'
										+ queryUrl
										+ '"><img src="'
										+ this.image_url
										+ '" class="image" style="height: 324px;"></a><span>'
										+ this.name + '</span>';
								html += '<a class="shop_link" href="'
										+ queryUrl
										+ '"><b>SHOP NOW</b></a></div>';
							}
							numRec++;
							/*
							 * if(numRec === respData.length) { return false; }
							 */
						});
		if (widgetMode !== "categories/recent") {
			html += '</div>';
		}

		html += '</div>';

		if (widgetMode !== "categories/recent") {
			html += '</div>';
		}
		if (numRec > 0) {
			document.getElementById(widgetElement).innerHTML = html;
		}
		var carousel = $("#mplCategoryCarousel");

		carousel.owlCarousel({
			items : 4,
			loop : true,
			nav : true,
			dots : false,
			navText : [],
			responsive : {
				// breakpoint from 0 up
				0 : {
					items : 1,
					stagePadding : 50,
				},
				// breakpoint from 480 up
				480 : {
					items : 2,
					stagePadding : 50,
				},
				// breakpoint from 768 up
				768 : {
					items : 3,
				},
				// breakpoint from 768 up
				1280 : {
					items : 5,
				}
			}
		/*
		 * items : 4,
		 * 
		 * navigation:true,
		 * 
		 * navigationText : [],
		 * 
		 * pagination:false,
		 * 
		 * itemsDesktop : [1199,3],
		 * 
		 * itemsDesktopSmall : [980,2],
		 * 
		 * itemsTablet: [768,2],
		 * 
		 * itemsMobile : [479,1],
		 * 
		 * rewindNav: false,
		 * 
		 * lazyLoad:true,
		 * 
		 * navigation : true,
		 * 
		 * rewindNav : false,
		 * 
		 * scrollPerPage : true
		 */
		});

	} else if (jQuery.inArray(widgetMode, collectionWidget) > -1) {

		/* Collection Widgets */

		widgetElement = collectionWidgetElement[jQuery.inArray(widgetMode,
				collectionWidget)];
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
		html += '<div class="wrapper background"><h1 class="">'
				+ collectionWidgetTitle[jQuery.inArray(widgetMode,
						collectionWidget)] + '</h1><ul class="collections">';
		var numRec = 0;
		jQuery
				.each(
						respData,
						function() {
							queryUrl = this.url + '?req=' + response.request_id;
							if (numRec < 1) { // first one show both images
								html += '<li class="chef sub "><img class="background brush-strokes-sprite sprite-water_color_copy_4" src="//'
										+ mplStaticResourceHost
										+ '/store/_ui/responsive/common/images/transparent.png">';
								html += '<img class="background brush-strokes-sprite sprite-water_color_copy_3" src="//'
										+ mplStaticResourceHost
										+ '/store/_ui/responsive/common/images/transparent.png">';
								html += '<h3>' + this.name + '</h3>';
								html += '<img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.top_left_image_url
										+ '" style="margin-right: 20px;width: calc(40% - 140px);">';
								html += '<img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.top_right_image_url
										+ '" style="width: calc(60% - 140px);width: -webkit-calc(60% - 140px);">';
								html += '<span>' + this.description + '';
								html += '<a href="' + queryUrl
										+ '">Shop The Collection</a></span>';
								html += '</li>';
							} else if (numRec < 2) { // show only one image,
														// different format
								html += '<li class=" sub "><img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.bottom_left_image_url
										+ '"><div class="copy"><h3>'
										+ this.name + '</h3>';
								html += '<a href="'
										+ queryUrl
										+ '">The Hottest New Styles</a></div></li>';
							} else if (numRec < 3) {
								html += '<li class="spotlight sub "><img class="image" src="//'
										+ DamMediaHost
										+ ''
										+ this.bottom_right_image_url
										+ '"><div class="copy"><h3>'
										+ this.name + '</h3>';
								html += '<a href="'
										+ queryUrl
										+ '">The Hottest New Styles</a></div></li>';
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

		/* We should never get to this part of the code */

		// console.log("widget mode we don't account for: " + widgetMode);
	}

}
$(document).on('click', ".IAQuickView,.iaAddToCartButton", function(e) {
	e.preventDefault();
})
$(document).ready(function(){
		$(document).on("click", "#callMe", function(e) {
			e.preventDefault();
			var url = $(this).attr('href');
			$.get(url, function(data) {
				$(data).modal();
			}).success(function(){
				//console.log("Modal loaded");
				//ACC.click2call.bindClick2CallForm();
				//ACC.click2call.clickToCallModalEvents();
			});
		});
	
	//spinner: $("<img src='" + ACC.config.commonResourcePath + "/images/spinner.gif' />"),
	

		/*
		 * AJAX call for generating OTP
		 * 
		 */
		$(document).on("click","#generateOTPBtn",function(){
			$(".error").each(function(){
				$(this).empty();
			});
			$.ajax({
				url: ACC.config.encodedContextPath+"/clickto/generateOTP",
				type:"GET",
				dataType:"JSON",
				data:$("#generateOTP").serialize(),
				success:function(data){
					if(data.otp_generated == "true"){
						$("#validateOTP").show();
						$("#generateOTP").hide();
						//TISPRDT-111 fix
						$("#c2cEmailId").val(data.emailId);
						$("#contactNo").val(data.contactNo);
						$("#customerName").val(data.customerName);
						$("#reason").val(data.reason);
						
					} if(data.error_name != null){
						$("label[for=errorCustomerName]").text(data.error_name);
					} if(data.error_email != null){
						$("label[for=errorCustomerEmail]").text(data.error_email);
					} if(data.error_contact != null){
						$("label[for=errorCustomerMobileNo]").text(data.error_contact);
					}
				},
				fail:function(fail){
					alert("Sorry we are unable to connect to Click 2 Call service. Please try again later.");
				}
			});
		});
		/*
		 * Validate the OTP
		 */
		$(document).on("click","#validateOTPBtn",function(){
			$(".error").each(function(){
				$(this).empty();
			});
			$.ajax({
				url:ACC.config.encodedContextPath+"/clickto/validateOTP",
				type:"GET",
				dataType:"JSON",
				data:$("#validateOTP").serialize(),
				success:function(data){
					if (data.valid_otp == "true" && data.click_to_call_response!= null) {
						/*TPR-691*/
						var selectedOption = $('select[name="reason"] option:selected').val();
						if(selectedOption=="Order enquiry/ Place or cancel order")
						  {
							utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_otp_success', event_type : 'support_call_click'}
								);
						  }
						else if(selectedOption=="Return  Product")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Return_Product_otp_success', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Refund Enquiry")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Refund_Enquiry_otp_success', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Product Information")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Product_Information_otp_success', event_type : 'support_call_click'}
									);
						  }
						else
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Other_Assitance_otp_success', event_type : 'support_call_click'}
									);
						  }
						/*TPR-691*/
						$("#validateOTP").hide();
						$("#generateOTP").hide();
						
						var responseXML = $.parseXML(data.click_to_call_response);
						var statusMessage = $(responseXML).find("statusMessage");
						var status = $(statusMessage).find("status");
						var ewt = $(statusMessage).find("ewt");
						if($(status).text()!= "undefined" && $(status).text()!= ""){
							$(".validOTP").show();
							$(".validOTP p").append($(ewt).text()+" secs");
						}
					}else if(data.invalid_otp != null){
						$("label[for=errorOTP]").text(data.invalid_otp);
						/*TPR-691*/
						//alert("hi"+selectedOption);
						
						var selectedOption = $('select[name="reason"] option:selected').val();
						if(selectedOption=="Order enquiry/ Place or cancel order")
						  {
							utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_invalid_otp', event_type : 'support_call_click'}
								);
						  }
						else if(selectedOption=="Return  Product")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Return_Product_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Refund Enquiry")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Refund_Enquiry_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Product Information")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Product_Information_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						else
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Other_Assitance_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						/*TPR-691*/
					}else if(data.error_otp!= null){
						$("label[for=errorOTP]").text(data.error_otp);
						/*TPR-691*/
						var selectedOption = $('select[name="reason"] option:selected').val();
						if(selectedOption=="Order enquiry/ Place or cancel order")
						  {
							utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_empty_otp', event_type : 'support_call_click'}
								);
						  }
						else if(selectedOption=="Return  Product")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Return_Product_empty_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Refund Enquiry")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Refund_Enquiry_empty_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Product Information")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Product_Information_empty_otp', event_type : 'support_call_click'}
									);
						  }
						else
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Other_Assitance_empty_otp', event_type : 'support_call_click'}
									);
						  }
					}/*TPR-691*/
					
					if(data.hasOwnProperty("click_to_call_response") && data.click_to_call_response.length == 0){
						$(".mandetoryFieldMissing p").empty();
						$(".mandetoryFieldMissing").show();
						$(".mandetoryFieldMissing p").text("Sorry we are unable to connect to the service. Please try again later");
					}
					$("#validateOTPBtn").removeAttr("disabled");
					$("#validateOTPBtn").parent().find("#spinner").remove();
				},
				beforeSend:function(){
					$("#validateOTPBtn").attr("disabled","disabled");
					$("#validateOTPBtn").parent().append("<img style='position: absolute; left: 29%;' id='spinner' src='" + ACC.config.commonResourcePath + "/images/spinner.gif' />");
				},
				fail:function(fail){
					alert("Sorry we are unable to connect to Click 2 Call service. Please try again later.");
				}
			});
		});
	
		$(document).on('hide.bs.modal', function () {
            $("#clicktoCallModal").remove();
            $(".modal-backdrop.in").remove();
		});
});
$(window).on("load", function(e) {
		var chatEstFlag = setInterval(function(){
			if($('#dCustEmail').text() != "") {
				$(".gwc-chat-title").css({
					"background":"none",
					"padding-left":"15px",
					"position": "relative",
					"top": "-7px"
				});
				clearInterval(chatEstFlag);
			}
		},50)
	});
function sendMessage(){
	//alert('triggering');
	chatSession.sendMessage($('#txtMessage').val());
	$('#txtMessage').val('');
	//var e = $.Event("keypress");
		//e.keyCode = 13; // # Some key code value
		//$('#txtMessage').trigger(e);
}
var liveChatText = '<div class="chat">'
	+'<h5>'
	+'<span class="cicon icon-comment"></span>'
	+'<a>Live Chat</a>'
	+'</h5></div>';
var chatFlag;

function handleChatInit(){
	$('#h').hide();
	$(".gwc-chat-registration-submit button").text("CONNECT");
	$("#gcbChatSkipRegistration").text("CANCEL");
	
	$('.no-click').remove();
	$("body").append("<div class='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	//$("#chatReason").prepend('<option class="default" value="" selected></option>');
	setTimeout(function(){
	$("#chatReason").prepend("<option class='default' value='' selected></option>").val('');
	if($("#chatReason").val() != '') {
		$('.gwc-chat-label[for="gcbChatReason"]').addClass('focused');
	} else {
		$('.gwc-chat-label[for="gcbChatReason"]').removeClass('focused');
	}
	  },50);
	
}
$(window).on('beforeunload',function(){
	if(sessionStorage.getItem('chatActive') == 'false'){
		sessionStorage.removeItem('chatActive');
		sessionStorage.removeItem('regUser');
		sessionStorage.removeItem('regEmail');
		if(chatObj){
			chatObj.close();
		}
	}
	//return false;
});


$(document).ready(function(){
	//$('.selectpicker').selectpicker();
	
	$(document).on("click", "#gcbChatRegister", function(e) {
		var chatEstFlag = setInterval(function(){
			if($('#dCustEmail').text() != "") {
				$(".gwc-chat-title").css({
					"background":"none",
					"padding-left":"15px",
					"position": "relative",
					"top": "-7px"
				});
				clearInterval(chatEstFlag);
			}
		},50)
	});
	$(document).on("click", "#chatMe", function(e) {
		e.preventDefault();
		//e.stopPropagation();
		chatObj.startChat();
//			$('.gcb-startChat').click();
			if(chatObj && chatObj.isMinimized()){
				chatObj.toggle();
			}else{
				
			}
	});
	
	$(document).on("click", ".gwc-chat-control-minimize", function(e) {
		//alert(($('.gwc-chat-body').css('display') == 'none'));
		
		setTimeout(function(){
			if(!$('.gwc-chat-body').is(":visible"))
			{
				$('.no-click').remove();
				$('.gwc-chat-embedded-window').addClass('minimized');
				$('.gwc-chat-control-minimize').append(liveChatText);
//				$('.gwc-chat-control-minimize').append($('.gwc-chat-control.gwc-chat-control-close').clone());
			
			}
			else{
				unreadMsgCount = 0;
				$('.no-click').remove();
				$("body").append("<div class='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				if($("#gcbChatCustName").val() == '' && $("#gcbChatEmail").val() == '' && $("#gcbChatPhoneNum").val() == ''){
					//$("#chatReason").prepend('<option class="default" value="" selected></option>');
					$("#chatReason").prepend("<option class='default' value='' selected></option>").val('');
				}
				if($("#chatReason").val() != '') {
					$('.gwc-chat-label[for="gcbChatReason"]').addClass('focused');
				} else {
					$('.gwc-chat-label[for="gcbChatReason"]').removeClass('focused');
				}
				$('.gwc-chat-embedded-window').removeClass('minimized');
				$('.gwc-chat-control-minimize').find('div.chat').remove();
				$('.gwc-chat-control-minimize > .gwc-chat-control.gwc-chat-control-close').remove();
			}
		},100);
	});
	$(document).on("click", ".gwc-chat-control-close", function(e) {
		e.stopPropagation();
		$('.no-click').remove();
		chatObj.close();
	});
	$(document).on("click", "#gcbChatSkipRegistration", function() {
		chatObj.close();
		unreadMsgCount = 0;
		$('.no-click').remove();
		return false; // prevent default behavior
	});
	
	$(document).on('focus','.gwc-chat-registration-input',function(){
		$(this).parent().siblings('.gwc-chat-label').addClass('focused');
	})
	$(document).on('focus','#chatReason',function(){
		//$(this).parent().siblings('.gwc-chat-label').addClass('focused');
		$("#chatReason option.default").remove();
		if(!$("#chatReason").val() == '') {
			$('.gwc-chat-label[for="gcbChatReason"]').addClass('focused');
		}
	})

	$(document).on('blur','.gwc-chat-registration-input, #chatReason',function(){
		if($(this).val() == '') {
			$(this).parent().siblings('.gwc-chat-label').removeClass('focused')
		}
	})
});

$(document).on("click",".lazy-need-help",function(){
//UF-412
	(function(co, b, r, o, w, s, e) {
	    e = co.getElementsByTagName(b)[0];
	    if (co.getElementById(r)){
	          return;
	    }
	    s = co.createElement(b); s.id = r; s.src = o;
	    s.setAttribute('data-gcb-url', w);
	    e.parentNode.insertBefore(s, e);
	})(document, 'script', 'gcb-js',
	//'http://10.9.17.46:8700/cobrowse/js/gcb.min.js',
	//'http://10.9.17.46:8700/cobrowse');
	//'https://219.65.91.73:443/cobrowse/js/gcb.min.js',
	//'https://219.65.91.73:443/cobrowse');
	   'https://prod-tulweb.tata-bss.com:443/cobrowse/js/gcb.min.js',
	   'https://prod-tulweb.tata-bss.com:443/cobrowse');
});

	   
var chatSession = null;
var chatObj = null;
var regUser = null;
var regEmail = null;
var unreadMsgCount = 0;
var _genesys = {
	debug: true,
		buttons:{
			chat:true,
			cobrowse:false
		},
		integration:
		{
			buttons: {
				position: "left", // Set to "right" to stick buttons to right side of the page.
				cobrowse: false, // Set to false to disable button completely (you can start Co-browse manually via API call).
				chat: false // Set to false if you don't want to use built-in chat button.
			},
			onReady: function() {
				//alert('button ready');
			}//,
		},
		chat:{
			localization: {
				'regExit': 'CANCEL',
				'regSubmit': 'CONNECT'
			},
			templates:'https://prod-tulweb.tata-bss.com:443/static/chatTemplates2.html',
			onReady: function(cobrowseAPI, isTopContext){
				
				chatObj = cobrowseAPI;
				//console.log('onReady : ' + chatObj.isMinimized());
				cobrowseAPI.onSession(function(session) {
					//alert('on session');
					
					chatSession = session;
					chatObj = cobrowseAPI;
					//$('.gwc-chat-body').css('display','block');
//					if(chatObj.isMinimized()){
//						//alert('chatobj minimized.toggling');
//						chatObj.toggle();
//					}
					chatSession.onMessageReceived(function(event) {
						if (event.content.type.url) {
							// this is "url" message
						} else if (event.content.type.text) {
							//alert('message recvd : ' + event.content.text);
						}
						if(chatObj.isMinimized()){
							//alert('yeah,message recvd : ' + event.content.text);
							unreadMsgCount++;
							if($('.gwc-chat-embedded-window.minimized div.chat h5 span').length == 1) {
								$('.gwc-chat-embedded-window.minimized div.chat h5').append('<span class="unread">'+unreadMsgCount+'</span>');
							} else {
								$('span.unread').text(unreadMsgCount);
							}
						}
					});
					$('.gwc-chat-body').css('height','418px');

					$('#dCustName').text(sessionStorage.getItem('regUser'));
					$('#dCustEmail').text(sessionStorage.getItem('regEmail'));
					sessionStorage.setItem('chatActive','true');
				});
				
			},
			ui: {
				onBeforeChat: function () {
					setTimeout(function() {
						//handleChatInit();

						if(chatObj.isMinimized()){
							//alert('chatobj minimized.toggling');
							$('.gwc-chat-embedded-window').addClass('minimized');
							$('.gwc-chat-control-minimize').append(liveChatText);
						}
					},0);
				},
				onBeforeRegistration: function(regForm) {
					setTimeout(function() {
						handleChatInit();
						sessionStorage.setItem('chatActive','false');
						console.log('before registraion ui');
						$('.selectpicker').selectpicker();
						jQuery(regForm).find("#gcbChatCustName" ).off();
						jQuery(regForm).find("#gcbChatCustName" ).blur(function() {
							regUser = $(this).val();
							sessionStorage.setItem('regUser',regUser);
							
							//alert(regUser);
						});
						
						jQuery(regForm).find("#gcbChatEmail" ).off();
						jQuery(regForm).find("#gcbChatEmail").blur(function() {
							regEmail = $(this).val();
							sessionStorage.setItem('regEmail',regEmail);
							//alert(regEmail);
						});
						
						var $skipBtn = jQuery(regForm).find('#gcbChatSkipRegistration');
						//alert('skip cancel regstrd');
						$skipBtn.on('click', function() {
							chatObj.close();
							unreadMsgCount = 0;
							$('.no-click').remove();
							return false; // prevent default behavior
						});
					},0);
					
				}
			}
		}
};
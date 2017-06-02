if (function(a, b) {
    "object" == typeof module && "object" == typeof module.exports ? module.exports = a.document ? b(a, !0) : function(a) {
        if (!a.document) throw new Error("jQuery requires a window with a document");
        return b(a);
    } : b(a);
}("undefined" != typeof window ? window : this, function(a, b) {
    function s(a) {
        var b = a.length, c = n.type(a);
        return "function" !== c && !n.isWindow(a) && (!(1 !== a.nodeType || !b) || ("array" === c || 0 === b || "number" == typeof b && b > 0 && b - 1 in a));
    }
    function x(a, b, c) {
        if (n.isFunction(b)) return n.grep(a, function(a, d) {
            return !!b.call(a, d, a) !== c;
        });
        if (b.nodeType) return n.grep(a, function(a) {
            return a === b !== c;
        });
        if ("string" == typeof b) {
            if (w.test(b)) return n.filter(b, a, c);
            b = n.filter(b, a);
        }
        return n.grep(a, function(a) {
            return g.call(b, a) >= 0 !== c;
        });
    }
    function D(a, b) {
        for (;(a = a[b]) && 1 !== a.nodeType; ) ;
        return a;
    }
    function G(a) {
        var b = F[a] = {};
        return n.each(a.match(E) || [], function(a, c) {
            b[c] = !0;
        }), b;
    }
    function I() {
        l.removeEventListener("DOMContentLoaded", I, !1), a.removeEventListener("load", I, !1), 
        n.ready();
    }
    function K() {
        Object.defineProperty(this.cache = {}, 0, {
            get: function() {
                return {};
            }
        }), this.expando = n.expando + Math.random();
    }
    function P(a, b, c) {
        var d;
        if (void 0 === c && 1 === a.nodeType) if (d = "data-" + b.replace(O, "-$1").toLowerCase(), 
        "string" == typeof (c = a.getAttribute(d))) {
            try {
                c = "true" === c || "false" !== c && ("null" === c ? null : +c + "" === c ? +c : N.test(c) ? n.parseJSON(c) : c);
            } catch (e) {}
            M.set(a, b, c);
        } else c = void 0;
        return c;
    }
    function Z() {
        return !0;
    }
    function $() {
        return !1;
    }
    function _() {
        try {
            return l.activeElement;
        } catch (a) {}
    }
    function jb(a, b) {
        return n.nodeName(a, "table") && n.nodeName(11 !== b.nodeType ? b : b.firstChild, "tr") ? a.getElementsByTagName("tbody")[0] || a.appendChild(a.ownerDocument.createElement("tbody")) : a;
    }
    function kb(a) {
        return a.type = (null !== a.getAttribute("type")) + "/" + a.type, a;
    }
    function lb(a) {
        var b = gb.exec(a.type);
        return b ? a.type = b[1] : a.removeAttribute("type"), a;
    }
    function mb(a, b) {
        for (var c = 0, d = a.length; d > c; c++) L.set(a[c], "globalEval", !b || L.get(b[c], "globalEval"));
    }
    function nb(a, b) {
        var c, d, e, f, g, h, i, j;
        if (1 === b.nodeType) {
            if (L.hasData(a) && (f = L.access(a), g = L.set(b, f), j = f.events)) {
                delete g.handle, g.events = {};
                for (e in j) for (c = 0, d = j[e].length; d > c; c++) n.event.add(b, e, j[e][c]);
            }
            M.hasData(a) && (h = M.access(a), i = n.extend({}, h), M.set(b, i));
        }
    }
    function ob(a, b) {
        var c = a.getElementsByTagName ? a.getElementsByTagName(b || "*") : a.querySelectorAll ? a.querySelectorAll(b || "*") : [];
        return void 0 === b || b && n.nodeName(a, b) ? n.merge([ a ], c) : c;
    }
    function pb(a, b) {
        var c = b.nodeName.toLowerCase();
        "input" === c && T.test(a.type) ? b.checked = a.checked : ("input" === c || "textarea" === c) && (b.defaultValue = a.defaultValue);
    }
    function sb(b, c) {
        var d, e = n(c.createElement(b)).appendTo(c.body), f = a.getDefaultComputedStyle && (d = a.getDefaultComputedStyle(e[0])) ? d.display : n.css(e[0], "display");
        return e.detach(), f;
    }
    function tb(a) {
        var b = l, c = rb[a];
        return c || (c = sb(a, b), "none" !== c && c || (qb = (qb || n("<iframe frameborder='0' width='0' height='0'/>")).appendTo(b.documentElement), 
        b = qb[0].contentDocument, b.write(), b.close(), c = sb(a, b), qb.detach()), rb[a] = c), 
        c;
    }
    function xb(a, b, c) {
        var d, e, f, g, h = a.style;
        return c = c || wb(a), c && (g = c.getPropertyValue(b) || c[b]), c && ("" !== g || n.contains(a.ownerDocument, a) || (g = n.style(a, b)), 
        vb.test(g) && ub.test(b) && (d = h.width, e = h.minWidth, f = h.maxWidth, h.minWidth = h.maxWidth = h.width = g, 
        g = c.width, h.width = d, h.minWidth = e, h.maxWidth = f)), void 0 !== g ? g + "" : g;
    }
    function yb(a, b) {
        return {
            get: function() {
                return a() ? void delete this.get : (this.get = b).apply(this, arguments);
            }
        };
    }
    function Fb(a, b) {
        if (b in a) return b;
        for (var c = b[0].toUpperCase() + b.slice(1), d = b, e = Eb.length; e--; ) if ((b = Eb[e] + c) in a) return b;
        return d;
    }
    function Gb(a, b, c) {
        var d = Ab.exec(b);
        return d ? Math.max(0, d[1] - (c || 0)) + (d[2] || "px") : b;
    }
    function Hb(a, b, c, d, e) {
        for (var f = c === (d ? "border" : "content") ? 4 : "width" === b ? 1 : 0, g = 0; 4 > f; f += 2) "margin" === c && (g += n.css(a, c + R[f], !0, e)), 
        d ? ("content" === c && (g -= n.css(a, "padding" + R[f], !0, e)), "margin" !== c && (g -= n.css(a, "border" + R[f] + "Width", !0, e))) : (g += n.css(a, "padding" + R[f], !0, e), 
        "padding" !== c && (g += n.css(a, "border" + R[f] + "Width", !0, e)));
        return g;
    }
    function Ib(a, b, c) {
        var d = !0, e = "width" === b ? a.offsetWidth : a.offsetHeight, f = wb(a), g = "border-box" === n.css(a, "boxSizing", !1, f);
        if (0 >= e || null == e) {
            if (e = xb(a, b, f), (0 > e || null == e) && (e = a.style[b]), vb.test(e)) return e;
            d = g && (k.boxSizingReliable() || e === a.style[b]), e = parseFloat(e) || 0;
        }
        return e + Hb(a, b, c || (g ? "border" : "content"), d, f) + "px";
    }
    function Jb(a, b) {
        for (var c, d, e, f = [], g = 0, h = a.length; h > g; g++) d = a[g], d.style && (f[g] = L.get(d, "olddisplay"), 
        c = d.style.display, b ? (f[g] || "none" !== c || (d.style.display = ""), "" === d.style.display && S(d) && (f[g] = L.access(d, "olddisplay", tb(d.nodeName)))) : (e = S(d), 
        "none" === c && e || L.set(d, "olddisplay", e ? c : n.css(d, "display"))));
        for (g = 0; h > g; g++) d = a[g], d.style && (b && "none" !== d.style.display && "" !== d.style.display || (d.style.display = b ? f[g] || "" : "none"));
        return a;
    }
    function Kb(a, b, c, d, e) {
        return new Kb.prototype.init(a, b, c, d, e);
    }
    function Sb() {
        return setTimeout(function() {
            Lb = void 0;
        }), Lb = n.now();
    }
    function Tb(a, b) {
        var c, d = 0, e = {
            height: a
        };
        for (b = b ? 1 : 0; 4 > d; d += 2 - b) c = R[d], e["margin" + c] = e["padding" + c] = a;
        return b && (e.opacity = e.width = a), e;
    }
    function Ub(a, b, c) {
        for (var d, e = (Rb[b] || []).concat(Rb["*"]), f = 0, g = e.length; g > f; f++) if (d = e[f].call(c, b, a)) return d;
    }
    function Vb(a, b, c) {
        var d, e, f, g, h, i, j, l = this, m = {}, o = a.style, p = a.nodeType && S(a), q = L.get(a, "fxshow");
        c.queue || (h = n._queueHooks(a, "fx"), null == h.unqueued && (h.unqueued = 0, i = h.empty.fire, 
        h.empty.fire = function() {
            h.unqueued || i();
        }), h.unqueued++, l.always(function() {
            l.always(function() {
                h.unqueued--, n.queue(a, "fx").length || h.empty.fire();
            });
        })), 1 === a.nodeType && ("height" in b || "width" in b) && (c.overflow = [ o.overflow, o.overflowX, o.overflowY ], 
        j = n.css(a, "display"), "inline" === ("none" === j ? L.get(a, "olddisplay") || tb(a.nodeName) : j) && "none" === n.css(a, "float") && (o.display = "inline-block")), 
        c.overflow && (o.overflow = "hidden", l.always(function() {
            o.overflow = c.overflow[0], o.overflowX = c.overflow[1], o.overflowY = c.overflow[2];
        }));
        for (d in b) if (e = b[d], Nb.exec(e)) {
            if (delete b[d], f = f || "toggle" === e, e === (p ? "hide" : "show")) {
                if ("show" !== e || !q || void 0 === q[d]) continue;
                p = !0;
            }
            m[d] = q && q[d] || n.style(a, d);
        } else j = void 0;
        if (n.isEmptyObject(m)) "inline" === ("none" === j ? tb(a.nodeName) : j) && (o.display = j); else {
            q ? "hidden" in q && (p = q.hidden) : q = L.access(a, "fxshow", {}), f && (q.hidden = !p), 
            p ? n(a).show() : l.done(function() {
                n(a).hide();
            }), l.done(function() {
                var b;
                L.remove(a, "fxshow");
                for (b in m) n.style(a, b, m[b]);
            });
            for (d in m) g = Ub(p ? q[d] : 0, d, l), d in q || (q[d] = g.start, p && (g.end = g.start, 
            g.start = "width" === d || "height" === d ? 1 : 0));
        }
    }
    function Wb(a, b) {
        var c, d, e, f, g;
        for (c in a) if (d = n.camelCase(c), e = b[d], f = a[c], n.isArray(f) && (e = f[1], 
        f = a[c] = f[0]), c !== d && (a[d] = f, delete a[c]), (g = n.cssHooks[d]) && "expand" in g) {
            f = g.expand(f), delete a[d];
            for (c in f) c in a || (a[c] = f[c], b[c] = e);
        } else b[d] = e;
    }
    function Xb(a, b, c) {
        var d, e, f = 0, g = Qb.length, h = n.Deferred().always(function() {
            delete i.elem;
        }), i = function() {
            if (e) return !1;
            for (var b = Lb || Sb(), c = Math.max(0, j.startTime + j.duration - b), d = c / j.duration || 0, f = 1 - d, g = 0, i = j.tweens.length; i > g; g++) j.tweens[g].run(f);
            return h.notifyWith(a, [ j, f, c ]), 1 > f && i ? c : (h.resolveWith(a, [ j ]), 
            !1);
        }, j = h.promise({
            elem: a,
            props: n.extend({}, b),
            opts: n.extend(!0, {
                specialEasing: {}
            }, c),
            originalProperties: b,
            originalOptions: c,
            startTime: Lb || Sb(),
            duration: c.duration,
            tweens: [],
            createTween: function(b, c) {
                var d = n.Tween(a, j.opts, b, c, j.opts.specialEasing[b] || j.opts.easing);
                return j.tweens.push(d), d;
            },
            stop: function(b) {
                var c = 0, d = b ? j.tweens.length : 0;
                if (e) return this;
                for (e = !0; d > c; c++) j.tweens[c].run(1);
                return b ? h.resolveWith(a, [ j, b ]) : h.rejectWith(a, [ j, b ]), this;
            }
        }), k = j.props;
        for (Wb(k, j.opts.specialEasing); g > f; f++) if (d = Qb[f].call(j, a, k, j.opts)) return d;
        return n.map(k, Ub, j), n.isFunction(j.opts.start) && j.opts.start.call(a, j), n.fx.timer(n.extend(i, {
            elem: a,
            anim: j,
            queue: j.opts.queue
        })), j.progress(j.opts.progress).done(j.opts.done, j.opts.complete).fail(j.opts.fail).always(j.opts.always);
    }
    function rc(a) {
        return function(b, c) {
            "string" != typeof b && (c = b, b = "*");
            var d, e = 0, f = b.toLowerCase().match(E) || [];
            if (n.isFunction(c)) for (;d = f[e++]; ) "+" === d[0] ? (d = d.slice(1) || "*", 
            (a[d] = a[d] || []).unshift(c)) : (a[d] = a[d] || []).push(c);
        };
    }
    function sc(a, b, c, d) {
        function g(h) {
            var i;
            return e[h] = !0, n.each(a[h] || [], function(a, h) {
                var j = h(b, c, d);
                return "string" != typeof j || f || e[j] ? f ? !(i = j) : void 0 : (b.dataTypes.unshift(j), 
                g(j), !1);
            }), i;
        }
        var e = {}, f = a === oc;
        return g(b.dataTypes[0]) || !e["*"] && g("*");
    }
    function tc(a, b) {
        var c, d, e = n.ajaxSettings.flatOptions || {};
        for (c in b) void 0 !== b[c] && ((e[c] ? a : d || (d = {}))[c] = b[c]);
        return d && n.extend(!0, a, d), a;
    }
    function uc(a, b, c) {
        for (var d, e, f, g, h = a.contents, i = a.dataTypes; "*" === i[0]; ) i.shift(), 
        void 0 === d && (d = a.mimeType || b.getResponseHeader("Content-Type"));
        if (d) for (e in h) if (h[e] && h[e].test(d)) {
            i.unshift(e);
            break;
        }
        if (i[0] in c) f = i[0]; else {
            for (e in c) {
                if (!i[0] || a.converters[e + " " + i[0]]) {
                    f = e;
                    break;
                }
                g || (g = e);
            }
            f = f || g;
        }
        return f ? (f !== i[0] && i.unshift(f), c[f]) : void 0;
    }
    function vc(a, b, c, d) {
        var e, f, g, h, i, j = {}, k = a.dataTypes.slice();
        if (k[1]) for (g in a.converters) j[g.toLowerCase()] = a.converters[g];
        for (f = k.shift(); f; ) if (a.responseFields[f] && (c[a.responseFields[f]] = b), 
        !i && d && a.dataFilter && (b = a.dataFilter(b, a.dataType)), i = f, f = k.shift()) if ("*" === f) f = i; else if ("*" !== i && i !== f) {
            if (!(g = j[i + " " + f] || j["* " + f])) for (e in j) if (h = e.split(" "), h[1] === f && (g = j[i + " " + h[0]] || j["* " + h[0]])) {
                !0 === g ? g = j[e] : !0 !== j[e] && (f = h[0], k.unshift(h[1]));
                break;
            }
            if (!0 !== g) if (g && a.throws) b = g(b); else try {
                b = g(b);
            } catch (l) {
                return {
                    state: "parsererror",
                    error: g ? l : "No conversion from " + i + " to " + f
                };
            }
        }
        return {
            state: "success",
            data: b
        };
    }
    function Bc(a, b, c, d) {
        var e;
        if (n.isArray(b)) n.each(b, function(b, e) {
            c || xc.test(a) ? d(a, e) : Bc(a + "[" + ("object" == typeof e ? b : "") + "]", e, c, d);
        }); else if (c || "object" !== n.type(b)) d(a, b); else for (e in b) Bc(a + "[" + e + "]", b[e], c, d);
    }
    function Kc(a) {
        return n.isWindow(a) ? a : 9 === a.nodeType && a.defaultView;
    }
    var c = [], d = c.slice, e = c.concat, f = c.push, g = c.indexOf, h = {}, i = h.toString, j = h.hasOwnProperty, k = {}, l = a.document, m = "2.1.1", n = function(a, b) {
        return new n.fn.init(a, b);
    }, o = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, p = /^-ms-/, q = /-([\da-z])/gi, r = function(a, b) {
        return b.toUpperCase();
    };
    n.fn = n.prototype = {
        jquery: m,
        constructor: n,
        selector: "",
        length: 0,
        toArray: function() {
            return d.call(this);
        },
        get: function(a) {
            return null != a ? 0 > a ? this[a + this.length] : this[a] : d.call(this);
        },
        pushStack: function(a) {
            var b = n.merge(this.constructor(), a);
            return b.prevObject = this, b.context = this.context, b;
        },
        each: function(a, b) {
            return n.each(this, a, b);
        },
        map: function(a) {
            return this.pushStack(n.map(this, function(b, c) {
                return a.call(b, c, b);
            }));
        },
        slice: function() {
            return this.pushStack(d.apply(this, arguments));
        },
        first: function() {
            return this.eq(0);
        },
        last: function() {
            return this.eq(-1);
        },
        eq: function(a) {
            var b = this.length, c = +a + (0 > a ? b : 0);
            return this.pushStack(c >= 0 && b > c ? [ this[c] ] : []);
        },
        end: function() {
            return this.prevObject || this.constructor(null);
        },
        push: f,
        sort: c.sort,
        splice: c.splice
    }, n.extend = n.fn.extend = function() {
        var a, b, c, d, e, f, g = arguments[0] || {}, h = 1, i = arguments.length, j = !1;
        for ("boolean" == typeof g && (j = g, g = arguments[h] || {}, h++), "object" == typeof g || n.isFunction(g) || (g = {}), 
        h === i && (g = this, h--); i > h; h++) if (null != (a = arguments[h])) for (b in a) c = g[b], 
        d = a[b], g !== d && (j && d && (n.isPlainObject(d) || (e = n.isArray(d))) ? (e ? (e = !1, 
        f = c && n.isArray(c) ? c : []) : f = c && n.isPlainObject(c) ? c : {}, g[b] = n.extend(j, f, d)) : void 0 !== d && (g[b] = d));
        return g;
    }, n.extend({
        expando: "jQuery" + (m + Math.random()).replace(/\D/g, ""),
        isReady: !0,
        error: function(a) {
            throw new Error(a);
        },
        noop: function() {},
        isFunction: function(a) {
            return "function" === n.type(a);
        },
        isArray: Array.isArray,
        isWindow: function(a) {
            return null != a && a === a.window;
        },
        isNumeric: function(a) {
            return !n.isArray(a) && a - parseFloat(a) >= 0;
        },
        isPlainObject: function(a) {
            return "object" === n.type(a) && !a.nodeType && !n.isWindow(a) && !(a.constructor && !j.call(a.constructor.prototype, "isPrototypeOf"));
        },
        isEmptyObject: function(a) {
            var b;
            for (b in a) return !1;
            return !0;
        },
        type: function(a) {
            return null == a ? a + "" : "object" == typeof a || "function" == typeof a ? h[i.call(a)] || "object" : typeof a;
        },
        globalEval: function(a) {
            var b, c = eval;
            (a = n.trim(a)) && (1 === a.indexOf("use strict") ? (b = l.createElement("script"), 
            b.text = a, l.head.appendChild(b).parentNode.removeChild(b)) : c(a));
        },
        camelCase: function(a) {
            return a.replace(p, "ms-").replace(q, r);
        },
        nodeName: function(a, b) {
            return a.nodeName && a.nodeName.toLowerCase() === b.toLowerCase();
        },
        each: function(a, b, c) {
            var e = 0, f = a.length, g = s(a);
            if (c) {
                if (g) for (;f > e && !1 !== b.apply(a[e], c); e++) ; else for (e in a) if (!1 === b.apply(a[e], c)) break;
            } else if (g) for (;f > e && !1 !== b.call(a[e], e, a[e]); e++) ; else for (e in a) if (!1 === b.call(a[e], e, a[e])) break;
            return a;
        },
        trim: function(a) {
            return null == a ? "" : (a + "").replace(o, "");
        },
        makeArray: function(a, b) {
            var c = b || [];
            return null != a && (s(Object(a)) ? n.merge(c, "string" == typeof a ? [ a ] : a) : f.call(c, a)), 
            c;
        },
        inArray: function(a, b, c) {
            return null == b ? -1 : g.call(b, a, c);
        },
        merge: function(a, b) {
            for (var c = +b.length, d = 0, e = a.length; c > d; d++) a[e++] = b[d];
            return a.length = e, a;
        },
        grep: function(a, b, c) {
            for (var e = [], f = 0, g = a.length, h = !c; g > f; f++) !b(a[f], f) !== h && e.push(a[f]);
            return e;
        },
        map: function(a, b, c) {
            var d, f = 0, g = a.length, h = s(a), i = [];
            if (h) for (;g > f; f++) null != (d = b(a[f], f, c)) && i.push(d); else for (f in a) null != (d = b(a[f], f, c)) && i.push(d);
            return e.apply([], i);
        },
        guid: 1,
        proxy: function(a, b) {
            var c, e, f;
            return "string" == typeof b && (c = a[b], b = a, a = c), n.isFunction(a) ? (e = d.call(arguments, 2), 
            f = function() {
                return a.apply(b || this, e.concat(d.call(arguments)));
            }, f.guid = a.guid = a.guid || n.guid++, f) : void 0;
        },
        now: Date.now,
        support: k
    }), n.each("Boolean Number String Function Array Date RegExp Object Error".split(" "), function(a, b) {
        h["[object " + b + "]"] = b.toLowerCase();
    });
    var t = function(a) {
        function fb(a, b, d, e) {
            var f, h, j, k, l, o, r, s, w, x;
            if ((b ? b.ownerDocument || b : v) !== n && m(b), b = b || n, d = d || [], !a || "string" != typeof a) return d;
            if (1 !== (k = b.nodeType) && 9 !== k) return [];
            if (p && !e) {
                if (f = _.exec(a)) if (j = f[1]) {
                    if (9 === k) {
                        if (!(h = b.getElementById(j)) || !h.parentNode) return d;
                        if (h.id === j) return d.push(h), d;
                    } else if (b.ownerDocument && (h = b.ownerDocument.getElementById(j)) && t(b, h) && h.id === j) return d.push(h), 
                    d;
                } else {
                    if (f[2]) return I.apply(d, b.getElementsByTagName(a)), d;
                    if ((j = f[3]) && c.getElementsByClassName && b.getElementsByClassName) return I.apply(d, b.getElementsByClassName(j)), 
                    d;
                }
                if (c.qsa && (!q || !q.test(a))) {
                    if (s = r = u, w = b, x = 9 === k && a, 1 === k && "object" !== b.nodeName.toLowerCase()) {
                        for (o = g(a), (r = b.getAttribute("id")) ? s = r.replace(bb, "\\$&") : b.setAttribute("id", s), 
                        s = "[id='" + s + "'] ", l = o.length; l--; ) o[l] = s + qb(o[l]);
                        w = ab.test(a) && ob(b.parentNode) || b, x = o.join(",");
                    }
                    if (x) try {
                        return I.apply(d, w.querySelectorAll(x)), d;
                    } catch (y) {} finally {
                        r || b.removeAttribute("id");
                    }
                }
            }
            return i(a.replace(R, "$1"), b, d, e);
        }
        function gb() {
            function b(c, e) {
                return a.push(c + " ") > d.cacheLength && delete b[a.shift()], b[c + " "] = e;
            }
            var a = [];
            return b;
        }
        function hb(a) {
            return a[u] = !0, a;
        }
        function ib(a) {
            var b = n.createElement("div");
            try {
                return !!a(b);
            } catch (c) {
                return !1;
            } finally {
                b.parentNode && b.parentNode.removeChild(b), b = null;
            }
        }
        function jb(a, b) {
            for (var c = a.split("|"), e = a.length; e--; ) d.attrHandle[c[e]] = b;
        }
        function kb(a, b) {
            var c = b && a, d = c && 1 === a.nodeType && 1 === b.nodeType && (~b.sourceIndex || D) - (~a.sourceIndex || D);
            if (d) return d;
            if (c) for (;c = c.nextSibling; ) if (c === b) return -1;
            return a ? 1 : -1;
        }
        function nb(a) {
            return hb(function(b) {
                return b = +b, hb(function(c, d) {
                    for (var e, f = a([], c.length, b), g = f.length; g--; ) c[e = f[g]] && (c[e] = !(d[e] = c[e]));
                });
            });
        }
        function ob(a) {
            return a && typeof a.getElementsByTagName !== C && a;
        }
        function pb() {}
        function qb(a) {
            for (var b = 0, c = a.length, d = ""; c > b; b++) d += a[b].value;
            return d;
        }
        function rb(a, b, c) {
            var d = b.dir, e = c && "parentNode" === d, f = x++;
            return b.first ? function(b, c, f) {
                for (;b = b[d]; ) if (1 === b.nodeType || e) return a(b, c, f);
            } : function(b, c, g) {
                var h, i, j = [ w, f ];
                if (g) {
                    for (;b = b[d]; ) if ((1 === b.nodeType || e) && a(b, c, g)) return !0;
                } else for (;b = b[d]; ) if (1 === b.nodeType || e) {
                    if (i = b[u] || (b[u] = {}), (h = i[d]) && h[0] === w && h[1] === f) return j[2] = h[2];
                    if (i[d] = j, j[2] = a(b, c, g)) return !0;
                }
            };
        }
        function sb(a) {
            return a.length > 1 ? function(b, c, d) {
                for (var e = a.length; e--; ) if (!a[e](b, c, d)) return !1;
                return !0;
            } : a[0];
        }
        function tb(a, b, c) {
            for (var d = 0, e = b.length; e > d; d++) fb(a, b[d], c);
            return c;
        }
        function ub(a, b, c, d, e) {
            for (var f, g = [], h = 0, i = a.length, j = null != b; i > h; h++) (f = a[h]) && (!c || c(f, d, e)) && (g.push(f), 
            j && b.push(h));
            return g;
        }
        function vb(a, b, c, d, e, f) {
            return d && !d[u] && (d = vb(d)), e && !e[u] && (e = vb(e, f)), hb(function(f, g, h, i) {
                var j, k, l, m = [], n = [], o = g.length, p = f || tb(b || "*", h.nodeType ? [ h ] : h, []), q = !a || !f && b ? p : ub(p, m, a, h, i), r = c ? e || (f ? a : o || d) ? [] : g : q;
                if (c && c(q, r, h, i), d) for (j = ub(r, n), d(j, [], h, i), k = j.length; k--; ) (l = j[k]) && (r[n[k]] = !(q[n[k]] = l));
                if (f) {
                    if (e || a) {
                        if (e) {
                            for (j = [], k = r.length; k--; ) (l = r[k]) && j.push(q[k] = l);
                            e(null, r = [], j, i);
                        }
                        for (k = r.length; k--; ) (l = r[k]) && (j = e ? K.call(f, l) : m[k]) > -1 && (f[j] = !(g[j] = l));
                    }
                } else r = ub(r === g ? r.splice(o, r.length) : r), e ? e(null, g, r, i) : I.apply(g, r);
            });
        }
        function wb(a) {
            for (var b, c, e, f = a.length, g = d.relative[a[0].type], h = g || d.relative[" "], i = g ? 1 : 0, k = rb(function(a) {
                return a === b;
            }, h, !0), l = rb(function(a) {
                return K.call(b, a) > -1;
            }, h, !0), m = [ function(a, c, d) {
                return !g && (d || c !== j) || ((b = c).nodeType ? k(a, c, d) : l(a, c, d));
            } ]; f > i; i++) if (c = d.relative[a[i].type]) m = [ rb(sb(m), c) ]; else {
                if (c = d.filter[a[i].type].apply(null, a[i].matches), c[u]) {
                    for (e = ++i; f > e && !d.relative[a[e].type]; e++) ;
                    return vb(i > 1 && sb(m), i > 1 && qb(a.slice(0, i - 1).concat({
                        value: " " === a[i - 2].type ? "*" : ""
                    })).replace(R, "$1"), c, e > i && wb(a.slice(i, e)), f > e && wb(a = a.slice(e)), f > e && qb(a));
                }
                m.push(c);
            }
            return sb(m);
        }
        function xb(a, b) {
            var c = b.length > 0, e = a.length > 0, f = function(f, g, h, i, k) {
                var l, m, o, p = 0, q = "0", r = f && [], s = [], t = j, u = f || e && d.find.TAG("*", k), v = w += null == t ? 1 : Math.random() || .1, x = u.length;
                for (k && (j = g !== n && g); q !== x && null != (l = u[q]); q++) {
                    if (e && l) {
                        for (m = 0; o = a[m++]; ) if (o(l, g, h)) {
                            i.push(l);
                            break;
                        }
                        k && (w = v);
                    }
                    c && ((l = !o && l) && p--, f && r.push(l));
                }
                if (p += q, c && q !== p) {
                    for (m = 0; o = b[m++]; ) o(r, s, g, h);
                    if (f) {
                        if (p > 0) for (;q--; ) r[q] || s[q] || (s[q] = G.call(i));
                        s = ub(s);
                    }
                    I.apply(i, s), k && !f && s.length > 0 && p + b.length > 1 && fb.uniqueSort(i);
                }
                return k && (w = v, j = t), r;
            };
            return c ? hb(f) : f;
        }
        var b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u = "sizzle" + -new Date(), v = a.document, w = 0, x = 0, y = gb(), z = gb(), A = gb(), B = function(a, b) {
            return a === b && (l = !0), 0;
        }, C = "undefined", D = 1 << 31, E = {}.hasOwnProperty, F = [], G = F.pop, H = F.push, I = F.push, J = F.slice, K = F.indexOf || function(a) {
            for (var b = 0, c = this.length; c > b; b++) if (this[b] === a) return b;
            return -1;
        }, L = "checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped", M = "[\\x20\\t\\r\\n\\f]", N = "(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+", O = N.replace("w", "w#"), P = "\\[" + M + "*(" + N + ")(?:" + M + "*([*^$|!~]?=)" + M + "*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|(" + O + "))|)" + M + "*\\]", Q = ":(" + N + ")(?:\\((('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|((?:\\\\.|[^\\\\()[\\]]|" + P + ")*)|.*)\\)|)", R = new RegExp("^" + M + "+|((?:^|[^\\\\])(?:\\\\.)*)" + M + "+$", "g"), S = new RegExp("^" + M + "*," + M + "*"), T = new RegExp("^" + M + "*([>+~]|" + M + ")" + M + "*"), U = new RegExp("=" + M + "*([^\\]'\"]*?)" + M + "*\\]", "g"), V = new RegExp(Q), W = new RegExp("^" + O + "$"), X = {
            ID: new RegExp("^#(" + N + ")"),
            CLASS: new RegExp("^\\.(" + N + ")"),
            TAG: new RegExp("^(" + N.replace("w", "w*") + ")"),
            ATTR: new RegExp("^" + P),
            PSEUDO: new RegExp("^" + Q),
            CHILD: new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\(" + M + "*(even|odd|(([+-]|)(\\d*)n|)" + M + "*(?:([+-]|)" + M + "*(\\d+)|))" + M + "*\\)|)", "i"),
            bool: new RegExp("^(?:" + L + ")$", "i"),
            needsContext: new RegExp("^" + M + "*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\(" + M + "*((?:-\\d)?\\d*)" + M + "*\\)|)(?=[^-]|$)", "i")
        }, Y = /^(?:input|select|textarea|button)$/i, Z = /^h\d$/i, $ = /^[^{]+\{\s*\[native \w/, _ = /^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/, ab = /[+~]/, bb = /'|\\/g, cb = new RegExp("\\\\([\\da-f]{1,6}" + M + "?|(" + M + ")|.)", "ig"), db = function(a, b, c) {
            var d = "0x" + b - 65536;
            return d !== d || c ? b : 0 > d ? String.fromCharCode(d + 65536) : String.fromCharCode(d >> 10 | 55296, 1023 & d | 56320);
        };
        try {
            I.apply(F = J.call(v.childNodes), v.childNodes), F[v.childNodes.length].nodeType;
        } catch (eb) {
            I = {
                apply: F.length ? function(a, b) {
                    H.apply(a, J.call(b));
                } : function(a, b) {
                    for (var c = a.length, d = 0; a[c++] = b[d++]; ) ;
                    a.length = c - 1;
                }
            };
        }
        c = fb.support = {}, f = fb.isXML = function(a) {
            var b = a && (a.ownerDocument || a).documentElement;
            return !!b && "HTML" !== b.nodeName;
        }, m = fb.setDocument = function(a) {
            var b, e = a ? a.ownerDocument || a : v, g = e.defaultView;
            return e !== n && 9 === e.nodeType && e.documentElement ? (n = e, o = e.documentElement, 
            p = !f(e), g && g !== g.top && (g.addEventListener ? g.addEventListener("unload", function() {
                m();
            }, !1) : g.attachEvent && g.attachEvent("onunload", function() {
                m();
            })), c.attributes = ib(function(a) {
                return a.className = "i", !a.getAttribute("className");
            }), c.getElementsByTagName = ib(function(a) {
                return a.appendChild(e.createComment("")), !a.getElementsByTagName("*").length;
            }), c.getElementsByClassName = $.test(e.getElementsByClassName) && ib(function(a) {
                return a.innerHTML = "<div class='a'></div><div class='a i'></div>", a.firstChild.className = "i", 
                2 === a.getElementsByClassName("i").length;
            }), c.getById = ib(function(a) {
                return o.appendChild(a).id = u, !e.getElementsByName || !e.getElementsByName(u).length;
            }), c.getById ? (d.find.ID = function(a, b) {
                if (typeof b.getElementById !== C && p) {
                    var c = b.getElementById(a);
                    return c && c.parentNode ? [ c ] : [];
                }
            }, d.filter.ID = function(a) {
                var b = a.replace(cb, db);
                return function(a) {
                    return a.getAttribute("id") === b;
                };
            }) : (delete d.find.ID, d.filter.ID = function(a) {
                var b = a.replace(cb, db);
                return function(a) {
                    var c = typeof a.getAttributeNode !== C && a.getAttributeNode("id");
                    return c && c.value === b;
                };
            }), d.find.TAG = c.getElementsByTagName ? function(a, b) {
                return typeof b.getElementsByTagName !== C ? b.getElementsByTagName(a) : void 0;
            } : function(a, b) {
                var c, d = [], e = 0, f = b.getElementsByTagName(a);
                if ("*" === a) {
                    for (;c = f[e++]; ) 1 === c.nodeType && d.push(c);
                    return d;
                }
                return f;
            }, d.find.CLASS = c.getElementsByClassName && function(a, b) {
                return typeof b.getElementsByClassName !== C && p ? b.getElementsByClassName(a) : void 0;
            }, r = [], q = [], (c.qsa = $.test(e.querySelectorAll)) && (ib(function(a) {
                a.innerHTML = "<select msallowclip=''><option selected=''></option></select>", a.querySelectorAll("[msallowclip^='']").length && q.push("[*^$]=" + M + "*(?:''|\"\")"), 
                a.querySelectorAll("[selected]").length || q.push("\\[" + M + "*(?:value|" + L + ")"), 
                a.querySelectorAll(":checked").length || q.push(":checked");
            }), ib(function(a) {
                var b = e.createElement("input");
                b.setAttribute("type", "hidden"), a.appendChild(b).setAttribute("name", "D"), a.querySelectorAll("[name=d]").length && q.push("name" + M + "*[*^$|!~]?="), 
                a.querySelectorAll(":enabled").length || q.push(":enabled", ":disabled"), a.querySelectorAll("*,:x"), 
                q.push(",.*:");
            })), (c.matchesSelector = $.test(s = o.matches || o.webkitMatchesSelector || o.mozMatchesSelector || o.oMatchesSelector || o.msMatchesSelector)) && ib(function(a) {
                c.disconnectedMatch = s.call(a, "div"), s.call(a, "[s!='']:x"), r.push("!=", Q);
            }), q = q.length && new RegExp(q.join("|")), r = r.length && new RegExp(r.join("|")), 
            b = $.test(o.compareDocumentPosition), t = b || $.test(o.contains) ? function(a, b) {
                var c = 9 === a.nodeType ? a.documentElement : a, d = b && b.parentNode;
                return a === d || !(!d || 1 !== d.nodeType || !(c.contains ? c.contains(d) : a.compareDocumentPosition && 16 & a.compareDocumentPosition(d)));
            } : function(a, b) {
                if (b) for (;b = b.parentNode; ) if (b === a) return !0;
                return !1;
            }, B = b ? function(a, b) {
                if (a === b) return l = !0, 0;
                var d = !a.compareDocumentPosition - !b.compareDocumentPosition;
                return d || (d = (a.ownerDocument || a) === (b.ownerDocument || b) ? a.compareDocumentPosition(b) : 1, 
                1 & d || !c.sortDetached && b.compareDocumentPosition(a) === d ? a === e || a.ownerDocument === v && t(v, a) ? -1 : b === e || b.ownerDocument === v && t(v, b) ? 1 : k ? K.call(k, a) - K.call(k, b) : 0 : 4 & d ? -1 : 1);
            } : function(a, b) {
                if (a === b) return l = !0, 0;
                var c, d = 0, f = a.parentNode, g = b.parentNode, h = [ a ], i = [ b ];
                if (!f || !g) return a === e ? -1 : b === e ? 1 : f ? -1 : g ? 1 : k ? K.call(k, a) - K.call(k, b) : 0;
                if (f === g) return kb(a, b);
                for (c = a; c = c.parentNode; ) h.unshift(c);
                for (c = b; c = c.parentNode; ) i.unshift(c);
                for (;h[d] === i[d]; ) d++;
                return d ? kb(h[d], i[d]) : h[d] === v ? -1 : i[d] === v ? 1 : 0;
            }, e) : n;
        }, fb.matches = function(a, b) {
            return fb(a, null, null, b);
        }, fb.matchesSelector = function(a, b) {
            if ((a.ownerDocument || a) !== n && m(a), b = b.replace(U, "='$1']"), !(!c.matchesSelector || !p || r && r.test(b) || q && q.test(b))) try {
                var d = s.call(a, b);
                if (d || c.disconnectedMatch || a.document && 11 !== a.document.nodeType) return d;
            } catch (e) {}
            return fb(b, n, null, [ a ]).length > 0;
        }, fb.contains = function(a, b) {
            return (a.ownerDocument || a) !== n && m(a), t(a, b);
        }, fb.attr = function(a, b) {
            (a.ownerDocument || a) !== n && m(a);
            var e = d.attrHandle[b.toLowerCase()], f = e && E.call(d.attrHandle, b.toLowerCase()) ? e(a, b, !p) : void 0;
            return void 0 !== f ? f : c.attributes || !p ? a.getAttribute(b) : (f = a.getAttributeNode(b)) && f.specified ? f.value : null;
        }, fb.error = function(a) {
            throw new Error("Syntax error, unrecognized expression: " + a);
        }, fb.uniqueSort = function(a) {
            var b, d = [], e = 0, f = 0;
            if (l = !c.detectDuplicates, k = !c.sortStable && a.slice(0), a.sort(B), l) {
                for (;b = a[f++]; ) b === a[f] && (e = d.push(f));
                for (;e--; ) a.splice(d[e], 1);
            }
            return k = null, a;
        }, e = fb.getText = function(a) {
            var b, c = "", d = 0, f = a.nodeType;
            if (f) {
                if (1 === f || 9 === f || 11 === f) {
                    if ("string" == typeof a.textContent) return a.textContent;
                    for (a = a.firstChild; a; a = a.nextSibling) c += e(a);
                } else if (3 === f || 4 === f) return a.nodeValue;
            } else for (;b = a[d++]; ) c += e(b);
            return c;
        }, d = fb.selectors = {
            cacheLength: 50,
            createPseudo: hb,
            match: X,
            attrHandle: {},
            find: {},
            relative: {
                ">": {
                    dir: "parentNode",
                    first: !0
                },
                " ": {
                    dir: "parentNode"
                },
                "+": {
                    dir: "previousSibling",
                    first: !0
                },
                "~": {
                    dir: "previousSibling"
                }
            },
            preFilter: {
                ATTR: function(a) {
                    return a[1] = a[1].replace(cb, db), a[3] = (a[3] || a[4] || a[5] || "").replace(cb, db), 
                    "~=" === a[2] && (a[3] = " " + a[3] + " "), a.slice(0, 4);
                },
                CHILD: function(a) {
                    return a[1] = a[1].toLowerCase(), "nth" === a[1].slice(0, 3) ? (a[3] || fb.error(a[0]), 
                    a[4] = +(a[4] ? a[5] + (a[6] || 1) : 2 * ("even" === a[3] || "odd" === a[3])), a[5] = +(a[7] + a[8] || "odd" === a[3])) : a[3] && fb.error(a[0]), 
                    a;
                },
                PSEUDO: function(a) {
                    var b, c = !a[6] && a[2];
                    return X.CHILD.test(a[0]) ? null : (a[3] ? a[2] = a[4] || a[5] || "" : c && V.test(c) && (b = g(c, !0)) && (b = c.indexOf(")", c.length - b) - c.length) && (a[0] = a[0].slice(0, b), 
                    a[2] = c.slice(0, b)), a.slice(0, 3));
                }
            },
            filter: {
                TAG: function(a) {
                    var b = a.replace(cb, db).toLowerCase();
                    return "*" === a ? function() {
                        return !0;
                    } : function(a) {
                        return a.nodeName && a.nodeName.toLowerCase() === b;
                    };
                },
                CLASS: function(a) {
                    var b = y[a + " "];
                    return b || (b = new RegExp("(^|" + M + ")" + a + "(" + M + "|$)")) && y(a, function(a) {
                        return b.test("string" == typeof a.className && a.className || typeof a.getAttribute !== C && a.getAttribute("class") || "");
                    });
                },
                ATTR: function(a, b, c) {
                    return function(d) {
                        var e = fb.attr(d, a);
                        return null == e ? "!=" === b : !b || (e += "", "=" === b ? e === c : "!=" === b ? e !== c : "^=" === b ? c && 0 === e.indexOf(c) : "*=" === b ? c && e.indexOf(c) > -1 : "$=" === b ? c && e.slice(-c.length) === c : "~=" === b ? (" " + e + " ").indexOf(c) > -1 : "|=" === b && (e === c || e.slice(0, c.length + 1) === c + "-"));
                    };
                },
                CHILD: function(a, b, c, d, e) {
                    var f = "nth" !== a.slice(0, 3), g = "last" !== a.slice(-4), h = "of-type" === b;
                    return 1 === d && 0 === e ? function(a) {
                        return !!a.parentNode;
                    } : function(b, c, i) {
                        var j, k, l, m, n, o, p = f !== g ? "nextSibling" : "previousSibling", q = b.parentNode, r = h && b.nodeName.toLowerCase(), s = !i && !h;
                        if (q) {
                            if (f) {
                                for (;p; ) {
                                    for (l = b; l = l[p]; ) if (h ? l.nodeName.toLowerCase() === r : 1 === l.nodeType) return !1;
                                    o = p = "only" === a && !o && "nextSibling";
                                }
                                return !0;
                            }
                            if (o = [ g ? q.firstChild : q.lastChild ], g && s) {
                                for (k = q[u] || (q[u] = {}), j = k[a] || [], n = j[0] === w && j[1], m = j[0] === w && j[2], 
                                l = n && q.childNodes[n]; l = ++n && l && l[p] || (m = n = 0) || o.pop(); ) if (1 === l.nodeType && ++m && l === b) {
                                    k[a] = [ w, n, m ];
                                    break;
                                }
                            } else if (s && (j = (b[u] || (b[u] = {}))[a]) && j[0] === w) m = j[1]; else for (;(l = ++n && l && l[p] || (m = n = 0) || o.pop()) && ((h ? l.nodeName.toLowerCase() !== r : 1 !== l.nodeType) || !++m || (s && ((l[u] || (l[u] = {}))[a] = [ w, m ]), 
                            l !== b)); ) ;
                            return (m -= e) === d || m % d == 0 && m / d >= 0;
                        }
                    };
                },
                PSEUDO: function(a, b) {
                    var c, e = d.pseudos[a] || d.setFilters[a.toLowerCase()] || fb.error("unsupported pseudo: " + a);
                    return e[u] ? e(b) : e.length > 1 ? (c = [ a, a, "", b ], d.setFilters.hasOwnProperty(a.toLowerCase()) ? hb(function(a, c) {
                        for (var d, f = e(a, b), g = f.length; g--; ) d = K.call(a, f[g]), a[d] = !(c[d] = f[g]);
                    }) : function(a) {
                        return e(a, 0, c);
                    }) : e;
                }
            },
            pseudos: {
                not: hb(function(a) {
                    var b = [], c = [], d = h(a.replace(R, "$1"));
                    return d[u] ? hb(function(a, b, c, e) {
                        for (var f, g = d(a, null, e, []), h = a.length; h--; ) (f = g[h]) && (a[h] = !(b[h] = f));
                    }) : function(a, e, f) {
                        return b[0] = a, d(b, null, f, c), !c.pop();
                    };
                }),
                has: hb(function(a) {
                    return function(b) {
                        return fb(a, b).length > 0;
                    };
                }),
                contains: hb(function(a) {
                    return function(b) {
                        return (b.textContent || b.innerText || e(b)).indexOf(a) > -1;
                    };
                }),
                lang: hb(function(a) {
                    return W.test(a || "") || fb.error("unsupported lang: " + a), a = a.replace(cb, db).toLowerCase(), 
                    function(b) {
                        var c;
                        do {
                            if (c = p ? b.lang : b.getAttribute("xml:lang") || b.getAttribute("lang")) return (c = c.toLowerCase()) === a || 0 === c.indexOf(a + "-");
                        } while ((b = b.parentNode) && 1 === b.nodeType);
                        return !1;
                    };
                }),
                target: function(b) {
                    var c = a.location && a.location.hash;
                    return c && c.slice(1) === b.id;
                },
                root: function(a) {
                    return a === o;
                },
                focus: function(a) {
                    return a === n.activeElement && (!n.hasFocus || n.hasFocus()) && !!(a.type || a.href || ~a.tabIndex);
                },
                enabled: function(a) {
                    return !1 === a.disabled;
                },
                disabled: function(a) {
                    return !0 === a.disabled;
                },
                checked: function(a) {
                    var b = a.nodeName.toLowerCase();
                    return "input" === b && !!a.checked || "option" === b && !!a.selected;
                },
                selected: function(a) {
                    return a.parentNode && a.parentNode.selectedIndex, !0 === a.selected;
                },
                empty: function(a) {
                    for (a = a.firstChild; a; a = a.nextSibling) if (a.nodeType < 6) return !1;
                    return !0;
                },
                parent: function(a) {
                    return !d.pseudos.empty(a);
                },
                header: function(a) {
                    return Z.test(a.nodeName);
                },
                input: function(a) {
                    return Y.test(a.nodeName);
                },
                button: function(a) {
                    var b = a.nodeName.toLowerCase();
                    return "input" === b && "button" === a.type || "button" === b;
                },
                text: function(a) {
                    var b;
                    return "input" === a.nodeName.toLowerCase() && "text" === a.type && (null == (b = a.getAttribute("type")) || "text" === b.toLowerCase());
                },
                first: nb(function() {
                    return [ 0 ];
                }),
                last: nb(function(a, b) {
                    return [ b - 1 ];
                }),
                eq: nb(function(a, b, c) {
                    return [ 0 > c ? c + b : c ];
                }),
                even: nb(function(a, b) {
                    for (var c = 0; b > c; c += 2) a.push(c);
                    return a;
                }),
                odd: nb(function(a, b) {
                    for (var c = 1; b > c; c += 2) a.push(c);
                    return a;
                }),
                lt: nb(function(a, b, c) {
                    for (var d = 0 > c ? c + b : c; --d >= 0; ) a.push(d);
                    return a;
                }),
                gt: nb(function(a, b, c) {
                    for (var d = 0 > c ? c + b : c; ++d < b; ) a.push(d);
                    return a;
                })
            }
        }, d.pseudos.nth = d.pseudos.eq;
        for (b in {
            radio: !0,
            checkbox: !0,
            file: !0,
            password: !0,
            image: !0
        }) d.pseudos[b] = function(a) {
            return function(b) {
                return "input" === b.nodeName.toLowerCase() && b.type === a;
            };
        }(b);
        for (b in {
            submit: !0,
            reset: !0
        }) d.pseudos[b] = function(a) {
            return function(b) {
                var c = b.nodeName.toLowerCase();
                return ("input" === c || "button" === c) && b.type === a;
            };
        }(b);
        return pb.prototype = d.filters = d.pseudos, d.setFilters = new pb(), g = fb.tokenize = function(a, b) {
            var c, e, f, g, h, i, j, k = z[a + " "];
            if (k) return b ? 0 : k.slice(0);
            for (h = a, i = [], j = d.preFilter; h; ) {
                (!c || (e = S.exec(h))) && (e && (h = h.slice(e[0].length) || h), i.push(f = [])), 
                c = !1, (e = T.exec(h)) && (c = e.shift(), f.push({
                    value: c,
                    type: e[0].replace(R, " ")
                }), h = h.slice(c.length));
                for (g in d.filter) !(e = X[g].exec(h)) || j[g] && !(e = j[g](e)) || (c = e.shift(), 
                f.push({
                    value: c,
                    type: g,
                    matches: e
                }), h = h.slice(c.length));
                if (!c) break;
            }
            return b ? h.length : h ? fb.error(a) : z(a, i).slice(0);
        }, h = fb.compile = function(a, b) {
            var c, d = [], e = [], f = A[a + " "];
            if (!f) {
                for (b || (b = g(a)), c = b.length; c--; ) f = wb(b[c]), f[u] ? d.push(f) : e.push(f);
                f = A(a, xb(e, d)), f.selector = a;
            }
            return f;
        }, i = fb.select = function(a, b, e, f) {
            var i, j, k, l, m, n = "function" == typeof a && a, o = !f && g(a = n.selector || a);
            if (e = e || [], 1 === o.length) {
                if (j = o[0] = o[0].slice(0), j.length > 2 && "ID" === (k = j[0]).type && c.getById && 9 === b.nodeType && p && d.relative[j[1].type]) {
                    if (!(b = (d.find.ID(k.matches[0].replace(cb, db), b) || [])[0])) return e;
                    n && (b = b.parentNode), a = a.slice(j.shift().value.length);
                }
                for (i = X.needsContext.test(a) ? 0 : j.length; i-- && (k = j[i], !d.relative[l = k.type]); ) if ((m = d.find[l]) && (f = m(k.matches[0].replace(cb, db), ab.test(j[0].type) && ob(b.parentNode) || b))) {
                    if (j.splice(i, 1), !(a = f.length && qb(j))) return I.apply(e, f), e;
                    break;
                }
            }
            return (n || h(a, o))(f, b, !p, e, ab.test(a) && ob(b.parentNode) || b), e;
        }, c.sortStable = u.split("").sort(B).join("") === u, c.detectDuplicates = !!l, 
        m(), c.sortDetached = ib(function(a) {
            return 1 & a.compareDocumentPosition(n.createElement("div"));
        }), ib(function(a) {
            return a.innerHTML = "<a href='#'></a>", "#" === a.firstChild.getAttribute("href");
        }) || jb("type|href|height|width", function(a, b, c) {
            return c ? void 0 : a.getAttribute(b, "type" === b.toLowerCase() ? 1 : 2);
        }), c.attributes && ib(function(a) {
            return a.innerHTML = "<input/>", a.firstChild.setAttribute("value", ""), "" === a.firstChild.getAttribute("value");
        }) || jb("value", function(a, b, c) {
            return c || "input" !== a.nodeName.toLowerCase() ? void 0 : a.defaultValue;
        }), ib(function(a) {
            return null == a.getAttribute("disabled");
        }) || jb(L, function(a, b, c) {
            var d;
            return c ? void 0 : !0 === a[b] ? b.toLowerCase() : (d = a.getAttributeNode(b)) && d.specified ? d.value : null;
        }), fb;
    }(a);
    n.find = t, n.expr = t.selectors, n.expr[":"] = n.expr.pseudos, n.unique = t.uniqueSort, 
    n.text = t.getText, n.isXMLDoc = t.isXML, n.contains = t.contains;
    var u = n.expr.match.needsContext, v = /^<(\w+)\s*\/?>(?:<\/\1>|)$/, w = /^.[^:#\[\.,]*$/;
    n.filter = function(a, b, c) {
        var d = b[0];
        return c && (a = ":not(" + a + ")"), 1 === b.length && 1 === d.nodeType ? n.find.matchesSelector(d, a) ? [ d ] : [] : n.find.matches(a, n.grep(b, function(a) {
            return 1 === a.nodeType;
        }));
    }, n.fn.extend({
        find: function(a) {
            var b, c = this.length, d = [], e = this;
            if ("string" != typeof a) return this.pushStack(n(a).filter(function() {
                for (b = 0; c > b; b++) if (n.contains(e[b], this)) return !0;
            }));
            for (b = 0; c > b; b++) n.find(a, e[b], d);
            return d = this.pushStack(c > 1 ? n.unique(d) : d), d.selector = this.selector ? this.selector + " " + a : a, 
            d;
        },
        filter: function(a) {
            return this.pushStack(x(this, a || [], !1));
        },
        not: function(a) {
            return this.pushStack(x(this, a || [], !0));
        },
        is: function(a) {
            return !!x(this, "string" == typeof a && u.test(a) ? n(a) : a || [], !1).length;
        }
    });
    var y, z = /^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/;
    (n.fn.init = function(a, b) {
        var c, d;
        if (!a) return this;
        if ("string" == typeof a) {
            if (!(c = "<" === a[0] && ">" === a[a.length - 1] && a.length >= 3 ? [ null, a, null ] : z.exec(a)) || !c[1] && b) return !b || b.jquery ? (b || y).find(a) : this.constructor(b).find(a);
            if (c[1]) {
                if (b = b instanceof n ? b[0] : b, n.merge(this, n.parseHTML(c[1], b && b.nodeType ? b.ownerDocument || b : l, !0)), 
                v.test(c[1]) && n.isPlainObject(b)) for (c in b) n.isFunction(this[c]) ? this[c](b[c]) : this.attr(c, b[c]);
                return this;
            }
            return d = l.getElementById(c[2]), d && d.parentNode && (this.length = 1, this[0] = d), 
            this.context = l, this.selector = a, this;
        }
        return a.nodeType ? (this.context = this[0] = a, this.length = 1, this) : n.isFunction(a) ? void 0 !== y.ready ? y.ready(a) : a(n) : (void 0 !== a.selector && (this.selector = a.selector, 
        this.context = a.context), n.makeArray(a, this));
    }).prototype = n.fn, y = n(l);
    var B = /^(?:parents|prev(?:Until|All))/, C = {
        children: !0,
        contents: !0,
        next: !0,
        prev: !0
    };
    n.extend({
        dir: function(a, b, c) {
            for (var d = [], e = void 0 !== c; (a = a[b]) && 9 !== a.nodeType; ) if (1 === a.nodeType) {
                if (e && n(a).is(c)) break;
                d.push(a);
            }
            return d;
        },
        sibling: function(a, b) {
            for (var c = []; a; a = a.nextSibling) 1 === a.nodeType && a !== b && c.push(a);
            return c;
        }
    }), n.fn.extend({
        has: function(a) {
            var b = n(a, this), c = b.length;
            return this.filter(function() {
                for (var a = 0; c > a; a++) if (n.contains(this, b[a])) return !0;
            });
        },
        closest: function(a, b) {
            for (var c, d = 0, e = this.length, f = [], g = u.test(a) || "string" != typeof a ? n(a, b || this.context) : 0; e > d; d++) for (c = this[d]; c && c !== b; c = c.parentNode) if (c.nodeType < 11 && (g ? g.index(c) > -1 : 1 === c.nodeType && n.find.matchesSelector(c, a))) {
                f.push(c);
                break;
            }
            return this.pushStack(f.length > 1 ? n.unique(f) : f);
        },
        index: function(a) {
            return a ? "string" == typeof a ? g.call(n(a), this[0]) : g.call(this, a.jquery ? a[0] : a) : this[0] && this[0].parentNode ? this.first().prevAll().length : -1;
        },
        add: function(a, b) {
            return this.pushStack(n.unique(n.merge(this.get(), n(a, b))));
        },
        addBack: function(a) {
            return this.add(null == a ? this.prevObject : this.prevObject.filter(a));
        }
    }), n.each({
        parent: function(a) {
            var b = a.parentNode;
            return b && 11 !== b.nodeType ? b : null;
        },
        parents: function(a) {
            return n.dir(a, "parentNode");
        },
        parentsUntil: function(a, b, c) {
            return n.dir(a, "parentNode", c);
        },
        next: function(a) {
            return D(a, "nextSibling");
        },
        prev: function(a) {
            return D(a, "previousSibling");
        },
        nextAll: function(a) {
            return n.dir(a, "nextSibling");
        },
        prevAll: function(a) {
            return n.dir(a, "previousSibling");
        },
        nextUntil: function(a, b, c) {
            return n.dir(a, "nextSibling", c);
        },
        prevUntil: function(a, b, c) {
            return n.dir(a, "previousSibling", c);
        },
        siblings: function(a) {
            return n.sibling((a.parentNode || {}).firstChild, a);
        },
        children: function(a) {
            return n.sibling(a.firstChild);
        },
        contents: function(a) {
            return a.contentDocument || n.merge([], a.childNodes);
        }
    }, function(a, b) {
        n.fn[a] = function(c, d) {
            var e = n.map(this, b, c);
            return "Until" !== a.slice(-5) && (d = c), d && "string" == typeof d && (e = n.filter(d, e)), 
            this.length > 1 && (C[a] || n.unique(e), B.test(a) && e.reverse()), this.pushStack(e);
        };
    });
    var E = /\S+/g, F = {};
    n.Callbacks = function(a) {
        a = "string" == typeof a ? F[a] || G(a) : n.extend({}, a);
        var b, c, d, e, f, g, h = [], i = !a.once && [], j = function(l) {
            for (b = a.memory && l, c = !0, g = e || 0, e = 0, f = h.length, d = !0; h && f > g; g++) if (!1 === h[g].apply(l[0], l[1]) && a.stopOnFalse) {
                b = !1;
                break;
            }
            d = !1, h && (i ? i.length && j(i.shift()) : b ? h = [] : k.disable());
        }, k = {
            add: function() {
                if (h) {
                    var c = h.length;
                    !function g(b) {
                        n.each(b, function(b, c) {
                            var d = n.type(c);
                            "function" === d ? a.unique && k.has(c) || h.push(c) : c && c.length && "string" !== d && g(c);
                        });
                    }(arguments), d ? f = h.length : b && (e = c, j(b));
                }
                return this;
            },
            remove: function() {
                return h && n.each(arguments, function(a, b) {
                    for (var c; (c = n.inArray(b, h, c)) > -1; ) h.splice(c, 1), d && (f >= c && f--, 
                    g >= c && g--);
                }), this;
            },
            has: function(a) {
                return a ? n.inArray(a, h) > -1 : !(!h || !h.length);
            },
            empty: function() {
                return h = [], f = 0, this;
            },
            disable: function() {
                return h = i = b = void 0, this;
            },
            disabled: function() {
                return !h;
            },
            lock: function() {
                return i = void 0, b || k.disable(), this;
            },
            locked: function() {
                return !i;
            },
            fireWith: function(a, b) {
                return !h || c && !i || (b = b || [], b = [ a, b.slice ? b.slice() : b ], d ? i.push(b) : j(b)), 
                this;
            },
            fire: function() {
                return k.fireWith(this, arguments), this;
            },
            fired: function() {
                return !!c;
            }
        };
        return k;
    }, n.extend({
        Deferred: function(a) {
            var b = [ [ "resolve", "done", n.Callbacks("once memory"), "resolved" ], [ "reject", "fail", n.Callbacks("once memory"), "rejected" ], [ "notify", "progress", n.Callbacks("memory") ] ], c = "pending", d = {
                state: function() {
                    return c;
                },
                always: function() {
                    return e.done(arguments).fail(arguments), this;
                },
                then: function() {
                    var a = arguments;
                    return n.Deferred(function(c) {
                        n.each(b, function(b, f) {
                            var g = n.isFunction(a[b]) && a[b];
                            e[f[1]](function() {
                                var a = g && g.apply(this, arguments);
                                a && n.isFunction(a.promise) ? a.promise().done(c.resolve).fail(c.reject).progress(c.notify) : c[f[0] + "With"](this === d ? c.promise() : this, g ? [ a ] : arguments);
                            });
                        }), a = null;
                    }).promise();
                },
                promise: function(a) {
                    return null != a ? n.extend(a, d) : d;
                }
            }, e = {};
            return d.pipe = d.then, n.each(b, function(a, f) {
                var g = f[2], h = f[3];
                d[f[1]] = g.add, h && g.add(function() {
                    c = h;
                }, b[1 ^ a][2].disable, b[2][2].lock), e[f[0]] = function() {
                    return e[f[0] + "With"](this === e ? d : this, arguments), this;
                }, e[f[0] + "With"] = g.fireWith;
            }), d.promise(e), a && a.call(e, e), e;
        },
        when: function(a) {
            var i, j, k, b = 0, c = d.call(arguments), e = c.length, f = 1 !== e || a && n.isFunction(a.promise) ? e : 0, g = 1 === f ? a : n.Deferred(), h = function(a, b, c) {
                return function(e) {
                    b[a] = this, c[a] = arguments.length > 1 ? d.call(arguments) : e, c === i ? g.notifyWith(b, c) : --f || g.resolveWith(b, c);
                };
            };
            if (e > 1) for (i = new Array(e), j = new Array(e), k = new Array(e); e > b; b++) c[b] && n.isFunction(c[b].promise) ? c[b].promise().done(h(b, k, c)).fail(g.reject).progress(h(b, j, i)) : --f;
            return f || g.resolveWith(k, c), g.promise();
        }
    });
    var H;
    n.fn.ready = function(a) {
        return n.ready.promise().done(a), this;
    }, n.extend({
        isReady: !1,
        readyWait: 1,
        holdReady: function(a) {
            a ? n.readyWait++ : n.ready(!0);
        },
        ready: function(a) {
            (!0 === a ? --n.readyWait : n.isReady) || (n.isReady = !0, !0 !== a && --n.readyWait > 0 || (H.resolveWith(l, [ n ]), 
            n.fn.triggerHandler && (n(l).triggerHandler("ready"), n(l).off("ready"))));
        }
    }), n.ready.promise = function(b) {
        return H || (H = n.Deferred(), "complete" === l.readyState ? setTimeout(n.ready) : (l.addEventListener("DOMContentLoaded", I, !1), 
        a.addEventListener("load", I, !1))), H.promise(b);
    }, n.ready.promise();
    var J = n.access = function(a, b, c, d, e, f, g) {
        var h = 0, i = a.length, j = null == c;
        if ("object" === n.type(c)) {
            e = !0;
            for (h in c) n.access(a, b, h, c[h], !0, f, g);
        } else if (void 0 !== d && (e = !0, n.isFunction(d) || (g = !0), j && (g ? (b.call(a, d), 
        b = null) : (j = b, b = function(a, b, c) {
            return j.call(n(a), c);
        })), b)) for (;i > h; h++) b(a[h], c, g ? d : d.call(a[h], h, b(a[h], c)));
        return e ? a : j ? b.call(a) : i ? b(a[0], c) : f;
    };
    n.acceptData = function(a) {
        return 1 === a.nodeType || 9 === a.nodeType || !+a.nodeType;
    }, K.uid = 1, K.accepts = n.acceptData, K.prototype = {
        key: function(a) {
            if (!K.accepts(a)) return 0;
            var b = {}, c = a[this.expando];
            if (!c) {
                c = K.uid++;
                try {
                    b[this.expando] = {
                        value: c
                    }, Object.defineProperties(a, b);
                } catch (d) {
                    b[this.expando] = c, n.extend(a, b);
                }
            }
            return this.cache[c] || (this.cache[c] = {}), c;
        },
        set: function(a, b, c) {
            var d, e = this.key(a), f = this.cache[e];
            if ("string" == typeof b) f[b] = c; else if (n.isEmptyObject(f)) n.extend(this.cache[e], b); else for (d in b) f[d] = b[d];
            return f;
        },
        get: function(a, b) {
            var c = this.cache[this.key(a)];
            return void 0 === b ? c : c[b];
        },
        access: function(a, b, c) {
            var d;
            return void 0 === b || b && "string" == typeof b && void 0 === c ? (d = this.get(a, b), 
            void 0 !== d ? d : this.get(a, n.camelCase(b))) : (this.set(a, b, c), void 0 !== c ? c : b);
        },
        remove: function(a, b) {
            var c, d, e, f = this.key(a), g = this.cache[f];
            if (void 0 === b) this.cache[f] = {}; else {
                n.isArray(b) ? d = b.concat(b.map(n.camelCase)) : (e = n.camelCase(b), b in g ? d = [ b, e ] : (d = e, 
                d = d in g ? [ d ] : d.match(E) || [])), c = d.length;
                for (;c--; ) delete g[d[c]];
            }
        },
        hasData: function(a) {
            return !n.isEmptyObject(this.cache[a[this.expando]] || {});
        },
        discard: function(a) {
            a[this.expando] && delete this.cache[a[this.expando]];
        }
    };
    var L = new K(), M = new K(), N = /^(?:\{[\w\W]*\}|\[[\w\W]*\])$/, O = /([A-Z])/g;
    n.extend({
        hasData: function(a) {
            return M.hasData(a) || L.hasData(a);
        },
        data: function(a, b, c) {
            return M.access(a, b, c);
        },
        removeData: function(a, b) {
            M.remove(a, b);
        },
        _data: function(a, b, c) {
            return L.access(a, b, c);
        },
        _removeData: function(a, b) {
            L.remove(a, b);
        }
    }), n.fn.extend({
        data: function(a, b) {
            var c, d, e, f = this[0], g = f && f.attributes;
            if (void 0 === a) {
                if (this.length && (e = M.get(f), 1 === f.nodeType && !L.get(f, "hasDataAttrs"))) {
                    for (c = g.length; c--; ) g[c] && (d = g[c].name, 0 === d.indexOf("data-") && (d = n.camelCase(d.slice(5)), 
                    P(f, d, e[d])));
                    L.set(f, "hasDataAttrs", !0);
                }
                return e;
            }
            return "object" == typeof a ? this.each(function() {
                M.set(this, a);
            }) : J(this, function(b) {
                var c, d = n.camelCase(a);
                if (f && void 0 === b) {
                    if (void 0 !== (c = M.get(f, a))) return c;
                    if (void 0 !== (c = M.get(f, d))) return c;
                    if (void 0 !== (c = P(f, d, void 0))) return c;
                } else this.each(function() {
                    var c = M.get(this, d);
                    M.set(this, d, b), -1 !== a.indexOf("-") && void 0 !== c && M.set(this, a, b);
                });
            }, null, b, arguments.length > 1, null, !0);
        },
        removeData: function(a) {
            return this.each(function() {
                M.remove(this, a);
            });
        }
    }), n.extend({
        queue: function(a, b, c) {
            var d;
            return a ? (b = (b || "fx") + "queue", d = L.get(a, b), c && (!d || n.isArray(c) ? d = L.access(a, b, n.makeArray(c)) : d.push(c)), 
            d || []) : void 0;
        },
        dequeue: function(a, b) {
            b = b || "fx";
            var c = n.queue(a, b), d = c.length, e = c.shift(), f = n._queueHooks(a, b), g = function() {
                n.dequeue(a, b);
            };
            "inprogress" === e && (e = c.shift(), d--), e && ("fx" === b && c.unshift("inprogress"), 
            delete f.stop, e.call(a, g, f)), !d && f && f.empty.fire();
        },
        _queueHooks: function(a, b) {
            var c = b + "queueHooks";
            return L.get(a, c) || L.access(a, c, {
                empty: n.Callbacks("once memory").add(function() {
                    L.remove(a, [ b + "queue", c ]);
                })
            });
        }
    }), n.fn.extend({
        queue: function(a, b) {
            var c = 2;
            return "string" != typeof a && (b = a, a = "fx", c--), arguments.length < c ? n.queue(this[0], a) : void 0 === b ? this : this.each(function() {
                var c = n.queue(this, a, b);
                n._queueHooks(this, a), "fx" === a && "inprogress" !== c[0] && n.dequeue(this, a);
            });
        },
        dequeue: function(a) {
            return this.each(function() {
                n.dequeue(this, a);
            });
        },
        clearQueue: function(a) {
            return this.queue(a || "fx", []);
        },
        promise: function(a, b) {
            var c, d = 1, e = n.Deferred(), f = this, g = this.length, h = function() {
                --d || e.resolveWith(f, [ f ]);
            };
            for ("string" != typeof a && (b = a, a = void 0), a = a || "fx"; g--; ) (c = L.get(f[g], a + "queueHooks")) && c.empty && (d++, 
            c.empty.add(h));
            return h(), e.promise(b);
        }
    });
    var Q = /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source, R = [ "Top", "Right", "Bottom", "Left" ], S = function(a, b) {
        return a = b || a, "none" === n.css(a, "display") || !n.contains(a.ownerDocument, a);
    }, T = /^(?:checkbox|radio)$/i;
    !function() {
        var a = l.createDocumentFragment(), b = a.appendChild(l.createElement("div")), c = l.createElement("input");
        c.setAttribute("type", "radio"), c.setAttribute("checked", "checked"), c.setAttribute("name", "t"), 
        b.appendChild(c), k.checkClone = b.cloneNode(!0).cloneNode(!0).lastChild.checked, 
        b.innerHTML = "<textarea>x</textarea>", k.noCloneChecked = !!b.cloneNode(!0).lastChild.defaultValue;
    }();
    var U = "undefined";
    k.focusinBubbles = "onfocusin" in a;
    var V = /^key/, W = /^(?:mouse|pointer|contextmenu)|click/, X = /^(?:focusinfocus|focusoutblur)$/, Y = /^([^.]*)(?:\.(.+)|)$/;
    n.event = {
        global: {},
        add: function(a, b, c, d, e) {
            var f, g, h, i, j, k, l, m, o, p, q, r = L.get(a);
            if (r) for (c.handler && (f = c, c = f.handler, e = f.selector), c.guid || (c.guid = n.guid++), 
            (i = r.events) || (i = r.events = {}), (g = r.handle) || (g = r.handle = function(b) {
                return typeof n !== U && n.event.triggered !== b.type ? n.event.dispatch.apply(a, arguments) : void 0;
            }), b = (b || "").match(E) || [ "" ], j = b.length; j--; ) h = Y.exec(b[j]) || [], 
            o = q = h[1], p = (h[2] || "").split(".").sort(), o && (l = n.event.special[o] || {}, 
            o = (e ? l.delegateType : l.bindType) || o, l = n.event.special[o] || {}, k = n.extend({
                type: o,
                origType: q,
                data: d,
                handler: c,
                guid: c.guid,
                selector: e,
                needsContext: e && n.expr.match.needsContext.test(e),
                namespace: p.join(".")
            }, f), (m = i[o]) || (m = i[o] = [], m.delegateCount = 0, l.setup && !1 !== l.setup.call(a, d, p, g) || a.addEventListener && a.addEventListener(o, g, !1)), 
            l.add && (l.add.call(a, k), k.handler.guid || (k.handler.guid = c.guid)), e ? m.splice(m.delegateCount++, 0, k) : m.push(k), 
            n.event.global[o] = !0);
        },
        remove: function(a, b, c, d, e) {
            var f, g, h, i, j, k, l, m, o, p, q, r = L.hasData(a) && L.get(a);
            if (r && (i = r.events)) {
                for (b = (b || "").match(E) || [ "" ], j = b.length; j--; ) if (h = Y.exec(b[j]) || [], 
                o = q = h[1], p = (h[2] || "").split(".").sort(), o) {
                    for (l = n.event.special[o] || {}, o = (d ? l.delegateType : l.bindType) || o, m = i[o] || [], 
                    h = h[2] && new RegExp("(^|\\.)" + p.join("\\.(?:.*\\.|)") + "(\\.|$)"), g = f = m.length; f--; ) k = m[f], 
                    !e && q !== k.origType || c && c.guid !== k.guid || h && !h.test(k.namespace) || d && d !== k.selector && ("**" !== d || !k.selector) || (m.splice(f, 1), 
                    k.selector && m.delegateCount--, l.remove && l.remove.call(a, k));
                    g && !m.length && (l.teardown && !1 !== l.teardown.call(a, p, r.handle) || n.removeEvent(a, o, r.handle), 
                    delete i[o]);
                } else for (o in i) n.event.remove(a, o + b[j], c, d, !0);
                n.isEmptyObject(i) && (delete r.handle, L.remove(a, "events"));
            }
        },
        trigger: function(b, c, d, e) {
            var f, g, h, i, k, m, o, p = [ d || l ], q = j.call(b, "type") ? b.type : b, r = j.call(b, "namespace") ? b.namespace.split(".") : [];
            if (g = h = d = d || l, 3 !== d.nodeType && 8 !== d.nodeType && !X.test(q + n.event.triggered) && (q.indexOf(".") >= 0 && (r = q.split("."), 
            q = r.shift(), r.sort()), k = q.indexOf(":") < 0 && "on" + q, b = b[n.expando] ? b : new n.Event(q, "object" == typeof b && b), 
            b.isTrigger = e ? 2 : 3, b.namespace = r.join("."), b.namespace_re = b.namespace ? new RegExp("(^|\\.)" + r.join("\\.(?:.*\\.|)") + "(\\.|$)") : null, 
            b.result = void 0, b.target || (b.target = d), c = null == c ? [ b ] : n.makeArray(c, [ b ]), 
            o = n.event.special[q] || {}, e || !o.trigger || !1 !== o.trigger.apply(d, c))) {
                if (!e && !o.noBubble && !n.isWindow(d)) {
                    for (i = o.delegateType || q, X.test(i + q) || (g = g.parentNode); g; g = g.parentNode) p.push(g), 
                    h = g;
                    h === (d.ownerDocument || l) && p.push(h.defaultView || h.parentWindow || a);
                }
                for (f = 0; (g = p[f++]) && !b.isPropagationStopped(); ) b.type = f > 1 ? i : o.bindType || q, 
                m = (L.get(g, "events") || {})[b.type] && L.get(g, "handle"), m && m.apply(g, c), 
                (m = k && g[k]) && m.apply && n.acceptData(g) && (b.result = m.apply(g, c), !1 === b.result && b.preventDefault());
                return b.type = q, e || b.isDefaultPrevented() || o._default && !1 !== o._default.apply(p.pop(), c) || !n.acceptData(d) || k && n.isFunction(d[q]) && !n.isWindow(d) && (h = d[k], 
                h && (d[k] = null), n.event.triggered = q, d[q](), n.event.triggered = void 0, h && (d[k] = h)), 
                b.result;
            }
        },
        dispatch: function(a) {
            a = n.event.fix(a);
            var b, c, e, f, g, h = [], i = d.call(arguments), j = (L.get(this, "events") || {})[a.type] || [], k = n.event.special[a.type] || {};
            if (i[0] = a, a.delegateTarget = this, !k.preDispatch || !1 !== k.preDispatch.call(this, a)) {
                for (h = n.event.handlers.call(this, a, j), b = 0; (f = h[b++]) && !a.isPropagationStopped(); ) for (a.currentTarget = f.elem, 
                c = 0; (g = f.handlers[c++]) && !a.isImmediatePropagationStopped(); ) (!a.namespace_re || a.namespace_re.test(g.namespace)) && (a.handleObj = g, 
                a.data = g.data, void 0 !== (e = ((n.event.special[g.origType] || {}).handle || g.handler).apply(f.elem, i)) && !1 === (a.result = e) && (a.preventDefault(), 
                a.stopPropagation()));
                return k.postDispatch && k.postDispatch.call(this, a), a.result;
            }
        },
        handlers: function(a, b) {
            var c, d, e, f, g = [], h = b.delegateCount, i = a.target;
            if (h && i.nodeType && (!a.button || "click" !== a.type)) for (;i !== this; i = i.parentNode || this) if (!0 !== i.disabled || "click" !== a.type) {
                for (d = [], c = 0; h > c; c++) f = b[c], e = f.selector + " ", void 0 === d[e] && (d[e] = f.needsContext ? n(e, this).index(i) >= 0 : n.find(e, this, null, [ i ]).length), 
                d[e] && d.push(f);
                d.length && g.push({
                    elem: i,
                    handlers: d
                });
            }
            return h < b.length && g.push({
                elem: this,
                handlers: b.slice(h)
            }), g;
        },
        props: "altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
        fixHooks: {},
        keyHooks: {
            props: "char charCode key keyCode".split(" "),
            filter: function(a, b) {
                return null == a.which && (a.which = null != b.charCode ? b.charCode : b.keyCode), 
                a;
            }
        },
        mouseHooks: {
            props: "button buttons clientX clientY offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
            filter: function(a, b) {
                var c, d, e, f = b.button;
                return null == a.pageX && null != b.clientX && (c = a.target.ownerDocument || l, 
                d = c.documentElement, e = c.body, a.pageX = b.clientX + (d && d.scrollLeft || e && e.scrollLeft || 0) - (d && d.clientLeft || e && e.clientLeft || 0), 
                a.pageY = b.clientY + (d && d.scrollTop || e && e.scrollTop || 0) - (d && d.clientTop || e && e.clientTop || 0)), 
                a.which || void 0 === f || (a.which = 1 & f ? 1 : 2 & f ? 3 : 4 & f ? 2 : 0), a;
            }
        },
        fix: function(a) {
            if (a[n.expando]) return a;
            var b, c, d, e = a.type, f = a, g = this.fixHooks[e];
            for (g || (this.fixHooks[e] = g = W.test(e) ? this.mouseHooks : V.test(e) ? this.keyHooks : {}), 
            d = g.props ? this.props.concat(g.props) : this.props, a = new n.Event(f), b = d.length; b--; ) c = d[b], 
            a[c] = f[c];
            return a.target || (a.target = l), 3 === a.target.nodeType && (a.target = a.target.parentNode), 
            g.filter ? g.filter(a, f) : a;
        },
        special: {
            load: {
                noBubble: !0
            },
            focus: {
                trigger: function() {
                    return this !== _() && this.focus ? (this.focus(), !1) : void 0;
                },
                delegateType: "focusin"
            },
            blur: {
                trigger: function() {
                    return this === _() && this.blur ? (this.blur(), !1) : void 0;
                },
                delegateType: "focusout"
            },
            click: {
                trigger: function() {
                    return "checkbox" === this.type && this.click && n.nodeName(this, "input") ? (this.click(), 
                    !1) : void 0;
                },
                _default: function(a) {
                    return n.nodeName(a.target, "a");
                }
            },
            beforeunload: {
                postDispatch: function(a) {
                    void 0 !== a.result && a.originalEvent && (a.originalEvent.returnValue = a.result);
                }
            }
        },
        simulate: function(a, b, c, d) {
            var e = n.extend(new n.Event(), c, {
                type: a,
                isSimulated: !0,
                originalEvent: {}
            });
            d ? n.event.trigger(e, null, b) : n.event.dispatch.call(b, e), e.isDefaultPrevented() && c.preventDefault();
        }
    }, n.removeEvent = function(a, b, c) {
        a.removeEventListener && a.removeEventListener(b, c, !1);
    }, n.Event = function(a, b) {
        return this instanceof n.Event ? (a && a.type ? (this.originalEvent = a, this.type = a.type, 
        this.isDefaultPrevented = a.defaultPrevented || void 0 === a.defaultPrevented && !1 === a.returnValue ? Z : $) : this.type = a, 
        b && n.extend(this, b), this.timeStamp = a && a.timeStamp || n.now(), void (this[n.expando] = !0)) : new n.Event(a, b);
    }, n.Event.prototype = {
        isDefaultPrevented: $,
        isPropagationStopped: $,
        isImmediatePropagationStopped: $,
        preventDefault: function() {
            var a = this.originalEvent;
            this.isDefaultPrevented = Z, a && a.preventDefault && a.preventDefault();
        },
        stopPropagation: function() {
            var a = this.originalEvent;
            this.isPropagationStopped = Z, a && a.stopPropagation && a.stopPropagation();
        },
        stopImmediatePropagation: function() {
            var a = this.originalEvent;
            this.isImmediatePropagationStopped = Z, a && a.stopImmediatePropagation && a.stopImmediatePropagation(), 
            this.stopPropagation();
        }
    }, n.each({
        mouseenter: "mouseover",
        mouseleave: "mouseout",
        pointerenter: "pointerover",
        pointerleave: "pointerout"
    }, function(a, b) {
        n.event.special[a] = {
            delegateType: b,
            bindType: b,
            handle: function(a) {
                var c, d = this, e = a.relatedTarget, f = a.handleObj;
                return (!e || e !== d && !n.contains(d, e)) && (a.type = f.origType, c = f.handler.apply(this, arguments), 
                a.type = b), c;
            }
        };
    }), k.focusinBubbles || n.each({
        focus: "focusin",
        blur: "focusout"
    }, function(a, b) {
        var c = function(a) {
            n.event.simulate(b, a.target, n.event.fix(a), !0);
        };
        n.event.special[b] = {
            setup: function() {
                var d = this.ownerDocument || this, e = L.access(d, b);
                e || d.addEventListener(a, c, !0), L.access(d, b, (e || 0) + 1);
            },
            teardown: function() {
                var d = this.ownerDocument || this, e = L.access(d, b) - 1;
                e ? L.access(d, b, e) : (d.removeEventListener(a, c, !0), L.remove(d, b));
            }
        };
    }), n.fn.extend({
        on: function(a, b, c, d, e) {
            var f, g;
            if ("object" == typeof a) {
                "string" != typeof b && (c = c || b, b = void 0);
                for (g in a) this.on(g, b, c, a[g], e);
                return this;
            }
            if (null == c && null == d ? (d = b, c = b = void 0) : null == d && ("string" == typeof b ? (d = c, 
            c = void 0) : (d = c, c = b, b = void 0)), !1 === d) d = $; else if (!d) return this;
            return 1 === e && (f = d, d = function(a) {
                return n().off(a), f.apply(this, arguments);
            }, d.guid = f.guid || (f.guid = n.guid++)), this.each(function() {
                n.event.add(this, a, d, c, b);
            });
        },
        one: function(a, b, c, d) {
            return this.on(a, b, c, d, 1);
        },
        off: function(a, b, c) {
            var d, e;
            if (a && a.preventDefault && a.handleObj) return d = a.handleObj, n(a.delegateTarget).off(d.namespace ? d.origType + "." + d.namespace : d.origType, d.selector, d.handler), 
            this;
            if ("object" == typeof a) {
                for (e in a) this.off(e, b, a[e]);
                return this;
            }
            return (!1 === b || "function" == typeof b) && (c = b, b = void 0), !1 === c && (c = $), 
            this.each(function() {
                n.event.remove(this, a, c, b);
            });
        },
        trigger: function(a, b) {
            return this.each(function() {
                n.event.trigger(a, b, this);
            });
        },
        triggerHandler: function(a, b) {
            var c = this[0];
            return c ? n.event.trigger(a, b, c, !0) : void 0;
        }
    });
    var ab = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/gi, bb = /<([\w:]+)/, cb = /<|&#?\w+;/, db = /<(?:script|style|link)/i, eb = /checked\s*(?:[^=]|=\s*.checked.)/i, fb = /^$|\/(?:java|ecma)script/i, gb = /^true\/(.*)/, hb = /^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g, ib = {
        option: [ 1, "<select multiple='multiple'>", "</select>" ],
        thead: [ 1, "<table>", "</table>" ],
        col: [ 2, "<table><colgroup>", "</colgroup></table>" ],
        tr: [ 2, "<table><tbody>", "</tbody></table>" ],
        td: [ 3, "<table><tbody><tr>", "</tr></tbody></table>" ],
        _default: [ 0, "", "" ]
    };
    ib.optgroup = ib.option, ib.tbody = ib.tfoot = ib.colgroup = ib.caption = ib.thead, 
    ib.th = ib.td, n.extend({
        clone: function(a, b, c) {
            var d, e, f, g, h = a.cloneNode(!0), i = n.contains(a.ownerDocument, a);
            if (!(k.noCloneChecked || 1 !== a.nodeType && 11 !== a.nodeType || n.isXMLDoc(a))) for (g = ob(h), 
            f = ob(a), d = 0, e = f.length; e > d; d++) pb(f[d], g[d]);
            if (b) if (c) for (f = f || ob(a), g = g || ob(h), d = 0, e = f.length; e > d; d++) nb(f[d], g[d]); else nb(a, h);
            return g = ob(h, "script"), g.length > 0 && mb(g, !i && ob(a, "script")), h;
        },
        buildFragment: function(a, b, c, d) {
            for (var e, f, g, h, i, j, k = b.createDocumentFragment(), l = [], m = 0, o = a.length; o > m; m++) if ((e = a[m]) || 0 === e) if ("object" === n.type(e)) n.merge(l, e.nodeType ? [ e ] : e); else if (cb.test(e)) {
                for (f = f || k.appendChild(b.createElement("div")), g = (bb.exec(e) || [ "", "" ])[1].toLowerCase(), 
                h = ib[g] || ib._default, f.innerHTML = h[1] + e.replace(ab, "<$1></$2>") + h[2], 
                j = h[0]; j--; ) f = f.lastChild;
                n.merge(l, f.childNodes), f = k.firstChild, f.textContent = "";
            } else l.push(b.createTextNode(e));
            for (k.textContent = "", m = 0; e = l[m++]; ) if ((!d || -1 === n.inArray(e, d)) && (i = n.contains(e.ownerDocument, e), 
            f = ob(k.appendChild(e), "script"), i && mb(f), c)) for (j = 0; e = f[j++]; ) fb.test(e.type || "") && c.push(e);
            return k;
        },
        cleanData: function(a) {
            for (var b, c, d, e, f = n.event.special, g = 0; void 0 !== (c = a[g]); g++) {
                if (n.acceptData(c) && (e = c[L.expando]) && (b = L.cache[e])) {
                    if (b.events) for (d in b.events) f[d] ? n.event.remove(c, d) : n.removeEvent(c, d, b.handle);
                    L.cache[e] && delete L.cache[e];
                }
                delete M.cache[c[M.expando]];
            }
        }
    }), n.fn.extend({
        text: function(a) {
            return J(this, function(a) {
                return void 0 === a ? n.text(this) : this.empty().each(function() {
                    (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) && (this.textContent = a);
                });
            }, null, a, arguments.length);
        },
        append: function() {
            return this.domManip(arguments, function(a) {
                if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
                    jb(this, a).appendChild(a);
                }
            });
        },
        prepend: function() {
            return this.domManip(arguments, function(a) {
                if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
                    var b = jb(this, a);
                    b.insertBefore(a, b.firstChild);
                }
            });
        },
        before: function() {
            return this.domManip(arguments, function(a) {
                this.parentNode && this.parentNode.insertBefore(a, this);
            });
        },
        after: function() {
            return this.domManip(arguments, function(a) {
                this.parentNode && this.parentNode.insertBefore(a, this.nextSibling);
            });
        },
        remove: function(a, b) {
            for (var c, d = a ? n.filter(a, this) : this, e = 0; null != (c = d[e]); e++) b || 1 !== c.nodeType || n.cleanData(ob(c)), 
            c.parentNode && (b && n.contains(c.ownerDocument, c) && mb(ob(c, "script")), c.parentNode.removeChild(c));
            return this;
        },
        empty: function() {
            for (var a, b = 0; null != (a = this[b]); b++) 1 === a.nodeType && (n.cleanData(ob(a, !1)), 
            a.textContent = "");
            return this;
        },
        clone: function(a, b) {
            return a = null != a && a, b = null == b ? a : b, this.map(function() {
                return n.clone(this, a, b);
            });
        },
        html: function(a) {
            return J(this, function(a) {
                var b = this[0] || {}, c = 0, d = this.length;
                if (void 0 === a && 1 === b.nodeType) return b.innerHTML;
                if ("string" == typeof a && !db.test(a) && !ib[(bb.exec(a) || [ "", "" ])[1].toLowerCase()]) {
                    a = a.replace(ab, "<$1></$2>");
                    try {
                        for (;d > c; c++) b = this[c] || {}, 1 === b.nodeType && (n.cleanData(ob(b, !1)), 
                        b.innerHTML = a);
                        b = 0;
                    } catch (e) {}
                }
                b && this.empty().append(a);
            }, null, a, arguments.length);
        },
        replaceWith: function() {
            var a = arguments[0];
            return this.domManip(arguments, function(b) {
                a = this.parentNode, n.cleanData(ob(this)), a && a.replaceChild(b, this);
            }), a && (a.length || a.nodeType) ? this : this.remove();
        },
        detach: function(a) {
            return this.remove(a, !0);
        },
        domManip: function(a, b) {
            a = e.apply([], a);
            var c, d, f, g, h, i, j = 0, l = this.length, m = this, o = l - 1, p = a[0], q = n.isFunction(p);
            if (q || l > 1 && "string" == typeof p && !k.checkClone && eb.test(p)) return this.each(function(c) {
                var d = m.eq(c);
                q && (a[0] = p.call(this, c, d.html())), d.domManip(a, b);
            });
            if (l && (c = n.buildFragment(a, this[0].ownerDocument, !1, this), d = c.firstChild, 
            1 === c.childNodes.length && (c = d), d)) {
                for (f = n.map(ob(c, "script"), kb), g = f.length; l > j; j++) h = c, j !== o && (h = n.clone(h, !0, !0), 
                g && n.merge(f, ob(h, "script"))), b.call(this[j], h, j);
                if (g) for (i = f[f.length - 1].ownerDocument, n.map(f, lb), j = 0; g > j; j++) h = f[j], 
                fb.test(h.type || "") && !L.access(h, "globalEval") && n.contains(i, h) && (h.src ? n._evalUrl && n._evalUrl(h.src) : n.globalEval(h.textContent.replace(hb, "")));
            }
            return this;
        }
    }), n.each({
        appendTo: "append",
        prependTo: "prepend",
        insertBefore: "before",
        insertAfter: "after",
        replaceAll: "replaceWith"
    }, function(a, b) {
        n.fn[a] = function(a) {
            for (var c, d = [], e = n(a), g = e.length - 1, h = 0; g >= h; h++) c = h === g ? this : this.clone(!0), 
            n(e[h])[b](c), f.apply(d, c.get());
            return this.pushStack(d);
        };
    });
    var qb, rb = {}, ub = /^margin/, vb = new RegExp("^(" + Q + ")(?!px)[a-z%]+$", "i"), wb = function(a) {
        return a.ownerDocument.defaultView.getComputedStyle(a, null);
    };
    !function() {
        function g() {
            f.style.cssText = "-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;display:block;margin-top:1%;top:1%;border:1px;padding:1px;width:4px;position:absolute", 
            f.innerHTML = "", d.appendChild(e);
            var g = a.getComputedStyle(f, null);
            b = "1%" !== g.top, c = "4px" === g.width, d.removeChild(e);
        }
        var b, c, d = l.documentElement, e = l.createElement("div"), f = l.createElement("div");
        f.style && (f.style.backgroundClip = "content-box", f.cloneNode(!0).style.backgroundClip = "", 
        k.clearCloneStyle = "content-box" === f.style.backgroundClip, e.style.cssText = "border:0;width:0;height:0;top:0;left:-9999px;margin-top:1px;position:absolute", 
        e.appendChild(f), a.getComputedStyle && n.extend(k, {
            pixelPosition: function() {
                return g(), b;
            },
            boxSizingReliable: function() {
                return null == c && g(), c;
            },
            reliableMarginRight: function() {
                var b, c = f.appendChild(l.createElement("div"));
                return c.style.cssText = f.style.cssText = "-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:0", 
                c.style.marginRight = c.style.width = "0", f.style.width = "1px", d.appendChild(e), 
                b = !parseFloat(a.getComputedStyle(c, null).marginRight), d.removeChild(e), b;
            }
        }));
    }(), n.swap = function(a, b, c, d) {
        var e, f, g = {};
        for (f in b) g[f] = a.style[f], a.style[f] = b[f];
        e = c.apply(a, d || []);
        for (f in b) a.style[f] = g[f];
        return e;
    };
    var zb = /^(none|table(?!-c[ea]).+)/, Ab = new RegExp("^(" + Q + ")(.*)$", "i"), Bb = new RegExp("^([+-])=(" + Q + ")", "i"), Cb = {
        position: "absolute",
        visibility: "hidden",
        display: "block"
    }, Db = {
        letterSpacing: "0",
        fontWeight: "400"
    }, Eb = [ "Webkit", "O", "Moz", "ms" ];
    n.extend({
        cssHooks: {
            opacity: {
                get: function(a, b) {
                    if (b) {
                        var c = xb(a, "opacity");
                        return "" === c ? "1" : c;
                    }
                }
            }
        },
        cssNumber: {
            columnCount: !0,
            fillOpacity: !0,
            flexGrow: !0,
            flexShrink: !0,
            fontWeight: !0,
            lineHeight: !0,
            opacity: !0,
            order: !0,
            orphans: !0,
            widows: !0,
            zIndex: !0,
            zoom: !0
        },
        cssProps: {
            float: "cssFloat"
        },
        style: function(a, b, c, d) {
            if (a && 3 !== a.nodeType && 8 !== a.nodeType && a.style) {
                var e, f, g, h = n.camelCase(b), i = a.style;
                return b = n.cssProps[h] || (n.cssProps[h] = Fb(i, h)), g = n.cssHooks[b] || n.cssHooks[h], 
                void 0 === c ? g && "get" in g && void 0 !== (e = g.get(a, !1, d)) ? e : i[b] : (f = typeof c, 
                "string" === f && (e = Bb.exec(c)) && (c = (e[1] + 1) * e[2] + parseFloat(n.css(a, b)), 
                f = "number"), void (null != c && c === c && ("number" !== f || n.cssNumber[h] || (c += "px"), 
                k.clearCloneStyle || "" !== c || 0 !== b.indexOf("background") || (i[b] = "inherit"), 
                g && "set" in g && void 0 === (c = g.set(a, c, d)) || (i[b] = c))));
            }
        },
        css: function(a, b, c, d) {
            var e, f, g, h = n.camelCase(b);
            return b = n.cssProps[h] || (n.cssProps[h] = Fb(a.style, h)), g = n.cssHooks[b] || n.cssHooks[h], 
            g && "get" in g && (e = g.get(a, !0, c)), void 0 === e && (e = xb(a, b, d)), "normal" === e && b in Db && (e = Db[b]), 
            "" === c || c ? (f = parseFloat(e), !0 === c || n.isNumeric(f) ? f || 0 : e) : e;
        }
    }), n.each([ "height", "width" ], function(a, b) {
        n.cssHooks[b] = {
            get: function(a, c, d) {
                return c ? zb.test(n.css(a, "display")) && 0 === a.offsetWidth ? n.swap(a, Cb, function() {
                    return Ib(a, b, d);
                }) : Ib(a, b, d) : void 0;
            },
            set: function(a, c, d) {
                var e = d && wb(a);
                return Gb(a, c, d ? Hb(a, b, d, "border-box" === n.css(a, "boxSizing", !1, e), e) : 0);
            }
        };
    }), n.cssHooks.marginRight = yb(k.reliableMarginRight, function(a, b) {
        return b ? n.swap(a, {
            display: "inline-block"
        }, xb, [ a, "marginRight" ]) : void 0;
    }), n.each({
        margin: "",
        padding: "",
        border: "Width"
    }, function(a, b) {
        n.cssHooks[a + b] = {
            expand: function(c) {
                for (var d = 0, e = {}, f = "string" == typeof c ? c.split(" ") : [ c ]; 4 > d; d++) e[a + R[d] + b] = f[d] || f[d - 2] || f[0];
                return e;
            }
        }, ub.test(a) || (n.cssHooks[a + b].set = Gb);
    }), n.fn.extend({
        css: function(a, b) {
            return J(this, function(a, b, c) {
                var d, e, f = {}, g = 0;
                if (n.isArray(b)) {
                    for (d = wb(a), e = b.length; e > g; g++) f[b[g]] = n.css(a, b[g], !1, d);
                    return f;
                }
                return void 0 !== c ? n.style(a, b, c) : n.css(a, b);
            }, a, b, arguments.length > 1);
        },
        show: function() {
            return Jb(this, !0);
        },
        hide: function() {
            return Jb(this);
        },
        toggle: function(a) {
            return "boolean" == typeof a ? a ? this.show() : this.hide() : this.each(function() {
                S(this) ? n(this).show() : n(this).hide();
            });
        }
    }), n.Tween = Kb, Kb.prototype = {
        constructor: Kb,
        init: function(a, b, c, d, e, f) {
            this.elem = a, this.prop = c, this.easing = e || "swing", this.options = b, this.start = this.now = this.cur(), 
            this.end = d, this.unit = f || (n.cssNumber[c] ? "" : "px");
        },
        cur: function() {
            var a = Kb.propHooks[this.prop];
            return a && a.get ? a.get(this) : Kb.propHooks._default.get(this);
        },
        run: function(a) {
            var b, c = Kb.propHooks[this.prop];
            return this.pos = b = this.options.duration ? n.easing[this.easing](a, this.options.duration * a, 0, 1, this.options.duration) : a, 
            this.now = (this.end - this.start) * b + this.start, this.options.step && this.options.step.call(this.elem, this.now, this), 
            c && c.set ? c.set(this) : Kb.propHooks._default.set(this), this;
        }
    }, Kb.prototype.init.prototype = Kb.prototype, Kb.propHooks = {
        _default: {
            get: function(a) {
                var b;
                return null == a.elem[a.prop] || a.elem.style && null != a.elem.style[a.prop] ? (b = n.css(a.elem, a.prop, ""), 
                b && "auto" !== b ? b : 0) : a.elem[a.prop];
            },
            set: function(a) {
                n.fx.step[a.prop] ? n.fx.step[a.prop](a) : a.elem.style && (null != a.elem.style[n.cssProps[a.prop]] || n.cssHooks[a.prop]) ? n.style(a.elem, a.prop, a.now + a.unit) : a.elem[a.prop] = a.now;
            }
        }
    }, Kb.propHooks.scrollTop = Kb.propHooks.scrollLeft = {
        set: function(a) {
            a.elem.nodeType && a.elem.parentNode && (a.elem[a.prop] = a.now);
        }
    }, n.easing = {
        linear: function(a) {
            return a;
        },
        swing: function(a) {
            return .5 - Math.cos(a * Math.PI) / 2;
        }
    }, n.fx = Kb.prototype.init, n.fx.step = {};
    var Lb, Mb, Nb = /^(?:toggle|show|hide)$/, Ob = new RegExp("^(?:([+-])=|)(" + Q + ")([a-z%]*)$", "i"), Pb = /queueHooks$/, Qb = [ Vb ], Rb = {
        "*": [ function(a, b) {
            var c = this.createTween(a, b), d = c.cur(), e = Ob.exec(b), f = e && e[3] || (n.cssNumber[a] ? "" : "px"), g = (n.cssNumber[a] || "px" !== f && +d) && Ob.exec(n.css(c.elem, a)), h = 1, i = 20;
            if (g && g[3] !== f) {
                f = f || g[3], e = e || [], g = +d || 1;
                do {
                    h = h || ".5", g /= h, n.style(c.elem, a, g + f);
                } while (h !== (h = c.cur() / d) && 1 !== h && --i);
            }
            return e && (g = c.start = +g || +d || 0, c.unit = f, c.end = e[1] ? g + (e[1] + 1) * e[2] : +e[2]), 
            c;
        } ]
    };
    n.Animation = n.extend(Xb, {
        tweener: function(a, b) {
            n.isFunction(a) ? (b = a, a = [ "*" ]) : a = a.split(" ");
            for (var c, d = 0, e = a.length; e > d; d++) c = a[d], Rb[c] = Rb[c] || [], Rb[c].unshift(b);
        },
        prefilter: function(a, b) {
            b ? Qb.unshift(a) : Qb.push(a);
        }
    }), n.speed = function(a, b, c) {
        var d = a && "object" == typeof a ? n.extend({}, a) : {
            complete: c || !c && b || n.isFunction(a) && a,
            duration: a,
            easing: c && b || b && !n.isFunction(b) && b
        };
        return d.duration = n.fx.off ? 0 : "number" == typeof d.duration ? d.duration : d.duration in n.fx.speeds ? n.fx.speeds[d.duration] : n.fx.speeds._default, 
        (null == d.queue || !0 === d.queue) && (d.queue = "fx"), d.old = d.complete, d.complete = function() {
            n.isFunction(d.old) && d.old.call(this), d.queue && n.dequeue(this, d.queue);
        }, d;
    }, n.fn.extend({
        fadeTo: function(a, b, c, d) {
            return this.filter(S).css("opacity", 0).show().end().animate({
                opacity: b
            }, a, c, d);
        },
        animate: function(a, b, c, d) {
            var e = n.isEmptyObject(a), f = n.speed(b, c, d), g = function() {
                var b = Xb(this, n.extend({}, a), f);
                (e || L.get(this, "finish")) && b.stop(!0);
            };
            return g.finish = g, e || !1 === f.queue ? this.each(g) : this.queue(f.queue, g);
        },
        stop: function(a, b, c) {
            var d = function(a) {
                var b = a.stop;
                delete a.stop, b(c);
            };
            return "string" != typeof a && (c = b, b = a, a = void 0), b && !1 !== a && this.queue(a || "fx", []), 
            this.each(function() {
                var b = !0, e = null != a && a + "queueHooks", f = n.timers, g = L.get(this);
                if (e) g[e] && g[e].stop && d(g[e]); else for (e in g) g[e] && g[e].stop && Pb.test(e) && d(g[e]);
                for (e = f.length; e--; ) f[e].elem !== this || null != a && f[e].queue !== a || (f[e].anim.stop(c), 
                b = !1, f.splice(e, 1));
                (b || !c) && n.dequeue(this, a);
            });
        },
        finish: function(a) {
            return !1 !== a && (a = a || "fx"), this.each(function() {
                var b, c = L.get(this), d = c[a + "queue"], e = c[a + "queueHooks"], f = n.timers, g = d ? d.length : 0;
                for (c.finish = !0, n.queue(this, a, []), e && e.stop && e.stop.call(this, !0), 
                b = f.length; b--; ) f[b].elem === this && f[b].queue === a && (f[b].anim.stop(!0), 
                f.splice(b, 1));
                for (b = 0; g > b; b++) d[b] && d[b].finish && d[b].finish.call(this);
                delete c.finish;
            });
        }
    }), n.each([ "toggle", "show", "hide" ], function(a, b) {
        var c = n.fn[b];
        n.fn[b] = function(a, d, e) {
            return null == a || "boolean" == typeof a ? c.apply(this, arguments) : this.animate(Tb(b, !0), a, d, e);
        };
    }), n.each({
        slideDown: Tb("show"),
        slideUp: Tb("hide"),
        slideToggle: Tb("toggle"),
        fadeIn: {
            opacity: "show"
        },
        fadeOut: {
            opacity: "hide"
        },
        fadeToggle: {
            opacity: "toggle"
        }
    }, function(a, b) {
        n.fn[a] = function(a, c, d) {
            return this.animate(b, a, c, d);
        };
    }), n.timers = [], n.fx.tick = function() {
        var a, b = 0, c = n.timers;
        for (Lb = n.now(); b < c.length; b++) (a = c[b])() || c[b] !== a || c.splice(b--, 1);
        c.length || n.fx.stop(), Lb = void 0;
    }, n.fx.timer = function(a) {
        n.timers.push(a), a() ? n.fx.start() : n.timers.pop();
    }, n.fx.interval = 13, n.fx.start = function() {
        Mb || (Mb = setInterval(n.fx.tick, n.fx.interval));
    }, n.fx.stop = function() {
        clearInterval(Mb), Mb = null;
    }, n.fx.speeds = {
        slow: 600,
        fast: 200,
        _default: 400
    }, n.fn.delay = function(a, b) {
        return a = n.fx ? n.fx.speeds[a] || a : a, b = b || "fx", this.queue(b, function(b, c) {
            var d = setTimeout(b, a);
            c.stop = function() {
                clearTimeout(d);
            };
        });
    }, function() {
        var a = l.createElement("input"), b = l.createElement("select"), c = b.appendChild(l.createElement("option"));
        a.type = "checkbox", k.checkOn = "" !== a.value, k.optSelected = c.selected, b.disabled = !0, 
        k.optDisabled = !c.disabled, a = l.createElement("input"), a.value = "t", a.type = "radio", 
        k.radioValue = "t" === a.value;
    }();
    var Zb, $b = n.expr.attrHandle;
    n.fn.extend({
        attr: function(a, b) {
            return J(this, n.attr, a, b, arguments.length > 1);
        },
        removeAttr: function(a) {
            return this.each(function() {
                n.removeAttr(this, a);
            });
        }
    }), n.extend({
        attr: function(a, b, c) {
            var d, e, f = a.nodeType;
            if (a && 3 !== f && 8 !== f && 2 !== f) return typeof a.getAttribute === U ? n.prop(a, b, c) : (1 === f && n.isXMLDoc(a) || (b = b.toLowerCase(), 
            d = n.attrHooks[b] || (n.expr.match.bool.test(b) ? Zb : void 0)), void 0 === c ? d && "get" in d && null !== (e = d.get(a, b)) ? e : (e = n.find.attr(a, b), 
            null == e ? void 0 : e) : null !== c ? d && "set" in d && void 0 !== (e = d.set(a, c, b)) ? e : (a.setAttribute(b, c + ""), 
            c) : void n.removeAttr(a, b));
        },
        removeAttr: function(a, b) {
            var c, d, e = 0, f = b && b.match(E);
            if (f && 1 === a.nodeType) for (;c = f[e++]; ) d = n.propFix[c] || c, n.expr.match.bool.test(c) && (a[d] = !1), 
            a.removeAttribute(c);
        },
        attrHooks: {
            type: {
                set: function(a, b) {
                    if (!k.radioValue && "radio" === b && n.nodeName(a, "input")) {
                        var c = a.value;
                        return a.setAttribute("type", b), c && (a.value = c), b;
                    }
                }
            }
        }
    }), Zb = {
        set: function(a, b, c) {
            return !1 === b ? n.removeAttr(a, c) : a.setAttribute(c, c), c;
        }
    }, n.each(n.expr.match.bool.source.match(/\w+/g), function(a, b) {
        var c = $b[b] || n.find.attr;
        $b[b] = function(a, b, d) {
            var e, f;
            return d || (f = $b[b], $b[b] = e, e = null != c(a, b, d) ? b.toLowerCase() : null, 
            $b[b] = f), e;
        };
    });
    var _b = /^(?:input|select|textarea|button)$/i;
    n.fn.extend({
        prop: function(a, b) {
            return J(this, n.prop, a, b, arguments.length > 1);
        },
        removeProp: function(a) {
            return this.each(function() {
                delete this[n.propFix[a] || a];
            });
        }
    }), n.extend({
        propFix: {
            for: "htmlFor",
            class: "className"
        },
        prop: function(a, b, c) {
            var d, e, f, g = a.nodeType;
            if (a && 3 !== g && 8 !== g && 2 !== g) return f = 1 !== g || !n.isXMLDoc(a), f && (b = n.propFix[b] || b, 
            e = n.propHooks[b]), void 0 !== c ? e && "set" in e && void 0 !== (d = e.set(a, c, b)) ? d : a[b] = c : e && "get" in e && null !== (d = e.get(a, b)) ? d : a[b];
        },
        propHooks: {
            tabIndex: {
                get: function(a) {
                    return a.hasAttribute("tabindex") || _b.test(a.nodeName) || a.href ? a.tabIndex : -1;
                }
            }
        }
    }), k.optSelected || (n.propHooks.selected = {
        get: function(a) {
            var b = a.parentNode;
            return b && b.parentNode && b.parentNode.selectedIndex, null;
        }
    }), n.each([ "tabIndex", "readOnly", "maxLength", "cellSpacing", "cellPadding", "rowSpan", "colSpan", "useMap", "frameBorder", "contentEditable" ], function() {
        n.propFix[this.toLowerCase()] = this;
    });
    var ac = /[\t\r\n\f]/g;
    n.fn.extend({
        addClass: function(a) {
            var b, c, d, e, f, g, h = "string" == typeof a && a, i = 0, j = this.length;
            if (n.isFunction(a)) return this.each(function(b) {
                n(this).addClass(a.call(this, b, this.className));
            });
            if (h) for (b = (a || "").match(E) || []; j > i; i++) if (c = this[i], d = 1 === c.nodeType && (c.className ? (" " + c.className + " ").replace(ac, " ") : " ")) {
                for (f = 0; e = b[f++]; ) d.indexOf(" " + e + " ") < 0 && (d += e + " ");
                g = n.trim(d), c.className !== g && (c.className = g);
            }
            return this;
        },
        removeClass: function(a) {
            var b, c, d, e, f, g, h = 0 === arguments.length || "string" == typeof a && a, i = 0, j = this.length;
            if (n.isFunction(a)) return this.each(function(b) {
                n(this).removeClass(a.call(this, b, this.className));
            });
            if (h) for (b = (a || "").match(E) || []; j > i; i++) if (c = this[i], d = 1 === c.nodeType && (c.className ? (" " + c.className + " ").replace(ac, " ") : "")) {
                for (f = 0; e = b[f++]; ) for (;d.indexOf(" " + e + " ") >= 0; ) d = d.replace(" " + e + " ", " ");
                g = a ? n.trim(d) : "", c.className !== g && (c.className = g);
            }
            return this;
        },
        toggleClass: function(a, b) {
            var c = typeof a;
            return "boolean" == typeof b && "string" === c ? b ? this.addClass(a) : this.removeClass(a) : this.each(n.isFunction(a) ? function(c) {
                n(this).toggleClass(a.call(this, c, this.className, b), b);
            } : function() {
                if ("string" === c) for (var b, d = 0, e = n(this), f = a.match(E) || []; b = f[d++]; ) e.hasClass(b) ? e.removeClass(b) : e.addClass(b); else (c === U || "boolean" === c) && (this.className && L.set(this, "__className__", this.className), 
                this.className = this.className || !1 === a ? "" : L.get(this, "__className__") || "");
            });
        },
        hasClass: function(a) {
            for (var b = " " + a + " ", c = 0, d = this.length; d > c; c++) if (1 === this[c].nodeType && (" " + this[c].className + " ").replace(ac, " ").indexOf(b) >= 0) return !0;
            return !1;
        }
    });
    var bc = /\r/g;
    n.fn.extend({
        val: function(a) {
            var b, c, d, e = this[0];
            return arguments.length ? (d = n.isFunction(a), this.each(function(c) {
                var e;
                1 === this.nodeType && (e = d ? a.call(this, c, n(this).val()) : a, null == e ? e = "" : "number" == typeof e ? e += "" : n.isArray(e) && (e = n.map(e, function(a) {
                    return null == a ? "" : a + "";
                })), (b = n.valHooks[this.type] || n.valHooks[this.nodeName.toLowerCase()]) && "set" in b && void 0 !== b.set(this, e, "value") || (this.value = e));
            })) : e ? (b = n.valHooks[e.type] || n.valHooks[e.nodeName.toLowerCase()], b && "get" in b && void 0 !== (c = b.get(e, "value")) ? c : (c = e.value, 
            "string" == typeof c ? c.replace(bc, "") : null == c ? "" : c)) : void 0;
        }
    }), n.extend({
        valHooks: {
            option: {
                get: function(a) {
                    var b = n.find.attr(a, "value");
                    return null != b ? b : n.trim(n.text(a));
                }
            },
            select: {
                get: function(a) {
                    for (var b, c, d = a.options, e = a.selectedIndex, f = "select-one" === a.type || 0 > e, g = f ? null : [], h = f ? e + 1 : d.length, i = 0 > e ? h : f ? e : 0; h > i; i++) if (c = d[i], 
                    !(!c.selected && i !== e || (k.optDisabled ? c.disabled : null !== c.getAttribute("disabled")) || c.parentNode.disabled && n.nodeName(c.parentNode, "optgroup"))) {
                        if (b = n(c).val(), f) return b;
                        g.push(b);
                    }
                    return g;
                },
                set: function(a, b) {
                    for (var c, d, e = a.options, f = n.makeArray(b), g = e.length; g--; ) d = e[g], 
                    (d.selected = n.inArray(d.value, f) >= 0) && (c = !0);
                    return c || (a.selectedIndex = -1), f;
                }
            }
        }
    }), n.each([ "radio", "checkbox" ], function() {
        n.valHooks[this] = {
            set: function(a, b) {
                return n.isArray(b) ? a.checked = n.inArray(n(a).val(), b) >= 0 : void 0;
            }
        }, k.checkOn || (n.valHooks[this].get = function(a) {
            return null === a.getAttribute("value") ? "on" : a.value;
        });
    }), n.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "), function(a, b) {
        n.fn[b] = function(a, c) {
            return arguments.length > 0 ? this.on(b, null, a, c) : this.trigger(b);
        };
    }), n.fn.extend({
        hover: function(a, b) {
            return this.mouseenter(a).mouseleave(b || a);
        },
        bind: function(a, b, c) {
            return this.on(a, null, b, c);
        },
        unbind: function(a, b) {
            return this.off(a, null, b);
        },
        delegate: function(a, b, c, d) {
            return this.on(b, a, c, d);
        },
        undelegate: function(a, b, c) {
            return 1 === arguments.length ? this.off(a, "**") : this.off(b, a || "**", c);
        }
    });
    var cc = n.now(), dc = /\?/;
    n.parseJSON = function(a) {
        return JSON.parse(a + "");
    }, n.parseXML = function(a) {
        var b, c;
        if (!a || "string" != typeof a) return null;
        try {
            c = new DOMParser(), b = c.parseFromString(a, "text/xml");
        } catch (d) {
            b = void 0;
        }
        return (!b || b.getElementsByTagName("parsererror").length) && n.error("Invalid XML: " + a), 
        b;
    };
    var ec, fc, gc = /#.*$/, hc = /([?&])_=[^&]*/, ic = /^(.*?):[ \t]*([^\r\n]*)$/gm, jc = /^(?:about|app|app-storage|.+-extension|file|res|widget):$/, kc = /^(?:GET|HEAD)$/, lc = /^\/\//, mc = /^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/, nc = {}, oc = {}, pc = "*/".concat("*");
    try {
        fc = location.href;
    } catch (qc) {
        fc = l.createElement("a"), fc.href = "", fc = fc.href;
    }
    ec = mc.exec(fc.toLowerCase()) || [], n.extend({
        active: 0,
        lastModified: {},
        etag: {},
        ajaxSettings: {
            url: fc,
            type: "GET",
            isLocal: jc.test(ec[1]),
            global: !0,
            processData: !0,
            async: !0,
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            accepts: {
                "*": pc,
                text: "text/plain",
                html: "text/html",
                xml: "application/xml, text/xml",
                json: "application/json, text/javascript"
            },
            contents: {
                xml: /xml/,
                html: /html/,
                json: /json/
            },
            responseFields: {
                xml: "responseXML",
                text: "responseText",
                json: "responseJSON"
            },
            converters: {
                "* text": String,
                "text html": !0,
                "text json": n.parseJSON,
                "text xml": n.parseXML
            },
            flatOptions: {
                url: !0,
                context: !0
            }
        },
        ajaxSetup: function(a, b) {
            return b ? tc(tc(a, n.ajaxSettings), b) : tc(n.ajaxSettings, a);
        },
        ajaxPrefilter: rc(nc),
        ajaxTransport: rc(oc),
        ajax: function(a, b) {
            function x(a, b, f, h) {
                var j, r, s, u, w, x = b;
                2 !== t && (t = 2, g && clearTimeout(g), c = void 0, e = h || "", v.readyState = a > 0 ? 4 : 0, 
                j = a >= 200 && 300 > a || 304 === a, f && (u = uc(k, v, f)), u = vc(k, u, v, j), 
                j ? (k.ifModified && (w = v.getResponseHeader("Last-Modified"), w && (n.lastModified[d] = w), 
                (w = v.getResponseHeader("etag")) && (n.etag[d] = w)), 204 === a || "HEAD" === k.type ? x = "nocontent" : 304 === a ? x = "notmodified" : (x = u.state, 
                r = u.data, s = u.error, j = !s)) : (s = x, (a || !x) && (x = "error", 0 > a && (a = 0))), 
                v.status = a, v.statusText = (b || x) + "", j ? o.resolveWith(l, [ r, x, v ]) : o.rejectWith(l, [ v, x, s ]), 
                v.statusCode(q), q = void 0, i && m.trigger(j ? "ajaxSuccess" : "ajaxError", [ v, k, j ? r : s ]), 
                p.fireWith(l, [ v, x ]), i && (m.trigger("ajaxComplete", [ v, k ]), --n.active || n.event.trigger("ajaxStop")));
            }
            "object" == typeof a && (b = a, a = void 0), b = b || {};
            var c, d, e, f, g, h, i, j, k = n.ajaxSetup({}, b), l = k.context || k, m = k.context && (l.nodeType || l.jquery) ? n(l) : n.event, o = n.Deferred(), p = n.Callbacks("once memory"), q = k.statusCode || {}, r = {}, s = {}, t = 0, u = "canceled", v = {
                readyState: 0,
                getResponseHeader: function(a) {
                    var b;
                    if (2 === t) {
                        if (!f) for (f = {}; b = ic.exec(e); ) f[b[1].toLowerCase()] = b[2];
                        b = f[a.toLowerCase()];
                    }
                    return null == b ? null : b;
                },
                getAllResponseHeaders: function() {
                    return 2 === t ? e : null;
                },
                setRequestHeader: function(a, b) {
                    var c = a.toLowerCase();
                    return t || (a = s[c] = s[c] || a, r[a] = b), this;
                },
                overrideMimeType: function(a) {
                    return t || (k.mimeType = a), this;
                },
                statusCode: function(a) {
                    var b;
                    if (a) if (2 > t) for (b in a) q[b] = [ q[b], a[b] ]; else v.always(a[v.status]);
                    return this;
                },
                abort: function(a) {
                    var b = a || u;
                    return c && c.abort(b), x(0, b), this;
                }
            };
            if (o.promise(v).complete = p.add, v.success = v.done, v.error = v.fail, k.url = ((a || k.url || fc) + "").replace(gc, "").replace(lc, ec[1] + "//"), 
            k.type = b.method || b.type || k.method || k.type, k.dataTypes = n.trim(k.dataType || "*").toLowerCase().match(E) || [ "" ], 
            null == k.crossDomain && (h = mc.exec(k.url.toLowerCase()), k.crossDomain = !(!h || h[1] === ec[1] && h[2] === ec[2] && (h[3] || ("http:" === h[1] ? "80" : "443")) === (ec[3] || ("http:" === ec[1] ? "80" : "443")))), 
            k.data && k.processData && "string" != typeof k.data && (k.data = n.param(k.data, k.traditional)), 
            sc(nc, k, b, v), 2 === t) return v;
            i = k.global, i && 0 == n.active++ && n.event.trigger("ajaxStart"), k.type = k.type.toUpperCase(), 
            k.hasContent = !kc.test(k.type), d = k.url, k.hasContent || (k.data && (d = k.url += (dc.test(d) ? "&" : "?") + k.data, 
            delete k.data), !1 === k.cache && (k.url = hc.test(d) ? d.replace(hc, "$1_=" + cc++) : d + (dc.test(d) ? "&" : "?") + "_=" + cc++)), 
            k.ifModified && (n.lastModified[d] && v.setRequestHeader("If-Modified-Since", n.lastModified[d]), 
            n.etag[d] && v.setRequestHeader("If-None-Match", n.etag[d])), (k.data && k.hasContent && !1 !== k.contentType || b.contentType) && v.setRequestHeader("Content-Type", k.contentType), 
            v.setRequestHeader("Accept", k.dataTypes[0] && k.accepts[k.dataTypes[0]] ? k.accepts[k.dataTypes[0]] + ("*" !== k.dataTypes[0] ? ", " + pc + "; q=0.01" : "") : k.accepts["*"]);
            for (j in k.headers) v.setRequestHeader(j, k.headers[j]);
            if (k.beforeSend && (!1 === k.beforeSend.call(l, v, k) || 2 === t)) return v.abort();
            u = "abort";
            for (j in {
                success: 1,
                error: 1,
                complete: 1
            }) v[j](k[j]);
            if (c = sc(oc, k, b, v)) {
                v.readyState = 1, i && m.trigger("ajaxSend", [ v, k ]), k.async && k.timeout > 0 && (g = setTimeout(function() {
                    v.abort("timeout");
                }, k.timeout));
                try {
                    t = 1, c.send(r, x);
                } catch (w) {
                    if (!(2 > t)) throw w;
                    x(-1, w);
                }
            } else x(-1, "No Transport");
            return v;
        },
        getJSON: function(a, b, c) {
            return n.get(a, b, c, "json");
        },
        getScript: function(a, b) {
            return n.get(a, void 0, b, "script");
        }
    }), n.each([ "get", "post" ], function(a, b) {
        n[b] = function(a, c, d, e) {
            return n.isFunction(c) && (e = e || d, d = c, c = void 0), n.ajax({
                url: a,
                type: b,
                dataType: e,
                data: c,
                success: d
            });
        };
    }), n.each([ "ajaxStart", "ajaxStop", "ajaxComplete", "ajaxError", "ajaxSuccess", "ajaxSend" ], function(a, b) {
        n.fn[b] = function(a) {
            return this.on(b, a);
        };
    }), n._evalUrl = function(a) {
        return n.ajax({
            url: a,
            type: "GET",
            dataType: "script",
            async: !1,
            global: !1,
            throws: !0
        });
    }, n.fn.extend({
        wrapAll: function(a) {
            var b;
            return n.isFunction(a) ? this.each(function(b) {
                n(this).wrapAll(a.call(this, b));
            }) : (this[0] && (b = n(a, this[0].ownerDocument).eq(0).clone(!0), this[0].parentNode && b.insertBefore(this[0]), 
            b.map(function() {
                for (var a = this; a.firstElementChild; ) a = a.firstElementChild;
                return a;
            }).append(this)), this);
        },
        wrapInner: function(a) {
            return this.each(n.isFunction(a) ? function(b) {
                n(this).wrapInner(a.call(this, b));
            } : function() {
                var b = n(this), c = b.contents();
                c.length ? c.wrapAll(a) : b.append(a);
            });
        },
        wrap: function(a) {
            var b = n.isFunction(a);
            return this.each(function(c) {
                n(this).wrapAll(b ? a.call(this, c) : a);
            });
        },
        unwrap: function() {
            return this.parent().each(function() {
                n.nodeName(this, "body") || n(this).replaceWith(this.childNodes);
            }).end();
        }
    }), n.expr.filters.hidden = function(a) {
        return a.offsetWidth <= 0 && a.offsetHeight <= 0;
    }, n.expr.filters.visible = function(a) {
        return !n.expr.filters.hidden(a);
    };
    var wc = /%20/g, xc = /\[\]$/, yc = /\r?\n/g, zc = /^(?:submit|button|image|reset|file)$/i, Ac = /^(?:input|select|textarea|keygen)/i;
    n.param = function(a, b) {
        var c, d = [], e = function(a, b) {
            b = n.isFunction(b) ? b() : null == b ? "" : b, d[d.length] = encodeURIComponent(a) + "=" + encodeURIComponent(b);
        };
        if (void 0 === b && (b = n.ajaxSettings && n.ajaxSettings.traditional), n.isArray(a) || a.jquery && !n.isPlainObject(a)) n.each(a, function() {
            e(this.name, this.value);
        }); else for (c in a) Bc(c, a[c], b, e);
        return d.join("&").replace(wc, "+");
    }, n.fn.extend({
        serialize: function() {
            return n.param(this.serializeArray());
        },
        serializeArray: function() {
            return this.map(function() {
                var a = n.prop(this, "elements");
                return a ? n.makeArray(a) : this;
            }).filter(function() {
                var a = this.type;
                return this.name && !n(this).is(":disabled") && Ac.test(this.nodeName) && !zc.test(a) && (this.checked || !T.test(a));
            }).map(function(a, b) {
                var c = n(this).val();
                return null == c ? null : n.isArray(c) ? n.map(c, function(a) {
                    return {
                        name: b.name,
                        value: a.replace(yc, "\r\n")
                    };
                }) : {
                    name: b.name,
                    value: c.replace(yc, "\r\n")
                };
            }).get();
        }
    }), n.ajaxSettings.xhr = function() {
        try {
            return new XMLHttpRequest();
        } catch (a) {}
    };
    var Cc = 0, Dc = {}, Ec = {
        0: 200,
        1223: 204
    }, Fc = n.ajaxSettings.xhr();
    a.ActiveXObject && n(a).on("unload", function() {
        for (var a in Dc) Dc[a]();
    }), k.cors = !!Fc && "withCredentials" in Fc, k.ajax = Fc = !!Fc, n.ajaxTransport(function(a) {
        var b;
        return k.cors || Fc && !a.crossDomain ? {
            send: function(c, d) {
                var e, f = a.xhr(), g = ++Cc;
                if (f.open(a.type, a.url, a.async, a.username, a.password), a.xhrFields) for (e in a.xhrFields) f[e] = a.xhrFields[e];
                a.mimeType && f.overrideMimeType && f.overrideMimeType(a.mimeType), a.crossDomain || c["X-Requested-With"] || (c["X-Requested-With"] = "XMLHttpRequest");
                for (e in c) f.setRequestHeader(e, c[e]);
                b = function(a) {
                    return function() {
                        b && (delete Dc[g], b = f.onload = f.onerror = null, "abort" === a ? f.abort() : "error" === a ? d(f.status, f.statusText) : d(Ec[f.status] || f.status, f.statusText, "string" == typeof f.responseText ? {
                            text: f.responseText
                        } : void 0, f.getAllResponseHeaders()));
                    };
                }, f.onload = b(), f.onerror = b("error"), b = Dc[g] = b("abort");
                try {
                    f.send(a.hasContent && a.data || null);
                } catch (h) {
                    if (b) throw h;
                }
            },
            abort: function() {
                b && b();
            }
        } : void 0;
    }), n.ajaxSetup({
        accepts: {
            script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
        },
        contents: {
            script: /(?:java|ecma)script/
        },
        converters: {
            "text script": function(a) {
                return n.globalEval(a), a;
            }
        }
    }), n.ajaxPrefilter("script", function(a) {
        void 0 === a.cache && (a.cache = !1), a.crossDomain && (a.type = "GET");
    }), n.ajaxTransport("script", function(a) {
        if (a.crossDomain) {
            var b, c;
            return {
                send: function(d, e) {
                    b = n("<script>").prop({
                        async: !0,
                        charset: a.scriptCharset,
                        src: a.url
                    }).on("load error", c = function(a) {
                        b.remove(), c = null, a && e("error" === a.type ? 404 : 200, a.type);
                    }), l.head.appendChild(b[0]);
                },
                abort: function() {
                    c && c();
                }
            };
        }
    });
    var Gc = [], Hc = /(=)\?(?=&|$)|\?\?/;
    n.ajaxSetup({
        jsonp: "callback",
        jsonpCallback: function() {
            var a = Gc.pop() || n.expando + "_" + cc++;
            return this[a] = !0, a;
        }
    }), n.ajaxPrefilter("json jsonp", function(b, c, d) {
        var e, f, g, h = !1 !== b.jsonp && (Hc.test(b.url) ? "url" : "string" == typeof b.data && !(b.contentType || "").indexOf("application/x-www-form-urlencoded") && Hc.test(b.data) && "data");
        return h || "jsonp" === b.dataTypes[0] ? (e = b.jsonpCallback = n.isFunction(b.jsonpCallback) ? b.jsonpCallback() : b.jsonpCallback, 
        h ? b[h] = b[h].replace(Hc, "$1" + e) : !1 !== b.jsonp && (b.url += (dc.test(b.url) ? "&" : "?") + b.jsonp + "=" + e), 
        b.converters["script json"] = function() {
            return g || n.error(e + " was not called"), g[0];
        }, b.dataTypes[0] = "json", f = a[e], a[e] = function() {
            g = arguments;
        }, d.always(function() {
            a[e] = f, b[e] && (b.jsonpCallback = c.jsonpCallback, Gc.push(e)), g && n.isFunction(f) && f(g[0]), 
            g = f = void 0;
        }), "script") : void 0;
    }), n.parseHTML = function(a, b, c) {
        if (!a || "string" != typeof a) return null;
        "boolean" == typeof b && (c = b, b = !1), b = b || l;
        var d = v.exec(a), e = !c && [];
        return d ? [ b.createElement(d[1]) ] : (d = n.buildFragment([ a ], b, e), e && e.length && n(e).remove(), 
        n.merge([], d.childNodes));
    };
    var Ic = n.fn.load;
    n.fn.load = function(a, b, c) {
        if ("string" != typeof a && Ic) return Ic.apply(this, arguments);
        var d, e, f, g = this, h = a.indexOf(" ");
        return h >= 0 && (d = n.trim(a.slice(h)), a = a.slice(0, h)), n.isFunction(b) ? (c = b, 
        b = void 0) : b && "object" == typeof b && (e = "POST"), g.length > 0 && n.ajax({
            url: a,
            type: e,
            dataType: "html",
            data: b
        }).done(function(a) {
            f = arguments, g.html(d ? n("<div>").append(n.parseHTML(a)).find(d) : a);
        }).complete(c && function(a, b) {
            g.each(c, f || [ a.responseText, b, a ]);
        }), this;
    }, n.expr.filters.animated = function(a) {
        return n.grep(n.timers, function(b) {
            return a === b.elem;
        }).length;
    };
    var Jc = a.document.documentElement;
    n.offset = {
        setOffset: function(a, b, c) {
            var d, e, f, g, h, i, j, k = n.css(a, "position"), l = n(a), m = {};
            "static" === k && (a.style.position = "relative"), h = l.offset(), f = n.css(a, "top"), 
            i = n.css(a, "left"), j = ("absolute" === k || "fixed" === k) && (f + i).indexOf("auto") > -1, 
            j ? (d = l.position(), g = d.top, e = d.left) : (g = parseFloat(f) || 0, e = parseFloat(i) || 0), 
            n.isFunction(b) && (b = b.call(a, c, h)), null != b.top && (m.top = b.top - h.top + g), 
            null != b.left && (m.left = b.left - h.left + e), "using" in b ? b.using.call(a, m) : l.css(m);
        }
    }, n.fn.extend({
        offset: function(a) {
            if (arguments.length) return void 0 === a ? this : this.each(function(b) {
                n.offset.setOffset(this, a, b);
            });
            var b, c, d = this[0], e = {
                top: 0,
                left: 0
            }, f = d && d.ownerDocument;
            return f ? (b = f.documentElement, n.contains(b, d) ? (typeof d.getBoundingClientRect !== U && (e = d.getBoundingClientRect()), 
            c = Kc(f), {
                top: e.top + c.pageYOffset - b.clientTop,
                left: e.left + c.pageXOffset - b.clientLeft
            }) : e) : void 0;
        },
        position: function() {
            if (this[0]) {
                var a, b, c = this[0], d = {
                    top: 0,
                    left: 0
                };
                return "fixed" === n.css(c, "position") ? b = c.getBoundingClientRect() : (a = this.offsetParent(), 
                b = this.offset(), n.nodeName(a[0], "html") || (d = a.offset()), d.top += n.css(a[0], "borderTopWidth", !0), 
                d.left += n.css(a[0], "borderLeftWidth", !0)), {
                    top: b.top - d.top - n.css(c, "marginTop", !0),
                    left: b.left - d.left - n.css(c, "marginLeft", !0)
                };
            }
        },
        offsetParent: function() {
            return this.map(function() {
                for (var a = this.offsetParent || Jc; a && !n.nodeName(a, "html") && "static" === n.css(a, "position"); ) a = a.offsetParent;
                return a || Jc;
            });
        }
    }), n.each({
        scrollLeft: "pageXOffset",
        scrollTop: "pageYOffset"
    }, function(b, c) {
        var d = "pageYOffset" === c;
        n.fn[b] = function(e) {
            return J(this, function(b, e, f) {
                var g = Kc(b);
                return void 0 === f ? g ? g[c] : b[e] : void (g ? g.scrollTo(d ? a.pageXOffset : f, d ? f : a.pageYOffset) : b[e] = f);
            }, b, e, arguments.length, null);
        };
    }), n.each([ "top", "left" ], function(a, b) {
        n.cssHooks[b] = yb(k.pixelPosition, function(a, c) {
            return c ? (c = xb(a, b), vb.test(c) ? n(a).position()[b] + "px" : c) : void 0;
        });
    }), n.each({
        Height: "height",
        Width: "width"
    }, function(a, b) {
        n.each({
            padding: "inner" + a,
            content: b,
            "": "outer" + a
        }, function(c, d) {
            n.fn[d] = function(d, e) {
                var f = arguments.length && (c || "boolean" != typeof d), g = c || (!0 === d || !0 === e ? "margin" : "border");
                return J(this, function(b, c, d) {
                    var e;
                    return n.isWindow(b) ? b.document.documentElement["client" + a] : 9 === b.nodeType ? (e = b.documentElement, 
                    Math.max(b.body["scroll" + a], e["scroll" + a], b.body["offset" + a], e["offset" + a], e["client" + a])) : void 0 === d ? n.css(b, c, g) : n.style(b, c, d, g);
                }, b, f ? d : void 0, f, null);
            };
        });
    }), n.fn.size = function() {
        return this.length;
    }, n.fn.andSelf = n.fn.addBack, "function" == typeof define && define.amd && define("jquery", [], function() {
        return n;
    });
    var Lc = a.jQuery, Mc = a.$;
    return n.noConflict = function(b) {
        return a.$ === n && (a.$ = Mc), b && a.jQuery === n && (a.jQuery = Lc), n;
    }, typeof b === U && (a.jQuery = a.$ = n), n;
}), function(e) {
    "function" == typeof define && define.amd ? define([ "jquery" ], e) : e(jQuery);
}(function(e) {
    function t(t, s) {
        var n, a, o, r = t.nodeName.toLowerCase();
        return "area" === r ? (n = t.parentNode, a = n.name, !(!t.href || !a || "map" !== n.nodeName.toLowerCase()) && (!!(o = e("img[usemap='#" + a + "']")[0]) && i(o))) : (/input|select|textarea|button|object/.test(r) ? !t.disabled : "a" === r ? t.href || s : s) && i(t);
    }
    function i(t) {
        return e.expr.filters.visible(t) && !e(t).parents().addBack().filter(function() {
            return "hidden" === e.css(this, "visibility");
        }).length;
    }
    function s(e) {
        for (var t, i; e.length && e[0] !== document; ) {
            if (("absolute" === (t = e.css("position")) || "relative" === t || "fixed" === t) && (i = parseInt(e.css("zIndex"), 10), 
            !isNaN(i) && 0 !== i)) return i;
            e = e.parent();
        }
        return 0;
    }
    function n() {
        this._curInst = null, this._keyEvent = !1, this._disabledInputs = [], this._datepickerShowing = !1, 
        this._inDialog = !1, this._mainDivId = "ui-datepicker-div", this._inlineClass = "ui-datepicker-inline", 
        this._appendClass = "ui-datepicker-append", this._triggerClass = "ui-datepicker-trigger", 
        this._dialogClass = "ui-datepicker-dialog", this._disableClass = "ui-datepicker-disabled", 
        this._unselectableClass = "ui-datepicker-unselectable", this._currentClass = "ui-datepicker-current-day", 
        this._dayOverClass = "ui-datepicker-days-cell-over", this.regional = [], this.regional[""] = {
            closeText: "Done",
            prevText: "Prev",
            nextText: "Next",
            currentText: "Today",
            monthNames: [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ],
            monthNamesShort: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ],
            dayNames: [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" ],
            dayNamesShort: [ "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" ],
            dayNamesMin: [ "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" ],
            weekHeader: "Wk",
            dateFormat: "mm/dd/yy",
            firstDay: 0,
            isRTL: !1,
            showMonthAfterYear: !1,
            yearSuffix: ""
        }, this._defaults = {
            showOn: "focus",
            showAnim: "fadeIn",
            showOptions: {},
            defaultDate: null,
            appendText: "",
            buttonText: "...",
            buttonImage: "",
            buttonImageOnly: !1,
            hideIfNoPrevNext: !1,
            navigationAsDateFormat: !1,
            gotoCurrent: !1,
            changeMonth: !1,
            changeYear: !1,
            yearRange: "c-10:c+10",
            showOtherMonths: !1,
            selectOtherMonths: !1,
            showWeek: !1,
            calculateWeek: this.iso8601Week,
            shortYearCutoff: "+10",
            minDate: null,
            maxDate: null,
            duration: "fast",
            beforeShowDay: null,
            beforeShow: null,
            onSelect: null,
            onChangeMonthYear: null,
            onClose: null,
            numberOfMonths: 1,
            showCurrentAtPos: 0,
            stepMonths: 1,
            stepBigMonths: 12,
            altField: "",
            altFormat: "",
            constrainInput: !0,
            showButtonPanel: !1,
            autoSize: !1,
            disabled: !1
        }, e.extend(this._defaults, this.regional[""]), this.regional.en = e.extend(!0, {}, this.regional[""]), 
        this.regional["en-US"] = e.extend(!0, {}, this.regional.en), this.dpDiv = a(e("<div id='" + this._mainDivId + "' class='ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all'></div>"));
    }
    function a(t) {
        var i = "button, .ui-datepicker-prev, .ui-datepicker-next, .ui-datepicker-calendar td a";
        return t.delegate(i, "mouseout", function() {
            e(this).removeClass("ui-state-hover"), -1 !== this.className.indexOf("ui-datepicker-prev") && e(this).removeClass("ui-datepicker-prev-hover"), 
            -1 !== this.className.indexOf("ui-datepicker-next") && e(this).removeClass("ui-datepicker-next-hover");
        }).delegate(i, "mouseover", o);
    }
    function o() {
        e.datepicker._isDisabledDatepicker(v.inline ? v.dpDiv.parent()[0] : v.input[0]) || (e(this).parents(".ui-datepicker-calendar").find("a").removeClass("ui-state-hover"), 
        e(this).addClass("ui-state-hover"), -1 !== this.className.indexOf("ui-datepicker-prev") && e(this).addClass("ui-datepicker-prev-hover"), 
        -1 !== this.className.indexOf("ui-datepicker-next") && e(this).addClass("ui-datepicker-next-hover"));
    }
    function r(t, i) {
        e.extend(t, i);
        for (var s in i) null == i[s] && (t[s] = i[s]);
        return t;
    }
    function h(e) {
        return function() {
            var t = this.element.val();
            e.apply(this, arguments), this._refresh(), t !== this.element.val() && this._trigger("change");
        };
    }
    e.ui = e.ui || {}, e.extend(e.ui, {
        version: "1.11.2",
        keyCode: {
            BACKSPACE: 8,
            COMMA: 188,
            DELETE: 46,
            DOWN: 40,
            END: 35,
            ENTER: 13,
            ESCAPE: 27,
            HOME: 36,
            LEFT: 37,
            PAGE_DOWN: 34,
            PAGE_UP: 33,
            PERIOD: 190,
            RIGHT: 39,
            SPACE: 32,
            TAB: 9,
            UP: 38
        }
    }), e.fn.extend({
        scrollParent: function(t) {
            var i = this.css("position"), s = "absolute" === i, n = t ? /(auto|scroll|hidden)/ : /(auto|scroll)/, a = this.parents().filter(function() {
                var t = e(this);
                return (!s || "static" !== t.css("position")) && n.test(t.css("overflow") + t.css("overflow-y") + t.css("overflow-x"));
            }).eq(0);
            return "fixed" !== i && a.length ? a : e(this[0].ownerDocument || document);
        },
        uniqueId: function() {
            var e = 0;
            return function() {
                return this.each(function() {
                    this.id || (this.id = "ui-id-" + ++e);
                });
            };
        }(),
        removeUniqueId: function() {
            return this.each(function() {
                /^ui-id-\d+$/.test(this.id) && e(this).removeAttr("id");
            });
        }
    }), e.extend(e.expr[":"], {
        data: e.expr.createPseudo ? e.expr.createPseudo(function(t) {
            return function(i) {
                return !!e.data(i, t);
            };
        }) : function(t, i, s) {
            return !!e.data(t, s[3]);
        },
        focusable: function(i) {
            return t(i, !isNaN(e.attr(i, "tabindex")));
        },
        tabbable: function(i) {
            var s = e.attr(i, "tabindex"), n = isNaN(s);
            return (n || s >= 0) && t(i, !n);
        }
    }), e("<a>").outerWidth(1).jquery || e.each([ "Width", "Height" ], function(t, i) {
        function s(t, i, s, a) {
            return e.each(n, function() {
                i -= parseFloat(e.css(t, "padding" + this)) || 0, s && (i -= parseFloat(e.css(t, "border" + this + "Width")) || 0), 
                a && (i -= parseFloat(e.css(t, "margin" + this)) || 0);
            }), i;
        }
        var n = "Width" === i ? [ "Left", "Right" ] : [ "Top", "Bottom" ], a = i.toLowerCase(), o = {
            innerWidth: e.fn.innerWidth,
            innerHeight: e.fn.innerHeight,
            outerWidth: e.fn.outerWidth,
            outerHeight: e.fn.outerHeight
        };
        e.fn["inner" + i] = function(t) {
            return void 0 === t ? o["inner" + i].call(this) : this.each(function() {
                e(this).css(a, s(this, t) + "px");
            });
        }, e.fn["outer" + i] = function(t, n) {
            return "number" != typeof t ? o["outer" + i].call(this, t) : this.each(function() {
                e(this).css(a, s(this, t, !0, n) + "px");
            });
        };
    }), e.fn.addBack || (e.fn.addBack = function(e) {
        return this.add(null == e ? this.prevObject : this.prevObject.filter(e));
    }), e("<a>").data("a-b", "a").removeData("a-b").data("a-b") && (e.fn.removeData = function(t) {
        return function(i) {
            return arguments.length ? t.call(this, e.camelCase(i)) : t.call(this);
        };
    }(e.fn.removeData)), e.ui.ie = !!/msie [\w.]+/.exec(navigator.userAgent.toLowerCase()), 
    e.fn.extend({
        focus: function(t) {
            return function(i, s) {
                return "number" == typeof i ? this.each(function() {
                    var t = this;
                    setTimeout(function() {
                        e(t).focus(), s && s.call(t);
                    }, i);
                }) : t.apply(this, arguments);
            };
        }(e.fn.focus),
        disableSelection: function() {
            var e = "onselectstart" in document.createElement("div") ? "selectstart" : "mousedown";
            return function() {
                return this.bind(e + ".ui-disableSelection", function(e) {
                    e.preventDefault();
                });
            };
        }(),
        enableSelection: function() {
            return this.unbind(".ui-disableSelection");
        },
        zIndex: function(t) {
            if (void 0 !== t) return this.css("zIndex", t);
            if (this.length) for (var i, s, n = e(this[0]); n.length && n[0] !== document; ) {
                if (("absolute" === (i = n.css("position")) || "relative" === i || "fixed" === i) && (s = parseInt(n.css("zIndex"), 10), 
                !isNaN(s) && 0 !== s)) return s;
                n = n.parent();
            }
            return 0;
        }
    }), e.ui.plugin = {
        add: function(t, i, s) {
            var n, a = e.ui[t].prototype;
            for (n in s) a.plugins[n] = a.plugins[n] || [], a.plugins[n].push([ i, s[n] ]);
        },
        call: function(e, t, i, s) {
            var n, a = e.plugins[t];
            if (a && (s || e.element[0].parentNode && 11 !== e.element[0].parentNode.nodeType)) for (n = 0; a.length > n; n++) e.options[a[n][0]] && a[n][1].apply(e.element, i);
        }
    };
    var l = 0, u = Array.prototype.slice;
    e.cleanData = function(t) {
        return function(i) {
            var s, n, a;
            for (a = 0; null != (n = i[a]); a++) try {
                (s = e._data(n, "events")) && s.remove && e(n).triggerHandler("remove");
            } catch (o) {}
            t(i);
        };
    }(e.cleanData), e.widget = function(t, i, s) {
        var n, a, o, r, h = {}, l = t.split(".")[0];
        return t = t.split(".")[1], n = l + "-" + t, s || (s = i, i = e.Widget), e.expr[":"][n.toLowerCase()] = function(t) {
            return !!e.data(t, n);
        }, e[l] = e[l] || {}, a = e[l][t], o = e[l][t] = function(e, t) {
            return this._createWidget ? void (arguments.length && this._createWidget(e, t)) : new o(e, t);
        }, e.extend(o, a, {
            version: s.version,
            _proto: e.extend({}, s),
            _childConstructors: []
        }), r = new i(), r.options = e.widget.extend({}, r.options), e.each(s, function(t, s) {
            return e.isFunction(s) ? void (h[t] = function() {
                var e = function() {
                    return i.prototype[t].apply(this, arguments);
                }, n = function(e) {
                    return i.prototype[t].apply(this, e);
                };
                return function() {
                    var t, i = this._super, a = this._superApply;
                    return this._super = e, this._superApply = n, t = s.apply(this, arguments), this._super = i, 
                    this._superApply = a, t;
                };
            }()) : void (h[t] = s);
        }), o.prototype = e.widget.extend(r, {
            widgetEventPrefix: a ? r.widgetEventPrefix || t : t
        }, h, {
            constructor: o,
            namespace: l,
            widgetName: t,
            widgetFullName: n
        }), a ? (e.each(a._childConstructors, function(t, i) {
            var s = i.prototype;
            e.widget(s.namespace + "." + s.widgetName, o, i._proto);
        }), delete a._childConstructors) : i._childConstructors.push(o), e.widget.bridge(t, o), 
        o;
    }, e.widget.extend = function(t) {
        for (var i, s, n = u.call(arguments, 1), a = 0, o = n.length; o > a; a++) for (i in n[a]) s = n[a][i], 
        n[a].hasOwnProperty(i) && void 0 !== s && (t[i] = e.isPlainObject(s) ? e.isPlainObject(t[i]) ? e.widget.extend({}, t[i], s) : e.widget.extend({}, s) : s);
        return t;
    }, e.widget.bridge = function(t, i) {
        var s = i.prototype.widgetFullName || t;
        e.fn[t] = function(n) {
            var a = "string" == typeof n, o = u.call(arguments, 1), r = this;
            return n = !a && o.length ? e.widget.extend.apply(null, [ n ].concat(o)) : n, a ? this.each(function() {
                var i, a = e.data(this, s);
                return "instance" === n ? (r = a, !1) : a ? e.isFunction(a[n]) && "_" !== n.charAt(0) ? (i = a[n].apply(a, o), 
                i !== a && void 0 !== i ? (r = i && i.jquery ? r.pushStack(i.get()) : i, !1) : void 0) : e.error("no such method '" + n + "' for " + t + " widget instance") : e.error("cannot call methods on " + t + " prior to initialization; attempted to call method '" + n + "'");
            }) : this.each(function() {
                var t = e.data(this, s);
                t ? (t.option(n || {}), t._init && t._init()) : e.data(this, s, new i(n, this));
            }), r;
        };
    }, e.Widget = function() {}, e.Widget._childConstructors = [], e.Widget.prototype = {
        widgetName: "widget",
        widgetEventPrefix: "",
        defaultElement: "<div>",
        options: {
            disabled: !1,
            create: null
        },
        _createWidget: function(t, i) {
            i = e(i || this.defaultElement || this)[0], this.element = e(i), this.uuid = l++, 
            this.eventNamespace = "." + this.widgetName + this.uuid, this.bindings = e(), this.hoverable = e(), 
            this.focusable = e(), i !== this && (e.data(i, this.widgetFullName, this), this._on(!0, this.element, {
                remove: function(e) {
                    e.target === i && this.destroy();
                }
            }), this.document = e(i.style ? i.ownerDocument : i.document || i), this.window = e(this.document[0].defaultView || this.document[0].parentWindow)), 
            this.options = e.widget.extend({}, this.options, this._getCreateOptions(), t), this._create(), 
            this._trigger("create", null, this._getCreateEventData()), this._init();
        },
        _getCreateOptions: e.noop,
        _getCreateEventData: e.noop,
        _create: e.noop,
        _init: e.noop,
        destroy: function() {
            this._destroy(), this.element.unbind(this.eventNamespace).removeData(this.widgetFullName).removeData(e.camelCase(this.widgetFullName)), 
            this.widget().unbind(this.eventNamespace).removeAttr("aria-disabled").removeClass(this.widgetFullName + "-disabled ui-state-disabled"), 
            this.bindings.unbind(this.eventNamespace), this.hoverable.removeClass("ui-state-hover"), 
            this.focusable.removeClass("ui-state-focus");
        },
        _destroy: e.noop,
        widget: function() {
            return this.element;
        },
        option: function(t, i) {
            var s, n, a, o = t;
            if (0 === arguments.length) return e.widget.extend({}, this.options);
            if ("string" == typeof t) if (o = {}, s = t.split("."), t = s.shift(), s.length) {
                for (n = o[t] = e.widget.extend({}, this.options[t]), a = 0; s.length - 1 > a; a++) n[s[a]] = n[s[a]] || {}, 
                n = n[s[a]];
                if (t = s.pop(), 1 === arguments.length) return void 0 === n[t] ? null : n[t];
                n[t] = i;
            } else {
                if (1 === arguments.length) return void 0 === this.options[t] ? null : this.options[t];
                o[t] = i;
            }
            return this._setOptions(o), this;
        },
        _setOptions: function(e) {
            var t;
            for (t in e) this._setOption(t, e[t]);
            return this;
        },
        _setOption: function(e, t) {
            return this.options[e] = t, "disabled" === e && (this.widget().toggleClass(this.widgetFullName + "-disabled", !!t), 
            t && (this.hoverable.removeClass("ui-state-hover"), this.focusable.removeClass("ui-state-focus"))), 
            this;
        },
        enable: function() {
            return this._setOptions({
                disabled: !1
            });
        },
        disable: function() {
            return this._setOptions({
                disabled: !0
            });
        },
        _on: function(t, i, s) {
            var n, a = this;
            "boolean" != typeof t && (s = i, i = t, t = !1), s ? (i = n = e(i), this.bindings = this.bindings.add(i)) : (s = i, 
            i = this.element, n = this.widget()), e.each(s, function(s, o) {
                function r() {
                    return t || !0 !== a.options.disabled && !e(this).hasClass("ui-state-disabled") ? ("string" == typeof o ? a[o] : o).apply(a, arguments) : void 0;
                }
                "string" != typeof o && (r.guid = o.guid = o.guid || r.guid || e.guid++);
                var h = s.match(/^([\w:-]*)\s*(.*)$/), l = h[1] + a.eventNamespace, u = h[2];
                u ? n.delegate(u, l, r) : i.bind(l, r);
            });
        },
        _off: function(t, i) {
            i = (i || "").split(" ").join(this.eventNamespace + " ") + this.eventNamespace, 
            t.unbind(i).undelegate(i), this.bindings = e(this.bindings.not(t).get()), this.focusable = e(this.focusable.not(t).get()), 
            this.hoverable = e(this.hoverable.not(t).get());
        },
        _delay: function(e, t) {
            function i() {
                return ("string" == typeof e ? s[e] : e).apply(s, arguments);
            }
            var s = this;
            return setTimeout(i, t || 0);
        },
        _hoverable: function(t) {
            this.hoverable = this.hoverable.add(t), this._on(t, {
                mouseenter: function(t) {
                    e(t.currentTarget).addClass("ui-state-hover");
                },
                mouseleave: function(t) {
                    e(t.currentTarget).removeClass("ui-state-hover");
                }
            });
        },
        _focusable: function(t) {
            this.focusable = this.focusable.add(t), this._on(t, {
                focusin: function(t) {
                    e(t.currentTarget).addClass("ui-state-focus");
                },
                focusout: function(t) {
                    e(t.currentTarget).removeClass("ui-state-focus");
                }
            });
        },
        _trigger: function(t, i, s) {
            var n, a, o = this.options[t];
            if (s = s || {}, i = e.Event(i), i.type = (t === this.widgetEventPrefix ? t : this.widgetEventPrefix + t).toLowerCase(), 
            i.target = this.element[0], a = i.originalEvent) for (n in a) n in i || (i[n] = a[n]);
            return this.element.trigger(i, s), !(e.isFunction(o) && !1 === o.apply(this.element[0], [ i ].concat(s)) || i.isDefaultPrevented());
        }
    }, e.each({
        show: "fadeIn",
        hide: "fadeOut"
    }, function(t, i) {
        e.Widget.prototype["_" + t] = function(s, n, a) {
            "string" == typeof n && (n = {
                effect: n
            });
            var o, r = n ? !0 === n || "number" == typeof n ? i : n.effect || i : t;
            n = n || {}, "number" == typeof n && (n = {
                duration: n
            }), o = !e.isEmptyObject(n), n.complete = a, n.delay && s.delay(n.delay), o && e.effects && e.effects.effect[r] ? s[t](n) : r !== t && s[r] ? s[r](n.duration, n.easing, a) : s.queue(function(i) {
                e(this)[t](), a && a.call(s[0]), i();
            });
        };
    }), e.widget;
    var d = !1;
    e(document).mouseup(function() {
        d = !1;
    }), e.widget("ui.mouse", {
        version: "1.11.2",
        options: {
            cancel: "input,textarea,button,select,option",
            distance: 1,
            delay: 0
        },
        _mouseInit: function() {
            var t = this;
            this.element.bind("mousedown." + this.widgetName, function(e) {
                return t._mouseDown(e);
            }).bind("click." + this.widgetName, function(i) {
                return !0 === e.data(i.target, t.widgetName + ".preventClickEvent") ? (e.removeData(i.target, t.widgetName + ".preventClickEvent"), 
                i.stopImmediatePropagation(), !1) : void 0;
            }), this.started = !1;
        },
        _mouseDestroy: function() {
            this.element.unbind("." + this.widgetName), this._mouseMoveDelegate && this.document.unbind("mousemove." + this.widgetName, this._mouseMoveDelegate).unbind("mouseup." + this.widgetName, this._mouseUpDelegate);
        },
        _mouseDown: function(t) {
            if (!d) {
                this._mouseMoved = !1, this._mouseStarted && this._mouseUp(t), this._mouseDownEvent = t;
                var i = this, s = 1 === t.which, n = !("string" != typeof this.options.cancel || !t.target.nodeName) && e(t.target).closest(this.options.cancel).length;
                return !(s && !n && this._mouseCapture(t)) || (this.mouseDelayMet = !this.options.delay, 
                this.mouseDelayMet || (this._mouseDelayTimer = setTimeout(function() {
                    i.mouseDelayMet = !0;
                }, this.options.delay)), this._mouseDistanceMet(t) && this._mouseDelayMet(t) && (this._mouseStarted = !1 !== this._mouseStart(t), 
                !this._mouseStarted) ? (t.preventDefault(), !0) : (!0 === e.data(t.target, this.widgetName + ".preventClickEvent") && e.removeData(t.target, this.widgetName + ".preventClickEvent"), 
                this._mouseMoveDelegate = function(e) {
                    return i._mouseMove(e);
                }, this._mouseUpDelegate = function(e) {
                    return i._mouseUp(e);
                }, this.document.bind("mousemove." + this.widgetName, this._mouseMoveDelegate).bind("mouseup." + this.widgetName, this._mouseUpDelegate), 
                t.preventDefault(), d = !0, !0));
            }
        },
        _mouseMove: function(t) {
            if (this._mouseMoved) {
                if (e.ui.ie && (!document.documentMode || 9 > document.documentMode) && !t.button) return this._mouseUp(t);
                if (!t.which) return this._mouseUp(t);
            }
            return (t.which || t.button) && (this._mouseMoved = !0), this._mouseStarted ? (this._mouseDrag(t), 
            t.preventDefault()) : (this._mouseDistanceMet(t) && this._mouseDelayMet(t) && (this._mouseStarted = !1 !== this._mouseStart(this._mouseDownEvent, t), 
            this._mouseStarted ? this._mouseDrag(t) : this._mouseUp(t)), !this._mouseStarted);
        },
        _mouseUp: function(t) {
            return this.document.unbind("mousemove." + this.widgetName, this._mouseMoveDelegate).unbind("mouseup." + this.widgetName, this._mouseUpDelegate), 
            this._mouseStarted && (this._mouseStarted = !1, t.target === this._mouseDownEvent.target && e.data(t.target, this.widgetName + ".preventClickEvent", !0), 
            this._mouseStop(t)), d = !1, !1;
        },
        _mouseDistanceMet: function(e) {
            return Math.max(Math.abs(this._mouseDownEvent.pageX - e.pageX), Math.abs(this._mouseDownEvent.pageY - e.pageY)) >= this.options.distance;
        },
        _mouseDelayMet: function() {
            return this.mouseDelayMet;
        },
        _mouseStart: function() {},
        _mouseDrag: function() {},
        _mouseStop: function() {},
        _mouseCapture: function() {
            return !0;
        }
    }), function() {
        function t(e, t, i) {
            return [ parseFloat(e[0]) * (p.test(e[0]) ? t / 100 : 1), parseFloat(e[1]) * (p.test(e[1]) ? i / 100 : 1) ];
        }
        function i(t, i) {
            return parseInt(e.css(t, i), 10) || 0;
        }
        function s(t) {
            var i = t[0];
            return 9 === i.nodeType ? {
                width: t.width(),
                height: t.height(),
                offset: {
                    top: 0,
                    left: 0
                }
            } : e.isWindow(i) ? {
                width: t.width(),
                height: t.height(),
                offset: {
                    top: t.scrollTop(),
                    left: t.scrollLeft()
                }
            } : i.preventDefault ? {
                width: 0,
                height: 0,
                offset: {
                    top: i.pageY,
                    left: i.pageX
                }
            } : {
                width: t.outerWidth(),
                height: t.outerHeight(),
                offset: t.offset()
            };
        }
        e.ui = e.ui || {};
        var n, a, o = Math.max, r = Math.abs, h = Math.round, l = /left|center|right/, u = /top|center|bottom/, d = /[\+\-]\d+(\.[\d]+)?%?/, c = /^\w+/, p = /%$/, f = e.fn.position;
        e.position = {
            scrollbarWidth: function() {
                if (void 0 !== n) return n;
                var t, i, s = e("<div style='display:block;position:absolute;width:50px;height:50px;overflow:hidden;'><div style='height:100px;width:auto;'></div></div>"), a = s.children()[0];
                return e("body").append(s), t = a.offsetWidth, s.css("overflow", "scroll"), i = a.offsetWidth, 
                t === i && (i = s[0].clientWidth), s.remove(), n = t - i;
            },
            getScrollInfo: function(t) {
                var i = t.isWindow || t.isDocument ? "" : t.element.css("overflow-x"), s = t.isWindow || t.isDocument ? "" : t.element.css("overflow-y"), n = "scroll" === i || "auto" === i && t.width < t.element[0].scrollWidth;
                return {
                    width: "scroll" === s || "auto" === s && t.height < t.element[0].scrollHeight ? e.position.scrollbarWidth() : 0,
                    height: n ? e.position.scrollbarWidth() : 0
                };
            },
            getWithinInfo: function(t) {
                var i = e(t || window), s = e.isWindow(i[0]), n = !!i[0] && 9 === i[0].nodeType;
                return {
                    element: i,
                    isWindow: s,
                    isDocument: n,
                    offset: i.offset() || {
                        left: 0,
                        top: 0
                    },
                    scrollLeft: i.scrollLeft(),
                    scrollTop: i.scrollTop(),
                    width: s || n ? i.width() : i.outerWidth(),
                    height: s || n ? i.height() : i.outerHeight()
                };
            }
        }, e.fn.position = function(n) {
            if (!n || !n.of) return f.apply(this, arguments);
            n = e.extend({}, n);
            var p, m, g, v, y, b, _ = e(n.of), x = e.position.getWithinInfo(n.within), w = e.position.getScrollInfo(x), k = (n.collision || "flip").split(" "), T = {};
            return b = s(_), _[0].preventDefault && (n.at = "left top"), m = b.width, g = b.height, 
            v = b.offset, y = e.extend({}, v), e.each([ "my", "at" ], function() {
                var e, t, i = (n[this] || "").split(" ");
                1 === i.length && (i = l.test(i[0]) ? i.concat([ "center" ]) : u.test(i[0]) ? [ "center" ].concat(i) : [ "center", "center" ]), 
                i[0] = l.test(i[0]) ? i[0] : "center", i[1] = u.test(i[1]) ? i[1] : "center", e = d.exec(i[0]), 
                t = d.exec(i[1]), T[this] = [ e ? e[0] : 0, t ? t[0] : 0 ], n[this] = [ c.exec(i[0])[0], c.exec(i[1])[0] ];
            }), 1 === k.length && (k[1] = k[0]), "right" === n.at[0] ? y.left += m : "center" === n.at[0] && (y.left += m / 2), 
            "bottom" === n.at[1] ? y.top += g : "center" === n.at[1] && (y.top += g / 2), p = t(T.at, m, g), 
            y.left += p[0], y.top += p[1], this.each(function() {
                var s, l, u = e(this), d = u.outerWidth(), c = u.outerHeight(), f = i(this, "marginLeft"), b = i(this, "marginTop"), D = d + f + i(this, "marginRight") + w.width, S = c + b + i(this, "marginBottom") + w.height, M = e.extend({}, y), C = t(T.my, u.outerWidth(), u.outerHeight());
                "right" === n.my[0] ? M.left -= d : "center" === n.my[0] && (M.left -= d / 2), "bottom" === n.my[1] ? M.top -= c : "center" === n.my[1] && (M.top -= c / 2), 
                M.left += C[0], M.top += C[1], a || (M.left = h(M.left), M.top = h(M.top)), s = {
                    marginLeft: f,
                    marginTop: b
                }, e.each([ "left", "top" ], function(t, i) {
                    e.ui.position[k[t]] && e.ui.position[k[t]][i](M, {
                        targetWidth: m,
                        targetHeight: g,
                        elemWidth: d,
                        elemHeight: c,
                        collisionPosition: s,
                        collisionWidth: D,
                        collisionHeight: S,
                        offset: [ p[0] + C[0], p[1] + C[1] ],
                        my: n.my,
                        at: n.at,
                        within: x,
                        elem: u
                    });
                }), n.using && (l = function(e) {
                    var t = v.left - M.left, i = t + m - d, s = v.top - M.top, a = s + g - c, h = {
                        target: {
                            element: _,
                            left: v.left,
                            top: v.top,
                            width: m,
                            height: g
                        },
                        element: {
                            element: u,
                            left: M.left,
                            top: M.top,
                            width: d,
                            height: c
                        },
                        horizontal: 0 > i ? "left" : t > 0 ? "right" : "center",
                        vertical: 0 > a ? "top" : s > 0 ? "bottom" : "middle"
                    };
                    d > m && m > r(t + i) && (h.horizontal = "center"), c > g && g > r(s + a) && (h.vertical = "middle"), 
                    h.important = o(r(t), r(i)) > o(r(s), r(a)) ? "horizontal" : "vertical", n.using.call(this, e, h);
                }), u.offset(e.extend(M, {
                    using: l
                }));
            });
        }, e.ui.position = {
            fit: {
                left: function(e, t) {
                    var i, s = t.within, n = s.isWindow ? s.scrollLeft : s.offset.left, a = s.width, r = e.left - t.collisionPosition.marginLeft, h = n - r, l = r + t.collisionWidth - a - n;
                    t.collisionWidth > a ? h > 0 && 0 >= l ? (i = e.left + h + t.collisionWidth - a - n, 
                    e.left += h - i) : e.left = l > 0 && 0 >= h ? n : h > l ? n + a - t.collisionWidth : n : h > 0 ? e.left += h : l > 0 ? e.left -= l : e.left = o(e.left - r, e.left);
                },
                top: function(e, t) {
                    var i, s = t.within, n = s.isWindow ? s.scrollTop : s.offset.top, a = t.within.height, r = e.top - t.collisionPosition.marginTop, h = n - r, l = r + t.collisionHeight - a - n;
                    t.collisionHeight > a ? h > 0 && 0 >= l ? (i = e.top + h + t.collisionHeight - a - n, 
                    e.top += h - i) : e.top = l > 0 && 0 >= h ? n : h > l ? n + a - t.collisionHeight : n : h > 0 ? e.top += h : l > 0 ? e.top -= l : e.top = o(e.top - r, e.top);
                }
            },
            flip: {
                left: function(e, t) {
                    var i, s, n = t.within, a = n.offset.left + n.scrollLeft, o = n.width, h = n.isWindow ? n.scrollLeft : n.offset.left, l = e.left - t.collisionPosition.marginLeft, u = l - h, d = l + t.collisionWidth - o - h, c = "left" === t.my[0] ? -t.elemWidth : "right" === t.my[0] ? t.elemWidth : 0, p = "left" === t.at[0] ? t.targetWidth : "right" === t.at[0] ? -t.targetWidth : 0, f = -2 * t.offset[0];
                    0 > u ? (0 > (i = e.left + c + p + f + t.collisionWidth - o - a) || r(u) > i) && (e.left += c + p + f) : d > 0 && ((s = e.left - t.collisionPosition.marginLeft + c + p + f - h) > 0 || d > r(s)) && (e.left += c + p + f);
                },
                top: function(e, t) {
                    var i, s, n = t.within, a = n.offset.top + n.scrollTop, o = n.height, h = n.isWindow ? n.scrollTop : n.offset.top, l = e.top - t.collisionPosition.marginTop, u = l - h, d = l + t.collisionHeight - o - h, c = "top" === t.my[1], p = c ? -t.elemHeight : "bottom" === t.my[1] ? t.elemHeight : 0, f = "top" === t.at[1] ? t.targetHeight : "bottom" === t.at[1] ? -t.targetHeight : 0, m = -2 * t.offset[1];
                    0 > u ? (s = e.top + p + f + m + t.collisionHeight - o - a, e.top + p + f + m > u && (0 > s || r(u) > s) && (e.top += p + f + m)) : d > 0 && (i = e.top - t.collisionPosition.marginTop + p + f + m - h, 
                    e.top + p + f + m > d && (i > 0 || d > r(i)) && (e.top += p + f + m));
                }
            },
            flipfit: {
                left: function() {
                    e.ui.position.flip.left.apply(this, arguments), e.ui.position.fit.left.apply(this, arguments);
                },
                top: function() {
                    e.ui.position.flip.top.apply(this, arguments), e.ui.position.fit.top.apply(this, arguments);
                }
            }
        }, function() {
            var t, i, s, n, o, r = document.getElementsByTagName("body")[0], h = document.createElement("div");
            t = document.createElement(r ? "div" : "body"), s = {
                visibility: "hidden",
                width: 0,
                height: 0,
                border: 0,
                margin: 0,
                background: "none"
            }, r && e.extend(s, {
                position: "absolute",
                left: "-1000px",
                top: "-1000px"
            });
            for (o in s) t.style[o] = s[o];
            t.appendChild(h), i = r || document.documentElement, i.insertBefore(t, i.firstChild), 
            h.style.cssText = "position: absolute; left: 10.7432222px;", n = e(h).offset().left, 
            a = n > 10 && 11 > n, t.innerHTML = "", i.removeChild(t);
        }();
    }(), e.ui.position, e.widget("ui.draggable", e.ui.mouse, {
        version: "1.11.2",
        widgetEventPrefix: "drag",
        options: {
            addClasses: !0,
            appendTo: "parent",
            axis: !1,
            connectToSortable: !1,
            containment: !1,
            cursor: "auto",
            cursorAt: !1,
            grid: !1,
            handle: !1,
            helper: "original",
            iframeFix: !1,
            opacity: !1,
            refreshPositions: !1,
            revert: !1,
            revertDuration: 500,
            scope: "default",
            scroll: !0,
            scrollSensitivity: 20,
            scrollSpeed: 20,
            snap: !1,
            snapMode: "both",
            snapTolerance: 20,
            stack: !1,
            zIndex: !1,
            drag: null,
            start: null,
            stop: null
        },
        _create: function() {
            "original" === this.options.helper && this._setPositionRelative(), this.options.addClasses && this.element.addClass("ui-draggable"), 
            this.options.disabled && this.element.addClass("ui-draggable-disabled"), this._setHandleClassName(), 
            this._mouseInit();
        },
        _setOption: function(e, t) {
            this._super(e, t), "handle" === e && (this._removeHandleClassName(), this._setHandleClassName());
        },
        _destroy: function() {
            return (this.helper || this.element).is(".ui-draggable-dragging") ? void (this.destroyOnClear = !0) : (this.element.removeClass("ui-draggable ui-draggable-dragging ui-draggable-disabled"), 
            this._removeHandleClassName(), void this._mouseDestroy());
        },
        _mouseCapture: function(t) {
            var i = this.options;
            return this._blurActiveElement(t), !(this.helper || i.disabled || e(t.target).closest(".ui-resizable-handle").length > 0) && (this.handle = this._getHandle(t), 
            !!this.handle && (this._blockFrames(!0 === i.iframeFix ? "iframe" : i.iframeFix), 
            !0));
        },
        _blockFrames: function(t) {
            this.iframeBlocks = this.document.find(t).map(function() {
                var t = e(this);
                return e("<div>").css("position", "absolute").appendTo(t.parent()).outerWidth(t.outerWidth()).outerHeight(t.outerHeight()).offset(t.offset())[0];
            });
        },
        _unblockFrames: function() {
            this.iframeBlocks && (this.iframeBlocks.remove(), delete this.iframeBlocks);
        },
        _blurActiveElement: function(t) {
            var i = this.document[0];
            if (this.handleElement.is(t.target)) try {
                i.activeElement && "body" !== i.activeElement.nodeName.toLowerCase() && e(i.activeElement).blur();
            } catch (s) {}
        },
        _mouseStart: function(t) {
            var i = this.options;
            return this.helper = this._createHelper(t), this.helper.addClass("ui-draggable-dragging"), 
            this._cacheHelperProportions(), e.ui.ddmanager && (e.ui.ddmanager.current = this), 
            this._cacheMargins(), this.cssPosition = this.helper.css("position"), this.scrollParent = this.helper.scrollParent(!0), 
            this.offsetParent = this.helper.offsetParent(), this.hasFixedAncestor = this.helper.parents().filter(function() {
                return "fixed" === e(this).css("position");
            }).length > 0, this.positionAbs = this.element.offset(), this._refreshOffsets(t), 
            this.originalPosition = this.position = this._generatePosition(t, !1), this.originalPageX = t.pageX, 
            this.originalPageY = t.pageY, i.cursorAt && this._adjustOffsetFromHelper(i.cursorAt), 
            this._setContainment(), !1 === this._trigger("start", t) ? (this._clear(), !1) : (this._cacheHelperProportions(), 
            e.ui.ddmanager && !i.dropBehaviour && e.ui.ddmanager.prepareOffsets(this, t), this._normalizeRightBottom(), 
            this._mouseDrag(t, !0), e.ui.ddmanager && e.ui.ddmanager.dragStart(this, t), !0);
        },
        _refreshOffsets: function(e) {
            this.offset = {
                top: this.positionAbs.top - this.margins.top,
                left: this.positionAbs.left - this.margins.left,
                scroll: !1,
                parent: this._getParentOffset(),
                relative: this._getRelativeOffset()
            }, this.offset.click = {
                left: e.pageX - this.offset.left,
                top: e.pageY - this.offset.top
            };
        },
        _mouseDrag: function(t, i) {
            if (this.hasFixedAncestor && (this.offset.parent = this._getParentOffset()), this.position = this._generatePosition(t, !0), 
            this.positionAbs = this._convertPositionTo("absolute"), !i) {
                var s = this._uiHash();
                if (!1 === this._trigger("drag", t, s)) return this._mouseUp({}), !1;
                this.position = s.position;
            }
            return this.helper[0].style.left = this.position.left + "px", this.helper[0].style.top = this.position.top + "px", 
            e.ui.ddmanager && e.ui.ddmanager.drag(this, t), !1;
        },
        _mouseStop: function(t) {
            var i = this, s = !1;
            return e.ui.ddmanager && !this.options.dropBehaviour && (s = e.ui.ddmanager.drop(this, t)), 
            this.dropped && (s = this.dropped, this.dropped = !1), "invalid" === this.options.revert && !s || "valid" === this.options.revert && s || !0 === this.options.revert || e.isFunction(this.options.revert) && this.options.revert.call(this.element, s) ? e(this.helper).animate(this.originalPosition, parseInt(this.options.revertDuration, 10), function() {
                !1 !== i._trigger("stop", t) && i._clear();
            }) : !1 !== this._trigger("stop", t) && this._clear(), !1;
        },
        _mouseUp: function(t) {
            return this._unblockFrames(), e.ui.ddmanager && e.ui.ddmanager.dragStop(this, t), 
            this.handleElement.is(t.target) && this.element.focus(), e.ui.mouse.prototype._mouseUp.call(this, t);
        },
        cancel: function() {
            return this.helper.is(".ui-draggable-dragging") ? this._mouseUp({}) : this._clear(), 
            this;
        },
        _getHandle: function(t) {
            return !this.options.handle || !!e(t.target).closest(this.element.find(this.options.handle)).length;
        },
        _setHandleClassName: function() {
            this.handleElement = this.options.handle ? this.element.find(this.options.handle) : this.element, 
            this.handleElement.addClass("ui-draggable-handle");
        },
        _removeHandleClassName: function() {
            this.handleElement.removeClass("ui-draggable-handle");
        },
        _createHelper: function(t) {
            var i = this.options, s = e.isFunction(i.helper), n = s ? e(i.helper.apply(this.element[0], [ t ])) : "clone" === i.helper ? this.element.clone().removeAttr("id") : this.element;
            return n.parents("body").length || n.appendTo("parent" === i.appendTo ? this.element[0].parentNode : i.appendTo), 
            s && n[0] === this.element[0] && this._setPositionRelative(), n[0] === this.element[0] || /(fixed|absolute)/.test(n.css("position")) || n.css("position", "absolute"), 
            n;
        },
        _setPositionRelative: function() {
            /^(?:r|a|f)/.test(this.element.css("position")) || (this.element[0].style.position = "relative");
        },
        _adjustOffsetFromHelper: function(t) {
            "string" == typeof t && (t = t.split(" ")), e.isArray(t) && (t = {
                left: +t[0],
                top: +t[1] || 0
            }), "left" in t && (this.offset.click.left = t.left + this.margins.left), "right" in t && (this.offset.click.left = this.helperProportions.width - t.right + this.margins.left), 
            "top" in t && (this.offset.click.top = t.top + this.margins.top), "bottom" in t && (this.offset.click.top = this.helperProportions.height - t.bottom + this.margins.top);
        },
        _isRootNode: function(e) {
            return /(html|body)/i.test(e.tagName) || e === this.document[0];
        },
        _getParentOffset: function() {
            var t = this.offsetParent.offset(), i = this.document[0];
            return "absolute" === this.cssPosition && this.scrollParent[0] !== i && e.contains(this.scrollParent[0], this.offsetParent[0]) && (t.left += this.scrollParent.scrollLeft(), 
            t.top += this.scrollParent.scrollTop()), this._isRootNode(this.offsetParent[0]) && (t = {
                top: 0,
                left: 0
            }), {
                top: t.top + (parseInt(this.offsetParent.css("borderTopWidth"), 10) || 0),
                left: t.left + (parseInt(this.offsetParent.css("borderLeftWidth"), 10) || 0)
            };
        },
        _getRelativeOffset: function() {
            if ("relative" !== this.cssPosition) return {
                top: 0,
                left: 0
            };
            var e = this.element.position(), t = this._isRootNode(this.scrollParent[0]);
            return {
                top: e.top - (parseInt(this.helper.css("top"), 10) || 0) + (t ? 0 : this.scrollParent.scrollTop()),
                left: e.left - (parseInt(this.helper.css("left"), 10) || 0) + (t ? 0 : this.scrollParent.scrollLeft())
            };
        },
        _cacheMargins: function() {
            this.margins = {
                left: parseInt(this.element.css("marginLeft"), 10) || 0,
                top: parseInt(this.element.css("marginTop"), 10) || 0,
                right: parseInt(this.element.css("marginRight"), 10) || 0,
                bottom: parseInt(this.element.css("marginBottom"), 10) || 0
            };
        },
        _cacheHelperProportions: function() {
            this.helperProportions = {
                width: this.helper.outerWidth(),
                height: this.helper.outerHeight()
            };
        },
        _setContainment: function() {
            var t, i, s, n = this.options, a = this.document[0];
            return this.relativeContainer = null, n.containment ? "window" === n.containment ? void (this.containment = [ e(window).scrollLeft() - this.offset.relative.left - this.offset.parent.left, e(window).scrollTop() - this.offset.relative.top - this.offset.parent.top, e(window).scrollLeft() + e(window).width() - this.helperProportions.width - this.margins.left, e(window).scrollTop() + (e(window).height() || a.body.parentNode.scrollHeight) - this.helperProportions.height - this.margins.top ]) : "document" === n.containment ? void (this.containment = [ 0, 0, e(a).width() - this.helperProportions.width - this.margins.left, (e(a).height() || a.body.parentNode.scrollHeight) - this.helperProportions.height - this.margins.top ]) : n.containment.constructor === Array ? void (this.containment = n.containment) : ("parent" === n.containment && (n.containment = this.helper[0].parentNode), 
            i = e(n.containment), void ((s = i[0]) && (t = /(scroll|auto)/.test(i.css("overflow")), 
            this.containment = [ (parseInt(i.css("borderLeftWidth"), 10) || 0) + (parseInt(i.css("paddingLeft"), 10) || 0), (parseInt(i.css("borderTopWidth"), 10) || 0) + (parseInt(i.css("paddingTop"), 10) || 0), (t ? Math.max(s.scrollWidth, s.offsetWidth) : s.offsetWidth) - (parseInt(i.css("borderRightWidth"), 10) || 0) - (parseInt(i.css("paddingRight"), 10) || 0) - this.helperProportions.width - this.margins.left - this.margins.right, (t ? Math.max(s.scrollHeight, s.offsetHeight) : s.offsetHeight) - (parseInt(i.css("borderBottomWidth"), 10) || 0) - (parseInt(i.css("paddingBottom"), 10) || 0) - this.helperProportions.height - this.margins.top - this.margins.bottom ], 
            this.relativeContainer = i))) : void (this.containment = null);
        },
        _convertPositionTo: function(e, t) {
            t || (t = this.position);
            var i = "absolute" === e ? 1 : -1, s = this._isRootNode(this.scrollParent[0]);
            return {
                top: t.top + this.offset.relative.top * i + this.offset.parent.top * i - ("fixed" === this.cssPosition ? -this.offset.scroll.top : s ? 0 : this.offset.scroll.top) * i,
                left: t.left + this.offset.relative.left * i + this.offset.parent.left * i - ("fixed" === this.cssPosition ? -this.offset.scroll.left : s ? 0 : this.offset.scroll.left) * i
            };
        },
        _generatePosition: function(e, t) {
            var i, s, n, a, o = this.options, r = this._isRootNode(this.scrollParent[0]), h = e.pageX, l = e.pageY;
            return r && this.offset.scroll || (this.offset.scroll = {
                top: this.scrollParent.scrollTop(),
                left: this.scrollParent.scrollLeft()
            }), t && (this.containment && (this.relativeContainer ? (s = this.relativeContainer.offset(), 
            i = [ this.containment[0] + s.left, this.containment[1] + s.top, this.containment[2] + s.left, this.containment[3] + s.top ]) : i = this.containment, 
            e.pageX - this.offset.click.left < i[0] && (h = i[0] + this.offset.click.left), 
            e.pageY - this.offset.click.top < i[1] && (l = i[1] + this.offset.click.top), e.pageX - this.offset.click.left > i[2] && (h = i[2] + this.offset.click.left), 
            e.pageY - this.offset.click.top > i[3] && (l = i[3] + this.offset.click.top)), o.grid && (n = o.grid[1] ? this.originalPageY + Math.round((l - this.originalPageY) / o.grid[1]) * o.grid[1] : this.originalPageY, 
            l = i ? n - this.offset.click.top >= i[1] || n - this.offset.click.top > i[3] ? n : n - this.offset.click.top >= i[1] ? n - o.grid[1] : n + o.grid[1] : n, 
            a = o.grid[0] ? this.originalPageX + Math.round((h - this.originalPageX) / o.grid[0]) * o.grid[0] : this.originalPageX, 
            h = i ? a - this.offset.click.left >= i[0] || a - this.offset.click.left > i[2] ? a : a - this.offset.click.left >= i[0] ? a - o.grid[0] : a + o.grid[0] : a), 
            "y" === o.axis && (h = this.originalPageX), "x" === o.axis && (l = this.originalPageY)), 
            {
                top: l - this.offset.click.top - this.offset.relative.top - this.offset.parent.top + ("fixed" === this.cssPosition ? -this.offset.scroll.top : r ? 0 : this.offset.scroll.top),
                left: h - this.offset.click.left - this.offset.relative.left - this.offset.parent.left + ("fixed" === this.cssPosition ? -this.offset.scroll.left : r ? 0 : this.offset.scroll.left)
            };
        },
        _clear: function() {
            this.helper.removeClass("ui-draggable-dragging"), this.helper[0] === this.element[0] || this.cancelHelperRemoval || this.helper.remove(), 
            this.helper = null, this.cancelHelperRemoval = !1, this.destroyOnClear && this.destroy();
        },
        _normalizeRightBottom: function() {
            "y" !== this.options.axis && "auto" !== this.helper.css("right") && (this.helper.width(this.helper.width()), 
            this.helper.css("right", "auto")), "x" !== this.options.axis && "auto" !== this.helper.css("bottom") && (this.helper.height(this.helper.height()), 
            this.helper.css("bottom", "auto"));
        },
        _trigger: function(t, i, s) {
            return s = s || this._uiHash(), e.ui.plugin.call(this, t, [ i, s, this ], !0), /^(drag|start|stop)/.test(t) && (this.positionAbs = this._convertPositionTo("absolute"), 
            s.offset = this.positionAbs), e.Widget.prototype._trigger.call(this, t, i, s);
        },
        plugins: {},
        _uiHash: function() {
            return {
                helper: this.helper,
                position: this.position,
                originalPosition: this.originalPosition,
                offset: this.positionAbs
            };
        }
    }), e.ui.plugin.add("draggable", "connectToSortable", {
        start: function(t, i, s) {
            var n = e.extend({}, i, {
                item: s.element
            });
            s.sortables = [], e(s.options.connectToSortable).each(function() {
                var i = e(this).sortable("instance");
                i && !i.options.disabled && (s.sortables.push(i), i.refreshPositions(), i._trigger("activate", t, n));
            });
        },
        stop: function(t, i, s) {
            var n = e.extend({}, i, {
                item: s.element
            });
            s.cancelHelperRemoval = !1, e.each(s.sortables, function() {
                var e = this;
                e.isOver ? (e.isOver = 0, s.cancelHelperRemoval = !0, e.cancelHelperRemoval = !1, 
                e._storedCSS = {
                    position: e.placeholder.css("position"),
                    top: e.placeholder.css("top"),
                    left: e.placeholder.css("left")
                }, e._mouseStop(t), e.options.helper = e.options._helper) : (e.cancelHelperRemoval = !0, 
                e._trigger("deactivate", t, n));
            });
        },
        drag: function(t, i, s) {
            e.each(s.sortables, function() {
                var n = !1, a = this;
                a.positionAbs = s.positionAbs, a.helperProportions = s.helperProportions, a.offset.click = s.offset.click, 
                a._intersectsWith(a.containerCache) && (n = !0, e.each(s.sortables, function() {
                    return this.positionAbs = s.positionAbs, this.helperProportions = s.helperProportions, 
                    this.offset.click = s.offset.click, this !== a && this._intersectsWith(this.containerCache) && e.contains(a.element[0], this.element[0]) && (n = !1), 
                    n;
                })), n ? (a.isOver || (a.isOver = 1, a.currentItem = i.helper.appendTo(a.element).data("ui-sortable-item", !0), 
                a.options._helper = a.options.helper, a.options.helper = function() {
                    return i.helper[0];
                }, t.target = a.currentItem[0], a._mouseCapture(t, !0), a._mouseStart(t, !0, !0), 
                a.offset.click.top = s.offset.click.top, a.offset.click.left = s.offset.click.left, 
                a.offset.parent.left -= s.offset.parent.left - a.offset.parent.left, a.offset.parent.top -= s.offset.parent.top - a.offset.parent.top, 
                s._trigger("toSortable", t), s.dropped = a.element, e.each(s.sortables, function() {
                    this.refreshPositions();
                }), s.currentItem = s.element, a.fromOutside = s), a.currentItem && (a._mouseDrag(t), 
                i.position = a.position)) : a.isOver && (a.isOver = 0, a.cancelHelperRemoval = !0, 
                a.options._revert = a.options.revert, a.options.revert = !1, a._trigger("out", t, a._uiHash(a)), 
                a._mouseStop(t, !0), a.options.revert = a.options._revert, a.options.helper = a.options._helper, 
                a.placeholder && a.placeholder.remove(), s._refreshOffsets(t), i.position = s._generatePosition(t, !0), 
                s._trigger("fromSortable", t), s.dropped = !1, e.each(s.sortables, function() {
                    this.refreshPositions();
                }));
            });
        }
    }), e.ui.plugin.add("draggable", "cursor", {
        start: function(t, i, s) {
            var n = e("body"), a = s.options;
            n.css("cursor") && (a._cursor = n.css("cursor")), n.css("cursor", a.cursor);
        },
        stop: function(t, i, s) {
            var n = s.options;
            n._cursor && e("body").css("cursor", n._cursor);
        }
    }), e.ui.plugin.add("draggable", "opacity", {
        start: function(t, i, s) {
            var n = e(i.helper), a = s.options;
            n.css("opacity") && (a._opacity = n.css("opacity")), n.css("opacity", a.opacity);
        },
        stop: function(t, i, s) {
            var n = s.options;
            n._opacity && e(i.helper).css("opacity", n._opacity);
        }
    }), e.ui.plugin.add("draggable", "scroll", {
        start: function(e, t, i) {
            i.scrollParentNotHidden || (i.scrollParentNotHidden = i.helper.scrollParent(!1)), 
            i.scrollParentNotHidden[0] !== i.document[0] && "HTML" !== i.scrollParentNotHidden[0].tagName && (i.overflowOffset = i.scrollParentNotHidden.offset());
        },
        drag: function(t, i, s) {
            var n = s.options, a = !1, o = s.scrollParentNotHidden[0], r = s.document[0];
            o !== r && "HTML" !== o.tagName ? (n.axis && "x" === n.axis || (s.overflowOffset.top + o.offsetHeight - t.pageY < n.scrollSensitivity ? o.scrollTop = a = o.scrollTop + n.scrollSpeed : t.pageY - s.overflowOffset.top < n.scrollSensitivity && (o.scrollTop = a = o.scrollTop - n.scrollSpeed)), 
            n.axis && "y" === n.axis || (s.overflowOffset.left + o.offsetWidth - t.pageX < n.scrollSensitivity ? o.scrollLeft = a = o.scrollLeft + n.scrollSpeed : t.pageX - s.overflowOffset.left < n.scrollSensitivity && (o.scrollLeft = a = o.scrollLeft - n.scrollSpeed))) : (n.axis && "x" === n.axis || (t.pageY - e(r).scrollTop() < n.scrollSensitivity ? a = e(r).scrollTop(e(r).scrollTop() - n.scrollSpeed) : e(window).height() - (t.pageY - e(r).scrollTop()) < n.scrollSensitivity && (a = e(r).scrollTop(e(r).scrollTop() + n.scrollSpeed))), 
            n.axis && "y" === n.axis || (t.pageX - e(r).scrollLeft() < n.scrollSensitivity ? a = e(r).scrollLeft(e(r).scrollLeft() - n.scrollSpeed) : e(window).width() - (t.pageX - e(r).scrollLeft()) < n.scrollSensitivity && (a = e(r).scrollLeft(e(r).scrollLeft() + n.scrollSpeed)))), 
            !1 !== a && e.ui.ddmanager && !n.dropBehaviour && e.ui.ddmanager.prepareOffsets(s, t);
        }
    }), e.ui.plugin.add("draggable", "snap", {
        start: function(t, i, s) {
            var n = s.options;
            s.snapElements = [], e(n.snap.constructor !== String ? n.snap.items || ":data(ui-draggable)" : n.snap).each(function() {
                var t = e(this), i = t.offset();
                this !== s.element[0] && s.snapElements.push({
                    item: this,
                    width: t.outerWidth(),
                    height: t.outerHeight(),
                    top: i.top,
                    left: i.left
                });
            });
        },
        drag: function(t, i, s) {
            var n, a, o, r, h, l, u, d, c, p, f = s.options, m = f.snapTolerance, g = i.offset.left, v = g + s.helperProportions.width, y = i.offset.top, b = y + s.helperProportions.height;
            for (c = s.snapElements.length - 1; c >= 0; c--) h = s.snapElements[c].left - s.margins.left, 
            l = h + s.snapElements[c].width, u = s.snapElements[c].top - s.margins.top, d = u + s.snapElements[c].height, 
            h - m > v || g > l + m || u - m > b || y > d + m || !e.contains(s.snapElements[c].item.ownerDocument, s.snapElements[c].item) ? (s.snapElements[c].snapping && s.options.snap.release && s.options.snap.release.call(s.element, t, e.extend(s._uiHash(), {
                snapItem: s.snapElements[c].item
            })), s.snapElements[c].snapping = !1) : ("inner" !== f.snapMode && (n = m >= Math.abs(u - b), 
            a = m >= Math.abs(d - y), o = m >= Math.abs(h - v), r = m >= Math.abs(l - g), n && (i.position.top = s._convertPositionTo("relative", {
                top: u - s.helperProportions.height,
                left: 0
            }).top), a && (i.position.top = s._convertPositionTo("relative", {
                top: d,
                left: 0
            }).top), o && (i.position.left = s._convertPositionTo("relative", {
                top: 0,
                left: h - s.helperProportions.width
            }).left), r && (i.position.left = s._convertPositionTo("relative", {
                top: 0,
                left: l
            }).left)), p = n || a || o || r, "outer" !== f.snapMode && (n = m >= Math.abs(u - y), 
            a = m >= Math.abs(d - b), o = m >= Math.abs(h - g), r = m >= Math.abs(l - v), n && (i.position.top = s._convertPositionTo("relative", {
                top: u,
                left: 0
            }).top), a && (i.position.top = s._convertPositionTo("relative", {
                top: d - s.helperProportions.height,
                left: 0
            }).top), o && (i.position.left = s._convertPositionTo("relative", {
                top: 0,
                left: h
            }).left), r && (i.position.left = s._convertPositionTo("relative", {
                top: 0,
                left: l - s.helperProportions.width
            }).left)), !s.snapElements[c].snapping && (n || a || o || r || p) && s.options.snap.snap && s.options.snap.snap.call(s.element, t, e.extend(s._uiHash(), {
                snapItem: s.snapElements[c].item
            })), s.snapElements[c].snapping = n || a || o || r || p);
        }
    }), e.ui.plugin.add("draggable", "stack", {
        start: function(t, i, s) {
            var n, a = s.options, o = e.makeArray(e(a.stack)).sort(function(t, i) {
                return (parseInt(e(t).css("zIndex"), 10) || 0) - (parseInt(e(i).css("zIndex"), 10) || 0);
            });
            o.length && (n = parseInt(e(o[0]).css("zIndex"), 10) || 0, e(o).each(function(t) {
                e(this).css("zIndex", n + t);
            }), this.css("zIndex", n + o.length));
        }
    }), e.ui.plugin.add("draggable", "zIndex", {
        start: function(t, i, s) {
            var n = e(i.helper), a = s.options;
            n.css("zIndex") && (a._zIndex = n.css("zIndex")), n.css("zIndex", a.zIndex);
        },
        stop: function(t, i, s) {
            var n = s.options;
            n._zIndex && e(i.helper).css("zIndex", n._zIndex);
        }
    }), e.ui.draggable, e.widget("ui.droppable", {
        version: "1.11.2",
        widgetEventPrefix: "drop",
        options: {
            accept: "*",
            activeClass: !1,
            addClasses: !0,
            greedy: !1,
            hoverClass: !1,
            scope: "default",
            tolerance: "intersect",
            activate: null,
            deactivate: null,
            drop: null,
            out: null,
            over: null
        },
        _create: function() {
            var t, i = this.options, s = i.accept;
            this.isover = !1, this.isout = !0, this.accept = e.isFunction(s) ? s : function(e) {
                return e.is(s);
            }, this.proportions = function() {
                return arguments.length ? void (t = arguments[0]) : t || (t = {
                    width: this.element[0].offsetWidth,
                    height: this.element[0].offsetHeight
                });
            }, this._addToManager(i.scope), i.addClasses && this.element.addClass("ui-droppable");
        },
        _addToManager: function(t) {
            e.ui.ddmanager.droppables[t] = e.ui.ddmanager.droppables[t] || [], e.ui.ddmanager.droppables[t].push(this);
        },
        _splice: function(e) {
            for (var t = 0; e.length > t; t++) e[t] === this && e.splice(t, 1);
        },
        _destroy: function() {
            var t = e.ui.ddmanager.droppables[this.options.scope];
            this._splice(t), this.element.removeClass("ui-droppable ui-droppable-disabled");
        },
        _setOption: function(t, i) {
            if ("accept" === t) this.accept = e.isFunction(i) ? i : function(e) {
                return e.is(i);
            }; else if ("scope" === t) {
                var s = e.ui.ddmanager.droppables[this.options.scope];
                this._splice(s), this._addToManager(i);
            }
            this._super(t, i);
        },
        _activate: function(t) {
            var i = e.ui.ddmanager.current;
            this.options.activeClass && this.element.addClass(this.options.activeClass), i && this._trigger("activate", t, this.ui(i));
        },
        _deactivate: function(t) {
            var i = e.ui.ddmanager.current;
            this.options.activeClass && this.element.removeClass(this.options.activeClass), 
            i && this._trigger("deactivate", t, this.ui(i));
        },
        _over: function(t) {
            var i = e.ui.ddmanager.current;
            i && (i.currentItem || i.element)[0] !== this.element[0] && this.accept.call(this.element[0], i.currentItem || i.element) && (this.options.hoverClass && this.element.addClass(this.options.hoverClass), 
            this._trigger("over", t, this.ui(i)));
        },
        _out: function(t) {
            var i = e.ui.ddmanager.current;
            i && (i.currentItem || i.element)[0] !== this.element[0] && this.accept.call(this.element[0], i.currentItem || i.element) && (this.options.hoverClass && this.element.removeClass(this.options.hoverClass), 
            this._trigger("out", t, this.ui(i)));
        },
        _drop: function(t, i) {
            var s = i || e.ui.ddmanager.current, n = !1;
            return !(!s || (s.currentItem || s.element)[0] === this.element[0]) && (this.element.find(":data(ui-droppable)").not(".ui-draggable-dragging").each(function() {
                var i = e(this).droppable("instance");
                return i.options.greedy && !i.options.disabled && i.options.scope === s.options.scope && i.accept.call(i.element[0], s.currentItem || s.element) && e.ui.intersect(s, e.extend(i, {
                    offset: i.element.offset()
                }), i.options.tolerance, t) ? (n = !0, !1) : void 0;
            }), !n && (!!this.accept.call(this.element[0], s.currentItem || s.element) && (this.options.activeClass && this.element.removeClass(this.options.activeClass), 
            this.options.hoverClass && this.element.removeClass(this.options.hoverClass), this._trigger("drop", t, this.ui(s)), 
            this.element)));
        },
        ui: function(e) {
            return {
                draggable: e.currentItem || e.element,
                helper: e.helper,
                position: e.position,
                offset: e.positionAbs
            };
        }
    }), e.ui.intersect = function() {
        function e(e, t, i) {
            return e >= t && t + i > e;
        }
        return function(t, i, s, n) {
            if (!i.offset) return !1;
            var a = (t.positionAbs || t.position.absolute).left + t.margins.left, o = (t.positionAbs || t.position.absolute).top + t.margins.top, r = a + t.helperProportions.width, h = o + t.helperProportions.height, l = i.offset.left, u = i.offset.top, d = l + i.proportions().width, c = u + i.proportions().height;
            switch (s) {
              case "fit":
                return a >= l && d >= r && o >= u && c >= h;

              case "intersect":
                return a + t.helperProportions.width / 2 > l && d > r - t.helperProportions.width / 2 && o + t.helperProportions.height / 2 > u && c > h - t.helperProportions.height / 2;

              case "pointer":
                return e(n.pageY, u, i.proportions().height) && e(n.pageX, l, i.proportions().width);

              case "touch":
                return (o >= u && c >= o || h >= u && c >= h || u > o && h > c) && (a >= l && d >= a || r >= l && d >= r || l > a && r > d);

              default:
                return !1;
            }
        };
    }(), e.ui.ddmanager = {
        current: null,
        droppables: {
            default: []
        },
        prepareOffsets: function(t, i) {
            var s, n, a = e.ui.ddmanager.droppables[t.options.scope] || [], o = i ? i.type : null, r = (t.currentItem || t.element).find(":data(ui-droppable)").addBack();
            e: for (s = 0; a.length > s; s++) if (!(a[s].options.disabled || t && !a[s].accept.call(a[s].element[0], t.currentItem || t.element))) {
                for (n = 0; r.length > n; n++) if (r[n] === a[s].element[0]) {
                    a[s].proportions().height = 0;
                    continue e;
                }
                a[s].visible = "none" !== a[s].element.css("display"), a[s].visible && ("mousedown" === o && a[s]._activate.call(a[s], i), 
                a[s].offset = a[s].element.offset(), a[s].proportions({
                    width: a[s].element[0].offsetWidth,
                    height: a[s].element[0].offsetHeight
                }));
            }
        },
        drop: function(t, i) {
            var s = !1;
            return e.each((e.ui.ddmanager.droppables[t.options.scope] || []).slice(), function() {
                this.options && (!this.options.disabled && this.visible && e.ui.intersect(t, this, this.options.tolerance, i) && (s = this._drop.call(this, i) || s), 
                !this.options.disabled && this.visible && this.accept.call(this.element[0], t.currentItem || t.element) && (this.isout = !0, 
                this.isover = !1, this._deactivate.call(this, i)));
            }), s;
        },
        dragStart: function(t, i) {
            t.element.parentsUntil("body").bind("scroll.droppable", function() {
                t.options.refreshPositions || e.ui.ddmanager.prepareOffsets(t, i);
            });
        },
        drag: function(t, i) {
            t.options.refreshPositions && e.ui.ddmanager.prepareOffsets(t, i), e.each(e.ui.ddmanager.droppables[t.options.scope] || [], function() {
                if (!this.options.disabled && !this.greedyChild && this.visible) {
                    var s, n, a, o = e.ui.intersect(t, this, this.options.tolerance, i), r = !o && this.isover ? "isout" : o && !this.isover ? "isover" : null;
                    r && (this.options.greedy && (n = this.options.scope, a = this.element.parents(":data(ui-droppable)").filter(function() {
                        return e(this).droppable("instance").options.scope === n;
                    }), a.length && (s = e(a[0]).droppable("instance"), s.greedyChild = "isover" === r)), 
                    s && "isover" === r && (s.isover = !1, s.isout = !0, s._out.call(s, i)), this[r] = !0, 
                    this["isout" === r ? "isover" : "isout"] = !1, this["isover" === r ? "_over" : "_out"].call(this, i), 
                    s && "isout" === r && (s.isout = !1, s.isover = !0, s._over.call(s, i)));
                }
            });
        },
        dragStop: function(t, i) {
            t.element.parentsUntil("body").unbind("scroll.droppable"), t.options.refreshPositions || e.ui.ddmanager.prepareOffsets(t, i);
        }
    }, e.ui.droppable, e.widget("ui.resizable", e.ui.mouse, {
        version: "1.11.2",
        widgetEventPrefix: "resize",
        options: {
            alsoResize: !1,
            animate: !1,
            animateDuration: "slow",
            animateEasing: "swing",
            aspectRatio: !1,
            autoHide: !1,
            containment: !1,
            ghost: !1,
            grid: !1,
            handles: "e,s,se",
            helper: !1,
            maxHeight: null,
            maxWidth: null,
            minHeight: 10,
            minWidth: 10,
            zIndex: 90,
            resize: null,
            start: null,
            stop: null
        },
        _num: function(e) {
            return parseInt(e, 10) || 0;
        },
        _isNumber: function(e) {
            return !isNaN(parseInt(e, 10));
        },
        _hasScroll: function(t, i) {
            if ("hidden" === e(t).css("overflow")) return !1;
            var s = i && "left" === i ? "scrollLeft" : "scrollTop", n = !1;
            return t[s] > 0 || (t[s] = 1, n = t[s] > 0, t[s] = 0, n);
        },
        _create: function() {
            var t, i, s, n, a, o = this, r = this.options;
            if (this.element.addClass("ui-resizable"), e.extend(this, {
                _aspectRatio: !!r.aspectRatio,
                aspectRatio: r.aspectRatio,
                originalElement: this.element,
                _proportionallyResizeElements: [],
                _helper: r.helper || r.ghost || r.animate ? r.helper || "ui-resizable-helper" : null
            }), this.element[0].nodeName.match(/canvas|textarea|input|select|button|img/i) && (this.element.wrap(e("<div class='ui-wrapper' style='overflow: hidden;'></div>").css({
                position: this.element.css("position"),
                width: this.element.outerWidth(),
                height: this.element.outerHeight(),
                top: this.element.css("top"),
                left: this.element.css("left")
            })), this.element = this.element.parent().data("ui-resizable", this.element.resizable("instance")), 
            this.elementIsWrapper = !0, this.element.css({
                marginLeft: this.originalElement.css("marginLeft"),
                marginTop: this.originalElement.css("marginTop"),
                marginRight: this.originalElement.css("marginRight"),
                marginBottom: this.originalElement.css("marginBottom")
            }), this.originalElement.css({
                marginLeft: 0,
                marginTop: 0,
                marginRight: 0,
                marginBottom: 0
            }), this.originalResizeStyle = this.originalElement.css("resize"), this.originalElement.css("resize", "none"), 
            this._proportionallyResizeElements.push(this.originalElement.css({
                position: "static",
                zoom: 1,
                display: "block"
            })), this.originalElement.css({
                margin: this.originalElement.css("margin")
            }), this._proportionallyResize()), this.handles = r.handles || (e(".ui-resizable-handle", this.element).length ? {
                n: ".ui-resizable-n",
                e: ".ui-resizable-e",
                s: ".ui-resizable-s",
                w: ".ui-resizable-w",
                se: ".ui-resizable-se",
                sw: ".ui-resizable-sw",
                ne: ".ui-resizable-ne",
                nw: ".ui-resizable-nw"
            } : "e,s,se"), this.handles.constructor === String) for ("all" === this.handles && (this.handles = "n,e,s,w,se,sw,ne,nw"), 
            t = this.handles.split(","), this.handles = {}, i = 0; t.length > i; i++) s = e.trim(t[i]), 
            a = "ui-resizable-" + s, n = e("<div class='ui-resizable-handle " + a + "'></div>"), 
            n.css({
                zIndex: r.zIndex
            }), "se" === s && n.addClass("ui-icon ui-icon-gripsmall-diagonal-se"), this.handles[s] = ".ui-resizable-" + s, 
            this.element.append(n);
            this._renderAxis = function(t) {
                var i, s, n, a;
                t = t || this.element;
                for (i in this.handles) this.handles[i].constructor === String && (this.handles[i] = this.element.children(this.handles[i]).first().show()), 
                this.elementIsWrapper && this.originalElement[0].nodeName.match(/textarea|input|select|button/i) && (s = e(this.handles[i], this.element), 
                a = /sw|ne|nw|se|n|s/.test(i) ? s.outerHeight() : s.outerWidth(), n = [ "padding", /ne|nw|n/.test(i) ? "Top" : /se|sw|s/.test(i) ? "Bottom" : /^e$/.test(i) ? "Right" : "Left" ].join(""), 
                t.css(n, a), this._proportionallyResize()), e(this.handles[i]).length;
            }, this._renderAxis(this.element), this._handles = e(".ui-resizable-handle", this.element).disableSelection(), 
            this._handles.mouseover(function() {
                o.resizing || (this.className && (n = this.className.match(/ui-resizable-(se|sw|ne|nw|n|e|s|w)/i)), 
                o.axis = n && n[1] ? n[1] : "se");
            }), r.autoHide && (this._handles.hide(), e(this.element).addClass("ui-resizable-autohide").mouseenter(function() {
                r.disabled || (e(this).removeClass("ui-resizable-autohide"), o._handles.show());
            }).mouseleave(function() {
                r.disabled || o.resizing || (e(this).addClass("ui-resizable-autohide"), o._handles.hide());
            })), this._mouseInit();
        },
        _destroy: function() {
            this._mouseDestroy();
            var t, i = function(t) {
                e(t).removeClass("ui-resizable ui-resizable-disabled ui-resizable-resizing").removeData("resizable").removeData("ui-resizable").unbind(".resizable").find(".ui-resizable-handle").remove();
            };
            return this.elementIsWrapper && (i(this.element), t = this.element, this.originalElement.css({
                position: t.css("position"),
                width: t.outerWidth(),
                height: t.outerHeight(),
                top: t.css("top"),
                left: t.css("left")
            }).insertAfter(t), t.remove()), this.originalElement.css("resize", this.originalResizeStyle), 
            i(this.originalElement), this;
        },
        _mouseCapture: function(t) {
            var i, s, n = !1;
            for (i in this.handles) ((s = e(this.handles[i])[0]) === t.target || e.contains(s, t.target)) && (n = !0);
            return !this.options.disabled && n;
        },
        _mouseStart: function(t) {
            var i, s, n, a = this.options, o = this.element;
            return this.resizing = !0, this._renderProxy(), i = this._num(this.helper.css("left")), 
            s = this._num(this.helper.css("top")), a.containment && (i += e(a.containment).scrollLeft() || 0, 
            s += e(a.containment).scrollTop() || 0), this.offset = this.helper.offset(), this.position = {
                left: i,
                top: s
            }, this.size = this._helper ? {
                width: this.helper.width(),
                height: this.helper.height()
            } : {
                width: o.width(),
                height: o.height()
            }, this.originalSize = this._helper ? {
                width: o.outerWidth(),
                height: o.outerHeight()
            } : {
                width: o.width(),
                height: o.height()
            }, this.sizeDiff = {
                width: o.outerWidth() - o.width(),
                height: o.outerHeight() - o.height()
            }, this.originalPosition = {
                left: i,
                top: s
            }, this.originalMousePosition = {
                left: t.pageX,
                top: t.pageY
            }, this.aspectRatio = "number" == typeof a.aspectRatio ? a.aspectRatio : this.originalSize.width / this.originalSize.height || 1, 
            n = e(".ui-resizable-" + this.axis).css("cursor"), e("body").css("cursor", "auto" === n ? this.axis + "-resize" : n), 
            o.addClass("ui-resizable-resizing"), this._propagate("start", t), !0;
        },
        _mouseDrag: function(t) {
            var i, s, n = this.originalMousePosition, a = this.axis, o = t.pageX - n.left || 0, r = t.pageY - n.top || 0, h = this._change[a];
            return this._updatePrevProperties(), !!h && (i = h.apply(this, [ t, o, r ]), this._updateVirtualBoundaries(t.shiftKey), 
            (this._aspectRatio || t.shiftKey) && (i = this._updateRatio(i, t)), i = this._respectSize(i, t), 
            this._updateCache(i), this._propagate("resize", t), s = this._applyChanges(), !this._helper && this._proportionallyResizeElements.length && this._proportionallyResize(), 
            e.isEmptyObject(s) || (this._updatePrevProperties(), this._trigger("resize", t, this.ui()), 
            this._applyChanges()), !1);
        },
        _mouseStop: function(t) {
            this.resizing = !1;
            var i, s, n, a, o, r, h, l = this.options, u = this;
            return this._helper && (i = this._proportionallyResizeElements, s = i.length && /textarea/i.test(i[0].nodeName), 
            n = s && this._hasScroll(i[0], "left") ? 0 : u.sizeDiff.height, a = s ? 0 : u.sizeDiff.width, 
            o = {
                width: u.helper.width() - a,
                height: u.helper.height() - n
            }, r = parseInt(u.element.css("left"), 10) + (u.position.left - u.originalPosition.left) || null, 
            h = parseInt(u.element.css("top"), 10) + (u.position.top - u.originalPosition.top) || null, 
            l.animate || this.element.css(e.extend(o, {
                top: h,
                left: r
            })), u.helper.height(u.size.height), u.helper.width(u.size.width), this._helper && !l.animate && this._proportionallyResize()), 
            e("body").css("cursor", "auto"), this.element.removeClass("ui-resizable-resizing"), 
            this._propagate("stop", t), this._helper && this.helper.remove(), !1;
        },
        _updatePrevProperties: function() {
            this.prevPosition = {
                top: this.position.top,
                left: this.position.left
            }, this.prevSize = {
                width: this.size.width,
                height: this.size.height
            };
        },
        _applyChanges: function() {
            var e = {};
            return this.position.top !== this.prevPosition.top && (e.top = this.position.top + "px"), 
            this.position.left !== this.prevPosition.left && (e.left = this.position.left + "px"), 
            this.size.width !== this.prevSize.width && (e.width = this.size.width + "px"), this.size.height !== this.prevSize.height && (e.height = this.size.height + "px"), 
            this.helper.css(e), e;
        },
        _updateVirtualBoundaries: function(e) {
            var t, i, s, n, a, o = this.options;
            a = {
                minWidth: this._isNumber(o.minWidth) ? o.minWidth : 0,
                maxWidth: this._isNumber(o.maxWidth) ? o.maxWidth : 1 / 0,
                minHeight: this._isNumber(o.minHeight) ? o.minHeight : 0,
                maxHeight: this._isNumber(o.maxHeight) ? o.maxHeight : 1 / 0
            }, (this._aspectRatio || e) && (t = a.minHeight * this.aspectRatio, s = a.minWidth / this.aspectRatio, 
            i = a.maxHeight * this.aspectRatio, n = a.maxWidth / this.aspectRatio, t > a.minWidth && (a.minWidth = t), 
            s > a.minHeight && (a.minHeight = s), a.maxWidth > i && (a.maxWidth = i), a.maxHeight > n && (a.maxHeight = n)), 
            this._vBoundaries = a;
        },
        _updateCache: function(e) {
            this.offset = this.helper.offset(), this._isNumber(e.left) && (this.position.left = e.left), 
            this._isNumber(e.top) && (this.position.top = e.top), this._isNumber(e.height) && (this.size.height = e.height), 
            this._isNumber(e.width) && (this.size.width = e.width);
        },
        _updateRatio: function(e) {
            var t = this.position, i = this.size, s = this.axis;
            return this._isNumber(e.height) ? e.width = e.height * this.aspectRatio : this._isNumber(e.width) && (e.height = e.width / this.aspectRatio), 
            "sw" === s && (e.left = t.left + (i.width - e.width), e.top = null), "nw" === s && (e.top = t.top + (i.height - e.height), 
            e.left = t.left + (i.width - e.width)), e;
        },
        _respectSize: function(e) {
            var t = this._vBoundaries, i = this.axis, s = this._isNumber(e.width) && t.maxWidth && t.maxWidth < e.width, n = this._isNumber(e.height) && t.maxHeight && t.maxHeight < e.height, a = this._isNumber(e.width) && t.minWidth && t.minWidth > e.width, o = this._isNumber(e.height) && t.minHeight && t.minHeight > e.height, r = this.originalPosition.left + this.originalSize.width, h = this.position.top + this.size.height, l = /sw|nw|w/.test(i), u = /nw|ne|n/.test(i);
            return a && (e.width = t.minWidth), o && (e.height = t.minHeight), s && (e.width = t.maxWidth), 
            n && (e.height = t.maxHeight), a && l && (e.left = r - t.minWidth), s && l && (e.left = r - t.maxWidth), 
            o && u && (e.top = h - t.minHeight), n && u && (e.top = h - t.maxHeight), e.width || e.height || e.left || !e.top ? e.width || e.height || e.top || !e.left || (e.left = null) : e.top = null, 
            e;
        },
        _getPaddingPlusBorderDimensions: function(e) {
            for (var t = 0, i = [], s = [ e.css("borderTopWidth"), e.css("borderRightWidth"), e.css("borderBottomWidth"), e.css("borderLeftWidth") ], n = [ e.css("paddingTop"), e.css("paddingRight"), e.css("paddingBottom"), e.css("paddingLeft") ]; 4 > t; t++) i[t] = parseInt(s[t], 10) || 0, 
            i[t] += parseInt(n[t], 10) || 0;
            return {
                height: i[0] + i[2],
                width: i[1] + i[3]
            };
        },
        _proportionallyResize: function() {
            if (this._proportionallyResizeElements.length) for (var e, t = 0, i = this.helper || this.element; this._proportionallyResizeElements.length > t; t++) e = this._proportionallyResizeElements[t], 
            this.outerDimensions || (this.outerDimensions = this._getPaddingPlusBorderDimensions(e)), 
            e.css({
                height: i.height() - this.outerDimensions.height || 0,
                width: i.width() - this.outerDimensions.width || 0
            });
        },
        _renderProxy: function() {
            var t = this.element, i = this.options;
            this.elementOffset = t.offset(), this._helper ? (this.helper = this.helper || e("<div style='overflow:hidden;'></div>"), 
            this.helper.addClass(this._helper).css({
                width: this.element.outerWidth() - 1,
                height: this.element.outerHeight() - 1,
                position: "absolute",
                left: this.elementOffset.left + "px",
                top: this.elementOffset.top + "px",
                zIndex: ++i.zIndex
            }), this.helper.appendTo("body").disableSelection()) : this.helper = this.element;
        },
        _change: {
            e: function(e, t) {
                return {
                    width: this.originalSize.width + t
                };
            },
            w: function(e, t) {
                var i = this.originalSize;
                return {
                    left: this.originalPosition.left + t,
                    width: i.width - t
                };
            },
            n: function(e, t, i) {
                var s = this.originalSize;
                return {
                    top: this.originalPosition.top + i,
                    height: s.height - i
                };
            },
            s: function(e, t, i) {
                return {
                    height: this.originalSize.height + i
                };
            },
            se: function(t, i, s) {
                return e.extend(this._change.s.apply(this, arguments), this._change.e.apply(this, [ t, i, s ]));
            },
            sw: function(t, i, s) {
                return e.extend(this._change.s.apply(this, arguments), this._change.w.apply(this, [ t, i, s ]));
            },
            ne: function(t, i, s) {
                return e.extend(this._change.n.apply(this, arguments), this._change.e.apply(this, [ t, i, s ]));
            },
            nw: function(t, i, s) {
                return e.extend(this._change.n.apply(this, arguments), this._change.w.apply(this, [ t, i, s ]));
            }
        },
        _propagate: function(t, i) {
            e.ui.plugin.call(this, t, [ i, this.ui() ]), "resize" !== t && this._trigger(t, i, this.ui());
        },
        plugins: {},
        ui: function() {
            return {
                originalElement: this.originalElement,
                element: this.element,
                helper: this.helper,
                position: this.position,
                size: this.size,
                originalSize: this.originalSize,
                originalPosition: this.originalPosition
            };
        }
    }), e.ui.plugin.add("resizable", "animate", {
        stop: function(t) {
            var i = e(this).resizable("instance"), s = i.options, n = i._proportionallyResizeElements, a = n.length && /textarea/i.test(n[0].nodeName), o = a && i._hasScroll(n[0], "left") ? 0 : i.sizeDiff.height, r = a ? 0 : i.sizeDiff.width, h = {
                width: i.size.width - r,
                height: i.size.height - o
            }, l = parseInt(i.element.css("left"), 10) + (i.position.left - i.originalPosition.left) || null, u = parseInt(i.element.css("top"), 10) + (i.position.top - i.originalPosition.top) || null;
            i.element.animate(e.extend(h, u && l ? {
                top: u,
                left: l
            } : {}), {
                duration: s.animateDuration,
                easing: s.animateEasing,
                step: function() {
                    var s = {
                        width: parseInt(i.element.css("width"), 10),
                        height: parseInt(i.element.css("height"), 10),
                        top: parseInt(i.element.css("top"), 10),
                        left: parseInt(i.element.css("left"), 10)
                    };
                    n && n.length && e(n[0]).css({
                        width: s.width,
                        height: s.height
                    }), i._updateCache(s), i._propagate("resize", t);
                }
            });
        }
    }), e.ui.plugin.add("resizable", "containment", {
        start: function() {
            var t, i, s, n, a, o, r, h = e(this).resizable("instance"), l = h.options, u = h.element, d = l.containment, c = d instanceof e ? d.get(0) : /parent/.test(d) ? u.parent().get(0) : d;
            c && (h.containerElement = e(c), /document/.test(d) || d === document ? (h.containerOffset = {
                left: 0,
                top: 0
            }, h.containerPosition = {
                left: 0,
                top: 0
            }, h.parentData = {
                element: e(document),
                left: 0,
                top: 0,
                width: e(document).width(),
                height: e(document).height() || document.body.parentNode.scrollHeight
            }) : (t = e(c), i = [], e([ "Top", "Right", "Left", "Bottom" ]).each(function(e, s) {
                i[e] = h._num(t.css("padding" + s));
            }), h.containerOffset = t.offset(), h.containerPosition = t.position(), h.containerSize = {
                height: t.innerHeight() - i[3],
                width: t.innerWidth() - i[1]
            }, s = h.containerOffset, n = h.containerSize.height, a = h.containerSize.width, 
            o = h._hasScroll(c, "left") ? c.scrollWidth : a, r = h._hasScroll(c) ? c.scrollHeight : n, 
            h.parentData = {
                element: c,
                left: s.left,
                top: s.top,
                width: o,
                height: r
            }));
        },
        resize: function(t) {
            var i, s, n, a, o = e(this).resizable("instance"), r = o.options, h = o.containerOffset, l = o.position, u = o._aspectRatio || t.shiftKey, d = {
                top: 0,
                left: 0
            }, c = o.containerElement, p = !0;
            c[0] !== document && /static/.test(c.css("position")) && (d = h), l.left < (o._helper ? h.left : 0) && (o.size.width = o.size.width + (o._helper ? o.position.left - h.left : o.position.left - d.left), 
            u && (o.size.height = o.size.width / o.aspectRatio, p = !1), o.position.left = r.helper ? h.left : 0), 
            l.top < (o._helper ? h.top : 0) && (o.size.height = o.size.height + (o._helper ? o.position.top - h.top : o.position.top), 
            u && (o.size.width = o.size.height * o.aspectRatio, p = !1), o.position.top = o._helper ? h.top : 0), 
            n = o.containerElement.get(0) === o.element.parent().get(0), a = /relative|absolute/.test(o.containerElement.css("position")), 
            n && a ? (o.offset.left = o.parentData.left + o.position.left, o.offset.top = o.parentData.top + o.position.top) : (o.offset.left = o.element.offset().left, 
            o.offset.top = o.element.offset().top), i = Math.abs(o.sizeDiff.width + (o._helper ? o.offset.left - d.left : o.offset.left - h.left)), 
            s = Math.abs(o.sizeDiff.height + (o._helper ? o.offset.top - d.top : o.offset.top - h.top)), 
            i + o.size.width >= o.parentData.width && (o.size.width = o.parentData.width - i, 
            u && (o.size.height = o.size.width / o.aspectRatio, p = !1)), s + o.size.height >= o.parentData.height && (o.size.height = o.parentData.height - s, 
            u && (o.size.width = o.size.height * o.aspectRatio, p = !1)), p || (o.position.left = o.prevPosition.left, 
            o.position.top = o.prevPosition.top, o.size.width = o.prevSize.width, o.size.height = o.prevSize.height);
        },
        stop: function() {
            var t = e(this).resizable("instance"), i = t.options, s = t.containerOffset, n = t.containerPosition, a = t.containerElement, o = e(t.helper), r = o.offset(), h = o.outerWidth() - t.sizeDiff.width, l = o.outerHeight() - t.sizeDiff.height;
            t._helper && !i.animate && /relative/.test(a.css("position")) && e(this).css({
                left: r.left - n.left - s.left,
                width: h,
                height: l
            }), t._helper && !i.animate && /static/.test(a.css("position")) && e(this).css({
                left: r.left - n.left - s.left,
                width: h,
                height: l
            });
        }
    }), e.ui.plugin.add("resizable", "alsoResize", {
        start: function() {
            var t = e(this).resizable("instance"), i = t.options, s = function(t) {
                e(t).each(function() {
                    var t = e(this);
                    t.data("ui-resizable-alsoresize", {
                        width: parseInt(t.width(), 10),
                        height: parseInt(t.height(), 10),
                        left: parseInt(t.css("left"), 10),
                        top: parseInt(t.css("top"), 10)
                    });
                });
            };
            "object" != typeof i.alsoResize || i.alsoResize.parentNode ? s(i.alsoResize) : i.alsoResize.length ? (i.alsoResize = i.alsoResize[0], 
            s(i.alsoResize)) : e.each(i.alsoResize, function(e) {
                s(e);
            });
        },
        resize: function(t, i) {
            var s = e(this).resizable("instance"), n = s.options, a = s.originalSize, o = s.originalPosition, r = {
                height: s.size.height - a.height || 0,
                width: s.size.width - a.width || 0,
                top: s.position.top - o.top || 0,
                left: s.position.left - o.left || 0
            }, h = function(t, s) {
                e(t).each(function() {
                    var t = e(this), n = e(this).data("ui-resizable-alsoresize"), a = {}, o = s && s.length ? s : t.parents(i.originalElement[0]).length ? [ "width", "height" ] : [ "width", "height", "top", "left" ];
                    e.each(o, function(e, t) {
                        var i = (n[t] || 0) + (r[t] || 0);
                        i && i >= 0 && (a[t] = i || null);
                    }), t.css(a);
                });
            };
            "object" != typeof n.alsoResize || n.alsoResize.nodeType ? h(n.alsoResize) : e.each(n.alsoResize, function(e, t) {
                h(e, t);
            });
        },
        stop: function() {
            e(this).removeData("resizable-alsoresize");
        }
    }), e.ui.plugin.add("resizable", "ghost", {
        start: function() {
            var t = e(this).resizable("instance"), i = t.options, s = t.size;
            t.ghost = t.originalElement.clone(), t.ghost.css({
                opacity: .25,
                display: "block",
                position: "relative",
                height: s.height,
                width: s.width,
                margin: 0,
                left: 0,
                top: 0
            }).addClass("ui-resizable-ghost").addClass("string" == typeof i.ghost ? i.ghost : ""), 
            t.ghost.appendTo(t.helper);
        },
        resize: function() {
            var t = e(this).resizable("instance");
            t.ghost && t.ghost.css({
                position: "relative",
                height: t.size.height,
                width: t.size.width
            });
        },
        stop: function() {
            var t = e(this).resizable("instance");
            t.ghost && t.helper && t.helper.get(0).removeChild(t.ghost.get(0));
        }
    }), e.ui.plugin.add("resizable", "grid", {
        resize: function() {
            var t, i = e(this).resizable("instance"), s = i.options, n = i.size, a = i.originalSize, o = i.originalPosition, r = i.axis, h = "number" == typeof s.grid ? [ s.grid, s.grid ] : s.grid, l = h[0] || 1, u = h[1] || 1, d = Math.round((n.width - a.width) / l) * l, c = Math.round((n.height - a.height) / u) * u, p = a.width + d, f = a.height + c, m = s.maxWidth && p > s.maxWidth, g = s.maxHeight && f > s.maxHeight, v = s.minWidth && s.minWidth > p, y = s.minHeight && s.minHeight > f;
            s.grid = h, v && (p += l), y && (f += u), m && (p -= l), g && (f -= u), /^(se|s|e)$/.test(r) ? (i.size.width = p, 
            i.size.height = f) : /^(ne)$/.test(r) ? (i.size.width = p, i.size.height = f, i.position.top = o.top - c) : /^(sw)$/.test(r) ? (i.size.width = p, 
            i.size.height = f, i.position.left = o.left - d) : ((0 >= f - u || 0 >= p - l) && (t = i._getPaddingPlusBorderDimensions(this)), 
            f - u > 0 ? (i.size.height = f, i.position.top = o.top - c) : (f = u - t.height, 
            i.size.height = f, i.position.top = o.top + a.height - f), p - l > 0 ? (i.size.width = p, 
            i.position.left = o.left - d) : (p = u - t.height, i.size.width = p, i.position.left = o.left + a.width - p));
        }
    }), e.ui.resizable, e.widget("ui.selectable", e.ui.mouse, {
        version: "1.11.2",
        options: {
            appendTo: "body",
            autoRefresh: !0,
            distance: 0,
            filter: "*",
            tolerance: "touch",
            selected: null,
            selecting: null,
            start: null,
            stop: null,
            unselected: null,
            unselecting: null
        },
        _create: function() {
            var t, i = this;
            this.element.addClass("ui-selectable"), this.dragged = !1, this.refresh = function() {
                t = e(i.options.filter, i.element[0]), t.addClass("ui-selectee"), t.each(function() {
                    var t = e(this), i = t.offset();
                    e.data(this, "selectable-item", {
                        element: this,
                        $element: t,
                        left: i.left,
                        top: i.top,
                        right: i.left + t.outerWidth(),
                        bottom: i.top + t.outerHeight(),
                        startselected: !1,
                        selected: t.hasClass("ui-selected"),
                        selecting: t.hasClass("ui-selecting"),
                        unselecting: t.hasClass("ui-unselecting")
                    });
                });
            }, this.refresh(), this.selectees = t.addClass("ui-selectee"), this._mouseInit(), 
            this.helper = e("<div class='ui-selectable-helper'></div>");
        },
        _destroy: function() {
            this.selectees.removeClass("ui-selectee").removeData("selectable-item"), this.element.removeClass("ui-selectable ui-selectable-disabled"), 
            this._mouseDestroy();
        },
        _mouseStart: function(t) {
            var i = this, s = this.options;
            this.opos = [ t.pageX, t.pageY ], this.options.disabled || (this.selectees = e(s.filter, this.element[0]), 
            this._trigger("start", t), e(s.appendTo).append(this.helper), this.helper.css({
                left: t.pageX,
                top: t.pageY,
                width: 0,
                height: 0
            }), s.autoRefresh && this.refresh(), this.selectees.filter(".ui-selected").each(function() {
                var s = e.data(this, "selectable-item");
                s.startselected = !0, t.metaKey || t.ctrlKey || (s.$element.removeClass("ui-selected"), 
                s.selected = !1, s.$element.addClass("ui-unselecting"), s.unselecting = !0, i._trigger("unselecting", t, {
                    unselecting: s.element
                }));
            }), e(t.target).parents().addBack().each(function() {
                var s, n = e.data(this, "selectable-item");
                return n ? (s = !t.metaKey && !t.ctrlKey || !n.$element.hasClass("ui-selected"), 
                n.$element.removeClass(s ? "ui-unselecting" : "ui-selected").addClass(s ? "ui-selecting" : "ui-unselecting"), 
                n.unselecting = !s, n.selecting = s, n.selected = s, s ? i._trigger("selecting", t, {
                    selecting: n.element
                }) : i._trigger("unselecting", t, {
                    unselecting: n.element
                }), !1) : void 0;
            }));
        },
        _mouseDrag: function(t) {
            if (this.dragged = !0, !this.options.disabled) {
                var i, s = this, n = this.options, a = this.opos[0], o = this.opos[1], r = t.pageX, h = t.pageY;
                return a > r && (i = r, r = a, a = i), o > h && (i = h, h = o, o = i), this.helper.css({
                    left: a,
                    top: o,
                    width: r - a,
                    height: h - o
                }), this.selectees.each(function() {
                    var i = e.data(this, "selectable-item"), l = !1;
                    i && i.element !== s.element[0] && ("touch" === n.tolerance ? l = !(i.left > r || a > i.right || i.top > h || o > i.bottom) : "fit" === n.tolerance && (l = i.left > a && r > i.right && i.top > o && h > i.bottom), 
                    l ? (i.selected && (i.$element.removeClass("ui-selected"), i.selected = !1), i.unselecting && (i.$element.removeClass("ui-unselecting"), 
                    i.unselecting = !1), i.selecting || (i.$element.addClass("ui-selecting"), i.selecting = !0, 
                    s._trigger("selecting", t, {
                        selecting: i.element
                    }))) : (i.selecting && ((t.metaKey || t.ctrlKey) && i.startselected ? (i.$element.removeClass("ui-selecting"), 
                    i.selecting = !1, i.$element.addClass("ui-selected"), i.selected = !0) : (i.$element.removeClass("ui-selecting"), 
                    i.selecting = !1, i.startselected && (i.$element.addClass("ui-unselecting"), i.unselecting = !0), 
                    s._trigger("unselecting", t, {
                        unselecting: i.element
                    }))), i.selected && (t.metaKey || t.ctrlKey || i.startselected || (i.$element.removeClass("ui-selected"), 
                    i.selected = !1, i.$element.addClass("ui-unselecting"), i.unselecting = !0, s._trigger("unselecting", t, {
                        unselecting: i.element
                    })))));
                }), !1;
            }
        },
        _mouseStop: function(t) {
            var i = this;
            return this.dragged = !1, e(".ui-unselecting", this.element[0]).each(function() {
                var s = e.data(this, "selectable-item");
                s.$element.removeClass("ui-unselecting"), s.unselecting = !1, s.startselected = !1, 
                i._trigger("unselected", t, {
                    unselected: s.element
                });
            }), e(".ui-selecting", this.element[0]).each(function() {
                var s = e.data(this, "selectable-item");
                s.$element.removeClass("ui-selecting").addClass("ui-selected"), s.selecting = !1, 
                s.selected = !0, s.startselected = !0, i._trigger("selected", t, {
                    selected: s.element
                });
            }), this._trigger("stop", t), this.helper.remove(), !1;
        }
    }), e.widget("ui.sortable", e.ui.mouse, {
        version: "1.11.2",
        widgetEventPrefix: "sort",
        ready: !1,
        options: {
            appendTo: "parent",
            axis: !1,
            connectWith: !1,
            containment: !1,
            cursor: "auto",
            cursorAt: !1,
            dropOnEmpty: !0,
            forcePlaceholderSize: !1,
            forceHelperSize: !1,
            grid: !1,
            handle: !1,
            helper: "original",
            items: "> *",
            opacity: !1,
            placeholder: !1,
            revert: !1,
            scroll: !0,
            scrollSensitivity: 20,
            scrollSpeed: 20,
            scope: "default",
            tolerance: "intersect",
            zIndex: 1e3,
            activate: null,
            beforeStop: null,
            change: null,
            deactivate: null,
            out: null,
            over: null,
            receive: null,
            remove: null,
            sort: null,
            start: null,
            stop: null,
            update: null
        },
        _isOverAxis: function(e, t, i) {
            return e >= t && t + i > e;
        },
        _isFloating: function(e) {
            return /left|right/.test(e.css("float")) || /inline|table-cell/.test(e.css("display"));
        },
        _create: function() {
            var e = this.options;
            this.containerCache = {}, this.element.addClass("ui-sortable"), this.refresh(), 
            this.floating = !!this.items.length && ("x" === e.axis || this._isFloating(this.items[0].item)), 
            this.offset = this.element.offset(), this._mouseInit(), this._setHandleClassName(), 
            this.ready = !0;
        },
        _setOption: function(e, t) {
            this._super(e, t), "handle" === e && this._setHandleClassName();
        },
        _setHandleClassName: function() {
            this.element.find(".ui-sortable-handle").removeClass("ui-sortable-handle"), e.each(this.items, function() {
                (this.instance.options.handle ? this.item.find(this.instance.options.handle) : this.item).addClass("ui-sortable-handle");
            });
        },
        _destroy: function() {
            this.element.removeClass("ui-sortable ui-sortable-disabled").find(".ui-sortable-handle").removeClass("ui-sortable-handle"), 
            this._mouseDestroy();
            for (var e = this.items.length - 1; e >= 0; e--) this.items[e].item.removeData(this.widgetName + "-item");
            return this;
        },
        _mouseCapture: function(t, i) {
            var s = null, n = !1, a = this;
            return !this.reverting && (!this.options.disabled && "static" !== this.options.type && (this._refreshItems(t), 
            e(t.target).parents().each(function() {
                return e.data(this, a.widgetName + "-item") === a ? (s = e(this), !1) : void 0;
            }), e.data(t.target, a.widgetName + "-item") === a && (s = e(t.target)), !!s && (!(this.options.handle && !i && (e(this.options.handle, s).find("*").addBack().each(function() {
                this === t.target && (n = !0);
            }), !n)) && (this.currentItem = s, this._removeCurrentsFromItems(), !0))));
        },
        _mouseStart: function(t, i, s) {
            var n, a, o = this.options;
            if (this.currentContainer = this, this.refreshPositions(), this.helper = this._createHelper(t), 
            this._cacheHelperProportions(), this._cacheMargins(), this.scrollParent = this.helper.scrollParent(), 
            this.offset = this.currentItem.offset(), this.offset = {
                top: this.offset.top - this.margins.top,
                left: this.offset.left - this.margins.left
            }, e.extend(this.offset, {
                click: {
                    left: t.pageX - this.offset.left,
                    top: t.pageY - this.offset.top
                },
                parent: this._getParentOffset(),
                relative: this._getRelativeOffset()
            }), this.helper.css("position", "absolute"), this.cssPosition = this.helper.css("position"), 
            this.originalPosition = this._generatePosition(t), this.originalPageX = t.pageX, 
            this.originalPageY = t.pageY, o.cursorAt && this._adjustOffsetFromHelper(o.cursorAt), 
            this.domPosition = {
                prev: this.currentItem.prev()[0],
                parent: this.currentItem.parent()[0]
            }, this.helper[0] !== this.currentItem[0] && this.currentItem.hide(), this._createPlaceholder(), 
            o.containment && this._setContainment(), o.cursor && "auto" !== o.cursor && (a = this.document.find("body"), 
            this.storedCursor = a.css("cursor"), a.css("cursor", o.cursor), this.storedStylesheet = e("<style>*{ cursor: " + o.cursor + " !important; }</style>").appendTo(a)), 
            o.opacity && (this.helper.css("opacity") && (this._storedOpacity = this.helper.css("opacity")), 
            this.helper.css("opacity", o.opacity)), o.zIndex && (this.helper.css("zIndex") && (this._storedZIndex = this.helper.css("zIndex")), 
            this.helper.css("zIndex", o.zIndex)), this.scrollParent[0] !== document && "HTML" !== this.scrollParent[0].tagName && (this.overflowOffset = this.scrollParent.offset()), 
            this._trigger("start", t, this._uiHash()), this._preserveHelperProportions || this._cacheHelperProportions(), 
            !s) for (n = this.containers.length - 1; n >= 0; n--) this.containers[n]._trigger("activate", t, this._uiHash(this));
            return e.ui.ddmanager && (e.ui.ddmanager.current = this), e.ui.ddmanager && !o.dropBehaviour && e.ui.ddmanager.prepareOffsets(this, t), 
            this.dragging = !0, this.helper.addClass("ui-sortable-helper"), this._mouseDrag(t), 
            !0;
        },
        _mouseDrag: function(t) {
            var i, s, n, a, o = this.options, r = !1;
            for (this.position = this._generatePosition(t), this.positionAbs = this._convertPositionTo("absolute"), 
            this.lastPositionAbs || (this.lastPositionAbs = this.positionAbs), this.options.scroll && (this.scrollParent[0] !== document && "HTML" !== this.scrollParent[0].tagName ? (this.overflowOffset.top + this.scrollParent[0].offsetHeight - t.pageY < o.scrollSensitivity ? this.scrollParent[0].scrollTop = r = this.scrollParent[0].scrollTop + o.scrollSpeed : t.pageY - this.overflowOffset.top < o.scrollSensitivity && (this.scrollParent[0].scrollTop = r = this.scrollParent[0].scrollTop - o.scrollSpeed), 
            this.overflowOffset.left + this.scrollParent[0].offsetWidth - t.pageX < o.scrollSensitivity ? this.scrollParent[0].scrollLeft = r = this.scrollParent[0].scrollLeft + o.scrollSpeed : t.pageX - this.overflowOffset.left < o.scrollSensitivity && (this.scrollParent[0].scrollLeft = r = this.scrollParent[0].scrollLeft - o.scrollSpeed)) : (t.pageY - e(document).scrollTop() < o.scrollSensitivity ? r = e(document).scrollTop(e(document).scrollTop() - o.scrollSpeed) : e(window).height() - (t.pageY - e(document).scrollTop()) < o.scrollSensitivity && (r = e(document).scrollTop(e(document).scrollTop() + o.scrollSpeed)), 
            t.pageX - e(document).scrollLeft() < o.scrollSensitivity ? r = e(document).scrollLeft(e(document).scrollLeft() - o.scrollSpeed) : e(window).width() - (t.pageX - e(document).scrollLeft()) < o.scrollSensitivity && (r = e(document).scrollLeft(e(document).scrollLeft() + o.scrollSpeed))), 
            !1 !== r && e.ui.ddmanager && !o.dropBehaviour && e.ui.ddmanager.prepareOffsets(this, t)), 
            this.positionAbs = this._convertPositionTo("absolute"), this.options.axis && "y" === this.options.axis || (this.helper[0].style.left = this.position.left + "px"), 
            this.options.axis && "x" === this.options.axis || (this.helper[0].style.top = this.position.top + "px"), 
            i = this.items.length - 1; i >= 0; i--) if (s = this.items[i], n = s.item[0], (a = this._intersectsWithPointer(s)) && s.instance === this.currentContainer && n !== this.currentItem[0] && this.placeholder[1 === a ? "next" : "prev"]()[0] !== n && !e.contains(this.placeholder[0], n) && ("semi-dynamic" !== this.options.type || !e.contains(this.element[0], n))) {
                if (this.direction = 1 === a ? "down" : "up", "pointer" !== this.options.tolerance && !this._intersectsWithSides(s)) break;
                this._rearrange(t, s), this._trigger("change", t, this._uiHash());
                break;
            }
            return this._contactContainers(t), e.ui.ddmanager && e.ui.ddmanager.drag(this, t), 
            this._trigger("sort", t, this._uiHash()), this.lastPositionAbs = this.positionAbs, 
            !1;
        },
        _mouseStop: function(t, i) {
            if (t) {
                if (e.ui.ddmanager && !this.options.dropBehaviour && e.ui.ddmanager.drop(this, t), 
                this.options.revert) {
                    var s = this, n = this.placeholder.offset(), a = this.options.axis, o = {};
                    a && "x" !== a || (o.left = n.left - this.offset.parent.left - this.margins.left + (this.offsetParent[0] === document.body ? 0 : this.offsetParent[0].scrollLeft)), 
                    a && "y" !== a || (o.top = n.top - this.offset.parent.top - this.margins.top + (this.offsetParent[0] === document.body ? 0 : this.offsetParent[0].scrollTop)), 
                    this.reverting = !0, e(this.helper).animate(o, parseInt(this.options.revert, 10) || 500, function() {
                        s._clear(t);
                    });
                } else this._clear(t, i);
                return !1;
            }
        },
        cancel: function() {
            if (this.dragging) {
                this._mouseUp({
                    target: null
                }), "original" === this.options.helper ? this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper") : this.currentItem.show();
                for (var t = this.containers.length - 1; t >= 0; t--) this.containers[t]._trigger("deactivate", null, this._uiHash(this)), 
                this.containers[t].containerCache.over && (this.containers[t]._trigger("out", null, this._uiHash(this)), 
                this.containers[t].containerCache.over = 0);
            }
            return this.placeholder && (this.placeholder[0].parentNode && this.placeholder[0].parentNode.removeChild(this.placeholder[0]), 
            "original" !== this.options.helper && this.helper && this.helper[0].parentNode && this.helper.remove(), 
            e.extend(this, {
                helper: null,
                dragging: !1,
                reverting: !1,
                _noFinalSort: null
            }), this.domPosition.prev ? e(this.domPosition.prev).after(this.currentItem) : e(this.domPosition.parent).prepend(this.currentItem)), 
            this;
        },
        serialize: function(t) {
            var i = this._getItemsAsjQuery(t && t.connected), s = [];
            return t = t || {}, e(i).each(function() {
                var i = (e(t.item || this).attr(t.attribute || "id") || "").match(t.expression || /(.+)[\-=_](.+)/);
                i && s.push((t.key || i[1] + "[]") + "=" + (t.key && t.expression ? i[1] : i[2]));
            }), !s.length && t.key && s.push(t.key + "="), s.join("&");
        },
        toArray: function(t) {
            var i = this._getItemsAsjQuery(t && t.connected), s = [];
            return t = t || {}, i.each(function() {
                s.push(e(t.item || this).attr(t.attribute || "id") || "");
            }), s;
        },
        _intersectsWith: function(e) {
            var t = this.positionAbs.left, i = t + this.helperProportions.width, s = this.positionAbs.top, n = s + this.helperProportions.height, a = e.left, o = a + e.width, r = e.top, h = r + e.height, l = this.offset.click.top, u = this.offset.click.left, d = "x" === this.options.axis || s + l > r && h > s + l, c = "y" === this.options.axis || t + u > a && o > t + u, p = d && c;
            return "pointer" === this.options.tolerance || this.options.forcePointerForContainers || "pointer" !== this.options.tolerance && this.helperProportions[this.floating ? "width" : "height"] > e[this.floating ? "width" : "height"] ? p : t + this.helperProportions.width / 2 > a && o > i - this.helperProportions.width / 2 && s + this.helperProportions.height / 2 > r && h > n - this.helperProportions.height / 2;
        },
        _intersectsWithPointer: function(e) {
            var t = "x" === this.options.axis || this._isOverAxis(this.positionAbs.top + this.offset.click.top, e.top, e.height), i = "y" === this.options.axis || this._isOverAxis(this.positionAbs.left + this.offset.click.left, e.left, e.width), s = t && i, n = this._getDragVerticalDirection(), a = this._getDragHorizontalDirection();
            return !!s && (this.floating ? a && "right" === a || "down" === n ? 2 : 1 : n && ("down" === n ? 2 : 1));
        },
        _intersectsWithSides: function(e) {
            var t = this._isOverAxis(this.positionAbs.top + this.offset.click.top, e.top + e.height / 2, e.height), i = this._isOverAxis(this.positionAbs.left + this.offset.click.left, e.left + e.width / 2, e.width), s = this._getDragVerticalDirection(), n = this._getDragHorizontalDirection();
            return this.floating && n ? "right" === n && i || "left" === n && !i : s && ("down" === s && t || "up" === s && !t);
        },
        _getDragVerticalDirection: function() {
            var e = this.positionAbs.top - this.lastPositionAbs.top;
            return 0 !== e && (e > 0 ? "down" : "up");
        },
        _getDragHorizontalDirection: function() {
            var e = this.positionAbs.left - this.lastPositionAbs.left;
            return 0 !== e && (e > 0 ? "right" : "left");
        },
        refresh: function(e) {
            return this._refreshItems(e), this._setHandleClassName(), this.refreshPositions(), 
            this;
        },
        _connectWith: function() {
            var e = this.options;
            return e.connectWith.constructor === String ? [ e.connectWith ] : e.connectWith;
        },
        _getItemsAsjQuery: function(t) {
            function i() {
                r.push(this);
            }
            var s, n, a, o, r = [], h = [], l = this._connectWith();
            if (l && t) for (s = l.length - 1; s >= 0; s--) for (a = e(l[s]), n = a.length - 1; n >= 0; n--) (o = e.data(a[n], this.widgetFullName)) && o !== this && !o.options.disabled && h.push([ e.isFunction(o.options.items) ? o.options.items.call(o.element) : e(o.options.items, o.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"), o ]);
            for (h.push([ e.isFunction(this.options.items) ? this.options.items.call(this.element, null, {
                options: this.options,
                item: this.currentItem
            }) : e(this.options.items, this.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"), this ]), 
            s = h.length - 1; s >= 0; s--) h[s][0].each(i);
            return e(r);
        },
        _removeCurrentsFromItems: function() {
            var t = this.currentItem.find(":data(" + this.widgetName + "-item)");
            this.items = e.grep(this.items, function(e) {
                for (var i = 0; t.length > i; i++) if (t[i] === e.item[0]) return !1;
                return !0;
            });
        },
        _refreshItems: function(t) {
            this.items = [], this.containers = [ this ];
            var i, s, n, a, o, r, h, l, u = this.items, d = [ [ e.isFunction(this.options.items) ? this.options.items.call(this.element[0], t, {
                item: this.currentItem
            }) : e(this.options.items, this.element), this ] ], c = this._connectWith();
            if (c && this.ready) for (i = c.length - 1; i >= 0; i--) for (n = e(c[i]), s = n.length - 1; s >= 0; s--) (a = e.data(n[s], this.widgetFullName)) && a !== this && !a.options.disabled && (d.push([ e.isFunction(a.options.items) ? a.options.items.call(a.element[0], t, {
                item: this.currentItem
            }) : e(a.options.items, a.element), a ]), this.containers.push(a));
            for (i = d.length - 1; i >= 0; i--) for (o = d[i][1], r = d[i][0], s = 0, l = r.length; l > s; s++) h = e(r[s]), 
            h.data(this.widgetName + "-item", o), u.push({
                item: h,
                instance: o,
                width: 0,
                height: 0,
                left: 0,
                top: 0
            });
        },
        refreshPositions: function(t) {
            this.offsetParent && this.helper && (this.offset.parent = this._getParentOffset());
            var i, s, n, a;
            for (i = this.items.length - 1; i >= 0; i--) s = this.items[i], s.instance !== this.currentContainer && this.currentContainer && s.item[0] !== this.currentItem[0] || (n = this.options.toleranceElement ? e(this.options.toleranceElement, s.item) : s.item, 
            t || (s.width = n.outerWidth(), s.height = n.outerHeight()), a = n.offset(), s.left = a.left, 
            s.top = a.top);
            if (this.options.custom && this.options.custom.refreshContainers) this.options.custom.refreshContainers.call(this); else for (i = this.containers.length - 1; i >= 0; i--) a = this.containers[i].element.offset(), 
            this.containers[i].containerCache.left = a.left, this.containers[i].containerCache.top = a.top, 
            this.containers[i].containerCache.width = this.containers[i].element.outerWidth(), 
            this.containers[i].containerCache.height = this.containers[i].element.outerHeight();
            return this;
        },
        _createPlaceholder: function(t) {
            t = t || this;
            var i, s = t.options;
            s.placeholder && s.placeholder.constructor !== String || (i = s.placeholder, s.placeholder = {
                element: function() {
                    var s = t.currentItem[0].nodeName.toLowerCase(), n = e("<" + s + ">", t.document[0]).addClass(i || t.currentItem[0].className + " ui-sortable-placeholder").removeClass("ui-sortable-helper");
                    return "tr" === s ? t.currentItem.children().each(function() {
                        e("<td>&#160;</td>", t.document[0]).attr("colspan", e(this).attr("colspan") || 1).appendTo(n);
                    }) : "img" === s && n.attr("src", t.currentItem.attr("src")), i || n.css("visibility", "hidden"), 
                    n;
                },
                update: function(e, n) {
                    (!i || s.forcePlaceholderSize) && (n.height() || n.height(t.currentItem.innerHeight() - parseInt(t.currentItem.css("paddingTop") || 0, 10) - parseInt(t.currentItem.css("paddingBottom") || 0, 10)), 
                    n.width() || n.width(t.currentItem.innerWidth() - parseInt(t.currentItem.css("paddingLeft") || 0, 10) - parseInt(t.currentItem.css("paddingRight") || 0, 10)));
                }
            }), t.placeholder = e(s.placeholder.element.call(t.element, t.currentItem)), t.currentItem.after(t.placeholder), 
            s.placeholder.update(t, t.placeholder);
        },
        _contactContainers: function(t) {
            var i, s, n, a, o, r, h, l, u, d, c = null, p = null;
            for (i = this.containers.length - 1; i >= 0; i--) if (!e.contains(this.currentItem[0], this.containers[i].element[0])) if (this._intersectsWith(this.containers[i].containerCache)) {
                if (c && e.contains(this.containers[i].element[0], c.element[0])) continue;
                c = this.containers[i], p = i;
            } else this.containers[i].containerCache.over && (this.containers[i]._trigger("out", t, this._uiHash(this)), 
            this.containers[i].containerCache.over = 0);
            if (c) if (1 === this.containers.length) this.containers[p].containerCache.over || (this.containers[p]._trigger("over", t, this._uiHash(this)), 
            this.containers[p].containerCache.over = 1); else {
                for (n = 1e4, a = null, u = c.floating || this._isFloating(this.currentItem), o = u ? "left" : "top", 
                r = u ? "width" : "height", d = u ? "clientX" : "clientY", s = this.items.length - 1; s >= 0; s--) e.contains(this.containers[p].element[0], this.items[s].item[0]) && this.items[s].item[0] !== this.currentItem[0] && (h = this.items[s].item.offset()[o], 
                l = !1, t[d] - h > this.items[s][r] / 2 && (l = !0), n > Math.abs(t[d] - h) && (n = Math.abs(t[d] - h), 
                a = this.items[s], this.direction = l ? "up" : "down"));
                if (!a && !this.options.dropOnEmpty) return;
                if (this.currentContainer === this.containers[p]) return void (this.currentContainer.containerCache.over || (this.containers[p]._trigger("over", t, this._uiHash()), 
                this.currentContainer.containerCache.over = 1));
                a ? this._rearrange(t, a, null, !0) : this._rearrange(t, null, this.containers[p].element, !0), 
                this._trigger("change", t, this._uiHash()), this.containers[p]._trigger("change", t, this._uiHash(this)), 
                this.currentContainer = this.containers[p], this.options.placeholder.update(this.currentContainer, this.placeholder), 
                this.containers[p]._trigger("over", t, this._uiHash(this)), this.containers[p].containerCache.over = 1;
            }
        },
        _createHelper: function(t) {
            var i = this.options, s = e.isFunction(i.helper) ? e(i.helper.apply(this.element[0], [ t, this.currentItem ])) : "clone" === i.helper ? this.currentItem.clone() : this.currentItem;
            return s.parents("body").length || e("parent" !== i.appendTo ? i.appendTo : this.currentItem[0].parentNode)[0].appendChild(s[0]), 
            s[0] === this.currentItem[0] && (this._storedCSS = {
                width: this.currentItem[0].style.width,
                height: this.currentItem[0].style.height,
                position: this.currentItem.css("position"),
                top: this.currentItem.css("top"),
                left: this.currentItem.css("left")
            }), (!s[0].style.width || i.forceHelperSize) && s.width(this.currentItem.width()), 
            (!s[0].style.height || i.forceHelperSize) && s.height(this.currentItem.height()), 
            s;
        },
        _adjustOffsetFromHelper: function(t) {
            "string" == typeof t && (t = t.split(" ")), e.isArray(t) && (t = {
                left: +t[0],
                top: +t[1] || 0
            }), "left" in t && (this.offset.click.left = t.left + this.margins.left), "right" in t && (this.offset.click.left = this.helperProportions.width - t.right + this.margins.left), 
            "top" in t && (this.offset.click.top = t.top + this.margins.top), "bottom" in t && (this.offset.click.top = this.helperProportions.height - t.bottom + this.margins.top);
        },
        _getParentOffset: function() {
            this.offsetParent = this.helper.offsetParent();
            var t = this.offsetParent.offset();
            return "absolute" === this.cssPosition && this.scrollParent[0] !== document && e.contains(this.scrollParent[0], this.offsetParent[0]) && (t.left += this.scrollParent.scrollLeft(), 
            t.top += this.scrollParent.scrollTop()), (this.offsetParent[0] === document.body || this.offsetParent[0].tagName && "html" === this.offsetParent[0].tagName.toLowerCase() && e.ui.ie) && (t = {
                top: 0,
                left: 0
            }), {
                top: t.top + (parseInt(this.offsetParent.css("borderTopWidth"), 10) || 0),
                left: t.left + (parseInt(this.offsetParent.css("borderLeftWidth"), 10) || 0)
            };
        },
        _getRelativeOffset: function() {
            if ("relative" === this.cssPosition) {
                var e = this.currentItem.position();
                return {
                    top: e.top - (parseInt(this.helper.css("top"), 10) || 0) + this.scrollParent.scrollTop(),
                    left: e.left - (parseInt(this.helper.css("left"), 10) || 0) + this.scrollParent.scrollLeft()
                };
            }
            return {
                top: 0,
                left: 0
            };
        },
        _cacheMargins: function() {
            this.margins = {
                left: parseInt(this.currentItem.css("marginLeft"), 10) || 0,
                top: parseInt(this.currentItem.css("marginTop"), 10) || 0
            };
        },
        _cacheHelperProportions: function() {
            this.helperProportions = {
                width: this.helper.outerWidth(),
                height: this.helper.outerHeight()
            };
        },
        _setContainment: function() {
            var t, i, s, n = this.options;
            "parent" === n.containment && (n.containment = this.helper[0].parentNode), ("document" === n.containment || "window" === n.containment) && (this.containment = [ 0 - this.offset.relative.left - this.offset.parent.left, 0 - this.offset.relative.top - this.offset.parent.top, e("document" === n.containment ? document : window).width() - this.helperProportions.width - this.margins.left, (e("document" === n.containment ? document : window).height() || document.body.parentNode.scrollHeight) - this.helperProportions.height - this.margins.top ]), 
            /^(document|window|parent)$/.test(n.containment) || (t = e(n.containment)[0], i = e(n.containment).offset(), 
            s = "hidden" !== e(t).css("overflow"), this.containment = [ i.left + (parseInt(e(t).css("borderLeftWidth"), 10) || 0) + (parseInt(e(t).css("paddingLeft"), 10) || 0) - this.margins.left, i.top + (parseInt(e(t).css("borderTopWidth"), 10) || 0) + (parseInt(e(t).css("paddingTop"), 10) || 0) - this.margins.top, i.left + (s ? Math.max(t.scrollWidth, t.offsetWidth) : t.offsetWidth) - (parseInt(e(t).css("borderLeftWidth"), 10) || 0) - (parseInt(e(t).css("paddingRight"), 10) || 0) - this.helperProportions.width - this.margins.left, i.top + (s ? Math.max(t.scrollHeight, t.offsetHeight) : t.offsetHeight) - (parseInt(e(t).css("borderTopWidth"), 10) || 0) - (parseInt(e(t).css("paddingBottom"), 10) || 0) - this.helperProportions.height - this.margins.top ]);
        },
        _convertPositionTo: function(t, i) {
            i || (i = this.position);
            var s = "absolute" === t ? 1 : -1, n = "absolute" !== this.cssPosition || this.scrollParent[0] !== document && e.contains(this.scrollParent[0], this.offsetParent[0]) ? this.scrollParent : this.offsetParent, a = /(html|body)/i.test(n[0].tagName);
            return {
                top: i.top + this.offset.relative.top * s + this.offset.parent.top * s - ("fixed" === this.cssPosition ? -this.scrollParent.scrollTop() : a ? 0 : n.scrollTop()) * s,
                left: i.left + this.offset.relative.left * s + this.offset.parent.left * s - ("fixed" === this.cssPosition ? -this.scrollParent.scrollLeft() : a ? 0 : n.scrollLeft()) * s
            };
        },
        _generatePosition: function(t) {
            var i, s, n = this.options, a = t.pageX, o = t.pageY, r = "absolute" !== this.cssPosition || this.scrollParent[0] !== document && e.contains(this.scrollParent[0], this.offsetParent[0]) ? this.scrollParent : this.offsetParent, h = /(html|body)/i.test(r[0].tagName);
            return "relative" !== this.cssPosition || this.scrollParent[0] !== document && this.scrollParent[0] !== this.offsetParent[0] || (this.offset.relative = this._getRelativeOffset()), 
            this.originalPosition && (this.containment && (t.pageX - this.offset.click.left < this.containment[0] && (a = this.containment[0] + this.offset.click.left), 
            t.pageY - this.offset.click.top < this.containment[1] && (o = this.containment[1] + this.offset.click.top), 
            t.pageX - this.offset.click.left > this.containment[2] && (a = this.containment[2] + this.offset.click.left), 
            t.pageY - this.offset.click.top > this.containment[3] && (o = this.containment[3] + this.offset.click.top)), 
            n.grid && (i = this.originalPageY + Math.round((o - this.originalPageY) / n.grid[1]) * n.grid[1], 
            o = this.containment ? i - this.offset.click.top >= this.containment[1] && i - this.offset.click.top <= this.containment[3] ? i : i - this.offset.click.top >= this.containment[1] ? i - n.grid[1] : i + n.grid[1] : i, 
            s = this.originalPageX + Math.round((a - this.originalPageX) / n.grid[0]) * n.grid[0], 
            a = this.containment ? s - this.offset.click.left >= this.containment[0] && s - this.offset.click.left <= this.containment[2] ? s : s - this.offset.click.left >= this.containment[0] ? s - n.grid[0] : s + n.grid[0] : s)), 
            {
                top: o - this.offset.click.top - this.offset.relative.top - this.offset.parent.top + ("fixed" === this.cssPosition ? -this.scrollParent.scrollTop() : h ? 0 : r.scrollTop()),
                left: a - this.offset.click.left - this.offset.relative.left - this.offset.parent.left + ("fixed" === this.cssPosition ? -this.scrollParent.scrollLeft() : h ? 0 : r.scrollLeft())
            };
        },
        _rearrange: function(e, t, i, s) {
            i ? i[0].appendChild(this.placeholder[0]) : t.item[0].parentNode.insertBefore(this.placeholder[0], "down" === this.direction ? t.item[0] : t.item[0].nextSibling), 
            this.counter = this.counter ? ++this.counter : 1;
            var n = this.counter;
            this._delay(function() {
                n === this.counter && this.refreshPositions(!s);
            });
        },
        _clear: function(e, t) {
            function i(e, t, i) {
                return function(s) {
                    i._trigger(e, s, t._uiHash(t));
                };
            }
            this.reverting = !1;
            var s, n = [];
            if (!this._noFinalSort && this.currentItem.parent().length && this.placeholder.before(this.currentItem), 
            this._noFinalSort = null, this.helper[0] === this.currentItem[0]) {
                for (s in this._storedCSS) ("auto" === this._storedCSS[s] || "static" === this._storedCSS[s]) && (this._storedCSS[s] = "");
                this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper");
            } else this.currentItem.show();
            for (this.fromOutside && !t && n.push(function(e) {
                this._trigger("receive", e, this._uiHash(this.fromOutside));
            }), !this.fromOutside && this.domPosition.prev === this.currentItem.prev().not(".ui-sortable-helper")[0] && this.domPosition.parent === this.currentItem.parent()[0] || t || n.push(function(e) {
                this._trigger("update", e, this._uiHash());
            }), this !== this.currentContainer && (t || (n.push(function(e) {
                this._trigger("remove", e, this._uiHash());
            }), n.push(function(e) {
                return function(t) {
                    e._trigger("receive", t, this._uiHash(this));
                };
            }.call(this, this.currentContainer)), n.push(function(e) {
                return function(t) {
                    e._trigger("update", t, this._uiHash(this));
                };
            }.call(this, this.currentContainer)))), s = this.containers.length - 1; s >= 0; s--) t || n.push(i("deactivate", this, this.containers[s])), 
            this.containers[s].containerCache.over && (n.push(i("out", this, this.containers[s])), 
            this.containers[s].containerCache.over = 0);
            if (this.storedCursor && (this.document.find("body").css("cursor", this.storedCursor), 
            this.storedStylesheet.remove()), this._storedOpacity && this.helper.css("opacity", this._storedOpacity), 
            this._storedZIndex && this.helper.css("zIndex", "auto" === this._storedZIndex ? "" : this._storedZIndex), 
            this.dragging = !1, t || this._trigger("beforeStop", e, this._uiHash()), this.placeholder[0].parentNode.removeChild(this.placeholder[0]), 
            this.cancelHelperRemoval || (this.helper[0] !== this.currentItem[0] && this.helper.remove(), 
            this.helper = null), !t) {
                for (s = 0; n.length > s; s++) n[s].call(this, e);
                this._trigger("stop", e, this._uiHash());
            }
            return this.fromOutside = !1, !this.cancelHelperRemoval;
        },
        _trigger: function() {
            !1 === e.Widget.prototype._trigger.apply(this, arguments) && this.cancel();
        },
        _uiHash: function(t) {
            var i = t || this;
            return {
                helper: i.helper,
                placeholder: i.placeholder || e([]),
                position: i.position,
                originalPosition: i.originalPosition,
                offset: i.positionAbs,
                item: i.currentItem,
                sender: t ? t.element : null
            };
        }
    }), e.widget("ui.accordion", {
        version: "1.11.2",
        options: {
            active: 0,
            animate: {},
            collapsible: !1,
            event: "click",
            header: "> li > :first-child,> :not(li):even",
            heightStyle: "auto",
            icons: {
                activeHeader: "ui-icon-triangle-1-s",
                header: "ui-icon-triangle-1-e"
            },
            activate: null,
            beforeActivate: null
        },
        hideProps: {
            borderTopWidth: "hide",
            borderBottomWidth: "hide",
            paddingTop: "hide",
            paddingBottom: "hide",
            height: "hide"
        },
        showProps: {
            borderTopWidth: "show",
            borderBottomWidth: "show",
            paddingTop: "show",
            paddingBottom: "show",
            height: "show"
        },
        _create: function() {
            var t = this.options;
            this.prevShow = this.prevHide = e(), this.element.addClass("ui-accordion ui-widget ui-helper-reset").attr("role", "tablist"), 
            t.collapsible || !1 !== t.active && null != t.active || (t.active = 0), this._processPanels(), 
            0 > t.active && (t.active += this.headers.length), this._refresh();
        },
        _getCreateEventData: function() {
            return {
                header: this.active,
                panel: this.active.length ? this.active.next() : e()
            };
        },
        _createIcons: function() {
            var t = this.options.icons;
            t && (e("<span>").addClass("ui-accordion-header-icon ui-icon " + t.header).prependTo(this.headers), 
            this.active.children(".ui-accordion-header-icon").removeClass(t.header).addClass(t.activeHeader), 
            this.headers.addClass("ui-accordion-icons"));
        },
        _destroyIcons: function() {
            this.headers.removeClass("ui-accordion-icons").children(".ui-accordion-header-icon").remove();
        },
        _destroy: function() {
            var e;
            this.element.removeClass("ui-accordion ui-widget ui-helper-reset").removeAttr("role"), 
            this.headers.removeClass("ui-accordion-header ui-accordion-header-active ui-state-default ui-corner-all ui-state-active ui-state-disabled ui-corner-top").removeAttr("role").removeAttr("aria-expanded").removeAttr("aria-selected").removeAttr("aria-controls").removeAttr("tabIndex").removeUniqueId(), 
            this._destroyIcons(), e = this.headers.next().removeClass("ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content ui-accordion-content-active ui-state-disabled").css("display", "").removeAttr("role").removeAttr("aria-hidden").removeAttr("aria-labelledby").removeUniqueId(), 
            "content" !== this.options.heightStyle && e.css("height", "");
        },
        _setOption: function(e, t) {
            return "active" === e ? void this._activate(t) : ("event" === e && (this.options.event && this._off(this.headers, this.options.event), 
            this._setupEvents(t)), this._super(e, t), "collapsible" !== e || t || !1 !== this.options.active || this._activate(0), 
            "icons" === e && (this._destroyIcons(), t && this._createIcons()), void ("disabled" === e && (this.element.toggleClass("ui-state-disabled", !!t).attr("aria-disabled", t), 
            this.headers.add(this.headers.next()).toggleClass("ui-state-disabled", !!t))));
        },
        _keydown: function(t) {
            if (!t.altKey && !t.ctrlKey) {
                var i = e.ui.keyCode, s = this.headers.length, n = this.headers.index(t.target), a = !1;
                switch (t.keyCode) {
                  case i.RIGHT:
                  case i.DOWN:
                    a = this.headers[(n + 1) % s];
                    break;

                  case i.LEFT:
                  case i.UP:
                    a = this.headers[(n - 1 + s) % s];
                    break;

                  case i.SPACE:
                  case i.ENTER:
                    this._eventHandler(t);
                    break;

                  case i.HOME:
                    a = this.headers[0];
                    break;

                  case i.END:
                    a = this.headers[s - 1];
                }
                a && (e(t.target).attr("tabIndex", -1), e(a).attr("tabIndex", 0), a.focus(), t.preventDefault());
            }
        },
        _panelKeyDown: function(t) {
            t.keyCode === e.ui.keyCode.UP && t.ctrlKey && e(t.currentTarget).prev().focus();
        },
        refresh: function() {
            var t = this.options;
            this._processPanels(), !1 === t.active && !0 === t.collapsible || !this.headers.length ? (t.active = !1, 
            this.active = e()) : !1 === t.active ? this._activate(0) : this.active.length && !e.contains(this.element[0], this.active[0]) ? this.headers.length === this.headers.find(".ui-state-disabled").length ? (t.active = !1, 
            this.active = e()) : this._activate(Math.max(0, t.active - 1)) : t.active = this.headers.index(this.active), 
            this._destroyIcons(), this._refresh();
        },
        _processPanels: function() {
            var e = this.headers, t = this.panels;
            this.headers = this.element.find(this.options.header).addClass("ui-accordion-header ui-state-default ui-corner-all"), 
            this.panels = this.headers.next().addClass("ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom").filter(":not(.ui-accordion-content-active)").hide(), 
            t && (this._off(e.not(this.headers)), this._off(t.not(this.panels)));
        },
        _refresh: function() {
            var t, i = this.options, s = i.heightStyle, n = this.element.parent();
            this.active = this._findActive(i.active).addClass("ui-accordion-header-active ui-state-active ui-corner-top").removeClass("ui-corner-all"), 
            this.active.next().addClass("ui-accordion-content-active").show(), this.headers.attr("role", "tab").each(function() {
                var t = e(this), i = t.uniqueId().attr("id"), s = t.next(), n = s.uniqueId().attr("id");
                t.attr("aria-controls", n), s.attr("aria-labelledby", i);
            }).next().attr("role", "tabpanel"), this.headers.not(this.active).attr({
                "aria-selected": "false",
                "aria-expanded": "false",
                tabIndex: -1
            }).next().attr({
                "aria-hidden": "true"
            }).hide(), this.active.length ? this.active.attr({
                "aria-selected": "true",
                "aria-expanded": "true",
                tabIndex: 0
            }).next().attr({
                "aria-hidden": "false"
            }) : this.headers.eq(0).attr("tabIndex", 0), this._createIcons(), this._setupEvents(i.event), 
            "fill" === s ? (t = n.height(), this.element.siblings(":visible").each(function() {
                var i = e(this), s = i.css("position");
                "absolute" !== s && "fixed" !== s && (t -= i.outerHeight(!0));
            }), this.headers.each(function() {
                t -= e(this).outerHeight(!0);
            }), this.headers.next().each(function() {
                e(this).height(Math.max(0, t - e(this).innerHeight() + e(this).height()));
            }).css("overflow", "auto")) : "auto" === s && (t = 0, this.headers.next().each(function() {
                t = Math.max(t, e(this).css("height", "").height());
            }).height(t));
        },
        _activate: function(t) {
            var i = this._findActive(t)[0];
            i !== this.active[0] && (i = i || this.active[0], this._eventHandler({
                target: i,
                currentTarget: i,
                preventDefault: e.noop
            }));
        },
        _findActive: function(t) {
            return "number" == typeof t ? this.headers.eq(t) : e();
        },
        _setupEvents: function(t) {
            var i = {
                keydown: "_keydown"
            };
            t && e.each(t.split(" "), function(e, t) {
                i[t] = "_eventHandler";
            }), this._off(this.headers.add(this.headers.next())), this._on(this.headers, i), 
            this._on(this.headers.next(), {
                keydown: "_panelKeyDown"
            }), this._hoverable(this.headers), this._focusable(this.headers);
        },
        _eventHandler: function(t) {
            var i = this.options, s = this.active, n = e(t.currentTarget), a = n[0] === s[0], o = a && i.collapsible, r = o ? e() : n.next(), h = s.next(), l = {
                oldHeader: s,
                oldPanel: h,
                newHeader: o ? e() : n,
                newPanel: r
            };
            t.preventDefault(), a && !i.collapsible || !1 === this._trigger("beforeActivate", t, l) || (i.active = !o && this.headers.index(n), 
            this.active = a ? e() : n, this._toggle(l), s.removeClass("ui-accordion-header-active ui-state-active"), 
            i.icons && s.children(".ui-accordion-header-icon").removeClass(i.icons.activeHeader).addClass(i.icons.header), 
            a || (n.removeClass("ui-corner-all").addClass("ui-accordion-header-active ui-state-active ui-corner-top"), 
            i.icons && n.children(".ui-accordion-header-icon").removeClass(i.icons.header).addClass(i.icons.activeHeader), 
            n.next().addClass("ui-accordion-content-active")));
        },
        _toggle: function(t) {
            var i = t.newPanel, s = this.prevShow.length ? this.prevShow : t.oldPanel;
            this.prevShow.add(this.prevHide).stop(!0, !0), this.prevShow = i, this.prevHide = s, 
            this.options.animate ? this._animate(i, s, t) : (s.hide(), i.show(), this._toggleComplete(t)), 
            s.attr({
                "aria-hidden": "true"
            }), s.prev().attr("aria-selected", "false"), i.length && s.length ? s.prev().attr({
                tabIndex: -1,
                "aria-expanded": "false"
            }) : i.length && this.headers.filter(function() {
                return 0 === e(this).attr("tabIndex");
            }).attr("tabIndex", -1), i.attr("aria-hidden", "false").prev().attr({
                "aria-selected": "true",
                tabIndex: 0,
                "aria-expanded": "true"
            });
        },
        _animate: function(e, t, i) {
            var s, n, a, o = this, r = 0, h = e.length && (!t.length || e.index() < t.index()), l = this.options.animate || {}, u = h && l.down || l, d = function() {
                o._toggleComplete(i);
            };
            return "number" == typeof u && (a = u), "string" == typeof u && (n = u), n = n || u.easing || l.easing, 
            a = a || u.duration || l.duration, t.length ? e.length ? (s = e.show().outerHeight(), 
            t.animate(this.hideProps, {
                duration: a,
                easing: n,
                step: function(e, t) {
                    t.now = Math.round(e);
                }
            }), void e.hide().animate(this.showProps, {
                duration: a,
                easing: n,
                complete: d,
                step: function(e, i) {
                    i.now = Math.round(e), "height" !== i.prop ? r += i.now : "content" !== o.options.heightStyle && (i.now = Math.round(s - t.outerHeight() - r), 
                    r = 0);
                }
            })) : t.animate(this.hideProps, a, n, d) : e.animate(this.showProps, a, n, d);
        },
        _toggleComplete: function(e) {
            var t = e.oldPanel;
            t.removeClass("ui-accordion-content-active").prev().removeClass("ui-corner-top").addClass("ui-corner-all"), 
            t.length && (t.parent()[0].className = t.parent()[0].className), this._trigger("activate", null, e);
        }
    }), e.widget("ui.menu", {
        version: "1.11.2",
        defaultElement: "<ul>",
        delay: 300,
        options: {
            icons: {
                submenu: "ui-icon-carat-1-e"
            },
            items: "> *",
            menus: "ul",
            position: {
                my: "left-1 top",
                at: "right top"
            },
            role: "menu",
            blur: null,
            focus: null,
            select: null
        },
        _create: function() {
            this.activeMenu = this.element, this.mouseHandled = !1, this.element.uniqueId().addClass("ui-menu ui-widget ui-widget-content").toggleClass("ui-menu-icons", !!this.element.find(".ui-icon").length).attr({
                role: this.options.role,
                tabIndex: 0
            }), this.options.disabled && this.element.addClass("ui-state-disabled").attr("aria-disabled", "true"), 
            this._on({
                "mousedown .ui-menu-item": function(e) {
                    e.preventDefault();
                },
                "click .ui-menu-item": function(t) {
                    var i = e(t.target);
                    !this.mouseHandled && i.not(".ui-state-disabled").length && (this.select(t), t.isPropagationStopped() || (this.mouseHandled = !0), 
                    i.has(".ui-menu").length ? this.expand(t) : !this.element.is(":focus") && e(this.document[0].activeElement).closest(".ui-menu").length && (this.element.trigger("focus", [ !0 ]), 
                    this.active && 1 === this.active.parents(".ui-menu").length && clearTimeout(this.timer)));
                },
                "mouseenter .ui-menu-item": function(t) {
                    if (!this.previousFilter) {
                        var i = e(t.currentTarget);
                        i.siblings(".ui-state-active").removeClass("ui-state-active"), this.focus(t, i);
                    }
                },
                mouseleave: "collapseAll",
                "mouseleave .ui-menu": "collapseAll",
                focus: function(e, t) {
                    var i = this.active || this.element.find(this.options.items).eq(0);
                    t || this.focus(e, i);
                },
                blur: function(t) {
                    this._delay(function() {
                        e.contains(this.element[0], this.document[0].activeElement) || this.collapseAll(t);
                    });
                },
                keydown: "_keydown"
            }), this.refresh(), this._on(this.document, {
                click: function(e) {
                    this._closeOnDocumentClick(e) && this.collapseAll(e), this.mouseHandled = !1;
                }
            });
        },
        _destroy: function() {
            this.element.removeAttr("aria-activedescendant").find(".ui-menu").addBack().removeClass("ui-menu ui-widget ui-widget-content ui-menu-icons ui-front").removeAttr("role").removeAttr("tabIndex").removeAttr("aria-labelledby").removeAttr("aria-expanded").removeAttr("aria-hidden").removeAttr("aria-disabled").removeUniqueId().show(), 
            this.element.find(".ui-menu-item").removeClass("ui-menu-item").removeAttr("role").removeAttr("aria-disabled").removeUniqueId().removeClass("ui-state-hover").removeAttr("tabIndex").removeAttr("role").removeAttr("aria-haspopup").children().each(function() {
                var t = e(this);
                t.data("ui-menu-submenu-carat") && t.remove();
            }), this.element.find(".ui-menu-divider").removeClass("ui-menu-divider ui-widget-content");
        },
        _keydown: function(t) {
            var i, s, n, a, o = !0;
            switch (t.keyCode) {
              case e.ui.keyCode.PAGE_UP:
                this.previousPage(t);
                break;

              case e.ui.keyCode.PAGE_DOWN:
                this.nextPage(t);
                break;

              case e.ui.keyCode.HOME:
                this._move("first", "first", t);
                break;

              case e.ui.keyCode.END:
                this._move("last", "last", t);
                break;

              case e.ui.keyCode.UP:
                this.previous(t);
                break;

              case e.ui.keyCode.DOWN:
                this.next(t);
                break;

              case e.ui.keyCode.LEFT:
                this.collapse(t);
                break;

              case e.ui.keyCode.RIGHT:
                this.active && !this.active.is(".ui-state-disabled") && this.expand(t);
                break;

              case e.ui.keyCode.ENTER:
              case e.ui.keyCode.SPACE:
                this._activate(t);
                break;

              case e.ui.keyCode.ESCAPE:
                this.collapse(t);
                break;

              default:
                o = !1, s = this.previousFilter || "", n = String.fromCharCode(t.keyCode), a = !1, 
                clearTimeout(this.filterTimer), n === s ? a = !0 : n = s + n, i = this._filterMenuItems(n), 
                i = a && -1 !== i.index(this.active.next()) ? this.active.nextAll(".ui-menu-item") : i, 
                i.length || (n = String.fromCharCode(t.keyCode), i = this._filterMenuItems(n)), 
                i.length ? (this.focus(t, i), this.previousFilter = n, this.filterTimer = this._delay(function() {
                    delete this.previousFilter;
                }, 1e3)) : delete this.previousFilter;
            }
            o && t.preventDefault();
        },
        _activate: function(e) {
            this.active.is(".ui-state-disabled") || (this.active.is("[aria-haspopup='true']") ? this.expand(e) : this.select(e));
        },
        refresh: function() {
            var t, i, s = this, n = this.options.icons.submenu, a = this.element.find(this.options.menus);
            this.element.toggleClass("ui-menu-icons", !!this.element.find(".ui-icon").length), 
            a.filter(":not(.ui-menu)").addClass("ui-menu ui-widget ui-widget-content ui-front").hide().attr({
                role: this.options.role,
                "aria-hidden": "true",
                "aria-expanded": "false"
            }).each(function() {
                var t = e(this), i = t.parent(), s = e("<span>").addClass("ui-menu-icon ui-icon " + n).data("ui-menu-submenu-carat", !0);
                i.attr("aria-haspopup", "true").prepend(s), t.attr("aria-labelledby", i.attr("id"));
            }), t = a.add(this.element), i = t.find(this.options.items), i.not(".ui-menu-item").each(function() {
                var t = e(this);
                s._isDivider(t) && t.addClass("ui-widget-content ui-menu-divider");
            }), i.not(".ui-menu-item, .ui-menu-divider").addClass("ui-menu-item").uniqueId().attr({
                tabIndex: -1,
                role: this._itemRole()
            }), i.filter(".ui-state-disabled").attr("aria-disabled", "true"), this.active && !e.contains(this.element[0], this.active[0]) && this.blur();
        },
        _itemRole: function() {
            return {
                menu: "menuitem",
                listbox: "option"
            }[this.options.role];
        },
        _setOption: function(e, t) {
            "icons" === e && this.element.find(".ui-menu-icon").removeClass(this.options.icons.submenu).addClass(t.submenu), 
            "disabled" === e && this.element.toggleClass("ui-state-disabled", !!t).attr("aria-disabled", t), 
            this._super(e, t);
        },
        focus: function(e, t) {
            var i, s;
            this.blur(e, e && "focus" === e.type), this._scrollIntoView(t), this.active = t.first(), 
            s = this.active.addClass("ui-state-focus").removeClass("ui-state-active"), this.options.role && this.element.attr("aria-activedescendant", s.attr("id")), 
            this.active.parent().closest(".ui-menu-item").addClass("ui-state-active"), e && "keydown" === e.type ? this._close() : this.timer = this._delay(function() {
                this._close();
            }, this.delay), i = t.children(".ui-menu"), i.length && e && /^mouse/.test(e.type) && this._startOpening(i), 
            this.activeMenu = t.parent(), this._trigger("focus", e, {
                item: t
            });
        },
        _scrollIntoView: function(t) {
            var i, s, n, a, o, r;
            this._hasScroll() && (i = parseFloat(e.css(this.activeMenu[0], "borderTopWidth")) || 0, 
            s = parseFloat(e.css(this.activeMenu[0], "paddingTop")) || 0, n = t.offset().top - this.activeMenu.offset().top - i - s, 
            a = this.activeMenu.scrollTop(), o = this.activeMenu.height(), r = t.outerHeight(), 
            0 > n ? this.activeMenu.scrollTop(a + n) : n + r > o && this.activeMenu.scrollTop(a + n - o + r));
        },
        blur: function(e, t) {
            t || clearTimeout(this.timer), this.active && (this.active.removeClass("ui-state-focus"), 
            this.active = null, this._trigger("blur", e, {
                item: this.active
            }));
        },
        _startOpening: function(e) {
            clearTimeout(this.timer), "true" === e.attr("aria-hidden") && (this.timer = this._delay(function() {
                this._close(), this._open(e);
            }, this.delay));
        },
        _open: function(t) {
            var i = e.extend({
                of: this.active
            }, this.options.position);
            clearTimeout(this.timer), this.element.find(".ui-menu").not(t.parents(".ui-menu")).hide().attr("aria-hidden", "true"), 
            t.show().removeAttr("aria-hidden").attr("aria-expanded", "true").position(i);
        },
        collapseAll: function(t, i) {
            clearTimeout(this.timer), this.timer = this._delay(function() {
                var s = i ? this.element : e(t && t.target).closest(this.element.find(".ui-menu"));
                s.length || (s = this.element), this._close(s), this.blur(t), this.activeMenu = s;
            }, this.delay);
        },
        _close: function(e) {
            e || (e = this.active ? this.active.parent() : this.element), e.find(".ui-menu").hide().attr("aria-hidden", "true").attr("aria-expanded", "false").end().find(".ui-state-active").not(".ui-state-focus").removeClass("ui-state-active");
        },
        _closeOnDocumentClick: function(t) {
            return !e(t.target).closest(".ui-menu").length;
        },
        _isDivider: function(e) {
            return !/[^\-\u2014\u2013\s]/.test(e.text());
        },
        collapse: function(e) {
            var t = this.active && this.active.parent().closest(".ui-menu-item", this.element);
            t && t.length && (this._close(), this.focus(e, t));
        },
        expand: function(e) {
            var t = this.active && this.active.children(".ui-menu ").find(this.options.items).first();
            t && t.length && (this._open(t.parent()), this._delay(function() {
                this.focus(e, t);
            }));
        },
        next: function(e) {
            this._move("next", "first", e);
        },
        previous: function(e) {
            this._move("prev", "last", e);
        },
        isFirstItem: function() {
            return this.active && !this.active.prevAll(".ui-menu-item").length;
        },
        isLastItem: function() {
            return this.active && !this.active.nextAll(".ui-menu-item").length;
        },
        _move: function(e, t, i) {
            var s;
            this.active && (s = "first" === e || "last" === e ? this.active["first" === e ? "prevAll" : "nextAll"](".ui-menu-item").eq(-1) : this.active[e + "All"](".ui-menu-item").eq(0)), 
            s && s.length && this.active || (s = this.activeMenu.find(this.options.items)[t]()), 
            this.focus(i, s);
        },
        nextPage: function(t) {
            var i, s, n;
            return this.active ? void (this.isLastItem() || (this._hasScroll() ? (s = this.active.offset().top, 
            n = this.element.height(), this.active.nextAll(".ui-menu-item").each(function() {
                return i = e(this), 0 > i.offset().top - s - n;
            }), this.focus(t, i)) : this.focus(t, this.activeMenu.find(this.options.items)[this.active ? "last" : "first"]()))) : void this.next(t);
        },
        previousPage: function(t) {
            var i, s, n;
            return this.active ? void (this.isFirstItem() || (this._hasScroll() ? (s = this.active.offset().top, 
            n = this.element.height(), this.active.prevAll(".ui-menu-item").each(function() {
                return i = e(this), i.offset().top - s + n > 0;
            }), this.focus(t, i)) : this.focus(t, this.activeMenu.find(this.options.items).first()))) : void this.next(t);
        },
        _hasScroll: function() {
            return this.element.outerHeight() < this.element.prop("scrollHeight");
        },
        select: function(t) {
            this.active = this.active || e(t.target).closest(".ui-menu-item");
            var i = {
                item: this.active
            };
            this.active.has(".ui-menu").length || this.collapseAll(t, !0), this._trigger("select", t, i);
        },
        _filterMenuItems: function(t) {
            var i = t.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&"), s = RegExp("^" + i, "i");
            return this.activeMenu.find(this.options.items).filter(".ui-menu-item").filter(function() {
                return s.test(e.trim(e(this).text()));
            });
        }
    }), e.widget("ui.autocomplete", {
        version: "1.11.2",
        defaultElement: "<input>",
        options: {
            appendTo: null,
            autoFocus: !1,
            delay: 300,
            minLength: 1,
            position: {
                my: "left top",
                at: "left bottom",
                collision: "none"
            },
            source: null,
            change: null,
            close: null,
            focus: null,
            open: null,
            response: null,
            search: null,
            select: null
        },
        requestIndex: 0,
        pending: 0,
        _create: function() {
            var t, i, s, n = this.element[0].nodeName.toLowerCase(), a = "textarea" === n, o = "input" === n;
            this.isMultiLine = !!a || !o && this.element.prop("isContentEditable"), this.valueMethod = this.element[a || o ? "val" : "text"], 
            this.isNewMenu = !0, this.element.addClass("ui-autocomplete-input").attr("autocomplete", "off"), 
            this._on(this.element, {
                keydown: function(n) {
                    if (this.element.prop("readOnly")) return t = !0, s = !0, void (i = !0);
                    t = !1, s = !1, i = !1;
                    var a = e.ui.keyCode;
                    switch (n.keyCode) {
                      case a.PAGE_UP:
                        t = !0, this._move("previousPage", n);
                        break;

                      case a.PAGE_DOWN:
                        t = !0, this._move("nextPage", n);
                        break;

                      case a.UP:
                        t = !0, this._keyEvent("previous", n);
                        break;

                      case a.DOWN:
                        t = !0, this._keyEvent("next", n);
                        break;

                      case a.ENTER:
                        this.menu.active && (t = !0, n.preventDefault(), this.menu.select(n));
                        break;

                      case a.TAB:
                        this.menu.active && this.menu.select(n);
                        break;

                      case a.ESCAPE:
                        this.menu.element.is(":visible") && (this.isMultiLine || this._value(this.term), 
                        this.close(n), n.preventDefault());
                        break;

                      default:
                        i = !0, this._searchTimeout(n);
                    }
                },
                keypress: function(s) {
                    if (t) return t = !1, void ((!this.isMultiLine || this.menu.element.is(":visible")) && s.preventDefault());
                    if (!i) {
                        var n = e.ui.keyCode;
                        switch (s.keyCode) {
                          case n.PAGE_UP:
                            this._move("previousPage", s);
                            break;

                          case n.PAGE_DOWN:
                            this._move("nextPage", s);
                            break;

                          case n.UP:
                            this._keyEvent("previous", s);
                            break;

                          case n.DOWN:
                            this._keyEvent("next", s);
                        }
                    }
                },
                input: function(e) {
                    return s ? (s = !1, void e.preventDefault()) : void this._searchTimeout(e);
                },
                focus: function() {
                    this.selectedItem = null, this.previous = this._value();
                },
                blur: function(e) {
                    return this.cancelBlur ? void delete this.cancelBlur : (clearTimeout(this.searching), 
                    this.close(e), void this._change(e));
                }
            }), this._initSource(), this.menu = e("<ul>").addClass("ui-autocomplete ui-front").appendTo(this._appendTo()).menu({
                role: null
            }).hide().menu("instance"), this._on(this.menu.element, {
                mousedown: function(t) {
                    t.preventDefault(), this.cancelBlur = !0, this._delay(function() {
                        delete this.cancelBlur;
                    });
                    var i = this.menu.element[0];
                    e(t.target).closest(".ui-menu-item").length || this._delay(function() {
                        var t = this;
                        this.document.one("mousedown", function(s) {
                            s.target === t.element[0] || s.target === i || e.contains(i, s.target) || t.close();
                        });
                    });
                },
                menufocus: function(t, i) {
                    var s, n;
                    return this.isNewMenu && (this.isNewMenu = !1, t.originalEvent && /^mouse/.test(t.originalEvent.type)) ? (this.menu.blur(), 
                    void this.document.one("mousemove", function() {
                        e(t.target).trigger(t.originalEvent);
                    })) : (n = i.item.data("ui-autocomplete-item"), !1 !== this._trigger("focus", t, {
                        item: n
                    }) && t.originalEvent && /^key/.test(t.originalEvent.type) && this._value(n.value), 
                    void ((s = i.item.attr("aria-label") || n.value) && e.trim(s).length && (this.liveRegion.children().hide(), 
                    e("<div>").text(s).appendTo(this.liveRegion))));
                },
                menuselect: function(e, t) {
                    var i = t.item.data("ui-autocomplete-item"), s = this.previous;
                    this.element[0] !== this.document[0].activeElement && (this.element.focus(), this.previous = s, 
                    this._delay(function() {
                        this.previous = s, this.selectedItem = i;
                    })), !1 !== this._trigger("select", e, {
                        item: i
                    }) && this._value(i.value), this.term = this._value(), this.close(e), this.selectedItem = i;
                }
            }), this.liveRegion = e("<span>", {
                role: "status",
                "aria-live": "assertive",
                "aria-relevant": "additions"
            }).addClass("ui-helper-hidden-accessible").appendTo(this.document[0].body), this._on(this.window, {
                beforeunload: function() {
                    this.element.removeAttr("autocomplete");
                }
            });
        },
        _destroy: function() {
            clearTimeout(this.searching), this.element.removeClass("ui-autocomplete-input").removeAttr("autocomplete"), 
            this.menu.element.remove(), this.liveRegion.remove();
        },
        _setOption: function(e, t) {
            this._super(e, t), "source" === e && this._initSource(), "appendTo" === e && this.menu.element.appendTo(this._appendTo()), 
            "disabled" === e && t && this.xhr && this.xhr.abort();
        },
        _appendTo: function() {
            var t = this.options.appendTo;
            return t && (t = t.jquery || t.nodeType ? e(t) : this.document.find(t).eq(0)), t && t[0] || (t = this.element.closest(".ui-front")), 
            t.length || (t = this.document[0].body), t;
        },
        _initSource: function() {
            var t, i, s = this;
            e.isArray(this.options.source) ? (t = this.options.source, this.source = function(i, s) {
                s(e.ui.autocomplete.filter(t, i.term));
            }) : "string" == typeof this.options.source ? (i = this.options.source, this.source = function(t, n) {
                s.xhr && s.xhr.abort(), s.xhr = e.ajax({
                    url: i,
                    data: t,
                    dataType: "json",
                    success: function(e) {
                        n(e);
                    },
                    error: function() {
                        n([]);
                    }
                });
            }) : this.source = this.options.source;
        },
        _searchTimeout: function(e) {
            clearTimeout(this.searching), this.searching = this._delay(function() {
                var t = this.term === this._value(), i = this.menu.element.is(":visible"), s = e.altKey || e.ctrlKey || e.metaKey || e.shiftKey;
                (!t || t && !i && !s) && (this.selectedItem = null, this.search(null, e));
            }, this.options.delay);
        },
        search: function(e, t) {
            return e = null != e ? e : this._value(), this.term = this._value(), e.length < this.options.minLength ? this.close(t) : !1 !== this._trigger("search", t) ? this._search(e) : void 0;
        },
        _search: function(e) {
            this.pending++, this.element.addClass("ui-autocomplete-loading"), this.cancelSearch = !1, 
            this.source({
                term: e
            }, this._response());
        },
        _response: function() {
            var t = ++this.requestIndex;
            return e.proxy(function(e) {
                t === this.requestIndex && this.__response(e), --this.pending || this.element.removeClass("ui-autocomplete-loading");
            }, this);
        },
        __response: function(e) {
            e && (e = this._normalize(e)), this._trigger("response", null, {
                content: e
            }), !this.options.disabled && e && e.length && !this.cancelSearch ? (this._suggest(e), 
            this._trigger("open")) : this._close();
        },
        close: function(e) {
            this.cancelSearch = !0, this._close(e);
        },
        _close: function(e) {
            this.menu.element.is(":visible") && (this.menu.element.hide(), this.menu.blur(), 
            this.isNewMenu = !0, this._trigger("close", e));
        },
        _change: function(e) {
            this.previous !== this._value() && this._trigger("change", e, {
                item: this.selectedItem
            });
        },
        _normalize: function(t) {
            return t.length && t[0].label && t[0].value ? t : e.map(t, function(t) {
                return "string" == typeof t ? {
                    label: t,
                    value: t
                } : e.extend({}, t, {
                    label: t.label || t.value,
                    value: t.value || t.label
                });
            });
        },
        _suggest: function(t) {
            var i = this.menu.element.empty();
            this._renderMenu(i, t), this.isNewMenu = !0, this.menu.refresh(), i.show(), this._resizeMenu(), 
            i.position(e.extend({
                of: this.element
            }, this.options.position)), this.options.autoFocus && this.menu.next();
        },
        _resizeMenu: function() {
            var e = this.menu.element;
            e.outerWidth(Math.max(e.width("").outerWidth() + 1, this.element.outerWidth()));
        },
        _renderMenu: function(t, i) {
            var s = this;
            e.each(i, function(e, i) {
                s._renderItemData(t, i);
            });
        },
        _renderItemData: function(e, t) {
            return this._renderItem(e, t).data("ui-autocomplete-item", t);
        },
        _renderItem: function(t, i) {
            return e("<li>").text(i.label).appendTo(t);
        },
        _move: function(e, t) {
            return this.menu.element.is(":visible") ? this.menu.isFirstItem() && /^previous/.test(e) || this.menu.isLastItem() && /^next/.test(e) ? (this.isMultiLine || this._value(this.term), 
            void this.menu.blur()) : void this.menu[e](t) : void this.search(null, t);
        },
        widget: function() {
            return this.menu.element;
        },
        _value: function() {
            return this.valueMethod.apply(this.element, arguments);
        },
        _keyEvent: function(e, t) {
            (!this.isMultiLine || this.menu.element.is(":visible")) && (this._move(e, t), t.preventDefault());
        }
    }), e.extend(e.ui.autocomplete, {
        escapeRegex: function(e) {
            return e.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&");
        },
        filter: function(t, i) {
            var s = RegExp(e.ui.autocomplete.escapeRegex(i), "i");
            return e.grep(t, function(e) {
                return s.test(e.label || e.value || e);
            });
        }
    }), e.widget("ui.autocomplete", e.ui.autocomplete, {
        options: {
            messages: {
                noResults: "No search results.",
                results: function(e) {
                    return e + (e > 1 ? " results are" : " result is") + " available, use up and down arrow keys to navigate.";
                }
            }
        },
        __response: function(t) {
            var i;
            this._superApply(arguments), this.options.disabled || this.cancelSearch || (i = t && t.length ? this.options.messages.results(t.length) : this.options.messages.noResults, 
            this.liveRegion.children().hide(), e("<div>").text(i).appendTo(this.liveRegion));
        }
    }), e.ui.autocomplete;
    var c, p = "ui-button ui-widget ui-state-default ui-corner-all", f = "ui-button-icons-only ui-button-icon-only ui-button-text-icons ui-button-text-icon-primary ui-button-text-icon-secondary ui-button-text-only", m = function() {
        var t = e(this);
        setTimeout(function() {
            t.find(":ui-button").button("refresh");
        }, 1);
    }, g = function(t) {
        var i = t.name, s = t.form, n = e([]);
        return i && (i = i.replace(/'/g, "\\'"), n = s ? e(s).find("[name='" + i + "'][type=radio]") : e("[name='" + i + "'][type=radio]", t.ownerDocument).filter(function() {
            return !this.form;
        })), n;
    };
    e.widget("ui.button", {
        version: "1.11.2",
        defaultElement: "<button>",
        options: {
            disabled: null,
            text: !0,
            label: null,
            icons: {
                primary: null,
                secondary: null
            }
        },
        _create: function() {
            this.element.closest("form").unbind("reset" + this.eventNamespace).bind("reset" + this.eventNamespace, m), 
            "boolean" != typeof this.options.disabled ? this.options.disabled = !!this.element.prop("disabled") : this.element.prop("disabled", this.options.disabled), 
            this._determineButtonType(), this.hasTitle = !!this.buttonElement.attr("title");
            var t = this, i = this.options, s = "checkbox" === this.type || "radio" === this.type, n = s ? "" : "ui-state-active";
            null === i.label && (i.label = "input" === this.type ? this.buttonElement.val() : this.buttonElement.html()), 
            this._hoverable(this.buttonElement), this.buttonElement.addClass(p).attr("role", "button").bind("mouseenter" + this.eventNamespace, function() {
                i.disabled || this === c && e(this).addClass("ui-state-active");
            }).bind("mouseleave" + this.eventNamespace, function() {
                i.disabled || e(this).removeClass(n);
            }).bind("click" + this.eventNamespace, function(e) {
                i.disabled && (e.preventDefault(), e.stopImmediatePropagation());
            }), this._on({
                focus: function() {
                    this.buttonElement.addClass("ui-state-focus");
                },
                blur: function() {
                    this.buttonElement.removeClass("ui-state-focus");
                }
            }), s && this.element.bind("change" + this.eventNamespace, function() {
                t.refresh();
            }), "checkbox" === this.type ? this.buttonElement.bind("click" + this.eventNamespace, function() {
                return !i.disabled && void 0;
            }) : "radio" === this.type ? this.buttonElement.bind("click" + this.eventNamespace, function() {
                if (i.disabled) return !1;
                e(this).addClass("ui-state-active"), t.buttonElement.attr("aria-pressed", "true");
                var s = t.element[0];
                g(s).not(s).map(function() {
                    return e(this).button("widget")[0];
                }).removeClass("ui-state-active").attr("aria-pressed", "false");
            }) : (this.buttonElement.bind("mousedown" + this.eventNamespace, function() {
                return !i.disabled && (e(this).addClass("ui-state-active"), c = this, void t.document.one("mouseup", function() {
                    c = null;
                }));
            }).bind("mouseup" + this.eventNamespace, function() {
                return !i.disabled && void e(this).removeClass("ui-state-active");
            }).bind("keydown" + this.eventNamespace, function(t) {
                return !i.disabled && void ((t.keyCode === e.ui.keyCode.SPACE || t.keyCode === e.ui.keyCode.ENTER) && e(this).addClass("ui-state-active"));
            }).bind("keyup" + this.eventNamespace + " blur" + this.eventNamespace, function() {
                e(this).removeClass("ui-state-active");
            }), this.buttonElement.is("a") && this.buttonElement.keyup(function(t) {
                t.keyCode === e.ui.keyCode.SPACE && e(this).click();
            })), this._setOption("disabled", i.disabled), this._resetButton();
        },
        _determineButtonType: function() {
            var e, t, i;
            this.type = this.element.is("[type=checkbox]") ? "checkbox" : this.element.is("[type=radio]") ? "radio" : this.element.is("input") ? "input" : "button", 
            "checkbox" === this.type || "radio" === this.type ? (e = this.element.parents().last(), 
            t = "label[for='" + this.element.attr("id") + "']", this.buttonElement = e.find(t), 
            this.buttonElement.length || (e = e.length ? e.siblings() : this.element.siblings(), 
            this.buttonElement = e.filter(t), this.buttonElement.length || (this.buttonElement = e.find(t))), 
            this.element.addClass("ui-helper-hidden-accessible"), i = this.element.is(":checked"), 
            i && this.buttonElement.addClass("ui-state-active"), this.buttonElement.prop("aria-pressed", i)) : this.buttonElement = this.element;
        },
        widget: function() {
            return this.buttonElement;
        },
        _destroy: function() {
            this.element.removeClass("ui-helper-hidden-accessible"), this.buttonElement.removeClass(p + " ui-state-active " + f).removeAttr("role").removeAttr("aria-pressed").html(this.buttonElement.find(".ui-button-text").html()), 
            this.hasTitle || this.buttonElement.removeAttr("title");
        },
        _setOption: function(e, t) {
            return this._super(e, t), "disabled" === e ? (this.widget().toggleClass("ui-state-disabled", !!t), 
            this.element.prop("disabled", !!t), void (t && ("checkbox" === this.type || "radio" === this.type ? this.buttonElement.removeClass("ui-state-focus") : this.buttonElement.removeClass("ui-state-focus ui-state-active")))) : void this._resetButton();
        },
        refresh: function() {
            var t = this.element.is("input, button") ? this.element.is(":disabled") : this.element.hasClass("ui-button-disabled");
            t !== this.options.disabled && this._setOption("disabled", t), "radio" === this.type ? g(this.element[0]).each(function() {
                e(this).is(":checked") ? e(this).button("widget").addClass("ui-state-active").attr("aria-pressed", "true") : e(this).button("widget").removeClass("ui-state-active").attr("aria-pressed", "false");
            }) : "checkbox" === this.type && (this.element.is(":checked") ? this.buttonElement.addClass("ui-state-active").attr("aria-pressed", "true") : this.buttonElement.removeClass("ui-state-active").attr("aria-pressed", "false"));
        },
        _resetButton: function() {
            if ("input" === this.type) return void (this.options.label && this.element.val(this.options.label));
            var t = this.buttonElement.removeClass(f), i = e("<span></span>", this.document[0]).addClass("ui-button-text").html(this.options.label).appendTo(t.empty()).text(), s = this.options.icons, n = s.primary && s.secondary, a = [];
            s.primary || s.secondary ? (this.options.text && a.push("ui-button-text-icon" + (n ? "s" : s.primary ? "-primary" : "-secondary")), 
            s.primary && t.prepend("<span class='ui-button-icon-primary ui-icon " + s.primary + "'></span>"), 
            s.secondary && t.append("<span class='ui-button-icon-secondary ui-icon " + s.secondary + "'></span>"), 
            this.options.text || (a.push(n ? "ui-button-icons-only" : "ui-button-icon-only"), 
            this.hasTitle || t.attr("title", e.trim(i)))) : a.push("ui-button-text-only"), t.addClass(a.join(" "));
        }
    }), e.widget("ui.buttonset", {
        version: "1.11.2",
        options: {
            items: "button, input[type=button], input[type=submit], input[type=reset], input[type=checkbox], input[type=radio], a, :data(ui-button)"
        },
        _create: function() {
            this.element.addClass("ui-buttonset");
        },
        _init: function() {
            this.refresh();
        },
        _setOption: function(e, t) {
            "disabled" === e && this.buttons.button("option", e, t), this._super(e, t);
        },
        refresh: function() {
            var t = "rtl" === this.element.css("direction"), i = this.element.find(this.options.items), s = i.filter(":ui-button");
            i.not(":ui-button").button(), s.button("refresh"), this.buttons = i.map(function() {
                return e(this).button("widget")[0];
            }).removeClass("ui-corner-all ui-corner-left ui-corner-right").filter(":first").addClass(t ? "ui-corner-right" : "ui-corner-left").end().filter(":last").addClass(t ? "ui-corner-left" : "ui-corner-right").end().end();
        },
        _destroy: function() {
            this.element.removeClass("ui-buttonset"), this.buttons.map(function() {
                return e(this).button("widget")[0];
            }).removeClass("ui-corner-left ui-corner-right").end().button("destroy");
        }
    }), e.ui.button, e.extend(e.ui, {
        datepicker: {
            version: "1.11.2"
        }
    });
    var v;
    e.extend(n.prototype, {
        markerClassName: "hasDatepicker",
        maxRows: 4,
        _widgetDatepicker: function() {
            return this.dpDiv;
        },
        setDefaults: function(e) {
            return r(this._defaults, e || {}), this;
        },
        _attachDatepicker: function(t, i) {
            var s, n, a;
            s = t.nodeName.toLowerCase(), n = "div" === s || "span" === s, t.id || (this.uuid += 1, 
            t.id = "dp" + this.uuid), a = this._newInst(e(t), n), a.settings = e.extend({}, i || {}), 
            "input" === s ? this._connectDatepicker(t, a) : n && this._inlineDatepicker(t, a);
        },
        _newInst: function(t, i) {
            return {
                id: t[0].id.replace(/([^A-Za-z0-9_\-])/g, "\\\\$1"),
                input: t,
                selectedDay: 0,
                selectedMonth: 0,
                selectedYear: 0,
                drawMonth: 0,
                drawYear: 0,
                inline: i,
                dpDiv: i ? a(e("<div class='" + this._inlineClass + " ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all'></div>")) : this.dpDiv
            };
        },
        _connectDatepicker: function(t, i) {
            var s = e(t);
            i.append = e([]), i.trigger = e([]), s.hasClass(this.markerClassName) || (this._attachments(s, i), 
            s.addClass(this.markerClassName).keydown(this._doKeyDown).keypress(this._doKeyPress).keyup(this._doKeyUp), 
            this._autoSize(i), e.data(t, "datepicker", i), i.settings.disabled && this._disableDatepicker(t));
        },
        _attachments: function(t, i) {
            var s, n, a, o = this._get(i, "appendText"), r = this._get(i, "isRTL");
            i.append && i.append.remove(), o && (i.append = e("<span class='" + this._appendClass + "'>" + o + "</span>"), 
            t[r ? "before" : "after"](i.append)), t.unbind("focus", this._showDatepicker), i.trigger && i.trigger.remove(), 
            s = this._get(i, "showOn"), ("focus" === s || "both" === s) && t.focus(this._showDatepicker), 
            ("button" === s || "both" === s) && (n = this._get(i, "buttonText"), a = this._get(i, "buttonImage"), 
            i.trigger = e(this._get(i, "buttonImageOnly") ? e("<img/>").addClass(this._triggerClass).attr({
                src: a,
                alt: n,
                title: n
            }) : e("<button type='button'></button>").addClass(this._triggerClass).html(a ? e("<img/>").attr({
                src: a,
                alt: n,
                title: n
            }) : n)), t[r ? "before" : "after"](i.trigger), i.trigger.click(function() {
                return e.datepicker._datepickerShowing && e.datepicker._lastInput === t[0] ? e.datepicker._hideDatepicker() : e.datepicker._datepickerShowing && e.datepicker._lastInput !== t[0] ? (e.datepicker._hideDatepicker(), 
                e.datepicker._showDatepicker(t[0])) : e.datepicker._showDatepicker(t[0]), !1;
            }));
        },
        _autoSize: function(e) {
            if (this._get(e, "autoSize") && !e.inline) {
                var t, i, s, n, a = new Date(2009, 11, 20), o = this._get(e, "dateFormat");
                o.match(/[DM]/) && (t = function(e) {
                    for (i = 0, s = 0, n = 0; e.length > n; n++) e[n].length > i && (i = e[n].length, 
                    s = n);
                    return s;
                }, a.setMonth(t(this._get(e, o.match(/MM/) ? "monthNames" : "monthNamesShort"))), 
                a.setDate(t(this._get(e, o.match(/DD/) ? "dayNames" : "dayNamesShort")) + 20 - a.getDay())), 
                e.input.attr("size", this._formatDate(e, a).length);
            }
        },
        _inlineDatepicker: function(t, i) {
            var s = e(t);
            s.hasClass(this.markerClassName) || (s.addClass(this.markerClassName).append(i.dpDiv), 
            e.data(t, "datepicker", i), this._setDate(i, this._getDefaultDate(i), !0), this._updateDatepicker(i), 
            this._updateAlternate(i), i.settings.disabled && this._disableDatepicker(t), i.dpDiv.css("display", "block"));
        },
        _dialogDatepicker: function(t, i, s, n, a) {
            var o, h, l, u, d, c = this._dialogInst;
            return c || (this.uuid += 1, o = "dp" + this.uuid, this._dialogInput = e("<input type='text' id='" + o + "' style='position: absolute; top: -100px; width: 0px;'/>"), 
            this._dialogInput.keydown(this._doKeyDown), e("body").append(this._dialogInput), 
            c = this._dialogInst = this._newInst(this._dialogInput, !1), c.settings = {}, e.data(this._dialogInput[0], "datepicker", c)), 
            r(c.settings, n || {}), i = i && i.constructor === Date ? this._formatDate(c, i) : i, 
            this._dialogInput.val(i), this._pos = a ? a.length ? a : [ a.pageX, a.pageY ] : null, 
            this._pos || (h = document.documentElement.clientWidth, l = document.documentElement.clientHeight, 
            u = document.documentElement.scrollLeft || document.body.scrollLeft, d = document.documentElement.scrollTop || document.body.scrollTop, 
            this._pos = [ h / 2 - 100 + u, l / 2 - 150 + d ]), this._dialogInput.css("left", this._pos[0] + 20 + "px").css("top", this._pos[1] + "px"), 
            c.settings.onSelect = s, this._inDialog = !0, this.dpDiv.addClass(this._dialogClass), 
            this._showDatepicker(this._dialogInput[0]), e.blockUI && e.blockUI(this.dpDiv), 
            e.data(this._dialogInput[0], "datepicker", c), this;
        },
        _destroyDatepicker: function(t) {
            var i, s = e(t), n = e.data(t, "datepicker");
            s.hasClass(this.markerClassName) && (i = t.nodeName.toLowerCase(), e.removeData(t, "datepicker"), 
            "input" === i ? (n.append.remove(), n.trigger.remove(), s.removeClass(this.markerClassName).unbind("focus", this._showDatepicker).unbind("keydown", this._doKeyDown).unbind("keypress", this._doKeyPress).unbind("keyup", this._doKeyUp)) : ("div" === i || "span" === i) && s.removeClass(this.markerClassName).empty());
        },
        _enableDatepicker: function(t) {
            var i, s, n = e(t), a = e.data(t, "datepicker");
            n.hasClass(this.markerClassName) && (i = t.nodeName.toLowerCase(), "input" === i ? (t.disabled = !1, 
            a.trigger.filter("button").each(function() {
                this.disabled = !1;
            }).end().filter("img").css({
                opacity: "1.0",
                cursor: ""
            })) : ("div" === i || "span" === i) && (s = n.children("." + this._inlineClass), 
            s.children().removeClass("ui-state-disabled"), s.find("select.ui-datepicker-month, select.ui-datepicker-year").prop("disabled", !1)), 
            this._disabledInputs = e.map(this._disabledInputs, function(e) {
                return e === t ? null : e;
            }));
        },
        _disableDatepicker: function(t) {
            var i, s, n = e(t), a = e.data(t, "datepicker");
            n.hasClass(this.markerClassName) && (i = t.nodeName.toLowerCase(), "input" === i ? (t.disabled = !0, 
            a.trigger.filter("button").each(function() {
                this.disabled = !0;
            }).end().filter("img").css({
                opacity: "0.5",
                cursor: "default"
            })) : ("div" === i || "span" === i) && (s = n.children("." + this._inlineClass), 
            s.children().addClass("ui-state-disabled"), s.find("select.ui-datepicker-month, select.ui-datepicker-year").prop("disabled", !0)), 
            this._disabledInputs = e.map(this._disabledInputs, function(e) {
                return e === t ? null : e;
            }), this._disabledInputs[this._disabledInputs.length] = t);
        },
        _isDisabledDatepicker: function(e) {
            if (!e) return !1;
            for (var t = 0; this._disabledInputs.length > t; t++) if (this._disabledInputs[t] === e) return !0;
            return !1;
        },
        _getInst: function(t) {
            try {
                return e.data(t, "datepicker");
            } catch (i) {
                throw "Missing instance data for this datepicker";
            }
        },
        _optionDatepicker: function(t, i, s) {
            var n, a, o, h, l = this._getInst(t);
            return 2 === arguments.length && "string" == typeof i ? "defaults" === i ? e.extend({}, e.datepicker._defaults) : l ? "all" === i ? e.extend({}, l.settings) : this._get(l, i) : null : (n = i || {}, 
            "string" == typeof i && (n = {}, n[i] = s), void (l && (this._curInst === l && this._hideDatepicker(), 
            a = this._getDateDatepicker(t, !0), o = this._getMinMaxDate(l, "min"), h = this._getMinMaxDate(l, "max"), 
            r(l.settings, n), null !== o && void 0 !== n.dateFormat && void 0 === n.minDate && (l.settings.minDate = this._formatDate(l, o)), 
            null !== h && void 0 !== n.dateFormat && void 0 === n.maxDate && (l.settings.maxDate = this._formatDate(l, h)), 
            "disabled" in n && (n.disabled ? this._disableDatepicker(t) : this._enableDatepicker(t)), 
            this._attachments(e(t), l), this._autoSize(l), this._setDate(l, a), this._updateAlternate(l), 
            this._updateDatepicker(l))));
        },
        _changeDatepicker: function(e, t, i) {
            this._optionDatepicker(e, t, i);
        },
        _refreshDatepicker: function(e) {
            var t = this._getInst(e);
            t && this._updateDatepicker(t);
        },
        _setDateDatepicker: function(e, t) {
            var i = this._getInst(e);
            i && (this._setDate(i, t), this._updateDatepicker(i), this._updateAlternate(i));
        },
        _getDateDatepicker: function(e, t) {
            var i = this._getInst(e);
            return i && !i.inline && this._setDateFromField(i, t), i ? this._getDate(i) : null;
        },
        _doKeyDown: function(t) {
            var i, s, n, a = e.datepicker._getInst(t.target), o = !0, r = a.dpDiv.is(".ui-datepicker-rtl");
            if (a._keyEvent = !0, e.datepicker._datepickerShowing) switch (t.keyCode) {
              case 9:
                e.datepicker._hideDatepicker(), o = !1;
                break;

              case 13:
                return n = e("td." + e.datepicker._dayOverClass + ":not(." + e.datepicker._currentClass + ")", a.dpDiv), 
                n[0] && e.datepicker._selectDay(t.target, a.selectedMonth, a.selectedYear, n[0]), 
                i = e.datepicker._get(a, "onSelect"), i ? (s = e.datepicker._formatDate(a), i.apply(a.input ? a.input[0] : null, [ s, a ])) : e.datepicker._hideDatepicker(), 
                !1;

              case 27:
                e.datepicker._hideDatepicker();
                break;

              case 33:
                e.datepicker._adjustDate(t.target, t.ctrlKey ? -e.datepicker._get(a, "stepBigMonths") : -e.datepicker._get(a, "stepMonths"), "M");
                break;

              case 34:
                e.datepicker._adjustDate(t.target, t.ctrlKey ? +e.datepicker._get(a, "stepBigMonths") : +e.datepicker._get(a, "stepMonths"), "M");
                break;

              case 35:
                (t.ctrlKey || t.metaKey) && e.datepicker._clearDate(t.target), o = t.ctrlKey || t.metaKey;
                break;

              case 36:
                (t.ctrlKey || t.metaKey) && e.datepicker._gotoToday(t.target), o = t.ctrlKey || t.metaKey;
                break;

              case 37:
                (t.ctrlKey || t.metaKey) && e.datepicker._adjustDate(t.target, r ? 1 : -1, "D"), 
                o = t.ctrlKey || t.metaKey, t.originalEvent.altKey && e.datepicker._adjustDate(t.target, t.ctrlKey ? -e.datepicker._get(a, "stepBigMonths") : -e.datepicker._get(a, "stepMonths"), "M");
                break;

              case 38:
                (t.ctrlKey || t.metaKey) && e.datepicker._adjustDate(t.target, -7, "D"), o = t.ctrlKey || t.metaKey;
                break;

              case 39:
                (t.ctrlKey || t.metaKey) && e.datepicker._adjustDate(t.target, r ? -1 : 1, "D"), 
                o = t.ctrlKey || t.metaKey, t.originalEvent.altKey && e.datepicker._adjustDate(t.target, t.ctrlKey ? +e.datepicker._get(a, "stepBigMonths") : +e.datepicker._get(a, "stepMonths"), "M");
                break;

              case 40:
                (t.ctrlKey || t.metaKey) && e.datepicker._adjustDate(t.target, 7, "D"), o = t.ctrlKey || t.metaKey;
                break;

              default:
                o = !1;
            } else 36 === t.keyCode && t.ctrlKey ? e.datepicker._showDatepicker(this) : o = !1;
            o && (t.preventDefault(), t.stopPropagation());
        },
        _doKeyPress: function(t) {
            var i, s, n = e.datepicker._getInst(t.target);
            return e.datepicker._get(n, "constrainInput") ? (i = e.datepicker._possibleChars(e.datepicker._get(n, "dateFormat")), 
            s = String.fromCharCode(null == t.charCode ? t.keyCode : t.charCode), t.ctrlKey || t.metaKey || " " > s || !i || i.indexOf(s) > -1) : void 0;
        },
        _doKeyUp: function(t) {
            var s = e.datepicker._getInst(t.target);
            if (s.input.val() !== s.lastVal) try {
                e.datepicker.parseDate(e.datepicker._get(s, "dateFormat"), s.input ? s.input.val() : null, e.datepicker._getFormatConfig(s)) && (e.datepicker._setDateFromField(s), 
                e.datepicker._updateAlternate(s), e.datepicker._updateDatepicker(s));
            } catch (n) {}
            return !0;
        },
        _showDatepicker: function(t) {
            if (t = t.target || t, "input" !== t.nodeName.toLowerCase() && (t = e("input", t.parentNode)[0]), 
            !e.datepicker._isDisabledDatepicker(t) && e.datepicker._lastInput !== t) {
                var i, n, a, o, h, l, u;
                i = e.datepicker._getInst(t), e.datepicker._curInst && e.datepicker._curInst !== i && (e.datepicker._curInst.dpDiv.stop(!0, !0), 
                i && e.datepicker._datepickerShowing && e.datepicker._hideDatepicker(e.datepicker._curInst.input[0])), 
                n = e.datepicker._get(i, "beforeShow"), !1 !== (a = n ? n.apply(t, [ t, i ]) : {}) && (r(i.settings, a), 
                i.lastVal = null, e.datepicker._lastInput = t, e.datepicker._setDateFromField(i), 
                e.datepicker._inDialog && (t.value = ""), e.datepicker._pos || (e.datepicker._pos = e.datepicker._findPos(t), 
                e.datepicker._pos[1] += t.offsetHeight), o = !1, e(t).parents().each(function() {
                    return !(o |= "fixed" === e(this).css("position"));
                }), h = {
                    left: e.datepicker._pos[0],
                    top: e.datepicker._pos[1]
                }, e.datepicker._pos = null, i.dpDiv.empty(), i.dpDiv.css({
                    position: "absolute",
                    display: "block",
                    top: "-1000px"
                }), e.datepicker._updateDatepicker(i), h = e.datepicker._checkOffset(i, h, o), i.dpDiv.css({
                    position: e.datepicker._inDialog && e.blockUI ? "static" : o ? "fixed" : "absolute",
                    display: "none",
                    left: h.left + "px",
                    top: h.top + "px"
                }), i.inline || (l = e.datepicker._get(i, "showAnim"), u = e.datepicker._get(i, "duration"), 
                i.dpDiv.css("z-index", s(e(t)) + 1), e.datepicker._datepickerShowing = !0, e.effects && e.effects.effect[l] ? i.dpDiv.show(l, e.datepicker._get(i, "showOptions"), u) : i.dpDiv[l || "show"](l ? u : null), 
                e.datepicker._shouldFocusInput(i) && i.input.focus(), e.datepicker._curInst = i));
            }
        },
        _updateDatepicker: function(t) {
            this.maxRows = 4, v = t, t.dpDiv.empty().append(this._generateHTML(t)), this._attachHandlers(t);
            var i, s = this._getNumberOfMonths(t), n = s[1], r = t.dpDiv.find("." + this._dayOverClass + " a");
            r.length > 0 && o.apply(r.get(0)), t.dpDiv.removeClass("ui-datepicker-multi-2 ui-datepicker-multi-3 ui-datepicker-multi-4").width(""), 
            n > 1 && t.dpDiv.addClass("ui-datepicker-multi-" + n).css("width", 17 * n + "em"), 
            t.dpDiv[(1 !== s[0] || 1 !== s[1] ? "add" : "remove") + "Class"]("ui-datepicker-multi"), 
            t.dpDiv[(this._get(t, "isRTL") ? "add" : "remove") + "Class"]("ui-datepicker-rtl"), 
            t === e.datepicker._curInst && e.datepicker._datepickerShowing && e.datepicker._shouldFocusInput(t) && t.input.focus(), 
            t.yearshtml && (i = t.yearshtml, setTimeout(function() {
                i === t.yearshtml && t.yearshtml && t.dpDiv.find("select.ui-datepicker-year:first").replaceWith(t.yearshtml), 
                i = t.yearshtml = null;
            }, 0));
        },
        _shouldFocusInput: function(e) {
            return e.input && e.input.is(":visible") && !e.input.is(":disabled") && !e.input.is(":focus");
        },
        _checkOffset: function(t, i, s) {
            var n = t.dpDiv.outerWidth(), a = t.dpDiv.outerHeight(), o = t.input ? t.input.outerWidth() : 0, r = t.input ? t.input.outerHeight() : 0, h = document.documentElement.clientWidth + (s ? 0 : e(document).scrollLeft()), l = document.documentElement.clientHeight + (s ? 0 : e(document).scrollTop());
            return i.left -= this._get(t, "isRTL") ? n - o : 0, i.left -= s && i.left === t.input.offset().left ? e(document).scrollLeft() : 0, 
            i.top -= s && i.top === t.input.offset().top + r ? e(document).scrollTop() : 0, 
            i.left -= Math.min(i.left, i.left + n > h && h > n ? Math.abs(i.left + n - h) : 0), 
            i.top -= Math.min(i.top, i.top + a > l && l > a ? Math.abs(a + r) : 0), i;
        },
        _findPos: function(t) {
            for (var i, s = this._getInst(t), n = this._get(s, "isRTL"); t && ("hidden" === t.type || 1 !== t.nodeType || e.expr.filters.hidden(t)); ) t = t[n ? "previousSibling" : "nextSibling"];
            return i = e(t).offset(), [ i.left, i.top ];
        },
        _hideDatepicker: function(t) {
            var i, s, n, a, o = this._curInst;
            !o || t && o !== e.data(t, "datepicker") || this._datepickerShowing && (i = this._get(o, "showAnim"), 
            s = this._get(o, "duration"), n = function() {
                e.datepicker._tidyDialog(o);
            }, e.effects && (e.effects.effect[i] || e.effects[i]) ? o.dpDiv.hide(i, e.datepicker._get(o, "showOptions"), s, n) : o.dpDiv["slideDown" === i ? "slideUp" : "fadeIn" === i ? "fadeOut" : "hide"](i ? s : null, n), 
            i || n(), this._datepickerShowing = !1, a = this._get(o, "onClose"), a && a.apply(o.input ? o.input[0] : null, [ o.input ? o.input.val() : "", o ]), 
            this._lastInput = null, this._inDialog && (this._dialogInput.css({
                position: "absolute",
                left: "0",
                top: "-100px"
            }), e.blockUI && (e.unblockUI(), e("body").append(this.dpDiv))), this._inDialog = !1);
        },
        _tidyDialog: function(e) {
            e.dpDiv.removeClass(this._dialogClass).unbind(".ui-datepicker-calendar");
        },
        _checkExternalClick: function(t) {
            if (e.datepicker._curInst) {
                var i = e(t.target), s = e.datepicker._getInst(i[0]);
                (i[0].id !== e.datepicker._mainDivId && 0 === i.parents("#" + e.datepicker._mainDivId).length && !i.hasClass(e.datepicker.markerClassName) && !i.closest("." + e.datepicker._triggerClass).length && e.datepicker._datepickerShowing && (!e.datepicker._inDialog || !e.blockUI) || i.hasClass(e.datepicker.markerClassName) && e.datepicker._curInst !== s) && e.datepicker._hideDatepicker();
            }
        },
        _adjustDate: function(t, i, s) {
            var n = e(t), a = this._getInst(n[0]);
            this._isDisabledDatepicker(n[0]) || (this._adjustInstDate(a, i + ("M" === s ? this._get(a, "showCurrentAtPos") : 0), s), 
            this._updateDatepicker(a));
        },
        _gotoToday: function(t) {
            var i, s = e(t), n = this._getInst(s[0]);
            this._get(n, "gotoCurrent") && n.currentDay ? (n.selectedDay = n.currentDay, n.drawMonth = n.selectedMonth = n.currentMonth, 
            n.drawYear = n.selectedYear = n.currentYear) : (i = new Date(), n.selectedDay = i.getDate(), 
            n.drawMonth = n.selectedMonth = i.getMonth(), n.drawYear = n.selectedYear = i.getFullYear()), 
            this._notifyChange(n), this._adjustDate(s);
        },
        _selectMonthYear: function(t, i, s) {
            var n = e(t), a = this._getInst(n[0]);
            a["selected" + ("M" === s ? "Month" : "Year")] = a["draw" + ("M" === s ? "Month" : "Year")] = parseInt(i.options[i.selectedIndex].value, 10), 
            this._notifyChange(a), this._adjustDate(n);
        },
        _selectDay: function(t, i, s, n) {
            var a, o = e(t);
            e(n).hasClass(this._unselectableClass) || this._isDisabledDatepicker(o[0]) || (a = this._getInst(o[0]), 
            a.selectedDay = a.currentDay = e("a", n).html(), a.selectedMonth = a.currentMonth = i, 
            a.selectedYear = a.currentYear = s, this._selectDate(t, this._formatDate(a, a.currentDay, a.currentMonth, a.currentYear)));
        },
        _clearDate: function(t) {
            var i = e(t);
            this._selectDate(i, "");
        },
        _selectDate: function(t, i) {
            var s, n = e(t), a = this._getInst(n[0]);
            i = null != i ? i : this._formatDate(a), a.input && a.input.val(i), this._updateAlternate(a), 
            s = this._get(a, "onSelect"), s ? s.apply(a.input ? a.input[0] : null, [ i, a ]) : a.input && a.input.trigger("change"), 
            a.inline ? this._updateDatepicker(a) : (this._hideDatepicker(), this._lastInput = a.input[0], 
            "object" != typeof a.input[0] && a.input.focus(), this._lastInput = null);
        },
        _updateAlternate: function(t) {
            var i, s, n, a = this._get(t, "altField");
            a && (i = this._get(t, "altFormat") || this._get(t, "dateFormat"), s = this._getDate(t), 
            n = this.formatDate(i, s, this._getFormatConfig(t)), e(a).each(function() {
                e(this).val(n);
            }));
        },
        noWeekends: function(e) {
            var t = e.getDay();
            return [ t > 0 && 6 > t, "" ];
        },
        iso8601Week: function(e) {
            var t, i = new Date(e.getTime());
            return i.setDate(i.getDate() + 4 - (i.getDay() || 7)), t = i.getTime(), i.setMonth(0), 
            i.setDate(1), Math.floor(Math.round((t - i) / 864e5) / 7) + 1;
        },
        parseDate: function(t, i, s) {
            if (null == t || null == i) throw "Invalid arguments";
            if ("" === (i = "object" == typeof i ? "" + i : i + "")) return null;
            var n, a, o, r, h = 0, l = (s ? s.shortYearCutoff : null) || this._defaults.shortYearCutoff, u = "string" != typeof l ? l : new Date().getFullYear() % 100 + parseInt(l, 10), d = (s ? s.dayNamesShort : null) || this._defaults.dayNamesShort, c = (s ? s.dayNames : null) || this._defaults.dayNames, p = (s ? s.monthNamesShort : null) || this._defaults.monthNamesShort, f = (s ? s.monthNames : null) || this._defaults.monthNames, m = -1, g = -1, v = -1, y = -1, b = !1, _ = function(e) {
                var i = t.length > n + 1 && t.charAt(n + 1) === e;
                return i && n++, i;
            }, x = function(e) {
                var t = _(e), s = "@" === e ? 14 : "!" === e ? 20 : "y" === e && t ? 4 : "o" === e ? 3 : 2, n = "y" === e ? s : 1, a = RegExp("^\\d{" + n + "," + s + "}"), o = i.substring(h).match(a);
                if (!o) throw "Missing number at position " + h;
                return h += o[0].length, parseInt(o[0], 10);
            }, w = function(t, s, n) {
                var a = -1, o = e.map(_(t) ? n : s, function(e, t) {
                    return [ [ t, e ] ];
                }).sort(function(e, t) {
                    return -(e[1].length - t[1].length);
                });
                if (e.each(o, function(e, t) {
                    var s = t[1];
                    return i.substr(h, s.length).toLowerCase() === s.toLowerCase() ? (a = t[0], h += s.length, 
                    !1) : void 0;
                }), -1 !== a) return a + 1;
                throw "Unknown name at position " + h;
            }, k = function() {
                if (i.charAt(h) !== t.charAt(n)) throw "Unexpected literal at position " + h;
                h++;
            };
            for (n = 0; t.length > n; n++) if (b) "'" !== t.charAt(n) || _("'") ? k() : b = !1; else switch (t.charAt(n)) {
              case "d":
                v = x("d");
                break;

              case "D":
                w("D", d, c);
                break;

              case "o":
                y = x("o");
                break;

              case "m":
                g = x("m");
                break;

              case "M":
                g = w("M", p, f);
                break;

              case "y":
                m = x("y");
                break;

              case "@":
                r = new Date(x("@")), m = r.getFullYear(), g = r.getMonth() + 1, v = r.getDate();
                break;

              case "!":
                r = new Date((x("!") - this._ticksTo1970) / 1e4), m = r.getFullYear(), g = r.getMonth() + 1, 
                v = r.getDate();
                break;

              case "'":
                _("'") ? k() : b = !0;
                break;

              default:
                k();
            }
            if (i.length > h && (o = i.substr(h), !/^\s+/.test(o))) throw "Extra/unparsed characters found in date: " + o;
            if (-1 === m ? m = new Date().getFullYear() : 100 > m && (m += new Date().getFullYear() - new Date().getFullYear() % 100 + (u >= m ? 0 : -100)), 
            y > -1) for (g = 1, v = y; !((a = this._getDaysInMonth(m, g - 1)) >= v); ) g++, 
            v -= a;
            if (r = this._daylightSavingAdjust(new Date(m, g - 1, v)), r.getFullYear() !== m || r.getMonth() + 1 !== g || r.getDate() !== v) throw "Invalid date";
            return r;
        },
        ATOM: "yy-mm-dd",
        COOKIE: "D, dd M yy",
        ISO_8601: "yy-mm-dd",
        RFC_822: "D, d M y",
        RFC_850: "DD, dd-M-y",
        RFC_1036: "D, d M y",
        RFC_1123: "D, d M yy",
        RFC_2822: "D, d M yy",
        RSS: "D, d M y",
        TICKS: "!",
        TIMESTAMP: "@",
        W3C: "yy-mm-dd",
        _ticksTo1970: 864e9 * (718685 + Math.floor(492.5) - Math.floor(19.7) + Math.floor(4.925)),
        formatDate: function(e, t, i) {
            if (!t) return "";
            var s, n = (i ? i.dayNamesShort : null) || this._defaults.dayNamesShort, a = (i ? i.dayNames : null) || this._defaults.dayNames, o = (i ? i.monthNamesShort : null) || this._defaults.monthNamesShort, r = (i ? i.monthNames : null) || this._defaults.monthNames, h = function(t) {
                var i = e.length > s + 1 && e.charAt(s + 1) === t;
                return i && s++, i;
            }, l = function(e, t, i) {
                var s = "" + t;
                if (h(e)) for (;i > s.length; ) s = "0" + s;
                return s;
            }, u = function(e, t, i, s) {
                return h(e) ? s[t] : i[t];
            }, d = "", c = !1;
            if (t) for (s = 0; e.length > s; s++) if (c) "'" !== e.charAt(s) || h("'") ? d += e.charAt(s) : c = !1; else switch (e.charAt(s)) {
              case "d":
                d += l("d", t.getDate(), 2);
                break;

              case "D":
                d += u("D", t.getDay(), n, a);
                break;

              case "o":
                d += l("o", Math.round((new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime() - new Date(t.getFullYear(), 0, 0).getTime()) / 864e5), 3);
                break;

              case "m":
                d += l("m", t.getMonth() + 1, 2);
                break;

              case "M":
                d += u("M", t.getMonth(), o, r);
                break;

              case "y":
                d += h("y") ? t.getFullYear() : (10 > t.getYear() % 100 ? "0" : "") + t.getYear() % 100;
                break;

              case "@":
                d += t.getTime();
                break;

              case "!":
                d += 1e4 * t.getTime() + this._ticksTo1970;
                break;

              case "'":
                h("'") ? d += "'" : c = !0;
                break;

              default:
                d += e.charAt(s);
            }
            return d;
        },
        _possibleChars: function(e) {
            var t, i = "", s = !1, n = function(i) {
                var s = e.length > t + 1 && e.charAt(t + 1) === i;
                return s && t++, s;
            };
            for (t = 0; e.length > t; t++) if (s) "'" !== e.charAt(t) || n("'") ? i += e.charAt(t) : s = !1; else switch (e.charAt(t)) {
              case "d":
              case "m":
              case "y":
              case "@":
                i += "0123456789";
                break;

              case "D":
              case "M":
                return null;

              case "'":
                n("'") ? i += "'" : s = !0;
                break;

              default:
                i += e.charAt(t);
            }
            return i;
        },
        _get: function(e, t) {
            return void 0 !== e.settings[t] ? e.settings[t] : this._defaults[t];
        },
        _setDateFromField: function(e, t) {
            if (e.input.val() !== e.lastVal) {
                var i = this._get(e, "dateFormat"), s = e.lastVal = e.input ? e.input.val() : null, n = this._getDefaultDate(e), a = n, o = this._getFormatConfig(e);
                try {
                    a = this.parseDate(i, s, o) || n;
                } catch (r) {
                    s = t ? "" : s;
                }
                e.selectedDay = a.getDate(), e.drawMonth = e.selectedMonth = a.getMonth(), e.drawYear = e.selectedYear = a.getFullYear(), 
                e.currentDay = s ? a.getDate() : 0, e.currentMonth = s ? a.getMonth() : 0, e.currentYear = s ? a.getFullYear() : 0, 
                this._adjustInstDate(e);
            }
        },
        _getDefaultDate: function(e) {
            return this._restrictMinMax(e, this._determineDate(e, this._get(e, "defaultDate"), new Date()));
        },
        _determineDate: function(t, i, s) {
            var o = null == i || "" === i ? s : "string" == typeof i ? function(i) {
                try {
                    return e.datepicker.parseDate(e.datepicker._get(t, "dateFormat"), i, e.datepicker._getFormatConfig(t));
                } catch (s) {}
                for (var n = (i.toLowerCase().match(/^c/) ? e.datepicker._getDate(t) : null) || new Date(), a = n.getFullYear(), o = n.getMonth(), r = n.getDate(), h = /([+\-]?[0-9]+)\s*(d|D|w|W|m|M|y|Y)?/g, l = h.exec(i); l; ) {
                    switch (l[2] || "d") {
                      case "d":
                      case "D":
                        r += parseInt(l[1], 10);
                        break;

                      case "w":
                      case "W":
                        r += 7 * parseInt(l[1], 10);
                        break;

                      case "m":
                      case "M":
                        o += parseInt(l[1], 10), r = Math.min(r, e.datepicker._getDaysInMonth(a, o));
                        break;

                      case "y":
                      case "Y":
                        a += parseInt(l[1], 10), r = Math.min(r, e.datepicker._getDaysInMonth(a, o));
                    }
                    l = h.exec(i);
                }
                return new Date(a, o, r);
            }(i) : "number" == typeof i ? isNaN(i) ? s : function(e) {
                var t = new Date();
                return t.setDate(t.getDate() + e), t;
            }(i) : new Date(i.getTime());
            return o = o && "Invalid Date" == "" + o ? s : o, o && (o.setHours(0), o.setMinutes(0), 
            o.setSeconds(0), o.setMilliseconds(0)), this._daylightSavingAdjust(o);
        },
        _daylightSavingAdjust: function(e) {
            return e ? (e.setHours(e.getHours() > 12 ? e.getHours() + 2 : 0), e) : null;
        },
        _setDate: function(e, t, i) {
            var s = !t, n = e.selectedMonth, a = e.selectedYear, o = this._restrictMinMax(e, this._determineDate(e, t, new Date()));
            e.selectedDay = e.currentDay = o.getDate(), e.drawMonth = e.selectedMonth = e.currentMonth = o.getMonth(), 
            e.drawYear = e.selectedYear = e.currentYear = o.getFullYear(), n === e.selectedMonth && a === e.selectedYear || i || this._notifyChange(e), 
            this._adjustInstDate(e), e.input && e.input.val(s ? "" : this._formatDate(e));
        },
        _getDate: function(e) {
            return !e.currentYear || e.input && "" === e.input.val() ? null : this._daylightSavingAdjust(new Date(e.currentYear, e.currentMonth, e.currentDay));
        },
        _attachHandlers: function(t) {
            var i = this._get(t, "stepMonths"), s = "#" + t.id.replace(/\\\\/g, "\\");
            t.dpDiv.find("[data-handler]").map(function() {
                var t = {
                    prev: function() {
                        e.datepicker._adjustDate(s, -i, "M");
                    },
                    next: function() {
                        e.datepicker._adjustDate(s, +i, "M");
                    },
                    hide: function() {
                        e.datepicker._hideDatepicker();
                    },
                    today: function() {
                        e.datepicker._gotoToday(s);
                    },
                    selectDay: function() {
                        return e.datepicker._selectDay(s, +this.getAttribute("data-month"), +this.getAttribute("data-year"), this), 
                        !1;
                    },
                    selectMonth: function() {
                        return e.datepicker._selectMonthYear(s, this, "M"), !1;
                    },
                    selectYear: function() {
                        return e.datepicker._selectMonthYear(s, this, "Y"), !1;
                    }
                };
                e(this).bind(this.getAttribute("data-event"), t[this.getAttribute("data-handler")]);
            });
        },
        _generateHTML: function(e) {
            var t, i, s, n, a, o, r, h, l, u, d, c, p, f, m, g, v, y, b, _, x, w, k, T, D, S, M, C, N, A, P, I, z, H, F, E, O, j, W, L = new Date(), R = this._daylightSavingAdjust(new Date(L.getFullYear(), L.getMonth(), L.getDate())), Y = this._get(e, "isRTL"), B = this._get(e, "showButtonPanel"), J = this._get(e, "hideIfNoPrevNext"), q = this._get(e, "navigationAsDateFormat"), K = this._getNumberOfMonths(e), V = this._get(e, "showCurrentAtPos"), U = this._get(e, "stepMonths"), Q = 1 !== K[0] || 1 !== K[1], G = this._daylightSavingAdjust(e.currentDay ? new Date(e.currentYear, e.currentMonth, e.currentDay) : new Date(9999, 9, 9)), X = this._getMinMaxDate(e, "min"), $ = this._getMinMaxDate(e, "max"), Z = e.drawMonth - V, et = e.drawYear;
            if (0 > Z && (Z += 12, et--), $) for (t = this._daylightSavingAdjust(new Date($.getFullYear(), $.getMonth() - K[0] * K[1] + 1, $.getDate())), 
            t = X && X > t ? X : t; this._daylightSavingAdjust(new Date(et, Z, 1)) > t; ) 0 > --Z && (Z = 11, 
            et--);
            for (e.drawMonth = Z, e.drawYear = et, i = this._get(e, "prevText"), i = q ? this.formatDate(i, this._daylightSavingAdjust(new Date(et, Z - U, 1)), this._getFormatConfig(e)) : i, 
            s = this._canAdjustMonth(e, -1, et, Z) ? "<a class='ui-datepicker-prev ui-corner-all' data-handler='prev' data-event='click' title='" + i + "'><span class='ui-icon ui-icon-circle-triangle-" + (Y ? "e" : "w") + "'>" + i + "</span></a>" : J ? "" : "<a class='ui-datepicker-prev ui-corner-all ui-state-disabled' title='" + i + "'><span class='ui-icon ui-icon-circle-triangle-" + (Y ? "e" : "w") + "'>" + i + "</span></a>", 
            n = this._get(e, "nextText"), n = q ? this.formatDate(n, this._daylightSavingAdjust(new Date(et, Z + U, 1)), this._getFormatConfig(e)) : n, 
            a = this._canAdjustMonth(e, 1, et, Z) ? "<a class='ui-datepicker-next ui-corner-all' data-handler='next' data-event='click' title='" + n + "'><span class='ui-icon ui-icon-circle-triangle-" + (Y ? "w" : "e") + "'>" + n + "</span></a>" : J ? "" : "<a class='ui-datepicker-next ui-corner-all ui-state-disabled' title='" + n + "'><span class='ui-icon ui-icon-circle-triangle-" + (Y ? "w" : "e") + "'>" + n + "</span></a>", 
            o = this._get(e, "currentText"), r = this._get(e, "gotoCurrent") && e.currentDay ? G : R, 
            o = q ? this.formatDate(o, r, this._getFormatConfig(e)) : o, h = e.inline ? "" : "<button type='button' class='ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all' data-handler='hide' data-event='click'>" + this._get(e, "closeText") + "</button>", 
            l = B ? "<div class='ui-datepicker-buttonpane ui-widget-content'>" + (Y ? h : "") + (this._isInRange(e, r) ? "<button type='button' class='ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all' data-handler='today' data-event='click'>" + o + "</button>" : "") + (Y ? "" : h) + "</div>" : "", 
            u = parseInt(this._get(e, "firstDay"), 10), u = isNaN(u) ? 0 : u, d = this._get(e, "showWeek"), 
            c = this._get(e, "dayNames"), p = this._get(e, "dayNamesMin"), f = this._get(e, "monthNames"), 
            m = this._get(e, "monthNamesShort"), g = this._get(e, "beforeShowDay"), v = this._get(e, "showOtherMonths"), 
            y = this._get(e, "selectOtherMonths"), b = this._getDefaultDate(e), _ = "", w = 0; K[0] > w; w++) {
                for (k = "", this.maxRows = 4, T = 0; K[1] > T; T++) {
                    if (D = this._daylightSavingAdjust(new Date(et, Z, e.selectedDay)), S = " ui-corner-all", 
                    M = "", Q) {
                        if (M += "<div class='ui-datepicker-group", K[1] > 1) switch (T) {
                          case 0:
                            M += " ui-datepicker-group-first", S = " ui-corner-" + (Y ? "right" : "left");
                            break;

                          case K[1] - 1:
                            M += " ui-datepicker-group-last", S = " ui-corner-" + (Y ? "left" : "right");
                            break;

                          default:
                            M += " ui-datepicker-group-middle", S = "";
                        }
                        M += "'>";
                    }
                    for (M += "<div class='ui-datepicker-header ui-widget-header ui-helper-clearfix" + S + "'>" + (/all|left/.test(S) && 0 === w ? Y ? a : s : "") + (/all|right/.test(S) && 0 === w ? Y ? s : a : "") + this._generateMonthYearHeader(e, Z, et, X, $, w > 0 || T > 0, f, m) + "</div><table class='ui-datepicker-calendar'><thead><tr>", 
                    C = d ? "<th class='ui-datepicker-week-col'>" + this._get(e, "weekHeader") + "</th>" : "", 
                    x = 0; 7 > x; x++) N = (x + u) % 7, C += "<th scope='col'" + ((x + u + 6) % 7 >= 5 ? " class='ui-datepicker-week-end'" : "") + "><span title='" + c[N] + "'>" + p[N] + "</span></th>";
                    for (M += C + "</tr></thead><tbody>", A = this._getDaysInMonth(et, Z), et === e.selectedYear && Z === e.selectedMonth && (e.selectedDay = Math.min(e.selectedDay, A)), 
                    P = (this._getFirstDayOfMonth(et, Z) - u + 7) % 7, I = Math.ceil((P + A) / 7), z = Q && this.maxRows > I ? this.maxRows : I, 
                    this.maxRows = z, H = this._daylightSavingAdjust(new Date(et, Z, 1 - P)), F = 0; z > F; F++) {
                        for (M += "<tr>", E = d ? "<td class='ui-datepicker-week-col'>" + this._get(e, "calculateWeek")(H) + "</td>" : "", 
                        x = 0; 7 > x; x++) O = g ? g.apply(e.input ? e.input[0] : null, [ H ]) : [ !0, "" ], 
                        j = H.getMonth() !== Z, W = j && !y || !O[0] || X && X > H || $ && H > $, E += "<td class='" + ((x + u + 6) % 7 >= 5 ? " ui-datepicker-week-end" : "") + (j ? " ui-datepicker-other-month" : "") + (H.getTime() === D.getTime() && Z === e.selectedMonth && e._keyEvent || b.getTime() === H.getTime() && b.getTime() === D.getTime() ? " " + this._dayOverClass : "") + (W ? " " + this._unselectableClass + " ui-state-disabled" : "") + (j && !v ? "" : " " + O[1] + (H.getTime() === G.getTime() ? " " + this._currentClass : "") + (H.getTime() === R.getTime() ? " ui-datepicker-today" : "")) + "'" + (j && !v || !O[2] ? "" : " title='" + O[2].replace(/'/g, "&#39;") + "'") + (W ? "" : " data-handler='selectDay' data-event='click' data-month='" + H.getMonth() + "' data-year='" + H.getFullYear() + "'") + ">" + (j && !v ? "&#xa0;" : W ? "<span class='ui-state-default'>" + H.getDate() + "</span>" : "<a class='ui-state-default" + (H.getTime() === R.getTime() ? " ui-state-highlight" : "") + (H.getTime() === G.getTime() ? " ui-state-active" : "") + (j ? " ui-priority-secondary" : "") + "' href='#'>" + H.getDate() + "</a>") + "</td>", 
                        H.setDate(H.getDate() + 1), H = this._daylightSavingAdjust(H);
                        M += E + "</tr>";
                    }
                    Z++, Z > 11 && (Z = 0, et++), M += "</tbody></table>" + (Q ? "</div>" + (K[0] > 0 && T === K[1] - 1 ? "<div class='ui-datepicker-row-break'></div>" : "") : ""), 
                    k += M;
                }
                _ += k;
            }
            return _ += l, e._keyEvent = !1, _;
        },
        _generateMonthYearHeader: function(e, t, i, s, n, a, o, r) {
            var h, l, u, d, c, p, f, m, g = this._get(e, "changeMonth"), v = this._get(e, "changeYear"), y = this._get(e, "showMonthAfterYear"), b = "<div class='ui-datepicker-title'>", _ = "";
            if (a || !g) _ += "<span class='ui-datepicker-month'>" + o[t] + "</span>"; else {
                for (h = s && s.getFullYear() === i, l = n && n.getFullYear() === i, _ += "<select class='ui-datepicker-month' data-handler='selectMonth' data-event='change'>", 
                u = 0; 12 > u; u++) (!h || u >= s.getMonth()) && (!l || n.getMonth() >= u) && (_ += "<option value='" + u + "'" + (u === t ? " selected='selected'" : "") + ">" + r[u] + "</option>");
                _ += "</select>";
            }
            if (y || (b += _ + (!a && g && v ? "" : "&#xa0;")), !e.yearshtml) if (e.yearshtml = "", 
            a || !v) b += "<span class='ui-datepicker-year'>" + i + "</span>"; else {
                for (d = this._get(e, "yearRange").split(":"), c = new Date().getFullYear(), p = function(e) {
                    var t = e.match(/c[+\-].*/) ? i + parseInt(e.substring(1), 10) : e.match(/[+\-].*/) ? c + parseInt(e, 10) : parseInt(e, 10);
                    return isNaN(t) ? c : t;
                }, f = p(d[0]), m = Math.max(f, p(d[1] || "")), f = s ? Math.max(f, s.getFullYear()) : f, 
                m = n ? Math.min(m, n.getFullYear()) : m, e.yearshtml += "<select class='ui-datepicker-year' data-handler='selectYear' data-event='change'>"; m >= f; f++) e.yearshtml += "<option value='" + f + "'" + (f === i ? " selected='selected'" : "") + ">" + f + "</option>";
                e.yearshtml += "</select>", b += e.yearshtml, e.yearshtml = null;
            }
            return b += this._get(e, "yearSuffix"), y && (b += (!a && g && v ? "" : "&#xa0;") + _), 
            b += "</div>";
        },
        _adjustInstDate: function(e, t, i) {
            var s = e.drawYear + ("Y" === i ? t : 0), n = e.drawMonth + ("M" === i ? t : 0), a = Math.min(e.selectedDay, this._getDaysInMonth(s, n)) + ("D" === i ? t : 0), o = this._restrictMinMax(e, this._daylightSavingAdjust(new Date(s, n, a)));
            e.selectedDay = o.getDate(), e.drawMonth = e.selectedMonth = o.getMonth(), e.drawYear = e.selectedYear = o.getFullYear(), 
            ("M" === i || "Y" === i) && this._notifyChange(e);
        },
        _restrictMinMax: function(e, t) {
            var i = this._getMinMaxDate(e, "min"), s = this._getMinMaxDate(e, "max"), n = i && i > t ? i : t;
            return s && n > s ? s : n;
        },
        _notifyChange: function(e) {
            var t = this._get(e, "onChangeMonthYear");
            t && t.apply(e.input ? e.input[0] : null, [ e.selectedYear, e.selectedMonth + 1, e ]);
        },
        _getNumberOfMonths: function(e) {
            var t = this._get(e, "numberOfMonths");
            return null == t ? [ 1, 1 ] : "number" == typeof t ? [ 1, t ] : t;
        },
        _getMinMaxDate: function(e, t) {
            return this._determineDate(e, this._get(e, t + "Date"), null);
        },
        _getDaysInMonth: function(e, t) {
            return 32 - this._daylightSavingAdjust(new Date(e, t, 32)).getDate();
        },
        _getFirstDayOfMonth: function(e, t) {
            return new Date(e, t, 1).getDay();
        },
        _canAdjustMonth: function(e, t, i, s) {
            var n = this._getNumberOfMonths(e), a = this._daylightSavingAdjust(new Date(i, s + (0 > t ? t : n[0] * n[1]), 1));
            return 0 > t && a.setDate(this._getDaysInMonth(a.getFullYear(), a.getMonth())), 
            this._isInRange(e, a);
        },
        _isInRange: function(e, t) {
            var i, s, n = this._getMinMaxDate(e, "min"), a = this._getMinMaxDate(e, "max"), o = null, r = null, h = this._get(e, "yearRange");
            return h && (i = h.split(":"), s = new Date().getFullYear(), o = parseInt(i[0], 10), 
            r = parseInt(i[1], 10), i[0].match(/[+\-].*/) && (o += s), i[1].match(/[+\-].*/) && (r += s)), 
            (!n || t.getTime() >= n.getTime()) && (!a || t.getTime() <= a.getTime()) && (!o || t.getFullYear() >= o) && (!r || r >= t.getFullYear());
        },
        _getFormatConfig: function(e) {
            var t = this._get(e, "shortYearCutoff");
            return t = "string" != typeof t ? t : new Date().getFullYear() % 100 + parseInt(t, 10), 
            {
                shortYearCutoff: t,
                dayNamesShort: this._get(e, "dayNamesShort"),
                dayNames: this._get(e, "dayNames"),
                monthNamesShort: this._get(e, "monthNamesShort"),
                monthNames: this._get(e, "monthNames")
            };
        },
        _formatDate: function(e, t, i, s) {
            t || (e.currentDay = e.selectedDay, e.currentMonth = e.selectedMonth, e.currentYear = e.selectedYear);
            var n = t ? "object" == typeof t ? t : this._daylightSavingAdjust(new Date(s, i, t)) : this._daylightSavingAdjust(new Date(e.currentYear, e.currentMonth, e.currentDay));
            return this.formatDate(this._get(e, "dateFormat"), n, this._getFormatConfig(e));
        }
    }), e.fn.datepicker = function(t) {
        if (!this.length) return this;
        e.datepicker.initialized || (e(document).mousedown(e.datepicker._checkExternalClick), 
        e.datepicker.initialized = !0), 0 === e("#" + e.datepicker._mainDivId).length && e("body").append(e.datepicker.dpDiv);
        var i = Array.prototype.slice.call(arguments, 1);
        return "string" != typeof t || "isDisabled" !== t && "getDate" !== t && "widget" !== t ? "option" === t && 2 === arguments.length && "string" == typeof arguments[1] ? e.datepicker["_" + t + "Datepicker"].apply(e.datepicker, [ this[0] ].concat(i)) : this.each(function() {
            "string" == typeof t ? e.datepicker["_" + t + "Datepicker"].apply(e.datepicker, [ this ].concat(i)) : e.datepicker._attachDatepicker(this, t);
        }) : e.datepicker["_" + t + "Datepicker"].apply(e.datepicker, [ this[0] ].concat(i));
    }, e.datepicker = new n(), e.datepicker.initialized = !1, e.datepicker.uuid = new Date().getTime(), 
    e.datepicker.version = "1.11.2", e.datepicker, e.widget("ui.dialog", {
        version: "1.11.2",
        options: {
            appendTo: "body",
            autoOpen: !0,
            buttons: [],
            closeOnEscape: !0,
            closeText: "Close",
            dialogClass: "",
            draggable: !0,
            hide: null,
            height: "auto",
            maxHeight: null,
            maxWidth: null,
            minHeight: 150,
            minWidth: 150,
            modal: !1,
            position: {
                my: "center",
                at: "center",
                of: window,
                collision: "fit",
                using: function(t) {
                    var i = e(this).css(t).offset().top;
                    0 > i && e(this).css("top", t.top - i);
                }
            },
            resizable: !0,
            show: null,
            title: null,
            width: 300,
            beforeClose: null,
            close: null,
            drag: null,
            dragStart: null,
            dragStop: null,
            focus: null,
            open: null,
            resize: null,
            resizeStart: null,
            resizeStop: null
        },
        sizeRelatedOptions: {
            buttons: !0,
            height: !0,
            maxHeight: !0,
            maxWidth: !0,
            minHeight: !0,
            minWidth: !0,
            width: !0
        },
        resizableRelatedOptions: {
            maxHeight: !0,
            maxWidth: !0,
            minHeight: !0,
            minWidth: !0
        },
        _create: function() {
            this.originalCss = {
                display: this.element[0].style.display,
                width: this.element[0].style.width,
                minHeight: this.element[0].style.minHeight,
                maxHeight: this.element[0].style.maxHeight,
                height: this.element[0].style.height
            }, this.originalPosition = {
                parent: this.element.parent(),
                index: this.element.parent().children().index(this.element)
            }, this.originalTitle = this.element.attr("title"), this.options.title = this.options.title || this.originalTitle, 
            this._createWrapper(), this.element.show().removeAttr("title").addClass("ui-dialog-content ui-widget-content").appendTo(this.uiDialog), 
            this._createTitlebar(), this._createButtonPane(), this.options.draggable && e.fn.draggable && this._makeDraggable(), 
            this.options.resizable && e.fn.resizable && this._makeResizable(), this._isOpen = !1, 
            this._trackFocus();
        },
        _init: function() {
            this.options.autoOpen && this.open();
        },
        _appendTo: function() {
            var t = this.options.appendTo;
            return t && (t.jquery || t.nodeType) ? e(t) : this.document.find(t || "body").eq(0);
        },
        _destroy: function() {
            var e, t = this.originalPosition;
            this._destroyOverlay(), this.element.removeUniqueId().removeClass("ui-dialog-content ui-widget-content").css(this.originalCss).detach(), 
            this.uiDialog.stop(!0, !0).remove(), this.originalTitle && this.element.attr("title", this.originalTitle), 
            e = t.parent.children().eq(t.index), e.length && e[0] !== this.element[0] ? e.before(this.element) : t.parent.append(this.element);
        },
        widget: function() {
            return this.uiDialog;
        },
        disable: e.noop,
        enable: e.noop,
        close: function(t) {
            var i, s = this;
            if (this._isOpen && !1 !== this._trigger("beforeClose", t)) {
                if (this._isOpen = !1, this._focusedElement = null, this._destroyOverlay(), this._untrackInstance(), 
                !this.opener.filter(":focusable").focus().length) try {
                    (i = this.document[0].activeElement) && "body" !== i.nodeName.toLowerCase() && e(i).blur();
                } catch (n) {}
                this._hide(this.uiDialog, this.options.hide, function() {
                    s._trigger("close", t);
                });
            }
        },
        isOpen: function() {
            return this._isOpen;
        },
        moveToTop: function() {
            this._moveToTop();
        },
        _moveToTop: function(t, i) {
            var s = !1, n = this.uiDialog.siblings(".ui-front:visible").map(function() {
                return +e(this).css("z-index");
            }).get(), a = Math.max.apply(null, n);
            return a >= +this.uiDialog.css("z-index") && (this.uiDialog.css("z-index", a + 1), 
            s = !0), s && !i && this._trigger("focus", t), s;
        },
        open: function() {
            var t = this;
            return this._isOpen ? void (this._moveToTop() && this._focusTabbable()) : (this._isOpen = !0, 
            this.opener = e(this.document[0].activeElement), this._size(), this._position(), 
            this._createOverlay(), this._moveToTop(null, !0), this.overlay && this.overlay.css("z-index", this.uiDialog.css("z-index") - 1), 
            this._show(this.uiDialog, this.options.show, function() {
                t._focusTabbable(), t._trigger("focus");
            }), this._makeFocusTarget(), void this._trigger("open"));
        },
        _focusTabbable: function() {
            var e = this._focusedElement;
            e || (e = this.element.find("[autofocus]")), e.length || (e = this.element.find(":tabbable")), 
            e.length || (e = this.uiDialogButtonPane.find(":tabbable")), e.length || (e = this.uiDialogTitlebarClose.filter(":tabbable")), 
            e.length || (e = this.uiDialog), e.eq(0).focus();
        },
        _keepFocus: function(t) {
            function i() {
                var t = this.document[0].activeElement;
                this.uiDialog[0] === t || e.contains(this.uiDialog[0], t) || this._focusTabbable();
            }
            t.preventDefault(), i.call(this), this._delay(i);
        },
        _createWrapper: function() {
            this.uiDialog = e("<div>").addClass("ui-dialog ui-widget ui-widget-content ui-corner-all ui-front " + this.options.dialogClass).hide().attr({
                tabIndex: -1,
                role: "dialog"
            }).appendTo(this._appendTo()), this._on(this.uiDialog, {
                keydown: function(t) {
                    if (this.options.closeOnEscape && !t.isDefaultPrevented() && t.keyCode && t.keyCode === e.ui.keyCode.ESCAPE) return t.preventDefault(), 
                    void this.close(t);
                    if (t.keyCode === e.ui.keyCode.TAB && !t.isDefaultPrevented()) {
                        var i = this.uiDialog.find(":tabbable"), s = i.filter(":first"), n = i.filter(":last");
                        t.target !== n[0] && t.target !== this.uiDialog[0] || t.shiftKey ? t.target !== s[0] && t.target !== this.uiDialog[0] || !t.shiftKey || (this._delay(function() {
                            n.focus();
                        }), t.preventDefault()) : (this._delay(function() {
                            s.focus();
                        }), t.preventDefault());
                    }
                },
                mousedown: function(e) {
                    this._moveToTop(e) && this._focusTabbable();
                }
            }), this.element.find("[aria-describedby]").length || this.uiDialog.attr({
                "aria-describedby": this.element.uniqueId().attr("id")
            });
        },
        _createTitlebar: function() {
            var t;
            this.uiDialogTitlebar = e("<div>").addClass("ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix").prependTo(this.uiDialog), 
            this._on(this.uiDialogTitlebar, {
                mousedown: function(t) {
                    e(t.target).closest(".ui-dialog-titlebar-close") || this.uiDialog.focus();
                }
            }), this.uiDialogTitlebarClose = e("<button type='button'></button>").button({
                label: this.options.closeText,
                icons: {
                    primary: "ui-icon-closethick"
                },
                text: !1
            }).addClass("ui-dialog-titlebar-close").appendTo(this.uiDialogTitlebar), this._on(this.uiDialogTitlebarClose, {
                click: function(e) {
                    e.preventDefault(), this.close(e);
                }
            }), t = e("<span>").uniqueId().addClass("ui-dialog-title").prependTo(this.uiDialogTitlebar), 
            this._title(t), this.uiDialog.attr({
                "aria-labelledby": t.attr("id")
            });
        },
        _title: function(e) {
            this.options.title || e.html("&#160;"), e.text(this.options.title);
        },
        _createButtonPane: function() {
            this.uiDialogButtonPane = e("<div>").addClass("ui-dialog-buttonpane ui-widget-content ui-helper-clearfix"), 
            this.uiButtonSet = e("<div>").addClass("ui-dialog-buttonset").appendTo(this.uiDialogButtonPane), 
            this._createButtons();
        },
        _createButtons: function() {
            var t = this, i = this.options.buttons;
            return this.uiDialogButtonPane.remove(), this.uiButtonSet.empty(), e.isEmptyObject(i) || e.isArray(i) && !i.length ? void this.uiDialog.removeClass("ui-dialog-buttons") : (e.each(i, function(i, s) {
                var n, a;
                s = e.isFunction(s) ? {
                    click: s,
                    text: i
                } : s, s = e.extend({
                    type: "button"
                }, s), n = s.click, s.click = function() {
                    n.apply(t.element[0], arguments);
                }, a = {
                    icons: s.icons,
                    text: s.showText
                }, delete s.icons, delete s.showText, e("<button></button>", s).button(a).appendTo(t.uiButtonSet);
            }), this.uiDialog.addClass("ui-dialog-buttons"), void this.uiDialogButtonPane.appendTo(this.uiDialog));
        },
        _makeDraggable: function() {
            function t(e) {
                return {
                    position: e.position,
                    offset: e.offset
                };
            }
            var i = this, s = this.options;
            this.uiDialog.draggable({
                cancel: ".ui-dialog-content, .ui-dialog-titlebar-close",
                handle: ".ui-dialog-titlebar",
                containment: "document",
                start: function(s, n) {
                    e(this).addClass("ui-dialog-dragging"), i._blockFrames(), i._trigger("dragStart", s, t(n));
                },
                drag: function(e, s) {
                    i._trigger("drag", e, t(s));
                },
                stop: function(n, a) {
                    var o = a.offset.left - i.document.scrollLeft(), r = a.offset.top - i.document.scrollTop();
                    s.position = {
                        my: "left top",
                        at: "left" + (o >= 0 ? "+" : "") + o + " top" + (r >= 0 ? "+" : "") + r,
                        of: i.window
                    }, e(this).removeClass("ui-dialog-dragging"), i._unblockFrames(), i._trigger("dragStop", n, t(a));
                }
            });
        },
        _makeResizable: function() {
            function t(e) {
                return {
                    originalPosition: e.originalPosition,
                    originalSize: e.originalSize,
                    position: e.position,
                    size: e.size
                };
            }
            var i = this, s = this.options, n = s.resizable, a = this.uiDialog.css("position"), o = "string" == typeof n ? n : "n,e,s,w,se,sw,ne,nw";
            this.uiDialog.resizable({
                cancel: ".ui-dialog-content",
                containment: "document",
                alsoResize: this.element,
                maxWidth: s.maxWidth,
                maxHeight: s.maxHeight,
                minWidth: s.minWidth,
                minHeight: this._minHeight(),
                handles: o,
                start: function(s, n) {
                    e(this).addClass("ui-dialog-resizing"), i._blockFrames(), i._trigger("resizeStart", s, t(n));
                },
                resize: function(e, s) {
                    i._trigger("resize", e, t(s));
                },
                stop: function(n, a) {
                    var o = i.uiDialog.offset(), r = o.left - i.document.scrollLeft(), h = o.top - i.document.scrollTop();
                    s.height = i.uiDialog.height(), s.width = i.uiDialog.width(), s.position = {
                        my: "left top",
                        at: "left" + (r >= 0 ? "+" : "") + r + " top" + (h >= 0 ? "+" : "") + h,
                        of: i.window
                    }, e(this).removeClass("ui-dialog-resizing"), i._unblockFrames(), i._trigger("resizeStop", n, t(a));
                }
            }).css("position", a);
        },
        _trackFocus: function() {
            this._on(this.widget(), {
                focusin: function(t) {
                    this._makeFocusTarget(), this._focusedElement = e(t.target);
                }
            });
        },
        _makeFocusTarget: function() {
            this._untrackInstance(), this._trackingInstances().unshift(this);
        },
        _untrackInstance: function() {
            var t = this._trackingInstances(), i = e.inArray(this, t);
            -1 !== i && t.splice(i, 1);
        },
        _trackingInstances: function() {
            var e = this.document.data("ui-dialog-instances");
            return e || (e = [], this.document.data("ui-dialog-instances", e)), e;
        },
        _minHeight: function() {
            var e = this.options;
            return "auto" === e.height ? e.minHeight : Math.min(e.minHeight, e.height);
        },
        _position: function() {
            var e = this.uiDialog.is(":visible");
            e || this.uiDialog.show(), this.uiDialog.position(this.options.position), e || this.uiDialog.hide();
        },
        _setOptions: function(t) {
            var i = this, s = !1, n = {};
            e.each(t, function(e, t) {
                i._setOption(e, t), e in i.sizeRelatedOptions && (s = !0), e in i.resizableRelatedOptions && (n[e] = t);
            }), s && (this._size(), this._position()), this.uiDialog.is(":data(ui-resizable)") && this.uiDialog.resizable("option", n);
        },
        _setOption: function(e, t) {
            var i, s, n = this.uiDialog;
            "dialogClass" === e && n.removeClass(this.options.dialogClass).addClass(t), "disabled" !== e && (this._super(e, t), 
            "appendTo" === e && this.uiDialog.appendTo(this._appendTo()), "buttons" === e && this._createButtons(), 
            "closeText" === e && this.uiDialogTitlebarClose.button({
                label: "" + t
            }), "draggable" === e && (i = n.is(":data(ui-draggable)"), i && !t && n.draggable("destroy"), 
            !i && t && this._makeDraggable()), "position" === e && this._position(), "resizable" === e && (s = n.is(":data(ui-resizable)"), 
            s && !t && n.resizable("destroy"), s && "string" == typeof t && n.resizable("option", "handles", t), 
            s || !1 === t || this._makeResizable()), "title" === e && this._title(this.uiDialogTitlebar.find(".ui-dialog-title")));
        },
        _size: function() {
            var e, t, i, s = this.options;
            this.element.show().css({
                width: "auto",
                minHeight: 0,
                maxHeight: "none",
                height: 0
            }), s.minWidth > s.width && (s.width = s.minWidth), e = this.uiDialog.css({
                height: "auto",
                width: s.width
            }).outerHeight(), t = Math.max(0, s.minHeight - e), i = "number" == typeof s.maxHeight ? Math.max(0, s.maxHeight - e) : "none", 
            "auto" === s.height ? this.element.css({
                minHeight: t,
                maxHeight: i,
                height: "auto"
            }) : this.element.height(Math.max(0, s.height - e)), this.uiDialog.is(":data(ui-resizable)") && this.uiDialog.resizable("option", "minHeight", this._minHeight());
        },
        _blockFrames: function() {
            this.iframeBlocks = this.document.find("iframe").map(function() {
                var t = e(this);
                return e("<div>").css({
                    position: "absolute",
                    width: t.outerWidth(),
                    height: t.outerHeight()
                }).appendTo(t.parent()).offset(t.offset())[0];
            });
        },
        _unblockFrames: function() {
            this.iframeBlocks && (this.iframeBlocks.remove(), delete this.iframeBlocks);
        },
        _allowInteraction: function(t) {
            return !!e(t.target).closest(".ui-dialog").length || !!e(t.target).closest(".ui-datepicker").length;
        },
        _createOverlay: function() {
            if (this.options.modal) {
                var t = !0;
                this._delay(function() {
                    t = !1;
                }), this.document.data("ui-dialog-overlays") || this._on(this.document, {
                    focusin: function(e) {
                        t || this._allowInteraction(e) || (e.preventDefault(), this._trackingInstances()[0]._focusTabbable());
                    }
                }), this.overlay = e("<div>").addClass("ui-widget-overlay ui-front").appendTo(this._appendTo()), 
                this._on(this.overlay, {
                    mousedown: "_keepFocus"
                }), this.document.data("ui-dialog-overlays", (this.document.data("ui-dialog-overlays") || 0) + 1);
            }
        },
        _destroyOverlay: function() {
            if (this.options.modal && this.overlay) {
                var e = this.document.data("ui-dialog-overlays") - 1;
                e ? this.document.data("ui-dialog-overlays", e) : this.document.unbind("focusin").removeData("ui-dialog-overlays"), 
                this.overlay.remove(), this.overlay = null;
            }
        }
    }), e.widget("ui.progressbar", {
        version: "1.11.2",
        options: {
            max: 100,
            value: 0,
            change: null,
            complete: null
        },
        min: 0,
        _create: function() {
            this.oldValue = this.options.value = this._constrainedValue(), this.element.addClass("ui-progressbar ui-widget ui-widget-content ui-corner-all").attr({
                role: "progressbar",
                "aria-valuemin": this.min
            }), this.valueDiv = e("<div class='ui-progressbar-value ui-widget-header ui-corner-left'></div>").appendTo(this.element), 
            this._refreshValue();
        },
        _destroy: function() {
            this.element.removeClass("ui-progressbar ui-widget ui-widget-content ui-corner-all").removeAttr("role").removeAttr("aria-valuemin").removeAttr("aria-valuemax").removeAttr("aria-valuenow"), 
            this.valueDiv.remove();
        },
        value: function(e) {
            return void 0 === e ? this.options.value : (this.options.value = this._constrainedValue(e), 
            void this._refreshValue());
        },
        _constrainedValue: function(e) {
            return void 0 === e && (e = this.options.value), this.indeterminate = !1 === e, 
            "number" != typeof e && (e = 0), !this.indeterminate && Math.min(this.options.max, Math.max(this.min, e));
        },
        _setOptions: function(e) {
            var t = e.value;
            delete e.value, this._super(e), this.options.value = this._constrainedValue(t), 
            this._refreshValue();
        },
        _setOption: function(e, t) {
            "max" === e && (t = Math.max(this.min, t)), "disabled" === e && this.element.toggleClass("ui-state-disabled", !!t).attr("aria-disabled", t), 
            this._super(e, t);
        },
        _percentage: function() {
            return this.indeterminate ? 100 : 100 * (this.options.value - this.min) / (this.options.max - this.min);
        },
        _refreshValue: function() {
            var t = this.options.value, i = this._percentage();
            this.valueDiv.toggle(this.indeterminate || t > this.min).toggleClass("ui-corner-right", t === this.options.max).width(i.toFixed(0) + "%"), 
            this.element.toggleClass("ui-progressbar-indeterminate", this.indeterminate), this.indeterminate ? (this.element.removeAttr("aria-valuenow"), 
            this.overlayDiv || (this.overlayDiv = e("<div class='ui-progressbar-overlay'></div>").appendTo(this.valueDiv))) : (this.element.attr({
                "aria-valuemax": this.options.max,
                "aria-valuenow": t
            }), this.overlayDiv && (this.overlayDiv.remove(), this.overlayDiv = null)), this.oldValue !== t && (this.oldValue = t, 
            this._trigger("change")), t === this.options.max && this._trigger("complete");
        }
    }), e.widget("ui.selectmenu", {
        version: "1.11.2",
        defaultElement: "<select>",
        options: {
            appendTo: null,
            disabled: null,
            icons: {
                button: "ui-icon-triangle-1-s"
            },
            position: {
                my: "left top",
                at: "left bottom",
                collision: "none"
            },
            width: null,
            change: null,
            close: null,
            focus: null,
            open: null,
            select: null
        },
        _create: function() {
            var e = this.element.uniqueId().attr("id");
            this.ids = {
                element: e,
                button: e + "-button",
                menu: e + "-menu"
            }, this._drawButton(), this._drawMenu(), this.options.disabled && this.disable();
        },
        _drawButton: function() {
            var t = this, i = this.element.attr("tabindex");
            this.label = e("label[for='" + this.ids.element + "']").attr("for", this.ids.button), 
            this._on(this.label, {
                click: function(e) {
                    this.button.focus(), e.preventDefault();
                }
            }), this.element.hide(), this.button = e("<span>", {
                class: "ui-selectmenu-button ui-widget ui-state-default ui-corner-all",
                tabindex: i || this.options.disabled ? -1 : 0,
                id: this.ids.button,
                role: "combobox",
                "aria-expanded": "false",
                "aria-autocomplete": "list",
                "aria-owns": this.ids.menu,
                "aria-haspopup": "true"
            }).insertAfter(this.element), e("<span>", {
                class: "ui-icon " + this.options.icons.button
            }).prependTo(this.button), this.buttonText = e("<span>", {
                class: "ui-selectmenu-text"
            }).appendTo(this.button), this._setText(this.buttonText, this.element.find("option:selected").text()), 
            this._resizeButton(), this._on(this.button, this._buttonEvents), this.button.one("focusin", function() {
                t.menuItems || t._refreshMenu();
            }), this._hoverable(this.button), this._focusable(this.button);
        },
        _drawMenu: function() {
            var t = this;
            this.menu = e("<ul>", {
                "aria-hidden": "true",
                "aria-labelledby": this.ids.button,
                id: this.ids.menu
            }), this.menuWrap = e("<div>", {
                class: "ui-selectmenu-menu ui-front"
            }).append(this.menu).appendTo(this._appendTo()), this.menuInstance = this.menu.menu({
                role: "listbox",
                select: function(e, i) {
                    e.preventDefault(), t._setSelection(), t._select(i.item.data("ui-selectmenu-item"), e);
                },
                focus: function(e, i) {
                    var s = i.item.data("ui-selectmenu-item");
                    null != t.focusIndex && s.index !== t.focusIndex && (t._trigger("focus", e, {
                        item: s
                    }), t.isOpen || t._select(s, e)), t.focusIndex = s.index, t.button.attr("aria-activedescendant", t.menuItems.eq(s.index).attr("id"));
                }
            }).menu("instance"), this.menu.addClass("ui-corner-bottom").removeClass("ui-corner-all"), 
            this.menuInstance._off(this.menu, "mouseleave"), this.menuInstance._closeOnDocumentClick = function() {
                return !1;
            }, this.menuInstance._isDivider = function() {
                return !1;
            };
        },
        refresh: function() {
            this._refreshMenu(), this._setText(this.buttonText, this._getSelectedItem().text()), 
            this.options.width || this._resizeButton();
        },
        _refreshMenu: function() {
            this.menu.empty();
            var e, t = this.element.find("option");
            t.length && (this._parseOptions(t), this._renderMenu(this.menu, this.items), this.menuInstance.refresh(), 
            this.menuItems = this.menu.find("li").not(".ui-selectmenu-optgroup"), e = this._getSelectedItem(), 
            this.menuInstance.focus(null, e), this._setAria(e.data("ui-selectmenu-item")), this._setOption("disabled", this.element.prop("disabled")));
        },
        open: function(e) {
            this.options.disabled || (this.menuItems ? (this.menu.find(".ui-state-focus").removeClass("ui-state-focus"), 
            this.menuInstance.focus(null, this._getSelectedItem())) : this._refreshMenu(), this.isOpen = !0, 
            this._toggleAttr(), this._resizeMenu(), this._position(), this._on(this.document, this._documentClick), 
            this._trigger("open", e));
        },
        _position: function() {
            this.menuWrap.position(e.extend({
                of: this.button
            }, this.options.position));
        },
        close: function(e) {
            this.isOpen && (this.isOpen = !1, this._toggleAttr(), this.range = null, this._off(this.document), 
            this._trigger("close", e));
        },
        widget: function() {
            return this.button;
        },
        menuWidget: function() {
            return this.menu;
        },
        _renderMenu: function(t, i) {
            var s = this, n = "";
            e.each(i, function(i, a) {
                a.optgroup !== n && (e("<li>", {
                    class: "ui-selectmenu-optgroup ui-menu-divider" + (a.element.parent("optgroup").prop("disabled") ? " ui-state-disabled" : ""),
                    text: a.optgroup
                }).appendTo(t), n = a.optgroup), s._renderItemData(t, a);
            });
        },
        _renderItemData: function(e, t) {
            return this._renderItem(e, t).data("ui-selectmenu-item", t);
        },
        _renderItem: function(t, i) {
            var s = e("<li>");
            return i.disabled && s.addClass("ui-state-disabled"), this._setText(s, i.label), 
            s.appendTo(t);
        },
        _setText: function(e, t) {
            t ? e.text(t) : e.html("&#160;");
        },
        _move: function(e, t) {
            var i, s, n = ".ui-menu-item";
            this.isOpen ? i = this.menuItems.eq(this.focusIndex) : (i = this.menuItems.eq(this.element[0].selectedIndex), 
            n += ":not(.ui-state-disabled)"), s = "first" === e || "last" === e ? i["first" === e ? "prevAll" : "nextAll"](n).eq(-1) : i[e + "All"](n).eq(0), 
            s.length && this.menuInstance.focus(t, s);
        },
        _getSelectedItem: function() {
            return this.menuItems.eq(this.element[0].selectedIndex);
        },
        _toggle: function(e) {
            this[this.isOpen ? "close" : "open"](e);
        },
        _setSelection: function() {
            var e;
            this.range && (window.getSelection ? (e = window.getSelection(), e.removeAllRanges(), 
            e.addRange(this.range)) : this.range.select(), this.button.focus());
        },
        _documentClick: {
            mousedown: function(t) {
                this.isOpen && (e(t.target).closest(".ui-selectmenu-menu, #" + this.ids.button).length || this.close(t));
            }
        },
        _buttonEvents: {
            mousedown: function() {
                var e;
                window.getSelection ? (e = window.getSelection(), e.rangeCount && (this.range = e.getRangeAt(0))) : this.range = document.selection.createRange();
            },
            click: function(e) {
                this._setSelection(), this._toggle(e);
            },
            keydown: function(t) {
                var i = !0;
                switch (t.keyCode) {
                  case e.ui.keyCode.TAB:
                  case e.ui.keyCode.ESCAPE:
                    this.close(t), i = !1;
                    break;

                  case e.ui.keyCode.ENTER:
                    this.isOpen && this._selectFocusedItem(t);
                    break;

                  case e.ui.keyCode.UP:
                    t.altKey ? this._toggle(t) : this._move("prev", t);
                    break;

                  case e.ui.keyCode.DOWN:
                    t.altKey ? this._toggle(t) : this._move("next", t);
                    break;

                  case e.ui.keyCode.SPACE:
                    this.isOpen ? this._selectFocusedItem(t) : this._toggle(t);
                    break;

                  case e.ui.keyCode.LEFT:
                    this._move("prev", t);
                    break;

                  case e.ui.keyCode.RIGHT:
                    this._move("next", t);
                    break;

                  case e.ui.keyCode.HOME:
                  case e.ui.keyCode.PAGE_UP:
                    this._move("first", t);
                    break;

                  case e.ui.keyCode.END:
                  case e.ui.keyCode.PAGE_DOWN:
                    this._move("last", t);
                    break;

                  default:
                    this.menu.trigger(t), i = !1;
                }
                i && t.preventDefault();
            }
        },
        _selectFocusedItem: function(e) {
            var t = this.menuItems.eq(this.focusIndex);
            t.hasClass("ui-state-disabled") || this._select(t.data("ui-selectmenu-item"), e);
        },
        _select: function(e, t) {
            var i = this.element[0].selectedIndex;
            this.element[0].selectedIndex = e.index, this._setText(this.buttonText, e.label), 
            this._setAria(e), this._trigger("select", t, {
                item: e
            }), e.index !== i && this._trigger("change", t, {
                item: e
            }), this.close(t);
        },
        _setAria: function(e) {
            var t = this.menuItems.eq(e.index).attr("id");
            this.button.attr({
                "aria-labelledby": t,
                "aria-activedescendant": t
            }), this.menu.attr("aria-activedescendant", t);
        },
        _setOption: function(e, t) {
            "icons" === e && this.button.find("span.ui-icon").removeClass(this.options.icons.button).addClass(t.button), 
            this._super(e, t), "appendTo" === e && this.menuWrap.appendTo(this._appendTo()), 
            "disabled" === e && (this.menuInstance.option("disabled", t), this.button.toggleClass("ui-state-disabled", t).attr("aria-disabled", t), 
            this.element.prop("disabled", t), t ? (this.button.attr("tabindex", -1), this.close()) : this.button.attr("tabindex", 0)), 
            "width" === e && this._resizeButton();
        },
        _appendTo: function() {
            var t = this.options.appendTo;
            return t && (t = t.jquery || t.nodeType ? e(t) : this.document.find(t).eq(0)), t && t[0] || (t = this.element.closest(".ui-front")), 
            t.length || (t = this.document[0].body), t;
        },
        _toggleAttr: function() {
            this.button.toggleClass("ui-corner-top", this.isOpen).toggleClass("ui-corner-all", !this.isOpen).attr("aria-expanded", this.isOpen), 
            this.menuWrap.toggleClass("ui-selectmenu-open", this.isOpen), this.menu.attr("aria-hidden", !this.isOpen);
        },
        _resizeButton: function() {
            var e = this.options.width;
            e || (e = this.element.show().outerWidth(), this.element.hide()), this.button.outerWidth(e);
        },
        _resizeMenu: function() {
            this.menu.outerWidth(Math.max(this.button.outerWidth(), this.menu.width("").outerWidth() + 1));
        },
        _getCreateOptions: function() {
            return {
                disabled: this.element.prop("disabled")
            };
        },
        _parseOptions: function(t) {
            var i = [];
            t.each(function(t, s) {
                var n = e(s), a = n.parent("optgroup");
                i.push({
                    element: n,
                    index: t,
                    value: n.attr("value"),
                    label: n.text(),
                    optgroup: a.attr("label") || "",
                    disabled: a.prop("disabled") || n.prop("disabled")
                });
            }), this.items = i;
        },
        _destroy: function() {
            this.menuWrap.remove(), this.button.remove(), this.element.show(), this.element.removeUniqueId(), 
            this.label.attr("for", this.ids.element);
        }
    }), e.widget("ui.slider", e.ui.mouse, {
        version: "1.11.2",
        widgetEventPrefix: "slide",
        options: {
            animate: !1,
            distance: 0,
            max: 100,
            min: 0,
            orientation: "horizontal",
            range: !1,
            step: 1,
            value: 0,
            values: null,
            change: null,
            slide: null,
            start: null,
            stop: null
        },
        numPages: 5,
        _create: function() {
            this._keySliding = !1, this._mouseSliding = !1, this._animateOff = !0, this._handleIndex = null, 
            this._detectOrientation(), this._mouseInit(), this._calculateNewMax(), this.element.addClass("ui-slider ui-slider-" + this.orientation + " ui-widget ui-widget-content ui-corner-all"), 
            this._refresh(), this._setOption("disabled", this.options.disabled), this._animateOff = !1;
        },
        _refresh: function() {
            this._createRange(), this._createHandles(), this._setupEvents(), this._refreshValue();
        },
        _createHandles: function() {
            var t, i, s = this.options, n = this.element.find(".ui-slider-handle").addClass("ui-state-default ui-corner-all"), o = [];
            for (i = s.values && s.values.length || 1, n.length > i && (n.slice(i).remove(), 
            n = n.slice(0, i)), t = n.length; i > t; t++) o.push("<span class='ui-slider-handle ui-state-default ui-corner-all' tabindex='0'></span>");
            this.handles = n.add(e(o.join("")).appendTo(this.element)), this.handle = this.handles.eq(0), 
            this.handles.each(function(t) {
                e(this).data("ui-slider-handle-index", t);
            });
        },
        _createRange: function() {
            var t = this.options, i = "";
            t.range ? (!0 === t.range && (t.values ? t.values.length && 2 !== t.values.length ? t.values = [ t.values[0], t.values[0] ] : e.isArray(t.values) && (t.values = t.values.slice(0)) : t.values = [ this._valueMin(), this._valueMin() ]), 
            this.range && this.range.length ? this.range.removeClass("ui-slider-range-min ui-slider-range-max").css({
                left: "",
                bottom: ""
            }) : (this.range = e("<div></div>").appendTo(this.element), i = "ui-slider-range ui-widget-header ui-corner-all"), 
            this.range.addClass(i + ("min" === t.range || "max" === t.range ? " ui-slider-range-" + t.range : ""))) : (this.range && this.range.remove(), 
            this.range = null);
        },
        _setupEvents: function() {
            this._off(this.handles), this._on(this.handles, this._handleEvents), this._hoverable(this.handles), 
            this._focusable(this.handles);
        },
        _destroy: function() {
            this.handles.remove(), this.range && this.range.remove(), this.element.removeClass("ui-slider ui-slider-horizontal ui-slider-vertical ui-widget ui-widget-content ui-corner-all"), 
            this._mouseDestroy();
        },
        _mouseCapture: function(t) {
            var i, s, n, a, o, h, l, u = this, d = this.options;
            return !d.disabled && (this.elementSize = {
                width: this.element.outerWidth(),
                height: this.element.outerHeight()
            }, this.elementOffset = this.element.offset(), i = {
                x: t.pageX,
                y: t.pageY
            }, s = this._normValueFromMouse(i), n = this._valueMax() - this._valueMin() + 1, 
            this.handles.each(function(t) {
                var i = Math.abs(s - u.values(t));
                (n > i || n === i && (t === u._lastChangedValue || u.values(t) === d.min)) && (n = i, 
                a = e(this), o = t);
            }), !1 !== this._start(t, o) && (this._mouseSliding = !0, this._handleIndex = o, 
            a.addClass("ui-state-active").focus(), h = a.offset(), l = !e(t.target).parents().addBack().is(".ui-slider-handle"), 
            this._clickOffset = l ? {
                left: 0,
                top: 0
            } : {
                left: t.pageX - h.left - a.width() / 2,
                top: t.pageY - h.top - a.height() / 2 - (parseInt(a.css("borderTopWidth"), 10) || 0) - (parseInt(a.css("borderBottomWidth"), 10) || 0) + (parseInt(a.css("marginTop"), 10) || 0)
            }, this.handles.hasClass("ui-state-hover") || this._slide(t, o, s), this._animateOff = !0, 
            !0));
        },
        _mouseStart: function() {
            return !0;
        },
        _mouseDrag: function(e) {
            var t = {
                x: e.pageX,
                y: e.pageY
            }, i = this._normValueFromMouse(t);
            return this._slide(e, this._handleIndex, i), !1;
        },
        _mouseStop: function(e) {
            return this.handles.removeClass("ui-state-active"), this._mouseSliding = !1, this._stop(e, this._handleIndex), 
            this._change(e, this._handleIndex), this._handleIndex = null, this._clickOffset = null, 
            this._animateOff = !1, !1;
        },
        _detectOrientation: function() {
            this.orientation = "vertical" === this.options.orientation ? "vertical" : "horizontal";
        },
        _normValueFromMouse: function(e) {
            var t, i, s, n, a;
            return "horizontal" === this.orientation ? (t = this.elementSize.width, i = e.x - this.elementOffset.left - (this._clickOffset ? this._clickOffset.left : 0)) : (t = this.elementSize.height, 
            i = e.y - this.elementOffset.top - (this._clickOffset ? this._clickOffset.top : 0)), 
            s = i / t, s > 1 && (s = 1), 0 > s && (s = 0), "vertical" === this.orientation && (s = 1 - s), 
            n = this._valueMax() - this._valueMin(), a = this._valueMin() + s * n, this._trimAlignValue(a);
        },
        _start: function(e, t) {
            var i = {
                handle: this.handles[t],
                value: this.value()
            };
            return this.options.values && this.options.values.length && (i.value = this.values(t), 
            i.values = this.values()), this._trigger("start", e, i);
        },
        _slide: function(e, t, i) {
            var s, n, a;
            this.options.values && this.options.values.length ? (s = this.values(t ? 0 : 1), 
            2 === this.options.values.length && !0 === this.options.range && (0 === t && i > s || 1 === t && s > i) && (i = s), 
            i !== this.values(t) && (n = this.values(), n[t] = i, a = this._trigger("slide", e, {
                handle: this.handles[t],
                value: i,
                values: n
            }), s = this.values(t ? 0 : 1), !1 !== a && this.values(t, i))) : i !== this.value() && !1 !== (a = this._trigger("slide", e, {
                handle: this.handles[t],
                value: i
            })) && this.value(i);
        },
        _stop: function(e, t) {
            var i = {
                handle: this.handles[t],
                value: this.value()
            };
            this.options.values && this.options.values.length && (i.value = this.values(t), 
            i.values = this.values()), this._trigger("stop", e, i);
        },
        _change: function(e, t) {
            if (!this._keySliding && !this._mouseSliding) {
                var i = {
                    handle: this.handles[t],
                    value: this.value()
                };
                this.options.values && this.options.values.length && (i.value = this.values(t), 
                i.values = this.values()), this._lastChangedValue = t, this._trigger("change", e, i);
            }
        },
        value: function(e) {
            return arguments.length ? (this.options.value = this._trimAlignValue(e), this._refreshValue(), 
            void this._change(null, 0)) : this._value();
        },
        values: function(t, i) {
            var s, n, a;
            if (arguments.length > 1) return this.options.values[t] = this._trimAlignValue(i), 
            this._refreshValue(), void this._change(null, t);
            if (!arguments.length) return this._values();
            if (!e.isArray(arguments[0])) return this.options.values && this.options.values.length ? this._values(t) : this.value();
            for (s = this.options.values, n = arguments[0], a = 0; s.length > a; a += 1) s[a] = this._trimAlignValue(n[a]), 
            this._change(null, a);
            this._refreshValue();
        },
        _setOption: function(t, i) {
            var s, n = 0;
            switch ("range" === t && !0 === this.options.range && ("min" === i ? (this.options.value = this._values(0), 
            this.options.values = null) : "max" === i && (this.options.value = this._values(this.options.values.length - 1), 
            this.options.values = null)), e.isArray(this.options.values) && (n = this.options.values.length), 
            "disabled" === t && this.element.toggleClass("ui-state-disabled", !!i), this._super(t, i), 
            t) {
              case "orientation":
                this._detectOrientation(), this.element.removeClass("ui-slider-horizontal ui-slider-vertical").addClass("ui-slider-" + this.orientation), 
                this._refreshValue(), this.handles.css("horizontal" === i ? "bottom" : "left", "");
                break;

              case "value":
                this._animateOff = !0, this._refreshValue(), this._change(null, 0), this._animateOff = !1;
                break;

              case "values":
                for (this._animateOff = !0, this._refreshValue(), s = 0; n > s; s += 1) this._change(null, s);
                this._animateOff = !1;
                break;

              case "step":
              case "min":
              case "max":
                this._animateOff = !0, this._calculateNewMax(), this._refreshValue(), this._animateOff = !1;
                break;

              case "range":
                this._animateOff = !0, this._refresh(), this._animateOff = !1;
            }
        },
        _value: function() {
            var e = this.options.value;
            return e = this._trimAlignValue(e);
        },
        _values: function(e) {
            var t, i, s;
            if (arguments.length) return t = this.options.values[e], t = this._trimAlignValue(t);
            if (this.options.values && this.options.values.length) {
                for (i = this.options.values.slice(), s = 0; i.length > s; s += 1) i[s] = this._trimAlignValue(i[s]);
                return i;
            }
            return [];
        },
        _trimAlignValue: function(e) {
            if (this._valueMin() >= e) return this._valueMin();
            if (e >= this._valueMax()) return this._valueMax();
            var t = this.options.step > 0 ? this.options.step : 1, i = (e - this._valueMin()) % t, s = e - i;
            return 2 * Math.abs(i) >= t && (s += i > 0 ? t : -t), parseFloat(s.toFixed(5));
        },
        _calculateNewMax: function() {
            var e = (this.options.max - this._valueMin()) % this.options.step;
            this.max = this.options.max - e;
        },
        _valueMin: function() {
            return this.options.min;
        },
        _valueMax: function() {
            return this.max;
        },
        _refreshValue: function() {
            var t, i, s, n, a, o = this.options.range, r = this.options, h = this, l = !this._animateOff && r.animate, u = {};
            this.options.values && this.options.values.length ? this.handles.each(function(s) {
                i = (h.values(s) - h._valueMin()) / (h._valueMax() - h._valueMin()) * 100, u["horizontal" === h.orientation ? "left" : "bottom"] = i + "%", 
                e(this).stop(1, 1)[l ? "animate" : "css"](u, r.animate), !0 === h.options.range && ("horizontal" === h.orientation ? (0 === s && h.range.stop(1, 1)[l ? "animate" : "css"]({
                    left: i + "%"
                }, r.animate), 1 === s && h.range[l ? "animate" : "css"]({
                    width: i - t + "%"
                }, {
                    queue: !1,
                    duration: r.animate
                })) : (0 === s && h.range.stop(1, 1)[l ? "animate" : "css"]({
                    bottom: i + "%"
                }, r.animate), 1 === s && h.range[l ? "animate" : "css"]({
                    height: i - t + "%"
                }, {
                    queue: !1,
                    duration: r.animate
                }))), t = i;
            }) : (s = this.value(), n = this._valueMin(), a = this._valueMax(), i = a !== n ? (s - n) / (a - n) * 100 : 0, 
            u["horizontal" === this.orientation ? "left" : "bottom"] = i + "%", this.handle.stop(1, 1)[l ? "animate" : "css"](u, r.animate), 
            "min" === o && "horizontal" === this.orientation && this.range.stop(1, 1)[l ? "animate" : "css"]({
                width: i + "%"
            }, r.animate), "max" === o && "horizontal" === this.orientation && this.range[l ? "animate" : "css"]({
                width: 100 - i + "%"
            }, {
                queue: !1,
                duration: r.animate
            }), "min" === o && "vertical" === this.orientation && this.range.stop(1, 1)[l ? "animate" : "css"]({
                height: i + "%"
            }, r.animate), "max" === o && "vertical" === this.orientation && this.range[l ? "animate" : "css"]({
                height: 100 - i + "%"
            }, {
                queue: !1,
                duration: r.animate
            }));
        },
        _handleEvents: {
            keydown: function(t) {
                var s, n, a, o = e(t.target).data("ui-slider-handle-index");
                switch (t.keyCode) {
                  case e.ui.keyCode.HOME:
                  case e.ui.keyCode.END:
                  case e.ui.keyCode.PAGE_UP:
                  case e.ui.keyCode.PAGE_DOWN:
                  case e.ui.keyCode.UP:
                  case e.ui.keyCode.RIGHT:
                  case e.ui.keyCode.DOWN:
                  case e.ui.keyCode.LEFT:
                    if (t.preventDefault(), !this._keySliding && (this._keySliding = !0, e(t.target).addClass("ui-state-active"), 
                    !1 === this._start(t, o))) return;
                }
                switch (a = this.options.step, s = n = this.options.values && this.options.values.length ? this.values(o) : this.value(), 
                t.keyCode) {
                  case e.ui.keyCode.HOME:
                    n = this._valueMin();
                    break;

                  case e.ui.keyCode.END:
                    n = this._valueMax();
                    break;

                  case e.ui.keyCode.PAGE_UP:
                    n = this._trimAlignValue(s + (this._valueMax() - this._valueMin()) / this.numPages);
                    break;

                  case e.ui.keyCode.PAGE_DOWN:
                    n = this._trimAlignValue(s - (this._valueMax() - this._valueMin()) / this.numPages);
                    break;

                  case e.ui.keyCode.UP:
                  case e.ui.keyCode.RIGHT:
                    if (s === this._valueMax()) return;
                    n = this._trimAlignValue(s + a);
                    break;

                  case e.ui.keyCode.DOWN:
                  case e.ui.keyCode.LEFT:
                    if (s === this._valueMin()) return;
                    n = this._trimAlignValue(s - a);
                }
                this._slide(t, o, n);
            },
            keyup: function(t) {
                var i = e(t.target).data("ui-slider-handle-index");
                this._keySliding && (this._keySliding = !1, this._stop(t, i), this._change(t, i), 
                e(t.target).removeClass("ui-state-active"));
            }
        }
    }), e.widget("ui.spinner", {
        version: "1.11.2",
        defaultElement: "<input>",
        widgetEventPrefix: "spin",
        options: {
            culture: null,
            icons: {
                down: "ui-icon-triangle-1-s",
                up: "ui-icon-triangle-1-n"
            },
            incremental: !0,
            max: null,
            min: null,
            numberFormat: null,
            page: 10,
            step: 1,
            change: null,
            spin: null,
            start: null,
            stop: null
        },
        _create: function() {
            this._setOption("max", this.options.max), this._setOption("min", this.options.min), 
            this._setOption("step", this.options.step), "" !== this.value() && this._value(this.element.val(), !0), 
            this._draw(), this._on(this._events), this._refresh(), this._on(this.window, {
                beforeunload: function() {
                    this.element.removeAttr("autocomplete");
                }
            });
        },
        _getCreateOptions: function() {
            var t = {}, i = this.element;
            return e.each([ "min", "max", "step" ], function(e, s) {
                var n = i.attr(s);
                void 0 !== n && n.length && (t[s] = n);
            }), t;
        },
        _events: {
            keydown: function(e) {
                this._start(e) && this._keydown(e) && e.preventDefault();
            },
            keyup: "_stop",
            focus: function() {
                this.previous = this.element.val();
            },
            blur: function(e) {
                return this.cancelBlur ? void delete this.cancelBlur : (this._stop(), this._refresh(), 
                void (this.previous !== this.element.val() && this._trigger("change", e)));
            },
            mousewheel: function(e, t) {
                if (t) {
                    if (!this.spinning && !this._start(e)) return !1;
                    this._spin((t > 0 ? 1 : -1) * this.options.step, e), clearTimeout(this.mousewheelTimer), 
                    this.mousewheelTimer = this._delay(function() {
                        this.spinning && this._stop(e);
                    }, 100), e.preventDefault();
                }
            },
            "mousedown .ui-spinner-button": function(t) {
                function i() {
                    this.element[0] === this.document[0].activeElement || (this.element.focus(), this.previous = s, 
                    this._delay(function() {
                        this.previous = s;
                    }));
                }
                var s;
                s = this.element[0] === this.document[0].activeElement ? this.previous : this.element.val(), 
                t.preventDefault(), i.call(this), this.cancelBlur = !0, this._delay(function() {
                    delete this.cancelBlur, i.call(this);
                }), !1 !== this._start(t) && this._repeat(null, e(t.currentTarget).hasClass("ui-spinner-up") ? 1 : -1, t);
            },
            "mouseup .ui-spinner-button": "_stop",
            "mouseenter .ui-spinner-button": function(t) {
                return e(t.currentTarget).hasClass("ui-state-active") ? !1 !== this._start(t) && void this._repeat(null, e(t.currentTarget).hasClass("ui-spinner-up") ? 1 : -1, t) : void 0;
            },
            "mouseleave .ui-spinner-button": "_stop"
        },
        _draw: function() {
            var e = this.uiSpinner = this.element.addClass("ui-spinner-input").attr("autocomplete", "off").wrap(this._uiSpinnerHtml()).parent().append(this._buttonHtml());
            this.element.attr("role", "spinbutton"), this.buttons = e.find(".ui-spinner-button").attr("tabIndex", -1).button().removeClass("ui-corner-all"), 
            this.buttons.height() > Math.ceil(.5 * e.height()) && e.height() > 0 && e.height(e.height()), 
            this.options.disabled && this.disable();
        },
        _keydown: function(t) {
            var i = this.options, s = e.ui.keyCode;
            switch (t.keyCode) {
              case s.UP:
                return this._repeat(null, 1, t), !0;

              case s.DOWN:
                return this._repeat(null, -1, t), !0;

              case s.PAGE_UP:
                return this._repeat(null, i.page, t), !0;

              case s.PAGE_DOWN:
                return this._repeat(null, -i.page, t), !0;
            }
            return !1;
        },
        _uiSpinnerHtml: function() {
            return "<span class='ui-spinner ui-widget ui-widget-content ui-corner-all'></span>";
        },
        _buttonHtml: function() {
            return "<a class='ui-spinner-button ui-spinner-up ui-corner-tr'><span class='ui-icon " + this.options.icons.up + "'>&#9650;</span></a><a class='ui-spinner-button ui-spinner-down ui-corner-br'><span class='ui-icon " + this.options.icons.down + "'>&#9660;</span></a>";
        },
        _start: function(e) {
            return !(!this.spinning && !1 === this._trigger("start", e)) && (this.counter || (this.counter = 1), 
            this.spinning = !0, !0);
        },
        _repeat: function(e, t, i) {
            e = e || 500, clearTimeout(this.timer), this.timer = this._delay(function() {
                this._repeat(40, t, i);
            }, e), this._spin(t * this.options.step, i);
        },
        _spin: function(e, t) {
            var i = this.value() || 0;
            this.counter || (this.counter = 1), i = this._adjustValue(i + e * this._increment(this.counter)), 
            this.spinning && !1 === this._trigger("spin", t, {
                value: i
            }) || (this._value(i), this.counter++);
        },
        _increment: function(t) {
            var i = this.options.incremental;
            return i ? e.isFunction(i) ? i(t) : Math.floor(t * t * t / 5e4 - t * t / 500 + 17 * t / 200 + 1) : 1;
        },
        _precision: function() {
            var e = this._precisionOf(this.options.step);
            return null !== this.options.min && (e = Math.max(e, this._precisionOf(this.options.min))), 
            e;
        },
        _precisionOf: function(e) {
            var t = "" + e, i = t.indexOf(".");
            return -1 === i ? 0 : t.length - i - 1;
        },
        _adjustValue: function(e) {
            var t, i, s = this.options;
            return t = null !== s.min ? s.min : 0, i = e - t, i = Math.round(i / s.step) * s.step, 
            e = t + i, e = parseFloat(e.toFixed(this._precision())), null !== s.max && e > s.max ? s.max : null !== s.min && s.min > e ? s.min : e;
        },
        _stop: function(e) {
            this.spinning && (clearTimeout(this.timer), clearTimeout(this.mousewheelTimer), 
            this.counter = 0, this.spinning = !1, this._trigger("stop", e));
        },
        _setOption: function(e, t) {
            if ("culture" === e || "numberFormat" === e) {
                var i = this._parse(this.element.val());
                return this.options[e] = t, void this.element.val(this._format(i));
            }
            ("max" === e || "min" === e || "step" === e) && "string" == typeof t && (t = this._parse(t)), 
            "icons" === e && (this.buttons.first().find(".ui-icon").removeClass(this.options.icons.up).addClass(t.up), 
            this.buttons.last().find(".ui-icon").removeClass(this.options.icons.down).addClass(t.down)), 
            this._super(e, t), "disabled" === e && (this.widget().toggleClass("ui-state-disabled", !!t), 
            this.element.prop("disabled", !!t), this.buttons.button(t ? "disable" : "enable"));
        },
        _setOptions: h(function(e) {
            this._super(e);
        }),
        _parse: function(e) {
            return "string" == typeof e && "" !== e && (e = window.Globalize && this.options.numberFormat ? Globalize.parseFloat(e, 10, this.options.culture) : +e), 
            "" === e || isNaN(e) ? null : e;
        },
        _format: function(e) {
            return "" === e ? "" : window.Globalize && this.options.numberFormat ? Globalize.format(e, this.options.numberFormat, this.options.culture) : e;
        },
        _refresh: function() {
            this.element.attr({
                "aria-valuemin": this.options.min,
                "aria-valuemax": this.options.max,
                "aria-valuenow": this._parse(this.element.val())
            });
        },
        isValid: function() {
            var e = this.value();
            return null !== e && e === this._adjustValue(e);
        },
        _value: function(e, t) {
            var i;
            "" !== e && null !== (i = this._parse(e)) && (t || (i = this._adjustValue(i)), e = this._format(i)), 
            this.element.val(e), this._refresh();
        },
        _destroy: function() {
            this.element.removeClass("ui-spinner-input").prop("disabled", !1).removeAttr("autocomplete").removeAttr("role").removeAttr("aria-valuemin").removeAttr("aria-valuemax").removeAttr("aria-valuenow"), 
            this.uiSpinner.replaceWith(this.element);
        },
        stepUp: h(function(e) {
            this._stepUp(e);
        }),
        _stepUp: function(e) {
            this._start() && (this._spin((e || 1) * this.options.step), this._stop());
        },
        stepDown: h(function(e) {
            this._stepDown(e);
        }),
        _stepDown: function(e) {
            this._start() && (this._spin((e || 1) * -this.options.step), this._stop());
        },
        pageUp: h(function(e) {
            this._stepUp((e || 1) * this.options.page);
        }),
        pageDown: h(function(e) {
            this._stepDown((e || 1) * this.options.page);
        }),
        value: function(e) {
            return arguments.length ? void h(this._value).call(this, e) : this._parse(this.element.val());
        },
        widget: function() {
            return this.uiSpinner;
        }
    }), e.widget("ui.tabs", {
        version: "1.11.2",
        delay: 300,
        options: {
            active: null,
            collapsible: !1,
            event: "click",
            heightStyle: "content",
            hide: null,
            show: null,
            activate: null,
            beforeActivate: null,
            beforeLoad: null,
            load: null
        },
        _isLocal: function() {
            var e = /#.*$/;
            return function(t) {
                var i, s;
                t = t.cloneNode(!1), i = t.href.replace(e, ""), s = location.href.replace(e, "");
                try {
                    i = decodeURIComponent(i);
                } catch (n) {}
                try {
                    s = decodeURIComponent(s);
                } catch (n) {}
                return t.hash.length > 1 && i === s;
            };
        }(),
        _create: function() {
            var t = this, i = this.options;
            this.running = !1, this.element.addClass("ui-tabs ui-widget ui-widget-content ui-corner-all").toggleClass("ui-tabs-collapsible", i.collapsible), 
            this._processTabs(), i.active = this._initialActive(), e.isArray(i.disabled) && (i.disabled = e.unique(i.disabled.concat(e.map(this.tabs.filter(".ui-state-disabled"), function(e) {
                return t.tabs.index(e);
            }))).sort()), this.active = !1 !== this.options.active && this.anchors.length ? this._findActive(i.active) : e(), 
            this._refresh(), this.active.length && this.load(i.active);
        },
        _initialActive: function() {
            var t = this.options.active, i = this.options.collapsible, s = location.hash.substring(1);
            return null === t && (s && this.tabs.each(function(i, n) {
                return e(n).attr("aria-controls") === s ? (t = i, !1) : void 0;
            }), null === t && (t = this.tabs.index(this.tabs.filter(".ui-tabs-active"))), (null === t || -1 === t) && (t = !!this.tabs.length && 0)), 
            !1 !== t && -1 === (t = this.tabs.index(this.tabs.eq(t))) && (t = !i && 0), !i && !1 === t && this.anchors.length && (t = 0), 
            t;
        },
        _getCreateEventData: function() {
            return {
                tab: this.active,
                panel: this.active.length ? this._getPanelForTab(this.active) : e()
            };
        },
        _tabKeydown: function(t) {
            var i = e(this.document[0].activeElement).closest("li"), s = this.tabs.index(i), n = !0;
            if (!this._handlePageNav(t)) {
                switch (t.keyCode) {
                  case e.ui.keyCode.RIGHT:
                  case e.ui.keyCode.DOWN:
                    s++;
                    break;

                  case e.ui.keyCode.UP:
                  case e.ui.keyCode.LEFT:
                    n = !1, s--;
                    break;

                  case e.ui.keyCode.END:
                    s = this.anchors.length - 1;
                    break;

                  case e.ui.keyCode.HOME:
                    s = 0;
                    break;

                  case e.ui.keyCode.SPACE:
                    return t.preventDefault(), clearTimeout(this.activating), void this._activate(s);

                  case e.ui.keyCode.ENTER:
                    return t.preventDefault(), clearTimeout(this.activating), void this._activate(s !== this.options.active && s);

                  default:
                    return;
                }
                t.preventDefault(), clearTimeout(this.activating), s = this._focusNextTab(s, n), 
                t.ctrlKey || (i.attr("aria-selected", "false"), this.tabs.eq(s).attr("aria-selected", "true"), 
                this.activating = this._delay(function() {
                    this.option("active", s);
                }, this.delay));
            }
        },
        _panelKeydown: function(t) {
            this._handlePageNav(t) || t.ctrlKey && t.keyCode === e.ui.keyCode.UP && (t.preventDefault(), 
            this.active.focus());
        },
        _handlePageNav: function(t) {
            return t.altKey && t.keyCode === e.ui.keyCode.PAGE_UP ? (this._activate(this._focusNextTab(this.options.active - 1, !1)), 
            !0) : t.altKey && t.keyCode === e.ui.keyCode.PAGE_DOWN ? (this._activate(this._focusNextTab(this.options.active + 1, !0)), 
            !0) : void 0;
        },
        _findNextTab: function(t, i) {
            for (var n = this.tabs.length - 1; -1 !== e.inArray(function() {
                return t > n && (t = 0), 0 > t && (t = n), t;
            }(), this.options.disabled); ) t = i ? t + 1 : t - 1;
            return t;
        },
        _focusNextTab: function(e, t) {
            return e = this._findNextTab(e, t), this.tabs.eq(e).focus(), e;
        },
        _setOption: function(e, t) {
            return "active" === e ? void this._activate(t) : "disabled" === e ? void this._setupDisabled(t) : (this._super(e, t), 
            "collapsible" === e && (this.element.toggleClass("ui-tabs-collapsible", t), t || !1 !== this.options.active || this._activate(0)), 
            "event" === e && this._setupEvents(t), void ("heightStyle" === e && this._setupHeightStyle(t)));
        },
        _sanitizeSelector: function(e) {
            return e ? e.replace(/[!"$%&'()*+,.\/:;<=>?@\[\]\^`{|}~]/g, "\\$&") : "";
        },
        refresh: function() {
            var t = this.options, i = this.tablist.children(":has(a[href])");
            t.disabled = e.map(i.filter(".ui-state-disabled"), function(e) {
                return i.index(e);
            }), this._processTabs(), !1 !== t.active && this.anchors.length ? this.active.length && !e.contains(this.tablist[0], this.active[0]) ? this.tabs.length === t.disabled.length ? (t.active = !1, 
            this.active = e()) : this._activate(this._findNextTab(Math.max(0, t.active - 1), !1)) : t.active = this.tabs.index(this.active) : (t.active = !1, 
            this.active = e()), this._refresh();
        },
        _refresh: function() {
            this._setupDisabled(this.options.disabled), this._setupEvents(this.options.event), 
            this._setupHeightStyle(this.options.heightStyle), this.tabs.not(this.active).attr({
                "aria-selected": "false",
                "aria-expanded": "false",
                tabIndex: -1
            }), this.panels.not(this._getPanelForTab(this.active)).hide().attr({
                "aria-hidden": "true"
            }), this.active.length ? (this.active.addClass("ui-tabs-active ui-state-active").attr({
                "aria-selected": "true",
                "aria-expanded": "true",
                tabIndex: 0
            }), this._getPanelForTab(this.active).show().attr({
                "aria-hidden": "false"
            })) : this.tabs.eq(0).attr("tabIndex", 0);
        },
        _processTabs: function() {
            var t = this, i = this.tabs, s = this.anchors, n = this.panels;
            this.tablist = this._getList().addClass("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all").attr("role", "tablist").delegate("> li", "mousedown" + this.eventNamespace, function(t) {
                e(this).is(".ui-state-disabled") && t.preventDefault();
            }).delegate(".ui-tabs-anchor", "focus" + this.eventNamespace, function() {
                e(this).closest("li").is(".ui-state-disabled") && this.blur();
            }), this.tabs = this.tablist.find("> li:has(a[href])").addClass("ui-state-default ui-corner-top").attr({
                role: "tab",
                tabIndex: -1
            }), this.anchors = this.tabs.map(function() {
                return e("a", this)[0];
            }).addClass("ui-tabs-anchor").attr({
                role: "presentation",
                tabIndex: -1
            }), this.panels = e(), this.anchors.each(function(i, s) {
                var n, a, o, r = e(s).uniqueId().attr("id"), h = e(s).closest("li"), l = h.attr("aria-controls");
                t._isLocal(s) ? (n = s.hash, o = n.substring(1), a = t.element.find(t._sanitizeSelector(n))) : (o = h.attr("aria-controls") || e({}).uniqueId()[0].id, 
                n = "#" + o, a = t.element.find(n), a.length || (a = t._createPanel(o), a.insertAfter(t.panels[i - 1] || t.tablist)), 
                a.attr("aria-live", "polite")), a.length && (t.panels = t.panels.add(a)), l && h.data("ui-tabs-aria-controls", l), 
                h.attr({
                    "aria-controls": o,
                    "aria-labelledby": r
                }), a.attr("aria-labelledby", r);
            }), this.panels.addClass("ui-tabs-panel ui-widget-content ui-corner-bottom").attr("role", "tabpanel"), 
            i && (this._off(i.not(this.tabs)), this._off(s.not(this.anchors)), this._off(n.not(this.panels)));
        },
        _getList: function() {
            return this.tablist || this.element.find("ol,ul").eq(0);
        },
        _createPanel: function(t) {
            return e("<div>").attr("id", t).addClass("ui-tabs-panel ui-widget-content ui-corner-bottom").data("ui-tabs-destroy", !0);
        },
        _setupDisabled: function(t) {
            e.isArray(t) && (t.length ? t.length === this.anchors.length && (t = !0) : t = !1);
            for (var i, s = 0; i = this.tabs[s]; s++) !0 === t || -1 !== e.inArray(s, t) ? e(i).addClass("ui-state-disabled").attr("aria-disabled", "true") : e(i).removeClass("ui-state-disabled").removeAttr("aria-disabled");
            this.options.disabled = t;
        },
        _setupEvents: function(t) {
            var i = {};
            t && e.each(t.split(" "), function(e, t) {
                i[t] = "_eventHandler";
            }), this._off(this.anchors.add(this.tabs).add(this.panels)), this._on(!0, this.anchors, {
                click: function(e) {
                    e.preventDefault();
                }
            }), this._on(this.anchors, i), this._on(this.tabs, {
                keydown: "_tabKeydown"
            }), this._on(this.panels, {
                keydown: "_panelKeydown"
            }), this._focusable(this.tabs), this._hoverable(this.tabs);
        },
        _setupHeightStyle: function(t) {
            var i, s = this.element.parent();
            "fill" === t ? (i = s.height(), i -= this.element.outerHeight() - this.element.height(), 
            this.element.siblings(":visible").each(function() {
                var t = e(this), s = t.css("position");
                "absolute" !== s && "fixed" !== s && (i -= t.outerHeight(!0));
            }), this.element.children().not(this.panels).each(function() {
                i -= e(this).outerHeight(!0);
            }), this.panels.each(function() {
                e(this).height(Math.max(0, i - e(this).innerHeight() + e(this).height()));
            }).css("overflow", "auto")) : "auto" === t && (i = 0, this.panels.each(function() {
                i = Math.max(i, e(this).height("").height());
            }).height(i));
        },
        _eventHandler: function(t) {
            var i = this.options, s = this.active, n = e(t.currentTarget), a = n.closest("li"), o = a[0] === s[0], r = o && i.collapsible, h = r ? e() : this._getPanelForTab(a), l = s.length ? this._getPanelForTab(s) : e(), u = {
                oldTab: s,
                oldPanel: l,
                newTab: r ? e() : a,
                newPanel: h
            };
            t.preventDefault(), a.hasClass("ui-state-disabled") || a.hasClass("ui-tabs-loading") || this.running || o && !i.collapsible || !1 === this._trigger("beforeActivate", t, u) || (i.active = !r && this.tabs.index(a), 
            this.active = o ? e() : a, this.xhr && this.xhr.abort(), l.length || h.length || e.error("jQuery UI Tabs: Mismatching fragment identifier."), 
            h.length && this.load(this.tabs.index(a), t), this._toggle(t, u));
        },
        _toggle: function(t, i) {
            function s() {
                a.running = !1, a._trigger("activate", t, i);
            }
            function n() {
                i.newTab.closest("li").addClass("ui-tabs-active ui-state-active"), o.length && a.options.show ? a._show(o, a.options.show, s) : (o.show(), 
                s());
            }
            var a = this, o = i.newPanel, r = i.oldPanel;
            this.running = !0, r.length && this.options.hide ? this._hide(r, this.options.hide, function() {
                i.oldTab.closest("li").removeClass("ui-tabs-active ui-state-active"), n();
            }) : (i.oldTab.closest("li").removeClass("ui-tabs-active ui-state-active"), r.hide(), 
            n()), r.attr("aria-hidden", "true"), i.oldTab.attr({
                "aria-selected": "false",
                "aria-expanded": "false"
            }), o.length && r.length ? i.oldTab.attr("tabIndex", -1) : o.length && this.tabs.filter(function() {
                return 0 === e(this).attr("tabIndex");
            }).attr("tabIndex", -1), o.attr("aria-hidden", "false"), i.newTab.attr({
                "aria-selected": "true",
                "aria-expanded": "true",
                tabIndex: 0
            });
        },
        _activate: function(t) {
            var i, s = this._findActive(t);
            s[0] !== this.active[0] && (s.length || (s = this.active), i = s.find(".ui-tabs-anchor")[0], 
            this._eventHandler({
                target: i,
                currentTarget: i,
                preventDefault: e.noop
            }));
        },
        _findActive: function(t) {
            return !1 === t ? e() : this.tabs.eq(t);
        },
        _getIndex: function(e) {
            return "string" == typeof e && (e = this.anchors.index(this.anchors.filter("[href$='" + e + "']"))), 
            e;
        },
        _destroy: function() {
            this.xhr && this.xhr.abort(), this.element.removeClass("ui-tabs ui-widget ui-widget-content ui-corner-all ui-tabs-collapsible"), 
            this.tablist.removeClass("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all").removeAttr("role"), 
            this.anchors.removeClass("ui-tabs-anchor").removeAttr("role").removeAttr("tabIndex").removeUniqueId(), 
            this.tablist.unbind(this.eventNamespace), this.tabs.add(this.panels).each(function() {
                e.data(this, "ui-tabs-destroy") ? e(this).remove() : e(this).removeClass("ui-state-default ui-state-active ui-state-disabled ui-corner-top ui-corner-bottom ui-widget-content ui-tabs-active ui-tabs-panel").removeAttr("tabIndex").removeAttr("aria-live").removeAttr("aria-busy").removeAttr("aria-selected").removeAttr("aria-labelledby").removeAttr("aria-hidden").removeAttr("aria-expanded").removeAttr("role");
            }), this.tabs.each(function() {
                var t = e(this), i = t.data("ui-tabs-aria-controls");
                i ? t.attr("aria-controls", i).removeData("ui-tabs-aria-controls") : t.removeAttr("aria-controls");
            }), this.panels.show(), "content" !== this.options.heightStyle && this.panels.css("height", "");
        },
        enable: function(t) {
            var i = this.options.disabled;
            !1 !== i && (void 0 === t ? i = !1 : (t = this._getIndex(t), i = e.isArray(i) ? e.map(i, function(e) {
                return e !== t ? e : null;
            }) : e.map(this.tabs, function(e, i) {
                return i !== t ? i : null;
            })), this._setupDisabled(i));
        },
        disable: function(t) {
            var i = this.options.disabled;
            if (!0 !== i) {
                if (void 0 === t) i = !0; else {
                    if (t = this._getIndex(t), -1 !== e.inArray(t, i)) return;
                    i = e.isArray(i) ? e.merge([ t ], i).sort() : [ t ];
                }
                this._setupDisabled(i);
            }
        },
        load: function(t, i) {
            t = this._getIndex(t);
            var s = this, n = this.tabs.eq(t), a = n.find(".ui-tabs-anchor"), o = this._getPanelForTab(n), r = {
                tab: n,
                panel: o
            };
            this._isLocal(a[0]) || (this.xhr = e.ajax(this._ajaxSettings(a, i, r)), this.xhr && "canceled" !== this.xhr.statusText && (n.addClass("ui-tabs-loading"), 
            o.attr("aria-busy", "true"), this.xhr.success(function(e) {
                setTimeout(function() {
                    o.html(e), s._trigger("load", i, r);
                }, 1);
            }).complete(function(e, t) {
                setTimeout(function() {
                    "abort" === t && s.panels.stop(!1, !0), n.removeClass("ui-tabs-loading"), o.removeAttr("aria-busy"), 
                    e === s.xhr && delete s.xhr;
                }, 1);
            })));
        },
        _ajaxSettings: function(t, i, s) {
            var n = this;
            return {
                url: t.attr("href"),
                beforeSend: function(t, a) {
                    return n._trigger("beforeLoad", i, e.extend({
                        jqXHR: t,
                        ajaxSettings: a
                    }, s));
                }
            };
        },
        _getPanelForTab: function(t) {
            var i = e(t).attr("aria-controls");
            return this.element.find(this._sanitizeSelector("#" + i));
        }
    }), e.widget("ui.tooltip", {
        version: "1.11.2",
        options: {
            content: function() {
                var t = e(this).attr("title") || "";
                return e("<a>").text(t).html();
            },
            hide: !0,
            items: "[title]:not([disabled])",
            position: {
                my: "left top+15",
                at: "left bottom",
                collision: "flipfit flip"
            },
            show: !0,
            tooltipClass: null,
            track: !1,
            close: null,
            open: null
        },
        _addDescribedBy: function(t, i) {
            var s = (t.attr("aria-describedby") || "").split(/\s+/);
            s.push(i), t.data("ui-tooltip-id", i).attr("aria-describedby", e.trim(s.join(" ")));
        },
        _removeDescribedBy: function(t) {
            var i = t.data("ui-tooltip-id"), s = (t.attr("aria-describedby") || "").split(/\s+/), n = e.inArray(i, s);
            -1 !== n && s.splice(n, 1), t.removeData("ui-tooltip-id"), s = e.trim(s.join(" ")), 
            s ? t.attr("aria-describedby", s) : t.removeAttr("aria-describedby");
        },
        _create: function() {
            this._on({
                mouseover: "open",
                focusin: "open"
            }), this.tooltips = {}, this.parents = {}, this.options.disabled && this._disable(), 
            this.liveRegion = e("<div>").attr({
                role: "log",
                "aria-live": "assertive",
                "aria-relevant": "additions"
            }).addClass("ui-helper-hidden-accessible").appendTo(this.document[0].body);
        },
        _setOption: function(t, i) {
            var s = this;
            return "disabled" === t ? (this[i ? "_disable" : "_enable"](), void (this.options[t] = i)) : (this._super(t, i), 
            void ("content" === t && e.each(this.tooltips, function(e, t) {
                s._updateContent(t.element);
            })));
        },
        _disable: function() {
            var t = this;
            e.each(this.tooltips, function(i, s) {
                var n = e.Event("blur");
                n.target = n.currentTarget = s.element[0], t.close(n, !0);
            }), this.element.find(this.options.items).addBack().each(function() {
                var t = e(this);
                t.is("[title]") && t.data("ui-tooltip-title", t.attr("title")).removeAttr("title");
            });
        },
        _enable: function() {
            this.element.find(this.options.items).addBack().each(function() {
                var t = e(this);
                t.data("ui-tooltip-title") && t.attr("title", t.data("ui-tooltip-title"));
            });
        },
        open: function(t) {
            var i = this, s = e(t ? t.target : this.element).closest(this.options.items);
            s.length && !s.data("ui-tooltip-id") && (s.attr("title") && s.data("ui-tooltip-title", s.attr("title")), 
            s.data("ui-tooltip-open", !0), t && "mouseover" === t.type && s.parents().each(function() {
                var t, s = e(this);
                s.data("ui-tooltip-open") && (t = e.Event("blur"), t.target = t.currentTarget = this, 
                i.close(t, !0)), s.attr("title") && (s.uniqueId(), i.parents[this.id] = {
                    element: this,
                    title: s.attr("title")
                }, s.attr("title", ""));
            }), this._updateContent(s, t));
        },
        _updateContent: function(e, t) {
            var i, s = this.options.content, n = this, a = t ? t.type : null;
            return "string" == typeof s ? this._open(t, e, s) : void ((i = s.call(e[0], function(i) {
                e.data("ui-tooltip-open") && n._delay(function() {
                    t && (t.type = a), this._open(t, e, i);
                });
            })) && this._open(t, e, i));
        },
        _open: function(t, i, s) {
            function n(e) {
                u.of = e, o.is(":hidden") || o.position(u);
            }
            var a, o, r, h, l, u = e.extend({}, this.options.position);
            if (s) {
                if (a = this._find(i)) return void a.tooltip.find(".ui-tooltip-content").html(s);
                i.is("[title]") && (t && "mouseover" === t.type ? i.attr("title", "") : i.removeAttr("title")), 
                a = this._tooltip(i), o = a.tooltip, this._addDescribedBy(i, o.attr("id")), o.find(".ui-tooltip-content").html(s), 
                this.liveRegion.children().hide(), s.clone ? (l = s.clone(), l.removeAttr("id").find("[id]").removeAttr("id")) : l = s, 
                e("<div>").html(l).appendTo(this.liveRegion), this.options.track && t && /^mouse/.test(t.type) ? (this._on(this.document, {
                    mousemove: n
                }), n(t)) : o.position(e.extend({
                    of: i
                }, this.options.position)), o.hide(), this._show(o, this.options.show), this.options.show && this.options.show.delay && (h = this.delayedShow = setInterval(function() {
                    o.is(":visible") && (n(u.of), clearInterval(h));
                }, e.fx.interval)), this._trigger("open", t, {
                    tooltip: o
                }), r = {
                    keyup: function(t) {
                        if (t.keyCode === e.ui.keyCode.ESCAPE) {
                            var s = e.Event(t);
                            s.currentTarget = i[0], this.close(s, !0);
                        }
                    }
                }, i[0] !== this.element[0] && (r.remove = function() {
                    this._removeTooltip(o);
                }), t && "mouseover" !== t.type || (r.mouseleave = "close"), t && "focusin" !== t.type || (r.focusout = "close"), 
                this._on(!0, i, r);
            }
        },
        close: function(t) {
            var i, s = this, n = e(t ? t.currentTarget : this.element), a = this._find(n);
            a && (i = a.tooltip, a.closing || (clearInterval(this.delayedShow), n.data("ui-tooltip-title") && !n.attr("title") && n.attr("title", n.data("ui-tooltip-title")), 
            this._removeDescribedBy(n), a.hiding = !0, i.stop(!0), this._hide(i, this.options.hide, function() {
                s._removeTooltip(e(this));
            }), n.removeData("ui-tooltip-open"), this._off(n, "mouseleave focusout keyup"), 
            n[0] !== this.element[0] && this._off(n, "remove"), this._off(this.document, "mousemove"), 
            t && "mouseleave" === t.type && e.each(this.parents, function(t, i) {
                e(i.element).attr("title", i.title), delete s.parents[t];
            }), a.closing = !0, this._trigger("close", t, {
                tooltip: i
            }), a.hiding || (a.closing = !1)));
        },
        _tooltip: function(t) {
            var i = e("<div>").attr("role", "tooltip").addClass("ui-tooltip ui-widget ui-corner-all ui-widget-content " + (this.options.tooltipClass || "")), s = i.uniqueId().attr("id");
            return e("<div>").addClass("ui-tooltip-content").appendTo(i), i.appendTo(this.document[0].body), 
            this.tooltips[s] = {
                element: t,
                tooltip: i
            };
        },
        _find: function(e) {
            var t = e.data("ui-tooltip-id");
            return t ? this.tooltips[t] : null;
        },
        _removeTooltip: function(e) {
            e.remove(), delete this.tooltips[e.attr("id")];
        },
        _destroy: function() {
            var t = this;
            e.each(this.tooltips, function(i, s) {
                var n = e.Event("blur"), a = s.element;
                n.target = n.currentTarget = a[0], t.close(n, !0), e("#" + i).remove(), a.data("ui-tooltip-title") && (a.attr("title") || a.attr("title", a.data("ui-tooltip-title")), 
                a.removeData("ui-tooltip-title"));
            }), this.liveRegion.remove();
        }
    });
    var y = "ui-effects-", b = e;
    e.effects = {
        effect: {}
    }, function(e, t) {
        function i(e, t, i) {
            var s = d[t.type] || {};
            return null == e ? i || !t.def ? null : t.def : (e = s.floor ? ~~e : parseFloat(e), 
            isNaN(e) ? t.def : s.mod ? (e + s.mod) % s.mod : 0 > e ? 0 : e > s.max ? s.max : e);
        }
        function s(i) {
            var s = l(), n = s._rgba = [];
            return i = i.toLowerCase(), f(h, function(e, a) {
                var o, r = a.re.exec(i), h = r && a.parse(r), l = a.space || "rgba";
                return h ? (o = s[l](h), s[u[l].cache] = o[u[l].cache], n = s._rgba = o._rgba, !1) : t;
            }), n.length ? ("0,0,0,0" === n.join() && e.extend(n, a.transparent), s) : a[i];
        }
        function n(e, t, i) {
            return i = (i + 1) % 1, 1 > 6 * i ? e + 6 * (t - e) * i : 1 > 2 * i ? t : 2 > 3 * i ? e + 6 * (t - e) * (2 / 3 - i) : e;
        }
        var a, r = /^([\-+])=\s*(\d+\.?\d*)/, h = [ {
            re: /rgba?\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,
            parse: function(e) {
                return [ e[1], e[2], e[3], e[4] ];
            }
        }, {
            re: /rgba?\(\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,
            parse: function(e) {
                return [ 2.55 * e[1], 2.55 * e[2], 2.55 * e[3], e[4] ];
            }
        }, {
            re: /#([a-f0-9]{2})([a-f0-9]{2})([a-f0-9]{2})/,
            parse: function(e) {
                return [ parseInt(e[1], 16), parseInt(e[2], 16), parseInt(e[3], 16) ];
            }
        }, {
            re: /#([a-f0-9])([a-f0-9])([a-f0-9])/,
            parse: function(e) {
                return [ parseInt(e[1] + e[1], 16), parseInt(e[2] + e[2], 16), parseInt(e[3] + e[3], 16) ];
            }
        }, {
            re: /hsla?\(\s*(\d+(?:\.\d+)?)\s*,\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,
            space: "hsla",
            parse: function(e) {
                return [ e[1], e[2] / 100, e[3] / 100, e[4] ];
            }
        } ], l = e.Color = function(t, i, s, n) {
            return new e.Color.fn.parse(t, i, s, n);
        }, u = {
            rgba: {
                props: {
                    red: {
                        idx: 0,
                        type: "byte"
                    },
                    green: {
                        idx: 1,
                        type: "byte"
                    },
                    blue: {
                        idx: 2,
                        type: "byte"
                    }
                }
            },
            hsla: {
                props: {
                    hue: {
                        idx: 0,
                        type: "degrees"
                    },
                    saturation: {
                        idx: 1,
                        type: "percent"
                    },
                    lightness: {
                        idx: 2,
                        type: "percent"
                    }
                }
            }
        }, d = {
            byte: {
                floor: !0,
                max: 255
            },
            percent: {
                max: 1
            },
            degrees: {
                mod: 360,
                floor: !0
            }
        }, c = l.support = {}, p = e("<p>")[0], f = e.each;
        p.style.cssText = "background-color:rgba(1,1,1,.5)", c.rgba = p.style.backgroundColor.indexOf("rgba") > -1, 
        f(u, function(e, t) {
            t.cache = "_" + e, t.props.alpha = {
                idx: 3,
                type: "percent",
                def: 1
            };
        }), l.fn = e.extend(l.prototype, {
            parse: function(n, o, r, h) {
                if (n === t) return this._rgba = [ null, null, null, null ], this;
                (n.jquery || n.nodeType) && (n = e(n).css(o), o = t);
                var d = this, c = e.type(n), p = this._rgba = [];
                return o !== t && (n = [ n, o, r, h ], c = "array"), "string" === c ? this.parse(s(n) || a._default) : "array" === c ? (f(u.rgba.props, function(e, t) {
                    p[t.idx] = i(n[t.idx], t);
                }), this) : "object" === c ? (n instanceof l ? f(u, function(e, t) {
                    n[t.cache] && (d[t.cache] = n[t.cache].slice());
                }) : f(u, function(t, s) {
                    var a = s.cache;
                    f(s.props, function(e, t) {
                        if (!d[a] && s.to) {
                            if ("alpha" === e || null == n[e]) return;
                            d[a] = s.to(d._rgba);
                        }
                        d[a][t.idx] = i(n[e], t, !0);
                    }), d[a] && 0 > e.inArray(null, d[a].slice(0, 3)) && (d[a][3] = 1, s.from && (d._rgba = s.from(d[a])));
                }), this) : t;
            },
            is: function(e) {
                var i = l(e), s = !0, n = this;
                return f(u, function(e, a) {
                    var o, r = i[a.cache];
                    return r && (o = n[a.cache] || a.to && a.to(n._rgba) || [], f(a.props, function(e, i) {
                        return null != r[i.idx] ? s = r[i.idx] === o[i.idx] : t;
                    })), s;
                }), s;
            },
            _space: function() {
                var e = [], t = this;
                return f(u, function(i, s) {
                    t[s.cache] && e.push(i);
                }), e.pop();
            },
            transition: function(e, t) {
                var s = l(e), n = s._space(), a = u[n], o = 0 === this.alpha() ? l("transparent") : this, r = o[a.cache] || a.to(o._rgba), h = r.slice();
                return s = s[a.cache], f(a.props, function(e, n) {
                    var a = n.idx, o = r[a], l = s[a], u = d[n.type] || {};
                    null !== l && (null === o ? h[a] = l : (u.mod && (l - o > u.mod / 2 ? o += u.mod : o - l > u.mod / 2 && (o -= u.mod)), 
                    h[a] = i((l - o) * t + o, n)));
                }), this[n](h);
            },
            blend: function(t) {
                if (1 === this._rgba[3]) return this;
                var i = this._rgba.slice(), s = i.pop(), n = l(t)._rgba;
                return l(e.map(i, function(e, t) {
                    return (1 - s) * n[t] + s * e;
                }));
            },
            toRgbaString: function() {
                var t = "rgba(", i = e.map(this._rgba, function(e, t) {
                    return null == e ? t > 2 ? 1 : 0 : e;
                });
                return 1 === i[3] && (i.pop(), t = "rgb("), t + i.join() + ")";
            },
            toHslaString: function() {
                var t = "hsla(", i = e.map(this.hsla(), function(e, t) {
                    return null == e && (e = t > 2 ? 1 : 0), t && 3 > t && (e = Math.round(100 * e) + "%"), 
                    e;
                });
                return 1 === i[3] && (i.pop(), t = "hsl("), t + i.join() + ")";
            },
            toHexString: function(t) {
                var i = this._rgba.slice(), s = i.pop();
                return t && i.push(~~(255 * s)), "#" + e.map(i, function(e) {
                    return e = (e || 0).toString(16), 1 === e.length ? "0" + e : e;
                }).join("");
            },
            toString: function() {
                return 0 === this._rgba[3] ? "transparent" : this.toRgbaString();
            }
        }), l.fn.parse.prototype = l.fn, u.hsla.to = function(e) {
            if (null == e[0] || null == e[1] || null == e[2]) return [ null, null, null, e[3] ];
            var t, i, s = e[0] / 255, n = e[1] / 255, a = e[2] / 255, o = e[3], r = Math.max(s, n, a), h = Math.min(s, n, a), l = r - h, u = r + h, d = .5 * u;
            return t = h === r ? 0 : s === r ? 60 * (n - a) / l + 360 : n === r ? 60 * (a - s) / l + 120 : 60 * (s - n) / l + 240, 
            i = 0 === l ? 0 : .5 >= d ? l / u : l / (2 - u), [ Math.round(t) % 360, i, d, null == o ? 1 : o ];
        }, u.hsla.from = function(e) {
            if (null == e[0] || null == e[1] || null == e[2]) return [ null, null, null, e[3] ];
            var t = e[0] / 360, i = e[1], s = e[2], a = e[3], o = .5 >= s ? s * (1 + i) : s + i - s * i, r = 2 * s - o;
            return [ Math.round(255 * n(r, o, t + 1 / 3)), Math.round(255 * n(r, o, t)), Math.round(255 * n(r, o, t - 1 / 3)), a ];
        }, f(u, function(s, n) {
            var a = n.props, o = n.cache, h = n.to, u = n.from;
            l.fn[s] = function(s) {
                if (h && !this[o] && (this[o] = h(this._rgba)), s === t) return this[o].slice();
                var n, r = e.type(s), d = "array" === r || "object" === r ? s : arguments, c = this[o].slice();
                return f(a, function(e, t) {
                    var s = d["object" === r ? e : t.idx];
                    null == s && (s = c[t.idx]), c[t.idx] = i(s, t);
                }), u ? (n = l(u(c)), n[o] = c, n) : l(c);
            }, f(a, function(t, i) {
                l.fn[t] || (l.fn[t] = function(n) {
                    var a, o = e.type(n), h = "alpha" === t ? this._hsla ? "hsla" : "rgba" : s, l = this[h](), u = l[i.idx];
                    return "undefined" === o ? u : ("function" === o && (n = n.call(this, u), o = e.type(n)), 
                    null == n && i.empty ? this : ("string" === o && (a = r.exec(n)) && (n = u + parseFloat(a[2]) * ("+" === a[1] ? 1 : -1)), 
                    l[i.idx] = n, this[h](l)));
                });
            });
        }), l.hook = function(t) {
            var i = t.split(" ");
            f(i, function(t, i) {
                e.cssHooks[i] = {
                    set: function(t, n) {
                        var a, o, r = "";
                        if ("transparent" !== n && ("string" !== e.type(n) || (a = s(n)))) {
                            if (n = l(a || n), !c.rgba && 1 !== n._rgba[3]) {
                                for (o = "backgroundColor" === i ? t.parentNode : t; ("" === r || "transparent" === r) && o && o.style; ) try {
                                    r = e.css(o, "backgroundColor"), o = o.parentNode;
                                } catch (h) {}
                                n = n.blend(r && "transparent" !== r ? r : "_default");
                            }
                            n = n.toRgbaString();
                        }
                        try {
                            t.style[i] = n;
                        } catch (h) {}
                    }
                }, e.fx.step[i] = function(t) {
                    t.colorInit || (t.start = l(t.elem, i), t.end = l(t.end), t.colorInit = !0), e.cssHooks[i].set(t.elem, t.start.transition(t.end, t.pos));
                };
            });
        }, l.hook("backgroundColor borderBottomColor borderLeftColor borderRightColor borderTopColor color columnRuleColor outlineColor textDecorationColor textEmphasisColor"), 
        e.cssHooks.borderColor = {
            expand: function(e) {
                var t = {};
                return f([ "Top", "Right", "Bottom", "Left" ], function(i, s) {
                    t["border" + s + "Color"] = e;
                }), t;
            }
        }, a = e.Color.names = {
            aqua: "#00ffff",
            black: "#000000",
            blue: "#0000ff",
            fuchsia: "#ff00ff",
            gray: "#808080",
            green: "#008000",
            lime: "#00ff00",
            maroon: "#800000",
            navy: "#000080",
            olive: "#808000",
            purple: "#800080",
            red: "#ff0000",
            silver: "#c0c0c0",
            teal: "#008080",
            white: "#ffffff",
            yellow: "#ffff00",
            transparent: [ null, null, null, 0 ],
            _default: "#ffffff"
        };
    }(b), function() {
        function t(t) {
            var i, s, n = t.ownerDocument.defaultView ? t.ownerDocument.defaultView.getComputedStyle(t, null) : t.currentStyle, a = {};
            if (n && n.length && n[0] && n[n[0]]) for (s = n.length; s--; ) i = n[s], "string" == typeof n[i] && (a[e.camelCase(i)] = n[i]); else for (i in n) "string" == typeof n[i] && (a[i] = n[i]);
            return a;
        }
        function i(t, i) {
            var s, a, o = {};
            for (s in i) a = i[s], t[s] !== a && (n[s] || (e.fx.step[s] || !isNaN(parseFloat(a))) && (o[s] = a));
            return o;
        }
        var s = [ "add", "remove", "toggle" ], n = {
            border: 1,
            borderBottom: 1,
            borderColor: 1,
            borderLeft: 1,
            borderRight: 1,
            borderTop: 1,
            borderWidth: 1,
            margin: 1,
            padding: 1
        };
        e.each([ "borderLeftStyle", "borderRightStyle", "borderBottomStyle", "borderTopStyle" ], function(t, i) {
            e.fx.step[i] = function(e) {
                ("none" !== e.end && !e.setAttr || 1 === e.pos && !e.setAttr) && (b.style(e.elem, i, e.end), 
                e.setAttr = !0);
            };
        }), e.fn.addBack || (e.fn.addBack = function(e) {
            return this.add(null == e ? this.prevObject : this.prevObject.filter(e));
        }), e.effects.animateClass = function(n, a, o, r) {
            var h = e.speed(a, o, r);
            return this.queue(function() {
                var a, o = e(this), r = o.attr("class") || "", l = h.children ? o.find("*").addBack() : o;
                l = l.map(function() {
                    return {
                        el: e(this),
                        start: t(this)
                    };
                }), a = function() {
                    e.each(s, function(e, t) {
                        n[t] && o[t + "Class"](n[t]);
                    });
                }, a(), l = l.map(function() {
                    return this.end = t(this.el[0]), this.diff = i(this.start, this.end), this;
                }), o.attr("class", r), l = l.map(function() {
                    var t = this, i = e.Deferred(), s = e.extend({}, h, {
                        queue: !1,
                        complete: function() {
                            i.resolve(t);
                        }
                    });
                    return this.el.animate(this.diff, s), i.promise();
                }), e.when.apply(e, l.get()).done(function() {
                    a(), e.each(arguments, function() {
                        var t = this.el;
                        e.each(this.diff, function(e) {
                            t.css(e, "");
                        });
                    }), h.complete.call(o[0]);
                });
            });
        }, e.fn.extend({
            addClass: function(t) {
                return function(i, s, n, a) {
                    return s ? e.effects.animateClass.call(this, {
                        add: i
                    }, s, n, a) : t.apply(this, arguments);
                };
            }(e.fn.addClass),
            removeClass: function(t) {
                return function(i, s, n, a) {
                    return arguments.length > 1 ? e.effects.animateClass.call(this, {
                        remove: i
                    }, s, n, a) : t.apply(this, arguments);
                };
            }(e.fn.removeClass),
            toggleClass: function(t) {
                return function(i, s, n, a, o) {
                    return "boolean" == typeof s || void 0 === s ? n ? e.effects.animateClass.call(this, s ? {
                        add: i
                    } : {
                        remove: i
                    }, n, a, o) : t.apply(this, arguments) : e.effects.animateClass.call(this, {
                        toggle: i
                    }, s, n, a);
                };
            }(e.fn.toggleClass),
            switchClass: function(t, i, s, n, a) {
                return e.effects.animateClass.call(this, {
                    add: i,
                    remove: t
                }, s, n, a);
            }
        });
    }(), function() {
        function t(t, i, s, n) {
            return e.isPlainObject(t) && (i = t, t = t.effect), t = {
                effect: t
            }, null == i && (i = {}), e.isFunction(i) && (n = i, s = null, i = {}), ("number" == typeof i || e.fx.speeds[i]) && (n = s, 
            s = i, i = {}), e.isFunction(s) && (n = s, s = null), i && e.extend(t, i), s = s || i.duration, 
            t.duration = e.fx.off ? 0 : "number" == typeof s ? s : s in e.fx.speeds ? e.fx.speeds[s] : e.fx.speeds._default, 
            t.complete = n || i.complete, t;
        }
        function i(t) {
            return !(t && "number" != typeof t && !e.fx.speeds[t]) || ("string" == typeof t && !e.effects.effect[t] || (!!e.isFunction(t) || "object" == typeof t && !t.effect));
        }
        e.extend(e.effects, {
            version: "1.11.2",
            save: function(e, t) {
                for (var i = 0; t.length > i; i++) null !== t[i] && e.data(y + t[i], e[0].style[t[i]]);
            },
            restore: function(e, t) {
                var i, s;
                for (s = 0; t.length > s; s++) null !== t[s] && (i = e.data(y + t[s]), void 0 === i && (i = ""), 
                e.css(t[s], i));
            },
            setMode: function(e, t) {
                return "toggle" === t && (t = e.is(":hidden") ? "show" : "hide"), t;
            },
            getBaseline: function(e, t) {
                var i, s;
                switch (e[0]) {
                  case "top":
                    i = 0;
                    break;

                  case "middle":
                    i = .5;
                    break;

                  case "bottom":
                    i = 1;
                    break;

                  default:
                    i = e[0] / t.height;
                }
                switch (e[1]) {
                  case "left":
                    s = 0;
                    break;

                  case "center":
                    s = .5;
                    break;

                  case "right":
                    s = 1;
                    break;

                  default:
                    s = e[1] / t.width;
                }
                return {
                    x: s,
                    y: i
                };
            },
            createWrapper: function(t) {
                if (t.parent().is(".ui-effects-wrapper")) return t.parent();
                var i = {
                    width: t.outerWidth(!0),
                    height: t.outerHeight(!0),
                    float: t.css("float")
                }, s = e("<div></div>").addClass("ui-effects-wrapper").css({
                    fontSize: "100%",
                    background: "transparent",
                    border: "none",
                    margin: 0,
                    padding: 0
                }), n = {
                    width: t.width(),
                    height: t.height()
                }, a = document.activeElement;
                try {
                    a.id;
                } catch (o) {
                    a = document.body;
                }
                return t.wrap(s), (t[0] === a || e.contains(t[0], a)) && e(a).focus(), s = t.parent(), 
                "static" === t.css("position") ? (s.css({
                    position: "relative"
                }), t.css({
                    position: "relative"
                })) : (e.extend(i, {
                    position: t.css("position"),
                    zIndex: t.css("z-index")
                }), e.each([ "top", "left", "bottom", "right" ], function(e, s) {
                    i[s] = t.css(s), isNaN(parseInt(i[s], 10)) && (i[s] = "auto");
                }), t.css({
                    position: "relative",
                    top: 0,
                    left: 0,
                    right: "auto",
                    bottom: "auto"
                })), t.css(n), s.css(i).show();
            },
            removeWrapper: function(t) {
                var i = document.activeElement;
                return t.parent().is(".ui-effects-wrapper") && (t.parent().replaceWith(t), (t[0] === i || e.contains(t[0], i)) && e(i).focus()), 
                t;
            },
            setTransition: function(t, i, s, n) {
                return n = n || {}, e.each(i, function(e, i) {
                    var a = t.cssUnit(i);
                    a[0] > 0 && (n[i] = a[0] * s + a[1]);
                }), n;
            }
        }), e.fn.extend({
            effect: function() {
                function i(t) {
                    function i() {
                        e.isFunction(a) && a.call(n[0]), e.isFunction(t) && t();
                    }
                    var n = e(this), a = s.complete, r = s.mode;
                    (n.is(":hidden") ? "hide" === r : "show" === r) ? (n[r](), i()) : o.call(n[0], s, i);
                }
                var s = t.apply(this, arguments), n = s.mode, a = s.queue, o = e.effects.effect[s.effect];
                return e.fx.off || !o ? n ? this[n](s.duration, s.complete) : this.each(function() {
                    s.complete && s.complete.call(this);
                }) : !1 === a ? this.each(i) : this.queue(a || "fx", i);
            },
            show: function(e) {
                return function(s) {
                    if (i(s)) return e.apply(this, arguments);
                    var n = t.apply(this, arguments);
                    return n.mode = "show", this.effect.call(this, n);
                };
            }(e.fn.show),
            hide: function(e) {
                return function(s) {
                    if (i(s)) return e.apply(this, arguments);
                    var n = t.apply(this, arguments);
                    return n.mode = "hide", this.effect.call(this, n);
                };
            }(e.fn.hide),
            toggle: function(e) {
                return function(s) {
                    if (i(s) || "boolean" == typeof s) return e.apply(this, arguments);
                    var n = t.apply(this, arguments);
                    return n.mode = "toggle", this.effect.call(this, n);
                };
            }(e.fn.toggle),
            cssUnit: function(t) {
                var i = this.css(t), s = [];
                return e.each([ "em", "px", "%", "pt" ], function(e, t) {
                    i.indexOf(t) > 0 && (s = [ parseFloat(i), t ]);
                }), s;
            }
        });
    }(), function() {
        var t = {};
        e.each([ "Quad", "Cubic", "Quart", "Quint", "Expo" ], function(e, i) {
            t[i] = function(t) {
                return Math.pow(t, e + 2);
            };
        }), e.extend(t, {
            Sine: function(e) {
                return 1 - Math.cos(e * Math.PI / 2);
            },
            Circ: function(e) {
                return 1 - Math.sqrt(1 - e * e);
            },
            Elastic: function(e) {
                return 0 === e || 1 === e ? e : -Math.pow(2, 8 * (e - 1)) * Math.sin((80 * (e - 1) - 7.5) * Math.PI / 15);
            },
            Back: function(e) {
                return e * e * (3 * e - 2);
            },
            Bounce: function(e) {
                for (var t, i = 4; ((t = Math.pow(2, --i)) - 1) / 11 > e; ) ;
                return 1 / Math.pow(4, 3 - i) - 7.5625 * Math.pow((3 * t - 2) / 22 - e, 2);
            }
        }), e.each(t, function(t, i) {
            e.easing["easeIn" + t] = i, e.easing["easeOut" + t] = function(e) {
                return 1 - i(1 - e);
            }, e.easing["easeInOut" + t] = function(e) {
                return .5 > e ? i(2 * e) / 2 : 1 - i(-2 * e + 2) / 2;
            };
        });
    }(), e.effects, e.effects.effect.blind = function(t, i) {
        var s, n, a, o = e(this), r = /up|down|vertical/, h = /up|left|vertical|horizontal/, l = [ "position", "top", "bottom", "left", "right", "height", "width" ], u = e.effects.setMode(o, t.mode || "hide"), d = t.direction || "up", c = r.test(d), p = c ? "height" : "width", f = c ? "top" : "left", m = h.test(d), g = {}, v = "show" === u;
        o.parent().is(".ui-effects-wrapper") ? e.effects.save(o.parent(), l) : e.effects.save(o, l), 
        o.show(), s = e.effects.createWrapper(o).css({
            overflow: "hidden"
        }), n = s[p](), a = parseFloat(s.css(f)) || 0, g[p] = v ? n : 0, m || (o.css(c ? "bottom" : "right", 0).css(c ? "top" : "left", "auto").css({
            position: "absolute"
        }), g[f] = v ? a : n + a), v && (s.css(p, 0), m || s.css(f, a + n)), s.animate(g, {
            duration: t.duration,
            easing: t.easing,
            queue: !1,
            complete: function() {
                "hide" === u && o.hide(), e.effects.restore(o, l), e.effects.removeWrapper(o), i();
            }
        });
    }, e.effects.effect.bounce = function(t, i) {
        var s, n, a, o = e(this), r = [ "position", "top", "bottom", "left", "right", "height", "width" ], h = e.effects.setMode(o, t.mode || "effect"), l = "hide" === h, u = "show" === h, d = t.direction || "up", c = t.distance, p = t.times || 5, f = 2 * p + (u || l ? 1 : 0), m = t.duration / f, g = t.easing, v = "up" === d || "down" === d ? "top" : "left", y = "up" === d || "left" === d, b = o.queue(), _ = b.length;
        for ((u || l) && r.push("opacity"), e.effects.save(o, r), o.show(), e.effects.createWrapper(o), 
        c || (c = o["top" === v ? "outerHeight" : "outerWidth"]() / 3), u && (a = {
            opacity: 1
        }, a[v] = 0, o.css("opacity", 0).css(v, y ? 2 * -c : 2 * c).animate(a, m, g)), l && (c /= Math.pow(2, p - 1)), 
        a = {}, a[v] = 0, s = 0; p > s; s++) n = {}, n[v] = (y ? "-=" : "+=") + c, o.animate(n, m, g).animate(a, m, g), 
        c = l ? 2 * c : c / 2;
        l && (n = {
            opacity: 0
        }, n[v] = (y ? "-=" : "+=") + c, o.animate(n, m, g)), o.queue(function() {
            l && o.hide(), e.effects.restore(o, r), e.effects.removeWrapper(o), i();
        }), _ > 1 && b.splice.apply(b, [ 1, 0 ].concat(b.splice(_, f + 1))), o.dequeue();
    }, e.effects.effect.clip = function(t, i) {
        var s, n, a, o = e(this), r = [ "position", "top", "bottom", "left", "right", "height", "width" ], h = e.effects.setMode(o, t.mode || "hide"), l = "show" === h, u = t.direction || "vertical", d = "vertical" === u, c = d ? "height" : "width", p = d ? "top" : "left", f = {};
        e.effects.save(o, r), o.show(), s = e.effects.createWrapper(o).css({
            overflow: "hidden"
        }), n = "IMG" === o[0].tagName ? s : o, a = n[c](), l && (n.css(c, 0), n.css(p, a / 2)), 
        f[c] = l ? a : 0, f[p] = l ? 0 : a / 2, n.animate(f, {
            queue: !1,
            duration: t.duration,
            easing: t.easing,
            complete: function() {
                l || o.hide(), e.effects.restore(o, r), e.effects.removeWrapper(o), i();
            }
        });
    }, e.effects.effect.drop = function(t, i) {
        var s, n = e(this), a = [ "position", "top", "bottom", "left", "right", "opacity", "height", "width" ], o = e.effects.setMode(n, t.mode || "hide"), r = "show" === o, h = t.direction || "left", l = "up" === h || "down" === h ? "top" : "left", u = "up" === h || "left" === h ? "pos" : "neg", d = {
            opacity: r ? 1 : 0
        };
        e.effects.save(n, a), n.show(), e.effects.createWrapper(n), s = t.distance || n["top" === l ? "outerHeight" : "outerWidth"](!0) / 2, 
        r && n.css("opacity", 0).css(l, "pos" === u ? -s : s), d[l] = (r ? "pos" === u ? "+=" : "-=" : "pos" === u ? "-=" : "+=") + s, 
        n.animate(d, {
            queue: !1,
            duration: t.duration,
            easing: t.easing,
            complete: function() {
                "hide" === o && n.hide(), e.effects.restore(n, a), e.effects.removeWrapper(n), i();
            }
        });
    }, e.effects.effect.explode = function(t, i) {
        function s() {
            b.push(this), b.length === d * c && n();
        }
        function n() {
            p.css({
                visibility: "visible"
            }), e(b).remove(), m || p.hide(), i();
        }
        var a, o, r, h, l, u, d = t.pieces ? Math.round(Math.sqrt(t.pieces)) : 3, c = d, p = e(this), f = e.effects.setMode(p, t.mode || "hide"), m = "show" === f, g = p.show().css("visibility", "hidden").offset(), v = Math.ceil(p.outerWidth() / c), y = Math.ceil(p.outerHeight() / d), b = [];
        for (a = 0; d > a; a++) for (h = g.top + a * y, u = a - (d - 1) / 2, o = 0; c > o; o++) r = g.left + o * v, 
        l = o - (c - 1) / 2, p.clone().appendTo("body").wrap("<div></div>").css({
            position: "absolute",
            visibility: "visible",
            left: -o * v,
            top: -a * y
        }).parent().addClass("ui-effects-explode").css({
            position: "absolute",
            overflow: "hidden",
            width: v,
            height: y,
            left: r + (m ? l * v : 0),
            top: h + (m ? u * y : 0),
            opacity: m ? 0 : 1
        }).animate({
            left: r + (m ? 0 : l * v),
            top: h + (m ? 0 : u * y),
            opacity: m ? 1 : 0
        }, t.duration || 500, t.easing, s);
    }, e.effects.effect.fade = function(t, i) {
        var s = e(this), n = e.effects.setMode(s, t.mode || "toggle");
        s.animate({
            opacity: n
        }, {
            queue: !1,
            duration: t.duration,
            easing: t.easing,
            complete: i
        });
    }, e.effects.effect.fold = function(t, i) {
        var s, n, a = e(this), o = [ "position", "top", "bottom", "left", "right", "height", "width" ], r = e.effects.setMode(a, t.mode || "hide"), h = "show" === r, l = "hide" === r, u = t.size || 15, d = /([0-9]+)%/.exec(u), c = !!t.horizFirst, p = h !== c, f = p ? [ "width", "height" ] : [ "height", "width" ], m = t.duration / 2, g = {}, v = {};
        e.effects.save(a, o), a.show(), s = e.effects.createWrapper(a).css({
            overflow: "hidden"
        }), n = p ? [ s.width(), s.height() ] : [ s.height(), s.width() ], d && (u = parseInt(d[1], 10) / 100 * n[l ? 0 : 1]), 
        h && s.css(c ? {
            height: 0,
            width: u
        } : {
            height: u,
            width: 0
        }), g[f[0]] = h ? n[0] : u, v[f[1]] = h ? n[1] : 0, s.animate(g, m, t.easing).animate(v, m, t.easing, function() {
            l && a.hide(), e.effects.restore(a, o), e.effects.removeWrapper(a), i();
        });
    }, e.effects.effect.highlight = function(t, i) {
        var s = e(this), n = [ "backgroundImage", "backgroundColor", "opacity" ], a = e.effects.setMode(s, t.mode || "show"), o = {
            backgroundColor: s.css("backgroundColor")
        };
        "hide" === a && (o.opacity = 0), e.effects.save(s, n), s.show().css({
            backgroundImage: "none",
            backgroundColor: t.color || "#ffff99"
        }).animate(o, {
            queue: !1,
            duration: t.duration,
            easing: t.easing,
            complete: function() {
                "hide" === a && s.hide(), e.effects.restore(s, n), i();
            }
        });
    }, e.effects.effect.size = function(t, i) {
        var s, n, a, o = e(this), r = [ "position", "top", "bottom", "left", "right", "width", "height", "overflow", "opacity" ], h = [ "position", "top", "bottom", "left", "right", "overflow", "opacity" ], l = [ "width", "height", "overflow" ], u = [ "fontSize" ], d = [ "borderTopWidth", "borderBottomWidth", "paddingTop", "paddingBottom" ], c = [ "borderLeftWidth", "borderRightWidth", "paddingLeft", "paddingRight" ], p = e.effects.setMode(o, t.mode || "effect"), f = t.restore || "effect" !== p, m = t.scale || "both", g = t.origin || [ "middle", "center" ], v = o.css("position"), y = f ? r : h, b = {
            height: 0,
            width: 0,
            outerHeight: 0,
            outerWidth: 0
        };
        "show" === p && o.show(), s = {
            height: o.height(),
            width: o.width(),
            outerHeight: o.outerHeight(),
            outerWidth: o.outerWidth()
        }, "toggle" === t.mode && "show" === p ? (o.from = t.to || b, o.to = t.from || s) : (o.from = t.from || ("show" === p ? b : s), 
        o.to = t.to || ("hide" === p ? b : s)), a = {
            from: {
                y: o.from.height / s.height,
                x: o.from.width / s.width
            },
            to: {
                y: o.to.height / s.height,
                x: o.to.width / s.width
            }
        }, ("box" === m || "both" === m) && (a.from.y !== a.to.y && (y = y.concat(d), o.from = e.effects.setTransition(o, d, a.from.y, o.from), 
        o.to = e.effects.setTransition(o, d, a.to.y, o.to)), a.from.x !== a.to.x && (y = y.concat(c), 
        o.from = e.effects.setTransition(o, c, a.from.x, o.from), o.to = e.effects.setTransition(o, c, a.to.x, o.to))), 
        ("content" === m || "both" === m) && a.from.y !== a.to.y && (y = y.concat(u).concat(l), 
        o.from = e.effects.setTransition(o, u, a.from.y, o.from), o.to = e.effects.setTransition(o, u, a.to.y, o.to)), 
        e.effects.save(o, y), o.show(), e.effects.createWrapper(o), o.css("overflow", "hidden").css(o.from), 
        g && (n = e.effects.getBaseline(g, s), o.from.top = (s.outerHeight - o.outerHeight()) * n.y, 
        o.from.left = (s.outerWidth - o.outerWidth()) * n.x, o.to.top = (s.outerHeight - o.to.outerHeight) * n.y, 
        o.to.left = (s.outerWidth - o.to.outerWidth) * n.x), o.css(o.from), ("content" === m || "both" === m) && (d = d.concat([ "marginTop", "marginBottom" ]).concat(u), 
        c = c.concat([ "marginLeft", "marginRight" ]), l = r.concat(d).concat(c), o.find("*[width]").each(function() {
            var i = e(this), s = {
                height: i.height(),
                width: i.width(),
                outerHeight: i.outerHeight(),
                outerWidth: i.outerWidth()
            };
            f && e.effects.save(i, l), i.from = {
                height: s.height * a.from.y,
                width: s.width * a.from.x,
                outerHeight: s.outerHeight * a.from.y,
                outerWidth: s.outerWidth * a.from.x
            }, i.to = {
                height: s.height * a.to.y,
                width: s.width * a.to.x,
                outerHeight: s.height * a.to.y,
                outerWidth: s.width * a.to.x
            }, a.from.y !== a.to.y && (i.from = e.effects.setTransition(i, d, a.from.y, i.from), 
            i.to = e.effects.setTransition(i, d, a.to.y, i.to)), a.from.x !== a.to.x && (i.from = e.effects.setTransition(i, c, a.from.x, i.from), 
            i.to = e.effects.setTransition(i, c, a.to.x, i.to)), i.css(i.from), i.animate(i.to, t.duration, t.easing, function() {
                f && e.effects.restore(i, l);
            });
        })), o.animate(o.to, {
            queue: !1,
            duration: t.duration,
            easing: t.easing,
            complete: function() {
                0 === o.to.opacity && o.css("opacity", o.from.opacity), "hide" === p && o.hide(), 
                e.effects.restore(o, y), f || ("static" === v ? o.css({
                    position: "relative",
                    top: o.to.top,
                    left: o.to.left
                }) : e.each([ "top", "left" ], function(e, t) {
                    o.css(t, function(t, i) {
                        var s = parseInt(i, 10), n = e ? o.to.left : o.to.top;
                        return "auto" === i ? n + "px" : s + n + "px";
                    });
                })), e.effects.removeWrapper(o), i();
            }
        });
    }, e.effects.effect.scale = function(t, i) {
        var s = e(this), n = e.extend(!0, {}, t), a = e.effects.setMode(s, t.mode || "effect"), o = parseInt(t.percent, 10) || (0 === parseInt(t.percent, 10) ? 0 : "hide" === a ? 0 : 100), r = t.direction || "both", h = t.origin, l = {
            height: s.height(),
            width: s.width(),
            outerHeight: s.outerHeight(),
            outerWidth: s.outerWidth()
        }, u = {
            y: "horizontal" !== r ? o / 100 : 1,
            x: "vertical" !== r ? o / 100 : 1
        };
        n.effect = "size", n.queue = !1, n.complete = i, "effect" !== a && (n.origin = h || [ "middle", "center" ], 
        n.restore = !0), n.from = t.from || ("show" === a ? {
            height: 0,
            width: 0,
            outerHeight: 0,
            outerWidth: 0
        } : l), n.to = {
            height: l.height * u.y,
            width: l.width * u.x,
            outerHeight: l.outerHeight * u.y,
            outerWidth: l.outerWidth * u.x
        }, n.fade && ("show" === a && (n.from.opacity = 0, n.to.opacity = 1), "hide" === a && (n.from.opacity = 1, 
        n.to.opacity = 0)), s.effect(n);
    }, e.effects.effect.puff = function(t, i) {
        var s = e(this), n = e.effects.setMode(s, t.mode || "hide"), a = "hide" === n, o = parseInt(t.percent, 10) || 150, r = o / 100, h = {
            height: s.height(),
            width: s.width(),
            outerHeight: s.outerHeight(),
            outerWidth: s.outerWidth()
        };
        e.extend(t, {
            effect: "scale",
            queue: !1,
            fade: !0,
            mode: n,
            complete: i,
            percent: a ? o : 100,
            from: a ? h : {
                height: h.height * r,
                width: h.width * r,
                outerHeight: h.outerHeight * r,
                outerWidth: h.outerWidth * r
            }
        }), s.effect(t);
    }, e.effects.effect.pulsate = function(t, i) {
        var s, n = e(this), a = e.effects.setMode(n, t.mode || "show"), o = "show" === a, r = "hide" === a, h = o || "hide" === a, l = 2 * (t.times || 5) + (h ? 1 : 0), u = t.duration / l, d = 0, c = n.queue(), p = c.length;
        for ((o || !n.is(":visible")) && (n.css("opacity", 0).show(), d = 1), s = 1; l > s; s++) n.animate({
            opacity: d
        }, u, t.easing), d = 1 - d;
        n.animate({
            opacity: d
        }, u, t.easing), n.queue(function() {
            r && n.hide(), i();
        }), p > 1 && c.splice.apply(c, [ 1, 0 ].concat(c.splice(p, l + 1))), n.dequeue();
    }, e.effects.effect.shake = function(t, i) {
        var s, n = e(this), a = [ "position", "top", "bottom", "left", "right", "height", "width" ], o = e.effects.setMode(n, t.mode || "effect"), r = t.direction || "left", h = t.distance || 20, l = t.times || 3, u = 2 * l + 1, d = Math.round(t.duration / u), c = "up" === r || "down" === r ? "top" : "left", p = "up" === r || "left" === r, f = {}, m = {}, g = {}, v = n.queue(), y = v.length;
        for (e.effects.save(n, a), n.show(), e.effects.createWrapper(n), f[c] = (p ? "-=" : "+=") + h, 
        m[c] = (p ? "+=" : "-=") + 2 * h, g[c] = (p ? "-=" : "+=") + 2 * h, n.animate(f, d, t.easing), 
        s = 1; l > s; s++) n.animate(m, d, t.easing).animate(g, d, t.easing);
        n.animate(m, d, t.easing).animate(f, d / 2, t.easing).queue(function() {
            "hide" === o && n.hide(), e.effects.restore(n, a), e.effects.removeWrapper(n), i();
        }), y > 1 && v.splice.apply(v, [ 1, 0 ].concat(v.splice(y, u + 1))), n.dequeue();
    }, e.effects.effect.slide = function(t, i) {
        var s, n = e(this), a = [ "position", "top", "bottom", "left", "right", "width", "height" ], o = e.effects.setMode(n, t.mode || "show"), r = "show" === o, h = t.direction || "left", l = "up" === h || "down" === h ? "top" : "left", u = "up" === h || "left" === h, d = {};
        e.effects.save(n, a), n.show(), s = t.distance || n["top" === l ? "outerHeight" : "outerWidth"](!0), 
        e.effects.createWrapper(n).css({
            overflow: "hidden"
        }), r && n.css(l, u ? isNaN(s) ? "-" + s : -s : s), d[l] = (r ? u ? "+=" : "-=" : u ? "-=" : "+=") + s, 
        n.animate(d, {
            queue: !1,
            duration: t.duration,
            easing: t.easing,
            complete: function() {
                "hide" === o && n.hide(), e.effects.restore(n, a), e.effects.removeWrapper(n), i();
            }
        });
    }, e.effects.effect.transfer = function(t, i) {
        var s = e(this), n = e(t.to), a = "fixed" === n.css("position"), o = e("body"), r = a ? o.scrollTop() : 0, h = a ? o.scrollLeft() : 0, l = n.offset(), u = {
            top: l.top - r,
            left: l.left - h,
            height: n.innerHeight(),
            width: n.innerWidth()
        }, d = s.offset(), c = e("<div class='ui-effects-transfer'></div>").appendTo(document.body).addClass(t.className).css({
            top: d.top - r,
            left: d.left - h,
            height: s.innerHeight(),
            width: s.innerWidth(),
            position: a ? "fixed" : "absolute"
        }).animate(u, t.duration, t.easing, function() {
            c.remove(), i();
        });
    };
}), function(a) {
    "use strict";
    !function(a, b, c, d) {
        a.widget("selectBox.selectBoxIt", {
            VERSION: "3.8.0",
            options: {
                showEffect: "none",
                showEffectOptions: {},
                showEffectSpeed: "medium",
                hideEffect: "none",
                hideEffectOptions: {},
                hideEffectSpeed: "medium",
                showFirstOption: !0,
                defaultText: "",
                defaultIcon: "",
                downArrowIcon: "",
                theme: "default",
                keydownOpen: !0,
                isMobile: function() {
                    return /iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/.test(navigator.userAgent || navigator.vendor || b.opera);
                },
                native: !1,
                aggressiveChange: !1,
                selectWhenHidden: !0,
                viewport: a(b),
                similarSearch: !1,
                copyAttributes: [ "title", "rel" ],
                copyClasses: "button",
                nativeMousedown: !1,
                customShowHideEvent: !1,
                autoWidth: !0,
                html: !0,
                populate: "",
                dynamicPositioning: !0,
                hideCurrent: !1
            },
            getThemes: function() {
                var b = this, c = a(b.element).attr("data-theme") || "c";
                return {
                    bootstrap: {
                        focus: "active",
                        hover: "",
                        enabled: "enabled",
                        disabled: "disabled",
                        arrow: "caret",
                        button: "btn",
                        list: "dropdown-menu",
                        container: "bootstrap",
                        open: "open"
                    },
                    jqueryui: {
                        focus: "ui-state-focus",
                        hover: "ui-state-hover",
                        enabled: "ui-state-enabled",
                        disabled: "ui-state-disabled",
                        arrow: "ui-icon ui-icon-triangle-1-s",
                        button: "ui-widget ui-state-default",
                        list: "ui-widget ui-widget-content",
                        container: "jqueryui",
                        open: "selectboxit-open"
                    },
                    jquerymobile: {
                        focus: "ui-btn-down-" + c,
                        hover: "ui-btn-hover-" + c,
                        enabled: "ui-enabled",
                        disabled: "ui-disabled",
                        arrow: "ui-icon ui-icon-arrow-d ui-icon-shadow",
                        button: "ui-btn ui-btn-icon-right ui-btn-corner-all ui-shadow ui-btn-up-" + c,
                        list: "ui-btn ui-btn-icon-right ui-btn-corner-all ui-shadow ui-btn-up-" + c,
                        container: "jquerymobile",
                        open: "selectboxit-open"
                    },
                    default: {
                        focus: "selectboxit-focus",
                        hover: "selectboxit-hover",
                        enabled: "selectboxit-enabled",
                        disabled: "selectboxit-disabled",
                        arrow: "selectboxit-default-arrow",
                        button: "selectboxit-btn",
                        list: "selectboxit-list",
                        container: "selectboxit-container",
                        open: "selectboxit-open"
                    }
                };
            },
            isDeferred: function(b) {
                return a.isPlainObject(b) && b.promise && b.done;
            },
            _create: function(b) {
                var d = this, e = d.options.populate, f = d.options.theme;
                if (d.element.is("select")) return d.widgetProto = a.Widget.prototype, d.originalElem = d.element[0], 
                d.selectBox = d.element, d.options.populate && d.add && !b && d.add(e), d.selectItems = d.element.find("option"), 
                d.firstSelectItem = d.selectItems.slice(0, 1), d.documentHeight = a(c).height(), 
                d.theme = a.isPlainObject(f) ? a.extend({}, d.getThemes().default, f) : d.getThemes()[f] ? d.getThemes()[f] : d.getThemes().default, 
                d.currentFocus = 0, d.blur = !0, d.textArray = [], d.currentIndex = 0, d.currentText = "", 
                d.flipped = !1, b || (d.selectBoxStyles = d.selectBox.attr("style")), d._createDropdownButton()._createUnorderedList()._copyAttributes()._replaceSelectBox()._addClasses(d.theme)._eventHandlers(), 
                d.originalElem.disabled && d.disable && d.disable(), d._ariaAccessibility && d._ariaAccessibility(), 
                d.isMobile = d.options.isMobile(), d._mobile && d._mobile(), d.options.native && this._applyNativeSelect(), 
                d.triggerEvent("create"), d;
            },
            _createDropdownButton: function() {
                var b = this, c = b.originalElemId = b.originalElem.id || "", d = b.originalElemValue = b.originalElem.value || "", e = b.originalElemName = b.originalElem.name || "", f = b.options.copyClasses, g = b.selectBox.attr("class") || "";
                return b.dropdownText = a("<span/>", {
                    id: c && c + "SelectBoxItText",
                    class: "selectboxit-text",
                    unselectable: "on",
                    text: b.firstSelectItem.text()
                }).attr("data-val", d), b.dropdownImageContainer = a("<span/>", {
                    class: "selectboxit-option-icon-container"
                }), b.dropdownImage = a("<i/>", {
                    id: c && c + "SelectBoxItDefaultIcon",
                    class: "selectboxit-default-icon",
                    unselectable: "on"
                }), b.dropdown = a("<span/>", {
                    id: c && c + "SelectBoxIt",
                    class: "selectboxit " + ("button" === f ? g : "") + " " + (b.selectBox.prop("disabled") ? b.theme.disabled : b.theme.enabled),
                    name: e,
                    tabindex: b.selectBox.attr("tabindex") || "0",
                    unselectable: "on"
                }).append(b.dropdownImageContainer.append(b.dropdownImage)).append(b.dropdownText), 
                b.dropdownContainer = a("<span/>", {
                    id: c && c + "SelectBoxItContainer",
                    class: "selectboxit-container " + b.theme.container + " " + ("container" === f ? g : "")
                }).append(b.dropdown), b;
            },
            _createUnorderedList: function() {
                var b, c, d, e, f, g, h, i, j, k, l, m, n, o = this, p = "", q = o.originalElemId || "", r = a("<ul/>", {
                    id: q && q + "SelectBoxItOptions",
                    class: "selectboxit-options",
                    tabindex: -1
                });
                if (o.options.showFirstOption || (o.selectItems.first().attr("disabled", "disabled"), 
                o.selectItems = o.selectBox.find("option").slice(1)), o.selectItems.each(function(q) {
                    m = a(this), c = "", d = "", b = m.prop("disabled"), e = m.attr("data-icon") || "", 
                    f = m.attr("data-iconurl") || "", g = f ? "selectboxit-option-icon-url" : "", h = f ? "style=\"background-image:url('" + f + "');\"" : "", 
                    i = m.attr("data-selectedtext"), j = m.attr("data-text"), l = j || m.text(), n = m.parent(), 
                    n.is("optgroup") && (c = "selectboxit-optgroup-option", 0 === m.index() && (d = '<span class="selectboxit-optgroup-header ' + n.first().attr("class") + '"data-disabled="true">' + n.first().attr("label") + "</span>")), 
                    p += d + '<li data-id="' + q + '" data-val="' + this.value + '" data-disabled="' + b + '" class="' + c + " selectboxit-option " + (a(this).attr("class") || "") + '"><a class="selectboxit-option-anchor"><span class="selectboxit-option-icon-container"><i class="selectboxit-option-icon ' + e + " " + (g || o.theme.container) + '"' + h + "></i></span>" + (o.options.html ? l : o.htmlEscape(l)) + "</a></li>", 
                    k = m.attr("data-search"), o.textArray[q] = b ? "" : k || l, this.selected && (o._setText(o.dropdownText, i || l), 
                    o.currentFocus = q);
                }), o.options.defaultText || o.selectBox.attr("data-text")) {
                    var s = o.options.defaultText || o.selectBox.attr("data-text");
                    o._setText(o.dropdownText, s), o.options.defaultText = s;
                }
                return r.append(p), o.list = r, o.dropdownContainer.append(o.list), o.listItems = o.list.children("li"), 
                o.listAnchors = o.list.find("a"), o.listItems.first().addClass("selectboxit-option-first"), 
                o.listItems.last().addClass("selectboxit-option-last"), o.list.find("li[data-disabled='true']").not(".optgroupHeader").addClass(o.theme.disabled), 
                o.dropdownImage.addClass(o.selectBox.attr("data-icon") || o.options.defaultIcon || o.listItems.eq(o.currentFocus).find("i").attr("class")), 
                o.dropdownImage.attr("style", o.listItems.eq(o.currentFocus).find("i").attr("style")), 
                o;
            },
            _replaceSelectBox: function() {
                var c, e, f = this, g = f.originalElem.id || "", h = f.selectBox.attr("data-size"), i = f.listSize = h === d ? "auto" : "0" === h ? "auto" : +h;
                return f.selectBox.css("display", "none").after(f.dropdownContainer), f.dropdownContainer.appendTo("body").addClass("selectboxit-rendering"), 
                f.dropdown.height(), f.downArrow = a("<i/>", {
                    id: g && g + "SelectBoxItArrow",
                    class: "selectboxit-arrow",
                    unselectable: "on"
                }), f.downArrowContainer = a("<span/>", {
                    id: g && g + "SelectBoxItArrowContainer",
                    class: "selectboxit-arrow-container",
                    unselectable: "on"
                }).append(f.downArrow), f.dropdown.append(f.downArrowContainer), f.listItems.removeClass("selectboxit-selected").eq(f.currentFocus).addClass("selectboxit-selected"), 
                c = f.downArrowContainer.outerWidth(!0), e = f.dropdownImage.outerWidth(!0), f.options.autoWidth && (f.dropdown.css({
                    width: "auto"
                }).css({
                    width: f.list.outerWidth(!0) + c + e
                }), f.list.css({
                    "min-width": f.dropdown.width()
                })), f.dropdownText.css({
                    "max-width": f.dropdownContainer.outerWidth(!0) - (c + e)
                }), f.selectBox.after(f.dropdownContainer), f.dropdownContainer.removeClass("selectboxit-rendering"), 
                "number" === a.type(i) && (f.maxHeight = f.listAnchors.outerHeight(!0) * i), f;
            },
            _scrollToView: function(a) {
                var b = this, c = b.listItems.eq(b.currentFocus), d = b.list.scrollTop(), e = c.height(), f = c.position().top, g = Math.abs(f), h = b.list.height();
                return "search" === a ? e > h - f ? b.list.scrollTop(d + (f - (h - e))) : -1 > f && b.list.scrollTop(f - e) : "up" === a ? -1 > f && b.list.scrollTop(d - g) : "down" === a && e > h - f && b.list.scrollTop(d + (g - h + e)), 
                b;
            },
            _callbackSupport: function(b) {
                var c = this;
                return a.isFunction(b) && b.call(c, c.dropdown), c;
            },
            _setText: function(a, b) {
                var c = this;
                return c.options.html ? a.html(b) : a.text(b), c;
            },
            open: function(a) {
                var b = this, c = b.options.showEffect, d = b.options.showEffectSpeed, e = b.options.showEffectOptions, f = b.options.native, g = b.isMobile;
                return !b.listItems.length || b.dropdown.hasClass(b.theme.disabled) ? b : (f || g || this.list.is(":visible") || (b.triggerEvent("open"), 
                b._dynamicPositioning && b.options.dynamicPositioning && b._dynamicPositioning(), 
                "none" === c ? b.list.show() : "show" === c || "slideDown" === c || "fadeIn" === c ? b.list[c](d) : b.list.show(c, e, d), 
                b.list.promise().done(function() {
                    b._scrollToView("search");
                })), b._callbackSupport(a), b);
            },
            close: function(a) {
                var b = this, c = b.options.hideEffect, d = b.options.hideEffectSpeed, e = b.options.hideEffectOptions, f = b.options.native, g = b.isMobile;
                return f || g || !b.list.is(":visible") || (b.triggerEvent("close"), "none" === c ? b.list.hide() : "hide" === c || "slideUp" === c || "fadeOut" === c ? b.list[c](d) : b.list.hide(c, e, d)), 
                b._callbackSupport(a), b;
            },
            toggle: function() {
                var a = this, b = a.list.is(":visible");
                b ? a.close() : b || a.open();
            },
            _keyMappings: {
                38: "up",
                40: "down",
                13: "enter",
                8: "backspace",
                9: "tab",
                32: "space",
                27: "esc"
            },
            _keydownMethods: function() {
                var a = this, b = a.list.is(":visible") || !a.options.keydownOpen;
                return {
                    down: function() {
                        a.moveDown && b && a.moveDown();
                    },
                    up: function() {
                        a.moveUp && b && a.moveUp();
                    },
                    enter: function() {
                        var b = a.listItems.eq(a.currentFocus);
                        a._update(b), "true" !== b.attr("data-preventclose") && a.close(), a.triggerEvent("enter");
                    },
                    tab: function() {
                        a.triggerEvent("tab-blur"), a.close();
                    },
                    backspace: function() {
                        a.triggerEvent("backspace");
                    },
                    esc: function() {
                        a.close();
                    }
                };
            },
            _eventHandlers: function() {
                var b, c, d = this, e = d.options.nativeMousedown, f = d.options.customShowHideEvent, g = d.focusClass, h = d.hoverClass, i = d.openClass;
                return this.dropdown.on({
                    "click.selectBoxIt": function() {
                        d.dropdown.trigger("focus", !0), d.originalElem.disabled || (d.triggerEvent("click"), 
                        e || f || d.toggle());
                    },
                    "mousedown.selectBoxIt": function() {
                        a(this).data("mdown", !0), d.triggerEvent("mousedown"), e && !f && d.toggle();
                    },
                    "mouseup.selectBoxIt": function() {
                        d.triggerEvent("mouseup");
                    },
                    "blur.selectBoxIt": function() {
                        d.blur && (d.triggerEvent("blur"), d.close(), a(this).removeClass(g));
                    },
                    "focus.selectBoxIt": function(b, c) {
                        var e = a(this).data("mdown");
                        a(this).removeData("mdown"), e || c || setTimeout(function() {
                            d.triggerEvent("tab-focus");
                        }, 0), c || (a(this).hasClass(d.theme.disabled) || a(this).addClass(g), d.triggerEvent("focus"));
                    },
                    "keydown.selectBoxIt": function(a) {
                        var b = d._keyMappings[a.keyCode], c = d._keydownMethods()[b];
                        c && (c(), !d.options.keydownOpen || "up" !== b && "down" !== b || d.open()), c && "tab" !== b && a.preventDefault();
                    },
                    "keypress.selectBoxIt": function(a) {
                        var b = a.charCode || a.keyCode, c = d._keyMappings[a.charCode || a.keyCode], e = String.fromCharCode(b);
                        d.search && (!c || c && "space" === c) && d.search(e, !0, !0), "space" === c && a.preventDefault();
                    },
                    "mouseenter.selectBoxIt": function() {
                        d.triggerEvent("mouseenter");
                    },
                    "mouseleave.selectBoxIt": function() {
                        d.triggerEvent("mouseleave");
                    }
                }), d.list.on({
                    "mouseover.selectBoxIt": function() {
                        d.blur = !1;
                    },
                    "mouseout.selectBoxIt": function() {
                        d.blur = !0;
                    },
                    "focusin.selectBoxIt": function() {
                        d.dropdown.trigger("focus", !0);
                    }
                }), d.list.on({
                    "mousedown.selectBoxIt": function() {
                        d._update(a(this)), d.triggerEvent("option-click"), "false" === a(this).attr("data-disabled") && "true" !== a(this).attr("data-preventclose") && d.close(), 
                        setTimeout(function() {
                            d.dropdown.trigger("focus", !0);
                        }, 0);
                    },
                    "focusin.selectBoxIt": function() {
                        d.listItems.not(a(this)).removeAttr("data-active"), a(this).attr("data-active", "");
                        var b = d.list.is(":hidden");
                        (d.options.searchWhenHidden && b || d.options.aggressiveChange || b && d.options.selectWhenHidden) && d._update(a(this)), 
                        a(this).addClass(g);
                    },
                    "mouseup.selectBoxIt": function() {
                        e && !f && (d._update(a(this)), d.triggerEvent("option-mouseup"), "false" === a(this).attr("data-disabled") && "true" !== a(this).attr("data-preventclose") && d.close());
                    },
                    "mouseenter.selectBoxIt": function() {
                        "false" === a(this).attr("data-disabled") && (d.listItems.removeAttr("data-active"), 
                        a(this).addClass(g).attr("data-active", ""), d.listItems.not(a(this)).removeClass(g), 
                        a(this).addClass(g), d.currentFocus = +a(this).attr("data-id"));
                    },
                    "mouseleave.selectBoxIt": function() {
                        "false" === a(this).attr("data-disabled") && (d.listItems.not(a(this)).removeClass(g).removeAttr("data-active"), 
                        a(this).addClass(g), d.currentFocus = +a(this).attr("data-id"));
                    },
                    "blur.selectBoxIt": function() {
                        a(this).removeClass(g);
                    }
                }, ".selectboxit-option"), d.list.on({
                    "click.selectBoxIt": function(a) {
                        a.preventDefault();
                    }
                }, "a"), d.selectBox.on({
                    "change.selectBoxIt, internal-change.selectBoxIt": function(a, e) {
                        var f, g;
                        e || (f = d.list.find('li[data-val="' + d.originalElem.value + '"]'), f.length && (d.listItems.eq(d.currentFocus).removeClass(d.focusClass), 
                        d.currentFocus = +f.attr("data-id"))), f = d.listItems.eq(d.currentFocus), g = f.attr("data-selectedtext"), 
                        b = f.attr("data-text"), c = b || f.find("a").text(), d._setText(d.dropdownText, g || c), 
                        d.dropdownText.attr("data-val", d.originalElem.value), f.find("i").attr("class") && (d.dropdownImage.attr("class", f.find("i").attr("class")).addClass("selectboxit-default-icon"), 
                        d.dropdownImage.attr("style", f.find("i").attr("style"))), d.triggerEvent("changed");
                    },
                    "disable.selectBoxIt": function() {
                        d.dropdown.addClass(d.theme.disabled);
                    },
                    "enable.selectBoxIt": function() {
                        d.dropdown.removeClass(d.theme.disabled);
                    },
                    "open.selectBoxIt": function() {
                        var a, b = d.list.find("li[data-val='" + d.dropdownText.attr("data-val") + "']");
                        b.length || (b = d.listItems.not("[data-disabled=true]").first()), d.currentFocus = +b.attr("data-id"), 
                        a = d.listItems.eq(d.currentFocus), d.dropdown.addClass(i).removeClass(h).addClass(g), 
                        d.listItems.removeClass(d.selectedClass).removeAttr("data-active").not(a).removeClass(g), 
                        a.addClass(d.selectedClass).addClass(g), d.options.hideCurrent && (d.listItems.show(), 
                        a.hide());
                    },
                    "close.selectBoxIt": function() {
                        d.dropdown.removeClass(i);
                    },
                    "blur.selectBoxIt": function() {
                        d.dropdown.removeClass(g);
                    },
                    "mouseenter.selectBoxIt": function() {
                        a(this).hasClass(d.theme.disabled) || d.dropdown.addClass(h);
                    },
                    "mouseleave.selectBoxIt": function() {
                        d.dropdown.removeClass(h);
                    },
                    destroy: function(a) {
                        a.preventDefault(), a.stopPropagation();
                    }
                }), d;
            },
            _update: function(a) {
                var c, e = this, f = e.options.defaultText || e.selectBox.attr("data-text"), g = e.listItems.eq(e.currentFocus);
                "false" === a.attr("data-disabled") && (e.listItems.eq(e.currentFocus).attr("data-selectedtext"), 
                c = g.attr("data-text"), c || g.text(), (f && e.options.html ? e.dropdownText.html() === f : e.dropdownText.text() === f) && e.selectBox.val() === a.attr("data-val") ? e.triggerEvent("change") : (e.selectBox.val(a.attr("data-val")), 
                e.currentFocus = +a.attr("data-id"), e.originalElem.value !== e.dropdownText.attr("data-val") && e.triggerEvent("change")));
            },
            _addClasses: function(a) {
                var b = this, c = (b.focusClass = a.focus, b.hoverClass = a.hover, a.button), d = a.list, e = a.arrow, f = a.container;
                return b.openClass = a.open, b.selectedClass = "selectboxit-selected", b.downArrow.addClass(b.selectBox.attr("data-downarrow") || b.options.downArrowIcon || e), 
                b.dropdownContainer.addClass(f), b.dropdown.addClass(c), b.list.addClass(d), b;
            },
            refresh: function(a, b) {
                var c = this;
                return c._destroySelectBoxIt()._create(!0), b || c.triggerEvent("refresh"), c._callbackSupport(a), 
                c;
            },
            htmlEscape: function(a) {
                return String(a).replace(/&/g, "&amp;").replace(/"/g, "&quot;").replace(/'/g, "&#39;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
            },
            triggerEvent: function(a) {
                var b = this, c = b.options.showFirstOption ? b.currentFocus : b.currentFocus - 1 >= 0 ? b.currentFocus : 0;
                return b.selectBox.trigger(a, {
                    selectbox: b.selectBox,
                    selectboxOption: b.selectItems.eq(c),
                    dropdown: b.dropdown,
                    dropdownOption: b.listItems.eq(b.currentFocus)
                }), b;
            },
            _copyAttributes: function() {
                var a = this;
                return a._addSelectBoxAttributes && a._addSelectBoxAttributes(), a;
            },
            _realOuterWidth: function(a) {
                if (a.is(":visible")) return a.outerWidth(!0);
                var b, c = a.clone();
                return c.css({
                    visibility: "hidden",
                    display: "block",
                    position: "absolute"
                }).appendTo("body"), b = c.outerWidth(!0), c.remove(), b;
            }
        });
        var e = a.selectBox.selectBoxIt.prototype;
        e.add = function(b, c) {
            this._populate(b, function(b) {
                var d, e, f = this, g = a.type(b), h = 0, i = [], j = f._isJSON(b), k = j && f._parseJSON(b);
                if (b && ("array" === g || j && k.data && "array" === a.type(k.data)) || "object" === g && b.data && "array" === a.type(b.data)) {
                    for (f._isJSON(b) && (b = k), b.data && (b = b.data), e = b.length; e - 1 >= h; h += 1) d = b[h], 
                    a.isPlainObject(d) ? i.push(a("<option/>", d)) : "string" === a.type(d) && i.push(a("<option/>", {
                        text: d,
                        value: d
                    }));
                    f.selectBox.append(i);
                } else b && "string" === g && !f._isJSON(b) ? f.selectBox.append(b) : b && "object" === g ? f.selectBox.append(a("<option/>", b)) : b && f._isJSON(b) && a.isPlainObject(f._parseJSON(b)) && f.selectBox.append(a("<option/>", f._parseJSON(b)));
                return f.dropdown ? f.refresh(function() {
                    f._callbackSupport(c);
                }, !0) : f._callbackSupport(c), f;
            });
        }, e._parseJSON = function(b) {
            return JSON && JSON.parse && JSON.parse(b) || a.parseJSON(b);
        }, e._isJSON = function(a) {
            var c = this;
            try {
                return c._parseJSON(a), !0;
            } catch (d) {
                return !1;
            }
        }, e._populate = function(b, c) {
            var d = this;
            return b = a.isFunction(b) ? b.call() : b, d.isDeferred(b) ? b.done(function(a) {
                c.call(d, a);
            }) : c.call(d, b), d;
        }, e._ariaAccessibility = function() {
            var b = this, c = a("label[for='" + b.originalElem.id + "']");
            return b.dropdownContainer.attr({
                role: "combobox",
                "aria-autocomplete": "list",
                "aria-haspopup": "true",
                "aria-expanded": "false",
                "aria-owns": b.list[0].id
            }), b.dropdownText.attr({
                "aria-live": "polite"
            }), b.dropdown.on({
                "disable.selectBoxIt": function() {
                    b.dropdownContainer.attr("aria-disabled", "true");
                },
                "enable.selectBoxIt": function() {
                    b.dropdownContainer.attr("aria-disabled", "false");
                }
            }), c.length && b.dropdownContainer.attr("aria-labelledby", c[0].id), b.list.attr({
                role: "listbox",
                "aria-hidden": "true"
            }), b.listItems.attr({
                role: "option"
            }), b.selectBox.on({
                "open.selectBoxIt": function() {
                    b.list.attr("aria-hidden", "false"), b.dropdownContainer.attr("aria-expanded", "true");
                },
                "close.selectBoxIt": function() {
                    b.list.attr("aria-hidden", "true"), b.dropdownContainer.attr("aria-expanded", "false");
                }
            }), b;
        }, e._addSelectBoxAttributes = function() {
            var b = this;
            return b._addAttributes(b.selectBox.prop("attributes"), b.dropdown), b.selectItems.each(function(c) {
                b._addAttributes(a(this).prop("attributes"), b.listItems.eq(c));
            }), b;
        }, e._addAttributes = function(b, c) {
            var d = this, e = d.options.copyAttributes;
            return b.length && a.each(b, function(b, d) {
                var f = d.name.toLowerCase(), g = d.value;
                "null" === g || -1 === a.inArray(f, e) && -1 === f.indexOf("data") || c.attr(f, g);
            }), d;
        }, e.destroy = function(a) {
            var b = this;
            return b._destroySelectBoxIt(), b.widgetProto.destroy.call(b), b._callbackSupport(a), 
            b;
        }, e._destroySelectBoxIt = function() {
            var b = this;
            return b.dropdown.off(".selectBoxIt"), a.contains(b.dropdownContainer[0], b.originalElem) && b.dropdownContainer.before(b.selectBox), 
            b.dropdownContainer.remove(), b.selectBox.removeAttr("style").attr("style", b.selectBoxStyles), 
            b.selectBox.show(), b.triggerEvent("destroy"), b;
        }, e.disable = function(a) {
            var b = this;
            return b.options.disabled || (b.close(), b.selectBox.attr("disabled", "disabled"), 
            b.dropdown.removeAttr("tabindex").removeClass(b.theme.enabled).addClass(b.theme.disabled), 
            b.setOption("disabled", !0), b.triggerEvent("disable")), b._callbackSupport(a), 
            b;
        }, e.disableOption = function(b, c) {
            var d, e, f, g = this;
            return "number" === a.type(b) && (g.close(), d = g.selectBox.find("option").eq(b), 
            g.triggerEvent("disable-option"), d.attr("disabled", "disabled"), g.listItems.eq(b).attr("data-disabled", "true").addClass(g.theme.disabled), 
            g.currentFocus === b && (e = g.listItems.eq(g.currentFocus).nextAll("li").not("[data-disabled='true']").first().length, 
            f = g.listItems.eq(g.currentFocus).prevAll("li").not("[data-disabled='true']").first().length, 
            e ? g.moveDown() : f ? g.moveUp() : g.disable())), g._callbackSupport(c), g;
        }, e._isDisabled = function() {
            var a = this;
            return a.originalElem.disabled && a.disable(), a;
        }, e._dynamicPositioning = function() {
            var b = this;
            if ("number" === a.type(b.listSize)) b.list.css("max-height", b.maxHeight || "none"); else {
                var c = b.dropdown.offset().top, d = b.list.data("max-height") || b.list.outerHeight(), e = b.dropdown.outerHeight(), f = b.options.viewport, g = f.height(), h = a.isWindow(f.get(0)) ? f.scrollTop() : f.offset().top, i = g + h >= c + e + d, j = !i;
                if (b.list.data("max-height") || b.list.data("max-height", b.list.outerHeight()), 
                j) if (b.dropdown.offset().top - h >= d) b.list.css("max-height", d), b.list.css("top", b.dropdown.position().top - b.list.outerHeight()); else {
                    var k = Math.abs(c + e + d - (g + h)), l = Math.abs(b.dropdown.offset().top - h - d);
                    l > k ? (b.list.css("max-height", d - k - e / 2), b.list.css("top", "auto")) : (b.list.css("max-height", d - l - e / 2), 
                    b.list.css("top", b.dropdown.position().top - b.list.outerHeight()));
                } else b.list.css("max-height", d), b.list.css("top", "auto");
            }
            return b;
        }, e.enable = function(a) {
            var b = this;
            return b.options.disabled && (b.triggerEvent("enable"), b.selectBox.removeAttr("disabled"), 
            b.dropdown.attr("tabindex", 0).removeClass(b.theme.disabled).addClass(b.theme.enabled), 
            b.setOption("disabled", !1), b._callbackSupport(a)), b;
        }, e.enableOption = function(b, c) {
            var d, e = this;
            return "number" === a.type(b) && (d = e.selectBox.find("option").eq(b), e.triggerEvent("enable-option"), 
            d.removeAttr("disabled"), e.listItems.eq(b).attr("data-disabled", "false").removeClass(e.theme.disabled)), 
            e._callbackSupport(c), e;
        }, e.moveDown = function(a) {
            var b = this;
            b.currentFocus += 1;
            var c = "true" === b.listItems.eq(b.currentFocus).attr("data-disabled"), d = b.listItems.eq(b.currentFocus).nextAll("li").not("[data-disabled='true']").first().length;
            if (b.currentFocus === b.listItems.length) b.currentFocus -= 1; else {
                if (c && d) return b.listItems.eq(b.currentFocus - 1).blur(), void b.moveDown();
                c && !d ? b.currentFocus -= 1 : (b.listItems.eq(b.currentFocus - 1).blur().end().eq(b.currentFocus).focusin(), 
                b._scrollToView("down"), b.triggerEvent("moveDown"));
            }
            return b._callbackSupport(a), b;
        }, e.moveUp = function(a) {
            var b = this;
            b.currentFocus -= 1;
            var c = "true" === b.listItems.eq(b.currentFocus).attr("data-disabled"), d = b.listItems.eq(b.currentFocus).prevAll("li").not("[data-disabled='true']").first().length;
            if (-1 === b.currentFocus) b.currentFocus += 1; else {
                if (c && d) return b.listItems.eq(b.currentFocus + 1).blur(), void b.moveUp();
                c && !d ? b.currentFocus += 1 : (b.listItems.eq(this.currentFocus + 1).blur().end().eq(b.currentFocus).focusin(), 
                b._scrollToView("up"), b.triggerEvent("moveUp"));
            }
            return b._callbackSupport(a), b;
        }, e._setCurrentSearchOption = function(a) {
            var b = this;
            return (b.options.aggressiveChange || b.options.selectWhenHidden || b.listItems.eq(a).is(":visible")) && !0 !== b.listItems.eq(a).data("disabled") && (b.listItems.eq(b.currentFocus).blur(), 
            b.currentIndex = a, b.currentFocus = a, b.listItems.eq(b.currentFocus).focusin(), 
            b._scrollToView("search"), b.triggerEvent("search")), b;
        }, e._searchAlgorithm = function(a, b) {
            var c, d, e, f, g = this, h = !1, i = g.textArray, j = g.currentText;
            for (c = a, e = i.length; e > c; c += 1) {
                for (f = i[c], d = 0; e > d; d += 1) -1 !== i[d].search(b) && (h = !0, d = e);
                if (h || (g.currentText = g.currentText.charAt(g.currentText.length - 1).replace(/[|()\[{.+*?$\\]/g, "\\$0"), 
                j = g.currentText), b = new RegExp(j, "gi"), j.length < 3) {
                    if (b = new RegExp(j.charAt(0), "gi"), -1 !== f.charAt(0).search(b)) return g._setCurrentSearchOption(c), 
                    (f.substring(0, j.length).toLowerCase() !== j.toLowerCase() || g.options.similarSearch) && (g.currentIndex += 1), 
                    !1;
                } else if (-1 !== f.search(b)) return g._setCurrentSearchOption(c), !1;
                if (f.toLowerCase() === g.currentText.toLowerCase()) return g._setCurrentSearchOption(c), 
                g.currentText = "", !1;
            }
            return !0;
        }, e.search = function(a, b, c) {
            var d = this;
            return c ? d.currentText += a.replace(/[|()\[{.+*?$\\]/g, "\\$0") : d.currentText = a.replace(/[|()\[{.+*?$\\]/g, "\\$0"), 
            d._searchAlgorithm(d.currentIndex, new RegExp(d.currentText, "gi")) && d._searchAlgorithm(0, d.currentText), 
            d._callbackSupport(b), d;
        }, e._updateMobileText = function() {
            var a, b, c, d = this;
            a = d.selectBox.find("option").filter(":selected"), b = a.attr("data-text"), c = b || a.text(), 
            d._setText(d.dropdownText, c), d.list.find('li[data-val="' + a.val() + '"]').find("i").attr("class") && d.dropdownImage.attr("class", d.list.find('li[data-val="' + a.val() + '"]').find("i").attr("class")).addClass("selectboxit-default-icon");
        }, e._applyNativeSelect = function() {
            var a = this;
            return a.dropdownContainer.append(a.selectBox), a.dropdown.attr("tabindex", "-1"), 
            a.selectBox.css({
                display: "block",
                visibility: "visible",
                width: a._realOuterWidth(a.dropdown),
                height: a.dropdown.outerHeight(),
                opacity: "0",
                position: "absolute",
                top: "0",
                left: "0",
                cursor: "pointer",
                "z-index": "999999",
                margin: a.dropdown.css("margin"),
                padding: "0",
                "-webkit-appearance": "menulist-button"
            }), a.originalElem.disabled && a.triggerEvent("disable"), this;
        }, e._mobileEvents = function() {
            var a = this;
            a.selectBox.on({
                "changed.selectBoxIt": function() {
                    a.hasChanged = !0, a._updateMobileText(), a.triggerEvent("option-click");
                },
                "mousedown.selectBoxIt": function() {
                    a.hasChanged || !a.options.defaultText || a.originalElem.disabled || (a._updateMobileText(), 
                    a.triggerEvent("option-click"));
                },
                "enable.selectBoxIt": function() {
                    a.selectBox.removeClass("selectboxit-rendering");
                },
                "disable.selectBoxIt": function() {
                    a.selectBox.addClass("selectboxit-rendering");
                }
            });
        }, e._mobile = function() {
            var a = this;
            return a.isMobile && (a._applyNativeSelect(), a._mobileEvents()), this;
        }, e.remove = function(b, c) {
            var d, e, f = this, g = a.type(b), h = 0, i = "";
            if ("array" === g) {
                for (e = b.length; e - 1 >= h; h += 1) d = b[h], "number" === a.type(d) && (i += i.length ? ", option:eq(" + d + ")" : "option:eq(" + d + ")");
                f.selectBox.find(i).remove();
            } else "number" === g ? f.selectBox.find("option").eq(b).remove() : f.selectBox.find("option").remove();
            return f.dropdown ? f.refresh(function() {
                f._callbackSupport(c);
            }, !0) : f._callbackSupport(c), f;
        }, e.selectOption = function(b, c) {
            var d = this, e = a.type(b);
            return "number" === e ? d.selectBox.val(d.selectItems.eq(b).val()).change() : "string" === e && d.selectBox.val(b).change(), 
            d._callbackSupport(c), d;
        }, e.setOption = function(b, c, d) {
            var e = this;
            return "string" === a.type(b) && (e.options[b] = c), e.refresh(function() {
                e._callbackSupport(d);
            }, !0), e;
        }, e.setOptions = function(b, c) {
            var d = this;
            return a.isPlainObject(b) && (d.options = a.extend({}, d.options, b)), d.refresh(function() {
                d._callbackSupport(c);
            }, !0), d;
        }, e.wait = function(a, b) {
            var c = this;
            return c.widgetProto._delay.call(c, b, a), c;
        };
    }(window.jQuery, window, document);
}(), function(a) {
    "use strict";
    "function" == typeof define && define.amd ? define([ "jquery" ], a) : "undefined" != typeof exports ? module.exports = a(require("jquery")) : a(jQuery);
}(function(a) {
    "use strict";
    var b = window.Slick || {};
    b = function() {
        function c(c, d) {
            var f, e = this;
            e.defaults = {
                accessibility: !0,
                adaptiveHeight: !1,
                appendArrows: a(c),
                appendDots: a(c),
                arrows: !0,
                asNavFor: null,
                prevArrow: '<button type="button" data-role="none" class="slick-prev" aria-label="Previous" tabindex="0" role="button">Previous</button>',
                nextArrow: '<button type="button" data-role="none" class="slick-next" aria-label="Next" tabindex="0" role="button">Next</button>',
                autoplay: !1,
                autoplaySpeed: 3e3,
                centerMode: !1,
                centerPadding: "50px",
                cssEase: "ease",
                customPaging: function(b, c) {
                    return a('<button type="button" data-role="none" role="button" tabindex="0" />').text(c + 1);
                },
                dots: !1,
                dotsClass: "slick-dots",
                draggable: !0,
                easing: "linear",
                edgeFriction: .35,
                fade: !1,
                focusOnSelect: !1,
                infinite: !0,
                initialSlide: 0,
                lazyLoad: "ondemand",
                mobileFirst: !1,
                pauseOnHover: !0,
                pauseOnFocus: !0,
                pauseOnDotsHover: !1,
                respondTo: "window",
                responsive: null,
                rows: 1,
                rtl: !1,
                slide: "",
                slidesPerRow: 1,
                slidesToShow: 1,
                slidesToScroll: 1,
                speed: 500,
                swipe: !0,
                swipeToSlide: !1,
                touchMove: !0,
                touchThreshold: 5,
                useCSS: !0,
                useTransform: !0,
                variableWidth: !1,
                vertical: !1,
                verticalSwiping: !1,
                waitForAnimate: !0,
                zIndex: 1e3
            }, e.initials = {
                animating: !1,
                dragging: !1,
                autoPlayTimer: null,
                currentDirection: 0,
                currentLeft: null,
                currentSlide: 0,
                direction: 1,
                $dots: null,
                listWidth: null,
                listHeight: null,
                loadIndex: 0,
                $nextArrow: null,
                $prevArrow: null,
                slideCount: null,
                slideWidth: null,
                $slideTrack: null,
                $slides: null,
                sliding: !1,
                slideOffset: 0,
                swipeLeft: null,
                $list: null,
                touchObject: {},
                transformsEnabled: !1,
                unslicked: !1
            }, a.extend(e, e.initials), e.activeBreakpoint = null, e.animType = null, e.animProp = null, 
            e.breakpoints = [], e.breakpointSettings = [], e.cssTransitions = !1, e.focussed = !1, 
            e.interrupted = !1, e.hidden = "hidden", e.paused = !0, e.positionProp = null, e.respondTo = null, 
            e.rowCount = 1, e.shouldClick = !0, e.$slider = a(c), e.$slidesCache = null, e.transformType = null, 
            e.transitionType = null, e.visibilityChange = "visibilitychange", e.windowWidth = 0, 
            e.windowTimer = null, f = a(c).data("slick") || {}, e.options = a.extend({}, e.defaults, d, f), 
            e.currentSlide = e.options.initialSlide, e.originalSettings = e.options, void 0 !== document.mozHidden ? (e.hidden = "mozHidden", 
            e.visibilityChange = "mozvisibilitychange") : void 0 !== document.webkitHidden && (e.hidden = "webkitHidden", 
            e.visibilityChange = "webkitvisibilitychange"), e.autoPlay = a.proxy(e.autoPlay, e), 
            e.autoPlayClear = a.proxy(e.autoPlayClear, e), e.autoPlayIterator = a.proxy(e.autoPlayIterator, e), 
            e.changeSlide = a.proxy(e.changeSlide, e), e.clickHandler = a.proxy(e.clickHandler, e), 
            e.selectHandler = a.proxy(e.selectHandler, e), e.setPosition = a.proxy(e.setPosition, e), 
            e.swipeHandler = a.proxy(e.swipeHandler, e), e.dragHandler = a.proxy(e.dragHandler, e), 
            e.keyHandler = a.proxy(e.keyHandler, e), e.instanceUid = b++, e.htmlExpr = /^(?:\s*(<[\w\W]+>)[^>]*)$/, 
            e.registerBreakpoints(), e.init(!0);
        }
        var b = 0;
        return c;
    }(), b.prototype.activateADA = function() {
        this.$slideTrack.find(".slick-active").attr({
            "aria-hidden": "false"
        }).find("a, input, button, select").attr({
            tabindex: "0"
        });
    }, b.prototype.addSlide = b.prototype.slickAdd = function(b, c, d) {
        var e = this;
        if ("boolean" == typeof c) d = c, c = null; else if (0 > c || c >= e.slideCount) return !1;
        e.unload(), "number" == typeof c ? 0 === c && 0 === e.$slides.length ? a(b).appendTo(e.$slideTrack) : d ? a(b).insertBefore(e.$slides.eq(c)) : a(b).insertAfter(e.$slides.eq(c)) : !0 === d ? a(b).prependTo(e.$slideTrack) : a(b).appendTo(e.$slideTrack), 
        e.$slides = e.$slideTrack.children(this.options.slide), e.$slideTrack.children(this.options.slide).detach(), 
        e.$slideTrack.append(e.$slides), e.$slides.each(function(b, c) {
            a(c).attr("data-slick-index", b);
        }), e.$slidesCache = e.$slides, e.reinit();
    }, b.prototype.animateHeight = function() {
        var a = this;
        if (1 === a.options.slidesToShow && !0 === a.options.adaptiveHeight && !1 === a.options.vertical) {
            var b = a.$slides.eq(a.currentSlide).outerHeight(!0);
            a.$list.animate({
                height: b
            }, a.options.speed);
        }
    }, b.prototype.animateSlide = function(b, c) {
        var d = {}, e = this;
        e.animateHeight(), !0 === e.options.rtl && !1 === e.options.vertical && (b = -b), 
        !1 === e.transformsEnabled ? !1 === e.options.vertical ? e.$slideTrack.animate({
            left: b
        }, e.options.speed, e.options.easing, c) : e.$slideTrack.animate({
            top: b
        }, e.options.speed, e.options.easing, c) : !1 === e.cssTransitions ? (!0 === e.options.rtl && (e.currentLeft = -e.currentLeft), 
        a({
            animStart: e.currentLeft
        }).animate({
            animStart: b
        }, {
            duration: e.options.speed,
            easing: e.options.easing,
            step: function(a) {
                a = Math.ceil(a), !1 === e.options.vertical ? (d[e.animType] = "translate(" + a + "px, 0px)", 
                e.$slideTrack.css(d)) : (d[e.animType] = "translate(0px," + a + "px)", e.$slideTrack.css(d));
            },
            complete: function() {
                c && c.call();
            }
        })) : (e.applyTransition(), b = Math.ceil(b), !1 === e.options.vertical ? d[e.animType] = "translate3d(" + b + "px, 0px, 0px)" : d[e.animType] = "translate3d(0px," + b + "px, 0px)", 
        e.$slideTrack.css(d), c && setTimeout(function() {
            e.disableTransition(), c.call();
        }, e.options.speed));
    }, b.prototype.getNavTarget = function() {
        var b = this, c = b.options.asNavFor;
        return c && null !== c && (c = a(c).not(b.$slider)), c;
    }, b.prototype.asNavFor = function(b) {
        var c = this, d = c.getNavTarget();
        null !== d && "object" == typeof d && d.each(function() {
            var c = a(this).slick("getSlick");
            c.unslicked || c.slideHandler(b, !0);
        });
    }, b.prototype.applyTransition = function(a) {
        var b = this, c = {};
        !1 === b.options.fade ? c[b.transitionType] = b.transformType + " " + b.options.speed + "ms " + b.options.cssEase : c[b.transitionType] = "opacity " + b.options.speed + "ms " + b.options.cssEase, 
        !1 === b.options.fade ? b.$slideTrack.css(c) : b.$slides.eq(a).css(c);
    }, b.prototype.autoPlay = function() {
        var a = this;
        a.autoPlayClear(), a.slideCount > a.options.slidesToShow && (a.autoPlayTimer = setInterval(a.autoPlayIterator, a.options.autoplaySpeed));
    }, b.prototype.autoPlayClear = function() {
        var a = this;
        a.autoPlayTimer && clearInterval(a.autoPlayTimer);
    }, b.prototype.autoPlayIterator = function() {
        var a = this, b = a.currentSlide + a.options.slidesToScroll;
        a.paused || a.interrupted || a.focussed || (!1 === a.options.infinite && (1 === a.direction && a.currentSlide + 1 === a.slideCount - 1 ? a.direction = 0 : 0 === a.direction && (b = a.currentSlide - a.options.slidesToScroll, 
        a.currentSlide - 1 == 0 && (a.direction = 1))), a.slideHandler(b));
    }, b.prototype.buildArrows = function() {
        var b = this;
        !0 === b.options.arrows && (b.$prevArrow = a(b.options.prevArrow).addClass("slick-arrow"), 
        b.$nextArrow = a(b.options.nextArrow).addClass("slick-arrow"), b.slideCount > b.options.slidesToShow ? (b.$prevArrow.removeClass("slick-hidden").removeAttr("aria-hidden tabindex"), 
        b.$nextArrow.removeClass("slick-hidden").removeAttr("aria-hidden tabindex"), b.htmlExpr.test(b.options.prevArrow) && b.$prevArrow.prependTo(b.options.appendArrows), 
        b.htmlExpr.test(b.options.nextArrow) && b.$nextArrow.appendTo(b.options.appendArrows), 
        !0 !== b.options.infinite && b.$prevArrow.addClass("slick-disabled").attr("aria-disabled", "true")) : b.$prevArrow.add(b.$nextArrow).addClass("slick-hidden").attr({
            "aria-disabled": "true",
            tabindex: "-1"
        }));
    }, b.prototype.buildDots = function() {
        var c, d, b = this;
        if (!0 === b.options.dots && b.slideCount > b.options.slidesToShow) {
            for (b.$slider.addClass("slick-dotted"), d = a("<ul />").addClass(b.options.dotsClass), 
            c = 0; c <= b.getDotCount(); c += 1) d.append(a("<li />").append(b.options.customPaging.call(this, b, c)));
            b.$dots = d.appendTo(b.options.appendDots), b.$dots.find("li").first().addClass("slick-active").attr("aria-hidden", "false");
        }
    }, b.prototype.buildOut = function() {
        var b = this;
        b.$slides = b.$slider.children(b.options.slide + ":not(.slick-cloned)").addClass("slick-slide"), 
        b.slideCount = b.$slides.length, b.$slides.each(function(b, c) {
            a(c).attr("data-slick-index", b).data("originalStyling", a(c).attr("style") || "");
        }), b.$slider.addClass("slick-slider"), b.$slideTrack = 0 === b.slideCount ? a('<div class="slick-track"/>').appendTo(b.$slider) : b.$slides.wrapAll('<div class="slick-track"/>').parent(), 
        b.$list = b.$slideTrack.wrap('<div aria-live="polite" class="slick-list"/>').parent(), 
        b.$slideTrack.css("opacity", 0), (!0 === b.options.centerMode || !0 === b.options.swipeToSlide) && (b.options.slidesToScroll = 1), 
        a("img[data-lazy]", b.$slider).not("[src]").addClass("slick-loading"), b.setupInfinite(), 
        b.buildArrows(), b.buildDots(), b.updateDots(), b.setSlideClasses("number" == typeof b.currentSlide ? b.currentSlide : 0), 
        !0 === b.options.draggable && b.$list.addClass("draggable");
    }, b.prototype.buildRows = function() {
        var b, c, d, e, f, g, h, a = this;
        if (e = document.createDocumentFragment(), g = a.$slider.children(), a.options.rows > 1) {
            for (h = a.options.slidesPerRow * a.options.rows, f = Math.ceil(g.length / h), b = 0; f > b; b++) {
                var i = document.createElement("div");
                for (c = 0; c < a.options.rows; c++) {
                    var j = document.createElement("div");
                    for (d = 0; d < a.options.slidesPerRow; d++) {
                        var k = b * h + (c * a.options.slidesPerRow + d);
                        g.get(k) && j.appendChild(g.get(k));
                    }
                    i.appendChild(j);
                }
                e.appendChild(i);
            }
            a.$slider.empty().append(e), a.$slider.children().children().children().css({
                width: 100 / a.options.slidesPerRow + "%",
                display: "inline-block"
            });
        }
    }, b.prototype.checkResponsive = function(b, c) {
        var e, f, g, d = this, h = !1, i = d.$slider.width(), j = window.innerWidth || a(window).width();
        if ("window" === d.respondTo ? g = j : "slider" === d.respondTo ? g = i : "min" === d.respondTo && (g = Math.min(j, i)), 
        d.options.responsive && d.options.responsive.length && null !== d.options.responsive) {
            f = null;
            for (e in d.breakpoints) d.breakpoints.hasOwnProperty(e) && (!1 === d.originalSettings.mobileFirst ? g < d.breakpoints[e] && (f = d.breakpoints[e]) : g > d.breakpoints[e] && (f = d.breakpoints[e]));
            null !== f ? null !== d.activeBreakpoint ? (f !== d.activeBreakpoint || c) && (d.activeBreakpoint = f, 
            "unslick" === d.breakpointSettings[f] ? d.unslick(f) : (d.options = a.extend({}, d.originalSettings, d.breakpointSettings[f]), 
            !0 === b && (d.currentSlide = d.options.initialSlide), d.refresh(b)), h = f) : (d.activeBreakpoint = f, 
            "unslick" === d.breakpointSettings[f] ? d.unslick(f) : (d.options = a.extend({}, d.originalSettings, d.breakpointSettings[f]), 
            !0 === b && (d.currentSlide = d.options.initialSlide), d.refresh(b)), h = f) : null !== d.activeBreakpoint && (d.activeBreakpoint = null, 
            d.options = d.originalSettings, !0 === b && (d.currentSlide = d.options.initialSlide), 
            d.refresh(b), h = f), b || !1 === h || d.$slider.trigger("breakpoint", [ d, h ]);
        }
    }, b.prototype.changeSlide = function(b, c) {
        var f, g, h, d = this, e = a(b.currentTarget);
        switch (e.is("a") && b.preventDefault(), e.is("li") || (e = e.closest("li")), h = d.slideCount % d.options.slidesToScroll != 0, 
        f = h ? 0 : (d.slideCount - d.currentSlide) % d.options.slidesToScroll, b.data.message) {
          case "previous":
            g = 0 === f ? d.options.slidesToScroll : d.options.slidesToShow - f, d.slideCount > d.options.slidesToShow && d.slideHandler(d.currentSlide - g, !1, c);
            break;

          case "next":
            g = 0 === f ? d.options.slidesToScroll : f, d.slideCount > d.options.slidesToShow && d.slideHandler(d.currentSlide + g, !1, c);
            break;

          case "index":
            var i = 0 === b.data.index ? 0 : b.data.index || e.index() * d.options.slidesToScroll;
            d.slideHandler(d.checkNavigable(i), !1, c), e.children().trigger("focus");
            break;

          default:
            return;
        }
    }, b.prototype.checkNavigable = function(a) {
        var c, d;
        if (c = this.getNavigableIndexes(), d = 0, a > c[c.length - 1]) a = c[c.length - 1]; else for (var e in c) {
            if (a < c[e]) {
                a = d;
                break;
            }
            d = c[e];
        }
        return a;
    }, b.prototype.cleanUpEvents = function() {
        var b = this;
        b.options.dots && null !== b.$dots && a("li", b.$dots).off("click.slick", b.changeSlide).off("mouseenter.slick", a.proxy(b.interrupt, b, !0)).off("mouseleave.slick", a.proxy(b.interrupt, b, !1)), 
        b.$slider.off("focus.slick blur.slick"), !0 === b.options.arrows && b.slideCount > b.options.slidesToShow && (b.$prevArrow && b.$prevArrow.off("click.slick", b.changeSlide), 
        b.$nextArrow && b.$nextArrow.off("click.slick", b.changeSlide)), b.$list.off("touchstart.slick mousedown.slick", b.swipeHandler), 
        b.$list.off("touchmove.slick mousemove.slick", b.swipeHandler), b.$list.off("touchend.slick mouseup.slick", b.swipeHandler), 
        b.$list.off("touchcancel.slick mouseleave.slick", b.swipeHandler), b.$list.off("click.slick", b.clickHandler), 
        a(document).off(b.visibilityChange, b.visibility), b.cleanUpSlideEvents(), !0 === b.options.accessibility && b.$list.off("keydown.slick", b.keyHandler), 
        !0 === b.options.focusOnSelect && a(b.$slideTrack).children().off("click.slick", b.selectHandler), 
        a(window).off("orientationchange.slick.slick-" + b.instanceUid, b.orientationChange), 
        a(window).off("resize.slick.slick-" + b.instanceUid, b.resize), a("[draggable!=true]", b.$slideTrack).off("dragstart", b.preventDefault), 
        a(window).off("load.slick.slick-" + b.instanceUid, b.setPosition), a(document).off("ready.slick.slick-" + b.instanceUid, b.setPosition);
    }, b.prototype.cleanUpSlideEvents = function() {
        var b = this;
        b.$list.off("mouseenter.slick", a.proxy(b.interrupt, b, !0)), b.$list.off("mouseleave.slick", a.proxy(b.interrupt, b, !1));
    }, b.prototype.cleanUpRows = function() {
        var b, a = this;
        a.options.rows > 1 && (b = a.$slides.children().children(), b.removeAttr("style"), 
        a.$slider.empty().append(b));
    }, b.prototype.clickHandler = function(a) {
        !1 === this.shouldClick && (a.stopImmediatePropagation(), a.stopPropagation(), a.preventDefault());
    }, b.prototype.destroy = function(b) {
        var c = this;
        c.autoPlayClear(), c.touchObject = {}, c.cleanUpEvents(), a(".slick-cloned", c.$slider).detach(), 
        c.$dots && c.$dots.remove(), c.$prevArrow && c.$prevArrow.length && (c.$prevArrow.removeClass("slick-disabled slick-arrow slick-hidden").removeAttr("aria-hidden aria-disabled tabindex").css("display", ""), 
        c.htmlExpr.test(c.options.prevArrow) && c.$prevArrow.remove()), c.$nextArrow && c.$nextArrow.length && (c.$nextArrow.removeClass("slick-disabled slick-arrow slick-hidden").removeAttr("aria-hidden aria-disabled tabindex").css("display", ""), 
        c.htmlExpr.test(c.options.nextArrow) && c.$nextArrow.remove()), c.$slides && (c.$slides.removeClass("slick-slide slick-active slick-center slick-visible slick-current").removeAttr("aria-hidden").removeAttr("data-slick-index").each(function() {
            a(this).attr("style", a(this).data("originalStyling"));
        }), c.$slideTrack.children(this.options.slide).detach(), c.$slideTrack.detach(), 
        c.$list.detach(), c.$slider.append(c.$slides)), c.cleanUpRows(), c.$slider.removeClass("slick-slider"), 
        c.$slider.removeClass("slick-initialized"), c.$slider.removeClass("slick-dotted"), 
        c.unslicked = !0, b || c.$slider.trigger("destroy", [ c ]);
    }, b.prototype.disableTransition = function(a) {
        var b = this, c = {};
        c[b.transitionType] = "", !1 === b.options.fade ? b.$slideTrack.css(c) : b.$slides.eq(a).css(c);
    }, b.prototype.fadeSlide = function(a, b) {
        var c = this;
        !1 === c.cssTransitions ? (c.$slides.eq(a).css({
            zIndex: c.options.zIndex
        }), c.$slides.eq(a).animate({
            opacity: 1
        }, c.options.speed, c.options.easing, b)) : (c.applyTransition(a), c.$slides.eq(a).css({
            opacity: 1,
            zIndex: c.options.zIndex
        }), b && setTimeout(function() {
            c.disableTransition(a), b.call();
        }, c.options.speed));
    }, b.prototype.fadeSlideOut = function(a) {
        var b = this;
        !1 === b.cssTransitions ? b.$slides.eq(a).animate({
            opacity: 0,
            zIndex: b.options.zIndex - 2
        }, b.options.speed, b.options.easing) : (b.applyTransition(a), b.$slides.eq(a).css({
            opacity: 0,
            zIndex: b.options.zIndex - 2
        }));
    }, b.prototype.filterSlides = b.prototype.slickFilter = function(a) {
        var b = this;
        null !== a && (b.$slidesCache = b.$slides, b.unload(), b.$slideTrack.children(this.options.slide).detach(), 
        b.$slidesCache.filter(a).appendTo(b.$slideTrack), b.reinit());
    }, b.prototype.focusHandler = function() {
        var b = this;
        b.$slider.off("focus.slick blur.slick").on("focus.slick blur.slick", "*:not(.slick-arrow)", function(c) {
            c.stopImmediatePropagation();
            var d = a(this);
            setTimeout(function() {
                b.options.pauseOnFocus && (b.focussed = d.is(":focus"), b.autoPlay());
            }, 0);
        });
    }, b.prototype.getCurrent = b.prototype.slickCurrentSlide = function() {
        return this.currentSlide;
    }, b.prototype.getDotCount = function() {
        var a = this, b = 0, c = 0, d = 0;
        if (!0 === a.options.infinite) for (;b < a.slideCount; ) ++d, b = c + a.options.slidesToScroll, 
        c += a.options.slidesToScroll <= a.options.slidesToShow ? a.options.slidesToScroll : a.options.slidesToShow; else if (!0 === a.options.centerMode) d = a.slideCount; else if (a.options.asNavFor) for (;b < a.slideCount; ) ++d, 
        b = c + a.options.slidesToScroll, c += a.options.slidesToScroll <= a.options.slidesToShow ? a.options.slidesToScroll : a.options.slidesToShow; else d = 1 + Math.ceil((a.slideCount - a.options.slidesToShow) / a.options.slidesToScroll);
        return d - 1;
    }, b.prototype.getLeft = function(a) {
        var c, d, f, b = this, e = 0;
        return b.slideOffset = 0, d = b.$slides.first().outerHeight(!0), !0 === b.options.infinite ? (b.slideCount > b.options.slidesToShow && (b.slideOffset = b.slideWidth * b.options.slidesToShow * -1, 
        e = d * b.options.slidesToShow * -1), b.slideCount % b.options.slidesToScroll != 0 && a + b.options.slidesToScroll > b.slideCount && b.slideCount > b.options.slidesToShow && (a > b.slideCount ? (b.slideOffset = (b.options.slidesToShow - (a - b.slideCount)) * b.slideWidth * -1, 
        e = (b.options.slidesToShow - (a - b.slideCount)) * d * -1) : (b.slideOffset = b.slideCount % b.options.slidesToScroll * b.slideWidth * -1, 
        e = b.slideCount % b.options.slidesToScroll * d * -1))) : a + b.options.slidesToShow > b.slideCount && (b.slideOffset = (a + b.options.slidesToShow - b.slideCount) * b.slideWidth, 
        e = (a + b.options.slidesToShow - b.slideCount) * d), b.slideCount <= b.options.slidesToShow && (b.slideOffset = 0, 
        e = 0), !0 === b.options.centerMode && !0 === b.options.infinite ? b.slideOffset += b.slideWidth * Math.floor(b.options.slidesToShow / 2) - b.slideWidth : !0 === b.options.centerMode && (b.slideOffset = 0, 
        b.slideOffset += b.slideWidth * Math.floor(b.options.slidesToShow / 2)), c = !1 === b.options.vertical ? a * b.slideWidth * -1 + b.slideOffset : a * d * -1 + e, 
        !0 === b.options.variableWidth && (f = b.slideCount <= b.options.slidesToShow || !1 === b.options.infinite ? b.$slideTrack.children(".slick-slide").eq(a) : b.$slideTrack.children(".slick-slide").eq(a + b.options.slidesToShow), 
        c = !0 === b.options.rtl ? f[0] ? -1 * (b.$slideTrack.width() - f[0].offsetLeft - f.width()) : 0 : f[0] ? -1 * f[0].offsetLeft : 0, 
        !0 === b.options.centerMode && (f = b.slideCount <= b.options.slidesToShow || !1 === b.options.infinite ? b.$slideTrack.children(".slick-slide").eq(a) : b.$slideTrack.children(".slick-slide").eq(a + b.options.slidesToShow + 1), 
        c = !0 === b.options.rtl ? f[0] ? -1 * (b.$slideTrack.width() - f[0].offsetLeft - f.width()) : 0 : f[0] ? -1 * f[0].offsetLeft : 0, 
        c += (b.$list.width() - f.outerWidth()) / 2)), c;
    }, b.prototype.getOption = b.prototype.slickGetOption = function(a) {
        return this.options[a];
    }, b.prototype.getNavigableIndexes = function() {
        var e, a = this, b = 0, c = 0, d = [];
        for (!1 === a.options.infinite ? e = a.slideCount : (b = -1 * a.options.slidesToScroll, 
        c = -1 * a.options.slidesToScroll, e = 2 * a.slideCount); e > b; ) d.push(b), b = c + a.options.slidesToScroll, 
        c += a.options.slidesToScroll <= a.options.slidesToShow ? a.options.slidesToScroll : a.options.slidesToShow;
        return d;
    }, b.prototype.getSlick = function() {
        return this;
    }, b.prototype.getSlideCount = function() {
        var d, e, b = this;
        return e = !0 === b.options.centerMode ? b.slideWidth * Math.floor(b.options.slidesToShow / 2) : 0, 
        !0 === b.options.swipeToSlide ? (b.$slideTrack.find(".slick-slide").each(function(c, f) {
            return f.offsetLeft - e + a(f).outerWidth() / 2 > -1 * b.swipeLeft ? (d = f, !1) : void 0;
        }), Math.abs(a(d).attr("data-slick-index") - b.currentSlide) || 1) : b.options.slidesToScroll;
    }, b.prototype.goTo = b.prototype.slickGoTo = function(a, b) {
        this.changeSlide({
            data: {
                message: "index",
                index: parseInt(a)
            }
        }, b);
    }, b.prototype.init = function(b) {
        var c = this;
        a(c.$slider).hasClass("slick-initialized") || (a(c.$slider).addClass("slick-initialized"), 
        c.buildRows(), c.buildOut(), c.setProps(), c.startLoad(), c.loadSlider(), c.initializeEvents(), 
        c.updateArrows(), c.updateDots(), c.checkResponsive(!0), c.focusHandler()), b && c.$slider.trigger("init", [ c ]), 
        !0 === c.options.accessibility && c.initADA(), c.options.autoplay && (c.paused = !1, 
        c.autoPlay());
    }, b.prototype.initADA = function() {
        var b = this;
        b.$slides.add(b.$slideTrack.find(".slick-cloned")).attr({
            "aria-hidden": "true",
            tabindex: "-1"
        }).find("a, input, button, select").attr({
            tabindex: "-1"
        }), b.$slideTrack.attr("role", "listbox"), b.$slides.not(b.$slideTrack.find(".slick-cloned")).each(function(c) {
            a(this).attr({
                role: "option",
                "aria-describedby": "slick-slide" + b.instanceUid + c
            });
        }), null !== b.$dots && b.$dots.attr("role", "tablist").find("li").each(function(c) {
            a(this).attr({
                role: "presentation",
                "aria-selected": "false",
                "aria-controls": "navigation" + b.instanceUid + c,
                id: "slick-slide" + b.instanceUid + c
            });
        }).first().attr("aria-selected", "true").end().find("button").attr("role", "button").end().closest("div").attr("role", "toolbar"), 
        b.activateADA();
    }, b.prototype.initArrowEvents = function() {
        var a = this;
        !0 === a.options.arrows && a.slideCount > a.options.slidesToShow && (a.$prevArrow.off("click.slick").on("click.slick", {
            message: "previous"
        }, a.changeSlide), a.$nextArrow.off("click.slick").on("click.slick", {
            message: "next"
        }, a.changeSlide));
    }, b.prototype.initDotEvents = function() {
        var b = this;
        !0 === b.options.dots && b.slideCount > b.options.slidesToShow && a("li", b.$dots).on("click.slick", {
            message: "index"
        }, b.changeSlide), !0 === b.options.dots && !0 === b.options.pauseOnDotsHover && a("li", b.$dots).on("mouseenter.slick", a.proxy(b.interrupt, b, !0)).on("mouseleave.slick", a.proxy(b.interrupt, b, !1));
    }, b.prototype.initSlideEvents = function() {
        var b = this;
        b.options.pauseOnHover && (b.$list.on("mouseenter.slick", a.proxy(b.interrupt, b, !0)), 
        b.$list.on("mouseleave.slick", a.proxy(b.interrupt, b, !1)));
    }, b.prototype.initializeEvents = function() {
        var b = this;
        b.initArrowEvents(), b.initDotEvents(), b.initSlideEvents(), b.$list.on("touchstart.slick mousedown.slick", {
            action: "start"
        }, b.swipeHandler), b.$list.on("touchmove.slick mousemove.slick", {
            action: "move"
        }, b.swipeHandler), b.$list.on("touchend.slick mouseup.slick", {
            action: "end"
        }, b.swipeHandler), b.$list.on("touchcancel.slick mouseleave.slick", {
            action: "end"
        }, b.swipeHandler), b.$list.on("click.slick", b.clickHandler), a(document).on(b.visibilityChange, a.proxy(b.visibility, b)), 
        !0 === b.options.accessibility && b.$list.on("keydown.slick", b.keyHandler), !0 === b.options.focusOnSelect && a(b.$slideTrack).children().on("click.slick", b.selectHandler), 
        a(window).on("orientationchange.slick.slick-" + b.instanceUid, a.proxy(b.orientationChange, b)), 
        a(window).on("resize.slick.slick-" + b.instanceUid, a.proxy(b.resize, b)), a("[draggable!=true]", b.$slideTrack).on("dragstart", b.preventDefault), 
        a(window).on("load.slick.slick-" + b.instanceUid, b.setPosition), a(document).on("ready.slick.slick-" + b.instanceUid, b.setPosition);
    }, b.prototype.initUI = function() {
        var a = this;
        !0 === a.options.arrows && a.slideCount > a.options.slidesToShow && (a.$prevArrow.show(), 
        a.$nextArrow.show()), !0 === a.options.dots && a.slideCount > a.options.slidesToShow && a.$dots.show();
    }, b.prototype.keyHandler = function(a) {
        var b = this;
        a.target.tagName.match("TEXTAREA|INPUT|SELECT") || (37 === a.keyCode && !0 === b.options.accessibility ? b.changeSlide({
            data: {
                message: !0 === b.options.rtl ? "next" : "previous"
            }
        }) : 39 === a.keyCode && !0 === b.options.accessibility && b.changeSlide({
            data: {
                message: !0 === b.options.rtl ? "previous" : "next"
            }
        }));
    }, b.prototype.lazyLoad = function() {
        function g(c) {
            a("img[data-lazy]", c).each(function() {
                var c = a(this), d = a(this).attr("data-lazy"), e = document.createElement("img");
                e.onload = function() {
                    c.animate({
                        opacity: 0
                    }, 100, function() {
                        c.attr("src", d).animate({
                            opacity: 1
                        }, 200, function() {
                            c.removeAttr("data-lazy").removeClass("slick-loading");
                        }), b.$slider.trigger("lazyLoaded", [ b, c, d ]);
                    });
                }, e.onerror = function() {
                    c.removeAttr("data-lazy").removeClass("slick-loading").addClass("slick-lazyload-error"), 
                    b.$slider.trigger("lazyLoadError", [ b, c, d ]);
                }, e.src = d;
            });
        }
        var c, d, e, f, b = this;
        !0 === b.options.centerMode ? !0 === b.options.infinite ? (e = b.currentSlide + (b.options.slidesToShow / 2 + 1), 
        f = e + b.options.slidesToShow + 2) : (e = Math.max(0, b.currentSlide - (b.options.slidesToShow / 2 + 1)), 
        f = b.options.slidesToShow / 2 + 1 + 2 + b.currentSlide) : (e = b.options.infinite ? b.options.slidesToShow + b.currentSlide : b.currentSlide, 
        f = Math.ceil(e + b.options.slidesToShow), !0 === b.options.fade && (e > 0 && e--, 
        f <= b.slideCount && f++)), c = b.$slider.find(".slick-slide").slice(e, f), g(c), 
        b.slideCount <= b.options.slidesToShow ? (d = b.$slider.find(".slick-slide"), g(d)) : b.currentSlide >= b.slideCount - b.options.slidesToShow ? (d = b.$slider.find(".slick-cloned").slice(0, b.options.slidesToShow), 
        g(d)) : 0 === b.currentSlide && (d = b.$slider.find(".slick-cloned").slice(-1 * b.options.slidesToShow), 
        g(d));
    }, b.prototype.loadSlider = function() {
        var a = this;
        a.setPosition(), a.$slideTrack.css({
            opacity: 1
        }), a.$slider.removeClass("slick-loading"), a.initUI(), "progressive" === a.options.lazyLoad && a.progressiveLazyLoad();
    }, b.prototype.next = b.prototype.slickNext = function() {
        this.changeSlide({
            data: {
                message: "next"
            }
        });
    }, b.prototype.orientationChange = function() {
        var a = this;
        a.checkResponsive(), a.setPosition();
    }, b.prototype.pause = b.prototype.slickPause = function() {
        var a = this;
        a.autoPlayClear(), a.paused = !0;
    }, b.prototype.play = b.prototype.slickPlay = function() {
        var a = this;
        a.autoPlay(), a.options.autoplay = !0, a.paused = !1, a.focussed = !1, a.interrupted = !1;
    }, b.prototype.postSlide = function(a) {
        var b = this;
        b.unslicked || (b.$slider.trigger("afterChange", [ b, a ]), b.animating = !1, b.setPosition(), 
        b.swipeLeft = null, b.options.autoplay && b.autoPlay(), !0 === b.options.accessibility && b.initADA());
    }, b.prototype.prev = b.prototype.slickPrev = function() {
        this.changeSlide({
            data: {
                message: "previous"
            }
        });
    }, b.prototype.preventDefault = function(a) {
        a.preventDefault();
    }, b.prototype.progressiveLazyLoad = function(b) {
        b = b || 1;
        var e, f, g, c = this, d = a("img[data-lazy]", c.$slider);
        d.length ? (e = d.first(), f = e.attr("data-lazy"), g = document.createElement("img"), 
        g.onload = function() {
            e.attr("src", f).removeAttr("data-lazy").removeClass("slick-loading"), !0 === c.options.adaptiveHeight && c.setPosition(), 
            c.$slider.trigger("lazyLoaded", [ c, e, f ]), c.progressiveLazyLoad();
        }, g.onerror = function() {
            3 > b ? setTimeout(function() {
                c.progressiveLazyLoad(b + 1);
            }, 500) : (e.removeAttr("data-lazy").removeClass("slick-loading").addClass("slick-lazyload-error"), 
            c.$slider.trigger("lazyLoadError", [ c, e, f ]), c.progressiveLazyLoad());
        }, g.src = f) : c.$slider.trigger("allImagesLoaded", [ c ]);
    }, b.prototype.refresh = function(b) {
        var d, e, c = this;
        e = c.slideCount - c.options.slidesToShow, !c.options.infinite && c.currentSlide > e && (c.currentSlide = e), 
        c.slideCount <= c.options.slidesToShow && (c.currentSlide = 0), d = c.currentSlide, 
        c.destroy(!0), a.extend(c, c.initials, {
            currentSlide: d
        }), c.init(), b || c.changeSlide({
            data: {
                message: "index",
                index: d
            }
        }, !1);
    }, b.prototype.registerBreakpoints = function() {
        var c, d, e, b = this, f = b.options.responsive || null;
        if ("array" === a.type(f) && f.length) {
            b.respondTo = b.options.respondTo || "window";
            for (c in f) if (e = b.breakpoints.length - 1, d = f[c].breakpoint, f.hasOwnProperty(c)) {
                for (;e >= 0; ) b.breakpoints[e] && b.breakpoints[e] === d && b.breakpoints.splice(e, 1), 
                e--;
                b.breakpoints.push(d), b.breakpointSettings[d] = f[c].settings;
            }
            b.breakpoints.sort(function(a, c) {
                return b.options.mobileFirst ? a - c : c - a;
            });
        }
    }, b.prototype.reinit = function() {
        var b = this;
        b.$slides = b.$slideTrack.children(b.options.slide).addClass("slick-slide"), b.slideCount = b.$slides.length, 
        b.currentSlide >= b.slideCount && 0 !== b.currentSlide && (b.currentSlide = b.currentSlide - b.options.slidesToScroll), 
        b.slideCount <= b.options.slidesToShow && (b.currentSlide = 0), b.registerBreakpoints(), 
        b.setProps(), b.setupInfinite(), b.buildArrows(), b.updateArrows(), b.initArrowEvents(), 
        b.buildDots(), b.updateDots(), b.initDotEvents(), b.cleanUpSlideEvents(), b.initSlideEvents(), 
        b.checkResponsive(!1, !0), !0 === b.options.focusOnSelect && a(b.$slideTrack).children().on("click.slick", b.selectHandler), 
        b.setSlideClasses("number" == typeof b.currentSlide ? b.currentSlide : 0), b.setPosition(), 
        b.focusHandler(), b.paused = !b.options.autoplay, b.autoPlay(), b.$slider.trigger("reInit", [ b ]);
    }, b.prototype.resize = function() {
        var b = this;
        a(window).width() !== b.windowWidth && (clearTimeout(b.windowDelay), b.windowDelay = window.setTimeout(function() {
            b.windowWidth = a(window).width(), b.checkResponsive(), b.unslicked || b.setPosition();
        }, 50));
    }, b.prototype.removeSlide = b.prototype.slickRemove = function(a, b, c) {
        var d = this;
        return "boolean" == typeof a ? (b = a, a = !0 === b ? 0 : d.slideCount - 1) : a = !0 === b ? --a : a, 
        !(d.slideCount < 1 || 0 > a || a > d.slideCount - 1) && (d.unload(), !0 === c ? d.$slideTrack.children().remove() : d.$slideTrack.children(this.options.slide).eq(a).remove(), 
        d.$slides = d.$slideTrack.children(this.options.slide), d.$slideTrack.children(this.options.slide).detach(), 
        d.$slideTrack.append(d.$slides), d.$slidesCache = d.$slides, void d.reinit());
    }, b.prototype.setCSS = function(a) {
        var d, e, b = this, c = {};
        !0 === b.options.rtl && (a = -a), d = "left" == b.positionProp ? Math.ceil(a) + "px" : "0px", 
        e = "top" == b.positionProp ? Math.ceil(a) + "px" : "0px", c[b.positionProp] = a, 
        !1 === b.transformsEnabled ? b.$slideTrack.css(c) : (c = {}, !1 === b.cssTransitions ? (c[b.animType] = "translate(" + d + ", " + e + ")", 
        b.$slideTrack.css(c)) : (c[b.animType] = "translate3d(" + d + ", " + e + ", 0px)", 
        b.$slideTrack.css(c)));
    }, b.prototype.setDimensions = function() {
        var a = this;
        !1 === a.options.vertical ? !0 === a.options.centerMode && a.$list.css({
            padding: "0px " + a.options.centerPadding
        }) : (a.$list.height(a.$slides.first().outerHeight(!0) * a.options.slidesToShow), 
        !0 === a.options.centerMode && a.$list.css({
            padding: a.options.centerPadding + " 0px"
        })), a.listWidth = a.$list.width(), a.listHeight = a.$list.height(), !1 === a.options.vertical && !1 === a.options.variableWidth ? (a.slideWidth = Math.ceil(a.listWidth / a.options.slidesToShow), 
        a.$slideTrack.width(Math.ceil(a.slideWidth * a.$slideTrack.children(".slick-slide").length))) : !0 === a.options.variableWidth ? a.$slideTrack.width(5e3 * a.slideCount) : (a.slideWidth = Math.ceil(a.listWidth), 
        a.$slideTrack.height(Math.ceil(a.$slides.first().outerHeight(!0) * a.$slideTrack.children(".slick-slide").length)));
        var b = a.$slides.first().outerWidth(!0) - a.$slides.first().width();
        !1 === a.options.variableWidth && a.$slideTrack.children(".slick-slide").width(a.slideWidth - b);
    }, b.prototype.setFade = function() {
        var c, b = this;
        b.$slides.each(function(d, e) {
            c = b.slideWidth * d * -1, !0 === b.options.rtl ? a(e).css({
                position: "relative",
                right: c,
                top: 0,
                zIndex: b.options.zIndex - 2,
                opacity: 0
            }) : a(e).css({
                position: "relative",
                left: c,
                top: 0,
                zIndex: b.options.zIndex - 2,
                opacity: 0
            });
        }), b.$slides.eq(b.currentSlide).css({
            zIndex: b.options.zIndex - 1,
            opacity: 1
        });
    }, b.prototype.setHeight = function() {
        var a = this;
        if (1 === a.options.slidesToShow && !0 === a.options.adaptiveHeight && !1 === a.options.vertical) {
            var b = a.$slides.eq(a.currentSlide).outerHeight(!0);
            a.$list.css("height", b);
        }
    }, b.prototype.setOption = b.prototype.slickSetOption = function() {
        var c, d, e, f, h, b = this, g = !1;
        if ("object" === a.type(arguments[0]) ? (e = arguments[0], g = arguments[1], h = "multiple") : "string" === a.type(arguments[0]) && (e = arguments[0], 
        f = arguments[1], g = arguments[2], "responsive" === arguments[0] && "array" === a.type(arguments[1]) ? h = "responsive" : void 0 !== arguments[1] && (h = "single")), 
        "single" === h) b.options[e] = f; else if ("multiple" === h) a.each(e, function(a, c) {
            b.options[a] = c;
        }); else if ("responsive" === h) for (d in f) if ("array" !== a.type(b.options.responsive)) b.options.responsive = [ f[d] ]; else {
            for (c = b.options.responsive.length - 1; c >= 0; ) b.options.responsive[c].breakpoint === f[d].breakpoint && b.options.responsive.splice(c, 1), 
            c--;
            b.options.responsive.push(f[d]);
        }
        g && (b.unload(), b.reinit());
    }, b.prototype.setPosition = function() {
        var a = this;
        a.setDimensions(), a.setHeight(), !1 === a.options.fade ? a.setCSS(a.getLeft(a.currentSlide)) : a.setFade(), 
        a.$slider.trigger("setPosition", [ a ]);
    }, b.prototype.setProps = function() {
        var a = this, b = document.body.style;
        a.positionProp = !0 === a.options.vertical ? "top" : "left", "top" === a.positionProp ? a.$slider.addClass("slick-vertical") : a.$slider.removeClass("slick-vertical"), 
        (void 0 !== b.WebkitTransition || void 0 !== b.MozTransition || void 0 !== b.msTransition) && !0 === a.options.useCSS && (a.cssTransitions = !0), 
        a.options.fade && ("number" == typeof a.options.zIndex ? a.options.zIndex < 3 && (a.options.zIndex = 3) : a.options.zIndex = a.defaults.zIndex), 
        void 0 !== b.OTransform && (a.animType = "OTransform", a.transformType = "-o-transform", 
        a.transitionType = "OTransition", void 0 === b.perspectiveProperty && void 0 === b.webkitPerspective && (a.animType = !1)), 
        void 0 !== b.MozTransform && (a.animType = "MozTransform", a.transformType = "-moz-transform", 
        a.transitionType = "MozTransition", void 0 === b.perspectiveProperty && void 0 === b.MozPerspective && (a.animType = !1)), 
        void 0 !== b.webkitTransform && (a.animType = "webkitTransform", a.transformType = "-webkit-transform", 
        a.transitionType = "webkitTransition", void 0 === b.perspectiveProperty && void 0 === b.webkitPerspective && (a.animType = !1)), 
        void 0 !== b.msTransform && (a.animType = "msTransform", a.transformType = "-ms-transform", 
        a.transitionType = "msTransition", void 0 === b.msTransform && (a.animType = !1)), 
        void 0 !== b.transform && !1 !== a.animType && (a.animType = "transform", a.transformType = "transform", 
        a.transitionType = "transition"), a.transformsEnabled = a.options.useTransform && null !== a.animType && !1 !== a.animType;
    }, b.prototype.setSlideClasses = function(a) {
        var c, d, e, f, b = this;
        d = b.$slider.find(".slick-slide").removeClass("slick-active slick-center slick-current").attr("aria-hidden", "true"), 
        b.$slides.eq(a).addClass("slick-current"), !0 === b.options.centerMode ? (c = Math.floor(b.options.slidesToShow / 2), 
        !0 === b.options.infinite && (a >= c && a <= b.slideCount - 1 - c ? b.$slides.slice(a - c, a + c + 1).addClass("slick-active").attr("aria-hidden", "false") : (e = b.options.slidesToShow + a, 
        d.slice(e - c + 1, e + c + 2).addClass("slick-active").attr("aria-hidden", "false")), 
        0 === a ? d.eq(d.length - 1 - b.options.slidesToShow).addClass("slick-center") : a === b.slideCount - 1 && d.eq(b.options.slidesToShow).addClass("slick-center")), 
        b.$slides.eq(a).addClass("slick-center")) : a >= 0 && a <= b.slideCount - b.options.slidesToShow ? b.$slides.slice(a, a + b.options.slidesToShow).addClass("slick-active").attr("aria-hidden", "false") : d.length <= b.options.slidesToShow ? d.addClass("slick-active").attr("aria-hidden", "false") : (f = b.slideCount % b.options.slidesToShow, 
        e = !0 === b.options.infinite ? b.options.slidesToShow + a : a, b.options.slidesToShow == b.options.slidesToScroll && b.slideCount - a < b.options.slidesToShow ? d.slice(e - (b.options.slidesToShow - f), e + f).addClass("slick-active").attr("aria-hidden", "false") : d.slice(e, e + b.options.slidesToShow).addClass("slick-active").attr("aria-hidden", "false")), 
        "ondemand" === b.options.lazyLoad && b.lazyLoad();
    }, b.prototype.setupInfinite = function() {
        var c, d, e, b = this;
        if (!0 === b.options.fade && (b.options.centerMode = !1), !0 === b.options.infinite && !1 === b.options.fade && (d = null, 
        b.slideCount > b.options.slidesToShow)) {
            for (e = !0 === b.options.centerMode ? b.options.slidesToShow + 1 : b.options.slidesToShow, 
            c = b.slideCount; c > b.slideCount - e; c -= 1) d = c - 1, a(b.$slides[d]).clone(!0).attr("id", "").attr("data-slick-index", d - b.slideCount).prependTo(b.$slideTrack).addClass("slick-cloned");
            for (c = 0; e > c; c += 1) d = c, a(b.$slides[d]).clone(!0).attr("id", "").attr("data-slick-index", d + b.slideCount).appendTo(b.$slideTrack).addClass("slick-cloned");
            b.$slideTrack.find(".slick-cloned").find("[id]").each(function() {
                a(this).attr("id", "");
            });
        }
    }, b.prototype.interrupt = function(a) {
        var b = this;
        a || b.autoPlay(), b.interrupted = a;
    }, b.prototype.selectHandler = function(b) {
        var c = this, d = a(b.target).is(".slick-slide") ? a(b.target) : a(b.target).parents(".slick-slide"), e = parseInt(d.attr("data-slick-index"));
        return e || (e = 0), c.slideCount <= c.options.slidesToShow ? (c.setSlideClasses(e), 
        void c.asNavFor(e)) : void c.slideHandler(e);
    }, b.prototype.slideHandler = function(a, b, c) {
        var d, e, f, g, j, h = null, i = this;
        return b = b || !1, !0 === i.animating && !0 === i.options.waitForAnimate || !0 === i.options.fade && i.currentSlide === a || i.slideCount <= i.options.slidesToShow ? void 0 : (!1 === b && i.asNavFor(a), 
        d = a, h = i.getLeft(d), g = i.getLeft(i.currentSlide), i.currentLeft = null === i.swipeLeft ? g : i.swipeLeft, 
        !1 === i.options.infinite && !1 === i.options.centerMode && (0 > a || a > i.getDotCount() * i.options.slidesToScroll) ? void (!1 === i.options.fade && (d = i.currentSlide, 
        !0 !== c ? i.animateSlide(g, function() {
            i.postSlide(d);
        }) : i.postSlide(d))) : !1 === i.options.infinite && !0 === i.options.centerMode && (0 > a || a > i.slideCount - i.options.slidesToScroll) ? void (!1 === i.options.fade && (d = i.currentSlide, 
        !0 !== c ? i.animateSlide(g, function() {
            i.postSlide(d);
        }) : i.postSlide(d))) : (i.options.autoplay && clearInterval(i.autoPlayTimer), e = 0 > d ? i.slideCount % i.options.slidesToScroll != 0 ? i.slideCount - i.slideCount % i.options.slidesToScroll : i.slideCount + d : d >= i.slideCount ? i.slideCount % i.options.slidesToScroll != 0 ? 0 : d - i.slideCount : d, 
        i.animating = !0, i.$slider.trigger("beforeChange", [ i, i.currentSlide, e ]), f = i.currentSlide, 
        i.currentSlide = e, i.setSlideClasses(i.currentSlide), i.options.asNavFor && (j = i.getNavTarget(), 
        j = j.slick("getSlick"), j.slideCount <= j.options.slidesToShow && j.setSlideClasses(i.currentSlide)), 
        i.updateDots(), i.updateArrows(), !0 === i.options.fade ? (!0 !== c ? (i.fadeSlideOut(f), 
        i.fadeSlide(e, function() {
            i.postSlide(e);
        })) : i.postSlide(e), void i.animateHeight()) : void (!0 !== c ? i.animateSlide(h, function() {
            i.postSlide(e);
        }) : i.postSlide(e))));
    }, b.prototype.startLoad = function() {
        var a = this;
        !0 === a.options.arrows && a.slideCount > a.options.slidesToShow && (a.$prevArrow.hide(), 
        a.$nextArrow.hide()), !0 === a.options.dots && a.slideCount > a.options.slidesToShow && a.$dots.hide(), 
        a.$slider.addClass("slick-loading");
    }, b.prototype.swipeDirection = function() {
        var a, b, c, d, e = this;
        return a = e.touchObject.startX - e.touchObject.curX, b = e.touchObject.startY - e.touchObject.curY, 
        c = Math.atan2(b, a), d = Math.round(180 * c / Math.PI), 0 > d && (d = 360 - Math.abs(d)), 
        45 >= d && d >= 0 ? !1 === e.options.rtl ? "left" : "right" : 360 >= d && d >= 315 ? !1 === e.options.rtl ? "left" : "right" : d >= 135 && 225 >= d ? !1 === e.options.rtl ? "right" : "left" : !0 === e.options.verticalSwiping ? d >= 35 && 135 >= d ? "down" : "up" : "vertical";
    }, b.prototype.swipeEnd = function(a) {
        var c, d, b = this;
        if (b.dragging = !1, b.interrupted = !1, b.shouldClick = !(b.touchObject.swipeLength > 10), 
        void 0 === b.touchObject.curX) return !1;
        if (!0 === b.touchObject.edgeHit && b.$slider.trigger("edge", [ b, b.swipeDirection() ]), 
        b.touchObject.swipeLength >= b.touchObject.minSwipe) {
            switch (d = b.swipeDirection()) {
              case "left":
              case "down":
                c = b.options.swipeToSlide ? b.checkNavigable(b.currentSlide + b.getSlideCount()) : b.currentSlide + b.getSlideCount(), 
                b.currentDirection = 0;
                break;

              case "right":
              case "up":
                c = b.options.swipeToSlide ? b.checkNavigable(b.currentSlide - b.getSlideCount()) : b.currentSlide - b.getSlideCount(), 
                b.currentDirection = 1;
            }
            "vertical" != d && (b.slideHandler(c), b.touchObject = {}, b.$slider.trigger("swipe", [ b, d ]));
        } else b.touchObject.startX !== b.touchObject.curX && (b.slideHandler(b.currentSlide), 
        b.touchObject = {});
    }, b.prototype.swipeHandler = function(a) {
        var b = this;
        if (!(!1 === b.options.swipe || "ontouchend" in document && !1 === b.options.swipe || !1 === b.options.draggable && -1 !== a.type.indexOf("mouse"))) switch (b.touchObject.fingerCount = a.originalEvent && void 0 !== a.originalEvent.touches ? a.originalEvent.touches.length : 1, 
        b.touchObject.minSwipe = b.listWidth / b.options.touchThreshold, !0 === b.options.verticalSwiping && (b.touchObject.minSwipe = b.listHeight / b.options.touchThreshold), 
        a.data.action) {
          case "start":
            b.swipeStart(a);
            break;

          case "move":
            b.swipeMove(a);
            break;

          case "end":
            b.swipeEnd(a);
        }
    }, b.prototype.swipeMove = function(a) {
        var d, e, f, g, h, b = this;
        return h = void 0 !== a.originalEvent ? a.originalEvent.touches : null, !(!b.dragging || h && 1 !== h.length) && (d = b.getLeft(b.currentSlide), 
        b.touchObject.curX = void 0 !== h ? h[0].pageX : a.clientX, b.touchObject.curY = void 0 !== h ? h[0].pageY : a.clientY, 
        b.touchObject.swipeLength = Math.round(Math.sqrt(Math.pow(b.touchObject.curX - b.touchObject.startX, 2))), 
        !0 === b.options.verticalSwiping && (b.touchObject.swipeLength = Math.round(Math.sqrt(Math.pow(b.touchObject.curY - b.touchObject.startY, 2)))), 
        e = b.swipeDirection(), "vertical" !== e ? (void 0 !== a.originalEvent && b.touchObject.swipeLength > 4 && a.preventDefault(), 
        g = (!1 === b.options.rtl ? 1 : -1) * (b.touchObject.curX > b.touchObject.startX ? 1 : -1), 
        !0 === b.options.verticalSwiping && (g = b.touchObject.curY > b.touchObject.startY ? 1 : -1), 
        f = b.touchObject.swipeLength, b.touchObject.edgeHit = !1, !1 === b.options.infinite && (0 === b.currentSlide && "right" === e || b.currentSlide >= b.getDotCount() && "left" === e) && (f = b.touchObject.swipeLength * b.options.edgeFriction, 
        b.touchObject.edgeHit = !0), !1 === b.options.vertical ? b.swipeLeft = d + f * g : b.swipeLeft = d + f * (b.$list.height() / b.listWidth) * g, 
        !0 === b.options.verticalSwiping && (b.swipeLeft = d + f * g), !0 !== b.options.fade && !1 !== b.options.touchMove && (!0 === b.animating ? (b.swipeLeft = null, 
        !1) : void b.setCSS(b.swipeLeft))) : void 0);
    }, b.prototype.swipeStart = function(a) {
        var c, b = this;
        return b.interrupted = !0, 1 !== b.touchObject.fingerCount || b.slideCount <= b.options.slidesToShow ? (b.touchObject = {}, 
        !1) : (void 0 !== a.originalEvent && void 0 !== a.originalEvent.touches && (c = a.originalEvent.touches[0]), 
        b.touchObject.startX = b.touchObject.curX = void 0 !== c ? c.pageX : a.clientX, 
        b.touchObject.startY = b.touchObject.curY = void 0 !== c ? c.pageY : a.clientY, 
        void (b.dragging = !0));
    }, b.prototype.unfilterSlides = b.prototype.slickUnfilter = function() {
        var a = this;
        null !== a.$slidesCache && (a.unload(), a.$slideTrack.children(this.options.slide).detach(), 
        a.$slidesCache.appendTo(a.$slideTrack), a.reinit());
    }, b.prototype.unload = function() {
        var b = this;
        a(".slick-cloned", b.$slider).remove(), b.$dots && b.$dots.remove(), b.$prevArrow && b.htmlExpr.test(b.options.prevArrow) && b.$prevArrow.remove(), 
        b.$nextArrow && b.htmlExpr.test(b.options.nextArrow) && b.$nextArrow.remove(), b.$slides.removeClass("slick-slide slick-active slick-visible slick-current").attr("aria-hidden", "true").css("width", "");
    }, b.prototype.unslick = function(a) {
        var b = this;
        b.$slider.trigger("unslick", [ b, a ]), b.destroy();
    }, b.prototype.updateArrows = function() {
        var a = this;
        Math.floor(a.options.slidesToShow / 2), !0 === a.options.arrows && a.slideCount > a.options.slidesToShow && !a.options.infinite && (a.$prevArrow.removeClass("slick-disabled").attr("aria-disabled", "false"), 
        a.$nextArrow.removeClass("slick-disabled").attr("aria-disabled", "false"), 0 === a.currentSlide ? (a.$prevArrow.addClass("slick-disabled").attr("aria-disabled", "true"), 
        a.$nextArrow.removeClass("slick-disabled").attr("aria-disabled", "false")) : a.currentSlide >= a.slideCount - a.options.slidesToShow && !1 === a.options.centerMode ? (a.$nextArrow.addClass("slick-disabled").attr("aria-disabled", "true"), 
        a.$prevArrow.removeClass("slick-disabled").attr("aria-disabled", "false")) : a.currentSlide >= a.slideCount - 1 && !0 === a.options.centerMode && (a.$nextArrow.addClass("slick-disabled").attr("aria-disabled", "true"), 
        a.$prevArrow.removeClass("slick-disabled").attr("aria-disabled", "false")));
    }, b.prototype.updateDots = function() {
        var a = this;
        null !== a.$dots && (a.$dots.find("li").removeClass("slick-active").attr("aria-hidden", "true"), 
        a.$dots.find("li").eq(Math.floor(a.currentSlide / a.options.slidesToScroll)).addClass("slick-active").attr("aria-hidden", "false"));
    }, b.prototype.visibility = function() {
        var a = this;
        a.options.autoplay && (document[a.hidden] ? a.interrupted = !0 : a.interrupted = !1);
    }, a.fn.slick = function() {
        var f, g, a = this, c = arguments[0], d = Array.prototype.slice.call(arguments, 1), e = a.length;
        for (f = 0; e > f; f++) if ("object" == typeof c || void 0 === c ? a[f].slick = new b(a[f], c) : g = a[f].slick[c].apply(a[f].slick, d), 
        void 0 !== g) return g;
        return a;
    };
}), "function" != typeof Object.create && (Object.create = function(obj) {
    function F() {}
    return F.prototype = obj, new F();
}), function($, window, document, undefined) {
    var ElevateZoom = {
        init: function(options, elem) {
            var self = this;
            self.elem = elem, self.$elem = $(elem), self.imageSrc = self.$elem.data("zoom-image") ? self.$elem.data("zoom-image") : self.$elem.attr("src"), 
            self.options = $.extend({}, $.fn.elevateZoom.options, options), self.options.tint && (self.options.lensColour = "none", 
            self.options.lensOpacity = "1"), "inner" == self.options.zoomType && (self.options.showLens = !1), 
            self.$elem.parent().removeAttr("title").removeAttr("alt"), self.zoomImage = self.imageSrc, 
            self.refresh(1), $("#" + self.options.gallery + " a").click(function(e) {
                return self.options.galleryActiveClass && ($("#" + self.options.gallery + " a").removeClass(self.options.galleryActiveClass), 
                $(this).addClass(self.options.galleryActiveClass)), e.preventDefault(), $(this).data("zoom-image") ? self.zoomImagePre = $(this).data("zoom-image") : self.zoomImagePre = $(this).data("image"), 
                self.swaptheimage($(this).data("image"), self.zoomImagePre), !1;
            });
        },
        refresh: function(length) {
            var self = this;
            setTimeout(function() {
                self.fetch(self.imageSrc);
            }, length || self.options.refresh);
        },
        fetch: function(imgsrc) {
            var self = this, newImg = new Image();
            newImg.onload = function() {
                self.largeWidth = newImg.width, self.largeHeight = newImg.height, self.startZoom(), 
                self.currentImage = self.imageSrc, self.options.onZoomedImageLoaded(self.$elem);
            }, newImg.src = imgsrc;
        },
        startZoom: function() {
            var self = this;
            if (self.nzWidth = self.$elem.width(), self.nzHeight = self.$elem.height(), self.isWindowActive = !1, 
            self.isLensActive = !1, self.isTintActive = !1, self.overWindow = !1, self.options.imageCrossfade && (self.zoomWrap = self.$elem.wrap('<div style="height:' + self.nzHeight + "px;width:" + self.nzWidth + 'px;" class="zoomWrapper" />'), 
            self.$elem.css("position", "absolute")), self.zoomLock = 1, self.scrollingLock = !1, 
            self.changeBgSize = !1, self.currentZoomLevel = self.options.zoomLevel, self.nzOffset = self.$elem.offset(), 
            self.widthRatio = self.largeWidth / self.currentZoomLevel / self.nzWidth, self.heightRatio = self.largeHeight / self.currentZoomLevel / self.nzHeight, 
            "window" == self.options.zoomType && (self.zoomWindowStyle = "overflow: hidden;background-position: 0px 0px;text-align:center;background-color: " + String(self.options.zoomWindowBgColour) + ";width: " + String(self.options.zoomWindowWidth) + "px;height: " + String(self.options.zoomWindowHeight) + "px;float: left;background-size: " + self.largeWidth / self.currentZoomLevel + "px " + self.largeHeight / self.currentZoomLevel + "px;display: none;z-index:100;border: " + String(self.options.borderSize) + "px solid " + self.options.borderColour + ";background-repeat: no-repeat;position: absolute;"), 
            "inner" == self.options.zoomType) {
                var borderWidth = self.$elem.css("border-left-width");
                self.zoomWindowStyle = "overflow: hidden;margin-left: " + String(borderWidth) + ";margin-top: " + String(borderWidth) + ";background-position: 0px 0px;width: " + String(self.nzWidth) + "px;height: " + String(self.nzHeight) + "px;px;float: left;display: none;cursor:" + self.options.cursor + ";px solid " + self.options.borderColour + ";background-repeat: no-repeat;position: absolute;";
            }
            "window" == self.options.zoomType && (self.nzHeight < self.options.zoomWindowWidth / self.widthRatio ? lensHeight = self.nzHeight : lensHeight = String(self.options.zoomWindowHeight / self.heightRatio), 
            self.largeWidth < self.options.zoomWindowWidth ? lensWidth = self.nzWidth : lensWidth = self.options.zoomWindowWidth / self.widthRatio, 
            self.lensStyle = "background-position: 0px 0px;width: " + String(self.options.zoomWindowWidth / self.widthRatio) + "px;height: " + String(self.options.zoomWindowHeight / self.heightRatio) + "px;float: right;display: none;overflow: hidden;z-index: 999;-webkit-transform: translateZ(0);opacity:" + self.options.lensOpacity + ";filter: alpha(opacity = " + 100 * self.options.lensOpacity + "); zoom:1;width:" + lensWidth + "px;height:" + lensHeight + "px;background-color:" + self.options.lensColour + ";cursor:" + self.options.cursor + ";border: " + self.options.lensBorderSize + "px solid " + self.options.lensBorderColour + ";background-repeat: no-repeat;position: absolute;"), 
            self.tintStyle = "display: block;position: absolute;background-color: " + self.options.tintColour + ";filter:alpha(opacity=0);opacity: 0;width: " + self.nzWidth + "px;height: " + self.nzHeight + "px;", 
            self.lensRound = "", "lens" == self.options.zoomType && (self.lensStyle = "background-position: 0px 0px;float: left;display: none;border: " + String(self.options.borderSize) + "px solid " + self.options.borderColour + ";width:" + String(self.options.lensSize) + "px;height:" + String(self.options.lensSize) + "px;background-repeat: no-repeat;position: absolute;"), 
            "round" == self.options.lensShape && (self.lensRound = "border-top-left-radius: " + String(self.options.lensSize / 2 + self.options.borderSize) + "px;border-top-right-radius: " + String(self.options.lensSize / 2 + self.options.borderSize) + "px;border-bottom-left-radius: " + String(self.options.lensSize / 2 + self.options.borderSize) + "px;border-bottom-right-radius: " + String(self.options.lensSize / 2 + self.options.borderSize) + "px;"), 
            self.zoomContainer = $('<div class="zoomContainer" style="-webkit-transform: translateZ(0);position:absolute;left:' + self.nzOffset.left + "px;top:" + self.nzOffset.top + "px;height:" + self.nzHeight + "px;width:" + self.nzWidth + 'px;"></div>'), 
            $("body").append(self.zoomContainer), self.options.containLensZoom && "lens" == self.options.zoomType && self.zoomContainer.css("overflow", "hidden"), 
            "inner" != self.options.zoomType && (self.zoomLens = $("<div class='zoomLens' style='" + self.lensStyle + self.lensRound + "'>&nbsp;</div>").appendTo(self.zoomContainer).click(function() {
                self.$elem.trigger("click");
            }), self.options.tint && (self.tintContainer = $("<div/>").addClass("tintContainer"), 
            self.zoomTint = $("<div class='zoomTint' style='" + self.tintStyle + "'></div>"), 
            self.zoomLens.wrap(self.tintContainer), self.zoomTintcss = self.zoomLens.after(self.zoomTint), 
            self.zoomTintImage = $('<img style="position: absolute; left: 0px; top: 0px; max-width: none; width: ' + self.nzWidth + "px; height: " + self.nzHeight + 'px;" src="' + self.imageSrc + '">').appendTo(self.zoomLens).click(function() {
                self.$elem.trigger("click");
            }))), isNaN(self.options.zoomWindowPosition) ? self.zoomWindow = $("<div style='z-index:999;left:" + self.windowOffsetLeft + "px;top:" + self.windowOffsetTop + "px;" + self.zoomWindowStyle + "' class='zoomWindow'>&nbsp;</div>").appendTo("body").click(function() {
                self.$elem.trigger("click");
            }) : self.zoomWindow = $("<div style='z-index:999;left:" + self.windowOffsetLeft + "px;top:" + self.windowOffsetTop + "px;" + self.zoomWindowStyle + "' class='zoomWindow'>&nbsp;</div>").appendTo(self.zoomContainer).click(function() {
                self.$elem.trigger("click");
            }), self.zoomWindowContainer = $("<div/>").addClass("zoomWindowContainer").css("width", self.options.zoomWindowWidth), 
            self.zoomWindow.wrap(self.zoomWindowContainer), "lens" == self.options.zoomType && self.zoomLens.css({
                backgroundImage: "url('" + self.imageSrc + "')"
            }), "window" == self.options.zoomType && self.zoomWindow.css({
                backgroundImage: "url('" + self.imageSrc + "')"
            }), "inner" == self.options.zoomType && self.zoomWindow.css({
                backgroundImage: "url('" + self.imageSrc + "')"
            }), self.$elem.bind("touchmove", function(e) {
                e.preventDefault();
                var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
                self.setPosition(touch);
            }), self.zoomContainer.bind("touchmove", function(e) {
                "inner" == self.options.zoomType && self.showHideWindow("show"), e.preventDefault();
                var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
                self.setPosition(touch);
            }), self.zoomContainer.bind("touchend", function(e) {
                self.showHideWindow("hide"), self.options.showLens && self.showHideLens("hide"), 
                self.options.tint && "inner" != self.options.zoomType && self.showHideTint("hide");
            }), self.$elem.bind("touchend", function(e) {
                self.showHideWindow("hide"), self.options.showLens && self.showHideLens("hide"), 
                self.options.tint && "inner" != self.options.zoomType && self.showHideTint("hide");
            }), self.options.showLens && (self.zoomLens.bind("touchmove", function(e) {
                e.preventDefault();
                var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
                self.setPosition(touch);
            }), self.zoomLens.bind("touchend", function(e) {
                self.showHideWindow("hide"), self.options.showLens && self.showHideLens("hide"), 
                self.options.tint && "inner" != self.options.zoomType && self.showHideTint("hide");
            })), self.$elem.bind("mousemove", function(e) {
                0 == self.overWindow && self.setElements("show"), self.lastX === e.clientX && self.lastY === e.clientY || (self.setPosition(e), 
                self.currentLoc = e), self.lastX = e.clientX, self.lastY = e.clientY;
            }), self.zoomContainer.bind("mousemove", function(e) {
                0 == self.overWindow && self.setElements("show"), self.lastX === e.clientX && self.lastY === e.clientY || (self.setPosition(e), 
                self.currentLoc = e), self.lastX = e.clientX, self.lastY = e.clientY;
            }), "inner" != self.options.zoomType && self.zoomLens.bind("mousemove", function(e) {
                self.lastX === e.clientX && self.lastY === e.clientY || (self.setPosition(e), self.currentLoc = e), 
                self.lastX = e.clientX, self.lastY = e.clientY;
            }), self.options.tint && "inner" != self.options.zoomType && self.zoomTint.bind("mousemove", function(e) {
                self.lastX === e.clientX && self.lastY === e.clientY || (self.setPosition(e), self.currentLoc = e), 
                self.lastX = e.clientX, self.lastY = e.clientY;
            }), "inner" == self.options.zoomType && self.zoomWindow.bind("mousemove", function(e) {
                self.lastX === e.clientX && self.lastY === e.clientY || (self.setPosition(e), self.currentLoc = e), 
                self.lastX = e.clientX, self.lastY = e.clientY;
            }), self.zoomContainer.add(self.$elem).mouseenter(function() {
                0 == self.overWindow && self.setElements("show");
            }).mouseleave(function() {
                self.scrollLock || (self.setElements("hide"), self.options.onDestroy(self.$elem));
            }), "inner" != self.options.zoomType && self.zoomWindow.mouseenter(function() {
                self.overWindow = !0, self.setElements("hide");
            }).mouseleave(function() {
                self.overWindow = !1;
            }), self.options.zoomLevel, self.options.minZoomLevel ? self.minZoomLevel = self.options.minZoomLevel : self.minZoomLevel = 2 * self.options.scrollZoomIncrement, 
            self.options.scrollZoom && self.zoomContainer.add(self.$elem).bind("mousewheel DOMMouseScroll MozMousePixelScroll", function(e) {
                self.scrollLock = !0, clearTimeout($.data(this, "timer")), $.data(this, "timer", setTimeout(function() {
                    self.scrollLock = !1;
                }, 250));
                var theEvent = e.originalEvent.wheelDelta || -1 * e.originalEvent.detail;
                return e.stopImmediatePropagation(), e.stopPropagation(), e.preventDefault(), theEvent / 120 > 0 ? self.currentZoomLevel >= self.minZoomLevel && self.changeZoomLevel(self.currentZoomLevel - self.options.scrollZoomIncrement) : self.options.maxZoomLevel ? self.currentZoomLevel <= self.options.maxZoomLevel && self.changeZoomLevel(parseFloat(self.currentZoomLevel) + self.options.scrollZoomIncrement) : self.changeZoomLevel(parseFloat(self.currentZoomLevel) + self.options.scrollZoomIncrement), 
                !1;
            });
        },
        setElements: function(type) {
            var self = this;
            if (!self.options.zoomEnabled) return !1;
            "show" == type && self.isWindowSet && ("inner" == self.options.zoomType && self.showHideWindow("show"), 
            "window" == self.options.zoomType && self.showHideWindow("show"), self.options.showLens && self.showHideLens("show"), 
            self.options.tint && "inner" != self.options.zoomType && self.showHideTint("show")), 
            "hide" == type && ("window" == self.options.zoomType && self.showHideWindow("hide"), 
            self.options.tint || self.showHideWindow("hide"), self.options.showLens && self.showHideLens("hide"), 
            self.options.tint && self.showHideTint("hide"));
        },
        setPosition: function(e) {
            var self = this;
            return !!self.options.zoomEnabled && (self.nzHeight = self.$elem.height(), self.nzWidth = self.$elem.width(), 
            self.nzOffset = self.$elem.offset(), self.options.tint && "inner" != self.options.zoomType && (self.zoomTint.css({
                top: 0
            }), self.zoomTint.css({
                left: 0
            })), self.options.responsive && !self.options.scrollZoom && self.options.showLens && (self.nzHeight < self.options.zoomWindowWidth / self.widthRatio ? lensHeight = self.nzHeight : lensHeight = String(self.options.zoomWindowHeight / self.heightRatio), 
            self.largeWidth < self.options.zoomWindowWidth ? lensWidth = self.nzWidth : lensWidth = self.options.zoomWindowWidth / self.widthRatio, 
            self.widthRatio = self.largeWidth / self.nzWidth, self.heightRatio = self.largeHeight / self.nzHeight, 
            "lens" != self.options.zoomType && (self.nzHeight < self.options.zoomWindowWidth / self.widthRatio ? lensHeight = self.nzHeight : lensHeight = String(self.options.zoomWindowHeight / self.heightRatio), 
            self.nzWidth < self.options.zoomWindowHeight / self.heightRatio ? lensWidth = self.nzWidth : lensWidth = String(self.options.zoomWindowWidth / self.widthRatio), 
            self.zoomLens.css("width", lensWidth), self.zoomLens.css("height", lensHeight), 
            self.options.tint && (self.zoomTintImage.css("width", self.nzWidth), self.zoomTintImage.css("height", self.nzHeight))), 
            "lens" == self.options.zoomType && self.zoomLens.css({
                width: String(self.options.lensSize) + "px",
                height: String(self.options.lensSize) + "px"
            })), self.zoomContainer.css({
                top: self.nzOffset.top
            }), self.zoomContainer.css({
                left: self.nzOffset.left
            }), self.mouseLeft = parseInt(e.pageX - self.nzOffset.left), self.mouseTop = parseInt(e.pageY - self.nzOffset.top), 
            "window" == self.options.zoomType && (self.Etoppos = self.mouseTop < self.zoomLens.height() / 2, 
            self.Eboppos = self.mouseTop > self.nzHeight - self.zoomLens.height() / 2 - 2 * self.options.lensBorderSize, 
            self.Eloppos = self.mouseLeft < 0 + self.zoomLens.width() / 2, self.Eroppos = self.mouseLeft > self.nzWidth - self.zoomLens.width() / 2 - 2 * self.options.lensBorderSize), 
            "inner" == self.options.zoomType && (self.Etoppos = self.mouseTop < self.nzHeight / 2 / self.heightRatio, 
            self.Eboppos = self.mouseTop > self.nzHeight - self.nzHeight / 2 / self.heightRatio, 
            self.Eloppos = self.mouseLeft < 0 + self.nzWidth / 2 / self.widthRatio, self.Eroppos = self.mouseLeft > self.nzWidth - self.nzWidth / 2 / self.widthRatio - 2 * self.options.lensBorderSize), 
            self.mouseLeft < 0 || self.mouseTop < 0 || self.mouseLeft > self.nzWidth || self.mouseTop > self.nzHeight ? void self.setElements("hide") : (self.options.showLens && (self.lensLeftPos = String(Math.floor(self.mouseLeft - self.zoomLens.width() / 2)), 
            self.lensTopPos = String(Math.floor(self.mouseTop - self.zoomLens.height() / 2))), 
            self.Etoppos && (self.lensTopPos = 0), self.Eloppos && (self.windowLeftPos = 0, 
            self.lensLeftPos = 0, self.tintpos = 0), "window" == self.options.zoomType && (self.Eboppos && (self.lensTopPos = Math.max(self.nzHeight - self.zoomLens.height() - 2 * self.options.lensBorderSize, 0)), 
            self.Eroppos && (self.lensLeftPos = self.nzWidth - self.zoomLens.width() - 2 * self.options.lensBorderSize)), 
            "inner" == self.options.zoomType && (self.Eboppos && (self.lensTopPos = Math.max(self.nzHeight - 2 * self.options.lensBorderSize, 0)), 
            self.Eroppos && (self.lensLeftPos = self.nzWidth - self.nzWidth - 2 * self.options.lensBorderSize)), 
            "lens" == self.options.zoomType && (self.windowLeftPos = String(-1 * ((e.pageX - self.nzOffset.left) * self.widthRatio - self.zoomLens.width() / 2)), 
            self.windowTopPos = String(-1 * ((e.pageY - self.nzOffset.top) * self.heightRatio - self.zoomLens.height() / 2)), 
            self.zoomLens.css({
                backgroundPosition: self.windowLeftPos + "px " + self.windowTopPos + "px"
            }), self.changeBgSize && (self.nzHeight > self.nzWidth ? ("lens" == self.options.zoomType && self.zoomLens.css({
                "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
            }), self.zoomWindow.css({
                "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
            })) : ("lens" == self.options.zoomType && self.zoomLens.css({
                "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvaluewidth + "px"
            }), self.zoomWindow.css({
                "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvaluewidth + "px"
            })), self.changeBgSize = !1), self.setWindowPostition(e)), self.options.tint && "inner" != self.options.zoomType && self.setTintPosition(e), 
            "window" == self.options.zoomType && self.setWindowPostition(e), "inner" == self.options.zoomType && self.setWindowPostition(e), 
            self.options.showLens && (self.fullwidth && "lens" != self.options.zoomType && (self.lensLeftPos = 0), 
            self.zoomLens.css({
                left: self.lensLeftPos + "px",
                top: self.lensTopPos + "px"
            })), void 0));
        },
        showHideWindow: function(change) {
            var self = this;
            "show" == change && (self.isWindowActive || (self.options.zoomWindowFadeIn ? self.zoomWindow.stop(!0, !0, !1).fadeIn(self.options.zoomWindowFadeIn) : self.zoomWindow.show(), 
            self.isWindowActive = !0)), "hide" == change && self.isWindowActive && (self.options.zoomWindowFadeOut ? self.zoomWindow.stop(!0, !0).fadeOut(self.options.zoomWindowFadeOut, function() {
                self.loop && (clearInterval(self.loop), self.loop = !1);
            }) : self.zoomWindow.hide(), self.isWindowActive = !1);
        },
        showHideLens: function(change) {
            var self = this;
            "show" == change && (self.isLensActive || (self.options.lensFadeIn ? self.zoomLens.stop(!0, !0, !1).fadeIn(self.options.lensFadeIn) : self.zoomLens.show(), 
            self.isLensActive = !0)), "hide" == change && self.isLensActive && (self.options.lensFadeOut ? self.zoomLens.stop(!0, !0).fadeOut(self.options.lensFadeOut) : self.zoomLens.hide(), 
            self.isLensActive = !1);
        },
        showHideTint: function(change) {
            var self = this;
            "show" == change && (self.isTintActive || (self.options.zoomTintFadeIn ? self.zoomTint.css({
                opacity: self.options.tintOpacity
            }).animate().stop(!0, !0).fadeIn("slow") : (self.zoomTint.css({
                opacity: self.options.tintOpacity
            }).animate(), self.zoomTint.show()), self.isTintActive = !0)), "hide" == change && self.isTintActive && (self.options.zoomTintFadeOut ? self.zoomTint.stop(!0, !0).fadeOut(self.options.zoomTintFadeOut) : self.zoomTint.hide(), 
            self.isTintActive = !1);
        },
        setLensPostition: function(e) {},
        setWindowPostition: function(e) {
            var self = this;
            if (isNaN(self.options.zoomWindowPosition)) self.externalContainer = $("#" + self.options.zoomWindowPosition), 
            self.externalContainerWidth = self.externalContainer.width(), self.externalContainerHeight = self.externalContainer.height(), 
            self.externalContainerOffset = self.externalContainer.offset(), self.windowOffsetTop = self.externalContainerOffset.top, 
            self.windowOffsetLeft = self.externalContainerOffset.left; else switch (self.options.zoomWindowPosition) {
              case 1:
                self.windowOffsetTop = self.options.zoomWindowOffety, self.windowOffsetLeft = +self.nzWidth;
                break;

              case 2:
                self.options.zoomWindowHeight > self.nzHeight && (self.windowOffsetTop = -1 * (self.options.zoomWindowHeight / 2 - self.nzHeight / 2), 
                self.windowOffsetLeft = self.nzWidth);
                break;

              case 3:
                self.windowOffsetTop = self.nzHeight - self.zoomWindow.height() - 2 * self.options.borderSize, 
                self.windowOffsetLeft = self.nzWidth;
                break;

              case 4:
                self.windowOffsetTop = self.nzHeight, self.windowOffsetLeft = self.nzWidth;
                break;

              case 5:
                self.windowOffsetTop = self.nzHeight, self.windowOffsetLeft = self.nzWidth - self.zoomWindow.width() - 2 * self.options.borderSize;
                break;

              case 6:
                self.options.zoomWindowHeight > self.nzHeight && (self.windowOffsetTop = self.nzHeight, 
                self.windowOffsetLeft = -1 * (self.options.zoomWindowWidth / 2 - self.nzWidth / 2 + 2 * self.options.borderSize));
                break;

              case 7:
                self.windowOffsetTop = self.nzHeight, self.windowOffsetLeft = 0;
                break;

              case 8:
                self.windowOffsetTop = self.nzHeight, self.windowOffsetLeft = -1 * (self.zoomWindow.width() + 2 * self.options.borderSize);
                break;

              case 9:
                self.windowOffsetTop = self.nzHeight - self.zoomWindow.height() - 2 * self.options.borderSize, 
                self.windowOffsetLeft = -1 * (self.zoomWindow.width() + 2 * self.options.borderSize);
                break;

              case 10:
                self.options.zoomWindowHeight > self.nzHeight && (self.windowOffsetTop = -1 * (self.options.zoomWindowHeight / 2 - self.nzHeight / 2), 
                self.windowOffsetLeft = -1 * (self.zoomWindow.width() + 2 * self.options.borderSize));
                break;

              case 11:
                self.windowOffsetTop = self.options.zoomWindowOffety, self.windowOffsetLeft = -1 * (self.zoomWindow.width() + 2 * self.options.borderSize);
                break;

              case 12:
                self.windowOffsetTop = -1 * (self.zoomWindow.height() + 2 * self.options.borderSize), 
                self.windowOffsetLeft = -1 * (self.zoomWindow.width() + 2 * self.options.borderSize);
                break;

              case 13:
                self.windowOffsetTop = -1 * (self.zoomWindow.height() + 2 * self.options.borderSize), 
                self.windowOffsetLeft = 0;
                break;

              case 14:
                self.options.zoomWindowHeight > self.nzHeight && (self.windowOffsetTop = -1 * (self.zoomWindow.height() + 2 * self.options.borderSize), 
                self.windowOffsetLeft = -1 * (self.options.zoomWindowWidth / 2 - self.nzWidth / 2 + 2 * self.options.borderSize));
                break;

              case 15:
                self.windowOffsetTop = -1 * (self.zoomWindow.height() + 2 * self.options.borderSize), 
                self.windowOffsetLeft = self.nzWidth - self.zoomWindow.width() - 2 * self.options.borderSize;
                break;

              case 16:
                self.windowOffsetTop = -1 * (self.zoomWindow.height() + 2 * self.options.borderSize), 
                self.windowOffsetLeft = self.nzWidth;
                break;

              default:
                self.windowOffsetTop = self.options.zoomWindowOffety, self.windowOffsetLeft = self.nzWidth;
            }
            self.isWindowSet = !0, self.windowOffsetTop = self.windowOffsetTop + self.options.zoomWindowOffety, 
            self.windowOffsetLeft = self.windowOffsetLeft + self.options.zoomWindowOffetx, self.zoomWindow.css({
                top: self.windowOffsetTop
            }), self.zoomWindow.css({
                left: self.windowOffsetLeft
            }), "inner" == self.options.zoomType && (self.zoomWindow.css({
                top: 0
            }), self.zoomWindow.css({
                left: 0
            })), self.windowLeftPos = String(-1 * ((e.pageX - self.nzOffset.left) * self.widthRatio - self.zoomWindow.width() / 2)), 
            self.windowTopPos = String(-1 * ((e.pageY - self.nzOffset.top) * self.heightRatio - self.zoomWindow.height() / 2)), 
            self.Etoppos && (self.windowTopPos = 0), self.Eloppos && (self.windowLeftPos = 0), 
            self.Eboppos && (self.windowTopPos = -1 * (self.largeHeight / self.currentZoomLevel - self.zoomWindow.height())), 
            self.Eroppos && (self.windowLeftPos = -1 * (self.largeWidth / self.currentZoomLevel - self.zoomWindow.width())), 
            self.fullheight && (self.windowTopPos = 0), self.fullwidth && (self.windowLeftPos = 0), 
            "window" != self.options.zoomType && "inner" != self.options.zoomType || (1 == self.zoomLock && (self.widthRatio <= 1 && (self.windowLeftPos = 0), 
            self.heightRatio <= 1 && (self.windowTopPos = 0)), "window" == self.options.zoomType && (self.largeHeight < self.options.zoomWindowHeight && (self.windowTopPos = 0), 
            self.largeWidth < self.options.zoomWindowWidth && (self.windowLeftPos = 0)), self.options.easing ? (self.xp || (self.xp = 0), 
            self.yp || (self.yp = 0), self.loop || (self.loop = setInterval(function() {
                self.xp += (self.windowLeftPos - self.xp) / self.options.easingAmount, self.yp += (self.windowTopPos - self.yp) / self.options.easingAmount, 
                self.scrollingLock ? (clearInterval(self.loop), self.xp = self.windowLeftPos, self.yp = self.windowTopPos, 
                self.xp = -1 * ((e.pageX - self.nzOffset.left) * self.widthRatio - self.zoomWindow.width() / 2), 
                self.yp = -1 * ((e.pageY - self.nzOffset.top) * self.heightRatio - self.zoomWindow.height() / 2), 
                self.changeBgSize && (self.nzHeight > self.nzWidth ? ("lens" == self.options.zoomType && self.zoomLens.css({
                    "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
                }), self.zoomWindow.css({
                    "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
                })) : ("lens" != self.options.zoomType && self.zoomLens.css({
                    "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvalueheight + "px"
                }), self.zoomWindow.css({
                    "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvaluewidth + "px"
                })), self.changeBgSize = !1), self.zoomWindow.css({
                    backgroundPosition: self.windowLeftPos + "px " + self.windowTopPos + "px"
                }), self.scrollingLock = !1, self.loop = !1) : Math.round(Math.abs(self.xp - self.windowLeftPos) + Math.abs(self.yp - self.windowTopPos)) < 1 ? (clearInterval(self.loop), 
                self.zoomWindow.css({
                    backgroundPosition: self.windowLeftPos + "px " + self.windowTopPos + "px"
                }), self.loop = !1) : (self.changeBgSize && (self.nzHeight > self.nzWidth ? ("lens" == self.options.zoomType && self.zoomLens.css({
                    "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
                }), self.zoomWindow.css({
                    "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
                })) : ("lens" != self.options.zoomType && self.zoomLens.css({
                    "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvaluewidth + "px"
                }), self.zoomWindow.css({
                    "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvaluewidth + "px"
                })), self.changeBgSize = !1), self.zoomWindow.css({
                    backgroundPosition: self.xp + "px " + self.yp + "px"
                }));
            }, 16))) : (self.changeBgSize && (self.nzHeight > self.nzWidth ? ("lens" == self.options.zoomType && self.zoomLens.css({
                "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
            }), self.zoomWindow.css({
                "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
            })) : ("lens" == self.options.zoomType && self.zoomLens.css({
                "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvaluewidth + "px"
            }), self.largeHeight / self.newvaluewidth < self.options.zoomWindowHeight ? self.zoomWindow.css({
                "background-size": self.largeWidth / self.newvaluewidth + "px " + self.largeHeight / self.newvaluewidth + "px"
            }) : self.zoomWindow.css({
                "background-size": self.largeWidth / self.newvalueheight + "px " + self.largeHeight / self.newvalueheight + "px"
            })), self.changeBgSize = !1), self.zoomWindow.css({
                backgroundPosition: self.windowLeftPos + "px " + self.windowTopPos + "px"
            })));
        },
        setTintPosition: function(e) {
            var self = this;
            self.nzOffset = self.$elem.offset(), self.tintpos = String(-1 * (e.pageX - self.nzOffset.left - self.zoomLens.width() / 2)), 
            self.tintposy = String(-1 * (e.pageY - self.nzOffset.top - self.zoomLens.height() / 2)), 
            self.Etoppos && (self.tintposy = 0), self.Eloppos && (self.tintpos = 0), self.Eboppos && (self.tintposy = -1 * (self.nzHeight - self.zoomLens.height() - 2 * self.options.lensBorderSize)), 
            self.Eroppos && (self.tintpos = -1 * (self.nzWidth - self.zoomLens.width() - 2 * self.options.lensBorderSize)), 
            self.options.tint && (self.fullheight && (self.tintposy = 0), self.fullwidth && (self.tintpos = 0), 
            self.zoomTintImage.css({
                left: self.tintpos + "px"
            }), self.zoomTintImage.css({
                top: self.tintposy + "px"
            }));
        },
        swaptheimage: function(smallimage, largeimage) {
            var self = this, newImg = new Image();
            self.options.loadingIcon && (self.spinner = $("<div style=\"background: url('" + self.options.loadingIcon + "') no-repeat center;height:" + self.nzHeight + "px;width:" + self.nzWidth + 'px;z-index: 2000;position: absolute; background-position: center center;"></div>'), 
            self.$elem.after(self.spinner)), self.options.onImageSwap(self.$elem), newImg.onload = function() {
                self.largeWidth = newImg.width, self.largeHeight = newImg.height, self.zoomImage = largeimage, 
                self.zoomWindow.css({
                    "background-size": self.largeWidth + "px " + self.largeHeight + "px"
                }), self.swapAction(smallimage, largeimage);
            }, newImg.src = largeimage;
        },
        swapAction: function(smallimage, largeimage) {
            var self = this, newImg2 = new Image();
            if (newImg2.onload = function() {
                self.nzHeight = newImg2.height, self.nzWidth = newImg2.width, self.options.onImageSwapComplete(self.$elem), 
                self.doneCallback();
            }, newImg2.src = smallimage, self.currentZoomLevel = self.options.zoomLevel, self.options.maxZoomLevel = !1, 
            "lens" == self.options.zoomType && self.zoomLens.css({
                backgroundImage: "url('" + largeimage + "')"
            }), "window" == self.options.zoomType && self.zoomWindow.css({
                backgroundImage: "url('" + largeimage + "')"
            }), "inner" == self.options.zoomType && self.zoomWindow.css({
                backgroundImage: "url('" + largeimage + "')"
            }), self.currentImage = largeimage, self.options.imageCrossfade) {
                var oldImg = self.$elem, newImg = oldImg.clone();
                if (self.$elem.attr("src", smallimage), self.$elem.after(newImg), newImg.stop(!0).fadeOut(self.options.imageCrossfade, function() {
                    $(this).remove();
                }), self.$elem.width("auto").removeAttr("width"), self.$elem.height("auto").removeAttr("height"), 
                oldImg.fadeIn(self.options.imageCrossfade), self.options.tint && "inner" != self.options.zoomType) {
                    var oldImgTint = self.zoomTintImage, newImgTint = oldImgTint.clone();
                    self.zoomTintImage.attr("src", largeimage), self.zoomTintImage.after(newImgTint), 
                    newImgTint.stop(!0).fadeOut(self.options.imageCrossfade, function() {
                        $(this).remove();
                    }), oldImgTint.fadeIn(self.options.imageCrossfade), self.zoomTint.css({
                        height: self.$elem.height()
                    }), self.zoomTint.css({
                        width: self.$elem.width()
                    });
                }
                self.zoomContainer.css("height", self.$elem.height()), self.zoomContainer.css("width", self.$elem.width()), 
                "inner" == self.options.zoomType && (self.options.constrainType || (self.zoomWrap.parent().css("height", self.$elem.height()), 
                self.zoomWrap.parent().css("width", self.$elem.width()), self.zoomWindow.css("height", self.$elem.height()), 
                self.zoomWindow.css("width", self.$elem.width()))), self.options.imageCrossfade && (self.zoomWrap.css("height", self.$elem.height()), 
                self.zoomWrap.css("width", self.$elem.width()));
            } else self.$elem.attr("src", smallimage), self.options.tint && (self.zoomTintImage.attr("src", largeimage), 
            self.zoomTintImage.attr("height", self.$elem.height()), self.zoomTintImage.css({
                height: self.$elem.height()
            }), self.zoomTint.css({
                height: self.$elem.height()
            })), self.zoomContainer.css("height", self.$elem.height()), self.zoomContainer.css("width", self.$elem.width()), 
            self.options.imageCrossfade && (self.zoomWrap.css("height", self.$elem.height()), 
            self.zoomWrap.css("width", self.$elem.width()));
            self.options.constrainType && ("height" == self.options.constrainType && (self.zoomContainer.css("height", self.options.constrainSize), 
            self.zoomContainer.css("width", "auto"), self.options.imageCrossfade ? (self.zoomWrap.css("height", self.options.constrainSize), 
            self.zoomWrap.css("width", "auto"), self.constwidth = self.zoomWrap.width()) : (self.$elem.css("height", self.options.constrainSize), 
            self.$elem.css("width", "auto"), self.constwidth = self.$elem.width()), "inner" == self.options.zoomType && (self.zoomWrap.parent().css("height", self.options.constrainSize), 
            self.zoomWrap.parent().css("width", self.constwidth), self.zoomWindow.css("height", self.options.constrainSize), 
            self.zoomWindow.css("width", self.constwidth)), self.options.tint && (self.tintContainer.css("height", self.options.constrainSize), 
            self.tintContainer.css("width", self.constwidth), self.zoomTint.css("height", self.options.constrainSize), 
            self.zoomTint.css("width", self.constwidth), self.zoomTintImage.css("height", self.options.constrainSize), 
            self.zoomTintImage.css("width", self.constwidth))), "width" == self.options.constrainType && (self.zoomContainer.css("height", "auto"), 
            self.zoomContainer.css("width", self.options.constrainSize), self.options.imageCrossfade ? (self.zoomWrap.css("height", "auto"), 
            self.zoomWrap.css("width", self.options.constrainSize), self.constheight = self.zoomWrap.height()) : (self.$elem.css("height", "auto"), 
            self.$elem.css("width", self.options.constrainSize), self.constheight = self.$elem.height()), 
            "inner" == self.options.zoomType && (self.zoomWrap.parent().css("height", self.constheight), 
            self.zoomWrap.parent().css("width", self.options.constrainSize), self.zoomWindow.css("height", self.constheight), 
            self.zoomWindow.css("width", self.options.constrainSize)), self.options.tint && (self.tintContainer.css("height", self.constheight), 
            self.tintContainer.css("width", self.options.constrainSize), self.zoomTint.css("height", self.constheight), 
            self.zoomTint.css("width", self.options.constrainSize), self.zoomTintImage.css("height", self.constheight), 
            self.zoomTintImage.css("width", self.options.constrainSize))));
        },
        doneCallback: function() {
            var self = this;
            self.options.loadingIcon && self.spinner.hide(), self.nzOffset = self.$elem.offset(), 
            self.nzWidth = self.$elem.width(), self.nzHeight = self.$elem.height(), self.currentZoomLevel = self.options.zoomLevel, 
            self.widthRatio = self.largeWidth / self.nzWidth, self.heightRatio = self.largeHeight / self.nzHeight, 
            "window" == self.options.zoomType && (self.nzHeight < self.options.zoomWindowWidth / self.widthRatio ? lensHeight = self.nzHeight : lensHeight = String(self.options.zoomWindowHeight / self.heightRatio), 
            self.options.zoomWindowWidth < self.options.zoomWindowWidth ? lensWidth = self.nzWidth : lensWidth = self.options.zoomWindowWidth / self.widthRatio, 
            self.zoomLens && (self.zoomLens.css("width", lensWidth), self.zoomLens.css("height", lensHeight)));
        },
        getCurrentImage: function() {
            return this.zoomImage;
        },
        getGalleryList: function() {
            var self = this;
            return self.gallerylist = [], self.options.gallery ? $("#" + self.options.gallery + " a").each(function() {
                var img_src = "";
                $(this).data("zoom-image") ? img_src = $(this).data("zoom-image") : $(this).data("image") && (img_src = $(this).data("image")), 
                img_src == self.zoomImage ? self.gallerylist.unshift({
                    href: "" + img_src,
                    title: $(this).find("img").attr("title")
                }) : self.gallerylist.push({
                    href: "" + img_src,
                    title: $(this).find("img").attr("title")
                });
            }) : self.gallerylist.push({
                href: "" + self.zoomImage,
                title: $(this).find("img").attr("title")
            }), self.gallerylist;
        },
        changeZoomLevel: function(value) {
            var self = this;
            self.scrollingLock = !0, self.newvalue = parseFloat(value).toFixed(2), newvalue = parseFloat(value).toFixed(2), 
            maxheightnewvalue = self.largeHeight / (self.options.zoomWindowHeight / self.nzHeight * self.nzHeight), 
            maxwidthtnewvalue = self.largeWidth / (self.options.zoomWindowWidth / self.nzWidth * self.nzWidth), 
            "inner" != self.options.zoomType && (maxheightnewvalue <= newvalue ? (self.heightRatio = self.largeHeight / maxheightnewvalue / self.nzHeight, 
            self.newvalueheight = maxheightnewvalue, self.fullheight = !0) : (self.heightRatio = self.largeHeight / newvalue / self.nzHeight, 
            self.newvalueheight = newvalue, self.fullheight = !1), maxwidthtnewvalue <= newvalue ? (self.widthRatio = self.largeWidth / maxwidthtnewvalue / self.nzWidth, 
            self.newvaluewidth = maxwidthtnewvalue, self.fullwidth = !0) : (self.widthRatio = self.largeWidth / newvalue / self.nzWidth, 
            self.newvaluewidth = newvalue, self.fullwidth = !1), "lens" == self.options.zoomType && (maxheightnewvalue <= newvalue ? (self.fullwidth = !0, 
            self.newvaluewidth = maxheightnewvalue) : (self.widthRatio = self.largeWidth / newvalue / self.nzWidth, 
            self.newvaluewidth = newvalue, self.fullwidth = !1))), "inner" == self.options.zoomType && (maxheightnewvalue = parseFloat(self.largeHeight / self.nzHeight).toFixed(2), 
            maxwidthtnewvalue = parseFloat(self.largeWidth / self.nzWidth).toFixed(2), newvalue > maxheightnewvalue && (newvalue = maxheightnewvalue), 
            newvalue > maxwidthtnewvalue && (newvalue = maxwidthtnewvalue), maxheightnewvalue <= newvalue ? (self.heightRatio = self.largeHeight / newvalue / self.nzHeight, 
            newvalue > maxheightnewvalue ? self.newvalueheight = maxheightnewvalue : self.newvalueheight = newvalue, 
            self.fullheight = !0) : (self.heightRatio = self.largeHeight / newvalue / self.nzHeight, 
            newvalue > maxheightnewvalue ? self.newvalueheight = maxheightnewvalue : self.newvalueheight = newvalue, 
            self.fullheight = !1), maxwidthtnewvalue <= newvalue ? (self.widthRatio = self.largeWidth / newvalue / self.nzWidth, 
            newvalue > maxwidthtnewvalue ? self.newvaluewidth = maxwidthtnewvalue : self.newvaluewidth = newvalue, 
            self.fullwidth = !0) : (self.widthRatio = self.largeWidth / newvalue / self.nzWidth, 
            self.newvaluewidth = newvalue, self.fullwidth = !1)), scrcontinue = !1, "inner" == self.options.zoomType && (self.nzWidth >= self.nzHeight && (self.newvaluewidth <= maxwidthtnewvalue ? scrcontinue = !0 : (scrcontinue = !1, 
            self.fullheight = !0, self.fullwidth = !0)), self.nzHeight > self.nzWidth && (self.newvaluewidth <= maxwidthtnewvalue ? scrcontinue = !0 : (scrcontinue = !1, 
            self.fullheight = !0, self.fullwidth = !0))), "inner" != self.options.zoomType && (scrcontinue = !0), 
            scrcontinue && (self.zoomLock = 0, self.changeZoom = !0, self.options.zoomWindowHeight / self.heightRatio <= self.nzHeight && (self.currentZoomLevel = self.newvalueheight, 
            "lens" != self.options.zoomType && "inner" != self.options.zoomType && (self.changeBgSize = !0, 
            self.zoomLens.css({
                height: String(self.options.zoomWindowHeight / self.heightRatio) + "px"
            })), "lens" != self.options.zoomType && "inner" != self.options.zoomType || (self.changeBgSize = !0)), 
            self.options.zoomWindowWidth / self.widthRatio <= self.nzWidth && ("inner" != self.options.zoomType && self.newvaluewidth > self.newvalueheight && (self.currentZoomLevel = self.newvaluewidth), 
            "lens" != self.options.zoomType && "inner" != self.options.zoomType && (self.changeBgSize = !0, 
            self.zoomLens.css({
                width: String(self.options.zoomWindowWidth / self.widthRatio) + "px"
            })), "lens" != self.options.zoomType && "inner" != self.options.zoomType || (self.changeBgSize = !0)), 
            "inner" == self.options.zoomType && (self.changeBgSize = !0, self.nzWidth > self.nzHeight && (self.currentZoomLevel = self.newvaluewidth), 
            self.nzHeight > self.nzWidth && (self.currentZoomLevel = self.newvaluewidth))), 
            self.setPosition(self.currentLoc);
        },
        closeAll: function() {
            self.zoomWindow && self.zoomWindow.hide(), self.zoomLens && self.zoomLens.hide(), 
            self.zoomTint && self.zoomTint.hide();
        },
        changeState: function(value) {
            var self = this;
            "enable" == value && (self.options.zoomEnabled = !0), "disable" == value && (self.options.zoomEnabled = !1);
        }
    };
    $.fn.elevateZoom = function(options) {
        return this.each(function() {
            var elevate = Object.create(ElevateZoom);
            elevate.init(options, this), $.data(this, "elevateZoom", elevate);
        });
    }, $.fn.elevateZoom.options = {
        zoomActivation: "hover",
        zoomEnabled: !0,
        preloading: 1,
        zoomLevel: 1,
        scrollZoom: !1,
        scrollZoomIncrement: .1,
        minZoomLevel: !1,
        maxZoomLevel: !1,
        easing: !1,
        easingAmount: 12,
        lensSize: 200,
        zoomWindowWidth: 400,
        zoomWindowHeight: 400,
        zoomWindowOffetx: 0,
        zoomWindowOffety: 0,
        zoomWindowPosition: 1,
        zoomWindowBgColour: "#fff",
        lensFadeIn: !1,
        lensFadeOut: !1,
        debug: !1,
        zoomWindowFadeIn: !1,
        zoomWindowFadeOut: !1,
        zoomWindowAlwaysShow: !1,
        zoomTintFadeIn: !1,
        zoomTintFadeOut: !1,
        borderSize: 4,
        showLens: !0,
        borderColour: "#888",
        lensBorderSize: 1,
        lensBorderColour: "#000",
        lensShape: "square",
        zoomType: "window",
        containLensZoom: !1,
        lensColour: "white",
        lensOpacity: .4,
        lenszoom: !1,
        tint: !1,
        tintColour: "#333",
        tintOpacity: .4,
        gallery: !1,
        galleryActiveClass: "zoomGalleryActive",
        imageCrossfade: !1,
        constrainType: !1,
        constrainSize: !1,
        loadingIcon: !1,
        cursor: "default",
        responsive: !0,
        onComplete: $.noop,
        onDestroy: function() {},
        onZoomedImageLoaded: function() {},
        onImageSwap: $.noop,
        onImageSwapComplete: $.noop
    };
}(jQuery, window, document), "undefined" == typeof jQuery) throw new Error("Bootstrap's JavaScript requires jQuery");

+function(a) {
    "use strict";
    var b = a.fn.jquery.split(" ")[0].split(".");
    if (b[0] < 2 && b[1] < 9 || 1 == b[0] && 9 == b[1] && b[2] < 1 || b[0] > 3) throw new Error("Bootstrap's JavaScript requires jQuery version 1.9.1 or higher, but lower than version 4");
}(jQuery), function(a) {
    "use strict";
    function b() {
        var a = document.createElement("bootstrap"), b = {
            WebkitTransition: "webkitTransitionEnd",
            MozTransition: "transitionend",
            OTransition: "oTransitionEnd otransitionend",
            transition: "transitionend"
        };
        for (var c in b) if (void 0 !== a.style[c]) return {
            end: b[c]
        };
        return !1;
    }
    a.fn.emulateTransitionEnd = function(b) {
        var c = !1, d = this;
        a(this).one("bsTransitionEnd", function() {
            c = !0;
        });
        var e = function() {
            c || a(d).trigger(a.support.transition.end);
        };
        return setTimeout(e, b), this;
    }, a(function() {
        a.support.transition = b(), a.support.transition && (a.event.special.bsTransitionEnd = {
            bindType: a.support.transition.end,
            delegateType: a.support.transition.end,
            handle: function(b) {
                if (a(b.target).is(this)) return b.handleObj.handler.apply(this, arguments);
            }
        });
    });
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        return this.each(function() {
            var c = a(this), e = c.data("bs.alert");
            e || c.data("bs.alert", e = new d(this)), "string" == typeof b && e[b].call(c);
        });
    }
    var c = '[data-dismiss="alert"]', d = function(b) {
        a(b).on("click", c, this.close);
    };
    d.VERSION = "3.3.7", d.TRANSITION_DURATION = 150, d.prototype.close = function(b) {
        function c() {
            g.detach().trigger("closed.bs.alert").remove();
        }
        var e = a(this), f = e.attr("data-target");
        f || (f = e.attr("href"), f = f && f.replace(/.*(?=#[^\s]*$)/, ""));
        var g = a("#" === f ? [] : f);
        b && b.preventDefault(), g.length || (g = e.closest(".alert")), g.trigger(b = a.Event("close.bs.alert")), 
        b.isDefaultPrevented() || (g.removeClass("in"), a.support.transition && g.hasClass("fade") ? g.one("bsTransitionEnd", c).emulateTransitionEnd(d.TRANSITION_DURATION) : c());
    };
    var e = a.fn.alert;
    a.fn.alert = b, a.fn.alert.Constructor = d, a.fn.alert.noConflict = function() {
        return a.fn.alert = e, this;
    }, a(document).on("click.bs.alert.data-api", c, d.prototype.close);
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        return this.each(function() {
            var d = a(this), e = d.data("bs.button"), f = "object" == typeof b && b;
            e || d.data("bs.button", e = new c(this, f)), "toggle" == b ? e.toggle() : b && e.setState(b);
        });
    }
    var c = function(b, d) {
        this.$element = a(b), this.options = a.extend({}, c.DEFAULTS, d), this.isLoading = !1;
    };
    c.VERSION = "3.3.7", c.DEFAULTS = {
        loadingText: "loading..."
    }, c.prototype.setState = function(b) {
        var c = "disabled", d = this.$element, e = d.is("input") ? "val" : "html", f = d.data();
        b += "Text", null == f.resetText && d.data("resetText", d[e]()), setTimeout(a.proxy(function() {
            d[e](null == f[b] ? this.options[b] : f[b]), "loadingText" == b ? (this.isLoading = !0, 
            d.addClass(c).attr(c, c).prop(c, !0)) : this.isLoading && (this.isLoading = !1, 
            d.removeClass(c).removeAttr(c).prop(c, !1));
        }, this), 0);
    }, c.prototype.toggle = function() {
        var a = !0, b = this.$element.closest('[data-toggle="buttons"]');
        if (b.length) {
            var c = this.$element.find("input");
            "radio" == c.prop("type") ? (c.prop("checked") && (a = !1), b.find(".active").removeClass("active"), 
            this.$element.addClass("active")) : "checkbox" == c.prop("type") && (c.prop("checked") !== this.$element.hasClass("active") && (a = !1), 
            this.$element.toggleClass("active")), c.prop("checked", this.$element.hasClass("active")), 
            a && c.trigger("change");
        } else this.$element.attr("aria-pressed", !this.$element.hasClass("active")), this.$element.toggleClass("active");
    };
    var d = a.fn.button;
    a.fn.button = b, a.fn.button.Constructor = c, a.fn.button.noConflict = function() {
        return a.fn.button = d, this;
    }, a(document).on("click.bs.button.data-api", '[data-toggle^="button"]', function(c) {
        var d = a(c.target).closest(".btn");
        b.call(d, "toggle"), a(c.target).is('input[type="radio"], input[type="checkbox"]') || (c.preventDefault(), 
        d.is("input,button") ? d.trigger("focus") : d.find("input:visible,button:visible").first().trigger("focus"));
    }).on("focus.bs.button.data-api blur.bs.button.data-api", '[data-toggle^="button"]', function(b) {
        a(b.target).closest(".btn").toggleClass("focus", /^focus(in)?$/.test(b.type));
    });
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        return this.each(function() {
            var d = a(this), e = d.data("bs.carousel"), f = a.extend({}, c.DEFAULTS, d.data(), "object" == typeof b && b), g = "string" == typeof b ? b : f.slide;
            e || d.data("bs.carousel", e = new c(this, f)), "number" == typeof b ? e.to(b) : g ? e[g]() : f.interval && e.pause().cycle();
        });
    }
    var c = function(b, c) {
        this.$element = a(b), this.$indicators = this.$element.find(".carousel-indicators"), 
        this.options = c, this.paused = null, this.sliding = null, this.interval = null, 
        this.$active = null, this.$items = null, this.options.keyboard && this.$element.on("keydown.bs.carousel", a.proxy(this.keydown, this)), 
        "hover" == this.options.pause && !("ontouchstart" in document.documentElement) && this.$element.on("mouseenter.bs.carousel", a.proxy(this.pause, this)).on("mouseleave.bs.carousel", a.proxy(this.cycle, this));
    };
    c.VERSION = "3.3.7", c.TRANSITION_DURATION = 600, c.DEFAULTS = {
        interval: 5e3,
        pause: "hover",
        wrap: !0,
        keyboard: !0
    }, c.prototype.keydown = function(a) {
        if (!/input|textarea/i.test(a.target.tagName)) {
            switch (a.which) {
              case 37:
                this.prev();
                break;

              case 39:
                this.next();
                break;

              default:
                return;
            }
            a.preventDefault();
        }
    }, c.prototype.cycle = function(b) {
        return b || (this.paused = !1), this.interval && clearInterval(this.interval), this.options.interval && !this.paused && (this.interval = setInterval(a.proxy(this.next, this), this.options.interval)), 
        this;
    }, c.prototype.getItemIndex = function(a) {
        return this.$items = a.parent().children(".item"), this.$items.index(a || this.$active);
    }, c.prototype.getItemForDirection = function(a, b) {
        var c = this.getItemIndex(b);
        if (("prev" == a && 0 === c || "next" == a && c == this.$items.length - 1) && !this.options.wrap) return b;
        var e = "prev" == a ? -1 : 1, f = (c + e) % this.$items.length;
        return this.$items.eq(f);
    }, c.prototype.to = function(a) {
        var b = this, c = this.getItemIndex(this.$active = this.$element.find(".item.active"));
        if (!(a > this.$items.length - 1 || a < 0)) return this.sliding ? this.$element.one("slid.bs.carousel", function() {
            b.to(a);
        }) : c == a ? this.pause().cycle() : this.slide(a > c ? "next" : "prev", this.$items.eq(a));
    }, c.prototype.pause = function(b) {
        return b || (this.paused = !0), this.$element.find(".next, .prev").length && a.support.transition && (this.$element.trigger(a.support.transition.end), 
        this.cycle(!0)), this.interval = clearInterval(this.interval), this;
    }, c.prototype.next = function() {
        if (!this.sliding) return this.slide("next");
    }, c.prototype.prev = function() {
        if (!this.sliding) return this.slide("prev");
    }, c.prototype.slide = function(b, d) {
        var e = this.$element.find(".item.active"), f = d || this.getItemForDirection(b, e), g = this.interval, h = "next" == b ? "left" : "right", i = this;
        if (f.hasClass("active")) return this.sliding = !1;
        var j = f[0], k = a.Event("slide.bs.carousel", {
            relatedTarget: j,
            direction: h
        });
        if (this.$element.trigger(k), !k.isDefaultPrevented()) {
            if (this.sliding = !0, g && this.pause(), this.$indicators.length) {
                this.$indicators.find(".active").removeClass("active");
                var l = a(this.$indicators.children()[this.getItemIndex(f)]);
                l && l.addClass("active");
            }
            var m = a.Event("slid.bs.carousel", {
                relatedTarget: j,
                direction: h
            });
            return a.support.transition && this.$element.hasClass("slide") ? (f.addClass(b), 
            f[0].offsetWidth, e.addClass(h), f.addClass(h), e.one("bsTransitionEnd", function() {
                f.removeClass([ b, h ].join(" ")).addClass("active"), e.removeClass([ "active", h ].join(" ")), 
                i.sliding = !1, setTimeout(function() {
                    i.$element.trigger(m);
                }, 0);
            }).emulateTransitionEnd(c.TRANSITION_DURATION)) : (e.removeClass("active"), f.addClass("active"), 
            this.sliding = !1, this.$element.trigger(m)), g && this.cycle(), this;
        }
    };
    var d = a.fn.carousel;
    a.fn.carousel = b, a.fn.carousel.Constructor = c, a.fn.carousel.noConflict = function() {
        return a.fn.carousel = d, this;
    };
    var e = function(c) {
        var d, e = a(this), f = a(e.attr("data-target") || (d = e.attr("href")) && d.replace(/.*(?=#[^\s]+$)/, ""));
        if (f.hasClass("carousel")) {
            var g = a.extend({}, f.data(), e.data()), h = e.attr("data-slide-to");
            h && (g.interval = !1), b.call(f, g), h && f.data("bs.carousel").to(h), c.preventDefault();
        }
    };
    a(document).on("click.bs.carousel.data-api", "[data-slide]", e).on("click.bs.carousel.data-api", "[data-slide-to]", e), 
    a(window).on("load", function() {
        a('[data-ride="carousel"]').each(function() {
            var c = a(this);
            b.call(c, c.data());
        });
    });
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        var c, d = b.attr("data-target") || (c = b.attr("href")) && c.replace(/.*(?=#[^\s]+$)/, "");
        return a(d);
    }
    function c(b) {
        return this.each(function() {
            var c = a(this), e = c.data("bs.collapse"), f = a.extend({}, d.DEFAULTS, c.data(), "object" == typeof b && b);
            !e && f.toggle && /show|hide/.test(b) && (f.toggle = !1), e || c.data("bs.collapse", e = new d(this, f)), 
            "string" == typeof b && e[b]();
        });
    }
    var d = function(b, c) {
        this.$element = a(b), this.options = a.extend({}, d.DEFAULTS, c), this.$trigger = a('[data-toggle="collapse"][href="#' + b.id + '"],[data-toggle="collapse"][data-target="#' + b.id + '"]'), 
        this.transitioning = null, this.options.parent ? this.$parent = this.getParent() : this.addAriaAndCollapsedClass(this.$element, this.$trigger), 
        this.options.toggle && this.toggle();
    };
    d.VERSION = "3.3.7", d.TRANSITION_DURATION = 350, d.DEFAULTS = {
        toggle: !0
    }, d.prototype.dimension = function() {
        return this.$element.hasClass("width") ? "width" : "height";
    }, d.prototype.show = function() {
        if (!this.transitioning && !this.$element.hasClass("in")) {
            var b, e = this.$parent && this.$parent.children(".panel").children(".in, .collapsing");
            if (!(e && e.length && (b = e.data("bs.collapse")) && b.transitioning)) {
                var f = a.Event("show.bs.collapse");
                if (this.$element.trigger(f), !f.isDefaultPrevented()) {
                    e && e.length && (c.call(e, "hide"), b || e.data("bs.collapse", null));
                    var g = this.dimension();
                    this.$element.removeClass("collapse").addClass("collapsing")[g](0).attr("aria-expanded", !0), 
                    this.$trigger.removeClass("collapsed").attr("aria-expanded", !0), this.transitioning = 1;
                    var h = function() {
                        this.$element.removeClass("collapsing").addClass("collapse in")[g](""), this.transitioning = 0, 
                        this.$element.trigger("shown.bs.collapse");
                    };
                    if (!a.support.transition) return h.call(this);
                    var i = a.camelCase([ "scroll", g ].join("-"));
                    this.$element.one("bsTransitionEnd", a.proxy(h, this)).emulateTransitionEnd(d.TRANSITION_DURATION)[g](this.$element[0][i]);
                }
            }
        }
    }, d.prototype.hide = function() {
        if (!this.transitioning && this.$element.hasClass("in")) {
            var b = a.Event("hide.bs.collapse");
            if (this.$element.trigger(b), !b.isDefaultPrevented()) {
                var c = this.dimension();
                this.$element[c](this.$element[c]())[0].offsetHeight, this.$element.addClass("collapsing").removeClass("collapse in").attr("aria-expanded", !1), 
                this.$trigger.addClass("collapsed").attr("aria-expanded", !1), this.transitioning = 1;
                var e = function() {
                    this.transitioning = 0, this.$element.removeClass("collapsing").addClass("collapse").trigger("hidden.bs.collapse");
                };
                return a.support.transition ? void this.$element[c](0).one("bsTransitionEnd", a.proxy(e, this)).emulateTransitionEnd(d.TRANSITION_DURATION) : e.call(this);
            }
        }
    }, d.prototype.toggle = function() {
        this[this.$element.hasClass("in") ? "hide" : "show"]();
    }, d.prototype.getParent = function() {
        return a(this.options.parent).find('[data-toggle="collapse"][data-parent="' + this.options.parent + '"]').each(a.proxy(function(c, d) {
            var e = a(d);
            this.addAriaAndCollapsedClass(b(e), e);
        }, this)).end();
    }, d.prototype.addAriaAndCollapsedClass = function(a, b) {
        var c = a.hasClass("in");
        a.attr("aria-expanded", c), b.toggleClass("collapsed", !c).attr("aria-expanded", c);
    };
    var e = a.fn.collapse;
    a.fn.collapse = c, a.fn.collapse.Constructor = d, a.fn.collapse.noConflict = function() {
        return a.fn.collapse = e, this;
    }, a(document).on("click.bs.collapse.data-api", '[data-toggle="collapse"]', function(d) {
        var e = a(this);
        e.attr("data-target") || d.preventDefault();
        var f = b(e), g = f.data("bs.collapse"), h = g ? "toggle" : e.data();
        c.call(f, h);
    });
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        var c = b.attr("data-target");
        c || (c = b.attr("href"), c = c && /#[A-Za-z]/.test(c) && c.replace(/.*(?=#[^\s]*$)/, ""));
        var d = c && a(c);
        return d && d.length ? d : b.parent();
    }
    function c(c) {
        c && 3 === c.which || (a(e).remove(), a(f).each(function() {
            var d = a(this), e = b(d), f = {
                relatedTarget: this
            };
            e.hasClass("open") && (c && "click" == c.type && /input|textarea/i.test(c.target.tagName) && a.contains(e[0], c.target) || (e.trigger(c = a.Event("hide.bs.dropdown", f)), 
            c.isDefaultPrevented() || (d.attr("aria-expanded", "false"), e.removeClass("open").trigger(a.Event("hidden.bs.dropdown", f)))));
        }));
    }
    function d(b) {
        return this.each(function() {
            var c = a(this), d = c.data("bs.dropdown");
            d || c.data("bs.dropdown", d = new g(this)), "string" == typeof b && d[b].call(c);
        });
    }
    var e = ".dropdown-backdrop", f = '[data-toggle="dropdown"]', g = function(b) {
        a(b).on("click.bs.dropdown", this.toggle);
    };
    g.VERSION = "3.3.7", g.prototype.toggle = function(d) {
        var e = a(this);
        if (!e.is(".disabled, :disabled")) {
            var f = b(e), g = f.hasClass("open");
            if (c(), !g) {
                "ontouchstart" in document.documentElement && !f.closest(".navbar-nav").length && a(document.createElement("div")).addClass("dropdown-backdrop").insertAfter(a(this)).on("click", c);
                var h = {
                    relatedTarget: this
                };
                if (f.trigger(d = a.Event("show.bs.dropdown", h)), d.isDefaultPrevented()) return;
                e.trigger("focus").attr("aria-expanded", "true"), f.toggleClass("open").trigger(a.Event("shown.bs.dropdown", h));
            }
            return !1;
        }
    }, g.prototype.keydown = function(c) {
        if (/(38|40|27|32)/.test(c.which) && !/input|textarea/i.test(c.target.tagName)) {
            var d = a(this);
            if (c.preventDefault(), c.stopPropagation(), !d.is(".disabled, :disabled")) {
                var e = b(d), g = e.hasClass("open");
                if (!g && 27 != c.which || g && 27 == c.which) return 27 == c.which && e.find(f).trigger("focus"), 
                d.trigger("click");
                var i = e.find(".dropdown-menu li:not(.disabled):visible a");
                if (i.length) {
                    var j = i.index(c.target);
                    38 == c.which && j > 0 && j--, 40 == c.which && j < i.length - 1 && j++, ~j || (j = 0), 
                    i.eq(j).trigger("focus");
                }
            }
        }
    };
    var h = a.fn.dropdown;
    a.fn.dropdown = d, a.fn.dropdown.Constructor = g, a.fn.dropdown.noConflict = function() {
        return a.fn.dropdown = h, this;
    }, a(document).on("click.bs.dropdown.data-api", c).on("click.bs.dropdown.data-api", ".dropdown form", function(a) {
        a.stopPropagation();
    }).on("click.bs.dropdown.data-api", f, g.prototype.toggle).on("keydown.bs.dropdown.data-api", f, g.prototype.keydown).on("keydown.bs.dropdown.data-api", ".dropdown-menu", g.prototype.keydown);
}(jQuery), function(a) {
    "use strict";
    function b(b, d) {
        return this.each(function() {
            var e = a(this), f = e.data("bs.modal"), g = a.extend({}, c.DEFAULTS, e.data(), "object" == typeof b && b);
            f || e.data("bs.modal", f = new c(this, g)), "string" == typeof b ? f[b](d) : g.show && f.show(d);
        });
    }
    var c = function(b, c) {
        this.options = c, this.$body = a(document.body), this.$element = a(b), this.$dialog = this.$element.find(".modal-dialog"), 
        this.$backdrop = null, this.isShown = null, this.originalBodyPad = null, this.scrollbarWidth = 0, 
        this.ignoreBackdropClick = !1, this.options.remote && this.$element.find(".modal-content").load(this.options.remote, a.proxy(function() {
            this.$element.trigger("loaded.bs.modal");
        }, this));
    };
    c.VERSION = "3.3.7", c.TRANSITION_DURATION = 300, c.BACKDROP_TRANSITION_DURATION = 150, 
    c.DEFAULTS = {
        backdrop: !0,
        keyboard: !0,
        show: !0
    }, c.prototype.toggle = function(a) {
        return this.isShown ? this.hide() : this.show(a);
    }, c.prototype.show = function(b) {
        var d = this, e = a.Event("show.bs.modal", {
            relatedTarget: b
        });
        this.$element.trigger(e), this.isShown || e.isDefaultPrevented() || (this.isShown = !0, 
        this.checkScrollbar(), this.setScrollbar(), this.$body.addClass("modal-open"), this.escape(), 
        this.resize(), this.$element.on("click.dismiss.bs.modal", '[data-dismiss="modal"]', a.proxy(this.hide, this)), 
        this.$dialog.on("mousedown.dismiss.bs.modal", function() {
            d.$element.one("mouseup.dismiss.bs.modal", function(b) {
                a(b.target).is(d.$element) && (d.ignoreBackdropClick = !0);
            });
        }), this.backdrop(function() {
            var e = a.support.transition && d.$element.hasClass("fade");
            d.$element.parent().length || d.$element.appendTo(d.$body), d.$element.show().scrollTop(0), 
            d.adjustDialog(), e && d.$element[0].offsetWidth, d.$element.addClass("in"), d.enforceFocus();
            var f = a.Event("shown.bs.modal", {
                relatedTarget: b
            });
            e ? d.$dialog.one("bsTransitionEnd", function() {
                d.$element.trigger("focus").trigger(f);
            }).emulateTransitionEnd(c.TRANSITION_DURATION) : d.$element.trigger("focus").trigger(f);
        }));
    }, c.prototype.hide = function(b) {
        b && b.preventDefault(), b = a.Event("hide.bs.modal"), this.$element.trigger(b), 
        this.isShown && !b.isDefaultPrevented() && (this.isShown = !1, this.escape(), this.resize(), 
        a(document).off("focusin.bs.modal"), this.$element.removeClass("in").off("click.dismiss.bs.modal").off("mouseup.dismiss.bs.modal"), 
        this.$dialog.off("mousedown.dismiss.bs.modal"), a.support.transition && this.$element.hasClass("fade") ? this.$element.one("bsTransitionEnd", a.proxy(this.hideModal, this)).emulateTransitionEnd(c.TRANSITION_DURATION) : this.hideModal());
    }, c.prototype.enforceFocus = function() {
        a(document).off("focusin.bs.modal").on("focusin.bs.modal", a.proxy(function(a) {
            document === a.target || this.$element[0] === a.target || this.$element.has(a.target).length || this.$element.trigger("focus");
        }, this));
    }, c.prototype.escape = function() {
        this.isShown && this.options.keyboard ? this.$element.on("keydown.dismiss.bs.modal", a.proxy(function(a) {
            27 == a.which && this.hide();
        }, this)) : this.isShown || this.$element.off("keydown.dismiss.bs.modal");
    }, c.prototype.resize = function() {
        this.isShown ? a(window).on("resize.bs.modal", a.proxy(this.handleUpdate, this)) : a(window).off("resize.bs.modal");
    }, c.prototype.hideModal = function() {
        var a = this;
        this.$element.hide(), this.backdrop(function() {
            a.$body.removeClass("modal-open"), a.resetAdjustments(), a.resetScrollbar(), a.$element.trigger("hidden.bs.modal");
        });
    }, c.prototype.removeBackdrop = function() {
        this.$backdrop && this.$backdrop.remove(), this.$backdrop = null;
    }, c.prototype.backdrop = function(b) {
        var d = this, e = this.$element.hasClass("fade") ? "fade" : "";
        if (this.isShown && this.options.backdrop) {
            var f = a.support.transition && e;
            if (this.$backdrop = a(document.createElement("div")).addClass("modal-backdrop " + e).appendTo(this.$body), 
            this.$element.on("click.dismiss.bs.modal", a.proxy(function(a) {
                return this.ignoreBackdropClick ? void (this.ignoreBackdropClick = !1) : void (a.target === a.currentTarget && ("static" == this.options.backdrop ? this.$element[0].focus() : this.hide()));
            }, this)), f && this.$backdrop[0].offsetWidth, this.$backdrop.addClass("in"), !b) return;
            f ? this.$backdrop.one("bsTransitionEnd", b).emulateTransitionEnd(c.BACKDROP_TRANSITION_DURATION) : b();
        } else if (!this.isShown && this.$backdrop) {
            this.$backdrop.removeClass("in");
            var g = function() {
                d.removeBackdrop(), b && b();
            };
            a.support.transition && this.$element.hasClass("fade") ? this.$backdrop.one("bsTransitionEnd", g).emulateTransitionEnd(c.BACKDROP_TRANSITION_DURATION) : g();
        } else b && b();
    }, c.prototype.handleUpdate = function() {
        this.adjustDialog();
    }, c.prototype.adjustDialog = function() {
        var a = this.$element[0].scrollHeight > document.documentElement.clientHeight;
        this.$element.css({
            paddingLeft: !this.bodyIsOverflowing && a ? this.scrollbarWidth : "",
            paddingRight: this.bodyIsOverflowing && !a ? this.scrollbarWidth : ""
        });
    }, c.prototype.resetAdjustments = function() {
        this.$element.css({
            paddingLeft: "",
            paddingRight: ""
        });
    }, c.prototype.checkScrollbar = function() {
        var a = window.innerWidth;
        if (!a) {
            var b = document.documentElement.getBoundingClientRect();
            a = b.right - Math.abs(b.left);
        }
        this.bodyIsOverflowing = document.body.clientWidth < a, this.scrollbarWidth = this.measureScrollbar();
    }, c.prototype.setScrollbar = function() {
        var a = parseInt(this.$body.css("padding-right") || 0, 10);
        this.originalBodyPad = document.body.style.paddingRight || "", this.bodyIsOverflowing && this.$body.css("padding-right", a + this.scrollbarWidth);
    }, c.prototype.resetScrollbar = function() {
        this.$body.css("padding-right", this.originalBodyPad);
    }, c.prototype.measureScrollbar = function() {
        var a = document.createElement("div");
        a.className = "modal-scrollbar-measure", this.$body.append(a);
        var b = a.offsetWidth - a.clientWidth;
        return this.$body[0].removeChild(a), b;
    };
    var d = a.fn.modal;
    a.fn.modal = b, a.fn.modal.Constructor = c, a.fn.modal.noConflict = function() {
        return a.fn.modal = d, this;
    }, a(document).on("click.bs.modal.data-api", '[data-toggle="modal"]', function(c) {
        var d = a(this), e = d.attr("href"), f = a(d.attr("data-target") || e && e.replace(/.*(?=#[^\s]+$)/, "")), g = f.data("bs.modal") ? "toggle" : a.extend({
            remote: !/#/.test(e) && e
        }, f.data(), d.data());
        d.is("a") && c.preventDefault(), f.one("show.bs.modal", function(a) {
            a.isDefaultPrevented() || f.one("hidden.bs.modal", function() {
                d.is(":visible") && d.trigger("focus");
            });
        }), b.call(f, g, this);
    });
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        return this.each(function() {
            var d = a(this), e = d.data("bs.tooltip"), f = "object" == typeof b && b;
            !e && /destroy|hide/.test(b) || (e || d.data("bs.tooltip", e = new c(this, f)), 
            "string" == typeof b && e[b]());
        });
    }
    var c = function(a, b) {
        this.type = null, this.options = null, this.enabled = null, this.timeout = null, 
        this.hoverState = null, this.$element = null, this.inState = null, this.init("tooltip", a, b);
    };
    c.VERSION = "3.3.7", c.TRANSITION_DURATION = 150, c.DEFAULTS = {
        animation: !0,
        placement: "top",
        selector: !1,
        template: '<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',
        trigger: "hover focus",
        title: "",
        delay: 0,
        html: !1,
        container: !1,
        viewport: {
            selector: "body",
            padding: 0
        }
    }, c.prototype.init = function(b, c, d) {
        if (this.enabled = !0, this.type = b, this.$element = a(c), this.options = this.getOptions(d), 
        this.$viewport = this.options.viewport && a(a.isFunction(this.options.viewport) ? this.options.viewport.call(this, this.$element) : this.options.viewport.selector || this.options.viewport), 
        this.inState = {
            click: !1,
            hover: !1,
            focus: !1
        }, this.$element[0] instanceof document.constructor && !this.options.selector) throw new Error("`selector` option must be specified when initializing " + this.type + " on the window.document object!");
        for (var e = this.options.trigger.split(" "), f = e.length; f--; ) {
            var g = e[f];
            if ("click" == g) this.$element.on("click." + this.type, this.options.selector, a.proxy(this.toggle, this)); else if ("manual" != g) {
                var h = "hover" == g ? "mouseenter" : "focusin", i = "hover" == g ? "mouseleave" : "focusout";
                this.$element.on(h + "." + this.type, this.options.selector, a.proxy(this.enter, this)), 
                this.$element.on(i + "." + this.type, this.options.selector, a.proxy(this.leave, this));
            }
        }
        this.options.selector ? this._options = a.extend({}, this.options, {
            trigger: "manual",
            selector: ""
        }) : this.fixTitle();
    }, c.prototype.getDefaults = function() {
        return c.DEFAULTS;
    }, c.prototype.getOptions = function(b) {
        return b = a.extend({}, this.getDefaults(), this.$element.data(), b), b.delay && "number" == typeof b.delay && (b.delay = {
            show: b.delay,
            hide: b.delay
        }), b;
    }, c.prototype.getDelegateOptions = function() {
        var b = {}, c = this.getDefaults();
        return this._options && a.each(this._options, function(a, d) {
            c[a] != d && (b[a] = d);
        }), b;
    }, c.prototype.enter = function(b) {
        var c = b instanceof this.constructor ? b : a(b.currentTarget).data("bs." + this.type);
        return c || (c = new this.constructor(b.currentTarget, this.getDelegateOptions()), 
        a(b.currentTarget).data("bs." + this.type, c)), b instanceof a.Event && (c.inState["focusin" == b.type ? "focus" : "hover"] = !0), 
        c.tip().hasClass("in") || "in" == c.hoverState ? void (c.hoverState = "in") : (clearTimeout(c.timeout), 
        c.hoverState = "in", c.options.delay && c.options.delay.show ? void (c.timeout = setTimeout(function() {
            "in" == c.hoverState && c.show();
        }, c.options.delay.show)) : c.show());
    }, c.prototype.isInStateTrue = function() {
        for (var a in this.inState) if (this.inState[a]) return !0;
        return !1;
    }, c.prototype.leave = function(b) {
        var c = b instanceof this.constructor ? b : a(b.currentTarget).data("bs." + this.type);
        if (c || (c = new this.constructor(b.currentTarget, this.getDelegateOptions()), 
        a(b.currentTarget).data("bs." + this.type, c)), b instanceof a.Event && (c.inState["focusout" == b.type ? "focus" : "hover"] = !1), 
        !c.isInStateTrue()) return clearTimeout(c.timeout), c.hoverState = "out", c.options.delay && c.options.delay.hide ? void (c.timeout = setTimeout(function() {
            "out" == c.hoverState && c.hide();
        }, c.options.delay.hide)) : c.hide();
    }, c.prototype.show = function() {
        var b = a.Event("show.bs." + this.type);
        if (this.hasContent() && this.enabled) {
            this.$element.trigger(b);
            var d = a.contains(this.$element[0].ownerDocument.documentElement, this.$element[0]);
            if (b.isDefaultPrevented() || !d) return;
            var e = this, f = this.tip(), g = this.getUID(this.type);
            this.setContent(), f.attr("id", g), this.$element.attr("aria-describedby", g), this.options.animation && f.addClass("fade");
            var h = "function" == typeof this.options.placement ? this.options.placement.call(this, f[0], this.$element[0]) : this.options.placement, i = /\s?auto?\s?/i, j = i.test(h);
            j && (h = h.replace(i, "") || "top"), f.detach().css({
                top: 0,
                left: 0,
                display: "block"
            }).addClass(h).data("bs." + this.type, this), this.options.container ? f.appendTo(this.options.container) : f.insertAfter(this.$element), 
            this.$element.trigger("inserted.bs." + this.type);
            var k = this.getPosition(), l = f[0].offsetWidth, m = f[0].offsetHeight;
            if (j) {
                var n = h, o = this.getPosition(this.$viewport);
                h = "bottom" == h && k.bottom + m > o.bottom ? "top" : "top" == h && k.top - m < o.top ? "bottom" : "right" == h && k.right + l > o.width ? "left" : "left" == h && k.left - l < o.left ? "right" : h, 
                f.removeClass(n).addClass(h);
            }
            var p = this.getCalculatedOffset(h, k, l, m);
            this.applyPlacement(p, h);
            var q = function() {
                var a = e.hoverState;
                e.$element.trigger("shown.bs." + e.type), e.hoverState = null, "out" == a && e.leave(e);
            };
            a.support.transition && this.$tip.hasClass("fade") ? f.one("bsTransitionEnd", q).emulateTransitionEnd(c.TRANSITION_DURATION) : q();
        }
    }, c.prototype.applyPlacement = function(b, c) {
        var d = this.tip(), e = d[0].offsetWidth, f = d[0].offsetHeight, g = parseInt(d.css("margin-top"), 10), h = parseInt(d.css("margin-left"), 10);
        isNaN(g) && (g = 0), isNaN(h) && (h = 0), b.top += g, b.left += h, a.offset.setOffset(d[0], a.extend({
            using: function(a) {
                d.css({
                    top: Math.round(a.top),
                    left: Math.round(a.left)
                });
            }
        }, b), 0), d.addClass("in");
        var i = d[0].offsetWidth, j = d[0].offsetHeight;
        "top" == c && j != f && (b.top = b.top + f - j);
        var k = this.getViewportAdjustedDelta(c, b, i, j);
        k.left ? b.left += k.left : b.top += k.top;
        var l = /top|bottom/.test(c), m = l ? 2 * k.left - e + i : 2 * k.top - f + j, n = l ? "offsetWidth" : "offsetHeight";
        d.offset(b), this.replaceArrow(m, d[0][n], l);
    }, c.prototype.replaceArrow = function(a, b, c) {
        this.arrow().css(c ? "left" : "top", 50 * (1 - a / b) + "%").css(c ? "top" : "left", "");
    }, c.prototype.setContent = function() {
        var a = this.tip(), b = this.getTitle();
        a.find(".tooltip-inner")[this.options.html ? "html" : "text"](b), a.removeClass("fade in top bottom left right");
    }, c.prototype.hide = function(b) {
        function d() {
            "in" != e.hoverState && f.detach(), e.$element && e.$element.removeAttr("aria-describedby").trigger("hidden.bs." + e.type), 
            b && b();
        }
        var e = this, f = a(this.$tip), g = a.Event("hide.bs." + this.type);
        if (this.$element.trigger(g), !g.isDefaultPrevented()) return f.removeClass("in"), 
        a.support.transition && f.hasClass("fade") ? f.one("bsTransitionEnd", d).emulateTransitionEnd(c.TRANSITION_DURATION) : d(), 
        this.hoverState = null, this;
    }, c.prototype.fixTitle = function() {
        var a = this.$element;
        (a.attr("title") || "string" != typeof a.attr("data-original-title")) && a.attr("data-original-title", a.attr("title") || "").attr("title", "");
    }, c.prototype.hasContent = function() {
        return this.getTitle();
    }, c.prototype.getPosition = function(b) {
        b = b || this.$element;
        var c = b[0], d = "BODY" == c.tagName, e = c.getBoundingClientRect();
        null == e.width && (e = a.extend({}, e, {
            width: e.right - e.left,
            height: e.bottom - e.top
        }));
        var f = window.SVGElement && c instanceof window.SVGElement, g = d ? {
            top: 0,
            left: 0
        } : f ? null : b.offset(), h = {
            scroll: d ? document.documentElement.scrollTop || document.body.scrollTop : b.scrollTop()
        }, i = d ? {
            width: a(window).width(),
            height: a(window).height()
        } : null;
        return a.extend({}, e, h, i, g);
    }, c.prototype.getCalculatedOffset = function(a, b, c, d) {
        return "bottom" == a ? {
            top: b.top + b.height,
            left: b.left + b.width / 2 - c / 2
        } : "top" == a ? {
            top: b.top - d,
            left: b.left + b.width / 2 - c / 2
        } : "left" == a ? {
            top: b.top + b.height / 2 - d / 2,
            left: b.left - c
        } : {
            top: b.top + b.height / 2 - d / 2,
            left: b.left + b.width
        };
    }, c.prototype.getViewportAdjustedDelta = function(a, b, c, d) {
        var e = {
            top: 0,
            left: 0
        };
        if (!this.$viewport) return e;
        var f = this.options.viewport && this.options.viewport.padding || 0, g = this.getPosition(this.$viewport);
        if (/right|left/.test(a)) {
            var h = b.top - f - g.scroll, i = b.top + f - g.scroll + d;
            h < g.top ? e.top = g.top - h : i > g.top + g.height && (e.top = g.top + g.height - i);
        } else {
            var j = b.left - f, k = b.left + f + c;
            j < g.left ? e.left = g.left - j : k > g.right && (e.left = g.left + g.width - k);
        }
        return e;
    }, c.prototype.getTitle = function() {
        var b = this.$element, c = this.options;
        return b.attr("data-original-title") || ("function" == typeof c.title ? c.title.call(b[0]) : c.title);
    }, c.prototype.getUID = function(a) {
        do {
            a += ~~(1e6 * Math.random());
        } while (document.getElementById(a));
        return a;
    }, c.prototype.tip = function() {
        if (!this.$tip && (this.$tip = a(this.options.template), 1 != this.$tip.length)) throw new Error(this.type + " `template` option must consist of exactly 1 top-level element!");
        return this.$tip;
    }, c.prototype.arrow = function() {
        return this.$arrow = this.$arrow || this.tip().find(".tooltip-arrow");
    }, c.prototype.enable = function() {
        this.enabled = !0;
    }, c.prototype.disable = function() {
        this.enabled = !1;
    }, c.prototype.toggleEnabled = function() {
        this.enabled = !this.enabled;
    }, c.prototype.toggle = function(b) {
        var c = this;
        b && ((c = a(b.currentTarget).data("bs." + this.type)) || (c = new this.constructor(b.currentTarget, this.getDelegateOptions()), 
        a(b.currentTarget).data("bs." + this.type, c))), b ? (c.inState.click = !c.inState.click, 
        c.isInStateTrue() ? c.enter(c) : c.leave(c)) : c.tip().hasClass("in") ? c.leave(c) : c.enter(c);
    }, c.prototype.destroy = function() {
        var a = this;
        clearTimeout(this.timeout), this.hide(function() {
            a.$element.off("." + a.type).removeData("bs." + a.type), a.$tip && a.$tip.detach(), 
            a.$tip = null, a.$arrow = null, a.$viewport = null, a.$element = null;
        });
    };
    var d = a.fn.tooltip;
    a.fn.tooltip = b, a.fn.tooltip.Constructor = c, a.fn.tooltip.noConflict = function() {
        return a.fn.tooltip = d, this;
    };
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        return this.each(function() {
            var d = a(this), e = d.data("bs.popover"), f = "object" == typeof b && b;
            !e && /destroy|hide/.test(b) || (e || d.data("bs.popover", e = new c(this, f)), 
            "string" == typeof b && e[b]());
        });
    }
    var c = function(a, b) {
        this.init("popover", a, b);
    };
    if (!a.fn.tooltip) throw new Error("Popover requires tooltip.js");
    c.VERSION = "3.3.7", c.DEFAULTS = a.extend({}, a.fn.tooltip.Constructor.DEFAULTS, {
        placement: "right",
        trigger: "click",
        content: "",
        template: '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
    }), c.prototype = a.extend({}, a.fn.tooltip.Constructor.prototype), c.prototype.constructor = c, 
    c.prototype.getDefaults = function() {
        return c.DEFAULTS;
    }, c.prototype.setContent = function() {
        var a = this.tip(), b = this.getTitle(), c = this.getContent();
        a.find(".popover-title")[this.options.html ? "html" : "text"](b), a.find(".popover-content").children().detach().end()[this.options.html ? "string" == typeof c ? "html" : "append" : "text"](c), 
        a.removeClass("fade top bottom left right in"), a.find(".popover-title").html() || a.find(".popover-title").hide();
    }, c.prototype.hasContent = function() {
        return this.getTitle() || this.getContent();
    }, c.prototype.getContent = function() {
        var a = this.$element, b = this.options;
        return a.attr("data-content") || ("function" == typeof b.content ? b.content.call(a[0]) : b.content);
    }, c.prototype.arrow = function() {
        return this.$arrow = this.$arrow || this.tip().find(".arrow");
    };
    var d = a.fn.popover;
    a.fn.popover = b, a.fn.popover.Constructor = c, a.fn.popover.noConflict = function() {
        return a.fn.popover = d, this;
    };
}(jQuery), function(a) {
    "use strict";
    function b(c, d) {
        this.$body = a(document.body), this.$scrollElement = a(a(c).is(document.body) ? window : c), 
        this.options = a.extend({}, b.DEFAULTS, d), this.selector = (this.options.target || "") + " .nav li > a", 
        this.offsets = [], this.targets = [], this.activeTarget = null, this.scrollHeight = 0, 
        this.$scrollElement.on("scroll.bs.scrollspy", a.proxy(this.process, this)), this.refresh(), 
        this.process();
    }
    function c(c) {
        return this.each(function() {
            var d = a(this), e = d.data("bs.scrollspy"), f = "object" == typeof c && c;
            e || d.data("bs.scrollspy", e = new b(this, f)), "string" == typeof c && e[c]();
        });
    }
    b.VERSION = "3.3.7", b.DEFAULTS = {
        offset: 10
    }, b.prototype.getScrollHeight = function() {
        return this.$scrollElement[0].scrollHeight || Math.max(this.$body[0].scrollHeight, document.documentElement.scrollHeight);
    }, b.prototype.refresh = function() {
        var b = this, c = "offset", d = 0;
        this.offsets = [], this.targets = [], this.scrollHeight = this.getScrollHeight(), 
        a.isWindow(this.$scrollElement[0]) || (c = "position", d = this.$scrollElement.scrollTop()), 
        this.$body.find(this.selector).map(function() {
            var b = a(this), e = b.data("target") || b.attr("href"), f = /^#./.test(e) && a(e);
            return f && f.length && f.is(":visible") && [ [ f[c]().top + d, e ] ] || null;
        }).sort(function(a, b) {
            return a[0] - b[0];
        }).each(function() {
            b.offsets.push(this[0]), b.targets.push(this[1]);
        });
    }, b.prototype.process = function() {
        var a, b = this.$scrollElement.scrollTop() + this.options.offset, c = this.getScrollHeight(), d = this.options.offset + c - this.$scrollElement.height(), e = this.offsets, f = this.targets, g = this.activeTarget;
        if (this.scrollHeight != c && this.refresh(), b >= d) return g != (a = f[f.length - 1]) && this.activate(a);
        if (g && b < e[0]) return this.activeTarget = null, this.clear();
        for (a = e.length; a--; ) g != f[a] && b >= e[a] && (void 0 === e[a + 1] || b < e[a + 1]) && this.activate(f[a]);
    }, b.prototype.activate = function(b) {
        this.activeTarget = b, this.clear();
        var c = this.selector + '[data-target="' + b + '"],' + this.selector + '[href="' + b + '"]', d = a(c).parents("li").addClass("active");
        d.parent(".dropdown-menu").length && (d = d.closest("li.dropdown").addClass("active")), 
        d.trigger("activate.bs.scrollspy");
    }, b.prototype.clear = function() {
        a(this.selector).parentsUntil(this.options.target, ".active").removeClass("active");
    };
    var d = a.fn.scrollspy;
    a.fn.scrollspy = c, a.fn.scrollspy.Constructor = b, a.fn.scrollspy.noConflict = function() {
        return a.fn.scrollspy = d, this;
    }, a(window).on("load.bs.scrollspy.data-api", function() {
        a('[data-spy="scroll"]').each(function() {
            var b = a(this);
            c.call(b, b.data());
        });
    });
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        return this.each(function() {
            var d = a(this), e = d.data("bs.tab");
            e || d.data("bs.tab", e = new c(this)), "string" == typeof b && e[b]();
        });
    }
    var c = function(b) {
        this.element = a(b);
    };
    c.VERSION = "3.3.7", c.TRANSITION_DURATION = 150, c.prototype.show = function() {
        var b = this.element, c = b.closest("ul:not(.dropdown-menu)"), d = b.data("target");
        if (d || (d = b.attr("href"), d = d && d.replace(/.*(?=#[^\s]*$)/, "")), !b.parent("li").hasClass("active")) {
            var e = c.find(".active:last a"), f = a.Event("hide.bs.tab", {
                relatedTarget: b[0]
            }), g = a.Event("show.bs.tab", {
                relatedTarget: e[0]
            });
            if (e.trigger(f), b.trigger(g), !g.isDefaultPrevented() && !f.isDefaultPrevented()) {
                var h = a(d);
                this.activate(b.closest("li"), c), this.activate(h, h.parent(), function() {
                    e.trigger({
                        type: "hidden.bs.tab",
                        relatedTarget: b[0]
                    }), b.trigger({
                        type: "shown.bs.tab",
                        relatedTarget: e[0]
                    });
                });
            }
        }
    }, c.prototype.activate = function(b, d, e) {
        function f() {
            g.removeClass("active").find("> .dropdown-menu > .active").removeClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded", !1), 
            b.addClass("active").find('[data-toggle="tab"]').attr("aria-expanded", !0), h ? (b[0].offsetWidth, 
            b.addClass("in")) : b.removeClass("fade"), b.parent(".dropdown-menu").length && b.closest("li.dropdown").addClass("active").end().find('[data-toggle="tab"]').attr("aria-expanded", !0), 
            e && e();
        }
        var g = d.find("> .active"), h = e && a.support.transition && (g.length && g.hasClass("fade") || !!d.find("> .fade").length);
        g.length && h ? g.one("bsTransitionEnd", f).emulateTransitionEnd(c.TRANSITION_DURATION) : f(), 
        g.removeClass("in");
    };
    var d = a.fn.tab;
    a.fn.tab = b, a.fn.tab.Constructor = c, a.fn.tab.noConflict = function() {
        return a.fn.tab = d, this;
    };
    var e = function(c) {
        c.preventDefault(), b.call(a(this), "show");
    };
    a(document).on("click.bs.tab.data-api", '[data-toggle="tab"]', e).on("click.bs.tab.data-api", '[data-toggle="pill"]', e);
}(jQuery), function(a) {
    "use strict";
    function b(b) {
        return this.each(function() {
            var d = a(this), e = d.data("bs.affix"), f = "object" == typeof b && b;
            e || d.data("bs.affix", e = new c(this, f)), "string" == typeof b && e[b]();
        });
    }
    var c = function(b, d) {
        this.options = a.extend({}, c.DEFAULTS, d), this.$target = a(this.options.target).on("scroll.bs.affix.data-api", a.proxy(this.checkPosition, this)).on("click.bs.affix.data-api", a.proxy(this.checkPositionWithEventLoop, this)), 
        this.$element = a(b), this.affixed = null, this.unpin = null, this.pinnedOffset = null, 
        this.checkPosition();
    };
    c.VERSION = "3.3.7", c.RESET = "affix affix-top affix-bottom", c.DEFAULTS = {
        offset: 0,
        target: window
    }, c.prototype.getState = function(a, b, c, d) {
        var e = this.$target.scrollTop(), f = this.$element.offset(), g = this.$target.height();
        if (null != c && "top" == this.affixed) return e < c && "top";
        if ("bottom" == this.affixed) return null != c ? !(e + this.unpin <= f.top) && "bottom" : !(e + g <= a - d) && "bottom";
        var h = null == this.affixed, i = h ? e : f.top, j = h ? g : b;
        return null != c && e <= c ? "top" : null != d && i + j >= a - d && "bottom";
    }, c.prototype.getPinnedOffset = function() {
        if (this.pinnedOffset) return this.pinnedOffset;
        this.$element.removeClass(c.RESET).addClass("affix");
        var a = this.$target.scrollTop(), b = this.$element.offset();
        return this.pinnedOffset = b.top - a;
    }, c.prototype.checkPositionWithEventLoop = function() {
        setTimeout(a.proxy(this.checkPosition, this), 1);
    }, c.prototype.checkPosition = function() {
        if (this.$element.is(":visible")) {
            var b = this.$element.height(), d = this.options.offset, e = d.top, f = d.bottom, g = Math.max(a(document).height(), a(document.body).height());
            "object" != typeof d && (f = e = d), "function" == typeof e && (e = d.top(this.$element)), 
            "function" == typeof f && (f = d.bottom(this.$element));
            var h = this.getState(g, b, e, f);
            if (this.affixed != h) {
                null != this.unpin && this.$element.css("top", "");
                var i = "affix" + (h ? "-" + h : ""), j = a.Event(i + ".bs.affix");
                if (this.$element.trigger(j), j.isDefaultPrevented()) return;
                this.affixed = h, this.unpin = "bottom" == h ? this.getPinnedOffset() : null, this.$element.removeClass(c.RESET).addClass(i).trigger(i.replace("affix", "affixed") + ".bs.affix");
            }
            "bottom" == h && this.$element.offset({
                top: g - b - f
            });
        }
    };
    var d = a.fn.affix;
    a.fn.affix = b, a.fn.affix.Constructor = c, a.fn.affix.noConflict = function() {
        return a.fn.affix = d, this;
    }, a(window).on("load", function() {
        a('[data-spy="affix"]').each(function() {
            var c = a(this), d = c.data();
            d.offset = d.offset || {}, null != d.offsetBottom && (d.offset.bottom = d.offsetBottom), 
            null != d.offsetTop && (d.offset.top = d.offsetTop), b.call(c, d);
        });
    });
}(jQuery), function(a) {
    "function" == typeof define && define.amd ? define([ "jquery" ], a) : a(jQuery);
}(function(a) {
    a.extend(a.fn, {
        validate: function(b) {
            if (!this.length) return void (b && b.debug && window.console && console.warn("Nothing selected, can't validate, returning nothing."));
            var c = a.data(this[0], "validator");
            return c || (this.attr("novalidate", "novalidate"), c = new a.validator(b, this[0]), 
            a.data(this[0], "validator", c), c.settings.onsubmit && (this.on("click.validate", ":submit", function(b) {
                c.settings.submitHandler && (c.submitButton = b.target), a(this).hasClass("cancel") && (c.cancelSubmit = !0), 
                void 0 !== a(this).attr("formnovalidate") && (c.cancelSubmit = !0);
            }), this.on("submit.validate", function(b) {
                function d() {
                    var d, e;
                    return !c.settings.submitHandler || (c.submitButton && (d = a("<input type='hidden'/>").attr("name", c.submitButton.name).val(a(c.submitButton).val()).appendTo(c.currentForm)), 
                    e = c.settings.submitHandler.call(c, c.currentForm, b), c.submitButton && d.remove(), 
                    void 0 !== e && e);
                }
                return c.settings.debug && b.preventDefault(), c.cancelSubmit ? (c.cancelSubmit = !1, 
                d()) : c.form() ? c.pendingRequest ? (c.formSubmitted = !0, !1) : d() : (c.focusInvalid(), 
                !1);
            })), c);
        },
        valid: function() {
            var b, c, d;
            return a(this[0]).is("form") ? b = this.validate().form() : (d = [], b = !0, c = a(this[0].form).validate(), 
            this.each(function() {
                b = c.element(this) && b, d = d.concat(c.errorList);
            }), c.errorList = d), b;
        },
        rules: function(b, c) {
            var d, e, f, g, h, i, j = this[0];
            if (b) switch (d = a.data(j.form, "validator").settings, e = d.rules, f = a.validator.staticRules(j), 
            b) {
              case "add":
                a.extend(f, a.validator.normalizeRule(c)), delete f.messages, e[j.name] = f, c.messages && (d.messages[j.name] = a.extend(d.messages[j.name], c.messages));
                break;

              case "remove":
                return c ? (i = {}, a.each(c.split(/\s/), function(b, c) {
                    i[c] = f[c], delete f[c], "required" === c && a(j).removeAttr("aria-required");
                }), i) : (delete e[j.name], f);
            }
            return g = a.validator.normalizeRules(a.extend({}, a.validator.classRules(j), a.validator.attributeRules(j), a.validator.dataRules(j), a.validator.staticRules(j)), j), 
            g.required && (h = g.required, delete g.required, g = a.extend({
                required: h
            }, g), a(j).attr("aria-required", "true")), g.remote && (h = g.remote, delete g.remote, 
            g = a.extend(g, {
                remote: h
            })), g;
        }
    }), a.extend(a.expr[":"], {
        blank: function(b) {
            return !a.trim("" + a(b).val());
        },
        filled: function(b) {
            return !!a.trim("" + a(b).val());
        },
        unchecked: function(b) {
            return !a(b).prop("checked");
        }
    }), a.validator = function(b, c) {
        this.settings = a.extend(!0, {}, a.validator.defaults, b), this.currentForm = c, 
        this.init();
    }, a.validator.format = function(b, c) {
        return 1 === arguments.length ? function() {
            var c = a.makeArray(arguments);
            return c.unshift(b), a.validator.format.apply(this, c);
        } : (arguments.length > 2 && c.constructor !== Array && (c = a.makeArray(arguments).slice(1)), 
        c.constructor !== Array && (c = [ c ]), a.each(c, function(a, c) {
            b = b.replace(new RegExp("\\{" + a + "\\}", "g"), function() {
                return c;
            });
        }), b);
    }, a.extend(a.validator, {
        defaults: {
            messages: {},
            groups: {},
            rules: {},
            errorClass: "error",
            validClass: "valid",
            errorElement: "label",
            focusCleanup: !1,
            focusInvalid: !0,
            errorContainer: a([]),
            errorLabelContainer: a([]),
            onsubmit: !0,
            ignore: ":hidden",
            ignoreTitle: !1,
            onfocusin: function(a) {
                this.lastActive = a, this.settings.focusCleanup && (this.settings.unhighlight && this.settings.unhighlight.call(this, a, this.settings.errorClass, this.settings.validClass), 
                this.hideThese(this.errorsFor(a)));
            },
            onfocusout: function(a) {
                this.checkable(a) || !(a.name in this.submitted) && this.optional(a) || this.element(a);
            },
            onkeyup: function(b, c) {
                var d = [ 16, 17, 18, 20, 35, 36, 37, 38, 39, 40, 45, 144, 225 ];
                9 === c.which && "" === this.elementValue(b) || -1 !== a.inArray(c.keyCode, d) || (b.name in this.submitted || b === this.lastElement) && this.element(b);
            },
            onclick: function(a) {
                a.name in this.submitted ? this.element(a) : a.parentNode.name in this.submitted && this.element(a.parentNode);
            },
            highlight: function(b, c, d) {
                "radio" === b.type ? this.findByName(b.name).addClass(c).removeClass(d) : a(b).addClass(c).removeClass(d);
            },
            unhighlight: function(b, c, d) {
                "radio" === b.type ? this.findByName(b.name).removeClass(c).addClass(d) : a(b).removeClass(c).addClass(d);
            }
        },
        setDefaults: function(b) {
            a.extend(a.validator.defaults, b);
        },
        messages: {
            required: "This field is required.",
            remote: "Please fix this field.",
            email: "Please enter a valid email address.",
            url: "Please enter a valid URL.",
            date: "Please enter a valid date.",
            dateISO: "Please enter a valid date ( ISO ).",
            number: "Please enter a valid number.",
            digits: "Please enter only digits.",
            creditcard: "Please enter a valid credit card number.",
            equalTo: "Please enter the same value again.",
            maxlength: a.validator.format("Please enter no more than {0} characters."),
            minlength: a.validator.format("Please enter at least {0} characters."),
            rangelength: a.validator.format("Please enter a value between {0} and {1} characters long."),
            range: a.validator.format("Please enter a value between {0} and {1}."),
            max: a.validator.format("Please enter a value less than or equal to {0}."),
            min: a.validator.format("Please enter a value greater than or equal to {0}.")
        },
        autoCreateRanges: !1,
        prototype: {
            init: function() {
                function b(b) {
                    var c = a.data(this.form, "validator"), d = "on" + b.type.replace(/^validate/, ""), e = c.settings;
                    e[d] && !a(this).is(e.ignore) && e[d].call(c, this, b);
                }
                this.labelContainer = a(this.settings.errorLabelContainer), this.errorContext = this.labelContainer.length && this.labelContainer || a(this.currentForm), 
                this.containers = a(this.settings.errorContainer).add(this.settings.errorLabelContainer), 
                this.submitted = {}, this.valueCache = {}, this.pendingRequest = 0, this.pending = {}, 
                this.invalid = {}, this.reset();
                var c, d = this.groups = {};
                a.each(this.settings.groups, function(b, c) {
                    "string" == typeof c && (c = c.split(/\s/)), a.each(c, function(a, c) {
                        d[c] = b;
                    });
                }), c = this.settings.rules, a.each(c, function(b, d) {
                    c[b] = a.validator.normalizeRule(d);
                }), a(this.currentForm).on("focusin.validate focusout.validate keyup.validate", ":text, [type='password'], [type='file'], select, textarea, [type='number'], [type='search'], [type='tel'], [type='url'], [type='email'], [type='datetime'], [type='date'], [type='month'], [type='week'], [type='time'], [type='datetime-local'], [type='range'], [type='color'], [type='radio'], [type='checkbox']", b).on("click.validate", "select, option, [type='radio'], [type='checkbox']", b), 
                this.settings.invalidHandler && a(this.currentForm).on("invalid-form.validate", this.settings.invalidHandler), 
                a(this.currentForm).find("[required], [data-rule-required], .required").attr("aria-required", "true");
            },
            form: function() {
                return this.checkForm(), a.extend(this.submitted, this.errorMap), this.invalid = a.extend({}, this.errorMap), 
                this.valid() || a(this.currentForm).triggerHandler("invalid-form", [ this ]), this.showErrors(), 
                this.valid();
            },
            checkForm: function() {
                this.prepareForm();
                for (var a = 0, b = this.currentElements = this.elements(); b[a]; a++) this.check(b[a]);
                return this.valid();
            },
            element: function(b) {
                var c = this.clean(b), d = this.validationTargetFor(c), e = !0;
                return this.lastElement = d, void 0 === d ? delete this.invalid[c.name] : (this.prepareElement(d), 
                this.currentElements = a(d), e = !1 !== this.check(d), e ? delete this.invalid[d.name] : this.invalid[d.name] = !0), 
                a(b).attr("aria-invalid", !e), this.numberOfInvalids() || (this.toHide = this.toHide.add(this.containers)), 
                this.showErrors(), e;
            },
            showErrors: function(b) {
                if (b) {
                    a.extend(this.errorMap, b), this.errorList = [];
                    for (var c in b) this.errorList.push({
                        message: b[c],
                        element: this.findByName(c)[0]
                    });
                    this.successList = a.grep(this.successList, function(a) {
                        return !(a.name in b);
                    });
                }
                this.settings.showErrors ? this.settings.showErrors.call(this, this.errorMap, this.errorList) : this.defaultShowErrors();
            },
            resetForm: function() {
                a.fn.resetForm && a(this.currentForm).resetForm(), this.submitted = {}, this.lastElement = null, 
                this.prepareForm(), this.hideErrors();
                var b, c = this.elements().removeData("previousValue").removeAttr("aria-invalid");
                if (this.settings.unhighlight) for (b = 0; c[b]; b++) this.settings.unhighlight.call(this, c[b], this.settings.errorClass, ""); else c.removeClass(this.settings.errorClass);
            },
            numberOfInvalids: function() {
                return this.objectLength(this.invalid);
            },
            objectLength: function(a) {
                var b, c = 0;
                for (b in a) c++;
                return c;
            },
            hideErrors: function() {
                this.hideThese(this.toHide);
            },
            hideThese: function(a) {
                a.not(this.containers).text(""), this.addWrapper(a).hide();
            },
            valid: function() {
                return 0 === this.size();
            },
            size: function() {
                return this.errorList.length;
            },
            focusInvalid: function() {
                if (this.settings.focusInvalid) try {
                    a(this.findLastActive() || this.errorList.length && this.errorList[0].element || []).filter(":visible").focus().trigger("focusin");
                } catch (b) {}
            },
            findLastActive: function() {
                var b = this.lastActive;
                return b && 1 === a.grep(this.errorList, function(a) {
                    return a.element.name === b.name;
                }).length && b;
            },
            elements: function() {
                var b = this, c = {};
                return a(this.currentForm).find("input, select, textarea").not(":submit, :reset, :image, :disabled").not(this.settings.ignore).filter(function() {
                    return !this.name && b.settings.debug && window.console && console.error("%o has no name assigned", this), 
                    !(this.name in c || !b.objectLength(a(this).rules())) && (c[this.name] = !0, !0);
                });
            },
            clean: function(b) {
                return a(b)[0];
            },
            errors: function() {
                var b = this.settings.errorClass.split(" ").join(".");
                return a(this.settings.errorElement + "." + b, this.errorContext);
            },
            reset: function() {
                this.successList = [], this.errorList = [], this.errorMap = {}, this.toShow = a([]), 
                this.toHide = a([]), this.currentElements = a([]);
            },
            prepareForm: function() {
                this.reset(), this.toHide = this.errors().add(this.containers);
            },
            prepareElement: function(a) {
                this.reset(), this.toHide = this.errorsFor(a);
            },
            elementValue: function(b) {
                var c, d = a(b), e = b.type;
                return "radio" === e || "checkbox" === e ? this.findByName(b.name).filter(":checked").val() : "number" === e && void 0 !== b.validity ? !b.validity.badInput && d.val() : (c = d.val(), 
                "string" == typeof c ? c.replace(/\r/g, "") : c);
            },
            check: function(b) {
                b = this.validationTargetFor(this.clean(b));
                var c, d, e, f = a(b).rules(), g = a.map(f, function(a, b) {
                    return b;
                }).length, h = !1, i = this.elementValue(b);
                for (d in f) {
                    e = {
                        method: d,
                        parameters: f[d]
                    };
                    try {
                        if ("dependency-mismatch" === (c = a.validator.methods[d].call(this, i, b, e.parameters)) && 1 === g) {
                            h = !0;
                            continue;
                        }
                        if (h = !1, "pending" === c) return void (this.toHide = this.toHide.not(this.errorsFor(b)));
                        if (!c) return this.formatAndAdd(b, e), !1;
                    } catch (j) {
                        throw this.settings.debug && window.console && console.log("Exception occurred when checking element " + b.id + ", check the '" + e.method + "' method.", j), 
                        j instanceof TypeError && (j.message += ".  Exception occurred when checking element " + b.id + ", check the '" + e.method + "' method."), 
                        j;
                    }
                }
                if (!h) return this.objectLength(f) && this.successList.push(b), !0;
            },
            customDataMessage: function(b, c) {
                return a(b).data("msg" + c.charAt(0).toUpperCase() + c.substring(1).toLowerCase()) || a(b).data("msg");
            },
            customMessage: function(a, b) {
                var c = this.settings.messages[a];
                return c && (c.constructor === String ? c : c[b]);
            },
            findDefined: function() {
                for (var a = 0; a < arguments.length; a++) if (void 0 !== arguments[a]) return arguments[a];
            },
            defaultMessage: function(b, c) {
                return this.findDefined(this.customMessage(b.name, c), this.customDataMessage(b, c), !this.settings.ignoreTitle && b.title || void 0, a.validator.messages[c], "<strong>Warning: No message defined for " + b.name + "</strong>");
            },
            formatAndAdd: function(b, c) {
                var d = this.defaultMessage(b, c.method), e = /\$?\{(\d+)\}/g;
                "function" == typeof d ? d = d.call(this, c.parameters, b) : e.test(d) && (d = a.validator.format(d.replace(e, "{$1}"), c.parameters)), 
                this.errorList.push({
                    message: d,
                    element: b,
                    method: c.method
                }), this.errorMap[b.name] = d, this.submitted[b.name] = d;
            },
            addWrapper: function(a) {
                return this.settings.wrapper && (a = a.add(a.parent(this.settings.wrapper))), a;
            },
            defaultShowErrors: function() {
                var a, b, c;
                for (a = 0; this.errorList[a]; a++) c = this.errorList[a], this.settings.highlight && this.settings.highlight.call(this, c.element, this.settings.errorClass, this.settings.validClass), 
                this.showLabel(c.element, c.message);
                if (this.errorList.length && (this.toShow = this.toShow.add(this.containers)), this.settings.success) for (a = 0; this.successList[a]; a++) this.showLabel(this.successList[a]);
                if (this.settings.unhighlight) for (a = 0, b = this.validElements(); b[a]; a++) this.settings.unhighlight.call(this, b[a], this.settings.errorClass, this.settings.validClass);
                this.toHide = this.toHide.not(this.toShow), this.hideErrors(), this.addWrapper(this.toShow).show();
            },
            validElements: function() {
                return this.currentElements.not(this.invalidElements());
            },
            invalidElements: function() {
                return a(this.errorList).map(function() {
                    return this.element;
                });
            },
            showLabel: function(b, c) {
                var d, e, f, g = this.errorsFor(b), h = this.idOrName(b), i = a(b).attr("aria-describedby");
                g.length ? (g.removeClass(this.settings.validClass).addClass(this.settings.errorClass), 
                g.html(c)) : (g = a("<" + this.settings.errorElement + ">").attr("id", h + "-error").addClass(this.settings.errorClass).html(c || ""), 
                d = g, this.settings.wrapper && (d = g.hide().show().wrap("<" + this.settings.wrapper + "/>").parent()), 
                this.labelContainer.length ? this.labelContainer.append(d) : this.settings.errorPlacement ? this.settings.errorPlacement(d, a(b)) : d.insertAfter(b), 
                g.is("label") ? g.attr("for", h) : 0 === g.parents("label[for='" + h + "']").length && (f = g.attr("id").replace(/(:|\.|\[|\]|\$)/g, "\\$1"), 
                i ? i.match(new RegExp("\\b" + f + "\\b")) || (i += " " + f) : i = f, a(b).attr("aria-describedby", i), 
                (e = this.groups[b.name]) && a.each(this.groups, function(b, c) {
                    c === e && a("[name='" + b + "']", this.currentForm).attr("aria-describedby", g.attr("id"));
                }))), !c && this.settings.success && (g.text(""), "string" == typeof this.settings.success ? g.addClass(this.settings.success) : this.settings.success(g, b)), 
                this.toShow = this.toShow.add(g);
            },
            errorsFor: function(b) {
                var c = this.idOrName(b), d = a(b).attr("aria-describedby"), e = "label[for='" + c + "'], label[for='" + c + "'] *";
                return d && (e = e + ", #" + d.replace(/\s+/g, ", #")), this.errors().filter(e);
            },
            idOrName: function(a) {
                return this.groups[a.name] || (this.checkable(a) ? a.name : a.id || a.name);
            },
            validationTargetFor: function(b) {
                return this.checkable(b) && (b = this.findByName(b.name)), a(b).not(this.settings.ignore)[0];
            },
            checkable: function(a) {
                return /radio|checkbox/i.test(a.type);
            },
            findByName: function(b) {
                return a(this.currentForm).find("[name='" + b + "']");
            },
            getLength: function(b, c) {
                switch (c.nodeName.toLowerCase()) {
                  case "select":
                    return a("option:selected", c).length;

                  case "input":
                    if (this.checkable(c)) return this.findByName(c.name).filter(":checked").length;
                }
                return b.length;
            },
            depend: function(a, b) {
                return !this.dependTypes[typeof a] || this.dependTypes[typeof a](a, b);
            },
            dependTypes: {
                boolean: function(a) {
                    return a;
                },
                string: function(b, c) {
                    return !!a(b, c.form).length;
                },
                function: function(a, b) {
                    return a(b);
                }
            },
            optional: function(b) {
                var c = this.elementValue(b);
                return !a.validator.methods.required.call(this, c, b) && "dependency-mismatch";
            },
            startRequest: function(a) {
                this.pending[a.name] || (this.pendingRequest++, this.pending[a.name] = !0);
            },
            stopRequest: function(b, c) {
                this.pendingRequest--, this.pendingRequest < 0 && (this.pendingRequest = 0), delete this.pending[b.name], 
                c && 0 === this.pendingRequest && this.formSubmitted && this.form() ? (a(this.currentForm).submit(), 
                this.formSubmitted = !1) : !c && 0 === this.pendingRequest && this.formSubmitted && (a(this.currentForm).triggerHandler("invalid-form", [ this ]), 
                this.formSubmitted = !1);
            },
            previousValue: function(b) {
                return a.data(b, "previousValue") || a.data(b, "previousValue", {
                    old: null,
                    valid: !0,
                    message: this.defaultMessage(b, "remote")
                });
            },
            destroy: function() {
                this.resetForm(), a(this.currentForm).off(".validate").removeData("validator");
            }
        },
        classRuleSettings: {
            required: {
                required: !0
            },
            email: {
                email: !0
            },
            url: {
                url: !0
            },
            date: {
                date: !0
            },
            dateISO: {
                dateISO: !0
            },
            number: {
                number: !0
            },
            digits: {
                digits: !0
            },
            creditcard: {
                creditcard: !0
            }
        },
        addClassRules: function(b, c) {
            b.constructor === String ? this.classRuleSettings[b] = c : a.extend(this.classRuleSettings, b);
        },
        classRules: function(b) {
            var c = {}, d = a(b).attr("class");
            return d && a.each(d.split(" "), function() {
                this in a.validator.classRuleSettings && a.extend(c, a.validator.classRuleSettings[this]);
            }), c;
        },
        normalizeAttributeRule: function(a, b, c, d) {
            /min|max/.test(c) && (null === b || /number|range|text/.test(b)) && (d = Number(d), 
            isNaN(d) && (d = void 0)), d || 0 === d ? a[c] = d : b === c && "range" !== b && (a[c] = !0);
        },
        attributeRules: function(b) {
            var c, d, e = {}, f = a(b), g = b.getAttribute("type");
            for (c in a.validator.methods) "required" === c ? (d = b.getAttribute(c), "" === d && (d = !0), 
            d = !!d) : d = f.attr(c), this.normalizeAttributeRule(e, g, c, d);
            return e.maxlength && /-1|2147483647|524288/.test(e.maxlength) && delete e.maxlength, 
            e;
        },
        dataRules: function(b) {
            var c, d, e = {}, f = a(b), g = b.getAttribute("type");
            for (c in a.validator.methods) d = f.data("rule" + c.charAt(0).toUpperCase() + c.substring(1).toLowerCase()), 
            this.normalizeAttributeRule(e, g, c, d);
            return e;
        },
        staticRules: function(b) {
            var c = {}, d = a.data(b.form, "validator");
            return d.settings.rules && (c = a.validator.normalizeRule(d.settings.rules[b.name]) || {}), 
            c;
        },
        normalizeRules: function(b, c) {
            return a.each(b, function(d, e) {
                if (!1 === e) return void delete b[d];
                if (e.param || e.depends) {
                    var f = !0;
                    switch (typeof e.depends) {
                      case "string":
                        f = !!a(e.depends, c.form).length;
                        break;

                      case "function":
                        f = e.depends.call(c, c);
                    }
                    f ? b[d] = void 0 === e.param || e.param : delete b[d];
                }
            }), a.each(b, function(d, e) {
                b[d] = a.isFunction(e) ? e(c) : e;
            }), a.each([ "minlength", "maxlength" ], function() {
                b[this] && (b[this] = Number(b[this]));
            }), a.each([ "rangelength", "range" ], function() {
                var c;
                b[this] && (a.isArray(b[this]) ? b[this] = [ Number(b[this][0]), Number(b[this][1]) ] : "string" == typeof b[this] && (c = b[this].replace(/[\[\]]/g, "").split(/[\s,]+/), 
                b[this] = [ Number(c[0]), Number(c[1]) ]));
            }), a.validator.autoCreateRanges && (null != b.min && null != b.max && (b.range = [ b.min, b.max ], 
            delete b.min, delete b.max), null != b.minlength && null != b.maxlength && (b.rangelength = [ b.minlength, b.maxlength ], 
            delete b.minlength, delete b.maxlength)), b;
        },
        normalizeRule: function(b) {
            if ("string" == typeof b) {
                var c = {};
                a.each(b.split(/\s/), function() {
                    c[this] = !0;
                }), b = c;
            }
            return b;
        },
        addMethod: function(b, c, d) {
            a.validator.methods[b] = c, a.validator.messages[b] = void 0 !== d ? d : a.validator.messages[b], 
            c.length < 3 && a.validator.addClassRules(b, a.validator.normalizeRule(b));
        },
        methods: {
            required: function(b, c, d) {
                if (!this.depend(d, c)) return "dependency-mismatch";
                if ("select" === c.nodeName.toLowerCase()) {
                    var e = a(c).val();
                    return e && e.length > 0;
                }
                return this.checkable(c) ? this.getLength(b, c) > 0 : b.length > 0;
            },
            email: function(a, b) {
                return this.optional(b) || /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(a);
            },
            url: function(a, b) {
                return this.optional(b) || /^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[\/?#]\S*)?$/i.test(a);
            },
            date: function(a, b) {
                return this.optional(b) || !/Invalid|NaN/.test(new Date(a).toString());
            },
            dateISO: function(a, b) {
                return this.optional(b) || /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/.test(a);
            },
            number: function(a, b) {
                return this.optional(b) || /^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(a);
            },
            digits: function(a, b) {
                return this.optional(b) || /^\d+$/.test(a);
            },
            creditcard: function(a, b) {
                if (this.optional(b)) return "dependency-mismatch";
                if (/[^0-9 \-]+/.test(a)) return !1;
                var c, d, e = 0, f = 0, g = !1;
                if (a = a.replace(/\D/g, ""), a.length < 13 || a.length > 19) return !1;
                for (c = a.length - 1; c >= 0; c--) d = a.charAt(c), f = parseInt(d, 10), g && (f *= 2) > 9 && (f -= 9), 
                e += f, g = !g;
                return e % 10 == 0;
            },
            minlength: function(b, c, d) {
                var e = a.isArray(b) ? b.length : this.getLength(b, c);
                return this.optional(c) || e >= d;
            },
            maxlength: function(b, c, d) {
                var e = a.isArray(b) ? b.length : this.getLength(b, c);
                return this.optional(c) || d >= e;
            },
            rangelength: function(b, c, d) {
                var e = a.isArray(b) ? b.length : this.getLength(b, c);
                return this.optional(c) || e >= d[0] && e <= d[1];
            },
            min: function(a, b, c) {
                return this.optional(b) || a >= c;
            },
            max: function(a, b, c) {
                return this.optional(b) || c >= a;
            },
            range: function(a, b, c) {
                return this.optional(b) || a >= c[0] && a <= c[1];
            },
            equalTo: function(b, c, d) {
                var e = a(d);
                return this.settings.onfocusout && e.off(".validate-equalTo").on("blur.validate-equalTo", function() {
                    a(c).valid();
                }), b === e.val();
            },
            remote: function(b, c, d) {
                if (this.optional(c)) return "dependency-mismatch";
                var e, f, g = this.previousValue(c);
                return this.settings.messages[c.name] || (this.settings.messages[c.name] = {}), 
                g.originalMessage = this.settings.messages[c.name].remote, this.settings.messages[c.name].remote = g.message, 
                d = "string" == typeof d && {
                    url: d
                } || d, g.old === b ? g.valid : (g.old = b, e = this, this.startRequest(c), f = {}, 
                f[c.name] = b, a.ajax(a.extend(!0, {
                    mode: "abort",
                    port: "validate" + c.name,
                    dataType: "json",
                    data: f,
                    context: e.currentForm,
                    success: function(d) {
                        var f, h, i, j = !0 === d || "true" === d;
                        e.settings.messages[c.name].remote = g.originalMessage, j ? (i = e.formSubmitted, 
                        e.prepareElement(c), e.formSubmitted = i, e.successList.push(c), delete e.invalid[c.name], 
                        e.showErrors()) : (f = {}, h = d || e.defaultMessage(c, "remote"), f[c.name] = g.message = a.isFunction(h) ? h(b) : h, 
                        e.invalid[c.name] = !0, e.showErrors(f)), g.valid = j, e.stopRequest(c, j);
                    }
                }, d)), "pending");
            }
        }
    });
    var b, c = {};
    a.ajaxPrefilter ? a.ajaxPrefilter(function(a, b, d) {
        var e = a.port;
        "abort" === a.mode && (c[e] && c[e].abort(), c[e] = d);
    }) : (b = a.ajax, a.ajax = function(d) {
        var e = ("mode" in d ? d : a.ajaxSettings).mode, f = ("port" in d ? d : a.ajaxSettings).port;
        return "abort" === e ? (c[f] && c[f].abort(), c[f] = b.apply(this, arguments), c[f]) : b.apply(this, arguments);
    });
}), window.TATA || (window.TATA = {});

var TATA = window.TATA;

TATA.CommonFunctions = {
    Toggle: function() {
        $(".toggle-link").on("click", function() {
            var Target = $(this).data("target-id");
            return $(".toggle-skip").not(Target).removeClass("active"), $(Target).toggleClass("active"), 
            !1;
        });
    },
    DocumentClick: function() {
        var elem = $(".toggle-skip");
        $(document).on("click", function(event) {
            elem.removeClass("active"), setTimeout(function() {
                $("#header-account").removeClass("active-sign-in active-sign-up active-forget-password");
            }, 500);
        }), elem.click(function(e) {
            e.stopPropagation();
        });
    },
    MainBanner: function() {
        $(".main-banner").slick({
            arrows: !1,
            dots: !0
        });
    },
    LookBookSlider: function() {
        $(".look-book-carousel").slick();
    },
    BrandSlider: function() {
        $(".brand-slider").slick({
            infinite: !0,
            slidesToShow: 3,
            slidesToScroll: 3,
            dots: !0,
            responsive: [ {
                breakpoint: 768,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                    arrows: !1
                }
            } ]
        });
    },
    Accordion: function() {
        $(document).on("click", ".accordion .accordion-title", function() {
            return $(this).toggleClass("active").next().stop().slideToggle(500), Acc.not($(this)).removeClass("active"), 
            $(".accordion-content").not($(this).next()).stop().slideUp(500), !1;
        });
    },
    ShopByCatagorySlider: function() {
        $(".shop-by-catagory-slider").slick({
            slidesToScroll: 6,
            slidesToShow: 6,
            variableWidth: !1,
            infinite: !1,
            arrows: !1,
            swipe: !1,
            dots: !0,
            responsive: [ {
                breakpoint: 768,
                settings: {
                    slidesToScroll: 1,
                    slidesToShow: 1,
                    infinite: !0,
                    swipe: !0,
                    variableWidth: !0
                }
            } ]
        });
    },
    wishlistInit: function() {
        $(document).on("click", ".add-to-wishlist", function() {
            $(this).hasClass("added") ? TATA.CommonFunctions.removeFromWishlist($(this).data("product"), this) : TATA.CommonFunctions.addToWishlist($(this).data("product"), this);
        });
    },
    urlToProductCode: function(productURL) {
        var n = productURL.lastIndexOf("-");
        return productURL.substring(n + 1, productURL.length).toUpperCase();
    },
    addToWishlist: function(productURL, element) {
        var productCode = TATA.CommonFunctions.urlToProductCode(productURL), requiredUrl = ACC.config.encodedContextPath + "/search/addToWishListInPLP", sizeSelected = !0;
        $("#variant li").hasClass("selected") && "#" != $("#variant,#sizevariant option:selected").val() || (sizeSelected = !1);
        var dataString = "wish=&product=" + productCode + "&sizeSelected=" + sizeSelected;
        if (!headerLoggedinStatus) return $(".wishAddLoginPlp").addClass("active"), setTimeout(function() {
            $(".wishAddLoginPlp").removeClass("active");
        }, 3e3), !1;
        $.ajax({
            contentType: "application/json; charset=utf-8",
            url: requiredUrl,
            data: dataString,
            dataType: "json",
            success: function(data) {
                1 == data ? ($(".wishAddSucessPlp").addClass("active"), setTimeout(function() {
                    $(".wishAddSucessPlp").removeClass("active");
                }, 3e3), $(element).addClass("added")) : ($(".wishAlreadyAddedPlp").addClass("active"), 
                setTimeout(function() {
                    $(".wishAlreadyAddedPlp").removeClass("active");
                }, 3e3)), $(element).addClass("added");
            },
            error: function(xhr, status, error) {
                alert(error), "undefined" != typeof utag && utag.link({
                    error_type: "wishlist_error"
                });
            }
        }), setTimeout(function() {
            $("a.wishlist#wishlist").popover("hide"), $("input.wishlist#add_to_wishlist").popover("hide");
        }, 0);
    },
    removeFromWishlist: function(productURL, element) {
        var productCode = TATA.CommonFunctions.urlToProductCode(productURL), requiredUrl = ACC.config.encodedContextPath + "/search/removeFromWishListInPLP", sizeSelected = !0;
        $("#variant li").hasClass("selected") && "#" != $("#variant,#sizevariant option:selected").val() || (sizeSelected = !1);
        var dataString = "wish=&product=" + productCode + "&sizeSelected=" + sizeSelected;
        return headerLoggedinStatus ? ($.ajax({
            contentType: "application/json; charset=utf-8",
            url: requiredUrl,
            data: dataString,
            dataType: "json",
            success: function(data) {
                1 == data ? ($(".wishRemoveSucessPlp").addClass("active"), setTimeout(function() {
                    $(".wishRemoveSucessPlp").removeClass("active");
                }, 3e3), $(element).removeClass("added")) : ($(".wishAlreadyAddedPlp").addClass("active"), 
                setTimeout(function() {
                    $(".wishAlreadyAddedPlp").removeClass("active");
                }, 3e3)), $(element).removeClass("added");
            },
            error: function(xhr, status, error) {
                alert(error);
            }
        }), setTimeout(function() {
            $("a.wishlist#wishlist").popover("hide"), $("input.wishlist#add_to_wishlist").popover("hide");
        }, 0), !1) : ($(".wishAddLoginPlp").addClass("active"), setTimeout(function() {
            $(".wishAddLoginPlp").removeClass("active");
        }, 3e3), !1);
    },
    leftBarAccordian: function() {
        $(window).width() >= 768 ? $(document).on("click", ".facetHead", function(e) {
            e.stopPropagation(), $(this).closest(".facet").toggleClass("open", function() {
                $(this).find(".allFacetValues").slideToggle();
            });
        }) : $(document).on("click", ".facetHead", function(e) {
            e.stopPropagation(), $(this).closest(".facet").addClass("open").find(".allFacetValues").show(), 
            $(this).closest(".facet").siblings().removeClass("open").find(".allFacetValues").hide();
        });
    },
    WindowScroll: function() {
        var winWidth = $(window).width();
        $(window).scrollTop() > 1 ? $("body").addClass("sticky-nav") : $("body").removeClass("sticky-nav");
        var $window = $(window), leftbarElelTop = $(".plp-banner").outerHeight() + $("header").outerHeight();
        winWidth >= 768 && $(".leftbar").length && ($(window).scrollTop() >= $(".product-grid:last-child").offset().top - 100 ? $(".leftbar").removeClass("sticky-leftbar") : $(".leftbar").toggleClass("sticky-leftbar", $window.scrollTop() > leftbarElelTop));
    },
    Header: {
        MobileMenu: function() {
            $("#hamburger-menu").on("click", function() {
                $("body").addClass("menu-open");
            }), $("#main-nav-close").on("click", function() {
                $("body").removeClass("menu-open");
            }), $("#main-nav .sub-menu-toggle").on("click", function() {
                $(this).toggleClass("active").next(".sub-menu").toggleClass("active");
            });
        },
        HeaderMinicart: function() {
            $("header .mini-bag").hide(), $("header .mini-cart-link,header #myWishlistHeader").html(""), 
            $("header .bag").hover(function() {
                $(this).find(".mini-bag").show();
            }, function() {
                $(this).find(".mini-bag").hide();
            });
        },
        init: function() {
            this.MobileMenu(), this.HeaderMinicart();
        }
    },
    Footer: function() {
        $(window).width() >= 768 && $("footer").find(".accordion").removeClass("accordion"), 
        $("#main-nav > ul").addClass("footer-cloned-ul").clone().appendTo(".footer-popular-search"), 
        $(".footer-popular-search .footer-cloned-ul > li").append("<br/>"), $(".footer-popular-search .footer-cloned-ul > li").each(function() {
            $(this).find(".sub-menu").length ? $(this).show() : $(this).hide();
        }), $(document).on("click", ".accordion h3", function() {
            return $(this).toggleClass("active").next().stop().slideToggle(500), Acc.not($(this)).removeClass("active"), 
            $(".accordion-content").not($(this).next()).stop().slideUp(500), !1;
        });
    },
    init: function() {
        var _self = TATA.CommonFunctions;
        _self.Header.init(), _self.Footer(), _self.Toggle(), _self.DocumentClick(), _self.WindowScroll(), 
        _self.MainBanner(), _self.LookBookSlider(), _self.BrandSlider(), _self.Accordion(), 
        _self.ShopByCatagorySlider(), _self.wishlistInit(), _self.leftBarAccordian();
    }
}, TATA.Pages = {
    PLP: {
        loadMoreInit: function() {
            var pathName = window.location.pathname, pageType = (window.location.search, $("#pageType").val());
            totalNoOfPages = parseInt($("input[name=noOfPages]").val()), "" == totalNoOfPages || totalNoOfPages, 
            currentPageNo = 1, $(document).on("click", ".loadMore", function() {
                pageQuery = $("#pageQuery").val(), "" == pageQuery && (url = "productsearch" == pageType ? $("#searchPageDeptHierTreeForm").serialize() : $("#categoryPageDeptHierTreeForm").serialize(), 
                pageQuery = url + TATA.Pages.PLP.addSortParameter()), "" != pageQuery && /page-[0-9]+/.test(pageQuery) ? (pageQueryString = pageQuery.match(/page-[0-9]+/), 
                prevPageNoString = pageQueryString[0].split("-"), prevPageNo = parseInt(prevPageNoString[1]), 
                currentPageNo = prevPageNo + 1, ajaxUrl = pageQuery.replace(/page-[0-9]+/, "page-" + currentPageNo)) : (currentPageNo++, 
                ajaxUrl = pathName.replace(/[\/]$/, "") + "/page-" + currentPageNo + "?" + pageQuery), 
                currentPageNo <= totalNoOfPages && (TATA.Pages.PLP.performLoadMore(ajaxUrl), currentPageNo == totalNoOfPages && $(this).hide());
            });
        },
        sortInit: function() {
            $(document).on("change", ".responsiveSort", function() {
                TATA.Pages.PLP.performSort();
            });
        },
        addSortParameter: function() {
            var item = $(".responsiveSort").val(), url = "";
            switch (item) {
              case "relevance":
                url += "&sort=relevance";
                break;

              case "new":
                url += "&sort=isProductNew";
                break;

              case "discount":
                url += "&sort=isDiscountedPrice";
                break;

              case "low":
                url += "&sort=price-asc";
                break;

              case "high":
                url += "&sort=price-desc";
            }
            return url;
        },
        performSort: function() {
            var pathName = window.location.pathname, pageType = $("#pageType").val();
            pathName = pathName.replace(/page-[0-9]+/, "page-1"), url = "productsearch" == pageType ? $("#searchPageDeptHierTreeForm").serialize() : $("#categoryPageDeptHierTreeForm").serialize();
            var queryUrl = url + TATA.Pages.PLP.addSortParameter();
            TATA.Pages.PLP.performAjax(pathName + "?" + queryUrl);
        },
        performLoadMore: function(ajaxUrl) {
            $("body").addClass("loader"), $.ajax({
                url: ajaxUrl,
                data: {
                    pageSize: 24,
                    lazyInterface: "Y"
                },
                success: function(x) {
                    var filtered = $.parseHTML(x);
                    $(filtered).has(".product-grid") && $(".product-grid-wrapper").append($(filtered).find(".product-grid-wrapper")), 
                    $("#pageQuery").val(ajaxUrl);
                },
                complete: function() {
                    $("body").removeClass("loader");
                }
            });
        },
        showSelectedRefinements: function() {
            $(".facetValues .facet-form input:checked").each(function() {
                $(this).parents(".allFacetValues").show(), $(this).parents(".facet").addClass("open");
            }), $(".facet-form input[type=checkbox]").each(function() {
                var colorString = $(this).attr("data-colour"), colorArray = colorString.split("_"), colorCode = "#" + colorArray[1];
                $(this).next("label").append("<span class='plp-filter-color'></span>"), $(this).next("label").find(".plp-filter-color").css("background-color", colorCode);
            });
        },
        filterByFacet: function() {
            $(document).on("click", ".reset-filters", function() {
                var resetUrl = $(this).data("resetqueryurl") + $(".responsiveSort").find(":selected");
                TATA.Pages.PLP.performAjax(resetUrl);
            }), $(document).on("click", ".remove-filter", function() {
                var relevantCheckbox = $(this).attr("data-facetCode");
                $("#" + relevantCheckbox).click();
            }), $(document).on("change", ".facet-form input:checkbox", function() {
                var requestUrl = $(this).closest("form").attr("action") + "?" + $(this).closest("form").serialize();
                TATA.Pages.PLP.performAjax(requestUrl);
            });
        },
        performAjax: function(requestUrl) {
            $("body").addClass("loader"), $.ajax({
                url: requestUrl,
                data: {
                    lazyInterface: "Y"
                },
                success: function(x) {
                    var filtered = $.parseHTML(x);
                    $(filtered).has(".filterblocks") && ($(".filterblocks").html($(filtered).find(".filterblocks")), 
                    TATA.Pages.PLP.showSelectedRefinements()), $(filtered).has(".product-grid") && $(".product-grid-wrapper").html($(filtered).find(".product-grid-wrapper").html()), 
                    $(filtered).has("input[name=noOfPages]") && (totalPages = parseInt($(filtered).find("input[name=noOfPages]").val()), 
                    totalPages > 1 ? ($("#pageQuery").val(""), currentPageNo = 1, totalNoOfPages = totalPages, 
                    $(".loadMore").show()) : $(".loadMore").hide());
                },
                complete: function() {
                    $("body").removeClass("loader"), $(".plp-leftbar-close a").on("click", function() {
                        $(".leftbar").removeClass("active"), $(".facet").removeClass("open");
                    });
                }
            });
        },
        Filtershow: function() {
            $(".plp-mob-filter").on("click", function() {
                $(".leftbar").addClass("active");
            }), $(".plp-leftbar-close a").on("click", function() {
                $(".leftbar").removeClass("active"), $(".facet").removeClass("open");
            });
        },
        showModelImg: function() {
            $(".plp-modelimg-show").on("click", function() {
                $(".plp-model-img").show().siblings().hide();
            }), $(".plp-productimg-show").on("click", function() {
                $(".plp-model-img").hide().siblings(".plp-default-img").show();
            });
        },
        addGiftWrap: function() {
            $("#addGiftWrap").on("change", function() {
                $(this).is(":checked") ? $(this).parents(".addGiftBlock").addClass("giftWrapAdded") : $(this).parents(".addGiftBlock").removeClass("giftWrapAdded");
            });
        },
        TwoColumnseperator: function() {
            $(".grid-count-two").on("click", function() {
                $(".product-list-wrapper").addClass("twocolumngrid");
            }), $(".grid-count-three").on("click", function() {
                $(".product-list-wrapper").removeClass("twocolumngrid");
            });
        },
        ProductSort: function() {
            $(".sort-wrapper .btn").on("click", function() {
                $(this).addClass("active").siblings().removeClass("active");
            });
        },
        productGrid: function() {
            $(".grid-seperator").on("click", function() {
                $(this).addClass("active").siblings().removeClass("active");
            });
        },
        productHover: function() {
            $(".product-grid").hover(function() {
                $(this).addClass("hover"), $(this).find(".plp-hover-img").show().siblings().hide();
            }, function() {
                $(this).removeClass("hover"), $(".plp-modelimg-show.active").length ? $(this).find(".plp-model-img").show().siblings().hide() : $(this).find(".plp-default-img").show().siblings().hide();
            });
        },
        init: function() {
            var _self = TATA.Pages.PLP;
            _self.Filtershow(), _self.showModelImg(), _self.addGiftWrap(), _self.TwoColumnseperator(), 
            _self.ProductSort(), _self.productGrid(), _self.productHover(), _self.filterByFacet(), 
            _self.showSelectedRefinements(), _self.sortInit(), _self.loadMoreInit();
        }
    },
    PDP: {
        Slider: function() {
            $(".pdp-img-slider").slick({
                slidesToShow: 1,
                slidesToScroll: 1,
                arrows: !0,
                fade: !0,
                asNavFor: ".pdp-img-nav",
                responsive: [ {
                    breakpoint: 768,
                    settings: {
                        fade: !1,
                        dots: !0,
                        arrows: !1
                    }
                } ]
            }), $(".pdp-img-nav").slick({
                slidesToShow: 5,
                asNavFor: ".pdp-img-slider",
                arrows: !1,
                vertical: !0,
                focusOnSelect: !0,
                responsive: [ {
                    breakpoint: 768,
                    settings: {
                        fade: !1,
                        dots: !0,
                        arrows: !1
                    }
                } ]
            });
        },
        Zoomer: function() {
            $(".zoomer").elevateZoom({
                zoomWindowWidth: 300,
                zoomWindowHeight: 300
            });
        },
        openPopup: function(url) {
            return newwindow = window.open(url, "name", "height=400,width=400"), window.focus && newwindow.focus(), 
            !1;
        },
        videoPlay: function() {
            $(".pdp-social-links .play").on("click", function() {
                $("body").addClass("pdp-video-active"), $("video").each(function() {
                    this.play();
                });
            }), $(".pdp-img-nav .slick-slide").on("click", function() {
                $("body").removeClass("pdp-video-active"), $("video").each(function() {
                    this.load();
                });
            });
        },
        BankEMI: function() {
            $(".emi-header").on("click", function() {
                var productVal = $("#prodPrice").val(), optionData = "<ul>";
                $("#EMITermTable").hide(), $("#emiTableTHead").hide(), $("#emiTableTbody").hide();
                var requiredUrl = ACC.config.encodedContextPath + "/p-enlistEMIBanks", dataString = "productVal=" + productVal;
                $.ajax({
                    contentType: "application/json; charset=utf-8",
                    url: requiredUrl,
                    data: dataString,
                    dataType: "json",
                    success: function(data) {
                        for (var i = 0; i < data.length; i++) optionData += "<li value='" + data[i] + "'>" + data[i] + "</li>";
                        optionData += "</ul>", $("#bankNameForEMI").html(optionData), utag.link({
                            link_obj: this,
                            link_text: "emi_more_information",
                            event_type: "emi_more_information",
                            product_id: productIdArray
                        });
                    },
                    error: function(xhr, status, error) {}
                });
            }), $("#bankNameForEMI li").on("click", function() {
                var productVal = $("#prodPrice").val(), selectedBank = $("#bankNameForEMI :selected").text(), contentData = "", productId = [];
                if (productId.push($("#product_id").val()), "select" != selectedBank) {
                    var dataString = "selectedEMIBank=" + selectedBank + "&productVal=" + productVal;
                    $.ajax({
                        url: ACC.config.encodedContextPath + "/p-getTerms",
                        data: dataString,
                        type: "GET",
                        cache: !1,
                        success: function(data) {
                            if (null != data) {
                                $("#emiTableTHead").show(), $("#emiTableTbody").show();
                                for (var index = 0; index < data.length; index++) contentData += "<tr>", contentData += "<td>" + data[index].term + "</td>", 
                                contentData += "<td>" + data[index].interestRate + "</td>", contentData += "<td>" + data[index].monthlyInstallment + "</td>", 
                                contentData += "<td>" + data[index].interestPayable + "</td>", contentData += "</tr>";
                                $("#emiTableTbody").html(contentData), $("#EMITermTable").show();
                            } else $("#emiNoData").show();
                            emiBankSelectedTealium = "emi_option_" + selectedBank.replace(/ /g, "").replace(/[^a-z0-9\s]/gi, "").toLowerCase(), 
                            emiBankSelected = selectedBank.toLowerCase().replace(/  +/g, " ").replace(/ /g, "_").replace(/[',."]/g, ""), 
                            "undefined" != typeof utag && utag.link({
                                link_text: emiBankSelectedTealium,
                                event_type: "emi_option_selected",
                                emi_selected_bank: emiBankSelected,
                                product_id: productId
                            });
                        },
                        error: function(resp) {
                            $("#emiSelectBank").show();
                        }
                    });
                }
            });
        },
        init: function() {
            var _self = TATA.Pages.PDP;
            _self.Slider(), _self.Zoomer(), _self.videoPlay(), _self.BankEMI();
        }
    },
    init: function() {
        var _self = TATA.Pages;
        _self.PLP.init(), _self.PDP.init();
    }
}, $(document).ready(function() {
    TATA.CommonFunctions.init(), TATA.Pages.init(), $("select").selectBoxIt(), $(window).width() <= 767 && $(".sort-by-fature .selectboxit-text").html("SORT"), 
    $(".lux-main-banner-slider .electronic-rotatingImage").owlCarousel({
        dots: !0,
        items: 1
    });
}), $(window).scroll(function() {
    TATA.CommonFunctions.WindowScroll();
}), $(document).ready(function() {
    function loginRequest() {
        $(".luxury-login").on("click", function(e) {
            $("#header-account").addClass("active"), $("body").removeClass("menu-open"), e.preventDefault();
            const loginURL = $(this).attr("href");
            $("#login-container .header-sign-in");
            $.ajax({
                url: loginURL,
                beforeSend: function() {
                    $("#login-container .header-sign-in").html('<div class="luxury-loader"></div>');
                },
                success: function(data) {
                    $("#login-container .header-sign-in").html(data);
                },
                complete: function() {
                    pwsRequest(), registerRequest(), targetLink(), LuxLoginValidate();
                }
            });
        });
    }
    function pwsRequest() {
        $(".js-password-forgotten").on("click", function(e) {
            e.preventDefault();
            const pwsRequest = $(this).attr("href");
            $.ajax({
                url: pwsRequest,
                beforeSend: function() {
                    $("#login-container .header-forget-pass").html('<div class="luxury-loader"></div>');
                },
                success: function(data) {
                    $("#login-container .header-forget-pass").html(data);
                },
                complete: function() {
                    registerRequest(), targetLink(), registerRequest(), LuxLoginValidate();
                }
            });
        });
    }
    function registerRequest() {
        $(".register_link").on("click", function(e) {
            e.preventDefault();
            const luxRegister = $(this).attr("href");
            $.ajax({
                url: luxRegister,
                beforeSend: function() {
                    $("#login-container .header-signup").html('<div class="luxury-loader"></div>');
                },
                success: function(data) {
                    $("#login-container .header-signup").html(data);
                },
                complete: function() {
                    loginRequest(), pwsRequest(), targetLink(), LuxLoginValidate();
                }
            });
        });
    }
    function targetLink() {
        $(".header-login-target-link").on("click", function() {
            var targetID = $(this).data("target-id");
            $("#header-account").removeClass("active-sign-in active-sign-up active-forget-password").addClass("active-" + targetID);
        }), $(".get-gender-value").on("click", function() {
            var genderValue = $(this).val();
            $("#gender").val(genderValue);
        });
    }
    function LuxLoginValidate() {
        tul.commonFunctions.init();
    }
    loginRequest();
});

var tul = {};

(function($) {
    "use strict";
    tul.commonFunctions = {
        login: function() {
            $("#loginForm").validate({
                onfocusout: !1,
                invalidHandler: function(form, validator) {
                    validator.numberOfInvalids() && ($("#loginForm").prepend('<div class="invalided-error">' + validator.errorList[0].message + "</div>"), 
                    validator.errorList[0].element.focus());
                },
                rules: {
                    j_username: {
                        required: !0,
                        email: !0,
                        maxlength: 120
                    },
                    j_password: {
                        required: !0,
                        maxlength: 30
                    }
                },
                submitHandler: function(form) {
                    form.submit();
                }
            }), $("#triggerLoginAjax").on("click", function(e) {
                $(".invalided-error").remove(), e.preventDefault(), $("#loginForm").submit();
            });
        },
        signup: function() {
            $("#extRegisterForm").validate({
                onfocusout: !1,
                invalidHandler: function(form, validator) {
                    validator.numberOfInvalids() && ($("#extRegisterForm").prepend('<div class="invalided-error">' + validator.errorList[0].message + "</div>"), 
                    validator.errorList[0].element.focus());
                },
                rules: {
                    firstName: {
                        required: !0,
                        maxlength: 100
                    },
                    lastName: {
                        required: !0,
                        maxlength: 30
                    },
                    mobileNumber: {
                        required: !0,
                        maxlength: 30
                    },
                    email: {
                        required: !0,
                        email: !0,
                        maxlength: 120
                    },
                    pwd: {
                        required: !0,
                        maxlength: 30
                    },
                    checkPwd: {
                        required: !0,
                        maxlength: 30,
                        equalTo: '[name="pwd"]'
                    }
                },
                submitHandler: function(form) {
                    form.submit();
                }
            }), $("#luxury_register").on("click", function(e) {
                $(".invalided-error").remove(), e.preventDefault(), $("#extRegisterForm").submit();
            });
        },
        forgetpassword: function() {
            $("#forgottenPwdForm").validate({
                onfocusout: !1,
                invalidHandler: function(form, validator) {
                    validator.numberOfInvalids() && ($("#forgottenPwdForm").prepend('<div class="invalided-error">' + validator.errorList[0].message + "</div>"), 
                    validator.errorList[0].element.focus());
                },
                rules: {
                    email: {
                        required: !0,
                        email: !0,
                        maxlength: 120
                    }
                },
                submitHandler: function(form) {
                    form.submit();
                }
            }), $("#luxuryForgotPasswordByEmailAjax").on("click", function(e) {
                $(".invalided-error").remove(), e.preventDefault(), $("#forgottenPwdForm").submit();
            });
        },
        init: function() {
            tul.commonFunctions.login(), tul.commonFunctions.signup(), tul.commonFunctions.forgetpassword();
        }
    }, $(document).ready(function() {
        tul.commonFunctions.init();
    });
}).call(tul.commonFunctions, window.jQuery);
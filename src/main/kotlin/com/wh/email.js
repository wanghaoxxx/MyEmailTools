var $B = function(e) {
    var a = {
        msie: /msie/.test(e) && !/opera/.test(e),
        opera: /opera/.test(e),
        safari: /webkit/.test(e) && !/chrome/.test(e),
        firefox: /firefox/.test(e),
        chrome: /chrome/.test(e),
        maxthon: /maxthon/.test(e),
        sogou: /se/.test(e),
        tt: /TencentTraveler/.test(e)
    };
    var f = "",
    c;
    for (var d in a) {
        if (a[d]) {
            f = "safari" == d ? "version": d;
            break
        }
    }
    a.version = f && RegExp("(?:" + f + ")[\\/: ]([\\d.]+)").test(e) ? RegExp.$1: "0";
    a.ie = a.msie;
    c = parseInt(a.version, 10);
    a.ie6 = a.msie && c == 6;
    a.ie7 = a.msie && c == 7;
    a.ie8 = a.msie && c == 8;
    a.ie9 = a.msie && c == 9;
    return a
} (navigator.userAgent.toLowerCase());
using(["r/core"],
function(h) {
    var O = h.global,
    e = O.document,
    C = h.dom,
    s = h.event,
    y = C.query,
    H = C.$;
    var a = h.string.trim;
    var J = O.document.forms.frm_reg;
    var Q = y(".formTips"),
    f = y(".advancedPro"),
    k = y(".advancedPro_up"),
    r = y(".normalPro"),
    u = H("UnlockMe"),
    P = y(".supportBtn"),
    v = y(".supportBox"),
    F,
    G,
    T = false;
    var x = function(R) {
        var E = H("freecode");
        clearInterval(F);
        E.style.color = "#333333";
        C.removeClass(E, "grey");
        E.innerHTML = "免费获取短信验证码"
    };
    var m = function(R) {
        var E = this.getAttribute("id");
        f.style.display = (E == "super") ? "": "none";
        r.style.display = (E == "super") ? "none": "";
        u.style.display = (E == "super") ? "none": ""
    };
    var B = function(Y) {
        var W = parseInt(this.getAttribute("id").substr(1, 1));
        var X = y(".vip");
        for (var R = 0,
        E = X.length; R < E; R++) {
            C.removeClass(X[R], "cur" + (R + 1))
        }
        C.addClass(X[W], "cur" + (W + 1))
    };
    function z(W, X) {
        var R = "",
        E = {
            mail: W + "@vip.sina.com",
            ts: new Date().getTime()
        };
        if (J.freetovip) {
            E.freetovip = J.freetovip.value
        }
        h.io.json("/register/chkmail.php", {
            method: "post",
            scope: this,
            params: E,
            success: function(Y) {
                if (Y.result) {
                    R = "邮箱名可用";
                    if (X) {
                        X()
                    }
                } else {
                    switch (Y.msg) {
                    case "mailname_isnull":
                        R = "邮箱名参数为空";
                        break;
                    case "domian_err":
                        R = "邮箱所属域名不正确";
                        break;
                    case "mailname_exists":
                    case "mailname_login_exists":
                        R = "抱歉！该邮箱名已被占用，请重新选择一个";
                        break;
                    case "overlimit":
                        R = "您的查询次数过多，请一分钟后再查询";
                        break;
                    default:
                        if (Y.msg.indexOf("mailname_err") == 0) {
                            R = "该邮箱名禁止注册，请换一个再试"
                        } else {
                            R = "异步通信错误"
                        }
                        break
                    }
                }
                i(R, "email");
                if (R == "邮箱名可用") {
                    K(J.email)
                } else {
                    g(J.email)
                }
            }
        })
    }
    function N(R, E) {
        h.io.json("/register/request_upload_sms.php", {
            method: "get",
            scope: this,
            params: {
                phoneNumber:
            },
            timeout: 30000,
            success: function(X) {
                if (X.result) {
                    if (y(".hideEle")) {
                        C.replaceClass(y(".hideEle"), "hideEle", "showHideEle")
                    }
                    y("em", y(".telSelect")).innerHTML = R;
                    var W = y("i", y(".telSelect"));
                    W[0].innerHTML = X.data.uploadMessage;
                    W[1].innerHTML = X.data.uploadNumber
                } else {
                    if (X.data.name) {
                        y(".syserror_tel").innerHTML = "";
                        i(X.msg, frm[X.data.name]);
                        var Y = C.getPos(frm[X.data.name]).y;
                        e.documentElement.scrollTop = Y - 10
                    } else {
                        y(".syserror_tel").innerHTML = X.msg
                    }
                }
            }
        })
    }
    function g(E) {
        E.style.border = "1px solid #1F87FF"
    }
    function K(E) {
        E.style.border = "1px solid #DAD7D7"
    }
    function S(ad) {
        var Z = a(ad.email.value),
        X = ad.fpsw.value,
        W = ad.npsw.value;
        var ab = a(ad.phonenumber.value),
        E = ad.msgvcode.value;
        var aa = 0,
        ac = 0,
        Y = [];
        if (!ad.checkbox1.checked) {
            O.alert("如果您拒绝接受《新浪网络服务使用协议》和《新浪VIP邮箱服务条款》，将无法继续注册。");
            return false
        }
        Z = Z.replace(/@vip.sina.com$/ig, "");
        if (!c(ad, Z)) {
            aa++;
            Y.push(ad.email)
        }
        if (!M(X)) {
            aa++;
            Y.push(ad.fpsw)
        }
        if (!o(W, X)) {
            aa++;
            Y.push(ad.npsw)
        }
        if (T) {
            if (!L(ad.phonenumber_up.value, "phonenumber_up")) {
                aa++;
                Y.push(ad.phonenumber_up)
            }
        } else {
            if (ab == "") {
                i("手机号码为空", "phonenumber", "tip");
                aa++;
                Y.push(ad.phonenumber)
            } else {
                if (!L(ab, "phonenumber")) {
                    aa++;
                    Y.push(ad.phonenumber)
                }
            }
            if (E == "") {
                i("短信验证码为空", "msgvcode", "tip");
                aa++;
                Y.push(ad.msgvcode)
            }
        }
        if (aa > 0) {
            ac = C.getPos(Y[0]).y;
            e.documentElement.scrollTop = ac - 10;
            return false
        }
        y(".logintip").innerHTML = "正在验证中...";
        var R = I(ad);
        if (T) {
            delete R.msgvcode
        }
        h.io.json(ad.action, {
            method: "post",
            scope: this,
            params: R,
            success: function(ae) {
                if (ae.result) {
                    O.location = ae.data.url
                } else {
                    if (ae.data.name) {
                        y(".logintip").innerHTML = "";
                        if (ae.data.name == "email") {
                            ad[ae.data.name].parentNode.parentNode.style.display = ""
                        }
                        if (ae.data.name == "msgvcode" && ae.errno == "315") {
                            x();
                            return i(ae.msg, "msgvcode", "error")
                        }
                        if (ae.errno == "315") {
                            return i(ae.msg, "phonenumber_up", "error")
                        }
                        i(ae.msg, ae.data.name, "error");
                        var af = C.getPos(ad[ae.data.name]).y;
                        e.documentElement.scrollTop = af - 10
                    } else {
                        y(".logintip").innerHTML = ae.msg
                    }
                }
            }
        });
        return false
    }
    function I(Y) {
        var R = {};
        var X;
        for (var W = 0,
        E = Y.elements.length; W < E; W++) {
            X = Y.elements[W];
            if (X.name == "") {
                continue
            }
            if (X.disabled) {
                continue
            }
            switch (X.tagName) {
            case "FIELDSET":
                continue;
            case "INPUT":
                switch (X.type) {
                case "radio":
                    if (X.checked) {
                        R[X.name] = X.value
                    }
                    break;
                case "checkbox":
                    if (X.checked) {
                        if (!R[X.name]) {
                            R[X.name] = [X.value]
                        } else {
                            R[X.name].push(X.value)
                        }
                    }
                    break;
                case "text":
                default:
                    R[X.name] = X.value;
                    break
                }
                break;
            case "SELECT":
            case "TEXTAREA":
            default:
                R[X.name] = X.value;
                break
            }
        }
        X = null;
        return R
    }
    function d(Y) {
        var R = [];
        for (var W in Y) {
            if (typeof(Y[W]) == "string") {
                R.push(W + "=" + encodeURIComponent(Y[W]))
            } else {
                for (var X = 0,
                E = Y[W].length; X < E; X++) {
                    R.push(W + "=" + encodeURIComponent(Y[W][X]))
                }
            }
        }
        return R
    }
    function l(E) {
        if (E == "") {
            C.hide(H("sys_error"))
        } else {
            if (H(E + "_error")) {
                C.hide(H(E + "_error"))
            }
        }
    }
    function i(W, X, E) {
        if (X == "") {
            H("sys_error").innerHTML = W;
            e.documentElement.scrollTop = 0
        } else {
            var R = H(X + "_error");
            if (R) {
                R.style.display = "";
                if (W == "邮箱名可用") {
                    R.className = "success"
                } else {
                    if (E == "tip") {
                        R.className = "notice"
                    } else {
                        R.className = "error"
                    }
                }
                y("td", R).innerHTML = W
            } else {
                O.alert(W)
            }
            R = null
        }
    }
    function D(X, W) {
        var E = y(".formTips"),
        R = y("span", E);
        var Y = C.getPos(W);
        if (Y) {
            E.style.left = Y.x + 109 + "px";
            E.style.top = Y.y + 45 + "px";
            R.innerHTML = X;
            E.style.display = ""
        }
    }
    function c(W, X) {
        if (!X || X == "") {
            i("请输入邮箱名", "email", "tip");
            return false
        }
        if (X.length < 4 || X.length > 16) {
            i("邮箱名必须是4-16个字符之间（包括4、16）", "email");
            return false
        }
        if (/[A-Z]/.test(X)) {
            i("不能有大写字母", "email");
            return false
        }
        if (X.indexOf(" ") > -1) {
            i("不能有空格", "email");
            return false
        }
        if ((/^[0-9]+$/g).test(X)) {
            i("不能全是数字", "email");
            return false
        }
        if ((/^[_]+$/g).test(X)) {
            i("不能全是下划线", "email");
            return false
        }
        if (X.slice(X.length - 1) == "_") {
            i("下划线不能在结尾", "email");
            return false
        }
        if (X.slice(0, 1) == "_") {
            i("下划线不能为第一个位", "email");
            return false
        }
        for (var E = 0; E < X.length; E++) {
            var R = X.charCodeAt(E);
            if (R > 65248 || R == 12288) {
                i("不能有全角字符", "email");
                return false
            }
        }
        if (/^[\u4e00-\u9fa5]/.test(X)) {
            i("不能有中文", "email");
            return false
        }
        if ((/>|<|,|\[|\]|\{|\}|\/|=|\||\'|\\|\'|:|;|\~|\!|\@|\#|\?|\+|\-|\*|\$|\%|\^|\&|\(|\)|`/i).test(X)) {
            i("邮箱名仅允许使用小写英文、数字或下划线", "email");
            return false
        }
        if (!/^[0-9a-z_]+$/.test(X)) {
            i("限英文小写,数字,下划线.不能全是数字,下划线,下划线不在首尾", "email");
            return false
        }
        return true
    }
    function M(R, X) {
        var Y;
        if (X == "1") {
            Y = "npsw"
        } else {
            Y = "fpsw"
        }
        if (!R || R == "") {
            i("请输入密码", Y, "tip");
            return false
        }
        if (R.length < 6 || R.length > 16) {
            i("密码的长度应该在6-16个字符之间", Y);
            return false
        }
        for (var E = 0; E < R.length; E++) {
            var W = R.charCodeAt(E);
            if (W > 65248 || W == 12288) {
                i("请勿使用全角字符", Y);
                return false
            }
        }
        if (/^[\u4e00-\u9fa5]/.test(R)) {
            i("请勿使用中文", Y);
            return false
        }
        if (!/^([\w\~\!\@\#\$\%\^\&\*\(\)\+\`\-\=\[\]\\\{\}\|\;\'\:\"\,\.\/\<\>\?]{6,16})$/.test(R)) {
            i("6-16位字符（字母、数字、特殊符号），区分大小写", Y);
            return false
        }
        return true
    }
    function o(E, R) {
        if (!E || E == "") {
            i("请再次输入密码", "npsw", "tip");
            return false
        }
        if (!M(E, 1)) {
            return false
        } else {
            if (E != R) {
                i("您两次输入的密码不一致", "npsw");
                return false
            }
        }
        return true
    }
    function L(E, R) {
        if (!E || E == "") {
            i("请输入手机号", R, "tip");
            return false
        }
        if (! (/(^1\d{10}$)/).test(E)) {
            i("手机号码输入格式错", R);
            return false
        }
        return true
    }
    function V(W, R) {
        var E = a(W.idnum.value);
        if (R == "1") {
            if (E.length != 15 && E.length != 18) {
                i("请输入正确的身份证号", "idnum");
                return false
            }
            if (E.length == 15) {
                if (! (/^[0-9]+$/).test(E)) {
                    i("身份证应由15或18位数字组成，18位最后一位可为字母X", "idnum");
                    return false
                }
            } else {
                if (! (/^[0-9]{18}$|^[0-9]{17}(x|X)$/).test(E)) {
                    i("身份证应由15或18位数字组成，18位最后一位可为字母X", "idnum");
                    return false
                }
            }
        } else {
            if (E.length < 3 || E.length > 20) {
                i("证件号码应在3-20个字符之间", "idnum");
                return false
            }
            if ((/>|<|,|\[|\]|\{|\}|\?|\/|\+|=|\||\'|\\|\"|:|;|\~|\!|\@|\#|\*|\$|\%|\^|\&|\(|\)|`/i).test(E)) {
                i("请勿使用特殊字符", "idnum");
                return false
            }
        }
        return true
    }
    function t(E) {
        if (J.safemail.value == "" && J.idnum.value == "" && J.safemphone.value == "") {
            i("必填项之一", E, "tip");
            return false
        }
        return true
    }
    function A(W) {
        var R = escape(W);
        var E;
        E = R.length - (R.length - R.replace(/\%u/g, "u").length) * 4;
        R = R.replace(/\%u/g, "uu");
        E = E - (R.length - R.replace(/\%/g, "").length) * 2;
        return E
    }
    function q() {
        return /msie/.test(navigator.userAgent.toLowerCase())
    }
    function p() {
        if (q() && event.keyCode == 13) {
            S(J)
        }
    }
    s.on(H("submitBtn"), "click",
    function() {
        S(J);
        return false
    });
    s.on(J.email, "focus",
    function() {
        g(this);
        Q.style.display = "none";
        H("email_error").style.display = "none";
        D("邮箱名长度为7-16个字符(包括7、16),限用英文小写、数字、下划线.不能全是数字或下划线.下划线不能在结尾", this)
    });
    s.on(J.email, "blur",
    function() {
        var E = this.value;
        Q.style.display = "none";
        K(this);
        E = E.replace(/@vip.sina.com$/ig, "");
        if (E != "") {
            if (!c(J, E)) {} else {
                z(E)
            }
        }
    });
    s.on(J.fpsw, "focus",
    function() {
        g(this);
        Q.style.display = "";
        H("fpsw_error").style.display = "none";
        D("6-16位字符（字母、数字、特殊符号），区分大小写", this)
    });
    s.on(J.fpsw, "blur",
    function() {
        Q.style.display = "none";
        K(this);
        if (this.value != "") {
            M(this.value)
        }
    });
    s.on(J.fpsw, "keydown",
    function() {
        if (J.npsw.value != "") {
            J.npsw.value = "";
            H("npsw_error").style.display = "none"
        }
    });
    s.on(J.npsw, "focus",
    function() {
        H("npsw_error").style.display = "none";
        g(this)
    });
    s.on(J.npsw, "blur",
    function() {
        K(this);
        if (this.value != "") {
            o(this.value, J.fpsw.value)
        }
    });
    s.on(J.phonenumber, "focus",
    function() {
        g(this);
        H("phonenumber_error").style.display = "none"
    });
    s.on(J.phonenumber, "blur",
    function() {
        var E = this.value;
        K(this);
        if (E != "") {
            if (L(E, "phonenumber")) {
                J.pswqa_q.options[1].selected = true;
                J.pswqa_a.value = E.substr(5, 6)
            }
        }
    });
    s.on(J.msgvcode, "focus",
    function() {
        g(this);
        H("msgvcode_error").style.display = "none"
    });
    s.on(J.msgvcode, "blur",
    function() {
        K(this)
    });
    s.on(J.phonenumber_up, "blur",
    function() {
        l("phonenumber_up");
        var E = this.value;
        K(this);
        if (L(E, "phonenumber_up")) {
            N(E)
        }
    });
    s.on(H("upCode"), "click",
    function() {
        l("phonenumber_up");
        var E = J.phonenumber_up.value;
        J.phonenumber.value = E;
        K(J.phonenumber_up);
        if (L(E, "phonenumber_up")) {
            N(E)
        }
    });
    s.on(H("freecode"), "click",
    function() {
        if (C.hasClass(this, "grey")) {
            return
        }
        var E = {
            phonenumber: J.phonenumber.value,
            email: J.email.value + "@vip.sina.com"
        };
        var W = 60,
        R = this;
        if (E.phonenumber == "") {
            i("手机号为空", "phonenumber", "tip")
        } else {
            if (!c(J, J.email.value)) {
                i("请检查邮箱名是否正确", "phonenumber", "tip");
                return false
            } else {
                if (L(E.phonenumber, "phonenumber")) {
                    F = setInterval(function() {
                        if (W == 0) {
                            clearInterval(F);
                            R.style.color = "#333333";
                            C.removeClass(R, "grey");
                            R.innerHTML = "免费获取短信验证码"
                        } else {
                            R.style.color = "#919191";
                            C.addClass(R, "grey");
                            R.innerHTML = W + "秒后重新获取";
                            W--
                        }
                    },
                    1000);
                    h.io.json("/cgi-bin/phonecode.php", {
                        method: "post",
                        scope: this,
                        params: E,
                        success: function(X) {
                            if (!X.result) {
                                clearInterval(F);
                                if (X.code == -103) {
                                    C.hide(f);
                                    C.show(k);
                                    J.phonenumber_up.value = J.phonenumber.value;
                                    T = true;
                                    s.trigger(H("upCode"), "click");
                                    return
                                }
                                R.innerHTML = "免费获取短信验证码";
                                R.style.color = "#333333";
                                C.removeClass(R, "grey");
                                i(X.msg, "phonenumber")
                            }
                        },
                        failure: function() {
                            clearInterval(F);
                            R.innerHTML = "免费获取短信验证码";
                            R.style.color = "#333333";
                            C.removeClass(R, "grey")
                        }
                    })
                }
            }
        }
    });
    s.on(P, "mouseover",
    function(E) {
        v.style.display = "";
        setTimeout(function() {
            C.addClass(v, "aniIn")
        },
        100)
    });
    s.on(P, "mouseout",
    function(E) {
        G = setTimeout(function() {
            if (q()) {
                v.style.display = "none"
            }
            C.removeClass(v, "aniIn");
            O.clearTimeout(G)
        },
        200)
    });
    s.on(v, "mouseover",
    function(E) {
        O.clearTimeout(G);
        this.style.display = ""
    });
    s.on(v, "mouseout",
    function(W) {
        var R = W.srcElement || W.target,
        E;
        var X = W.toElement || W.relatedTarget;
        if (!C.contains(this, X)) {
            if (q()) {
                v.style.display = "none"
            }
            C.removeClass(v, "aniIn")
        }
    });
    s.on(H("p0"), "click", B);
    s.on(H("p1"), "click", B);
    s.on(H("p2"), "click", B);
    s.on(H("p3"), "click", B);
    var U = y(".closebt"),
    j = y(".openbt"),
    n = y(".tr_bg");
    var b = y(".ly_tree");
    s.on(n, "click",
    function() {
        if ($B.ie6) {
            C.css(b, "right", "0px")
        }
        if (U.parentNode.style.display == "none") {
            U.parentNode.style.display = "";
            j.parentNode.style.display = "none"
        } else {
            U.parentNode.style.display = "none";
            j.parentNode.style.display = ""
        }
    });
    var w = function(R) {
        var E;
        if ($B.ie) {
            E = e.documentElement.clientWidth
        } else {
            E = e.body.clientWidth
        }
        if (E < 948) {
            C.css(b, "right", (E - 948) + "px")
        } else {
            C.css(b, "right", "0px")
        }
    };
    if (!$B.ie6) {
        s.on(O, "resize", w)
    } (function() {
        var E = O.location.search;
        if (E && E.indexOf("r=wanwan_top") != -1) {
            b.style.display = "";
            if (!$B.ie6) {
                w()
            }
        } else {
            b.style.display = "none"
        }
    })()
});
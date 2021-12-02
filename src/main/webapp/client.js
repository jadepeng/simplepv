var bszCaller, bszTag, scriptTag, ready;

var t,
  e,
  n,
  a = !1,
  c = [];

// 修复Node同构代码的问题
if (typeof document !== 'undefined') {
  (ready = function (t) {
    return (
      a || 'interactive' === document.readyState || 'complete' === document.readyState
        ? t.call(document)
        : c.push(function () {
            return t.call(this);
          }),
      this
    );
  }),
    (e = function () {
      for (var t = 0, e = c.length; t < e; t++) c[t].apply(document);
      c = [];
    }),
    (n = function () {
      a ||
        ((a = !0),
        e.call(window),
        document.removeEventListener
          ? document.removeEventListener('DOMContentLoaded', n, !1)
          : document.attachEvent &&
            (document.detachEvent('onreadystatechange', n), window == window.top && (clearInterval(t), (t = null))));
    }),
    document.addEventListener
      ? document.addEventListener('DOMContentLoaded', n, !1)
      : document.attachEvent &&
        (document.attachEvent('onreadystatechange', function () {
          /loaded|complete/.test(document.readyState) && n();
        }),
        window == window.top &&
          (t = setInterval(function () {
            try {
              a || document.documentElement.doScroll('left');
            } catch (t) {
              return;
            }
            n();
          }, 5)));
}

bszCaller = {
  fetch: function (t, e) {
    var n = 'SimplePVCallback' + Math.floor(1099511627776 * Math.random());
    t = t.replace('=SimplePVCallback', '=' + n);
    (scriptTag = document.createElement('SCRIPT')),
      (scriptTag.type = 'text/javascript'),
      (scriptTag.defer = !0),
      (scriptTag.src = t),
      document.getElementsByTagName('HEAD')[0].appendChild(scriptTag);
    window[n] = this.evalCall(e);
  },
  evalCall: function (e) {
    return function (t) {
      ready(function () {
        try {
          e(t),
            scriptTag && scriptTag.parentElement && scriptTag.parentElement.removeChild && scriptTag.parentElement.removeChild(scriptTag);
        } catch (t) {
          console.log(t), bszTag.hides();
        }
      });
    };
  },
};

const fetch = siteUrl => {
  bszTag && bszTag.hides();
  bszCaller.fetch(`${siteUrl}/api/pv/${window.btoa(location.href)}?jsonpCallback=SimplePVCallback`, function (t) {
    bszTag.texts(t), bszTag.shows();
  });
};

bszTag = {
  bszs: ['site_pv', 'page_pv'],
  texts: function (n) {
    this.bszs.map(function (t) {
      var e = document.getElementById('busuanzi_value_' + t);
      e && (e.innerHTML = n[t]);
    });
  },
  hides: function () {
    this.bszs.map(function (t) {
      var e = document.getElementById('busuanzi_container_' + t);
      e && (e.style.display = 'none');
    });
  },
  shows: function () {
    this.bszs.map(function (t) {
      var e = document.getElementById('busuanzi_container_' + t);
      e && (e.style.display = 'inline');
    });
  },
};

if (typeof document !== 'undefined') {
  // fetch('http://localhost:8080/');
  fetch('https://pv.iflyresearch.com');
}

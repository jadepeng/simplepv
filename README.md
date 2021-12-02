# simplepv

类似不蒜子的 PV 统计器

## step0

下载编译:

```bash
git clone https://github.com/jadepeng/simplepv
cd simplepv
mvn package -DskipTests
```

部署 web 程序

```bash
    java -jar simplepv-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

输出

```
2021-12-02 20:25:49.014  INFO 35916 --- [           main] com.jadepeng.simplepv.SimplepvApp        : The following profiles are active: prod
2021-12-02 20:25:53.585  INFO 35916 --- [           main] c.j.simplepv.config.WebConfigurer        : Web application configuration, using profiles: prod
2021-12-02 20:25:53.589  INFO 35916 --- [           main] c.j.simplepv.config.WebConfigurer        : Web application fully configured
2021-12-02 20:26:02.580  INFO 35916 --- [           main] org.jboss.threads                        : JBoss Threads version 3.1.0.Final
2021-12-02 20:26:02.697  INFO 35916 --- [           main] com.jadepeng.simplepv.SimplepvApp        : Started SimplepvApp in 15.936 seconds (JVM running for 16.79)
2021-12-02 20:26:02.707  INFO 35916 --- [           main] com.jadepeng.simplepv.SimplepvApp        :
----------------------------------------------------------
	Application 'simplepv' is running! Access URLs:
	Local: 		http://localhost:58080/
	External: 	http://172.1.1.12:58080/
	Profile(s): 	[prod]
----------------------------------------------------------

```

本程序默认使用 h2 作为存储，所以不用另外安装 mysql。

## step1

引用 client.js， 也可以直接放入到网页中

```js
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
  fetch('http://localhost:8080/');
}
```

上面 fetch 的地址，填写 webserver 部署后的地址。

## step2

在需要显示 pv 的地方

```html
<span id="busuanzi_container_site_pv">本站总访问量<span id="busuanzi_value_site_pv"></span>次</span>
<span id="busuanzi_container_page_pv">本文总阅读量<span id="busuanzi_value_page_pv"></span>次</span>
```

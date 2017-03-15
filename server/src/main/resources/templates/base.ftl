<#macro page_head_meta>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
</#macro>

<#macro page_head_title>
  <title>Zally API Specification Linter</title>
</#macro>

<#macro page_head_favicon>
<link rel="icon" href="favicon.ico">
</#macro>

<#macro page_head_assets>
  <link href="https://fonts.googleapis.com/css?family=Ubuntu:300,500,300italic|Ubuntu+Mono" rel="stylesheet" type="text/css">
  <link href="https://unpkg.com/dress-code@2.0.0/dist/css/dress-code.min.css" rel="stylesheet" type="text/css">
  <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
  <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script src="//cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.6/handlebars.min.js"></script>
  <script src="https://unpkg.com/oauth2-client-js@0.0.15"></script>
  <script type="text/javascript">
      Handlebars.registerHelper('ifEq', function(a, b, opts) {
          return a == b  ? opts.fn(this) : opts.inverse(this);
      });
      Handlebars.registerHelper('ifLength', function(a, opts){
              return !!a && a.length ? opts.fn(this) : opts.inverse(this);
      });
      Handlebars.registerHelper('ifContent', function(a, opts){
              return a && !!a.trim() ? opts.fn(this) : opts.inverse(this);
      })
  </script>

</#macro>

<#macro page_head_assets_extra></#macro>

<#macro page_body></#macro>

<#macro display_page>
<!doctype html>
<html>
<head>
  <@page_head_meta/>
  <@page_head_title/>
  <@page_head_favicon/>
  <@page_head_assets/>
  <@page_head_assets_extra/>
</head>
<body>
<div class="dc-page">
    <div class="dc-container">
      <@page_body/>
    </div>
</div>
<div class="loader hidden" style="position: fixed; top: 0px; left: 0px; width: 100vw; height: 100vh; display: none;">
    <div class="mask" style="height: 100%; background: #fff; opacity: .5;">
    </div>
    <div class="inner" style="position: absolute; text-align: center; width: 100%; height: 100%; top: 33%;">
        <?xml version="1.0" encoding="utf-8"?><svg width='60px' height='60px' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid" class="uil-spin"><rect x="0" y="0" width="100" height="100" fill="none" class="bk"></rect><g transform="translate(50 50)"><g transform="rotate(0) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g><g transform="rotate(45) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0.12s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0.12s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g><g transform="rotate(90) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0.25s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0.25s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g><g transform="rotate(135) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0.37s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0.37s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g><g transform="rotate(180) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0.5s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0.5s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g><g transform="rotate(225) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0.62s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0.62s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g><g transform="rotate(270) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0.75s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0.75s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g><g transform="rotate(315) translate(34 0)"><circle cx="0" cy="0" r="8" fill="#000"><animate attributeName="opacity" from="1" to="0.1" begin="0.87s" dur="1s" repeatCount="indefinite"></animate><animateTransform attributeName="transform" type="scale" from="1.5" to="1" begin="0.87s" dur="1s" repeatCount="indefinite"></animateTransform></circle></g></g></svg>
    </div>
</div>
</body>
</html>
</#macro>

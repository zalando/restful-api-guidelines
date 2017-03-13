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
</#macro>

<#macro page_body></#macro>

<#macro display_page>
<!doctype html>
<html>
<head>
  <@page_head_meta/>
  <@page_head_title/>
  <@page_head_favicon/>
  <@page_head_assets/>
</head>
<body>
<div class="dc-page">
    <div class="dc-container">
      <@page_body/>
    </div>
</div>
</body>
</html>
</#macro>

<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Zally linter report</title>

    <link href="https://fonts.googleapis.com/css?family=Ubuntu:300,500,300italic|Ubuntu+Mono" rel="stylesheet" type="text/css">

    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

    <link rel="icon" href="favicon.ico">
    <style media="screen" type="text/css">
        <#include "style.css">
    </style>
</head>

<body>
<div class="dc-page">
    <div class="dc-container">
        <div class="dc-card">
            <ul class="dc-list">
                <#list violations as v>
                <li>${v.title} ${v.description} ${v.violationType}</li>
                </#list>
            </ul>
        </div>
    </div>
</div>
</body>
</html>

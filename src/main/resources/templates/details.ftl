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
            <h2 class="dc-h2">Zally report</h2>
            <ul class="dc-list">
                <#list violations as v>
                <li style="margin-bottom: 35px; border-bottom: 1px solid #ccc;">
                    <h4 class="dc-h4">
                        <#if v.violationType == "MUST">
                            <span class="dc-status dc-status--error"></span>
                        <#elseif v.violationType == "SHOULD">
                            <span class="dc-status dc-status--new"></span>
                        <#else>
                            <span class="dc-status dc-status--inactive"></span>
                        </#if>
                        ${v.violationType} &dash; ${v.title}
                    </h4>
                    <p>
                        ${v.description}
                    </p>

                    <#if v.path.isPresent()>
                        <p>
                            Path: <strong>${v.path.get()}</strong>
                        </p>
                    </#if>

                    <#if v.ruleLink??>
                        <p>
                        Rule: <a href="${v.ruleLink}" class="dc-link">${v.ruleLink}</a>
                        </p>
                    </#if>
                </li>
                </#list>
            </ul>
        </div>
    </div>
</div>
</body>
</html>

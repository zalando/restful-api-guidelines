<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Try Zally</title>

    <link href="https://fonts.googleapis.com/css?family=Ubuntu:300,500,300italic|Ubuntu+Mono" rel="stylesheet" type="text/css">

    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

    <link rel="icon" href="favicon.ico">
    <style media="screen" type="text/css">
        <#include "style.css">
    </style>
    <script type="text/javascript">
        function submit() {
//            https://raw.github.bus.zalan.do/ArtMC/partner-service-adapter/master/src/main/resources/api-swagger.yaml?token=AAAG7MtF1YkfDq3iLNLtMKBlZWyEba98ks5YZOkZwA%3D%3D
            var fullPath = $('#pathInput').text()
            window.location = '/details?id=' + atob(JSON.stringify({repo_url: }))
        }

    </script>
</head>

<body>
<div class="dc-page">
    <div class="dc-container">
        <div class="dc-card">
            <form action="try_back">
                Enter full path to your swagger file from repo: (for Example https://raw.github.bus.zalan.do/ArtMC/partner-service-adapter/master/src/main/resources/api-swagger.yaml?token=AAAG7MtF1YkfDq3iLNLtMKBlZWyEba98ks5YZOkZwA%3D%3D)<br>
                <input type="text" name="path" value="Mickey"><br>
                <input type="submit" value="Submit">
            </form>
           <input type="text" id="pathInput">
            <input type="button" onclick="submit()">
        </div>
    </div>
</div>
</body>
</html>

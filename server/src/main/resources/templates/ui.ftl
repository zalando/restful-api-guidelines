<#include "base.ftl">

<#macro page_head_assets_extra>
<script>
/*<![CDATA[*/
window.__INITIAL_STATE__ = {
    oauth: {
        enabled: ${oauth.enabled?c},
        authorization_url: "${oauth.authorizationUrl}",
        client_id: "${oauth.clientId}",
        redirect_uri: "${oauth.redirectUri}",
        scopes: "${oauth.scopes}",
        tokeninfo_url: "${oauth.tokeninfoUrl}"
    }
};
/*]]>*/
</script>
<script><#include "ui.js"></script>
</#macro>


<#macro page_body>
<div class="dc-card">
    <h2 class="dc-h2">Zally report</h2>
    <form>
        <label for="url" class="dc-label">Enter full path to your swagger file from repo</label>
        <input class="dc-input dc-input--block" id="url" type="text" name="path"
               placeholder="e.g https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/json/petstore.json">
        <button type="submit" class="dc-btn dc-btn--primary">Submit</button>
    </form>
    <ul class="dc-list">
    </ul>
</div>

<script id="tmp-violations" type="text/x-handlebars-template">
   {{#each violations}}
        <li style="margin-bottom: 32px; padding-bottom: 32px; border-bottom: 1px solid #ccc;">
            <h4 class="dc-h4">
              {{#ifEq "MUST" violationType}}
                  <span class="dc-status dc-status--error"></span>
              {{else ifEq "SHOULD" violationType}}
                  <span class="dc-status dc-status--new"></span>
              {{else}}
                  <span class="dc-status dc-status--inactive"></span>
              {{/ifEq}}
              {{violationType}} &dash; {{title}}
            </h4>

            <p>{{description}}</p>

            {{#ifContent ruleLink}}
              <p>Rule: <a href="{{ruleLink}}" class="dc-link">{{ruleLink}}</a></p>
            {{/ifContent}}

            {{#ifLength paths}}
              <p>Paths:</p>
              <ul>
                {{#each paths}}
                  <li>{{this}}</li>
                {{/each}}
              </ul>
            {{/ifLength}}
          </li>
   {{/each}}
</script>
<script type="text/javascript">
    var dummyData = {violations:[{violationType: 'MUST', description: "Description", ruleLink: "rulelink-", paths: ["patha", "pathsb"]},{violationType: 'MUST', description: "Description", ruleLink: "rulelink-", paths: ["patha", "pathsb"]}]};
    var template = $('#tmp-violations').html();
    var templateScript = Handlebars.compile(template);
    var htmlOutput = templateScript(dummyData);
    $('.dc-list').html(htmlOutput);
</script>

</#macro>


<@display_page/>

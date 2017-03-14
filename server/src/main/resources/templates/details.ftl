<#include "base.ftl">


<#macro page_body>
<div class="dc-card">
  <h2 class="dc-h2">Zally report</h2>
  <ul class="dc-list">
    <#list violations as v>
      <li style="margin-bottom: 32px; padding-bottom: 32px; border-bottom: 1px solid #ccc;">
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

        <p>${v.description}</p>

        <#if v.ruleLink?? && v.ruleLink?has_content>
          <p>Rule: <a href="${v.ruleLink}" class="dc-link">${v.ruleLink}</a></p>
        </#if>

        <#if v.paths?? && v.paths?size != 0>
          <p>Paths:</p>
          <ul>
            <#list v.paths as p>
              <li>${p}</li>
            </#list>
          </ul>
        </#if>

      </li>
    </#list>
  </ul>
</div>
</#macro>


<@display_page/>

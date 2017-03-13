<#include "base.ftl">

<#macro page_body>
<div class="dc-card">
  <form action="results">
    <label for="url" class="dc-label">Enter full path to your swagger file from repo</label>
    <input class="dc-input dc-input--block" id="url" type="text" name="path"
           placeholder="e.g https://raw.github.bus.zalan.do/ArtMC/partner-service-adapter/master/src/main/resources/api-swagger.yaml?token=AAAG7MtF1YkfDq3iLNLtMKBlZWyEba98ks5YZOkZwA%3D%3D">
    <button type="submit" class="dc-btn dc-btn--primary">Submit</button>
  </form>
</div>
</#macro>

<@display_page/>

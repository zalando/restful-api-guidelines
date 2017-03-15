(function (INITIAL_STATE) {

  /**
   * @param {Object} oauthConfiguration
   */
  function oauthFirewall(oauthConfiguration) {
    if(!oauthConfiguration || !oauthConfiguration.enabled) { return; }

    function requestToken(oauthConfiguration, OauthClient, OAuthProvider) {
      var request = new OauthClient.Request({
        client_id: oauthConfiguration.client_id,
        redirect_uri: oauthConfiguration.redirect_uri,
        scopes: oauthConfiguration.scopes
      });
      OAuthProvider.remember(request);
      window.location.href = OAuthProvider.requestToken(request);
    }

    var OauthClient = window['oauth2-client-js'];

    var OAuthProvider = new OauthClient.Provider({
      id: 'zally',
      authorization_url: oauthConfiguration.authorization_url
    });

    // do we have a response from auth server?
    // check if we can parse the url fragment
    if (window.location.hash.length) {
      var response;
      try {
        response = OAuthProvider.parse(window.location.hash); // https://localhost:8443/ui#access_token=blah_blah
      } catch(err) {
        console.error(err);
        if (response instanceof Error) {
          return console.error(response);
        }
      }
    }

    if (!OAuthProvider.hasAccessToken()) {
      requestToken(oauthConfiguration, OauthClient, OAuthProvider);
    }

    // check validity of token
    $.ajax({
      url: oauthConfiguration.tokeninfo_url,
      type: 'POST',
      dataType: 'json',
      headers: {
        Authorization: 'Bearer ' + OAuthProvider.getAccessToken()
      },
      error: function() {
        console.error(arguments);
        //requestToken(state, OauthClient, OAuthProvider); // get new token if tokeninfo returns error
      }
    });
  }

  //
  // VOID
  //
  oauthFirewall(INITIAL_STATE.oauth);

  var violationsUrl = 'http://localhost:8080/api-violations';
  $(document).on('submit', '#api-violations-form', function(event){
    event.preventDefault();
    $('.loader').show();
    var path = $('#url').val();
    var data = {
        api_definition: path
    };

    $.ajax({
        url: violationsUrl,
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        headers: {
        },
        data: JSON.stringify(data),
        success: function(data){
            var dummyData = {violations:[{violationType: 'MUST', description: "Description", ruleLink: "rulelink-", paths: ["patha", "pathsb"]},{violationType: 'MUST', description: "Description", ruleLink: "rulelink-", paths: ["patha", "pathsb"]}]};
            var template = $('#tmp-violations').html();
            var templateScript = Handlebars.compile(template);
            var htmlOutput = templateScript(data);
            $('.dc-list').html(htmlOutput);

        },
        error: function(error){
            console.log(arguments);

        },
        complete: function(){
            $('.loader').hide();
        }
    })

  })

})(window.__INITIAL_STATE__ || {});

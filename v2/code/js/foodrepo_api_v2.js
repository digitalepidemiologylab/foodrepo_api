/*

This is just a draft of what a JavaScript consumption of
FoodRepo's API could be

*/

let foodRepoApi = {
  REVISION: "ALPHA",
  IMAGES: "images",
  NUTRIENTS: "nutrients"
};

(function(global) {
  /*
      HELPERS
  */

  // Gets a value or a list of values and a name, and returns a string in the form
  // name=value1&name=value2=name=value3
  function array_to_query(values, name) {
    if (typeof values === "undefined" || values === null || typeof name !== "string") {
      return null;
    }
    let includes;
    if (!Array.isArray(values)) {
      includes = [values];
    }
    return values.map(function(value) {
      return name + "=" + value;
    }).join("&");
  }

  // AJAX helper
  function reach_url(method, url, headers, data, callback) {
    let xhttp = new XMLHttpRequest;
    xhttp.onreadystatechange = function() {
      if (this.readyState === 4) {
        if (typeof callback === "function") {
          callback(this);
        }
      }
    };

    // Open connection
    xhttp.open(method, url, true);

    // Prepare POST data, if any
    let isPostWithData = method === 'POST' && data;
    if (isPostWithData) {
      xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      if (typeof data !== 'string') data = JSON.stringify(data);
    }

    // Put headers
    if (Array.isArray(headers)) {
      for (let i = 0, n = headers.length; i < n; i++) {
        let header = headers[i];
        if (Array.isArray(header) && header.length === 2) {
          xhttp.setRequestHeader(header[0], header[1]);
        }
      }
    }

    // Send request
    xhttp.send(isPostWithData ? data : null);
  }

  // Make a call to the endpoint and handle the response
  function requestURL(method, endpoint, kind, query, post_data, callback) {
    if (!callback) throw new Error('Callback is needed');
    let url = endpoint.host + "/api/v" + endpoint.version + "/" + kind + (query ? "?" + query.join("&") : "");

    // Authorization token is sent in the request header
    // Example: Authorization: Token token="582736451287365"
    let headers = [
      ["Authorization", 'Token token="' + endpoint.api_key + '"']
    ];

    function handler(ajax) {
      let me = this;
      let response;
      try {
        response = JSON.parse(ajax.responseText);
        function create_call(name) {
          return function() {
            reach_url(method, unescape(response.links[name]),  headers, post_data, me.bind(me));
          };
        }
        response.next  = create_call('next');
        response.prev  = create_call('prev');
        response.first = create_call('first');
        response.last  = create_call('last');
      } catch (error) {
        response = ajax.responseText;
      }
      callback(response);
    }
    reach_url(method, url, headers, post_data, handler.bind(handler));
  }

  function requestPostURL(endpoint, kind, post_data, callback) {
    requestURL('POST', endpoint, kind, null, post_data, callback);
  }

  function requestProductURL(endpoint, id, includes, page_number, page_size, callback) {
    let query = [];
    includes = array_to_query(includes, "include");
    if (includes)    query.push(includes);
    if (page_number) query.push("page[number]=" + page_number);
    if (page_size)   query.push("page[size]=" + page_size);

    let kind = 'products';
    if (id) kind += "/";

    requestURL('GET', endpoint, kind, query, null, callback);
  }

  function requestSearchURL(endpoint, terms, callback) {
    requestPostURL(endpoint, "products/_search", terms, callback);
  }

  /*
      FoodRepo API per se
  */
  let Endpoint = function(api_key, host, version) {
    if (!api_key) {
      console.error("Food Repo endpoints need an API key");
      return null;
    }
    //this.host = host || "https://foorepo-staging.herokuapp.com";
    this.host = host || "https://www.foodrepo.org";
    this.version = version || 2;
    this.api_key = api_key;
  };

  Endpoint.prototype.product = function(id, includes, callback) {
    requestProductURL(this, id, includes, null, null, function(response) {
      callback(response);
    });
  };

  Endpoint.prototype.products = function(page_number, page_size, callback) {
    requestProductURL(this, null, null, page_number, page_size, function(response) {
      callback(response);
    });
  };

  Endpoint.prototype.products_search= function(terms, callback) {
    let data = {
      _source: true,
      query: {
        constant_score : {
          filter : {
            terms : terms
          }
        }
      }
    };

    requestSearchURL(this, data, function(response) {
      callback(response);
    });
  };


  global.Endpoint = Endpoint;
})(foodRepoApi);


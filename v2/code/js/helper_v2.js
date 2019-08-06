// Helper function to get query parameters in url, nothing to do with Food Repo's API
let urlParams = '';
let list_element = document.getElementById('list'),
  search_element = document.getElementById('search'),
  detail_element = document.getElementById('detail'),
  count_element = document.getElementById('count'),
  time_element = document.getElementById('time'),
  error_element = document.getElementById('error'),
  api_key_element = document.getElementById('api_key'),
  results_element = document.getElementById('results'),
  count,
  request_timeout = null,
  product_batch = 100,
  max_products = 1000,
  endpoint = null,
  api_key,
  host = urlParams['host'];

(window.onpopstate = function () {
  let match,
    pl = /\+/g,  // Regex for replacing addition symbol with a space
    search = /([^&=]+)=?([^&]*)/g,
    decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
    query = window.location.search.substring(1);

  urlParams = {};
  while (match = search.exec(query)) {
    urlParams[decode(match[1])] = decode(match[2]);
  }

  function autoAPIKey() {
    if (urlParams['apikey']) {
      api_key_element.value = urlParams['apikey'];
      start();
    }
  }
  setTimeout(autoAPIKey, 100);
})();

// When the API key changes, we clear everything
function reset() {
  results_element.style.display = 'block';
  search_element.innerHTML = '';
  detail_element.innerHTML = '';
  time_element.innerHTML = '';
  count_element.innerHTML = '';
  list_element.innerHTML = '';
  error_element.innerHTML = '';
  count = 0;

  // Cancel previous request timeout, if any
  if (request_timeout) clearTimeout(request_timeout);
}

function show_products(data) {
  count += data.length;
  count_element.innerHTML = 'Count: ' + count;
  let descriptions = '';
  for (let i = 0, n = data.length; i < n; i++) {
    let product = data[i];
    let product_name = product.attributes.name ? product.attributes.name : "";
    descriptions += 'id: ' + product.id + ", barcode: " + product.attributes.barcode + ', name: "' + product_name + '"<br>';
  }
  list_element.innerHTML += descriptions;
}

function process_product_data(data, current_endpoint) {
  // If we're receiving our first batch...
  if (count === 0 && data.length > 1) {
    let product0 = data[0], product1 = data[1];

    // Request details for first product
    endpoint.product(product0.id, null, function(response) {
      if (endpoint !== current_endpoint) return;

      detail_element.innerHTML = JSON.stringify(response, null, 4);
    });

    // Use product search API for first 2 products
    console.log(product1)
    let search_terms = {barcode : [product0.attributes.barcode, product1.attributes.barcode]};
    endpoint.products_search(search_terms, function(response) {
      if (endpoint !== current_endpoint) return;

      search_element.innerHTML = JSON.stringify(response, null, 4);
    });
  }

  show_products(data);
}

function start() {
  api_key = api_key_element.value;
  if (!endpoint || api_key !== endpoint.api_key) {
    reset();

    // Create new endpoint, with provided API key
    endpoint = new foodRepoApi.Endpoint(api_key, host);

    // Store endpoint to make sure we won't show results once
    // the endpoint has been changed (because of a new API key, for instance)
    let current_endpoint = endpoint;

    // Get a list of all products, by batches
    let last_call = new Date();
    endpoint.products(1, product_batch, function(response) {
      if (endpoint !== current_endpoint) return;

      let data = response.data;
      if (data) {
        // Show time it took to get the batch
        let time = Math.round((new Date() - last_call) / 10) / 100;
        time_element.innerHTML = 'Last ' + data.length + ' products got in ' + time + 's';

        // Do whatever we want with the data
        process_product_data(data, current_endpoint);

        // If we don't have enough products,
        // request next batch
        if (count < max_products) {
          last_call = new Date();
          request_timeout = setTimeout(response.next, 1);
        }
      } else {
        error_element.innerHTML = response;
      }
    });
  }
}

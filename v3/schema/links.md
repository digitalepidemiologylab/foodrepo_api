# Links Object

Inspired by [JSON-API's `links`](http://jsonapi.org/format/#document-links), object, this allows the API consumer to know more about the current list of objects requested.

### Context

Using the `/products` endpoint, the response object will contain a `links` object.

### Schema

* `first`
  * Type: `string` (optional)
  * Description: URL to the first page of the current request.
* `prev`
  * Type: `string` (optional)
  * Description: URL to the previous page of the current request.
* `self`
  * Type: `string` (optional)
  * Description: URL to the current page of current request.
* `next`
  * Type: `string` (optional)
  * Description: URL to the next page of the current request. <b>Use the presence of this URL to know whether you have more pages to request when downloading pages sequentially from our API.</b>
* `last`
  * Type: `string` (optional)
  * Description: URL to the final page of the current request.

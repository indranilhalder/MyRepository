Node Warmer
===========

What does it do?
----------------

This is a basic node warming service that primes the caches and loads some
pages to ensure the hybris server is in a state that it can accept a high
volume of web traffic immediately. While the warming process is occurring we
return a 501 error so that requests are not being queued. If we don't return
errors to prevent queuing then when the server is ready it could be immediately
under too much load and could potentially crash or cause request timeouts.

* loads all ComposedTypes into the cache
* loads all Enumeration Types into the cache
* loads ProductData objects into the DTO Cache (and outputs estimated memory consumption)
* makes an HTTP request to the home page and a category page (although configurable to load any list of pages)
* front end servlet filter that returns a 503 until the server is "warmed up"

More info : https://wiki.hybris.com/display/ps/Node+Warmer

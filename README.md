# twitter-labs-reactive
## Spring WebFlux Implementation of Twitter Labs Streaming APIs

* `twitter-labs-reactive-core` - Where the majority of the code is located. This includes the Twitter API models, along with the client logic to invoke the APIs.
* `twitter-labs-reactive-autoconfigure` - Auto configuration and properties to bootstrap the client.
* `twitter-labs-reactive-app` - A simple Spring Boot application that utilises the above two libraries to start the stream and log the tweets to the console.

Since the time this project was created, the streaming API's have been deprecated and moved to [Twitter API v2](https://developer.twitter.com/en/docs/twitter-api/early-access) so this may no longer work as expected.

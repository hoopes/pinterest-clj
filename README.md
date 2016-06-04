# pinterest-clj

A Clojure library for interacting with the pinterest API.

It's not finished yet - there are still API calls that are not implemented. If you are interested in adding a function or two if you need it, it should be simple to add them - look at the existing code, and copy one of them.

The pinterest API explorer is awfully helpful: https://developers.pinterest.com/tools/api-explorer/

## Leiningen Coordinates

[![Clojars Project](https://clojars.org/hoopes/pinterest-clj/latest-version.svg)](http://clojars.org/hoopes/pinterest-clj)

## Usage

1) Get an API key (easy to get a new one for your account at the API explorer)
2) Use the API key in calls against the API

```clj
(require '[pinterest-clj.core :as pinterest])
```

Get information about the user the token is for!

```clj
(pinterest/me auth-token)
```

Get information about a particular board id.

```clj
(pinterest/board auth-token board-id)
```

## License

Copyright © 2016 Matthew Hoopes

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

(ns pinterest-clj.core
  (:require [clj-http.client :as http]
            [clojure.string :as s]
            [clojure.pprint :as pp]
            [clojure.walk :as walk]
            [wharf.core :as w]))

; Feel free to add more functionality as needed:
; See: https://developers.pinterest.com/tools/api-explorer/

(def base-url
  "https://api.pinterest.com/v1")

(defn- keyword-transform [k]
  "There really must be a better way to do this. Take a
  string or keyword, convert underscores to hyphens, and
  make sure to return a keyword"
  (-> k
      (w/underscore->hyphen)
      (s/lower-case)
      (s/replace-first ":" "") ; gah!
      (keyword)))

; ...this could be middleware, no?
(defn- process [resp]
  (let [json (get-in resp [:body :data])]
    (w/transform-keys keyword-transform json)))

(defn- has-value [tuple]
  "Make sure the value of this key/value tuple is not empty.
  Need to handle both strings and sequences here"
  (not (empty? (second tuple))))

(defn- filter-empty [params]
  "Take a *map*, and filter the empty values out"
  (let [filtered (filter has-value params)]
    (into {} filtered)))

(defn- gather-args [args]
  "
  Convert a list of keywords to a comma-joined string,
  replacing hyphens with underscores for the proper
  param name in the URL.
  "
  (when args
    (let [under (map w/hyphen->underscore args)
          strings (map #(s/replace-first % ":" "") under)]
      (s/join "," strings))))

(defn- get-request [url query-params]

  (let [query-params (filter-empty query-params)
        req-opts {:as :json
                  ;:debug true
                  :query-params query-params}
        resp (http/get url req-opts)]
    (process resp)))

(defn me
  "
  https://developers.pinterest.com/tools/api-explorer/

  See 'me', and the associated fields we can take.
  "
  ([token]
    (me token [:id
               :first_name
               :last_name
               :url
               :account_type
               :username
               :bio
               :counts
               :created_at
               :image]))

  ([token fields]

    (let [url (str base-url "/me")
          params {:access_token token
                  :fields (gather-args fields)}]
      (get-request url params))))

(defn my-boards
  ([token]
    (my-boards token [:id
                      :name
                      :url
                      :privacy
                      :counts
                      :reason
                      :created_at
                      :creator
                      :description
                      :image]))

  ([token fields]

    (let [url (str base-url "/me/boards")
          params {:access_token token
                  :fields (gather-args fields)}]
      (get-request url params))))

(defn board
  ([token board-id]
    (board token board-id [:id
                           :name
                           :url
                           :counts
                           :privacy
                           :reason
                           :created_at
                           :creator
                           :description
                           :image]))

  ([token board-id fields]

    (let [url (str base-url "/boards/" board-id)
          params {:access_token token
                  :fields (gather-args fields)}]
      (get-request url params))))

(defn board-pins
  ([token board-id]
    (board-pins token board-id [:id
                                :link
                                :note
                                :url
                                :attribution
                                :board
                                :color
                                :counts
                                :created_at
                                :creator
                                :original_link
                                :metadata
                                :image]))

  ([token board-id fields]

    (let [url (str base-url "/boards/" board-id "/pins")
          params {:access_token token
                  :fields (gather-args fields)}]
      (get-request url params))))

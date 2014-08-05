(ns guestbook.handler
  (:use compojure.core
        ring.middleware.resource
        ring.middleware.file-info
        hiccup.middleware
        guestbook.routes.home)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]            
            [guestbook.models.db :as db]
            [guestbook.routes.auth :refer [auth-routes]]))

(defn init []
  (println "guestbook is starting")
  (if-not (.exists (java.io.File. "./db.sq3")) ;; if the dbfile doesn't exists
    (db/create-guestbook-table))) ;; create it (otherwise use it)

(defn destroy []
  (println "guestbook is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes auth-routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))



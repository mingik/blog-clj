(ns guestbook.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to guestbook"]
     (include-css "/css/screen.css")]
    [:body body]))

(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer
             [form-to label text-field password-field submit-button]]
            [noir.response :refer [redirect]]))

(defn control [field name text]
  (list (label name text)
        (field name)
        [:br]))

(defn registration-page []
  (layout/common
   (form-to [:post "/register"]
            (control text-field :id "screen name")
            (control password-field :pass "Password")
            (control password-field :pass1 "Retype Password")
            (submit-button "Create Account"))))

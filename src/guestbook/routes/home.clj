(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer :all] ;; library for layout
            [guestbook.models.db :as db])) ;; to talk with db

;; Helper functions:
;1) Formate the time/date
(defn format-time [timestamp]
  (-> "dd/MM/yyyy"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))


;2) renders an HTML list containing the existing comments (taken from db)
(defn show-guests []
  [:ul.guests ; unordered list html tag
   (for [{:keys [message name timestamp]} (db/read-guests)] ; iterate through each entry from query
     [:li ; construct list element html tag
      [:blockqute message]
      [:p "-" [:cite name]]
      [:time (format-time timestamp)]])])

;; function that handles GET / requests
;; in a nutshell, it renders the page by placing html tags and text on it
;; it also calls other function that return data from databases etc
(defn home [& [name message error]] ; render the webpage function
  (layout/common
   [:h1 "Guestbook"]
   [:p "Welcome to my guestbook"]
   [:p error]

   ; here we call our show-guests function
   ; to generate the list of existing comments
   (show-guests)

   [:hr]

   ; here we create a form with text fields called "name" and "message"
   ; these will be sent when the form posts to the server as keywords of
   ; the same name
   (form-to [:post "/"]
            [:p "Name:"]
            (text-field "name" name)
            [:p "Message:"]
            (text-area {:rows 10 :cols 40} "message" message)
            [:br]
            (submit-button "comment"))))

;; function to evaluate the form request:
(defn save-message [name message]
  (cond
   (empty? name)
   (home name message "Some dummy forgot to leave a name")

   (empty? message)
   (home name message "Dont' you have something to say?")

   :else
   (do
     (db/save-message name message) ; save it to db
     (home)))) ; call render the webpage function

;; Define routes: for each route request, call corresponding function:
(defroutes home-routes
  (GET "/" [] (home)) 
  (POST "/" [name message] (save-message name message)))

(defroutes auth-routes
  (GET "/register" [_] (registration-page))
  (POST "/register" [id pass pass1]
        (if (= pass pass1)
          (redirect "/")
          (registration-page))))



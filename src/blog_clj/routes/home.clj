(ns blog-clj.routes.home
  (:require [compojure.core :refer :all]
            [blog-clj.views.layout :as layout]
            [hiccup.form :refer :all]
            [blog-clj.models.db :as db]))

(defn header []
  (layout/common
   [:p [:a {:href "/newpost"} "New Post"]]))

(defn format-time [timestamp]
  (-> "dd/MM/yyyy hh:mm:ss"
      (java.text.SimpleDateFormat.)
      (.format timestamp)))

;; TODO: Implement the function that displays only one post, based on some id (name)
;(defn show-post [id] )

(defn show-posts []
  [:div.posts
   (for [{:keys [name post tags timestamp]}
         (db/read-posts)]
     [:div
      [:h2 name]
      [:p [:h5 "Posted on " (format-time timestamp)]]
      [:hr]
      [:p post]
      [:p [:h4 "Filed under: " tags]]])])

(defn create-post [& [name post tags]]
  (layout/common
   (header)
   (form-to [:post "/"]
    [:p [:b "Title:"]]
    (text-field "name" name)
    [:p [:b "Blog Post"]]
    (text-area {:rows 10 :cols 40} "post" post)
    [:br]
    [:p [:b "Tags"]]
    [:p "Comma separated, please"]
    (text-field "tags" tags)
    [:br]
    (submit-button "Submit"))))


(defn home []
  (layout/common
   (header)
   [:h1 "My Blog"]
   (show-posts)
   [:hr]
   ))

(defn save-post [name post tags]
  ;; TODO: add validation on empty fields
  (do
    (db/save-post name post tags)
    (home)))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/newpost" [] (create-post))
  ;; TODO: (GET "/:id" [id] (show-post id)
  (POST "/" [name post tags] (save-post name post tags)))

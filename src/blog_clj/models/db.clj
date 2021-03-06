(ns blog-clj.models.db
  (:require [clojure.java.jdbc :as sql])
  (:import java.sql.DriverManager))

;; TODO: Modify to MongoDB usage (save tags as array and create index on them so you could search on tags)

(def db {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite",
         :subname "db.sq3"})

(defn create-blog-table []
  (sql/with-connection
    db
    (sql/create-table
     :blog
     [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
     [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
     [:name "TEXT"]
     [:post "TEXT"]
     [:tags "TEXT"])

    (sql/do-commands "CREATE INDEX timestamp_index ON blog (timestamp)")))

(defn read-posts []
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM blog ORDER BY timestamp DESC"]
      (doall res))))

(defn save-post [name post tags]
  (sql/with-connection
    db
    (sql/insert-values
     :blog
     [:name :post :tags :timestamp]
     [name post tags (new java.util.Date)])))

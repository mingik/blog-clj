(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql]) ; for Clojure namespaces
  (:import java.sql.DriverManager)) ; for Java classes

;; Definition for our database connection:
;; it's simply a map containing the class for
;; the JDBC driver, the protocol, the name of dbfile
(def db {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite",
         :subname "db.sq3"})

;; function to create the table for storing the guest messages:
(defn create-guestbook-table []
  (sql/with-connection
   db
   (sql/create-table 
    :guestbook
    [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
    [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
    [:name "TEXT"]
    [:message "TEXT"])
   (sql/do-commands "CREATE INDEX timestamp_index ON guestbook (timestamp)")))

;; function to reade the guests data (messages that were already stored earlier in db):
(defn read-guests []
  (sql/with-connection
    db
    (sql/with-query-results res
      ["SELECT * FROM guestbook ORDER BY timestamp DESC"]
      (doall res)))) ; completely evalutate res (otherwise connection will be closed

;; function to save new messages to our guestbook
(defn save-message [name message]
  (sql/with-connection
    db
    (sql/insert-values
     :guestbook
     [:name :message :timestamp]
     [name message (new java.util.Date)])))







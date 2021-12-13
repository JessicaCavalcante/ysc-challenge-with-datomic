(ns credit-card-with-datomic.main
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [credit-card-with-datomic.db :as db]
            [credit-card-with-datomic.model :as model]
            [java-time :as j]
            [schema.core :as s]))

(s/set-fn-validation! true)

(db/delete-db!)
(def conn (db/connect!))
(db/create-schema! conn)

(def cliente (model/new-client "John doe", (model/uuid)))
(pprint @(d/transact conn [cliente]))

(def compra1 (model/new-purchase (j/sql-date (j/local-date 2021 1 10)), 30M, "Ifood", "Food", (model/uuid)))
(pprint @(d/transact conn [compra1]))

(def cartao (model/new-card "1111.1111", "592", 50M, (j/sql-date (j/local-date 2021 9 28)), cliente, compra1, (model/uuid)))
(pprint @(d/transact conn [cartao]))


(pprint (db/all-cards (d/db conn)))

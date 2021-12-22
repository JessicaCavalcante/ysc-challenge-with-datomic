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

(def client (model/new-client "John doe", (model/uuid)))
(pprint @(d/transact conn [client]))

(def purchase1 (model/new-purchase (j/sql-date (j/local-date 2021 1 10)), 30M, "Ifood", "Food", (model/uuid)))
(pprint @(d/transact conn [purchase1]))

(def purchase2 (model/new-purchase (j/sql-date (j/local-date 2021 1 10)), 20M, "Ifood", "Food", (model/uuid)))
(pprint @(d/transact conn [purchase2]))

(def card (model/new-card "1111.1111", "592", 50M, (j/sql-date (j/local-date 2021 9 28)), client, (model/uuid)))
(pprint @(d/transact conn [card]))

(db/add-purchases! conn [purchase1, purchase2] card)

(pprint (db/all-cards (d/db conn)))

(pprint (db/purchase-by-card (d/db conn)))
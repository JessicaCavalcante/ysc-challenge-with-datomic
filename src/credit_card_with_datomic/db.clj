(ns credit-card-with-datomic.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [credit-card-with-datomic.model :as model]
            [schema.core :as s]
            ))

(def db-uri "datomic:dev://localhost:4334/credit-card")

(defn connect! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn delete-db! []
  (d/delete-database db-uri))

(def schema [{
              ;CARDS
              :db/ident       :card/number
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :card/cvv
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :card/limit
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one}
             {:db/ident       :card/validate
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one}
             {:db/ident       :card/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}

             ;CARD/CLIENT
             {:db/ident       :card/client
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/one}

             ;PURCHASE
             {:db/ident       :purchase/date
              :db/valueType   :db.type/instant
              :db/cardinality :db.cardinality/one}
             {:db/ident       :purchase/price
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one}
             {:db/ident       :purchase/establishment
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :purchase/category
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :purchase/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}


             ;CARD/PURCHASE
             {:db/ident       :card/purchase
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/many}

             ;CLIENTS
             {:db/ident       :client/name
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :client/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}

             ])

(defn create-schema! [conn]
  (d/transact conn schema))

(defn all-cards [db]
  (d/q '[:find (pull ?entity [*])
         :where [?entity :card/number]] db))


(defn generate-adds-purchase [purchases card]
  (reduce (fn [db-adds purchase] (conj db-adds [:db/add
                                            [:card/id (:card/id card)]
                                            :card/purchase
                                            [:purchase/id (:purchase/id purchase)]]))
          []
          purchases))

(defn add-purchases! [conn purchase card]
  (let [a-transacionar (generate-adds-purchase purchase card)]
    (d/transact conn a-transacionar)))

(defn purchase-by-card [db]
  (d/q '[:find ?number (count ?price) (sum ?price)
         :keys card purchase-total sum-total
         :with ?card
         :where [?card :card/number ?number]
         [?card :card/purchase ?purchase]
         [?purchase :purchase/price ?price]] db))

(ns credit-card-with-datomic.model
  (:require [java-time :as j]
    [schema.core :as s]))

(defn uuid [] (java.util.UUID/randomUUID))

(def Client
  {:client/name           s/Str
   :client/id             java.util.UUID})

(def Purchase
  {:purchase/date          java.util.Date
   :purchase/price         BigDecimal
   :purchase/establishment s/Str
   :purchase/category      s/Str
   :purchase/id            java.util.UUID})

(def Card
  {:card/number                    s/Str
   :card/cvv                       s/Str
   :card/limit                     BigDecimal
   :card/validate                  java.util.Date
   :card/client                    Client
   (s/optional-key :card/purchase) Purchase
   :card/id                        java.util.UUID})

(s/defn new-card :- Card
  [number cvv limit validate Client uuid]
  {:card/number   number
   :card/cvv      cvv
   :card/limit    limit
   :card/validate validate
   :card/client   Client
   :card/id       uuid})

(s/defn new-client :- Client
  [name uuid]
  {:client/name           name
   :client/id             uuid})

(s/defn new-purchase
  [date price establishment category uuid]
  {:purchase/date          date
   :purchase/price         price
   :purchase/establishment establishment
   :purchase/category      category
   :purchase/id            uuid})
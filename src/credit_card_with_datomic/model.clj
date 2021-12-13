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
  {:card/number   s/Str
   :card/cvv      s/Str
   :card/limit    BigDecimal
   :card/validate java.util.Date
   :card/client   Client
   :card/purchase Purchase
   :card/id       java.util.UUID})

(s/defn new-card :- Card
  [number cvv limit validate Client Purchase uuid]
  {:card/number   number
   :card/cvv      cvv
   :card/limit    limit
   :card/validate validate
   :card/client   Client
   :card/purchase Purchase
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

;(def Produto
;  {:produto/id                             java.util.UUID
;   (s/optional-key :produto/nome)          s/Str
;   (s/optional-key :produto/slug)          s/Str
;   (s/optional-key :produto/preco)         BigDecimal
;   (s/optional-key :produto/palavra-chave) [s/Str]
;   (s/optional-key :produto/categoria)     Categoria
;   (s/optional-key :produto/estoque)       s/Int
;   (s/optional-key :produto/digital)       s/Bool})



;(s/defn novo-produto  :- Produto
;  ([nome slug preco]
;   (novo-produto (uuid) nome slug preco))
;  ([uuid nome slug preco]
;   (novo-produto uuid nome slug preco 0))
;  ([uuid nome slug preco estoque]
;   {:produto/id    uuid
;    :produto/nome  nome
;    :produto/slug  slug
;    :produto/preco preco
;    :produto/estoque estoque
;    :produto/digital false}))

;(defn new-card [number cvv limit validate uuid]
;  {:card/number   number
;   :card/cvv      cvv
;   :card/limit    limit
;   :card/validate validate
;   :card/id       uuid})


;(s/defn new-purchase)


;(defn nova-categoria
;  ([nome]
;   (nova-categoria (uuid) nome))
;  ([uuid nome]
;   {:categoria/id uuid
;    :categoria/nome nome}))
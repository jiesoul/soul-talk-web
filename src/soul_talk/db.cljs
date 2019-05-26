(ns soul-talk.db
  (:require [reagent.core :as r]))

(def default-db
  {:api-url      "www.jiesoul.com"
   :user (js->clj js/user :keywordize-keys true)
   :login-events []})

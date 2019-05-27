(ns soul-talk.db
  (:require [reagent.core :as r]
            [soul-talk.local-storage :as ls]))

(def user-key "soul-talk-login-key")

(defn set-user-ls
  [user]
  (ls/set-item! user-key user))

(def default-db
  {:active-page :home
   :breadcrumb ["Home"]
   :user (js->clj js/user :keywordize-keys true)
   :login-events []})

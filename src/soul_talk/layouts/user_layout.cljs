(ns soul-talk.layouts.user-layout
  (:require [antd :as antd]
            [soul-talk.components.global-footer :refer [footer]]
            [reagent.core :as r]))

(defn copyright []
  [:> antd/Fragment
   "Copyright "
   [:> antd/Icon {:type "copyright"}]
   " 2019 "])

(defn user-layout [children]
  [:> antd/Layout {:title ""}
   [:div.container
    [:div.content
     [:div.top
      [:div.header
       [:> antd/Link {:to "/"}
        ;[:img.logo {:alt "logo"
        ;            :src "logo"}]
        [:span "Soul Talk"]]]
      children]
     [footer]]]])

(ns soul-talk.components.global-header
  (:require [soul-talk.routes :refer [navigate!]]))

(defn logo []
  [:div.logo
   [:a {:on-click #(navigate! "/")}
    [:h1 "Soul Talk"]]])

(defn header [nav]
  [:> js/antd.Layout.Header
   {:className "home-header"}
   [:> js/antd.Row
    [:> js/antd.Col {:span 2}
     [logo]]
    [:> js/antd.Col {:span 22
                  :style {:text-align "right"}}
     nav]]])
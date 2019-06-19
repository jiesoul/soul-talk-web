(ns soul-talk.components.global-header
  (:require [soul-talk.routes :refer [navigate!]]))

(defn logo []
  [:a {:on-click #(navigate! "/")}
   [:h1 "Soul Talk"]])

(defn header [nav]
  [:> js/antd.Layout.Header
   [:> js/antd.Row
    [:> js/antd.Col {:span 2}
     [logo]]
    [:> js/antd.Col {:span 22
                  :style {:text-align "right"
                          :padding-right "20px"}}
     nav]]])
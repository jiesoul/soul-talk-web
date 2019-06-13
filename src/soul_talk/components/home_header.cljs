(ns soul-talk.components.home-header
  (:require [soul-talk.components.global-header :refer [logo]]))

(defn header [nav]
  [:> js/Layout.Header
   [:> js/Row
    [:> js/Col {:span 2}
     [logo]]
    [:> js/Col {:span  22
                  :style {:text-align    "right"
                          :padding-right "20px"}}
     nav]]])
(ns soul-talk.components.home-header
  (:require [soul-talk.components.global-header :refer [logo]]))

(defn header [nav]
  [:> js/antd.Layout.Header
   [:> js/antd.Row
    [:> js/antd.Col {:span 2}
     [logo]]
    [:> js/antd.Col {:span  22
                  :style {:text-align    "right"
                          :padding-right "20px"}}
     nav]]])
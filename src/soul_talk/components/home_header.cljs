(ns soul-talk.components.home-header
  (:require [soul-talk.components.global-header :refer [logo]]
            [antd :as antd]))

(defn header [nav]
  [:> antd/Layout.Header
   [:> antd/Row
    [:> antd/Col {:span 2}
     [logo]]
    [:> antd/Col {:span  22
                  :style {:text-align    "right"
                          :padding-right "20px"}}
     nav]]])
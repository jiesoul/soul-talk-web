(ns soul-talk.components.home-header
  (:require [antd :as antd]
            [soul-talk.components.global-header :refer [logo]]))

(defn header [nav]
  [:> antd/Layout.Header
   [:> antd/Row
    [:> antd/Col {:span 2}
     [logo]]
    [:> antd/Col {:span 22
                  :style {:text-align "right"
                          :padding-right "20px"}}
     nav]]])
(ns soul-talk.components.global-header
  (:require [antd :as antd]))

(defn logo []
  [:h1 {:className "brand"} "Soul Talk"])

(defn header [nav]
  [:> antd/Layout.Header
   [:> antd/Row
    [:> antd/Col {:span 2}
     [logo]]
    [:> antd/Col {:span 22
                  :style {:text-align "right"
                          :padding-right "20px"}}
     nav]]])
(ns soul-talk.components.global-header
  (:require [antd :as antd]
            [soul-talk.routes :refer [navigate!]]))

(defn logo []
  [:h1 {:className "brand"
        :on-click #(navigate! "/")} "Soul Talk"])

(defn header [nav]
  [:> antd/Layout.Header
   [:> antd/Row
    [:> antd/Col {:span 2}
     [logo]]
    [:> antd/Col {:span 22
                  :style {:text-align "right"
                          :padding-right "20px"}}
     nav]]])
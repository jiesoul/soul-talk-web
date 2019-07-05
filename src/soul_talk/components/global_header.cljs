(ns soul-talk.components.global-header
  (:require [soul-talk.routes :refer [navigate!]]))

(defn logo []
  [:div.logo
   [:a {:on-click #(navigate! "#/")}
    [:h1 "JIESOUL的个人网站"]]])

(defn header [nav]
  [:> js/antd.Layout.Header
   {:className "home-header"}
   [:> js/antd.Row
    [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8}
     [logo]]
    [:> js/antd.Col {:xs 24 :sm 24 :md 16 :lg 16
                  :style {:text-align "right"}}
     nav]]])
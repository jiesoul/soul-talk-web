(ns soul-talk.pages.header
  (:require [antd :as antd]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]))

(defn header-common [menu]
  [:div
   [:> antd/Layout.Header
    [:> antd/Row
     [:> antd/Col {:span 2}
      [:h1 {:className "brand"} "Soul Talk"]]
     [:> antd/Col {:span 22}
      [:div menu]]]]])
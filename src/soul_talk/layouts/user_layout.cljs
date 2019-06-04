(ns soul-talk.layouts.user-layout
  (:require [antd :as antd]
            [soul-talk.components.global-footer :refer [footer]]
            [reagent.core :as r]))

(defn copyright []
  [:> antd/Layout.Footer
   "Copyright "
   [:> antd/Icon {:type "copyright"}]
   " 2019 "])

(defn user-layout [children]
  [:> antd/Layout {:title ""}
   [:> antd/Layout.Content
    [:<>
     children
     copyright]]])

(ns soul-talk.layouts.blank-layout
  (:require [soul-talk.components.global-footer :refer [footer]]))

(defn layout [children]
  [:> js/Layout
   [:> js/Layout.Content
    children]
   [:> js/Divider]
   [footer]])

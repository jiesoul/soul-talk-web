(ns soul-talk.layouts.blank-layout
  (:require [soul-talk.components.global-footer :refer [footer]]
            ))

(defn layout [children]
  [:> js/antd.Layout
   [:> js/antd.Layout.Content
    children]
   [:> js/antd.Divider]
   [footer]])

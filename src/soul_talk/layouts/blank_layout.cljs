(ns soul-talk.layouts.blank-layout
  (:require [soul-talk.components.global-footer :refer [footer]]
            [antd :as antd]))

(defn layout [children]
  [:> antd/Layout
   [:> antd/Layout.Content
    children]
   [:> antd/Divider]
   [footer]])

(ns soul-talk.layouts.blank-layout
  (:require [antd :as antd]
            [soul-talk.components.global-footer :refer [footer]]))

(defn layout [children]
  [:> antd/Layout
   [:> antd/Layout.Content
    children]
   [:> antd/Divider]
   [footer]])

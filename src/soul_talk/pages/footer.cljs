(ns soul-talk.pages.footer
  (:require [antd :as antd]))

(defn home-footer-component []
  (fn []
    [:> antd/Layout.Footer {:style {:text-align "center"}}
     [:h2 "Made with By Ant Design and JIESOUL "]]))
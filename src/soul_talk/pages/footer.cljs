(ns soul-talk.pages.footer
  (:require [antd :as antd]))

(defn home-footer-component []
  (fn []
    [:> antd/Layout.Footer {:style {:text-align "center"}}
     [:h4 "Made with By"
      [:> antd/Button
       {:type "link"
        :href "https://ant.design"
        :target "_blank"}
       "Ant Design"]
      "and JIESOUL "]]))
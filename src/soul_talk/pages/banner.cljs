(ns soul-talk.pages.banner
  (:require [antd :as antd]))

(defn home-banner-component []
  (fn []
    [:> antd/Carousel {:auto-play true}
     [:div {:style {:min-height "100vh"}}
      [:h2.display-4.font-italic "进一步有一步的欢喜"]]
     [:div
      [:h3 "2"]]]))
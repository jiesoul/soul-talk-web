(ns soul-talk.pages.blog
  (:require [antd :as antd]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout]]))

(defn main-component []
  (fn []
    [:div
     [:> antd/Row
      [:> antd/Col {:span 20 :offset 2}]]]))

(defn blog-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:> antd/Layout.Content {:className "home"}
      [:> antd/Layout.Content {:className "home-wrapper"}
       [:div
        [:h1 ""]]]
      [:> antd/Layout.Content {:className "home-wrapper"}
       [:div.page1
        [main-component]]]]]))

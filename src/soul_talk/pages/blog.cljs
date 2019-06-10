(ns soul-talk.pages.blog
  (:require [antd :as antd]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout nav]]
            [soul-talk.pages.post :refer [blog-post-component]]
            [soul-talk.components.home-header :refer [header]]))

(defn main-component []
  (fn []
    [:div
     [:> antd/Row
      [:> antd/Col {:span 20 :offset 2}]]]))

(defn blog-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:> antd/Layout.Content {:className "blog"}
      [:> antd/Layout.Content {:className "blog-wrapper"}
       [:> antd/Row
        [header [nav @active-page]]]
       [:> antd/Row
        [:> antd/Col {:span 16 :offset 4}
         [blog-post-component]]]]]]))

(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout nav]]
            [soul-talk.pages.post :refer [blog-post-component archives-component]]
            [soul-talk.pages.users :refer [where-component]]
            [soul-talk.components.home-header :refer [header]]
            [antd :as antd])
  (:import [goog.history.Html5History]))


(defn main-component []
  (fn []
    [:div
     [:> antd/Row
      [:> antd/Col {:span 20 :offset 2}
       [blog-post-component]]]]))

(defn home-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    (fn []
      [layout
       [:> antd/Layout.Content {:className "home"}
        [:> antd/Layout.Content {:className "home-wrapper"}
         [:> antd/Row
          [header [nav @active-page]]]
         [:> antd/Row
          [:div
           [:h1 "进一步有一步的欢喜"]]]]
        [:> antd/Layout.Content {:className "home-wrapper page1"}
         [:div.page1
          [main-component]]]]])))
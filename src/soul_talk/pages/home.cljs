(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout nav]]
            [soul-talk.components.post :refer [home-posts]]
            [soul-talk.pages.users :refer [where-component]]
            [soul-talk.components.home-header :refer [header]]
            [antd :as antd])
  (:import [goog.history.Html5History]))

(defn home-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    (fn []
      [layout
       [:div
        [:div.home-wrapper
         [:> antd/Row
          [header [nav @active-page]]]
         [:> antd/Row
          [:div.home-wrapper-section
           [:h1 "进一步有一步的欢喜"]]]]
        [:div.home-wrapper-page1
         [home-posts]]]])))
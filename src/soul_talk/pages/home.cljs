(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [soul-talk.pages.common :as c]
            [soul-talk.pages.header :refer [home-header-component]]
            [soul-talk.pages.banner :refer [home-banner-component]]
            [soul-talk.pages.footer :refer [home-footer-component]]
            [soul-talk.pages.post :refer [blog-post-component archives-component]]
            [soul-talk.pages.users :refer [where-component]]
            [antd :as antd])
  (:import [goog.history.Html5History]))

(defn main-component []
  (fn []
    [:> antd/Layout.Content
     [:> antd/Row
      [:> antd/Col {:span 24}
       [home-banner-component]]]]))

(defn home-component []
  (fn []
    [:> antd/Layout
     [home-header-component]
     [main-component]
     [home-footer-component]]))

(defn home-page []
  [home-component])
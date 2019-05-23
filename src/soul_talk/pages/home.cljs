(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [antd :as antd]
            [soul-talk.pages.header :refer [header-component home-menu-component]]
            [soul-talk.pages.banner :refer [home-banner-component]]
            [soul-talk.pages.footer :refer [home-footer-component]]
            [soul-talk.pages.post :refer [blog-post-component archives-component]]
            [soul-talk.pages.users :refer [where-component]])
  (:import [goog.history.Html5History]))

(defn main-component []
  (fn []
    [:> antd/Layout.Content {:style {:padding "0 50px"}}
     [blog-post-component]]))

(defn home-component []
  (fn []
    [:> antd/Layout
     [header-component home-menu-component]
     ;[home-banner-component]
     [:> antd/Divider]
     [main-component]
     [home-footer-component]]))

(defn home-page []
  [home-component])
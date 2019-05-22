(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [antd :as antd]
            [soul-talk.pages.header :refer [home-header-component]]
            [soul-talk.pages.banner :refer [home-banner-component]]
            [soul-talk.pages.footer :refer [home-footer-component]]
            [soul-talk.pages.post :refer [blog-post-component archives-component]]
            [soul-talk.pages.users :refer [where-component]])
  (:import [goog.history.Html5History]))

(defn main-component []
  (fn []
    [:> antd/Layout.Content {:style {:padding "0 50px"}}
     [:> antd/Row {:style {:margin "16px 0" :background "#fff" :padding 24 :min-height 280}}
      [:> antd/Col {:span 24}
       [home-banner-component]]]]))

(defn home-component []
  (fn []
    [:> js/antd.Layout
     [home-header-component]
     [main-component]
     [home-footer-component]]))

(defn home-page []
  [home-component])
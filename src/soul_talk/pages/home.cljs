(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [antd :as antd]
            [soul-talk.pages.layout :refer [home-basic header-common footer-common]]
            [soul-talk.pages.post :refer [blog-post-component archives-component]]
            [soul-talk.pages.users :refer [where-component]])
  (:import [goog.history.Html5History]))

(defn home-nav []
  (fn []
    [:> antd/Menu {:id "nav"
                   :key "nav"
                   :theme "dark"
                   :mode "horizontal"
                   :defaultSelectKeys ["home"]
                   :style {:line-height "64px"}}
     [:> antd/Menu.Item {:key "home"} "Home"]
     ;[:> antd/Menu.Item {:key "blog"} "Blog"]
     ;[:> antd/Menu.Item {:key "about"} "About"]
     ]))

(defn main-component []
  (fn []
    [:div
     [:> antd/Layout.Content {:style {:padding "0 50px"}}
      [blog-post-component]]]))

(defn home-page []
  [home-basic
   [header-common home-nav]
   [main-component]
   [footer-common]])
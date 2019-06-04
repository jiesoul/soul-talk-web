(ns soul-talk.pages.home
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.pages.post :refer [blog-post-component archives-component]]
            [soul-talk.pages.users :refer [where-component]]
            [antd :as antd])
  (:import [goog.history.Html5History]))

(defn home-nav []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    (fn []
      [:> antd/Menu {:id                  "nav"
                     :key                 "nav"
                     :theme               "dark"
                     :mode                "horizontal"
                     :default-select-keys [(key->js @active-page)]
                     :style               {:line-height "64px"}}
       [:> antd/Menu.Item {:key "home"} "Home"]
       ;[:> antd/Menu.Item {:key "blog"} "Blog"]
       ;[:> antd/Menu.Item {:key "about"} "About"]
       ])))

(defn main-component []
  (fn []
    [:div
     [:> antd/Row
      [:> antd/Col {:span 20 :offset 2}
       [blog-post-component]]]]))

(defn home-page []
  [:div
   [main-component]
   [footer]])
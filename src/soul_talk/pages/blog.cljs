(ns soul-talk.pages.blog
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout]]
            [soul-talk.components.post :refer [blog-posts blog-archives blog-archives-posts]]))

(defn blog-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:div.home-wrapper-page1
      [:> js/antd.Row {:gutter 16}
       [:> js/antd.Col {:span 14 :offset 2}
        [:> js/antd.Card
         {:title "文章列表"}
         [blog-posts]]]
       [:> js/antd.Col {:span 6}
        [blog-archives]]]]]))

(defn blog-archives-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:div.home-wrapper-page1
      [:> js/antd.Row {:gutter 16}
       [:> js/antd.Col {:span 16 :offset 2}
        [:> js/antd.Card
         {:title "文章列表"}
         [blog-archives-posts]]]
       [:> js/antd.Col {:span 4}
        [blog-archives]]]]]))

(ns soul-talk.pages.blog
  (:require
            [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout nav]]
            [soul-talk.components.post :refer [blog-posts blog-archives blog-archives-posts]]
            [soul-talk.components.home-header :refer [header]]
            ))

(defn blog-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:> js/antd.Layout.Content {:className "blog"}
      [:> js/antd.Layout.Content {:className "blog-wrapper"}
       [:> js/antd.Row
        [header [nav @active-page]]]
       [:> js/antd.Row
        [:> js/antd.Col {:span 14 :offset 2}
         [:> js/antd.Card
          {:title "文章列表"}
          [blog-posts]]]
        [:> js/antd.Col {:span 6}
         [:> js/antd.Card
          {:title "归档"}
          [blog-archives]]]]]]]))

(defn blog-archives-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:> js/antd.Layout.Content {:className "blog"}
      [:> js/antd.Layout.Content {:className "blog-wrapper"}
       [:> js/antd.Row
        [header [nav @active-page]]]
       [:> js/antd.Row
        [:> js/antd.Col {:span 16 :offset 4}
         [:> js/antd.Card
          {:title "文章列表"}
          [blog-archives-posts]]]]]]]))

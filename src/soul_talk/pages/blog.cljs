(ns soul-talk.pages.blog
  (:require [antd :as antd]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [soul-talk.layouts.home-layout :refer [layout nav]]
            [soul-talk.components.post :refer [blog-posts blog-archives blog-archives-posts]]
            [soul-talk.components.home-header :refer [header]]))

(defn blog-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:> antd/Layout.Content {:className "blog"}
      [:> antd/Layout.Content {:className "blog-wrapper"}
       [:> antd/Row
        [header [nav @active-page]]]
       [:> antd/Row
        [:> antd/Col {:span 14 :offset 2}
         [:> antd/Card
          {:title "文章列表"}
          [blog-posts]]]
        [:> antd/Col {:span 6}
         [:> antd/Card
          {:title "归档"}
          [blog-archives]]]]]]]))

(defn blog-archives-page []
  (r/with-let [active-page (rf/subscribe [:active-page])]
    [layout
     [:> antd/Layout.Content {:className "blog"}
      [:> antd/Layout.Content {:className "blog-wrapper"}
       [:> antd/Row
        [header [nav @active-page]]]
       [:> antd/Row
        [:> antd/Col {:span 16 :offset 4}
         [:> antd/Card
          {:title "文章列表"}
          [blog-archives-posts]]]]]]]))

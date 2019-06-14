(ns soul-talk.layouts.home-layout
  (:require [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.routes :refer [navigate!]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [antd :as antd]))

(defn nav [active-page]
  [:> antd/Menu {:id "home-nav"
                 :mode                "horizontal"
                 :theme               "light"
                 :defaultSelectKeys ["home"]
                 :selectedKeys       [(key->js active-page)]}
   [:> antd/Menu.Item {:key      "home"
                       :on-click #(navigate! "/")}
    "首页"]
   [:> antd/Menu.Item {:key      "blog"
                       :on-click #(navigate! "#/blog")}
    "博客"]])

(defn layout [children]
  (r/with-let [active-page (rf/subscribe [:active-page])]
    (fn []
      [:> antd/Layout
       [:> antd/Layout.Content {:className "home"}
        children]
       ;[:> antd/Divier]
       [footer]])))
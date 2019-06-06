(ns soul-talk.layouts.home-layout
  (:require [antd :as antd]
            [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.components.home-header :refer [header]]
            [soul-talk.routes :refer [navigate!]]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn nav [active-page]
  [:> antd/Menu {:id "home-nav"
                 :mode                "horizontal"
                 :theme               "dark"
                 :default-select-keys ["home"]
                 :selected-keys       [(key->js active-page)]}
   [:> antd/Menu.Item {:key      "home"
                       :on-click #(navigate! "/")}
    "首页"]
   [:> antd/Menu.Item {:key      "blog"
                       :on-click #(navigate! "#/blog")}
    "博客"]])

(defn layout [children]
  (r/with-let [active-page (rf/subscribe [:active-page])]
    (fn []
      [:div
       [:> antd/Layout {:className "home"}
        [header
         [nav @active-page]]
        [:> antd/Layout
         children]
        [footer]]])))
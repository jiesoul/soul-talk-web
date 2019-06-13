(ns soul-talk.layouts.home-layout
  (:require [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.routes :refer [navigate!]]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn nav [active-page]
  [:> js/Menu {:id "home-nav"
                 :mode                "horizontal"
                 :theme               "light"
                 :default-select-keys ["home"]
                 :selected-keys       [(key->js active-page)]}
   [:> js/Menu.Item {:key      "home"
                       :on-click #(navigate! "/")}
    "首页"]
   [:> js/Menu.Item {:key      "blog"
                       :on-click #(navigate! "#/blog")}
    "博客"]])

(defn layout [children]
  (r/with-let [active-page (rf/subscribe [:active-page])]
    (fn []
      [:> js/Layout
       [:> js/Layout.Content {:className "home"}
        children]
       ;[:> js/Divier]
       [footer]])))
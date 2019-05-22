(ns soul-talk.pages.header
  (:require [antd :as antd]))

(defn home-menu-component []
  (fn []
    [:> antd/Menu {:id "nav"
                   :key "nav"
                   :theme "dark"
                   :mode "horizontal"
                   :default-select-keys ["home"]
                   :style {:line-height "64px"}}
     [:> antd/Menu.Item {:key "home"} "Home"]
     [:> antd/Menu.Item {:key "blog"} "Blog"]
     [:> antd/Menu.Item {:key "about"} "About"]
     ]))

(defn home-header-component []
  (fn []
    [:> antd/Layout.Header
     [:div {:className "logo" :align "middle" :style {:text-align "center"}}
      "Soul Talk"]
     [home-menu-component]]))


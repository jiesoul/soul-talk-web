(ns soul-talk.pages.header
  (:require [antd :as antd]))

(defn home-menu-component []
  (fn []
    [:> antd/Menu {:id "nav"
                   :key "nav"
                   :align "right"
                   :mode "horizontal"
                   :default-select-keys ["home"]}
     [:> antd/Menu.Item {:key "home"} "Home"]
     [:> antd/Menu.Item {:key "blog"} "Blog"]
     [:> antd/Menu.Item {:key "about"} "About"]
     ]))

(defn home-header-component []
  (fn []
    [:> antd/Layout.Header
     [:> antd/Row
      [:> antd/Col {:lg 4 :md 5 :sm 20 :xs 20}
       [:a {:id "logo"}
        [:span "Soul Talk"]]]
      [:> antd/Col {:lg 20 :md 19 :sm 0 :xs 0}
       [home-menu-component]]]]))


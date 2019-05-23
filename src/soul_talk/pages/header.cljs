(ns soul-talk.pages.header
  (:require [antd :as antd]
            [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]))

(defn home-menu-component []
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


(defn admin-menu-component []
  (r/with-let [user (subscribe [:user])]
    (if @user
      (fn []
        [:> antd/Menu {:id "admin-nav"
                       :key "admin-nav"
                       :theme "dark"
                       :mode "horizontal"
                       :style {:line-height "64px"}}
         [:> antd/Menu.Item {:key "cancel"}
          [:> antd/Button {:type "link"}
           "退出"]]]))))

(defn header-component [menu-component]
  (fn []
    [:> antd/Layout.Header
     [:> antd/Row
      [:> antd/Col {:span 2}
       [:h1 {:className "brand"} "Soul Talk"]]
      [:> antd/Col {:span 22}
       [menu-component]]]]))


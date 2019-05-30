(ns soul-talk.pages.layout
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.pages.header :refer [header-common]]
            [antd :as antd]))

(defn admin-nav []
  (r/with-let [user (subscribe [:user])]
    (when @user
      (fn []
        [:div
         [:> antd/Menu {:id                  "admin-nav"
                        :key                 "admin-nav"
                        :theme               "dark"
                        :mode                "horizontal"
                        :style               {:line-height "64px"}
                        :default-select-keys ["user-name"]}
          [:> antd/Menu.Item {:key "user-name"}
           (str "欢迎你 " (:name @user))]
          [:> antd/Menu.Item {:key      "cancel"
                              :on-click #(dispatch [:logout])}
           "退出"]]]))))

(defn admin-sidebar []
  (r/with-let [user (subscribe [:user])
               active-page (subscribe [:active-page])]
    (when @user
      (fn []
        [:> antd/Layout.Sider {:style {:background "#FFF" :margin "5px"}}
         [:> antd/Menu {:mode                "inline"
                        :default-select-keys ["admin"]
                        :default-open-keys   ["blog"]
                        :selected-keys       [(key->js @active-page)]
                        :style               {:height       "100%"
                                              :border-right 0}}
          [:> antd/Menu.Item {:key      "admin"
                              :on-click #(dispatch [:navigate-to "#/admin"])}
           "面板"]
          [:> antd/Menu.SubMenu {:key   "blog"
                                 :title "博客管理"}
           [:> antd/Menu.Item {:key      "categories"
                               :on-click #(dispatch [:navigate-to "#/categories"])}
            "分类"]
           [:> antd/Menu.Item {:key      "posts"
                               :on-click #(dispatch [:navigate-to "#/posts"])}
            "文章"]]

          [:> antd/Menu.SubMenu {:key   "1"
                                 :title "用户"}
           [:> antd/Menu.Item {:key "profile"} "个人信息"]
           [:> antd/Menu.Item {:key "password"} "密码修改"]
           ]]]))))

(defn admin-basic [header sidebar main]
  [:> antd/Layout
   [:div header]
   [:> antd/Layout
    [:div sidebar]
    [:> antd/Layout {:style {:padding "0 24px 24px"}}
     [:div main]]]])

(defn admin-header-main [header main]
  [:> antd/Layout
   [:div header]
   [:> antd/Layout
    [:div main]]])

(defn admin-default [main]
  [admin-basic
   [header-common admin-nav]
   [admin-sidebar]
   [main]])

(defn home-basic
  ([main] (home-basic nil main nil))
  ([main footer] (home-basic nil main footer))
  ([header main footer]
   (fn [header main footer]
     [:<>
      [:div header]
      [:div main]
      [:div footer]])))


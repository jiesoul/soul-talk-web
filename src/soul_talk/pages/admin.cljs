(ns soul-talk.pages.admin
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [antd :as antd]
            [soul-talk.routes :refer [categories]]
            [soul-talk.pages.header :refer [header-component admin-menu-component]]
            [soul-talk.pages.common :as c]))


(defn admin-sidebar []
  (r/with-let [user (subscribe [:user])
               active-page (subscribe [:active-page])]
    (when @user
      (fn []
        [:> antd/Menu {:mode                "inline"
                       :default-select-keys ["dash"]
                       :default-open-keys   ["blog"]
                       :selected-keys       [(key->js @active-page)]
                       :style               {:height       "100%"
                                             :border-right 0}}
         [:> antd/Menu.Item {:key "dash"} "面板"]
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
          ]]))))

(defn main-component []
  [:div
   [:h1 "欢迎"]])

(defn admin-page-component [page-component]
  (r/with-let []
    (fn [page-component]
      [:> antd/Layout
       [header-component admin-menu-component]
       [:> antd/Layout
        [:> antd/Layout.Sider {:style {:background "#FFFFFF"}}
         [admin-sidebar]]
        [:> antd/Layout {:style {:padding "0 24px 24px"}}
         [page-component]]]])))



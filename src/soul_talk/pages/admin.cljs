(ns soul-talk.pages.admin
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [secretary.core :refer [dispatch!]]
            [antd :as antd]
            [soul-talk.routes :refer [categories]]
            [soul-talk.pages.header :refer [header-component admin-menu-component]]))


(defn admin-sidebar []
  (r/with-let [user (subscribe [:user])]
    (when @user
      (fn []
        [:> antd/Menu {:mode                "inline"
                       :default-select-keys ["1"]
                       :default-open-keys   ["blog"]
                       :style               {:height       "100%"
                                             :border-right 0}}
         [:> antd/Menu.Item {:key "1"} "面板"]
         [:> antd/Menu.SubMenu {:key   "blog"
                                :title "博客管理"}
          [:> antd/Menu.Item {:key      "category"
                              :on-click (fn []
                                          (dispatch [:navigate-to "#/categories"]))}
           "分类"]
          [:> antd/Menu.Item {:key "post"} "文章"]]

         [:> antd/Menu.SubMenu {:key   "1"
                                :title "用户"}
          [:> antd/Menu.Item {:key "profile"} "个人信息"]
          [:> antd/Menu.Item {:key "password"} "密码修改"]
          ]]))))

(defn main-component []
  [:div
   [:h1 "欢迎"]])

(defn admin-page-component [page-component]
  (js/console.log "load admin-page-component: " page-component)
  (fn [page-component]
    [:> antd/Layout
     [header-component admin-menu-component]
     [:> antd/Layout
      [:> antd/Layout.Sider {:style {:background "#fff"}}
       [admin-sidebar]]
      [:> antd/Layout {:style {:padding "0 24px 24px"}}
       [:> antd/Breadcrumb
        {:style {:margin "16px 0"}}
        [:> antd/Breadcrumb.Item "Home"]]
       [:> antd/Layout.Content
        {:style {:background "#fff"
                 :padding    24
                 :margin     0
                 :min-height 280}}
        (js/console.log "load admin header....")
        [page-component]]]]]))



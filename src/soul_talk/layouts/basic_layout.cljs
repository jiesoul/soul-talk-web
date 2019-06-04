(ns soul-talk.layouts.basic-layout
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.components.global-header :refer [header]]
            [soul-talk.components.header-dropdown :refer [header-dropdown]]
            [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.routes :refer [navigate!]]
            [antd :as antd]))


(defn nav [user]
  [:> antd/Menu
   [:> antd/Menu.Item {:key "user-profile"
                       :on-click #(navigate! "#/user-profile")}
    [:> antd/Icon {:type "user"}]
    "个人信息"]
   [:> antd/Menu.Item {:key "change-pass"
                       :on-click #(navigate! "#/change-pass")}
    [:> antd/Icon {:type "setting"}]
    "密码修改"]
   [:> antd/Menu.Divider]
   [:> antd/Menu.Item {:key      "cancel"
                       :on-click #(dispatch [:logout])}
    [:> antd/Icon {:type "poweroff"}]
    "退出登录"]])

(defn sidebar [active-page]
  [:> antd/Layout.Sider
   [:> antd/Menu {:mode                "inline"
                  :theme               "light"
                  :default-select-keys ["admin"]
                  :default-open-keys   ["blog" "user"]
                  :selected-keys       [(key->js @active-page)]
                  :style               {:height       "100%"
                                        :border-right 0}}
    [:> antd/Menu.Item {:key      "admin"
                        :on-click #(navigate! "#/admin")}
     [:span
      [:> antd/Icon {:type "area-chart"}]
      [:span "Dash"]]]
    [:> antd/Menu.SubMenu {:key   "blog"
                           :title (r/as-element [:span
                                                 [:> antd/Icon {:type "form"}]
                                                 [:span "文章管理"]])}
     [:> antd/Menu.Item {:key      "categories"
                         :on-click #(navigate! "#/categories")}
      "分类"]
     [:> antd/Menu.Item {:key      "posts"
                         :on-click #(navigate! "#/posts")}
      "文章"]]

    [:> antd/Menu.SubMenu {:key   "user"
                           :title (r/as-element
                                    [:span
                                     [:> antd/Icon {:type "user"}]
                                     [:span "个人管理"]])}
     [:> antd/Menu.Item {:key      "user-profile"
                         :icon     "user"
                         :on-click #(navigate! "#/user-profile")}
      "个人信息"]
     [:> antd/Menu.Item {:key      "change-pass"
                         :on-click #(navigate! "#/change-pass")} "密码修改"]
     ]]])

(defn basic-layout [main]
  (r/with-let [user (subscribe [:user])
               active-page (subscribe [:active-page])]
    [:div
     [:> antd/Layout
      [header
       [header-dropdown (r/as-element [nav user]) (:name @user)]]
      [:> antd/Layout
       [sidebar active-page]
       [:> antd/Layout.Content {:className "main"}
        main]]
      [footer]]]))


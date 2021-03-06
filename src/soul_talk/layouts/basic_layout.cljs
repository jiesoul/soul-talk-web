(ns soul-talk.layouts.basic-layout
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.components.global-header :refer [header]]
            [soul-talk.components.header-dropdown :refer [header-dropdown]]
            [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.routes :refer [navigate!]]
            [soul-talk.components.common :as c]))


(defn nav []
  [:> js/antd.Menu
   [:> js/antd.Menu.Item {:key "user-profile"
                       :on-click #(navigate! "#/user-profile")}
    [:> js/antd.Icon {:type "user"}]
    "个人信息"]
   [:> js/antd.Menu.Item {:key "change-pass"
                       :on-click #(navigate! "#/change-pass")}
    [:> js/antd.Icon {:type "setting"}]
    "密码修改"]
   [:> js/antd.Menu.Divider]
   [:> js/antd.Menu.Item {:key      "cancel"
                       :on-click #(dispatch [:logout])}
    [:> js/antd.Icon {:type "poweroff"}]
    "退出登录"]])

(defn sidebar [active-page]
  [:> js/antd.Layout.Sider {:className "sidebar"}
   [:> js/antd.Menu {:mode                "inline"
                  :className            "sidebar"
                  :theme               "light"
                  :default-select-keys ["admin"]
                  :default-open-keys   ["blog" "user"]
                  :selected-keys       [(key->js @active-page)]}
    [:> js/antd.Menu.Item {:key      "admin"
                        :on-click #(navigate! "#/admin")}
     [:span
      [:> js/antd.Icon {:type "area-chart"}]
      [:span "Dash"]]]
    [:> js/antd.Menu.SubMenu {:key   "blog"
                           :title (r/as-element [:span
                                                 [:> js/antd.Icon {:type "form"}]
                                                 [:span "文章管理"]])}
     [:> js/antd.Menu.Item {:key      "categories"
                         :on-click #(navigate! "#/categories")}
      "分类"]
     [:> js/antd.Menu.Item {:key      "posts"
                         :on-click #(navigate! "#/posts")}
      "文章"]]

    [:> js/antd.Menu.SubMenu {:key   "user"
                           :title (r/as-element
                                    [:span
                                     [:> js/antd.Icon {:type "user"}]
                                     [:span "个人管理"]])}
     [:> js/antd.Menu.Item {:key      "user-profile"
                         :icon     "user"
                         :on-click #(navigate! "#/user-profile")}
      "个人信息"]
     [:> js/antd.Menu.Item {:key      "change-pass"
                         :on-click #(navigate! "#/change-pass")} "密码修改"]
     ]]])

(defn basic-layout [main]
  (r/with-let [user (subscribe [:user])
               active-page (subscribe [:active-page])
               breadcrumb (subscribe [:breadcrumb])]
    (fn []
      [:div.admin
       [:> js/antd.Layout
        [header
         [header-dropdown (r/as-element [nav]) (:name @user)]]
        [:> js/antd.Layout
         [sidebar active-page]
         [:> js/antd.Layout.Content {:className "main"}
          [:div
           [c/breadcrumb-component @breadcrumb]
           [:hr]
           main]]]
        [footer]]])))


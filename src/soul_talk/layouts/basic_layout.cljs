(ns soul-talk.layouts.basic-layout
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.components.global-header :refer [header]]
            [soul-talk.components.header-dropdown :refer [header-dropdown]]
            [soul-talk.components.global-footer :refer [footer]]
            [soul-talk.routes :refer [navigate!]]))


(defn nav []
  [:> js/Menu
   [:> js/Menu.Item {:key "user-profile"
                       :on-click #(navigate! "#/user-profile")}
    [:> js/Icon {:type "user"}]
    "个人信息"]
   [:> js/Menu.Item {:key "change-pass"
                       :on-click #(navigate! "#/change-pass")}
    [:> js/Icon {:type "setting"}]
    "密码修改"]
   [:> js/Menu.Divider]
   [:> js/Menu.Item {:key      "cancel"
                       :on-click #(dispatch [:logout])}
    [:> js/Icon {:type "poweroff"}]
    "退出登录"]])

(defn sidebar [active-page]
  [:> js/Layout.Sider
   [:> js/Menu {:mode                "inline"
                  :theme               "light"
                  :default-select-keys ["admin"]
                  :default-open-keys   ["blog" "user"]
                  :selected-keys       [(key->js @active-page)]
                  :style               {:height       "100%"
                                        :border-right 0}}
    [:> js/Menu.Item {:key      "admin"
                        :on-click #(navigate! "#/admin")}
     [:span
      [:> js/Icon {:type "area-chart"}]
      [:span "Dash"]]]
    [:> js/Menu.SubMenu {:key   "blog"
                           :title (r/as-element [:span
                                                 [:> js/Icon {:type "form"}]
                                                 [:span "文章管理"]])}
     [:> js/Menu.Item {:key      "categories"
                         :on-click #(navigate! "#/categories")}
      "分类"]
     [:> js/Menu.Item {:key      "posts"
                         :on-click #(navigate! "#/posts")}
      "文章"]]

    [:> js/Menu.SubMenu {:key   "user"
                           :title (r/as-element
                                    [:span
                                     [:> js/Icon {:type "user"}]
                                     [:span "个人管理"]])}
     [:> js/Menu.Item {:key      "user-profile"
                         :icon     "user"
                         :on-click #(navigate! "#/user-profile")}
      "个人信息"]
     [:> js/Menu.Item {:key      "change-pass"
                         :on-click #(navigate! "#/change-pass")} "密码修改"]
     ]]])

(defn basic-layout [main]
  (r/with-let [user (subscribe [:user])
               active-page (subscribe [:active-page])]
    (fn []
      [:div
       [:> js/Layout
        [header
         [header-dropdown (r/as-element [nav]) (:name @user)]]
        [:> js/Layout {:style {:min-height "660px"}}
         [sidebar active-page]
         [:> js/Layout.Content {:className "main"}
          main]]
        [footer]]])))


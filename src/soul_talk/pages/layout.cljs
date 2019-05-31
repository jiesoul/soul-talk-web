(ns soul-talk.pages.layout
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.pages.header :refer [header-common]]
            [soul-talk.pages.footer :refer [footer-common]]
            [soul-talk.routes :refer [navigate!]]
            [antd :as antd]))

(defn admin-nav []
  (r/with-let [user (subscribe [:user])]
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
      "退出"]]))

(defn admin-sidebar []
  (r/with-let [user (subscribe [:user])
               active-page (subscribe [:active-page])]
    (when @user
      (fn []
        [:> antd/Layout.Sider
         [:> antd/Menu {:mode                "inline"
                        :theme                "dark"
                        :default-select-keys ["admin"]
                        :default-open-keys   ["blog" "user"]
                        :selected-keys       [(key->js @active-page)]
                        :style               {:height       "100%"
                                              :border-right 0}}
          [:> antd/Menu.Item {:key      "admin"
                              :on-click #(navigate! "#/admin")}
           [:span
            [:> antd/Icon {:type"area-chart"}]
            [:span  "Dash"]]]
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
           [:> antd/Menu.Item {:key "user-profile"
                               :icon "user"
                               :on-click #(navigate! "#/user-profile")}
            "个人信息"]
           [:> antd/Menu.Item {:key "change-pass"
                               :on-click #(navigate! "#/change-pass")} "密码修改"]
           ]]]))))

(defn admin-basic [header sidebar main]
  [:> antd/Layout
   [:div header]
   [:> antd/Layout
    [:div sidebar]
    [:> antd/Layout {:style {:padding "0 24px 24px"}}
     [:div main]]]
   [footer-common]])

(defn admin-header-main [header main]
  (admin-basic header nil main))

(defn admin-default [main]
  (admin-basic
    (header-common admin-nav)
    [admin-sidebar]
    main))

(defn home-basic
  ([main] (home-basic nil main nil))
  ([main footer] (home-basic nil main footer))
  ([header main footer]
   (fn [header main footer]
     [:div
      [:div header]
      [:div main]
      [:div footer]])))


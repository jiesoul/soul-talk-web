(ns soul-talk.pages.layout
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.routes :refer [navigate!]]
            [antd :as antd]))

(defn logo []
  [:h1 {:className "brand"} "Soul Talk"])

(defn header-common [menu]
  [:div
   [:> antd/Layout.Header {:style {:padding "0 10px"}}
    [:> antd/Row
     [:> antd/Col {:span 2}
      [logo]]
     [:> antd/Col {:span 22}
      [menu]]]]])

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

(defn footer-common []
  (fn []
    [:> antd/Layout.Footer {:style {:text-align "center"
                                    :background "#3e3e3e"}}
     [:h4 {:style {:color "#FFF"}}
      "Made with By "
      [:a
       {:type "link"
        :href "https://ant.design"
        :target "_blank"}
       "Ant Design"]
      " and JIESOUL "]]))

(defn admin-basic [header sidebar main]
  [:> antd/Layout
   header
   [:> antd/Layout
    sidebar
    [:> antd/Layout {:style {:padding "0 24px 24px"}}
     main]]
   ;[:> antd/Divider]
   [footer-common]])

(defn admin-header-main [header main]
  [admin-basic header nil main])

(defn admin-default [main]
  [admin-basic
   [header-common admin-nav]
   [admin-sidebar]
   main])

(defn admin-post-edit [menu main]
  [admin-basic
   [header-common menu]
   nil
   main])

(defn home-nav []
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

(defn home-basic
  ([main] (home-basic nil main nil))
  ([main footer] (home-basic nil main footer))
  ([header main footer]
   (fn [header main footer]
     [:div
      [:div header]
      [:div main]
      [:div footer]])))


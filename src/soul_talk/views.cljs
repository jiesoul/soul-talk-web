(ns soul-talk.views
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.routes :refer [logged-in? navigate!]]
            [soul-talk.pages.common :refer [loading-throber error-modal success-modal]]
            [soul-talk.pages.home :refer [home-page]]
            [soul-talk.pages.admin :refer [main-component]]
            [soul-talk.pages.auth :refer [login-page register-page]]
            [soul-talk.pages.users :refer [users-page change-pass-page user-profile-page]]
            [soul-talk.pages.post :refer [posts-page post-view-page edit-post-page post-archives-page add-post-page]]
            [soul-talk.pages.category :as category]
            [soul-talk.pages.tag :as tag]
            [clojure.string :as str]
            [antd :as antd]
            [soul-talk.pages.header :refer [header-component admin-menu-component]]))


(defn admin-sidebar-link [url title page & icon]
  (let [active-page (subscribe [:active-page])]
    [:li.nav-item
     [:a.nav-link
      {:href  url
       :class (when (= page @active-page)
                "active")}
      (if icon
        [:i {:class icon}])
      " " title]]))

(defn admin-sidebar []
  (fn []
    (r/with-let [user (subscribe [:user])]
      (when @user
        [:> antd/Menu {:mode "inline"
                       :default-select-keys ["1"]
                       :default-open-keys ["blog"]
                       :style {:height "100%"
                               :border-right 0}}
         [:> antd/Menu.Item {:key "1"} "面板"]
         [:> antd/Menu.SubMenu {:key   "blog"
                                :title "博客管理"}
          [:> antd/Menu.Item {:key "category"
                              :on-click (dispatch [:categories])}
           "分类"]
          [:> antd/Menu.Item {:key "post"} "文章"]]

         [:> antd/Menu.SubMenu {:key "1"
                                :title "用户"}
          [:> antd/Menu.Item {:key "profile"} "个人信息"]
          [:> antd/Menu.Item {:key "password"} "密码修改"]
          ]]))))




;;多重方法  响应对应的页面
(defmulti pages (fn [page _] page))

;;页面
(defmethod pages :home [_ _] [home-page])
(defmethod pages :login [_ _] [login-page])
(defmethod pages :register [_ _] [register-page])
(defmethod pages :posts/archives [_ _] [post-archives-page])
(defmethod pages :posts/view [_ _] [post-view-page])


(defn admin-page [page]
  (r/with-let [user (subscribe [:user])]
    (if @user
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
                   :padding 24
                   :margin 0
                   :min-height 280}}
          [page]]]]]
      (pages :login nil))))

;;后台页面
(defmethod pages :admin [_ _]
  (admin-page main-component))

(defmethod pages :change-pass [_ _]
  (admin-page change-pass-page))

(defmethod pages :user-profile [_ _]
  (admin-page user-profile-page))

(defmethod pages :users [_ _]
  (admin-page users-page))

(defmethod pages :categories [_ _]
  (admin-page category/categories-page))

(defmethod pages :categories/add [_ _]
  (admin-page category/edit-page))

(defmethod pages :categories/edit [_ _]
  (admin-page category/edit-page))

(defmethod pages :posts [_ _]
  (admin-page posts-page))

(defmethod pages :posts/add [_ _]
  (r/with-let [user (subscribe [:user])]
              (if @user
                [edit-post-page]
                (pages :login nil))))

(defmethod pages :posts/edit [_ _]
  (r/with-let [user (subscribe [:user])]
    (js/console.log "posts edit")
              (if @user
                [edit-post-page]
                (pages :login nil))))

(defmethod pages :tags/add [_ _]
  (admin-page tag/add-page))

(defmethod pages :default [_ _] [:div])

;; 根据配置加载不同页面
(defn main-page []
  (r/with-let [active-page (subscribe [:active-page])
               user (subscribe [:user])]
    [:div
     [loading-throber]
     [success-modal]
     ;[error-modal]
     (pages @active-page @user)]))
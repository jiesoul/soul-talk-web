(ns soul-talk.views
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.routes :refer [logged-in? navigate!]]
            [soul-talk.pages.common :refer [loading-modal error-modal success-modal spin-loading]]
            [soul-talk.pages.home :refer [home-page]]
            [soul-talk.pages.admin :refer [main-component]]
            [soul-talk.pages.auth :refer [login-page register-page]]
            [soul-talk.pages.users :refer [users-page change-pass-page user-profile-page]]
            [soul-talk.pages.post :refer [posts-page post-view-page edit-post-page post-archives-page add-post-page]]
            [soul-talk.pages.category :as category]
            [soul-talk.pages.tag :as tag]
            [clojure.string :as str]
            [antd :as antd]
            [soul-talk.pages.admin :refer [admin-page-component main-component]]))

;;多重方法  响应对应的页面
(defmulti pages (fn [page _] page))

;;页面
(defmethod pages :home [_ _] [home-page])
(defmethod pages :login [_ _] [login-page])
(defmethod pages :register [_ _] [register-page])
(defmethod pages :posts/archives [_ _] [post-archives-page])
(defmethod pages :posts/view [_ _] [post-view-page])

(defn admin-page [page-component]
  (r/with-let [user (subscribe [:user])]
    (if @user
      [admin-page-component page-component]
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

(defmethod pages :categories-add [_ _]
  (js/console.log "views: load categories add page")
  (admin-page category/edit-page))

(defmethod pages :categories-edit [_ _]
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
    (if @user
      [edit-post-page]
      (pages :login nil))))

(defmethod pages :tags/add [_ _]
  (admin-page tag/add-page))

(defmethod pages :default [_ _] [:div "页面未找到"])

;; 根据配置加载不同页面
(defn main-page []
  (r/with-let [active-page (subscribe [:active-page])]
    [:div
     ;[loading-modal]
     [success-modal]
     [error-modal]
     [:div
      (pages @active-page nil)]]))
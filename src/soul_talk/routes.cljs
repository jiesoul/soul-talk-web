(ns soul-talk.routes
  (:require [goog.events :as events]
            [secretary.core :as secretary :refer-macros [defroute]]
            [accountant.core :as accountant]
            [reagent.core :as r]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]])
  (:import [goog History]
           [goog.History EventType]))


;; 判断是否登录
(defn logged-in? []
  @(subscribe [:user]))

(defn context-url [url]
  (str url))

(defn href [url]
  {:href (str url)})

(defn navigate! [url]
  (accountant/navigate! (context-url url)))

;; 加载多个事件
(defn run-events
  [events]
  (doseq [event events]
    (dispatch event)))

(defn run-events-admin
  [events]
  (doseq [event events]
    (if (logged-in?)
      (dispatch event)
      (dispatch [:add-login-event event]))))


(defn home-page-events [& events]
  (.scrollTo js/window 0 0)
  (run-events (into
                [[:load-categories]
                 [:load-tags]
                 [:set-active-page :home]]
                events)))

;; 首页
(defroute "/" []
  (run-events
    [[:load-posts {:page 1 :pre-page 3}]
     [:set-active-page :home]]))



(defroute "/blog" []
  (let [pagination {:page     1
                    :pre-page 20}]
    (run-events
      [[:load-posts pagination]
       [:load-posts-archives]
       [:set-active-page :blog]])))

(defroute "/blog/archives/:year/:month" [year month]
  (run-events [[:load-posts-archives-year-month year month]
               [:load-posts-archives]
               [:set-active-page :blog/archives]]))

(defroute "/login" []
  (run-events [[:set-active-page :login]]))

(defroute "/register" []
  (run-events [[:set-active-page :register]]))

;; 无登录下把事件加入登录事件
(defn admin-page-events [& events]
  (.scrollTo js/window 0 0)
  (run-events-admin (into
                      [[:set-active-page :admin]]
                      events)))

;; 后台管理
(defroute admin "/admin" []
  (run-events [[:set-breadcrumb ["面板"]]
               [:set-active-page :admin]]))

(defroute "/change-pass" []
  (run-events [[:set-breadcrumb ["个人管理" "修改密码"]]
               [:set-active-page :change-pass]]))

(defroute "/user-profile" []
  (run-events [[:set-breadcrumb ["个人管理" "个人信息"]]
               [:set-active-page :user-profile]]))

(defroute "/users" []
  (run-events [[:set-breadcrumb ["用户" "清单"]]
               [:admin/load-users]
               [:set-active-page :users]]))

(defroute "/categories" []
  (run-events [[:set-breadcrumb ["分类" "清单"]]
               [:load-categories]
               [:set-active-page :categories]]))

(defroute "/category/edit/" []
  (run-events [[:close-category]
               [:set-breadcrumb ["分类" "新增"]]
               [:set-active-page :categories-add]]))

(defroute "/category/edit/:id" [id]
  (run-events [[:close-category]
               [:set-breadcrumb ["分类" "编辑"]]
               [:load-category id]
               [:set-active-page :categories-edit]]))

(defroute "/category/view/:id" [id]
  (run-events [[:load-category id]
               [:set-active-page :category-view]]))


(defroute "/posts" []
  (run-events [[:admin/load-posts]
               [:set-breadcrumb ["文章" "列表"]]
               [:set-active-page :posts]]))



(defroute "/posts/add" []
  (r/with-let [user (subscribe [:user])]
    (run-events [[:load-categories]
                 [:load-tags]
                 [:set-active-page :posts/add]])))

(defroute "/posts/:id/edit" [id]
  (if-not (or (logged-in?)
            (nil? @(subscribe [:post])))
    (navigate! "/login")
    (run-events [[:load-post id]
                 [:load-categories]
                 [:set-active-page :posts/edit]])))

(defroute "/posts/:id" [id]
  (run-events [[:load-categories]
               [:load-post id]
               [:set-active-page :posts/view]]))

(defroute "*" []
  )

(secretary/set-config! :prefix "#")

;; 使用浏览器可以使用前进后退 历史操作
(defn hook-browser-navigation! []
  (doto
    (History.)
    (events/listen EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
    (.setEnabled true))
  (accountant/configure-navigation!
    {:nav-handler
                        (fn [path]
                          (secretary/dispatch! path))
     :path-exists?
                        (fn [path]
                          (secretary/locate-route path))
     :reload-same-path? true})
  (accountant/dispatch-current!))
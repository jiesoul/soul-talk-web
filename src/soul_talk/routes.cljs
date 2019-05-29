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

(defn href [url]
  {:href (str url)})

(defn navigate! [url]
  (accountant/navigate! url))

;; 加载多个事件
(defn run-events
  [events]
  (doseq [event events]
    (dispatch event)))

(defn run-events-admin
  [events]
  (js/console.log events)
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


(secretary/set-config! :prefix "#")

;; 首页
(defroute "/" []
  (let [pagination {:page     1
                    :pre-page 3}]
    (home-page-events
      [[:load-posts pagination]
       [:load-posts-archives]])))

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
  (run-events [[:set-active-page :admin]]))

(defroute change-pass "/change-pass" []
  (run-events [[:set-active-page :change-pass]]))

(defroute user-profile "/user-profile" []
  (run-events [[:set-active-page :user-profile]]))

(defroute users "/users" []
  (run-events [[:admin/load-users]
               [:set-active-page :users]]))

(defroute categories "/categories" []
  (js/console.log "load category list page")
  (run-events [[:set-breadcrumb ["Blog" "Category" "List"]]
               [:load-categories]
               [:set-active-page :categories]]))

(defroute categroies-add "/categories-add" []
  (run-events
    [[:close-category]
     [:set-breadcrumb ["Blog" "Category" "Add"]]
     [:set-active-page :categories-add]]))

(defroute categories-edit "/categories/:id/edit" [id]
  (run-events [[:close-category]
               [:set-breadcrumb ["Blog" "Category" "Edit"]]
               [:load-category id]
               [:set-active-page :categories-edit]]))

(defroute category "/categories/:id" [id]
  (run-events [[:load-category id]
               [:set-active-page :category-view]]))


(defroute posts "/posts" []
  (run-events [[:admin/load-posts]
               [:set-active-page :posts]]))

(defroute "/posts/archives/:year/:month" [year month]
  (run-events [[:load-posts-archives-year-month year month]
               [:set-active-page :posts/archives]]))

(defroute "/posts/add" []
  (r/with-let [user (subscribe [:user])
               post {:publish 0
                     :author  (:name @user)}]
    (run-events [[:load-categories]
                 [:load-tags]
                 [:init-post post]
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
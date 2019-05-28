(ns soul-talk.handlers
  (:require [re-frame.core :refer [inject-cofx dispatch dispatch-sync reg-event-db reg-event-fx subscribe]]
            [soul-talk.db :refer [default-db]]
            [soul-talk.local-storage :as storage]
            soul-talk.handler.posts
            soul-talk.handler.errors
            soul-talk.handler.auth
            soul-talk.handler.admin
            soul-talk.handler.users
            soul-talk.handler.category
            soul-talk.handler.tag
            soul-talk.handler.files))

;; 初始化
(reg-event-fx
  :initialize-db
  [(inject-cofx :local-store "soul-talk-login-user")]
  (fn [cofx _]
    (let [val (:local-store cofx)
          db (:db cofx)]
      (js/console.log val)
      (js/console.log db)
      (js/console.log default-db)
      {:db (merge db (assoc default-db :user (js->clj val :keywordize-keys true)))})))

;; 设置当前页
(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :active-page page)))

(reg-event-db
  :set-breadcrumb
  (fn [db [_ breadcrumb]]
    (assoc db :breadcrumb breadcrumb)))

(reg-event-fx
  :navigate-to
  (fn [_ [_ url]]
    {:navigate url}))

(reg-event-db
  :set-success
  (fn [db [_ message]]
    (assoc db :success message)))

;; 取消加载
(reg-event-db
  :unset-loading
  (fn [db _]
    (dissoc db :loading? :error :should-be-loading?)))

;; 设置加载为 true
(reg-event-db
  :set-loading-for-real-this-time
  (fn [{:keys [should-be-loading?] :as db} _]
    (if should-be-loading?
      (assoc db :loading? true)
      db)))

;; 设置加载
(reg-event-fx
  :set-loading
  (fn [{db :db} _]
    {:dispatch-later [{:ms 100 :dispatch [:set-loading-for-real-this-time]}]
     :db             (-> db
                       (assoc :should-be-loading? true)
                       (dissoc :error))}))
(ns soul-talk.handler.auth
  (:require [soul-talk.db :as db]
            [soul-talk.auth-validate :refer [login-errors reg-errors]]
            [re-frame.core :refer [reg-event-fx reg-event-db dispatch]]
            [ajax.core :refer [POST GET DELETE PUT]]))


;; 运行 login
(reg-event-fx
  :run-login-events
  (fn [{{events :login-events :as db} :db} _]
    {:dispatch-n events
     :db         db}))

;; 添加login
(reg-event-db
  :add-login-event
  (fn [db [_ event]]
    (update db :login-events conj event)))

(reg-event-db
  :handle-login-error
  (fn [db {:keys [error]}]
    (assoc db :error error)))

;; 处理login ok
(reg-event-fx
  :handle-login-ok
  (fn [{:keys [db]} [_ {:keys [user token]}]]
    {:db         (assoc db :user user :auth-token token)
     :dispatch-n (list
                   [:run-login-events]
                   [:set-active-page :admin]
                   [:navigate-to "/#/admin"])
     :set-user! user}))

;; login
(reg-event-fx
  :login
  (fn [_ [_ {:keys [email password] :as user}]]
    (if-let [error (login-errors user)]
      {:dispatch [:set-error (first error)]}
      {:http {:method        POST
              :url           "/api/login"
              :ajax-map      {:params {:email    email
                                       :password password}}
              :success-event [:handle-login-ok]}})))


;; 处理register ok
(reg-event-fx
  :handle-register
  (fn [{:keys [db]} [_ {:keys [user]}]]
    (js/alert "register ok")
    {:dispatch-n (list
                   [:set-active-page :admin])
     :db         (assoc db :user user)}))

;; register
(reg-event-fx
  :register
  (fn [_ [_ {:keys [email password pass-confirm] :as user}]]
    (if-let [error (reg-errors user)]
      {:dispatch [:set-error (first error)]}
      {:http {:method        POST
              :url           "/api/register"
              :ajax-map      {:params {:email        email
                                       :password     password
                                       :pass-confirm pass-confirm}}
              :success-event [:handle-register]}})))

(reg-event-fx
  :handle-logout
  (fn [_ _]
    {:dispatch-n (list
                   [:set-active-page :login]
                   [:navigate-to "/#/login"])}))

(reg-event-fx
  :logout
  (fn [_ _]
    {:http      {:method               POST
                 :url                  "/api/logout"
                 :ignore-response-body true
                 :success-event        [:handle-logout]
                 :error-event          [:handle-logout]}
     :db db/default-db
     :set-user! nil}))

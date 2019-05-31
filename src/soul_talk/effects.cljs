(ns soul-talk.effects
  (:require [re-frame.core :as rf :refer [dispatch reg-fx reg-event-fx]]
            [accountant.core :as accountant]
            [soul-talk.local-storage :as storage]))
(def api-url "http://localhost:3001")

(reg-fx
  :http
  (fn [{:keys [method
               url
               success-event
               error-event
               ignore-response-body
               ajax-map]
        :or {error-event [:ajax-error]
             ajax-map {}}}]
    (dispatch [:set-loading])
    (method (str api-url url) (merge
                  {:handler       (fn [response]
                                    (js/console.log "server response message: " response)
                                    (when success-event
                                      (dispatch (if ignore-response-body
                                                  success-event
                                                  (conj success-event response))))
                                    (dispatch [:unset-loading]))
                   :error-handler (fn [error]
                                    (dispatch (conj error-event error))
                                    (dispatch [:unset-loading]))}
                  ajax-map))))


(reg-fx
  :navigate
  (fn [url]
    (accountant/navigate! url)))

(reg-fx
  :reload-page
  (fn [_]
    (accountant/dispatch-current!)))

(reg-fx
  :set-user!
  (fn [user-identity]
    (storage/set-item! storage/login-user-key user-identity)))

(reg-fx
  :set-auth-token!
  (fn [auth-token]
    (storage/set-item! storage/auth-token-key auth-token)))
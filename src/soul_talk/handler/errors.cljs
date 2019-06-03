(ns soul-talk.handler.errors
  (:require [re-frame.core :refer [dispatch dispatch-sync reg-event-db reg-event-fx]]))

(reg-event-fx
  :ajax-error
  (fn [db [_ {:keys [response status] :as resp}]]
    (js/console.log "error response: " resp)
    (js/console.log "response status: " status)
    (let [error (condp = status
                  403 (:message response)
                  500 "内部错误"
                  400 (:message response)
                  "未知错误")]
      (js/console.log "error: " error)
      {:dispatch-n (list [:set-error error])})))


(reg-event-db
  :set-error
  (fn [db [_ error]]
    (assoc db :error error)))

(reg-event-db
  :clean-error
  (fn [db _]
    (dissoc db :error)))
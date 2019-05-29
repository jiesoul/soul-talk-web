(ns soul-talk.handler.errors
  (:require [re-frame.core :refer [dispatch dispatch-sync reg-event-db reg-event-fx]]))

(reg-event-db
  :ajax-error
  (fn [db [_ {:keys [response status] :as resp}]]
    (assoc db :error (condp = status
                       403 (:message response)
                       500 "内部错误"
                       "未知错误"))))


(reg-event-db
  :set-error
  (fn [db [_ error]]
    (assoc db :error error)))

(reg-event-db
  :clean-error
  (fn [db _]
    (dissoc db :error)))
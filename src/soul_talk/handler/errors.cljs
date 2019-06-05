(ns soul-talk.handler.errors
  (:require [re-frame.core :refer [dispatch dispatch-sync reg-event-db reg-event-fx]]
            [taoensso.timbre :as log]))

(reg-event-fx
  :ajax-error
  (fn [db [_ {:keys [response status] :as resp}]]
    (log/error "ajax error: " resp)
    {:dispatch-n (list [:set-error (:message response)]
                   (when (= status 401)
                     [:logout]))}))


(reg-event-db
  :set-error
  (fn [db [_ error]]
    (assoc db :error error)))

(reg-event-db
  :clean-error
  (fn [db _]
    (dissoc db :error)))
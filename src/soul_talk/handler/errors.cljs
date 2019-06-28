(ns soul-talk.handler.errors
  (:require [re-frame.core :refer [dispatch dispatch-sync reg-event-db reg-event-fx]]
            [taoensso.timbre :as log]))

(reg-event-fx
  :ajax-error
  (fn [db [_ {:keys [response status status-text] :as resp}]]
    {:dispatch-n (condp = status
                   0 [:set-error status-text]
                   401 (list [:set-error "验证过期，请重新登录"] [:logout])
                   [:set-error (:message response)])}))


(reg-event-db
  :set-error
  (fn [db [_ error]]
    (assoc db :error error)))

(reg-event-db
  :clean-error
  (fn [db _]
    (dissoc db :error)))
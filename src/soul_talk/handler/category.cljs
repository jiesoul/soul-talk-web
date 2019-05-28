(ns soul-talk.handler.category
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [ajax.core :refer [POST GET DELETE PUT]]
            [clojure.string :as str]))

(reg-event-db
  :set-categories
  (fn [db [_ {:keys [categories]}]]
    (assoc db :categories categories)))

(reg-event-fx
  :load-categories
  (fn [_ _]
    {:http {:method        GET
            :url           "/api/categories"
            :success-event [:set-categories]}}))

(reg-event-db
  :close-category
  (fn [db _]
    (dissoc db :category)))

(reg-event-db
  :categories/add-ok
  (fn [db [_ {:keys [category]}]]
    (assoc db :success (str "Add " (:name category) " successful!"))))

(reg-event-fx
  :categories/add
  (fn [_ [_ {:keys [name] :as category}]]
    (if (str/blank? name)
      {:dispatch [:set-error "名称不能为空"]}
      {:http {:method POST
              :url    "/api/admin/categories/"
                      :ajax-map {:params category}
                      :success-event [:categories/add-ok]
                      :error-event #(js/alert "发生错误请重试")}})))

(reg-event-db
  :set-category
  (fn [db [_ {:keys [category]}]]
    (assoc db :category category)))

(reg-event-db
  :update-category
  (fn [db [_ key value]]
    (assoc-in db [:category key] value)))

(reg-event-fx
  :load-category
  (fn [_ [_ id]]
    {:http {:method        GET
            :url           (str "/api/categories/" id)
            :success-event [:set-category]}}))

(reg-event-db
  :categories/edit-ok
  (fn [db [_ _]]
    (-> db
      (assoc :success "update successful"))))

(reg-event-fx
  :categories/edit
  (fn [_ [_ {:keys [name] :as category}]]
    (if (str/blank? name)
      {:dispatch [:set-error "名称不能为空"]}
      {:http {:method        PUT
              :url           "/api/admin/categories/"
              :ajax-map      {:params category}
              :success-event [:categories/edit-ok]}})))

(reg-event-db
  :categories/delete-ok
  (fn [db _]
    (assoc db :success "delete successful!")))

(reg-event-db
  :categories/delete-error
  (fn [db [_ {:keys [response]}]]
    (assoc db :error (:message response))))

(reg-event-fx
  :categories/delete
  (fn [_ [_ id]]
    {:http {:method        DELETE
            :url           (str "/api/admin/categories/" id)
            :success-event [:categories/delete-ok]}}))

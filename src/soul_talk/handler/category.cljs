(ns soul-talk.handler.category
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]
            [ajax.core :refer [POST GET DELETE PUT]]
            [clojure.string :as str]
            [soul-talk.db :refer [api-uri]]))

(reg-event-db
  :set-categories
  (fn [db [_ {:keys [categories]}]]
    (assoc db :categories categories)))

(reg-event-fx
  :load-categories
  (fn [_ _]
    {:http {:method        GET
            :url           (str api-uri "/categories")
            :success-event [:set-categories]}}))

(reg-event-db
  :close-category
  (fn [db _]
    (dissoc db :category)))

(reg-event-fx
  :categories/add-ok
  (fn [{:keys [db]} [_ {:keys [category]}]]
    {:db         (assoc db [:categories] conj category)
     :dispatch-n (list [:set-success "保存成功"])}))

(reg-event-fx
  :categories/add
  (fn [_ [_ {:keys [name] :as category}]]
    (if (str/blank? name)
      {:dispatch [:set-error "名称不能为空"]}
      {:http {:method        POST
              :url           (str api-uri "/admin/categories/")
              :ajax-map      {:params category}
              :success-event [:categories/add-ok]}})))

(reg-event-db
  :set-category
  (fn [db [_ {:keys [category]}]]
    (assoc db :category category)))

(reg-event-fx
  :load-category
  (fn [_ [_ id]]
    {:http {:method        GET
            :url           (str api-uri "/categories/" id)
            :success-event [:set-category]}}))

(reg-event-fx
  :categories/edit-ok
  (fn [_ _]
    {:dispatch-n (list [:set-success "保存成功"]
                   [:load-categories])}))

(reg-event-fx
  :categories/edit
  (fn [_ [_ {:keys [name] :as category}]]
    (if (str/blank? name)
      {:dispatch [:set-error "名称不能为空"]}
      {:http {:method        PUT
              :url           (str api-uri "/admin/categories/")
              :ajax-map      {:params category}
              :success-event [:categories/edit-ok]}})))

(reg-event-fx
  :categories/delete-ok
  (fn [db _]
    {:dispatch-n (list [:set-success "删除成功"]
                   [:load-categories])}))

(reg-event-db
  :categories/delete-error
  (fn [db [_ {:keys [response]}]]
    (assoc db :error (:message response))))

(reg-event-fx
  :categories/delete
  (fn [_ [_ id]]
    {:http {:method        DELETE
            :url           (str api-uri "/admin/categories/" id)
            :success-event [:categories/delete-ok]}}))

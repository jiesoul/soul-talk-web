(ns soul-talk.handler.posts
  (:require [re-frame.core :refer [reg-event-fx reg-event-db subscribe]]
            [ajax.core :refer [POST GET DELETE PUT]]
            [soul-talk.db :refer [api-uri]]))

(reg-event-db
  :set-posts
  (fn [db [_ {:keys [posts pagination]}]]
    (assoc db :posts posts
              :pagination pagination)))

(reg-event-fx
  :load-posts
  (fn [_ [_ pagination]]
    {:http {:method        GET
            :url           (str api-uri "/posts")
            :ajax-map      {:params pagination}
            :success-event [:set-posts]}}))

(reg-event-db
  :close-posts
  (fn [db _]
    (dissoc db :posts)))

(reg-event-db
  :admin/set-posts
  (fn [db [_ {:keys [posts pagination]}]]
    (assoc db :admin/posts posts
              :admin/pagination pagination)))

(reg-event-fx
  :admin/load-posts
  (fn [_ [_ pagination]]
    {:http {:method        GET
            :url           (str api-uri "/admin/posts")
            :ajax-map      {:params pagination}
            :success-event [:admin/set-posts]}}))

(reg-event-db
  :admin/close-posts
  (fn [db _]
    (dissoc db :admin/posts)))

(reg-event-fx
  :posts/add-ok
  (fn [{:keys [db]} [_ {:keys [post]}]]
    {:dispatch-n (list [:set-success "保存成功"])}))

(reg-event-fx
  :posts/add
  (fn [_ [_ post]]
    {:http {:method        POST
            :url           (str api-uri "/admin/posts")
            :ajax-map      {:params post}
            :success-event [:posts/add-ok post]}}))

(reg-event-fx
  :posts/upload-ok
  (fn [{:keys [db]} [_ {:keys [id] :as post}]]
    {:db (-> db
           )
     :dispatch-n (list [:set-success "保存成功"])}))

(reg-event-db
  :posts/upload-error
  (fn [_ [_ {:keys [message]}]]
    (js/alert message)))

(reg-event-fx
  :posts/upload
  (fn [_ [_ files]]
    (let [data (doto
                 (js/FormData.)
                 (.append "file" files))]
      {:http
       {:method   POST
        :url               (str api-uri "/admin/posts/upload")
        :ajax-map          {:body data}
        :success-event [:posts/upload-ok]
        :error-event [:posts/upload-error]}})))

(reg-event-fx
  :posts/edit-ok
  (fn [_ _]
    {:dispatch-n (list [:set-success "保存成功"]
                   [:admin/load-posts])}))

(reg-event-fx
  :posts/edit-error
  (fn [_ [_ {:keys [response]}]]
    {:dispatch [:set-error (:message response)]}))

(reg-event-fx
  :posts/edit
  (fn [_ [_ {:keys [id counter] :as post}]]
    {:http {:method        PUT
            :url           (str api-uri "/admin/posts/" id)
            :ajax-map      {:params post}
            :success-event [:posts/edit-ok]
            :error-event   [:posts/edit-error]}}))

(reg-event-db
  :set-post
  (fn [db [_ {post :post}]]
    (assoc db :post post)))

(reg-event-fx
  :load-post
  (fn [_ [_ id]]
    {:http {:method        GET
            :url           (str api-uri "/posts/" id)
            :success-event [:set-post]}}))

(reg-event-db
  :close-post
  (fn [db _]
    (dissoc db :post)))

(reg-event-fx
  :posts/delete-ok
  (fn [{:keys [db]} [_ id]]
    (let [posts (get db :admin/posts)
          posts (remove #(= id (:id %)) posts)]
      {:dispatch [:set-success "删除成功"]
       :db       (assoc db :admin/posts posts)})))

(reg-event-db
  :posts/delete-error
  (fn [_ _]
    (js/alert "delete fail")))

(reg-event-fx
  :posts/delete
  (fn [_ [_ id]]
    {:http {:method        DELETE
            :url           (str api-uri "/admin/posts/" id)
            :success-event [:posts/delete-ok id]
            :error-event   [:posts/delete-error]}}))

(reg-event-fx
  :posts/publish-ok
  (fn [_ _]
    {:dispatch-n (list [:set-success "发布成功"]
                        [:admin/load-posts])}))

(reg-event-fx
  :posts/publish-error
  (fn [_ _]
    (js/alert "publish failed")))

(reg-event-fx
  :posts/publish
  (fn [_ [_ id]]
    {:http {:method PUT
            :url (str api-uri "/admin/posts/" id "/publish")
            :success-event [:posts/publish-ok]
            :error-event [:posts/publish-error]}}))

(reg-event-db
  :set-posts-archives
  (fn [db [_ {:keys [archives]}]]
    (assoc db :posts-archives archives)))

(reg-event-fx
  :load-posts-archives
  (fn [_ _]
    {:http {:method        GET
            :url           (str api-uri "/posts/archives")
            :success-event [:set-posts-archives]}}))

(reg-event-db
  :set-posts-archives-year-month
  (fn [db [_ {:keys [posts]}]]
    (assoc db :posts posts)))

(reg-event-fx
  :load-posts-archives-year-month
  (fn [_ [_ year month]]
    {:http {:method        GET
            :url           (str api-uri "/posts/archives/" year "/" month)
            :success-event [:set-posts-archives-year-month]}}))
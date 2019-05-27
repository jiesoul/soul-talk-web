(ns soul-talk.pages.category
  (:require [soul-talk.pages.common :as c]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as r]
            [antd :as antd]))

(defn columns []
  [{:title "name" :dataIndex "name", :key "name", :align "center"}
   {:title  "操作" :dataIndex "tags", :align "center"
    :render #(r/as-element
               [:div
                [:> antd/Button {:icon     "delete"
                                 :type     "danger"
                                 :on-click (fn []
                                             (let [id ()]
                                               (dispatch [:categories/delete])))}]
                [:> antd/Divider {:type "vertical"}]
                [:> antd/Button {:icon     "edit"
                                 :on-click (fn []
                                             (dispatch [:navigate-to (str
                                                                       "#/categories/"
                                                                       (:id (js->clj %2 :keywordize-keys true))
                                                                       "/edit")]))}]])}])

(defn categories-list []
  (r/with-let [categories (subscribe [:categories])
               user (subscribe [:user])
               confirm-open? (r/atom false)]
    (when @user
      (js/console.log "load ok categories: " (clj->js @categories))
      [:div
       [:> antd/Table {:bordered   true
                       :columns    (clj->js (columns))
                       :dataSource (clj->js @categories)
                       :row-key    "id"}]])))

(defn categories-page []
  (r/with-let [user (subscribe [:user])]
    (fn []
      (when @user
        [:> antd/Row
         [:> antd/Col {:offset "20"}
          [:> antd/Button
           {:type     "primary"
            :icon     "plus"
            :on-click #(dispatch [:navigate-to "#/categories-add"])}
           "添加"]]
         [:> antd/Divider]
         [categories-list]]))))

(defn edit-page []
  (r/with-let [ori-category (subscribe [:category])
               error    (subscribe [:error])]
    (fn []
      (let [category (-> @ori-category
                       (update :name #(or % ""))
                       r/atom)
            name     (r/cursor category [:name])]
        [:> antd/Row
         [:> antd/Form
          [:> antd/Input
           {:defaultValue @name
            :on-change #(reset! name (-> % .-target .-value))}]
          (when @error
            [:div.alert.alert-danger.smaller @error])]
         [:> antd/Row {:style {:margin-top "10px"}}
          [:div
           [:> antd/Button
            {:type     "cancel"
             :on-click #(dispatch [:navigate-to "#/categories"])}
            "返回"]
           [:> antd/Button
            {:type     "primary"
             :on-click #(if @ori-category
                          (dispatch [:categories/edit @category])
                          (dispatch [:categories/add @category]))}
            "保存"]]]]))))


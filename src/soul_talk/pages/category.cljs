(ns soul-talk.pages.category
  (:require [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as r]
            [antd :as antd]
            [soul-talk.layouts.basic-layout :refer [basic-layout]]
            [soul-talk.components.common :as c]))

(defn columns []
  [{:title "name" :dataIndex "name", :key "name", :align "center"}
   {:title  "操作" :dataIndex "tags", :align "center"
    :render (fn [_ category index]
              (let [{:keys [id name]} (js->clj category :keywordize-keys true)]
                (r/as-element
                  [:div
                   [:> antd/Button {:icon     "delete"
                                    :type     "danger"
                                    :on-click (fn []
                                                (c/show-confirm
                                                  "分类删除"
                                                  (str "你确认要删除分类 " name " 吗？")
                                                  #(dispatch [:categories/delete id])
                                                  #(js/console.log "cancel")))}]
                   [:> antd/Divider {:type "vertical"}]
                   [:> antd/Button {:icon "edit"
                                    :on-click #(dispatch [:navigate-to
                                                          (str "#/categories/" id "/edit")])}]])))}])

(defn categories-list []
  (r/with-let [categories (subscribe [:categories])]
    (fn []
      [:div
       [:> antd/Table {:bordered   true
                       :columns    (clj->js (columns))
                       :dataSource (clj->js @categories)
                       :row-key    "id"
                       :size       "small"}]])))

(defn categories-page []
  [basic-layout
   [:div
    [c/breadcrumb-component ["分类" "列表"]]
    [:> antd/Layout.Content {:className "main"}
     [:> antd/Button
      {:type     "primary"
       :icon     "plus"
       :size     "small"
       :on-click #(dispatch [:navigate-to "#/categories-add"])}
      "添加"]
     [:> antd/Divider]
     [categories-list]]]])

(defn edit-page []
  (r/with-let [category (subscribe [:category])
               error    (subscribe [:error])
               name (r/cursor category [:name])
               id (r/cursor category [:id])]
    (fn []
      [basic-layout
       [:div
        [c/breadcrumb-component ["分类" "编辑"]]
        [:> antd/Layout.Content
         {:style {:background "#fff"
                  :padding    24
                  :margin     0
                  :min-height 280}}

         [:> antd/Form
          [:> antd/Input
           {:value     @name
            :on-change #(let [new-value (.-target.value %)]
                          (dispatch [:update-value [:category :name] new-value]))}]
          (when @error
            [:div
             [:> antd/Alert {:message @error :type "error"}]])]
         [:> antd/Row {:style {:margin-top "10px"}}
          [:div
           [:> antd/Button
            {:type     "cancel"
             :size  "small"
             :on-click #(dispatch [:navigate-to "#/categories"])}
            "返回"]
           [:> antd/Button
            {:type     "primary"
             :size "small"
             :on-click #(if (nil? @id)
                          (dispatch [:categories/add @category])
                          (dispatch [:categories/edit @category]))}
            "保存"]]]]]])))




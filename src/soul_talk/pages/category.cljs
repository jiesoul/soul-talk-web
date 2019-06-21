(ns soul-talk.pages.category
  (:require [re-frame.core :refer [dispatch subscribe]]
            [soul-talk.routes :refer [navigate!]]
            [reagent.core :as r]
            [soul-talk.layouts.basic-layout :refer [basic-layout]]
            [soul-talk.components.common :as c]
            ))

(defn columns []
  [{:title "name" :dataIndex "name", :key "name", :align "center"}
   {:title  "操作" :dataIndex "tags", :align "center"
    :render (fn [_ category index]
              (let [{:keys [id name]} (js->clj category :keywordize-keys true)]
                (r/as-element
                  [:div
                   [:> js/antd.Button {:icon     "edit"
                                    :on-click #(navigate! (str "#/category/edit/" id))}]
                   [:> js/antd.Divider {:type "vertical"}]
                   [:> js/antd.Button {:icon     "delete"
                                    :type     "danger"
                                    :on-click (fn []
                                                (c/show-confirm
                                                  "分类删除"
                                                  (str "你确认要删除分类 " name " 吗？")
                                                  #(dispatch [:categories/delete id])
                                                  #(js/console.log "cancel")))}]
                   ])))}])

(defn categories-list []
  (r/with-let [categories (subscribe [:categories])]
    (fn []
      [:div
       [:> js/antd.Table {:bordered   true
                       :columns    (clj->js (columns))
                       :dataSource (clj->js @categories)
                       :row-key    "id"
                       :size       "small"}]])))

(defn categories-page []
  [basic-layout
   [:> js/antd.Layout.Content {:className "main"}
    [:div.table-operations
     [:> js/antd.Button
      {:type     "primary"
       :icon     "plus"
       :size     "small"
       :on-click #(navigate! "#/category/edit/")}
      "新增"]]
    [categories-list]]])

(defn edit-component [category]
  (let [id (r/cursor category [:id])
        name (r/cursor category [:name])]
    (fn [category]
      [basic-layout
       [:> js/antd.Layout.Content
        [:> js/antd.Row
         [:> js/antd.Col {:span 8 :offset 8}
          [:> js/antd.Input
           {:defaultValue @name
            :addon-before "名称："
            :type         :text
            :onChange     #(reset! name (.-target.value %))}]
          [:hr]
          [:div {:style {:text-align "center"}}
           [:> js/antd.Button
            {:type     "cancel"
             :size     "small"
             :on-click #(dispatch [:navigate-to "#/categories"])}
            "返回"]
           [:> js/antd.Divider {:type "vertical"}]
           [:> js/antd.Button
            {:type     "primary"
             :size     "small"
             :on-click #(if (nil? @id)
                          (dispatch [:categories/add @category])
                          (dispatch [:categories/edit @category]))}
            "保存"]]]]]])))

(defn add-page []
  (let [edit-category (-> {:name ""}
                        r/atom)]
    [edit-component edit-category]))

(defn edit-page []
  (r/with-let [category (subscribe [:category])]
    (when @category
      (fn []
        (let [edit-category (-> @category
                              r/atom)]
          [edit-component edit-category])))))




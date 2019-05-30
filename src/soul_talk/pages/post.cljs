(ns soul-talk.pages.post
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.pages.layout :refer [admin-default admin-header-main]]
            [soul-talk.pages.common :as c]
            [soul-talk.pages.header :refer [header-common]]
            [soul-talk.widgets.md-editor :refer [editor]]
            [cljs-time.format :as ft :refer [parse formatter]]
            [cljs-time.local :as local]
            [antd :as antd]))

(def custom-formatter (formatter "yyyy-MM-dd HH:mm"))

(defn archives-component []
  (r/with-let [posts-archives (subscribe [:posts-archives])]
    (fn []
      [:div.p-3
       [:h4.font-italic "Archives"]
       [:ol.list-unstyled.mb-0
        (for [{:keys [year month] :as archive} @posts-archives]
          ^{:key archive}
          [:li [:a {:href (str "/posts/archives/" year "/" month)} (str month " " year)]])]])))

(defn blog-post-component []
  (r/with-let [posts (subscribe [:posts])
               pagination (subscribe [:pagination])
               offset (r/cursor pagination [:offset])
               page (r/cursor pagination [:page])
               prev-page (r/cursor pagination [:previous])
               next-page (r/cursor pagination [:next])
               pre-page (r/cursor pagination[:pre-page])
               total-pages (r/cursor pagination [:total-pages])]

    (fn []
      [:> antd/Typography
       [:> antd/Row
        (for [{:keys [id title create_time author content] :as post} @posts]
          (let [url (str "/#/posts/" id)]
            ^{:key post} [:div.blog-post
                          [:> antd/Typography.Title
                           [:a.text-muted
                            {:href   url
                             :target "_blank"}
                            title]]
                          [:p.blog-post-meta (str (.toDateString (js/Date. create_time)) " by " author)]
                          [:> antd/Divider]
                          [:> antd/Typography.Paragraph
                           {:ellipsis {:rows 20
                                       :expandable false}}
                           [c/markdown-preview content]]
                          [:> antd/Col {:span 2 :offset 20}
                           [:> antd/Button
                            {:size "small"
                             :href url
                             :target "_blank"}
                            "全部"]]
                          [:> antd/Divider]]))
        [:> antd/Row
         [:> antd/Col {:span 2}
          [:> antd/Button
           {:type "link"
            :on-click #(dispatch [:load-posts {:page     @next-page
                                               :pre-page @pre-page}])
            :class    (if (>= @page @total-pages) "disabled")}
           "Older"]]
         [:> antd/Col {:span 2 :offset 20}
          [:> antd/Button
           {:type "link"
            :on-click #(dispatch [:load-posts {:page     @prev-page
                                               :pre-page @pre-page}])
            :class    (if (zero? @offset) "disabled")}
           "Newer"]]]]])))

(defn list-columns []
  [{:title "标题" :dataIndex "title", :key "title", :align "center"}
   {:title "创建时间" :dataIndex "create-time" :key "create-time" :align "center"}
   {:title "更新时间" :dataIndex "modify-time" :key "modify-time" :align "center"}
   {:title "发布状态" :dataIndex "publish" :key "publish" :align "center"}
   {:title "作者" :dataIndex "author" :key "author" :align "center"}
   {:title "浏览量" :dataIndex "counter" :key "counter" :align "center"}
   {:title  "操作" :dataIndex "actions" :key "actions" :align "center"
    :render (fn [_ post]
              (r/as-element
                (let [{:keys [id publish]} (js->clj post :keywordize-keys true)]
                  [:div
                   [:> antd/Button {
                                    :size "small"
                                    :target "_blank"
                                    :href (str "#/posts/" id)}
                    "查看"]
                   [:> antd/Divider {:type "vertical"}]
                   [:> antd/Button {:icon "edit"
                                    :size "small"
                                    :target "_blank"
                                    :href   (str "#/posts/" id "/edit")}]
                   [:> antd/Divider {:type "vertical"}]
                   [:> antd/Button {:type     "danger"
                                    :icon "delete"
                                    :size "small"
                                    :on-click (fn []
                                                (c/show-confirm
                                                  "文章删除"
                                                  (str "你确认要删除这篇文章吗？")
                                                  #(dispatch [:posts/delete id])
                                                  #(js/console.log "cancel")))}]
                   [:> antd/Divider {:type "vertical"}]
                   (when (zero? publish)
                     [:> antd/Button {:type     "primary"
                                      :size "small"
                                      :on-click #(dispatch [:posts/publish id])}
                      "发布"])])))}])

(defn posts-list []
  (r/with-let [posts (subscribe [:admin/posts])]
    ;(js/console.log (map #(update % :create_time (comp str (fn [date] (ft/parse custom-formatter date)))) @posts))
    [:div
     [:> antd/Table {:columns    (clj->js (list-columns))
                     :dataSource (clj->js @posts)
                     :row-key "id"
                     :bordered true
                     :size "small"}]]))

(defn posts-main []
  (fn []
    [:div
     [c/breadcrumb-component ["Home" "Post" "List"]]
     [:> antd/Layout.Content
      {:style {:background "#fff"
               :padding    12
               :margin     0
               :min-height 480}}
      [:> antd/Button
       {:target "_blank"
        :href   "#/posts/add"
        :size "small"}
       "写文章"]
      [:> antd/Divider]
      [posts-list]]]))

(defn posts-page []
  [admin-default
   posts-main])

(defn edit-menu []
  (r/with-let [post (subscribe [:post])
               categories (subscribe [:categories])
               post-id (r/cursor post [:id])
               category-id (r/cursor post [:category])]
    (fn []
      [:div {:style {:color "#FFF"}}
       [:> antd/Col {:span 2 :offset 2}
        (if @post-id "修改文章" "写文章")]
       [:> antd/Col {:span 4 :offset 6}
        [:> antd/Select {:value {:key @category-id}
                         :labelInValue true
                         :style        {:width 120}
                         :on-change    #(let [val (:key (js->clj % :keywordize-keys true))]
                                          (dispatch [:update-post :category val]))}
         [:> antd/Select.Option {:value ""} "选择分类"]
         (doall
           (for [{:keys [id name]} @categories]
             ^{:key id} [:> antd/Select.Option {:value id} name]))]]
       [:> antd/Col {:span 2}
        [:> antd/Button {:type     "primary"
                         :on-click #(if (nil? @post-id)
                                      (dispatch [:posts/add @post])
                                      (dispatch [:posts/edit @post]))}
         "保存"]]])))

(defn edit-post-main []
  (r/with-let [post       (subscribe [:post])
               title      (r/cursor post [:title])
               content     (r/cursor post [:content])
               user (subscribe [:user])]
    (fn []
      [:> antd/Layout.Content {:style {:backdrop-color "#fff"}}
       [:> antd/Col {:span 16 :offset 4 :style {:padding-top "10px"}}
        [:> antd/Form
         [:> antd/Input
          {:on-change   #(let [val (-> % .-target .-value)]
                           (dispatch [:update-value [:post :title] val]))
           :placeholder "请输入标题"
           :value       @title}]]
        [:> antd/Row
         [editor content [:post :content]]]]])))

(defn edit-post-page []
  [admin-header-main
   [header-common
    [edit-menu]]
   [edit-post-main]])


(defn post-view-page []
  (r/with-let [post (subscribe [:post])
               user (subscribe [:user])]
    (fn []
      (if @post
        [:> antd/Layout.Content {:style {:padding "50px"}}
         [:> antd/Typography.Title {:style {:text-align "center"}}
          (:title @post)]
         [:> antd/Divider]
         [:div.container
          [c/markdown-preview (:content @post)]]
         [:> antd/Divider ]]))))

(defn post-archives-page []
  (r/with-let [posts (subscribe [:posts])]
    (fn []
      [:div.container-fluid
       [:nav.navbar.navbar-expand-lg.navbar-light.bg-light
        [:a.navbar-brand
         {:href "#"} "Soul Talk"]
        [:div.container
         [:ul.navbar-nav
          [:li.nav-item.active
           [:a.nav-link
            {:href "#"}
            "文章"]]]]]
       [:div.container
        (doall
          (for [{:keys [id title create_time author] :as post} @posts]
            ^{:key post} [:div.blog-post
                          [:h2.blog-post-title
                           [:a.text-muted
                            {:href   (str "/posts/" id)
                             :target "_blank"}
                            title]]
                          [:p.blog-post-meta (str (.toDateString (js/Date. create_time)) " by " author)]
                          [:hr]]))]])))
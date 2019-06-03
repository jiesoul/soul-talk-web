(ns soul-talk.pages.post
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.pages.layout :refer [admin-default admin-header-main header-common logo]]
            [soul-talk.pages.common :as c]
            [soul-talk.widgets.md-editor :refer [editor]]
            [soul-talk.date-utils :as du]
            [antd :as antd]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [clojure.string :as str]))

(defn post-errors [post]
  (->
    (b/validate
      post
      :title [[v/required :message "标题不能为空\n"]]
      :category [[v/required :message "请选择一个分类\n"]]
      :content [[v/required :message "内容不能为空\n"]])
    first
    (vals)))

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
      [:> antd/Layout.Content
       (for [{:keys [id title create_time author content] :as post} @posts]
         (let [url (str "/#/posts/" id)]
           ^{:key post} [:> antd/Row
                         [:> antd/Typography
                          [:> antd/Typography.Title
                           [:a.text-muted
                            {:href   url
                             :target "_blank"}
                            title]]
                          [:p.blog-post-meta (str (.toDateString (js/Date. create_time)) " by " author)]
                          ;[:> antd/Divider]
                          [:> antd/Typography.Paragraph
                           {:ellipsis {:rows       20
                                       :expandable false}}
                           [c/markdown-preview content]
                           [:> antd/Col {:span 2}
                            [:> antd/Button
                             {:size   "small"
                              :href   url
                              :target "_blank"}
                             "全部"]]]
                          [:> antd/Divider]]]))
       [:> antd/Row
        [:> antd/Col {:span 2}
         [:> antd/Button
          {:type     "link"
           :on-click #(dispatch [:load-posts {:page     @next-page
                                              :pre-page @pre-page}])
           :class    (if (>= @page @total-pages) "disabled")}
          "Older"]]
        [:> antd/Col {:span 2 :offset 20}
         [:> antd/Button
          {:type     "link"
           :on-click #(dispatch [:load-posts {:page     @prev-page
                                              :pre-page @pre-page}])
           :class    (if (zero? @offset) "disabled")}
          "Newer"]]]])))

(defn list-columns []
  [{:title "标题" :dataIndex "title", :key "title", :align "center"}
   {:title  "创建时间" :dataIndex "create_time" :key "create_time" :align "center"
    :render (fn [_ post]
              (let [post (js->clj post :keywordize-keys true)]
                (du/to-date-time (:create_time post))))}
   {:title  "更新时间" :dataIndex "modify_time" :key "modify_time" :align "center"
    :render (fn [_ post]
              (let [post (js->clj post :keywordize-keys true)]
                (js/console.log (type (:create_time post)))
                (du/to-date-time (:modify_time post))))}
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
    (fn []
      [:div
       [:> antd/Table {:columns    (clj->js (list-columns))
                       :dataSource (clj->js @posts)
                       :row-key    "id"
                       :bordered   true
                       :size       "small"}]])))

(defn posts-page []
  (fn []
    [admin-default
     [:<>
      [c/breadcrumb-component ["文章" "列表"]]
      [:> antd/Layout.Content {:className "main"}
       [:> antd/Button
        {:target "_blank"
         :href   "#/posts/add"
         :size   "small"}
        "写文章"]
       [:> antd/Divider]
       [posts-list]]]]))

(defn edit-menu [edited-post]
  (r/with-let [post (subscribe [:post])
               categories (subscribe [:categories])]
    (fn [edited-post]
      (let [category (r/cursor edited-post [:category])]
        [:div {:style {:color "#FFF"}}
         [:> antd/Col {:span 2}
          (if @post "修改文章" "写文章")]
         [:> antd/Col {:span 4 :offset 16}
          [:> antd/Select {:value        {:key @category}
                           :labelInValue true
                           :style        {:width 120}
                           :on-change    #(let [val (:key (js->clj % :keywordize-keys true))]
                                            (reset! category val))}
           [:> antd/Select.Option {:value ""} "选择分类"]
           (doall
             (for [{:keys [id name]} @categories]
               ^{:key id} [:> antd/Select.Option {:value id} name]))]]
         [:> antd/Col {:span 2}
          [:> antd/Button {:type     "primary"
                           :on-click #(if-let [error (r/as-element (post-errors @edited-post))]
                                        (dispatch [:set-error error])
                                        (if @post
                                          (dispatch [:posts/edit @edited-post])
                                          (dispatch [:posts/add @edited-post])))}
           "保存"]]]))))


(defn post-edit-basic [post]
  (fn []
   [:> antd/Layout.Header
    [:> antd/Col {:span 2}
     [logo]]
    [:> antd/Col {:span 16 :offset 2}
     [edit-menu post]]] ))

(defn add-post-page []
  (r/with-let [user (subscribe [:user])]
    (fn []
      (let [edited-post (-> {:author  (:name @user)
                             :publish 0
                             :counter 0
                             :create_time (js/Date.)
                             :modify_time (js/Date.)}
                          r/atom)
            content     (r/cursor edited-post [:content])
            title       (r/cursor edited-post [:title])]

        [:> antd/Layout
         [post-edit-basic edited-post]
         [:> antd/Layout.Content {:style {:backdrop-color "#fff"}}
          [:> antd/Col {:span 16 :offset 4 :style {:padding-top "10px"}}
           [:> antd/Form
            [:> antd/Input
             {:on-change   #(let [val (-> % .-target .-value)]
                              (reset! title val))
              :placeholder "请输入标题"}]]
           [:> antd/Row
            [editor content]
            ]]]]))))

(defn edit-post-page []
  (r/with-let [post (subscribe [:post])
               user (subscribe [:user])]
    (fn []
      (let [edited-post (-> @post
                          (update :id #(or % nil))
                          (update :title #(or % nil))
                          (update :content #(or % nil))
                          (update :category #(or % nil))
                          (update :author #(or % (:name @user)))
                          (update :publish #(or % 0))
                          (update :counter #(or % 0))
                          (update :create_time #(or % (js/Date.)))
                          r/atom)
            content     (r/cursor edited-post [:content])
            title       (r/cursor edited-post [:title])]
        (if-not @post
          [:div [:> antd/Spin {:tip "loading"}]]
          [:> antd/Layout
           [post-edit-basic edited-post]
           [:> antd/Layout.Content {:style {:backdrop-color "#fff"}}
            [:> antd/Col {:span 16 :offset 4 :style {:padding-top "10px"}}
             [:> antd/Form
              [:> antd/Input
               {:on-change    #(let [val (-> % .-target .-value)]
                                 (js/console.log val)
                                 (reset! title val))
                :placeholder  "请输入标题"
                :size         "large"
                :defaultValue @title}]]
             [:> antd/Row
              [editor content]]]]])))))


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
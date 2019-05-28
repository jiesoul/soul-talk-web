(ns soul-talk.pages.post
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.pages.common :as c]
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
                   [:> antd/Button {:type "link"
                                    :href (str "#/posts/" id)}
                    "查看"]
                   (when (zero? publish)
                     [:> antd/Divider {:type "vertical"}]
                     [:> antd/Button {:type     "link"
                                      :on-click #(dispatch [:posts/publish id])}])
                   [:> antd/Divider {:type "vertical"}]
                   [:> antd/Button {:type     "link"
                                    :on-click #(dispatch [:navigate-to (str "#/posts/" id "/edit")])}
                    "修改"]
                   [:> antd/Divider {:type "vertical"}]
                   [:> antd/Button {:type     "link"
                                    :on-click #(dispatch [:posts/delete id])}
                    "删除"]])))}])

(defn posts-list []
  (r/with-let [posts (subscribe [:admin/posts])]
    (js/console.log (map #(update % :create_time (comp str (fn [date] (ft/parse custom-formatter date)))) @posts))
    [:div
     [:> antd/Table {:columns    (clj->js (list-columns))
                     :dataSource (clj->js @posts)
                     :row-key "id"
                     :bordered true
                     :size "small"}]]))

(defn posts-page []
  (fn []
    [:div
     [c/breadcrumb-component ["Home" "Post" "list"]]
     [:> antd/Layout.Content
      {:style {:background "#fff"
               :padding    24
               :margin     0
               :min-height 280}}
      [:> antd/Button
       {:target "_blank"
        :href   "/posts/add"
        :size "small"}
       "写文章"]
      [:> antd/Divider]
      [posts-list]]]))

(defn add-post-page []
  (r/with-let
    [user (subscribe [:user])
     categories (subscribe [:categories])
     error (subscribe [:error])]
    (let [edited-post (-> {:author  (:name @user)
                           :publish 0}
                        r/atom)
          title       (r/cursor edited-post [:title])
          content     (r/cursor edited-post [:content])
          category    (r/cursor edited-post [:category])]
      [:div.container-fluid
       [:nav.navbar.navbar-expand-lg.navbar-light.bg-light
        [:a.navbar-brand
         {:href "#"} "Soul Talk"]
        [:div.container
         [:ul.navbar-nav
          [:li.nav-item.active
           [:h6.title
            (if @edited-post "修改文章" "写文章")]]]]]
       [:div.container
        [:main#main.col-md-12.ml-sm-auto.col-lg-12.px-4
         [:div
          [:div.form-group
           [:input.form-control.input-lg
            {:type        :text
             :placeholder "请输入标题"
             :value       @title
             :on-change   #(reset! title (-> % .-target .-value))}]]
          [:div.form-group
           [c/editor content]]
          (when @error
            [:div.alert.alert-danger @error])
          [:div.form-inline
           [:div.form-group
            [:select#category.mr-2.form-control.form-control-sm
             {:on-change    #(reset! category (-> % .-target .-value))
              :defaultValue @category}
             [:option "请选择一个分类"]
             (for [{:keys [id name]} @categories]
               ^{:key id}
               [:option
                {:value id}
                name])]
            [:a.btn.btn-outline-primary.btn-sm.mr-2
             {:on-click
              #(dispatch [:posts/add @edited-post])}
             "保存"]]]]]]])))

(defn edit-post-page []
  (r/with-let [user (subscribe [:user])
               original-post (subscribe [:post])
               error (subscribe [:error])
               categories  (subscribe [:categories])
               edited-post (-> @original-post
                             (update :title #(or % ""))
                             (update :content #(or % ""))
                             (update :category #(or % ""))
                             (update :author #(or % (:name @user)))
                             (update :publish #(or % 0))
                             r/atom)
               title       (r/cursor edited-post [:title])
               content     (r/cursor edited-post [:content])
               category    (r/cursor edited-post [:category])
               c-list      #(mapv
                              (fn [{:keys [id name]}] {:id id :label name})
                              @categories)]
    (fn []
      (let []
        [:div.container-fluid
         [:nav.navbar.navbar-expand-lg.navbar-light.bg-light
          [:a.navbar-brand
           {:href "#"} "Soul Talk"]
          [:div.container
           [:ul.navbar-nav
            [:li.nav-item.active
             [:h6.title
              (if @original-post "修改文章" "写文章")]]]]]
         [:div.container
          [:main#main.col-md-12.ml-sm-auto.col-lg-12.px-4
           [:div
            [:div.form-group
             [:> antd/Input
              :model title
              :on-change #(reset! title %)
              :placeholder "标题"
              :width "100%"
              :class "form-control input-lg"]]
            [:div.form-group
             [editor content title]]
            (when @error
              [:div.alert.alert-danger @error])
            [:div.form-inline
             [:div.form-group
              [:select#category.mr-2.form-control.form-control-sm
               {:on-change #(reset! category (-> % .-target .-value))
                :value     @category}
               [:option "请选择一个分类"]
               (doall
                 (for [{:keys [id name]} @categories]
                   ^{:key id}
                   [:option
                    {:value id}
                    name]))]
              [:a.btn.btn-outline-primary.btn-sm.mr-2
               {:on-click
                (if @original-post
                  #(dispatch [:posts/edit @edited-post])
                  #(dispatch [:posts/add @edited-post]))}
               "保存"]]]]]]]))))


(defn post-view-page []
  (r/with-let [post (subscribe [:post])
               user (subscribe [:user])]
    (fn []
      (if @post
        [:> antd/Layout.Content {:style {:padding "50px"}}
         [:> antd/Typography.Title {:style {:text-align "center"}}
          (:title @post)]
         [:hr]
         [:div.container
          [c/markdown-preview (:content @post)]]
         [:hr]
         (if @user
           [:div.text-center
            [:a.btn.btn-outline-primary.btn-sm.mr-2
             {:href (str "/posts/" (:id @post) "/edit")}
             "修改文章"]])]))))

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
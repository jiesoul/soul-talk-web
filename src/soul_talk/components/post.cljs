(ns soul-talk.components.post
  (:require [reagent.core :as r]
            [re-frame.core :as rf :refer [subscribe dispatch]]
            [soul-talk.layouts.basic-layout :refer [basic-layout]]
            [soul-talk.layouts.post-layout :refer [post-layout]]
            [soul-talk.layouts.blank-layout :refer [layout]]
            [soul-talk.components.common :as c]
            [soul-talk.components.md-editor :refer [editor]]
            [soul-talk.date-utils :as du]
            [soul-talk.post-validate :refer [post-errors]]
            [clojure.string :as str]
            [antd :as antd]))

(defn home-posts []
  (r/with-let [posts (subscribe [:posts])
               pagination (subscribe [:pagination])]
    (when @posts
      (fn []
        (let [edited-pagination (-> @pagination
                                  r/atom)
              page (r/cursor edited-pagination [:page])
              pre-page (r/cursor edited-pagination[:pre-page])
              total (r/cursor edited-pagination [:total])]
          [:> antd/Layout.Content
           [:> antd/Row {:gutter 16}
            (for [{:keys [id title create_time author content] :as post} @posts]
              (let [url (str "/#/posts/" id)]
                ^{:key post}
                [:> antd/Col {:span 8}
                 [:> antd/Card {:activeTabKey id
                                :title        (r/as-element
                                                [:div
                                                 [:a.text-muted
                                                  {:href   url
                                                   :target "_blank"}
                                                  title]
                                                 [:br]
                                                 [:span (str (.toDateString (js/Date. create_time)) " by " author)]])
                                :bodyStyle    {:height "300px" :overflow "hidden"}
                                :style        {:margin 5}
                                ;:bordered     false
                                :hoverable    true}
                  [c/markdown-preview content]]]))]
           [:> antd/Row
            [:> antd/Col {:span  16 :offset 4
                          :style {:text-align "center"}}
             [:> antd/Pagination {:current   @page
                                  :pageSize  @pre-page
                                  :total     @total
                                  :on-change #(do (reset! page %1)
                                                  (reset! pre-page %2)
                                                  (dispatch [:load-posts @edited-pagination]))}]]]])))))

(defn blog-posts-list [posts]
  [:div
   (for [{:keys [id title create_time author content] :as post} @posts]
     (let [url (str "/#/posts/" id)]
       ^{:key post}
       [:> antd/Row
        [:div
         [:a.text-muted
          {:href   url
           :target "_blank"}
          [:h2 title]]
         [:span (str (.toDateString (js/Date. create_time)) " by " author)]]
        [:> antd/Divider]]))])

(defn blog-posts []
  (r/with-let [posts (subscribe [:posts])
               pagination (subscribe [:pagination])]
    (when @posts
      (fn []
        (let [edited-pagination (-> @pagination
                                  r/atom)
              page (r/cursor edited-pagination [:page])
              pre-page (r/cursor edited-pagination[:pre-page])
              total (r/cursor edited-pagination [:total])]
          [:> antd/Layout.Content
           [blog-posts-list posts]
           (when @total
             [:> antd/Row {:style {:text-align "center"}}
              [:> antd/Pagination {:current   @page
                                   :pageSize  @pre-page
                                   :total     @total
                                   :on-change #(do (reset! page %1)
                                                   (reset! pre-page %2)
                                                   (dispatch [:load-posts @edited-pagination]))}]])])))))

(defn blog-archives []
  (r/with-let [posts-archives (subscribe [:posts-archives])]
    (fn []
      [:div.p-3
       [:ul.list-unstyled.mb-0
        (for [{:keys [year month] :as archive} @posts-archives]
          ^{:key archive}
          [:li [:a {:href (str "/#/blog/archives/" year "/" month)} (str year "年 " month "月 ")]])]])))

(defn blog-archives-posts []
  (r/with-let [posts (subscribe [:posts])]
    (when @posts
      (fn []
        [:> antd/Layout.Content
         [blog-posts-list posts]]))))

(defn list-columns []
  [{:title "标题" :dataIndex "title", :key "title", :align "center"}
   {:title  "创建时间" :dataIndex "create_time" :key "create_time" :align "center"
    :render (fn [_ post]
              (let [post (js->clj post :keywordize-keys true)]
                (du/to-date-time (:create_time post))))}
   {:title  "更新时间" :dataIndex "modify_time" :key "modify_time" :align "center"
    :render (fn [_ post]
              (let [post (js->clj post :keywordize-keys true)]
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
                                    :size   "small"
                                    :target "_blank"
                                    :href   (str "#/posts/" id)}
                    "查看"]
                   [:> antd/Divider {:type "vertical"}]
                   [:> antd/Button {:icon   "edit"
                                    :size   "small"
                                    :target "_blank"
                                    :href   (str "#/posts/" id "/edit")}]
                   [:> antd/Divider {:type "vertical"}]
                   [:> antd/Button {:type     "danger"
                                    :icon     "delete"
                                    :size     "small"
                                    :on-click (fn []
                                                (r/as-element
                                                  (c/show-confirm
                                                    "文章删除"
                                                    (str "你确认要删除这篇文章吗？")
                                                    #(dispatch [:posts/delete id])
                                                    #(js/console.log "cancel"))))}]
                   [:> antd/Divider {:type "vertical"}]
                   (when (zero? publish)
                     [:> antd/Button {:type     "primary"
                                      :size     "small"
                                      :on-click #(dispatch [:posts/publish id])}
                      "发布"])])))}])

(defn posts-list []
  (r/with-let [posts (subscribe [:admin/posts])]
    (fn []
      [:div
       [:> antd/Table {:columns    (clj->js (list-columns))
                       :dataSource (clj->js @posts)
                       :row-key    "id"
                       :bordered   true
                       :size       "small"}]])))

(defn posts-page []
  [basic-layout
   [:> antd/Layout.Content {:className "main"}
    [:> antd/Button
     {:target "_blank"
      :href   "#/posts/add"
      :size   "small"}
     "写文章"]
    [:> antd/Divider]
    [posts-list]]])

(defn add-post-page []
  (r/with-let [post (rf/subscribe [:post])
               user (rf/subscribe [:user])
               categories (rf/subscribe [:categories])]
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

        [post-layout
         post
         edited-post
         categories
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
               user (subscribe [:user])
               categories (subscribe [:categories])]
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
          [post-layout
           post
           edited-post
           categories
           [:> antd/Layout.Content {:style {:backdrop-color "#fff"}}
            [:> antd/Col {:span 16 :offset 4 :style {:padding-top "10px"}}
             [:> antd/Form
              [:> antd/Input
               {:on-change    #(let [val (-> % .-target .-value)]
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
      [:div
       (doall
         (for [{:keys [id title create_time author] :as post} @posts]
           ^{:key post} [:div.blog-post
                         [:h2.blog-post-title
                          [:a.text-muted
                           {:href   (str "/posts/" id)
                            :target "_blank"}
                           title]]
                         [:p.blog-post-meta (str (.toDateString (js/Date. create_time)) " by " author)]
                         [:hr]]))])))
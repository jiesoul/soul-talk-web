(ns soul-talk.components.post
  (:require [reagent.core :as r]
            [re-frame.core :as rf :refer [subscribe dispatch]]
            [soul-talk.routes :refer (navigate!)]
            [soul-talk.layouts.basic-layout :refer [basic-layout]]
            [soul-talk.layouts.post-layout :refer [post-layout]]
            [soul-talk.layouts.blank-layout :refer [layout]]
            [soul-talk.components.common :as c]
            [soul-talk.components.md-editor :refer [editor]]
            [soul-talk.date-utils :as du]
            [soul-talk.post-validate :refer [post-errors]]
            [soul-talk.date-utils :refer [to-date]]
            [clojure.string :as str]))

(defn home-posts []
  (r/with-let [posts (subscribe [:posts])
               loading? (subscribe [:loading?])]
    (fn []
      [:> js/antd.Skeleton
       {:loading @loading?
        :active true}
       [:> js/antd.Layout.Content
        [:> js/antd.Row {:gutter 10}
         (for [{:keys [id title create_time author content] :as post} @posts]
           (let [url (str "/#/posts/" id)]
             ^{:key post}
             [:> js/antd.Col {:xs 24 :sm 24 :md 8 :lg 8}
              [:> js/antd.Card {:activeTabKey id
                                :title        (r/as-element
                                                [:div
                                                 [:a.text-muted
                                                  {:href   url
                                                   :target "_blank"}
                                                  title]
                                                 [:br]
                                                 [:span (str (to-date create_time) " by " author)]])
                                :bodyStyle    {:height "220px" :overflow "hidden"}
                                :style        {:margin 5}
                                ;:bordered     false
                                :hoverable    true}
               content]]))]]])))

(defn blog-posts-list [posts]
  [:> js/antd.List
   {:itemLayout "vertical"
    :size       "small"
    :dataSource @posts
    :renderItem #(let [{:keys [id title content create_time author] :as post} (js->clj % :keywordize-keys true)]
                   (r/as-element
                     [:> js/antd.List.Item
                      [:> js/antd.List.Item.Meta
                       {:title       (r/as-element [:a
                                                    {:href   (str "#/posts/" id)
                                                     :target "_blank"}
                                                    [:h2 title]])
                        :description (str " " (to-date create_time) " by " author)}]
                      [c/markdown-preview content]]))}])

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
          [:> js/antd.Card
           {:title "文章列表"}
           [:div
            [blog-posts-list posts]
            (when (pos? @total)
              [:> js/antd.Row {:style {:text-align "center"}}
               [:> js/antd.Pagination {:current   @page
                                       :pageSize  @pre-page
                                       :total     @total
                                       :on-change #(do (reset! page %1)
                                                       (reset! pre-page %2)
                                                       (dispatch [:load-posts @edited-pagination]))}]])]])))))

(defn blog-archives-posts []
  (r/with-let [posts (subscribe [:posts])]
    (when @posts
      (fn []
        [:> js/antd.Card
         {:title "文章列表"}
         [:> js/antd.Layout.Content
          [blog-posts-list posts]]]))))

(defn blog-archives []
  (r/with-let [posts-archives (subscribe [:posts-archives])]
    (when @posts-archives
      (fn []
        [:> js/antd.Card
         {:title "文章归档"}
         [:> js/antd.List
          {:itemLayout "vertical"
           :dataSource @posts-archives
           :renderItem (fn [post]
                         (let [{:keys [year month counter :as post]} (js->clj post :keywordize-keys true)
                               title (str year "年 " month " 月 (" counter ")")]
                           (r/as-element
                             [:> js/antd.List.Item
                              [:div
                               [:a
                                {:on-click #(navigate! (str "#/blog/archives/" year "/" month))}
                                title]]])))}]]))))



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
                   [:> js/antd.Button {
                                       :size   "small"
                                       :target "_blank"
                                       :href   (str "#/posts/" id)}
                    "查看"]
                   [:> js/antd.Divider {:type "vertical"}]
                   [:> js/antd.Button {:icon   "edit"
                                       :size   "small"
                                       :target "_blank"
                                       :href   (str "#/posts/" id "/edit")}]
                   [:> js/antd.Divider {:type "vertical"}]
                   [:> js/antd.Button {:type     "danger"
                                       :icon     "delete"
                                       :size     "small"
                                       :on-click (fn []
                                                   (r/as-element
                                                     (c/show-confirm
                                                       "文章删除"
                                                       (str "你确认要删除这篇文章吗？")
                                                       #(dispatch [:posts/delete id])
                                                       #(js/console.log "cancel"))))}]
                   [:> js/antd.Divider {:type "vertical"}]
                   (when (zero? publish)
                     [:> js/antd.Button {:type     "primary"
                                         :size     "small"
                                         :on-click #(dispatch [:posts/publish id])}
                      "发布"])])))}])

(defn posts-list []
  (r/with-let [posts (subscribe [:admin/posts])]
    (fn []
      [:div
       [:> js/antd.Table {:columns    (clj->js (list-columns))
                          :dataSource (clj->js @posts)
                          :row-key    "id"
                          :bordered   true
                          :size       "small"}]])))

(defn posts-page []
  [basic-layout
   [:> js/antd.Layout.Content {:className "main"}
    [:> js/antd.Button
     {:target "_blank"
      :href   "#/posts/add"
      :size   "small"}
     "写文章"]
    [:> js/antd.Divider]
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
         [:> js/antd.Layout.Content {:style {:backdrop-color "#fff"}}
          [:> js/antd.Col {:span 16 :offset 4 :style {:padding-top "10px"}}
           [:> js/antd.Form
            [:> js/antd.Input
             {:on-change   #(let [val (-> % .-target .-value)]
                              (reset! title val))
              :placeholder "请输入标题"}]]
           [:> js/antd.Row
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
          [:div [:> js/antd.Spin {:tip "loading"}]]
          [post-layout
           post
           edited-post
           categories
           [:> js/antd.Layout.Content {:style {:backdrop-color "#fff"}}
            [:> js/antd.Col {:span 16 :offset 4 :style {:padding-top "10px"}}
             [:> js/antd.Form
              [:> js/antd.Input
               {:on-change    #(let [val (-> % .-target .-value)]
                                 (reset! title val))
                :placeholder  "请输入标题"
                :size         "large"
                :defaultValue @title}]]
             [:> js/antd.Row
              [editor content]]]]])))))


(defn post-view-page []
  (r/with-let [post (subscribe [:post])
               user (subscribe [:user])]
    (fn []
      (if @post
        [:div.post-view
         [:> js/antd.Card
          [:div
           [:> js/antd.Typography.Title {:style {:text-align "center"}}
            (:title @post)]
           [:div
            {:style {:text-align "center"}}
            (str (to-date (:create_time @post)) " by " (:author @post))]
           [:> js/antd.Divider]
           [:> js/antd.Typography.Text
            [c/markdown-preview (:content @post)]]]]]))))

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
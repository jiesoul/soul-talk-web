(ns soul-talk.article.component
  (:require [reagent.core :as r]
            [re-frame.core :as rf :refer [subscribe dispatch]]
            [soul-talk.routes :refer (navigate!)]
            [soul-talk.common.layout :refer [layout basic-layout]]
            [soul-talk.article.layout :refer [article-layout]]
            [soul-talk.common.common :as c]
            [soul-talk.common.md-editor :refer [editor]]
            [soul-talk.date-utils :as du]
            [soul-talk.post-validate :refer [post-errors]]
            [soul-talk.date-utils :refer [to-date]]
            [clojure.string :as str]))

(defn home-articles []
  (r/with-let [articles (subscribe [:public-articles])
               loading? (subscribe [:loading?])]
    (fn []
      [:> js/antd.Skeleton
       {:loading @loading?
        :active true}
       [:> js/antd.Layout.Content
        [:> js/antd.Row {:gutter 10}
         (for [{:keys [id title create_time author content] :as post} @articles]
           (let [url (str "/#/articles/" id)]
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

(defn blog-articles-list [articles]
  [:> js/antd.List
   {:itemLayout "vertical"
    :size       "small"
    :dataSource @articles
    :renderItem #(let [{:keys [id title content create_time author] :as post} (js->clj % :keywordize-keys true)]
                   (r/as-element
                     [:> js/antd.List.Item
                      [:> js/antd.List.Item.Meta
                       {:title       (r/as-element [:a
                                                    {:href   (str "#/articles/" id)
                                                     :target "_blank"}
                                                    [:h2 title]])
                        :description (str " " (to-date create_time) " by " author)}]
                      [c/markdown-preview content]]))}])

(defn blog-articles []
  (r/with-let [articles (subscribe [:public-articles])
               pagination (subscribe [:pagination])]
    (when @articles
      (fn []
        (let [edited-pagination (-> @pagination
                                  r/atom)
              page (r/cursor edited-pagination [:page])
              pre-page (r/cursor edited-pagination[:pre-page])
              total (r/cursor edited-pagination [:total])]
          [:> js/antd.Card
           {:title "文章列表"}
           [:div
            [blog-articles-list articles]
            (when (pos? @total)
              [:> js/antd.Row {:style {:text-align "center"}}
               [:> js/antd.Pagination {:current   @page
                                       :pageSize  @pre-page
                                       :total     @total
                                       :on-change #(do (reset! page %1)
                                                       (reset! pre-page %2)
                                                       (dispatch [:load-articles @edited-pagination]))}]])]])))))

(defn blog-archives-articles []
  (r/with-let [articles (subscribe [:public-articles])]
    (when @articles
      (fn []
        [:> js/antd.Card
         {:title "文章列表"}
         [:> js/antd.Layout.Content
          [blog-articles-list articles]]]))))

(defn blog-archives []
  (r/with-let [articles-archives (subscribe [:public-articles-archives])]
    (when @articles-archives
      (fn []
        [:> js/antd.Card
         {:title "文章归档"}
         [:> js/antd.List
          {:itemLayout "vertical"
           :dataSource @articles-archives
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
                                       :href   (str "#/articles/" id)}
                    "查看"]
                   [:> js/antd.Divider {:type "vertical"}]
                   [:> js/antd.Button {:icon   "edit"
                                       :size   "small"
                                       :target "_blank"
                                       :href   (str "#/articles/" id "/edit")}]
                   [:> js/antd.Divider {:type "vertical"}]
                   [:> js/antd.Button {:type     "danger"
                                       :icon     "delete"
                                       :size     "small"
                                       :on-click (fn []
                                                   (r/as-element
                                                     (c/show-confirm
                                                       "文章删除"
                                                       (str "你确认要删除这篇文章吗？")
                                                       #(dispatch [:articles/delete id])
                                                       #(js/console.log "cancel"))))}]
                   [:> js/antd.Divider {:type "vertical"}]
                   (when (zero? publish)
                     [:> js/antd.Button {:type     "primary"
                                         :size     "small"
                                         :on-click #(dispatch [:articles/publish id])}
                      "发布"])])))}])

(defn articles-list []
  (r/with-let [articles (subscribe [:articles])]
    (fn []
      [:div
       [:> js/antd.Table {:columns    (clj->js (list-columns))
                          :dataSource (clj->js @articles)
                          :row-key    "id"
                          :bordered   true
                          :size       "small"}]])))

(defn articles-page []
  [basic-layout
   [:> js/antd.Layout.Content {:className "main"}
    [:> js/antd.Button
     {:target "_blank"
      :href   "#/articles/add"
      :size   "small"}
     "写文章"]
    [:> js/antd.Divider]
    [articles-list]]])

(defn add-article-page []
  (r/with-let [article (rf/subscribe [:article])
               user (rf/subscribe [:user])
               categories (rf/subscribe [:categories])]
    (fn []
      (let [edited-article (-> @article
                          (update :id #(or % nil))
                          (update :title #(or % nil))
                          (update :content #(or % nil))
                          (update :category #(or % nil))
                          (update :author #(or % (:name @user)))
                          (update :publish #(or % 0))
                          (update :counter #(or % 0))
                          (update :create_time #(or % (js/Date.)))
                          r/atom)
            content     (r/cursor edited-article [:content])
            title       (r/cursor edited-article [:title])]

        [article-layout
         article
         edited-article
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

(defn edit-article-page []
  (r/with-let [article (subscribe [:article])
               user (subscribe [:user])
               categories (subscribe [:categories])]
    (fn []
      (let [edited-article (-> @article
                          (update :id #(or % nil))
                          (update :title #(or % nil))
                          (update :content #(or % nil))
                          (update :category #(or % nil))
                          (update :author #(or % (:name @user)))
                          (update :publish #(or % 0))
                          (update :counter #(or % 0))
                          (update :create_time #(or % (js/Date.)))
                          r/atom)
            content     (r/cursor edited-article [:content])
            title       (r/cursor edited-article [:title])]
        (if-not @article
          [:div [:> js/antd.Spin {:tip "loading"}]]
          [article-layout
           article
           edited-article
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


(defn article-view-page []
  (r/with-let [article (subscribe [:article])
               user (subscribe [:user])]
    (fn []
      (if @article
        [:div.article-view
         [:> js/antd.Card
          [:div
           [:> js/antd.Typography.Title {:style {:text-align "center"}}
            (:title @article)]
           [:div
            {:style {:text-align "center"}}
            (str (to-date (:create_time @article)) " by " (:author @article))]
           [:> js/antd.Divider]
           [:> js/antd.Typography.Text
            [c/markdown-preview (:content @article)]]]]]))))

(defn article-archives-page []
  (r/with-let [articles (subscribe [:articles])]
    (fn []
      [:div
       (doall
         (for [{:keys [id title create_time author] :as article} @articles]
           ^{:key article} [:div.blog-article
                         [:h2.blog-article-title
                          [:a.text-muted
                           {:href   (str "/articles/" id)
                            :target "_blank"}
                           title]]
                         [:p.blog-article-meta (str (.toDateString (js/Date. create_time)) " by " author)]
                         [:hr]]))])))
(ns soul-talk.layouts.post-layout
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [soul-talk.components.global-header :refer [header]]
            ))

(defn post-errors [post]
  (->
    (b/validate
      post
      :title [[v/required :message "标题不能为空\n"]]
      :category [[v/required :message "请选择一个分类\n"]]
      :content [[v/required :message "内容不能为空\n"]])
    first
    (vals)))

(defn category-select [category categories]
  [:> js/antd.Select {:value        {:key @category}
                   :labelInValue true
                   :style        {:width 120 :padding "5px"}
                   :on-change    #(let [val (:key (js->clj % :keywordize-keys true))]
                                    (reset! category val))}
   [:> js/antd.Select.Option {:value ""} "选择分类"]
   (doall
     (for [{:keys [id name]} @categories]
       ^{:key id} [:> js/antd.Select.Option {:value id} name]))])

(defn edit-menu [post edited-post categories]
  (let [category (r/cursor edited-post [:category])]
    [:div {:style {:color "#FFF"}}
     [:> js/antd.Col {:span 2 :offset 2}
      [:h3 {:style {:color "#FFF"}}
       (if @post "修改文章" "写文章")]]
     [:> js/antd.Col {:span 16}
      [category-select category categories]
      [:> js/antd.Button {:ghost   true
                       :on-click #(if-let [error (r/as-element (post-errors @edited-post))]
                                    (rf/dispatch [:set-error error])
                                    (if @post
                                      (rf/dispatch [:posts/edit @edited-post])
                                      (rf/dispatch [:posts/add @edited-post])))}
       "保存"]]]))

(defn post-layout [post edited-post categories main]
  [:> js/antd.Layout
   [header
    [edit-menu post edited-post categories]]
   [:> js/antd.Layout.Content {:className "main"}
    main]])
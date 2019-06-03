(ns soul-talk.pages.common
  (:require [re-frame.core :as rf :refer [dispatch subscribe]]
            [reagent.core :as r]
            [cljs-time.local :as local]
            [antd :as antd]
            [showdown]
            [hljs]))

(defn to-time [date]
  (str (.toDateString (js/Date. date))))

(defn loading-modal []
  (r/with-let [loading? (subscribe [:loading?])]
    (fn []
      (when @loading?
        [:> antd/Spin {:tip  "加载中。。。。"
                       :size "large"}]))))

(defn spin-loading []
  (r/with-let [loading? (subscribe [:loading?])]
    (when @loading?
      (antd/message.loading "正在加载中。。。。"))))

(defn success-modal []
  (r/with-let [success (subscribe [:success])]
    (when @success
      (antd/message.success @success)
      (dispatch [:clean-success]))))

(defn show-confirm
  [title content ok-fun cancel-fun]
  (antd/Modal.confirm
    (clj->js {:centered true
              :title    title
              :content  content
              :onOk     ok-fun
              :onCancel cancel-fun})))

(defn error-modal []
  (r/with-let [error (subscribe [:error])]
    (when @error
      (antd/message.error @error)
      (dispatch [:clean-error]))))

(defn breadcrumb-component [items]
  (fn [items]
    [:> antd/Breadcrumb {:style {:margin "10px 0"}}
     (for [item items]
       ^{:key item}
       [:> antd/Breadcrumb.Item item])]))

(defn validation-modal [title errors]
  [:> antd/Modal {:is-open (boolean @errors)}
   [:> antd/ModalHeader title]
   [:> antd/ModalBody
    [:ul
     (doall
       (for [[_ error] @errors]
         ^{:key error}
         [:li error]))]]
   [:> antd/ModalFooter
    [:button.btn.btn-sm.btn-danger
     {:on-click #(reset! errors nil)}
     "Close"]]])

;;高亮代码 循环查找结节
(defn highlight-code [node]
  (let [nodes (.querySelectorAll (r/dom-node node) "pre code")]
    (loop [i (.-length nodes)]
      (when-not (neg? i)
        (when-let [item (.item nodes i)]
          (.highlightBlock js/hljs item))
        (recur (dec i))))))

;; 处理 markdown 转换
(defn markdown-preview []
  (let [md-parser (js/showdown.Converter.)]
    (r/create-class
      {:component-did-mount
       #(highlight-code (r/dom-node %))
       :component-did-update
       #(highlight-code (r/dom-node %))
       :reagent-render
       (fn [content]
         [:div
          {:dangerouslySetInnerHTML
           {:__html (.makeHtml md-parser (str content))}}])})))
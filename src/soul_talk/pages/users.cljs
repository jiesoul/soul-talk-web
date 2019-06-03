(ns soul-talk.pages.users
  (:require [soul-talk.pages.common :as c]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [soul-talk.pages.layout :refer [admin-default]]
            [re-frame.core :refer [subscribe dispatch]]
            [antd :as antd]
            [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn where-component []
  (fn []
    [:div.p-3
     [:h4.font-italic "联系我"]
     [:ol.list-unsty
      [:li [:a {:href "https://github.com/jiesoul"
                :target "_blank"}
            [:i.fa.fa-github]
            " GitHub"]]
      [:li [:a {:href "https://weibo.com/jiesoul"
                :target "_blank"}
            [:i.fa.fa-weibo] " Weibo"]]
      [:li [:a {:href "https://twitter.com/jiesoul1982"
                :target "_blank"}
            [:i.fa.fa-twitter]
            " Twitter"]]]]))

(defn users-page []
  (fn []
    [:div
     [:h2 "User List"]
     (r/with-let
       [users (subscribe [:admin/users])]
       (if @users
         [:div.table-responsive
          [:table.table.table-striped.table-sm
           [:thead
            [:tr
             [:th "email"]
             [:th "name"]
             [:th "last_login"]
             [:th "Header"]]]
           [:tbody
            (for [{:keys [email name last_login] :as user} @users]
              ^{:key user} [:tr
                            [:td email]
                            [:td name]
                            [:td (.toDateString (js/Date. last_login))]
                            [:td "action"]])]]]))]))


(defn change-pass-errors [{:keys [pass-old pass-new] :as params}]
  (first
    (b/validate params
      :pass-old [[v/required :message "旧密码不能为空"]
                 [v/min-count 7 :message "旧密码至少8位"]]
      :pass-new [[v/required :message "新密码不能为空"]
                 [v/min-count 7 :message "新密码至少8 位"]
                 [not= pass-old :message "新密码不能和旧密码一样"]]
      :pass-confirm [[v/required :message "确认密码不能为空："]
                     [= pass-new :message "确认密码必须和新密码相同"]])))

(defn change-pass-page []
  (r/with-let [user (subscribe [:user])
                pass-data (r/atom @user)
                pass-old (r/cursor pass-data [:pass-old])
                pass-new (r/cursor pass-data [:pass-new])
                pass-confirm (r/cursor pass-data [:pass-confirm])
                error (subscribe [:error])]
    (fn []
      (js/console.log "pass-date: " @pass-data)
      (js/console.log "error: " @error)
      (if @user
        [admin-default
         [:div
          [c/breadcrumb-component ["个人信息" "修改密码"]]
          [:> antd/Layout.Content {:className "main" :align "center"}
           [:> antd/Row
            [:> antd/Col {:span 8 :offset 8}
             [:> antd/Input {:id "username"
                             :disabled true
                             :addon-before "用户名："
                             :value (:name @pass-data)}]
             [:> antd/Input.Password {:id "old-pass"
                                      :name "old-pass"
                                      :placeholder "请输入旧密码"
                                      :addon-before "旧密码："
                                      :on-change #(reset! pass-old (.-target.value %))}]
             [:> antd/Input.Password {:id "pass-new"
                                      :name "pass-new"
                                      :placeholder "请输入新密码"
                                      :addon-before "新密码："
                                      :on-change #(reset! pass-new (.-target.value %))}]
             [:> antd/Input.Password {:id "pass-confirm"
                                      :name "pass-confirm"
                                      :placeholder "重复新密码"
                                      :addon-before "新密码："
                                      :on-change #(reset! pass-confirm (.-target.value %))}]
             [:div
              [:> antd/Button {:type     "primary"
                               :on-click #(if-let [error (r/as-element (change-pass-errors @pass-data))]
                                            (dispatch [:set-error error])
                                            (dispatch [:change-pass @pass-data]))}
               "保存"]]]]]]]))))


(defn user-profile-page []
  (r/with-let [user (subscribe [:user])
               edited-user (r/atom @user)
               name (r/cursor edited-user [:name])
               error (subscribe [:error])]
    (fn []
      (if @user
        [admin-default
         [:div
          [c/breadcrumb-component ["User" "profile"]]
          [:> antd/Layout.Content {:className "main"}
           [:> antd/Row
            [:> antd/Col {:span 8 :offset 8}
             [:> antd/Input
              {:id           "email"
               :type         :text
               :addon-before "邮箱："
               :disabled     true
               :value        (:email @edited-user)
               :read-only    true}
              ]
             [:> antd/Input
              {:id        "name"
               :type      :text
               :addon-before "名字："
               :value     @name
               :on-change #(reset! name (-> % .-target .-value))}]
             (when @error
               [:div.alert.alert-message @error])
             [:div {:style {:text-align "center"}}
              [:> antd/Button
               {:type     :submit
                :on-click #(dispatch [:save-user-profile @edited-user])}
               "保存"]]]]]]]))))


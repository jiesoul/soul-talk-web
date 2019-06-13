(ns soul-talk.pages.users
  (:require [reagent.core :as r]
            [taoensso.timbre :as log]
            [re-frame.core :refer [subscribe dispatch]]
            [soul-talk.layouts.basic-layout :refer [basic-layout]]
            [soul-talk.user-validate :refer [change-pass-errors]]
            [soul-talk.components.common :as c]))

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




(defn change-pass-page []
  (r/with-let [user (subscribe [:user])
                pass-data (r/atom @user)
                pass-old (r/cursor pass-data [:pass-old])
                pass-new (r/cursor pass-data [:pass-new])
                pass-confirm (r/cursor pass-data [:pass-confirm])
                error (subscribe [:error])]
    (fn []
      (if @user
        [basic-layout
         [:div
          [c/breadcrumb-component ["个人信息" "修改密码"]]
          [:> js/Layout.Content {:className "main" :align "center"}
           [:> js/Row
            [:> js/Col {:span 8 :offset 8}
             [:> js/Input {:id "username"
                             :disabled true
                             :addon-before "用户名："
                             :value (:name @pass-data)}]
             [:> js/Input.Password {:id "old-pass"
                                      :name "old-pass"
                                      :placeholder "请输入旧密码"
                                      :addon-before "旧密码："
                                      :on-change #(reset! pass-old (.-target.value %))}]
             [:> js/Input.Password {:id "pass-new"
                                      :name "pass-new"
                                      :placeholder "请输入新密码"
                                      :addon-before "新密码："
                                      :on-change #(reset! pass-new (.-target.value %))}]
             [:> js/Input.Password {:id "pass-confirm"
                                      :name "pass-confirm"
                                      :placeholder "重复新密码"
                                      :addon-before "新密码："
                                      :on-change #(reset! pass-confirm (.-target.value %))}]
             [:div
              [:> js/Button {:type     "primary"
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
        [basic-layout
         [:div
          [c/breadcrumb-component ["User" "profile"]]
          [:> js/Layout.Content {:className "main"}
           [:> js/Row
            [:> js/Col {:span 8 :offset 8}
             [:> js/Input
              {:id           "email"
               :type         :text
               :addon-before "邮箱："
               :disabled     true
               :value        (:email @edited-user)
               :read-only    true}
              ]
             [:> js/Input
              {:id        "name"
               :type      :text
               :addon-before "名字："
               :value     @name
               :on-change #(reset! name (-> % .-target .-value))}]
             (when @error
               [:div.alert.alert-message @error])
             [:div {:style {:text-align "center"}}
              [:> js/Button
               {:type     :submit
                :on-click #(dispatch [:save-user-profile @edited-user])}
               "保存"]]]]]]]))))


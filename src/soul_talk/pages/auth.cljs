(ns soul-talk.pages.auth
  (:require [reagent.core :as r]
            [antd :as antd]
            [re-frame.core :refer [dispatch subscribe]]
            [soul-talk.layouts.user-layout :refer [user-layout]]
            [bouncer.core :as b]
            [bouncer.validators :as v])
  (:import goog.History))


(defn reg-errors [{:keys [password] :as params}]
  (first
    (b/validate
      params
      :email [[v/required :message "email 不能为空"]
              [v/email :message "email 不合法"]]
      :password [[v/required :message "密码不能为空"]
                 [v/min-count 7 :message "密码最少8位"]]
      :pass-confirm [[= password :message "两次密码必须一样"]])))

(defn login-errors [params]
  (first
    (b/validate
      params
      :email [[v/required :message "email 不能为空"]
              [v/email :message "email 不合法"]]
      :password [[v/required :message "密码不能为空"]
                 [v/min-count 7 :message "密码最少8位"]])))

(defn login-page []
  (r/with-let  [login-data (r/atom {:email ""
                             :password ""})
         error     (subscribe [:error])
         email (r/cursor login-data [:email])
         password (r/cursor login-data [:password])]
    (fn []
      [user-layout
       [:> antd/Layout {:style {:min-height "100vh"}
                        :align "middle"}
        [:> antd/Row {:align   "middle"
                      :justify "space-around"}
         [:> antd/Col {:span 4 :offset 10}
          [:> antd/Form {:align     "middle"
                         :className "login-form"}
           [:h1.h3.mb-3.font-weight-normal.text-center "Login"]
           [:> antd/Input
            {:id          "email"
             :prefix      (r/as-element [:> antd/Icon {:type "user"}])
             :type        :text
             :name        "email"
             :placeholder "请输入Email"
             :required    true
             :auto-focus  true
             :on-change   #(reset! email (-> % .-target .-value))}]
           [:> antd/Input.Password
            {:id          "password"
             :type        :password
             :prefix      (r/as-element [:> antd/Icon {:type "lock"}])
             :name        "password"
             :placeholder "请输入密码"
             :required    true
             :on-change   #(reset! password (-> % .-target .-value))}]
           (when @error
             [:div @error])
           [:> antd/Button
            {:type     "submit"
             :on-click #(dispatch [:login @login-data])}
            "Login"]]]]]])))

(defn register-page []
  (r/with-let
    [reg-data (r/atom nil)
     error (subscribe [:error])
     email (r/cursor reg-data [:email])
     password (r/cursor reg-data [:password])
     pass-confirm (r/cursor reg-data [:pass-confirm])]
    (fn []
      [:div.text-center.container
       [:div.form-signin
        [:h1.h3.mb-3.font-weight-normal.text-center "Register"]
        [:label.sr-only
         {:for "email"}]
        [:input#email.form-control
         {:type        :text
          :placeholder "请输入Email"
          :required    true
          :auto-focus  true
          :on-change   #(reset! email (-> % .-target .-value))}]
        [:label.sr-only
         {:for "password"}]
        [:input#password.form-control
         {:type        :password
          :placeholder "请输入密码"
          :required    true
          :on-change   #(reset! password (-> % .-target .-value))}]
        [:label.sr-only
         {:for "pass-confirm"}]
        [:input#pass-confirm.form-control
         {:type        :password
          :placeholder "请再次输入密码"
          :required    true
          :on-change   #(reset! pass-confirm (-> % .-target .-value))}]
        (when @error
          [:div.alert.alert-danger @error])
        [:button.btn.btn-lg.btn-primary.btn-block
         {:type     "submit"
          :on-click #(dispatch [:register @reg-data])}
         "Register"]]])))
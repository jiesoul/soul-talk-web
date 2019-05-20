(ns soul-talk.pages.landing
  (:require [antizer.reagent :as ant]
            [reagent.core :as r]
            [antd :as antd]))

(defn header-component []
      [:> js/antd.Layout
       [:> js/antd.Input {:value       "soul"
                       :id          "test-id"
                       :placeholder "你的测试"}]])

(defn base-layout []
  (fn []
    [ant/locale-provider {:locale (ant/locales "zh_CN")}
     [ant/layout
      [ant/affix
       [ant/layout-header {:class "banner light"}
        (r/as-element
          [ant/row
           [ant/col {:span 12} [:h2.banner-header "Soul Talk"]]
           [ant/col {:span 1 :offset 11}
            [:a {:href "#/blog"}
             "Blog"]]])
        ]]
      [ant/layout-content
       [ant/collapse
        [:div
         [:h3 "1"]]
        [:div
         [:h3 "2"]]]]]]))